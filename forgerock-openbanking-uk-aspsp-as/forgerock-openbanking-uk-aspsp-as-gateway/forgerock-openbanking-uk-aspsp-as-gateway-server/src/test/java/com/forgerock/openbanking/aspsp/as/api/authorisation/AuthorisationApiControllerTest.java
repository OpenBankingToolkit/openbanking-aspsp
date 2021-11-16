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
package com.forgerock.openbanking.aspsp.as.api.authorisation;

import com.forgerock.openbanking.am.gateway.AMGateway;
import com.forgerock.openbanking.am.services.AMGatewayService;
import com.forgerock.openbanking.analytics.services.TokenUsageService;
import com.forgerock.openbanking.aspsp.as.api.authorisation.redirect.AuthorisationApiController;
import com.forgerock.openbanking.aspsp.as.api.oauth2.discovery.DiscoveryConfig;
import com.forgerock.openbanking.aspsp.as.service.headless.authorisation.HeadLessAuthorisationService;
import com.forgerock.openbanking.common.model.rcs.RedirectionAction;
import com.forgerock.openbanking.common.services.JwtOverridingService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static com.forgerock.openbanking.aspsp.as.api.authorisation.JwtTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AuthorisationApiControllerTest {

    @Mock
    private HeadLessAuthorisationService headLessAuthorisationService;
    @Mock
    private TppStoreService tppStoreService;
    @Mock
    private CryptoApiClient cryptoApiClient;
    @Mock
    private AMGatewayService amGatewayService;
    @Mock
    private DiscoveryConfig discoveryConfig;
    @Mock
    private JwtOverridingService jwtOverridingService;
    @Mock
    private TokenUsageService tokenUsageService;

    private AuthorisationApiController authorisationApiController ;
    private final String clientId = "98e119f6-196f-4296-98d4-f1a2f445bca2";


    @Before
    public void setUp(){
        this.authorisationApiController = new AuthorisationApiController(this.headLessAuthorisationService,
                this.cryptoApiClient, this.tppStoreService, "cookieName", false, this.amGatewayService,
                this.jwtOverridingService, this.tokenUsageService, this.discoveryConfig);
    }

    @Test
    public void shouldGetAuthorisationGivenAllScopes() throws OBErrorException, OBErrorResponseException,
            InvalidTokenException, ParseException, IOException {
        // Given
        String clientId = "98e119f6-196f-4296-98d4-f1a2f445bca2";
        List<String> responseTypes = List.of("code id_token");
        given(discoveryConfig.getSupportedResponseTypes()).willReturn(responseTypes);
        String jwt = toEncodedSignedTestJwt("jwt/authorisation.jwt");
        Tpp tpp = new Tpp();
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setJwks_uri("url");
        tpp.setRegistrationResponse(registrationResponse);
        given(tppStoreService.findByClientId(clientId)).willReturn(Optional.of(tpp));
        SignedJWT signedJwt = mock(SignedJWT.class);
        given(cryptoApiClient.validateJws(anyString(), anyString(), anyString() )).willReturn(signedJwt);
        AMGateway amGateway = mock(AMGateway.class);
        given(amGatewayService.getAmGateway(jwt)).willReturn(amGateway);
        String state =  "10d260bf-a7d9-444a-92d9-7b7a5f088208";
        String scopes = "openid accounts payments";
        given(headLessAuthorisationService.getAuthorisation(amGateway, responseTypes.get(0), clientId, state, null,
                scopes, null, jwt, null, null)).willReturn(new ResponseEntity(HttpStatus.FOUND));

        // When
        ResponseEntity responseEntity = authorisationApiController.getAuthorisation(responseTypes.get(0), clientId,
                null, null, scopes, null, jwt, true, null, null, null, null, null);

        // Then no exception
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

    @Test
    public void shouldNotThrowExceptionContainAllScopesAnyOrder() throws OBErrorException, OBErrorResponseException,
            InvalidTokenException, ParseException, IOException {
        // Given
        String clientId = "98e119f6-196f-4296-98d4-f1a2f445bca2";
        List<String> responseTypes = List.of("code id_token");
        given(discoveryConfig.getSupportedResponseTypes()).willReturn(responseTypes);
        String jwt = toEncodedSignedTestJwt("jwt/authorisation.jwt");
        Tpp tpp = new Tpp();
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setJwks_uri("url");
        tpp.setRegistrationResponse(registrationResponse);
        given(tppStoreService.findByClientId(clientId)).willReturn(Optional.of(tpp));
        SignedJWT signedJwt = mock(SignedJWT.class);
        given(cryptoApiClient.validateJws(anyString(), anyString(), anyString() )).willReturn(signedJwt);

        // When
        authorisationApiController.getAuthorisation(responseTypes.get(0), clientId, null,
                null, "payments openid accounts", null, jwt, true, null, null, null, null, null);

        // Then no exception
    }

    @Test
    public void shouldNotThrowExceptionWhenNoUserInfo() throws OBErrorException, OBErrorResponseException,
            InvalidTokenException, ParseException, IOException {
        // Given
        String clientId = "98e119f6-196f-4296-98d4-f1a2f445bca2";
        List<String> responseTypes = List.of("code id_token");
        given(discoveryConfig.getSupportedResponseTypes()).willReturn(responseTypes);
        String jwt = toEncodedSignedTestJwt("jwt/authorisation-no-user-info.jwt");
        Tpp tpp = new Tpp();
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setJwks_uri("url");
        tpp.setRegistrationResponse(registrationResponse);
        given(tppStoreService.findByClientId(clientId)).willReturn(Optional.of(tpp));
        SignedJWT signedJwt = mock(SignedJWT.class);
        given(cryptoApiClient.validateJws(anyString(), anyString(), anyString() )).willReturn(signedJwt);

        // When
        authorisationApiController.getAuthorisation(responseTypes.get(0), clientId, null,
                null, "payments openid accounts", null, jwt, true, null, null, null, null, null);

        // Then no exception
    }

    @Test
    public void shouldReturnRedirectActionWhenJwtScopesDoNotMatchQueryParamScope()
            throws OBErrorException, OBErrorResponseException {
        // Given
        List<String> responseTypes = List.of("code id_token");
        given(discoveryConfig.getSupportedResponseTypes()).willReturn(responseTypes);
        String jwt = toEncodedSignedTestJwt("jwt/authorisation.jwt");
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setJwks_uri("url");
        Tpp tpp = new Tpp();
        tpp.setRegistrationResponse(registrationResponse);
        given(tppStoreService.findByClientId(this.clientId)).willReturn(Optional.of(tpp));


        // When
        ResponseEntity responseEntity = authorisationApiController.getAuthorisation(responseTypes.get(0),
                "98e119f6-196f-4296-98d4-f1a2f445bca2", "98e119f6-xxxx-yyyy-zzzz-f1a2f445bca2", null, "openid accounts", "https://www.google.com", jwt, true, null,
                null, null, null, null);
        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        assertTrue(responseEntity.getHeaders().getLocation().toString().contains("error"));
    }

    @Test
    public void shouldReturnRedirectActionWhenResponseTypeNotMatch()
            throws OBErrorException, OBErrorResponseException {
        // Given
        List<String> responseTypes = List.of("code id_token");
        given(discoveryConfig.getSupportedResponseTypes()).willReturn(responseTypes);
        String jwt = toEncodedSignedTestJwt("jwt/authorisation.jwt");
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setJwks_uri("url");
        Tpp tpp = new Tpp();
        tpp.setRegistrationResponse(registrationResponse);

        // When
        ResponseEntity<RedirectionAction> responseEntity = authorisationApiController.getAuthorisation("responseTypes.get(0)",
                "98e119f6-196f-4296-98d4-f1a2f445bca2", "98e119f6-xxxx-yyyy-zzzz-f1a2f445bca2", null, "openid accounts", "https://www.google.com", jwt, true, null,
                null, null, null, null);
        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        assertTrue(responseEntity.getHeaders().getLocation().toString().contains("error"));
    }

    @Test
    public void shouldNotThrowExceptionWhen_responseTypesMatch() throws OBErrorException, OBErrorResponseException,
            InvalidTokenException, ParseException, IOException {
        // Given
        String clientId = "98e119f6-196f-4296-98d4-f1a2f445bca2";
        List<String> responseTypes = List.of("code id_token");
        given(discoveryConfig.getSupportedResponseTypes()).willReturn(responseTypes);
        String jwt = toEncodedSignedTestJwt("jwt/authorisation.jwt");
        Tpp tpp = new Tpp();
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setJwks_uri("url");
        tpp.setRegistrationResponse(registrationResponse);
        given(tppStoreService.findByClientId(clientId)).willReturn(Optional.of(tpp));
        String responseType = responseTypes.get(0);
        SignedJWT signedJwt = mock(SignedJWT.class);
        given(cryptoApiClient.validateJws(anyString(), anyString(), anyString() )).willReturn(signedJwt);

        // When
        authorisationApiController.getAuthorisation(responseType, clientId, null, null,
                "openid accounts payments", null, jwt, true, null, null, null, null, null);

        // Then no exception
    }

    @Test
    public void shouldConvertQueryToFragment() {
        // Given
        URI uri = UriComponentsBuilder.fromHttpUrl("https://google.com?test=toto").build().toUri();

        // When
        ResponseEntity responseEntity = authorisationApiController.convertQueryToFragment(uri, new HttpHeaders(),
                "state1");

        // Then
        assertThat(responseEntity.getHeaders().getLocation().toString())
                .isEqualTo("https://google.com#test=toto&state=state1");
    }

    private static String toEncodedSignedTestJwt(final String jwtPayloadFilePath) {
        return utf8FileToString
                .andThen(jsonStringToClaimsSet)
                .andThen(claimsSetToRsa256Jwt)
                .andThen(signJwt)
                .andThen(serializeJwt)
                .apply(jwtPayloadFilePath);
    }

}
