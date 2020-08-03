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

import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRInternationalScheduledConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalScheduledConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalScheduledConsent4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRInternationalScheduledConsent5;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.service.converter.payment.OBWriteInternationalScheduledConsentConverter;

import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteInternationalScheduledConsentConverter.toOBWriteInternationalScheduledConsent1;

@Service
public class FRInternationalScheduledConsentConverter {

    public FRInternationalScheduledConsent2 toFRInternationalConsent2(FRInternationalScheduledConsent1 frInternationalScheduledConsent1) {
        FRInternationalScheduledConsent2 frInternationalScheduledConsent2 = new FRInternationalScheduledConsent2();
        frInternationalScheduledConsent2.setStatus(frInternationalScheduledConsent1.getStatus());
        frInternationalScheduledConsent2.setId(frInternationalScheduledConsent1.getId());
        frInternationalScheduledConsent2.setUserId(frInternationalScheduledConsent1.getUserId());
        frInternationalScheduledConsent2.setAccountId(frInternationalScheduledConsent1.getAccountId());
        frInternationalScheduledConsent2.setCreated(frInternationalScheduledConsent1.getCreated());
        frInternationalScheduledConsent2.setInternationalScheduledConsent(OBWriteInternationalScheduledConsentConverter.toOBWriteInternationalScheduledConsent2(frInternationalScheduledConsent1.getInternationalScheduledConsent()));
        frInternationalScheduledConsent2.setPispId(frInternationalScheduledConsent1.getPispId());
        frInternationalScheduledConsent2.setPispName(frInternationalScheduledConsent1.getPispName());
        frInternationalScheduledConsent2.setStatusUpdate(frInternationalScheduledConsent1.getStatusUpdate());
        frInternationalScheduledConsent2.setUpdated(frInternationalScheduledConsent1.getUpdated());
        return frInternationalScheduledConsent2;
    }

    public FRInternationalScheduledConsent1 toFRInternationalConsent1(FRInternationalScheduledConsent2 frInternationalScheduledConsent2) {
        FRInternationalScheduledConsent1 frInternationalScheduledConsent1 = new FRInternationalScheduledConsent1();
        frInternationalScheduledConsent1.setStatus(frInternationalScheduledConsent2.getStatus());
        frInternationalScheduledConsent1.setId(frInternationalScheduledConsent2.getId());
        frInternationalScheduledConsent1.setUserId(frInternationalScheduledConsent2.getUserId());
        frInternationalScheduledConsent1.setAccountId(frInternationalScheduledConsent2.getAccountId());
        frInternationalScheduledConsent1.setCreated(frInternationalScheduledConsent2.getCreated());
        frInternationalScheduledConsent1.setInternationalScheduledConsent(toOBWriteInternationalScheduledConsent1(frInternationalScheduledConsent2.getInternationalScheduledConsent()));
        frInternationalScheduledConsent1.setPispId(frInternationalScheduledConsent2.getPispId());
        frInternationalScheduledConsent1.setPispName(frInternationalScheduledConsent2.getPispName());
        frInternationalScheduledConsent1.setStatusUpdate(frInternationalScheduledConsent2.getStatusUpdate());
        frInternationalScheduledConsent1.setUpdated(frInternationalScheduledConsent2.getUpdated());
        return frInternationalScheduledConsent1;
    }

    public FRInternationalScheduledConsent1 toFRInternationalConsent1(FRInternationalScheduledConsent4 frInternationalScheduledConsent4) {
        FRInternationalScheduledConsent1 frInternationalScheduledConsent1 = new FRInternationalScheduledConsent1();
        frInternationalScheduledConsent1.setStatus(frInternationalScheduledConsent4.getStatus());
        frInternationalScheduledConsent1.setId(frInternationalScheduledConsent4.getId());
        frInternationalScheduledConsent1.setUserId(frInternationalScheduledConsent4.getUserId());
        frInternationalScheduledConsent1.setAccountId(frInternationalScheduledConsent4.getAccountId());
        frInternationalScheduledConsent1.setCreated(frInternationalScheduledConsent4.getCreated());
        frInternationalScheduledConsent1.setInternationalScheduledConsent(toOBWriteInternationalScheduledConsent1(frInternationalScheduledConsent4.getInternationalScheduledConsent()));
        frInternationalScheduledConsent1.setPispId(frInternationalScheduledConsent4.getPispId());
        frInternationalScheduledConsent1.setPispName(frInternationalScheduledConsent4.getPispName());
        frInternationalScheduledConsent1.setStatusUpdate(frInternationalScheduledConsent4.getStatusUpdate());
        frInternationalScheduledConsent1.setUpdated(frInternationalScheduledConsent4.getUpdated());
        return frInternationalScheduledConsent1;
    }

    public FRInternationalScheduledConsent1 toFRInternationalConsent1(FRInternationalScheduledConsent5 frInternationalScheduledConsent5) {
        FRInternationalScheduledConsent1 frInternationalScheduledConsent1 = new FRInternationalScheduledConsent1();
        frInternationalScheduledConsent1.setStatus(frInternationalScheduledConsent5.getStatus());
        frInternationalScheduledConsent1.setId(frInternationalScheduledConsent5.getId());
        frInternationalScheduledConsent1.setUserId(frInternationalScheduledConsent5.getUserId());
        frInternationalScheduledConsent1.setAccountId(frInternationalScheduledConsent5.getAccountId());
        frInternationalScheduledConsent1.setCreated(frInternationalScheduledConsent5.getCreated());
        frInternationalScheduledConsent1.setInternationalScheduledConsent(toOBWriteInternationalScheduledConsent1(frInternationalScheduledConsent5.getInternationalScheduledConsent()));
        frInternationalScheduledConsent1.setPispId(frInternationalScheduledConsent5.getPispId());
        frInternationalScheduledConsent1.setPispName(frInternationalScheduledConsent5.getPispName());
        frInternationalScheduledConsent1.setStatusUpdate(frInternationalScheduledConsent5.getStatusUpdate());
        frInternationalScheduledConsent1.setUpdated(frInternationalScheduledConsent5.getUpdated());
        return frInternationalScheduledConsent1;
    }
}
