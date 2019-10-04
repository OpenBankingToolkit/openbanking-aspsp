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
public class RcsConfiguration implements ApplicationConfiguration {
    @Value("${rcs.issuerid}")
    public String issuerId;
    @Value("${rcs.jwks_uri}")
    public String jwksUri;
    @Value("${rcs.endpoint.url}")
    public String rcsUrl;
    @Value("${rcs.endpoint.consentInfo}")
    public String consentInformationPath;
    @Value("${rcs.endpoint.aispConsentResponse}")
    public String aispConsentResponsePath;
    @Value("${rcs.endpoint.pispConsentResponse}")
    public String pispConsentResponsePath;

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
