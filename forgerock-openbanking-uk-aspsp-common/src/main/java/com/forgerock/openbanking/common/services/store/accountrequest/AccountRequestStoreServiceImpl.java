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
package com.forgerock.openbanking.common.services.store.accountrequest;

import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountRequest;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccountRequest;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccountAccessConsent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class AccountRequestStoreServiceImpl implements AccountRequestStoreService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRequestStoreService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Value("${rs-store.base-url}")
    private String rsStoreRoot;

    @Override
    public Optional get(String consentId) {
        switch (IntentType.identify(consentId)) {
            case ACCOUNT_REQUEST:
                return getAccountRequest(consentId);
            case ACCOUNT_ACCESS_CONSENT:
                return getAccountAccessConsent(consentId);
            case CUSTOMER_INFO_CONSENT:
                return getCustomerInfoAccountAccessConsent(consentId);
            default:
                throw new IllegalArgumentException("Consent ID '" + consentId + "' doesn't correspond to an account access");
        }
    }

    @Override
    public void save(AccountRequest accountRequest) {
        switch (IntentType.identify(accountRequest.getId())) {
            case ACCOUNT_REQUEST:
                saveAccountRequest(accountRequest);
                break;
            case ACCOUNT_ACCESS_CONSENT:
                saveAccountAccessConsent((FRAccountAccessConsent) accountRequest);
                break;
            case CUSTOMER_INFO_CONSENT:
                saveCustomerInfoAccountAccessConsent((FRAccountAccessConsent) accountRequest);
                break;
            default:
                throw new IllegalArgumentException("Consent ID '" + accountRequest.getId() + "' doesn't correspond to an account access");
        }
    }

    private Optional<FRAccountRequest> getAccountRequest(String accountRequestId) {
        ParameterizedTypeReference<Optional<FRAccountRequest>> ptr = new ParameterizedTypeReference<Optional<FRAccountRequest>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/account-requests/" + accountRequestId);
        URI uri = builder.build().encode().toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, null, ptr).getBody();
    }

    private Optional<FRAccountAccessConsent> getAccountAccessConsent(String consentId) {
        ParameterizedTypeReference<Optional<FRAccountAccessConsent>> ptr = new ParameterizedTypeReference<Optional<FRAccountAccessConsent>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/account-access-consents/" + consentId);
        URI uri = builder.build().encode().toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, null, ptr).getBody();
    }

    private Optional<FRAccountAccessConsent> getCustomerInfoAccountAccessConsent(String consentId) {
        return getAccountAccessConsent(consentId);
    }

    private void saveAccountRequest(AccountRequest accountRequest) {
        HttpEntity<AccountRequest> request = new HttpEntity<>(accountRequest, new HttpHeaders());
        restTemplate.exchange(rsStoreRoot + "/api/account-requests/", HttpMethod.PUT,
                request, Void.class);
    }

    private void saveAccountAccessConsent(FRAccountAccessConsent accountAccessConsent1) {
        HttpEntity<AccountRequest> request = new HttpEntity<>(accountAccessConsent1, new HttpHeaders());
        try {
            restTemplate.exchange(rsStoreRoot + "/api/account-access-consents/", HttpMethod.PUT,
                    request, Void.class);
        } catch (HttpClientErrorException e) {
            LOGGER.debug("Error: {}", e.getResponseBodyAsString(), e);
            throw e;
        }
    }

    private void saveCustomerInfoAccountAccessConsent(FRAccountAccessConsent accountAccessConsent1) {
        saveAccountAccessConsent(accountAccessConsent1);
    }
}
