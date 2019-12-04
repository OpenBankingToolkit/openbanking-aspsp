/**
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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.fundconfirmations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.openbanking.v3_0.funds.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.common.model.rcs.consentdecision.InternationalPaymentConsentDecision;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.funds.FundsConfirmationService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Slf4j
class FundsConfirmationConsentDecisionDelegate implements ConsentDecisionDelegate {

    private FundsConfirmationService fundsConfirmationService;
    private AccountStoreService accountStoreService;
    private ObjectMapper objectMapper;
    private FRFundsConfirmationConsent1 consent;

    FundsConfirmationConsentDecisionDelegate(FundsConfirmationService fundsConfirmationService, AccountStoreService accountStoreService, ObjectMapper objectMapper, FRFundsConfirmationConsent1 consent) {
        this.fundsConfirmationService = fundsConfirmationService;
        this.accountStoreService = accountStoreService;
        this.objectMapper = objectMapper;
        this.consent = consent;
    }

    @Override
    public String getTppIdBehindConsent() { return consent.getPispId(); }

    @Override
    public String getUserIDBehindConsent() {
        return consent.getUserId();
    }

    @Override
    public void consentDecision(String consentDecisionSerialised, boolean decision) throws IOException, OBErrorException {
        InternationalPaymentConsentDecision paymentConsentDecision = objectMapper.readValue(consentDecisionSerialised, InternationalPaymentConsentDecision.class);
        applyUpdate(getUserIDBehindConsent(), paymentConsentDecision.getAccountId(), decision, consent);
    }

    @Override
    public void autoaccept(List<FRAccount2> accounts, String username) throws OBErrorException {
        applyUpdate(username, accounts.get(0).getId(), true, consent);
    }

    private void applyUpdate(String userId, String accountId, boolean decision, FRFundsConfirmationConsent1 consent) throws OBErrorException {
        if (decision) {
            if (StringUtils.isEmpty(accountId)) {
                log.error("No account was selected by user {} for authorising consent: {}", userId, consent);
                throw new IllegalArgumentException("Missing account id");
            }
            final List<FRAccount2> accounts = accountStoreService.get(userId);
            boolean isAny = accounts.stream()
                    .anyMatch(account -> account.getId().equals(accountId));
            if (!isAny) {
                log.error("The account selected [{}] is not owned by this user {}. List accounts {}", accountId, userId, accounts);
                throw new OBErrorException(OBRIErrorType.RCS_CONSENT_DECISION_INVALID_ACCOUNT, userId, accountId, accounts);
            }

            log.debug("The current payment '{}' has been authorised", consent.getId());
            consent.setStatus(ConsentStatusCode.AUTHORISED);
            consent.setAccountId(accountId);
        } else {
            log.debug("The current payment '{}' has been denied", consent.getId());
            consent.setStatus(ConsentStatusCode.REJECTED);
        }
        fundsConfirmationService.updateConsent(consent);
    }

}
