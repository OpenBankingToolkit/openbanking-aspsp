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
package com.forgerock.openbanking.aspsp.as.service.headless.accesstoken;

import com.forgerock.openbanking.am.gateway.AMGateway;
import com.forgerock.openbanking.aspsp.as.api.authorisation.redirect.AuthorisationApi;
import com.forgerock.openbanking.aspsp.as.service.PairClientIDAuthMethod;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.ErrorCode;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

import static com.forgerock.openbanking.constants.OIDCConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;



@RunWith(MockitoJUnitRunner.class)
public class HeadLessAccessTokenServiceTest {

    @Mock
    AuthorisationApi authorisationApi;
    @Mock
    AMGateway amGateway;

    @InjectMocks
    HeadLessAccessTokenService headlessAccessTokenService;

    HttpServletRequest request;
    ParameterizedTypeReference<AccessTokenResponse> typeReference;

    @Before
    public void setUp(){
        this.typeReference = new ParameterizedTypeReference<>() {};
    }

    @Test
    public void success_getAccessToken() throws OBErrorResponseException, OBErrorException, URISyntaxException {
        // Given
        PairClientIDAuthMethod clientIdAuthMethod = getClientIDAuthMethod(TokenEndpointAuthMethods.CLIENT_SECRET_BASIC);
        MultiValueMap<String, String> params = getParamsMap(GrantType.HEADLESS_AUTH);
        HttpHeaders httpHeaders =  new HttpHeaders();
        httpHeaders.setLocation(new URI("http://acme.com/#code=access_code"));
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();

        ResponseEntity<AccessTokenResponse> responseEntity = new ResponseEntity<>(accessTokenResponse,
                httpHeaders, HttpStatus.OK);
        given(authorisationApi.getAuthorisation(params.getFirst(OIDCClaim.RESPONSE_TYPE),
                params.getFirst(OIDCClaim.CLIENT_ID),
                params.getFirst(OIDCClaim.STATE),
                params.getFirst(OIDCClaim.NONCE),
                params.getFirst(OIDCClaim.SCOPE),
                params.getFirst(OIDCClaim.REDIRECT_URI),
                params.getFirst(OIDCClaim.REQUEST),
                true,
                params.getFirst(OIDCClaim.USERNAME),
                params.getFirst(OIDCClaim.PASSWORD),
                "",
                null,
                request)).willReturn(responseEntity);
        String requestBody = "grant_type=authorization_code&redirect_uri=https%3A%2F%2Facme.co.uk&code=access_code";
        given(amGateway.toAM(request, new HttpHeaders(), this.typeReference, requestBody)).willReturn(responseEntity);

        // When
        ResponseEntity<AccessTokenResponse> response = headlessAccessTokenService.getAccessToken(amGateway,
                clientIdAuthMethod, params, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void failWhenNoLocationHeader_getAccessToken() throws OBErrorResponseException, OBErrorException {
        // Given
        PairClientIDAuthMethod clientIdAuthMethod = getClientIDAuthMethod(TokenEndpointAuthMethods.CLIENT_SECRET_BASIC);
        MultiValueMap<String, String> params = getParamsMap(GrantType.HEADLESS_AUTH);
        HttpHeaders httpHeaders =  new HttpHeaders();
        //httpHeaders.setLocation(new URI("http://acme.com/#code=access_code"));
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();

        ResponseEntity<AccessTokenResponse> responseEntity = new ResponseEntity<>(accessTokenResponse,
                httpHeaders, HttpStatus.OK);
        given(authorisationApi.getAuthorisation(params.getFirst(OIDCClaim.RESPONSE_TYPE),
                params.getFirst(OIDCClaim.CLIENT_ID),
                params.getFirst(OIDCClaim.STATE),
                params.getFirst(OIDCClaim.NONCE),
                params.getFirst(OIDCClaim.SCOPE),
                params.getFirst(OIDCClaim.REDIRECT_URI),
                params.getFirst(OIDCClaim.REQUEST),
                true,
                params.getFirst(OIDCClaim.USERNAME),
                params.getFirst(OIDCClaim.PASSWORD),
                "",
                null,
                request)).willReturn(responseEntity);

        // When
        OBErrorResponseException exception =
                catchThrowableOfType(() -> headlessAccessTokenService.getAccessToken(amGateway, clientIdAuthMethod,
                        params, request), OBErrorResponseException.class);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getCategory()).isEqualTo(OBRIErrorResponseCategory.HEADLESS_AUTH);
    }

    @Test
    public void failWhenNoAuthorisation_getAccessToken() throws OBErrorResponseException, OBErrorException {
        // Given
        PairClientIDAuthMethod clientIdAuthMethod = getClientIDAuthMethod(TokenEndpointAuthMethods.CLIENT_SECRET_BASIC);
        MultiValueMap<String, String> params = getParamsMap(GrantType.HEADLESS_AUTH);
        HttpHeaders httpHeaders =  new HttpHeaders();

        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"error\":\"broken stuff\"}",
                httpHeaders, HttpStatus.BAD_REQUEST);
        given(authorisationApi.getAuthorisation(params.getFirst(OIDCClaim.RESPONSE_TYPE),
                params.getFirst(OIDCClaim.CLIENT_ID),
                params.getFirst(OIDCClaim.STATE),
                params.getFirst(OIDCClaim.NONCE),
                params.getFirst(OIDCClaim.SCOPE),
                params.getFirst(OIDCClaim.REDIRECT_URI),
                params.getFirst(OIDCClaim.REQUEST),
                true,
                params.getFirst(OIDCClaim.USERNAME),
                params.getFirst(OIDCClaim.PASSWORD),
                "",
                null,
                request)).willReturn(responseEntity);

        // When
        OBErrorResponseException exception =
                catchThrowableOfType(() -> headlessAccessTokenService.getAccessToken(amGateway, clientIdAuthMethod,
                        params, request), OBErrorResponseException.class);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getCategory()).isEqualTo(OBRIErrorResponseCategory.HEADLESS_AUTH);
        assertThat(exception.getErrors().get(0).getErrorCode()).isEqualTo(ErrorCode.OBRI_HEADLESS_AS_ERROR.toString());
    }

    // ToDo: Extract to test helper class as also used in @AccessTokenApiControllerTest
    private PairClientIDAuthMethod getClientIDAuthMethod(TokenEndpointAuthMethods clientIdAuthMethod) {
        PairClientIDAuthMethod pairClientIDAuthMethod = new PairClientIDAuthMethod();
        pairClientIDAuthMethod.setAuthMethod(clientIdAuthMethod);
        return pairClientIDAuthMethod;
    }

    private MultiValueMap<String, String> getParamsMap(GrantType grantType) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(OIDCClaim.RESPONSE_TYPE, ResponseType.CODE);
        params.add(OIDCClaim.CLIENT_ID, "test_client_id");
        params.add(OIDCClaim.STATE, "test_state");
        params.add(OIDCClaim.NONCE, "test_nonce");
        params.add(OIDCClaim.SCOPE, "test_scope");
        params.add(OIDCClaim.REDIRECT_URI, "https://acme.co.uk");
        params.add(OIDCClaim.REQUEST, "test_request");
        params.add(OIDCClaim.USERNAME, "test_username");
        params.add(OIDCClaim.PASSWORD, "test_password");
        params.add(OIDCClaim.GRANT_TYPE, grantType.type);
        return params;
    }
}