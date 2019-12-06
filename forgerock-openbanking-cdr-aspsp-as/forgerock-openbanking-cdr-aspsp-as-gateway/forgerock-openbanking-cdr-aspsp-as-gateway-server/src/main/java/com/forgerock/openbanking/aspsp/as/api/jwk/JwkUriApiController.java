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
