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
package com.forgerock.openbanking.aspsp.as.api.authorisation.redirect;

import com.forgerock.openbanking.am.gateway.AMGateway;
import com.forgerock.openbanking.am.services.AMGatewayService;
import com.forgerock.openbanking.analytics.model.entries.TokenUsage;
import com.forgerock.openbanking.analytics.services.TokenUsageService;
import com.forgerock.openbanking.aspsp.as.service.JwtOverridingService;
import com.forgerock.openbanking.aspsp.as.service.headless.authorisation.HeadLessAuthorisationService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.claim.Claim;
import com.forgerock.openbanking.model.claim.Claims;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.SignedJWT;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@Slf4j
public class AuthorisationApiController implements AuthorisationApi {

    @Autowired
    private HeadLessAuthorisationService headLessAuthorisationService;
    @Autowired
    private CryptoApiClient cryptoApiClient;
    @Autowired
    private TppStoreService tppStoreService;
    @Value("${am.cookie.name}")
    private String cookieName;
    @Value("${as.headless.always-enable}")
    private boolean isHeadlessAlwaysEnabled;
    @Autowired
    private AMGatewayService amGatewayService;
    @Autowired
    private JwtOverridingService jwtOverridingService;
    @Autowired
    private TokenUsageService tokenUsageService;

    private static Pattern ID_TOKEN_PATTERN = Pattern.compile("id_token=?([^&]+)?");


    @Override
    public ResponseEntity getAuthorisation(
            @ApiParam(value = "OpenID response type")
            @RequestParam(value = "response_type", required = true) String responseType,

            @ApiParam(value = "OpenID Client ID")
            @RequestParam(value = "client_id", required = true) String clientId,

            @ApiParam(value = "OpenID state")
            @RequestParam(value = "state", required = false) String state,

            @ApiParam(value = "OpenID nonce")
            @RequestParam(value = "nonce", required = false) String nonce,

            @ApiParam(value = "OpenID scopes")
            @RequestParam(value = "scope", required = false) String scopes,

            @ApiParam(value = "OpenID redirect_uri")
            @RequestParam(value = "redirect_uri", required = false) String redirectUri,

            @ApiParam(value = "OpenID request parameters")
            @RequestParam(value = "request", required = true) String requestParametersSerialised,

            @ApiParam(value = "Default username for the headless authentication")
            @RequestHeader(value = "X_HEADLESS_AUTH_ENABLED", required = false, defaultValue = "false") boolean isHeadlessEnabled,

            @ApiParam(value = "Default username for the headless authentication")
            @RequestHeader(value = "X_HEADLESS_AUTH_USERNAME", required = false, defaultValue = "") String username,

            @ApiParam(value = "Default username password for the headless authentication")
            @RequestHeader(value = "X_HEADLESS_AUTH_PASSWORD", required = false, defaultValue = "") String password,

            @ApiParam(value = "Cookie containing the user session", required = false)
            @CookieValue(value = "${am.cookie.name}", required = false) String ssoToken,

            @RequestBody(required = false) MultiValueMap body,

            HttpServletRequest request
    ) throws OBErrorResponseException, OBErrorException {
        SignedJWT requestParameterJwt = validateRequestParameter(responseType, clientId, state, nonce, scopes, redirectUri, requestParametersSerialised);
        requestParametersSerialised = requestParameterJwt.serialize();
        try {
            state = getState(state, requestParameterJwt);
        } catch (ParseException e) {
            throw new OBErrorResponseException(
                    OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID.getHttpStatus(),
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID.toOBError1(e.getMessage()));
        }

        AMGateway amGateway = amGatewayService.getAmGateway(requestParametersSerialised);

        ResponseEntity responseEntity;
        if (isHeadlessAlwaysEnabled || isHeadlessEnabled) {
            responseEntity = headLessAuthorisationService.getAuthorisation(amGateway, responseType, clientId, state, nonce, scopes, redirectUri, requestParametersSerialised, username, password);
        } else {
            HashMap<String, String> queryParameters = new HashMap<>();
            queryParameters.put("request", requestParametersSerialised);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Cookie", cookieName + "=" + ssoToken);
            responseEntity = amGateway.toAM(request, httpHeaders, queryParameters, new ParameterizedTypeReference<String>() {
            }, body);
        }
        log.debug("responseEntity {}", responseEntity);

        //Rewriting the response as we need to re-sign the id token
        if (hasFragmentIdToken(responseEntity)) {
            try {
                HttpHeaders writableHttpHeaders = HttpHeaders.writableHttpHeaders(responseEntity.getHeaders());
                writableHttpHeaders.set(HttpHeaders.LOCATION, replaceIdToken(responseEntity.getHeaders().getLocation()));
                responseEntity = new ResponseEntity(null, writableHttpHeaders, HttpStatus.FOUND);
                tokenUsageService.incrementTokenUsage(TokenUsage.ID_TOKEN);
            } catch (ParseException e) {
                String supportUID = UUID.randomUUID().toString();
                log.error("Error when parsing ID token from fragment {}. Support UID {}", responseEntity.getHeaders().getLocation().getFragment(), supportUID, e);
                throw new OBErrorResponseException(
                        OBRIErrorType.AUTHORIZE_INVALID_ID_TOKEN.getHttpStatus(),
                        OBRIErrorResponseCategory.ACCESS_TOKEN,
                        OBRIErrorType.AUTHORIZE_INVALID_ID_TOKEN.toOBError1(supportUID));
            }
        } else if (hasQueryParamIdToken(responseEntity)) {
            responseEntity = convertQueryToFragment(responseEntity.getHeaders().getLocation(), responseEntity.getHeaders(), state);
            tokenUsageService.incrementTokenUsage(TokenUsage.ID_TOKEN);
        } else {
            log.debug("responseEntity {} is null or is not a redirection", responseEntity);
        }
        return responseEntity;
    }

    private static boolean hasQueryParamIdToken(ResponseEntity responseEntity) {
        if (responseEntity == null) {
            return false;
        }
        if (responseEntity.getStatusCode() != HttpStatus.FOUND) {
            return false;
        }
        if (responseEntity.getHeaders().getLocation() == null) {
            return false;
        }
        log.debug("Location {}", responseEntity.getHeaders().getLocation());
        if (responseEntity.getHeaders().getLocation().getQuery() == null) {
            return false;
        }
        return responseEntity.getHeaders().getLocation().getQuery().contains("error_description");
    }

    private static boolean hasFragmentIdToken(ResponseEntity responseEntity) {
        if (responseEntity == null) {
            return false;
        }
        if (responseEntity.getStatusCode() != HttpStatus.FOUND) {
            return false;
        }
        if (responseEntity.getHeaders().getLocation() == null) {
            return false;
        }
        log.debug("Location {}", responseEntity.getHeaders().getLocation());
        if (responseEntity.getHeaders().getLocation().getFragment() == null) {
            return false;
        }

        if (!responseEntity.getHeaders().getLocation().getFragment().contains("id_token")) {
            log.debug("Fragment {} doesn't contains id_token", responseEntity.getHeaders().getLocation().getFragment());
            return false;
        }
        return true;
    }

    public String getState(String state, SignedJWT requestJwt) throws ParseException {
        if (state != null) {
            return state;
        }
        return requestJwt.getJWTClaimsSet().getStringClaim(OpenBankingConstants.RequestParameterClaim.STATE);
    }

    public ResponseEntity convertQueryToFragment(URI location, HttpHeaders httpHeaders, String state) {
        log.debug("Query contains values and in hybrid flow, it should use the fragment");
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(location);
        uriComponentsBuilder.replaceQuery("");
        String fragment = location.getQuery();
        if (!fragment.contains("state=")) {
            fragment += "&state=" + state;
        }
        uriComponentsBuilder.fragment(fragment);
        HttpHeaders writableHttpHeaders = HttpHeaders.writableHttpHeaders(httpHeaders);
        writableHttpHeaders.set(HttpHeaders.LOCATION, uriComponentsBuilder.toUriString());
        return new ResponseEntity(null, writableHttpHeaders, HttpStatus.FOUND);
    }

    private SignedJWT validateRequestParameter(String responseType, String clientId, String state, String nonce, String scopes, String redirectUri, String requestParametersSerialised) throws OBErrorException {
        SignedJWT requestParameters;
        try {
            try {
                EncryptedJWT.parse(requestParametersSerialised);
                log.debug("Request parameter {} is encrypted (JWE).", requestParametersSerialised);
                requestParameters = cryptoApiClient.decryptJwe(requestParametersSerialised);
                requestParametersSerialised = requestParameters.serialize();
                log.debug("Request parameter {} decrypted (JWS).", requestParametersSerialised);
            } catch (ParseException | JOSEException e) {
                //If we got an exception, it means it's a JWS
                log.debug("Request parameter {} is just signed (JWS).", requestParametersSerialised);
                requestParameters = SignedJWT.parse(requestParametersSerialised);
            }

            verifyQueryParameterMatchesRequestParameterClaim(requestParameters, "client_id", clientId);
            Optional<Tpp> byClientId = tppStoreService.findByClientId(clientId);
            if (!byClientId.isPresent()) {
                throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_FORMAT_INVALID, "Unknown client id '" + clientId + "'");
            }
            Tpp tpp = byClientId.get();

            log.debug("Validate the request parameter");
            if (tpp.getRegistrationResponse().getJwks() != null) {
                cryptoApiClient.validateJwsWithJWK(requestParametersSerialised, clientId, tpp.getRegistrationResponse().getJwks().getKeys().get(0).toJSONString());
            } else {
                cryptoApiClient.validateJws(requestParametersSerialised, clientId, tpp.getRegistrationResponse().getJwks_uri());
            }
            List<String> MANDATORY_CLAIMS = Arrays.asList(
                    OpenBankingConstants.RequestParameterClaim.AUD,
                    OpenBankingConstants.RequestParameterClaim.SCOPE,
                    OpenBankingConstants.RequestParameterClaim.ISS,
                    OpenBankingConstants.RequestParameterClaim.CLAIMS,
                    OpenBankingConstants.RequestParameterClaim.RESPONSE_TYPE,
                    OpenBankingConstants.RequestParameterClaim.REDIRECT_URI,
                    OpenBankingConstants.RequestParameterClaim.EXP,
                    OpenBankingConstants.RequestParameterClaim.NONCE,
                    OpenBankingConstants.RequestParameterClaim.CLIENT_ID
            );

            for (String mandatoryClaim : MANDATORY_CLAIMS) {
                if (requestParameters.getJWTClaimsSet().getClaim(mandatoryClaim) == null) {
                    throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_CLAIM_MANDATORY, mandatoryClaim);
                }
            }

            verifyQueryParameterMatchesRequestParameterClaim(requestParameters, "response_type", responseType);
            verifyQueryParameterMatchesRequestParameterClaim(requestParameters, "state", state);
            verifyQueryParameterMatchesRequestParameterClaim(requestParameters, "nonce", nonce);
            verifyQueryParameterMatchesRequestParameterClaim(requestParameters, "redirect_uri", redirectUri);
            verifyScopeQueryParameterMatchesRequestParameterClaim(requestParameters, scopes);

            verifyRequestparameterClaims(requestParameters);
        } catch (ParseException | IOException e) {
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_FORMAT_INVALID, e.getMessage());
        } catch (InvalidTokenException e) {
            log.error("Invalid Request parameter {}. Reason: {}", requestParametersSerialised, e.getMessage(), e);
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID, e.getMessage());
        }
        return requestParameters;
    }

    private void verifyScopeQueryParameterMatchesRequestParameterClaim(SignedJWT requestParameters, String value) throws ParseException, OBErrorException {
        if (value == null) {
            return;
        }
        String scope = "scope";
        List<String> requestParamScopes = Arrays.asList(value.split(" "));
        String jwtScopeString = requestParameters.getJWTClaimsSet().getStringClaim(scope);
        List<String> jwtScopes = jwtScopeString == null ? Collections.emptyList() : Arrays.asList(jwtScopeString.split(" "));

        if (!requestParamScopes.containsAll(jwtScopes)) {
            log.error("Query parameter '{} : {}' was expected to match value in JWT Claim: {}", scope, value, jwtScopes);
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_QUERY_PARAM_DIFF_CLAIM, scope, scope);
        }
    }

    private void verifyQueryParameterMatchesRequestParameterClaim(SignedJWT requestParameters, String name, String value) throws ParseException, OBErrorException {
        if (value != null
                && !value.equals(requestParameters.getJWTClaimsSet().getStringClaim(name))) {
            log.error("Query parameter '{} : {}' was expected to match value in JWT Claim: {}", name, value, requestParameters.getJWTClaimsSet().getStringClaim(name));
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_QUERY_PARAM_DIFF_CLAIM, name, name);
        }
    }

    private void verifyRequestparameterClaims(SignedJWT requestParameters) throws OBErrorException, ParseException {
        JSONObject claims = requestParameters.getJWTClaimsSet().getJSONObjectClaim(OIDCConstants
                .OIDCClaim.CLAIMS);

        if (!claims.containsKey(OpenBankingConstants.RequestParameterClaim.ID_TOKEN)) {
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID, "No id token claims");
        }

        Map<String, Claim> idTokenClaims = validateIdToken(claims);

        validateUserInfo(claims, idTokenClaims);
    }

    private void validateUserInfo(JSONObject claims, Map<String, Claim> idTokenClaims) throws OBErrorException {
        Map<String, Claim> userInfoClaims = Claims.parseClaimsFrom(OpenBankingConstants.RequestParameterClaim.USERINFO, claims);
        if (userInfoClaims.isEmpty()) {
            return;
        }

        if (!userInfoClaims.get(OpenBankingConstants.IdTokenClaim.INTENT_ID)
                .equals(idTokenClaims.get(OpenBankingConstants.IdTokenClaim.INTENT_ID))) {
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID,
                    "claims->id_token->" + OpenBankingConstants.IdTokenClaim.INTENT_ID + " is not equal to " +
                            "claims->user_info->" + OpenBankingConstants.IdTokenClaim.INTENT_ID);
        }
    }

    private Map<String, Claim> validateIdToken(JSONObject claims) throws OBErrorException {
        Map<String, Claim> idTokenClaims = Claims.parseClaimsFrom(OpenBankingConstants.RequestParameterClaim.ID_TOKEN, claims);

        if (!idTokenClaims.containsKey(OpenBankingConstants.IdTokenClaim.INTENT_ID)) {
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID,
                    "claims->id_token should contains the claim '" + OpenBankingConstants.IdTokenClaim.INTENT_ID + "'");
        }

        if (!idTokenClaims.get(OpenBankingConstants.IdTokenClaim.INTENT_ID).isEssential()) {
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID,
                    "'" + OpenBankingConstants.IdTokenClaim.INTENT_ID + "' should be essential");
        }

//      Disabling mandatory ACR security check as the conformance tests is being lenient to help OB adoption. We may need to make ACR mandatory sometime in the future
        if (idTokenClaims.containsKey(OpenBankingConstants.IdTokenClaim.ACR)
                && !idTokenClaims.get(OpenBankingConstants.IdTokenClaim.ACR).isEssential()) {
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID,
                    "'" + OpenBankingConstants.IdTokenClaim.ACR + "' should be essential");
        }
        return idTokenClaims;
    }

    private String replaceIdToken(URI uri) throws ParseException {
        String uriSerialised = uri.toString();
        Matcher matcher = ID_TOKEN_PATTERN.matcher(uriSerialised);
        while (matcher.find()) {
            String oldIdToken = matcher.group(1);
            String newIdToken = jwtOverridingService.rewriteJWS(oldIdToken);
            uriSerialised = uriSerialised.replaceAll(oldIdToken, newIdToken);
        }
        return uriSerialised;
    }
}
