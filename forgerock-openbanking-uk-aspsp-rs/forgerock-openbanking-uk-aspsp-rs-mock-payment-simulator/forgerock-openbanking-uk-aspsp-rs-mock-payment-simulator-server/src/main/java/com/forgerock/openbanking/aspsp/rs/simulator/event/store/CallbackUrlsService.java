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
package com.forgerock.openbanking.aspsp.rs.simulator.event.store;

import com.forgerock.openbanking.common.model.openbanking.persistence.event.FRCallbackUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@Service
@Slf4j
public class CallbackUrlsService {
    private static final String BASE_RESOURCE_PATH = "/api/callback-urls/";

    private String rsStoreRoot;
    private RestTemplate restTemplate;

    public CallbackUrlsService(@Value("${rs-store.base-url}") String rsStoreRoot, RestTemplate restTemplate) {
        this.rsStoreRoot = rsStoreRoot;
        this.restTemplate = restTemplate;
    }

    public Collection<FRCallbackUrl> findByTppId(String tppId) {
        log.debug("Read all the callback URLs");
        ParameterizedTypeReference<Collection<FRCallbackUrl>> ptr =
                new ParameterizedTypeReference<Collection<FRCallbackUrl>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + BASE_RESOURCE_PATH + "search/findByTppId"
        );
        builder.queryParam("tppId", tppId);
        URI uri = builder.build().encode().toUri();
        ResponseEntity<Collection<FRCallbackUrl>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);

        return entity.getBody();
    }
}
