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
import com.forgerock.openbanking.common.model.openbanking.forgerock.AccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_0.funds.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.common.model.rcs.consentdetails.FundsConfirmationConsentDetails;
import com.forgerock.openbanking.common.services.store.funds.FundsConfirmationService;
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

@Service
@Slf4j
public class RCSFundsConfirmationDetailsApi implements RCSDetailsApi {

    private final FundsConfirmationService fundsConfirmationService;
    private final RCSErrorService rcsErrorService;
    private final AccountService accountService;
    private final TppStoreService tppStoreService;

    public RCSFundsConfirmationDetailsApi(FundsConfirmationService fundsConfirmationService, RCSErrorService rcsErrorService, AccountService accountService, TppStoreService tppStoreService) {
        this.fundsConfirmationService = fundsConfirmationService;
        this.rcsErrorService = rcsErrorService;
        this.accountService = accountService;
        this.tppStoreService = tppStoreService;
    }

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<AccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The payment id '{}'", consentId);

        log.debug("Populate the model with the payment and consent data");

        FRFundsConfirmationConsent1 consent = fundsConfirmationService.getConsent(consentId);

        // Verify that the 'DebtorAccount' matches one of the accounts of the user and define as the selected account.
        Optional<AccountWithBalance> matchingUserAccount = accountService.findAccountByIdentification(consent.getDebtorAccount().getIdentification(), accounts);
        if (!matchingUserAccount.isPresent()) {
            log.error("The PISP '{}' created the funds confirmation request '{}' but the debtor account: {} on the consent " +
                    " is not one of the user's accounts: {}.", consent.getPispId(), consentId, consent.getDebtorAccount(), accounts);
            return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_INVALID_FUNDS_CONFIRMATION_REQUEST,
                    consent.getPispId(), consentId, clientId);
        }

        Optional<Tpp> isTpp = tppStoreService.findById(consent.getPispId());
        if (!isTpp.isPresent()) {
            log.error("The TPP '{}' (Client ID {}) that created this consent id '{}' doesn't exist anymore.", consent.getPispId(), clientId, consentId);
            return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, consentId);
        }
        Tpp tpp = isTpp.get();

        //Verify the pisp is the same than the one that created this payment ^
        verifyTppCreatedPayment(clientId, isTpp.get().getClientId(), consentId);

        //Associate the payment to this user
        consent.setUserId(username);
        fundsConfirmationService.updateConsent(consent);

        return ResponseEntity.ok(FundsConfirmationConsentDetails.builder()
                .expirationDateTime(consent.getFundsConfirmationConsent().getData().getExpirationDateTime())
                .accounts(Collections.singletonList(matchingUserAccount.get()))
                .username(username)
                .logo(tpp.getLogo())
                .merchantName(consent.getPispName())
                .clientId(clientId)
                .build());
    }
}
