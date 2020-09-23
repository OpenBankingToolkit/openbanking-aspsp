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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.FRInternationalStandingOrderConsent3;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalStandingOrderConsent;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toFRWriteInternationalStandingOrderConsent;

public class FRInternationalStandingOrderConsentConverter {

    public static FRInternationalStandingOrderConsent toFRInternationalStandingOrderConsent(FRInternationalStandingOrderConsent3 frInternationalStandingOrderConsent3) {
        FRInternationalStandingOrderConsent frInternationalScheduledConsent5 = new FRInternationalStandingOrderConsent();
        frInternationalScheduledConsent5.setStatus(frInternationalStandingOrderConsent3.getStatus());
        frInternationalScheduledConsent5.setId(frInternationalStandingOrderConsent3.getId());
        frInternationalScheduledConsent5.setUserId(frInternationalStandingOrderConsent3.getUserId());
        frInternationalScheduledConsent5.setAccountId(frInternationalStandingOrderConsent3.getAccountId());
        frInternationalScheduledConsent5.setCreated(frInternationalStandingOrderConsent3.getCreated());
        frInternationalScheduledConsent5.setInternationalStandingOrderConsent(toFRWriteInternationalStandingOrderConsent(frInternationalStandingOrderConsent3.getInternationalStandingOrderConsent()));
        frInternationalScheduledConsent5.setPispId(frInternationalStandingOrderConsent3.getPispId());
        frInternationalScheduledConsent5.setPispName(frInternationalStandingOrderConsent3.getPispName());
        frInternationalScheduledConsent5.setStatusUpdate(frInternationalStandingOrderConsent3.getStatusUpdate());
        frInternationalScheduledConsent5.setUpdated(frInternationalStandingOrderConsent3.getUpdated());
        return frInternationalScheduledConsent5;
    }
}
