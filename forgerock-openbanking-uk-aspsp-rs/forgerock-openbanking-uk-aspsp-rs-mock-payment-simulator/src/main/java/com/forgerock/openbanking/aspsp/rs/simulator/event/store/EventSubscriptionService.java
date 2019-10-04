/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.simulator.event.store;

import com.forgerock.openbanking.commons.model.openbanking.v3_1_2.event.FREventSubscription1;
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
public class EventSubscriptionService {
    private static final String BASE_RESOURCE_PATH = "/api/event-subscriptions/";

    private String rsStoreRoot;
    private RestTemplate restTemplate;

    public EventSubscriptionService(@Value("${rs-store.base-url}") String rsStoreRoot, RestTemplate restTemplate) {
        this.rsStoreRoot = rsStoreRoot;
        this.restTemplate = restTemplate;
    }

    public Collection<FREventSubscription1> findByTppId(String tppId) {
        log.debug("Read all the event subscription for {}", tppId);
        ParameterizedTypeReference<Collection<FREventSubscription1>> ptr =
                new ParameterizedTypeReference<Collection<FREventSubscription1>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + BASE_RESOURCE_PATH + "search/findByTppId"
        );
        builder.queryParam("tppId", tppId);
        URI uri = builder.build().encode().toUri();
        ResponseEntity<Collection<FREventSubscription1>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);

        return entity.getBody();
    }
}
