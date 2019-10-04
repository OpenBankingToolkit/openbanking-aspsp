/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

public class JWT {

    public static String jws(String scope, String... authorities) throws JOSEException {
        return jws(scope, OIDCConstants.GrantType.AUTHORIZATION_CODE, authorities);
    }

    public static String jws(String scope,  OIDCConstants.GrantType grantType,  String... authorities) throws JOSEException {
        JsonObject idTokenClaims = Json.object()
                .add(OpenBankingConstants.AMAccessTokenClaim.ID_TOKEN, Json.object()
                        .add(OpenBankingConstants.AMAccessTokenClaim.INTENT_ID, Json.object()
                                .add("value", "test-tpp")));
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer("https://am.dev-ob.forgerock.financial:443/oauth2/auth")
                .claim(OBConstants.OIDCClaim.GRANT_TYPE, grantType.type)
                .claim(OBConstants.OIDCClaim.SCOPE, Collections.singleton(scope))
                .claim(OpenBankingConstants.AMAccessTokenClaim.CLAIMS, idTokenClaims.toString())
                .expirationTime(Date.from(Instant.now().plusSeconds(300)))
                .audience("test-tpp");
        Arrays.stream(authorities).forEach(a -> builder.claim(OpenBankingConstants.SSOClaim.AUTHORITIES, Collections.singletonList("GROUP_FORGEROCK")));
        JWTClaimsSet claims = builder.build();
        JWSHeader jwsHeader = new JWSHeader
                .Builder(JWSAlgorithm.HS256)
                .build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, claims);
        signedJWT.sign(new MACSigner(UUID.randomUUID().toString()));
        return signedJWT.serialize();
    }
}
