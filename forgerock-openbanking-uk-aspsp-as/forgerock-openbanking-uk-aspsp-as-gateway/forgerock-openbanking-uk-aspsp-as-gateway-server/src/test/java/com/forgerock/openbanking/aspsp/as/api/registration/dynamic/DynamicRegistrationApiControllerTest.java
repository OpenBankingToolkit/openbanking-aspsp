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
package com.forgerock.openbanking.aspsp.as.api.registration.dynamic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.as.service.SSAService;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.common.error.exception.oauth2.*;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.utils.extractor.TokenExtractor;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class DynamicRegistrationApiControllerTest {

    @Mock
    TppStoreService tppStoreService;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    TokenExtractor tokenExtractor;

    @Mock
    TppRegistrationService tppRegistrationService;

    List<String> supportedAuthMethod;

    @Mock
    SSAService ssaService;

    private DynamicRegistrationApiController dynamicRegistrationApiController;
    private final String clientId = "clientId";
    private final String authorizationString = "Bearer tpps-registration-access-token";
    private final String tppName = "tppName";
    private final String principalName = "principalName";


    @Before
    public void setUp(){
        dynamicRegistrationApiController = new DynamicRegistrationApiController(tppStoreService, objectMapper,
                tokenExtractor, tppRegistrationService, supportedAuthMethod);
    }

    @Test
    public void throwsOAuth2MissingAuthInfoExceptionWhenNoAuthenticationHeaderToken_unregister(){
        // given
        Principal principal = mock(Principal.class);
        given(principal.getName()).willReturn(this.principalName);


        // when
        OAuth2BearerTokenUsageMissingAuthInfoException exception = catchThrowableOfType(()->
                dynamicRegistrationApiController.unregister(clientId, null,
                        principal), OAuth2BearerTokenUsageMissingAuthInfoException.class);
        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getHttpStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getWwwAuthenticateResponseHeaderValue()).isEqualTo(
                OAuth2Constants.OAUTH2_WWW_AUTHENTICATE_HEADER_VALUE_BEARER);
    }

    @Test
    public void throwsOAuth2InvalidClientIdWhenPrincipalIsNull_unregister() {
        // given
        Principal principal = mock(Principal.class);

        // when
        OAuth2InvalidClientException exception = catchThrowableOfType(()->
                dynamicRegistrationApiController.unregister(clientId, authorizationString,
                null), OAuth2InvalidClientException.class);
        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getHttpStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getRfc6750ErrorCode()).isEqualTo("invalid_client");
    }

    @Test
    public void throwsOAuth2InvalidClientExceptionWhenClientIdIsNull_unregister() throws OBErrorResponseException {
        // given
        DynamicRegistrationApiController dynamicRegistrationApiController =
                new DynamicRegistrationApiController(tppStoreService, objectMapper, tokenExtractor,
                        tppRegistrationService, supportedAuthMethod);
        String clientId = null;
        Principal principal = mock(Principal.class);
        given(principal.getName()).willReturn(tppName);
        given(tppStoreService.findByClientId(tppName)).willReturn(Optional.of(this.getValidTpp()));

        // when
        OAuth2InvalidClientException exception = catchThrowableOfType(()->
                dynamicRegistrationApiController.unregister(clientId, this.authorizationString,
                        principal), OAuth2InvalidClientException.class);
        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getHttpStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getRfc6750ErrorCode()).isEqualTo(OAuth2Exception.INVALID_CLIENT);
    }

    @Test
    public void throwsOAuth2ClientExceptionWhenTppNotRegistered_unregister(){
        // given
        DynamicRegistrationApiController dynamicRegistrationApiController =
                new DynamicRegistrationApiController(tppStoreService, objectMapper, tokenExtractor,
                        tppRegistrationService, supportedAuthMethod);
        String clientId = this.clientId;
        Principal principal = mock(Principal.class);
        given(principal.getName()).willReturn(tppName);
        given(tppStoreService.findByClientId(tppName)).willReturn(Optional.empty());

        // when
        OAuth2InvalidClientException exception = catchThrowableOfType(()->
                dynamicRegistrationApiController.unregister(clientId, this.authorizationString, principal),
                OAuth2InvalidClientException.class);
        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getHttpStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getRfc6750ErrorCode()).isEqualTo(OAuth2Exception.INVALID_CLIENT);
    }

    @Test
    public void throwsOAuth2BearerTokenUsageInvalidTokenExceptionWhenInvalidAuthorizationString_unregister() {
        // given
        DynamicRegistrationApiController dynamicRegistrationApiController =
                new DynamicRegistrationApiController(tppStoreService, objectMapper, tokenExtractor,
                        tppRegistrationService, supportedAuthMethod);
        String clientId = this.clientId;
        Principal principal = mock(Principal.class);
        given(principal.getName()).willReturn(tppName);
        given(tppStoreService.findByClientId(tppName)).willReturn(Optional.of(this.getValidTpp()));
        given(tokenExtractor.extract(this.authorizationString)).willThrow(new AuthenticationServiceException("test"));

        // when
        OAuth2BearerTokenUsageInvalidTokenException exception = catchThrowableOfType(()->
                        dynamicRegistrationApiController.unregister(clientId, this.authorizationString, principal),
                OAuth2BearerTokenUsageInvalidTokenException.class);
        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getHttpStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getWwwAuthenticateResponseHeaderValue()).isNull();
        assertThat(exception.getErrorCode()).isEqualTo(OAuth2BearerTokenUsageException.ErrorEnum.INVALID_TOKEN);
    }

    /**
     * This tests the scenario when the tpp, as identified by MATLS, is requesting that a client id different from
     * the one associated with the TPP associated with the client_id they are requesting to delete. i.e. when a tpp
     * is requesting the deletion of a client registration they didn't make.
     */
    @Test
    public void throwsOAuth2BearerTokenUsageInvalidTokenExceptionWhenNotOwner_unregister() {
        // given
        DynamicRegistrationApiController dynamicRegistrationApiController =
                new DynamicRegistrationApiController(tppStoreService, objectMapper, tokenExtractor,
                        tppRegistrationService, supportedAuthMethod);
        String clientId = this.clientId;
        Principal principal = mock(Principal.class);
        given(principal.getName()).willReturn(tppName);
        given(tppStoreService.findByClientId(tppName)).willReturn(Optional.of(this.getValidTpp()));
        String accessToken = "not-the-tpps-registration-access-token";
        given(tokenExtractor.extract(this.authorizationString)).willReturn(accessToken);

        // when
        OAuth2BearerTokenUsageInvalidTokenException exception = catchThrowableOfType(()->
                        dynamicRegistrationApiController.unregister(clientId, this.authorizationString, principal),
                OAuth2BearerTokenUsageInvalidTokenException.class);
        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getHttpStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getWwwAuthenticateResponseHeaderValue()).isNull();
        assertThat(exception.getErrorCode()).isEqualTo(OAuth2BearerTokenUsageException.ErrorEnum.INVALID_TOKEN);
    }

    /**
     * This tests the scenario when the tpp, as identified by MATLS, is requesting that a client id different from
     * the one associated with the TPP associated with the client_id they are requesting to delete. i.e. when a tpp
     * is requesting the deletion of a client registration they didn't make.
     */
    @Test
    public void successful_unregister() throws OAuth2BearerTokenUsageMissingAuthInfoException,
            OAuth2BearerTokenUsageInvalidTokenException, OAuth2InvalidClientException {
        // given
        DynamicRegistrationApiController dynamicRegistrationApiController =
                new DynamicRegistrationApiController(tppStoreService, objectMapper, tokenExtractor,
                        tppRegistrationService, supportedAuthMethod);
        String clientId = this.clientId;
        Principal principal = mock(Principal.class);
        given(principal.getName()).willReturn(tppName);
        given(tppStoreService.findByClientId(tppName)).willReturn(Optional.of(this.getValidTpp()));
        String accessToken = "tpps-registration-access-token";
        given(tokenExtractor.extract(this.authorizationString)).willReturn(accessToken);

        // when

        ResponseEntity<?> unregisterResponse = dynamicRegistrationApiController.unregister(clientId,
                this.authorizationString, principal);
        // then
        assertThat(unregisterResponse).isNotNull();
        assertThat(unregisterResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getRegisterResult() {
    }

    @Test
    public void testGetRegisterResult() {
    }

    @Test
    public void updateClient() {
    }

    @Test
    public void testUpdateClient() {
    }

    @Test
    public void register() {
    }

    private Tpp getValidTpp(){
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setRegistrationAccessToken("tpps-registration-access-token");
        Tpp tpp =  new Tpp();
        tpp.setRegistrationResponse(registrationResponse);
        tpp.setClientId(this.clientId);
        tpp.setName(this.tppName);
        return tpp;
    }
}