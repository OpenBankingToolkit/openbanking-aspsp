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
package com.forgerock.openbanking.aspsp.as.service.registrationrequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationRequest;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import static com.forgerock.openbanking.constants.OpenBankingConstants.RegistrationTppRequestClaims;

@Component
@Slf4j
public class RegistrationRequestFactory {

    TppRegistrationService tppRegistrationService;
    DirectorySoftwareStatementFactory softwareStatementFactory;
    ObjectMapper objectMapper;

    @Autowired
    public RegistrationRequestFactory(TppRegistrationService tppRegistrationService,
                                      DirectorySoftwareStatementFactory softwareStatementFactory,
                                      ObjectMapper objectMapper){
        this.tppRegistrationService = tppRegistrationService;
        this.softwareStatementFactory = softwareStatementFactory;
        this.objectMapper = objectMapper;
    }

    public RegistrationRequest getRegistrationRequestFromJwt(String registrationRequestJwtSerialised)  throws DynamicClientRegistrationException {

        try {
            RegistrationRequestJWTClaims registrationRequestJWTClaims =
                    getJwtClaimsSet(registrationRequestJwtSerialised, JWTClaimsOrigin.REGISTRATION_REQUEST_JWT);
            String ssaJwtSerialised =
                    registrationRequestJWTClaims.getRequiredStringClaim(RegistrationTppRequestClaims.SOFTWARE_STATEMENT);
            RegistrationRequestJWTClaims ssaJwtClaims = getJwtClaimsSet(ssaJwtSerialised,
                    JWTClaimsOrigin.SOFTWARE_STATEMENT_ASSERTION);
            String issuer = ssaJwtClaims.getRequiredStringClaim(OpenBankingConstants.SSAClaims.ISSUER);

            tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(ssaJwtSerialised, issuer);
            DirectorySoftwareStatement regRequestSoftwareStatement =
                    softwareStatementFactory.getSoftwareStatement(ssaJwtClaims);

            String softwareJwkUri = regRequestSoftwareStatement.getSoftware_jwks_endpoint();
            String softwareClientId =
                    ssaJwtClaims.getRequiredStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_CLIENT_ID);
            tppRegistrationService.verifyTPPRegistrationRequestSignature(registrationRequestJwtSerialised,
                    softwareClientId, softwareJwkUri);
            Map<String, Object> registrationRequestClaimsAsJson = registrationRequestJWTClaims.toJSONObject();
            String registrationRequestJson = JSONObjectUtils.toJSONString(registrationRequestClaimsAsJson);

            try {
                RegistrationRequest request = objectMapper.readValue(registrationRequestJson, RegistrationRequest.class);
                request.setJson(registrationRequestJson);
                request.setDirectorySoftwareStatement(regRequestSoftwareStatement);
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


    private RegistrationRequestJWTClaims getJwtClaimsSet(String registrationRequestJwtSerialised,
                                                         JWTClaimsOrigin origin) throws ParseException,
            DynamicClientRegistrationException {
        SignedJWT registrationRequestJws = SignedJWT.parse(registrationRequestJwtSerialised);
        JWTClaimsSet registrationRequestClaimsSet = registrationRequestJws.getJWTClaimsSet();
        if(registrationRequestClaimsSet == null){
            String errorString = "Registration Request JWT contained no claims";
            log.debug("getRegistrationRequestFromJwt() {}", errorString);
            throw new DynamicClientRegistrationException(errorString,
                    DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
        }
        RegistrationRequestJWTClaims registrationRequestJWTClaims =
                new RegistrationRequestJWTClaims(registrationRequestClaimsSet,
                        origin);
        return registrationRequestJWTClaims;
    }

    public RegistrationRequest getRegistrationRequestFromManualRegistrationJson(String registrationRequestJson,
                                                                                ManualRegistrationRequest manualRegistrationRequest,
                                                                                ObjectMapper objectMapper)
            throws DynamicClientRegistrationException {
        try{
            RegistrationRequest request = objectMapper.readValue(registrationRequestJson, RegistrationRequest.class);
            request.setRedirectUris(manualRegistrationRequest.getRedirectUris());
            String softwareStatementAssertion = manualRegistrationRequest.getSoftwareStatementAssertion();
            if (StringUtils.isEmpty(softwareStatementAssertion)){
                String errorMessage = "Manual Request did not contain a valid software statement";
                log.info("getRegistrationRequestFromManualregistrationJson() {}", errorMessage);
                throw new DynamicClientRegistrationException(errorMessage,
                        DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT);
            }
            request.setSoftwareStatement(softwareStatementAssertion);
            request.setJson(registrationRequestJson);

            String ssaJwtSerialised = request.getSoftwareStatement();
            RegistrationRequestJWTClaims softwareStatementClaims = getJwtClaimsSet(ssaJwtSerialised,
                    JWTClaimsOrigin.SOFTWARE_STATEMENT_ASSERTION);
            String issuer = softwareStatementClaims.getRequiredStringClaim(OpenBankingConstants.SSAClaims.ISSUER);
            tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(ssaJwtSerialised, issuer);
            DirectorySoftwareStatement regRequestSoftwareStatement =
                    softwareStatementFactory.getSoftwareStatement(softwareStatementClaims);




            request.setDirectorySoftwareStatement(regRequestSoftwareStatement);

            return request;
        } catch (IOException | ParseException ioe){
            String errorMessage = "Could not map Manual Registration Request JWT fields to internal object";
            log.info("getRegistrationRequestFromManualRegistrationJson() {}", errorMessage, ioe);
            throw new DynamicClientRegistrationException("Could not map registration request jwt fields to " +
                    "internal object " + ioe.getMessage(), DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
        }
    }


}
