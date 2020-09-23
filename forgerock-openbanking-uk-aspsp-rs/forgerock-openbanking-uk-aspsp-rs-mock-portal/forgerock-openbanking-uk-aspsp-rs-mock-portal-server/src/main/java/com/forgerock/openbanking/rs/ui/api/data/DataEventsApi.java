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
import com.forgerock.openbanking.common.model.openbanking.persistence.event.v3_0.FREventNotification;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

@Api(tags = "DataEvents", description = "the data events export/import API")
@RequestMapping(value = "/api/data")
public interface DataEventsApi {

    @ApiOperation(
            value = "Export all data events",
            notes = "Export the all events data as OB format",
            response = FREventNotification.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "SSOToken")
            },
            tags = {"DataEvents",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Export all events data",
                    response = FREventNotification.class),
    })
    @RequestMapping(
            value = "/events/all",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET
    )
    ResponseEntity<Collection<FREventNotification>> exportEvents();

    @ApiOperation(
            value = "Export all TppId data events",
            notes = "Export the all TppId events data as OB format",
            response = FREventNotification.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "SSOToken")
            },
            tags = {"DataEvents",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Export the tppId events data",
                    response = FREventNotification.class),
    })
    @RequestMapping(
            value = "/events",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET
    )
    ResponseEntity<Collection<FREventNotification>> exportEventsByTppId(
            @RequestBody FRDataEvent dataEvent
    ) throws OBErrorResponseException;

    @ApiOperation(
            value = "Import data events",
            notes = "Import data events as OB format",
            response = FRDataEvent.class,
            authorizations = {
                    @Authorization(value = "SSOToken")
            },
            tags = {"DataEvents",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Import the tppId data events",
                    response = FRDataEvent.class),
    })
    @PreAuthorize("hasAuthority('ROLE_DATA')")
    @RequestMapping(
            value = "/events",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.POST
    )
    ResponseEntity<Collection<FREventNotification>> importEvents(
            @RequestBody FRDataEvent frDataEvent
    ) throws OBErrorResponseException;

    @ApiOperation(
            value = "Update data events",
            notes = "Update data events as OB format",
            response = FRDataEvent.class,
            authorizations = {
                    @Authorization(value = "SSOToken")
            },
            tags = {"DataEvents",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Update data events",
                    response = FRDataEvent.class),
    })
    @PreAuthorize("hasAuthority('ROLE_DATA')")
    @RequestMapping(
            value = "/events",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.PUT
    )
    ResponseEntity<Collection<FREventNotification>> updateEvents(
            @RequestBody FRDataEvent frDataEvent
    ) throws OBErrorResponseException;

    @ApiOperation(
            value = "Remove data events",
            notes = "Remove data events as OB format",
            response = FRDataEvent.class,
            authorizations = {
                    @Authorization(value = "SSOToken")
            },
            tags = {"DataEvents",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Remove data events",
                    response = FRDataEvent.class),
    })
    @PreAuthorize("hasAuthority('ROLE_DATA')")
    @RequestMapping(
            value = "/events",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.DELETE
    )
    ResponseEntity<Void> removeEvents(
            @RequestBody FRDataEvent frDataEvent
    ) throws OBErrorResponseException;
}
