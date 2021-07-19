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

import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.spring.security.multiauth.model.authentication.X509Authentication;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.x500.X500Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Collection;

@Slf4j
public abstract class ApiClientIdentity {

    X509Authentication authentication;

    public ApiClientIdentity(X509Authentication certificateAuthentication) throws OAuth2InvalidClientException {
        this.authentication = certificateAuthentication;
        this.throwIfNotValidCertAuthority();
    }

    /**
     * Returns true if the MATLS certificate presented by the ApiClient was a PSD2/eIDAS style certificate.
     * @return true if an PSD2/eIDAS certificate, false otherwise
     * */
    public abstract boolean isPSD2Certificate();


    /**
     * Return the unique TppIdentifier. This can be overridden for different ApiClientIdentity types. For example,
     * OBWacs should use the NCA registration ID found in the organisationIdentifier (oid 2.5.4.97) of the subject
     * Name in the certificate. For an OB Transport (legacy OB Directory issued certs) we might need to use the OU
     * field from the certificate subject....
     * @return a UID for the TPP.
     */
    public String getTransportCertificateCn(){
        try {
            X509Certificate transportCert = getTransportCertificate();
            if(transportCert != null) {
                X500Name x500name = new JcaX509CertificateHolder(transportCert).getSubject();
                RDN cn = x500name.getRDNs(BCStyle.CN)[0];
                String cnString = IETFUtils.valueToString(cn.getFirst().getValue());
                return cnString;
            } else {
                log.debug("getTppIdentifier() No certificates available from request");
                throw new ApiClientException("No certificates available from request Principal");
            }
        } catch (CertificateEncodingException | ApiClientException e) {
            log.debug("getTransportCertificateCn() failed to get CN from transport certificate.", e);
            return null;
        }
    }

    /**
     * Returns the leaf certificate of the cert chain presented by the ApiClient
     * @return the client certificate presented to the MATLS endpoint
     */
    public X509Certificate getTransportCertificate(){
        X509Certificate[] certificateChain = this.authentication.getCertificateChain();
        if(certificateChain != null && certificateChain.length >= 1){
            return certificateChain[0];
        } else {
            return null;
        }
    }


    public boolean isUnregistered(){
        return authentication.getAuthorities().contains(OBRIRole.UNREGISTERED_TPP);
    }


    public String getUsername() {
        return authentication.getName();
    }

    /**
     * Return the authorities identified by the spring-security-multi-auth.
     * @return
     */
    public Collection<GrantedAuthority> getAuthorities() {
        return authentication.getAuthorities();
    }

    /**
     * Check the transport certificate is issued by a recognised authority. Will throw with errors that can be
     * directly returned from Client Registration Endpoints.
     */
    public void throwIfNotValidCertAuthority() throws OAuth2InvalidClientException {
        if (this.getAuthorities().contains(OBRIRole.UNKNOWN_CERTIFICATE)) {
            String helpString = "The transport certificate is not signed by a recognised " +
                    "authority";
            X509Certificate certificate = this.getTransportCertificate();
            if(certificate != null){
                X500Principal principal = certificate.getIssuerX500Principal();
                if(principal != null) {
                    helpString += " '" + principal + "'";
                }
            }
            throw new OAuth2InvalidClientException(helpString);
        }
    };

    /**
     * Check if the TPP was identified as being already onboarded
     */
    public void throwIfTppAlreadyOnboarded() throws OAuth2InvalidClientException {
        log.debug("throwIfTppAlreadyOnboarded() User detail: username: '{}' and authorities: '{}'", this.getUsername(),
                this.getAuthorities());
        if (this.getAuthorities().contains(OBRIRole.ROLE_AISP)
                || this.getAuthorities().contains(OBRIRole.ROLE_PISP)
                || this.getAuthorities().contains(OBRIRole.ROLE_CBPII)) {
            String errorMessage = "A software statement has already been registered with this transport certificate.";
            log.debug("throwIfTppAlreadyOnboarded() {}", errorMessage);
            throw new OAuth2InvalidClientException(errorMessage);
        }
    }

    public void throwIfTppNotOnboarded() throws OAuth2InvalidClientException {
        log.debug("throwIfTppNotOnboarded() user detail: username '{}', and authorities: '{}'", this.getUsername(),
                this.getAuthorities());
        Collection<? extends GrantedAuthority> authorities = this.getAuthorities();
        if(authorities.contains(OBRIRole.UNREGISTERED_TPP)){
            String errorString = "Tpp is not onboarded.";
            log.debug("throwIfTppNotOnboarded() {}", errorString);
            throw new OAuth2InvalidClientException(errorString);
        }
    };
}
