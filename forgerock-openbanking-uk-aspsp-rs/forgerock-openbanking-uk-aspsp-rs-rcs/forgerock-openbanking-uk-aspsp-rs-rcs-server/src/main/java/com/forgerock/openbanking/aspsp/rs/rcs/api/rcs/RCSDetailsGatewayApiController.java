/**
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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs;

import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.am.services.UserProfileService;
import com.forgerock.openbanking.analytics.model.entries.PsuCounterEntry;
import com.forgerock.openbanking.analytics.services.PsuCounterEntryKPIService;
import com.forgerock.openbanking.aspsp.rs.rcs.services.IntentTypeService;
import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.common.constants.RCSConstants;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.data.UserDataService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.claim.Claims;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class RCSDetailsGatewayApiController implements RCSDetailsGatewayApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(RCSDetailsGatewayApiController.class);

    @Autowired
    private AccountStoreService accountsService;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private AMOpenBankingConfiguration amOpenBankingConfiguration;
    @Autowired
    private RCSErrorService rcsErrorService;
    @Autowired
    private IntentTypeService intentTypeService;
    @Autowired
    private PsuCounterEntryKPIService psuCounterEntryKPIService;

    @Override
    public ResponseEntity details(@RequestBody String consentRequestJwt,
                                  @CookieValue(value = "${am.cookie.name}") String ssoToken) throws OBErrorException {
        LOGGER.debug("Received a consent request with consent_request='{}'", consentRequestJwt);
        try {
            //Verify the RCS JWT
            LOGGER.debug("Validate consent request JWS");
            //TODO disabling this for now as the consent request JWT as a very short period of life
            //cryptoApiClient.validateJws(consentRequestJwt, amOpenBankingConfiguration.getIssuerID(), amOpenBankingConfiguration.jwksUri);

            LOGGER.debug("Parse consent request JWS");
            SignedJWT signedJWT = (SignedJWT) JWTParser.parse(consentRequestJwt);

            LOGGER.debug("Read payment ID from the claims");
            //Read the claims
            Claims claims = Claims.parseClaims(signedJWT.getJWTClaimsSet().getJSONObjectClaim(OIDCConstants
                    .OIDCClaim.CLAIMS));
            if (!claims.getIdTokenClaims().containsKey(OpenBankingConstants.IdTokenClaim.INTENT_ID)) {
                throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID, "No intent ID");
            }
            String intentId = claims.getIdTokenClaims().get(OpenBankingConstants.IdTokenClaim.INTENT_ID)
                    .getValue();
            String clientId = signedJWT.getJWTClaimsSet().getStringClaim(RCSConstants.Claims.CLIENT_ID);

            Map<String, String> profile = userProfileService.getProfile(ssoToken, amOpenBankingConfiguration.endpointUserProfile,
                    amOpenBankingConfiguration.cookieName);
            String username = profile.get(amOpenBankingConfiguration.userProfileId);
            List<FRAccountWithBalance> accounts = getAccountOrGenerateData(username);

            LOGGER.debug("intent Id from the requested claims '{}'", intentId);
            return intentTypeService.consentDetails(intentId, consentRequestJwt,accounts, username, clientId);
        } catch (ParseException e) {
            LOGGER.error("Could not parse the JWT", e);
            throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_FORMAT);
        } catch (OBErrorException e) {
            return rcsErrorService.invalidConsentError(consentRequestJwt, e);
        }
    }

    private List<FRAccountWithBalance> getAccountOrGenerateData(String username) {
        List<FRAccountWithBalance> accounts;
        try {
            accounts = accountsService.getAccountWithBalances(username);

            if (accounts.isEmpty()) {
                psuCounterEntryKPIService.pushPsuCounterEntry(PsuCounterEntry.builder()
                        .count(1l)
                        .day(DateTime.now())
                        .build());
                userDataService.generateUserData(username);
                accounts = accountsService.getAccountWithBalances(username);
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == NOT_FOUND) {
                psuCounterEntryKPIService.pushPsuCounterEntry(PsuCounterEntry.builder()
                        .count(1l)
                        .day(DateTime.now())
                        .build());
                userDataService.generateUserData(username);
                accounts = accountsService.getAccountWithBalances(username);
            } else {
                throw e;
            }
        }
        return accounts;
    }
}
