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

import com.forgerock.openbanking.am.services.AMOIDCRegistrationService;
import com.forgerock.openbanking.analytics.services.TppEntriesKPIService;
import com.forgerock.openbanking.aspsp.as.api.registration.dynamic.dto.RegistrationError;
import com.forgerock.openbanking.aspsp.as.configuration.ForgeRockDirectoryConfiguration;
import com.forgerock.openbanking.aspsp.as.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationRequest;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TppRegistrationServiceTest {

    private CryptoApiClient cryptoApiClient = mock(CryptoApiClient.class);
    private OpenBankingDirectoryConfiguration openBankingDreictoryConfiguration = mock(OpenBankingDirectoryConfiguration.class);
    private ForgeRockDirectoryConfiguration forgeRockDirectoryConfiguration = mock(ForgeRockDirectoryConfiguration.class);
    private TppStoreService tppStoreService = mock(TppStoreService.class);
    private AMOIDCRegistrationService amoidcRegistrationService = mock(AMOIDCRegistrationService.class);
    private TppEntriesKPIService tppEntriesKPIService = mock(TppEntriesKPIService.class);
    private TppRegistrationService tppRegistrationService = null;

    private OIDCRegistrationRequest registrationRequest = mock(OIDCRegistrationRequest.class);
    private List<String> validRedirectUris = new ArrayList <>(List.of("https://google.co.uk"));
    private String softwareId = "SoftwareID";
    private JWTClaimsSet validSsaJwtClaims = null;


    @Before
    public void setUp() throws Exception {
        this.tppRegistrationService = new TppRegistrationService(this.cryptoApiClient,
            this.openBankingDreictoryConfiguration, this.forgeRockDirectoryConfiguration,
            this.tppStoreService, this.amoidcRegistrationService, this.tppEntriesKPIService);


        this.validSsaJwtClaims = new JWTClaimsSet.Builder()
            .claim(OpenBankingConstants.SSAClaims.SOFTWARE_ID, softwareId)
            .claim(OpenBankingConstants.SSAClaims.SOFTWARE_REDIRECT_URIS, this.validRedirectUris)
            .build();
    }

    @After
    public void tearDown() throws Exception {
        reset(cryptoApiClient, openBankingDreictoryConfiguration, forgeRockDirectoryConfiguration, tppStoreService,
                amoidcRegistrationService, tppStoreService, registrationRequest);
    }

    @Test
    public void shouldFailWhenSsaHasNoSoftwareId_VerifyTPPRegistrationRequestAgainstSSA() {
        // Given
        when(this.registrationRequest.getSoftwareId()).thenReturn("notNull");
        JWTClaimsSet ssaClaimsSet = new JWTClaimsSet.Builder().build();

        // When
        OIDCException exception = catchThrowableOfType(() ->
                tppRegistrationService.verifyTPPRegistrationRequestAgainstSSA(this.registrationRequest, ssaClaimsSet),
                OIDCException.class);
        // Then
        assertThat(exception.getMessage()).contains(RegistrationError.ErrorEnum.INVALID_SOFTWARE_STATEMENT.toString());
    }

    @Test
    public void shouldSucceedWhenRequestSoftwareIdIsNull_VerifyTPPRegistrationRequestAgainstSSA() throws OBErrorException,
            OIDCException {
        // Given
        when(this.registrationRequest.getRedirectUris()).thenReturn(this.validRedirectUris);

        // When
        tppRegistrationService.verifyTPPRegistrationRequestAgainstSSA(this.registrationRequest, this.validSsaJwtClaims);

        // Then
    }

    @Test
    public void shouldFailWhenSoftwareIdsAreDifferent_VerifyTPPRegistrationRequestAgainstSSA() {
        // Given
        when(this.registrationRequest.getSoftwareId()).thenReturn("invalidSoftwareId");

        // When
        OIDCException exception = catchThrowableOfType(() ->
        tppRegistrationService.verifyTPPRegistrationRequestAgainstSSA(this.registrationRequest, this.validSsaJwtClaims),
                OIDCException.class);

        // Then
        assertThat(exception.getRegistrationError().getError())
                .isEqualTo(RegistrationError.ErrorEnum.INVALID_CLIENT_METADATA);
    }

    @Test
    public void shouldSucceedWhenSoftwareIdsMatch_verifyTPPRegistrationRequestAgainstSSA() throws OBErrorException,
            OIDCException {
        // Given
        when(this.registrationRequest.getSoftwareId()).thenReturn(this.softwareId);

        // When
        tppRegistrationService.verifyTPPRegistrationRequestAgainstSSA(this.registrationRequest, this.validSsaJwtClaims);

        // Then
    }

    /***************************** test redirect url logic ************************************************************/
    @Test
    public void shouldFailWhenNoRedirectUrlInSsa_verifyTppRegistrationRequestAgainstSSA(){
        // Given
        when(this.registrationRequest.getSoftwareId()).thenReturn(this.softwareId);
        when(this.registrationRequest.getRedirectUris()).thenReturn(this.validRedirectUris);

        JWTClaimsSet ssaClaimsSet = new JWTClaimsSet.Builder()
                .claim(OpenBankingConstants.SSAClaims.SOFTWARE_ID, softwareId)
                .build();

        // When
        OIDCException exception = catchThrowableOfType(() ->
            tppRegistrationService.verifyTPPRegistrationRequestAgainstSSA(this.registrationRequest, ssaClaimsSet),
                OIDCException.class);

        // Then
        assertThat(exception.getRegistrationError().getError())
                .isEqualTo(RegistrationError.ErrorEnum.INVALID_REDIRECT_URI);
    }

    // ToDo: This is ignored due to issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/17
    @Ignore
    @Test
    public void shouldFailWhenRequestRedirectUriNotInSsa_verifyTppRegistrationRequestAgainstSSA(){
        // Given
        String redirectUriNotInSsa = "https://mytpp.com/oidc";
        List<String> redirectUrisNotInSsa = new ArrayList<String>(List.of(redirectUriNotInSsa));
        when(this.registrationRequest.getSoftwareId()).thenReturn(this.softwareId);
        when(this.registrationRequest.getRedirectUris()).thenReturn(redirectUrisNotInSsa);

        // When
        OIDCException exception = catchThrowableOfType(() ->
                        tppRegistrationService.verifyTPPRegistrationRequestAgainstSSA(this.registrationRequest,
                                this.validSsaJwtClaims), OIDCException.class);

        // Then
        assertThat(exception.getRegistrationError().getError())
                .isEqualTo(RegistrationError.ErrorEnum.INVALID_REDIRECT_URI);
    }

    @Test
    public void shouldSucceedWhenRequestRedirectUriInSsa_verifyTppRegistrationRequestAgainstSSA()
            throws OBErrorException, OIDCException {

        // Given
        when(this.registrationRequest.getSoftwareId()).thenReturn(this.softwareId);
        when(this.registrationRequest.getRedirectUris()).thenReturn(this.validRedirectUris);

        // When
        tppRegistrationService.verifyTPPRegistrationRequestAgainstSSA(this.registrationRequest,this.validSsaJwtClaims);

        //then
    }

    // ToDo: This is ignored due to issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/17
    @Ignore
    @Test
    public void shouldSucceedToGetRedirectUris_getSsaStringListClaim() throws OIDCException, ParseException {
        // given
        String claimName = OpenBankingConstants.SSAClaims.SOFTWARE_REDIRECT_URIS;

        String registrationRequestJwtSerialised = "eyJraWQiOiI4ODhhNDEyOTU1NjEyNTc4MjM2YTdjOGMwNWVjNDA2YmU0MTczN2M3IiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiI1ZmY4ODAxN2FlMzIxYjAwMTAxZDcwNTkiLCJleHAiOjE2MTAxMjE0MjMsImdyYW50X3R5cGVzIjpbInJlZnJlc2hfdG9rZW4iLCJjbGllbnRfY3JlZGVudGlhbHMiLCJhdXRob3JpemF0aW9uX2NvZGUiXSwiaWRfdG9rZW5fc2lnbmVkX3Jlc3BvbnNlX2FsZyI6IlBTMjU2IiwicmVkaXJlY3RfdXJpcyI6WyJodHRwczovL2dvb2dsZS5jb20iXSwicmVxdWVzdF9vYmplY3RfZW5jcnlwdGlvbl9hbGciOiJSU0EtT0FFUC0yNTYiLCJyZXF1ZXN0X29iamVjdF9lbmNyeXB0aW9uX2VuYyI6IkEyNTZHQ00iLCJyZXF1ZXN0X29iamVjdF9zaWduaW5nX2FsZyI6IlBTMjU2IiwicmVzcG9uc2VfdHlwZXMiOlsiY29kZSBpZF90b2tlbiJdLCJzY29wZSI6Im9wZW5pZCBwYXltZW50cyBmdW5kc2NvbmZpcm1hdGlvbnMgYWNjb3VudHMiLCJzb2Z0d2FyZV9zdGF0ZW1lbnQiOiJleUpyYVdRaU9pSXhObVJpWWpjeVlqZzRZVFUwTUdOak4yVXhNamd4TmpFNFltVmhNV1V3WlRnek1UTmpOalZqSWl3aVlXeG5Jam9pVUZNeU5UWWlmUS5leUp2Y21kZmFuZHJjMTlsYm1Sd2IybHVkQ0k2SWxSUFJFOGlMQ0p6YjJaMGQyRnlaVjl0YjJSbElqb2lWRVZUVkNJc0luTnZablIzWVhKbFgzSmxaR2x5WldOMFgzVnlhWE1pT2x0ZExDSnZjbWRmYzNSaGRIVnpJam9pUVdOMGFYWmxJaXdpYzI5bWRIZGhjbVZmWTJ4cFpXNTBYMmxrSWpvaU5XWm1PRGd3TVRkaFpUTXlNV0l3TURFd01XUTNNRFU1SWl3aWFYTnpJam9pUm05eVoyVlNiMk5ySWl3aWMyOW1kSGRoY21WZmFuZHJjMTlsYm1Sd2IybHVkQ0k2SW1oMGRIQnpPbHd2WEM5elpYSjJhV05sTG1ScGNtVmpkRzl5ZVM1a1pYWXRiMkl1Wm05eVoyVnliMk5yTG1acGJtRnVZMmxoYkRvNE1EYzBYQzloY0dsY0wzTnZablIzWVhKbExYTjBZWFJsYldWdWRGd3ZOV1ptT0Rnd01UZGhaVE15TVdJd01ERXdNV1EzTURVNVhDOWhjSEJzYVdOaGRHbHZibHd2YW5kclgzVnlhU0lzSW5OdlpuUjNZWEpsWDJsa0lqb2lOV1ptT0Rnd01UZGhaVE15TVdJd01ERXdNV1EzTURVNUlpd2liM0puWDJOdmJuUmhZM1J6SWpwYlhTd2liMkpmY21WbmFYTjBjbmxmZEc5eklqb2lhSFIwY0hNNlhDOWNMMlJwY21WamRHOXllUzVrWlhZdGIySXVabTl5WjJWeWIyTnJMbVpwYm1GdVkybGhiRG80TURjMFhDOTBiM05jTHlJc0ltOXlaMTlwWkNJNklqVm1aV0l3WmpNM05ESmxZbUUwTURBeE1UWXpOakF5WWlJc0luTnZablIzWVhKbFgycDNhM05mY21WMmIydGxaRjlsYm1Sd2IybHVkQ0k2SWxSUFJFOGlMQ0p6YjJaMGQyRnlaVjl5YjJ4bGN5STZXeUpCU1ZOUUlpd2lRMEpRU1VraUxDSkVRVlJCSWl3aVVFbFRVQ0pkTENKbGVIQWlPakUyTVRBM01qWXdORE1zSW05eVoxOXVZVzFsSWpvaVFXNXZibmx0YjNWeklpd2liM0puWDJwM2EzTmZjbVYyYjJ0bFpGOWxibVJ3YjJsdWRDSTZJbFJQUkU4aUxDSnBZWFFpT2pFMk1UQXhNakV5TkRNc0ltcDBhU0k2SWpJeU1UUXdZbUpqTFRneE5qRXROR1ZpWXkwNE56UmxMV1F3WVRaaFpHTXpORE5oT0NKOS5WUUdpeHdHbkNJRmFkdUN2bVdIZE5ZcG1oQ2lYTzdBUk80U1FjeHJNQjBtSTVlc2RjQzBqbm05bUl6cGVUWnhDWnNaUUpSLWtjT1B6ckZfWUppMEZ0cGlVekFPTTNVc2ZiNGZMMGgwVjFOLXVPU2VfbkhlQ1Y2SDBNZ1dTNUdXZnNDQXZ1VlQ1d251RGlGR3ZPVVh6Z0xjVzVOOW5hUndITTZEcm9kS0xzU3pxcFAyRUJJYllGc09QcjBsUVVYWmhLbTJQekc5NWlsUlVpT0tIckJXcE11b0xtSU9kR2FIcF9SaFllalp0OHpxY01rWUZzeFpLX3pXc0pFOTlkZTRoRDZGQk9nTlFYYldRWm92M01KYmgzd2Z0cVJPaTYwRGxFS1ZCZkdhUnB0LVNqMWhMUUFkMHNQSzQzclhBLUlmZDZMaVNTM2RFWHVweTMxaWJNVUV0SFEiLCJzdWJqZWN0X3R5cGUiOiJwYWlyd2lzZSIsInRva2VuX2VuZHBvaW50X2F1dGhfbWV0aG9kIjoiY2xpZW50X3NlY3JldF9iYXNpYyIsInRva2VuX2VuZHBvaW50X2F1dGhfc2lnbmluZ19hbGciOiJQUzI1NiJ9.JOZkwdF83xuGhS6U9EAL4Wx-4op8VKP-gkZ26vcEFq2uAQm6oGqHaV6YWf2FC3pm8-LCrRTbG_vOKzxZhv6TLpGsxJ6FkicKNNmlDQVYV-sdGY2rXnWhryn2UaSBYptU4qw4gbJjpwxo8dApA0rOK93dYuLr84oFXGRd83oKfWm6RDZREFMSUVVLbhT9wSxJ4FMJtc30V-Rqk2nTStERgjU-b1JeVG2INeQHw1UCJFzIF87YNB6AKQ8BP3ufI3dC4IpEPhESFZ98IF0D2Orpndl1A5KjzfB22O2Q7_wyA83eyoYG6sSyuyhmk-9M83irVt8DLzYgqt2Z-a68H2N8NA";
        SignedJWT registrationRequestJws = SignedJWT.parse(registrationRequestJwtSerialised);
        String ssaSerialised = registrationRequestJws.getJWTClaimsSet()
                .getStringClaim(OpenBankingConstants.RegistrationTppRequestClaims.SOFTWARE_STATEMENT);

        SignedJWT ssaJws = SignedJWT.parse(ssaSerialised);
        JWTClaimsSet ssaClaims = ssaJws.getJWTClaimsSet();
        JSONObject ssaJwsJson = new JSONObject(ssaClaims.toJSONObject());

        // When
        List<String> redirectUris = tppRegistrationService.getSsaStringListClaim(ssaClaims, claimName, "error");

        // Then
        assertThat(redirectUris).isNotNull();
        assertThat(redirectUris.isEmpty()).isFalse();
    }
}