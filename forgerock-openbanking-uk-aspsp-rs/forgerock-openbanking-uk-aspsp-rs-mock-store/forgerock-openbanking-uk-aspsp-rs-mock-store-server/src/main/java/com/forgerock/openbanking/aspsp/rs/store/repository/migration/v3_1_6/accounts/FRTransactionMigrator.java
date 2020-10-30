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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.accounts.FRTransaction5;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRTransaction;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRTransactionConverter.toFRTransactionData;

public class FRTransactionMigrator {

    public static FRTransaction toFRTransaction(FRTransaction5 frTransaction5) {
        return frTransaction5 == null ? null : FRTransaction.builder()
                .id(frTransaction5.getId())
                .accountId(frTransaction5.getAccountId())
                .statementIds(frTransaction5.getStatementIds())
                .transaction(toFRTransactionData(frTransaction5.getTransaction()))
                .created(frTransaction5.getCreated())
                .updated(frTransaction5.getUpdated())
                .bookingDateTime(frTransaction5.getBookingDateTime())
                .build();
    }
}
