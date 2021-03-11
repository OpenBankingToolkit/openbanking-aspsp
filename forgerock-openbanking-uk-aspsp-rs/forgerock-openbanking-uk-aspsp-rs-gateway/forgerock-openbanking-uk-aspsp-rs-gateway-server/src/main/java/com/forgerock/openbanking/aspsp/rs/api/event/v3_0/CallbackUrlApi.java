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
package com.forgerock.openbanking.aspsp.rs.api.event.v3_0;

import com.forgerock.openbanking.api.annotations.OBGroupName;
import com.forgerock.openbanking.api.annotations.OBReference;
import com.forgerock.openbanking.api.annotations.OpenBankingAPI;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;
import uk.org.openbanking.datamodel.event.OBCallbackUrl1;
import uk.org.openbanking.datamodel.event.OBCallbackUrlResponse1;
import uk.org.openbanking.datamodel.event.OBCallbackUrlsResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Api(value = "callback-urls", description = "the event notification callback urls API")
@OpenBankingAPI(
        obVersion = "3.0",
        obGroupName = OBGroupName.EVENT,
        obReference = OBReference.EVENTS
)
@RequestMapping(value = "/open-banking/v3.0/callback-urls")
public interface CallbackUrlApi {

    @ApiOperation(value = "Create a callback URL", nickname = "createCallbackURL", notes = "", response = OBCallbackUrlResponse1.class, authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Accounts scope"),
                    @AuthorizationScope(scope = "payments", description = "Payments  scope"),
                    @AuthorizationScope(scope = "fundsconfirmations", description = "Funds Confirmations scope")
            })
    }, tags = {"Callback URLs"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Callback URLs Created", response = OBCallbackUrlResponse1.class),
            @ApiResponse(code = 400, message = "Bad request", response = OBErrorResponse1.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 415, message = "Unsupported Media Type"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = OBErrorResponse1.class)})
    @PreAuthorize("hasAnyAuthority('ROLE_AISP', 'ROLE_PISP')")
    @OpenBankingAPI(
            obReference = OBReference.CREATE_CALLBACK_URL
    )
    @RequestMapping(
            produces = {"application/json; charset=utf-8"},
            consumes = {"application/json; charset=utf-8"},
            method = RequestMethod.POST)
    ResponseEntity<OBCallbackUrlResponse1> createCallbackUrls(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBCallbackUrl1 obCallbackUrl1Param,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload." ,required=true)
            @RequestHeader(value="x-jws-signature", required=false) String xJwsSignature,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException;

    @ApiOperation(value = "Read all callback URLs", nickname = "readCallbackUrls", notes = "", response = OBCallbackUrlsResponse1.class, authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Accounts scope"),
                    @AuthorizationScope(scope = "payments", description = "Payments  scope"),
                    @AuthorizationScope(scope = "fundsconfirmations", description = "Funds Confirmations scope")
            })
    }, tags = {"Callback URLs"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Callback URLs Read", response = OBCallbackUrlResponse1.class),
            @ApiResponse(code = 400, message = "Bad request", response = OBErrorResponse1.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = OBErrorResponse1.class)})
    @PreAuthorize("hasAnyAuthority('ROLE_AISP', 'ROLE_PISP')")
    @OpenBankingAPI(
            obReference = OBReference.GET_CALLBACK_URLS
    )
    @RequestMapping(
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET)
    ResponseEntity<OBCallbackUrlsResponse1> readCallBackUrls(
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException;

    @ApiOperation(value = "Amend a callback URI", nickname = "amendCallbackURL", notes = "", response = OBCallbackUrlResponse1.class, authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Accounts scope"),
                    @AuthorizationScope(scope = "payments", description = "Payments  scope"),
                    @AuthorizationScope(scope = "fundsconfirmations", description = "Funds Confirmations scope")
            })
    }, tags = {"Callback URLs"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Callback URLs Amended", response = OBCallbackUrlResponse1.class),
            @ApiResponse(code = 400, message = "Bad request", response = OBErrorResponse1.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = OBErrorResponse1.class)})
    @PreAuthorize("hasAnyAuthority('ROLE_AISP', 'ROLE_PISP')")
    @OpenBankingAPI(
            obReference = OBReference.AMEND_CALLBACK_URL
    )
    @RequestMapping(value = "/{CallbackUrlId}",
            produces = {"application/json; charset=utf-8"},
            consumes = {"application/json; charset=utf-8"},
            method = RequestMethod.PUT)
    ResponseEntity<OBCallbackUrlResponse1> updateCallbackUrl(
            @ApiParam(value = "CallbackUrlId", required = true)
            @PathVariable("CallbackUrlId") String callbackUrlId,

            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBCallbackUrl1 obCallbackUrl1Param,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = false) String xJwsSignature,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException;

    @ApiOperation(value = "Delete a callback URI", nickname = "deleteCallbackURL", notes = "", authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Accounts scope"),
                    @AuthorizationScope(scope = "payments", description = "Payments  scope"),
                    @AuthorizationScope(scope = "fundsconfirmations", description = "Funds Confirmations scope")
            })
    }, tags = {"Callback URLs"})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Callback URLs Deleted", response = OBCallbackUrlResponse1.class),
            @ApiResponse(code = 400, message = "Bad request", response = OBErrorResponse1.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = OBErrorResponse1.class)})
    @PreAuthorize("hasAnyAuthority('ROLE_AISP', 'ROLE_PISP')")
    @OpenBankingAPI(
            obReference = OBReference.DELETE_CALLBACK_URL
    )
    @RequestMapping(value = "/{CallbackUrlId}",
            method = RequestMethod.DELETE)
    ResponseEntity deleteCallbackUrl(
            @ApiParam(value = "CallbackUrlId", required = true)
            @PathVariable("CallbackUrlId") String callbackUrlId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException;

}
