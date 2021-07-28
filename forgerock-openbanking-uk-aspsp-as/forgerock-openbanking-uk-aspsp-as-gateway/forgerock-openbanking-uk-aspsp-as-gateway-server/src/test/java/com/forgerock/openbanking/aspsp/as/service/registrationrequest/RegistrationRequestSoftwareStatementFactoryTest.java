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
package com.forgerock.openbanking.aspsp.as.service.registrationrequest;

import com.forgerock.openbanking.aspsp.as.TestHelperFunctions;
import com.forgerock.openbanking.aspsp.as.configuration.ForgeRockDirectoryConfiguration;
import com.forgerock.openbanking.aspsp.as.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class RegistrationRequestSoftwareStatementFactoryTest {

    RegistrationRequestSoftwareStatementFactory softwareStatementFactory;


    @Before
    public void setup(){
        OpenBankingDirectoryConfiguration openbankingDirectoryConfiguration = new OpenBankingDirectoryConfiguration();
        openbankingDirectoryConfiguration.issuerId = "OpenBanking Ltd";
        ForgeRockDirectoryConfiguration forgerockDirectoryConfiguration = new ForgeRockDirectoryConfiguration();
        forgerockDirectoryConfiguration.id = "ForgeRock";
        this.softwareStatementFactory =
                new RegistrationRequestSoftwareStatementFactory(openbankingDirectoryConfiguration,
                        forgerockDirectoryConfiguration);
    }

    @Test
    public void getSoftwareStatement() throws ParseException, DynamicClientRegistrationException {
        // Given
        String registrationRequestJwtSerialised = TestHelperFunctions.getValidOBSsaSerialised();
        SignedJWT signedRegistrationRequestJwt = SignedJWT.parse(registrationRequestJwtSerialised);
        JWTClaimsSet registrationRequestClaimsSet = signedRegistrationRequestJwt.getJWTClaimsSet();
        RegistrationRequestJWTClaims registrationRequestJWTClaims =
                new RegistrationRequestJWTClaims(registrationRequestClaimsSet,
                        JWTClaimsOrigin.REGISTRATION_REQUEST_JWT);

        String ssaSerialised = TestHelperFunctions.getValidSsaSerialised();
        SignedJWT registrationRequestJws = SignedJWT.parse(registrationRequestJwtSerialised);
        JWTClaimsSet ssaJwtClaims = registrationRequestJws.getJWTClaimsSet();
        RegistrationRequestJWTClaims ssaJWTClaims =
                new RegistrationRequestJWTClaims(ssaJwtClaims,
                        JWTClaimsOrigin.REGISTRATION_REQUEST_JWT);

        // When
        RegistrationRequestSoftwareStatement statement = softwareStatementFactory.getSoftwareStatement(ssaJWTClaims);

        // Then
        assertThat(statement).isNotNull();


    }
}