/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.simulator.event.store;

import com.forgerock.openbanking.common.model.openbanking.forgerock.event.FREventNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AggregatedPollingService {
    private static final String BASE_RESOURCE_PATH = "/api/aggregated-polling";

    private String rsStoreRoot;
    private RestTemplate restTemplate;

    public AggregatedPollingService(@Value("${rs-store.base-url}") String rsStoreRoot, RestTemplate restTemplate) {
        this.rsStoreRoot = rsStoreRoot;
        this.restTemplate = restTemplate;
    }

    public FREventNotification createPendingEventNotification(FREventNotification eventNotification) {
        log.debug("Create an pending event notification {} on {}", eventNotification, rsStoreRoot + BASE_RESOURCE_PATH);
        return restTemplate.postForObject(rsStoreRoot + BASE_RESOURCE_PATH, eventNotification, FREventNotification.class);
    }
}
