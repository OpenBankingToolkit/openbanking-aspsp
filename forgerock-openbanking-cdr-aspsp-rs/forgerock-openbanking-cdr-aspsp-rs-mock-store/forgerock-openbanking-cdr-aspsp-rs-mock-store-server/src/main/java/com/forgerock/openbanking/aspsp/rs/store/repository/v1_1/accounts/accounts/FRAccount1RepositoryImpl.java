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
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.accounts;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.v1_1.FRAccount1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class FRAccount1RepositoryImpl implements FRAccount1RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRAccount1RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRAccount1Repository accountsRepository;

    @Override
    public Page<FRAccount1> byUserIDWithPermissions(String userID, List<OBExternalPermissions1Code> permissions, Pageable
            pageable) {
        Page<FRAccount1> accounts = accountsRepository.findByUserID(userID, pageable);
        try {
            for (FRAccount1 account : accounts) {
                filterAccount(account, permissions);
            }
            return accounts;
        } catch (IllegalArgumentException e) {
            return new PageImpl<>(Collections.emptyList());
        }
    }

    @Override
    public FRAccount1 byAccountId(String accountId, List<OBExternalPermissions1Code> permissions) {
        Optional<FRAccount1> isAccount = accountsRepository.findById(accountId);
        if (!isAccount.isPresent()) {
            return null;
        }
        FRAccount1 account = isAccount.get();
        try {
            filterAccount(account, permissions);
            return account;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public List<FRAccount1> byAccountIds(List<String> accountIds, List<OBExternalPermissions1Code> permissions) {
        Iterable<FRAccount1> accounts = accountsRepository.findAllById(accountIds);
        try {
            for (FRAccount1 account : accounts) {
                filterAccount(account, permissions);
            }
            return StreamSupport.stream(accounts.spliterator(), false).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    private void filterAccount(FRAccount1 account, List<OBExternalPermissions1Code> permissions) {
        for (OBExternalPermissions1Code permission: permissions) {
            switch (permission) {

            case READACCOUNTSBASIC:
                account.getAccount().setAccount(null);
                account.getAccount().setServicer(null);
                break;
            }
        }
    }
}
