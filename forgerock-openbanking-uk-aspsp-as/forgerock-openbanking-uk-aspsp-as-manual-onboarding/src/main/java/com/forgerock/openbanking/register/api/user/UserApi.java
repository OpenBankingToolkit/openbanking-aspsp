/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.register.api.user;

import com.forgerock.openbanking.auth.model.AuthorisationResponse;
import com.forgerock.openbanking.auth.model.ExchangeCodeResponse;
import com.forgerock.openbanking.commons.model.directory.User;
import com.forgerock.openbanking.exceptions.OIDCException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.model.UserContext;
import com.forgerock.openbanking.register.model.ManualRegUser;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.text.ParseException;

@Api(
        tags = "Users",
        description = "manage users"
)
@RequestMapping("/api/user")
public interface UserApi {

    @ApiOperation(value = "initiateLogin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user", response = String.class),
    })
    @RequestMapping(value = "/initiate-login", method = RequestMethod.GET)
    ResponseEntity<String> startAuthorisationCodeFlow(
            @RequestParam(value = "originUrl") String originUrl,
            HttpServletResponse response,
            Principal principal
    );

    @ApiOperation(value = "Login by exchange ID token to session")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user", response = User.class),
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    ResponseEntity<ExchangeCodeResponse> login(
            @CookieValue(value = "OIDC_ORIGIN_URL") String originURL,
            @RequestBody AuthorisationResponse authorisationResponse,
            HttpServletResponse response,
            Principal principal
    ) throws OIDCException, ParseException, InvalidTokenException, CertificateEncodingException;


    @ApiOperation(value = "logout the user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "True is logout with success", response = Boolean.class),
    })
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    ResponseEntity<Boolean> logout(
            HttpServletResponse response,
            Principal principal
    );

    @ApiOperation(value = "Get the user profile",
            authorizations = {
            @Authorization(value = "Bearer token", scopes = {
                    @AuthorizationScope(scope = "role", description = "Needs to be authenticated")
            })
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user", response = User.class),
    })
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_FORGEROCK_INTERNAL_APP')")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    ResponseEntity<ManualRegUser> getUser(
            HttpServletResponse response,
            Authentication principal
    ) throws CertificateEncodingException;

    ManualRegUser fromUserContext(UserContext userContext);

    @ApiOperation(value = "Register the user",
            authorizations = {})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The user is registered"),
    })
    @RequestMapping(value = "/", method = RequestMethod.POST)
    ResponseEntity register(
            @RequestParam(value = "username") String username,
            Principal principal
    );

    @ApiOperation(value = "Authenticate user",
            authorizations = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user ID token"),
    })
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    ResponseEntity authenticate(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    );
}
