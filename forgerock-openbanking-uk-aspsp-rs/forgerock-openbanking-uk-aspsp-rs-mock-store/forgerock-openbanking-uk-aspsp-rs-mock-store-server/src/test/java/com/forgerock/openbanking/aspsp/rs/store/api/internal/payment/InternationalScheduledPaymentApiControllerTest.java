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
package com.forgerock.openbanking.aspsp.rs.store.api.internal.payment;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.payments.InternationalScheduledConsent4Repository;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalScheduledConsent4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InternationalScheduledPaymentApiControllerTest {

    @Mock
    private InternationalScheduledConsent4Repository repository;

    @InjectMocks
    private InternationalScheduledPaymentApiController controller;

    @Test
    public void getInternationalScheduledPaymentConsent() {
        // Given
        FRInternationalScheduledConsent4 expectedConsent = new FRInternationalScheduledConsent4();
        expectedConsent.id = "expectedId123";
        when(repository.findById(eq("pay123"))).thenReturn(Optional.of(expectedConsent));

        // When
        ResponseEntity resp = controller.get("pay123");

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((FRInternationalScheduledConsent4) Objects.requireNonNull(resp.getBody())).id).isEqualTo("expectedId123");
    }

    @Test
    public void getInternationalScheduledPaymentConsent_notFound() {
        // Given
        when(repository.findById(eq("pay123"))).thenReturn(Optional.empty());

        // When
        ResponseEntity resp = controller.get("pay123");

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}