/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
