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
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalScheduledConsent2;
import com.forgerock.openbanking.common.model.rcs.consentdetails.InternationalSchedulePaymentConsentDetails;
import com.forgerock.openbanking.common.services.store.payment.InternationalScheduledPaymentService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.OBScheduledPayment1;
import uk.org.openbanking.datamodel.payment.OBExchangeRate2;
import uk.org.openbanking.datamodel.payment.OBInternationalScheduled2;
import uk.org.openbanking.datamodel.payment.OBRemittanceInformation1;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RCSInternationalScheduledPaymentDetailsApi implements RCSDetailsApi {

    private final InternationalScheduledPaymentService paymentService;
    private final RCSErrorService rcsErrorService;
    private final AccountService accountService;
    private final TppStoreService tppStoreService;

    public RCSInternationalScheduledPaymentDetailsApi(InternationalScheduledPaymentService paymentService, RCSErrorService rcsErrorService, AccountService accountService, TppStoreService tppStoreService) {
        this.paymentService = paymentService;
        this.rcsErrorService = rcsErrorService;
        this.accountService = accountService;
        this.tppStoreService = tppStoreService;
    }

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<FRAccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The payment id '{}'", consentId);

        log.debug("Populate the model with the payment and consent data");

        FRInternationalScheduledConsent2 payment = paymentService.getPayment(consentId);

        // Only show the debtor account if specified in consent
        if (payment.getInitiation().getDebtorAccount() != null) {
            Optional<FRAccountWithBalance> matchingUserAccount = accountService.findAccountByIdentification(payment.getInitiation().getDebtorAccount().getIdentification(), accounts);
            if (!matchingUserAccount.isPresent()) {
                log.error("The PISP '{}' created the payment request '{}' but the debtor account: {} on the payment consent " +
                        " is not one of the user's accounts: {}.", payment.getPispId(), consentId, payment.getInitiation().getDebtorAccount(), accounts);
                return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_DEBTOR_ACCOUNT_NOT_FOUND,
                        payment.getPispId(), consentId, accounts);
            }
            accounts = Collections.singletonList(matchingUserAccount.get());
        }

        Optional<Tpp> isTpp = tppStoreService.findById(payment.getPispId());
        if (!isTpp.isPresent()) {
            log.error("The TPP '{}' (Client ID {}) that created this consent id '{}' doesn't exist anymore.", payment.getPispId(), clientId, consentId);
            return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, consentId);
        }
        Tpp tpp = isTpp.get();

        //Verify the pisp is the same than the one that created this payment ^
        verifyTppCreatedPayment(clientId, isTpp.get().getClientId(), consentId);

        //Associate the payment to this user
        payment.setUserId(username);
        paymentService.updatePayment(payment);

        final OBInternationalScheduled2 initiation = payment.getInitiation();
        final OBScheduledPayment1 obScheduledPayment = new OBScheduledPayment1()
                .accountId(payment.getAccountId())
                .scheduledPaymentId(initiation.getInstructionIdentification())
                .scheduledPaymentDateTime(initiation.getRequestedExecutionDateTime())
                .creditorAccount(initiation.getCreditorAccount())
                .instructedAmount(initiation.getInstructedAmount())
                .reference(initiation.getRemittanceInformation().getReference());

        final OBExchangeRate2 exchangeRateInformation = payment.getCalculatedExchangeRate();

        return ResponseEntity.ok(InternationalSchedulePaymentConsentDetails.builder()
                .scheduledPayment(obScheduledPayment)
                .rate(new OBExchangeRate2()
                        .exchangeRate(exchangeRateInformation.getExchangeRate())
                        .rateType(exchangeRateInformation.getRateType())
                        .contractIdentification(exchangeRateInformation.getContractIdentification())
                        .unitCurrency(exchangeRateInformation.getUnitCurrency()))
                .accounts(accounts)
                .username(username)
                .logo(tpp.getLogo())
                .merchantName(payment.getPispName())
                .clientId(clientId)
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .paymentReference(Optional.ofNullable(
                        initiation.getRemittanceInformation())
                        .map(OBRemittanceInformation1::getReference)
                        .orElse(""))
                .build());
    }
}
