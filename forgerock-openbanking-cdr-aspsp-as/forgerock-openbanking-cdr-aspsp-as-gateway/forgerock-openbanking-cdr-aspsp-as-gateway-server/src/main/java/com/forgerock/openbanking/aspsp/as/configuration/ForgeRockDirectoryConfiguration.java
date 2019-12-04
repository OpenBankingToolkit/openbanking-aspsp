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
package com.forgerock.openbanking.aspsp.as.configuration;

import com.forgerock.openbanking.config.ApplicationConfiguration;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

@Service
public class ForgeRockDirectoryConfiguration implements ApplicationConfiguration {
    @Value("${forgerockdirectory.id}")
    public String id;
    @Value("${forgerockdirectory.jwks_uri}")
    public String jwksUri;

    @Value("${forgerockdirectory.certificate.o}")
    public String o;
    @Value("${forgerockdirectory.certificate.l}")
    public String l;
    @Value("${forgerockdirectory.certificate.st}")
    public String st;
    @Value("${forgerockdirectory.certificate.c}")
    public String c;

    private JWKSet jwkSet = null;

    public String getId() {
        return id;
    }

    @Override
    public String getIssuerID() {
        return id;
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
