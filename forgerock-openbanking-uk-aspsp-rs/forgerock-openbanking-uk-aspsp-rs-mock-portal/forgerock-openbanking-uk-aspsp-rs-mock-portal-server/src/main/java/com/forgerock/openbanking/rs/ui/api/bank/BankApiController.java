/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.rs.ui.api.bank;

import com.forgerock.openbanking.core.services.ApplicationApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@RestController
public class BankApiController implements BankApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankApiController.class);

    @Autowired
    private ApplicationApiClient applicationApiClient;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    @RequestMapping(value = "/keys/jwk_uri", method = RequestMethod.GET)
    public ResponseEntity<String> getJwkUri(Principal principal) {
        return ResponseEntity.ok(applicationApiClient.signingEncryptionKeysJwkUri("CURRENT"));
    }
}
