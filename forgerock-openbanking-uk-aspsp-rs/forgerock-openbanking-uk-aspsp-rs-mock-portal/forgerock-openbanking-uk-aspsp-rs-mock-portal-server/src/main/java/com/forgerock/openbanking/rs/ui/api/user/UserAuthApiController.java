/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
