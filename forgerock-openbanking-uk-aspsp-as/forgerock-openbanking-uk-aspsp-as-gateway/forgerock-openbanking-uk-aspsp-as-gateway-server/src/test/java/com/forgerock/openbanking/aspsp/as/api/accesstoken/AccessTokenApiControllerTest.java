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
import com.forgerock.openbanking.am.services.AMGatewayService;
import com.forgerock.openbanking.aspsp.as.service.MatlsRequestVerificationService;
import com.forgerock.openbanking.aspsp.as.service.PairClientIDAuthMethod;
import com.forgerock.openbanking.aspsp.as.service.headless.accesstoken.HeadLessAccessTokenService;
import com.forgerock.openbanking.common.services.JwtOverridingService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.oidc.AccessTokenResponse;
import org.junit.After;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class AccessTokenApiControllerTest {

    @Mock
    private AMASPSPGateway amGateway;
    @Mock
    private HeadLessAccessTokenService headLessAccessTokenService;
    @Mock
    private AMGatewayService amGatewayService;
    @Mock
    private JwtOverridingService jwtOverridingService;
    @Mock
    private MatlsRequestVerificationService matlsRequestVerificationService;

    @InjectMocks
    AccessTokenApiController accessTokenApiController;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAccessToken() throws OBErrorResponseException, OBErrorException, JwtOverridingService.AccessTokenReWriteException {
        // Given
        MultiValueMap<String, String> params  = new LinkedMultiValueMap<>();
        params.add(OIDCConstants.OIDCClaim.GRANT_TYPE, OIDCConstants.GrantType.CLIENT_CREDENTIAL.type);
        String authorization = "AuthString";
        Authentication principal = mock(Authentication.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        PairClientIDAuthMethod pairClientIDAuthMethod = mock(PairClientIDAuthMethod.class);
        given(matlsRequestVerificationService.verifyMATLSMatchesRequest(params, authorization, principal))
                .willReturn(pairClientIDAuthMethod);
        HttpHeaders  httpHeaders = new HttpHeaders();
        ParameterizedTypeReference<AccessTokenResponse> parameterizedTypeRef =
                new ParameterizedTypeReference<AccessTokenResponse>() {};
        ResponseEntity responseEntity = new ResponseEntity(null, httpHeaders, HttpStatus.FOUND);
        given(amGateway.toAM(request, httpHeaders, parameterizedTypeRef, params)).willReturn(responseEntity);
        ResponseEntity modifiedResponseEntity = new ResponseEntity(null, httpHeaders, HttpStatus.OK);
        given(jwtOverridingService.rewriteAccessTokenResponseIdToken(responseEntity)).willReturn(modifiedResponseEntity);

        // When
        ResponseEntity result = this.accessTokenApiController.getAccessToken(params, authorization, principal, request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void failsWhenAMReturnsError_getAccessToken() throws OBErrorResponseException {
        // Given
        MultiValueMap<String, String> params  = new LinkedMultiValueMap<>();
        params.add(OIDCConstants.OIDCClaim.GRANT_TYPE, OIDCConstants.GrantType.CLIENT_CREDENTIAL.type);
        String authorization = "AuthString";
        Authentication principal = mock(Authentication.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        PairClientIDAuthMethod pairClientIDAuthMethod = new PairClientIDAuthMethod();
        given(matlsRequestVerificationService.verifyMATLSMatchesRequest(params, authorization, principal))
                .willReturn(pairClientIDAuthMethod);
        HttpHeaders  httpHeaders = new HttpHeaders();
        ParameterizedTypeReference<AccessTokenResponse> parameterizedTypeRef =
                new ParameterizedTypeReference<AccessTokenResponse>() {};
        ResponseEntity responseEntity = new ResponseEntity("{\"error_description\":\"Client authentication failed\"," +
                "\"error\":\"invalid_client\"}", httpHeaders, HttpStatus.BAD_REQUEST);
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
        MultiValueMap<String, String> params  = new LinkedMultiValueMap<>();
        params.add(OIDCConstants.OIDCClaim.GRANT_TYPE, OIDCConstants.GrantType.CLIENT_CREDENTIAL.type);
        String authorization = "AuthString";
        Authentication principal = mock(Authentication.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        PairClientIDAuthMethod pairClientIDAuthMethod = mock(PairClientIDAuthMethod.class);
        given(pairClientIDAuthMethod.getAuthMethod()).willReturn(OIDCConstants.TokenEndpointAuthMethods.PRIVATE_KEY_JWT);
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
}