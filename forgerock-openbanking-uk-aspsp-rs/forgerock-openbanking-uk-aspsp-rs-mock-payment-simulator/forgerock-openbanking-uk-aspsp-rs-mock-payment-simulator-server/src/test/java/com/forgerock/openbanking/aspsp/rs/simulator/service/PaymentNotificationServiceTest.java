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
package com.forgerock.openbanking.aspsp.rs.simulator.service;

import com.forgerock.openbanking.aspsp.rs.simulator.event.notification.EventNotificationService;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.PaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
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
        PaymentConsent payment = FRDomesticConsent.builder()
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