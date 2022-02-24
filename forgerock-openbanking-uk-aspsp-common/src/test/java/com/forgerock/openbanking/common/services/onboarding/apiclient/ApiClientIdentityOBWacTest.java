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
package com.forgerock.openbanking.common.services.onboarding.apiclient;

import com.forgerock.openbanking.common.TestHelperFunctions;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2Exception;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.spring.security.multiauth.model.authentication.PSD2Authentication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ApiClientIdentityOBWacTest {

    private PSD2Authentication authentication = mock(PSD2Authentication.class);
    private final String QWAC_CERT_PATH = "src/test/resources/certificates/QWac.pem";
    private final String QWAC_CERT_CN = "api.getkevin.eu";
    X509Certificate[] certChain;

    @Before
    public void setUp() throws Exception {
         certChain = TestHelperFunctions.getCertChainFromFile(QWAC_CERT_PATH);
    }

    @After
    public void tearDown() throws Exception {
        reset(authentication);
    }

    @Test
    public void getAuthorisationNumber() throws CertificateException, IOException, OAuth2InvalidClientException {
        // Given
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(QWAC_CERT_PATH);
        when(authentication.getCertificateChain()).thenReturn(certChain);
        ApiClientIdentity apiClientIdentity = new ApiClientIdentityOBWac(authentication);
        // When
        Optional<String> authorisationNumber = apiClientIdentity.getAuthorisationNumber();

        // Then
        assertThat(authorisationNumber.isPresent()).isTrue();
        assertThat(authorisationNumber.get()).isEqualTo("PSDLT-BL-LB000458");
    }

    @Test
    public void getTppIdentifier() throws CertificateException, IOException, OAuth2InvalidClientException {
        // Given
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(QWAC_CERT_PATH);
        when(authentication.getCertificateChain()).thenReturn(certChain);
        ApiClientIdentity apiClientIdentity = new ApiClientIdentityOBWac(authentication);
        // When
        String tppIdentifier = apiClientIdentity.getTppIdentifier();

        // Then
        assertThat(tppIdentifier).isEqualTo("PSDLT-BL-LB000458");
    }

    @Test
    public void isPSD2Certificate() throws OAuth2InvalidClientException, CertificateException, IOException {
        // Given
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(QWAC_CERT_PATH);
        when(authentication.getCertificateChain()).thenReturn(certChain);
        ApiClientIdentity apiClientIdentity = new ApiClientIdentityOBWac(authentication);
        // When
        boolean isPSD2Cert = apiClientIdentity.isPSD2Certificate();

        // Then
        assertThat(isPSD2Cert).isTrue();
    }

    @Test
    public void getTransportCertificateCn() throws CertificateException, IOException, OAuth2InvalidClientException {
        // Given
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(QWAC_CERT_PATH);
        when(authentication.getCertificateChain()).thenReturn(certChain);
        ApiClientIdentity apiClientIdentity = new ApiClientIdentityOBWac(authentication);
        // When
        String certCn = apiClientIdentity.getTransportCertificateCn();

        // Then
        assertThat(certCn).isEqualTo(QWAC_CERT_CN);
    }

    @Test
    public void getTransportCertificate() throws CertificateException, IOException, OAuth2InvalidClientException {
        // Given
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(QWAC_CERT_PATH);
        when(authentication.getCertificateChain()).thenReturn(certChain);
        ApiClientIdentity apiClientIdentity = new ApiClientIdentityOBWac(authentication);
        // When
        X509Certificate transportCert = apiClientIdentity.getTransportCertificate();

        // Then
        assertThat(transportCert).isEqualTo(certChain[0]);
    }

    @Test
    public void getUsername() throws CertificateException, IOException, OAuth2InvalidClientException {
        // Given
        String USERNAME = "Username";
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(QWAC_CERT_PATH);
        when(authentication.getCertificateChain()).thenReturn(certChain);
        when(authentication.getName()).thenReturn(USERNAME);
        ApiClientIdentity apiClientIdentity = new ApiClientIdentityOBWac(authentication);
        // When
        String username = apiClientIdentity.getUsername();

        // Then
        assertThat(username).isEqualTo(USERNAME);
    }


    @Test
    public void throwIfNotValidCertAuthority() throws CertificateException, IOException {
        // Given
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(QWAC_CERT_PATH);
        when(authentication.getCertificateChain()).thenReturn(certChain);
        Collection<GrantedAuthority> authorities = new ArrayList<>(List.of(OBRIRole.UNKNOWN_CERTIFICATE));
        when(authentication.getAuthorities()).thenReturn(authorities);

        // When
        OAuth2InvalidClientException exception =
                catchThrowableOfType( () -> new ApiClientIdentityOBWac(authentication),
                        OAuth2InvalidClientException.class);

        //Then
        assertThat(exception.getRfc6750ErrorCode()).isEqualTo(OAuth2Exception.INVALID_CLIENT);
    }
}