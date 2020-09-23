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
package com.forgerock.openbanking.common.services.store.account;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v2_0.FRAccount2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AccountStoreServiceImpl implements AccountStoreService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${rs-store.base-url}")
    private String rsStoreRoot;

    @Override
    public FRAccount2 getAccount(String accountId) {
        ParameterizedTypeReference<FRAccount2> ptr = new ParameterizedTypeReference<FRAccount2>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/accounts/" + accountId);


        URI uri = builder.build().encode().toUri();
        ResponseEntity<FRAccount2> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }

    @Override
    public Optional findAccountByIdentification(String identification) {
        ParameterizedTypeReference<Optional<FRAccount2>> ptr = new ParameterizedTypeReference<Optional<FRAccount2>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/accounts/search/findByIdentification");
        builder.queryParam("identification", identification);

        URI uri = builder.build().encode().toUri();
        try {
            ResponseEntity<Optional<FRAccount2>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
            return entity.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }

    @Override
    public List<FRAccount2> get(String userID) {
        // This is necessary as auth server always uses lowercase user id
        String lowercaseUserId = userID.toLowerCase();
        log.debug("Searching for accounts with user ID: {}", lowercaseUserId);

        ParameterizedTypeReference<List<FRAccount2>> ptr = new ParameterizedTypeReference<List<FRAccount2>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/accounts/search/findByUserId");
        builder.queryParam("userId", lowercaseUserId);

        URI uri = builder.build().encode().toUri();
        ResponseEntity<List<FRAccount2>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }

    @Override
    public List<AccountWithBalance> getAccountWithBalances(String userID) {
        // This is necessary as auth server always uses lowercase user id
        String lowercaseUserId = userID.toLowerCase();
        log.debug("Searching for accounts with user ID: {}", lowercaseUserId);

        ParameterizedTypeReference<List<AccountWithBalance>> ptr = new ParameterizedTypeReference<List<AccountWithBalance>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/accounts/search/findByUserId");
        builder.queryParam("userId", lowercaseUserId);
        builder.queryParam("withBalance", true);

        URI uri = builder.build().encode().toUri();
        ResponseEntity<List<AccountWithBalance>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }
}
