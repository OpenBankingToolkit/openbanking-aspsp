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
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.services.openbanking.OBHeaderCheckerService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RSEndpointWrapperTest {

    @Test
    public void verifyIdempotencyKey_invalid() {
        // Given (stubbed endpoint wrapper)
        TestRSEndpointWrapper testRSEndpointWrapper = new TestRSEndpointWrapper(
                new RSEndpointWrapperService(new OBHeaderCheckerService(null), null, null, null,null, null, null, true,null, null, null, null, null, null, null, null)
        );

        // When (idempotency key is not valid)
        assertThatThrownBy(() ->
                testRSEndpointWrapper.verifyIdempotencyKeyLength("")
        )
        // Then (throw correct error)
        .isExactlyInstanceOf(OBErrorException.class)
        .hasMessage("Invalid Idempotency Key provided in header. The x-idempotency-key in the request headers must be between 1 and 40 characters. Provided value: '' has length: 0")
        ;
    }

    @Test
    public void verifyIdempotencyKey_valid() throws Exception {
        // Given (stubbed endpoint wrapper)
        TestRSEndpointWrapper testRSEndpointWrapper = new TestRSEndpointWrapper(
                new RSEndpointWrapperService(new OBHeaderCheckerService(null), null, null, null, null, null, null, true,null, null, null, null, null, null, null, null)
        );

        // When (idempotency key is valid)
        testRSEndpointWrapper.verifyIdempotencyKeyLength(UUID.randomUUID().toString());

        // Then No Exception is thrown
    }


    // Simple override of abstract class so we can test public 'verify' superclass methods in one place
    // Testing of run() method implementations can be done for concrete subclass tests
    private static class TestRSEndpointWrapper extends RSEndpointWrapper
    {
        public TestRSEndpointWrapper(RSEndpointWrapperService rsEndpointWrapperService) {
            super(rsEndpointWrapperService);
        }

        @Override
        protected ResponseEntity run(Object main) throws OBErrorException, JsonProcessingException {
            throw new RuntimeException();
        }
    };
}