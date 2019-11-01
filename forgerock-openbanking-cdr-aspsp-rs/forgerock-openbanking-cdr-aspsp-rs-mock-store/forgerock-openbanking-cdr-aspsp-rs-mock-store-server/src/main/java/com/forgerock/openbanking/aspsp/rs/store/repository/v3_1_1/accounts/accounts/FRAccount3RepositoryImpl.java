/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.accounts;

import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRAccount3;
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
import uk.org.openbanking.datamodel.account.OBCashAccount5;
import uk.org.openbanking.datamodel.account.OBExternalAccountIdentification3Code;
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
public class FRAccount3RepositoryImpl implements FRAccount3RepositoryCustom {
    @Autowired
    @Lazy
    private FRAccount3Repository accountsRepository;


    private MongoTemplate mongoTemplate;

    public FRAccount3RepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Collection<FRAccount3> byUserIDWithPermissions(String userID, List<OBExternalPermissions1Code> permissions, Pageable
            pageable) {
        Collection<FRAccount3> accounts = accountsRepository.findByUserID(userID);
        try {
            for (FRAccount3 account : accounts) {
                filterAccount(account, permissions);
            }
            return accounts;
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public FRAccount3 byAccountId(String accountId, List<OBExternalPermissions1Code> permissions) {
        Optional<FRAccount3> isAccount = accountsRepository.findById(accountId);
        if (!isAccount.isPresent()) {
            return null;
        }
        FRAccount3 account = isAccount.get();
        try {
            filterAccount(account, permissions);
            return account;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public List<FRAccount3> byAccountIds(List<String> accountIds, List<OBExternalPermissions1Code> permissions) {
        Iterable<FRAccount3> accounts = accountsRepository.findAllById(accountIds);
        try {
            for (FRAccount3 account : accounts) {
                filterAccount(account, permissions);
            }
            return StreamSupport.stream(accounts.spliterator(), false).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    private void filterAccount(FRAccount3 account, List<OBExternalPermissions1Code> permissions) {
        for (OBExternalPermissions1Code permission: permissions) {
            switch (permission) {

                case READACCOUNTSBASIC:
                    account.getAccount().setAccount(null);
                    account.getAccount().setServicer(null);
                    break;
                case READACCOUNTSDETAIL:
                    if (!CollectionUtils.isEmpty(account.getAccount().getAccount())) {
                        for (OBCashAccount5 subAccount : account.getAccount().getAccount()) {
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
                = mongoTemplate.aggregate(aggregation, FRAccount3.class, UserIds.class);
        return groupResults.getMappedResults().stream().map(UserIds::getUserID).collect(Collectors.toList());
    }


    @Builder
    @Data
    @EqualsAndHashCode
    public static class UserIds {
        private String userID;

    }
}
