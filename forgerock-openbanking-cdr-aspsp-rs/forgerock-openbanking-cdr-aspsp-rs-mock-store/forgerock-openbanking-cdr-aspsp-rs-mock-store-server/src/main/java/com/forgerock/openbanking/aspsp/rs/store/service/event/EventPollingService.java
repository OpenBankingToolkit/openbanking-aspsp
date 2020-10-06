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
package com.forgerock.openbanking.aspsp.rs.store.service.event;

import com.forgerock.openbanking.aspsp.rs.store.repository.FRPendingEventsRepository;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FREventNotification;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.event.OBEventPolling1;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Intended to hold the business logic of polling and acknowledge the events separately to the persistence layer (Mongo interface) and the REST layer (controller)
 * Makes testing and re-use much easier and controller class less complicated
 */
@Service
@Slf4j
public class EventPollingService {
    private final FRPendingEventsRepository frPendingEventsRepository;

    // The TPP can never request more events than this.
    private static final Integer MAX_EVENTS = 50;

    public EventPollingService(FRPendingEventsRepository frPendingEventsRepository) {
        this.frPendingEventsRepository = frPendingEventsRepository;
    }

    public void acknowledgeEvents(OBEventPolling1 obEventPolling, String tppId) {
        Preconditions.checkNotNull(tppId);
        Preconditions.checkNotNull(obEventPolling);

        if (obEventPolling.getAck() !=null && !obEventPolling.getAck().isEmpty()) {
            log.debug("TPP '{}' is acknowledging (and therefore deleting) the following event notifications: {}", tppId, obEventPolling.getAck());
            obEventPolling.getAck()
                    .forEach(
                            jti -> frPendingEventsRepository.deleteByTppIdAndJti(tppId, jti)
                    );
        }
    }

    public void recordTppEventErrors(OBEventPolling1 obEventPolling, String tppId) {
        Preconditions.checkNotNull(tppId);
        Preconditions.checkNotNull(obEventPolling);
        if (obEventPolling.getSetErrs()!=null && !obEventPolling.getSetErrs().isEmpty()) {
            log.debug("Persisting {} event notification errors for keys: {}", obEventPolling.getSetErrs().size(), obEventPolling.getSetErrs().keySet());
            obEventPolling.getSetErrs()
                    .forEach((key, value) -> frPendingEventsRepository.findByTppIdAndJti(tppId, key)
                            .ifPresent(event -> {
                                event.setErrors(value);
                                frPendingEventsRepository.save(event);
                            }));
        }

    }

    public Map<String, String> fetchNewEvents(OBEventPolling1 obEventPolling, String tppId) throws OBErrorResponseException {
        Preconditions.checkNotNull(tppId);
        Preconditions.checkNotNull(obEventPolling);
        if (obEventPolling.getMaxEvents() !=null && obEventPolling.getMaxEvents() <= 0) {
            // Zero notifications can be requested by TPP when they just want to send acknowledgements and/or errors to sandbox
            log.debug("Polling request for TPP: '{}' requested no event notifications so none will be returned", tppId);
            return Collections.emptyMap();
        }

        // Long polling is currently optional in OB specs and as it requires more work to implement it will be left out for now.
        if (obEventPolling.isReturnImmediately() !=null && !obEventPolling.isReturnImmediately()) {
            log.warn("TPP: {} requested long polling on the event notification API but it is not supported", tppId);
            throw new OBErrorResponseException(
                    HttpStatus.NOT_IMPLEMENTED,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.LONG_POLLING_NOT_SUPPORTED_FOR_EVENTS.toOBError1());
        }

        // Load all event notifications for TPP
        log.debug("Loading all notifications for TPP: {}", tppId);
        return frPendingEventsRepository.findByTppId(tppId).stream()
                .filter(event -> !event.hasErrors())
                .collect(Collectors.toMap(FREventNotification::getJti, FREventNotification::getSignedJwt));
    }

    public Map<String, String> truncateEvents(Integer maxEvents, Map<String, String> eventNotifications, String tppId) {
        Preconditions.checkNotNull(tppId);
        log.debug("Request to truncate {} event notification to be max of {}", eventNotifications.size(), maxEvents);
        if (eventNotifications.isEmpty()) {
            return eventNotifications; // Nothing to do
        }

        if (maxEvents == null || maxEvents > MAX_EVENTS) {
            log.debug("TPP {} requested a number of event notifications ({}) on polling that exceeds that allowed maximum on the sandbox ({}). Only {} will be returned.", tppId, maxEvents, MAX_EVENTS, MAX_EVENTS);
            maxEvents = MAX_EVENTS;
        }

        if (eventNotifications.size() > maxEvents) {
            log.debug("TPP has {} pending event notifications. Only the first {} will be returned.", eventNotifications.size(), maxEvents);
            return eventNotifications.entrySet().stream()
                    .limit(maxEvents)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } else {
            return eventNotifications;
        }

    }
}
