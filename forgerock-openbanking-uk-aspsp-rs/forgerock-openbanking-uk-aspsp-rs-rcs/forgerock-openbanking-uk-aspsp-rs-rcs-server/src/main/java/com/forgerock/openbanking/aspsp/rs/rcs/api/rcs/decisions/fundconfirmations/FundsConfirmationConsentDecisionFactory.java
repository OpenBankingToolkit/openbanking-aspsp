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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.fundconfirmations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.common.model.openbanking.persistence.funds.v3_0.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.funds.FundsConfirmationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class FundsConfirmationConsentDecisionFactory {

    private FundsConfirmationService fundsConfirmationService;
    private ObjectMapper objectMapper;
    private AccountStoreService accountStoreService;

    public FundsConfirmationConsentDecisionFactory(FundsConfirmationService fundsConfirmationService, ObjectMapper objectMapper, AccountStoreService accountStoreService) {
        this.fundsConfirmationService = fundsConfirmationService;
        this.objectMapper = objectMapper;
        this.accountStoreService = accountStoreService;
    }

    public ConsentDecisionDelegate create(final String intentId) {
        FRFundsConfirmationConsent1 consent = fundsConfirmationService.getConsent(intentId);
        return new FundsConfirmationConsentDecisionDelegate(fundsConfirmationService, accountStoreService, objectMapper, consent);
    }

}
