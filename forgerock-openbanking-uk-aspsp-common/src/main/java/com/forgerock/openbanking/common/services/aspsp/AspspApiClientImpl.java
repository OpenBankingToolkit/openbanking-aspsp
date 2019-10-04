/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
