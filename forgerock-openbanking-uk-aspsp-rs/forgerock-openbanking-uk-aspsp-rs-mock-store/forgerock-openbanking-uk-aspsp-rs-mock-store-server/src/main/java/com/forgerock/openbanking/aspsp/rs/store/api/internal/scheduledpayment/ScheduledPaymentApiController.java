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
package com.forgerock.openbanking.aspsp.rs.store.api.internal.scheduledpayment;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.scheduledpayments.FRScheduledPaymentRepository;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRScheduledPayment;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class ScheduledPaymentApiController implements ScheduledPaymentApi {

    private FRScheduledPaymentRepository scheduledPaymentRepository;

    @Autowired
    public ScheduledPaymentApiController(FRScheduledPaymentRepository scheduledPaymentRepository) {
        this.scheduledPaymentRepository = scheduledPaymentRepository;
    }

    public ResponseEntity<FRScheduledPayment> create(
            @RequestBody FRScheduledPayment scheduledPayment
    ) {
        log.debug("Create scheduled payment {}", scheduledPayment);
        return new ResponseEntity<>(scheduledPaymentRepository.save(scheduledPayment), HttpStatus.CREATED);
    }

    public ResponseEntity update(
            @RequestBody FRScheduledPayment scheduledPayment,
            @PathVariable("id") String id
    ) {
        Preconditions.checkArgument(id.equals(scheduledPayment.getId()), "id in URL does not match id in provided update");
        log.debug("Update scheduled payment {}", scheduledPayment);

        Optional<FRScheduledPayment> byId = scheduledPaymentRepository.findById(id);
        if (byId.isPresent()) {
            scheduledPayment.setId(id);
            return ResponseEntity.ok(scheduledPaymentRepository.save(scheduledPayment));
        } else {
            log.debug("Scheduled payment not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Scheduled payment not found: '"+id+"'");
        }

    }

    @Override
    public ResponseEntity<List<FRScheduledPayment>> getAll(ScheduledPaymentStatus status, DateTime toDateTime) {
        log.debug("Find FR Scheduled Payment by status {} and before date/time: {}", status, toDateTime);
        return ResponseEntity.ok(scheduledPaymentRepository.findByStatus(status, toDateTime));
    }
}
