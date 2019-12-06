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
package com.forgerock.openbanking.common.services.aspsp;

import com.forgerock.openbanking.core.services.ApplicationApiClientImpl;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class AspspApiClientImpl implements AspspApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationApiClientImpl.class);

    @Override
    public String testMatls(RestTemplate restTemplate, String testMatlsEndpoint) {
        ParameterizedTypeReference<String> ptr = new ParameterizedTypeReference<String>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(testMatlsEndpoint);
        URI uri = builder.build().encode().toUri();

        LOGGER.debug("Test matls on endpoint {}", testMatlsEndpoint);
        ResponseEntity<String> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }

    @Override
    public OIDCRegistrationResponse getOnboardingResult(RestTemplate restTemplate, String onboardingEndpoint) {
        ParameterizedTypeReference<OIDCRegistrationResponse> ptr = new ParameterizedTypeReference<OIDCRegistrationResponse>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(onboardingEndpoint);
        URI uri = builder.build().encode().toUri();

        LOGGER.debug("Get onboard result from enpoint {}", onboardingEndpoint);
        return restTemplate.exchange(uri, HttpMethod.GET, null, ptr).getBody();
    }

    @Override
    public OIDCRegistrationResponse onboard(RestTemplate restTemplate, String onboardingEndpoint, String registrationRequestJwtSerialised) {
        ParameterizedTypeReference<OIDCRegistrationResponse> ptr = new ParameterizedTypeReference<OIDCRegistrationResponse>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(onboardingEndpoint);
        URI uri = builder.build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/jwt"));

        HttpEntity<String> request = new HttpEntity<>(registrationRequestJwtSerialised, headers);

        LOGGER.debug("Onboard software state on endpoint {}", onboardingEndpoint);
        return restTemplate.exchange(uri, HttpMethod.POST, request, ptr).getBody();
    }

    @Override
    public void offBoard(RestTemplate restTemplate, String onboardingEndpoint) {
        ParameterizedTypeReference<Void> ptr = new ParameterizedTypeReference<Void>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(onboardingEndpoint);
        URI uri = builder.build().encode().toUri();

        LOGGER.debug("un-onboard software statement {}", onboardingEndpoint);
        restTemplate.exchange(uri, HttpMethod.DELETE, null, ptr);
    }
}
