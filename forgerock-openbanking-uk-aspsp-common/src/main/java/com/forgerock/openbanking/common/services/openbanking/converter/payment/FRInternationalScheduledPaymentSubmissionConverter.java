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
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalScheduledPaymentSubmission2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalScheduledPaymentSubmission4;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalScheduled2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled3Data;

import static uk.org.openbanking.datamodel.service.converter.payment.OBInternationalScheduledConverter.toOBWriteInternationalScheduled3DataInitiation;

public class FRInternationalScheduledPaymentSubmissionConverter {

    public static FRInternationalScheduledPaymentSubmission4 toFRInternationalScheduledPaymentSubmission4(FRInternationalScheduledPaymentSubmission2 frInternationalScheduledPaymentSubmission2) {
        return frInternationalScheduledPaymentSubmission2 == null ? null : FRInternationalScheduledPaymentSubmission4.builder()
                .id(frInternationalScheduledPaymentSubmission2.getId())
                .internationalScheduledPayment(toOBWriteInternationalScheduled3(frInternationalScheduledPaymentSubmission2.getInternationalScheduledPayment()))
                .created(frInternationalScheduledPaymentSubmission2.getCreated())
                .updated(frInternationalScheduledPaymentSubmission2.getUpdated())
                .idempotencyKey(frInternationalScheduledPaymentSubmission2.getIdempotencyKey())
                .obVersion(frInternationalScheduledPaymentSubmission2.getObVersion())
                .build();
    }

    public static OBWriteInternationalScheduled3 toOBWriteInternationalScheduled3(OBWriteInternationalScheduled2 obWriteInternationalScheduled2) {
        return obWriteInternationalScheduled2 == null ? null : (new OBWriteInternationalScheduled3())
                .data(toOBWriteInternationalScheduled3Data(obWriteInternationalScheduled2.getData()))
                .risk(obWriteInternationalScheduled2.getRisk());
    }

    public static OBWriteInternationalScheduled3Data toOBWriteInternationalScheduled3Data(OBWriteDataInternationalScheduled2 data) {
        return data == null ? null : new OBWriteInternationalScheduled3Data()
                .consentId(data.getConsentId())
                .initiation(toOBWriteInternationalScheduled3DataInitiation(data.getInitiation()));
    }
}
