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
package com.forgerock.openbanking.common.services.store.data;

import com.forgerock.openbanking.common.model.data.FRDataEvent;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FREventNotification;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

/**
 * Logical entity Service to provide logic on the data sent to and from rs-store (store-server) and rs-ui (portal-server) apis
 */
@Service
@Slf4j
public class DataEventsService {

    private final RestTemplate restTemplate;

    @Value("${rs-store.base-url}")
    private String rsStoreRoot;

    private final String URL_CONTEXT = "/api/data/events";

    @Autowired
    public DataEventsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Export all events
     * @return {@link FREventNotification} collection
     */
    public Collection<FREventNotification> exportEvents() {
        log.debug("Export all data events");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + URL_CONTEXT + "/all");
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.GET, null, getParameterizedTypeReference()).getBody();
    }

    /**
     * Export the events by tppId (tpp username)
     * @param frDataEvent the entity body {@link FRDataEvent}
     * @return {@link FREventNotification} collection
     * @throws {@link OBErrorResponseException}
     */
    public Collection<FREventNotification> exportEventsByTppId(FRDataEvent frDataEvent) throws OBErrorResponseException {
        validateTppId(frDataEvent);
        log.debug("Export all data events by user {}", frDataEvent.getTppId());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + URL_CONTEXT);
        builder.queryParam("tppId", frDataEvent.getTppId());
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.GET, null, getParameterizedTypeReference()).getBody();
    }

    /**
     * Import events into the system
     * @param frDataEvent the entity body {@link FRDataEvent}
     * @return Imported {@link FREventNotification} collection
     * @throws {@link OBErrorResponseException}
     */
    public Collection<FREventNotification> importEvents(FRDataEvent frDataEvent) throws OBErrorResponseException {
        log.debug("Import events");
        Preconditions.checkNotNull(frDataEvent.getTppId(),"The tppId cannot be null.");
        Preconditions.checkElementIndex(0,frDataEvent.getObEventNotification2List().size(),"The events cannot be empty");
        validateTppId(frDataEvent);
        validateEvents(frDataEvent);
        HttpEntity request = new HttpEntity(frDataEvent, new HttpHeaders());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + URL_CONTEXT);
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.POST, request, getParameterizedTypeReference()).getBody();
    }

    /**
     * Update existing events
     * @param frDataEvent the entity body {@link FRDataEvent}
     * @return Updated {@link FREventNotification} collection
     * @throws {@link OBErrorResponseException}
     */
    public Collection<FREventNotification> updateEvents(FRDataEvent frDataEvent) throws OBErrorResponseException {
        log.debug("Update events");
        validateTppId(frDataEvent);
        validateEvents(frDataEvent);
        HttpEntity request = new HttpEntity(frDataEvent, new HttpHeaders());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + URL_CONTEXT);
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.PUT, request, getParameterizedTypeReference()).getBody();
    }

    /**
     * Remove the events related to tppId
     * @param frDataEvent the entity body {@link FRDataEvent}
     * @return {@link Void}
     * @throws {@link OBErrorResponseException}
     */
    public Void removeEvents(FRDataEvent frDataEvent) throws OBErrorResponseException {
        log.debug("Delete events for the tppId {}", frDataEvent.getTppId());
        validateTppId(frDataEvent);
        HttpEntity request = new HttpEntity(frDataEvent, new HttpHeaders());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + URL_CONTEXT);
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.DELETE, request, Void.class).getBody();
    }
    /**
     * Validation of mandatory tppId
     * @param frDataEvent the entity body {@link FRDataEvent}
     * @throws {@link OBErrorResponseException}
     */
    private void validateTppId(FRDataEvent frDataEvent) throws OBErrorResponseException {
        if (frDataEvent.getTppId() == null) {
            throw new OBErrorResponseException(
                    OBRIErrorType.DATA_INVALID_REQUEST.getHttpStatus(),
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.DATA_INVALID_REQUEST.toOBError1("The tppId cannot be null"));
        }
    }

    /**
     * Validation of event list when must be mandatory for import and update
     * @param frDataEvent the entity body {@link FRDataEvent}
     * @throws {@link OBErrorResponseException}
     */
    private void validateEvents(FRDataEvent frDataEvent) throws OBErrorResponseException {
        if (frDataEvent.getObEventNotification2List().isEmpty()) {
            throw new OBErrorResponseException(
                    OBRIErrorType.DATA_INVALID_REQUEST.getHttpStatus(),
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.DATA_INVALID_REQUEST.toOBError1("The events cannot be empty"));
        }
    }

    /**
     * Enable capturing and passing a generic Type
     * @return {@link ParameterizedTypeReference}
     */
    private ParameterizedTypeReference<Collection<FREventNotification>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
