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

import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_0.data.FRUserData3;
import com.forgerock.openbanking.exceptions.OBErrorException;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Api(tags = "Data", description = "the Financial data export/import API")
@RequestMapping(value = "/api/data")
public interface DataApi {

    @ApiOperation(
            value = "Check if the user has some Data",
            response = Boolean.class,
            authorizations = {
                    @Authorization(value = "SSOToken")
            },
            tags = {"Data",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "true if the user already has some data",
                    response = Boolean.class),
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(
            value = "/user/has-data",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET
    )
    ResponseEntity<FRUserData3> hasData(
            Principal principal
    );

    @ApiOperation(
            value = "Export User Data",
            notes = "Export the user financial data as OB format",
            response = FRUserData3.class,
            authorizations = {
                    @Authorization(value = "SSOToken")
            },
            tags = {"Data",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Export the user financial data",
                    response = FRUserData3.class),
    })
    @RequestMapping(
            value = "/user",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET
    )
    ResponseEntity<FRUserData3> exportUserData(
            Principal principal
    );



    @ApiOperation(
            value = "Import User Data",
            notes = "This import method will update the existing data, it will not erase the previous data. You are also in" +
                    " charge of importing consistent data ;)",
            response = FRUserData3.class,
            authorizations = {
                    @Authorization(value = "SSOToken")
            },
            tags = {"Data",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User financial data updated with success",
                    response = FRUserData3.class),
    })
    @PreAuthorize("hasAuthority('ROLE_DATA')")

    @RequestMapping(
            value = "/user",
            produces = {"application/json; charset=utf-8"},
            consumes = {"application/json; charset=utf-8"},
            method = RequestMethod.PUT
    )
    ResponseEntity<FRUserData3> updateUserData(
            @ApiParam(value = "User financial data", required = true)
            @RequestBody FRUserData3 userData,

            Principal principal
    ) throws OBErrorException;


    @ApiOperation(
            value = "Import User Data",
            notes = "This import method will create new objects. Meaning it would ignore the ID you may have set and create the new object. Therefore, you may want to delete your data first",
            response = FRUserData3.class,
            authorizations = {
                    @Authorization(value = "SSOToken")
            },
            tags = {"Data",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User financial data created with success",
                    response = FRUserData3.class),
    })

    @PreAuthorize("hasAuthority('ROLE_DATA')")
    @RequestMapping(
            value = "/user",
            produces = {"application/json; charset=utf-8"},
            consumes = {"application/json; charset=utf-8"},
            method = RequestMethod.POST
    )
    ResponseEntity<FRUserData3> createUserData(
            @ApiParam(value = "User financial data", required = true)
            @RequestBody FRUserData3 userData,

            Principal principal
    ) throws OBErrorException;


    @ApiOperation(
            value = "Import User Data",
            notes = "This import method will delete all the financial data associated with this user",
            response = FRUserData3.class,
            authorizations = {
                    @Authorization(value = "SSOToken")
            },
            tags = {"Data",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User financial data deleted with success",
                    response = FRUserData3.class),
    })
    @PreAuthorize("hasRole('ROLE_DATA')")
    @RequestMapping(
            value = "/user",
            method = RequestMethod.DELETE
    )
    ResponseEntity deleteUserData(
            Principal principal
    ) throws OBErrorException;

    @ApiOperation(
            value = "Generate new financial Data",
            notes = "This endpoint will delete all the previous data and generate randomly sample data",
            response = FRUserData3.class,
            authorizations = {
                    @Authorization(value = "SSOToken")
            },
            tags = {"Data",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User financial data generated with success",
                    response = FRUserData3.class),
    })
    @PreAuthorize("hasRole('ROLE_DATA')")
    @RequestMapping(
            value = "/user/generate",
            method = RequestMethod.POST
    )
    ResponseEntity generateData(
            @ApiParam(value = "Data profile", required = false)
            @RequestParam(name = "profile", required = false) String profile,

            Principal principal
    ) throws OBErrorException;

    @ApiOperation(value = "Get the user profile for the registration",
            authorizations = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user profiles"),
    })
    @RequestMapping(value = "/profiles", method = RequestMethod.GET)
    ResponseEntity getProfiles(
    );
}
