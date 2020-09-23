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
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalScheduled1;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalScheduled2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled3Data;

import static uk.org.openbanking.datamodel.service.converter.payment.OBInternationalScheduledConverter.toOBWriteInternationalScheduled3DataInitiation;

public class FRInternationalScheduledPaymentSubmissionConverter {

    public static FRInternationalScheduledPaymentSubmission toFRInternationalScheduledPaymentSubmission(InternationalScheduledPaymentSubmission2 frInternationalScheduledPaymentSubmission2) {
        return frInternationalScheduledPaymentSubmission2 == null ? null : FRInternationalScheduledPaymentSubmission.builder()
                .id(frInternationalScheduledPaymentSubmission2.getId())
                .internationalScheduledPayment(toOBWriteInternationalScheduled3(frInternationalScheduledPaymentSubmission2.getInternationalScheduledPayment()))
                .created(frInternationalScheduledPaymentSubmission2.getCreated())
                .updated(frInternationalScheduledPaymentSubmission2.getUpdated())
                .idempotencyKey(frInternationalScheduledPaymentSubmission2.getIdempotencyKey())
                .obVersion(frInternationalScheduledPaymentSubmission2.getObVersion())
                .build();
    }

    public static OBWriteInternationalScheduled3 toOBWriteInternationalScheduled2(OBWriteInternationalScheduled1 obWriteInternationalScheduled1) {
        return obWriteInternationalScheduled1 == null ? null : (new OBWriteInternationalScheduled3())
                .data(toOBWriteInternationalScheduled3Data(obWriteInternationalScheduled1.getData()))
                .risk(obWriteInternationalScheduled1.getRisk());
    }

    public static OBWriteInternationalScheduled3 toOBWriteInternationalScheduled3(OBWriteInternationalScheduled2 obWriteInternationalScheduled2) {
        return obWriteInternationalScheduled2 == null ? null : (new OBWriteInternationalScheduled3())
                .data(toOBWriteInternationalScheduled3Data(obWriteInternationalScheduled2.getData()))
                .risk(obWriteInternationalScheduled2.getRisk());
    }

    public static OBWriteInternationalScheduled3Data toOBWriteInternationalScheduled3Data(OBWriteDataInternationalScheduled1 data) {
        return data == null ? null : new OBWriteInternationalScheduled3Data()
                .consentId(data.getConsentId())
                .initiation(toOBWriteInternationalScheduled3DataInitiation(data.getInitiation()));
    }

    public static OBWriteInternationalScheduled3Data toOBWriteInternationalScheduled3Data(OBWriteDataInternationalScheduled2 data) {
        return data == null ? null : new OBWriteInternationalScheduled3Data()
                .consentId(data.getConsentId())
                .initiation(toOBWriteInternationalScheduled3DataInitiation(data.getInitiation()));
    }
}
