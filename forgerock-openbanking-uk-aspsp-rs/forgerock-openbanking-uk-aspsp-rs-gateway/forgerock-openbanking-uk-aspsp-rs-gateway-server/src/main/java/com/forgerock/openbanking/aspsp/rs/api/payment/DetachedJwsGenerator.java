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
package com.forgerock.openbanking.aspsp.rs.api.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.jwt.model.CreateDetachedJwtResponse;
import com.forgerock.openbanking.jwt.model.SigningRequest;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.forgerock.openbanking.common.model.version.OBVersion.v3_1_4;

@Component
@Slf4j
public class DetachedJwsGenerator {

    private final CryptoApiClient cryptoApiClient;
    private final ObjectMapper mapper;

    public DetachedJwsGenerator(CryptoApiClient cryptoApiClient, ObjectMapper mapper) {
        this.cryptoApiClient = cryptoApiClient;
        this.mapper = mapper;
    }

    public String generateDetachedJws(ResponseEntity response, OBVersion obVersion, String tan, String financialId) throws JsonProcessingException {
        String jwsSignature = "";

        SigningRequest.CustomHeaderClaims.CustomHeaderClaimsBuilder claimsBuilder = SigningRequest.CustomHeaderClaims.builder()
                .includeOBIss(true)
                .includeOBIat(true)
                .includeCrit(true)
                .tan(tan);

        if (obVersion == null || obVersion.isBeforeVersion(v3_1_4)) {
            claimsBuilder.includeB64(true);
        }
        SigningRequest signingRequest = SigningRequest.builder()
                .customHeaderClaims(claimsBuilder.build())
                .build();
        if (response.getBody() instanceof Resource) {
            log.debug("JWT signing does not currently work for a file response which it just a file byte stream (currently XML or JSON)");
            jwsSignature = null;
        } else {
            CreateDetachedJwtResponse createDetachedJwtResponse = cryptoApiClient.signPayloadToDetachedJwt(signingRequest, financialId, mapper.writeValueAsString(response.getBody()));
            if (createDetachedJwtResponse != null) {
                jwsSignature = createDetachedJwtResponse.getDetachedSignature();
            }
        }
        log.debug("Signed response claims. Signature: {}", jwsSignature);
        return jwsSignature;
    }
}

