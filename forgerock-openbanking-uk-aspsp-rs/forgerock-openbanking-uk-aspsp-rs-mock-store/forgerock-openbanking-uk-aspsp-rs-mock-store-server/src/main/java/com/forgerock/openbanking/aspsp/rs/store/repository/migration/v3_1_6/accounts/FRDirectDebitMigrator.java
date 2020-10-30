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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.accounts.FRDirectDebit1;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRDirectDebit;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRDirectDebitConverter.toFRDirectDebitData;

public class FRDirectDebitMigrator {

    public static FRDirectDebit toFRDirectDebit(FRDirectDebit1 frDirectDebit1) {
        return frDirectDebit1 == null ? null : FRDirectDebit.builder()
                .id(frDirectDebit1.getId())
                .accountId(frDirectDebit1.getAccountId())
                .directDebit(toFRDirectDebitData(frDirectDebit1.getDirectDebit()))
                .created(frDirectDebit1.getCreated())
                .updated(frDirectDebit1.getUpdated())
                .build();
    }
}
