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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.autoaccept;

import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.am.services.UserProfileService;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.aspsp.rs.rcs.services.IntentTypeService;
import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.aspsp.rs.rcs.services.RcsService;
import com.forgerock.openbanking.common.conf.RcsConfiguration;
import com.forgerock.openbanking.common.constants.RCSConstants;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.rcs.RedirectionAction;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.data.UserDataService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.claim.Claims;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@Slf4j
public class AutodecisionsApiController implements AutodecisionsApi {

    @Autowired
    private RcsService rcsService;
    @Autowired
    private RcsConfiguration rcsConfiguration;
    @Autowired
    private AMOpenBankingConfiguration amOpenBankingConfiguration;
    @Autowired
    private RCSErrorService rcsErrorService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private IntentTypeService intentTypeService;
    @Autowired
    private AccountStoreService accountsService;
    @Autowired
    private UserDataService userDataService;

    @Override
    public ResponseEntity<RedirectionAction> autoAccept(
            @RequestBody String consentRequestJwt,
            @CookieValue(value = "${am.cookie.name}") String ssoToken)
            throws OBErrorException {
        try {
            log.debug("Parse consent request JWS");
            SignedJWT signedJWT = (SignedJWT) JWTParser.parse(consentRequestJwt);

            log.debug("Read payment ID from the claims");
            //Read the claims
            Claims claims = Claims.parseClaims(signedJWT.getJWTClaimsSet().getJSONObjectClaim(OIDCConstants
                    .OIDCClaim.CLAIMS));
            if (!claims.getIdTokenClaims().containsKey(OpenBankingConstants.IdTokenClaim.INTENT_ID)) {
                return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID, "No intent ID");
            }
            String intentId = claims.getIdTokenClaims().get(OpenBankingConstants.IdTokenClaim.INTENT_ID)
                    .getValue();
            String clientId = signedJWT.getJWTClaimsSet().getStringClaim(RCSConstants.Claims.CLIENT_ID);
            String redirectUri = signedJWT.getJWTClaimsSet()
                    .getStringClaim(OIDCConstants.OIDCClaim.CONSENT_APPROVAL_REDIRECT_URI);
            String csrf = signedJWT.getJWTClaimsSet().getStringClaim(RCSConstants.Claims.CSRF);
            List<String> scopes = new ArrayList<>(
                    signedJWT.getJWTClaimsSet().getJSONObjectClaim(RCSConstants.Claims.SCOPES).keySet());

            Map<String, String> profile = userProfileService.getProfile(ssoToken, amOpenBankingConfiguration.endpointUserProfile,
                    amOpenBankingConfiguration.cookieName);
            String username = profile.get(amOpenBankingConfiguration.userProfileId);
            List<FRAccount2> accounts = getAccountOrGenerateData(username);
            //Call the right decision controller, cased on the intent type
            ConsentDecisionDelegate consentDecisionController = intentTypeService.getConsentDecision(intentId);
            consentDecisionController.autoaccept(accounts, username);

            log.debug("Redirect the resource owner to the original oauth2/openid request but this time, with the " +
                    "consent response jwt '{}'.", consentRequestJwt);
            String consentJwt = rcsService.generateRCSConsentResponse(rcsConfiguration,
                    amOpenBankingConfiguration, csrf, true, scopes, clientId);

            ResponseEntity responseEntity = rcsService.sendRCSResponseToAM(ssoToken, RedirectionAction.builder()
                    .redirectUri(redirectUri)
                    .consentJwt(consentJwt)
                    .requestMethod(HttpMethod.POST)
                    .build());
            log.debug("Response received from AM: {}", responseEntity);

            if (responseEntity.getStatusCode() != HttpStatus.FOUND) {
                log.error("When sending the consent response {} to AM, it failed to returned a 302", consentJwt, responseEntity);
                throw new OBErrorException(OBRIErrorType.RCS_CONSENT_RESPONSE_FAILURE);
            }

            String location = responseEntity.getHeaders().getFirst(HttpHeaders.LOCATION);
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
        } catch (Exception e) {
            log.error("Unexpected error while authorising consent", e);
            throw new OBErrorException(OBRIErrorType.RCS_CONSENT_RESPONSE_FAILURE);
        }
    }

    private List<FRAccount2> getAccountOrGenerateData(String username) {
        List<FRAccount2> accounts;
        try {
            accounts = accountsService.get(username);
            if (accounts.isEmpty()) {
                userDataService.generateUserData(username);
                accounts = accountsService.get(username);
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == NOT_FOUND) {
                userDataService.generateUserData(username);
                accounts = accountsService.get(username);
            } else {
                throw e;
            }
        }
        return accounts;
    }
}
