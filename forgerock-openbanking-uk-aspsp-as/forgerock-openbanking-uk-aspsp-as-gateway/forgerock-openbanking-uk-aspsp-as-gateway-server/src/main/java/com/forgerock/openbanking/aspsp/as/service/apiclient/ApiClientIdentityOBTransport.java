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

import com.forgerock.openbanking.aspsp.as.service.registrationrequest.RegistrationRequest;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.spring.security.multiauth.model.authentication.X509Authentication;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ApiClientIdentityOBTransport extends ApiClientIdentity {

    public ApiClientIdentityOBTransport(X509Authentication certificateAuthentication)
            throws OAuth2InvalidClientException {
        super(certificateAuthentication);
    }

    @Override
    public boolean isPSD2Certificate() {
        return false;
    }

    @Override
    public Optional<String> getAuthorisationNumber() {
        log.info("OBTransport certificates do not have the AuthorisationNumber field in the subject: 2.5.4.97");
        return Optional.empty();
    }

    @Override
    public String getTppIdentifier() {
        return this.getTransportCertificateCn();
    }

    @Override
    public boolean wasIssuedWith(RegistrationRequest registrationRequest) {
        return getTppIdentifier().equals(registrationRequest.getSoftwareId());
    }
}
