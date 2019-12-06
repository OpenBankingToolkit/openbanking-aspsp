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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions;


import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * Holds common business logic for the payment consent decision updates so that they are not duplicated across many delegates
 */
@Slf4j
@Component
public class PaymentConsentDecisionUpdater {

    private final AccountStoreService accountStoreService;

    @Autowired
    public PaymentConsentDecisionUpdater(AccountStoreService accountStoreService) {
        this.accountStoreService = accountStoreService;
    }

    public <T extends FRPaymentConsent> void applyUpdate(String userId, String accountId, boolean decision, Consumer<T> paymentConsentUpdater, T paymentConsent) throws OBErrorException {
        if (decision) {
            if (StringUtils.isEmpty(accountId)) {
                log.error("No account was selected for payment [{}] by user {} for consent: {}", userId, paymentConsent);
                throw new IllegalArgumentException("Missing account id");
            }
            List<FRAccount2> accounts = accountStoreService.get(userId);
            boolean isAny = accounts.stream()
                    .anyMatch(account -> account.getId().equals(accountId));
            if (!isAny) {
                log.error("The account selected [{}] is not owned by this user {}. List accounts {}", accountId, userId, accounts);
                throw new OBErrorException(OBRIErrorType.RCS_CONSENT_DECISION_INVALID_ACCOUNT, userId, accountId, accounts);
            }

            paymentConsent.setStatus(ConsentStatusCode.AUTHORISED);
            paymentConsent.setAccountId(accountId);
        } else {
            log.debug("The current payment consent: '{}' has been rejected by the PSU: {}", paymentConsent.getId(), userId);
            paymentConsent.setStatus(ConsentStatusCode.REJECTED);
        }
        paymentConsentUpdater.accept(paymentConsent);
    }
}
