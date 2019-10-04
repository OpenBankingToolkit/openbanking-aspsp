/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as.service;

import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class JwtOverridingService {

    @Autowired
    private CryptoApiClient cryptoApiClient;
    @Autowired
    private AMOpenBankingConfiguration amOpenBankingConfiguration;

    public String rewriteJWS(String jws) throws ParseException {

        JWTClaimsSet jwtClaimsSet = SignedJWT.parse(jws).getJWTClaimsSet();
        return cryptoApiClient.signClaims(amOpenBankingConfiguration.getIssuerID(), jwtClaimsSet, false);
    }
}
