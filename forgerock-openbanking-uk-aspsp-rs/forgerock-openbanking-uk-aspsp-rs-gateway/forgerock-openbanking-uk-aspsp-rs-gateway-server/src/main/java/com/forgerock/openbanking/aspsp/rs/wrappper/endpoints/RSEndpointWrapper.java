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
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.openbanking.IdempotencyService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.utils.ApiVersionUtils;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import uk.org.openbanking.OBConstants;
import uk.org.openbanking.OBHeaders;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static com.forgerock.openbanking.model.error.OBRIErrorType.SERVER_ERROR;

@Slf4j
public abstract class RSEndpointWrapper<T extends RSEndpointWrapper<T, R>, R> {

    protected RSEndpointWrapperService rsEndpointWrapperService;
    protected String authorization;
    protected String xFapiFinancialId;
    protected Principal principal;
    protected SignedJWT accessToken;
    protected String oAuth2ClientId;
    protected AdditionalFilter additionalFilter;
    protected OBVersion obVersion;
    protected TppStoreService tppStoreService;

    public RSEndpointWrapper(RSEndpointWrapperService rsEndpointWrapperService, TppStoreService tppStoreService) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.tppStoreService = tppStoreService;
    }

    public T authorization(String authorization) {
        this.authorization = authorization;
        return (T) this;
    }

    public T xFapiFinancialId(String xFapiFinancialId) {
        this.xFapiFinancialId = xFapiFinancialId;
        return (T) this;
    }

    public T principal(Principal principal) {
        this.principal = principal;
        return (T) this;
    }

    public T obVersion(OBVersion obVersion) {
        this.obVersion = obVersion;
        return (T) this;
    }

    public T filters(AdditionalFilter<T> additionalFilter) {
        this.additionalFilter = additionalFilter;
        return (T) this;
    }


    public ResponseEntity execute(R main) throws OBErrorResponseException {
        log.info("execute method");
        try {
            log.debug("execute() Apply filters");
            applyFilters();
            if (additionalFilter != null) {
                additionalFilter.filter(this);
            }
            log.debug("execute() Filters applied");

            log.info("execute() Call main lambda");
            ResponseEntity response = run(main);
            String tan = rsEndpointWrapperService.getTan();
            // TODO: Does this mean we create jwsSignatures for all responses to RS calls? We don't need to sign
            //  requests to accounts endpoints, although I guess it doesn't hurt if we do. Just wasteful in terms of
            //  processing time etc.
            String jwsSignature = rsEndpointWrapperService.generateDetachedJws(response, obVersion, tan,
                    xFapiFinancialId);

            return ResponseEntity
                    .status(response.getStatusCode())
                    .header("x-jws-signature", jwsSignature)
                    .body(response.getBody());
        } catch (OBErrorException e) {
            log.warn("Verification failed", e);
            throw new OBErrorResponseException(
                    e.getObriErrorType().getHttpStatus(),
                    OBRIErrorResponseCategory.REQUEST_FILTER,
                    e.getOBError());

        } catch (JsonProcessingException e) {
            log.warn("Failed to process JSON response", e);
            throw new OBErrorResponseException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    OBRIErrorResponseCategory.REQUEST_FILTER,
                    SERVER_ERROR.toOBError1());
        }
    }

    protected void applyFilters() throws OBErrorException {
        verifyFinancialId();
    }

    protected abstract ResponseEntity run(R main) throws OBErrorException, JsonProcessingException;

    public void verifyAccessToken(List<String> expectedScopes, List<OIDCConstants.GrantType> expectedGrantTypes) throws OBErrorException {
        try {
            //Verify access token
            log.info("Verify the access token {}", authorization);
            accessToken = rsEndpointWrapperService.verifyAccessToken(authorization);

            String grantTypeSerialised = accessToken.getJWTClaimsSet().getStringClaim(OBConstants.OIDCClaim.GRANT_TYPE);
            if (grantTypeSerialised == null) {
                log.error("We managed to get an access token that doesn't have a grant type claim defined: {}", authorization);
                throw new OBErrorException(SERVER_ERROR,
                        "Access token grant type is undefined"
                );
            }
            OIDCConstants.GrantType grantType = OIDCConstants.GrantType.fromType(grantTypeSerialised);

            if (!OIDCConstants.GrantType.REFRESH_TOKEN.equals(grantType) && !expectedGrantTypes.contains(grantType)) {
                log.debug("The access token grant type {} doesn't match one of the expected grant types {}", grantType, expectedGrantTypes);
                throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_GRANT_TYPE,
                        grantType, expectedGrantTypes
                );
            }

            List<String> scopes = (List<String>) accessToken.getJWTClaimsSet().getClaim(OBConstants.OIDCClaim.SCOPE);

            if (!scopes.containsAll(expectedScopes)) {
                log.warn("The access token {} doesn't contain the scope {}", authorization, expectedScopes);
                throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_SCOPE,
                        expectedScopes
                );
            }
        } catch (ParseException e) {
            log.warn("Couldn't parse the the access token {}. It's probably not stateless and therefore, not " +
                    "an access token generated by our ASPSP-AS", authorization);
            throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_FORMAT);
        } catch (InvalidTokenException e) {
            log.warn("Invalid access token {}", authorization);
            throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID,
                    e.getMessage()
            );
        } catch (IOException e) {
            log.error("IO exception", e);
            throw new OBErrorException(SERVER_ERROR,
                    e.getMessage()
            );
        }
    }

    public void verifyFinancialId() throws OBErrorException {
        if (!rsEndpointWrapperService.verifyFinancialIdHeader(xFapiFinancialId)) {
            log.warn("Financial ID received {} is not the one expected {}", xFapiFinancialId, rsEndpointWrapperService.rsConfiguration.financialId);
            throw new OBErrorException(OBRIErrorType.FINANCIAL_ID_INVALID,
                    rsEndpointWrapperService.rsConfiguration.financialId,
                    xFapiFinancialId
            );
        }
        log.debug("xFapiFinancialId '{}' is valid", xFapiFinancialId);
    }

    public void verifyJwsDetachedSignature(String jwsDetachedSignature, HttpServletRequest request) throws OBErrorException {
        if (rsEndpointWrapperService.isDetachedSignatureEnable) {
            rsEndpointWrapperService.detachedJwsVerifier.verifyDetachedJws(jwsDetachedSignature, obVersion, request,
                    this.oAuth2ClientId);
        }
    }

    /**
     * Validate the two parameter passed contains a valid {@link OBVersion} and if they match
     * @param version
     * @param versionToCompare
     * @throws OBErrorException
     */
    public void verifyMatcherVersion(String version, String versionToCompare) throws OBErrorException {
        if (!ApiVersionUtils.getOBVersion(version).equals(ApiVersionUtils.getOBVersion(versionToCompare))) {
            log.warn("Version on the callback url '{} doesn't match with the version value field '{}'", version, versionToCompare);
            StringBuilder message = new StringBuilder()
                    .append("Version on the callback url field ").append(version)
                    .append(" doesn't match with the version value field ").append(versionToCompare);
            throw new OBErrorException(OBRIErrorType.REQUEST_OBJECT_INVALID, message.toString());
        }
        log.debug("The version url field value '{}' and version field value '{}' match and is valid", version, versionToCompare);
    }

    public void verifyIdempotencyKeyLength(String xIdempotencyKey) throws OBErrorException {
        if (!IdempotencyService.isIdempotencyKeyHeaderValid(xIdempotencyKey)) {
            log.warn("Header value for {} must be between 1 and 40 characters. Provided header {} : {}'", OBHeaders.X_IDEMPOTENCY_KEY, OBHeaders.X_IDEMPOTENCY_KEY, xIdempotencyKey);
            throw new OBErrorException(OBRIErrorType.IDEMPOTENCY_KEY_INVALID,
                    xIdempotencyKey,
                    (xIdempotencyKey == null) ? 0 : xIdempotencyKey.length()
            );
        }
        log.debug("xIdempotency key '{}' is valid length", xIdempotencyKey);
    }

    // This method ensures that the certificate used for MATLS to access the endpoint belongs to the same
    // organisation that the access token provided in the request authorization header was issued to.
    public void verifyMatlsFromAccessToken() throws OBErrorException {
        try {
            log.debug("verifyMatlsFromAccessToken() called");
            String oauth2ClientId = accessToken.getJWTClaimsSet().getAudience().get(0);
            //MTLS check. We verify that the certificate is associated with the expected AISP ID
            Optional<Tpp> tpp = this.tppStoreService.findByClientId(oauth2ClientId);
            UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            if(tpp.isPresent()){
                String authorisationNumberFromTppRecord = tpp.get().getAuthorisationNumber();
                if (!currentUser.getUsername().equals(authorisationNumberFromTppRecord)) {
                    log.warn("TPP ID from account token {} is not the one associated with the certificate {}",
                            oauth2ClientId, currentUser.getUsername());
                    throw new OBErrorException(OBRIErrorType.MATLS_TPP_AUTHENTICATION_INVALID_FROM_ACCESS_TOKEN,
                            currentUser.getUsername(),
                            oauth2ClientId
                    );
                }
            }
            this.oAuth2ClientId = oauth2ClientId;
            log.info("TPP AuthorizationNumber {} has been verified against X509 certificate (MTLS)",
                    currentUser.getUsername());
        } catch (ParseException e) {
            log.warn("Access token {} doesn't look to be a JWT. You need to enable stateless", authorization);
            throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_FORMAT);
        }
    }

    protected void filters() throws OBErrorException {
        verifyFinancialId();
    }

    public interface AdditionalFilter<T> {
        void filter(T wrapper) throws OBErrorException;
    }
}
