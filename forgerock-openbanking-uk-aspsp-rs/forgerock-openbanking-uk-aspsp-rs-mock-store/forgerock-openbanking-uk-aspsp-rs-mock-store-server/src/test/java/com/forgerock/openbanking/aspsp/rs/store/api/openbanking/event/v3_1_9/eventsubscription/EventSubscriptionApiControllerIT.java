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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1_9.eventsubscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.events.EventSubscriptionsRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.domain.event.FREventSubscriptionData;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FREventSubscription;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.repositories.TppRepository;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.event.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class EventSubscriptionApiControllerIT {
    private static final String RESOURCE_URI = "/open-banking/" + OBVersion.v3_1_9.getCanonicalName() + "/event-subscriptions";
    private static final String BASE_URL = "https://rs-store:";

    @LocalServerPort
    private int port;

    @Autowired
    private EventSubscriptionsRepository eventSubscriptionsRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;

    @MockBean
    private TppRepository tppRepository;

    @Autowired
    private SpringSecForTest springSecForTest;

    private String clientId;

    private Tpp tpp;

    @Before
    public void setUp() {
        tpp = new Tpp();
        tpp.setId(UUID.randomUUID().toString());
        given(tppRepository.findByClientId(any())).willReturn(tpp);

        clientId = UUID.randomUUID().toString();
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void createEventSubscription() {
        // Given
        String url = "http://callback" + UUID.randomUUID();
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        OBEventSubscription1 obEventSubscription1 = new OBEventSubscription1()
                .data(new OBEventSubscription1Data()
                        .callbackUrl(url)
                        .version(OBVersion.v3_0.getCanonicalVersion())
                );

        // When
        HttpResponse<OBEventSubscription1> response = Unirest.post(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventSubscription1)
                .asObject(OBEventSubscription1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getBody().getData().getCallbackUrl()).isEqualTo(url);
        assertThat(response.getBody().getData().getVersion()).isEqualTo(OBVersion.v3_0.getCanonicalVersion());

        final Collection<FREventSubscription> byTpp = eventSubscriptionsRepository.findByTppId(tpp.getId());
        assertThat(byTpp.stream().findFirst().orElseThrow(AssertionError::new).getEventSubscription().getCallbackUrl()).isEqualTo(url);
    }

    @Test
    public void createCallbackUrls_urlAlreadyExistsForTpp_conflict() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        eventSubscriptionsRepository.save(FREventSubscription.builder()
                .tppId(tpp.getId())
                .eventSubscription(FREventSubscriptionData.builder().build())
                .build());
        OBEventSubscription1 obEventSubscription1 = new OBEventSubscription1()
                .data(new OBEventSubscription1Data()
                        .callbackUrl("http://callback")
                        .version(OBVersion.v3_0.getCanonicalVersion())
                );

        // When
        HttpResponse response = Unirest.post(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventSubscription1)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());

        final Collection<FREventSubscription> byClientId = eventSubscriptionsRepository.findByTppId(tpp.getId());
        assertThat(byClientId.size()).isEqualTo(1); // Should still be just 1
    }

    @Test
    public void readEventSubscription() {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        eventSubscriptionsRepository.save(FREventSubscription.builder()
                .tppId(tpp.getId())
                .eventSubscription(FREventSubscriptionData.builder()
                        .callbackUrl("http://callback")
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                        .build())
                .build());

        // When
        HttpResponse<OBEventSubscriptionsResponse1> response = Unirest.get(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(OBEventSubscriptionsResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getLinks().getSelf()).contains("/event-subscriptions");
        assertThat(response.getBody().getData().getEventSubscription().get(0).getCallbackUrl()).isEqualTo("http://callback");
    }

    @Test
    public void updateEventSubscription() {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        String eventSubsId = UUID.randomUUID().toString();
        OBEventSubscriptionResponse1 obEventSubscription1 = new OBEventSubscriptionResponse1()
                .data(new OBEventSubscriptionResponse1Data()
                        .eventSubscriptionId(eventSubsId)
                        .callbackUrl("http://callback/update")
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                );
        FREventSubscription frEventSubscription = FREventSubscription.builder()
                .id(eventSubsId)
                .eventSubscription(FREventSubscriptionData.builder()
                        .callbackUrl("http://callback")
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                        .build())
                .build();
        eventSubscriptionsRepository.save(frEventSubscription);

        // When
        HttpResponse<OBEventSubscriptionResponse1> response = Unirest.put(BASE_URL + port + RESOURCE_URI + "/" + eventSubsId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventSubscription1)
                .asObject(OBEventSubscriptionResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getLinks().getSelf()).contains("/event-subscriptions");
        assertThat(response.getBody().getData().getCallbackUrl()).isEqualTo("http://callback/update");

        final Optional<FREventSubscription> byId = eventSubscriptionsRepository.findById(eventSubsId);
        assertThat(byId.orElseThrow(AssertionError::new).getEventSubscription().getCallbackUrl()).isEqualTo("http://callback/update");
    }


    @Test
    public void updateEventSubscription_olderVersion() {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        String eventSubsId = UUID.randomUUID().toString();
        OBEventSubscriptionResponse1 obEventSubscription1 = new OBEventSubscriptionResponse1()
                .data(new OBEventSubscriptionResponse1Data()
                        .eventSubscriptionId(eventSubsId)
                        .callbackUrl("http://callback/update")
                        .version(OBVersion.v3_1_2.getCanonicalVersion())
                );
        FREventSubscription frEventSubscription = FREventSubscription.builder()
                .id(eventSubsId)
                .eventSubscription(FREventSubscriptionData.builder()
                        .callbackUrl("http://callback")
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                        .build())
                .build();
        eventSubscriptionsRepository.save(frEventSubscription);

        // When
        HttpResponse<OBEventSubscriptionResponse1> response = Unirest.put(BASE_URL + port + RESOURCE_URI + "/" + eventSubsId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventSubscription1)
                .asObject(OBEventSubscriptionResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getLinks().getSelf()).contains("/event-subscriptions");
    }

    @Test
    public void updateEventSubscription_notFound() {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        String eventSubsId = UUID.randomUUID().toString();
        OBEventSubscriptionResponse1 obEventSubscription1 = new OBEventSubscriptionResponse1()
                .data(new OBEventSubscriptionResponse1Data()
                        .eventSubscriptionId(eventSubsId)
                        .callbackUrl("http://callback/update")
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                );

        // When
        HttpResponse<OBEventSubscriptionResponse1> response = Unirest.put(BASE_URL + port + RESOURCE_URI + "/" + eventSubsId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventSubscription1)
                .asObject(OBEventSubscriptionResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(400);

    }

    @Test
    public void deleteEventSubscription() {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        String eventSubsId = UUID.randomUUID().toString();
        FREventSubscription frEventSubscription = FREventSubscription.builder()
                .eventSubscription(FREventSubscriptionData.builder()
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                        .build())
                .id(eventSubsId)
                .tppId(tpp.getId())
                .build();
        eventSubscriptionsRepository.save(frEventSubscription);

        // When
        HttpResponse response = Unirest.delete(BASE_URL + port + RESOURCE_URI + "/" + eventSubsId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(204);

        final Optional<FREventSubscription> byId = eventSubscriptionsRepository.findById(eventSubsId);
        assertThat(byId.isPresent()).isFalse();
    }

    @Test
    public void deleteEventSubscription_notFound() {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        String eventSubsId = UUID.randomUUID().toString();

        // When
        HttpResponse response = Unirest.delete(BASE_URL + port + RESOURCE_URI + "/" + eventSubsId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);

    }
}
