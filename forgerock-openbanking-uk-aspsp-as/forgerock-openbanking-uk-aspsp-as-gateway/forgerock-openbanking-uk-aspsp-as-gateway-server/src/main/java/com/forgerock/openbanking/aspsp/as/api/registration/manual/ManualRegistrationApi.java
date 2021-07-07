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
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.2.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.as.api.registration.manual;

import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientException;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationRequest;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;


@Api(tags = "Registration", description = "the manual registration API")
public interface ManualRegistrationApi {

    @ApiOperation(
            value = "Register a new TPP via manual onboarding",
            notes = "Protected by the Open Banking directory",
            tags = {"Registration",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "TPP registered with success",
                    response = OIDCRegistrationResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 405, message = "Method Not Allowed", response = Void.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = Void.class),
            @ApiResponse(code = 429, message = "Too Many Requests", response = Void.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class)
    })

    @PreAuthorize("hasAuthority('ROLE_FORGEROCK_INTERNAL_APP')")
    @RequestMapping(
            value = "/open-banking/manual-onboarding/registerApplication/",
            produces = {"application/json; charset=utf-8"},
            consumes = {"application/json; charset=utf-8"},
            method = RequestMethod.POST
    )
    ResponseEntity<OIDCRegistrationResponse> registerApplication(
            @ApiParam(value = "Registration request", required = true)
            @Valid
            @RequestBody ManualRegistrationRequest manualRegistrationRequest,

            Principal principal
    ) throws OBErrorResponseException, OBErrorException, ApiClientException, DynamicClientRegistrationException;

    @PreAuthorize("hasAuthority('ROLE_FORGEROCK_INTERNAL_APP')")
    @RequestMapping(
            value = "/open-banking/manual-onboarding/registerApplication/{clientId}",
            method = RequestMethod.DELETE
    )
    ResponseEntity<Boolean> unregisterApplication(
            @ApiParam(value = "Unregister application", required = true)
            @Valid
            @PathVariable(value = "clientId") String clientId,

            Principal principal
    ) throws OBErrorResponseException;
}
