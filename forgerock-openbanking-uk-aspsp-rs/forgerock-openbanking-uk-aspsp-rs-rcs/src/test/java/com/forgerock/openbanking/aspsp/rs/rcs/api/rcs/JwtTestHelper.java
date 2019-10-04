/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.io.IOException;
import java.text.ParseException;
import java.util.function.Function;

public final class JwtTestHelper {

    public static final Function<String, String> utf8FileToString = fileName -> {
        try {
            return Resources.toString(Resources.getResource(fileName), Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    };

    public static final Function<String, JWTClaimsSet> jsonStringToClaimsSet = jsonString -> {
        try {
            return JWTClaimsSet.parse(jsonString);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    };

    public static final Function<JWTClaimsSet, SignedJWT> claimsSetToRsa256Jwt = jwtClaimsSet ->
            new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), jwtClaimsSet);

    public static final Function<SignedJWT, String> serializeJwt = SignedJWT::serialize;


    public static final Function<SignedJWT, SignedJWT> signJwt = unsignedJwt -> {
        try {
            final RSAKey rsaJWK = new RSAKeyGenerator(2048).generate();
            unsignedJwt.sign(new RSASSASigner(rsaJWK));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return unsignedJwt;
    };

}
