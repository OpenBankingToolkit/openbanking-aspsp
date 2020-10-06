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
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.party;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public class FRParty2RepositoryImpl implements FRParty2RepositoryCustom {
    @Autowired
    @Lazy
    private FRParty2Repository party1Repository;

    @Override
    public FRParty byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions) {
        return filter(party1Repository.findByAccountId(accountId), permissions);
    }

    @Override
    public FRParty byUserIdWithPermissions(String userId, List<OBExternalPermissions1Code> permissions) {
        return filter(party1Repository.findByUserId(userId), permissions);
    }

    @Override
    public Page<FRParty> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(party1Repository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRParty> filter(Page<FRParty> parties, List<OBExternalPermissions1Code> permissions) {
        return parties;
    }

    private FRParty filter(FRParty party, List<OBExternalPermissions1Code> permissions) {
        return party;
    }
}
