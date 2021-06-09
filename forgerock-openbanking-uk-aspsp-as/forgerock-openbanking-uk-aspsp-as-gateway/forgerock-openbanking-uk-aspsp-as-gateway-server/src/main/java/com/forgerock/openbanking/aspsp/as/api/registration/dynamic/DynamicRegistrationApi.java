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
package com.forgerock.openbanking.aspsp.as.api.registration.dynamic;

import com.forgerock.openbanking.aspsp.as.service.OIDCException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2BearerTokenUsageInvalidTokenException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2BearerTokenUsageMissingAuthInfoException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 *
 * Interface specifying endpoints that required to implement the Open Banking (OB) Dynamic
 * Client Registration (DCR) specification.
 *
 * <ul>
 *     <li> <a href=https://openbankinguk.github.io/dcr-docs-pub/v3.3/dynamic-client-registration.html>
 *         Open Banking Dynamic Client Registration Specifications</a></li>
 *     <li><a href=https://datatracker.ietf.org/doc/html/rfc7591>
 *         The OAuth2 Dynamic Client Registration standard</a></li>
 * </ul>
 */
@Api(value = "register", description = "the register API")
@RequestMapping(value = "/open-banking/register")
public interface DynamicRegistrationApi {

    @PreAuthorize("hasAnyAuthority('ROLE_PISP', 'ROLE_AISP', 'ROLE_CBPII', 'ROLE_EIDAS')")
    @ApiOperation(value = "Delete a client by way of Client ID", nickname = "registerClientIdDelete", notes = "",
            authorizations = { @Authorization(value = "TPPOAuth2Security", scopes = {}) },
            tags = {"Client " +"Registration", "Optional",})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Client deleted"),
            @ApiResponse(code = 401, message = "Request failed due to unknown or invalid Client or invalid access token"),
            @ApiResponse(code = 403, message = "The client does not have permission to read, update or delete the Client"),
            @ApiResponse(code = 405, message = "The client does not have permission to read, update or delete the Client")})
    @RequestMapping(value = "/",
            method = RequestMethod.DELETE)
    ResponseEntity<Void> unregister(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = false)
            String authorization,
            Principal principal

    ) throws  OAuth2BearerTokenUsageMissingAuthInfoException, OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException;

    @PreAuthorize("hasAnyAuthority('ROLE_PISP', 'ROLE_AISP', 'ROLE_CBPII', 'ROLE_EIDAS')")
    @ApiOperation(
            value = "Delete a client by way of Client ID", nickname = "registerClientIdDelete", notes = "",
            authorizations = {@Authorization(value = "TPPOAuth2Security", scopes = {})},
            tags = {"Client Registration", "Optional",})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Client deleted"),
            @ApiResponse(code = 401, message = "Request failed due to unknown or invalid Client or invalid access token"),
            @ApiResponse(code = 403, message = "The client does not have permission to read, update or delete the Client"),
            @ApiResponse(code = 405, message = "The client does not have permission to read, update or delete the Client")})
    @RequestMapping(value = "/{ClientId}",
            method = RequestMethod.DELETE)
    ResponseEntity<Void> unregister(
            @ApiParam(value = "The client ID", required = true)
            @PathVariable("ClientId")
            String clientId,
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = false)
            String authorization,
            Principal principal

    ) throws  OAuth2BearerTokenUsageMissingAuthInfoException, OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException;


    @PreAuthorize("hasAnyAuthority('ROLE_PISP', 'ROLE_AISP', 'ROLE_CBPII', 'ROLE_EIDAS')")
    @ApiOperation(value = "Get a client by way of Client ID", nickname = "registerClientIdGet", notes = "", authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {

            })
    }, tags = {"Client Registration", "Optional",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client registration", response = OIDCRegistrationResponse.class),
            @ApiResponse(code = 401, message = "Request failed due to unknown or invalid Client or invalid access token"),
            @ApiResponse(code = 403, message = "The client does not have permission to read, update or delete the Client")})
    @RequestMapping(value = "/{ClientId}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<OIDCRegistrationResponse> getRegisterResult(
            @ApiParam(value = "The client ID", required = true)
            @PathVariable("ClientId") String clientId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            Principal principal

    ) throws OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException, OAuth2BearerTokenUsageMissingAuthInfoException;


    @PreAuthorize("hasAnyAuthority('ROLE_PISP', 'ROLE_AISP', 'ROLE_CBPII', 'ROLE_EIDAS')")
    @ApiOperation(value = "Update a client by way of Client ID", nickname = "registerClientIdPut", notes = "", authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {

            })
    }, tags = {"Client Registration", "Optional",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client registration", response = OIDCRegistrationResponse.class),
            @ApiResponse(code = 400, message = "Request failed due to client error", response = Void.class),
            @ApiResponse(code = 401, message = "Request failed due to unknown or invalid Client or invalid access token"),
            @ApiResponse(code = 403, message = "The client does not have permission to read, update or delete the Client")})
    @RequestMapping(value = "/{ClientId}",
            produces = {"application/json"},
            consumes = {"application/jwt"},
            method = RequestMethod.PUT)
    ResponseEntity<OIDCRegistrationResponse> updateClient(
            @ApiParam(value = "The client ID", required = true)
            @PathVariable("ClientId") String clientId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "A request to register a Software Statement Assertion with an ASPSP")
            @Valid
            @RequestBody String registrationRequestJwtSerialised,

            Principal principal
    ) throws OBErrorException, OIDCException, OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException, OAuth2BearerTokenUsageMissingAuthInfoException;



    @PreAuthorize("hasAnyAuthority('ROLE_PISP', 'ROLE_AISP', 'ROLE_CBPII', 'ROLE_EIDAS')")
    @ApiOperation(value = "Get a client by way of Client ID", nickname = "registerClientIdGet", notes = "", authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {

            })
    }, tags = {"Client Registration", "Optional",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client registration", response = OIDCRegistrationResponse.class),
            @ApiResponse(code = 401, message = "Request failed due to unknown or invalid Client or invalid access token"),
            @ApiResponse(code = 403, message = "The client does not have permission to read, update or delete the Client")})
    @RequestMapping(value = "/",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<OIDCRegistrationResponse> getRegisterResult(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            Principal principal

    ) throws OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException, OAuth2BearerTokenUsageMissingAuthInfoException;


    @PreAuthorize("hasAnyAuthority('ROLE_PISP', 'ROLE_AISP', 'ROLE_CBPII', 'ROLE_EIDAS')")
    @ApiOperation(value = "Update a client by way of Client ID", nickname = "registerClientIdPut", notes = "", authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {

            })
    }, tags = {"Client Registration", "Optional",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client registration", response = OIDCRegistrationResponse.class),
            @ApiResponse(code = 400, message = "Request failed due to client error", response = Void.class),
            @ApiResponse(code = 401, message = "Request failed due to unknown or invalid Client or invalid access token"),
            @ApiResponse(code = 403, message = "The client does not have permission to read, update or delete the Client")})
    @RequestMapping(value = "/",
            produces = {"application/json"},
            consumes = {"application/jwt"},
            method = RequestMethod.PUT)
    ResponseEntity<OIDCRegistrationResponse> updateClient(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "A request to register a Software Statement Assertion with an ASPSP")
            @Valid
            @RequestBody String registrationRequestJwtSerialised,

            Principal principal
    ) throws OBErrorException, OIDCException, OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException, OAuth2BearerTokenUsageMissingAuthInfoException;


    @PreAuthorize("hasAnyAuthority('UNREGISTERED_TPP', 'ROLE_PISP', 'ROLE_AISP', 'ROLE_CBPII', 'ROLE_EIDAS')")
    @ApiOperation(value = "Register a client by way of a Software Statement Assertion", nickname = "registerPost",
            notes = "Endpoint will be secured by way of Mutual Authentication over TLS", tags = {"Client Registration", "Conditional",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Client registration", response = OIDCRegistrationResponse.class),
            @ApiResponse(code = 400, message = "Request failed due to client error", response = Void.class)})
    @RequestMapping(value = "/",
            produces = {"application/json"},
            consumes = {"application/jwt"},
            method = RequestMethod.POST)
    ResponseEntity<OIDCRegistrationResponse> register(
            @ApiParam(value = "A request to register a Software Statement Assertion with an ASPSP")
            @Valid
            @RequestBody String registrationRequestJwtSerialised,

            Principal principal
    ) throws  OIDCException, OBErrorResponseException;
}
