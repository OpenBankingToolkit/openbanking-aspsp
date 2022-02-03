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
package com.forgerock.openbanking.common.services.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.common.error.exception.PermissionDenyException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2BearerTokenUsageInvalidTokenException;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountRequest;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.org.openbanking.OBConstants;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.forgerock.openbanking.model.error.OBRIErrorType.SERVER_ERROR;

@Service
@Slf4j
public class AccessTokenService {

    private final ObjectMapper objectMapper;
    private final AMResourceServerService amResourceServerService;
    private final TppStoreService tppStoreService;

    public AccessTokenService(ObjectMapper objectMapper, AMResourceServerService amResourceServerService,
                              TppStoreService tppStoreService) {
        this.objectMapper = objectMapper;
        this.amResourceServerService = amResourceServerService;
        this.tppStoreService = tppStoreService;
    }


    public String getIntentId(SignedJWT accessToken) throws ParseException, IOException {
        String claims = accessToken.getJWTClaimsSet().getStringClaim(OpenBankingConstants.AMAccessTokenClaim.CLAIMS);
        JsonNode jsonClaims = objectMapper.readTree(claims);
        JsonNode idTokenClaims = jsonClaims.get(OpenBankingConstants.AMAccessTokenClaim.ID_TOKEN);
        JsonNode intentClaims = idTokenClaims.get(OpenBankingConstants.AMAccessTokenClaim.INTENT_ID);
        return intentClaims.path("value").asText();
    }

    public List<FRExternalPermissionsCode> isAllowed(AccountRequest accountRequest,
                                                     List<FRExternalPermissionsCode> permissions) throws PermissionDenyException {

        List<FRExternalPermissionsCode> allowedPermissions = new ArrayList<>();
        for (FRExternalPermissionsCode permission : permissions) {
            if (accountRequest.getPermissions().contains(permission)) {
                allowedPermissions.add(permission);
            }
        }
        if (allowedPermissions.isEmpty()) {
            throw new PermissionDenyException("Not allowed to do the following actions: " + permissions);
        }
        return allowedPermissions;
    }

    public void verifyAccessTokenGrantTypes(List<OIDCConstants.GrantType> expectedGrantTypes, SignedJWT accessToken)
            throws OAuth2BearerTokenUsageInvalidTokenException {
        try{
            String grantTypeSerialised = getGrantTypes(accessToken);
            OIDCConstants.GrantType grantType = OIDCConstants.GrantType.fromType(grantTypeSerialised);
            if (!OIDCConstants.GrantType.REFRESH_TOKEN.equals(grantType) && !expectedGrantTypes.contains(grantType)){
                log.debug("Access token grant type '{}' is not one of the expected grant types {}", grantType,
                        expectedGrantTypes);
                throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_GRANT_TYPE,
                        grantType, expectedGrantTypes);
            }
            log.debug("verifyAccessTokenGrantTypes() - access token contains expected grant type");
        } catch (Exception e) {
            log.info("verifyAccessTokenGrantTypes() caught exception", e);
            throw new OAuth2BearerTokenUsageInvalidTokenException(e.getMessage());
        }
    }

    public void verifyAccessTokenScopes(List<String> expectedScopes, SignedJWT accessToken)
            throws OBErrorException{
        try{
            List<String> scopes = getScopes(accessToken);
            if(!scopes.containsAll(expectedScopes)){
                log.info("Access token did not contain expected scopes. Token scopes '{}' Expected scopes '{}'",
                        scopes, expectedScopes);
                throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID, "Did not contain the expected scopes");
            }
            log.debug("verifyAccessTokenScopes() accessToken contains expected scopes");
        } catch (Exception e) {
            throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID,  "Access token did not contain valid " +
                    "scopes");
        }
    }

    public SignedJWT validateAccessTokenWithAM(String authorizationHeaderValue)
            throws OAuth2BearerTokenUsageInvalidTokenException {
        try {
            SignedJWT parsedAccessToken = amResourceServerService.verifyAccessToken(authorizationHeaderValue);
            log.debug("validateAccessTokenWithAM() returning '{}'", parsedAccessToken);
            return parsedAccessToken;
        } catch (ParseException e) {
            log.info("validateAccessTokenWithAM() failed to parse access token '{}'", authorizationHeaderValue, e);
            throw new OAuth2BearerTokenUsageInvalidTokenException("Failed to parse access token");
        } catch (InvalidTokenException e) {
            log.info("validateAccessTokenWithAM() token does not contain the expected audience", e);
            throw new OAuth2BearerTokenUsageInvalidTokenException(e.getMessage());
        } catch (IOException e) {
            log.info("validateAccessTokenWithAM() Failed to validate access token.", e);
            throw new OAuth2BearerTokenUsageInvalidTokenException("Failed to validate access token");
        }
    }

    private List<String> getScopes(SignedJWT accessToken) throws OBErrorException, ParseException {
        JWTClaimsSet claims = getJwtClaims(accessToken);
        List<String> scopes = getStringListClaim(claims, OBConstants.OIDCClaim.SCOPE);
        return scopes;
    }

    private List<String> getStringListClaim(JWTClaimsSet claimSet, String claimName) throws ParseException,
            OBErrorException {
        List<String> claims = claimSet.getStringListClaim(claimName);
        if(claims == null){
            throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID,
                    "Does not contain claims of type " +  claimName);
        }
        return claims;
    }

    private @NotNull String getGrantTypes(SignedJWT accessToken) throws ParseException, OBErrorException {
        JWTClaimsSet jwtClaims = getJwtClaims(accessToken);
        String grantTypes = getStringClaim(jwtClaims, OBConstants.OIDCClaim.GRANT_TYPE);
        return grantTypes;
    }

    private String getStringClaim(JWTClaimsSet jwtClaims, String claimType) throws ParseException, OBErrorException {
        String claimValue = jwtClaims.getStringClaim(claimType);
        if(claimValue == null){
            log.error("We managed to get an access token that doesn't have any {} claims defined", claimType);
            throw new OBErrorException(SERVER_ERROR, "Access token does not contain claims of type " + claimType);
        }
        return claimValue;
    }

    private JWTClaimsSet getJwtClaims(SignedJWT accessToken) throws ParseException, OBErrorException {
        JWTClaimsSet jwtClaims = accessToken.getJWTClaimsSet();
        if(jwtClaims == null){
            log.info("getGrantTypes() Access token doesn't have any claims.");
            throw new OBErrorException(SERVER_ERROR, "Access token grant type is undefined"
            );
        }
        return jwtClaims;
    }


    public String getAccessTokenFromBearerAuthorizationValue(String authorizationHeaderValue) throws OBErrorException {
        log.debug("getAccessTokenFromBearerAuthorizationValue called with '{}'", authorizationHeaderValue);
        String accessToken = null;
        if(authorizationHeaderValue.startsWith("Bearer ")){
            accessToken = authorizationHeaderValue.replace("Bearer ", "");
            accessToken = accessToken.trim();
            log.debug("getAccessTokenFromBearerAuthorizationValue() returning accessToken '{}'", accessToken);
            return accessToken;
        } else {
            log.info("getAccessTokenFromBearerAuthorizationValue() authorization header value does not contain a " +
                    "Bearer token. Authorization: '{}'", authorizationHeaderValue);
            throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID, "Authorization is not of type Bearer");
        }
    }


    public void verifyMatlsFromAccessToken(SignedJWT accessToken, String tppName) throws OAuth2BearerTokenUsageInvalidTokenException {
        log.debug("verifyMatlsFromAccessToken() called");
        try {
            JWTClaimsSet jwtClaimSet = getJwtClaims(accessToken);
            String oauth2ClientId = getAudience(jwtClaimSet);
            Optional<Tpp> tpp = this.tppStoreService.findByClientId(oauth2ClientId);
            if(tpp.isPresent()){
                String authorisationNumberFromTppRecord = tpp.get().getAuthorisationNumber();
                if (!tppName.equals(authorisationNumberFromTppRecord)) {
                    log.warn("TPP ID from account token {} is not the one associated with the certificate {}",
                            authorisationNumberFromTppRecord, tppName);
                    throw new OBErrorException(OBRIErrorType.MATLS_TPP_AUTHENTICATION_INVALID_FROM_ACCESS_TOKEN,
                            tppName,
                            oauth2ClientId
                    );
                }
            }
        } catch (OAuth2BearerTokenUsageInvalidTokenException oe){
            log.info("verifyMatlsFromAccessToken() caught exception", oe);
            throw oe;
        } catch (Exception e) {
            log.info("verifyMatlsFromAccessToken() caught exception", e);
            throw new OAuth2BearerTokenUsageInvalidTokenException("Access token was not issued to the organisation " +
                    "that owns the TLS certificate used to make the request.");
        }
    }

    private String getAudience(JWTClaimsSet jwtClaimSet) throws OAuth2BearerTokenUsageInvalidTokenException {
        List<String> audience = jwtClaimSet.getAudience();
        if(audience != null && !audience.isEmpty()){
            return audience.get(0);
        } else {
            throw new OAuth2BearerTokenUsageInvalidTokenException("Access Token does not contain an audience");
        }
    }
}
