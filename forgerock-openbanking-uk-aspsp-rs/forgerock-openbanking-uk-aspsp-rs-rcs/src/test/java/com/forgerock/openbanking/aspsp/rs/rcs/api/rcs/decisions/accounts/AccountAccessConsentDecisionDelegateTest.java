/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.accounts;

import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.AbstractDecisionDelegateTest;
import com.forgerock.openbanking.commons.model.openbanking.v3_0.account.FRAccountAccessConsent1;
import com.forgerock.openbanking.commons.model.version.OBVersion;
import com.forgerock.openbanking.commons.services.store.accountrequest.AccountRequestStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;
import uk.org.openbanking.datamodel.account.OBReadConsentResponse1;
import uk.org.openbanking.datamodel.account.OBReadConsentResponse1Data;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountAccessConsentDecisionDelegateTest extends AbstractDecisionDelegateTest {
    private AccountRequestStoreService accountRequestStoreService;
    private FRAccountAccessConsent1 consent;
    private AccountAccessConsentDecisionDelegate decisionDelegate;

    @Before
    public void setup() {
        accountRequestStoreService = mock(AccountRequestStoreService.class);
        consent = new FRAccountAccessConsent1();
        consent.setId(CONSENT_ID);
        consent.setAispId(PISP_ID);
        consent.setUserId(USER_ID);
        consent.setObVersion(OBVersion.v3_1);
        consent.setAccountAccessConsent(new OBReadConsentResponse1().data(new OBReadConsentResponse1Data()));

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
        assertThat(consent.getStatus()).isEqualTo(OBExternalRequestStatus1Code.AUTHORISED);
        verify(accountRequestStoreService, times(1)).save(any());
    }
    @Test
    public void consentDecision_userRejected_setStatusRejected_updateConsent() throws Exception{
        // When
        decisionDelegate.consentDecision(toSerializedDecision(ACCOUNT_ID, false), false);

        // Then
        assertThat(consent.getAccountIds()).isEmpty();
        assertThat(consent.getStatus()).isEqualTo(OBExternalRequestStatus1Code.REJECTED);
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
        assertThat(consent.getStatus()).isEqualTo(OBExternalRequestStatus1Code.AUTHORISED);
        verify(accountRequestStoreService, times(1)).save(any());
    }

    protected static String toSerializedDecision(String accountId, boolean decision) {
        return String.format("{\"consentJwt\": \"6876986986986986jjkhgkjguoto8\", \"decision\": \"%s\", \"sharedAccounts\": [\"%s\"]}", String.valueOf(decision).toLowerCase(), Objects.requireNonNull(accountId));
    }


}