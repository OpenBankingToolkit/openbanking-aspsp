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

import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDataDomestic;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomestic;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomestic1;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomestic2;
import uk.org.openbanking.datamodel.payment.OBWriteDomestic1;
import uk.org.openbanking.datamodel.payment.OBWriteDomestic2;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConsentConverter.toFRWriteDomesticDataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConsentConverter.toOBDomestic2;

public class FRWriteDomesticConverter {

    // OB to FR
    public static FRWriteDomestic toFRWriteDomestic(OBWriteDomestic1 obWriteDomestic1) {
        return obWriteDomestic1 == null ? null : FRWriteDomestic.builder()
                .data(toFRWriteDataDomestic(obWriteDomestic1.getData()))
                .risk(toFRRisk(obWriteDomestic1.getRisk()))
                .build();
    }

    public static FRWriteDomestic toFRWriteDomestic(OBWriteDomestic2 obWriteDomestic2) {
        return obWriteDomestic2 == null ? null : FRWriteDomestic.builder()
                .data(toFRWriteDataDomestic(obWriteDomestic2.getData()))
                .risk(toFRRisk(obWriteDomestic2.getRisk()))
                .build();
    }

    public static FRWriteDataDomestic toFRWriteDataDomestic(OBWriteDataDomestic1 data) {
        return data == null ? null : FRWriteDataDomestic.builder()
                .consentId(data.getConsentId())
                .initiation(toFRWriteDomesticDataInitiation(data.getInitiation()))
                .build();
    }

    public static FRWriteDataDomestic toFRWriteDataDomestic(OBWriteDataDomestic2 data) {
        return data == null ? null : FRWriteDataDomestic.builder()
                .consentId(data.getConsentId())
                .initiation(toFRWriteDomesticDataInitiation(data.getInitiation()))
                .build();
    }

    // FR to OB
    public static OBWriteDomestic2 toOBWriteDomestic2(FRWriteDomestic domesticPayment) {
        return domesticPayment == null ? null : new OBWriteDomestic2()
                .data(toOBWriteDataDomestic2(domesticPayment.getData()))
                .risk(toOBRisk1(domesticPayment.getRisk()));
    }

    public static OBWriteDataDomestic2 toOBWriteDataDomestic2(FRWriteDataDomestic data) {
        return data == null ? null : new OBWriteDataDomestic2()
                .consentId(data.getConsentId())
                .initiation(toOBDomestic2(data.getInitiation()));
    }
}
