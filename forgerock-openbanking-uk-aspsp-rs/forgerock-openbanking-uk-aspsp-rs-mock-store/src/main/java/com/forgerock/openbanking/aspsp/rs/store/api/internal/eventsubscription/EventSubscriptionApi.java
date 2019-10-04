/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.eventsubscription;

import com.forgerock.openbanking.commons.model.openbanking.v3_1_2.event.FREventSubscription1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@RequestMapping(value = "/api/event-subscriptions")
public interface EventSubscriptionApi {

    @RequestMapping(value = "/search/findByTppId",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity<Collection<FREventSubscription1>> findByTppId(
            @RequestParam("tppId") String tppId
    );

}
