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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.payments.FRInternationalConsent2;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalConsent;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConsentConverter.toFRWriteInternationalConsent;

public class FRInternationalConsentMigrator {

    public static FRInternationalConsent toFRInternationalConsent(FRInternationalConsent2 frInternationalConsent2) {
        FRInternationalConsent frInternationalConsent = new FRInternationalConsent();

        frInternationalConsent.setId(frInternationalConsent2.getId());
        frInternationalConsent.setStatus(frInternationalConsent2.getStatus());
        frInternationalConsent.setUserId(frInternationalConsent2.getUserId());
        frInternationalConsent.setAccountId(frInternationalConsent2.getAccountId());
        frInternationalConsent.setCreated(frInternationalConsent2.getCreated());
        frInternationalConsent.setInternationalConsent(toFRWriteInternationalConsent(frInternationalConsent2.getInternationalConsent()));
        frInternationalConsent.setPispId(frInternationalConsent2.getPispId());
        frInternationalConsent.setPispName(frInternationalConsent2.getPispName());
        frInternationalConsent.setStatusUpdate(frInternationalConsent2.getStatusUpdate());

        return frInternationalConsent;
    }
}
