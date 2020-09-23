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
package com.forgerock.openbanking.aspsp.rs.simulator.event.notification;

import com.forgerock.openbanking.analytics.model.entries.callback.CallBackCounterEntry;
import com.forgerock.openbanking.analytics.model.entries.callback.CallBackResponseStatus;
import com.forgerock.openbanking.analytics.services.CallBackCountersKPIService;
import com.forgerock.openbanking.aspsp.rs.simulator.event.store.AggregatedPollingService;
import com.forgerock.openbanking.aspsp.rs.simulator.event.store.CallbackUrlsService;
import com.forgerock.openbanking.aspsp.rs.simulator.event.store.EventSubscriptionService;
import com.forgerock.openbanking.common.model.openbanking.event.FREventSubscription1;
import com.forgerock.openbanking.common.model.openbanking.forgerock.event.FREventNotification;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_0.event.FRCallbackUrl1;
import com.forgerock.openbanking.common.services.notification.EventSubject;
import com.forgerock.openbanking.common.services.notification.EventType;
import com.forgerock.openbanking.model.Tpp;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.event.OBEventSubscription1Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service to handle all current and future event types. Responsible for building an event notification from provided subject and type
 * and taking the appropriate action for the provided TPP (e.g. send real-time message, save for polling)
 */
@Service
@Slf4j
public class EventNotificationService {

    private final SignedJwtEventBuilder signedJwtEventBuilder;
    private final EventSubscriptionService eventSubscriptionService;
    private final CallbackUrlsService callbackUrlsService;
    private final AggregatedPollingService aggregatedPollingService;
    private final TppEventNotifier tppEventNotifier;
    private final CallBackCountersKPIService callBackCountersKPIService;

    public EventNotificationService(SignedJwtEventBuilder signedJwtEventBuilder, EventSubscriptionService eventSubscriptionService, CallbackUrlsService callbackUrlsService, AggregatedPollingService aggregatedPollingService, TppEventNotifier tppEventNotifier, CallBackCountersKPIService callBackCountersKPIService) {
        this.signedJwtEventBuilder = signedJwtEventBuilder;
        this.eventSubscriptionService = eventSubscriptionService;
        this.callbackUrlsService = callbackUrlsService;
        this.aggregatedPollingService = aggregatedPollingService;
        this.tppEventNotifier = tppEventNotifier;
        this.callBackCountersKPIService = callBackCountersKPIService;
    }

    public void createAndSendNotification(EventSubject eventSubject, Tpp tpp, EventType eventType) throws CallbackFailedException {
        // Create a notification
        FREventNotification eventNotification = signedJwtEventBuilder.build(tpp, eventSubject, eventType);
        if (eventNotification==null || StringUtils.isEmpty(eventNotification.getSignedJwt()) ) {
            log.error("Failed to generate JWT for event notification due to null or empty JWT: {}. Aborting the callback to TPPs", eventNotification);
            throw new CallbackFailedException("Failed to generate JWT for event notification due to null or empty JWT");
        }
        log.debug("Built an event notification with signed jwt: {}", eventNotification);

        // Check event subscription exists
        Optional<FREventSubscription1> eventSubscriptions = eventSubscriptionService.findByTppId(tpp.getId())
                .stream().findFirst();
        if (eventSubscriptions.isPresent()) {
            // Event subscription exists to use this to notify
            notifyToEventSubscription(tpp, eventSubscriptions.get(), eventNotification, eventSubject, eventType);
        } else {
            // Event subscription does not exist to try legacy callback URL if present
            notifyToLegacyCallbackUrl(tpp, eventNotification, eventSubject, eventType);
        }
    }

    private void notifyToEventSubscription(Tpp tpp, FREventSubscription1 eventSubscription, FREventNotification eventNotification, EventSubject eventSubject, EventType eventType) {
        log.debug("Found event subscription {} for the PISP Id: {}", eventSubscription, tpp.getId());
        OBEventSubscription1Data eventSubscription1Data = eventSubscription.getObEventSubscription1().getData();
        List<String> eventTypesForTpp = eventSubscription1Data.getEventTypes();
        // Check event type filter
        if (eventTypesForTpp !=null && eventTypesForTpp.stream()
                .noneMatch(e -> e.equals(eventType.getEventName()))) {
            log.debug("TPP {} has subscribed to event types: {} but this event is type: {}", tpp.getId(), eventTypesForTpp, eventType.getEventName());
            return;
        }

        String callbackUrl = eventSubscription.getObEventSubscription1().getData().getCallbackUrl();
        if (!StringUtils.isEmpty(callbackUrl)) {
            log.debug("Found event subscription callback URL for the PISP Id: {}", tpp.getId());
            try {
                doCallback(tpp, callbackUrl, eventNotification, eventSubject);
            } catch (CallbackFailedException e) {
                // Save failed attempt so it can be polled later
                log.warn("Callback to event subscription callback URL: {} failed so saving event for future polling", callbackUrl);
                aggregatedPollingService.createPendingEventNotification(eventNotification);
            }
        } else {
            // No callback URL but TPP has an event subscription so save for polling
            aggregatedPollingService.createPendingEventNotification(eventNotification);
            log.info("Created a pending notification that can be polled by the TPP for payment id: '{}' for PISP: {}", eventSubject.getId(), tpp.getId());
        }
    }

    /**
     * Prior to event subscriptions in 3.1.1, TPP could subscribe to callbacks on resource update events.
     */
    private void notifyToLegacyCallbackUrl(Tpp tpp, FREventNotification eventNotification, EventSubject eventSubject, EventType eventType) throws CallbackFailedException {
        // Fall back to legacy callback
        final Optional<FRCallbackUrl1> isCallbackUrl = callbackUrlsService.findByTppId(tpp.getId())
                .stream().findFirst();
        if (isCallbackUrl.isPresent() && EventType.RESOURCE_UPDATE_EVENT == eventType) {
            log.debug("Found callback URL: {} for the PISP Id: {}", isCallbackUrl.get(), tpp.getId());
            try {
                doCallback(tpp, isCallbackUrl.get().getCallBackUrlString(), eventNotification, eventSubject);
                log.debug("Sent callback successfully to: {} for the PISP Id: {}", isCallbackUrl.get().getCallBackUrlString(), tpp.getId());
            } catch (CallbackFailedException e) {
                log.warn("Callback failed and no event subscription for TPP: {} so event notification will be thrown away: {}", tpp.getOfficialName(), eventNotification);
                throw e;
            }
        } else {
            log.info("TPP with id: {} has no event subscription or callback URL so no payment notification will be available", tpp.getId());
        }
    }

    private void doCallback(Tpp tpp, String callbackUrl, FREventNotification eventNotification, EventSubject eventSubject) throws CallbackFailedException {
        CallBackCounterEntry.CallBackCounterEntryBuilder callBackCounterEntryBuilder = CallBackCounterEntry
                .builder()
                .date(DateTime.now())
                .tppId(tpp.getId())
                .redirectUri(callbackUrl);
        try {
            log.debug("Sending payment notification for payment id: '{}' to Url: {}", eventSubject.getId(), callbackUrl);
            tppEventNotifier.sendNotification(callbackUrl, eventNotification.getSignedJwt());
            log.debug("Successfully sent payment notification for payment id: '{}' to TPP Callback Url: {}", eventSubject.getId(), callbackUrl);
            callBackCounterEntryBuilder.callBackResponseStatus(CallBackResponseStatus.SUCCESS);
        } catch (Exception e) {
            log.warn("Failed to send event notification callback to URL: '{}' for TPP with Pisp Id: '{}' after max number of retries", callbackUrl, tpp.getId(), e);
            // Log error but continue to try and send notifications. One TPP may be down but others ok.
            callBackCounterEntryBuilder.callBackResponseStatus(CallBackResponseStatus.FAILURE);
            throw new CallbackFailedException("Callback for event notification has failed after reaching max retries", e);
        }
        callBackCountersKPIService.sendCallBackEntries(Collections.singletonList(callBackCounterEntryBuilder.build()));
        log.info("Successfully sent payment notification for payment id: '{}' to callbackUrl: {} for PISP: {}", eventSubject.getId(), callbackUrl, tpp.getId());
    }

}
