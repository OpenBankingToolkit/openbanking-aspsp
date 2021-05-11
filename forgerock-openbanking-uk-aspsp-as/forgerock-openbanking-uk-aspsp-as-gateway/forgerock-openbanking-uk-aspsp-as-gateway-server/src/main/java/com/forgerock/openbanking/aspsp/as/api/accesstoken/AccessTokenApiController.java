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

import com.forgerock.openbanking.am.gateway.AMASPSPGateway;
import com.forgerock.openbanking.am.gateway.AMGateway;
import com.forgerock.openbanking.am.services.AMGatewayService;
import com.forgerock.openbanking.analytics.services.TokenUsageService;
import com.forgerock.openbanking.aspsp.as.service.JwtOverridingService;
import com.forgerock.openbanking.aspsp.as.service.headless.accesstoken.HeadLessAccessTokenService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.AccessTokenResponse;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.security.Principal;
import java.text.ParseException;
import java.util.Base64;
import java.util.UUID;

@Controller
@Slf4j
public class AccessTokenApiController implements AccessTokenApi {

    private AMASPSPGateway amGateway;
    private HeadLessAccessTokenService headLessAccessTokenService;
    private TppStoreService tppStoreService;
    private AMGatewayService amGatewayService;
    private JwtOverridingService jwtOverridingService;
    private TokenUsageService tokenUsageService;

    @Autowired
    public AccessTokenApiController(AMASPSPGateway amGateway, HeadLessAccessTokenService headLessAccessTokenService,
                                    TppStoreService tppStoreService, AMGatewayService amGatewayService,
                                    JwtOverridingService jwtOverridingService, TokenUsageService tokenUsageService) {
        this.amGateway = amGateway;
        this.headLessAccessTokenService = headLessAccessTokenService;
        this.tppStoreService = tppStoreService;
        this.amGatewayService = amGatewayService;
        this.jwtOverridingService = jwtOverridingService;
        this.tokenUsageService = tokenUsageService;
    }

    @Override
    public ResponseEntity getAccessToken(MultiValueMap paramMap, String authorization, Principal principal,
                                         HttpServletRequest request) throws OBErrorResponseException, OBErrorException {

        PairClientIDAuthMethod clientIDAuthMethod = verifyMATLSMatchesRequest(paramMap, authorization, principal);

        AMGateway amGateway = this.amGateway;
        //The token endpoint can also be used as audience, as per OIDC spec
        if (clientIDAuthMethod.authMethod == OIDCConstants.TokenEndpointAuthMethods.PRIVATE_KEY_JWT) {
            amGateway = amGatewayService.getAmGateway((String) paramMap.getFirst("client_assertion"));
        }

        OIDCConstants.GrantType grantType = OIDCConstants.GrantType.fromType((String) paramMap.getFirst("grant_type"));
        ResponseEntity responseEntity;
        switch (grantType) {
            case HEADLESS_AUTH:
                responseEntity = headLessAccessTokenService.getAccessToken(amGateway, clientIDAuthMethod, paramMap, request);
                break;
            case CLIENT_CREDENTIAL:
            case AUTHORIZATION_CODE:
            case PASSWORD:
            case REFRESH_TOKEN:
            default:
                //proxy to AM
                ResponseEntity responseFromAM = amGateway.toAM(request, new HttpHeaders(),
                        new ParameterizedTypeReference<AccessTokenResponse>() {}, paramMap);
                responseEntity = ResponseEntity.status(responseFromAM.getStatusCode()).body(responseFromAM.getBody());
        }

        //Rewriting the response as we need to re-sign the id token
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            AccessTokenResponse accessTokenResponse = (AccessTokenResponse) responseEntity.getBody();
            if (accessTokenResponse.getIdToken() != null) {
                responseEntity = rewriteIdToken(accessTokenResponse);
            }
            tokenUsageService.incrementTokenUsage(accessTokenResponse);
        }
        return responseEntity;
    }

    private ResponseEntity rewriteIdToken(AccessTokenResponse accessTokenResponse) throws OBErrorResponseException {
        try {
            accessTokenResponse.setId_token(jwtOverridingService.rewriteJWS(accessTokenResponse.getIdToken()));
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (ParseException e) {
            String supportUID = UUID.randomUUID().toString();
            log.error("Error when parsing ID token {}. Support UID {}", accessTokenResponse.getIdToken(), supportUID,  e);
            throw new OBErrorResponseException(
                    OBRIErrorType.ACCESS_TOKEN_INVALID_ID_TOKEN.getHttpStatus(),
                    OBRIErrorResponseCategory.ACCESS_TOKEN,
                    OBRIErrorType.ACCESS_TOKEN_INVALID_ID_TOKEN.toOBError1(supportUID));
        }
    }

    private PairClientIDAuthMethod verifyMATLSMatchesRequest(MultiValueMap paramMap, String authorization, Principal principal)
            throws OBErrorResponseException {
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        String tppIdFromMATLS = currentUser.getUsername();
        //the X509 authentication already check that the TPP exist
        Tpp tpp = tppStoreService.findByClientId(tppIdFromMATLS).get();
        PairClientIDAuthMethod pairClientIDAuthMethod = new PairClientIDAuthMethod();

        if (paramMap.get("client_assertion") != null) {
            String clientAssertion = (String) paramMap.getFirst("client_assertion");
            log.debug("Read client ID from client assertion found: {}", clientAssertion);
            try {
                SignedJWT jws = (SignedJWT) JWTParser.parse(clientAssertion);
                pairClientIDAuthMethod.clientId = jws.getJWTClaimsSet().getSubject();
                Object clientIDFromBody = paramMap.getFirst("client_id");
                if (clientIDFromBody != null
                        && !pairClientIDAuthMethod.clientId.equals(clientIDFromBody)) {
                    log.error("Client ID from the request body {} is not matching the client assertion sub {}", clientIDFromBody, pairClientIDAuthMethod.clientId);
                    throw new OBErrorResponseException(
                            OBRIErrorType.ACCESS_TOKEN_CLIENT_ID_MISS_MATCH.getHttpStatus(),
                            OBRIErrorResponseCategory.ACCESS_TOKEN,
                            OBRIErrorType.ACCESS_TOKEN_CLIENT_ID_MISS_MATCH.toOBError1(clientIDFromBody, pairClientIDAuthMethod.clientId));
                }
                pairClientIDAuthMethod.authMethod = OIDCConstants.TokenEndpointAuthMethods.PRIVATE_KEY_JWT;
            } catch (ParseException e) {
                log.error("Parse client assertion error", e);
                throw new OBErrorResponseException(
                        OBRIErrorType.ACCESS_TOKEN_CLIENT_ASSERTION_FORMAT_INVALID.getHttpStatus(),
                        OBRIErrorResponseCategory.ACCESS_TOKEN,
                        OBRIErrorType.ACCESS_TOKEN_CLIENT_ASSERTION_FORMAT_INVALID.toOBError1(clientAssertion));
            }
        } else if (authorization != null) {
            log.debug("Read client ID from client authorisation header: {}", authorization);
            pairClientIDAuthMethod.clientId = clientIDFromBasic(authorization);
            Object clientIDFromBody = paramMap.getFirst("client_id");
            if (clientIDFromBody != null
                    && !pairClientIDAuthMethod.clientId.equals(clientIDFromBody)) {
                log.error("Client ID from the request body {} is not matching the client secret basic {}", clientIDFromBody, pairClientIDAuthMethod.clientId);
                throw new OBErrorResponseException(
                        OBRIErrorType.ACCESS_TOKEN_CLIENT_ID_MISS_MATCH.getHttpStatus(),
                        OBRIErrorResponseCategory.ACCESS_TOKEN,
                        OBRIErrorType.ACCESS_TOKEN_CLIENT_ID_MISS_MATCH.toOBError1(clientIDFromBody, pairClientIDAuthMethod.clientId));
            }
            pairClientIDAuthMethod.authMethod = OIDCConstants.TokenEndpointAuthMethods.CLIENT_SECRET_BASIC;
        } else if (paramMap.get("client_secret") != null) {
            log.debug("Read client ID from client body parameter 'client_id'");
            pairClientIDAuthMethod.clientId = (String) paramMap.getFirst("client_id");
            pairClientIDAuthMethod.authMethod = OIDCConstants.TokenEndpointAuthMethods.CLIENT_SECRET_POST;
        } else {
            log.debug("Read client ID from client body parameter 'client_id'");
            pairClientIDAuthMethod.authMethod = OIDCConstants.TokenEndpointAuthMethods.TLS_CLIENT_AUTH;
            pairClientIDAuthMethod.clientId = (String) paramMap.getFirst("client_id");
        }

        OIDCConstants.TokenEndpointAuthMethods authMethodsFromTpp = OIDCConstants.TokenEndpointAuthMethods.fromType(tpp.getRegistrationResponse().getTokenEndpointAuthMethod());

        if (!authMethodsFromTpp.equals(pairClientIDAuthMethod.authMethod)) {
            throw new OBErrorResponseException(
                    OBRIErrorType.ACCESS_TOKEN_WRONG_AUTH_METHOD.getHttpStatus(),
                    OBRIErrorResponseCategory.ACCESS_TOKEN,
                    OBRIErrorType.ACCESS_TOKEN_WRONG_AUTH_METHOD.toOBError1(pairClientIDAuthMethod.authMethod.type, authMethodsFromTpp.type));
        }

        if (!tpp.getClientId().equals(pairClientIDAuthMethod.clientId)) {
            throw new OBErrorResponseException(
                    OBRIErrorType.ACCESS_TOKEN_CREDENTIAL_NOT_MATCHING_CLIENT_CERTS.getHttpStatus(),
                    OBRIErrorResponseCategory.ACCESS_TOKEN,
                    OBRIErrorType.ACCESS_TOKEN_CREDENTIAL_NOT_MATCHING_CLIENT_CERTS.toOBError1(tpp.getClientId(), pairClientIDAuthMethod.clientId, pairClientIDAuthMethod.authMethod.type));
        }
        return pairClientIDAuthMethod;
    }

    private String clientIDFromBasic(String authorization) {
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            return values[0];
        }
        return null;
    }

    public class PairClientIDAuthMethod {
        public String clientId;
        public OIDCConstants.TokenEndpointAuthMethods authMethod;
    }
}
