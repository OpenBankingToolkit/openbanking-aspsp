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

import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderData;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalStandingOrder1;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalStandingOrder2;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4Data;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toFRWriteInternationalStandingOrderDataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toOBWriteInternationalStandingOrder4DataInitiation;

public class FRWriteInternationalStandingOrderConverter {

    public static FRWriteInternationalStandingOrder toFRWriteInternationalStandingOrder(OBWriteInternationalStandingOrder1 internationalStandingOrder) {
        return internationalStandingOrder == null ? null : FRWriteInternationalStandingOrder.builder()
                .data(toFRWriteInternationalStandingOrderData(internationalStandingOrder.getData()))
                .risk(toFRRisk(internationalStandingOrder.getRisk()))
                .build();
    }

    public static FRWriteInternationalStandingOrder toFRWriteInternationalStandingOrder(OBWriteInternationalStandingOrder2 internationalStandingOrder) {
        return internationalStandingOrder == null ? null : FRWriteInternationalStandingOrder.builder()
                .data(toFRWriteInternationalStandingOrderData(internationalStandingOrder.getData()))
                .risk(toFRRisk(internationalStandingOrder.getRisk()))
                .build();
    }

    public static FRWriteInternationalStandingOrder toFRWriteInternationalStandingOrder(OBWriteInternationalStandingOrder3 internationalStandingOrder) {
        return internationalStandingOrder == null ? null : FRWriteInternationalStandingOrder.builder()
                .data(toFRWriteInternationalStandingOrderData(internationalStandingOrder.getData()))
                .risk(toFRRisk(internationalStandingOrder.getRisk()))
                .build();
    }

    public static FRWriteInternationalStandingOrder toFRWriteInternationalStandingOrder(OBWriteInternationalStandingOrder4 internationalStandingOrder) {
        return internationalStandingOrder == null ? null : FRWriteInternationalStandingOrder.builder()
                .data(toFRWriteInternationalStandingOrderData(internationalStandingOrder.getData()))
                .risk(toFRRisk(internationalStandingOrder.getRisk()))
                .build();
    }

    public static FRWriteInternationalStandingOrderData toFRWriteInternationalStandingOrderData(OBWriteDataInternationalStandingOrder1 data) {
        return data == null ? null : FRWriteInternationalStandingOrderData.builder()
                .consentId(data.getConsentId())
                .initiation(toFRWriteInternationalStandingOrderDataInitiation(data.getInitiation()))
                .build();
    }

    public static FRWriteInternationalStandingOrderData toFRWriteInternationalStandingOrderData(OBWriteDataInternationalStandingOrder2 data) {
        return data == null ? null : FRWriteInternationalStandingOrderData.builder()
                .consentId(data.getConsentId())
                .initiation(toFRWriteInternationalStandingOrderDataInitiation(data.getInitiation()))
                .build();
    }

    public static FRWriteInternationalStandingOrderData toFRWriteInternationalStandingOrderData(OBWriteDataInternationalStandingOrder3 data) {
        return data == null ? null : FRWriteInternationalStandingOrderData.builder()
                .consentId(data.getConsentId())
                .initiation(toFRWriteInternationalStandingOrderDataInitiation(data.getInitiation()))
                .build();
    }

    public static FRWriteInternationalStandingOrderData toFRWriteInternationalStandingOrderData(OBWriteInternationalStandingOrder4Data data) {
        return data == null ? null : FRWriteInternationalStandingOrderData.builder()
                .consentId(data.getConsentId())
                .initiation(toFRWriteInternationalStandingOrderDataInitiation(data.getInitiation()))
                .build();
    }
}
