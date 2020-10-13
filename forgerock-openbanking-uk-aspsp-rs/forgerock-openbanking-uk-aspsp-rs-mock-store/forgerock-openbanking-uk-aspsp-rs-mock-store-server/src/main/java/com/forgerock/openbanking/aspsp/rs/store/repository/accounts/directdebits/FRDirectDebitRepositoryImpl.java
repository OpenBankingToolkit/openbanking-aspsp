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
package com.forgerock.openbanking.aspsp.rs.store.repository.accounts.directdebits;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRDirectDebit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;

import java.util.List;

public class FRDirectDebitRepositoryImpl implements FRDirectDebitRepositoryCustom {

    @Autowired
    @Lazy
    private FRDirectDebitRepository directDebitRepository;

    @Override
    public Page<FRDirectDebit> byAccountIdWithPermissions(String accountId, List<FRExternalPermissionsCode> permissions,
                                                          Pageable pageable) {
        return filter(directDebitRepository.findByAccountId(accountId, pageable), permissions);
    }

    @Override
    public Page<FRDirectDebit> byAccountIdInWithPermissions(List<String> accountIds, List<FRExternalPermissionsCode> permissions, Pageable pageable) {
        return filter(directDebitRepository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRDirectDebit> filter(Page<FRDirectDebit> directDebits, List<FRExternalPermissionsCode> permissions) {
        return directDebits;
    }
}
