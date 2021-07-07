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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import static com.forgerock.openbanking.constants.OpenBankingConstants.RegistrationTppRequestClaims;

@Component
@Slf4j
public class RegistrationRequestFactory {

    TppRegistrationService tppRegistrationService;

    @Autowired
    public RegistrationRequestFactory(TppRegistrationService tppRegistrationService){
        this.tppRegistrationService = tppRegistrationService;
    }

    public RegistrationRequest getRegistrationRequest(String registrationRequestJwtSerialised
            , ObjectMapper objectMapper) throws DynamicClientRegistrationException {

        SignedJWT registrationRequestJws;
        try {
            registrationRequestJws = SignedJWT.parse(registrationRequestJwtSerialised);
            String ssaSerialised = registrationRequestJws.getJWTClaimsSet()
                    .getStringClaim(RegistrationTppRequestClaims.SOFTWARE_STATEMENT);
            if(ssaSerialised == null){
                log.debug("getRegistrationRequestFromSerialisedJwt() No SSA provided in registationJWT");
                throw new DynamicClientRegistrationException("The software_statement field of the registration request " +
                        "object must contain a valid software statement signed by a trusted issuer",
                        DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
            }

            JWTClaimsSet registrationRequestClaimsSet = registrationRequestJws.getJWTClaimsSet();
            if(registrationRequestClaimsSet == null) {
                throw new DynamicClientRegistrationException("Registration Request JWT contained no claims",
                        DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
            }
            Map<String, Object> registrationRequestClaimsAsJson = registrationRequestClaimsSet.toJSONObject();
            String registrationRequestJson = JSONObjectUtils.toJSONString(registrationRequestClaimsAsJson);

            try {
                RegistrationRequest request = objectMapper.readValue(registrationRequestJson, RegistrationRequest.class);
                request.setJson(registrationRequestJson);
                request.setTppRegistrationService(tppRegistrationService);
                return request;
            } catch (IOException ioe){

                throw new DynamicClientRegistrationException("Could not map registration request jwt fields to " +
                        "internal object " + ioe.getMessage(), DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
            }
        } catch (ParseException pe){
            String errorMessage = "Could not parse the provided registration JWT";
            log.debug("getRegistrationRequest() {}", errorMessage, pe);
            throw new DynamicClientRegistrationException(errorMessage,
                    DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA );
        }
    }
}
