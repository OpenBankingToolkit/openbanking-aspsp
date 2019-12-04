/**
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
package com.forgerock.openbanking.common.model.as.discovery;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OIDCDiscoveryResponse {

    @JsonProperty("version")
    private String version;
    @JsonProperty("issuer")
    private String issuer;
    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;
    @JsonProperty("token_endpoint")
    private String tokenEndpoint;
    @JsonProperty("userinfo_endpoint")
    private String userinfoEndpoint;
    @JsonProperty("introspection_endpoint")
    private String introspectionEndpoint;
    @JsonProperty("jwks_uri")
    private String jwksUri;
    @JsonProperty("registration_endpoint")
    private String registrationEndpoint;
    @JsonProperty("scopes_supported")
    private List<String> scopesEupported;
    @JsonProperty("response_types_supported")
    private List<String> responseTypesSupported;
    @JsonProperty("response_modes_supported")
    private List<String> responseModesSupported;
    @JsonProperty("grant_types_supported")
    private List<String> grantTypesSupported;
    @JsonProperty("acr_values_supported")
    private List<String> acrValuesSupported;
    @JsonProperty("subject_types_supported")
    private List<String> subjectTypesSupported;

    @JsonProperty("id_token_signing_alg_values_supported")
    private List<String> idTokenSigningAlgValuesSupported;
    @JsonProperty("id_token_encryption_alg_values_supported")
    private List<String> idTokenEncryptionAlgValuesSupported;
    @JsonProperty("id_token_encryption_enc_values_supported")
    private List<String> idTokenEncryptionEncValuesSupported;

    @JsonProperty("userinfo_signing_alg_values_supported")
    private List<String> userinfoSigningAlgValuesSupported;
    @JsonProperty("userinfo_encryption_alg_values_supported")
    private List<String> userinfoEncryptionAlgValuesSupported;
    @JsonProperty("userinfo_encryption_enc_values_supported")
    private List<String> userinfoEncryptionEncValuesSupported;

    @JsonProperty("request_object_signing_alg_values_supported")
    private List<String> requestObjectSigningAlgValuesSupported;
    @JsonProperty("request_object_encryption_alg_values_supported")
    private List<String> requestObjectEncryptionAlgValuesSupported;
    @JsonProperty("request_object_encryption_enc_values_supported")
    private List<String> requestObjectEncryptionEncValuesSupported;

    @JsonProperty("token_endpoint_auth_methods_supported")
    private List<String> tokenEndpointAuthMethodsSupported;
    @JsonProperty("token_endpoint_auth_signing_alg_values_supported")
    private List<String> tokenEndpointAuthSigningAlgValuesSupported;

    @JsonProperty("display_values_supported")
    private List<String> displayValuesSupported;
    @JsonProperty("claim_types_supported")
    private List<String> claimTypesSupported;
    @JsonProperty("claims_supported")
    private List<String> claimsSupported;
    @JsonProperty("service_documentation")
    private String serviceDocumentation;
    @JsonProperty("claims_locales_supported")
    private List<String> claimsLocalesSupported;
    @JsonProperty("ui_locales_supported")
    private List<String> uiLocalesSupported;
    @JsonProperty("claims_parameter_supported")
    private Boolean claimsParameterSupported;
    @JsonProperty("request_parameter_supported")
    private Boolean requestParameterSupported;
    @JsonProperty("request_uri_parameter_supported")
    private Boolean requestUriParameterSupported;
    @JsonProperty("require_request_uri_registration")
    private Boolean requireRequestUriRegistration;
    @JsonProperty("op_policy_uri")
    private String opPolicyUri;
    @JsonProperty("op_tos_uri")
    private String opTosUri;
    @JsonProperty("tls_client_auth_subject_dn")
    private String tlsClientAuthSubjectDn;


}
