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
package com.forgerock.openbanking.common.services.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Service
public class RsStoreGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsStoreGateway.class);

    private RestTemplate restTemplate;
    private String rsStoreRoot;

    public RsStoreGateway(RestTemplate restTemplate, @Value("${rs-store.base-url}") String rsStoreRoot) {
        this.restTemplate = restTemplate;
        this.rsStoreRoot = rsStoreRoot;
    }

    public ResponseEntity toRsStore(HttpServletRequest httpServletRequest, HttpHeaders additionalHttpHeaders, Class<?> type) {
        return toRsStore(httpServletRequest, additionalHttpHeaders, Collections.emptyMap(), type, null);
    }

    public ResponseEntity toRsStore(HttpServletRequest httpServletRequest, HttpHeaders additionalHttpHeaders, Map<String, String> additionalParams, Class<?> type) {
        return toRsStore(httpServletRequest, additionalHttpHeaders, additionalParams, type, null);
    }

    public ResponseEntity toRsStore(HttpServletRequest httpServletRequest, HttpHeaders additionalHttpHeaders, Map<String, String> additionalParams, Class<?> type, Object body) {
        ServletServerHttpRequest request = new ServletServerHttpRequest(httpServletRequest);
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpRequest(request)
                    .uri(new URI(rsStoreRoot));

            HttpHeaders httpHeaders = request.getHeaders();
            additionalHttpHeaders.forEach(httpHeaders::addAll);
            HttpEntity httpEntity = new HttpEntity(body, httpHeaders);

            additionalParams.entrySet().stream().filter(e -> Objects.nonNull(e.getValue())).forEach(e -> builder.replaceQueryParam(e.getKey(), e.getValue()));
            URI uri = builder.build().encode().toUri();

            try {
                return restTemplate.exchange(uri, request.getMethod(), httpEntity, type);
                // HttpClient errors will be handled in CustomRestExceptionHandler
            } catch (HttpClientErrorException e) {
                return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
            } catch (HttpServerErrorException e) {
                if (e.getStatusCode()==HttpStatus.NOT_IMPLEMENTED) {
                    // Don't raise Exceptions for 'Not Implemented' as this is legitimate for backend services and should be passed on to client
                    return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
                }
                LOGGER.error("Can't proxy to rs store: {}", e.getResponseBodyAsString(), e);
                LOGGER.debug("URI: {}, Method: {}, Headers: {}, Body: {}, Response Type: {}", uri, request.getMethod(),  httpEntity.getHeaders(), httpEntity.getBody(), type);
                throw e;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("RS store root path is not a URI", e);
        }
    }
}
