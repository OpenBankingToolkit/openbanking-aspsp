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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1_2.eventsubscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.repositories.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_2.events.EventSubscriptionsRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.event.FREventSubscription1;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
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
    private static final String RESOURCE_URI = "/open-banking/"+OBVersion.v3_1_2.getCanonicalName()+"/event-subscriptions";
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
        String url = "http://callback"+UUID.randomUUID().toString();
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

        final Collection<FREventSubscription1> byTpp = eventSubscriptionsRepository.findByTppId(tpp.getId());
        assertThat(byTpp.stream().findFirst().orElseThrow(AssertionError::new).getObEventSubscription1().getData().getCallbackUrl()).isEqualTo(url);
    }

    @Test
    public void createCallbackUrls_urlAlreadyExistsForTpp_conflict() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        String id = UUID.randomUUID().toString();
        eventSubscriptionsRepository.save(FREventSubscription1.builder()
                .tppId(tpp.getId())
                .obEventSubscription1(new OBEventSubscription1())
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

        final Collection<FREventSubscription1> byClientId = eventSubscriptionsRepository.findByTppId(tpp.getId());
        assertThat(byClientId.size()).isEqualTo(1); // Should still be just 1
    }

    @Test
    public void readEventSubscription() {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        eventSubscriptionsRepository.save(FREventSubscription1.builder()
                .tppId(tpp.getId())
                .obEventSubscription1(new OBEventSubscription1()
                .data(new OBEventSubscription1Data()
                .callbackUrl("http://callback")
                .version(OBVersion.v3_1_2.getCanonicalVersion())))
                .build());

        // When
        HttpResponse<OBEventSubscriptionsResponse1> response = Unirest.get(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(OBEventSubscriptionsResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
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
                        .version(OBVersion.v3_1.getCanonicalVersion())
                );
        FREventSubscription1 frEventSubscription1 = FREventSubscription1.builder()
                .id(eventSubsId)
                .obEventSubscription1(new OBEventSubscription1()
                        .data(new OBEventSubscription1Data()
                                .callbackUrl("http://callback")
                                .version(OBVersion.v3_1.getCanonicalVersion())
                        ))
                .build();
        eventSubscriptionsRepository.save(frEventSubscription1);

        // When
        HttpResponse<OBEventSubscriptionResponse1> response = Unirest.put(BASE_URL + port + RESOURCE_URI+"/"+eventSubsId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventSubscription1)
                .asObject(OBEventSubscriptionResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getCallbackUrl()).isEqualTo("http://callback/update");

        final Optional<FREventSubscription1> byId = eventSubscriptionsRepository.findById(eventSubsId);
        assertThat(byId.orElseThrow(AssertionError::new).getObEventSubscription1().getData().getCallbackUrl()).isEqualTo("http://callback/update");
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
                        .version(OBVersion.v3_0.getCanonicalVersion())
                );
        FREventSubscription1 frEventSubscription1 = FREventSubscription1.builder()
                .id(eventSubsId)
                .obEventSubscription1(new OBEventSubscription1()
                        .data(new OBEventSubscription1Data()
                                .callbackUrl("http://callback")
                                .version(OBVersion.v3_1_2.getCanonicalVersion())
                        ))
                .build();
        eventSubscriptionsRepository.save(frEventSubscription1);

        // When
        HttpResponse<OBEventSubscriptionResponse1> response = Unirest.put(BASE_URL + port + RESOURCE_URI+"/"+eventSubsId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventSubscription1)
                .asObject(OBEventSubscriptionResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
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
                        .version(OBVersion.v3_0.getCanonicalVersion())
                );

        // When
        HttpResponse<OBEventSubscriptionResponse1> response = Unirest.put(BASE_URL + port + RESOURCE_URI+"/"+eventSubsId)
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
        FREventSubscription1 frEventSubscription1 = FREventSubscription1.builder()
                .obEventSubscription1(new OBEventSubscription1().data(new OBEventSubscription1Data()
                .version(OBVersion.v3_1_2.getCanonicalVersion())))
                .id(eventSubsId)
                .tppId(tpp.getId())
                .build();
        eventSubscriptionsRepository.save(frEventSubscription1);

        // When
        HttpResponse response = Unirest.delete(BASE_URL + port + RESOURCE_URI+"/"+eventSubsId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(204);

        final Optional<FREventSubscription1> byId = eventSubscriptionsRepository.findById(eventSubsId);
        assertThat(byId.isPresent()).isFalse();
    }

    @Test
    public void deleteEventSubscription_notFound() {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        String eventSubsId = UUID.randomUUID().toString();

        // When
        HttpResponse response = Unirest.delete(BASE_URL + port + RESOURCE_URI+"/"+eventSubsId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);

    }
}