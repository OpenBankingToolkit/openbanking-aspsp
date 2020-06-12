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

import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRInternationalStandingOrderConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalStandingOrderConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRInternationalStandingOrderConsent3;
import org.springframework.stereotype.Service;

import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteInternationalStandingOrderConsentConverter.toOBWriteInternationalStandingOrderConsent1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteInternationalStandingOrderConsentConverter.toOBWriteInternationalStandingOrderConsent2;

@Service
public class FRInternationalStandingOrderConsentConverter {

    public FRInternationalStandingOrderConsent2 toFRInternationalConsent2(FRInternationalStandingOrderConsent1 frInternationalStandingOrderConsent1) {
        FRInternationalStandingOrderConsent2 frInternationalScheduledConsent2 = new FRInternationalStandingOrderConsent2();
        frInternationalScheduledConsent2.setStatus(frInternationalStandingOrderConsent1.getStatus());
        frInternationalScheduledConsent2.setId(frInternationalStandingOrderConsent1.getId());
        frInternationalScheduledConsent2.setUserId(frInternationalStandingOrderConsent1.getUserId());
        frInternationalScheduledConsent2.setAccountId(frInternationalStandingOrderConsent1.getAccountId());
        frInternationalScheduledConsent2.setCreated(frInternationalStandingOrderConsent1.getCreated());
        frInternationalScheduledConsent2.setInternationalStandingOrderConsent(toOBWriteInternationalStandingOrderConsent2(frInternationalStandingOrderConsent1.getInternationalStandingOrderConsent()));
        frInternationalScheduledConsent2.setPispId(frInternationalStandingOrderConsent1.getPispId());
        frInternationalScheduledConsent2.setPispName(frInternationalStandingOrderConsent1.getPispName());
        frInternationalScheduledConsent2.setStatusUpdate(frInternationalStandingOrderConsent1.getStatusUpdate());
        frInternationalScheduledConsent2.setUpdated(frInternationalStandingOrderConsent1.getUpdated());
        return frInternationalScheduledConsent2;
    }

    public FRInternationalStandingOrderConsent1 toFRInternationalConsent1(FRInternationalStandingOrderConsent2 frInternationalStandingOrderConsent2) {
        FRInternationalStandingOrderConsent1 frInternationalScheduledConsent1 = new FRInternationalStandingOrderConsent1();
        frInternationalScheduledConsent1.setStatus(frInternationalStandingOrderConsent2.getStatus());
        frInternationalScheduledConsent1.setId(frInternationalStandingOrderConsent2.getId());
        frInternationalScheduledConsent1.setUserId(frInternationalStandingOrderConsent2.getUserId());
        frInternationalScheduledConsent1.setAccountId(frInternationalStandingOrderConsent2.getAccountId());
        frInternationalScheduledConsent1.setCreated(frInternationalStandingOrderConsent2.getCreated());
        frInternationalScheduledConsent1.setInternationalStandingOrderConsent(toOBWriteInternationalStandingOrderConsent1(frInternationalStandingOrderConsent2.getInternationalStandingOrderConsent()));
        frInternationalScheduledConsent1.setPispId(frInternationalStandingOrderConsent2.getPispId());
        frInternationalScheduledConsent1.setPispName(frInternationalStandingOrderConsent2.getPispName());
        frInternationalScheduledConsent1.setStatusUpdate(frInternationalStandingOrderConsent2.getStatusUpdate());
        frInternationalScheduledConsent1.setUpdated(frInternationalStandingOrderConsent2.getUpdated());
        return frInternationalScheduledConsent1;
    }

    public FRInternationalStandingOrderConsent1 toFRInternationalConsent1(FRInternationalStandingOrderConsent3 frInternationalStandingOrderConsent3) {
        FRInternationalStandingOrderConsent1 frInternationalScheduledConsent1 = new FRInternationalStandingOrderConsent1();
        frInternationalScheduledConsent1.setStatus(frInternationalStandingOrderConsent3.getStatus());
        frInternationalScheduledConsent1.setId(frInternationalStandingOrderConsent3.getId());
        frInternationalScheduledConsent1.setUserId(frInternationalStandingOrderConsent3.getUserId());
        frInternationalScheduledConsent1.setAccountId(frInternationalStandingOrderConsent3.getAccountId());
        frInternationalScheduledConsent1.setCreated(frInternationalStandingOrderConsent3.getCreated());
        frInternationalScheduledConsent1.setInternationalStandingOrderConsent(toOBWriteInternationalStandingOrderConsent1(frInternationalStandingOrderConsent3.getInternationalStandingOrderConsent()));
        frInternationalScheduledConsent1.setPispId(frInternationalStandingOrderConsent3.getPispId());
        frInternationalScheduledConsent1.setPispName(frInternationalStandingOrderConsent3.getPispName());
        frInternationalScheduledConsent1.setStatusUpdate(frInternationalStandingOrderConsent3.getStatusUpdate());
        frInternationalScheduledConsent1.setUpdated(frInternationalStandingOrderConsent3.getUpdated());
        return frInternationalScheduledConsent1;
    }

    public FRInternationalStandingOrderConsent2 toFRInternationalConsent2(FRInternationalStandingOrderConsent3 frInternationalStandingOrderConsent2) {
        FRInternationalStandingOrderConsent2 frInternationalScheduledConsent2 = new FRInternationalStandingOrderConsent2();
        frInternationalScheduledConsent2.setStatus(frInternationalStandingOrderConsent2.getStatus());
        frInternationalScheduledConsent2.setId(frInternationalStandingOrderConsent2.getId());
        frInternationalScheduledConsent2.setUserId(frInternationalStandingOrderConsent2.getUserId());
        frInternationalScheduledConsent2.setAccountId(frInternationalStandingOrderConsent2.getAccountId());
        frInternationalScheduledConsent2.setCreated(frInternationalStandingOrderConsent2.getCreated());
        frInternationalScheduledConsent2.setInternationalStandingOrderConsent(toOBWriteInternationalStandingOrderConsent2(frInternationalStandingOrderConsent2.getInternationalStandingOrderConsent()));
        frInternationalScheduledConsent2.setPispId(frInternationalStandingOrderConsent2.getPispId());
        frInternationalScheduledConsent2.setPispName(frInternationalStandingOrderConsent2.getPispName());
        frInternationalScheduledConsent2.setStatusUpdate(frInternationalStandingOrderConsent2.getStatusUpdate());
        frInternationalScheduledConsent2.setUpdated(frInternationalStandingOrderConsent2.getUpdated());
        return frInternationalScheduledConsent2;
    }

}
