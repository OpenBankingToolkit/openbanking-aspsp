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
package com.forgerock.openbanking.rs.ui.api.data;

import com.forgerock.openbanking.analytics.model.entries.PsuCounterEntry;
import com.forgerock.openbanking.analytics.services.PsuCounterEntryKPIService;
import com.forgerock.openbanking.common.conf.data.DataConfigurationProperties;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2BearerTokenUsageInvalidTokenException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.openbanking.common.model.data.FRUserData;
import com.forgerock.openbanking.common.services.security.Psd2WithSessionApiHelperService;
import com.forgerock.openbanking.common.services.store.data.UserDataService;
import com.forgerock.openbanking.common.services.token.AccessTokenService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jwt.SignedJWT;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class DataApiController implements DataApi {

    private UserDataService userDataService;
    private DataConfigurationProperties dataConfig;
    private PsuCounterEntryKPIService psuCounterEntryKPIService;
    private final CryptoApiClient cryptoApiClient;
    private final Psd2WithSessionApiHelperService psd2WithSessionApiHelperService;
    private final AccessTokenService accessTokenService;


    @Autowired
    public DataApiController(UserDataService userDataService, DataConfigurationProperties dataConfig,
                             PsuCounterEntryKPIService psuCounterEntryKPIService, CryptoApiClient cryptoApiClient,
                             Psd2WithSessionApiHelperService psd2WithSessionApiHelperService,
                             AccessTokenService accessTokenService) {
        this.userDataService = userDataService;
        this.dataConfig = dataConfig;
        this.psuCounterEntryKPIService = psuCounterEntryKPIService;
        this.cryptoApiClient = cryptoApiClient;
        this.psd2WithSessionApiHelperService = psd2WithSessionApiHelperService;
        this.accessTokenService = accessTokenService;
    }


    @Override
    public ResponseEntity hasData(
            @ApiParam(value = "PSU User session")
            @CookieValue(value = "obri-session", required = true) String obriSession,

            @ApiParam(value = "The access token")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) String authorization,

            Principal principal
    ) throws OAuth2InvalidClientException, OBErrorException, OAuth2BearerTokenUsageInvalidTokenException {
        log.debug("hasData() called");
        String tppName = psd2WithSessionApiHelperService.getTppName(principal);
        String psuName = psd2WithSessionApiHelperService.getPsuNameFromSession(obriSession);
        verifyAccessTokenAndVerifyTppIdentity(authorization, tppName);
        log.info("hasData() called with session for psu '{}' by tpp '{}'", psuName, tppName);
        return ResponseEntity.ok(userDataService.hasData(psuName));
    }

    @Override
    public ResponseEntity exportUserData(
            @ApiParam(value = "PSU User session")
            @CookieValue(value = "obri-session", required = true) String obriSession,

            @ApiParam(value = "The access token")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) String authorization,

            Principal principal
    ) throws OAuth2InvalidClientException, OBErrorException, OAuth2BearerTokenUsageInvalidTokenException {
        log.debug("exportUserData() called");

        String tppName = psd2WithSessionApiHelperService.getTppName(principal);
        String psuName = psd2WithSessionApiHelperService.getPsuNameFromSession(obriSession);
        verifyAccessTokenAndVerifyTppIdentity(authorization, tppName);
        log.info("exportUserData() called with session for psu '{}' by tpp '{}'", psuName, tppName);
        return ResponseEntity.ok(userDataService.exportUserData(psuName));
    }

    @Override
    public ResponseEntity updateUserData(
            @ApiParam(value = "PSU User session")
            @CookieValue(value = "obri-session", required = true) String obriSession,

            @ApiParam(value = "The access token")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) String authorization,

            @ApiParam(value = "User financial data", required = true)
            @RequestBody FRUserData userData,

            Principal principal
    ) throws OBErrorException, OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException {
       try {
           log.debug("updateUserData() called");
           String tppName = psd2WithSessionApiHelperService.getTppName(principal);
           String psuName = psd2WithSessionApiHelperService.getPsuNameFromSession(obriSession);
           verifyAccessTokenAndVerifyTppIdentity(authorization, tppName);
           log.info("updateUserData() called with session for psu '{}' by tpp '{}'", psuName, tppName);

           userData.setUserName(psuName);
           if (!userDataService.hasData(psuName)) {
               psuCounterEntryKPIService.pushPsuCounterEntry(PsuCounterEntry.builder()
                       .count(1l)
                       .day(DateTime.now())
                       .build());
           }

           return ResponseEntity.ok(userDataService.updateUserData(userData));
       } catch (HttpClientErrorException e) {

           if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
               log.debug("TPP bad request: {}", e.getResponseBodyAsString(), e);
               throw new OBErrorException(OBRIErrorType.DATA_INVALID_REQUEST,
                       e.getResponseBodyAsString()
               );
           } else {
               log.error("Internal server: {}", e.getResponseBodyAsString(), e);
               throw new OBErrorException(OBRIErrorType.SERVER_ERROR);
           }
       }
    }

    @Override
    public ResponseEntity createUserData(
            @ApiParam(value = "PSU User session")
            @CookieValue(value = "obri-session", required = true) String obriSession,

            @ApiParam(value = "The access token")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) String authorization,

            @ApiParam(value = "User financial data", required = true)
            @RequestBody FRUserData userData,

            Principal principal
    ) throws OBErrorException, OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException {
        try {
            log.debug("createUserData() called");
            String tppName = psd2WithSessionApiHelperService.getTppName(principal);
            String psuName = psd2WithSessionApiHelperService.getPsuNameFromSession(obriSession);
            verifyAccessTokenAndVerifyTppIdentity(authorization, tppName);
            log.info("createUserData() called with session for psu '{}' by tpp '{}'", psuName, tppName);
            userData.setUserName(psuName);
            if (!userDataService.hasData(psuName)) {
                psuCounterEntryKPIService.pushPsuCounterEntry(PsuCounterEntry.builder()
                        .count(1l)
                        .day(DateTime.now())
                        .build());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(userDataService.createUserData(userData));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.debug("TPP bad request: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.DATA_INVALID_REQUEST,
                        e.getResponseBodyAsString()
                );
            } else {
                log.error("Internal server: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.SERVER_ERROR);
            }
        }
    }

    @Override
    public ResponseEntity deleteUserData(
            @ApiParam(value = "PSU User session")
            @CookieValue(value = "obri-session", required = true) String obriSession,

            @ApiParam(value = "The access token")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) String authorization,

            Principal principal
    ) throws OBErrorException, OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException {
        try {
            log.debug("deleteUserData() called");
            String tppName = psd2WithSessionApiHelperService.getTppName(principal);
            String psuName = psd2WithSessionApiHelperService.getPsuNameFromSession(obriSession);
            verifyAccessTokenAndVerifyTppIdentity(authorization, tppName);
            log.info("deleteUserData() called with session for psu '{}' by tpp '{}'", psuName, tppName);

            userDataService.deleteUserData(psuName);
            return ResponseEntity.ok(userDataService.exportUserData(psuName));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.debug("TPP bad request: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.DATA_INVALID_REQUEST,
                        e.getResponseBodyAsString()
                );
            } else {
                log.error("Internal server: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.SERVER_ERROR);
            }
        }
    }

    @Override
    public ResponseEntity generateData(
            @ApiParam(value = "PSU User session")
            @CookieValue(value = "obri-session", required = true) String obriSession,

            @ApiParam(value = "The access token")
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) String authorization,

            @ApiParam(value = "Data profile", required = false)
            @RequestParam(name = "profile", required = false) String profile,

            Principal principal
    ) throws OBErrorException, OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException {
        try {
            log.debug("generateData() called");
            String tppName = psd2WithSessionApiHelperService.getTppName(principal);
            String psuName = psd2WithSessionApiHelperService.getPsuNameFromSession(obriSession);
            verifyAccessTokenAndVerifyTppIdentity(authorization, tppName);
            log.info("generateUserData() called with session for psu '{}' by tpp '{}'", psuName, tppName);

            final String defaultProfile = profile != null ? profile : dataConfig.getDefaultProfile();

            Optional<DataConfigurationProperties.DataTemplateProfile> any = dataConfig.getProfiles().stream().filter(t -> t.getId().equals(defaultProfile)).findAny();
            if (!any.isPresent()) {
                throw new OBErrorException(OBRIErrorType.DATA_INVALID_REQUEST,
                        "Profile '" + profile + "' doesn't exist."
                );
            }

            if (!userDataService.deleteUserData(psuName)) {
                psuCounterEntryKPIService.pushPsuCounterEntry(PsuCounterEntry.builder()
                        .count(1l)
                        .day(DateTime.now())
                        .build());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(userDataService.generateUserData(psuName,
                    defaultProfile));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.debug("TPP bad request: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.DATA_INVALID_REQUEST,
                        e.getResponseBodyAsString()
                );
            } else {
                log.error("Internal server: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.SERVER_ERROR);
            }
        }
    }

    private void verifyAccessTokenAndVerifyTppIdentity(String authorization, String tppName) throws OBErrorException, OAuth2BearerTokenUsageInvalidTokenException {
        String accessTokenEncoded = accessTokenService.getAccessTokenFromBearerAuthorizationValue(authorization);
        SignedJWT accessToken = accessTokenService.validateAccessTokenWithAM(accessTokenEncoded);
        accessTokenService.verifyAccessTokenGrantTypes(List.of(OIDCConstants.GrantType.CLIENT_CREDENTIAL),
                accessToken);
        accessTokenService.verifyAccessTokenScopes(List.of(OpenBankingConstants.Scope.OPENID), accessToken);
        accessTokenService.verifyMatlsFromAccessToken(accessToken, tppName);
    }

    @Override
    public ResponseEntity getProfiles() {
        return ResponseEntity.ok(dataConfig.getProfiles());
    }
}
