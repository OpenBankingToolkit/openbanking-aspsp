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
package com.forgerock.openbanking.common.services.store.metric;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class PsuMetricService {
    private static final String BASE_RESOURCE_PATH = "/api/metric/psu";

    private String rsStoreRoot;
    private RestTemplate restTemplate;

    @Autowired
    public PsuMetricService(RestTemplate restTemplate,
                            @Value("${rs-store.base-url}") String rsStoreRoot) {
        this.restTemplate = restTemplate;
        this.rsStoreRoot = rsStoreRoot;
    }

    public Integer count(DateTime fromDateTime, DateTime toDateTime) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + BASE_RESOURCE_PATH + "/count");
        builder.queryParam("fromDate", fromDateTime.toString(ISODateTimeFormat.date()));
        builder.queryParam("toDate", toDateTime.toString(ISODateTimeFormat.date()));
        URI uri = builder.build().encode().toUri();
        return restTemplate.getForObject(uri, Integer.class);
    }
}
