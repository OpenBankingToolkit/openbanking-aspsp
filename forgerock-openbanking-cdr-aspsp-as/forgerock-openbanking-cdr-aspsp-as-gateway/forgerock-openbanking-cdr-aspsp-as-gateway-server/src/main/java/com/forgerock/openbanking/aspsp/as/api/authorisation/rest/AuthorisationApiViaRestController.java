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
package com.forgerock.openbanking.aspsp.as.api.authorisation.rest;

import com.forgerock.openbanking.aspsp.as.api.authorisation.redirect.AuthorisationApi;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@Slf4j
public class AuthorisationApiViaRestController implements AuthorisationApiViaRest {

    @Autowired
    private AuthorisationApi authorisationApi;

    @Override
    public ResponseEntity getAuthorisation(
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
            @RequestParam(value = "redirect_uri", required = false) String redirectUri,

            @ApiParam(value = "OpenID request parameters")
            @RequestParam(value = "request", required = true) String requestParametersSerialised,

            @ApiParam(value = "Cookie containing the user session", required = false)
            @CookieValue(value = "${am.cookie.name}", required = false) String ssoToken,

            @RequestBody(required = false) MultiValueMap body,

            HttpServletRequest request
    ) throws OBErrorResponseException, OBErrorException {

        ResponseEntity<String> responseEntity = authorisationApi.getAuthorisation(responseType, clientId, state, nonce, scopes, redirectUri, requestParametersSerialised,
                false, "", "",
                ssoToken, body, new CustomPathHttpRequestWrapper(request));
        log.debug("Response received by AM: {}", responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.FOUND) {
            return ResponseEntity.ok(StageResponse.builder()
                    .stage(Stage.ERROR)
                    .message("AM is not returning a 302. Likely a major issue in the request that the TPP wants to troubleshoot." +
                            "Redirect the TPP to the same request but via the redirect model.")
                    .build()
            );
        }

        String location = responseEntity.getHeaders().getFirst(HttpHeaders.LOCATION);
        log.debug("Location returned by AM '{}'", location);

        return ResponseEntity.ok(StageResponse.builder()
                .stage(getStage(location))
                .uri(location)
                .build()
        );
    }

    public enum Stage {
        AUTHENTICATION,
        CONSENT,
        REDIRECT_TPP,
        REDIRECT_AM,
        ERROR
    }

    private Pattern amLocat = Pattern.compile("https\\:\\/\\/as\\.aspsp.*");
    private Pattern bankLoginLocat = Pattern.compile("https\\:\\/\\/bank.*\\/login.*");
    private Pattern bankConsentLocat = Pattern.compile("https\\:\\/\\/bank.*\\/consent.*");

    public Stage getStage(String location) {
        Matcher bankLoginMatcher = bankLoginLocat.matcher(location);
        if (bankLoginMatcher.find()) {
            log.debug("The redirection was to authentication");
            return Stage.AUTHENTICATION;
        }
        Matcher bankConsentMatcher = bankConsentLocat.matcher(location);
        if (bankConsentMatcher.find()) {
            log.debug("The redirection was to consent");
            return Stage.CONSENT;
        }
        Matcher amMatcher = amLocat.matcher(location);
        if (amMatcher.find()) {
            log.debug("The redirection was to AM");
            return Stage.REDIRECT_AM;
        }
        log.debug("Redirect unknown, must be TPP");
        return Stage.REDIRECT_TPP;
    }

    @Data
    @Builder
    public static class StageResponse {
        private Stage stage;
        private String uri;
        private String message;
    }
}
