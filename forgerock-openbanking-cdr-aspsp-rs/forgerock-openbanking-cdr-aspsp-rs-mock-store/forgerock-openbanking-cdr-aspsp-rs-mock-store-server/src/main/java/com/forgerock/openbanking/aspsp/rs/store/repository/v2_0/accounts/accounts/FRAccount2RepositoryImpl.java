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
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.accounts;

import com.forgerock.openbanking.common.model.openbanking.persistence.v2_0.account.FRAccount2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.account.OBExternalAccountIdentification3Code;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class FRAccount2RepositoryImpl implements FRAccount2RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRAccount2RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRAccount2Repository accountsRepository;

    private MongoTemplate mongoTemplate;

    public FRAccount2RepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Collection<FRAccount2> byUserIDWithPermissions(String userID, List<OBExternalPermissions1Code> permissions, Pageable
            pageable) {
        Collection<FRAccount2> accounts = accountsRepository.findByUserID(userID);
        try {
            for (FRAccount2 account : accounts) {
                filterAccount(account, permissions);
            }
            return accounts;
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public FRAccount2 byAccountId(String accountId, List<OBExternalPermissions1Code> permissions) {
        Optional<FRAccount2> isAccount = accountsRepository.findById(accountId);
        if (!isAccount.isPresent()) {
            return null;
        }
        FRAccount2 account = isAccount.get();
        try {
            filterAccount(account, permissions);
            return account;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public List<FRAccount2> byAccountIds(List<String> accountIds, List<OBExternalPermissions1Code> permissions) {
        Iterable<FRAccount2> accounts = accountsRepository.findAllById(accountIds);
        try {
            for (FRAccount2 account : accounts) {
                filterAccount(account, permissions);
            }
            return StreamSupport.stream(accounts.spliterator(), false).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    private void filterAccount(FRAccount2 account, List<OBExternalPermissions1Code> permissions) {
        for (OBExternalPermissions1Code permission: permissions) {
            switch (permission) {

                case READACCOUNTSBASIC:
                    account.getAccount().setAccount(null);
                    account.getAccount().setServicer(null);
                    break;
                case READACCOUNTSDETAIL:
                    if (!CollectionUtils.isEmpty(account.getAccount().getAccount())) {
                        for (OBCashAccount3 subAccount : account.getAccount().getAccount()) {
                            if (!permissions.contains(OBExternalPermissions1Code.READPAN)
                                    && OBExternalAccountIdentification3Code.PAN.toString().equals(subAccount.getSchemeName()))
                            {
                                subAccount.setIdentification("xxx");
                            }
                        }
                    }
                    break;
            }
        }
    }

}
