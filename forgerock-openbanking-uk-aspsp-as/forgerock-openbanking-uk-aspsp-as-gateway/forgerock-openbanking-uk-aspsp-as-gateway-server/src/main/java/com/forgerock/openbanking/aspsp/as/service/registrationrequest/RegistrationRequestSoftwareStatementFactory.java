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

import com.forgerock.openbanking.aspsp.as.configuration.ForgeRockDirectoryConfiguration;
import com.forgerock.openbanking.aspsp.as.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.constants.OpenBankingConstants.SSAClaims;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.util.JSONObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RegistrationRequestSoftwareStatementFactory {

    private final OpenBankingDirectoryConfiguration openBankingDirectoryConfiguration;
    private final ForgeRockDirectoryConfiguration forgeRockDirectoryConfiguration;

    @Autowired
    public RegistrationRequestSoftwareStatementFactory(OpenBankingDirectoryConfiguration openbankingDirectoryConfiguration,
                                         ForgeRockDirectoryConfiguration forgeRockDirectoryConfiguration){
        this.openBankingDirectoryConfiguration = openbankingDirectoryConfiguration;
        this.forgeRockDirectoryConfiguration = forgeRockDirectoryConfiguration;
    }

    RegistrationRequestSoftwareStatement getSoftwareStatement(RegistrationRequestJWTClaims softwareStatementClaims)
            throws DynamicClientRegistrationException, ParseException {

        RegistrationRequestSoftwareStatement softwareStatement = null;
        String issuer = softwareStatementClaims.getRequiredStringClaim(SSAClaims.ISSUER);
        if(issuer.equals(openBankingDirectoryConfiguration.getIssuerID())){
            OpenBankingSoftwareStatement.OpenBankingSoftwareStatementBuilder softwareStatementBuilder =
                    new OpenBankingSoftwareStatement.OpenBankingSoftwareStatementBuilder();
            softwareStatement = softwareStatementBuilder
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
                    .organisation_competent_authority_claims(getCompententAuthorityClaims(softwareStatementClaims))
                    .software_environment(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_ENVIRONMENT))
                    .software_mode(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_MODE))
                    .software_id(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_ID))
                    .software_client_id(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_CLIENT_ID))
                    .software_client_name(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_CLIENT_NAME))
                    .software_client_description(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_CLIENT_DESCRIPTION))
                    .software_version(softwareStatementClaims.getRequiredDoubleClaim(SSAClaims.SOFTWARE_VERSION))
                    .software_client_uri(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_CLIENT_URI))
                    .software_redirect_uris(softwareStatementClaims.getRequiredStringListClaims(SSAClaims.SOFTWARE_REDIRECT_URIS))
                    .software_roles(softwareStatementClaims.getRequiredStringListClaims(SSAClaims.SOFTWARE_ROLES))
                    .software_logo_uri(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_LOGO_URI))
                    .software_jwks_endpoint(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_JWKS_ENDPOINT))
                    .software_jwks_revoked_endpoint(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_JWKS_REVOKED_ENDPOINT))
                    .software_policy_uri(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_POLICY_URI))
                    .software_tos_uri(softwareStatementClaims.getRequiredStringClaim(SSAClaims.SOFTWARE_TOS_URI))
                    .build();
        }
        return softwareStatement;
    }

    private OrganisationAuthorityClaims getCompententAuthorityClaims(RegistrationRequestJWTClaims softwareStatementClaims)
            throws ParseException {
        JSONObject contactsJsonArray = (JSONObject) softwareStatementClaims.getClaim(SSAClaims.ORGANISATION_COMPETENT_AUTHORITY_CLAIMS);
        OrganisationAuthorityClaims authorityClaims =
                new OrganisationAuthorityClaims.OrganisationAuthorityClaimsBuilder()
                .authority_id(JSONObjectUtils.getString(contactsJsonArray, SSAClaims.OCAC_AUTHORITY_ID))
                .registration_id(JSONObjectUtils.getString(contactsJsonArray, SSAClaims.OCAC_REGISTRATION_ID))
                .status(JSONObjectUtils.getString(contactsJsonArray, SSAClaims.OCAC_STATUS))
                .authorizations(getAuthorizations(contactsJsonArray)).build();
        return authorityClaims;
    }

    private List<AuthorisationClaim> getAuthorizations(JSONObject contactsJsonArray) throws ParseException {
        List<AuthorisationClaim> authClaims =  new ArrayList<>();
        JSONArray authorityClaims = (JSONArray) contactsJsonArray.get(SSAClaims.OCAC_AUTHORISATIONS);
        for(int arrayIndex = 0; arrayIndex < authorityClaims.size(); ++arrayIndex){
            JSONObject authorityClaimJson = (JSONObject) authorityClaims.get(arrayIndex);
            AuthorisationClaim authClaim = new AuthorisationClaim.AuthorisationClaimBuilder()
                    .member_state(JSONObjectUtils.getString(authorityClaimJson, SSAClaims.OCAC_MEMBER_STATE))
                    .roles(JSONObjectUtils.getStringList(authorityClaimJson, SSAClaims.OCAC_ROLES)).build();
            authClaims.add(authClaim);
        }
        return authClaims;
    }

    private List<OrganisationContact> getOrgContacts(RegistrationRequestJWTClaims softwareStatementClaims){
        List<OrganisationContact> contacts = new ArrayList<>();
        JSONArray contactsJsonArray = (JSONArray) softwareStatementClaims.getClaim(SSAClaims.ORG_CONTACTS);
        if(contactsJsonArray != null){
            for (Object contactJson : contactsJsonArray) {
                JSONObject contactJsonObject = ((JSONObject) contactJson);
                OrganisationContact orgContact =
                        new OrganisationContact.OrganisationContactBuilder()
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

    public static String getContactField(JSONObject contactJsonObject, String field){
        String fieldValue = null;
        try{
            fieldValue = JSONObjectUtils.getString(contactJsonObject, field);
        } catch(ParseException pe) {
            log.warn("Warning: ParseException getting field {} as string from {}",
                    field, JSONObjectUtils.toJSONString(contactJsonObject));
        }
        return fieldValue;
    }
}
