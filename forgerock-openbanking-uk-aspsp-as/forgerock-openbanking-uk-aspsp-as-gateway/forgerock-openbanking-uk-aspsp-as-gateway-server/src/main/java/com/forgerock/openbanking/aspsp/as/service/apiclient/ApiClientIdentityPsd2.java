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
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.RegistrationRequest;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.spring.security.multiauth.model.authentication.PSD2Authentication;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public abstract class ApiClientIdentityPsd2 extends ApiClientIdentity {

    private final PSD2Authentication authentication;
    private Psd2CertInfo psd2CertInfo;
    private String authorisationNumber;

    protected ApiClientIdentityPsd2(PSD2Authentication authentication) throws OAuth2InvalidClientException {
        super(authentication);
        this.authentication = authentication;
        try {
            this.psd2CertInfo = new Psd2CertInfo(authentication.getCertificateChain());
            Optional<String> isAuthorisationNumber = this.psd2CertInfo.getOrganizationId();
            if(isAuthorisationNumber.isPresent()){
                this.authorisationNumber = isAuthorisationNumber.get();
            } else {
                String errorString = "PSD2 eIDAS certificate must have organisationalIdentifier set in the subject";
                log.info("APIClientIdentityPsd2() {}", errorString);
                throw new OAuth2InvalidClientException(errorString);
            }
        } catch (InvalidPsd2EidasCertificate e) {
            String errorString = "To register you must use a PSD2 eIDAS certificate from a trusted issuer, such as the OpenBanking test directory.";
            log.info(errorString, e);
            throw new OAuth2InvalidClientException(errorString);
        }
    }

    @Override
    public Optional<String> getAuthorisationNumber(){
        return this.psd2CertInfo.getOrganizationId();
    }

    @Override
    public String getTppIdentifier(){
        return this.authorisationNumber;
    }

    @Override
    public boolean isPSD2Certificate() {
        return this.psd2CertInfo.isPsd2Cert();
    }

    @Override
    public boolean wasIssuedWith(RegistrationRequest registrationRequest) {
        String regRequestAuthNum = registrationRequest.getAuthorisationNumber();
        boolean wasIssuedWith = this.getTppIdentifier().equals(regRequestAuthNum);
        if(!wasIssuedWith) {
            log.info("wasIssuedWith() registrationRequest was not issued to this api client. ApiClientIdentity '{}', " +
                    "registrationRequest was issued to '{}'", this.getTppIdentifier(), regRequestAuthNum);
        } else {
            log.debug("wasIssuedWith() registrationRequest was issued to this ApiClientIdentity '{};", this);
        }
        return wasIssuedWith;
    }
}
