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

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.InternationalConsent5Repository;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalConsent;
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
public class InternationalPaymentApiController implements InternationalPaymentApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternationalPaymentApiController.class);

    private InternationalConsent5Repository internationalConsentRepository;
    private ConsentMetricService consentMetricService;

    @Autowired
    public InternationalPaymentApiController(InternationalConsent5Repository internationalConsentRepository,
                                             ConsentMetricService consentMetricService
    ) {
        this.internationalConsentRepository = internationalConsentRepository;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity get(
            @PathVariable("paymentId") String paymentId
    ) {
        LOGGER.debug("Find payment by id {}", paymentId);
        Optional<FRInternationalConsent> byPaymentId = internationalConsentRepository.findById(paymentId);
        return byPaymentId.<ResponseEntity>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment id '" + paymentId + "' not found"));

    }

    @Override
    public ResponseEntity<Collection<FRInternationalConsent>> findByStatus(
            @RequestParam("status") ConsentStatusCode status
    ) {
        LOGGER.debug("Find payment by status {}", status);
        return new ResponseEntity<>(internationalConsentRepository.findByStatus(status.toOBTransactionIndividualStatus1Code()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FRInternationalConsent> update(
            @RequestBody FRInternationalConsent paymentSetup
    ) {
        LOGGER.debug("Update payment {}", paymentSetup);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(paymentSetup.getId(), paymentSetup.getStatus().name()));
        return new ResponseEntity<>(internationalConsentRepository.save(paymentSetup), HttpStatus.OK);
    }
}
