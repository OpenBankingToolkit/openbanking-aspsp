/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.payment;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticScheduledConsent2Repository;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticScheduledConsent2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Optional;

@Controller
public class DomesticScheduledPaymentApiController implements DomesticScheduledPaymentApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomesticScheduledPaymentApiController.class);

    private final DomesticScheduledConsent2Repository consentRepository;
    private ConsentMetricService consentMetricService;

    @Autowired
    public DomesticScheduledPaymentApiController(DomesticScheduledConsent2Repository consentRepository, ConsentMetricService consentMetricService) {
        this.consentRepository = consentRepository;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity get(
            @PathVariable("paymentId") String paymentId
    ) {
        LOGGER.debug("Find payment by id {}", paymentId);
        Optional<FRDomesticScheduledConsent2> byPaymentId = consentRepository.findById(paymentId);
        return byPaymentId.<ResponseEntity>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment id '" + paymentId + "' not found"));

    }

    @Override
    public ResponseEntity<Collection<FRDomesticScheduledConsent2>> findByStatus(
            @RequestParam("status") ConsentStatusCode status
    ) {
        LOGGER.debug("Find payment by status {}", status);
        return new ResponseEntity<>(consentRepository.findByStatus(status.toOBTransactionIndividualStatus1Code()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FRDomesticScheduledConsent2> update(
            @RequestBody FRDomesticScheduledConsent2 paymentSetup
    ) {
        LOGGER.debug("Update payment {}", paymentSetup);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(paymentSetup.getId(), paymentSetup.getStatus().name()));
        return new ResponseEntity<>(consentRepository.save(paymentSetup), HttpStatus.OK);
    }
}