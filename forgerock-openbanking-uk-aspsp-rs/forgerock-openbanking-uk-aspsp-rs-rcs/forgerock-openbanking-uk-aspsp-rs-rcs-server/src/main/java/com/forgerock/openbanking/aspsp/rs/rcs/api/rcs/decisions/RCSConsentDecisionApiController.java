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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.am.services.UserProfileService;
import com.forgerock.openbanking.aspsp.rs.rcs.services.IntentTypeService;
import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.aspsp.rs.rcs.services.RcsService;
import com.forgerock.openbanking.common.conf.RcsConfiguration;
import com.forgerock.openbanking.common.constants.RCSConstants;
import com.forgerock.openbanking.common.error.exception.AccessTokenReWriteException;
import com.forgerock.openbanking.common.model.rcs.RedirectionAction;
import com.forgerock.openbanking.common.model.rcs.consentdecision.ConsentDecision;
import com.forgerock.openbanking.common.services.JwtOverridingService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.utils.JwsClaimsUtils;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.claim.Claims;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
public class RCSConsentDecisionApiController implements RCSConsentDecisionApi {

    private final CryptoApiClient cryptoApiClient;
    private final RcsService rcsService;
    private final RcsConfiguration rcsConfiguration;
    private final AMOpenBankingConfiguration amOpenBankingConfiguration;
    private final RCSErrorService rcsErrorService;
    private final ObjectMapper objectMapper;
    private final UserProfileService userProfileService;
    private final IntentTypeService intentTypeService;
    private final TppStoreService tppStoreService;
    private final JwtOverridingService jwtOverridingService;

    @Autowired
    public RCSConsentDecisionApiController(CryptoApiClient cryptoApiClient, RcsService rcsService,
                                           RcsConfiguration rcsConfiguration,
                                           AMOpenBankingConfiguration amOpenBankingConfiguration,
                                           RCSErrorService rcsErrorService, ObjectMapper objectMapper,
                                           UserProfileService userProfileService,
                                           IntentTypeService intentTypeService, TppStoreService tppStoreService,
                                           JwtOverridingService jwtOverridingService) {
        this.cryptoApiClient = cryptoApiClient;
        this.rcsService = rcsService;
        this.rcsConfiguration = rcsConfiguration;
        this.amOpenBankingConfiguration = amOpenBankingConfiguration;
        this.rcsErrorService = rcsErrorService;
        this.objectMapper = objectMapper;
        this.userProfileService = userProfileService;
        this.intentTypeService = intentTypeService;
        this.tppStoreService = tppStoreService;
        this.jwtOverridingService = jwtOverridingService;
    }

    /**
     *
     * @param consentDecisionSerialised
     * @param ssoToken
     * @return
     * @throws OBErrorException
     */
    @Override
    public ResponseEntity decisionAccountSharing(
            @RequestBody String consentDecisionSerialised,
            @CookieValue(value = "${am.cookie.name}") String ssoToken) throws OBErrorException {
        log.debug("decisionAccountSharing() consentDecisionSerialised is {}", consentDecisionSerialised);

        //Send a Consent response JWT to the initial request, which is define in the code
        if (consentDecisionSerialised == null || consentDecisionSerialised.isEmpty()) {
            log.debug("Consent decision is empty");
            return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_DECISION_EMPTY);
        }

        ConsentDecision consentDecision;
        try {
            consentDecision = objectMapper.readValue(consentDecisionSerialised, ConsentDecision.class);
        } catch (IOException e) {
            log.error("Remote consent decisions invalid", e);
            throw new OBErrorException(OBRIErrorType.RCS_CONSENT_DECISIONS_FORMAT, e.getMessage());
        }

        String consentRequestJwt = consentDecision.getConsentJwt();
        if (consentRequestJwt == null || consentRequestJwt.isEmpty() || consentRequestJwt.isBlank()) {
            log.error("Remote consent decisions invalid - consentRequestJwt is null ");
            throw new OBErrorException(OBRIErrorType.RCS_CONSENT_DECISIONS_FORMAT, "consentRequestJwt was null or " +
                    "empty");
        }

        try {
            try {
                log.debug("Received an accept consent request");

                //TODO check token but ignore if it's expired
                //cryptoApiClient.validateJws(consentDecision.getConsentJwt(),
                //        amOpenBankingConfiguration.getIssuerID(),  amOpenBankingConfiguration.jwksUri);
                SignedJWT consentContextJwt = (SignedJWT) JWTParser.parse(consentRequestJwt);
                boolean decision = RCSConstants.Decision.ALLOW.equals(consentDecision.getDecision());
                log.debug("The decision is '{}'", decision);

                //here is a good time to actually save that the consent has been approved by our resource owner
                Claims claims = JwsClaimsUtils.getClaims(consentContextJwt);
                String intentId = claims.getIdTokenClaims().get(OpenBankingConstants.IdTokenClaim.INTENT_ID)
                        .getValue();
                String csrf = consentContextJwt.getJWTClaimsSet().getStringClaim(RCSConstants.Claims.CSRF);
                String clientId = consentContextJwt.getJWTClaimsSet().getStringClaim(RCSConstants.Claims.CLIENT_ID);
                List<String> scopes = new ArrayList<>(
                        consentContextJwt.getJWTClaimsSet().getJSONObjectClaim(RCSConstants.Claims.SCOPES).keySet());
                String redirectUri = consentContextJwt.getJWTClaimsSet()
                        .getStringClaim(OIDCConstants.OIDCClaim.CONSENT_APPROVAL_REDIRECT_URI);


                ConsentDecisionDelegate consentDecisionDelegate = intentTypeService.getConsentDecision(intentId);
                if (consentDecisionDelegate == null) {
                    log.error("No Consent Decision Delegate available from the intent type Service.");
                    throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID,
                            "Invalid intent ID? '" + intentId + "'");
                }

                //Verify consent is own by the right TPP
                String tppIdBehindConsent = consentDecisionDelegate.getTppIdBehindConsent();
                Optional<Tpp> isTpp = tppStoreService.findById(tppIdBehindConsent);
                if (isTpp.isEmpty()) {
                    log.error("The TPP '{}' that created this intent id '{}' doesn't exist anymore.", tppIdBehindConsent, intentId);
                    return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                            tppIdBehindConsent, intentId, clientId);
                }

                if (!clientId.equals(isTpp.get().getClientId())) {
                    log.error("The TPP '{}' created the account request '{}' but it's TPP '{}' that is trying to get" +
                            " consent for it.", tppIdBehindConsent, intentId, clientId);
                    throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID_CONSENT,
                            tppIdBehindConsent, intentId, clientId);
                }

                //Verify consent decision is send by the same user
                Map<String, String> profile = userProfileService.getProfile(ssoToken, amOpenBankingConfiguration.endpointUserProfile,
                        amOpenBankingConfiguration.cookieName);
                String username = profile.get(amOpenBankingConfiguration.userProfileId);
                String userIdBehindConsent = consentDecisionDelegate.getUserIDBehindConsent();

                if (!username.equals(userIdBehindConsent)) {
                    log.error("The consent was associated with user '{}' but now, its user '{}' that " +
                            "send the consent decision.", userIdBehindConsent, username);
                    throw new OBErrorException(OBRIErrorType.RCS_CONSENT_DECISION_INVALID_USER,
                            userIdBehindConsent, username);
                }

                //Call the right decision delegate, cased on the intent type
                consentDecisionDelegate.consentDecision(consentDecisionSerialised, decision);

                log.debug("Redirect the resource owner to the original oauth2/openid request but this time, with the " +
                        "consent response jwt '{}'.", consentContextJwt.toString());
                String consentJwt = rcsService.generateRCSConsentResponse(rcsConfiguration,
                        amOpenBankingConfiguration, csrf, decision, scopes, clientId);

                ResponseEntity responseEntity = rcsService.sendRCSResponseToAM(ssoToken,
                        RedirectionAction.builder()
                                .redirectUri(redirectUri)
                                .consentJwt(consentJwt)
                                .requestMethod(HttpMethod.POST)
                                .build());
                log.debug("Response received from AM: {}", responseEntity);

                if (responseEntity.getStatusCode() != HttpStatus.FOUND) {
                    log.error("When sending the consent response {} to AM, it failed to returned a 302. response '{}' ",
                            consentJwt, responseEntity);
                    throw new OBErrorException(OBRIErrorType.RCS_CONSENT_RESPONSE_FAILURE);
                } else if (locationContainsError(responseEntity.getHeaders().getLocation())) {
                    log.error("When sending the consent response {} to AM, it failed. response '{}' ",
                            consentJwt, responseEntity);
                    return rcsErrorService.invalidConsentError(responseEntity.getHeaders().getLocation());
                }

                ResponseEntity rewrittenResponseEntity = null;
                try {
                    rewrittenResponseEntity =
                            jwtOverridingService.rewriteIdTokenFragmentInLocationHeader(responseEntity);
                } catch (AccessTokenReWriteException e) {
                    log.info("decisionAccountSharing() Failed to re-write id_token", e);
                    throw new OBErrorException(OBRIErrorType.RCS_CONSENT_RESPONSE_FAILURE);
                }

                String location = rewrittenResponseEntity.getHeaders().getFirst("Location");
                log.debug("The redirection to the consent page should be in the location '{}'", location);

                return ResponseEntity.ok(RedirectionAction.builder()
                        .redirectUri(location)
                        .build());
            } catch (JOSEException e) {
                log.error("Could not generate consent context JWT", e);
                throw new OBErrorException(OBRIErrorType.RCS_CONSENT_RESPONSE_FAILURE);
            } catch (ParseException e) {
                log.error("Could not parse the JWT", e);
                throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_FORMAT);
            } /* catch (InvalidTokenException e) {
            log.error("Remote consent request invalid", e);
            throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID, e.getMessage());
            } */ catch (IOException e) {
                log.error("Remote consent decisions invalid", e);
                throw new OBErrorException(OBRIErrorType.RCS_CONSENT_DECISIONS_FORMAT, e.getMessage());
            }
        } catch (OBErrorException e) {
            return rcsErrorService.invalidConsentError(consentRequestJwt, e);
        }
    }

    private boolean locationContainsError(URI location) {
        if (location != null) {
            if (location.getQuery() != null) {
                return location.getQuery().contains("error");
            } else if (location.getFragment() != null) {
                return location.getFragment().contains("error");
            }
        }
        return false;
    }

    @AfterReturning
    public void test(){
        this.rcsService.toString();
    }
}
