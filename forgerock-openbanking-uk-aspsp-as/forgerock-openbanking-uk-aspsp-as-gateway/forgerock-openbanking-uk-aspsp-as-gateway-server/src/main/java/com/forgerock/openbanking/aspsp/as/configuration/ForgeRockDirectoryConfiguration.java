/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as.configuration;

import com.forgerock.openbanking.config.ApplicationConfiguration;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

@Service
public class ForgeRockDirectoryConfiguration implements ApplicationConfiguration {
    @Value("${forgerockdirectory.id}")
    public String id;
    @Value("${forgerockdirectory.jwks_uri}")
    public String jwksUri;

    @Value("${forgerockdirectory.certificate.o}")
    public String o;
    @Value("${forgerockdirectory.certificate.l}")
    public String l;
    @Value("${forgerockdirectory.certificate.st}")
    public String st;
    @Value("${forgerockdirectory.certificate.c}")
    public String c;

    private JWKSet jwkSet = null;

    public String getId() {
        return id;
    }

    @Override
    public String getIssuerID() {
        return id;
    }

    public synchronized JWKSet getJwkSet() {
        try {
            jwkSet = JWKSet.load(new URL(jwksUri));
        } catch (IOException e) {
            throw new RuntimeException("Can't connect to RCS", e);
        } catch (ParseException e) {
            throw new RuntimeException("Can't parse RCS JWKS", e);
        }
        return jwkSet;
    }

}
