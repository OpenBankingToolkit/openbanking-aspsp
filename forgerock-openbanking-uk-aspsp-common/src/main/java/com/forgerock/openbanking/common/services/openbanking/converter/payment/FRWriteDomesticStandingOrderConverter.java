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

import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDataDomesticScheduled;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDataDomesticStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticScheduled;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrder;
import uk.org.openbanking.datamodel.payment.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticScheduledConsentConverter.toOBDomesticScheduled2;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticStandingOrderConsentConverter.*;

public class FRWriteDomesticStandingOrderConverter {

    public static FRWriteDomesticStandingOrder toFRWriteDomesticStandingOrder(OBWriteDomesticStandingOrder1 obWriteDomesticStandingOrder1) {
        return obWriteDomesticStandingOrder1 == null ? null : FRWriteDomesticStandingOrder.builder()
                .data(toFRWriteDataDomesticStandingOrder(obWriteDomesticStandingOrder1.getData()))
                .risk(toFRRisk(obWriteDomesticStandingOrder1.getRisk()))
                .build();
    }

    public static FRWriteDomesticStandingOrder toFRWriteDomesticStandingOrder(OBWriteDomesticStandingOrder2 obWriteDomesticStandingOrder2) {
        return obWriteDomesticStandingOrder2 == null ? null : FRWriteDomesticStandingOrder.builder()
                .data(toFRWriteDataDomesticStandingOrder(obWriteDomesticStandingOrder2.getData()))
                .risk(toFRRisk(obWriteDomesticStandingOrder2.getRisk()))
                .build();
    }

    public static FRWriteDomesticStandingOrder toFRWriteDomesticStandingOrder(OBWriteDomesticStandingOrder3 obWriteDomesticStandingOrder3) {
        return obWriteDomesticStandingOrder3 == null ? null : FRWriteDomesticStandingOrder.builder()
                .data(toFRWriteDataDomesticStandingOrder(obWriteDomesticStandingOrder3.getData()))
                .risk(toFRRisk(obWriteDomesticStandingOrder3.getRisk()))
                .build();
    }

    public static FRWriteDataDomesticStandingOrder toFRWriteDataDomesticStandingOrder(OBWriteDataDomesticStandingOrder1 data) {
        return data == null ? null : FRWriteDataDomesticStandingOrder.builder()
                .consentId(data.getConsentId())
                .initiation(toFRWriteDomesticStandingOrderDataInitiation(data.getInitiation()))
                .build();
    }

    public static FRWriteDataDomesticStandingOrder toFRWriteDataDomesticStandingOrder(OBWriteDataDomesticStandingOrder2 data) {
        return data == null ? null : FRWriteDataDomesticStandingOrder.builder()
                .consentId(data.getConsentId())
                .initiation(toFRWriteDomesticStandingOrderDataInitiation(data.getInitiation()))
                .build();
    }

    public static FRWriteDataDomesticStandingOrder toFRWriteDataDomesticStandingOrder(OBWriteDataDomesticStandingOrder3 data) {
        return data == null ? null : FRWriteDataDomesticStandingOrder.builder()
                .consentId(data.getConsentId())
                .initiation(toFRWriteDomesticStandingOrderDataInitiation(data.getInitiation()))
                .build();
    }

    public static FRWriteDataDomesticStandingOrder toFRWriteDataDomesticStandingOrder(OBWriteDomesticStandingOrder3Data data) {
        return data == null ? null : FRWriteDataDomesticStandingOrder.builder()
                .consentId(data.getConsentId())
                .initiation(toFRWriteDomesticStandingOrderDataInitiation(data.getInitiation()))
                .build();
    }

    public static FRWriteDataDomesticStandingOrder toFRWriteDomesticStandingOrder(OBWriteDataDomesticStandingOrder3 data) {
        return data == null ? null : FRWriteDataDomesticStandingOrder.builder()
                .consentId(data.getConsentId())
                .initiation(toFRWriteDomesticStandingOrderDataInitiation(data.getInitiation()))
                .build();
    }

    //FR to OB
    public static OBWriteDomesticStandingOrder3 toOBWriteDomesticDomesticStandingOrder3(FRWriteDomesticStandingOrder domesticStandingOrderPayment) {
        return domesticStandingOrderPayment == null ? null : new OBWriteDomesticStandingOrder3()
                .data(toOBWriteDataDomesticDomesticStandingOrder3(domesticStandingOrderPayment.getData()))
                .risk(toOBRisk1(domesticStandingOrderPayment.getRisk()));
    }

    public static OBWriteDomesticStandingOrder3Data toOBWriteDataDomesticDomesticStandingOrder3(FRWriteDataDomesticStandingOrder data) {
        return data == null ? null : new OBWriteDomesticStandingOrder3Data()
                .consentId(data.getConsentId())
                .initiation(toOBWriteDomesticStandingOrder3DataInitiation(data.getInitiation()));
    }
}
