/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.utils;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
public class HashUtils {

    public static String computeHash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] code_bytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            final byte[] half_code_bytes = Arrays.copyOfRange(code_bytes, 0, code_bytes.length / 2);
            return new String(Base64.getUrlEncoder().encode(half_code_bytes));

        } catch (NoSuchAlgorithmException ex) {
            log.error("Failed to compute hash for token: '{}' with exception.", token, ex);
        }
        return null;
    }

    public static String computeSHA256FullHash(String contentToEncode) {
        Preconditions.checkNotNull(contentToEncode, "Cannot hash null");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contentToEncode.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Unknown algorithm for file hash: SHA-256");
        }
    }
}
