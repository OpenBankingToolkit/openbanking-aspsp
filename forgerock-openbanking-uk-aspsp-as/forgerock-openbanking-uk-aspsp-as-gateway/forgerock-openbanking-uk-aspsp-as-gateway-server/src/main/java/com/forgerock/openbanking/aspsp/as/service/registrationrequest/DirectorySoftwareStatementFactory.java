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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.as.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.constants.OpenBankingConstants.SSAClaims;
import com.forgerock.openbanking.model.*;
import com.forgerock.openbanking.model.DirectorySoftwareStatementOpenBanking.DirectorySoftwareStatementOpenBankingBuilder;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.util.JSONObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DirectorySoftwareStatementFactory {

    private final OpenBankingDirectoryConfiguration openBankingDirectoryConfiguration;

    @Autowired
    public DirectorySoftwareStatementFactory(
            OpenBankingDirectoryConfiguration openbankingDirectoryConfiguration){
        this.openBankingDirectoryConfiguration = openbankingDirectoryConfiguration;}

    public static String getContactField(JSONObject contactJsonObject, String field) {
        String fieldValue = null;
        try {
            fieldValue = JSONObjectUtils.getString(contactJsonObject, field);
        } catch (ParseException pe) {
            log.warn("Warning: ParseException getting field {} as string from {}",
                    field, JSONObjectUtils.toJSONString(contactJsonObject));
        }
        return fieldValue;
    }

    public DirectorySoftwareStatement getSoftwareStatement(RegistrationRequestJWTClaims softwareStatementClaims)
            throws DynamicClientRegistrationException, ParseException {

        DirectorySoftwareStatement softwareStatement = null;
        String issuer = softwareStatementClaims.getRequiredStringClaim(SSAClaims.ISSUER);

        DirectorySoftwareStatementOpenBankingBuilder softwareStatementBuilder = DirectorySoftwareStatementOpenBanking.builder();
        softwareStatementBuilder
            .software_id(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_ID))
            .iss(issuer)
            .iat(softwareStatementClaims.getRequiredDateValue(SSAClaims.ISSUED_AT))
            .jti(softwareStatementClaims.getRequiredStringClaim(SSAClaims.JWT_ID))
            .org_id(softwareStatementClaims.getRequiredStringClaim(SSAClaims.ORG_ID))
            .org_name(softwareStatementClaims.getRequiredStringClaim(SSAClaims.ORG_NAME))
            .org_status(softwareStatementClaims.getRequiredStringClaim(SSAClaims.ORG_STATUS))
            .org_contacts(getOrgContacts(softwareStatementClaims))
            .org_jwks_endpoint(softwareStatementClaims.getRequiredStringClaim(SSAClaims.ORG_JWKS_ENDPOINT))
            .org_jwks_revoked_endpoint(softwareStatementClaims.getRequiredStringClaim(SSAClaims.ORG_JWKS_REVOKED_ENDPOINT))
            .software_mode(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_MODE))
            .software_id(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_ID))
            .software_client_id(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_CLIENT_ID))

            .software_roles(softwareStatementClaims.getRequiredStringListClaims(SSAClaims.SOFTWARE_ROLES))
            .software_jwks_endpoint(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_JWKS_ENDPOINT))
            .software_jwks_revoked_endpoint(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_JWKS_REVOKED_ENDPOINT));


        // Optional values that may or may not be present
        softwareStatementClaims.getOptionalStringListClaims(SSAClaims.SOFTWARE_REDIRECT_URIS)
                .ifPresent(softwareStatementBuilder::software_redirect_uris);
        softwareStatementClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_LOGO_URI)
                .ifPresent(softwareStatementBuilder::software_logo_uri);
        softwareStatementClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_CLIENT_NAME)
                .ifPresent(softwareStatementBuilder::software_client_name);
        softwareStatementClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_CLIENT_DESCRIPTION)
                .ifPresent(softwareStatementBuilder::software_client_description);
        softwareStatementClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_TOS_URI)
                .ifPresent(softwareStatementBuilder::software_tos_uri);
        softwareStatementClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_POLICY_URI)
                .ifPresent(softwareStatementBuilder::software_policy_uri);
        softwareStatementClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_ENVIRONMENT)
                .ifPresent(softwareStatementBuilder::software_environment);
        softwareStatementClaims.getOptionalDoubleClaim(SSAClaims.SOFTWARE_VERSION)
                .ifPresent(softwareStatementBuilder::software_version);
        softwareStatementClaims.getOptionalStringClaim(SSAClaims.SOFTWARE_CLIENT_URI)
                .ifPresent(softwareStatementBuilder::software_client_uri);

        if (issuer.equals(openBankingDirectoryConfiguration.getIssuerID())) {
            softwareStatementBuilder.organisation_competent_authority_claims(
                    getCompetentAuthorityClaims(softwareStatementClaims));
        }

        softwareStatement = softwareStatementBuilder.build();

        return softwareStatement;
    }

    private OrganisationAuthorityClaims getCompetentAuthorityClaims(
            RegistrationRequestJWTClaims softwareStatementClaims) throws ParseException {
        JSONObject contactsJsonArray =
                (JSONObject) softwareStatementClaims.getClaim( SSAClaims.ORGANISATION_COMPETENT_AUTHORITY_CLAIMS);
        return OrganisationAuthorityClaims.builder()
                .authority_id(JSONObjectUtils.getString(contactsJsonArray, SSAClaims.OCAC_AUTHORITY_ID))
                .registration_id(JSONObjectUtils.getString(contactsJsonArray, SSAClaims.OCAC_REGISTRATION_ID))
                .status(JSONObjectUtils.getString(contactsJsonArray, SSAClaims.OCAC_STATUS))
                .authorisations(getAuthorizations(contactsJsonArray)).build();
    }

    private List<AuthorisationClaim> getAuthorizations(JSONObject contactsJsonArray) throws ParseException {
        List<AuthorisationClaim> authClaims = new ArrayList<>();
        JSONArray authorityClaims = (JSONArray) contactsJsonArray.get(SSAClaims.OCAC_AUTHORISATIONS);
        for (Object authorityClaim : authorityClaims) {
            JSONObject authorityClaimJson = (JSONObject) authorityClaim;
            AuthorisationClaim authClaim = AuthorisationClaim.builder()
                    .member_state(JSONObjectUtils.getString(authorityClaimJson, SSAClaims.OCAC_MEMBER_STATE))
                    .roles(JSONObjectUtils.getStringList(authorityClaimJson, SSAClaims.OCAC_ROLES)).build();
            authClaims.add(authClaim);
        }
        return authClaims;
    }

    private List<OrganisationContact> getOrgContacts(RegistrationRequestJWTClaims softwareStatementClaims) {
        List<OrganisationContact> contacts = new ArrayList<>();
        JSONArray contactsJsonArray = (JSONArray) softwareStatementClaims.getClaim(SSAClaims.ORG_CONTACTS);
        if (contactsJsonArray != null) {
            for (Object contactJson : contactsJsonArray) {
                JSONObject contactJsonObject = ((JSONObject) contactJson);
                OrganisationContact orgContact =
                        OrganisationContact.builder()
                                .name(getContactField(contactJsonObject, "name"))
                                .email(getContactField(contactJsonObject, "email"))
                                .type(getContactField(contactJsonObject, "type"))
                                .phone(getContactField(contactJsonObject, "phone")).build();
                contacts.add(orgContact);
            }
        } else {
            String errorString = "No " + SSAClaims.ORG_CONTACTS + " field available from Software Statement";
            log.info("getOrgContacts() {}", errorString);
        }
        return contacts;
    }

    public DirectorySoftwareStatement getSoftwareStatementFromJsonString(String ssaAsJson,
                                                                         ObjectMapper objectMapper) throws IOException {
        DirectorySoftwareStatement softwareStatement = objectMapper.readValue(ssaAsJson, DirectorySoftwareStatement.class);
        return softwareStatement;
    }
}
