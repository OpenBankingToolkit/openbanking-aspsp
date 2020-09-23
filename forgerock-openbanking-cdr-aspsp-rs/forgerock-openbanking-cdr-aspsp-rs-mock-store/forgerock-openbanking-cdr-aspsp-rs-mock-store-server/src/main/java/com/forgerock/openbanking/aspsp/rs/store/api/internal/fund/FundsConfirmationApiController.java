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
package com.forgerock.openbanking.aspsp.rs.store.api.internal.fund;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.funds.FundsConfirmationConsentRepository;
import com.forgerock.openbanking.common.model.openbanking.persistence.funds.v3_0.FRFundsConfirmationConsent1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.payment.OBTransactionIndividualStatus1Code;

import java.util.Collection;

@Controller
@Slf4j
public class FundsConfirmationApiController implements FundsConfirmationApi {

    private final FundsConfirmationConsentRepository fundsConfirmationConsentRepository;

    public FundsConfirmationApiController(FundsConfirmationConsentRepository fundsConfirmationConsentRepository) {
        this.fundsConfirmationConsentRepository = fundsConfirmationConsentRepository;
    }

    @Override
    public ResponseEntity get(
            @PathVariable("id") String id
    ) {
        log.debug("Find consent by id {}", id);
        return fundsConfirmationConsentRepository.findById(id)
                .<ResponseEntity>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment id '" + id + "' not found"));

    }

    @Override
    public ResponseEntity<Collection<FRFundsConfirmationConsent1>> findByStatus(
            @RequestParam("status") String status
    ) {
        log.debug("Find payment by status {}", status);
        return ResponseEntity.ok(fundsConfirmationConsentRepository.findByStatus(OBTransactionIndividualStatus1Code.fromValue(status)));
    }

    @Override
    public ResponseEntity<FRFundsConfirmationConsent1> update(
            @RequestBody FRFundsConfirmationConsent1 consent
    ) {
        log.debug("Update consent {}", consent);
        return ResponseEntity.ok(fundsConfirmationConsentRepository.save(consent));
    }
}
