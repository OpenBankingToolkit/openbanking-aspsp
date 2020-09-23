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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.FRInternationalScheduledConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRInternationalScheduledConsent5;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConsentConverter.toFRWriteInternationalScheduledConsent;

public class FRInternationalScheduledConsentConverter {

    public static FRInternationalScheduledConsent5 toFRInternationalScheduledConsent5(FRInternationalScheduledConsent2 frInternationalScheduledConsent2) {
        FRInternationalScheduledConsent5 frInternationalScheduledConsent5 = new FRInternationalScheduledConsent5();
        frInternationalScheduledConsent5.setStatus(frInternationalScheduledConsent2.getStatus());
        frInternationalScheduledConsent5.setId(frInternationalScheduledConsent2.getId());
        frInternationalScheduledConsent5.setUserId(frInternationalScheduledConsent2.getUserId());
        frInternationalScheduledConsent5.setAccountId(frInternationalScheduledConsent2.getAccountId());
        frInternationalScheduledConsent5.setCreated(frInternationalScheduledConsent2.getCreated());
        frInternationalScheduledConsent5.setInternationalScheduledConsent(toFRWriteInternationalScheduledConsent(frInternationalScheduledConsent2.getInternationalScheduledConsent()));
        frInternationalScheduledConsent5.setPispId(frInternationalScheduledConsent2.getPispId());
        frInternationalScheduledConsent5.setPispName(frInternationalScheduledConsent2.getPispName());
        frInternationalScheduledConsent5.setStatusUpdate(frInternationalScheduledConsent2.getStatusUpdate());
        frInternationalScheduledConsent5.setUpdated(frInternationalScheduledConsent2.getUpdated());
        return frInternationalScheduledConsent5;
    }
}
