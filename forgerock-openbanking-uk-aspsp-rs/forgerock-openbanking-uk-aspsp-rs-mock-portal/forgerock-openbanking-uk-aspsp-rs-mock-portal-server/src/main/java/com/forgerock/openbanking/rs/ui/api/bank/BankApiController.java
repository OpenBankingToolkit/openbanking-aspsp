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
