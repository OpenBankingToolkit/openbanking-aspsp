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

import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.forgerock.openbanking.constants.OpenBankingConstants.SSAClaims;

@Slf4j
public class RegistrationRequestJWTClaims {

    private final JWTClaimsSet claimsSet;
    private final JWTClaimsOrigin claimsOrigin;
    private final DynamicClientRegistrationErrorType errorType;

    public RegistrationRequestJWTClaims(JWTClaimsSet claimsSet, JWTClaimsOrigin claimsOrigin){
        this.claimsSet = claimsSet;
        this.claimsOrigin = claimsOrigin;
        if(claimsOrigin == JWTClaimsOrigin.REGISTRATION_REQUEST_JWT){
            this.errorType = DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA;
        } else {
            this.errorType = DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT;
        }
    }


    /**
     * getRequiredStringClaim Obtains a string containing the value for a specific claim. Catches the
     * ParseExceptions thrown by nimbusds and instead throw an DynamicClientRegistrationException.
     *
     * @param claimName        @String the name of the SSA claim to obtain from the ssaClaims
     * @return A non null or empty @String containing the ssa claim value
     * @throws DynamicClientRegistrationException when the claim specified by the claimName does not exist, or is empty.
     */
    public String getRequiredStringClaim(String claimName) throws DynamicClientRegistrationException {
        Optional<String> claimValue = getOptionalStringClaim(claimName);
        if(!stringClaimValueIsValid(claimValue)){
            String errorString = "Claim '" + claimName + "' was not set in " + claimsOrigin + ". This claim " +
                    "is required";
            log.debug("getStringClaims() {}", errorString);
            throw new DynamicClientRegistrationException(errorString, errorType);
        }
        return claimValue.get();
    }

    /**
     * getRequiredStringClaim Obtains a string containing the value for a specific claim. Catches the
     * ParseExceptions thrown by nimbusds and instead throw an DynamicClientRegistrationException.
     *
     * @param claimName        @String the name of the SSA claim to obtain from the ssaClaims
     * @return An Optional<String> that will contain the value if it was present or be empty of not
     * @throws DynamicClientRegistrationException when the claim specified by the claimName does not exist, or is empty.
     */
    public Optional<String> getOptionalStringClaim(String claimName) throws DynamicClientRegistrationException {
        try {
            return Optional.ofNullable(claimsSet.getStringClaim(claimName));
        } catch (ParseException e) {
            String errorString =
                    "Failed to parse claim '" + claimName + "' from claim set in " + claimsOrigin.toString();
            log.debug("getStringClaims() {}", errorString, e);
            throw new DynamicClientRegistrationException(errorString, errorType);
        }
    }

    private boolean stringClaimValueIsValid(Optional<String> claimValue){
        return claimValue.isPresent() && !StringUtils.isEmpty(claimValue.get());
    }


    /**
     * getRequiredStringListClaims Obtains a list of strings containing the values for a specific claim. Catches the
     * ParseExceptions thrown by nimbusds and instead throw an DynamicClientRegistrationException.
     *
     * @param claimName  {@code String} the name of the SSA claim to obtain from the ssaClaims
     * @return A non null or empty {@code List<String>} containing the ssa claim value
     * @throws @DynamicClientRegistrationException when the claim specified by the claimName does not exist, or is
     * empty.
     */
    public List<String> getRequiredStringListClaims(String claimName) throws DynamicClientRegistrationException {
            Optional<List<String>> values = getOptionalStringListClaims(claimName);
            if (!stringListClaimsAreValid(values)) {
                String errorString = "Claim '" + claimName + "' was not set in " + claimsOrigin + ". This claim " +
                        "is required";
                log.debug("getStringClaims() {}", errorString);
                throw new DynamicClientRegistrationException(errorString, getErrorType(claimName));
            }
            return values.get();

    }

    private boolean stringListClaimsAreValid(Optional<List<String>> values) {
        return values.isPresent() && !values.get().isEmpty();
    }

    public long getRequiredLongValue(String claimName) throws DynamicClientRegistrationException {
        Optional<Long> value = getOptionalLongValue(claimName);
        if (value.isEmpty()){
            String errorString = "Claim '" + claimName + "' was not set in " + claimsOrigin + ". This claim " +
                    "is required";
            log.debug("getRequiredLongValue() {}", errorString);
            throw new DynamicClientRegistrationException(errorString, getErrorType(claimName));
        }
        return value.get();
    }


    public Optional<Long> getOptionalLongValue(String claimName) throws DynamicClientRegistrationException {
        try {
            return Optional.ofNullable(this.claimsSet.getLongClaim(claimName));
        } catch (ParseException pe){
            String errorString =
                    "Failed to parse claim '" + claimName + "' from claim set in " + claimsOrigin.toString();
            log.debug("getOptionalLongValue() {}", errorString, pe);
            throw new DynamicClientRegistrationException(errorString, getErrorType(claimName));
        }
    }

    public Date getRequiredDateValue(String claimName) throws DynamicClientRegistrationException {
        Optional<Date> value = getOptionalDateValue(claimName);
        if (value.isEmpty()){
            String errorString = "Claim '" + claimName + "' was not set in " + claimsOrigin + ". This claim " +
                    "is required";
            log.debug("getRequiredLongValue() {}", errorString);
            throw new DynamicClientRegistrationException(errorString, getErrorType(claimName));
        }
        return value.get();
    }

    public Optional<Date> getOptionalDateValue(String claimName) throws DynamicClientRegistrationException {
        return Optional.ofNullable((Date) this.claimsSet.getClaim(claimName));
    }

    public Double getRequiredDoubleClaim(String claimName) throws DynamicClientRegistrationException {
        Optional<Double> value = getOptionalDoubleClaim(claimName);
        if(value.isEmpty()){
            String errorString = "Claim '" + claimName + "' was not set in " + claimsOrigin + ". This claims is " +
                    "required.";
            log.debug("getRequiredDoubleClaim() {}", errorString);
            throw new DynamicClientRegistrationException(errorString, getErrorType(claimName));
        }
        return value.get();
    }

    public Optional<Double> getOptionalDoubleClaim(String claimName) throws DynamicClientRegistrationException {
        try {
            return Optional.ofNullable(this.claimsSet.getDoubleClaim(claimName));
        } catch (ParseException pe){
            String errorString =
                    "Failed to parse claim '" + claimName + "' from claim set in " + claimsOrigin.toString();
            log.debug("getOptionalLongValue() {}", errorString, pe);
            throw new DynamicClientRegistrationException(errorString, getErrorType(claimName));
        }
    }


    public Optional<List<String>> getOptionalStringListClaims(String claimName) throws DynamicClientRegistrationException {
        try{
            return Optional.ofNullable(claimsSet.getStringListClaim(claimName));
        } catch (ParseException e){
            String errorString =
                    "Failed to parse claim '" + claimName + "' from claim set in " + claimsOrigin.toString();
            log.debug("getStringListClaims() {}", errorString, e);
            throw new DynamicClientRegistrationException(errorString, getErrorType(claimName));
        }
    }

    private DynamicClientRegistrationErrorType getErrorType(String claimName){
        if(claimName.equals(SSAClaims.SOFTWARE_REDIRECT_URIS)){
            return DynamicClientRegistrationErrorType.INVALID_REDIRECT_URI;
        } else {
            return this.errorType;
        }
    }

    public Object getClaim(String claimName){
        return this.claimsSet.getClaim(claimName);
    }

    public Map<String, Object> toJSONObject(){
        return claimsSet.toJSONObject();
    }

}
