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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.FRDomesticConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticConsent5;

import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticConsentConverter.toOBWriteDomesticConsent4;

public class FRDomesticConsentConverter {

    public static FRDomesticConsent5 toFRDomesticConsent5(FRDomesticConsent2 frDomesticConsent2) {
        FRDomesticConsent5 frDomesticConsent5 = new FRDomesticConsent5();

        frDomesticConsent5.setId(frDomesticConsent5.getId());
        frDomesticConsent5.setStatus(frDomesticConsent5.getStatus());
        frDomesticConsent5.setUserId(frDomesticConsent5.getUserId());
        frDomesticConsent5.setAccountId(frDomesticConsent5.getAccountId());
        frDomesticConsent5.setCreated(frDomesticConsent5.getCreated());
        frDomesticConsent5.setDomesticConsent(toOBWriteDomesticConsent4(frDomesticConsent2.getDomesticConsent()));
        frDomesticConsent5.setPispId(frDomesticConsent5.getPispId());
        frDomesticConsent5.setPispName(frDomesticConsent5.getPispName());
        frDomesticConsent5.setStatusUpdate(frDomesticConsent5.getStatusUpdate());

        return frDomesticConsent5;
    }

}
