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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.FRDomesticStandingOrderConsent3;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticStandingOrderConsent;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticStandingOrderConsentConverter.toFRWriteDomesticStandingOrderConsent;

public class FRDomesticStandingOrderConsentConverter {

    public static FRDomesticStandingOrderConsent toFRDomesticStandingOrderConsent(FRDomesticStandingOrderConsent3 frDomesticStandingOrderConsent3) {
        FRDomesticStandingOrderConsent frDomesticScheduledConsent5 = new FRDomesticStandingOrderConsent();
        frDomesticScheduledConsent5.setStatus(frDomesticStandingOrderConsent3.getStatus());
        frDomesticScheduledConsent5.setId(frDomesticStandingOrderConsent3.getId());
        frDomesticScheduledConsent5.setUserId(frDomesticStandingOrderConsent3.getUserId());
        frDomesticScheduledConsent5.setAccountId(frDomesticStandingOrderConsent3.getAccountId());
        frDomesticScheduledConsent5.setCreated(frDomesticStandingOrderConsent3.getCreated());
        frDomesticScheduledConsent5.setDomesticStandingOrderConsent(toFRWriteDomesticStandingOrderConsent(frDomesticStandingOrderConsent3.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent5.setPispId(frDomesticStandingOrderConsent3.getPispId());
        frDomesticScheduledConsent5.setPispName(frDomesticStandingOrderConsent3.getPispName());
        frDomesticScheduledConsent5.setStatusUpdate(frDomesticStandingOrderConsent3.getStatusUpdate());
        frDomesticScheduledConsent5.setUpdated(frDomesticStandingOrderConsent3.getUpdated());
        return frDomesticScheduledConsent5;
    }
}
