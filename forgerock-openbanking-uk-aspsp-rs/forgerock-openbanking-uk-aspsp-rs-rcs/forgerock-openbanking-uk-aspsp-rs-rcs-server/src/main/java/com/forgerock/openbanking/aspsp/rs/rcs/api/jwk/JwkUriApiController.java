/**
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
package com.forgerock.openbanking.aspsp.rs.rcs.api.jwk;

import com.forgerock.openbanking.core.services.ApplicationApiClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class JwkUriApiController implements JwkUriApi {
    private final static Logger LOGGER = LoggerFactory.getLogger(JwkUriApiController.class);

    @Autowired
    private ApplicationApiClientImpl applicationApiClient;

    @Override
    public ResponseEntity<String> jwksUri() {
        LOGGER.debug("JwksUri has been called.");
        return ResponseEntity.ok(applicationApiClient.signingEncryptionKeysJwkUri("CURRENT"));
    }
}
