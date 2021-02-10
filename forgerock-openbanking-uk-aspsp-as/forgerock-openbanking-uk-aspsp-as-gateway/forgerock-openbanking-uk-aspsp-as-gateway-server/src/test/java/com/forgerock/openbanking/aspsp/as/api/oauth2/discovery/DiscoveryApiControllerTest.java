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
package com.forgerock.openbanking.aspsp.as.api.oauth2.discovery;

import com.forgerock.openbanking.am.gateway.AMASPSPGateway;
import com.forgerock.openbanking.common.model.as.discovery.OIDCDiscoveryResponse;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.github.jsonzou.jmockdata.JMockData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DiscoveryApiControllerTest {

    @Mock
    private AMASPSPGateway amGateway;
    @Mock
    private DiscoveryConfig config;
    @InjectMocks
    private DiscoveryApiController discoveryApiController;

    @Before
    public void setUp() {
        given(config.isDynamicRegistrationEnable()).willReturn(true);
        discoveryApiController = new DiscoveryApiController(amGateway, config, "localhost", "8074", "3.1.2", "3.2");
    }

    @Test
    public void dynamicRegistrationContainsPortWhenNot443() throws OBErrorResponseException {
        // Given
        MockHttpServletRequest req = new MockHttpServletRequest();
        OIDCDiscoveryResponse oidcDiscoveryResponse = JMockData.mock(OIDCDiscoveryResponse.class);
        ResponseEntity<OIDCDiscoveryResponse> response = new ResponseEntity<>(oidcDiscoveryResponse, HttpStatus.OK);
        given(amGateway.toAM(req, new HttpHeaders(), new ParameterizedTypeReference<OIDCDiscoveryResponse>() {})).willReturn(response);

        // When
        ResponseEntity<OIDCDiscoveryResponse> discovery = discoveryApiController.getDiscovery(req);

        // Then
        assertThat(discovery.getBody().getRegistrationEndpoint()).isEqualTo("https://matls.as.aspsp.localhost:8074/open-banking/register/");
    }

    @Test
    public void dynamicRegistrationDoesNotContainsPortWhen443() throws OBErrorResponseException {
        // Given
        discoveryApiController = new DiscoveryApiController(amGateway, config, "localhost", "443", "", "443");
        MockHttpServletRequest req = new MockHttpServletRequest();
        OIDCDiscoveryResponse oidcDiscoveryResponse = JMockData.mock(OIDCDiscoveryResponse.class);
        ResponseEntity<OIDCDiscoveryResponse> response = new ResponseEntity<>(oidcDiscoveryResponse, HttpStatus.OK);
        given(amGateway.toAM(req, new HttpHeaders(), new ParameterizedTypeReference<OIDCDiscoveryResponse>() {})).willReturn(response);

        // When
        ResponseEntity<OIDCDiscoveryResponse> discovery = discoveryApiController.getDiscovery(req);

        // Then
        assertThat(discovery.getBody().getRegistrationEndpoint()).isEqualTo("https://matls.as.aspsp.localhost/open-banking/register/");
    }

    @Test
    public void requireRequestUriRegistration_isNull_when_requestUriParameterNotSupported() throws OBErrorResponseException {
        // Given
        MockHttpServletRequest req = new MockHttpServletRequest();
        OIDCDiscoveryResponse oidcDiscoveryResponse = JMockData.mock(OIDCDiscoveryResponse.class);
        oidcDiscoveryResponse.setRequireRequestUriRegistration(true);
        oidcDiscoveryResponse.setRequestUriParameterSupported(false);
        ResponseEntity<OIDCDiscoveryResponse> response = new ResponseEntity<>(oidcDiscoveryResponse, HttpStatus.OK);
        given(amGateway.toAM(req, new HttpHeaders(), new ParameterizedTypeReference<OIDCDiscoveryResponse>() {})).willReturn(response);

        // When
        ResponseEntity<OIDCDiscoveryResponse> discovery = discoveryApiController.getDiscovery(req);

        // Then
        OIDCDiscoveryResponse respBody = Objects.requireNonNull(discovery.getBody());
        assertThat(respBody.getRequestUriParameterSupported()).isFalse();
        assertThat(respBody.getRequireRequestUriRegistration()).isNull();
    }

    @Test
    public void testCustomAPIVersion() throws OBErrorResponseException {
        // Given
        discoveryApiController = new DiscoveryApiController(amGateway, config, "localhost", "8074", "RWApiVersion", "CRApiVersion");
        MockHttpServletRequest req = new MockHttpServletRequest();
        OIDCDiscoveryResponse oidcDiscoveryResponse = JMockData.mock(OIDCDiscoveryResponse.class);
        ResponseEntity<OIDCDiscoveryResponse> response = new ResponseEntity<>(oidcDiscoveryResponse, HttpStatus.OK);
        given(amGateway.toAM(req, new HttpHeaders(), new ParameterizedTypeReference<OIDCDiscoveryResponse>() {})).willReturn(response);

        // When
        ResponseEntity<OIDCDiscoveryResponse> discovery = discoveryApiController.getDiscovery(req);

        // Then
        assertThat(discovery.getBody().getReadWriteApiVersion()).isEqualTo("RWApiVersion");
        assertThat(discovery.getBody().getClientRegistrationApiVersion()).isEqualTo("CRApiVersion");
    }

    @Test
    public void testResponseTypesSupported() throws OBErrorResponseException {
        // Given
        List<String> responseTypes = List.of("code", "id_token", "code id_token");
        given(config.getSupportedResponseTypes()).willReturn(responseTypes);
        discoveryApiController = new DiscoveryApiController(amGateway, config, "localhost", "", "", "");
        MockHttpServletRequest req = new MockHttpServletRequest();
        OIDCDiscoveryResponse oidcDiscoveryResponse = JMockData.mock(OIDCDiscoveryResponse.class);
        List<String> amResponseTypes = new ArrayList<>();
        amResponseTypes.addAll(responseTypes);
        amResponseTypes.add("device_code");
        amResponseTypes.add("device_code code id_token");
        oidcDiscoveryResponse.setResponseTypesSupported(amResponseTypes);
        ResponseEntity<OIDCDiscoveryResponse> response = new ResponseEntity<>(oidcDiscoveryResponse, HttpStatus.OK);
        given(amGateway.toAM(req, new HttpHeaders(), new ParameterizedTypeReference<OIDCDiscoveryResponse>() {})).willReturn(response);

        // When
        ResponseEntity<OIDCDiscoveryResponse> discovery = discoveryApiController.getDiscovery(req);

        // Then
        assertThat(discovery.getBody().getResponseTypesSupported()).isEqualTo(responseTypes);
    }

    @Test
    public void testResponseTypeSupported() throws OBErrorResponseException {
        // Given
        List<String> responseTypes = List.of("code id_token");
        given(config.getSupportedResponseTypes()).willReturn(responseTypes);
        discoveryApiController = new DiscoveryApiController(amGateway, config, "localhost", "", "", "");
        MockHttpServletRequest req = new MockHttpServletRequest();
        OIDCDiscoveryResponse oidcDiscoveryResponse = JMockData.mock(OIDCDiscoveryResponse.class);
        List<String> amResponseTypes = new ArrayList<>();
        amResponseTypes.addAll(responseTypes);
        amResponseTypes.add("device_code");
        amResponseTypes.add("device_code code id_token");
        oidcDiscoveryResponse.setResponseTypesSupported(amResponseTypes);
        ResponseEntity<OIDCDiscoveryResponse> response = new ResponseEntity<>(oidcDiscoveryResponse, HttpStatus.OK);
        given(amGateway.toAM(req, new HttpHeaders(), new ParameterizedTypeReference<OIDCDiscoveryResponse>() {})).willReturn(response);

        // When
        ResponseEntity<OIDCDiscoveryResponse> discovery = discoveryApiController.getDiscovery(req);

        // Then
        assertThat(discovery.getBody().getResponseTypesSupported()).isEqualTo(responseTypes);
    }


    @Test
    public void testResponseTypesSupported_dont_match() {
        // Given
        List<String> responseTypes = List.of("code", "id_token", "code id_token");
        given(config.getSupportedResponseTypes()).willReturn(responseTypes);
        discoveryApiController = new DiscoveryApiController(amGateway, config, "localhost", "", "", "");
        MockHttpServletRequest req = new MockHttpServletRequest();
        OIDCDiscoveryResponse oidcDiscoveryResponse = JMockData.mock(OIDCDiscoveryResponse.class);
        List<String> amResponseTypes = new ArrayList<>();
        amResponseTypes.addAll(responseTypes.subList(0,1));
        amResponseTypes.add("device_code");
        amResponseTypes.add("device_code code id_token");
        oidcDiscoveryResponse.setResponseTypesSupported(amResponseTypes);
        ResponseEntity<OIDCDiscoveryResponse> response = new ResponseEntity<>(oidcDiscoveryResponse, HttpStatus.OK);
        given(amGateway.toAM(req, new HttpHeaders(), new ParameterizedTypeReference<OIDCDiscoveryResponse>() {})).willReturn(response);

        // When
        OBErrorResponseException e = catchThrowableOfType(() -> discoveryApiController.getDiscovery(req), OBErrorResponseException.class);

        // Then
        assertThat(e.getErrors().get(0).getMessage()).isEqualTo("The response types supported by the authorisation server '" + amResponseTypes + "' don't match with the response types supported '" + responseTypes + "' by as-api");
        assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testResponseTypeSupported_dont_match() {
        // Given
        List<String> responseTypes = List.of("code id_token");
        given(config.getSupportedResponseTypes()).willReturn(responseTypes);
        discoveryApiController = new DiscoveryApiController(amGateway, config, "localhost", "", "", "");
        MockHttpServletRequest req = new MockHttpServletRequest();
        OIDCDiscoveryResponse oidcDiscoveryResponse = JMockData.mock(OIDCDiscoveryResponse.class);
        List<String> amResponseTypes = List.of("device_code", "device_code code id_token");
        oidcDiscoveryResponse.setResponseTypesSupported(amResponseTypes);
        ResponseEntity<OIDCDiscoveryResponse> response = new ResponseEntity<>(oidcDiscoveryResponse, HttpStatus.OK);
        given(amGateway.toAM(req, new HttpHeaders(), new ParameterizedTypeReference<OIDCDiscoveryResponse>() {})).willReturn(response);

        // When
        OBErrorResponseException e = catchThrowableOfType(() -> discoveryApiController.getDiscovery(req), OBErrorResponseException.class);

        // Then
        assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(e.getErrors().get(0).getMessage()).isEqualTo("The response types supported by the authorisation server '" + amResponseTypes + "' don't match with the response types supported '" + responseTypes + "' by as-api");
    }
}
