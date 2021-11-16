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
package com.forgerock.openbanking.common.services;

import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.common.error.exception.AccessTokenReWriteException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.oidc.AccessTokenResponse;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * JwtOverridingService provides an API that allows a JWT to be parsed and then rewritten and resigned with the
 * signing key that the ASPSP has been configured with. This is necessary because we allow AM to process the /authorize
 * requests, but AM, even if configured to sign with the correct OB Signing key, does not not respond with a jwt that
 * contains the kid for the key, which means it can't be validated against the OB Sandbox Directory's jwks_uri. So we
 * must validate, parse, then recreate the id_token, signing it with the OB Signing key (or what ever the
 * deployment's signing key is configured to be), and assigning a correct kid in the jwk header.
 */
@Service
@Slf4j
public class JwtOverridingService {

    private static final String ID_TOKEN_PARAM = "id_token";
    private static final String ERROR_PARAM = "error";
    private static final String EQUALS_CHAR = "=";
    private static final Pattern ID_TOKEN_PATTERN = Pattern.compile(ID_TOKEN_PARAM + EQUALS_CHAR + "?([^&]+)?");

    private final CryptoApiClient cryptoApiClient;
    private final AMOpenBankingConfiguration amOpenBankingConfiguration;

    @Autowired
    public JwtOverridingService(CryptoApiClient cryptoApiClient, AMOpenBankingConfiguration amOpenBankingConfiguration) {
        this.cryptoApiClient = cryptoApiClient;
        this.amOpenBankingConfiguration = amOpenBankingConfiguration;
    }

    private String rewriteJWS(String jws) throws ParseException {

        JWTClaimsSet jwtClaimsSet = SignedJWT.parse(jws).getJWTClaimsSet();
        return cryptoApiClient.signClaims(amOpenBankingConfiguration.getIssuerID(), jwtClaimsSet, false);
    }

    /**
     * rewriteAccessTokenResponseIdToken - creates a new id_token, signed with the correct ID and containing the
     * correct kid in the header. This is required as currently AM uses a different algorithm to create the kid than
     * the OB Directory does, so when signing with the OB Signing Key or an OBSeal then the key ID in the AM
     * generated id_token doesn't match the kid in the OB Directory jwks_uri and hence the id_token can't be
     * validated. To fix that we resign it using the crypto API which produces a verifyable token.
     *
     * @param responseEntity the response entity that contains the id_token. Note, the id_token will only exist if
     *                       the request to get the access token contained the openid scope, which is not mandatory.
     *                       If the response entity does not contain an id_token then there is nothing to be
     *                       re-written and the responseEntity will be returned.
     * @return Either the responseEntity passed to the method (if it contained no id_token to be re-written), or a
     * new responseEntity containing a new id_token that is verifiable.
     * @throws AccessTokenReWriteException
     */
    public ResponseEntity rewriteAccessTokenResponseIdToken(ResponseEntity responseEntity)
            throws AccessTokenReWriteException {

        if (responseEntity == null) {
            throw new AccessTokenReWriteException("rewriteAccessTokenResponseIdToken() responseEntity is null");
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            log.debug("rewriteAccessTokenResponseIdToken() responseEntity does not have success status");
            throw new AccessTokenReWriteException("Failed to rewrite access token response's id_token: responseEntity" +
                    " status code was " + responseEntity.getStatusCode() + ". Expected 200 (OK)");
        }

        AccessTokenResponse accessTokenResponse = (AccessTokenResponse) responseEntity.getBody();
        if (accessTokenResponse == null) {
            log.debug("rewriteAccessTokenResponseIdToken() Expected body in responseEntity '{}'",
                    responseEntity);
            throw new AccessTokenReWriteException("Failed to rewrite access token response's id_token; responseEntity" +
                    " has no body");
        }

        String id_token = accessTokenResponse.getId_token();
        if (id_token == null) {
            log.debug("rewriteAccessTokenResponseIdToken() responseEntity body contains no id_token; '{}'",
                    accessTokenResponse);
            return responseEntity;
        } else {
            try {
                String rewrittenJWS = rewriteJWS(id_token);
                if (rewrittenJWS == null || rewrittenJWS.isEmpty() || rewrittenJWS.isBlank()) {
                    log.debug("rewriteAccessTokenResponseIdToken() rewrittenJWS is null or empty.");
                    throw new AccessTokenReWriteException("Failed to rewrite access token response's id_token; " +
                            "re-written JWS is null or empty");

                }
                accessTokenResponse.setId_token(rewrittenJWS);
            } catch (ParseException e) {
                log.debug("rewriteAccessTokenResponseIdToken() Failed to parse id_token: '{}'", id_token, e);
                throw new AccessTokenReWriteException("Failed to rewrite access token response's id_token; Could not " +
                        "parse id_token", e);
            }

            ResponseEntity rewrittenResponseEntity = ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
            log.trace("rewriteAccessTokenResponseIdToken() re-written responseEntity is '{}'", responseEntity);
            return rewrittenResponseEntity;
        }

    }

    /**
     * Rewrite the id_token fragment of a ResponseEntity such that it is replaced with a new id_token containing the
     * same body, but that is signed by the ASPSP's signing key and contains the correct kid in the jwt header.
     * @param responseEntity
     * @return a new @ResponseEntity
     * @throws AccessTokenReWriteException if there is no id_token in the Location header fragment that can be replaced,
     * or if the id_token in that fragment can't be parsed.
     */
    public ResponseEntity rewriteIdTokenFragmentInLocationHeader(ResponseEntity responseEntity)
            throws AccessTokenReWriteException {
        if (responseEntity == null) {
            log.debug("getFragmentIdTokenInLocationHeader(): responseEntity is null");
            throw new AccessTokenReWriteException("Failed to find id_token to re-write: responseEntity is null");
        }
        log.trace("rewriteIdTokenFragmentInLocationHeader() responseEntity is '{}'", responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.FOUND) {
            log.debug("getFragmentIdTokenInLocationHeader(): responseEntity does not have a FOUND http Status; '{}'",
                    responseEntity);
            throw new AccessTokenReWriteException("Failed to find id_token to re-write: responseEntity does not have " +
                    "a FOUND http status");
        }

        if (responseEntity.getHeaders().getLocation() == null) {
            log.debug("getFragmentIdTokenInLocationHeader(): responseEntity has no location header: '{}'",
                    responseEntity);
            throw new AccessTokenReWriteException("Failed to find id_token to re-write: responseEntity has no " +
                    "location header");
        } else {
            log.debug("getFragmentIdTokenInLocationHeader(): Location is: {}",
                    responseEntity.getHeaders().getLocation());
        }

        String uriFragment = responseEntity.getHeaders().getLocation().getFragment();
        if (uriFragment == null || uriFragment.isEmpty() || uriFragment.isBlank()) {
            log.debug("getFragmentIdTokenInLocationHeader(): Location has no fragment");
            throw new AccessTokenReWriteException("Failed to find id_token to re-write: responseEntity's Location has" +
                    " no fragment");
        }

        ResponseEntity rewrittenResponseEntity = null;
        try {
            String locationString = responseEntity.getHeaders().getLocation().toString();
            if (locationString.contains(ID_TOKEN_PARAM + EQUALS_CHAR)) {
                Matcher matcher = ID_TOKEN_PATTERN.matcher(locationString);
                while (matcher.find()) {
                    String oldIdToken = matcher.group(1);
                    String newIdToken = rewriteJWS(oldIdToken);
                    String newLocationHeaderValue = locationString.replaceAll(oldIdToken, newIdToken);
                    if (!newLocationHeaderValue.isEmpty() && !newLocationHeaderValue.isBlank()) {
                        HttpHeaders writableHttpHeaders = HttpHeaders.writableHttpHeaders(responseEntity.getHeaders());
                        writableHttpHeaders.set(HttpHeaders.LOCATION, newLocationHeaderValue);
                        rewrittenResponseEntity = new ResponseEntity(responseEntity.getBody(), writableHttpHeaders,
                                HttpStatus.FOUND);
                    } else {
                        log.debug("rewriteIdTokenFragmentInLocationHeader() No new location header value.");
                        throw new AccessTokenReWriteException("Failed to rewrite id_token: new Location header value is " +
                                "null or empty");
                    }
                }
            } else if (locationString.contains(ERROR_PARAM + EQUALS_CHAR)) { // reject by user
                throw new AccessTokenReWriteException("Rejected by user");
            }
        } catch (ParseException e) {
            log.error("rewriteIdTokenFragmentInLocationHeader() Could not parse the JWT", e);
            throw new AccessTokenReWriteException("Failed to parse the responseEntity's Location fragment id_token", e);
        }

        return rewrittenResponseEntity;
    }

}
