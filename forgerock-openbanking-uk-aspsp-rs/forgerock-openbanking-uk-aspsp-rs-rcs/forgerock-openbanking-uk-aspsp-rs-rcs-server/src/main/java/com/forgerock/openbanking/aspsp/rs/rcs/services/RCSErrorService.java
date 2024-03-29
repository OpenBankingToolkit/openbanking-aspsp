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
package com.forgerock.openbanking.aspsp.rs.rcs.services;

import brave.Tracer;
import com.forgerock.openbanking.common.model.rcs.RedirectionAction;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.google.common.base.Splitter;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import uk.org.openbanking.OBConstants;

import java.net.URI;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Map;

@Service
@Slf4j
public class RCSErrorService {
    private final Tracer tracer;

    @Autowired
    public RCSErrorService(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * This service is intended for scenarios where the consent provided by the TPP is invalid so we cannot proceed with PSU authorisation.
     * In this situation, we want to wrap the error message inside a redirect back to the provided callback URL as that the TPP will be aware of the issue and
     * the PSU can receive a more helpful response.
     *
     * @param consentContextJwt The JWT provided in the consent request from TPP.
     * @param obriErrorType The type of error
     * @param args The arguments to add to the parameterized error message
     * @return The HTTP response for the RCS UI
     * @throws OBErrorException If we cannot handle the provided consent JWT or extract the TPP redirect URL we have to fall back to an error response to the UI.
     */
    public ResponseEntity<RedirectionAction> invalidConsentError(String consentContextJwt, OBRIErrorType obriErrorType, Object... args) throws OBErrorException {

        return invalidConsentError(consentContextJwt, new OBErrorException(obriErrorType, args));
    }

    public ResponseEntity<RedirectionAction> invalidConsentError(String consentContextJwt, OBErrorException obError) throws OBErrorException {
        try {
            Map<String, String> params = extractParams(consentContextJwt);
            String redirectURL =  URLDecoder.decode(params.get("redirect_uri"), "UTF-8");
            String state = "";

            if (params.get("state") != null) {
                state = URLDecoder.decode(params.get("state"), "UTF-8");
            } else {
                String requestParameter = params.get("request");
                if (requestParameter != null) {
                    SignedJWT requestParameterJwt = (SignedJWT) JWTParser.parse(requestParameter);
                    state = requestParameterJwt.getJWTClaimsSet().getStringClaim(OBConstants.OIDCClaim.STATE);
                }
            }

            if (StringUtils.isEmpty(redirectURL)) {
                log.warn("Null or empty redirect URL. Falling back to just throwing error back to UI");
                throw obError;
            }
            UriComponents uriComponents = UriComponentsBuilder
                    .fromHttpUrl(redirectURL)
                    .fragment("error=invalid_request_object&state=" + state + "&error_description=" +
                            String.format(obError.getObriErrorType().getMessage(), obError.getArgs()))
                    .encode()
                    .build();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(RedirectionAction.builder()
                            .redirectUri(uriComponents.toUriString())
                            .requestMethod(HttpMethod.GET)
                            .build());
        } catch (Exception e) {
            log.warn("Failed to turn error into a redirect back to TPP with and Exception. " +
                    "Falling back to just throwing error back to UI", e);
            throw obError;
        }
    }

    public ResponseEntity<RedirectionAction> invalidConsentError(URI redirectURL) throws OBErrorException {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(RedirectionAction.builder()
                            .redirectUri(redirectURL.toString())
                            .requestMethod(HttpMethod.GET)
                            .build());
        } catch (Exception e) {
            log.warn("invalidConsentError(String redirectURL): " +
                    "Failed to turn error into a redirect back to TPP with and Exception. " +
                    "Falling back to just throwing error back to UI", e);
            throw new OBErrorException(OBRIErrorType.SERVER_ERROR);
        }
    }

    public ResponseEntity<RedirectionAction> error(OBRIErrorType obriErrorType, Object... args) throws OBErrorException {
        throw new OBErrorException(obriErrorType, args);
    }

    private Map<String, String> extractParams(String consentContextJwt) throws ParseException {
        log.debug("Parse consent request JWS: {}", consentContextJwt);
        SignedJWT signedJWT = (SignedJWT) JWTParser.parse(consentContextJwt);
        log.debug("Get claim: {} from JWT: {}", OIDCConstants.OIDCClaim.CONSENT_APPROVAL_REDIRECT_URI, signedJWT);
        String amRedirectUri = signedJWT.getJWTClaimsSet()
                .getStringClaim(OIDCConstants.OIDCClaim.CONSENT_APPROVAL_REDIRECT_URI);
        log.debug("Get TPP callback URL from AM URL: {}", amRedirectUri);

        String query = amRedirectUri.split("\\?")[1];
        return Splitter.on('&').trimResults().withKeyValueSeparator("=").split(query);
    }
}
