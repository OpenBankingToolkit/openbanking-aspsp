/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.aspsp;

import com.forgerock.openbanking.common.model.as.discovery.OIDCDiscoveryResponse;
import com.forgerock.openbanking.constants.OIDCConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class ASDiscoveryService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ASDiscoveryService.class);
    @Autowired
    private RestTemplate restTemplate;

    public OIDCDiscoveryResponse discovery(String oidcRootEndpoint) {
        String wellKnownEndpoint = oidcRootEndpoint + OIDCConstants.Endpoint.WELL_KNOWN;
        LOGGER.debug("Call the well-known endpoint of the AS {}", wellKnownEndpoint);

        ParameterizedTypeReference<OIDCDiscoveryResponse> ptr = new ParameterizedTypeReference<OIDCDiscoveryResponse>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(wellKnownEndpoint);
        URI uri = builder.build().encode().toUri();

        ResponseEntity<OIDCDiscoveryResponse> entity = restTemplate.exchange(
                uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }
}
