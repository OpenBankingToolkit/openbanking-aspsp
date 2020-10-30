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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.data;

import com.forgerock.openbanking.common.model.data.FRDataEvent;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FREventNotification;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@RequestMapping("/api/data")
public interface DataEventsApi {

    @RequestMapping(value = "/events", method = RequestMethod.POST)
    ResponseEntity importEvents(
            @RequestBody FRDataEvent frDataEvent
    ) throws OBErrorResponseException;

    @RequestMapping(value = "/events", method = RequestMethod.PUT)
    ResponseEntity updateEvents(
            @RequestBody FRDataEvent frDataEvent
    ) throws OBErrorResponseException;

    @RequestMapping(value = "/events/all", method = RequestMethod.GET)
    ResponseEntity<Collection<FREventNotification>> exportEvents();

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    ResponseEntity<Collection<FREventNotification>> exportEventsByTppId(@RequestParam String tppId);

    @RequestMapping(value = "/events", method = RequestMethod.DELETE)
    ResponseEntity removeEvents(
            @RequestBody FRDataEvent frDataEvent
    );
}
