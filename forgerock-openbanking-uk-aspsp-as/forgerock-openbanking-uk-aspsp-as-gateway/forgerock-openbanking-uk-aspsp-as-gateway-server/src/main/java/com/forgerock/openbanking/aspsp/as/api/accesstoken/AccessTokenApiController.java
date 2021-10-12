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
import com.forgerock.openbanking.aspsp.as.service.MatlsRequestVerificationService;
import com.forgerock.openbanking.aspsp.as.service.PairClientIDAuthMethod;
import com.forgerock.openbanking.aspsp.as.service.headless.accesstoken.HeadLessAccessTokenService;
import com.forgerock.openbanking.common.error.exception.AccessTokenReWriteException;
import com.forgerock.openbanking.common.services.JwtOverridingService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OIDCConstants.GrantType;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.AccessTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.UUID;

import static com.forgerock.openbanking.constants.OIDCConstants.OIDCClaim.CLIENT_ASSERTION;
import static com.forgerock.openbanking.constants.OIDCConstants.TokenEndpointAuthMethods.PRIVATE_KEY_JWT;

@Controller
@Slf4j
public class AccessTokenApiController implements AccessTokenApi {

    private final AMASPSPGateway amGateway;
    private final HeadLessAccessTokenService headLessAccessTokenService;
    private final AMGatewayService amGatewayService;
    private final JwtOverridingService jwtOverridingService;
    private final MatlsRequestVerificationService matlsRequestVerificationService;


    @Autowired
    public AccessTokenApiController(AMASPSPGateway amGateway, HeadLessAccessTokenService headLessAccessTokenService,
                                    AMGatewayService amGatewayService, JwtOverridingService jwtOverridingService,
                                    MatlsRequestVerificationService matlsRequestVerificationService) {
        this.amGateway = amGateway;
        this.headLessAccessTokenService = headLessAccessTokenService;
        this.amGatewayService = amGatewayService;
        this.jwtOverridingService = jwtOverridingService;
        this.matlsRequestVerificationService = matlsRequestVerificationService;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_PISP', 'ROLE_AISP', 'ROLE_CBPII')")
    public ResponseEntity getAccessToken(MultiValueMap<String, String> paramMap, String authorization,
                                         Principal principal,
                                         HttpServletRequest request) throws OBErrorResponseException, OBErrorException {
        log.debug("getAccessToken(), paramMap {}", paramMap);
        PairClientIDAuthMethod clientIDAuthMethod =
                matlsRequestVerificationService.verifyMATLSMatchesRequest(paramMap, authorization, principal);

        AMGateway amGateway = this.amGateway;
        //The token endpoint can also be used as audience, as per OIDC spec
        if (clientIDAuthMethod.getAuthMethod() == PRIVATE_KEY_JWT) {
            String clientAssertion = paramMap.getFirst(CLIENT_ASSERTION);
            if (clientAssertion == null || clientAssertion.isBlank()) {
                log.debug("getAccessToken() clientAssertion was null or blank");
                throw new OBErrorResponseException(
                        OBRIErrorType.ACCESS_TOKEN_INVALID.getHttpStatus(),
                        OBRIErrorResponseCategory.ACCESS_TOKEN,
                        OBRIErrorType.ACCESS_TOKEN_INVALID.toOBError1("No client_assertion in body"
                        ));
            }
            amGateway = amGatewayService.getAmGateway(clientAssertion);
        }

        // can throw a UnsupportedOIDCGrantTypeException
        GrantType grantType = GrantType.fromType(paramMap.getFirst(OIDCConstants.OIDCClaim.GRANT_TYPE));

        ResponseEntity<AccessTokenResponse> responseEntity = getAccessToken(paramMap, request,
                clientIDAuthMethod, amGateway, grantType);

        try {
            responseEntity = jwtOverridingService.rewriteAccessTokenResponseIdToken(responseEntity);
        } catch (AccessTokenReWriteException e) {
            log.debug("Failed to rewrite the access token response's id_token.", e);
            String supportUID = UUID.randomUUID().toString();
            throw new OBErrorResponseException(
                    OBRIErrorType.ACCESS_TOKEN_INVALID_ID_TOKEN.getHttpStatus(),
                    OBRIErrorResponseCategory.ACCESS_TOKEN,
                    OBRIErrorType.ACCESS_TOKEN_INVALID_ID_TOKEN.toOBError1(supportUID));
        }

        return responseEntity;
    }

    @NotNull
    private ResponseEntity<AccessTokenResponse> getAccessToken(MultiValueMap<String, String> paramMap,
                                                               HttpServletRequest request,
                                                               PairClientIDAuthMethod clientIDAuthMethod,
                                                               AMGateway amGateway, GrantType grantType)
            throws OBErrorResponseException, OBErrorException {
        ResponseEntity<AccessTokenResponse> responseEntity;
        switch (grantType) {
            case HEADLESS_AUTH: {
                responseEntity = headLessAccessTokenService.getAccessToken(amGateway, clientIDAuthMethod, paramMap,
                        request);
                HttpStatus statusCode = responseEntity.getStatusCode();
                if (!statusCode.is2xxSuccessful() && !statusCode.is3xxRedirection()) {
                    log.warn("getAccessToken() unsuccessful call to headlessAuthTokenService. StatusCode: {}, body: {}",
                            statusCode, responseEntity.getBody());
                    throw new OBErrorResponseException(
                            OBRIErrorType.ACCESS_TOKEN_INVALID.getHttpStatus(),
                            OBRIErrorResponseCategory.ACCESS_TOKEN,
                            OBRIErrorType.ACCESS_TOKEN_INVALID.toOBError1(responseEntity.getBody())
                    );
                }
                log.debug("getAccessToken() response from headLessAccessTokenService; {}", responseEntity);
                break;
            }
            case CLIENT_CREDENTIAL:
            case AUTHORIZATION_CODE:
            case PASSWORD:
            case REFRESH_TOKEN:
            default: {
                //proxy to AM
                ResponseEntity responseFromAM = amGateway.toAM(request, new HttpHeaders(),
                        new ParameterizedTypeReference<AccessTokenResponse>() {
                        }, paramMap);
                log.debug("getAccessToken() response from AM; {}", responseFromAM);
                HttpStatus statusCode = responseFromAM.getStatusCode();
                if (!statusCode.is2xxSuccessful() && !statusCode.is3xxRedirection()) {
                    log.warn("getAccessToken() Un-successful call to AM to get access token. Status code: {}, body " +
                            "{}", responseFromAM.getStatusCode(), responseFromAM.getBody());
                    throw new OBErrorResponseException(
                            OBRIErrorType.ACCESS_TOKEN_INVALID.getHttpStatus(),
                            OBRIErrorResponseCategory.ACCESS_TOKEN,
                            OBRIErrorType.ACCESS_TOKEN_INVALID.toOBError1(responseFromAM.getBody()));
                }
                AccessTokenResponse accessTokenResponse = (AccessTokenResponse) responseFromAM.getBody();
                log.debug("getAccessToken() accessTokenResponse from AM is {}", accessTokenResponse);
                responseEntity = ResponseEntity.status(responseFromAM.getStatusCode()).body(accessTokenResponse);
                log.debug("getAccessToken() new reponse to return is {}", responseEntity);
            }
        }
        return responseEntity;
    }
}
