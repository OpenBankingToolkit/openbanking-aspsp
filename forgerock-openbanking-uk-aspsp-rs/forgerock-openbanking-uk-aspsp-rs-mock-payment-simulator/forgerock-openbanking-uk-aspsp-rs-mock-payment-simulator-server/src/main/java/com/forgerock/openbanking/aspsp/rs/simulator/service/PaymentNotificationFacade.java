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

import com.forgerock.openbanking.aspsp.rs.simulator.event.notification.CallbackFailedException;
import com.forgerock.openbanking.aspsp.rs.simulator.event.notification.EventNotificationService;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRScheduledPayment;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.PaymentConsent;
import com.forgerock.openbanking.common.services.notification.EventSubject;
import com.forgerock.openbanking.common.services.notification.EventType;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.model.Tpp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Handles different payments types and changes them into generic event notifications for the EventNotificationService
 */
@Service
@Slf4j
public class PaymentNotificationFacade {
    private static final String EVENT_VERSION = "v3.1"; // For now events only exist in 3.1.x so this is ok - needs to be refactored in 4.x onwards to work out version from payment.

    private final PaymentToResourceConverter paymentToResourceConverter;
    private final TppStoreService tppStoreService;
    private final EventNotificationService eventNotificationService;

    @Autowired
    public PaymentNotificationFacade(PaymentToResourceConverter paymentToResourceConverter, TppStoreService tppStoreService, EventNotificationService eventNotificationService) {
        this.paymentToResourceConverter = paymentToResourceConverter;
        this.tppStoreService = tppStoreService;
        this.eventNotificationService = eventNotificationService;
    }

    public void paymentStatusChanged(PaymentConsent paymentConsent) {
        log.debug("Send notification for changed payment status. id: '{}', status: '{}'", paymentConsent.getId(), paymentConsent.getStatus());
        createAndSendNotification(paymentConsent.getId(), paymentConsent.getPispId());
    }

    public void paymentStatusChanged(FRScheduledPayment scheduledPayment) {
        if (scheduledPayment.getPispId()==null) {
            // Some old scheduled payments may have been created without pisp ids so ignore them for notifications
            log.warn("Cannot send a notification for scheduled payment id: '{}' because the pisp id on the payment is null", scheduledPayment.getId());
            return;
        }
        log.debug("Send notification for changed scheduled payment status. id: '{}', status: '{}'", scheduledPayment.getId(), scheduledPayment.getStatus());
        createAndSendNotification(scheduledPayment.getId(), scheduledPayment.getPispId());
    }

    public void paymentStatusChanged(FRStandingOrder frStandingOrder) {
        if (frStandingOrder.getPispId()==null) {
            // Some old standing orders may have been created without pisp ids so ignore them for notifications
            log.warn("Cannot send a notification for standing order payment id: '{}' because the pisp id on the payment is null", frStandingOrder.getId());
            return;
        }
        log.debug("Send notification for changed standing order payment status. id: '{}', status: '{}'", frStandingOrder.getId(), frStandingOrder.getStatus());
        createAndSendNotification(frStandingOrder.getId(), frStandingOrder.getPispId());
    }

    private void createAndSendNotification(String paymentId, String pispId) {
        log.debug("Notification for payment '{}' to PISP Id: '{}'", paymentId, pispId);

        // Get the TPP
        Optional<Tpp> isTpp = tppStoreService.findById(pispId);
        if (isTpp.isEmpty()) {
            // Pisp id is from payment so this is an internal error  if TPP not found here
            log.error("No TPP was found for pispId '{}' on payment: {}. No notification will be sent.", pispId, paymentId);
            return; // Don't throw exception as we wish to continue processing other standing orders
        }
        Tpp tpp = isTpp.get();
        log.debug("Loaded TPP: {} for pisp id: {}", tpp.getOfficialName(), pispId);

        // Convert payment to a changed resource
        EventSubject eventSubject = paymentToResourceConverter.toResource(paymentId, EVENT_VERSION);

        try {
            eventNotificationService.createAndSendNotification(eventSubject, tpp, EventType.RESOURCE_UPDATE_EVENT);
        } catch (CallbackFailedException e) {
            log.warn("Callback failed for TPP client ID: {} with an event type of {} for payment: {}.", tpp.getClientId(), EventType.RESOURCE_UPDATE_EVENT, eventSubject.getId(), e);
            // Don't re-throw exception as we wish to continue processing other payments
        }
    }
}
