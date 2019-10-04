/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.domesticstandingorders;

import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.AbstractDecisionDelegateTest;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRDomesticStandingOrderConsent3;
import com.forgerock.openbanking.common.services.store.payment.DomesticStandingOrderService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DomesticStandingOrdersConsentDecisionDelegateTest extends AbstractDecisionDelegateTest {
    private DomesticStandingOrderService paymentService;
    private FRDomesticStandingOrderConsent3 consent;
    private DomesticStandingOrdersConsentDecisionDelegate decisionDelegate;

    @Before
    public void setup() {
        paymentService = mock(DomesticStandingOrderService.class);
        consent = FRDomesticStandingOrderConsent3.builder()
                .id(CONSENT_ID)
                .pispId(PISP_ID)
                .userId(USER_ID)
                .build();
        decisionDelegate = new DomesticStandingOrdersConsentDecisionDelegate(
                getPaymentConsentDecisionUpdater(),
                paymentService,
                OBJECT_MAPPER,
                consent);
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
    public void consentDecision_userAuthorised_setAccountIdAndStatus_updateConsent() throws Exception{
        // When
        decisionDelegate.consentDecision(toSerializedDecision(ACCOUNT_ID, true), true);

        // Then
        assertThat(consent.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(consent.getStatus()).isEqualTo(ConsentStatusCode.AUTHORISED);
        verify(paymentService, times(1)).updatePayment(any());
    }
    @Test
    public void consentDecision_userRejected_setStatusRejected_updateConsent() throws Exception{
        // When
        decisionDelegate.consentDecision(toSerializedDecision(ACCOUNT_ID, false), false);

        // Then
        assertThat(consent.getAccountId()).isNull();
        assertThat(consent.getStatus()).isEqualTo(ConsentStatusCode.REJECTED);
        verify(paymentService, times(1)).updatePayment(any());
    }

    @Test
    public void consentDecision_userAuthorised_missingAccountId_exception() {
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
        verifyZeroInteractions(paymentService);
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
                .hasMessage(EXPECTED_WRONG_ACC_ID_MSG);

        // Then
        assertThat(consent.getAccountId()).isNull();
        assertThat(consent.getStatus()).isNull();
        verifyZeroInteractions(paymentService);
    }

    @Test
    public void autoaccept_setAccountIdAndAuthorise_updateConsent() throws Exception {
        // When
        decisionDelegate.autoaccept(USER_ACCOUNT_LIST, USER_ID);

        // Then
        assertThat(consent.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(consent.getStatus()).isEqualTo(ConsentStatusCode.AUTHORISED);
        verify(paymentService, times(1)).updatePayment(any());
    }
}