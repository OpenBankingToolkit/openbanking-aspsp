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
package com.forgerock.openbanking.register.service;

import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class ManualRegistrationApplicationService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${rs-store.base-url}")
    private String rsStoreRoot;


    public Optional<ManualRegistrationApplication> findById(String id) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl
                (rsStoreRoot + "/manualRegistrationApplications/" + id);
        URI uri = builder.build().encode().toUri();
        log.debug("Find id {}", id);
        try {
            ResponseEntity<ManualRegistrationApplication> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ManualRegistrationApplication.class);
            return Optional.of(entity.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }

    public Optional<ManualRegistrationApplication> findBySoftwareClientId(String softwareClientId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl
                (rsStoreRoot + "/manualRegistrationApplications/search/findBySoftwareClientId");
        builder.queryParam("softwareClientId", softwareClientId);
        URI uri = builder.build().encode().toUri();
        log.debug("Find softwareClientId {}", softwareClientId);
        try {
            ResponseEntity<ManualRegistrationApplication> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ManualRegistrationApplication.class);
            return Optional.of(entity.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }

    public Collection<ManualRegistrationApplication> getAllApplications(String userId) {
        ParameterizedTypeReference<Resources<ManualRegistrationApplication>> ptr = new ParameterizedTypeReference<Resources<ManualRegistrationApplication>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/manualRegistrationApplications/search/findByUserId");
        builder.queryParam("userId", userId);
        URI uri = builder.build().encode().toUri();
        return restTemplate.exchange(
                uri, HttpMethod.GET, null, ptr).getBody().getContent();
    }

    public ManualRegistrationApplication createApplication(ManualRegistrationApplication application) {
        HttpEntity<ManualRegistrationApplication> request = new HttpEntity<>(application, new HttpHeaders());
        return restTemplate.postForObject(rsStoreRoot + "/manualRegistrationApplications/", request, ManualRegistrationApplication.class);
    }

    public Long deleteApplication(ManualRegistrationApplication application) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl
                (rsStoreRoot + "/manualRegistrationApplications/" + application.getId());
        URI uri = builder.build().encode().toUri();
        log.debug("Delete application", application);
        return restTemplate.exchange(uri, HttpMethod.DELETE, null, Long.class).getBody();
    }
}
