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
package com.forgerock.openbanking.common.services.store.account.standingorder;

import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStandingOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.org.openbanking.datamodel.account.OBStandingOrder6;

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

    public FRStandingOrder createStandingOrder(OBStandingOrder6 standingOrder, String pispId) {
        log.debug("Create a standing order in the store. {}", standingOrder);
        FRStandingOrder frStandingOrder = FRStandingOrder.builder()
                .standingOrder(standingOrder)
                .accountId(standingOrder.getAccountId())
                .id(standingOrder.getStandingOrderId())
                .status(StandingOrderStatus.PENDING)
                .pispId(pispId)
                .build();
        return restTemplate.postForObject(rsStoreRoot + BASE_RESOURCE_PATH, frStandingOrder, FRStandingOrder.class);
    }

    public Collection<FRStandingOrder> getActiveStandingOrders() {
        log.debug("Get active standing orders in the store. {}");
        ParameterizedTypeReference<List<FRStandingOrder>> ptr =
                new ParameterizedTypeReference<List<FRStandingOrder>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + BASE_RESOURCE_PATH + "search/active"
        );
        URI uri = builder.build().encode().toUri();
        ResponseEntity<List<FRStandingOrder>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }

    public void updateStandingOrder(FRStandingOrder standingOrder) {
        log.debug("Update a standing order in the store. {}", standingOrder);
        restTemplate.put(rsStoreRoot + BASE_RESOURCE_PATH+"/"+standingOrder.getId(), standingOrder);
    }

}
