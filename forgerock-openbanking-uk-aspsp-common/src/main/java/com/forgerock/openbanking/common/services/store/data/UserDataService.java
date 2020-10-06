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
package com.forgerock.openbanking.common.services.store.data;

import com.forgerock.openbanking.common.conf.data.DataConfigurationProperties;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.data.FRUserData;
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

    public FRUserData exportUserData(String userId) {
        LOGGER.debug("Export data for user {}", userId);

        HttpEntity request = new HttpEntity<>(null, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/data/user");
        builder.queryParam("userId", userId);
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.GET, request, FRUserData.class).getBody();
    }

    public FRUserData createUserData(FRUserData userData) {
        LOGGER.debug("Create data for user {}", userData);

        HttpEntity request = new HttpEntity<>(userData, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/data/user");

        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.POST, request, FRUserData.class).getBody();
    }

    public FRUserData updateUserData(FRUserData userData) {
        LOGGER.debug("Update data for w user {}", userData);

        HttpEntity request = new HttpEntity<>(userData, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/data/user");

        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.PUT, request, FRUserData.class).getBody();
    }

    public boolean deleteUserData(String userId) {
        LOGGER.debug("Delete data for user {}", userId);

        HttpEntity request = new HttpEntity<>(null, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/data/user");
        builder.queryParam("userId", userId);
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.DELETE, request, Boolean.class).getBody();
    }

    public FRUserData generateUserData(String userId) {
        return generateUserData(userId.toLowerCase(), dataConfig.getDefaultProfile());
    }

    public FRUserData generateUserData(String userId, String profile) {
        LOGGER.debug("Generate data for user {}", userId);

        HttpEntity request = new HttpEntity<>(null, new HttpHeaders());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/api/fake-data/generate");
        builder.queryParam("userId", userId);
        builder.queryParam("username", userId);
        builder.queryParam("profile", profile);
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(uri, HttpMethod.POST, request, FRUserData.class).getBody();
    }
}
