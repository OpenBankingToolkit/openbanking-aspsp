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

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Builder
@Data
@EqualsAndHashCode(callSuper=false)
public class OpenBankingSoftwareStatement implements RegistrationRequestSoftwareStatement {
    String iss;
    Date iat;
    String jti;
    String software_environment;
    String software_mode;
    String software_id;
    String software_client_id;
    String software_client_name;
    String software_client_description;
    Double software_version;
    String software_client_uri;
    List<String> software_redirect_uris;
    List<String> software_roles;
    OrganisationAuthorityClaims organisation_competent_authority_claims;
    String software_logo_uri;
    String org_status;
    String org_id;
    String org_name;
    List<OrganisationContact> org_contacts;
    String org_jwks_endpoint;
    String org_jwks_revoked_endpoint;
    String software_jwks_endpoint;
    String software_jwks_revoked_endpoint;
    String software_policy_uri;
    String software_tos_uri;
    String software_on_behalf_of_org;
}
