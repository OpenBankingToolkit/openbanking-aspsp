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

import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRDomesticConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticConsent2;
import org.springframework.stereotype.Service;

import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticConsentConverter.toOBWriteDomesticConsent1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticConsentConverter.toOBWriteDomesticConsent2;

@Service
public class FRDomesticConsentConverter {

    public FRDomesticConsent2 toFRDomesticConsent2(FRDomesticConsent1 frDomesticConsent1) {
        FRDomesticConsent2 frDomesticConsent2 = new FRDomesticConsent2();

        frDomesticConsent2.setId(frDomesticConsent1.getId());
        frDomesticConsent2.setStatus(frDomesticConsent1.getStatus());
        frDomesticConsent2.setUserId(frDomesticConsent1.getUserId());
        frDomesticConsent2.setAccountId(frDomesticConsent1.getAccountId());
        frDomesticConsent2.setCreated(frDomesticConsent1.getCreated());
        frDomesticConsent2.setDomesticConsent(toOBWriteDomesticConsent2(frDomesticConsent1.getDomesticConsent()));
        frDomesticConsent2.setPispId(frDomesticConsent1.getPispId());
        frDomesticConsent2.setPispName(frDomesticConsent1.getPispName());
        frDomesticConsent2.setStatusUpdate(frDomesticConsent1.getStatusUpdate());

        return frDomesticConsent2;
    }

    public FRDomesticConsent1 toFRDomesticConsent1(FRDomesticConsent2 frDomesticConsent2) {
        FRDomesticConsent1 frDomesticConsent1 = new FRDomesticConsent1();

        frDomesticConsent1.setId(frDomesticConsent2.getId());
        frDomesticConsent1.setStatus(frDomesticConsent2.getStatus());
        frDomesticConsent1.setUserId(frDomesticConsent2.getUserId());
        frDomesticConsent1.setAccountId(frDomesticConsent2.getAccountId());
        frDomesticConsent1.setCreated(frDomesticConsent2.getCreated());
        frDomesticConsent1.setDomesticConsent(toOBWriteDomesticConsent1(frDomesticConsent2.getDomesticConsent()));
        frDomesticConsent1.setPispId(frDomesticConsent2.getPispId());
        frDomesticConsent1.setPispName(frDomesticConsent2.getPispName());
        frDomesticConsent1.setStatusUpdate(frDomesticConsent2.getStatusUpdate());

        return frDomesticConsent1;
    }
}
