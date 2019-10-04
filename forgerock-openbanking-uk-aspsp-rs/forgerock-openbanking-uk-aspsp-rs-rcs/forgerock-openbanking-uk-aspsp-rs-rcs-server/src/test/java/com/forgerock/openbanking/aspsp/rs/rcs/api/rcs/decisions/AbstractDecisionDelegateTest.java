/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
