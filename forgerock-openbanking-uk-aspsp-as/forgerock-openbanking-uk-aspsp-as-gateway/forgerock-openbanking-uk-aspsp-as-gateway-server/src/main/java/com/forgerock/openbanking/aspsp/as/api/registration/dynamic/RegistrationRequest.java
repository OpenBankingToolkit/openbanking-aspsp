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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientIdentity;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.JWTClaimsOrigin;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.RegistrationRequestJWTClaims;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.utils.JwsClaimsUtils;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants.SSAClaims;
import com.forgerock.openbanking.model.SoftwareStatementRole;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationRequest;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.forgerock.openbanking.constants.OIDCConstants.TokenEndpointAuthMethods.TLS_CLIENT_AUTH;

@Slf4j
public class RegistrationRequest extends OIDCRegistrationRequest {

    public Optional<String> getSsaJKSEndpointClaim() throws DynamicClientRegistrationException {
        RegistrationRequestJWTClaims registrationRequestJWTClaims = this.getSoftwareStatementClaims();
        return registrationRequestJWTClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_JWKS_ENDPOINT);
    }

    public String getRegistrationRequestJWTSerialized() {
        return registrationRequestJWTSerialized;
    }

    public void setRegistrationRequestJWTSerialized(String registrationRequestJWTSerialized) {
        this.registrationRequestJWTSerialized = registrationRequestJWTSerialized;
    }

    @JsonIgnore
    private String registrationRequestJWTSerialized;
    @JsonIgnore
    String registrationRequestJson = null;
    @JsonIgnore
    private TppRegistrationService tppRegistrationService;
    @JsonIgnore
    RegistrationRequestJWTClaims ssaClaims = null;
    @JsonIgnore
    public void setJson(String registrationRequestJson) {
        this.registrationRequestJson = registrationRequestJson;
    }

    @JsonIgnore
    public String toJson(){
        return this.registrationRequestJson;
    }

    @JsonIgnore
    public JSONObject getSoftwareStatementClaimsAsJson() throws DynamicClientRegistrationException {
        try {
            SignedJWT ssaJws = SignedJWT.parse(getSoftwareStatement());
            JWTClaimsSet ssaClaims = ssaJws.getJWTClaimsSet();
            JSONObject ssaJwsJson = new JSONObject(ssaClaims.toJSONObject());
            return ssaJwsJson;
        } catch (ParseException pe){
            throw new DynamicClientRegistrationException("Invalid SSA. Could not parse software statement JWK. Error:" +
                    " " + pe.getMessage(),DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT);
        }
    }

    @JsonIgnore
    public RegistrationRequestJWTClaims getSoftwareStatementClaims() throws DynamicClientRegistrationException {
        if(this.ssaClaims == null) {
            try {
                String softwareStatement = getSoftwareStatement();
                SignedJWT ssaJws = SignedJWT.parse(softwareStatement);
                JWTClaimsSet claimsSet = ssaJws.getJWTClaimsSet();
                this.ssaClaims = new RegistrationRequestJWTClaims(claimsSet, JWTClaimsOrigin.SOFTWARE_STATEMENT_ASSERTION);
            } catch (ParseException pe){
                throw new DynamicClientRegistrationException("Couldn't parse the Software Statement in the " +
                        "Registration Request JWT. Error was; " + pe.getMessage(),
                        DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT);
            }
        }
        return ssaClaims;
    }

    @JsonIgnore
    public String getSoftwareClientIdFromSSA() throws DynamicClientRegistrationException {
        return this.getSoftwareStatementClaims().getRequiredStringClaim(SSAClaims.SOFTWARE_CLIENT_ID);
    }

    @JsonIgnore
    public String validateSsaAgainstIssuingDirectoryJwksUri() throws DynamicClientRegistrationException {

        if(this.tppRegistrationService == null){
            String errorMessage = "No TPP registration service available with which to validate SSA signature. " +
                    "Registration requests should be created by the RegistrationRequestFactory";
            log.warn("validateSsaAgainstIssuingDirectoryJwksUri() {}. NullPointerException inevitable.", errorMessage);
        }
        String ssa = this.getSoftwareStatement();
        String issuer = this.getSsaIssuer();
        String signingAuthority = tppRegistrationService.validateSsaAgainstIssuingDirectoryJwksUri(ssa, issuer);
        if(signingAuthority == null){
            String errorMessage = "The SSA in the registration request JWK was not signed by a recognised authority. " +
                    "SSA is '" + ssa + "'";
            log.debug("validateSsaAgainstIssuingDirectoryJwksUri() {}", errorMessage);
            throw new DynamicClientRegistrationException(errorMessage,
                    DynamicClientRegistrationErrorType.UNAPPROVED_SOFTWARE_STATEMENT);
        }
        log.debug("validateSsaAgainstIssuingDirectoryJwksUri() validated SSA against '{}'", signingAuthority);
        return signingAuthority;
    }

    @JsonIgnore
    public void setTppRegistrationService(TppRegistrationService tppRegistrationService) {
        this.tppRegistrationService = tppRegistrationService;
    }

    /**
     * Section 3.1.1 of <a href=https://datatracker.ietf.org/doc/html/rfc7591#section-3.1.1>OAuth 2.0 Dynamic Client
     * Registration Protocol</a> states that client metadata values conveyed in the software statement MUST take
     *    precedence over those conveyed using plain JSON elements. To enforce that we will copy the values of the
     *    software statement claims into the fields of the Registration Request
     * @param clientIdentity
     */
    @JsonIgnore
    public void overwriteRegistrationRequestFieldsFromSSAClaims(ApiClientIdentity clientIdentity)
            throws DynamicClientRegistrationException {
        RegistrationRequestJWTClaims ssaClaims = getSoftwareStatementClaims();

        ssaClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_JWKS_ENDPOINT).ifPresent(this::setJwks_uri);
        ssaClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_CLIENT_NAME).ifPresent(this::setClientName);
        ssaClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_LOGO_URI).ifPresent(this::setLogoUri);
        this.setContacts(parseContacts(ssaClaims));
        ssaClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_TOS_URI).ifPresent(this::setTosUri);
        ssaClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_POLICY_URI).ifPresent(this::setPolicyUri);

        Set<SoftwareStatementRole> types = getSoftwareStatementRoles();

        String scope = this.getScope();
        if (!StringUtils.isEmpty(scope)) {
            log.debug("Transfer scope value into scopes");
            this.setScopes(Stream.of(this.getScope().split(" ")).collect(Collectors.toList()));
        }

        log.debug("Adding accounts and payments scope depending of the TPP type");
        Set<String> scopes = new HashSet<>(this.getScopes());

        scopes.add(OpenBankingConstants.Scope.OPENID);
        if ( !types.contains(SoftwareStatementRole.AISP) && scopes.contains(OpenBankingConstants.Scope.ACCOUNTS)) {
            scopes.remove(OpenBankingConstants.Scope.ACCOUNTS);
        }
        if (!types.contains(SoftwareStatementRole.PISP) && scopes.contains(OpenBankingConstants.Scope.PAYMENTS)) {
            scopes.remove(OpenBankingConstants.Scope.PAYMENTS);
        }
        if (!types.contains(SoftwareStatementRole.CBPII) && scopes.contains(OpenBankingConstants.Scope.FUNDS_CONFIRMATIONS)) {
            scopes.remove(OpenBankingConstants.Scope.FUNDS_CONFIRMATIONS);
        }
        OIDCConstants.TokenEndpointAuthMethods authMethods = OIDCConstants.TokenEndpointAuthMethods.fromType(this.getTokenEndpointAuthMethod());

        if (authMethods == TLS_CLIENT_AUTH) {
            this.setTlsClientAuthSubjectDn(clientIdentity.getTransportCertificate().getSubjectDN().toString());
        }

        this.setScopes(new ArrayList<>(scopes));
        this.setScope(scopes.stream().collect(Collectors.joining(" ")));

    }

    @JsonIgnore
    public List<String> parseContacts(RegistrationRequestJWTClaims ssaClaims) {
        List<String> contacts = new ArrayList<>();
        JSONArray contactsJsonArray = (JSONArray) ssaClaims.getClaim(SSAClaims.ORG_CONTACTS);
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

    @JsonIgnore
    public Set<SoftwareStatementRole> getSoftwareStatementRoles() throws DynamicClientRegistrationException {
        RegistrationRequestJWTClaims claimSet = getSoftwareStatementClaims();
        List<String> rolesSerialised = claimSet.getRequiredStringListClaims(SSAClaims.SOFTWARE_ROLES);
        Set<SoftwareStatementRole> roles = rolesSerialised.stream().map(SoftwareStatementRole::valueOf)
                .collect(Collectors.toSet());

        return roles;
    }

    @JsonIgnore
    public String getOrganisationName() throws DynamicClientRegistrationException {
        RegistrationRequestJWTClaims claimsSet = getSoftwareStatementClaims();
        return claimsSet.getRequiredStringClaim(SSAClaims.ORG_NAME);
    }

    @JsonIgnore
    public String getSoftwareIdFromSSA() throws DynamicClientRegistrationException {
        RegistrationRequestJWTClaims ssaClaims = this.getSoftwareStatementClaims();
        return ssaClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_ID);
    }

    @JsonIgnore
    public Optional<List<String>> getRedirectUrisFromSSA() throws DynamicClientRegistrationException {
        RegistrationRequestJWTClaims ssaClaims = this.getSoftwareStatementClaims();
        return ssaClaims.getOptionalStringListValues(SSAClaims.SOFTWARE_REDIRECT_URIS);
    }

    public String getSsaIssuer() throws DynamicClientRegistrationException {
        RegistrationRequestJWTClaims ssaClaims = this.getSoftwareStatementClaims();
        return ssaClaims.getRequiredStringClaim(SSAClaims.ISSUER);
    }

    public Optional<String> getSsaSoftwareSigningJWKClaim() throws DynamicClientRegistrationException {
        RegistrationRequestJWTClaims jwtClaims = this.getSoftwareStatementClaims();
        return jwtClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_SIGINING_JWK);
    }

}
