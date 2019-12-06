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

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.InternationalStandingOrderConsent3Repository;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRInternationalStandingOrderConsent3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.payment.OBTransactionIndividualStatus1Code;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InternationalStandingOrderApiControllerTest {

    @Mock
    private InternationalStandingOrderConsent3Repository repository;

    @InjectMocks
    private InternationalStandingOrderApiController controller;

    @Test
    public void getInternationalStandingOrderPaymentConsent() {
        // Given
        FRInternationalStandingOrderConsent3 expectedConsent = new FRInternationalStandingOrderConsent3();
        expectedConsent.id = "expectedId123";
        when(repository.findById(eq("pay123"))).thenReturn(Optional.of(expectedConsent));

        // When
        ResponseEntity resp = controller.get("pay123");

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((FRInternationalStandingOrderConsent3) Objects.requireNonNull(resp.getBody())).id).isEqualTo("expectedId123");
    }

    @Test
    public void getInternationalStandingOrderPaymentConsent_notFound() {
        // Given
        when(repository.findById(eq("pay123"))).thenReturn(Optional.empty());

        // When
        ResponseEntity resp = controller.get("pay123");

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void findByStatus() {
        // Given
        FRInternationalStandingOrderConsent3 expectedConsent = new FRInternationalStandingOrderConsent3();
        expectedConsent.id = "expectedId123";
        when(repository.findByStatus(eq(OBTransactionIndividualStatus1Code.ACCEPTEDSETTLEMENTINPROCESS))).thenReturn(Collections.singleton(expectedConsent));

        // When
        ResponseEntity<Collection<FRInternationalStandingOrderConsent3>> resp = controller.findByStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).containsExactly(expectedConsent);
    }

    @Test
    public void findByStatus_noneFound() {
        // Given
        when(repository.findByStatus(eq(OBTransactionIndividualStatus1Code.ACCEPTEDSETTLEMENTINPROCESS))).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<Collection<FRInternationalStandingOrderConsent3>> resp = controller.findByStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isEmpty();
    }

}