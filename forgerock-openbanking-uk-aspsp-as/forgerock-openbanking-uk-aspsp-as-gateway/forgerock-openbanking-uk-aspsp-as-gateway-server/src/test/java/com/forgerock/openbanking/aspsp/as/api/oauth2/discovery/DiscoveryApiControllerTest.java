/**
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

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
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
        discoveryApiController = new DiscoveryApiController(amGateway, config, "localhost", "8074", "", "443");
    }

    @Test
    public void dynamicRegistrationContainsPortWhenNot443() {
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
    public void dynamicRegistrationDoesNotContainsPortWhen443() {
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
    public void requireRequestUriRegistration_isNull_when_requestUriParameterNotSupported() {
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
    public void testCustomAPIVersion() {
        // Given
        discoveryApiController = new DiscoveryApiController(amGateway, config, "localhost", "443", "apiVersion", "443");
        MockHttpServletRequest req = new MockHttpServletRequest();
        OIDCDiscoveryResponse oidcDiscoveryResponse = JMockData.mock(OIDCDiscoveryResponse.class);
        ResponseEntity<OIDCDiscoveryResponse> response = new ResponseEntity<>(oidcDiscoveryResponse, HttpStatus.OK);
        given(amGateway.toAM(req, new HttpHeaders(), new ParameterizedTypeReference<OIDCDiscoveryResponse>() {})).willReturn(response);

        // When
        ResponseEntity<OIDCDiscoveryResponse> discovery = discoveryApiController.getDiscovery(req);

        // Then
        assertThat(discovery.getBody().getVersion()).isEqualTo("apiVersion");
    }
}