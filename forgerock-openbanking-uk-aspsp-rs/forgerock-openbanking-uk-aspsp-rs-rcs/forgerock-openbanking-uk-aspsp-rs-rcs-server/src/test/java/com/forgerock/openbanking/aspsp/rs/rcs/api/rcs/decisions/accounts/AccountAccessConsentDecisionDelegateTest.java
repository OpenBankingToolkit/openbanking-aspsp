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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.accounts;

import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.AbstractDecisionDelegateTest;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRReadConsentResponse;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRReadConsentResponseData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalRequestStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccountAccessConsent;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.store.accountrequest.AccountRequestStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class AccountAccessConsentDecisionDelegateTest extends AbstractDecisionDelegateTest {
    private AccountRequestStoreService accountRequestStoreService;
    private FRAccountAccessConsent consent;
    private AccountAccessConsentDecisionDelegate decisionDelegate;

    @Before
    public void setup() {
        accountRequestStoreService = mock(AccountRequestStoreService.class);
        consent = new FRAccountAccessConsent();
        consent.setId(CONSENT_ID);
        consent.setAispId(PISP_ID);
        consent.setUserId(USER_ID);
        consent.setObVersion(OBVersion.v3_1);
        consent.setAccountAccessConsent(FRReadConsentResponse.builder().data(FRReadConsentResponseData.builder()
                .statusUpdateDateTime(DateTime.now()).build()).build());

        decisionDelegate = new AccountAccessConsentDecisionDelegate(
                getAccountStoreService(),
                OBJECT_MAPPER,
                accountRequestStoreService,
                consent);
    }

    @Test
    public void getTppIdBehindConsent() {
        assertThat(decisionDelegate.getTppIdBehindConsent()).isEqualTo(consent.getAispId());
    }

    @Test
    public void getUserIDBehindConsent() {
        assertThat(decisionDelegate.getUserIDBehindConsent()).isEqualTo(consent.getUserId());
    }

    @Test
    public void consentDecision_userAuthorised_setAccountIdAndStatus_updateConsent() throws Exception{
        // When
        decisionDelegate.consentDecision(toSerializedDecision(ACCOUNT_ID, true), true);

        // Then
        assertThat(consent.getAccountIds().get(0)).isEqualTo(ACCOUNT_ID);
        assertThat(consent.getStatus()).isEqualTo(FRExternalRequestStatusCode.AUTHORISED);
        assertThat(consent.getStatusUpdateDateTime()).isNotNull();
        verify(accountRequestStoreService, times(1)).save(any());
    }
    @Test
    public void consentDecision_userRejected_setStatusRejected_updateConsent() throws Exception{
        // When
        decisionDelegate.consentDecision(toSerializedDecision(ACCOUNT_ID, false), false);

        // Then
        assertThat(consent.getAccountIds()).isEmpty();
        assertThat(consent.getStatus()).isEqualTo(FRExternalRequestStatusCode.REJECTED);
        assertThat(consent.getStatusUpdateDateTime()).isNotNull();
        verify(accountRequestStoreService, times(1)).save(any());
    }

    @Test
    public void consentDecision_userAuthorised_missingAccountId_exception() {
        // Given
        String serializedDecisionMissingAccountId = toSerializedDecision("", true);

        // When
        assertThatThrownBy(() ->
                decisionDelegate.consentDecision(serializedDecisionMissingAccountId, true)
        )
                .isInstanceOf(OBErrorException.class)
                .hasMessage("The PSU test1 is trying to share an account '[12345678]' he doesn't own. List of his accounts '[]'");

        // Then
        assertThat(consent.getAccountIds()).isEmpty();
        assertThat(consent.getStatus()).isNull();
        verifyZeroInteractions(accountRequestStoreService);
    }

    @Test
    public void consentDecision_userAuthorised_accountNotFound_exception() {
        // Given
        String serializedDecisionWrongAccountId = toSerializedDecision(WRONG_ACCOUNT_ID, true);

        // When
        assertThatThrownBy(() ->
                decisionDelegate.consentDecision(serializedDecisionWrongAccountId, true)
        )
                .isInstanceOf(OBErrorException.class)
                .hasMessage("The PSU test1 is trying to share an account '["+ACCOUNT_ID+"]' he doesn't own. List of his accounts '["+WRONG_ACCOUNT_ID+"]'");

        // Then
        assertThat(consent.getAccountIds()).isEmpty();
        assertThat(consent.getStatus()).isNull();
        verifyZeroInteractions(accountRequestStoreService);
    }

    @Test
    public void autoaccept_setAccountIdAndAuthorise_updateConsent() throws Exception {
        // When
        decisionDelegate.autoaccept(USER_ACCOUNT_LIST, USER_ID);

        // Then
        assertThat(consent.getAccountIds().get(0)).isEqualTo(ACCOUNT_ID);
        assertThat(consent.getStatus()).isEqualTo(FRExternalRequestStatusCode.AUTHORISED);
        assertThat(consent.getStatusUpdateDateTime()).isNotNull();
        verify(accountRequestStoreService, times(1)).save(any());
    }

    protected static String toSerializedDecision(String accountId, boolean decision) {
        return String.format("{\"consentJwt\": \"6876986986986986jjkhgkjguoto8\", \"decision\": \"%s\", \"sharedAccounts\": [\"%s\"]}", String.valueOf(decision).toLowerCase(), Objects.requireNonNull(accountId));
    }


}
