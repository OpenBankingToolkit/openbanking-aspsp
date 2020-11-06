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
package com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments;

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.payments.FRInternationalPaymentSubmission2;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalPaymentSubmission;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConverter.toFRWriteInternational;

public class FRInternationalPaymentSubmissionMigrator {

    public static FRInternationalPaymentSubmission toFRInternationalPaymentSubmission(FRInternationalPaymentSubmission2 frInternationalPaymentSubmission2) {
        return frInternationalPaymentSubmission2 == null ? null : FRInternationalPaymentSubmission.builder()
                .id(frInternationalPaymentSubmission2.getId())
                .internationalPayment(toFRWriteInternational(frInternationalPaymentSubmission2.getInternationalPayment()))
                .created(frInternationalPaymentSubmission2.getCreated())
                .updated(frInternationalPaymentSubmission2.getUpdated())
                .idempotencyKey(frInternationalPaymentSubmission2.getIdempotencyKey())
                .obVersion(frInternationalPaymentSubmission2.getObVersion())
                .build();
    }
}
