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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@Slf4j
public class DiscoveryApiController implements DiscoveryApi {

    private AMASPSPGateway amGateway;
    private String dnsHostRoot;
    private String readWriteApiVersion;
    private String clientRegistrationApiVersion;
    private String scgwPort;
    private DiscoveryConfig discoveryConfig;

    public DiscoveryApiController(AMASPSPGateway amGateway,
                                  DiscoveryConfig discoveryConfig,
                                  @Value("${dns.hosts.root}") String dnsHostRoot,
                                  @Value("${scgw.port}") String scgwPort,
                                  @Value("${rs-discovery.read-write-api.version:3.1.4}") String readWriteApiVersion,
                                  @Value("${rs-discovery.client-registration-api.version:3.2.0}") String clientRegistrationApiVersion) {
        this.amGateway = amGateway;
        this.dnsHostRoot = dnsHostRoot;
        this.scgwPort = scgwPort;
        this.discoveryConfig = discoveryConfig;
        this.readWriteApiVersion = readWriteApiVersion;
        this.clientRegistrationApiVersion = clientRegistrationApiVersion;
    }

    @Override
    public ResponseEntity<OIDCDiscoveryResponse> getDiscovery(
            HttpServletRequest request
    ) {
        String normalEndpoint = "https://as.aspsp." + dnsHostRoot;
        String matlsProtectedEndpoint = withPort("https://matls.as.aspsp." + dnsHostRoot);

        HttpHeaders additionalHttpHeaders = new HttpHeaders();
        ParameterizedTypeReference<OIDCDiscoveryResponse> ptr = new ParameterizedTypeReference<OIDCDiscoveryResponse>() {
        };
        ResponseEntity<OIDCDiscoveryResponse> responseEntity = amGateway.toAM(request, additionalHttpHeaders, ptr);
        OIDCDiscoveryResponse discoveryResponse = Objects.requireNonNull(responseEntity.getBody());
        log.debug("Discovery response received by AM: {}", discoveryResponse);

        discoveryResponse.setVersion(readWriteApiVersion);
        discoveryResponse.setReadWriteApiVersion(readWriteApiVersion);
        discoveryResponse.setClientRegistrationApiVersion(clientRegistrationApiVersion);

        //Override the well-known
        discoveryResponse.setIssuer(switchToNonMatls(discoveryResponse.getIssuer()));
        discoveryResponse.setIntrospectionEndpoint(switchToSCGWPort(switchToMatls(discoveryResponse.getIntrospectionEndpoint())));
        discoveryResponse.setUserinfoEndpoint(switchToSCGWPort(switchToMatls(discoveryResponse.getUserinfoEndpoint())));
        discoveryResponse.setTokenEndpoint(switchToSCGWPort(switchToMatls(discoveryResponse.getTokenEndpoint())));

        if (discoveryConfig.getObieJwkUri() != null && !"".equals(discoveryConfig.getObieJwkUri())) {
            discoveryResponse.setJwksUri(discoveryConfig.getObieJwkUri());
        } else {
            discoveryResponse.setJwksUri(withPort(normalEndpoint) + "/api/jwk/jwk_uri");
        }

        discoveryResponse.setAuthorizationEndpoint(withPort(normalEndpoint) + "/oauth2/authorize");

        log.debug("Supported Token Endpoint Auth Methods from AM: {} will be restricted to as-api configured values: {}", discoveryResponse.getTokenEndpointAuthMethodsSupported(), discoveryConfig.getSupportedAuthMethod());
        discoveryResponse.setTokenEndpointAuthMethodsSupported(discoveryConfig.getSupportedAuthMethod());

        log.debug("Grant Types from AM: {} will be restricted to as-api configured values: {}", discoveryResponse.getGrantTypesSupported(), discoveryConfig.getSupportedGrantTypes());
        discoveryResponse.setGrantTypesSupported(discoveryConfig.getSupportedGrantTypes());

        log.debug("Set requestUriParameterSupported={} from as-api config", discoveryConfig.isRequestUriParameterSupported());
        discoveryResponse.setRequestUriParameterSupported(discoveryConfig.isRequestUriParameterSupported());

        if (discoveryConfig.isDynamicRegistrationEnable()) {
            discoveryResponse.setRegistrationEndpoint(matlsProtectedEndpoint + "/open-banking/register/");
        } else {
            log.debug("Dynamic Registration disabled. Registration endpoint will not appear in the well-known endpoint response.");
            discoveryResponse.setRegistrationEndpoint(null);
        }
        if (discoveryConfig.isUserInfoEnable()) {
            discoveryResponse.setUserinfoEndpoint(switchToSCGWPort(switchToMatls(discoveryResponse.getUserinfoEndpoint())));
        } else {
            log.debug("User Info endpoint disabled. It will not appear in the well-known endpoint response.");
            discoveryResponse.setUserinfoEndpoint(null);
            discoveryResponse.setUserinfoEncryptionAlgValuesSupported(null);
            discoveryResponse.setUserinfoEncryptionEncValuesSupported(null);
            discoveryResponse.setUserinfoSigningAlgValuesSupported(null);
        }
        if (discoveryConfig.isIntrospectionEnable()) {
            discoveryResponse.setIntrospectionEndpoint(switchToSCGWPort(switchToMatls(discoveryResponse.getIntrospectionEndpoint())));
        } else {
            log.debug("Introspection endpoint disabled. It will not appear in the well-known endpoint response.");
            discoveryResponse.setIntrospectionEndpoint(null);
        }


        // Don't show empty fields in response
        if (CollectionUtils.isEmpty(discoveryResponse.getIdTokenSigningAlgValuesSupported())) {
            discoveryResponse.setIdTokenSigningAlgValuesSupported(null);
        }
        if (!discoveryConfig.isIdTokenEncryptionAlgValuesEnabled() || CollectionUtils.isEmpty(discoveryResponse.getIdTokenEncryptionAlgValuesSupported())) {
            discoveryResponse.setIdTokenEncryptionAlgValuesSupported(null);
        }
        if (!discoveryConfig.isIdTokenEncryptionEncValuesEnabled() || CollectionUtils.isEmpty(discoveryResponse.getIdTokenEncryptionEncValuesSupported())) {
            discoveryResponse.setIdTokenEncryptionEncValuesSupported(null);
        }

        if (CollectionUtils.isEmpty(discoveryResponse.getRequestObjectSigningAlgValuesSupported())) {
            discoveryResponse.setRequestObjectSigningAlgValuesSupported(null);
        }
        if (!discoveryConfig.isRequestObjectEncryptionAlgValuesEnabled() || CollectionUtils.isEmpty(discoveryResponse.getRequestObjectEncryptionAlgValuesSupported())) {
            discoveryResponse.setRequestObjectEncryptionAlgValuesSupported(null);
        }
        if (!discoveryConfig.isRequestObjectEncryptionEncValuesEnabled() || CollectionUtils.isEmpty(discoveryResponse.getRequestObjectEncryptionEncValuesSupported())) {
            discoveryResponse.setRequestObjectEncryptionEncValuesSupported(null);
        }
        if (CollectionUtils.isEmpty(discoveryResponse.getTokenEndpointAuthSigningAlgValuesSupported())) {
            discoveryResponse.setTokenEndpointAuthSigningAlgValuesSupported(null);
        }

        // If 'Request Uri Parameter' not active then don't show 'Require Request Uri Registration' value as it is not relevant to our API
        if (!discoveryResponse.getRequestUriParameterSupported()) {
            discoveryResponse.setRequireRequestUriRegistration(null);
        }

        log.debug("Discovery response after manipulation: {}", discoveryResponse);

        return ResponseEntity.ok(discoveryResponse);
    }

    private String switchToMatls(String endpoint) {
        return endpoint.replaceFirst("https://as.", "https://matls.as.");
    }

    private String switchToNonMatls(String endpoint) {
        return endpoint.replaceFirst("https://matls.as.", "https://as.");
    }

    private String switchToSCGWPort(String endpoint) {
        if (scgwPort.equals("443") ) {
            return endpoint.replaceFirst(":443", "");
        }
        if (endpoint.contains(":" + scgwPort)) {
            return endpoint;
        }
        if (endpoint.contains(":443")) {
            return endpoint.replaceFirst(":443", ":" + scgwPort);
        }
        return endpoint.replaceFirst(dnsHostRoot, dnsHostRoot + ":" + scgwPort);
    }

    private String withPort(String host) {
        if (scgwPort.equals("443")) {
            return host;
        }
        return host + ":" + scgwPort;
    }
}
