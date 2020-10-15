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
package com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payment;

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.payments.FRDomesticScheduledConsent2;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticScheduledConsent;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticScheduledConsentConverter.toFRWriteDomesticScheduledConsent;

public class FRDomesticScheduledConsentMigrator {

    public static FRDomesticScheduledConsent toFRDomesticScheduledConsent(FRDomesticScheduledConsent2 frDomesticScheduledConsent2) {
        FRDomesticScheduledConsent frDomesticScheduledConsent = new FRDomesticScheduledConsent();
        frDomesticScheduledConsent.setStatus(frDomesticScheduledConsent2.getStatus());
        frDomesticScheduledConsent.setId(frDomesticScheduledConsent2.getId());
        frDomesticScheduledConsent.setUserId(frDomesticScheduledConsent2.getUserId());
        frDomesticScheduledConsent.setAccountId(frDomesticScheduledConsent2.getAccountId());
        frDomesticScheduledConsent.setCreated(frDomesticScheduledConsent2.getCreated());
        frDomesticScheduledConsent.setDomesticScheduledConsent(toFRWriteDomesticScheduledConsent(frDomesticScheduledConsent2.getDomesticScheduledConsent()));
        frDomesticScheduledConsent.setPispId(frDomesticScheduledConsent2.getPispId());
        frDomesticScheduledConsent.setPispName(frDomesticScheduledConsent2.getPispName());
        frDomesticScheduledConsent.setStatusUpdate(frDomesticScheduledConsent2.getStatusUpdate());
        frDomesticScheduledConsent.setUpdated(frDomesticScheduledConsent2.getUpdated());
        return frDomesticScheduledConsent;
    }
}
