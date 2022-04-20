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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1_9.aggregatedpolling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.events.FRPendingEventsRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FREventNotification;
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
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.event.OBEventPolling1;
import uk.org.openbanking.datamodel.event.OBEventPolling1SetErrs;
import uk.org.openbanking.datamodel.event.OBEventPollingResponse1;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class AggregatedPollingApiControllerIT {
    private static final String RESOURCE_URI = "/open-banking/v3.1.9/events";
    private static final String BASE_URL = "https://rs-store:";

    @LocalServerPort
    private int port;

    @Autowired
    private FRPendingEventsRepository frPendingEventsRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;
    @Autowired
    private SpringSecForTest springSecForTest;

    @MockBean
    private TppRepository tppRepository;

    private String clientId;

    private Tpp tpp;

    @Before
    public void setUp() {
        tpp = new Tpp();
        tpp.setId(UUID.randomUUID().toString());
        given(tppRepository.findByClientId(any())).willReturn(tpp);

        clientId = UUID.randomUUID().toString();
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);

        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
    }

    @Test
    public void pollEvents_noEvents_emptySuccessResponse() {
        // Given
        OBEventPolling1 obEventPolling = new OBEventPolling1()
                .maxEvents(null) // All
                .returnImmediately(true);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventPolling)
                .asObject(OBEventPollingResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getSets().isEmpty()).isTrue();
    }

    @Test
    public void pollEvents_twoEvents_allRequested() {
        // Given
        OBEventPolling1 obEventPolling = new OBEventPolling1()
                .maxEvents(null) // All
                .returnImmediately(true);
        FREventNotification frEventNotification1 = FREventNotification.builder()
                .jti("jti1")
                .signedJwt("e23239709rdg790")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification1);
        FREventNotification frEventNotification2 = FREventNotification.builder()
                .jti("jti2")
                .signedJwt("o78o7tefkwjg")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification2);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventPolling)
                .asObject(OBEventPollingResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().isMoreAvailable()).isFalse();
        assertThat(response.getBody().getSets().size()).isEqualTo(2);
    }

    @Test
    public void pollEvents_twoEvents_twoRequested() {
        // Given
        OBEventPolling1 obEventPolling = new OBEventPolling1()
                .maxEvents(2);
        FREventNotification frEventNotification1 = FREventNotification.builder()
                .jti("jti1")
                .signedJwt("e23239709rdg790")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification1);
        FREventNotification frEventNotification2 = FREventNotification.builder()
                .jti("jti2")
                .signedJwt("o78o7tefkwjg")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification2);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventPolling)
                .asObject(OBEventPollingResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().isMoreAvailable()).isFalse();
        assertThat(response.getBody().getSets().size()).isEqualTo(2);
    }

    @Test
    public void pollEvents_oneEvents_twoRequested_moreAvailable() {
        // Given
        OBEventPolling1 obEventPolling = new OBEventPolling1()
                .maxEvents(1);
        FREventNotification frEventNotification1 = FREventNotification.builder()
                .jti("jti1")
                .signedJwt("e23239709rdg790")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification1);
        FREventNotification frEventNotification2 = FREventNotification.builder()
                .jti("jti2")
                .signedJwt("o78o7tefkwjg")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification2);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventPolling)
                .asObject(OBEventPollingResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().isMoreAvailable()).isTrue();
        assertThat(response.getBody().getSets().size()).isEqualTo(1);
    }

    @Test
    public void pollEvents_ackExistingEvent_requestOneMore() {
        OBEventPolling1 obEventPolling = new OBEventPolling1()
                .maxEvents(1)
                .ack(Collections.singletonList("jti1"));
        FREventNotification frEventNotification1 = FREventNotification.builder()
                .jti("jti1")
                .signedJwt("e23239709rdg790")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification1);
        FREventNotification frEventNotification2 = FREventNotification.builder()
                .jti("jti2")
                .signedJwt("o78o7tefkwjg")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification2);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventPolling)
                .asObject(OBEventPollingResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().isMoreAvailable()).isFalse();
        assertThat(response.getBody().getSets().size()).isEqualTo(1);

        // Check acknowledged is deleted
        assertThat(frPendingEventsRepository.findByTppIdAndJti(tpp.getId(), frEventNotification1.getJti()).isEmpty()).isTrue();
    }

    @Test
    public void pollEvents_ackExistingEvent_requestNone() {
        OBEventPolling1 obEventPolling = new OBEventPolling1()
                .maxEvents(0)
                .ack(Collections.singletonList("jti1"));
        FREventNotification frEventNotification1 = FREventNotification.builder()
                .jti("jti1")
                .signedJwt("e23239709rdg790")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification1);
        FREventNotification frEventNotification2 = FREventNotification.builder()
                .jti("jti2")
                .signedJwt("o78o7tefkwjg")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification2);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventPolling)
                .asObject(OBEventPollingResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getSets().isEmpty()).isTrue();

        // Check acknowledged is deleted
        assertThat(frPendingEventsRepository.findByTppIdAndJti(tpp.getId(), frEventNotification1.getJti()).isEmpty()).isTrue();
    }

    @Test
    public void pollEvents_ackExistingEvent_errorExistingEvent_requestNone() {
        OBEventPolling1 obEventPolling = new OBEventPolling1()
                .maxEvents(0)
                .ack(Collections.singletonList("jti1"))
                .setErrs(Collections.singletonMap("jti2", new OBEventPolling1SetErrs().err("err1").description("Error")));
        FREventNotification frEventNotification1 = FREventNotification.builder()
                .jti("jti1")
                .signedJwt("e23239709rdg790")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification1);
        FREventNotification frEventNotification2 = FREventNotification.builder()
                .jti("jti2")
                .signedJwt("o78o7tefkwjg")
                .tppId(tpp.getId())
                .build();
        frPendingEventsRepository.save(frEventNotification2);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventPolling)
                .asObject(OBEventPollingResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getSets().isEmpty()).isTrue();

        // Check acknowledged event is deleted and errored event is stored with error message
        assertThat(frPendingEventsRepository.findByTppIdAndJti(tpp.getId(), frEventNotification1.getJti()).isEmpty()).isTrue();
        assertThat(frPendingEventsRepository.findByTppIdAndJti(tpp.getId(), frEventNotification2.getJti())
                .orElseThrow(AssertionError::new)
                .getErrors().getError()).isEqualTo("err1");
    }

    @Test
    public void pollEvents_ackEventThatDoesNotExist() {
        OBEventPolling1 obEventPolling = new OBEventPolling1()
                .maxEvents(0)
                .ack(Collections.singletonList("jti1"));

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post(BASE_URL + port + RESOURCE_URI)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obEventPolling)
                .asObject(OBEventPollingResponse1.class);
        log.debug("Response: {} {} , {}", response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

}
