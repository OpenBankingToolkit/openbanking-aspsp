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
import com.forgerock.openbanking.aspsp.rs.filter.MultiReadHttpServletRequest;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.services.openbanking.IdempotencyService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.model.CreateDetachedJwtResponse;
import com.forgerock.openbanking.jwt.model.SigningRequest;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
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
import java.util.stream.Collectors;

import static com.forgerock.openbanking.model.error.OBRIErrorType.SERVER_ERROR;


@Slf4j
public abstract class RSEndpointWrapper<T extends RSEndpointWrapper<T, R>, R> {
    protected RSEndpointWrapperService rsEndpointWrapperService;
    protected String authorization;
    protected String xFapiFinancialId;
    protected Principal principal;
    protected SignedJWT accessToken;
    protected String tppId;
    protected AdditionalFilter additionalFilter;


    public RSEndpointWrapper(RSEndpointWrapperService rsEndpointWrapperService) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
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

    public T filters(AdditionalFilter<T> additionalFilter) {
        this.additionalFilter = additionalFilter;
        return (T) this;
    }

    public ResponseEntity execute(R main) throws OBErrorResponseException {
        log.info("execute method");
        try {
            log.debug("Apply filters");
            applyFilters();
            if (additionalFilter != null) {
                additionalFilter.filter(this);
            }
            log.debug("Filters applied");

            log.info("Call main lambda");
            ResponseEntity response = run(main);
            String jwsSignature = "";

            SigningRequest signingRequest = SigningRequest.builder()
                    .customHeaderClaims(
                            SigningRequest.CustomHeaderClaims.builder()
                                    .includeB64(true)
                                    .includeOBIss(true)
                                    .includeOBIat(true)
                                    .includeCrit(true)
                                    .tan(rsEndpointWrapperService.getTan())
                                    .build())
                    .build();
            if (response.getBody() instanceof Resource) {
                log.debug("JWT signing does not currently work for a file response which it just a file byte stream (currently XML or JSON)");
                jwsSignature = null;
            } else {
                CreateDetachedJwtResponse createDetachedJwtResponse = rsEndpointWrapperService.getCryptoApiClient().signPayloadToDetachedJwt(signingRequest,
                        rsEndpointWrapperService.getFinancialId(),
                        rsEndpointWrapperService.mapper.writeValueAsString(response.getBody()));
                if (createDetachedJwtResponse != null) {
                    jwsSignature = createDetachedJwtResponse.getDetachedSignature();
                }
            }
            log.debug("Signed response claims. Signature: {}", jwsSignature);

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
            accessToken = rsEndpointWrapperService.amResourceServerService.verifyAccessToken(authorization);

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
                log.warn("The access token {} doesn't contain the scope {}", authorization, OpenBankingConstants.Scope.ACCOUNTS);
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
        if (!rsEndpointWrapperService.obHeaderCheckerService.verifyFinancialIdHeader(xFapiFinancialId)) {
            log.warn("Financial ID received {} is not the one expected {}", xFapiFinancialId,
                    rsEndpointWrapperService.rsConfiguration.financialId);
            throw new OBErrorException(OBRIErrorType.FINANCIAL_ID_INVALID,
                    rsEndpointWrapperService.rsConfiguration.financialId,
                    xFapiFinancialId
            );
        }
        log.debug("xFapiFinancialId '{}' is valid", xFapiFinancialId);
    }

    public void verifyJwsDetachedSignature(String jwsDetachedSignature, HttpServletRequest request) throws OBErrorException {

        if (rsEndpointWrapperService.isDetachedSignatureEnable && jwsDetachedSignature != null) {
            try {
                MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest(request);
                String body = multiReadRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                log.info("Verify detached signature {} with payload {}", jwsDetachedSignature, body);
                UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
                Tpp tpp = rsEndpointWrapperService.tppStoreService.findByClientId(currentUser.getUsername()).get();
                if (tpp.getRegistrationResponse().getJwks() != null) {
                    rsEndpointWrapperService.cryptoApiClient.validateDetachedJWSWithJWK(jwsDetachedSignature, body, null,
                            tpp.getClientId(), tpp.getRegistrationResponse().getJwks().getKeys().get(0));
                } else {
                    rsEndpointWrapperService.cryptoApiClient.validateDetachedJWS(jwsDetachedSignature, body, null,
                            tpp.getClientId(), tpp.getRegistrationResponse().getJwks_uri());
                }
            } catch (InvalidTokenException e) {
                log.warn("Invalid detached signature {}", jwsDetachedSignature, e);
                throw new OBErrorException(OBRIErrorType.DETACHED_JWS_INVALID, jwsDetachedSignature, e.getMessage());
            } catch (IOException e) {
                log.error("Can't get the request body", e);
                throw new OBErrorException(OBRIErrorType.DETACHED_JWS_UN_ACCESSIBLE);
            } catch (ParseException e) {
                log.error("Can't parse JWS", e);
                throw new OBErrorException(SERVER_ERROR);
            }
        }
    }

    public void verifyIdempotencyKeyLength(String xIdempotencyKey) throws OBErrorException {
        if (!IdempotencyService.isIdempotencyKeyHeaderValid(xIdempotencyKey)) {
            log.warn("Header value for {} must be between 1 and 40 characters. Provided header {} : {}'", OBHeaders.X_IDEMPOTENCY_KEY, OBHeaders.X_IDEMPOTENCY_KEY, xIdempotencyKey);
            throw new OBErrorException(OBRIErrorType.IDEMPOTENCY_KEY_INVALID,
                    xIdempotencyKey,
                    (xIdempotencyKey==null)?0:xIdempotencyKey.length()
            );
        }
        log.debug("xIdempotency key '{}' is valid length", xIdempotencyKey);
    }

    public void verifyMatlsFromAccessToken() throws OBErrorException {
        try {
            tppId = accessToken.getJWTClaimsSet().getAudience().get(0);
            //MTLS check. We verify that the certificate is associated with the expected AISP ID
            UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            if (!currentUser.getUsername().equals(tppId)) {
                log.warn("TPP ID from account token {} is not the one associated with the certificate {}",
                        tppId, currentUser.getUsername());
                throw new OBErrorException(OBRIErrorType.MATLS_TPP_AUTHENTICATION_INVALID_FROM_ACCESS_TOKEN,
                        currentUser.getUsername(),
                        tppId
                );
            }
            log.info("AISP ID {} has been verified against X509 certificate (MTLS)", currentUser.getUsername());
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
