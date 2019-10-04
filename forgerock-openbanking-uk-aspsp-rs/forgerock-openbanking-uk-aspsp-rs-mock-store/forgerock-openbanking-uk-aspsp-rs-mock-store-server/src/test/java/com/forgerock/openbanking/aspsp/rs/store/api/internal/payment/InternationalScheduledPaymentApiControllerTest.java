/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.payment;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalScheduledConsent2Repository;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalScheduledConsent2;
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
    private InternationalScheduledConsent2Repository repository;

    @InjectMocks
    private InternationalScheduledPaymentApiController controller;

    @Test
    public void getInternationalScheduledPaymentConsent() {
        // Given
        FRInternationalScheduledConsent2 expectedConsent = new FRInternationalScheduledConsent2();
        expectedConsent.id = "expectedId123";
        when(repository.findById(eq("pay123"))).thenReturn(Optional.of(expectedConsent));

        // When
        ResponseEntity resp = controller.get("pay123");

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((FRInternationalScheduledConsent2) Objects.requireNonNull(resp.getBody())).id).isEqualTo("expectedId123");
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