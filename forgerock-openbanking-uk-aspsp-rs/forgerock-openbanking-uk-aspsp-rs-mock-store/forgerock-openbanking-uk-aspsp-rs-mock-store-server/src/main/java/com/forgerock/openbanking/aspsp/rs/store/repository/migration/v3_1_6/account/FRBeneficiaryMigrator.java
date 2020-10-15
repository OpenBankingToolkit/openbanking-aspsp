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
package com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.account;

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.accounts.FRBeneficiary3;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRBeneficiary;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountBeneficiaryConverter.toFRAccountBeneficiary;

public class FRBeneficiaryMigrator {

    public static FRBeneficiary toFRBeneficiary(FRBeneficiary3 frBeneficiary3) {
        return frBeneficiary3 == null ? null : FRBeneficiary.builder()
                .id(frBeneficiary3.getId())
                .accountId(frBeneficiary3.getAccountId())
                .beneficiary(toFRAccountBeneficiary(frBeneficiary3.getBeneficiary()))
                .created(frBeneficiary3.getCreated())
                .updated(frBeneficiary3.getUpdated())
                .build();
    }
}
