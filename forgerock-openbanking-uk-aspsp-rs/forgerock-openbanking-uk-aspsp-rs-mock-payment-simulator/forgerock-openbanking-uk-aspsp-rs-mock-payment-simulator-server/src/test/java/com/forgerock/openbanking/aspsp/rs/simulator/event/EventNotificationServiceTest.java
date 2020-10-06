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
package com.forgerock.openbanking.aspsp.rs.simulator.event;

import com.forgerock.openbanking.analytics.services.CallBackCountersKPIService;
import com.forgerock.openbanking.aspsp.rs.simulator.event.notification.EventNotificationService;
import com.forgerock.openbanking.aspsp.rs.simulator.event.notification.SignedJwtEventBuilder;
import com.forgerock.openbanking.aspsp.rs.simulator.event.notification.TppEventNotifier;
import com.forgerock.openbanking.aspsp.rs.simulator.event.store.AggregatedPollingService;
import com.forgerock.openbanking.aspsp.rs.simulator.event.store.CallbackUrlsService;
import com.forgerock.openbanking.aspsp.rs.simulator.event.store.EventSubscriptionService;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FREventSubscription;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FREventNotification;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FRCallbackUrl;
import com.forgerock.openbanking.common.services.notification.EventSubject;
import com.forgerock.openbanking.common.services.notification.EventType;
import com.forgerock.openbanking.model.Tpp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.event.OBCallbackUrl1;
import uk.org.openbanking.datamodel.event.OBCallbackUrlData1;
import uk.org.openbanking.datamodel.event.OBEventSubscription1;
import uk.org.openbanking.datamodel.event.OBEventSubscription1Data;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class EventNotificationServiceTest {

    @Mock
    private SignedJwtEventBuilder signedJwtEventBuilder;
    @Mock
    private EventSubscriptionService eventSubscriptionService;
    @Mock
    private CallbackUrlsService callbackUrlsService;
    @Mock
    private AggregatedPollingService aggregatedPollingService;
    @Mock
    private TppEventNotifier tppEventNotifier;
    @Mock
    private CallBackCountersKPIService callBackCountersKPIService;
    @InjectMocks
    EventNotificationService eventNotificationService;

    private final static Tpp TPP = Tpp.builder()
            .id("tpp1")
            .clientId("1111")
            .officialName("test")
            .build();
    private static final EventSubject EVENT_SUBJECT = EventSubject.builder()
            .id("PDC_1")
            .type("domestic-payment")
            .version("v3.1")
            .url("http://myRS/open-banking/v3.1/domestic-payments/PDC_1")
            .build();
    private static final String SIGNED_JWT = "we3498yrt93p4hf;ihfl3iu4g98432gil";
    private static final FREventNotification EVENT_NOTIFICATION = FREventNotification.builder()
            .jti("111")
                .tppId(TPP.getId())
            .signedJwt(SIGNED_JWT)
                .id("testEvent1")
                .build();

    @Before
    public void before() {
        given(signedJwtEventBuilder.build(any(), any(), any())).willReturn(EVENT_NOTIFICATION);
    }

    @Test
    public void createAndSendNotification_gotEventSubscription_WithCallbackUrl_send() throws Exception{
        // Given
        final String tppEventCallback = "http://myTpp/v3.1/event-notifications";
        given(eventSubscriptionService.findByTppId(eq(TPP.getId()))).willReturn(Collections.singleton(
                FREventSubscription.builder()
                        .obEventSubscription1(new OBEventSubscription1().data(
                                new OBEventSubscription1Data()
                                        .callbackUrl(tppEventCallback)))
                        .build()
        ));

        // When
        eventNotificationService.createAndSendNotification(EVENT_SUBJECT, TPP, EventType.RESOURCE_UPDATE_EVENT);

        // Then
        verify(tppEventNotifier).sendNotification(eq(tppEventCallback), eq(SIGNED_JWT));
        verifyZeroInteractions(aggregatedPollingService);
        verifyZeroInteractions(callbackUrlsService);
    }

    @Test
    public void createAndSendNotification_gotEventSubscription_WithCallbackUrl_withEventType_send() throws Exception{
        // Given
        final String tppEventCallback = "http://myTpp/v3.1/event-notifications";
        given(eventSubscriptionService.findByTppId(eq(TPP.getId()))).willReturn(Collections.singleton(
                FREventSubscription.builder()
                        .obEventSubscription1(new OBEventSubscription1().data(
                                new OBEventSubscription1Data()
                                        .callbackUrl(tppEventCallback)
                                        .eventTypes(Collections.singletonList(EventType.RESOURCE_UPDATE_EVENT.getEventName()))
                        ))
                        .build()
        ));

        // When
        eventNotificationService.createAndSendNotification(EVENT_SUBJECT, TPP, EventType.RESOURCE_UPDATE_EVENT);

        // Then
        verify(tppEventNotifier).sendNotification(eq(tppEventCallback), eq(SIGNED_JWT));
        verifyZeroInteractions(aggregatedPollingService);
        verifyZeroInteractions(callbackUrlsService);
    }

    @Test
    public void createAndSendNotification_gotEventSubscription_WithoutCallbackUrl_save() throws Exception{
        // Given
        given(eventSubscriptionService.findByTppId(eq(TPP.getId()))).willReturn(Collections.singleton(
                FREventSubscription.builder()
                        .obEventSubscription1(new OBEventSubscription1().data(
                                new OBEventSubscription1Data()
                                        .callbackUrl(null)))
                        .build()
        ));

        // When
        eventNotificationService.createAndSendNotification(EVENT_SUBJECT, TPP, EventType.RESOURCE_UPDATE_EVENT);

        // Then
        verify(aggregatedPollingService).createPendingEventNotification(eq(EVENT_NOTIFICATION));
        verifyZeroInteractions(tppEventNotifier);
        verifyZeroInteractions(callbackUrlsService);
    }

    @Test
    public void createAndSendNotification_noEventSubscription_sendToLegacyCallback() throws Exception {
        // Given
        final String tppEventCallback = "http://myTpp/v3.1/event-notifications";
        given(eventSubscriptionService.findByTppId(any())).willReturn(Collections.emptyList());
        given(callbackUrlsService.findByTppId(eq(TPP.getId()))).willReturn(Collections.singleton(
                FRCallbackUrl.builder()
                        .obCallbackUrl(new OBCallbackUrl1().data(new OBCallbackUrlData1().url(tppEventCallback)))
                        .build()
        ));

        // When
        eventNotificationService.createAndSendNotification(EVENT_SUBJECT, TPP, EventType.RESOURCE_UPDATE_EVENT);

        // Then
        verify(tppEventNotifier).sendNotification(eq(tppEventCallback), eq(SIGNED_JWT));
        verifyZeroInteractions(aggregatedPollingService);
    }


}