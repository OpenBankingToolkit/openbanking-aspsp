/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.data;

import com.forgerock.openbanking.common.conf.data.DataConfigurationProperties;
import com.forgerock.openbanking.common.model.openbanking.v3_0.account.data.FRUserData3;
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
public class UserDataService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserDataService.class);
    @Autowired
    private DataConfigurationProperties dataConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${rs-store.base-url}")
    private String rsStoreRoot;

    public Boolean hasData(String userId) {
        LOGGER.debug("Export data for user {}", userId);

        HttpEntity request = new HttpEntity<>(null, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/data/user/has-data");
        builder.queryParam("userId", userId);
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.GET, request, Boolean.class).getBody();
    }

    public FRUserData3 exportUserData(String userId) {
        LOGGER.debug("Export data for user {}", userId);

        HttpEntity request = new HttpEntity<>(null, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/data/user");
        builder.queryParam("userId", userId);
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.GET, request, FRUserData3.class).getBody();
    }

    public FRUserData3 createUserData(FRUserData3 userData) {
        LOGGER.debug("Create data for user {}", userData);

        HttpEntity request = new HttpEntity<>(userData, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/data/user");

        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.POST, request, FRUserData3.class).getBody();
    }

    public FRUserData3 updateUserData(FRUserData3 userData) {
        LOGGER.debug("Update data for w user {}", userData);

        HttpEntity request = new HttpEntity<>(userData, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/data/user");

        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.PUT, request, FRUserData3.class).getBody();
    }

    public boolean deleteUserData(String userId) {
        LOGGER.debug("Delete data for user {}", userId);

        HttpEntity request = new HttpEntity<>(null, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/data/user");
        builder.queryParam("userId", userId);
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.DELETE, request, Boolean.class).getBody();
    }

    public FRUserData3 generateUserData(String userId) {
        return generateUserData(userId.toLowerCase(), dataConfig.getDefaultProfile());
    }

    public FRUserData3 generateUserData(String userId, String profile) {
        LOGGER.debug("Generate data for user {}", userId);

        HttpEntity request = new HttpEntity<>(null, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/fake-data/generate");
        builder.queryParam("userId", userId);
        builder.queryParam("username", userId);
        builder.queryParam("profile", profile);
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.POST, request, FRUserData3.class).getBody();
    }
}
