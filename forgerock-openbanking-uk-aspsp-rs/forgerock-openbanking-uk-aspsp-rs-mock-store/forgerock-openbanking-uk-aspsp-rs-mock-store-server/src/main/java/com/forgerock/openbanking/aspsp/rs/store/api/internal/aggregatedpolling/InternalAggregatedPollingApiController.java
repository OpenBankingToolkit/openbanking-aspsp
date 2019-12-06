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
