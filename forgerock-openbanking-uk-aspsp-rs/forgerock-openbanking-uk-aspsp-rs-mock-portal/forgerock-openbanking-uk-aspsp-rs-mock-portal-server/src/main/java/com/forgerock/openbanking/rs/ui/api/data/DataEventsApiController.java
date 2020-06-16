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
package com.forgerock.openbanking.rs.ui.api.data;

import com.forgerock.openbanking.common.model.data.FRDataEvent;
import com.forgerock.openbanking.common.model.openbanking.forgerock.event.FREventNotification;
import com.forgerock.openbanking.common.services.store.data.DataEventsService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@Controller
@Slf4j
public class DataEventsApiController implements DataEventsApi{

    private final DataEventsService dataEventsService;

    @Autowired
    public DataEventsApiController(DataEventsService dataEventsService) {
        this.dataEventsService = dataEventsService;
    }

    @Override
    public ResponseEntity<Collection<FREventNotification>> exportEvents() {
        log.debug("Export all events data");
        return ResponseEntity.ok(dataEventsService.exportEvents());
    }

    @Override
    public ResponseEntity<Collection<FREventNotification>> exportEventsByTppId(@RequestBody FRDataEvent frDataEvent) throws OBErrorResponseException {
        log.debug("Export all events data for tppId {}", frDataEvent.getTppId());
        return ResponseEntity.ok(dataEventsService.exportEventsByTppId(frDataEvent));
    }

    @Override
    public ResponseEntity<Collection<FREventNotification>> importEvents(@RequestBody FRDataEvent frDataEvent) throws OBErrorResponseException {
        log.debug("Import events");
        return new ResponseEntity(dataEventsService.importEvents(frDataEvent), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Collection<FREventNotification>> updateEvents(@RequestBody FRDataEvent frDataEvent) throws OBErrorResponseException {
       log.debug("Update events");
       return ResponseEntity.ok(dataEventsService.updateEvents(frDataEvent));
    }

    @Override
    public ResponseEntity<Void> removeEvents(FRDataEvent frDataEvent) throws OBErrorResponseException {
        log.debug("Remove events for the tppId {}", frDataEvent.getTppId());
        return new ResponseEntity(dataEventsService.removeEvents(frDataEvent), HttpStatus.NO_CONTENT);
    }
}
