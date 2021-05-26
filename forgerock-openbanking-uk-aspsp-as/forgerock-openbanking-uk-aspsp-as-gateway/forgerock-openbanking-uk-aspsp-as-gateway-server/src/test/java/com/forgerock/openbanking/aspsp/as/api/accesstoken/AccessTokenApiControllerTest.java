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
import com.forgerock.openbanking.aspsp.as.service.MatlsRequestVerificationService;
import com.forgerock.openbanking.aspsp.as.service.PairClientIDAuthMethod;
import com.forgerock.openbanking.aspsp.as.service.headless.accesstoken.HeadLessAccessTokenService;
import com.forgerock.openbanking.common.error.exception.AccessTokenReWriteException;
import com.forgerock.openbanking.common.services.JwtOverridingService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OIDCConstants.GrantType;
import com.forgerock.openbanking.constants.OIDCConstants.TokenEndpointAuthMethods;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.oidc.AccessTokenResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;

import static com.forgerock.openbanking.constants.OIDCConstants.GrantType.CLIENT_CREDENTIAL;
import static com.forgerock.openbanking.constants.OIDCConstants.TokenEndpointAuthMethods.CLIENT_SECRET_BASIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class AccessTokenApiControllerTest {

    private final String authorization = "AuthString";
    @InjectMocks
    AccessTokenApiController accessTokenApiController;
    private ParameterizedTypeReference<AccessTokenResponse> parameterizedTypeRef;
    private HttpHeaders httpHeaders;
    private String amErrorResponse;
    @Mock
    private AMASPSPGateway amGateway;
    @Mock
    private HeadLessAccessTokenService headLessAccessTokenService;
    @Mock
    private JwtOverridingService jwtOverridingService;
    @Mock
    private MatlsRequestVerificationService matlsRequestVerificationService;
    @Mock
    private Authentication principal;
    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() {
        this.parameterizedTypeRef = new ParameterizedTypeReference<>() {
        };
        this.httpHeaders = new HttpHeaders();
        this.amErrorResponse = "{\"error_description\":\"Client authentication failed\", \"error\":\"invalid_client\"}";
    }

    @Test
    public void successWithClientCredentials_getAccessToken() throws OBErrorResponseException, OBErrorException,
            AccessTokenReWriteException {
        // Given
        MultiValueMap<String, String> params = getParamsMap(CLIENT_CREDENTIAL);
        PairClientIDAuthMethod clientIDAndAuthMethod = getClientIDAuthMethod(CLIENT_SECRET_BASIC);
        given(matlsRequestVerificationService.verifyMATLSMatchesRequest(params, this.authorization, this.principal))
                .willReturn(clientIDAndAuthMethod);


        ResponseEntity<Object> responseEntity = new ResponseEntity<>(null, this.httpHeaders, HttpStatus.FOUND);
        given(amGateway.toAM(request, httpHeaders, parameterizedTypeRef, params)).willReturn(responseEntity);

        ResponseEntity<Object> modifiedResponseEntity = new ResponseEntity<>(null, this.httpHeaders, HttpStatus.OK);
        given(jwtOverridingService.rewriteAccessTokenResponseIdToken(responseEntity)).willReturn(modifiedResponseEntity);

        // When
        ResponseEntity result = this.accessTokenApiController.getAccessToken(params, this.authorization, principal,
                this.request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void failsWhenAMReturnsError_getAccessToken() throws OBErrorResponseException {
        // Given
        MultiValueMap<String, String> params = getParamsMap(CLIENT_CREDENTIAL);
        PairClientIDAuthMethod pairClientIDAuthMethod = getClientIDAuthMethod(CLIENT_SECRET_BASIC);
        given(matlsRequestVerificationService.verifyMATLSMatchesRequest(params, this.authorization, this.principal))
                .willReturn(pairClientIDAuthMethod);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(this.amErrorResponse, httpHeaders,
                HttpStatus.BAD_REQUEST);
        given(amGateway.toAM(request, httpHeaders, parameterizedTypeRef, params)).willReturn(responseEntity);


        // When
        OBErrorResponseException e = catchThrowableOfType(() -> this.accessTokenApiController.getAccessToken(params,
                authorization, principal, request), OBErrorResponseException.class);

        // Then
        assertThat(e).isNotNull();
        assertThat(e.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(e.getCategory()).isEqualTo(OBRIErrorResponseCategory.ACCESS_TOKEN);
    }

    @Test
    public void failsWhenNoClientCredentials_getAccessToken() throws OBErrorResponseException {
        // Given
        MultiValueMap<String, String> params = getParamsMap(CLIENT_CREDENTIAL);
        PairClientIDAuthMethod pairClientIDAuthMethod = getClientIDAuthMethod(TokenEndpointAuthMethods.PRIVATE_KEY_JWT);
        given(matlsRequestVerificationService.verifyMATLSMatchesRequest(params, authorization, principal))
                .willReturn(pairClientIDAuthMethod);

        // When
        OBErrorResponseException e = catchThrowableOfType(() -> this.accessTokenApiController.getAccessToken(params,
                authorization, principal, request), OBErrorResponseException.class);

        // Then
        assertThat(e).isNotNull();
        assertThat(e.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(e.getCategory()).isEqualTo(OBRIErrorResponseCategory.ACCESS_TOKEN);
    }


    @Test
    public void successWithHeadlessAuth_getAccessToken() throws OBErrorResponseException, OBErrorException,
            AccessTokenReWriteException {
        // Given
        MultiValueMap<String, String> params = getParamsMap(GrantType.HEADLESS_AUTH);
        PairClientIDAuthMethod pairClientIDAuthMethod = getClientIDAuthMethod(CLIENT_SECRET_BASIC);
        given(matlsRequestVerificationService.verifyMATLSMatchesRequest(params, authorization, principal))
                .willReturn(pairClientIDAuthMethod);

        ResponseEntity<AccessTokenResponse> responseEntity = new ResponseEntity<>(null, httpHeaders, HttpStatus.FOUND);
        given(headLessAccessTokenService.getAccessToken(amGateway, pairClientIDAuthMethod, params, request))
                .willReturn(responseEntity);

        ResponseEntity<Object> modifiedResponseEntity = new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
        given(jwtOverridingService.rewriteAccessTokenResponseIdToken(responseEntity)).willReturn(modifiedResponseEntity);

        // When
        ResponseEntity result = this.accessTokenApiController.getAccessToken(params, authorization, principal,
                request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    private MultiValueMap<String, String> getParamsMap(GrantType grantType) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(OIDCConstants.OIDCClaim.GRANT_TYPE, grantType.type);
        return params;
    }

    private PairClientIDAuthMethod getClientIDAuthMethod(TokenEndpointAuthMethods clientIdAuthMethod) {
        PairClientIDAuthMethod pairClientIDAuthMethod = new PairClientIDAuthMethod();
        pairClientIDAuthMethod.setAuthMethod(clientIdAuthMethod);
        return pairClientIDAuthMethod;
    }
}