/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.conf;

import com.forgerock.openbanking.config.ApplicationConfiguration;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

@Service
public class RSSimulatorConfiguration implements ApplicationConfiguration {

    public final String issuerId;
    public final String jwksUri;

    private JWKSet jwkSet = null;

    public RSSimulatorConfiguration(
            @Value("${rs-simulator.issuerid}") String issuerId,
            @Value("${rs-simulator.jwks_uri}") String jwksUri) {
        this.issuerId = issuerId;
        this.jwksUri = jwksUri;
    }

    @Override
    public String getIssuerID() {
        return issuerId;
    }

    public synchronized JWKSet getJwkSet() {
        try {
            jwkSet = JWKSet.load(new URL(jwksUri));
        } catch (IOException e) {
            throw new RuntimeException("Can't connect to RS simulator", e);
        } catch (ParseException e) {
            throw new RuntimeException("Can't parse RS simulator JWKS", e);
        }
        return jwkSet;
    }

}
