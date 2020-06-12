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

import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRInternationalConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalConsent2;
import org.springframework.stereotype.Service;

import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteInternationalConsentConverter.toOBWriteInternationalConsent1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteInternationalConsentConverter.toOBWriteInternationalConsent2;

@Service
public class FRInternationalConsentConverter {

    public FRInternationalConsent2 toFRInternationalConsent2(FRInternationalConsent1 frInternationalConsent1) {
        FRInternationalConsent2 frInternationalConsent2 = new FRInternationalConsent2();

        frInternationalConsent2.setId(frInternationalConsent1.getId());
        frInternationalConsent2.setStatus(frInternationalConsent1.getStatus());
        frInternationalConsent2.setUserId(frInternationalConsent1.getUserId());
        frInternationalConsent2.setAccountId(frInternationalConsent1.getAccountId());
        frInternationalConsent2.setCreated(frInternationalConsent1.getCreated());
        frInternationalConsent2.setInternationalConsent(toOBWriteInternationalConsent2(frInternationalConsent1.getInternationalConsent()));
        frInternationalConsent2.setPispId(frInternationalConsent1.getPispId());
        frInternationalConsent2.setPispName(frInternationalConsent1.getPispName());
        frInternationalConsent2.setStatusUpdate(frInternationalConsent1.getStatusUpdate());

        return frInternationalConsent2;
    }

    public FRInternationalConsent1 toFRInternationalConsent1(FRInternationalConsent2 frInternationalConsent2) {
        FRInternationalConsent1 frInternationalConsent1 = new FRInternationalConsent1();

        frInternationalConsent1.setId(frInternationalConsent2.getId());
        frInternationalConsent1.setStatus(frInternationalConsent2.getStatus());
        frInternationalConsent1.setUserId(frInternationalConsent2.getUserId());
        frInternationalConsent1.setAccountId(frInternationalConsent2.getAccountId());
        frInternationalConsent1.setCreated(frInternationalConsent2.getCreated());
        frInternationalConsent1.setInternationalConsent(toOBWriteInternationalConsent1(frInternationalConsent2.getInternationalConsent()));
        frInternationalConsent1.setPispId(frInternationalConsent2.getPispId());
        frInternationalConsent1.setPispName(frInternationalConsent2.getPispName());
        frInternationalConsent1.setStatusUpdate(frInternationalConsent2.getStatusUpdate());

        return frInternationalConsent1;
    }
}
