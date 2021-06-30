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
package com.forgerock.openbanking.aspsp.as.service;

import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
import java.security.Principal;
import java.text.ParseException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MatlsRequestVerificationService {


    private static TppStoreService tppStoreService;
    private static final String CLIENT_ID = "client_id";
    private static final String BASIC = "Basic";

    public MatlsRequestVerificationService(TppStoreService tppStoreService) {
        this.tppStoreService = tppStoreService;
    }

    public PairClientIDAuthMethod verifyMATLSMatchesRequest(MultiValueMap paramMap, String authorization,
                                                            Principal principal)
            throws OBErrorResponseException {
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();

        //the X509 authentication already check that the TPP cert exist but not if tpp is registered
        Tpp tpp = getTppRegistered(currentUser);

        PairClientIDAuthMethod pairClientIDAuthMethod;

        String clientAssertion = (String) paramMap.getFirst(OIDCConstants.OIDCClaim.CLIENT_ASSERTION);
        if (clientAssertion != null) {
            pairClientIDAuthMethod = getPairClientIdAuthMethodFromClientAssertion(clientAssertion, paramMap);
        } else if (authorization != null) {
            pairClientIDAuthMethod = getPairClientIdAuthMethodFromAuthorization(authorization, paramMap);
        } else if (paramMap.get("client_secret") != null) {
            pairClientIDAuthMethod = getPairClientIdAuthMethodWhenClientSecret(paramMap);
        } else {
            pairClientIDAuthMethod = getPairClientIdAuthMethodDefault(paramMap);
        }
        // can throw UnsupportedOIDCAuthMethodsException
        OIDCRegistrationResponse registrationResponse = tpp.getRegistrationResponse();
        String tokenEndpointAuthMethod = registrationResponse.getTokenEndpointAuthMethod();
        OIDCConstants.TokenEndpointAuthMethods authMethodsFromTpp = OIDCConstants.TokenEndpointAuthMethods.fromType(tokenEndpointAuthMethod);

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

    private Tpp getTppRegistered(UserDetails currentUser) throws OBErrorResponseException {
        // if the current user have unregistered tpp authority means that isn't registered yet.
        List<String> authorities = currentUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Optional<Tpp> optionalTpp = tppStoreService.findByClientId(currentUser.getUsername());
        if (!optionalTpp.isPresent() || authorities.contains(OBRIRole.UNREGISTERED_TPP)) {
            throw new OBErrorResponseException(
                    OBRIErrorType.TPP_REGISTRATION_NOT_REGISTERED.getHttpStatus(),
                    OBRIErrorResponseCategory.ACCESS_TOKEN,
                    OBRIErrorType.TPP_REGISTRATION_NOT_REGISTERED.toOBError1(currentUser.getUsername() + ", identified from certificate, "));
        }
        return optionalTpp.get();
    }

    private PairClientIDAuthMethod getPairClientIdAuthMethodFromClientAssertion(String clientAssertion, MultiValueMap paramMap) throws OBErrorResponseException {
        PairClientIDAuthMethod pairClientIDAuthMethod = new PairClientIDAuthMethod();
        if (clientAssertion == null || clientAssertion.isEmpty() || clientAssertion.isBlank()) {
            throw new OBErrorResponseException(
                    OBRIErrorType.ACCESS_TOKEN_INVALID.getHttpStatus(),
                    OBRIErrorResponseCategory.ACCESS_TOKEN,
                    OBRIErrorType.ACCESS_TOKEN_INVALID.toOBError1("No client_assertion in body"));
        }
        log.debug("Read client ID from client assertion found: {}", clientAssertion);
        try {
            SignedJWT jws = (SignedJWT) JWTParser.parse(clientAssertion);
            pairClientIDAuthMethod.clientId = jws.getJWTClaimsSet().getSubject();
            Object clientIDFromBody = paramMap.getFirst(CLIENT_ID);
            if (clientIDFromBody != null
                    && !pairClientIDAuthMethod.clientId.equals(clientIDFromBody)) {
                log.error("Client ID from the request body {} is not matching the client assertion sub {}",
                        clientIDFromBody, pairClientIDAuthMethod.clientId);
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
        return pairClientIDAuthMethod;
    }

    private PairClientIDAuthMethod getPairClientIdAuthMethodFromAuthorization(String authorization, MultiValueMap paramMap) throws OBErrorResponseException {
        log.debug("Read client ID from client authorisation header: {}", authorization);
        PairClientIDAuthMethod pairClientIDAuthMethod = new PairClientIDAuthMethod();
        pairClientIDAuthMethod.clientId = clientIDFromBasic(authorization);
        Object clientIDFromBody = paramMap.getFirst(CLIENT_ID);
        if (clientIDFromBody != null
                && !pairClientIDAuthMethod.clientId.equals(clientIDFromBody)) {
            log.error("Client ID from the request body {} is not matching the client secret basic {}", clientIDFromBody, pairClientIDAuthMethod.clientId);
            throw new OBErrorResponseException(
                    OBRIErrorType.ACCESS_TOKEN_CLIENT_ID_MISS_MATCH.getHttpStatus(),
                    OBRIErrorResponseCategory.ACCESS_TOKEN,
                    OBRIErrorType.ACCESS_TOKEN_CLIENT_ID_MISS_MATCH.toOBError1(clientIDFromBody, pairClientIDAuthMethod.clientId));
        }
        pairClientIDAuthMethod.authMethod = OIDCConstants.TokenEndpointAuthMethods.CLIENT_SECRET_BASIC;
        return pairClientIDAuthMethod;
    }

    private PairClientIDAuthMethod getPairClientIdAuthMethodWhenClientSecret(MultiValueMap paramMap) throws OBErrorResponseException {
        PairClientIDAuthMethod pairClientIDAuthMethod = new PairClientIDAuthMethod();
        log.debug("Read client ID from client body parameter 'client_id'");
        pairClientIDAuthMethod.clientId = (String) paramMap.getFirst(CLIENT_ID);
        pairClientIDAuthMethod.authMethod = OIDCConstants.TokenEndpointAuthMethods.CLIENT_SECRET_POST;
        return pairClientIDAuthMethod;
    }

    private PairClientIDAuthMethod getPairClientIdAuthMethodDefault(MultiValueMap paramMap) throws OBErrorResponseException {
        PairClientIDAuthMethod pairClientIDAuthMethod = new PairClientIDAuthMethod();
        log.debug("Read client ID from client body parameter 'client_id'");
        pairClientIDAuthMethod.authMethod = OIDCConstants.TokenEndpointAuthMethods.TLS_CLIENT_AUTH;
        pairClientIDAuthMethod.clientId = (String) paramMap.getFirst(CLIENT_ID);
        return pairClientIDAuthMethod;
    }

    private String clientIDFromBasic(String authorization) {
        if (authorization != null && authorization.startsWith(BASIC)) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring(BASIC.length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            return values[0];
        }
        return null;
    }

}
