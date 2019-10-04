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
public class OpenBankingDirectoryConfiguration implements ApplicationConfiguration {
    @Value("${openbankingdirectory.id}")
    public String id;
    @Value("${openbankingdirectory.issuerid}")
    public String issuerId;
    @Value("${openbankingdirectory.jwks_uri}")
    public String jwksUri;
    @Value("${openbankingdirectory.ocsp}")
    public String ocsp;

    @Value("${openbankingdirectory.mit.id}")
    public String mitId;
    @Value("${openbankingdirectory.mit.issuerid}")
    public String mitIssuerId;
    @Value("${openbankingdirectory.mit.jwks_uri}")
    public String mitJwksUri;
    @Value("${openbankingdirectory.mit.ocsp}")
    public String mitOcsp;
    @Value("${openbankingdirectory.mit.enabled}")
    public Boolean mitEnabled;

    @Value("${openbankingdirectory.certificate.o}")
    public String o;
    @Value("${openbankingdirectory.certificate.l}")
    public String l;
    @Value("${openbankingdirectory.certificate.st}")
    public String st;
    @Value("${openbankingdirectory.certificate.c}")
    public String c;

    private JWKSet jwkSet = null;

    @Override
    public String getIssuerID() {
        return issuerId;
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
