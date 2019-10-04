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
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.FileConsent2Repository;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRFileConsent2;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FilePaymentApiController implements FilePaymentApi {
    private final FileConsent2Repository consentRepository;
    private ConsentMetricService consentMetricService;

    @Autowired
    public FilePaymentApiController(FileConsent2Repository consentRepository, ConsentMetricService consentMetricService) {
        this.consentRepository = consentRepository;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity get(@PathVariable("paymentId") String paymentId) {
        log.debug("Find file payment by id {}", paymentId);
        Optional<FRFileConsent2> byPaymentId = consentRepository.findById(paymentId);
        return byPaymentId.<ResponseEntity>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment id '" + paymentId + "' not found"));
    }

    @Override
    public ResponseEntity<FRFileConsent2> update(@RequestBody FRFileConsent2 paymentSetup) {
        log.debug("Update file payment {}", paymentSetup);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(paymentSetup.getId(), paymentSetup.getStatus().name()));
        return ResponseEntity.ok(consentRepository.save(paymentSetup));
    }

    @Override
    public ResponseEntity<Collection<FRFileConsent2>> findByStatus(
            @RequestParam("status") ConsentStatusCode status) {
        log.debug("Find file payment by status {}", status);
        return new ResponseEntity<>(consentRepository.findByStatus(status), HttpStatus.OK);
    }
}
