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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.cert.exception.InvalidPsd2EidasCertificate;
import com.forgerock.openbanking.aspsp.as.service.OIDCException;
import com.forgerock.openbanking.aspsp.as.service.SSAService;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.aspsp.as.service.apiclient.*;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.error.exception.oauth2.*;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.utils.extractor.TokenExtractor;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.forgerock.spring.security.multiauth.model.authentication.X509Authentication;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.*;

import static com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType.UNAPPROVED_SOFTWARE_STATEMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.openMocks;


@RunWith(Parameterized.class)

public class DynamicRegistrationApiControllerTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws CertificateException, IOException {
        List<Object[]> testCerts = new ArrayList<Object[]>();
        testCerts.add(new Object[]{new CertificateTestSpec("FrTransport",
                "src/test/resources/certificates/fr-transport.pem", true)});
        testCerts.add(new Object[]{new CertificateTestSpec("OBWac", "src/test/resources/certificates/OBWac.pem",
                true)});
        testCerts.add(new Object[]{new CertificateTestSpec("QWAC", "src/test/resources/certificates/QWac.pem", true)});
        testCerts.add(new Object[]{new CertificateTestSpec("OBTransport", "src/test/resources/certificates/OBTransport.pem", false)});
        return testCerts;
    }

    private CertificateTestSpec testSpec;

    public DynamicRegistrationApiControllerTest(CertificateTestSpec testSpec){
        this.testSpec = testSpec;
    }

    @Mock
    TppStoreService tppStoreService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    TokenExtractor tokenExtractor;

    @Mock
    TppRegistrationService tppRegistrationService;

    private RegistrationRequestFactory registrationRequestFactory;

    List<String> supportedAuthMethod = Arrays.asList("private_key_jwt");

    @Mock
    SSAService ssaService;

    private DynamicRegistrationApiController dynamicRegistrationApiController;
    private final String clientId = "clientId";
    private final String authorizationString = "Bearer tpps-registration-access-token";
    private final String tppName = "tppName";
    private final String principalName = "principalName";
    private final ApiClientIdentityFactory identityFactory = new ApiClientIdentityFactory();

    private AutoCloseable closeMocks;


    @Before
    public void setUp(){
        this.closeMocks = openMocks(this);
        registrationRequestFactory = new RegistrationRequestFactory(tppRegistrationService);
        dynamicRegistrationApiController = new DynamicRegistrationApiController(tppStoreService, objectMapper,
                tokenExtractor, tppRegistrationService, supportedAuthMethod, this.identityFactory, registrationRequestFactory);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @After
    public void teardown() throws Exception {
        this.closeMocks.close();
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
        String clientId = this.clientId;
        Principal principal = mock(Principal.class);
        given(principal.getName()).willReturn(tppName);
        given(tppStoreService.findByClientId(tppName)).willReturn(Optional.of(this.getValidTpp()));
        String accessToken = "tpps-registration-access-token";
        given(tokenExtractor.extract(this.authorizationString)).willReturn(accessToken);

        // when

        ResponseEntity<?> unregisterResponse = dynamicRegistrationApiController.unregister(clientId,
                this.authorizationString, principal);
        // thenobjectMapper
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
    public void failWithInvalidClientIfCertificateIsNotFromATrustedParty_register() throws OIDCException,
            OBErrorResponseException, OAuth2InvalidClientException, InvalidPsd2EidasCertificate {
        // given
        @Valid String registrationRequestJwtSerialised = getValidRegistrationRequestJWTSerialised();
        Collection<OBRIRole> authorities = new ArrayList<>(List.of(OBRIRole.UNKNOWN_CERTIFICATE));
        X509Authentication principal = testSpec.getPrincipal(authorities);

        // when
        OAuth2InvalidClientException exception = catchThrowableOfType( () ->
                dynamicRegistrationApiController.register(registrationRequestJwtSerialised,
                principal), OAuth2InvalidClientException.class);

        // then
        assertThat(exception.getRfc6750ErrorCode()).isEqualTo(OAuth2Exception.INVALID_CLIENT);
    }


    @Test
    public void failWithInvalidClientIfCertificateIsAlreadyRegistered_register() throws OIDCException,
            OBErrorResponseException, OAuth2InvalidClientException, InvalidPsd2EidasCertificate {
        // given
        @Valid String registrationRequestJwtSerialised = getValidRegistrationRequestJWTSerialised();
        Collection<OBRIRole> authorities = new ArrayList<>(List.of(OBRIRole.ROLE_AISP, OBRIRole.ROLE_AISP));
        X509Authentication principal = testSpec.getPrincipal(authorities);

        // when
        OAuth2InvalidClientException exception = catchThrowableOfType( () ->
                dynamicRegistrationApiController.register(registrationRequestJwtSerialised,
                        principal), OAuth2InvalidClientException.class);

        // then
        assertThat(exception.getRfc6750ErrorCode()).isEqualTo(OAuth2Exception.INVALID_CLIENT);
    }

    @Test
    public void failIfSsaIsNotSignedByTrustedParty_register() throws InvalidPsd2EidasCertificate {
        @Valid String registrationRequestJwtSerialised = getValidRegistrationRequestJWTSerialised();
        Collection<OBRIRole> authorities = new ArrayList<>(List.of(OBRIRole.ROLE_ANONYMOUS,
                OBRIRole.UNREGISTERED_TPP,OBRIRole.ROLE_EIDAS));
        X509Authentication principal = testSpec.getPrincipal(authorities);
        given(this.tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(anyString())).willReturn(null);

        // when
        DynamicClientRegistrationException  exception = catchThrowableOfType( () ->
                dynamicRegistrationApiController.register(registrationRequestJwtSerialised,
                        principal), DynamicClientRegistrationException.class);

        assertThat(exception.getErrorType()).isEqualTo(
                UNAPPROVED_SOFTWARE_STATEMENT);
    }

    @Test
    public void failIfSsaIsInvalid_register() throws CertificateException, IOException, OBErrorException, ParseException,
            DynamicClientRegistrationException, InvalidPsd2EidasCertificate {
        @Valid String registrationRequestJwtSerialised = getValidRegistrationRequestJWTSerialised();
        Collection<OBRIRole> authorities = new ArrayList<>(List.of(OBRIRole.ROLE_ANONYMOUS,
                OBRIRole.UNREGISTERED_TPP, OBRIRole.ROLE_EIDAS));
        X509Authentication principal = testSpec.getPrincipal(authorities);
        RegistrationRequest regRequest =
                registrationRequestFactory.getRegistrationRequest(registrationRequestJwtSerialised, this.objectMapper);

        String directoryName = "ForgeRock";
        given(this.tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(anyString())).willReturn(directoryName);
        String softwareClientId = regRequest.getSoftwareClientId();
        JWTClaimsSet claimSet = regRequest.getSoftwareStatementClaims();
        Mockito.doThrow(new DynamicClientRegistrationException("blah",
                DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT))
                .when(this.tppRegistrationService)
                .verifyTPPRegistrationRequestSignature(registrationRequestJwtSerialised, softwareClientId, claimSet);

        // when
        DynamicClientRegistrationException  exception = catchThrowableOfType( () ->
                dynamicRegistrationApiController.register(registrationRequestJwtSerialised,
                        principal), DynamicClientRegistrationException.class);

        assertThat(exception.getErrorType()).isEqualTo(
                DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT);
    }

    @Test
    public void failIfSsaIsHasSoftwareIdDifferentFromRequestObject_register() throws CertificateException, IOException,
            OBErrorException, ParseException,DynamicClientRegistrationException, OIDCException, InvalidPsd2EidasCertificate {
        @Valid String registrationRequestJwtSerialised = getValidRegistrationRequestJWTSerialised();
        Collection<OBRIRole> authorities = new ArrayList<>(List.of(OBRIRole.ROLE_ANONYMOUS, OBRIRole.UNREGISTERED_TPP
                , OBRIRole.ROLE_EIDAS));
        X509Authentication principal = testSpec.getPrincipal(authorities);
        RegistrationRequest regRequest =
                registrationRequestFactory.getRegistrationRequest(registrationRequestJwtSerialised, this.objectMapper);
        String directoryName = "ForgeRock";
        given(this.tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(anyString())).willReturn(directoryName);
        String softwareClientId = regRequest.getSoftwareClientId();
        JWTClaimsSet claimSet = regRequest.getSoftwareStatementClaims();
        Mockito.doThrow(new DynamicClientRegistrationException("blah",
                DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT))
                .when(this.tppRegistrationService)
                .verifyTPPRegistrationRequestAgainstSSA(regRequest, claimSet);

        // when
        DynamicClientRegistrationException  exception = catchThrowableOfType( () ->
                dynamicRegistrationApiController.register(registrationRequestJwtSerialised,
                        principal), DynamicClientRegistrationException.class);

        assertThat(exception.getErrorType()).isEqualTo(
                DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT);
    }

    @Test
    public void shouldSucceed_register() throws ParseException, OIDCException, OAuth2InvalidClientException,
            DynamicClientRegistrationException, InvalidPsd2EidasCertificate, ApiClientException {

        @Valid String registrationRequestJwtSerialised = getValidRegistrationRequestJWTSerialised();
        Collection<OBRIRole> authorities = new ArrayList<>(List.of(OBRIRole.ROLE_ANONYMOUS, OBRIRole.UNREGISTERED_TPP
                , OBRIRole.ROLE_EIDAS));
        X509Authentication principal = testSpec.getPrincipal(authorities);
        ApiClientIdentity apiClientIdentity = this.identityFactory.getApiClientIdentity(principal);
        String directoryName = "ForgeRock";
        given(this.tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(anyString())).willReturn(directoryName);
        RegistrationRequest regRequest =
                registrationRequestFactory.getRegistrationRequest(registrationRequestJwtSerialised, this.objectMapper);

        Map<String, String> ssaValues = new HashMap<>();
        ssaValues.put("bish", "bash");
        JSONObject ssaJwsJson = new JSONObject(ssaValues);

        Tpp tpp = new Tpp();
        tpp.setRegistrationResponse(new OIDCRegistrationResponse());
        String tppIdentifier = apiClientIdentity.getTppIdentifier();
        given(this.tppRegistrationService.registerTpp(eq(tppIdentifier),
                any(RegistrationRequest.class),
                eq(directoryName))).willReturn(tpp);

        // when
        ResponseEntity<OIDCRegistrationResponse> response = dynamicRegistrationApiController.register(registrationRequestJwtSerialised,
                        principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

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

    private String getValidRegistrationRequestJWTSerialised(){
        return "eyJraWQiOiI4MDBjODBhNzVjOGEwYWQ0Y2FiNzY0NTJlNGY1ZjlkODE0NDFmZjdjIiwiYWxnIjoiUFMyNTYifQ.eyJ0b2tlbl9lbmRwb2ludF9hdXRoX3NpZ25pbmdfYWxnIjoiUFMyNTYiLCJyZXF1ZXN0X29iamVjdF9lbmNyeXB0aW9uX2FsZyI6IlJTQS1PQUVQLTI1NiIsImdyYW50X3R5cGVzIjpbImF1dGhvcml6YXRpb25fY29kZSIsInJlZnJlc2hfdG9rZW4iLCJjbGllbnRfY3JlZGVudGlhbHMiXSwiaXNzIjoiNjBjNzViYTNjNDUwNDUwMDExZWZhNjc5IiwicmVkaXJlY3RfdXJpcyI6WyJodHRwczpcL1wvd3d3Lmdvb2dsZS5jb20iXSwidG9rZW5fZW5kcG9pbnRfYXV0aF9tZXRob2QiOiJwcml2YXRlX2tleV9qd3QiLCJzb2Z0d2FyZV9zdGF0ZW1lbnQiOiJleUpyYVdRaU9pSmhaR015TmpJMU1HWmtNMlV6TmpJMFlqWTJPR014TkRneE4yWXhaVFE1WTJGbU9ESTVNVGhpSWl3aVlXeG5Jam9pVUZNeU5UWWlmUS5leUp2Y21kZmFuZHJjMTlsYm1Sd2IybHVkQ0k2SWxSUFJFOGlMQ0p6YjJaMGQyRnlaVjl0YjJSbElqb2lWRVZUVkNJc0luTnZablIzWVhKbFgzSmxaR2x5WldOMFgzVnlhWE1pT2xzaWFIUjBjSE02WEM5Y0wyZHZiMmRzWlM1amJ5NTFheUpkTENKdmNtZGZjM1JoZEhWeklqb2lRV04wYVhabElpd2ljMjltZEhkaGNtVmZZMnhwWlc1MFgyNWhiV1VpT2lKS1lXMXBaU2R6SUZOdlpuUjNZWEpsSUVGd2NHeHBZMkYwYVc5dUlpd2ljMjltZEhkaGNtVmZZMnhwWlc1MFgybGtJam9pTmpCak56VmlZVE5qTkRVd05EVXdNREV4WldaaE5qYzVJaXdpYVhOeklqb2lSbTl5WjJWU2IyTnJJaXdpYzI5bWRIZGhjbVZmWTJ4cFpXNTBYMlJsYzJOeWFYQjBhVzl1SWpvaVZHVnpkQ0JoY0hBaUxDSnpiMlowZDJGeVpWOXFkMnR6WDJWdVpIQnZhVzUwSWpvaWFIUjBjSE02WEM5Y0wzTmxjblpwWTJVdVpHbHlaV04wYjNKNUxtUmxkaTF2WWk1bWIzSm5aWEp2WTJzdVptbHVZVzVqYVdGc09qZ3dOelJjTDJGd2FWd3ZjMjltZEhkaGNtVXRjM1JoZEdWdFpXNTBYQzgyTUdNM05XSmhNMk0wTlRBME5UQXdNVEZsWm1FMk56bGNMMkZ3Y0d4cFkyRjBhVzl1WEM5cWQydGZkWEpwSWl3aWMyOW1kSGRoY21WZmFXUWlPaUkyTUdNM05XSmhNMk0wTlRBME5UQXdNVEZsWm1FMk56a2lMQ0p2Y21kZlkyOXVkR0ZqZEhNaU9sdGRMQ0p2WWw5eVpXZHBjM1J5ZVY5MGIzTWlPaUpvZEhSd2N6cGNMMXd2WkdseVpXTjBiM0o1TG1SbGRpMXZZaTVtYjNKblpYSnZZMnN1Wm1sdVlXNWphV0ZzT2pnd056UmNMM1J2YzF3dklpd2liM0puWDJsa0lqb2lOakJqTnpWaU9XTmpORFV3TkRVd01ERXhaV1poTmpjNElpd2ljMjltZEhkaGNtVmZhbmRyYzE5eVpYWnZhMlZrWDJWdVpIQnZhVzUwSWpvaVZFOUVUeUlzSW5OdlpuUjNZWEpsWDNKdmJHVnpJanBiSWtSQlZFRWlMQ0pEUWxCSlNTSXNJbEJKVTFBaUxDSkJTVk5RSWwwc0ltVjRjQ0k2TVRZeU5UYzFOVEU0T1N3aWIzSm5YMjVoYldVaU9pSkJibTl1ZVcxdmRYTWlMQ0p2Y21kZmFuZHJjMTl5WlhadmEyVmtYMlZ1WkhCdmFXNTBJam9pVkU5RVR5SXNJbWxoZENJNk1UWXlOVEUxTURNNE9Td2lhblJwSWpvaU1UQXlZVFF5WmpBdE9EUXdaaTAwWlRRMExXRTJOek10TVRnd05EQTNOV000TVRVekluMC5jWjhhUHpuMVo1MklCeGVnVDFmSmVGMTdTczRKSjVYSkF0MVd6TGF2WDRPSDJBS0l4QUdwMDM4Ni1Pd0twR0ZFQkxrVVB1MFp3VzUtczhNYl9uQjU1OHZDY2NObW14cFV4UExSMl9aZkNzSWlpWVRUNWpYYXh2UnVFa0xJc3Zocy15aVcyc05BRXlRRjJwY010M1NpS01YQTFVeTNIWV9lU0pqaURwQlhBMjZGMlF2b1lJdU1iTzd1WkVEeUp4aTd3RVJjMVFpWlB4UzMydUQ4eHhvd19hbHNEOXJpSVBzNG1HRkg2b1RIekdVSG04RUl3MkRleW9LZmMweFItWVoyUjk5NkxYSUpYMkpmOTJGT0RoSURnOU93eXNMMm1Va2QwbDBULTg4UzJYUmVQWHQ2Z0Z3eXV0bFd2MjkzMTY4R1IteExJeTRBcTIwREJkblBGaUc0eWciLCJzY29wZSI6Im9wZW5pZCBhY2NvdW50cyBwYXltZW50cyBmdW5kc2NvbmZpcm1hdGlvbnMiLCJyZXF1ZXN0X29iamVjdF9zaWduaW5nX2FsZyI6IlBTMjU2IiwiZXhwIjoxNjI1MTUwODcwLCJyZXF1ZXN0X29iamVjdF9lbmNyeXB0aW9uX2VuYyI6IkExMjhDQkMtSFMyNTYiLCJpYXQiOjE2MjUxNTA1NzAsImp0aSI6ImVlNmVjYzhkLTJiNmEtNDJkMS04ZGZlLTBjMmEyNmIyNzU1NiIsInJlc3BvbnNlX3R5cGVzIjpbImNvZGUgaWRfdG9rZW4iXSwiaWRfdG9rZW5fc2lnbmVkX3Jlc3BvbnNlX2FsZyI6IlBTMjU2In0.Ep9b9GXM0wFZtq1HSH4j6LDojAHTgUvxSQIjzxGX9QPklrJoAk_4Zg_Wooy3Jnw4OsoL92pzqoP8CtsQLDVYCvEfGh9TgbS31ItjXjcACBNAx6sWfT0NaE0T1bmjeSppj8pM18qgkNPXRv211AED0QVizE3b07arNjjj2SaVuarWp1AkSEysb4qepejZFazxAzEQuz8s66SxpPKdMfFKcaJUlr2xGbKiHFuAa6f0QrUSIfIUNQf-6DdrFL1w68EoAkkfbagAx5G4S2e_m0SraIbb9aZqm5LMvAVRYsG5tN8yBPfpWchHGI5_uJeFmNtipVfWuu7KuwiGJmGOd3OtiQ";
    }

    private User getValidUserWithAuthorities(Collection<? extends GrantedAuthority> authorities){
        return new User(this.principalName, "password", authorities);
    }
}