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
package com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accounts;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import uk.org.openbanking.datamodel.account.OBAccount3Account;
import uk.org.openbanking.datamodel.account.OBExternalAccountIdentification4Code;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Repository
public class FRAccount4RepositoryImpl implements FRAccount4RepositoryCustom {

    @Autowired
    @Lazy
    private FRAccount4Repository accountsRepository;

    private MongoTemplate mongoTemplate;

    public FRAccount4RepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Collection<FRAccount> byUserIDWithPermissions(String userID, List<OBExternalPermissions1Code> permissions, Pageable
            pageable) {
        Collection<FRAccount> accounts = accountsRepository.findByUserID(userID);
        try {
            for (FRAccount account : accounts) {
                filterAccount(account, permissions);
            }
            return accounts;
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public FRAccount byAccountId(String accountId, List<OBExternalPermissions1Code> permissions) {
        Optional<FRAccount> isAccount = accountsRepository.findById(accountId);
        if (!isAccount.isPresent()) {
            return null;
        }
        FRAccount account = isAccount.get();
        try {
            filterAccount(account, permissions);
            return account;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public List<FRAccount> byAccountIds(List<String> accountIds, List<OBExternalPermissions1Code> permissions) {
        Iterable<FRAccount> accounts = accountsRepository.findAllById(accountIds);
        try {
            for (FRAccount account : accounts) {
                filterAccount(account, permissions);
            }
            return StreamSupport.stream(accounts.spliterator(), false).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    private void filterAccount(FRAccount account, List<OBExternalPermissions1Code> permissions) {
        for (OBExternalPermissions1Code permission: permissions) {
            switch (permission) {

                case READACCOUNTSBASIC:
                    account.getAccount().setAccount(null);
                    account.getAccount().setServicer(null);
                    break;
                case READACCOUNTSDETAIL:
                    if (!CollectionUtils.isEmpty(account.getAccount().getAccount())) {
                        for (OBAccount3Account subAccount : account.getAccount().getAccount()) {
                            if (!permissions.contains(OBExternalPermissions1Code.READPAN)
                                    && OBExternalAccountIdentification4Code.PAN.toString().equals(subAccount.getSchemeName()))
                            {
                                subAccount.setIdentification("xxx");
                            }
                        }
                    }
                    break;
            }
        }
    }


    @Override
    public List<String> getUserIds(DateTime from, DateTime to) {
        Aggregation aggregation = newAggregation(
                Aggregation.match(
                        Criteria.where("created").gt(from)),
                Aggregation.match(
                        Criteria.where("created").lt(to)),
                Aggregation.group("userID")
                .first("userID").as("userID"),
                project("userID")
        );
        //Convert the aggregation result into a List
        AggregationResults<UserIds> groupResults
                = mongoTemplate.aggregate(aggregation, FRAccount.class, UserIds.class);
        return groupResults.getMappedResults().stream().map(UserIds::getUserID).collect(Collectors.toList());
    }


    @Builder
    @Data
    @EqualsAndHashCode
    public static class UserIds {
        private String userID;

    }
}
