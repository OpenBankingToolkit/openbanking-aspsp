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
import com.forgerock.cert.psd2.Psd2Role;
import com.forgerock.cert.psd2.RoleOfPsp;
import com.forgerock.openbanking.aspsp.as.api.registration.CertificateTestSpec;
import com.forgerock.openbanking.common.TestHelperFunctions;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.error.exception.oauth2.*;
import com.forgerock.openbanking.common.services.onboarding.TppRegistrationService;
import com.forgerock.openbanking.common.services.onboarding.apiclient.ApiClientException;
import com.forgerock.openbanking.common.services.onboarding.apiclient.ApiClientIdentity;
import com.forgerock.openbanking.common.services.onboarding.apiclient.ApiClientIdentityFactory;
import com.forgerock.openbanking.common.services.onboarding.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.common.services.onboarding.registrationrequest.DirectorySoftwareStatementFactory;
import com.forgerock.openbanking.common.services.onboarding.registrationrequest.RegistrationRequest;
import com.forgerock.openbanking.common.services.onboarding.registrationrequest.RegistrationRequestFactory;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.utils.extractor.TokenExtractor;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.forgerock.spring.security.multiauth.model.authentication.X509Authentication;
import com.forgerock.spring.security.multiauth.model.granttypes.PSD2GrantType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.security.Principal;
import java.security.cert.CertificateException;
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
                "src/test/resources/certificates/fr-transport.pem",
                TestHelperFunctions.getValidForgeRockSsaRegistrationRequestJWTSerialised(),
                TestHelperFunctions.getValidOpenBankingSsaRegistrationRequestJWTSerialised(),
                true)});
        testCerts.add(new Object[]{new CertificateTestSpec("OBWac",
                "src/test/resources/certificates/OBWac.pem",
                TestHelperFunctions.getValidOpenBankingSsaRegistrationRequestJWTSerialised(),
                TestHelperFunctions.getValidForgeRockSsaRegistrationRequestJWTSerialised(),
                true)});
        return testCerts;
    }

    private final CertificateTestSpec testSpec;

    public DynamicRegistrationApiControllerTest(CertificateTestSpec testSpec){
        this.testSpec = testSpec;
    }

    @Mock
    TppStoreService tppStoreService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    TokenExtractor tokenExtractor;

    DirectorySoftwareStatementFactory softwareStatementFactory;

    @Mock
    TppRegistrationService tppRegistrationService;

    private RegistrationRequestFactory registrationRequestFactory;

    List<String> supportedAuthMethod = Arrays.asList("private_key_jwt", "client_secret_basic");

    private DynamicRegistrationApiController dynamicRegistrationApiController;
    private final String clientId = "clientId";
    private final String authorizationString = "Bearer tpps-registration-access-token";
    private final String tppName = "tppName";
    private final String principalName = "principalName";
    private final ApiClientIdentityFactory identityFactory = new ApiClientIdentityFactory();
    private String registrationRequestJwtSerialised;

    private AutoCloseable closeMocks;


    @Before
    public void setUp(){
        this.closeMocks = openMocks(this);
        OpenBankingDirectoryConfiguration obDirectoryConfig = new OpenBankingDirectoryConfiguration();
        obDirectoryConfig.issuerId = "OpenBanking Ltd";

        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.softwareStatementFactory = new DirectorySoftwareStatementFactory(obDirectoryConfig);
        this.registrationRequestFactory = new RegistrationRequestFactory(this.tppRegistrationService,
                 this.softwareStatementFactory, this.objectMapper);
        this.dynamicRegistrationApiController = new DynamicRegistrationApiController(this.tppStoreService,
                this.objectMapper, this.tokenExtractor, this.tppRegistrationService, this.supportedAuthMethod,
                this.identityFactory, this.registrationRequestFactory);
        this.registrationRequestJwtSerialised = testSpec.getRegistrationRequest();
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
                dynamicRegistrationApiController.deleteRegistration(clientId, null,
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
                dynamicRegistrationApiController.deleteRegistration(clientId, authorizationString,
                null), OAuth2InvalidClientException.class);
        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getHttpStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getRfc6750ErrorCode()).isEqualTo("invalid_client");
    }

    @Test
    public void throwsOAuth2InvalidClientExceptionWhenClientIdIsNull_unregister() throws OAuth2InvalidClientException {
        // given
        String clientId = null;
        Principal principal = mock(Principal.class);


        // when
        OAuth2InvalidClientException exception = catchThrowableOfType(()->
                dynamicRegistrationApiController.deleteRegistration(clientId, this.authorizationString,
                        principal), OAuth2InvalidClientException.class);
        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getHttpStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getRfc6750ErrorCode()).isEqualTo(OAuth2Exception.INVALID_CLIENT);
    }

    @Test
    public void throwsOAuth2BearerTokenUsageInvalidTokenExceptionWhenInvalidAuthorizationString_unregister()
            throws OAuth2InvalidClientException, OAuth2BearerTokenUsageMissingAuthInfoException,
            OAuth2BearerTokenUsageInvalidTokenException {
        // given
        String clientId = this.clientId;
        Principal principal = mock(Principal.class);
        Tpp tpp = this.getValidTpp();
        given(principal.getName()).willReturn(tppName);
        given(tppRegistrationService.getTpp(clientId)).willReturn(tpp);
        given(tppRegistrationService.validateAccessTokenIsValidForOidcRegistration(tpp, authorizationString))
                .willThrow( new OAuth2BearerTokenUsageInvalidTokenException("test"));

        // when
        OAuth2BearerTokenUsageInvalidTokenException exception = catchThrowableOfType(()->
                        dynamicRegistrationApiController.deleteRegistration(clientId, this.authorizationString, principal),
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
    public void throwsOAuth2BearerTokenUsageInvalidTokenExceptionWhenNotOwner_unregister()
            throws OAuth2InvalidClientException, OAuth2BearerTokenUsageMissingAuthInfoException,
            OAuth2BearerTokenUsageInvalidTokenException {
        // given
        String clientId = this.clientId;
        Principal principal = mock(Principal.class);
        Tpp tpp = this.getValidTpp();
        given(principal.getName()).willReturn(tppName);
        given(tppRegistrationService.getTpp(clientId)).willReturn(tpp);
        String accessToken = "not-the-tpps-registration-access-token";
        given(tppRegistrationService.validateAccessTokenIsValidForOidcRegistration(tpp, this.authorizationString)).willThrow(
                new OAuth2BearerTokenUsageInvalidTokenException("Invalid token"));


        // when
        OAuth2BearerTokenUsageInvalidTokenException exception = catchThrowableOfType(()->
                        dynamicRegistrationApiController.deleteRegistration(clientId, this.authorizationString, principal),
                OAuth2BearerTokenUsageInvalidTokenException.class);
        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getHttpStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getWwwAuthenticateResponseHeaderValue()).isNull();
        assertThat(exception.getErrorCode()).isEqualTo(OAuth2BearerTokenUsageException.ErrorEnum.INVALID_TOKEN);
    }

    @Test
    public void successful_unregister() throws OAuth2BearerTokenUsageMissingAuthInfoException,
            OAuth2BearerTokenUsageInvalidTokenException, OAuth2InvalidClientException {
        // given
        String clientId = this.clientId;
        Principal principal = mock(Principal.class);
        given(principal.getName()).willReturn(tppName);
        given(tppRegistrationService.getTpp(clientId)).willReturn(this.getValidTpp());
        String accessToken = "tpps-registration-access-token";
        given(tokenExtractor.extract(this.authorizationString)).willReturn(accessToken);

        // when

        ResponseEntity<?> unregisterResponse = dynamicRegistrationApiController.deleteRegistration(clientId,
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
    public void successful_updateClient() throws InvalidPsd2EidasCertificate, OAuth2InvalidClientException,
            DynamicClientRegistrationException, OAuth2BearerTokenUsageMissingAuthInfoException, OAuth2BearerTokenUsageInvalidTokenException {
        // Given
        String clientId = "3105f70b-b417-427e-922d-7ba04d16278a";
        String authToken = "eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkIjoiRm9sN0lwZEtlTFptekt0Q0VnaTFMRGhTSXpNPSIsImFsZyI6IkVTMjU2In0.eyJzdWIiOiIzMTA1ZjcwYi1iNDE3LTQyN2UtOTIyZC03YmEwNGQxNjI3OGEiLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXVkaXRUcmFja2luZ0lkIjoiZTY2MDZiOGYtNDA2Ni00Y2U2LWIxZDAtYTQ1MzM0MDEzYjA5LTIwNTkiLCJpc3MiOiJodHRwczovL2FzLmFzcHNwLmRldi1vYi5mb3JnZXJvY2suZmluYW5jaWFsOjgwNzQvb2F1dGgyIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiMW9zekR5NTJxNlByUU51anpGVDhOT1U4U1VZIiwiYXVkIjoiMzEwNWY3MGItYjQxNy00MjdlLTkyMmQtN2JhMDRkMTYyNzhhIiwibmJmIjoxNjI2MzUxNzMxLCJzY29wZSI6W10sImF1dGhfdGltZSI6MTYyNjM1MTczMSwicmVhbG0iOiIvb3BlbmJhbmtpbmciLCJleHAiOjE2MjY0MzgxMzEsImlhdCI6MTYyNjM1MTczMSwiZXhwaXJlc19pbiI6ODY0MDAsImp0aSI6IldmYm13OGtkUFk1bEhZSldMa3lDS3RmekZ1NCJ9.vhH9AGDKbxK1R_tnq8_nOkIpPH7se68MxOC8y-Wq4SW4_ffMBj1ChkckU-q2wJ_4hh_l1sgdlCdkom_VQFvN9Q";
        String authTokenHeaderValue = "Bearer " +
                "eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkIjoiRm9sN0lwZEtlTFptekt0Q0VnaTFMRGhTSXpNPSIsImFsZyI6IkVTMjU2In0.eyJzdWIiOiIzMTA1ZjcwYi1iNDE3LTQyN2UtOTIyZC03YmEwNGQxNjI3OGEiLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXVkaXRUcmFja2luZ0lkIjoiZTY2MDZiOGYtNDA2Ni00Y2U2LWIxZDAtYTQ1MzM0MDEzYjA5LTIwNTkiLCJpc3MiOiJodHRwczovL2FzLmFzcHNwLmRldi1vYi5mb3JnZXJvY2suZmluYW5jaWFsOjgwNzQvb2F1dGgyIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiMW9zekR5NTJxNlByUU51anpGVDhOT1U4U1VZIiwiYXVkIjoiMzEwNWY3MGItYjQxNy00MjdlLTkyMmQtN2JhMDRkMTYyNzhhIiwibmJmIjoxNjI2MzUxNzMxLCJzY29wZSI6W10sImF1dGhfdGltZSI6MTYyNjM1MTczMSwicmVhbG0iOiIvb3BlbmJhbmtpbmciLCJleHAiOjE2MjY0MzgxMzEsImlhdCI6MTYyNjM1MTczMSwiZXhwaXJlc19pbiI6ODY0MDAsImp0aSI6IldmYm13OGtkUFk1bEhZSldMa3lDS3RmekZ1NCJ9.vhH9AGDKbxK1R_tnq8_nOkIpPH7se68MxOC8y-Wq4SW4_ffMBj1ChkckU-q2wJ_4hh_l1sgdlCdkom_VQFvN9Q";

        Collection<? extends GrantedAuthority> authorities = new ArrayList<>(List.of(OBRIRole.ROLE_DATA,
                OBRIRole.ROLE_AISP, OBRIRole.ROLE_CBPII,
                OBRIRole.ROLE_EIDAS, new PSD2GrantType(new RoleOfPsp(Psd2Role.PSP_IC))));
        X509Authentication principal = testSpec.getPrincipal(authorities);
        String directoryName = "ForgeRock";
        given(this.tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(anyString(), eq("ForgeRock")))
                .willReturn(directoryName);
        given(tokenExtractor.extract(authTokenHeaderValue)).willReturn(authToken);


        Tpp tpp = this.getValidTpp();
        tpp.setClientId("3105f70b-b417-427e-922d-7ba04d16278a");

        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setRegistrationAccessToken(authToken);
        tpp.setRegistrationResponse(registrationResponse);
        given(tppRegistrationService.getTpp(clientId)).willReturn(tpp);
        given(tppRegistrationService.validateAccessTokenIsValidForOidcRegistration(tpp, authTokenHeaderValue))
                .willReturn(authToken);
        given(this.tppRegistrationService.updateTpp(any(ApiClientIdentity.class), eq(tpp), eq(authToken),
                any(RegistrationRequest.class))).willReturn(tpp);
        given(tokenExtractor.extract(authTokenHeaderValue)).willReturn(authToken);

        // when
        ResponseEntity<OIDCRegistrationResponse> response =
                dynamicRegistrationApiController.updateRegistration(clientId, authTokenHeaderValue,
                        registrationRequestJwtSerialised,
                principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void failWithInvalidClientIfCertificateIsNotFromATrustedParty_register() throws InvalidPsd2EidasCertificate {
        // given
        
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
    public void failWithInvalidClientIfSoftwareStatementDoesNotBelongToApiClient_register() throws InvalidPsd2EidasCertificate{
        // Given
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>(List.of(OBRIRole.ROLE_DATA,
                OBRIRole.ROLE_AISP, OBRIRole.ROLE_CBPII,
                OBRIRole.ROLE_EIDAS, new PSD2GrantType(new RoleOfPsp(Psd2Role.PSP_IC))));
        X509Authentication principal = testSpec.getPrincipal(authorities);

        String registrationRequestJwtSerial = testSpec.getRegRequestIssuedToDifferentTpp();

        // When
        OAuth2InvalidClientException exception = catchThrowableOfType( () ->
                dynamicRegistrationApiController.register(registrationRequestJwtSerial,
                        principal), OAuth2InvalidClientException.class);

        assertThat(exception.getRfc6750ErrorCode()).isEqualTo(OAuth2Exception.INVALID_CLIENT);
    }


    @Test
    public void willRegisterIfCertHasBeenUsedToPreviouslyRegister_register() throws InvalidPsd2EidasCertificate,
            OAuth2InvalidClientException, DynamicClientRegistrationException {
        // given
        
        Collection<OBRIRole> authorities = new ArrayList<>(List.of(OBRIRole.ROLE_AISP, OBRIRole.ROLE_AISP));
        X509Authentication principal = testSpec.getPrincipal(authorities);

        String authToken = "eyJ0eXAiOiJKV1QiLCJ6aXAiOiJOT05FIiwia2lkIjoiRm9sN0lwZEtlTFptekt0Q0VnaTFMRGhTSXpNPSIsImFsZyI6IkVTMjU2In0.eyJzdWIiOiIzMTA1ZjcwYi1iNDE3LTQyN2UtOTIyZC03YmEwNGQxNjI3OGEiLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXVkaXRUcmFja2luZ0lkIjoiZTY2MDZiOGYtNDA2Ni00Y2U2LWIxZDAtYTQ1MzM0MDEzYjA5LTIwNTkiLCJpc3MiOiJodHRwczovL2FzLmFzcHNwLmRldi1vYi5mb3JnZXJvY2suZmluYW5jaWFsOjgwNzQvb2F1dGgyIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiMW9zekR5NTJxNlByUU51anpGVDhOT1U4U1VZIiwiYXVkIjoiMzEwNWY3MGItYjQxNy00MjdlLTkyMmQtN2JhMDRkMTYyNzhhIiwibmJmIjoxNjI2MzUxNzMxLCJzY29wZSI6W10sImF1dGhfdGltZSI6MTYyNjM1MTczMSwicmVhbG0iOiIvb3BlbmJhbmtpbmciLCJleHAiOjE2MjY0MzgxMzEsImlhdCI6MTYyNjM1MTczMSwiZXhwaXJlc19pbiI6ODY0MDAsImp0aSI6IldmYm13OGtkUFk1bEhZSldMa3lDS3RmekZ1NCJ9.vhH9AGDKbxK1R_tnq8_nOkIpPH7se68MxOC8y-Wq4SW4_ffMBj1ChkckU-q2wJ_4hh_l1sgdlCdkom_VQFvN9Q";

        Tpp tpp = new Tpp();
        tpp.setClientId("3105f70b-b417-427e-922d-7ba04d16278a");
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setRegistrationAccessToken(authToken);
        tpp.setRegistrationResponse(registrationResponse);
        given(tppStoreService.findByClientId("testname")).willReturn(Optional.of(tpp));
        given(this.tppRegistrationService.registerTpp(any(ApiClientIdentity.class),
                any(RegistrationRequest.class))).willReturn(tpp);

        // when
        ResponseEntity<OIDCRegistrationResponse> response =
                dynamicRegistrationApiController.register(registrationRequestJwtSerialised,
                        principal);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void failIfSsaIsNotSignedByTrustedParty_register() throws InvalidPsd2EidasCertificate,
            DynamicClientRegistrationException {
        
        Collection<OBRIRole> authorities = new ArrayList<>(List.of(OBRIRole.ROLE_ANONYMOUS,
                OBRIRole.UNREGISTERED_TPP,OBRIRole.ROLE_EIDAS));
        X509Authentication principal = testSpec.getPrincipal(authorities);
        given(this.tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(anyString(), anyString()))
                .willThrow(new DynamicClientRegistrationException("Test throw", UNAPPROVED_SOFTWARE_STATEMENT));

        // when
        DynamicClientRegistrationException  exception = catchThrowableOfType( () ->
                dynamicRegistrationApiController.register(registrationRequestJwtSerialised,
                        principal), DynamicClientRegistrationException.class);

        assertThat(exception.getErrorType()).isEqualTo(
                UNAPPROVED_SOFTWARE_STATEMENT);
    }

    @Test
    public void failIfSsaIsHasSoftwareIdDifferentFromRequestObject_register()
            throws DynamicClientRegistrationException, InvalidPsd2EidasCertificate {
        
        Collection<OBRIRole> authorities = new ArrayList<>(List.of(OBRIRole.ROLE_ANONYMOUS, OBRIRole.UNREGISTERED_TPP
                , OBRIRole.ROLE_EIDAS));
        X509Authentication principal = testSpec.getPrincipal(authorities);
        RegistrationRequest regRequest =
                registrationRequestFactory.getRegistrationRequestFromJwt(registrationRequestJwtSerialised);
        String directoryName = "ForgeRock";
        given(this.tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(anyString(), eq("ForgeRock")))
                .willReturn(directoryName);
        Mockito.doThrow(new DynamicClientRegistrationException("blah",
                DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT))
                .when(this.tppRegistrationService)
                .verifyTPPRegistrationRequestAgainstSSA(regRequest);

        // when
        DynamicClientRegistrationException  exception = catchThrowableOfType( () ->
                dynamicRegistrationApiController.register(registrationRequestJwtSerialised,
                        principal), DynamicClientRegistrationException.class);

        assertThat(exception.getErrorType()).isEqualTo(
                DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT);
    }

    @Test
    public void shouldSucceed_register() throws OAuth2InvalidClientException,
            DynamicClientRegistrationException, InvalidPsd2EidasCertificate, ApiClientException {

        
        Collection<OBRIRole> authorities = new ArrayList<>(List.of(OBRIRole.ROLE_ANONYMOUS, OBRIRole.UNREGISTERED_TPP
                , OBRIRole.ROLE_EIDAS));
        X509Authentication principal = testSpec.getPrincipal(authorities);
        ApiClientIdentity apiClientIdentity = this.identityFactory.getApiClientIdentity(principal);
        String directoryName = "ForgeRock";
        given(this.tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(anyString(), eq("ForgeRock")))
                .willReturn(directoryName);
        RegistrationRequest regRequest =
                registrationRequestFactory.getRegistrationRequestFromJwt(registrationRequestJwtSerialised);

        Tpp tpp = new Tpp();
        tpp.setRegistrationResponse(new OIDCRegistrationResponse());
        given(this.tppRegistrationService.registerTpp(any(ApiClientIdentity.class),
                any(RegistrationRequest.class))).willReturn(tpp);

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
        tpp.setAuthorisationNumber(this.tppName);
        return tpp;
    }
}