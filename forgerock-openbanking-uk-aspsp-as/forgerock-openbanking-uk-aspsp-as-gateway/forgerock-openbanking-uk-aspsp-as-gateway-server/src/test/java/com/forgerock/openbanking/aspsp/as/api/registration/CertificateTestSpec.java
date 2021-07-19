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
package com.forgerock.openbanking.aspsp.as.api.registration;

import com.forgerock.cert.Psd2CertInfo;
import com.forgerock.cert.exception.InvalidPsd2EidasCertificate;
import com.forgerock.openbanking.aspsp.as.TestHelperFunctions;
import com.forgerock.spring.security.multiauth.model.authentication.PSD2Authentication;
import com.forgerock.spring.security.multiauth.model.authentication.X509Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;

public class CertificateTestSpec {

    X509Certificate[] certsChain;
    String certType;
    boolean isEidas;

    public CertificateTestSpec(String certType, String filePath, boolean isEidas) throws CertificateException,
            IOException {
        this.certsChain = TestHelperFunctions.getCertChainFromFile(filePath);
        this.certType = certType;
        this.isEidas = isEidas;
    };

    public X509Authentication getPrincipal(Collection<? extends GrantedAuthority> authorities)
            throws InvalidPsd2EidasCertificate {
        if(isEidas){
            return new PSD2Authentication("testname", authorities, certsChain, new Psd2CertInfo(certsChain));
        } else {
            return new X509Authentication("testname", authorities, certsChain);
        }
    }
}
