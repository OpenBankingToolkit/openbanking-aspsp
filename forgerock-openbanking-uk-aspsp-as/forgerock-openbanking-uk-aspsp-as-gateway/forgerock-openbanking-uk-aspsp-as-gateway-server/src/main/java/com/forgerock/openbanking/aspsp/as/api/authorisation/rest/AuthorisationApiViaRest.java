/**
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
package com.forgerock.openbanking.aspsp.as.api.authorisation.rest;

import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Api(tags = "Authorisation", description = "the authorisation endpoint")
@RequestMapping(
        value = "/oauth2/rest/"
)
public interface AuthorisationApiViaRest {

    @RequestMapping(
            value = "/authorize",
            method = {RequestMethod.GET}
    )
    ResponseEntity getAuthorisation(
            @ApiParam(value = "OpenID response type")
            @RequestParam(value = "response_type", required = true) String responseType,

            @ApiParam(value = "OpenID Client ID")
            @RequestParam(value = "client_id", required = true) String clientId,

            @ApiParam(value = "OpenID state")
            @RequestParam(value = "state", required = false) String state,

            @ApiParam(value = "OpenID nonce")
            @RequestParam(value = "nonce", required = false) String nonce,

            @ApiParam(value = "OpenID scopes")
            @RequestParam(value = "scope", required = false) String scopes,

            @ApiParam(value = "OpenID redirect_uri")
            @RequestParam(value = "redirect_uri", required = true) String redirectUri,

            @ApiParam(value = "OpenID request parameters")
            @RequestParam(value = "request", required = true) String requestParameters,

            @ApiParam(value = "Cookie containing the user session", defaultValue = "")
            @CookieValue(value = "${am.cookie.name}", required = false) String ssoToken,

            @RequestBody(required = false) MultiValueMap body,

            HttpServletRequest request
    ) throws OBErrorResponseException, OBErrorException;


}
