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
package com.forgerock.openbanking.aspsp.as.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.services.AMOIDCRegistrationService;
import com.forgerock.openbanking.analytics.model.entries.TppEntry;
import com.forgerock.openbanking.analytics.services.TppEntriesKPIService;
import com.forgerock.openbanking.aspsp.as.configuration.ForgeRockDirectoryConfiguration;
import com.forgerock.openbanking.aspsp.as.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientIdentity;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.RegistrationRequest;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants.TokenEndpointAuthMethods;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.DirectorySoftwareStatement;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Service
@Slf4j
public class TppRegistrationService {

    private final CryptoApiClient cryptoApiClient;
    private final OpenBankingDirectoryConfiguration openBankingDirectoryConfiguration;
    private final ForgeRockDirectoryConfiguration forgeRockDirectoryConfiguration;
    private final TppStoreService tppStoreService;
    private final AMOIDCRegistrationService amoidcRegistrationService;
    private final TppEntriesKPIService tppEntriesKPIService;

    @Autowired
    public TppRegistrationService(CryptoApiClient cryptoApiClient,
                                  OpenBankingDirectoryConfiguration openBankingDirectoryConfiguration,
                                  ForgeRockDirectoryConfiguration forgeRockDirectoryConfiguration,
                                  TppStoreService tppStoreService,
                                  AMOIDCRegistrationService amoidcRegistrationService,
                                  TppEntriesKPIService tppEntriesKPIService) {
        this.cryptoApiClient = cryptoApiClient;
        this.openBankingDirectoryConfiguration = openBankingDirectoryConfiguration;
        this.forgeRockDirectoryConfiguration = forgeRockDirectoryConfiguration;
        this.tppStoreService = tppStoreService;
        this.amoidcRegistrationService = amoidcRegistrationService;
        this.tppEntriesKPIService = tppEntriesKPIService;
    }

    public String validateSsaAgainstIssuingDirectoryJwksUri(String ssaSerialised, String issuer)
            throws DynamicClientRegistrationException {
        log.debug("validateSsaAgainstIssuingDirectoryJwksUri(): issuer is {}", issuer);
        if (ssaSerialised == null) {
            return null;
        }

        if(issuer.equals(openBankingDirectoryConfiguration.issuerId)) {
            try {
                log.debug("validateSsaAgainstIssuingDirectoryJwksUri() Verify the SSA against OB directory");
                cryptoApiClient.validateJws(ssaSerialised, openBankingDirectoryConfiguration.getIssuerID(),
                        openBankingDirectoryConfiguration.jwksUri);
                return openBankingDirectoryConfiguration.id;
            } catch (InvalidTokenException | HttpClientErrorException | ParseException | IOException e) {
                log.debug("validateSsaAgainstIssuingDirectoryJwksUri() Invalid SSA signature from OB directory", e);
            }
        } else if(issuer.equals(forgeRockDirectoryConfiguration.getIssuerID())) {
            try {
                log.debug("validateSsaAgainstIssuingDirectoryJwksUri() Verify the SSA against ForgeRock directory");
                cryptoApiClient.validateJws(ssaSerialised, null,
                        forgeRockDirectoryConfiguration.jwksUri);
                return forgeRockDirectoryConfiguration.id;
            } catch (InvalidTokenException | ParseException | IOException e) {
                log.debug("validateSsaAgainstIssuingDirectoryJwksUri() Invalid SSA signature from ForgeRock directory", e);
            }
        } else {
            String errorMessage = "Unrecognised ssa. Issuer is '" + issuer + "'. Please use an Open Banking issued " +
                    "SSA.";
            log.debug("validateSsaAgainstIssuingDirectoryJwksUri() {}", errorMessage);
            throw new DynamicClientRegistrationException(errorMessage,
                    DynamicClientRegistrationErrorType.UNAPPROVED_SOFTWARE_STATEMENT);
        }

        return null;
    }

    public void verifySSASoftwareIDMatchesMatlsTransportCertSoftwareId(String softwareIdFromSSA, String softwareIdFromMatls)
            throws OAuth2InvalidClientException {
        log.debug("verifySSASoftwareIdAgainstTransportCert() Verify the MTLS certificate matches the SSA. Software ID" +
                " from the certificate {} " +
                "and from the SSA {}", softwareIdFromMatls, softwareIdFromSSA);
        if (!softwareIdFromMatls.equals(softwareIdFromSSA)) {
            log.info("SSA software ID '{}' doesn't match the Certificate CN '{}'", softwareIdFromSSA,
                    softwareIdFromMatls);
            log.debug("verifySSASoftwareIdAgainstTransportCert() verification failed");
            throw new OAuth2InvalidClientException("Legacy OBTransport certificate used did not refer to the same " +
                    "softwareId as the SSA provided.");
        }
        log.trace("{}:verifySSASoftwareIdAgainstTransportCert() verification successful",
                this.getClass().getSimpleName());
    }

    /**
     * Verifies the registration request jwt against the jwks_uri and checks that the ssaSoftwareClient id matches
     * the software_client_id claim in the software statement provided in the registration request is the same as the
     * issuer of the registration request jwt.
     * @param registrationRequestJwsSerialised - the serialised registration request jwt as received in the request
     * @param ssaSoftwareClientId - the software_client_id claim from the software statement in the registration request
     * @param jwks_uri - the jwks_uri claim taken from the software statement in the registration request
     * @throws DynamicClientRegistrationException Thrown if the registration request can't be validated
     */
    public void verifyTPPRegistrationRequestSignature(String registrationRequestJwsSerialised, String ssaSoftwareClientId,
                                                      String jwks_uri)
            throws DynamicClientRegistrationException {

        String methodName = "verifyTPPRegistrationRequestSignature()";
        try {
            log.debug("{} validating registration request JWT issued by '{}' against jwks_uri; '{}'",
                    methodName, ssaSoftwareClientId, jwks_uri);
            cryptoApiClient.validateJws(registrationRequestJwsSerialised, ssaSoftwareClientId, jwks_uri);
            log.info("{} Registration request JWT signature is valid, issuer & ssa's client Id is '{}', jwks_uri; '{}'",
                    methodName, ssaSoftwareClientId, jwks_uri);
        } catch (InvalidTokenException | ParseException | IOException e) {
            String errorMessage = "Invalid TPP registration request JWT. Failed to verify signature of Registration " +
                    "Request with SSA for software client Id of " + ssaSoftwareClientId + " against jwks_uri '" +
                    jwks_uri +"'. Error; " + e.getMessage();
            log.info("{}; {}", methodName, errorMessage, e);
            throw new DynamicClientRegistrationException(errorMessage,
                    DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT);
        }
    }

    /**
     * There are conditions in the Open Banking DCR 3.3 specifications that need checking. See
     * <a href=https://openbankinguk.github.io/dcr-docs-pub/v3.3/dynamic-client-registration.html>Open Banking Dynamic
     * Client Registration v3.3</a> for the specifications
     * @param registrationRequest the registration request object against which the tests should be made.
     * @throws DynamicClientRegistrationException if the request does not meet with the specifications
     */
    public void verifyTPPRegistrationRequestAgainstSSA(RegistrationRequest registrationRequest)
            throws DynamicClientRegistrationException {

        log.debug("verifyTPPRegistrationRequestAgainstSSA() Verify TPP registration request matches the SSA request");
        log.debug("verifyTPPRegistrationRequestAgainstSSA() Verify the software id");

        // The OB Dynamic Client Registration spec says that;
        // If specified, the software_id in the request MUST match the software_id specified in the SSA. ASPSPs can
        // choose to allow multiple registrations for a given software statement. The Software ID must be represented
        // as a Base62 UUID.
        // The cardinality of software_id in the registration request spec os 0..1 meaning this can be null and that's
        // OK.
        String registrationRequestSoftwareId = registrationRequest.getSoftwareId();

        if (StringUtils.isNotBlank(registrationRequestSoftwareId)){
            // Throws if no claims available. ssaClaimSetSoftwareId will not be null or empty if it returns

            String ssaClaimSetSoftwareId = registrationRequest.getSoftwareIdFromSSA();
            if(!registrationRequestSoftwareId.equals(ssaClaimSetSoftwareId)){
                String errorDescription = "The software_id in the registration request differs from that in the " +
                        "Software Statement";
                log.debug("verifyTPPRegistrationRequestAgainstSSA() " + errorDescription + ". ssaClaimSetSoftwareId: " +
                                "'{}', registrationRequestSoftwareId: '{}'", ssaClaimSetSoftwareId,
                        registrationRequestSoftwareId);
                throw new DynamicClientRegistrationException(errorDescription,
                        DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
            }
        }

        // ToDo: Address issue https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/17
        // It would seem that when the SSA's used by the api-tests in the functional test suite are turned into a
        // registration request JWT, and passed to the AS via the registration API, and is then decoded the SSA no
        // longer seems to contain valid entries for the SSA's registration URIs. This means that we can't perform
        // the required checks on the redirect URIs. See the following issue. This issue has existed for a long time
        // I would guess and was hinted at in the previous version of this method, where there was a comment stating
        // that 'we did not check the redirect uri for now 'due to a directory bug'. It's unlikely that this is due
        // to a directory bug however, as SSAs from both FR and OB Directory seem to result in parsed SSAs with no
        // valid redirect URIs in.
        //
        // So when the following issue is fixed we can re-instate the redirect uri checks below and remove the @Ignore
        // from the TppRegistrationServiceTests.
        //
        // Also getSsaStringListClaim need to be fixed to throw if the SSA redirect URI list is empty.
        boolean workaroundForOBDirectoryIssue = true;
        log.debug("verifyTPPRegistrationRequestAgainstSSA() Verify the redirect uri");

        List<String> registrationRequestRedirectUris = registrationRequest.getRedirectUris();

        // Throws if no claims available. ssaClaimRedirectUris will not be null or empty if it returns
        Optional<List<String>> ssaClaimRedirectUris = registrationRequest.getRedirectUrisFromSSA();
        if (registrationRequestRedirectUris == null || registrationRequestRedirectUris.isEmpty() ) {
            if(!workaroundForOBDirectoryIssue){
                String errorMessage = "No redirect_uri claims available from the Client Registration";
                log.debug("verifyTPPRegistrationRequestAgainstSSA() {}", errorMessage);
                throw new DynamicClientRegistrationException(errorMessage,
                        DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
            } else {
                log.debug("verifyTPPRegistrationRequestAgainstSSA() The redirect uris were not set in the " +
                        "registration request. Setting them to be the same as those set in the ssa.");
                if(ssaClaimRedirectUris.isPresent()) {
                    registrationRequest.setRedirectUris(ssaClaimRedirectUris.get());
                }
                /*else {
                    String errorMessage = "The Software Statement contains no redirect_uris";
                    log.info("verifyTPPRegistrationRequestAgainstSSA() {}", errorMessage);
                    throw new DynamicClientRegistrationException(errorMessage,
                            DynamicClientRegistrationErrorType.INVALID_REDIRECT_URI);
                };*/
            }
        } else if (ssaClaimRedirectUris.isPresent() && ssaClaimRedirectUris.get().containsAll(registrationRequestRedirectUris)) {
            log.warn("Redirect Uri in the request doesn't match the redirect URI in the SSA");
            //TODO due to OB directory bug, we won't check redirect uri for now
            if(!workaroundForOBDirectoryIssue) {
                String errorDescription = "The redirect URI's in the registration request must be a subset of the " +
                        "URI's in the Software Statement";
                log.info("verifyTPPRegistrationRequestAgainstSSA() {}", errorDescription);
                throw new DynamicClientRegistrationException(errorDescription,
                        DynamicClientRegistrationErrorType.INVALID_REDIRECT_URI);
            }
        }
    }


    public Tpp registerTpp(ApiClientIdentity clientIdentity, RegistrationRequest oidcRegistrationRequest)
            throws DynamicClientRegistrationException {
        
        Optional<DirectorySoftwareStatement> jti = tppStoreService.findByAuthorisationNumber(clientIdentity.getUsername())
            .stream()
            .map(Tpp::getDirectorySoftwareStatement)
            .filter(ssa -> ssa.getJti().equals(oidcRegistrationRequest.getJti()))
            .findAny();
        
        if (jti.isPresent()) {
            log.info("registerTpp() this tpp has already registered with an identical SSA. " +
              "You can only onboard with a single software statement if you generate a new SSA");
            return null;
        }
            

        log.debug("registerTpp() Send the OAuth2 dynamic registration request to the AS");
        OIDCRegistrationResponse oidcRegistrationResponse = amoidcRegistrationService.register(oidcRegistrationRequest);
        log.debug("registerTpp() Response from the AS: {}", oidcRegistrationResponse);

        String cn = clientIdentity.getTransportCertificateCn();
        // ToDo: Previously this came from the spring config and was either set to the value found in either
        // openBankingDirectoryConfiguration.getIssuerID() or forgeRockDirectoryConfiguration.id()

        String ssaIssuer = oidcRegistrationRequest.getSsaIssuer();
        String directoryId = getDirectoryIdFromSsaIssuer(ssaIssuer);

        removeSecretIfNeeded(oidcRegistrationResponse);

        String officialName = getOrgSoftwareCombinedTppName(oidcRegistrationRequest, oidcRegistrationResponse);

        // ToDo: Is this just the same as the SoftwareStatement

        Tpp tpp = Tpp.builder()
                .id(oidcRegistrationResponse.getClientId())
                .created(new Date())
                .certificateCn(cn)
                .name(oidcRegistrationResponse.getClientName())
                .officialName(officialName)
                .clientId(oidcRegistrationResponse.getClientId())
                .types(oidcRegistrationRequest.getSoftwareStatementRoles())
                .softwareId(oidcRegistrationRequest.getDirectorySoftwareStatement().getSoftware_id())
                .authorisationNumber(clientIdentity.getAuthorisationNumber().orElse(null))
                .directorySoftwareStatement(oidcRegistrationRequest.getDirectorySoftwareStatement())
                .tppRequest(oidcRegistrationRequest.toJson())
                .registrationResponse(oidcRegistrationResponse)
                .directoryId(directoryId)
                .build();

        updateTppMetrics(tpp, false);

        return tppStoreService.createTpp(tpp);
    }

    private String getDirectoryIdFromSsaIssuer(String ssaIssuer) throws DynamicClientRegistrationException {
        String directoryId;
        if (ssaIssuer.equals(openBankingDirectoryConfiguration.issuerId)) {
            directoryId = openBankingDirectoryConfiguration.id;
        }
        else if (ssaIssuer.equals(forgeRockDirectoryConfiguration.getIssuerID())){
            directoryId = forgeRockDirectoryConfiguration.id;
        } else {
            String errorString = "Invalid SSA issuer. Issuer " + ssaIssuer + " does not match a trusted issuer";
            log.debug("registerTpp() {}", errorString);
            throw new DynamicClientRegistrationException(errorString,
                    DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT);
        }
        return directoryId;
    }

    public Tpp updateTpp(ApiClientIdentity clientIdentity, Tpp tpp, String token, RegistrationRequest oidcRegistrationRequest)
            throws DynamicClientRegistrationException {
        log.debug("updateTpp() Updating tpp '{}'", tpp.getClientId());
        log.debug("updateTpp() Sending the OAuth2 dynamic registration request to AM");
        OIDCRegistrationResponse oidcRegistrationResponse = amoidcRegistrationService.updateOIDCClient(token,
                oidcRegistrationRequest, tpp.getClientId());
        log.debug("updateTpp() Response from AM: {}", oidcRegistrationResponse);

        String ssaIssuer = oidcRegistrationRequest.getSsaIssuer();
        String directoryId = this.getDirectoryIdFromSsaIssuer(ssaIssuer);

        removeSecretIfNeeded(oidcRegistrationResponse);

        String officialName = getOrgSoftwareCombinedTppName(oidcRegistrationRequest, oidcRegistrationResponse);

        Tpp updatedTpp = Tpp.builder()
                .created(tpp.getCreated())
                .id(tpp.getId())
                .certificateCn(tpp.getCertificateCn())
                .name(oidcRegistrationResponse.getClientName())
                .officialName(officialName)
                .clientId(oidcRegistrationResponse.getClientId())
                .types(oidcRegistrationRequest.getSoftwareStatementRoles())
                .softwareId(oidcRegistrationRequest.getDirectorySoftwareStatement().getSoftware_id())
                .authorisationNumber(clientIdentity.getAuthorisationNumber().orElse(null))
                .directorySoftwareStatement(oidcRegistrationRequest.getDirectorySoftwareStatement())
                .tppRequest(oidcRegistrationRequest.toJson())
                .registrationResponse(oidcRegistrationResponse)
                .directoryId(directoryId)
                .build();

        updateTppMetrics(tpp, false);

        return tppStoreService.save(updatedTpp);
    }


    private void updateTppMetrics(Tpp tpp, boolean isDeleted) {
        log.debug("updateTppMetrics() creating metrics for Tpp {}. Is being {}", tpp.getClientId(), isDeleted?
                "deleted":"created");
        TppEntry.TppEntryBuilder tppEntryBuilder = TppEntry.builder()
                .created(new DateTime(tpp.getCreated()))
                .deleted(isDeleted ? DateTime.now() : null)
                .directoryId(tpp.getDirectoryId())
                .oidcClientId(tpp.getClientId())
                .logoUri(tpp.getLogo())
                .name(tpp.getName())
                .types(tpp.getTypes());

        try {
            DirectorySoftwareStatement ssaClaim = tpp.getDirectorySoftwareStatement();
            tppEntryBuilder.softwareId(ssaClaim.getSoftware_id())
                    .organisationId(ssaClaim.getOrg_id())
                    .organisationName(ssaClaim.getOrg_name());
        } catch (NullPointerException e) {
            log.warn("Couldn't read TPP SSA, skipping SSA claims population to TPP entry for this TPP {}", tpp, e);
        }


        tppEntriesKPIService.pushTppEntry(tppEntryBuilder.build());
    }

    private void removeSecretIfNeeded(OIDCRegistrationResponse oidcRegistrationResponse) {
        log.debug("removeSecretIfNeeded() Remove client secret to response to workaround AM bug");
        String tokenEndpointAuthMethod = oidcRegistrationResponse.getTokenEndpointAuthMethod();
        TokenEndpointAuthMethods tokenEndpointAuthMethods = TokenEndpointAuthMethods.fromType(tokenEndpointAuthMethod);
        switch (tokenEndpointAuthMethods) {
            case CLIENT_SECRET_POST:
            case CLIENT_SECRET_BASIC:
                break;
            case CLIENT_SECRET_JWT:
            case TLS_CLIENT_AUTH:
            case PRIVATE_KEY_JWT:
                oidcRegistrationResponse.setClientSecret(null);
                oidcRegistrationResponse.setClientSecretExpiresAt(null);
                break;
        }
    }

    private String getOrgSoftwareCombinedTppName(RegistrationRequest registrationRequest,
                                                 OIDCRegistrationResponse registrationResponse){
        String organisationName = registrationRequest.getOrganisationName();
        String officialName = organisationName + " - " + registrationResponse.getClientName();
        log.debug("getOrgSoftwareCombinedTppName() returning official name {}", officialName);
        return officialName;
    }

    public void deleteOAuth2RegistrationAndTppRecord(Tpp tpp) {
        String methodName = "deleteOAuth2RegistrationAndTppRecord()";
        String clientId = tpp.getClientId();
        log.debug("{}; called for OIDC Registration client Id {}", methodName, clientId);
        Optional<String> accessTokenOpt = tpp.getRegistrationAccessToken();
        accessTokenOpt.ifPresent(accessToken -> deleteOAuth2ClientFromAm(accessToken, tpp));
        log.debug("{} Deleting {} from rs store", methodName, clientId);
        tppStoreService.deleteTPP(tpp);
    }

    private void deleteOAuth2ClientFromAm(String token, Tpp tpp){
        log.debug("deleteOAuth2ClientFromAm() Deleting {} from AM", tpp.getClientId());
        try {
            amoidcRegistrationService.deleteOIDCClient(token, tpp.getClientId());
            log.info("deleteOAuth2ClientFromAm() deleted OAuth2 client Id '{}' from AM", tpp.getClientId());
            updateTppMetrics(tpp, true);
        } catch (Exception e){
            log.warn("deleteOAuth2ClientFromAm() - Failed to delete OIDCClient '{}' from AM. Error was ",
                tpp.getClientId(), e);
        }
    }

    public OIDCRegistrationResponse getOIDCClient(String token, Tpp tpp) {
        log.debug("Read TPP {} in AM", tpp.getClientId());
        return amoidcRegistrationService.getOIDCClient(token, tpp.getClientId());
    }
}
