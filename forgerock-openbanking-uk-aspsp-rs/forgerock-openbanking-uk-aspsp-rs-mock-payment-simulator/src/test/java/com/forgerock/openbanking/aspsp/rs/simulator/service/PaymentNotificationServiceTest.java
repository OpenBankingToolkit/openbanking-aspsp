/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.simulator.service;

import com.forgerock.openbanking.aspsp.rs.simulator.event.notification.EventNotificationService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRDomesticConsent1;
import com.forgerock.openbanking.common.services.notification.EventSubject;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.model.Tpp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PaymentNotificationServiceTest {

    private static final String PISP_ID = "pisp1";
    private static final String CLIENT_ID = "pisp1";

    private static Tpp tpp = new Tpp();

    @Mock
    private PaymentToResourceConverter paymentToResourceConverter;
    @Mock
    private EventNotificationService eventNotificationService;
    @Mock
    private TppStoreService tppStoreService;

    @InjectMocks
    private PaymentNotificationFacade paymentNotificationService;

    @Before
    public void setUp() {
        tpp = new Tpp();
        tpp.setId(PISP_ID);
        tpp.setClientId(CLIENT_ID);
    }

    @Test
    public void paymentStatusChanged_notifyCallbackUrl() throws Exception {
        // Given
        FRPaymentConsent payment = FRDomesticConsent1.builder()
                .id("PDC_111")
                .pispId(PISP_ID)
                .build();
        given(tppStoreService.findById(PISP_ID)).willReturn(Optional.of(tpp));
        EventSubject subject = EventSubject.builder().id("PDC_111").build();
        given(paymentToResourceConverter.toResource(eq("PDC_111"), any())).willReturn(subject);
        // When
        paymentNotificationService.paymentStatusChanged(payment);

        // Then
        verify(eventNotificationService).createAndSendNotification(eq(subject), eq(tpp), any());
    }
}