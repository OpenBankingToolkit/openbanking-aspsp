/**
 * Copyright 2019 ForgeRock AS.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;

import com.forgerock.openbanking.aspsp.rs.rcs.services.AccountService;
import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRRemittanceInformation;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
import com.forgerock.openbanking.common.model.rcs.consentdetails.DomesticPaymentConsentDetails;
import com.forgerock.openbanking.common.services.store.payment.DomesticPaymentService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;

@Service
@Slf4j
public class RCSDomesticPaymentDetailsApi implements RCSDetailsApi {

    private final RCSErrorService rcsErrorService;
    private final DomesticPaymentService paymentService;
    private final TppStoreService tppStoreService;
    private final AccountService accountService;

    public RCSDomesticPaymentDetailsApi(RCSErrorService rcsErrorService, DomesticPaymentService paymentService, TppStoreService tppStoreService, AccountService accountService) {
        this.rcsErrorService = rcsErrorService;
        this.paymentService = paymentService;
        this.tppStoreService = tppStoreService;
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<AccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The payment id '{}'", consentId);

        log.debug("Populate the model with the payment and consent data");

        FRDomesticConsent domesticConsent = paymentService.getPayment(consentId);

        // Only show the debtor account if specified in consent
        if (domesticConsent.getInitiation().getDebtorAccount() != null) {
            Optional<AccountWithBalance> matchingUserAccount = accountService.findAccountByIdentification(domesticConsent.getInitiation().getDebtorAccount().getIdentification(), accounts);
            if (matchingUserAccount.isEmpty()) {
                log.error("The PISP '{}' created the payment request '{}' but the debtor account: {} on the payment consent " +
                        " is not one of the user's accounts: {}.", domesticConsent.getPispId(), consentId, domesticConsent.getInitiation().getDebtorAccount(), accounts);
                return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_DEBTOR_ACCOUNT_NOT_FOUND,
                        domesticConsent.getPispId(), consentId, accounts);
            }
            accounts = Collections.singletonList(matchingUserAccount.get());
        }

        Optional<Tpp> isTpp = tppStoreService.findById(domesticConsent.getPispId());
        if (isTpp.isEmpty()) {
            log.error("The TPP '{}' (Client ID {}) that created this consent id '{}' doesn't exist anymore.", domesticConsent.getPispId(), clientId, consentId);
            return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, consentId);
        }
        Tpp tpp = isTpp.get();

        //Verify the pisp is the same than the one that created this payment ^
        verifyTppCreatedPayment(clientId, tpp.getClientId(), consentId);

        //Associate the payment to this user
        domesticConsent.setUserId(username);
        paymentService.updatePayment(domesticConsent);

        return ResponseEntity.ok(DomesticPaymentConsentDetails.builder()
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(domesticConsent.getInitiation().getInstructedAmount()))
                .accounts(accounts)
                .username(username)
                .logo(tpp.getLogo())
                .merchantName(domesticConsent.getPispName())
                .clientId(clientId)
                .paymentReference(Optional.ofNullable(
                        domesticConsent.getInitiation().getRemittanceInformation())
                        .map(FRRemittanceInformation::getReference)
                        .orElse(""))
                .build());
    }
}
