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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.as.TestHelperFunctions;
import com.forgerock.openbanking.aspsp.as.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;


@RunWith(MockitoJUnitRunner.class)
public class RegistrationRequestFactoryTest {

    @Mock
    private TppRegistrationService tppRegistrationService;

    private RegistrationRequestFactory registrationRequestFactory;

    @Before
    public void setUp() throws Exception {
        OpenBankingDirectoryConfiguration openbankingDirectoryConfiguration = new OpenBankingDirectoryConfiguration();
        openbankingDirectoryConfiguration.issuerId = "OpenBanking";

        DirectorySoftwareStatementFactory softwareStatementFactory = new DirectorySoftwareStatementFactory(openbankingDirectoryConfiguration);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.registrationRequestFactory = new RegistrationRequestFactory(this.tppRegistrationService,
                softwareStatementFactory, objectMapper);

    }

    @Test
    public void getRegistrationRequestFromJwt() throws DynamicClientRegistrationException {
        // Given
        String registrationRequestJWT = TestHelperFunctions.getValidForgeRockSsaRegistrationRequestJWTSerialised();


        // When
        RegistrationRequest regRequest =
                registrationRequestFactory.getRegistrationRequestFromJwt(registrationRequestJWT);

        // Then
        assertThat(regRequest).isNotNull();
        assertThat(regRequest.getSoftwareStatement()).isNotNull();
    }

    @Test
    public void failIfSsaIsInvalid_getRegistrationRequestFromManualRegistrationJson()
            throws DynamicClientRegistrationException {

        // Given
        String registrationRequestJwtSerialised = TestHelperFunctions.getValidForgeRockSsaRegistrationRequestJWTSerialised();


        // When
        RegistrationRequest regRequest =
                registrationRequestFactory.getRegistrationRequestFromJwt(registrationRequestJwtSerialised);

        // Then
        assertThat(regRequest).isNotNull();
    }

    @Test
    public void failIfDynamicRegistrationJWTIsInvalid_getRegRequestFromManualRegistrationJson() throws DynamicClientRegistrationException {
        // Given
        String registrationRequestJwtSerialised = TestHelperFunctions.getValidForgeRockSsaRegistrationRequestJWTSerialised();
        String registrationRequestJWKUri = "https://service.directory.dev-ob.forgerock" +
                ".financial:8074/api/software-statement/60c75ba3c450450011efa679/application/jwk_uri";
        Mockito.doThrow(new DynamicClientRegistrationException("blah",
                        DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT))
                .when(this.tppRegistrationService)
                .verifyTPPRegistrationRequestSignature(anyString(), eq("60c75ba3c450450011efa679"), eq(registrationRequestJWKUri));

        // When
        DynamicClientRegistrationException  exception =
                catchThrowableOfType( () -> registrationRequestFactory.getRegistrationRequestFromJwt(registrationRequestJwtSerialised), DynamicClientRegistrationException.class);

        Assertions.assertThat(exception.getErrorType()).isEqualTo(
                DynamicClientRegistrationErrorType.INVALID_SOFTWARE_STATEMENT);
    }
}