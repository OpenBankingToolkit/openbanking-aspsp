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

import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRDomesticStandingOrderConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticStandingOrderConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRDomesticStandingOrderConsent3;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticStandingOrderConsent5;
import org.springframework.stereotype.Service;

import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticStandingOrderConsentConverter.toOBWriteDomesticStandingOrderConsent1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticStandingOrderConsentConverter.toOBWriteDomesticStandingOrderConsent2;

@Service
public class FRDomesticStandingOrderConsentConverter {

    public FRDomesticStandingOrderConsent2 toFRDomesticConsent2(FRDomesticStandingOrderConsent1 frDomesticStandingOrderConsent1) {
        FRDomesticStandingOrderConsent2 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent2();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent1.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent1.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent1.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent1.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent1.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(toOBWriteDomesticStandingOrderConsent2(frDomesticStandingOrderConsent1.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent1.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent1.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent1.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent1.getUpdated());
        return frDomesticScheduledConsent2;
    }

    public FRDomesticStandingOrderConsent1 toFRDomesticConsent1(FRDomesticStandingOrderConsent2 frDomesticStandingOrderConsent2) {
        FRDomesticStandingOrderConsent1 frDomesticScheduledConsent1 = new FRDomesticStandingOrderConsent1();
        frDomesticScheduledConsent1.setStatus(frDomesticStandingOrderConsent2.getStatus());
        frDomesticScheduledConsent1.setId(frDomesticStandingOrderConsent2.getId());
        frDomesticScheduledConsent1.setUserId(frDomesticStandingOrderConsent2.getUserId());
        frDomesticScheduledConsent1.setAccountId(frDomesticStandingOrderConsent2.getAccountId());
        frDomesticScheduledConsent1.setCreated(frDomesticStandingOrderConsent2.getCreated());
        frDomesticScheduledConsent1.setDomesticStandingOrderConsent(toOBWriteDomesticStandingOrderConsent1(frDomesticStandingOrderConsent2.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent1.setPispId(frDomesticStandingOrderConsent2.getPispId());
        frDomesticScheduledConsent1.setPispName(frDomesticStandingOrderConsent2.getPispName());
        frDomesticScheduledConsent1.setStatusUpdate(frDomesticStandingOrderConsent2.getStatusUpdate());
        frDomesticScheduledConsent1.setUpdated(frDomesticStandingOrderConsent2.getUpdated());
        return frDomesticScheduledConsent1;
    }

    public FRDomesticStandingOrderConsent2 toFRDomesticConsent2(FRDomesticStandingOrderConsent3 frDomesticStandingOrderConsent3) {
        FRDomesticStandingOrderConsent2 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent2();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent3.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent3.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent3.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent3.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent3.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(toOBWriteDomesticStandingOrderConsent2(frDomesticStandingOrderConsent3.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent3.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent3.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent3.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent3.getUpdated());
        return frDomesticScheduledConsent2;
    }


    public FRDomesticStandingOrderConsent1 toFRDomesticConsent1(FRDomesticStandingOrderConsent3 frDomesticStandingOrderConsent3) {
        FRDomesticStandingOrderConsent1 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent1();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent3.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent3.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent3.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent3.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent3.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(toOBWriteDomesticStandingOrderConsent1(frDomesticStandingOrderConsent3.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent3.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent3.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent3.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent3.getUpdated());
        return frDomesticScheduledConsent2;
    }

    public FRDomesticStandingOrderConsent1 toFRDomesticConsent1(FRDomesticStandingOrderConsent5 frDomesticStandingOrderConsent5) {
        FRDomesticStandingOrderConsent1 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent1();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent5.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent5.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent5.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent5.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent5.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(toOBWriteDomesticStandingOrderConsent1(frDomesticStandingOrderConsent5.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent5.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent5.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent5.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent5.getUpdated());
        return frDomesticScheduledConsent2;
    }

    public FRDomesticStandingOrderConsent2 toFRDomesticConsent2(FRDomesticStandingOrderConsent5 frDomesticStandingOrderConsent5) {
        FRDomesticStandingOrderConsent2 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent2();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent5.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent5.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent5.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent5.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent5.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(toOBWriteDomesticStandingOrderConsent2(frDomesticStandingOrderConsent5.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent5.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent5.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent5.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent5.getUpdated());
        return frDomesticScheduledConsent2;
    }
}
