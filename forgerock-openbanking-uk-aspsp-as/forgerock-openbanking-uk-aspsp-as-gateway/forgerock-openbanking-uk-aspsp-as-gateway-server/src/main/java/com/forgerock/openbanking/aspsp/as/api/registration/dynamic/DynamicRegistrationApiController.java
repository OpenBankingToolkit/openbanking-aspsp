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
import com.forgerock.openbanking.aspsp.as.service.OIDCException;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2BearerTokenUsageInvalidTokenException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2BearerTokenUsageMissingAuthInfoException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.utils.extractor.TokenExtractor;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.SoftwareStatementRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationRequest;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.forgerock.spring.security.multiauth.model.authentication.X509Authentication;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.forgerock.openbanking.common.utils.X509CertificateHelper.getCn;
import static com.forgerock.openbanking.constants.OpenBankingConstants.RegistrationTppRequestClaims;
import static com.forgerock.openbanking.constants.OpenBankingConstants.SSAClaims;

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

    @Autowired
    public DynamicRegistrationApiController(TppStoreService tppStoreService,
                                            ObjectMapper objectMapper,
                                            TokenExtractor tokenExtractor,
                                            TppRegistrationService tppRegistrationService,
                                            @Value("${dynamic-registration.supported-token-endpoint-auth-method}")
                                            List<String> supportedAuthMethod){
        this.tppStoreService = tppStoreService;
        this.objectMapper = objectMapper;
        this.tokenExtractor = tokenExtractor;
        this.tppRegistrationService = tppRegistrationService;
        this.supportedAuthMethod = supportedAuthMethod;
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
    public ResponseEntity<Void> unregister(String authorization,Principal principal)
            throws OAuth2BearerTokenUsageMissingAuthInfoException, OAuth2InvalidClientException,
            OAuth2BearerTokenUsageInvalidTokenException {
        log.debug("unregister() called with no clientId");
        return unregister(null, authorization, principal);
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
    public ResponseEntity<Void> unregister(String clientId, String authorization, Principal principal)
            throws OAuth2BearerTokenUsageMissingAuthInfoException, OAuth2InvalidClientException,
            OAuth2BearerTokenUsageInvalidTokenException {
        log.debug("unregister() called for clientId; '{}'", clientId);
        checkAuthArgs(principal, authorization);
        Tpp tpp = getTpp(principal);

        verifyTppClientIDMatchesRequestClientId(tpp, clientId);
        String accessToken = verifyRegistrationAccessToken(tpp, authorization);

        tppRegistrationService.unregisterTpp(accessToken, tpp);
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
    public ResponseEntity<OIDCRegistrationResponse> getRegisterResult( String authorization, Principal principal)
            throws OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException,
            OAuth2BearerTokenUsageMissingAuthInfoException {

        return getRegisterResult(null, authorization, principal);
    }

    /**
     * Implementation of the GET /register endpoint
     * @param clientId      the id of the client registration resource to be returned.
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
    public ResponseEntity<OIDCRegistrationResponse> getRegisterResult( String clientId, String authorization,
                                                                       Principal principal)
            throws OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException,
            OAuth2BearerTokenUsageMissingAuthInfoException {

        log.debug("getRegisterResultCalled for clientId {}, principal is {}", clientId, principal);
        checkAuthArgs(principal, authorization);
        Tpp tpp = getTpp(principal);
        verifyTppClientIDMatchesRequestClientId(tpp, clientId);
        String accessToken = verifyRegistrationAccessToken(tpp, authorization);

        return ResponseEntity.ok(tppRegistrationService.getOIDCClient(accessToken, tpp));
    }

    @Override
    public ResponseEntity<OIDCRegistrationResponse> updateClient(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "A request to register a Software Statement Assertion with an ASPSP"  )
            @Valid
            @RequestBody String registrationRequestJwtSerialised,

            Principal principal
    ) throws OBErrorException, OIDCException, OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException, OAuth2BearerTokenUsageMissingAuthInfoException {
        return updateClient(null, authorization, registrationRequestJwtSerialised, principal);
    }

    @Override
    public ResponseEntity<OIDCRegistrationResponse> updateClient(
            @ApiParam(value = "The client ID",required=false)
            @PathVariable("ClientId") String clientId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "A request to register a Software Statement Assertion with an ASPSP"  )
            @Valid
            @RequestBody String registrationRequestJwtSerialised,

            Principal principal
    ) throws OBErrorException, OIDCException, OAuth2InvalidClientException, OAuth2BearerTokenUsageInvalidTokenException,
            OAuth2BearerTokenUsageMissingAuthInfoException {
        X509Authentication currentUser = (X509Authentication) principal;

        Tpp tpp = getTpp(principal);
        verifyTppClientIDMatchesRequestClientId(tpp, clientId);
        String accessToken = verifyRegistrationAccessToken(tpp, authorization);
        try {
            SignedJWT registrationRequestJws = SignedJWT.parse(registrationRequestJwtSerialised);
            String ssaSerialised = registrationRequestJws.getJWTClaimsSet()
                    .getStringClaim(RegistrationTppRequestClaims.SOFTWARE_STATEMENT);

            if(ssaSerialised == null){
                log.debug("No SSA provided in registationJWT");
                throw  new OBErrorException(OBRIErrorType.TPP_REGISTRATION_SSA_INVALID);
            }

            //Convert in json for convenience
            String registrationRequestJson =
                    JSONObjectUtils.toJSONString(registrationRequestJws.getJWTClaimsSet().toJSONObject());

            OIDCRegistrationRequest oidcRegistrationRequest = objectMapper.readValue(
                    registrationRequestJson, OIDCRegistrationRequest.class);

            //Override client ID
            oidcRegistrationRequest.setClientId(clientId);

            log.debug("TPP request json payload {}", registrationRequestJson);

            String directoryId = tppRegistrationService.verifySSA(ssaSerialised);
            if (directoryId == null) {
                log.debug("None of the directories signed this SSA {}", ssaSerialised);
                throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_SSA_INVALID);
            }

            SignedJWT ssaJws = SignedJWT.parse(ssaSerialised);
            JWTClaimsSet ssaClaims = ssaJws.getJWTClaimsSet();
            JSONObject ssaJwsJson = new JSONObject(ssaClaims.toJSONObject());
            boolean isEidasCert = currentUser.getAuthorities().contains(OBRIRole.ROLE_EIDAS);
            log.debug("isEidasCert {}", isEidasCert);
            log.debug("SSA {}", ssaSerialised);
            log.debug("SSA json payload {}", ssaJwsJson.toJSONString());

            verifyRegistrationRequest(isEidasCert, registrationRequestJwtSerialised,
                    ssaClaims, oidcRegistrationRequest, tpp.getCertificateCn());

            Set<SoftwareStatementRole> types = tppRegistrationService.prepareRegistrationRequestWithSSA(ssaClaims, oidcRegistrationRequest, currentUser);

            tpp = tppRegistrationService.updateTpp(tpp, accessToken, registrationRequestJson, ssaClaims, ssaJwsJson, oidcRegistrationRequest,
                    directoryId, types);

            return ResponseEntity.status(HttpStatus.OK).body(tpp.getRegistrationResponse());
        } catch (HttpClientErrorException e) {
            log.error("An error happened in the AS '{}'", e.getResponseBodyAsString(), e);
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_OIDC_CLIENT_REGISTRATION_ISSUE, e.getMessage());
        } catch (ParseException | IOException e) {
            log.error("Couldn't parse registration request", e);
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_REQUEST_INVALID_FORMAT);
        }
    }


    @Override
    public ResponseEntity<OIDCRegistrationResponse> register(
            @ApiParam(value = "A request to register a Software Statement Assertion with an ASPSP"  )
            @Valid
            @RequestBody String registrationRequestJwtSerialised,

            Principal principal
    ) throws OBErrorResponseException, OIDCException {
        log.debug("register TPP: request {}", registrationRequestJwtSerialised);

        try {
            X509Authentication authentication = (X509Authentication) principal;
            User currentUser = (User) authentication.getPrincipal();
            log.debug("User detail: username {} and authorities {}", currentUser.getUsername(), currentUser.getAuthorities());
            if (currentUser.getAuthorities().contains(OBRIRole.ROLE_AISP)
                    || currentUser.getAuthorities().contains(OBRIRole.ROLE_PISP)
                    || currentUser.getAuthorities().contains(OBRIRole.ROLE_CBPII)) {
                throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_ALREADY_REGISTERED,
                        currentUser.getUsername()
                );
            }

            if (currentUser.getAuthorities().contains(OBRIRole.UNKNOWN_CERTIFICATE)) {
                throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_UNKNOWN_TRANSPORT_CERTIFICATE,
                        currentUser.getUsername()
                );
            }

            if (currentUser.getAuthorities().contains(OBRIRole.UNREGISTERED_TPP)) {
                try {
                    SignedJWT registrationRequestJws = SignedJWT.parse(registrationRequestJwtSerialised);
                    String ssaSerialised = registrationRequestJws.getJWTClaimsSet()
                            .getStringClaim(RegistrationTppRequestClaims.SOFTWARE_STATEMENT);

                    if(ssaSerialised == null){
                        log.debug("No SSA provided in registationJWT");
                        throw  new OBErrorException(OBRIErrorType.TPP_REGISTRATION_SSA_INVALID);
                    }

                    //Convert in json for convenience
                    String registrationRequestJson =
                            JSONObjectUtils.toJSONString(registrationRequestJws.getJWTClaimsSet().toJSONObject());
                    OIDCRegistrationRequest oidcRegistrationRequest = objectMapper.readValue(
                            registrationRequestJson, OIDCRegistrationRequest.class);

                    String directoryId = tppRegistrationService.verifySSA(ssaSerialised);
                    if (directoryId == null) {
                        log.debug("None of the directories signed this SSA {}", ssaSerialised);
                        throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_SSA_INVALID);
                    } else {
                        log.debug("SSA is valid and issued by {}", directoryId);
                    }

                    SignedJWT ssaJws = SignedJWT.parse(ssaSerialised);
                    JWTClaimsSet ssaClaims = ssaJws.getJWTClaimsSet();
                    JSONObject ssaJwsJson = new JSONObject(ssaClaims.toJSONObject());
                    //delete client ID
                    oidcRegistrationRequest.setClientId(null);
                    boolean isEidasCert = currentUser.getAuthorities().contains(OBRIRole.ROLE_EIDAS);
                    log.debug("isEidasCert {}", isEidasCert);
                    log.debug("TPP request json payload {}", registrationRequestJson);
                    log.debug("SSA {}", ssaSerialised);
                    log.debug("SSA json payload {}", ssaJwsJson.toJSONString());

                    verifyRegistrationRequest(isEidasCert, registrationRequestJwtSerialised,
                            ssaClaims, oidcRegistrationRequest, getCn(authentication.getCertificateChain()[0]));

                    Set<SoftwareStatementRole> types = tppRegistrationService.prepareRegistrationRequestWithSSA(ssaClaims, oidcRegistrationRequest, authentication);

                    Tpp tpp = tppRegistrationService.registerTpp(getCn(authentication.getCertificateChain()[0]), registrationRequestJson, ssaClaims, ssaJwsJson, oidcRegistrationRequest, directoryId, types);

                    return ResponseEntity.status(HttpStatus.CREATED).body(tpp.getRegistrationResponse());
                } catch (HttpClientErrorException e) {
                    log.error("An error happened in the AS '{}'", e.getResponseBodyAsString(), e);
                    throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_OIDC_CLIENT_REGISTRATION_ISSUE, e.getMessage());
                } catch (ParseException | IOException e) {
                    log.error("Couldn't parse registration request", e);
                    throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_REQUEST_INVALID_FORMAT);
                }
            }
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_UNKNOWN_TRANSPORT_CERTIFICATE,
                    currentUser.getUsername()
            );
        } catch (OBErrorException e) {
            throw new OBErrorResponseException(
                    e.getObriErrorType().getHttpStatus(),
                    OBRIErrorResponseCategory.TPP_REGISTRATION,
                    e.getOBError());
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
     * @throws OAuth2BearerTokenUsageMissingAuthInfoException
     */
    private void checkAuthArgs(Principal principal, String authorization)
            throws OAuth2BearerTokenUsageMissingAuthInfoException, OAuth2InvalidClientException {
        if(getPrincipalName(principal).equalsIgnoreCase("anonymous")){
            throw new OAuth2InvalidClientException("Mutual TLS failed - no principal found from request. This " +
                    "endpoint must be called using SSL using an OBWac certificate which will be used by the ASPSP for" +
                    " Mutual Authentication TLS");
        }
        if(StringUtils.isEmpty(authorization)){
            throw new OAuth2BearerTokenUsageMissingAuthInfoException("No valid Bearer authorization header. " +
                    "This should be set to the registration_access_token found in the response when you successfully " +
                    "used the /register endpoint to perform dynamic client registration.");
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



    private void verifyRegistrationRequest(
            boolean isEidasCert,
            String registrationRequestJwtSerialised,
            JWTClaimsSet ssaClaims,
            OIDCRegistrationRequest oidcRegistrationRequest,
            String cn
    ) throws OBErrorException, ParseException, OIDCException {
        log.trace("{}:verifyRegistrationRequest()", this.getClass().getSimpleName());
        //Verify request
        String softwareId = ssaClaims.getStringClaim(SSAClaims.SOFTWARE_ID);

        // eIDAS certificates only identify the TPP, NOT the Software Statement. So if we have an eIDAS certificate we
        // won't find the softwareId anywhere in the certificate DN.
        if(!isEidasCert) {
            tppRegistrationService.verifySSASoftwareIDAgainstTransportCert(softwareId, cn);
        }

        String softwareClientId = ssaClaims.getStringClaim(SSAClaims.SOFTWARE_CLIENT_ID);
        tppRegistrationService.verifyTPPRegistrationRequestSignature(registrationRequestJwtSerialised, softwareClientId,
                ssaClaims);
        tppRegistrationService.verifyTPPRegistrationRequestAgainstSSA(oidcRegistrationRequest, ssaClaims);
        verifyAuthenticationMethodSupported(oidcRegistrationRequest);
        log.trace("{}:verifyRegistrationRequest() registration request is valid", this.getClass().getSimpleName());
    }

    /**
     * verifyTppClientIDMatchesRequestClientId() tests to see if the tpp's clientId matches a specific client Id.
     * The tpp is identified from the Principal of the request, which is derived from a certificate (MATLS). This
     * method can test if the clientID for which the registration request is being made (taken from the request URL
     * path for example) is the same clientId issued when the Tpp onboarded.
     * @param tpp - The tpp that made the request as identified by MATLS, cookie etc.
     * @param oidcClientIdFromRequest the OIDC clientId specified in the request (e.g. as a URI path element)
     * @throws OBErrorResponseException - if the oidcClientIDFromRequest does not match the client Id registered when
     * the TPP onboarded.
     */
    private void verifyTppClientIDMatchesRequestClientId(Tpp tpp, String oidcClientIdFromRequest)
            throws OAuth2InvalidClientException {
        log.debug("verifyTppClientIDMatchesRequestClientId() tpp is '{}', oidcClientIdFromRequest is '{}'",
                tpp==null?"tpp is null!":tpp.toString(),oidcClientIdFromRequest);


        if (oidcClientIdFromRequest == null || tpp == null || !tpp.getClientId().equals(oidcClientIdFromRequest)) {
            log.info("verifyTppClientIDMatchesRequestClientId() the tpp's clientId '{}' does not match the "
                            + "oidcClientIdFromRequest '{}'", tpp == null?"tpp is null":tpp.getClientId(),
                            oidcClientIdFromRequest);
            throw new OAuth2InvalidClientException("ClientId specified in the request url was {}. The ClientId " +
                    "of the Tpp associated with your MATLS certificate used in this call was {}. Please use a " +
                    "certificate associated with the Tpp that performed the registration.");
        }
        log.debug("verifyTppClientIDMatchesRequestClientId() - clientIds match");
    }

    private void verifyAuthenticationMethodSupported(OIDCRegistrationRequest oidcRegistrationRequest)
            throws OBErrorException {
        if (!supportedAuthMethod.contains(oidcRegistrationRequest.getTokenEndpointAuthMethod())) {
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_INVALID_AUTH_METHOD,
                    oidcRegistrationRequest.getTokenEndpointAuthMethod()
            );
        }
    }

    /**
     * getTpp returns the tpp associated with the principal. If not tpp can be found will throw.
     * @param principal the principal as obtained from the client MATLS certificate used to make the request
     * @return a Tpp object belonging to the principal. If no tpp can be found then will throw
     * OAuth2InvalidClientException
     * @throws OAuth2InvalidClientException
     */
    private Tpp getTpp( Principal principal) throws OAuth2InvalidClientException {
        //Todo: this next line looks odd. findByClientId but passing the principal.getName which I would think would
        // return the name pulled from the MATLS certificate?
        Optional<Tpp> optionalTpp = tppStoreService.findByClientId(principal.getName());
        if (!optionalTpp.isPresent()) {
            log.info("getTpp() no tpp found for principal '{}'", principal==null?"Principal is null":
                    principal.getName());
            throw new OAuth2InvalidClientException("No tpp associated with the MATLS certificate used in the request." +
                    " The certificate is associated with OrganisationId " + principal.getName() +". This Tpp is not " +
                    "currently onboarded");
        }
        return optionalTpp.get();
    }

    /**
     * The registration_access_token was provided to the client in response to a successful registration request.
     * @param tpp - the tpp associated with the MATLS client certificate used to make the request.
     * @param authorization - the Authorization header value from the request
     * @return the access token if valid
     * @throws OBErrorResponseException
     */
    private String verifyRegistrationAccessToken(@NotNull Tpp tpp, String authorization)
            throws OAuth2BearerTokenUsageMissingAuthInfoException,OAuth2BearerTokenUsageInvalidTokenException {
        log.debug("verifyRegistrationAccessToken() tpp is '{}', authorization is '{}'", tpp==null?"null":tpp.toString(),
                authorization);
        // Told you tpp must not be null!
        Objects.requireNonNull(tpp);

        String accessToken = getAccessToken(authorization);
        validateBearerTokenBelongsToTpp(accessToken, tpp);

        log.debug("verifyRegistrationAccessToken() Tpp '{}' has valid accessToken.", tpp.getClientId());
        return accessToken;
    }


    private String getAccessToken(String authorization) throws OAuth2BearerTokenUsageMissingAuthInfoException,
            OAuth2BearerTokenUsageInvalidTokenException {
        String accessToken = null;
        if(StringUtils.isEmpty(authorization)){
            throw new OAuth2BearerTokenUsageMissingAuthInfoException("AuthorizationHeader is empty. It should " +
                    "contain a Bearer OAuth2 token. See rfc-6750");
        } else {
            try {
                accessToken = tokenExtractor.extract(authorization);
            } catch (AuthenticationServiceException re) {
                String errorMessage = String.format("Failed to extract Bearer token from authorization header: " +
                                "'%s'. Error was %s. Authorization header should contain an OAuth2 Bearer token. " +
                                "See rfc-6750", authorization, re.getMessage());
                log.debug("verifyRegistrationAccessToken() {}}", errorMessage);
                throw new OAuth2BearerTokenUsageInvalidTokenException(errorMessage);
            }
        }
        return accessToken;
    }

    private void validateBearerTokenBelongsToTpp(String accessToken, @NotNull Tpp tpp)
            throws OAuth2BearerTokenUsageInvalidTokenException {
        // Told you tpp must not be null!
        Objects.requireNonNull(tpp);

        boolean accessTokenBelongsToTpp = false;
        Optional<String> registrationAccessToken = tpp.getRegistrationAccessToken();
        if(registrationAccessToken.isPresent()) {
            accessTokenBelongsToTpp = registrationAccessToken.get().equals(accessToken);
        }
        if(!accessTokenBelongsToTpp){
            String errorMessage = "Authorization Bearer token is not the bearer token issued to the TPP " +
                    "identified by the MATLS client certificate used to make this request. Please use the " +
                    "registration_access_token that was provided in your successful request to the POST /register" +
                    " endpoint. i.e. When you succesfully performed dynamic client registration.";
            log.debug("verifyRegistrationAccessToken() {}", errorMessage);
            throw new OAuth2BearerTokenUsageInvalidTokenException(errorMessage);
        }
    }
}
