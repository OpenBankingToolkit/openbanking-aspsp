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

import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.spring.security.multiauth.model.authentication.X509Authentication;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.security.core.GrantedAuthority;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Collection;

@Slf4j
public abstract class ApiClientIdentity {

    X509Authentication authentication;

    public ApiClientIdentity(X509Authentication certificateAuthentication){
        this.authentication = certificateAuthentication;
    }

    public String getTppIdentifier(){
        try {
            X509Certificate transportCert = getTransportCertificate();
            if(transportCert != null) {
                X500Name x500name = new JcaX509CertificateHolder(transportCert).getSubject();
                RDN cn = x500name.getRDNs(BCStyle.CN)[0];
                return IETFUtils.valueToString(cn.getFirst().getValue());
            } else {
                log.debug("getTppIdentifier() No certificates available from request");
                throw new ApiClientException("No certificates available from request Principal");
            }
        } catch (CertificateEncodingException | ApiClientException e) {
            return null;
        }
    }

    public X509Certificate getTransportCertificate(){
        X509Certificate[] certificateChain = this.authentication.getCertificateChain();
        if(certificateChain != null && certificateChain.length >= 1){
            return certificateChain[0];
        } else {
            return null;
        }
    }

    public abstract boolean isPSD2Certificate();

    public boolean isUnregistered(){
        return authentication.getAuthorities().contains(OBRIRole.UNREGISTERED_TPP);
    }

    public String getUsername() {
        return authentication.getName();
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authentication.getAuthorities();
    }
}
