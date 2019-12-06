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
package com.forgerock.openbanking.aspsp.as.api.authorisation;

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
