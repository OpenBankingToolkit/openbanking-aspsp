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


import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRDomesticScheduledConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticScheduledConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRDomesticScheduledConsent4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticScheduledConsent5;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.payment.OBExternalPermissions2Code;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticScheduledConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent4Data;
import uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticScheduledConsentConverter;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDomesticStandingOrderConsentConverter.toOBAuthorisation1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBDomesticScheduledConverter.toOBDomesticScheduled1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticScheduledConsentConverter.toOBWriteDomesticScheduledConsent2;

@Service
public class FRDomesticScheduledConsentConverter {

    public FRDomesticScheduledConsent2 toFRDomesticConsent2(FRDomesticScheduledConsent1 frDomesticScheduledConsent1) {
        FRDomesticScheduledConsent2 frDomesticScheduledConsent2 = new FRDomesticScheduledConsent2();
        frDomesticScheduledConsent2.setStatus(frDomesticScheduledConsent1.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticScheduledConsent1.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticScheduledConsent1.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticScheduledConsent1.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticScheduledConsent1.getCreated());
        frDomesticScheduledConsent2.setDomesticScheduledConsent(toOBWriteDomesticScheduledConsent2(frDomesticScheduledConsent1.getDomesticScheduledConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticScheduledConsent1.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticScheduledConsent1.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticScheduledConsent1.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticScheduledConsent1.getUpdated());
        return frDomesticScheduledConsent2;
    }

    public FRDomesticScheduledConsent1 toFRDomesticConsent1(FRDomesticScheduledConsent2 frDomesticScheduledConsent2) {
        FRDomesticScheduledConsent1 frDomesticScheduledConsent1 = new FRDomesticScheduledConsent1();
        frDomesticScheduledConsent1.setStatus(frDomesticScheduledConsent2.getStatus());
        frDomesticScheduledConsent1.setId(frDomesticScheduledConsent2.getId());
        frDomesticScheduledConsent1.setUserId(frDomesticScheduledConsent2.getUserId());
        frDomesticScheduledConsent1.setAccountId(frDomesticScheduledConsent2.getAccountId());
        frDomesticScheduledConsent1.setCreated(frDomesticScheduledConsent2.getCreated());
        frDomesticScheduledConsent1.setDomesticScheduledConsent(OBWriteDomesticScheduledConsentConverter.toOBWriteDomesticScheduledConsent1(frDomesticScheduledConsent2.getDomesticScheduledConsent()));
        frDomesticScheduledConsent1.setPispId(frDomesticScheduledConsent2.getPispId());
        frDomesticScheduledConsent1.setPispName(frDomesticScheduledConsent2.getPispName());
        frDomesticScheduledConsent1.setStatusUpdate(frDomesticScheduledConsent2.getStatusUpdate());
        frDomesticScheduledConsent1.setUpdated(frDomesticScheduledConsent2.getUpdated());
        return frDomesticScheduledConsent1;
    }

    public FRDomesticScheduledConsent1 toFRDomesticConsent1(FRDomesticScheduledConsent4 frDomesticScheduledConsent4) {
        FRDomesticScheduledConsent1 frDomesticScheduledConsent1 = new FRDomesticScheduledConsent1();
        frDomesticScheduledConsent1.setStatus(frDomesticScheduledConsent4.getStatus());
        frDomesticScheduledConsent1.setId(frDomesticScheduledConsent4.getId());
        frDomesticScheduledConsent1.setUserId(frDomesticScheduledConsent4.getUserId());
        frDomesticScheduledConsent1.setAccountId(frDomesticScheduledConsent4.getAccountId());
        frDomesticScheduledConsent1.setCreated(frDomesticScheduledConsent4.getCreated());
        frDomesticScheduledConsent1.setDomesticScheduledConsent(OBWriteDomesticScheduledConsentConverter.toOBWriteDomesticScheduledConsent1(frDomesticScheduledConsent4.getDomesticScheduledConsent()));
        frDomesticScheduledConsent1.setPispId(frDomesticScheduledConsent4.getPispId());
        frDomesticScheduledConsent1.setPispName(frDomesticScheduledConsent4.getPispName());
        frDomesticScheduledConsent1.setStatusUpdate(frDomesticScheduledConsent4.getStatusUpdate());
        frDomesticScheduledConsent1.setUpdated(frDomesticScheduledConsent4.getUpdated());
        return frDomesticScheduledConsent1;
    }

    public FRDomesticScheduledConsent1 toFRDomesticConsent1(FRDomesticScheduledConsent5 frDomesticScheduledConsent5) {
        FRDomesticScheduledConsent1 frDomesticScheduledConsent1 = new FRDomesticScheduledConsent1();
        frDomesticScheduledConsent1.setStatus(frDomesticScheduledConsent5.getStatus());
        frDomesticScheduledConsent1.setId(frDomesticScheduledConsent5.getId());
        frDomesticScheduledConsent1.setUserId(frDomesticScheduledConsent5.getUserId());
        frDomesticScheduledConsent1.setAccountId(frDomesticScheduledConsent5.getAccountId());
        frDomesticScheduledConsent1.setCreated(frDomesticScheduledConsent5.getCreated());
        frDomesticScheduledConsent1.setDomesticScheduledConsent(toOBWriteDomesticScheduledConsent1(frDomesticScheduledConsent5.getDomesticScheduledConsent()));
        frDomesticScheduledConsent1.setPispId(frDomesticScheduledConsent5.getPispId());
        frDomesticScheduledConsent1.setPispName(frDomesticScheduledConsent5.getPispName());
        frDomesticScheduledConsent1.setStatusUpdate(frDomesticScheduledConsent5.getStatusUpdate());
        frDomesticScheduledConsent1.setUpdated(frDomesticScheduledConsent5.getUpdated());
        return frDomesticScheduledConsent1;
    }

    // TODO #272 - move to uk-datamodel
    public static OBWriteDomesticScheduledConsent1 toOBWriteDomesticScheduledConsent1(OBWriteDomesticScheduledConsent4 domesticScheduledConsent4) {
        return domesticScheduledConsent4 == null ? null : (new OBWriteDomesticScheduledConsent1())
                .data(toOBWriteDataDomesticScheduledConsent1(domesticScheduledConsent4.getData()))
                .risk(domesticScheduledConsent4.getRisk());
    }

    public static OBWriteDataDomesticScheduledConsent1 toOBWriteDataDomesticScheduledConsent1(OBWriteDomesticScheduledConsent4Data data) {
        return data == null ? null : (new OBWriteDataDomesticScheduledConsent1())
                .permission(OBExternalPermissions2Code.valueOf(data.getPermission().name()))
                .initiation(toOBDomesticScheduled1(data.getInitiation()))
                .authorisation(toOBAuthorisation1(data.getAuthorisation()));
    }
}
