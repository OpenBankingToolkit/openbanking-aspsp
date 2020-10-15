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
package com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts;

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.accounts.FRStandingOrder5;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStandingOrder;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRStandingOrderConverter.toFRStandingOrderData;

public class FRStandingOrderMigrator {

    public static FRStandingOrder toFRStandingOrder(FRStandingOrder5 frStandingOrder5) {
        return frStandingOrder5 == null ? null : FRStandingOrder.builder()
                .id(frStandingOrder5.getId())
                .accountId(frStandingOrder5.getAccountId())
                .standingOrder(toFRStandingOrderData(frStandingOrder5.getStandingOrder()))
                .pispId(frStandingOrder5.getPispId())
                .created(frStandingOrder5.getCreated())
                .updated(frStandingOrder5.getUpdated())
                .rejectionReason(frStandingOrder5.getRejectionReason())
                .status(frStandingOrder5.getStatus())
                .build();
    }
}
