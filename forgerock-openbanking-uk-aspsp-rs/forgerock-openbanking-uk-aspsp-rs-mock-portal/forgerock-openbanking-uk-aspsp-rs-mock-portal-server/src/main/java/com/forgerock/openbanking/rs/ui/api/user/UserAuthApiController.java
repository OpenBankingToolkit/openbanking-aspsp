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
package com.forgerock.openbanking.rs.ui.api.user;

import com.forgerock.openbanking.am.gateway.AMASPSPGateway;
import com.forgerock.openbanking.analytics.model.entries.SessionCounterType;
import com.forgerock.openbanking.auth.services.SessionService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OIDCException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class UserAuthApiController {

    @Autowired
    private SessionService sessionService;
    @Value("${am.oidc.endpoint.accesstoken}")
    public String amAccessTokenEndpoint;
    @Autowired
    private AMASPSPGateway amGateway;


    @ApiOperation(value = "Authenticate user",
            authorizations = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user ID token"),
    })
    @RequestMapping(value = "/api/user/authenticate", method = RequestMethod.POST)
    public ResponseEntity authenticate(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            Principal principal
    ) throws OBErrorException {
        try {
            return ResponseEntity.ok(sessionService.authenticate(username, password, (Authentication) principal,
                    SessionCounterType.DIRECTORY, amGateway, amAccessTokenEndpoint));
        } catch (OIDCException e) {
            log.error("OIDC exception", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
