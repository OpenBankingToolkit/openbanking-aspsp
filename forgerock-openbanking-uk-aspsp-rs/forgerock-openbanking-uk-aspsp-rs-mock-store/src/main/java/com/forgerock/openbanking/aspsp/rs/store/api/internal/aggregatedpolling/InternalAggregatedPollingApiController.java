/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.aggregatedpolling;

import com.forgerock.openbanking.aspsp.rs.store.repository.FRPendingEventsRepository;
import com.forgerock.openbanking.common.model.openbanking.forgerock.event.FREventNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class InternalAggregatedPollingApiController implements AggregatedPollingApi {
    private FRPendingEventsRepository frPendingEventsRepository;

    @Autowired
    public InternalAggregatedPollingApiController(FRPendingEventsRepository frPendingEventsRepository) {
        this.frPendingEventsRepository = frPendingEventsRepository;
    }

    @Override
    public ResponseEntity<FREventNotification> create(
            @RequestBody FREventNotification eventNotification) {
        log.debug("Create event notification {}", eventNotification);
        return new ResponseEntity<>(frPendingEventsRepository.save(eventNotification), HttpStatus.CREATED);
    }
}
