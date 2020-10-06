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
package com.forgerock.openbanking.aspsp.rs.store.api.internal.eventsubscription;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_2.events.EventSubscriptionsRepository;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FREventSubscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
@Slf4j
public class EventSubscriptionApiController implements EventSubscriptionApi {
    private final EventSubscriptionsRepository eventSubscriptionsRepository;

    public EventSubscriptionApiController(EventSubscriptionsRepository eventSubscriptionsRepository) {
        this.eventSubscriptionsRepository = eventSubscriptionsRepository;
    }

    @Override
    public ResponseEntity<Collection<FREventSubscription>> findByTppId(
            @RequestParam("tppId")  String tppId) {
        log.debug("Find all Event Subscriptions for TPP: {}", tppId);
        return ResponseEntity.ok(eventSubscriptionsRepository.findByTppId(tppId));
    }
}
