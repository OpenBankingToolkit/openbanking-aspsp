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
package com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6;

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.FRInternationalConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRInternationalConsent5;

import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteInternationalConsentConverter.toOBWriteInternationalConsent5;

public class FRInternationalConsentConverter {

    public static FRInternationalConsent5 toFRInternationalConsent5(FRInternationalConsent2 frInternationalConsent2) {
        FRInternationalConsent5 frInternationalConsent5 = new FRInternationalConsent5();

        frInternationalConsent5.setId(frInternationalConsent2.getId());
        frInternationalConsent5.setStatus(frInternationalConsent2.getStatus());
        frInternationalConsent5.setUserId(frInternationalConsent2.getUserId());
        frInternationalConsent5.setAccountId(frInternationalConsent2.getAccountId());
        frInternationalConsent5.setCreated(frInternationalConsent2.getCreated());
        frInternationalConsent5.setInternationalConsent(toOBWriteInternationalConsent5(frInternationalConsent2.getInternationalConsent()));
        frInternationalConsent5.setPispId(frInternationalConsent2.getPispId());
        frInternationalConsent5.setPispName(frInternationalConsent2.getPispName());
        frInternationalConsent5.setStatusUpdate(frInternationalConsent2.getStatusUpdate());

        return frInternationalConsent5;
    }
}
