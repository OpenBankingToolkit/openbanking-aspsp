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

import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRInternationalStandingOrderPaymentSubmission3;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalStandingOrderPaymentSubmission4;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4Data;

import static uk.org.openbanking.datamodel.service.converter.payment.OBInternationalStandingOrderConverter.toOBWriteInternationalStandingOrder4DataInitiation;

public class FRInternationalStandingOrderPaymentSubmissionConverter {

    public static FRInternationalStandingOrderPaymentSubmission4 toFRInternationalStandingOrderPaymentSubmission4(FRInternationalStandingOrderPaymentSubmission3 frInternationalStandingOrderPaymentSubmission3) {
        return frInternationalStandingOrderPaymentSubmission3 == null ? null : FRInternationalStandingOrderPaymentSubmission4.builder()
                .id(frInternationalStandingOrderPaymentSubmission3.getId())
                .internationalStandingOrder(toOBWriteInternationalStandingOrder4(frInternationalStandingOrderPaymentSubmission3.getInternationalStandingOrder()))
                .created(frInternationalStandingOrderPaymentSubmission3.getCreated())
                .updated(frInternationalStandingOrderPaymentSubmission3.getUpdated())
                .idempotencyKey(frInternationalStandingOrderPaymentSubmission3.getIdempotencyKey())
                .version(frInternationalStandingOrderPaymentSubmission3.obVersion)
                .build();
    }

    public static OBWriteInternationalStandingOrder4 toOBWriteInternationalStandingOrder4(OBWriteInternationalStandingOrder3 obWriteInternationalStandingOrder3) {
        return obWriteInternationalStandingOrder3 == null ? null : (new OBWriteInternationalStandingOrder4())
                .data(toOBWriteInternationalStandingOrder4Data(obWriteInternationalStandingOrder3.getData()))
                .risk(obWriteInternationalStandingOrder3.getRisk());
    }

    public static OBWriteInternationalStandingOrder4Data toOBWriteInternationalStandingOrder4Data(OBWriteDataInternationalStandingOrder3 data) {
        return data == null ? null : (new OBWriteInternationalStandingOrder4Data())
                .consentId(data.getConsentId())
                .initiation(toOBWriteInternationalStandingOrder4DataInitiation(data.getInitiation()));
    }
}
