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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.internationalpayment;

import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.AbstractDecisionDelegateTest;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalConsent4;
import com.forgerock.openbanking.common.services.store.payment.InternationalPaymentService;
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
public class InternationalPaymentConsentDecisionDelegateTest extends AbstractDecisionDelegateTest {
    private InternationalPaymentService paymentService;
    private FRInternationalConsent4 consent;
    private InternationalPaymentConsentDecisionDelegate decisionDelegate;

    @Before
    public void setup() {
        paymentService = mock(InternationalPaymentService.class);
        consent = FRInternationalConsent4.builder()
                .id(CONSENT_ID)
                .pispId(PISP_ID)
                .userId(USER_ID)
                .build();
        decisionDelegate = new InternationalPaymentConsentDecisionDelegate(
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
    public void consentDecision_userAuthorised_setAccountIdAndStatus_updateConsent() throws Exception {
        // When
        decisionDelegate.consentDecision(toSerializedDecision(ACCOUNT_ID, true), true);

        // Then
        assertThat(consent.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(consent.getStatus()).isEqualTo(ConsentStatusCode.AUTHORISED);
        verify(paymentService, times(1)).updatePayment(any());
    }

    @Test
    public void consentDecision_userRejected_setStatusRejected_updateConsent() throws Exception {
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