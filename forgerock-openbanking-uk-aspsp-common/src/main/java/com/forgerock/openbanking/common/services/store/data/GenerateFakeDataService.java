/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class GenerateFakeDataService {
    private final static Logger LOGGER = LoggerFactory.getLogger(GenerateFakeDataService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Value("${rs-store.base-url}")
    private String rsStoreRoot;

    public void generateFakeData(String username) {
        LOGGER.debug("Generate fake data for new user {}", username);

        HttpEntity request = new HttpEntity<>(username, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/v2.0/generateData");
        builder.queryParam("userId", username);
        builder.queryParam("username", username);
        URI uri = builder.build().encode().toUri();
        //TODO get the endpoint from the configuration
        restTemplate.exchange(uri, HttpMethod.POST, request, Void.class);
    }
}
