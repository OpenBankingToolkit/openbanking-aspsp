/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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