/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.eventsubscription;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_2.events.EventSubscriptionsRepository;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_2.event.FREventSubscription1;
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
    public ResponseEntity<Collection<FREventSubscription1>> findByTppId(
            @RequestParam("tppId")  String tppId) {
        log.debug("Find all Event Subscriptions for TPP: {}", tppId);
        return ResponseEntity.ok(eventSubscriptionsRepository.findByTppId(tppId));
    }
}
