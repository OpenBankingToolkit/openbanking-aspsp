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
import com.forgerock.openbanking.aspsp.as.api.oauth2.discovery.DiscoveryConfig;
import com.forgerock.openbanking.aspsp.as.service.headless.authorisation.HeadLessAuthorisationService;
import com.forgerock.openbanking.common.services.JwtOverridingService;
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
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;


@Controller
@Slf4j
/**
 * Spring Controller that handles OAuth2 Authorization and implements the /authorize endpoint.
 */
public class AuthorisationApiController implements AuthorisationApi {

    private final  HeadLessAuthorisationService headLessAuthorisationService;
    private final  CryptoApiClient cryptoApiClient;
    private final  TppStoreService tppStoreService;
    private final  String cookieName;
    private final  boolean isHeadlessAlwaysEnabled;
    private final  AMGatewayService amGatewayService;
    private final  JwtOverridingService jwtOverridingService;
    private final  TokenUsageService tokenUsageService;
    private final  DiscoveryConfig discoveryConfig;
    private static Pattern ID_TOKEN_PATTERN = Pattern.compile("id_token=?([^&]+)?");

    @Autowired
    public AuthorisationApiController(HeadLessAuthorisationService headLessAuthorisationService, 
                                      CryptoApiClient cryptoApiClient, TppStoreService tppStoreService,
                                      @Value("${am.cookie.name}") String cookieName,
                                      @Value("${as.headless.always-enable}") boolean isHeadlessAlwaysEnabled,
                                      AMGatewayService amGatewayService, JwtOverridingService jwtOverridingService, 
                                      TokenUsageService tokenUsageService, DiscoveryConfig discoveryConfig) {
        this.headLessAuthorisationService = headLessAuthorisationService;
        this.cryptoApiClient = cryptoApiClient;
        this.tppStoreService = tppStoreService;
        this.cookieName = cookieName;
        this.isHeadlessAlwaysEnabled = isHeadlessAlwaysEnabled;
        this.amGatewayService = amGatewayService;
        this.jwtOverridingService = jwtOverridingService;
        this.tokenUsageService = tokenUsageService;
        this.discoveryConfig = discoveryConfig;
    }
    
    

    /**
     * @param responseType required = true
     * @param clientId required = true
     * @param state required = false
     * @param nonce required = false
     * @param scopes required = false
     * @param redirectUri required = false,
     * @param requestParametersSerialised required = true)
     * @param isHeadlessEnabled required = false, defaultValue = "false"
     * @param username required = false, defaultValue = ""
     * @param password required = false, defaultValue = ""
     * @param ssoToken (required = false)
     * @param body required = false
     * @return A <code>ResponseEntity</code> containing the result of authorization request
     */
    @Override
    public ResponseEntity getAuthorisation(
            String responseType, String clientId, String state, String nonce, String scopes, String redirectUri,
            String requestParametersSerialised, boolean isHeadlessEnabled, String username, String password,
            String ssoToken, MultiValueMap body, HttpServletRequest request
    ) throws OBErrorResponseException, OBErrorException {
        // FAPI compliant ('code id_token'): https://github.com/ForgeCloud/ob-deploy/issues/674
        if(!discoveryConfig.getSupportedResponseTypes().contains(responseType)){
            log.error("The response types requested '" + responseType + "' don't match with the response types " +
                    "supported '" + discoveryConfig.getSupportedResponseTypes() + "' by as-api");
            throw new OBErrorResponseException(
                    OBRIErrorType.REQUEST_RESPONSE_TYPE_MISMATCH.getHttpStatus(),
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.REQUEST_RESPONSE_TYPE_MISMATCH.toOBError1(responseType,
                            discoveryConfig.getSupportedResponseTypes().toString()));
        }
        SignedJWT requestParameterJwt = validateRequestParameter(responseType, clientId, state, nonce, scopes,
                redirectUri, requestParametersSerialised);

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
            log.debug("getAuthorisation() performing headless authorisation");
            responseEntity = headLessAuthorisationService.getAuthorisation(amGateway, responseType, clientId, state,
                    nonce, scopes, redirectUri, requestParametersSerialised, username, password);
        } else {
            log.debug("getAuthorisation() delegating authorisation to AM");
            HashMap<String, String> queryParameters = new HashMap<>();
            queryParameters.put("request", requestParametersSerialised);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Cookie", cookieName + "=" + ssoToken);
            responseEntity = amGateway.toAM(request, httpHeaders, queryParameters,
                    new ParameterizedTypeReference<String>() {}, body);
        }
        log.debug("getAuthorisation() responseEntity {}", responseEntity);

        // The id_token should be in the URL fragment, not as a query parameter. If it appears as a query parameter
        // re-write it to appear as a fragment
        if (hasQueryParamIdToken(responseEntity)) {
            responseEntity = convertQueryToFragment(responseEntity.getHeaders().getLocation(),
                    responseEntity.getHeaders(), state);
        }

        //Rewriting the response as we need to re-sign the id token. We can assume the id_token will exist as a fragment
        if(hasFragmentIdToken(responseEntity)) {
            try {
                responseEntity = this.jwtOverridingService.rewriteIdTokenFragmentInLocationHeader(responseEntity);
                tokenUsageService.incrementTokenUsage(TokenUsage.ID_TOKEN);
            } catch (JwtOverridingService.AccessTokenReWriteException e){
                String supportUID = UUID.randomUUID().toString();
                log.info("getAuthorisation() Failed to re-write the id_token", e);
                throw new OBErrorResponseException(
                        OBRIErrorType.AUTHORIZE_INVALID_ID_TOKEN.getHttpStatus(),
                        OBRIErrorResponseCategory.ACCESS_TOKEN,
                        OBRIErrorType.AUTHORIZE_INVALID_ID_TOKEN.toOBError1(supportUID));
            }
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

    private SignedJWT validateRequestParameter(String responseType, String clientId, String state, String nonce,
                                               String scopes, String redirectUri, String requestParametersSerialised)
            throws OBErrorException {
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
                throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_FORMAT_INVALID, "Unknown client id '"
                        + clientId + "'");
            }
            Tpp tpp = byClientId.get();

            log.debug("Validate the request parameter signature");
            SignedJWT validatedJwt = null;
            boolean validated = false;
            OIDCRegistrationResponse registrationResponse = tpp.getRegistrationResponse();
            if (registrationResponse != null) {
                JWKSet jwkSet = registrationResponse.getJwks();
                if (jwkSet != null) {
                    List<JWK> jwkSetKeys = jwkSet.getKeys();
                    if (jwkSetKeys != null && !jwkSetKeys.isEmpty()) {
                        JWK jwk = jwkSetKeys.get(0);
                        String jwksKeys = jwk.toString();
                        log.debug("validateRequestParameters() tpp has jwksKeys as part of registraiton. They will be" +
                                " used to validate the request parameter.");
                        // ToDo: This throws is the jwt can't be validated. I was checking to see if the return (a
                        //  SignedJWT was non-null as well, but it would appear that the API can return a null
                        //  SignedJWT even when the jwt is valid. This seems wrong but I have to stop fixing stuff
                        //  somewhere or I will never get this PR merged!!!
                        cryptoApiClient.validateJwsWithJWK(requestParametersSerialised, clientId, jwksKeys);
                        validated = true;
                    } else {
                        log.error("validateRequestParameter() tpp has no jwkSetKeys; {}", tpp);
                    }
                }

                if( validated == false){
                    String jwks_uri = tpp.getRegistrationResponse().getJwks_uri();
                    if(jwks_uri == null || jwks_uri.isBlank()){
                        log.debug("validateRequestparameters() tpp does no have a jwks_uri in it's registration " +
                                "details; {}", tpp);
                    } else {
                        log.debug("validateRequestParameter() Validating request parameter using jwks_uri: " +
                                        "requestParametersSerialised: '{}', clientId; '{}', jwks_url: {}",
                                requestParametersSerialised, clientId, jwks_uri);
                        // ToDo: This throws is the jwt can't be validated. I was checking to see if the return (a
                        //  SignedJWT was non-null as well, but it would appear that the API can return a null
                        //  SignedJWT even when the jwt is valid. This seems wrong but I have to stop fixing stuff
                        //  somewhere or I will never get this PR merged!!!
                        cryptoApiClient.validateJws(requestParametersSerialised, clientId, jwks_uri);
                        validated = true;
                    }
                }
            } else {
                log.error("validateRequestParameter() tpp has no registration response; {}", tpp);
            }

            if(validated == false){
                log.debug("validateRequestParameter() failed to validate the request parameter");
                throw new InvalidTokenException("Failed to validate jwt.");
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

    private void verifyScopeQueryParameterMatchesRequestParameterClaim(SignedJWT requestParameters, String value)
            throws ParseException, OBErrorException {
        if (value == null) {
            return;
        }
        String scope = "scope";
        List<String> requestParamScopes = Arrays.asList(value.split(" "));
        String jwtScopeString = requestParameters.getJWTClaimsSet().getStringClaim(scope);
        List<String> jwtScopes = jwtScopeString == null ? Collections.emptyList() :
                Arrays.asList(jwtScopeString.split(" "));

        if (!requestParamScopes.containsAll(jwtScopes)) {
            log.error("Query parameter '{} : {}' was expected to match value in JWT Claim: {}", scope, value, jwtScopes);
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_QUERY_PARAM_DIFF_CLAIM, scope, scope);
        }
    }

    private void verifyQueryParameterMatchesRequestParameterClaim(SignedJWT requestParameters, String name,
                                                                  String value)
            throws ParseException, OBErrorException {
        if (value != null
                && !value.equals(requestParameters.getJWTClaimsSet().getStringClaim(name))) {
            log.error("Query parameter '{} : {}' was expected to match value in JWT Claim: {}", name, value,
                    requestParameters.getJWTClaimsSet().getStringClaim(name));
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_QUERY_PARAM_DIFF_CLAIM, name, name);
        }
    }

    private void verifyRequestparameterClaims(SignedJWT requestParameters) throws OBErrorException, ParseException {

        JWTClaimsSet claimSet = requestParameters.getJWTClaimsSet();
        JSONObject claims = new JSONObject(claimSet.getJSONObjectClaim(OIDCConstants.OIDCClaim.CLAIMS));

        if (!claims.containsKey(OpenBankingConstants.RequestParameterClaim.ID_TOKEN)) {
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID, "No id token claims");
        }

        Map<String, Claim> idTokenClaims = validateIdToken(claims);

        validateUserInfo(claims, idTokenClaims);
    }

    private void validateUserInfo(JSONObject claims, Map<String, Claim> idTokenClaims) throws OBErrorException {
        Map<String, Claim> userInfoClaims = Claims.parseClaimsFrom(OpenBankingConstants.RequestParameterClaim.USERINFO,
                claims);
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
        Map<String, Claim> idTokenClaims = Claims.parseClaimsFrom(OpenBankingConstants.RequestParameterClaim.ID_TOKEN,
                claims);

        if (!idTokenClaims.containsKey(OpenBankingConstants.IdTokenClaim.INTENT_ID)) {
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID,
                    "claims->id_token should contains the claim '" + OpenBankingConstants.IdTokenClaim.INTENT_ID + "'");
        }

        if (!idTokenClaims.get(OpenBankingConstants.IdTokenClaim.INTENT_ID).isEssential()) {
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID,
                    "'" + OpenBankingConstants.IdTokenClaim.INTENT_ID + "' should be essential");
        }

//      Disabling mandatory ACR security check as the conformance tests is being lenient to help OB adoption. We may
//      need to make ACR mandatory sometime in the future
        if (idTokenClaims.containsKey(OpenBankingConstants.IdTokenClaim.ACR)
                && !idTokenClaims.get(OpenBankingConstants.IdTokenClaim.ACR).isEssential()) {
            throw new OBErrorException(OBRIErrorType.REQUEST_PARAMETER_JWT_INVALID,
                    "'" + OpenBankingConstants.IdTokenClaim.ACR + "' should be essential");
        }
        return idTokenClaims;
    }
}
