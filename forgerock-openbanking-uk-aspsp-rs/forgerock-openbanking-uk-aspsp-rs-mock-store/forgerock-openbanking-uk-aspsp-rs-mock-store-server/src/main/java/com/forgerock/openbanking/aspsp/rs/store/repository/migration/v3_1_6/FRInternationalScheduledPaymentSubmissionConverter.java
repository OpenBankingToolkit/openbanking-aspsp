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
package com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6;

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.InternationalScheduledPaymentSubmission2;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalScheduledPaymentSubmission;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConverter.toFRWriteInternationalScheduled;

public class FRInternationalScheduledPaymentSubmissionConverter {

    public static FRInternationalScheduledPaymentSubmission toFRInternationalScheduledPaymentSubmission(InternationalScheduledPaymentSubmission2 frInternationalScheduledPaymentSubmission2) {
        return frInternationalScheduledPaymentSubmission2 == null ? null : FRInternationalScheduledPaymentSubmission.builder()
                .id(frInternationalScheduledPaymentSubmission2.getId())
                .internationalScheduledPayment(toFRWriteInternationalScheduled(frInternationalScheduledPaymentSubmission2.getInternationalScheduledPayment()))
                .created(frInternationalScheduledPaymentSubmission2.getCreated())
                .updated(frInternationalScheduledPaymentSubmission2.getUpdated())
                .idempotencyKey(frInternationalScheduledPaymentSubmission2.getIdempotencyKey())
                .obVersion(frInternationalScheduledPaymentSubmission2.getObVersion())
                .build();
    }
}