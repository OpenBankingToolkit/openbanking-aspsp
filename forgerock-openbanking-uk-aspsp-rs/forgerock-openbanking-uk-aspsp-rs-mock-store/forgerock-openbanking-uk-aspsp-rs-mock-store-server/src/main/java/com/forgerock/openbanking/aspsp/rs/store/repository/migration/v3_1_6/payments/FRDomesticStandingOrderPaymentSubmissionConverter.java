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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.payments.FRDomesticStandingOrderPaymentSubmission3;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticStandingOrderPaymentSubmission;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticStandingOrderConverter.toFRWriteDomesticStandingOrder;

public class FRDomesticStandingOrderPaymentSubmissionConverter {

    public static FRDomesticStandingOrderPaymentSubmission toFRDomesticStandingOrderPaymentSubmission(FRDomesticStandingOrderPaymentSubmission3 submission3) {
        return submission3 == null ? null : FRDomesticStandingOrderPaymentSubmission.builder()
                .id(submission3.getId())
                .domesticStandingOrder(toFRWriteDomesticStandingOrder(submission3.getDomesticStandingOrder()))
                .created(submission3.getCreated())
                .updated(submission3.getUpdated())
                .idempotencyKey(submission3.getIdempotencyKey())
                .obVersion(submission3.getObVersion())
                .build();
    }
}
