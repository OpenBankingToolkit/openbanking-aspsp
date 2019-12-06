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
package com.forgerock.openbanking.common.conf;

import com.forgerock.openbanking.config.ApplicationConfiguration;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

@Service
public class RSConfiguration implements ApplicationConfiguration {
    public final String issuerId;
    public final String financialId;
    public final String jwksUri;

    private JWKSet jwkSet = null;

    public RSConfiguration(
            @Value("${rs.issuerid}") String issuerId,
            @Value("${rs.financial_id}") String financialId,
            @Value("${rs.jwks_uri}") String jwksUri) {
        this.issuerId = issuerId;
        this.financialId = financialId;
        this.jwksUri = jwksUri;
    }

    @Override
    public String getIssuerID() {
        return issuerId;
    }

    public synchronized JWKSet getJwkSet() {
        try {
            jwkSet = JWKSet.load(new URL(jwksUri));
        } catch (IOException e) {
            throw new RuntimeException("Can't connect to RS-API", e);
        } catch (ParseException e) {
            throw new RuntimeException("Can't parse RS-API JWKS", e);
        }
        return jwkSet;
    }

}
