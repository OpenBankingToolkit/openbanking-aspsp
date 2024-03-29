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
package com.forgerock.openbanking.common.services.onboarding.registrationrequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.common.services.onboarding.TppRegistrationService;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationRequest;
import com.forgerock.openbanking.model.DirectorySoftwareStatement;
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

    public RegistrationRequest getRegistrationRequestFromJwt(String registrationRequestJwtSerialised)
            throws DynamicClientRegistrationException {

        String methodName = "getRegistrationRequestFromJwt()";
        try {
            log.debug("{} called", methodName);
            RegistrationRequestJWTClaims registrationRequestJWTClaims =
                    getJwtClaimsSet(registrationRequestJwtSerialised, JWTClaimsOrigin.REGISTRATION_REQUEST_JWT);
            String ssaJwtSerialised =
                    registrationRequestJWTClaims.getRequiredStringClaim(RegistrationTppRequestClaims.SOFTWARE_STATEMENT);
            RegistrationRequestJWTClaims ssaJwtClaims = getJwtClaimsSet(ssaJwtSerialised,
                    JWTClaimsOrigin.SOFTWARE_STATEMENT_ASSERTION);
            String issuer = ssaJwtClaims.getRequiredStringClaim(OpenBankingConstants.SSAClaims.ISSUER);
            log.debug("{}, SSA issuer is '{}'", methodName, issuer);

            tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(ssaJwtSerialised, issuer);
            log.info("{}. SSA is valid and was issued by '{}'", methodName, issuer);

            DirectorySoftwareStatement softwareStatement =
                    softwareStatementFactory.getSoftwareStatement(ssaJwtClaims);
            String softwareJwkUri = softwareStatement.getSoftware_jwks_endpoint();
            String softwareClientId =
                    ssaJwtClaims.getRequiredStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_CLIENT_ID);
            tppRegistrationService.verifyTPPRegistrationRequestSignature(registrationRequestJwtSerialised,
                    softwareClientId, softwareJwkUri);
            Map<String, Object> registrationRequestClaimsAsJson = registrationRequestJWTClaims.toJSONObject();
            String registrationRequestJson = JSONObjectUtils.toJSONString(registrationRequestClaimsAsJson);

            try {
                RegistrationRequest request = objectMapper.readValue(registrationRequestJson, RegistrationRequest.class);
                request.setJson(registrationRequestJson);
                request.setDirectorySoftwareStatement(softwareStatement);
                return request;
            } catch (IOException ioe){
                String errorMessage =
                        "Could not map registration request jwt fields to internal object; " + ioe.getMessage();
                log.info("{}; {} registrationRequestJson; '{}'", methodName, errorMessage, registrationRequestJson);
                throw new DynamicClientRegistrationException(errorMessage,
                        DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
            }
        } catch (ParseException pe){
            String errorMessage = "Could not parse the provided registration JWT";
            log.info("{}; {} {}", methodName, errorMessage, registrationRequestJwtSerialised, pe);
            throw new DynamicClientRegistrationException(errorMessage,
                    DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA );
        }
    }


    private RegistrationRequestJWTClaims getJwtClaimsSet(String registrationRequestJwtSerialised,
                                                         JWTClaimsOrigin origin) throws ParseException,
            DynamicClientRegistrationException {
        String methodName = "getJwtClaimSet()";
        log.trace("{} called with jwt of type {}", methodName, origin.toString());
        SignedJWT registrationRequestJws = SignedJWT.parse(registrationRequestJwtSerialised);
        JWTClaimsSet registrationRequestClaimsSet = registrationRequestJws.getJWTClaimsSet();
        if(registrationRequestClaimsSet == null){
            String errorString = "Registration Request JWT contained no claims";
            log.info("{}; {}. jwtSerialised is {}", methodName, errorString, registrationRequestJwtSerialised);
            throw new DynamicClientRegistrationException(errorString,
                    DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
        }
        RegistrationRequestJWTClaims registrationRequestJWTClaims =
                new RegistrationRequestJWTClaims(registrationRequestClaimsSet, origin);
        return registrationRequestJWTClaims;
    }

    public RegistrationRequest getRegistrationRequestFromManualRegistrationJson(String registrationRequestJson,
                                                                                ManualRegistrationRequest manualRegistrationRequest,
                                                                                ObjectMapper objectMapper)
            throws DynamicClientRegistrationException {
        String methodName = "getRegistrationRequestFromManualRegistrationJson()";
        log.debug("{} called with regisratrationRequestJson; '{}', manualRegistrationRequest; '{}'", methodName,
                registrationRequestJson, manualRegistrationRequest);
        try{
            RegistrationRequest request = objectMapper.readValue(registrationRequestJson, RegistrationRequest.class);
            request.setRedirectUris(manualRegistrationRequest.getRedirectUris());
            String softwareStatementAssertion = manualRegistrationRequest.getSoftwareStatementAssertion();
            if (StringUtils.isEmpty(softwareStatementAssertion)){
                String errorMessage = "Manual Request did not contain a valid software statement";
                log.info("{} {}. registrationRequestJson is '{}'", methodName, errorMessage, registrationRequestJson);
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
            log.debug("{}, returning registrationRequest; '{}'", methodName, request);
            return request;
        } catch (IOException | ParseException ioe){
            String errorMessage = "Could not map Manual Registration Request JWT fields to internal object";
            log.info("{} {}",methodName, errorMessage, ioe);
            throw new DynamicClientRegistrationException("Could not map registration request jwt fields to " +
                    "internal object " + ioe.getMessage(), DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
        }
    }
}
