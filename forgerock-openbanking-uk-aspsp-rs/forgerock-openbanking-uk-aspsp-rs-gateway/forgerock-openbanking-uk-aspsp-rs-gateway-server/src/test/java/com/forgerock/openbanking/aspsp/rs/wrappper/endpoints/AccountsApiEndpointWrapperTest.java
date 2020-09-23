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
import com.forgerock.openbanking.common.model.openbanking.persistence.v1_1.account.FRAccountRequest1;
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.error.OBStandardErrorCodes1;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

public class AccountsApiEndpointWrapperTest {
    private static AccountsApiEndpointWrapper wrapper;

    @Before
    public void setup() {
        wrapper = new AccountsApiEndpointWrapper(null) {
            @Override
            protected ResponseEntity run(Object main) throws OBErrorException, JsonProcessingException {
                return null;
            }
        };
    }

    @Test
    public void verifyAccountId_matches() throws Exception {
        // Given
        String accountId = "12345";
        wrapper.accountRequest = new FRAccountRequest1();
        wrapper.accountRequest.setAccountIds(Collections.singletonList(accountId));

        // Then
        assertThatCode(() -> {
            wrapper.accountId(accountId)
                    .verifyAccountId();

        }).doesNotThrowAnyException();
    }

    @Test
    public void verifyAccountId_noMatch() throws Exception {
        // Given
        wrapper.accountRequest = new FRAccountRequest1();
        wrapper.accountRequest.setAccountIds(Collections.singletonList("differentAccount123"));
        wrapper.accountId("12345");

        // When
        OBErrorException obErrorException = catchThrowableOfType(
                () -> wrapper.verifyAccountId(),
                OBErrorException.class
        );

        // Then
        assertThat(obErrorException.getMessage()).isEqualTo("You are not authorised to access account '12345'. The account request 'null' only authorised the following accounts: '[differentAccount123]'");
        assertThat(obErrorException.getOBError().getErrorCode()).isEqualTo(OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID.getValue());
        assertThat(obErrorException.getObriErrorType().getHttpStatus().value()).isEqualTo(400);
    }

    @Test
    public void verifyAccountId_null_noException() throws Exception {
        // Given
        wrapper.accountRequest = new FRAccountRequest1();
        wrapper.accountRequest.setAccountIds(Collections.singletonList("12345"));

        // Then
        assertThatCode(() -> {
            wrapper.accountId(null)
                    .verifyAccountId();
        }).doesNotThrowAnyException();
    }
}