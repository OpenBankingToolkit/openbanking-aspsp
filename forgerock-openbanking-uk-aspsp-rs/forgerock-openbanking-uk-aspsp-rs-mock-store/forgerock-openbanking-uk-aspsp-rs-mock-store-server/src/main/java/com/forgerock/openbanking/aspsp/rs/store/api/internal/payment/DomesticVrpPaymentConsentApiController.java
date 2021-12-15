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
import com.forgerock.openbanking.aspsp.rs.store.repository.vrp.DomesticVRPConsentRepository;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.Optional;

@Controller
@Slf4j
public class DomesticVrpPaymentConsentApiController implements DomesticVrpPaymentConsentApi {

    private final DomesticVRPConsentRepository consentRepository;
    private ConsentMetricService consentMetricService;

    @Autowired
    public DomesticVrpPaymentConsentApiController(
            DomesticVRPConsentRepository consentRepository, ConsentMetricService consentMetricService
    ) {
        this.consentRepository = consentRepository;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity get(String consentId) {
        log.debug("Find VRP consent by id {}", consentId);
        Optional<FRDomesticVRPConsent> byConsentId = consentRepository.findById(consentId);
        return byConsentId.
                <ResponseEntity>map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consent id '" + consentId + "' not found")
                );
    }

    @Override
    public ResponseEntity<Collection<FRDomesticVRPConsent>> findByStatus(ConsentStatusCode status) {
        log.debug("Find VRP consent by status {}", status);
        return new ResponseEntity<>(
                consentRepository.findByStatus(status.toOBTransactionIndividualStatus1Code()), HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<FRDomesticVRPConsent> update(FRDomesticVRPConsent vrpConsent) {
        log.debug("Update VRP consent {}", vrpConsent);
        consentMetricService.sendConsentActivity(
                new ConsentStatusEntry(vrpConsent.getId(), vrpConsent.getStatus().name())
        );
        return new ResponseEntity<>(consentRepository.save(vrpConsent), HttpStatus.OK);
    }
}
