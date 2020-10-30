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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.common.model.notification.ResourceUpdateEvent;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FREventNotification;
import com.forgerock.openbanking.common.services.notification.EventSubject;
import com.forgerock.openbanking.common.services.notification.EventType;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.Tpp;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static com.forgerock.openbanking.constants.OpenBankingConstants.EventNotificationClaims.*;

/**
 * Changes an event subject (e.g. a changed payment) into a signed Event Notification JWT that can be sent to interested TPPs
 */
@Component
@Slf4j
public class SignedJwtEventBuilder {
    private static final int JWT_EXPIRATION_TIME_MINUTES = 5;

    private final String issuerId;
    private final CryptoApiClient cryptoApiClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public SignedJwtEventBuilder(
            @Value("${am.realm.auth.oidc.issuerid}") String issuerId,
            CryptoApiClient cryptoApiClient,
            ObjectMapper objectMapper
    ) {
        this.issuerId = issuerId;
        this.cryptoApiClient = cryptoApiClient;
        this.objectMapper = objectMapper;
    }

    public FREventNotification build(Tpp tpp, EventSubject eventSubject, EventType eventType) {
        log.debug("About to create event notification for changed resource: {}", eventSubject);

        final ResourceUpdateEvent resourceUpdateEvent = ResourceUpdateEvent.builder()
                .subject(
                        ResourceUpdateEvent.Subject.builder()
                                .subjectType(OB_SUBJECT_TYPE)
                                .resourceId(eventSubject.getId())
                                .resourceType(eventSubject.getType())
                                .resourceLinks(
                                        Collections.singletonList(
                                                ResourceUpdateEvent.ResourceLink.builder()
                                                        .link(eventSubject.getUrl())
                                                        .version(eventSubject.getVersion())
                                                        .build()
                                        ))
                                .build()
                ).build();

        log.debug("Created resource update event for changed resource: {}", resourceUpdateEvent);
        String jti = UUID.randomUUID().toString();
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer(issuerId)
                    .issueTime(new Date())
                    .jwtID(UUID.randomUUID().toString())
                    .subject(eventSubject.getUrl())
                    .audience(tpp.getClientId())
                    .expirationTime(new Date(Instant.now().plus(JWT_EXPIRATION_TIME_MINUTES, ChronoUnit.MINUTES).toEpochMilli()))
                    .claim(TRANSACTION_IDENTIFIER, UUID.randomUUID().toString())
                    .claim(TIME_OF_EVENT, DateUtils.toSecondsSinceEpoch(new Date()))
                    .claim(EVENTS, Collections.singletonMap(eventType.getEventName(), objectMapper.writeValueAsString(resourceUpdateEvent)))
                    .build();
            log.debug("Built claims set for event notification: {}", claimsSet);

            // https://openbanking.atlassian.net/wiki/spaces/DZ/pages/645367055/Event+Notification+API+Specification+-+v3.0#EventNotificationAPISpecification-v3.0-EventNotificationMessageSigning
            String signedJwt = cryptoApiClient.signClaims(issuerId, claimsSet, false);
            log.debug("Signed event notification: {}", signedJwt);

            return FREventNotification.builder()
                    .jti(jti)
                    .signedJwt(signedJwt)
                    .tppId(tpp.getId())
                    .build();
        } catch (IOException e) {
            log.error("Unable to parse the generated resource event '{}' into a notification JWT. This is an internal error with event builder.", resourceUpdateEvent, e);
            throw new IllegalStateException("Internal error generating notification",e);
        }
    }


}
