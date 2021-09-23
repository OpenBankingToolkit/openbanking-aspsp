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
package com.forgerock.openbanking.aspsp.as.service.apiclient;

import com.forgerock.openbanking.aspsp.as.TestHelperFunctions;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ApiClientIdentityQWacTest {

    private PSD2Authentication authentication = mock(PSD2Authentication.class);
    private final String OBWAC_CERT_PATH = "src/test/resources/certificates/OBWac.pem";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAuthorisationNumber() throws CertificateException, IOException, OAuth2InvalidClientException {
        // Given
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(OBWAC_CERT_PATH);
        when(authentication.getCertificateChain()).thenReturn(certChain);
        ApiClientIdentity apiClientIdentity = new ApiClientIdentityOBWac(authentication);
        // When
        Optional<String> authorisationNumber = apiClientIdentity.getAuthorisationNumber();

        // Then
        assertThat(authorisationNumber.isPresent()).isTrue();
        assertThat(authorisationNumber.get()).isEqualTo("PSDGB-OB-Unknown0015800001041REAAY");
    }

    @Test
    public void isPSD2Certificate() throws OAuth2InvalidClientException, CertificateException, IOException {
        // Given
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(OBWAC_CERT_PATH);
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
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(OBWAC_CERT_PATH);
        when(authentication.getCertificateChain()).thenReturn(certChain);
        ApiClientIdentity apiClientIdentity = new ApiClientIdentityOBWac(authentication);
        // When
        String certCn = apiClientIdentity.getTransportCertificateCn();

        // Then
        assertThat(certCn).isEqualTo(TestHelperFunctions.FORGEROCK_OB_TEST_DIRECTORY_ORG_ID);
    }

    @Test
    public void getTransportCertificate() throws CertificateException, IOException, OAuth2InvalidClientException {
        // Given
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(OBWAC_CERT_PATH);
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
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(OBWAC_CERT_PATH);
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
        X509Certificate[] certChain = TestHelperFunctions.getCertChainFromFile(OBWAC_CERT_PATH);
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

    @Test
    public void testGetTransportCertificateCn() {
    }
}