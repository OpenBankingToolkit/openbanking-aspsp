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
import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.payments.paymentsetup.FRPaymentSetup1Repository;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v1_1.payment.FRPaymentSetup1;
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
public class PaymentApiController implements PaymentApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentApiController.class);

    private final FRPaymentSetup1Repository frPaymentSetup1Repository;
    private ConsentMetricService consentMetricService;


    @Autowired
    public PaymentApiController(FRPaymentSetup1Repository frPaymentSetup1Repository, ConsentMetricService consentMetricService) {
        this.frPaymentSetup1Repository = frPaymentSetup1Repository;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity get(
            @PathVariable("paymentId") String paymentId
    ) {
        LOGGER.debug("Find payment by id {}", paymentId);
        Optional<FRPaymentSetup1> byPaymentId = frPaymentSetup1Repository.findById(paymentId);

        return byPaymentId.<ResponseEntity>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment id '" + paymentId + "' not found"));

    }

    @Override
    public ResponseEntity<Collection<FRPaymentSetup1>> findByStatus(
            @RequestParam("status") ConsentStatusCode status
    ) {
        LOGGER.debug("Find payment by status {}", status);
        return new ResponseEntity<>(frPaymentSetup1Repository.findByStatus(status.toOBTransactionIndividualStatus1Code()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FRPaymentSetup1> update(
            @RequestBody FRPaymentSetup1 paymentSetup
    ) {
        LOGGER.debug("Update payment {}", paymentSetup);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(paymentSetup.getId(), paymentSetup.getStatus().name()));
        return new ResponseEntity<>(frPaymentSetup1Repository.save(paymentSetup), HttpStatus.OK);
    }
}
