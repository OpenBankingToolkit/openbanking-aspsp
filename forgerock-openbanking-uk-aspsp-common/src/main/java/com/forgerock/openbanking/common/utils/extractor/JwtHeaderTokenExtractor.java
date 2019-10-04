/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.utils.extractor;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {
    public static String HEADER_PREFIX = "Bearer ";

    @Override
    public String extract(String header) {
        if ("".equals(header) || header == null) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }

        return header.substring(HEADER_PREFIX.length(), header.length());
    }
}