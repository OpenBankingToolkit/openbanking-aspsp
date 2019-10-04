/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.account.standingorder;

import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRStandingOrder5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.org.openbanking.datamodel.account.OBStandingOrder5;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class StandingOrderService {
    private static final String BASE_RESOURCE_PATH = "/api/accounts/standing-orders/";

    private String rsStoreRoot;
    private RestTemplate restTemplate;

    @Autowired
    public StandingOrderService(RestTemplate restTemplate,
                                @Value("${rs-store.base-url}") String rsStoreRoot) {
        this.restTemplate = restTemplate;
        this.rsStoreRoot = rsStoreRoot;
    }

    public FRStandingOrder5 createStandingOrder(OBStandingOrder5 standingOrder, String pispId) {
        log.debug("Create a standing order in the store. {}", standingOrder);
        FRStandingOrder5 frStandingOrder = FRStandingOrder5.builder()
                .standingOrder(standingOrder)
                .accountId(standingOrder.getAccountId())
                .id(standingOrder.getStandingOrderId())
                .status(StandingOrderStatus.PENDING)
                .pispId(pispId)
                .build();
        return restTemplate.postForObject(rsStoreRoot + BASE_RESOURCE_PATH, frStandingOrder, FRStandingOrder5.class);
    }

    public Collection<FRStandingOrder5> getActiveStandingOrders() {
        log.debug("Get active standing orders in the store. {}");
        ParameterizedTypeReference<List<FRStandingOrder5>> ptr =
                new ParameterizedTypeReference<List<FRStandingOrder5>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + BASE_RESOURCE_PATH + "search/active"
        );
        URI uri = builder.build().encode().toUri();
        ResponseEntity<List<FRStandingOrder5>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }

    public void updateStandingOrder(FRStandingOrder5 standingOrder) {
        log.debug("Update a standing order in the store. {}", standingOrder);
        restTemplate.put(rsStoreRoot + BASE_RESOURCE_PATH+"/"+standingOrder.getId(), standingOrder);
    }

}
