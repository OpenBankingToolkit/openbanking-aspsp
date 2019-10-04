/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.payment;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticConsent2Repository;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticConsent2;
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
public class DomesticPaymentApiControllerTest {

    @Mock
    private DomesticConsent2Repository repository;

    @InjectMocks
    private DomesticPaymentApiController controller;

    @Test
    public void getDomesticPaymentConsent() {
        // Given
        FRDomesticConsent2 expectedConsent = new FRDomesticConsent2();
        expectedConsent.id = "expectedId123";
        when(repository.findById(eq("pay123"))).thenReturn(Optional.of(expectedConsent));

        // When
        ResponseEntity resp = controller.get("pay123");

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((FRDomesticConsent2) Objects.requireNonNull(resp.getBody())).id).isEqualTo("expectedId123");
    }

    @Test
    public void getDomesticPaymentConsent_notFound() {
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
        FRDomesticConsent2 expectedConsent = new FRDomesticConsent2();
        expectedConsent.id = "expectedId123";
        when(repository.findByStatus(eq(OBTransactionIndividualStatus1Code.ACCEPTEDSETTLEMENTINPROCESS))).thenReturn(Collections.singleton(expectedConsent));

        // When
        ResponseEntity<Collection<FRDomesticConsent2>> resp = controller.findByStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).containsExactly(expectedConsent);
    }

    @Test
    public void findByStatus_noneFound() {
        // Given
        when(repository.findByStatus(eq(OBTransactionIndividualStatus1Code.ACCEPTEDSETTLEMENTINPROCESS))).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<Collection<FRDomesticConsent2>> resp = controller.findByStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isEmpty();
    }

}