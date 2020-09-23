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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.aspsp.rs.store.repository.FRPendingEventsRepository;
import com.forgerock.openbanking.common.model.data.FRDataEvent;
import com.forgerock.openbanking.common.model.data.OBEventNotification2;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.v3_0.FREventNotification;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.*;

@Controller("DataEventsApi")
@Slf4j
public class DataEventsApiController implements DataEventsApi {

    private final FRPendingEventsRepository frPendingEventsRepository;

    private final CryptoApiClient cryptoApiClient;

    private final ObjectMapper mapper;

    private final AMOpenBankingConfiguration amOpenBankingConfiguration;

    @Autowired
    public DataEventsApiController(FRPendingEventsRepository frPendingEventsRepository, CryptoApiClient cryptoApiClient, ObjectMapper mapper, AMOpenBankingConfiguration amOpenBankingConfiguration) {
        this.frPendingEventsRepository = frPendingEventsRepository;
        this.cryptoApiClient = cryptoApiClient;
        this.mapper = mapper;
        this.amOpenBankingConfiguration = amOpenBankingConfiguration;
    }

    @Override
    public ResponseEntity importEvents(@RequestBody FRDataEvent frDataEvent) throws OBErrorResponseException {
        try {
            createEvents(frDataEvent);
            return new ResponseEntity<>(frPendingEventsRepository.findByTppId(frDataEvent.getTppId()), HttpStatus.CREATED);
        } catch (Exception exception) {
            throw handleError(exception);
        }
    }

    @Override
    public ResponseEntity updateEvents(@RequestBody FRDataEvent frDataEvent) throws OBErrorResponseException {
        try {
            if (frDataEvent.getJti() != null) {
                return updateEventsByTppIdAndJti(frDataEvent);
            } else {
                return updateEventsByTppId(frDataEvent);
            }
        } catch (Exception exception) {
            throw handleError(exception);
        }
    }

    @Override
    public ResponseEntity<Collection<FREventNotification>> exportEvents() {
        log.debug("Find all Events");
        List<FREventNotification> frEventNotificationList = new ArrayList<>();
        frEventNotificationList.addAll(frPendingEventsRepository.findAll());
        return ResponseEntity.ok(frEventNotificationList);
    }

    @Override
    public ResponseEntity<Collection<FREventNotification>> exportEventsByTppId(@RequestParam String tppId) {
        log.debug("Find all Events for TPP:{}", tppId);
        List<FREventNotification> frEventNotificationList = new ArrayList<>(frPendingEventsRepository.findByTppId(tppId));
        return ResponseEntity.ok(frEventNotificationList);
    }

    @Override
    public ResponseEntity<Void> removeEvents(FRDataEvent frDataEvent) {
        log.debug("Remove all Events for TPP:{}", frDataEvent.getTppId());
        frPendingEventsRepository.deleteByTppId(frDataEvent.getTppId());
        return new ResponseEntity(Void.class, HttpStatus.NO_CONTENT);
    }

    /**
     * Update events by tppId
     * @param frDataEvent the entity body {@link FRDataEvent}
     * @return {@link ResponseEntity}
     * @throws {@link OBErrorResponseException}
     */
    private ResponseEntity updateEventsByTppId(@RequestBody FRDataEvent frDataEvent) throws OBErrorResponseException {
        List<FREventNotification> events = new ArrayList<>(frPendingEventsRepository.findByTppId(frDataEvent.getTppId()));
        if (!events.isEmpty()) {
            updateEvents(events, frDataEvent);
            return ResponseEntity.ok(new ArrayList<>(frPendingEventsRepository.findByTppId(frDataEvent.getTppId())));
        } else {
            log.debug("#updateEventsByTppId No events to update found");
            return ResponseEntity.ok(events);
        }

    }

    /**
     * Update events by tppId and Jti
     * @param frDataEvent the entity body {@link FRDataEvent}
     * @return {@link ResponseEntity}
     * @throws {@link OBErrorResponseException}
     */
    private ResponseEntity updateEventsByTppIdAndJti(FRDataEvent frDataEvent) throws OBErrorResponseException {
        Optional<FREventNotification> optionalFREventNotification = frPendingEventsRepository.findByTppIdAndJti(frDataEvent.getTppId(), frDataEvent.getJti());
        if (!optionalFREventNotification.isEmpty()) {
            updateEvents(Arrays.asList(optionalFREventNotification.get()), frDataEvent);
            return ResponseEntity.ok(Arrays.asList(frPendingEventsRepository.findByTppIdAndJti(frDataEvent.getTppId(), frDataEvent.getJti()).get()));
        } else {
            log.debug("#updateEventsByTppIdAndJti No events to update found");
            return ResponseEntity.ok(new ArrayList());
        }
    }

    /**
     * Update events
     * @param eventsToUpdate list of {@link FREventNotification} to update
     * @param frDataEvent the entity body {@link FRDataEvent}
     * @throws {@link OBErrorResponseException}
     */
    private void updateEvents(List<FREventNotification> eventsToUpdate, FRDataEvent frDataEvent) throws OBErrorResponseException {
        try {
            frDataEvent.getObEventNotification2List().forEach(fde -> {
                Optional<FREventNotification> optionalFREventNotification = eventsToUpdate.stream().filter(e -> e.getJti().equals(fde.getJti())).findFirst();
                if (optionalFREventNotification.isEmpty()) {
                    log.error("Error updating the events, the jti set in the request not match with any existing event");
                    throw new RuntimeException(new OBErrorResponseException(
                            OBRIErrorType.DATA_INVALID_REQUEST.getHttpStatus(),
                            OBRIErrorResponseCategory.REQUEST_INVALID,
                            OBRIErrorType.DATA_INVALID_REQUEST.toOBError1("The jti set in the request not match with any existing event")));
                }
                try {
                    FREventNotification frEventNotification = optionalFREventNotification.get();
                    frEventNotification.setSignedJwt(signPayload(fde));
                    frPendingEventsRepository.save(frEventNotification);
                } catch (OBErrorException obErrorException) {
                    throw new RuntimeException(obErrorException);
                }
            });
        } catch (RuntimeException exception) {
            throw handleError(exception);
        }
    }

    /**
     * Create new events
     * @param frDataEvent the entity body {@link FRDataEvent}
     */
    private void createEvents(FRDataEvent frDataEvent) {
        frDataEvent.getObEventNotification2List().forEach(
                e -> {
                    try {
                        FREventNotification frEventNotification = FREventNotification.builder()
                                .jti(UUID.randomUUID().toString())
                                .signedJwt(
                                        signPayload(e)
                                )
                                .tppId(frDataEvent.getTppId())
                                .build();
                        log.debug("Create event notification {}", frEventNotification);
                        frPendingEventsRepository.save(frEventNotification);
                        frDataEvent.setJti(frEventNotification.getJti());
                    } catch (OBErrorException obErrorException) {
                        throw new RuntimeException(obErrorException);
                    }
                }
        );
    }

    /**
     * Sign the payload
     * @param obEventNotification2 {@link OBEventNotification2} to sing
     * @return String of jwt
     * @throws {@link OBErrorException}
     */
    private String signPayload(OBEventNotification2 obEventNotification2) throws OBErrorException {
        try {
            log.debug("Signing the payload: {}", obEventNotification2);
            JWTClaimsSet jwtClaimsSet = JWTClaimsSet.parse(mapper.writeValueAsString(obEventNotification2));
            JSONObjectUtils.parse(mapper.writeValueAsString(obEventNotification2));
            return cryptoApiClient.signClaims(amOpenBankingConfiguration.getIssuerID(), jwtClaimsSet, false);
        } catch (JsonProcessingException | ParseException e) {
            throw new OBErrorException(OBRIErrorType.SERVER_ERROR, "Error processing the payload to sign it: {}", e.getMessage());
        }
    }

    /**
     * Handle the exception and reformat to {@link OBErrorResponseException}
     * @param exception the exception to handle
     * @return {@link OBErrorResponseException} the response exception
     */
    private OBErrorResponseException handleError(Exception exception) {
        if (exception instanceof OBErrorException) {
            OBErrorException obErrorException = (OBErrorException) exception;
            return new OBErrorResponseException(obErrorException.getObriErrorType().getHttpStatus(),
                    OBRIErrorResponseCategory.SERVER_INTERNAL_ERROR,
                    obErrorException.getOBError());
        } else if (exception instanceof OBErrorResponseException) {
            return (OBErrorResponseException) exception;
        } else if (exception.getCause() != null && exception.getCause() instanceof OBErrorResponseException) {
            return (OBErrorResponseException) exception.getCause();
        } else {
            return new OBErrorResponseException(
                    OBRIErrorType.SERVER_ERROR.getHttpStatus(),
                    OBRIErrorResponseCategory.SERVER_INTERNAL_ERROR,
                    OBRIErrorType.SERVER_ERROR.toOBError1(exception.getCause().getMessage()));
        }
    }
}
