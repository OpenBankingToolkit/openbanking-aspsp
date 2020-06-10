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
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalStandingOrderConsent4;
import com.forgerock.openbanking.common.model.rcs.consentdetails.InternationalStandingOrderPaymentConsentDetails;
import com.forgerock.openbanking.common.services.store.payment.InternationalStandingOrderService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.OBStandingOrder5;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4DataInitiation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAccountConverter.toOBCashAccount5;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount;

@Service
@Slf4j
public class RCSInternationalStandingOrderPaymentDetailsApi implements RCSDetailsApi {

    private final InternationalStandingOrderService paymentService;
    private final RCSErrorService rcsErrorService;
    private final AccountService accountService;
    private final TppStoreService tppStoreService;

    public RCSInternationalStandingOrderPaymentDetailsApi(InternationalStandingOrderService paymentService, RCSErrorService rcsErrorService, AccountService accountService, TppStoreService tppStoreService) {
        this.paymentService = paymentService;
        this.rcsErrorService = rcsErrorService;
        this.tppStoreService = tppStoreService;
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<FRAccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The payment id '{}'", consentId);

        log.debug("Populate the model with the payment and consent data");

        FRInternationalStandingOrderConsent4 payment = paymentService.getPayment(consentId);

        Optional<Tpp> isTpp = tppStoreService.findById(payment.getPispId());
        if (!isTpp.isPresent()) {
            log.error("The TPP '{}' (Client ID {}) that created this consent id '{}' doesn't exist anymore.", payment.getPispId(), clientId, consentId);
            return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, consentId);
        }
        Tpp tpp = isTpp.get();

        // Only show the debtor account if specified in consent
        if (payment.getInitiation().getDebtorAccount() != null) {
            Optional<FRAccountWithBalance> matchingUserAccount = accountService.findAccountByIdentification(payment.getInitiation().getDebtorAccount().getIdentification(), accounts);
            if (!matchingUserAccount.isPresent()) {
                log.error("The PISP '{}' created the payment request '{}' but the debtor account: {} on the payment consent " +
                        " is not one of the user's accounts: {}.", payment.getPispId(), consentId, payment.getInitiation().getDebtorAccount(), accounts);
                return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_DEBTOR_ACCOUNT_NOT_FOUND,
                        payment.getPispId(), consentId, accounts);
            }
            accounts = Collections.singletonList(matchingUserAccount.get());
        }


        //Verify the pisp is the same than the one that created this payment ^
        verifyTppCreatedPayment(clientId, isTpp.get().getClientId(), consentId);

        //Associate the payment to this user
        payment.setUserId(username);
        paymentService.updatePayment(payment);

        final OBWriteInternationalStandingOrder4DataInitiation initiation = payment.getInitiation();
        OBStandingOrder5 standingOrder = new OBStandingOrder5()
                .accountId(payment.getAccountId())
                .standingOrderId(payment.getId())
                .finalPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .nextPaymentDateTime(initiation.getFirstPaymentDateTime())
                .nextPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .frequency(initiation.getFrequency())
                .creditorAccount(toOBCashAccount5(initiation.getCreditorAccount()))
                .reference(initiation.getReference());

        return ResponseEntity.ok(InternationalStandingOrderPaymentConsentDetails.builder()
                .standingOrder(standingOrder)
                .accounts(accounts)
                .logo(tpp.getLogo())
                .username(username)
                .merchantName(payment.getPispName())
                .clientId(clientId)
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .paymentReference(Optional.ofNullable(
                        payment.getInitiation().getReference())
                        .orElse(""))
                .build());
    }

}
