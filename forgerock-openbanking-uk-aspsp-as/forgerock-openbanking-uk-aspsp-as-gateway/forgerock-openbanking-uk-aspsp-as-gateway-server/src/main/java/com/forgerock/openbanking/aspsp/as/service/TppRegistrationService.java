/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as.service;

import com.forgerock.openbanking.am.services.AMOIDCRegistrationService;
import com.forgerock.openbanking.analytics.model.entries.TppEntry;
import com.forgerock.openbanking.analytics.services.TppEntriesKPIService;
import com.forgerock.openbanking.aspsp.as.configuration.ForgeRockDirectoryConfiguration;
import com.forgerock.openbanking.aspsp.as.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.authentication.model.authentication.X509Authentication;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
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
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.forgerock.openbanking.constants.OIDCConstants.TokenEndpointAuthMethods.TLS_CLIENT_AUTH;

@Service
@Slf4j
public class TppRegistrationService {

    @Autowired
    private CryptoApiClient cryptoApiClient;
    @Autowired
    private OpenBankingDirectoryConfiguration openBankingDirectoryConfiguration;
    @Autowired
    private ForgeRockDirectoryConfiguration forgeRockDirectoryConfiguration;
    @Autowired
    private TppStoreService tppStoreService;
    @Autowired
    private AMOIDCRegistrationService amoidcRegistrationService;
    @Autowired
    private TppEntriesKPIService tppEntriesKPIService;


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
        log.debug("Verify the MTLS certificate matches the SSA. Software ID from the certificate {} " +
                "and from the SSA {}", softwareIdFromMatls, softwareIdFromSSA);
        if (!softwareIdFromMatls.equals(softwareIdFromSSA)) {
            log.error("SSA software ID '{}' doesn't match the Certificate CN '{}'", softwareIdFromSSA, softwareIdFromMatls);
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_TRANSPORT_CERTIFICATE_NOT_MATCHING_SSA,
                    softwareIdFromSSA,
                    softwareIdFromMatls
            );
        }
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

    public void verifyTPPRegistrationRequestAgainstSSA(OIDCRegistrationRequest oidcRegistrationRequest, JWTClaimsSet ssaClaims) throws OBErrorException, ParseException {
        log.debug("Verify TPP registration request matches the SSA request");
        log.debug("- Verify the software id");
        if (oidcRegistrationRequest.getSoftwareId() != null
                && !"".equals(oidcRegistrationRequest.getSoftwareId())
                && !oidcRegistrationRequest.getSoftwareId().equals(ssaClaims.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_ID))
        ) {
            log.debug("Software ID doesn't match the SSA");
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_REQUEST_NOT_MATCHING_SSA,
                    OpenBankingConstants.SSAClaims.SOFTWARE_ID
            );
        }

        log.debug("- Verify the redirect uri");
        if (oidcRegistrationRequest.getRedirectUris() == null || oidcRegistrationRequest.getRedirectUris().isEmpty()) {
            log.debug("The redirect uri was not set, we override it with the SSA redirect uris");
            oidcRegistrationRequest.setRedirectUris(ssaClaims.getStringListClaim(OpenBankingConstants.SSAClaims.SOFTWARE_REDIRECT_URIS));
        }

        if (!ssaClaims.getStringListClaim(OpenBankingConstants.SSAClaims.SOFTWARE_REDIRECT_URIS).containsAll(oidcRegistrationRequest.getRedirectUris())) {
            log.warn("Redirect Uris doesn't match the SSA");
            //TODO due to OB directory bug, we won't check redirect uri for now
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Redirect Uris doesn't match the SSA");
        }
    }

    public List<String> parseContacts(JWTClaimsSet ssaClaims) {
        List<String> contacts = new ArrayList<>();
        JSONArray contactsJsonArray = (JSONArray) ssaClaims.getClaim(OpenBankingConstants.SSAClaims.ORG_CONTACTS);
        if (contactsJsonArray != null) {
            for (Object contactJson : contactsJsonArray) {
                JSONObject contactJsonObject = ((JSONObject) contactJson);
                StringBuilder contact = new StringBuilder();
                contact.append("email:").append(contactJsonObject.getAsString("email")).append(";");
                contact.append("name:").append(contactJsonObject.getAsString("name")).append(";");
                contact.append("phone:").append(contactJsonObject.getAsString("phone")).append(";");
                contact.append("type:").append(contactJsonObject.getAsString("type")).append(";");
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
        oidcRegistrationRequest.setContacts( parseContacts(ssaClaims));
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
