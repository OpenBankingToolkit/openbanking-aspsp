/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as.api.jwk;

import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.core.services.ApplicationApiClientImpl;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@Slf4j
public class JwkUriApiController implements JwkUriApi {

    @Autowired
    private ApplicationApiClientImpl applicationApiClient;

    @Autowired
    private AMOpenBankingConfiguration amOpenBankingConfiguration;

    @Override
    public ResponseEntity<String> jwksUri() throws ParseException {
        log.debug("JwksUri has been called.");

        log.debug("Load the jwkUri of the AS-API.");
        String jwkUriContentFromApp = applicationApiClient.signingEncryptionKeysJwkUri("CURRENT");
        JWKSet jwkSetFromApp = JWKSet.parse(jwkUriContentFromApp);

        log.debug("Load the jwkUri of AM.");
        JWKSet jwkSetFromAM = amOpenBankingConfiguration.getJwkSet();

        List<JWK> asJwkSet = jwkSetFromAM.getKeys().stream()
                .filter(k -> !k.getKeyUse().equals(KeyUse.ENCRYPTION)).collect(Collectors.toList());
        asJwkSet.addAll(jwkSetFromApp.getKeys());

        return ResponseEntity.ok(new JWKSet(asJwkSet).toJSONObject().toJSONString());
    }
}
