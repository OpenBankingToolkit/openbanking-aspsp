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
package com.forgerock.openbanking.rs.ui.api.services;

import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
@Component
public class DataApiHelperService {

    private final CryptoApiClient cryptoApiClient;

    public DataApiHelperService(CryptoApiClient cryptoApiClient) {
        this.cryptoApiClient = cryptoApiClient;
    }

    /**
     * getTppName
     * @param principal - A principal representing the TPP that made a request to the service
     * @return A <code>String</code> containing the tppName
     */
    public String getTppName(Principal principal) throws OAuth2InvalidClientException {
        String tppName = null;
        Authentication authentication =(Authentication) principal;
        if(authentication != null){
            Object authPrincipal = authentication.getPrincipal();
            if(authPrincipal != null){
                UserDetails requestApiClient = (UserDetails) authentication.getPrincipal();
                if(requestApiClient != null) {
                    tppName = requestApiClient.getUsername();
                    log.debug("getTppName() tppName is '{}'", tppName);
                    return tppName;
                } else {
                    log.info("getTppName() Unable to obtain UserDetails from Principal '{}'", principal.getName());
                }
            } else {
                log.info("getTppName() Unable to obtain authPrincipal from Authentication '{}'", authentication);
            }
        } else {
            log.info("getTppName() Principal is not of type Authentication; '{}'", principal);
        }
        throw new OAuth2InvalidClientException("Could not obtain tppId from principal " + principal.toString());
    }

    public String getPsuNameFromSession(String obriSession) throws OBErrorException {
        try {
            SignedJWT session = cryptoApiClient.decryptJwe(obriSession);
            String psuName = session.getJWTClaimsSet().getSubject();
            return psuName;
        } catch (Exception e) {
            log.info("getPsuNameFromSession() caught exception getting psu name from session '{};", obriSession, e);
        }
        throw new OBErrorException(OBRIErrorType.SESSION_TOKEN_INVALID_FORMAT);
    }
}
