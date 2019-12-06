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
package com.forgerock.openbanking.aspsp.as.api.oauth2.discovery;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Getter
public class DiscoveryConfig {

    @Value("${dynamic-registration.enable}")
    private boolean isDynamicRegistrationEnable;
    @Value("${dynamic-registration.supported-token-endpoint-auth-method}")
    private List<String> supportedAuthMethod;
    @Value("${grant-types.supported}")
    private List<String> supportedGrantTypes;
    @Value("${user-info.enable}")
    private boolean isUserInfoEnable;
    @Value("${introspection.enable}")
    private boolean isIntrospectionEnable;
    @Value("${request-uri-parameter.supported:true}")
    private boolean requestUriParameterSupported;
    @Value("${request_object_encryption_alg_values.enabled:true}")
    private boolean isRequestObjectEncryptionAlgValuesEnabled;
    @Value("${request_object_encryption_enc_values.enabled:true}")
    private boolean isRequestObjectEncryptionEncValuesEnabled;
    @Value("${id_token_encryption_alg_values.enabled:true}")
    private boolean isIdTokenEncryptionAlgValuesEnabled;
    @Value("${id_token_encryption_enc_values.enabled:true}")
    private boolean isIdTokenEncryptionEncValuesEnabled;
    @Value("${obie.jwk_uri:}")
    private String obieJwkUri;
}
