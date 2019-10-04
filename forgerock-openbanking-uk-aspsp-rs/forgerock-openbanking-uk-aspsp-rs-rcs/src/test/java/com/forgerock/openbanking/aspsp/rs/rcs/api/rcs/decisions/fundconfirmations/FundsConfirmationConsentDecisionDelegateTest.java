/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.fundconfirmations;

import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.AbstractDecisionDelegateTest;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.commons.model.openbanking.v3_0.funds.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.commons.services.store.account.AccountStoreService;
import com.forgerock.openbanking.commons.services.store.funds.FundsConfirmationService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FundsConfirmationConsentDecisionDelegateTest extends AbstractDecisionDelegateTest {
    private FundsConfirmationConsentDecisionDelegate decisionDelegate;
    private FundsConfirmationService fundsConfirmationService;
    private AccountStoreService accountStoreService;
    private FRFundsConfirmationConsent1 consent;

    @Before
    public void setup() {
        fundsConfirmationService = mock(FundsConfirmationService.class);
        accountStoreService = mock(AccountStoreService.class);
        consent = FRFundsConfirmationConsent1.builder()
                .id(CONSENT_ID)
                .pispId(PISP_ID)
                .userId(USER_ID)
                .build();
        given(accountStoreService.get(eq(USER_ID))).willReturn(Collections.singletonList(USER_ACCOUNT));
        decisionDelegate = new FundsConfirmationConsentDecisionDelegate(fundsConfirmationService, accountStoreService, OBJECT_MAPPER, consent);
    }

    @Test
    public void getTppIdBehindConsent() {
        assertThat(decisionDelegate.getTppIdBehindConsent()).isEqualTo(consent.getPispId());
    }

    @Test
    public void getUserIDBehindConsent() {
        assertThat(decisionDelegate.getUserIDBehindConsent()).isEqualTo(consent.getUserId());
    }

    @Test
    public void consentDecision_authorised_setAccountIdAndStatus_updateConsent() throws Exception{
        // When
        decisionDelegate.consentDecision(toSerializedDecision(ACCOUNT_ID, true), true);

        // Then
        assertThat(consent.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(consent.getStatus()).isEqualTo(ConsentStatusCode.AUTHORISED);
        verify(fundsConfirmationService, times(1)).updateConsent(any());
    }

    @Test
    public void consentDecision_rejected_setStatus_updateConsent() throws Exception{
        // When
        decisionDelegate.consentDecision(toSerializedDecision(ACCOUNT_ID, false), false);

        // Then
        assertThat(consent.getAccountId()).isNull();
        assertThat(consent.getStatus()).isEqualTo(ConsentStatusCode.REJECTED);
        verify(fundsConfirmationService, times(1)).updateConsent(any());
    }

    @Test
    public void consentDecision_authorised_missingAccountId_exception() throws Exception {
        // Given
        String serializedDecisionMissingAccountId = toSerializedDecision("", true);

        // When
        assertThatThrownBy(() ->
            decisionDelegate.consentDecision(serializedDecisionMissingAccountId, true)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(EXPECTED_MISSING_ACC_ID_MSG);

        // Then
        assertThat(consent.getAccountId()).isNull();
        assertThat(consent.getStatus()).isNull();
        verifyZeroInteractions(fundsConfirmationService);
    }

    @Test
    public void consentDecision_authorised_accountNotFound_obException() throws Exception {
        // Given
        String serializedDecisionWrongAccountId = toSerializedDecision(WRONG_ACCOUNT_ID, true);

        // When
        assertThatThrownBy(() ->
                decisionDelegate.consentDecision(serializedDecisionWrongAccountId, true)
        )
                .isInstanceOf(OBErrorException.class)
                .hasMessage(EXPECTED_WRONG_ACC_ID_MSG);

        // Then
        assertThat(consent.getAccountId()).isNull();
        assertThat(consent.getStatus()).isNull();
        verifyZeroInteractions(fundsConfirmationService);
    }


    @Test
    public void autoaccept_setAccountIdAndAuthorise_updateConsent() throws Exception {
        // Given
        given(accountStoreService.get(eq(USER_ID))).willReturn(Collections.singletonList(USER_ACCOUNT));

        // When
        decisionDelegate.autoaccept(USER_ACCOUNT_LIST, USER_ID);

        // Then
        assertThat(consent.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(consent.getStatus()).isEqualTo(ConsentStatusCode.AUTHORISED);
        verify(fundsConfirmationService, times(1)).updateConsent(any());
    }

}