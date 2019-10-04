/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.jwk;

import com.forgerock.openbanking.core.services.ApplicationApiClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController()
public class JwkUriApiController implements JwkUriApi {
    private final static Logger LOGGER = LoggerFactory.getLogger(JwkUriApiController.class);

    @Autowired
    private ApplicationApiClientImpl applicationApiClient;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public ResponseEntity<String> jwksUri() {
        LOGGER.debug("JwksUri has been called.");
        return ResponseEntity.ok(applicationApiClient.signingEncryptionKeysJwkUri("CURRENT"));
    }
}
