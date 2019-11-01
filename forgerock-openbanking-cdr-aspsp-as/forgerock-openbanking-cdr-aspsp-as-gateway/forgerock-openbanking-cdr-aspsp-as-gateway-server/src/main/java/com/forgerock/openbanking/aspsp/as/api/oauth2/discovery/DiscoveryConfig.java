/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
