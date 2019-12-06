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
package com.forgerock.openbanking.aspsp.rs.store.repository;

import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRPaymentSubmission;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;

/**
 * This class allows reuse of idempotent save logic for payment submissions.
 *
 * It wraps the relevant repository but adds logic to check if this is a valid idempotent request.
 * <ul>
 * <li/> If valid and repeated -> return existing object and do nothing
 * <li/> If valid and not repeated -> save and return new object
 * <li/> If invalid -> throw OBErrorResponseException
 * </ul>
 * @param <T> Type of payment submission (e.g. domestic single, international scheduled etc etc )
 */
@Slf4j
public class IdempotentRepositoryAdapter<T extends FRPaymentSubmission> {

    private final MongoRepository<T, String> repo;

    public IdempotentRepositoryAdapter(MongoRepository<T, String> repo) {
        this.repo = repo;
    }

    public T idempotentSave(T paymentSubmission) throws OBErrorResponseException {
        Optional<T> isPaymentSubmission = repo.findById(paymentSubmission.getId());
        if (isPaymentSubmission.isPresent()) {
            log.info("A payment with this payment id '{}' was already found. Checking idempotency key.", isPaymentSubmission.get().getId());
            validateIdempotencyRequest(paymentSubmission, isPaymentSubmission.get());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return isPaymentSubmission.get();
        } else {
            log.debug("No payment with this id '{}' exists. Proceed to create it.", paymentSubmission.getId());
            log.debug("Saving new payment submission: {}", paymentSubmission);
            paymentSubmission = repo.save(paymentSubmission);
            log.info("Created new Payment Submission: {}", paymentSubmission.getId());
            return paymentSubmission;
        }
    }
}
