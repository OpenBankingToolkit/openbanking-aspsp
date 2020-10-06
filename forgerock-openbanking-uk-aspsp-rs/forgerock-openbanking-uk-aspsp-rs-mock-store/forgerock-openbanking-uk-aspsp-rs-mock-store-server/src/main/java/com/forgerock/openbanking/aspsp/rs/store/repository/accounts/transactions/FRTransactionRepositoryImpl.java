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
package com.forgerock.openbanking.aspsp.rs.store.repository.accounts.transactions;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRTransaction;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.account.OBExternalAccountIdentification3Code;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.Collections;
import java.util.List;

public class FRTransactionRepositoryImpl implements FRTransactionRepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRTransactionRepositoryImpl.class);

    @Autowired
    @Lazy
    private FRTransactionRepository transactionRepository;

    @Override
    public Page<FRTransaction> byAccountIdAndBookingDateTimeBetweenWithPermissions(String accountId, DateTime
            fromBookingDateTime, DateTime toBookingDateTime, List<OBExternalPermissions1Code> permissions,
                                                                                   Pageable pageable) {

        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                    .findByAccountIdAndBookingDateTimeBetween(accountId, fromBookingDateTime, toBookingDateTime, pageable), permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(accountId,
                                    OBCreditDebitCode.CREDIT, fromBookingDateTime, toBookingDateTime, pageable),
                    permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(accountId,
                                    OBCreditDebitCode.DEBIT, fromBookingDateTime, toBookingDateTime, pageable),
                    permissions);
        } else {
            LOGGER.warn("Need at least one of the following permissions: " +
                    OBExternalPermissions1Code.READTRANSACTIONSCREDITS + " or " + OBExternalPermissions1Code
                    .READTRANSACTIONSDEBITS);
            return new PageImpl<>(Collections.emptyList());
        }
    }

    @Override
    public Page<FRTransaction> byAccountIdAndStatementIdAndBookingDateTimeBetweenWithPermissions(
            String accountId,
            String statementId,
            DateTime fromBookingDateTime,
            DateTime toBookingDateTime,
            List<OBExternalPermissions1Code> permissions,
            Pageable pageable) {

        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                    .findByAccountIdAndStatementIdsAndBookingDateTimeBetween(accountId, statementId, fromBookingDateTime, toBookingDateTime, pageable), permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndStatementIdsAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(accountId, statementId,
                                    OBCreditDebitCode.CREDIT, fromBookingDateTime, toBookingDateTime, pageable),
                    permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndStatementIdsAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(accountId, statementId,
                                    OBCreditDebitCode.DEBIT, fromBookingDateTime, toBookingDateTime, pageable),
                    permissions);
        } else {
            LOGGER.warn("Need at least one of the following permissions: " +
                    OBExternalPermissions1Code.READTRANSACTIONSCREDITS + " or " + OBExternalPermissions1Code
                    .READTRANSACTIONSDEBITS);
            return new PageImpl<>(Collections.emptyList());
        }
    }

    @Override
    public Page<FRTransaction> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code>
            permissions, Pageable pageable) {

        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                    .findByAccountIdIn(accountIds, pageable), permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdInAndTransactionCreditDebitIndicator(accountIds, OBCreditDebitCode.CREDIT,
                                    pageable),
                    permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdInAndTransactionCreditDebitIndicator(accountIds, OBCreditDebitCode.DEBIT,
                                    pageable),
                    permissions);
        } else {
            LOGGER.warn("Need at least one of the following permissions: " +
                    OBExternalPermissions1Code.READTRANSACTIONSCREDITS + " or " + OBExternalPermissions1Code
                    .READTRANSACTIONSDEBITS);
            return new PageImpl<>(Collections.emptyList());
        }
    }



    @Override
    public Page<FRTransaction> byAccountIdInAndBookingDateTimeBetweenWithPermissions(List<String> accountIds,
                                                                                     DateTime fromBookingDateTime, DateTime toBookingDateTime, List<OBExternalPermissions1Code> permissions,
                                                                                     Pageable pageable) {

        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                    .findByAccountIdInAndBookingDateTimeBetween(accountIds, fromBookingDateTime, toBookingDateTime,
                            pageable), permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdInAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(accountIds,
                                    OBCreditDebitCode.CREDIT, fromBookingDateTime, toBookingDateTime, pageable),
                    permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdInAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(accountIds,
                                    OBCreditDebitCode.DEBIT, fromBookingDateTime, toBookingDateTime, pageable),
                    permissions);
        } else {
            LOGGER.warn("Need at least one of the following permissions: " +
                    OBExternalPermissions1Code.READTRANSACTIONSCREDITS + " or " + OBExternalPermissions1Code
                    .READTRANSACTIONSDEBITS);
            return new PageImpl<>(Collections.emptyList());
        }
    }

    private Page<FRTransaction> filterTransition(Page<FRTransaction> transactions, List<OBExternalPermissions1Code> permissions) {
        for (FRTransaction transaction: transactions) {
            for (OBExternalPermissions1Code permission: permissions) {
                switch (permission) {
                    case READTRANSACTIONSBASIC:
                        transaction.getTransaction().setTransactionInformation("");
                        transaction.getTransaction().setBalance(null);
                        transaction.getTransaction().setMerchantDetails(null);
                        transaction.getTransaction().setCreditorAgent(null);
                        transaction.getTransaction().setDebtorAgent(null);
                        break;
                }
                if (!permissions.contains(OBExternalPermissions1Code.READPAN)
                        && transaction.getTransaction().getDebtorAccount() != null
                        && OBExternalAccountIdentification3Code.PAN.toString().equals(transaction.getTransaction().getDebtorAccount().getSchemeName()))
                {
                    transaction.getTransaction().getDebtorAccount().setIdentification("xxx");
                }
                if (!permissions.contains(OBExternalPermissions1Code.READPAN)
                        && transaction.getTransaction().getCreditorAccount() != null
                        && OBExternalAccountIdentification3Code.PAN.toString().equals(transaction.getTransaction().getCreditorAccount().getSchemeName()))
                {
                    transaction.getTransaction().getCreditorAccount().setIdentification("xxx");
                }
            }
        }
        return transactions;
    }
}
