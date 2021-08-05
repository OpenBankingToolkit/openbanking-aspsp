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

import com.forgerock.cert.Psd2CertInfo;
import com.forgerock.cert.exception.InvalidPsd2EidasCertificate;
import com.forgerock.openbanking.aspsp.as.TestHelperFunctions;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.spring.security.multiauth.model.authentication.PSD2Authentication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class ApiClientIdentityFactoryTest {

    @Test
    public void returnsApiClientFRTransport_getApiClientIdentity() throws CertificateException, IOException,
            ApiClientException, InvalidPsd2EidasCertificate, OAuth2InvalidClientException {
        // given
        X509Certificate[] certificatesChain = TestHelperFunctions.getCertChainFromFile(
                "src/test/resources/certificates/fr-transport.pem");
        Psd2CertInfo certInfo = new Psd2CertInfo(certificatesChain);
        String tppName = "TestTppName";
        Collection<OBRIRole> authorities = new ArrayList<>();
        authorities.add(OBRIRole.UNREGISTERED_TPP);
        PSD2Authentication authentication = new PSD2Authentication(tppName, authorities,certificatesChain, certInfo);
        ApiClientIdentityFactory identityFactory = new ApiClientIdentityFactory();

        // when
        ApiClientIdentity identity = identityFactory.getApiClientIdentity(authentication);

        // then
        assertThat(identity).isInstanceOf(ApiClientIdentityFRTransport.class);
    }

    @Test
    public void returnsApiClientOBWac_getApiClientIdentity() throws CertificateException, IOException,
            ApiClientException, InvalidPsd2EidasCertificate, OAuth2InvalidClientException {
        // given
        X509Certificate[] certificatesChain = TestHelperFunctions.getCertChainFromFile(
                "src/test/resources/certificates/OBWac.pem");
        Psd2CertInfo certInfo = new Psd2CertInfo(certificatesChain);
        String tppName = "TestTppName";
        Collection<OBRIRole> authorities = new ArrayList<>();
        authorities.add(OBRIRole.UNREGISTERED_TPP);
        PSD2Authentication authentication = new PSD2Authentication(tppName, authorities,certificatesChain, certInfo);
        ApiClientIdentityFactory identityFactory = new ApiClientIdentityFactory();

        // when
        ApiClientIdentity identity = identityFactory.getApiClientIdentity(authentication);

        // then
        assertThat(identity).isInstanceOf(ApiClientIdentityOBWac.class);
    }

    @Test
    public void returnsApiClientQWac_getApiClientIdentity() throws CertificateException, IOException,
            InvalidPsd2EidasCertificate, ApiClientException, OAuth2InvalidClientException {
        // given
        X509Certificate[] certificatesChain = TestHelperFunctions.getCertChainFromFile(
                "src/test/resources/certificates/QWac.pem");
        Psd2CertInfo certInfo = new Psd2CertInfo(certificatesChain);
        String tppName = "TestTppName";
        Collection<OBRIRole> authorities = new ArrayList<>();
        authorities.add(OBRIRole.UNREGISTERED_TPP);
        PSD2Authentication authentication = new PSD2Authentication(tppName, authorities,certificatesChain, certInfo);
        ApiClientIdentityFactory identityFactory = new ApiClientIdentityFactory();

        // when
        ApiClientIdentity identity = identityFactory.getApiClientIdentity(authentication);

        // then
        assertThat(identity).isInstanceOf(ApiClientIdentityQWac.class);
    }

}