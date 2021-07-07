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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.security.auth.x500.X500Principal;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Collection;

/**
 * Application of the decorator pattern to make interpretation of the Spring Security User class more readable
 */
@Slf4j
public class RequesterIdentity extends User {

    private X509Authentication authentication;

    static public RequesterIdentity getUserDecorator(Principal principal){
        X509Authentication authentication = (X509Authentication) principal;
        User user =  (User) authentication.getPrincipal();
        RequesterIdentity decorator = new RequesterIdentity(user.getUsername(), user.getPassword(), user.getAuthorities(),
                authentication);
        decorator.setAuthentication(authentication);
        return decorator;
    }

    private void setAuthentication(X509Authentication authentication) {
        this.authentication = authentication;
    }

    public RequesterIdentity(User user, X509Authentication authentication){
        super(user.getUsername(), user.getPassword(), user.getAuthorities());
        this.authentication = authentication;
    }

    public RequesterIdentity(String username, String password, Collection<? extends GrantedAuthority> authorities,
                             X509Authentication authentication) {
        super(username, password, authorities);
        this.authentication = authentication;
    }

    public boolean isUnregistered() {
        return this.getAuthorities().contains(OBRIRole.UNREGISTERED_TPP);
    }

    /**
     * returns the transport certificate used for MATLS by the client
     * @return the X509Certificate or null if none exists
     */
    public X509Certificate getTransportCertificate() {
        X509Certificate[] certificateChain = this.authentication.getCertificateChain();
        if(certificateChain != null && certificateChain.length >= 1){
            return certificateChain[0];
        } else {
            return null;
        }
    }

    public void throwIfCertificateAuthorityUnknown() throws OAuth2InvalidClientException {
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
    }

    public void throwIfTppAlreadyOnboarded() throws OAuth2InvalidClientException {
        log.debug("throwIfTppAlreadyOnboarded() User detail: username: '{}' and authorities: '{}'", this.getUsername(),
                this.getAuthorities());
        if (this.getAuthorities().contains(OBRIRole.ROLE_AISP)
                || this.getAuthorities().contains(OBRIRole.ROLE_PISP)
                || this.getAuthorities().contains(OBRIRole.ROLE_CBPII)) {
            throw new OAuth2InvalidClientException("A software statement has already been registered with this " +
                    "transport certificate.");
        }
    }

    public boolean presentedEidasCert(){
        return getAuthorities().contains(OBRIRole.ROLE_EIDAS);
    }


}
