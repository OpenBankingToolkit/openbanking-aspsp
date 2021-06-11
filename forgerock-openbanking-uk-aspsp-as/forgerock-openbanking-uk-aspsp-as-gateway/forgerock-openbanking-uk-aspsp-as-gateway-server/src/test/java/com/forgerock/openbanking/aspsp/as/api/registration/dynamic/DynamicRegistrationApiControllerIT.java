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
package com.forgerock.openbanking.aspsp.as.api.registration.dynamic;

import com.forgerock.openbanking.aspsp.as.ForgerockOpenbankingAsApiApplication;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.model.Tpp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;

/**
 * SpringBoot integration test for {@link DynamicRegistrationApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = ForgerockOpenbankingAsApiApplication.class)
@ActiveProfiles("test")
public class DynamicRegistrationApiControllerIT {

    private static final String REGISTRATION_PATH = "/open-banking/register/";

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private TppStoreService tppStoreService;

    @Test
    public void shouldUnregister() {
        // Given
        String clientId = "DemoTPP";
        String url = unregisterUrl(clientId);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders());
        Tpp tpp = mock(Tpp.class);
        given(tpp.getClientId()).willReturn(clientId);
        given(tppStoreService.findByClientId(clientId)).willReturn(Optional.of(tpp));

        // When
        ResponseEntity<Object> response = restTemplate.exchange(url, DELETE, request, Object.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "");
        return headers;
    }

    private String unregisterUrl(String clientId) {
        return "http://localhost:" + port + REGISTRATION_PATH + "/" + clientId;
    }

}