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
package com.forgerock.openbanking.common.error;

import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicRegistrationAdviceResponseBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class CustomRestExceptionHandlerTest {

    @Test
    public void handleDynamicClientRegistrationException() {
        // Given

        CustomRestExceptionHandler customRestExceptionHandler = new CustomRestExceptionHandler();
        DynamicClientRegistrationException dce = new DynamicClientRegistrationException("test",
                DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
        WebRequest request = null;
        ResponseEntity<DynamicRegistrationAdviceResponseBody> response = 
                customRestExceptionHandler.handleDynamicClientRegistrationException(dce, request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo(
                DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA.toString());

    }
}