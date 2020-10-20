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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.accounts.FRBalance1;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRBalance;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRCashBalanceConverter.toFRCashBalance;

public class FRBalanceMigrator {

    public static FRBalance toFRBalance(FRBalance1 frBalance1) {
        return frBalance1 == null ? null : FRBalance.builder()
                .id(frBalance1.getId())
                .accountId(frBalance1.getAccountId())
                .balance(toFRCashBalance(frBalance1.getBalance()))
                .created(frBalance1.getCreated())
                .updated(frBalance1.getUpdated())
                .build();
    }
}
