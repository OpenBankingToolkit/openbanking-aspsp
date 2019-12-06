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
package com.forgerock.openbanking.aspsp.as.api.accesstoken;

import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Api(tags = "AccessToken", description = "the access token")
@RequestMapping(
        value = "/oauth2/"
)
public interface AccessTokenApi {
    @ApiOperation(
            value = "OAuth2 access token",
            tags = {"AccessToken", "OAuth2"}
    )

    @RequestMapping(
            value = "/access_token",
            method = RequestMethod.POST
    )
    @PreAuthorize("hasAnyAuthority('ROLE_PISP', 'ROLE_AISP', 'ROLE_CBPII')")
    ResponseEntity getAccessToken(
            @RequestBody MultiValueMap paramMap,

            @ApiParam(value = "Authorization header")
            @RequestHeader(value = "Authorization", required = false) String authorization,

            Principal principal,

            HttpServletRequest request
    ) throws OBErrorResponseException, OBErrorException;
}
