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

import com.forgerock.openbanking.am.services.AMOIDCRegistrationService;
import com.forgerock.openbanking.analytics.model.entries.TppEntry;
import com.forgerock.openbanking.analytics.services.TppEntriesKPIService;
import com.forgerock.openbanking.aspsp.as.api.registration.dynamic.dto.RegistrationError;
import com.forgerock.openbanking.aspsp.as.configuration.ForgeRockDirectoryConfiguration;
import com.forgerock.openbanking.aspsp.as.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.utils.JwsClaimsUtils;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.SoftwareStatementRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationRequest;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWTClaimsSet;
import dev.openbanking4.spring.security.multiauth.model.authentication.X509Authentication;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.forgerock.openbanking.constants.OIDCConstants.TokenEndpointAuthMethods.TLS_CLIENT_AUTH;

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

    public String verifySSA(String ssaSerialised) {
        if (ssaSerialised == null) {
            return null;
        }
        try {
            log.debug("Verify the SSA against OB directory");
            cryptoApiClient.validateJws(ssaSerialised, openBankingDirectoryConfiguration.getIssuerID(),
                    openBankingDirectoryConfiguration.jwksUri);
            return openBankingDirectoryConfiguration.id;
        } catch (InvalidTokenException | HttpClientErrorException | ParseException | IOException e) {
            log.debug("Invalid SSA signature from OB directory", e);
        }

        try {
            log.debug("Verify the SSA against ForgeRock directory");
            cryptoApiClient.validateJws(ssaSerialised, null,
                    forgeRockDirectoryConfiguration.jwksUri);
            return forgeRockDirectoryConfiguration.id;
        } catch (InvalidTokenException | ParseException | IOException e) {
            log.debug("Invalid SSA signature from ForgeRock directory", e);
        }
        return null;
    }

    public String getCNFromSSA(String directoryId, JWTClaimsSet ssaClaims) throws ParseException {
        return ssaClaims.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_ID);
    }

    public void verifySSASoftwareIDAgainstTransportCert(String softwareIdFromSSA, String softwareIdFromMatls) throws OBErrorException {
        log.trace("{}:verifySSASoftwareIdAgainstTransportCert()", this.getClass().getSimpleName());
        log.debug("Verify the MTLS certificate matches the SSA. Software ID from the certificate {} " +
                "and from the SSA {}", softwareIdFromMatls, softwareIdFromSSA);
        if (!softwareIdFromMatls.equals(softwareIdFromSSA)) {
            log.error("SSA software ID '{}' doesn't match the Certificate CN '{}'", softwareIdFromSSA, softwareIdFromMatls);
            log.trace("{}:verifySSASoftwareIdAgainstTransportCert() verification failed", this.getClass().getSimpleName());
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_TRANSPORT_CERTIFICATE_NOT_MATCHING_SSA,
                    softwareIdFromSSA,
                    softwareIdFromMatls
            );
        }
        log.trace("{}:verifySSASoftwareIdAgainstTransportCert() verification successful",
                this.getClass().getSimpleName());
    }

    public void verifyTPPRegistrationRequestSignature(String registrationRequestJwtSerialised, String softwareClientId, JWTClaimsSet ssaClaims) throws OBErrorException, ParseException {
        try {
            log.debug("Validate the TPP registration request");
            String softwareJWKUri = ssaClaims.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_JWKS_ENDPOINT);
            if (softwareJWKUri != null) {
                cryptoApiClient.validateJws(registrationRequestJwtSerialised, softwareClientId, softwareJWKUri);
                return;
            }
            String jwk = ssaClaims.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_SIGINING_JWK);
            if (jwk != null) {
                cryptoApiClient.validateJwsWithJWK(registrationRequestJwtSerialised, softwareClientId, jwk);
                return;
            }
            log.error("SSA should have JWK_URI or JWK. SSA {}", ssaClaims.toJSONObject());
            throw new OBErrorException(OBRIErrorType.SERVER_ERROR, "Couldn't verify registration JWT");
        } catch (InvalidTokenException | ParseException | IOException e) {
            log.error("Invalid TPP registration request token: " + e.getMessage(), e);
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_REQUEST_JWT_INVALID,
                    e.getMessage()
            );
        }
    }

    public void verifyTPPRegistrationRequestAgainstSSA(OIDCRegistrationRequest oidcRegistrationRequest,
                                                       JWTClaimsSet ssaClaims) throws OBErrorException, OIDCException {
        log.debug("Verify TPP registration request matches the SSA request");
        log.debug("- Verify the software id");

        // The OB Dynamic Client Registration spec says that;
        // If specified, the software_id in the request MUST match the software_id specified in the SSA. ASPSPs can
        // choose to allow multiple registrations for a given software statement. The Software ID must be represented
        // as a Base62 UUID.
        // The cardinality of software_id in the registration request spec os 0..1 meaning this can be null and that's
        // OK.
        String registrationRequestSoftwareId = oidcRegistrationRequest.getSoftwareId();

        String noSoftwareIdErrorDescription = "Failed to obtain redirect URIs from the software statement";
        // Throws if no claims available. ssaClaimSetSoftwareId will not be null or empty if it returns
        String ssaClaimSetSoftwareId = getSsaStringClaim(ssaClaims, OpenBankingConstants.SSAClaims.SOFTWARE_ID,
                noSoftwareIdErrorDescription);

        if (StringUtils.isNotBlank(registrationRequestSoftwareId)){
            if(!registrationRequestSoftwareId.equals(ssaClaimSetSoftwareId)){
                String errorDescription = "The software_id in the registration request differs from that in the " +
                        "Software Statement";
                log.debug(errorDescription + ". ssaClaimSetSoftwareId: '{}', registrationRequestSoftwareId: '{}'",
                        ssaClaimSetSoftwareId, registrationRequestSoftwareId);

                RegistrationError registrationError = new RegistrationError()
                        .error(RegistrationError.ErrorEnum.INVALID_CLIENT_METADATA)
                        .errorDescription(errorDescription);
                throw new OIDCException(registrationError);
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
        log.debug("- Verify the redirect uri");
        List<String> registrationRequestRedirectUris = oidcRegistrationRequest.getRedirectUris();
        String noRedirectUriErrorDescription = "Failed to obtain redirect URIs from the software statement";

        // Throws if no claims available. ssaClaimRedirectUris will not be null or empty if it returns
        List<String> ssaClaimRedirectUris = getSsaStringListClaim(ssaClaims,
                OpenBankingConstants.SSAClaims.SOFTWARE_REDIRECT_URIS, noRedirectUriErrorDescription);


        if (registrationRequestRedirectUris == null || registrationRequestRedirectUris.isEmpty() ) {
            if(!workaroundForOBDirectoryIssue){
                throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_REQUEST_JWT_INVALID,
                        "At least one redirect_uri must be specified");
            } else {
                log.debug("The redirect uris were not set in the registration request. Setting them to be the " +
                        "same as those set in the ssa.");
                oidcRegistrationRequest.setRedirectUris(ssaClaimRedirectUris);
            }
        }

        if (!ssaClaimRedirectUris.containsAll(registrationRequestRedirectUris)) {
            log.warn("Redirect Uri in the request doesn't match the redirect URI in the SSA");
            //TODO due to OB directory bug, we won't check redirect uri for now
            if(!workaroundForOBDirectoryIssue) {
                String errorDescription = "The redirect URI's in the registration request must be a subset of the " +
                        "URI's in the Software Statement";

                throw new OIDCException(new RegistrationError()
                        .error(RegistrationError.ErrorEnum.INVALID_REDIRECT_URI)
                        .errorDescription(errorDescription));
            }
        }
    }


    public List<String> parseContacts(JWTClaimsSet ssaClaims) {
        List<String> contacts = new ArrayList<>();
        JSONArray contactsJsonArray = (JSONArray) ssaClaims.getClaim(OpenBankingConstants.SSAClaims.ORG_CONTACTS);
        if (contactsJsonArray != null) {
            for (Object contactJson : contactsJsonArray) {
                JSONObject contactJsonObject = ((JSONObject) contactJson);
                StringBuilder contact = new StringBuilder();
                contact.append("email:").append(JwsClaimsUtils.getContactField(contactJsonObject, "email")).append(";");
                contact.append("name:").append(JwsClaimsUtils.getContactField(contactJsonObject, "name")).append(";");
                contact.append("phone:").append(JwsClaimsUtils.getContactField(contactJsonObject, "phone")).append(";");
                contact.append("type:").append(JwsClaimsUtils.getContactField(contactJsonObject, "type")).append(";");
                contacts.add(contact.toString());
            }
        }
        return contacts;
    }

    public Tpp registerTpp(String cn, String registrationRequestJson,
                           JWTClaimsSet ssaClaims, JSONObject ssaJwsJson,
                           OIDCRegistrationRequest oidcRegistrationRequest,
                           String directoryId, Set<SoftwareStatementRole> types) {
        log.debug("Send the OAuth2 dynamic registration request to the AS");
        OIDCRegistrationResponse oidcRegistrationResponse = amoidcRegistrationService.register(oidcRegistrationRequest);
        log.debug("Response from the AS: {}", oidcRegistrationResponse);

        removeSecretIfNeeded(oidcRegistrationResponse);

        String officialName = getOfficialTppName(ssaClaims, oidcRegistrationResponse);

        Tpp tpp = Tpp.builder()
                .id(oidcRegistrationResponse.getClientId())
                .created(new Date())
                .certificateCn(cn)
                .name(oidcRegistrationResponse.getClientName())
                .officialName(officialName)
                .clientId(oidcRegistrationResponse.getClientId())
                .types(types)
                .ssa(ssaJwsJson.toJSONString())
                .tppRequest(registrationRequestJson)
                .registrationResponse(oidcRegistrationResponse)
                .directoryId(directoryId)
                .build();

        pushTppEntry(tpp, false);

        return tppStoreService.createTpp(tpp);
    }

    public Tpp updateTpp(Tpp tpp, String token, String registrationRequestJson,
                         JWTClaimsSet ssaClaims, JSONObject ssaJwsJson,
                         OIDCRegistrationRequest oidcRegistrationRequest,
                         String directoryId, Set<SoftwareStatementRole> types) {
        log.debug("Send the OAuth2 dynamic registration request to the AS");
        OIDCRegistrationResponse oidcRegistrationResponse = amoidcRegistrationService.updateOIDCClient(token, oidcRegistrationRequest, tpp.getClientId());
        log.debug("Response from the AS: {}", oidcRegistrationResponse);

        removeSecretIfNeeded(oidcRegistrationResponse);

        String officialName = getOfficialTppName(ssaClaims, oidcRegistrationResponse);

        Tpp updatedTpp = Tpp.builder()
                .created(tpp.getCreated())
                .id(tpp.getId())
                .certificateCn(tpp.getCertificateCn())
                .name(oidcRegistrationResponse.getClientName())
                .officialName(officialName)
                .clientId(oidcRegistrationResponse.getClientId())
                .types(types)
                .ssa(ssaJwsJson.toJSONString())
                .tppRequest(registrationRequestJson)
                .registrationResponse(oidcRegistrationResponse)
                .directoryId(directoryId)
                .build();

        pushTppEntry(tpp, false);

        return tppStoreService.save(updatedTpp);
    }

    /**
     * getSsaStringClaim Obtains a string containing the value for a specific claim. Catches the
     * ParseExceptions thrown by nimbusds and instead throw an OIDCException.
     *
     * @param ssaClaims        @JWTClaimSet containing the claim to be retrieved
     * @param claimName        @String the name of the SSA claim to obtain from the ssaClaims
     * @param errorDescription @String an error description that will be part of the exception if it is not possible
     *                         to obtain the requested ssaClaim.
     * @return A non null or empty @String containing the ssa claim value
     * @throws OIDCException when the claim specified by the claimName does not exist, or is empty.
     */
    private String getSsaStringClaim(@NotNull JWTClaimsSet ssaClaims,
                                     @NotNull String claimName,
                                     @NotNull String errorDescription)
            throws OIDCException {

        RegistrationError registrationError = new RegistrationError()
                .error(RegistrationError.ErrorEnum.INVALID_SOFTWARE_STATEMENT)
                .errorDescription(errorDescription);
        try{
            String claimValue = ssaClaims.getStringClaim(claimName);
            if(StringUtils.isBlank(claimValue)){
                log.debug(errorDescription);
                throw new OIDCException(registrationError);
            }
            return claimValue;
        } catch (ParseException e){
            log.debug(errorDescription);
            throw new OIDCException(registrationError);
        }
    }

    /**
     * getSsaStringListClaim Obtains a list of string values containing the values for a specific claim. Catch the
     * ParseExceptions thrown by nimbusds and instead throw an OIDCException.
     *
     * @param ssaClaims        @JWTClaimSet containing the claim to be retrieved
     * @param claimName        @String the name of the SSA claim to obtain from the ssaClaims
     * @param errorDescription @String an error description that will be part of the exception if it is not possible
     *                         to obtain the requested ssaClaim.
     * @return A non null but potentially empty @List<String> containing the ssa claim values
     * @throws OIDCException when the claim specified by the claimName does not exist, or is empty.
     */
    public List<String> getSsaStringListClaim(@NotNull JWTClaimsSet ssaClaims,
                                               @NotNull String claimName,
                                               @NotNull String errorDescription)
            throws OIDCException {

        RegistrationError registrationError = new RegistrationError()
                .error(RegistrationError.ErrorEnum.INVALID_REDIRECT_URI)
                .errorDescription(errorDescription);
        try{
            List<String> claimValue = ssaClaims.getStringListClaim(claimName);
            // ToDo: we should throw when the claimValue is empty here. see issue;
            //  https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/17
            //  Remember to fix the javadoc above when adding back the empty check ;-)
            if(claimValue == null /* || claimValue.isEmpty() */){
                log.debug(errorDescription);
                throw new OIDCException(registrationError);
            }
            return claimValue;
        } catch (ParseException e){
            log.debug(errorDescription);
            throw new OIDCException(registrationError);
        }
    }

    private void pushTppEntry(Tpp tpp, boolean isDeleted) {
        TppEntry.TppEntryBuilder tppEntryBuilder = TppEntry.builder()
                .created(new DateTime(tpp.getCreated()))
                .deleted(isDeleted ? DateTime.now() : null)
                .directoryId(tpp.getDirectoryId())
                .oidcClientId(tpp.getClientId())
                .logoUri(tpp.getLogo())
                .name(tpp.getName())
                .types(tpp.getTypes());

        try {
            JWTClaimsSet ssaClaim = tpp.getSsaClaim();
            tppEntryBuilder.softwareId(ssaClaim.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_ID))
                    .organisationId(ssaClaim.getStringClaim(OpenBankingConstants.SSAClaims.ORG_ID))
                    .organisationName(ssaClaim.getStringClaim(OpenBankingConstants.SSAClaims.ORG_NAME));
        } catch (ParseException e) {
            log.warn("Couldn't read TPP SSA, skipping SSA claims population to TPP entry for this TPP {}", e, tpp);
        }


        tppEntriesKPIService.pushTppEntry(tppEntryBuilder.build());
    }

    private void removeSecretIfNeeded(OIDCRegistrationResponse oidcRegistrationResponse) {
        log.debug("Remove client secret to response to workaround AM bug");
        OIDCConstants.TokenEndpointAuthMethods tokenEndpointAuthMethods = OIDCConstants.TokenEndpointAuthMethods.fromType(oidcRegistrationResponse.getTokenEndpointAuthMethod());
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

    private String getOfficialTppName(JWTClaimsSet ssaClaims, OIDCRegistrationResponse oidcRegistrationResponse) {
        String officialName = oidcRegistrationResponse.getClientName();
        try {
            String organisationName = ssaClaims.getStringClaim(OpenBankingConstants.SSAClaims.ORG_NAME);
            officialName = organisationName + " - " + oidcRegistrationResponse.getClientName();
        } catch (ParseException e) {
            log.error("Couldn't parse SSA claims. Continue using the TPP name {} instead",
                    ssaClaims, officialName, e);
        }
        return officialName;
    }


    public Set<SoftwareStatementRole> prepareRegistrationRequestWithSSA(
            JWTClaimsSet ssaClaims,
            OIDCRegistrationRequest oidcRegistrationRequest,
            X509Authentication currentUser
    ) throws ParseException {
        //Import information from the SSA into the registration request, as per the OAuth2 dynamic registration
        oidcRegistrationRequest.setJwks_uri(ssaClaims.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_JWKS_ENDPOINT));
        oidcRegistrationRequest.setClientName(ssaClaims.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_CLIENT_NAME));
        oidcRegistrationRequest.setLogoUri(ssaClaims.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_LOGO_URI));
        oidcRegistrationRequest.setContacts(parseContacts(ssaClaims));
        oidcRegistrationRequest.setTosUri(ssaClaims.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_TOS_URI));
        oidcRegistrationRequest.setPolicyUri(ssaClaims.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_POLICY_URI));

        List<String> rolesSerialised = ssaClaims.getStringListClaim(OpenBankingConstants.SSAClaims.SOFTWARE_ROLES);
        Set<SoftwareStatementRole> types = rolesSerialised.stream().map(role -> SoftwareStatementRole.valueOf(role)).collect(Collectors.toSet());

        if (oidcRegistrationRequest.getScope() != null || "".equals(oidcRegistrationRequest.getScope())) {
            log.debug("Transfer scope value into scopes");
            oidcRegistrationRequest.setScopes(Stream.of(oidcRegistrationRequest.getScope().split(" ")).collect(Collectors.toList()));
        }
        log.debug("Adding accounts and payments scope depending of the TPP type");
        Set<String> scopes = new HashSet<>(oidcRegistrationRequest.getScopes());

        scopes.add(OpenBankingConstants.Scope.OPENID);
        if (!types.contains(SoftwareStatementRole.AISP) && scopes.contains(OpenBankingConstants.Scope.ACCOUNTS)) {
            scopes.remove(OpenBankingConstants.Scope.ACCOUNTS);
        }
        if (!types.contains(SoftwareStatementRole.PISP) && scopes.contains(OpenBankingConstants.Scope.PAYMENTS)) {
            scopes.remove(OpenBankingConstants.Scope.PAYMENTS);
        }
        if (!types.contains(SoftwareStatementRole.CBPII) && scopes.contains(OpenBankingConstants.Scope.FUNDS_CONFIRMATIONS)) {
            scopes.remove(OpenBankingConstants.Scope.FUNDS_CONFIRMATIONS);
        }
        OIDCConstants.TokenEndpointAuthMethods authMethods = OIDCConstants.TokenEndpointAuthMethods.fromType(oidcRegistrationRequest.getTokenEndpointAuthMethod());

        if (authMethods == TLS_CLIENT_AUTH) {
            oidcRegistrationRequest.setTlsClientAuthSubjectDn(currentUser.getCertificateChain()[0].getSubjectDN().toString());
        }

        oidcRegistrationRequest.setScopes(new ArrayList<>(scopes));
        oidcRegistrationRequest.setScope(scopes.stream().collect(Collectors.joining(" ")));

        return types;
    }

    public void unregisterTpp(String token, Tpp tpp) {
        log.debug("Unregister TPP {}", tpp);

        log.debug("Delete in AM");
        amoidcRegistrationService.deleteOIDCClient(token, tpp.getClientId());
        pushTppEntry(tpp, true);

        log.debug("Delete in rs store");
        tppStoreService.deleteTPP(tpp);
    }


    public OIDCRegistrationResponse getOIDCClient(String token, Tpp tpp) {
        log.debug("Read TPP {} in AM", tpp.getClientId());
        return amoidcRegistrationService.getOIDCClient(token, tpp.getClientId());
    }
}
