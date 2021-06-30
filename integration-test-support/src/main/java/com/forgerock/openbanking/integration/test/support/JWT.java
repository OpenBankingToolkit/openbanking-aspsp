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
package com.forgerock.openbanking.integration.test.support;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import uk.org.openbanking.OBConstants;

import java.time.Instant;
import java.util.*;

public class JWT {

    public static String jws(String scope, String... authorities) throws JOSEException {
        return jws(scope, OIDCConstants.GrantType.AUTHORIZATION_CODE, authorities);
    }

    public static String jws(String scope,  OIDCConstants.GrantType grantType,  String... authorities) throws JOSEException {
        JsonObject idTokenClaims = Json.object()
                .add(OpenBankingConstants.AMAccessTokenClaim.ID_TOKEN, Json.object()
                        .add(OpenBankingConstants.AMAccessTokenClaim.INTENT_ID, Json.object()
                                .add(SupportConstants.LITERAL_VALUE, SupportConstants.USER_AUDIENCE)));
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer(SupportConstants.ISSUER)
                .claim(OBConstants.OIDCClaim.GRANT_TYPE, grantType.type)
                .claim(OBConstants.OIDCClaim.SCOPE, Collections.singleton(scope))
                .claim(OpenBankingConstants.AMAccessTokenClaim.CLAIMS, idTokenClaims.toString())
                .expirationTime(Date.from(Instant.now().plusSeconds(300)))
                .audience(SupportConstants.USER_AUDIENCE);
        Arrays.stream(authorities).forEach(a -> builder.claim(OpenBankingConstants.SSOClaim.AUTHORITIES, SupportConstants.AUTHORITIES));
        JWTClaimsSet claims = builder.build();
        JWSHeader jwsHeader = new JWSHeader
                .Builder(JWSAlgorithm.HS256)
                .build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, claims);
        signedJWT.sign(new MACSigner(UUID.randomUUID().toString()));
        return signedJWT.serialize();
    }
}
