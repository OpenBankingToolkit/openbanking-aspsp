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
package com.forgerock.openbanking.aspsp.as.api.registration.dynamic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientException;
import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientIdentity;
import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientIdentityFactory;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.RegistrationRequest;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.RegistrationRequestFactory;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2BearerTokenUsageInvalidTokenException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2BearerTokenUsageMissingAuthInfoException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.utils.extractor.TokenExtractor;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * Class to implement the Open Banking (OB) Dynamic Client Registration (DCR) specification. This spec in turn relies
 * on many other standards; <p>
 * <ul>
 *     <li> <a href=https://openbankinguk.github.io/dcr-docs-pub/v3.3/dynamic-client-registration.html>
 *         Open Banking Dynamic Client Registration Specifications</a></li>
 *     <li><a href=https://datatracker.ietf.org/doc/html/rfc7591>
 *         The OAuth2 Dynamic Client Registration standard</a></li>
 *     <li><a href=https://www.rfc-editor.org/rfc/rfc6749>
 *         The OAuth2 Authorization Framework</a></li>
 *     <li><a href=https://datatracker.ietf.org/doc/html/rfc6750>
 *         The OAuth2 Authorization Framework: Bearer Token Usage</a></li>
 *     <li> <a href=https://www.ietf.org/rfc/rfc8705.html>
 *         OAuth 2.0 Mutual TLS Authentication and Certificate bound access tokens</a></li>
 * </ul>
 */
@Controller
@Slf4j
public class DynamicRegistrationApiController implements DynamicRegistrationApi {

    public static final String ORIGIN_ID_EIDAS = "EIDAS";
    private final TppStoreService tppStoreService;
    private final ObjectMapper objectMapper;
    private final TokenExtractor tokenExtractor;
    private final TppRegistrationService tppRegistrationService;
    private final List<String> supportedAuthMethod;
    private final ApiClientIdentityFactory apiClientIdentityFactory;
    private final RegistrationRequestFactory registrationRequestFactory;

    @Autowired
    public DynamicRegistrationApiController(TppStoreService tppStoreService,
                                            ObjectMapper objectMapper,
                                            TokenExtractor tokenExtractor,
                                            TppRegistrationService tppRegistrationService,
                                            @Value("${dynamic-registration.supported-token-endpoint-auth-method}")
                                            List<String> supportedAuthMethod,
                                            ApiClientIdentityFactory apiClientIdentityFactory,
                                            RegistrationRequestFactory registrationRequestFactory){
        this.tppStoreService = tppStoreService;
        this.objectMapper = objectMapper;
        this.tokenExtractor = tokenExtractor;
        this.tppRegistrationService = tppRegistrationService;
        this.supportedAuthMethod = supportedAuthMethod;
        this.apiClientIdentityFactory = apiClientIdentityFactory;
        this.registrationRequestFactory = registrationRequestFactory;
    }

    /**
     * Implementation of the DELETE /register endpoint
     *
     * @param authorization the value of the authorization header - this must match the registration_access_token
     *                      issued when the client registered. REQUIRED
     * @param principal - the Principal of the certificate used in the TLS connection used to call the endpoint. This
     *                 is used to identify the client that is making the request
     * @return
     * @throws OAuth2BearerTokenUsageMissingAuthInfoException
     * @throws OAuth2InvalidClientException
     */
    @Override
    public ResponseEntity<Void> deleteRegistration(String authorization, Principal principal)
            throws OAuth2BearerTokenUsageMissingAuthInfoException, OAuth2InvalidClientException,
            OAuth2BearerTokenUsageInvalidTokenException {
        log.debug("deleteRegistration() called with no clientId");
        return deleteRegistration(null, authorization, principal);
    }

    /**
     * Implementation of the DELETE /register endpoint
     *
     * @param clientId      the client Id to be unregistered
     * @param authorization the value of the authorization header - this must match the registration_access_token
     *        issued when the client registered.
     * @param principal     the Principal of the certificate used in the TLS connection used to call the endpoint. This
     *        is used to identify the client that is making the request
     * @return A
     * @throws OAuth2BearerTokenUsageMissingAuthInfoException
     * @throws OAuth2InvalidClientException
     * @throws OAuth2BearerTokenUsageInvalidTokenException
     */
    @Override
    public ResponseEntity<Void> deleteRegistration(String clientId, String authorization, Principal principal)
            throws OAuth2BearerTokenUsageMissingAuthInfoException, OAuth2InvalidClientException,
            OAuth2BearerTokenUsageInvalidTokenException {
        String methodName = "deleteRegistration()";
        log.info("{} called for ClientId '{}'", methodName, clientId);
        checkAuthArgsContainValidInformation(principal, authorization);
        
        Tpp tpp = getTpp(clientId);
        ensureTppOwnsOidcRegistration(tpp, principal.getName());

        String accessToken = validateAccessTokenIsValidForOidcRegistration(tpp, authorization);

        tppRegistrationService.deleteOAuth2RegistrationAndTppRecord(tpp);
        log.info("{} Unregistered ClientId '{}'", methodName, clientId);
        return ResponseEntity.ok().build();
    }

    /**
     * Implementation of the GET /register endpoint
     * @param authorization the value of the authorization header - this must match the registration_access_token
     *        issued when the client registered.
     * @param principal     the Principal of the certificate used in the TLS connection used to call the endpoint. This
     *        is used to identify the client that is making the request
     * @return
     * @throws OAuth2InvalidClientException
     * @throws OAuth2BearerTokenUsageInvalidTokenException
     * @throws OAuth2BearerTokenUsageMissingAuthInfoException
     */
    @Override
    public ResponseEntity<OIDCRegistrationResponse> getRegistration(String authorization, Principal principal)
            throws OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException,
            OAuth2BearerTokenUsageMissingAuthInfoException {
        log.info("Received a request to get registration information. No ClientId provided");
        ResponseEntity<OIDCRegistrationResponse> result = getRegistration(null, authorization, principal);
        if(result.getStatusCode() == HttpStatus.OK){
            log.info("Successfully returning registration information where no ClientId was provided");
        }
        return result;
    }

    /**
     * Implementation of the GET /register endpoint
     * @param clientId      the id of the client registration resource to be returned.
     * @param authorization the value of the authorization header - this must match the registration_access_token
     *        issued when the client registered.
     * @param principal     the Principal of the certificate used in the TLS connection used to call the endpoint. This
     *        is used to identify the client that is making the request
     * @return
     * @throws OAuth2InvalidClientException - the OAuth2 Dynamic Client Registration spec says "When an OAuth 2.0
     * error condition occurs, such as the client presenting an invalid initial access token, the authorization server
     *    returns an error response appropriate to the OAuth 2.0 token type. This exception will be thrown if the
     *    principal was not present. This is likely because an incorrect or no SSL certificate was provided.
     * @throws OAuth2BearerTokenUsageInvalidTokenException -
     * @throws OAuth2BearerTokenUsageMissingAuthInfoException
     */
    @Override
    public ResponseEntity<OIDCRegistrationResponse> getRegistration(String clientId, String authorization,
                                                                    Principal principal)
            throws OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException,
            OAuth2BearerTokenUsageMissingAuthInfoException {

        log.info("Received a request to get registration information for clientId {}, principal is {}", clientId,
                principal);
        checkAuthArgsContainValidInformation(principal, authorization);
        if(clientId == null){
            throw new OAuth2InvalidClientException("No client id provided. Request must be of the form " +
                    "/register/{clientId) where client Id is taken from the client_id in the registration response");
        }
        Tpp tpp = getTpp(clientId);
        ensureTppOwnsOidcRegistration(tpp, principal.getName());
        String accessToken = validateAccessTokenIsValidForOidcRegistration(tpp, authorization);
        OIDCRegistrationResponse registrationResponse = tppRegistrationService.getOIDCClient(accessToken, tpp);
        log.info("Successfully returning registration information for clientId {}", registrationResponse.getClientId());
        return ResponseEntity.ok(registrationResponse);
    }

    @Override
    public ResponseEntity<OIDCRegistrationResponse> updateRegistration(String authorization,
                                                                       String registrationRequestJwtSerialised,
                                                                       Principal principal
    ) throws OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException,
            OAuth2BearerTokenUsageMissingAuthInfoException, DynamicClientRegistrationException {
        log.info("Received a request to update registration information. No client Id provided");
        return updateRegistration(null, authorization, registrationRequestJwtSerialised, principal);
    }

    /**
     * Update the information relating to an existing OAuth2 client registration
     * @param clientId the client_id of the OAuth2 client registration that the ApiClient wishes to update
     * @param authorization An Authorisation Token as per https://tools.ietf.org/html/rfc6750
     * @param registrationRequestJwtSerialised A request to register a Software Statement Assertion with an ASPSP
     * @param principal - the principal identity that is making the request
     * @return returns a ResponseEntity used to determine if the request was successful and, if so, gain access to any
     * body returned, headers etc.
     * @throws OAuth2InvalidClientException
     * @throws OAuth2BearerTokenUsageInvalidTokenException
     * @throws OAuth2BearerTokenUsageMissingAuthInfoException
     * @throws DynamicClientRegistrationException
     */
    @Override
    public ResponseEntity<OIDCRegistrationResponse> updateRegistration(String clientId, String authorization,
                                                                       String registrationRequestJwtSerialised,
                                                                       Principal principal)
            throws OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException,
            OAuth2BearerTokenUsageMissingAuthInfoException, DynamicClientRegistrationException {
        String methodName = "updateRegistration()";
        try {
            log.info("{} called for ClientId '{}'. Princpal is {}", methodName, clientId, principal);
            ApiClientIdentity apiClientIdentity = this.apiClientIdentityFactory.getApiClientIdentity(principal);
            apiClientIdentity.throwIfTppNotOnboarded();
            RegistrationRequest registrationRequest =
                    registrationRequestFactory.getRegistrationRequestFromJwt(registrationRequestJwtSerialised);


            Tpp tpp = getTpp(clientId);
            ensureTppOwnsOidcRegistration(tpp, principal.getName());
            String accessToken = validateAccessTokenIsValidForOidcRegistration(tpp, authorization);

            //Override client ID
            registrationRequest.setClientId(clientId);

            verifyRegistrationRequest(apiClientIdentity, registrationRequest);
            registrationRequest.overwriteRegistrationRequestFieldsFromSSAClaims(apiClientIdentity);

            tpp = tppRegistrationService.updateTpp(apiClientIdentity, tpp, accessToken, registrationRequest);
            log.info("{} Updated registration information for ClientId {}", methodName, tpp.getClientId());
            return ResponseEntity.status(HttpStatus.OK).body(tpp.getRegistrationResponse());
        } catch (ApiClientException e) {
            String errorMessage =
                    "Error updating registration for clientId '" + clientId + " Error was: " + e.getMessage();
            log.info("{} {}", methodName, errorMessage, e);
            throw new OAuth2InvalidClientException(errorMessage);
        }
    }

    @Override
    public ResponseEntity<OIDCRegistrationResponse> register(
            @ApiParam(value = "A request to register a Software Statement Assertion with an ASPSP"  )
            @Valid
            @RequestBody String registrationRequestJwtSerialised,
            Principal principal
    ) throws OAuth2InvalidClientException, DynamicClientRegistrationException {
        String methodName = "register()";
        log.info("{} Received request to create a new client registration. {}",
                methodName,  registrationRequestJwtSerialised);

        try {
            ApiClientIdentity apiClientIdentity = this.apiClientIdentityFactory.getApiClientIdentity(principal);
            String tppIdentifier = apiClientIdentity.getTransportCertificateCn();

            RegistrationRequest registrationRequest =
                registrationRequestFactory.getRegistrationRequestFromJwt(registrationRequestJwtSerialised);
            //delete client ID
            registrationRequest.setClientId(null);
            verifyRegistrationRequest(apiClientIdentity, registrationRequest);
            registrationRequest.overwriteRegistrationRequestFieldsFromSSAClaims(apiClientIdentity);

            Tpp tpp = tppRegistrationService.registerTpp(apiClientIdentity, registrationRequest);
            OIDCRegistrationResponse registrationResponse = tpp.getRegistrationResponse();
            log.info("{} Registration succeeded. tpp {} now has OAuth2 ClientId of {}", methodName,
                    tppIdentifier, tpp.getClientId());
            return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
        }   catch (ApiClientException e) {
            log.info("Failed to create new client registration. There was an error related to the client requesting " +
                            "the registration; '{}'", e.getMessage());
            log.debug("register() caught ApiClientException.", e);
            throw new OAuth2InvalidClientException("Invalid certificate presented. Error was " + e.getMessage());
        }
    }


    /**
     * <p>Checks that the arguments provided to a method that requires a BEARER authorization token and a principal
     * are both provided and valid.</p>
     *
     * <b>BEARER Authorization Errors</b>
     *
     * <p>
     * If the authorize token is missing then we should return a OAuth2MissingAuthInfoException error in accord with
     * <a href=https://datatracker.ietf.org/doc/html/rfc6750#section-3.1>The OAuth 2.0 Authorization Framework: Bearer
     * Token Usage</a>.
     * </p>
     *
     * <b>M-TLS Principal Errors</b>
     * <p>The principal is pulled out of the certificate and is used for M-TLS purposes - i.e. to identify the TPP. Here
     * <a href=https://www.rfc-editor.org/rfc/rfc8705.html>RFC 8705
     * OAuth 2.0 Mutual-TLS Client Authentication and Certificate-Bound Access Tokens</a></p>
     *
     * <p>This says "If no certificate is presented, or that which is presented doesn't match that which is expected for
     * the given client_id, the authorization server returns a normal OAuth 2.0 error response per <
     * a href=https://www.rfc-editor.org/rfc/rfc6749#section-5.2>Section 5.2 of [RFC6749]</a> with the invalid_client
     * error code to indicate failed client authentication."</p>
     *
     * @param principal - The Principal that is used for MATLS authentication to identify the TPP organisation
     * @param authorization - the Authorization header from the request.
     * @throws OAuth2InvalidClientException - when the principal was not obtained from the request. This will be due
     * to the certificate not belonging to an onboarded TPP for example.
     * @throws OAuth2BearerTokenUsageMissingAuthInfoException - Thrown when there is no valid
     */
    private void checkAuthArgsContainValidInformation(Principal principal, String authorization)
            throws OAuth2BearerTokenUsageMissingAuthInfoException, OAuth2InvalidClientException {
        if(getPrincipalName(principal).equalsIgnoreCase("anonymous")){
            String errorMessage = "Mutual TLS failed - no principal found from request. This " +
                    "endpoint must be called using SSL using an OBWac certificate which will be used by the ASPSP for" +
                    " Mutual Authentication TLS";
            log.info("{}; authorization; '{}', principal: '{}'", errorMessage, authorization, principal);
            throw new OAuth2InvalidClientException(errorMessage);
        }
        if(StringUtils.isEmpty(authorization)){
            String errorMessage = "No valid Bearer authorization header. " +
                    "This should be set to the registration_access_token found in the response when you successfully " +
                    "used the /register endpoint to perform dynamic client registration.";
            log.info("{}; authorization; '{}', principal; '{}'", errorMessage, authorization, principal);
            throw new OAuth2BearerTokenUsageMissingAuthInfoException(errorMessage);
        }
    }

    /**
     * Safe way to get the name from the principal. If the principal is null or empty, or the name of the principal
     * is null or empty this method will return "Anonymous" instead.
     * @param principal - the Principal from which to safely get the name
     * @return A valid string. If no principal could be obtained this will be "Anonymous"
     */
    private String getPrincipalName(@Nullable Principal principal){
        if(!StringUtils.isEmpty(principal)){
            String principalName = principal.getName();
            if(!StringUtils.isEmpty(principalName)){
                return principalName;
            }
        }
        return "Anonymous";
    }

    /**
     * checks software id from SSA matches cn from old OB Transport certs.
     * calls verifyTppRegistrationRequestSignature on tppRegistrationService - checks the actual registration request
     * jwt is signed by a valid party
     * Checks the registration request against the ssa
     * @param clientIdentity - The identity of the TPP that made the request as presented to the API via spring
     *                       security principal.
     * @param registrationRequest - the registration request parsed into an object
     * @throws DynamicClientRegistrationException
     * @throws OAuth2InvalidClientException
     */
    private void verifyRegistrationRequest(
            ApiClientIdentity clientIdentity,
            RegistrationRequest registrationRequest
    ) throws DynamicClientRegistrationException, OAuth2InvalidClientException {
        log.trace("verifyRegistrationRequest()");
        //Verify request
        String softwareId = registrationRequest.getSoftwareIdFromSSA();

        // eIDAS certificates only identify the TPP, NOT the Software Statement. So if we have an eIDAS certificate we
        // won't find the softwareId anywhere in the certificate DN.
        if(!clientIdentity.isPSD2Certificate()) {
            throw new OAuth2InvalidClientException("Onboarding must be done with a PSD2 eIDAS certificate. " +
                    "OBTransport certificates have been depricated");
        }

        tppRegistrationService.verifyTPPRegistrationRequestAgainstSSA(registrationRequest);
        verifyAuthenticationMethodSupported(registrationRequest);
        log.trace("verifyRegistrationRequest() registration request is valid");
    }

    /**
     * Tests to see if the tpp's authorisationNumber matches a specific authorisationNumber.
     *
     * @param tpp - The tpp that made the request as identified by MATLS, cookie etc.
     * @param authorisationNumber the authorisationNumber to test
     * @throws OBErrorResponseException - if the oidcClientIDFromRequest does not match the client Id registered when
     * the TPP onboarded.
     */
    private void ensureTppOwnsOidcRegistration(@NotNull Tpp tpp, String authorisationNumber)
            throws OAuth2InvalidClientException {
        log.debug("ensureTppOwnsOidcRegistration() stored tpp clientId is '{}', authorisationNumber is '{}'",
                tpp==null?"tpp is null!":tpp.getClientId(),authorisationNumber);

        Optional<String> tppsAuthorisationNumber = getAuthorisationNumber(tpp);
        if (authorisationNumber == null || tppsAuthorisationNumber.isEmpty() || !tppsAuthorisationNumber.get().equals(authorisationNumber)) {
            String errorMessage =
                    "The clientId specified in the request url belongs to a different Tpp from the one indicated by " +
                            "the authorisationNumber in the transport certificate. authorisationNumber from cert was" +
                            " '" + authorisationNumber + "'. Please use a certificate associated with the Tpp that " +
                            "performed the registration.";
            log.info("{}; tpp; '{}', authorisationNumber; '{}'", errorMessage, tpp, authorisationNumber);
            throw new OAuth2InvalidClientException(errorMessage);
        }
        log.debug("ensureTppOwnsOidcRegistration() - success - TPP owns the clientId specified in request URL");
    }

    private Optional<String> getAuthorisationNumber(Tpp tpp){
        return Optional.ofNullable(tpp.getAuthorisationNumber());
    }

    private void verifyAuthenticationMethodSupported(RegistrationRequest registrationRequest)
            throws DynamicClientRegistrationException {
        String requestedTokenEndpointAuthMethod = registrationRequest.getTokenEndpointAuthMethod();
        if(requestedTokenEndpointAuthMethod == null){
            String errorString = "The registration request must contain a token_endpoint_auth_method";
            log.debug("verifyAuthenticationMethodSupported() {}", errorString);
            throw new DynamicClientRegistrationException(errorString, DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
        } else {
            if (!supportedAuthMethod.contains(requestedTokenEndpointAuthMethod)) {
                String errorString =
                        "The requested token endpoint authentication method " + requestedTokenEndpointAuthMethod
                                + " is not supported";
                log.info("verifyAuthenticationMethodSupported() {}; registrationRequest; '{}'", errorString, registrationRequest);
                throw new DynamicClientRegistrationException(errorString, DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
            }
        }
    }

    /**
     * getTpp returns the tpp associated with the principal. If not tpp can be found will throw.
     * @param clientId the principal as obtained from the client MATLS certificate used to make the request
     * @return a Tpp object belonging to the principal. If no tpp can be found then will throw
     * OAuth2InvalidClientException
     * @throws OAuth2InvalidClientException
     */
    private @NotNull Tpp getTpp(String clientId) throws OAuth2InvalidClientException {
        //Todo: this next line looks odd. findByClientId but passing the principal.getName which I would think would
        // return the name pulled from the MATLS certificate?
        Optional<Tpp> optionalTpp = tppStoreService.findByClientId(clientId);
        if (optionalTpp.isEmpty()) {
            String errorMessage = "No registration exists for the clientId in the request path. clientId was '" +
                    clientId + "'";
            log.info("getTpp() {}", errorMessage);
            throw new OAuth2InvalidClientException(errorMessage);
        }
        Tpp tpp = optionalTpp.get();
        log.debug("getTpp(): Tpp is {}", tpp);
        return tpp;
    }

    /**
     * The registration_access_token was provided to the client in response to a successful registration request.
     * @param tpp - the tpp associated with the MATLS client certificate used to make the request. Required: true,
     *            NotNull: true
     * @param authorization - the Authorization header value from the request
     * @return the access token if valid
     * @throws OAuth2BearerTokenUsageMissingAuthInfoException
     * @throws OAuth2BearerTokenUsageInvalidTokenException
     */
    private String validateAccessTokenIsValidForOidcRegistration(@NotNull Tpp tpp, String authorization)
            throws OAuth2BearerTokenUsageMissingAuthInfoException,OAuth2BearerTokenUsageInvalidTokenException {
        log.debug("validateAccessTokenIsValidForOidcRegistration() tpp is '{}', authorization is '{}'", tpp==null?
                        "null":tpp.getClientId(),
                authorization);
        // Told you tpp must not be null!
        Objects.requireNonNull(tpp);

        String accessToken = getAccessTokenFromAuthHeaderValue(authorization);
        validateBearerTokenBelongsToTpp(accessToken, tpp);

        log.debug("validateAccessTokenIsValidForOidcRegistration() Tpp '{}' has valid accessToken.", tpp.getClientId());
        return accessToken;
    }


    private String getAccessTokenFromAuthHeaderValue(String authorization)
            throws OAuth2BearerTokenUsageMissingAuthInfoException, OAuth2BearerTokenUsageInvalidTokenException {
        String accessToken = null;
        if(StringUtils.isEmpty(authorization)){
            String errorMessage = "AuthorizationHeader is empty. It should contain a Bearer OAuth2 token. See rfc-6750";
            log.info("getAccessTokenFromAuthHeaderValue() {}", errorMessage);
            throw new OAuth2BearerTokenUsageMissingAuthInfoException(errorMessage);
        } else {
            try {
                accessToken = tokenExtractor.extract(authorization);
            } catch (AuthenticationServiceException re) {
                String errorMessage = String.format("Failed to extract Bearer token from authorization header: " +
                                "'%s'. Error was %s. Authorization header should contain an OAuth2 Bearer token. " +
                                "See rfc-6750", authorization, re.getMessage());
                log.info("validateAccessTokenIsValidForOidcRegistration() {}", errorMessage);
                throw new OAuth2BearerTokenUsageInvalidTokenException(errorMessage);
            }
        }
        log.debug("getAccessTokenFromAuthHeaderValue() got access token");
        return accessToken;
    }

    private void validateBearerTokenBelongsToTpp(String accessToken, @NotNull Tpp tpp)
            throws OAuth2BearerTokenUsageInvalidTokenException {
        // Told you tpp must not be null!
        Objects.requireNonNull(tpp);
        log.debug("validateBearerTokenBelongsToTpp() validate access token in request belongs to {}",
                tpp.getClientId());

        boolean accessTokenBelongsToTpp = false;
        Optional<String> registrationAccessToken = tpp.getRegistrationAccessToken();
        if(registrationAccessToken.isPresent()) {
            accessTokenBelongsToTpp = registrationAccessToken.get().equals(accessToken);
        }
        if(!accessTokenBelongsToTpp){
            String errorMessage = "Authorization Bearer token is not the bearer token issued to the TPP " +
                    "identified by the MATLS client certificate used to make this request. Please use the " +
                    "registration_access_token that was provided in your successful request to the POST /register" +
                    " endpoint. i.e. When you successfully performed dynamic client registration.";
            log.info("validateAccessTokenIsValidForOidcRegistration() {}", errorMessage);
            throw new OAuth2BearerTokenUsageInvalidTokenException(errorMessage);
        } else {
            log.debug("validateBearerTokenBelongsToTpp() access token does belong to Tpp");
        }
    }
}
