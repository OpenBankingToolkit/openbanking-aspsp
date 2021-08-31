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
package com.forgerock.openbanking.aspsp.as.service.registrationrequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientIdentity;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.model.SoftwareStatementRole;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationRequest;
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

    @JsonIgnore
    String registrationRequestJson = null;

    @JsonIgnore
    DirectorySoftwareStatement directorySoftwareStatement;


    @JsonIgnore
    public void setJson(String registrationRequestJson) {
        this.registrationRequestJson = registrationRequestJson;
    }

    @JsonIgnore
    public String toJson(){
        return this.registrationRequestJson;
    }



    @JsonIgnore
    public String getSoftwareStatementClaimsAsJsonString() throws DynamicClientRegistrationException {
        try {
            SignedJWT ssaJws = SignedJWT.parse(getSoftwareStatement());
            JWTClaimsSet ssaClaims = ssaJws.getJWTClaimsSet();
            JSONObject ssaJwsJson = new JSONObject(ssaClaims.toJSONObject());
            return ssaJwsJson.toJSONString();
        } catch (ParseException pe){
            throw new DynamicClientRegistrationException("Invalid SSA. Could not parse software statement JWK. Error:" +
                    " " + pe.getMessage(),DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT);
        }
    }

    /**
     * Section 3.1.1 of <a href=https://datatracker.ietf.org/doc/html/rfc7591#section-3.1.1>OAuth 2.0 Dynamic Client
     * Registration Protocol</a> states that client metadata values conveyed in the software statement MUST take
     *    precedence over those conveyed using plain JSON elements. To enforce that we will copy the values of the
     *    software statement claims into the fields of the Registration Request
     * @param clientIdentity
     */
    @JsonIgnore
    public void overwriteRegistrationRequestFieldsFromSSAClaims(ApiClientIdentity clientIdentity) {
        String methodName = "overwriteRegistrationRequestFieldsFromSSAClaims";
        Optional.ofNullable(this.directorySoftwareStatement.getSoftware_jwks_endpoint()).ifPresent(this::setJwks_uri);
        Optional.ofNullable(this.directorySoftwareStatement.getSoftware_client_id()).ifPresent(this::setClientName);
        Optional.ofNullable(this.directorySoftwareStatement.getSoftware_logo_uri()).ifPresent(this::setLogoUri);
        Optional.ofNullable(this.directorySoftwareStatement.getSoftware_tos_uri()).ifPresent(this::setTosUri);
        Optional.ofNullable(this.directorySoftwareStatement.getSoftware_policy_uri()).ifPresent(this::setPolicyUri);

        String regRequestScope = this.getScope();
        if (!StringUtils.isEmpty(regRequestScope)) {
            log.debug("{} Transfer scope value into registrationRequestScopes", methodName);
            this.setScopes(Stream.of(regRequestScope.split(" ")).collect(Collectors.toList()));
        }

        log.debug("{} Adding accounts and payments scope depending of the TPP type", methodName);
        Set<String> registrationRequestScopes = new HashSet<>(this.getScopes());

        registrationRequestScopes.add(OpenBankingConstants.Scope.OPENID);

        if( !directorySoftwareStatement.hasRole(SoftwareStatementRole.AISP) ){
            registrationRequestScopes.remove(OpenBankingConstants.Scope.ACCOUNTS);
        }

        if (!directorySoftwareStatement.hasRole(SoftwareStatementRole.PISP)) {
            registrationRequestScopes.remove(OpenBankingConstants.Scope.PAYMENTS);
        }

        if (!directorySoftwareStatement.hasRole(SoftwareStatementRole.CBPII)) {
            registrationRequestScopes.remove(OpenBankingConstants.Scope.FUNDS_CONFIRMATIONS);
        }

        OIDCConstants.TokenEndpointAuthMethods authMethods = OIDCConstants.TokenEndpointAuthMethods.fromType(
                this.getTokenEndpointAuthMethod());

        if (authMethods == TLS_CLIENT_AUTH) {
            this.setTlsClientAuthSubjectDn(clientIdentity.getTransportCertificate().getSubjectDN().toString());
        }

        this.setScopes(new ArrayList<>(registrationRequestScopes));
        this.setScope(registrationRequestScopes.stream().collect(Collectors.joining(" ")));

    }


    @JsonIgnore
    public Set<SoftwareStatementRole> getSoftwareStatementRoles()  {
        List<String> rolesSerialised = this.directorySoftwareStatement.getSoftware_roles();
        Set<SoftwareStatementRole> roles = rolesSerialised.stream().map(SoftwareStatementRole::valueOf)
                .collect(Collectors.toSet());
        return roles;
    }

    @JsonIgnore
    public String getOrganisationName() {
        return this.directorySoftwareStatement.getOrg_name();
    }

    @JsonIgnore
    public String getSoftwareIdFromSSA() {
        return this.directorySoftwareStatement.getSoftware_client_id();
    }

    @JsonIgnore
    public Optional<List<String>> getRedirectUrisFromSSA() {
        return Optional.ofNullable(this.directorySoftwareStatement.getSoftware_redirect_uris());
    }

    @JsonIgnore
    public String getSsaIssuer() {
        return this.directorySoftwareStatement.getIss();
    }

    /**
    *  Represents the uniqe id of the JWT SSA
    **/
    @JsonIgnore
    public String getJti() {
        return this.directorySoftwareStatement.getJti();
    }

    @JsonIgnore
    public void setDirectorySoftwareStatement(DirectorySoftwareStatement directorySoftwareStatement){
        this.directorySoftwareStatement = directorySoftwareStatement;
    }
}
