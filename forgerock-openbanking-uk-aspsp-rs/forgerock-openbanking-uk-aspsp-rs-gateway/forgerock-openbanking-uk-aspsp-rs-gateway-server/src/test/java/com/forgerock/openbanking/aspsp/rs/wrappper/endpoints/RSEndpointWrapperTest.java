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
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RSEndpointWrapperTest {

    @Mock
    private RSEndpointWrapperService rsEndpointWrapperService;

    @Mock
    private TppStoreService tppStoreService;

    @InjectMocks
    private TestRSEndpointWrapper testRSEndpointWrapper;

    @Test
    public void shouldVerifyIdempotencyKeyGivenValidKey() throws OBErrorException {
        // Given (stubbed endpoint wrapper)
        String xIdempotencyKey = UUID.randomUUID().toString();

        // When
        testRSEndpointWrapper.verifyIdempotencyKeyLength(xIdempotencyKey);

        // Then No Exception is thrown
    }

    @Test
    public void shouldFailToVerifyIdempotencyKeyGivenInvalidKey() {
        // Given
        String xIdempotencyKey = "";

        // When
        OBErrorException exception = catchThrowableOfType(() -> testRSEndpointWrapper.verifyIdempotencyKeyLength(xIdempotencyKey), OBErrorException.class);

        // Then
        assertThat(exception).hasMessage("Invalid Idempotency Key provided in header. The x-idempotency-key in the request headers must be between 1 and 40 characters. Provided value: '' has length: 0");
    }

    @Test
    public void shouldIgnoreJwsDetachedSignatureGivenVerificationDisabled() throws OBErrorException {
        // Given
        testRSEndpointWrapper.rsEndpointWrapperService.isDetachedSignatureEnable = false;
        HttpServletRequest request = mock(HttpServletRequest.class);

        // When
        testRSEndpointWrapper.verifyJwsDetachedSignature(null, request);

        // Then No Exception is thrown
    }

    @Test
    public void shouldMatch_verifyMatcherVersion(){
        // Given
        String value = "https://tpp.callbackurl.com/v3.1.2/event-notifications";
        String valueToCompare = "3.1.2";

        OBErrorException exception = catchThrowableOfType(() -> testRSEndpointWrapper.verifyMatcherVersion(value, valueToCompare), OBErrorException.class);;
        // Then
        assertThat(exception).isNull();
    }

    @Test
    public void shouldFailMatch_verifyMatcherVersion(){
        // Given
        String value = "https://tpp.callbackurl.com/v3.1.2/event-notifications";
        String valueToCompare = "3.1.3";

        OBErrorException exception = catchThrowableOfType(() -> testRSEndpointWrapper.verifyMatcherVersion(value, valueToCompare), OBErrorException.class);;
        // Then
        assertThat(exception).hasMessage("The object received is invalid. Reason 'Version on the callback url field https://tpp.callbackurl.com/v3.1.2/event-notifications doesn't match with the version value field 3.1.3'");
    }

    // Simple override of abstract class so we can test public 'verify' superclass methods in one place
    // Testing of run() method implementations can be done for concrete subclass tests
    private static class TestRSEndpointWrapper extends RSEndpointWrapper
    {
        public TestRSEndpointWrapper(RSEndpointWrapperService rsEndpointWrapperService,
                                     TppStoreService tppStoreService) {
            super(rsEndpointWrapperService, tppStoreService);
        }

        @Override
        protected ResponseEntity run(Object main) throws OBErrorException, JsonProcessingException {
            throw new RuntimeException();
        }
    }
}