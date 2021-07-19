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
import com.forgerock.openbanking.aspsp.as.api.registration.dynamic.RegistrationRequest;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class RegistrationRequestFactoryTest {

    @Mock
    private TppRegistrationService tppRegistrationService;

    private ObjectMapper objectMapper;

    private RegistrationRequestFactory registrationRequestFactory;

    @Before
    public void setUp() throws Exception {
        this.registrationRequestFactory = new RegistrationRequestFactory(tppRegistrationService);
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    public void getRegistrationRequestFromJwt() {
    }

    @Test
    public void failIfSsaIsInvalid_getRegistrationRequestFromManualRegistrationJson()
            throws DynamicClientRegistrationException {

        // Given
        String registrationRequestJwtSerialised = TestHelperFunctions.getValidRegistrationRequestJWTSerialised();


        // When
        RegistrationRequest regRequest =
                registrationRequestFactory.getRegistrationRequestFromJwt(registrationRequestJwtSerialised, this.objectMapper);

        // Then
        assertThat(regRequest).isNotNull();
    }
}