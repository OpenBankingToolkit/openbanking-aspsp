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
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRFileConsent5;
import com.forgerock.openbanking.common.model.rcs.consentdetails.FilePaymentConsentDetails;
import com.forgerock.openbanking.common.services.store.payment.FilePaymentService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.payment.OBWriteDomestic2DataInitiationRemittanceInformation;
import uk.org.openbanking.datamodel.payment.OBWriteFile2DataInitiation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RCSFilePaymentDetailsApi implements RCSDetailsApi {

    private final FilePaymentService paymentService;
    private final TppStoreService tppStoreService;
    private final AccountService accountService;
    private final RCSErrorService rcsErrorService;

    public RCSFilePaymentDetailsApi(FilePaymentService paymentService, TppStoreService tppStoreService, AccountService accountService, RCSErrorService rcsErrorService) {
        this.paymentService = paymentService;
        this.tppStoreService = tppStoreService;
        this.accountService = accountService;
        this.rcsErrorService = rcsErrorService;
    }

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<FRAccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The payment id '{}'", consentId);
        log.debug("Populate the model with the payment and consent data");

        FRFileConsent5 consent = paymentService.getPayment(consentId);

        checkValidPisp(consent, clientId);

        // A null debtor account will let PSU select account when authorising
        if (consent.getInitiation().getDebtorAccount()!=null) {
            Optional<FRAccountWithBalance> matchingUserAccount = accountService.findAccountByIdentification(consent.getInitiation().getDebtorAccount().getIdentification(), accounts);
            if (!matchingUserAccount.isPresent()) {
                log.error("The PISP '{}' created the payment consent '{}' but the debtor account: {} on the consent " +
                        " is not one of the user's accounts: {}.", consent.getPispId(), consent.getId(), consent.getInitiation().getDebtorAccount(), accounts);
                return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_DEBTOR_ACCOUNT_NOT_FOUND,
                        consent.getPispId(), consent.getId(), accounts);
            } else {
                accounts = Collections.singletonList(matchingUserAccount.get());
            }
        }

        associatePaymentToUser(consent, username);

        Optional<Tpp> isTpp = tppStoreService.findById(consent.getPispId());
        if (!isTpp.isPresent()) {
            log.error("The TPP '{}' (Client ID {}) that created this consent id '{}' doesn't exist anymore.", consent.getPispId(), clientId, consentId);
            return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, consentId);
        }
        Tpp tpp = isTpp.get();

        final OBWriteFile2DataInitiation initiation = consent.getWriteFileConsent().getData().getInitiation();
        return ResponseEntity.ok(FilePaymentConsentDetails.builder()
                .accounts(accounts)
                .username(username)
                .merchantName(consent.getPispName())
                .clientId(clientId)
                .fileReference(initiation.getFileReference())
                .numberOfTransactions(initiation.getNumberOfTransactions())
                .totalAmount(
                        new OBActiveOrHistoricCurrencyAndAmount()
                        .amount( initiation.getControlSum().toPlainString())
                                .currency(consent.getPayments().get(0).getInstructedAmount().getCurrency())
                )
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .paymentReference(Optional.ofNullable(
                        initiation.getRemittanceInformation())
                        .map(OBWriteDomestic2DataInitiationRemittanceInformation::getReference)
                        .orElse(""))
                .build());
    }

    private void associatePaymentToUser(FRFileConsent5 consent, String username) {
        consent.setUserId(username);
        paymentService.updatePayment(consent);
    }

    private void checkValidPisp(FRPaymentConsent consent, String clientId) throws OBErrorException {
        Optional<Tpp> isTpp = tppStoreService.findById(consent.getPispId());
        if (!isTpp.isPresent()) {
            log.error("The PISP '{}' that created this payment id '{}' doesn't exist anymore.", consent.getPispId(), consent.getId());
            throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP, consent.getPispId(), consent.getId(), clientId);
        }

        //Verify the pisp is the same than the one that created this payment ^
        verifyTppCreatedPayment(clientId, isTpp.get().getClientId(), consent.getId());
    }

}
