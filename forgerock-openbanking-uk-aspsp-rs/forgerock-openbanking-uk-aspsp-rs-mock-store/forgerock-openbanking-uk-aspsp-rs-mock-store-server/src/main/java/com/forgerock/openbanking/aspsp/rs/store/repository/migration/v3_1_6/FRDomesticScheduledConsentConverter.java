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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.FRDomesticScheduledConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticScheduledConsent5;

import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticScheduledConsentConverter.toOBWriteDomesticScheduledConsent4;

public class FRDomesticScheduledConsentConverter {

    public static FRDomesticScheduledConsent5 toFRDomesticScheduledConsent5(FRDomesticScheduledConsent2 frDomesticScheduledConsent2) {
        FRDomesticScheduledConsent5 frDomesticScheduledConsent5 = new FRDomesticScheduledConsent5();
        frDomesticScheduledConsent5.setStatus(frDomesticScheduledConsent2.getStatus());
        frDomesticScheduledConsent5.setId(frDomesticScheduledConsent2.getId());
        frDomesticScheduledConsent5.setUserId(frDomesticScheduledConsent2.getUserId());
        frDomesticScheduledConsent5.setAccountId(frDomesticScheduledConsent2.getAccountId());
        frDomesticScheduledConsent5.setCreated(frDomesticScheduledConsent2.getCreated());
        frDomesticScheduledConsent5.setDomesticScheduledConsent(toOBWriteDomesticScheduledConsent4(frDomesticScheduledConsent2.getDomesticScheduledConsent()));
        frDomesticScheduledConsent5.setPispId(frDomesticScheduledConsent2.getPispId());
        frDomesticScheduledConsent5.setPispName(frDomesticScheduledConsent2.getPispName());
        frDomesticScheduledConsent5.setStatusUpdate(frDomesticScheduledConsent2.getStatusUpdate());
        frDomesticScheduledConsent5.setUpdated(frDomesticScheduledConsent2.getUpdated());
        return frDomesticScheduledConsent5;
    }
}
