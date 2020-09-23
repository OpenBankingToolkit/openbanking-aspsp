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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.FRFileConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRFileConsent5;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteFileConsentConverter.toFRWriteFileConsent;

public class FRFileConsentConverter {

    public static FRFileConsent5 toFRFileConsent5(FRFileConsent2 frFileConsent2) {
        return FRFileConsent5.builder()
                .id(frFileConsent2.getId())
                .status(frFileConsent2.getStatus())
                .writeFileConsent(toFRWriteFileConsent(frFileConsent2.getWriteFileConsent()))
                .accountId(frFileConsent2.getAccountId())
                .userId(frFileConsent2.getUserId())
                .pispId(frFileConsent2.getPispId())
                .pispName(frFileConsent2.getPispName())
                .idempotencyKey(frFileConsent2.getIdempotencyKey())
                .created(frFileConsent2.getCreated())
                .statusUpdate(frFileConsent2.getStatusUpdate())
                .updated(frFileConsent2.getUpdated())
                .payments(frFileConsent2.getPayments())
                .fileContent(frFileConsent2.getFileContent())
                .obVersion(frFileConsent2.getObVersion())
                .build();
    }
}
