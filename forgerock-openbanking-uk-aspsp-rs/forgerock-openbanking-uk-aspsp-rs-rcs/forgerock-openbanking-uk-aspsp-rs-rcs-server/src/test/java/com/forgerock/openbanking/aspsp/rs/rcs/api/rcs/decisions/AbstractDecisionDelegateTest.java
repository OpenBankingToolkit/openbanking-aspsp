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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Helper class for testing decision delegates
 */
public abstract class AbstractDecisionDelegateTest {
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected static final String CONSENT_ID = "123";
    protected static final String PISP_ID = "456";
    protected static final String ACCOUNT_ID = "12345678";
    protected static final String USER_ID = "test1";
    protected static final FRAccount2 USER_ACCOUNT = FRAccount2.builder()
            .id(ACCOUNT_ID)
            .userID(USER_ID)
            .build();
    protected static final List<FRAccount2> USER_ACCOUNT_LIST = Collections.singletonList(USER_ACCOUNT);
    protected static final String WRONG_ACCOUNT_ID = "999999999999";

    protected static final String EXPECTED_MISSING_ACC_ID_MSG = "Missing account id";
    protected static final String EXPECTED_WRONG_ACC_ID_MSG = "The PSU "+USER_ID+" is trying to share an account '"+WRONG_ACCOUNT_ID+"' he doesn't own. List of his accounts '[FRAccount2(id="+ACCOUNT_ID+", userID="+USER_ID+", account=null, latestStatementId=null, created=null, updated=null)]'";

    protected static AccountStoreService getAccountStoreService() {
        AccountStoreService accountStoreService = mock(AccountStoreService.class);
        given(accountStoreService.get(eq(USER_ID))).willReturn(Collections.singletonList(USER_ACCOUNT));
        return accountStoreService;
    }

    protected static PaymentConsentDecisionUpdater getPaymentConsentDecisionUpdater() {
        return new PaymentConsentDecisionUpdater(getAccountStoreService());
    }

    protected static String toSerializedDecision(String accountId, boolean decision) {
        return String.format("{\"consentJwt\": \"6876986986986986jjkhgkjguoto8\", \"decision\": \"%s\", \"accountId\": \"%s\"}", String.valueOf(decision).toLowerCase(), Objects.requireNonNull(accountId));
    }
}
