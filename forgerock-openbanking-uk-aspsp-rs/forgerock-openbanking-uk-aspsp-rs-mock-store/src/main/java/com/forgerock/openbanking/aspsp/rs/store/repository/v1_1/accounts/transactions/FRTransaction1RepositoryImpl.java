/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.transactions;

import com.forgerock.openbanking.commons.model.openbanking.v1_1.account.FRTransaction1;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.Collections;
import java.util.List;

public class FRTransaction1RepositoryImpl implements FRTransaction1RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRTransaction1RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRTransaction1Repository transactionRepository;

    @Override
    public Page<FRTransaction1> byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code>
            permissions, Pageable pageable) {

        OBExternalPermissions1Code permissionLevel = getPermissionLevel(permissions);
        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                    .findByAccountId(accountId, pageable), permissionLevel);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndTransactionCreditDebitIndicator(accountId, OBCreditDebitCode.CREDIT, pageable),
                    permissionLevel);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndTransactionCreditDebitIndicator(accountId, OBCreditDebitCode.DEBIT, pageable),
                    permissionLevel);
        } else {
            LOGGER.warn("Need at least one of the following permissions: " +
                    OBExternalPermissions1Code.READTRANSACTIONSCREDITS + " or " + OBExternalPermissions1Code
                    .READTRANSACTIONSDEBITS);
            return new PageImpl<>(Collections.emptyList());
        }
    }

    @Override
    public Page<FRTransaction1> byAccountIdAndBookingDateTimeBetweenWithPermissions(String accountId, DateTime
            fromBookingDateTime, DateTime toBookingDateTime, List<OBExternalPermissions1Code> permissions,
                                                                                    Pageable pageable) {

        OBExternalPermissions1Code permissionLevel = getPermissionLevel(permissions);
        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                    .findByAccountIdAndBookingDateTimeBetween(accountId, fromBookingDateTime, toBookingDateTime, pageable), permissionLevel);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(accountId,
                                    OBCreditDebitCode.CREDIT, fromBookingDateTime, toBookingDateTime, pageable),
                    permissionLevel);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(accountId,
                                    OBCreditDebitCode.DEBIT, fromBookingDateTime, toBookingDateTime, pageable),
                    permissionLevel);
        } else {
            LOGGER.warn("Need at least one of the following permissions: " +
                    OBExternalPermissions1Code.READTRANSACTIONSCREDITS + " or " + OBExternalPermissions1Code
                    .READTRANSACTIONSDEBITS);
            return new PageImpl<>(Collections.emptyList());
        }
    }

    @Override
    public Page<FRTransaction1> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code>
            permissions, Pageable pageable) {

        OBExternalPermissions1Code permissionLevel = getPermissionLevel(permissions);
        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                    .findByAccountIdIn(accountIds, pageable), permissionLevel);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdInAndTransactionCreditDebitIndicator(accountIds, OBCreditDebitCode.CREDIT,
                                    pageable),
                    permissionLevel);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdInAndTransactionCreditDebitIndicator(accountIds, OBCreditDebitCode.DEBIT,
                                    pageable),
                    permissionLevel);
        } else {
            LOGGER.warn("Need at least one of the following permissions: " +
                    OBExternalPermissions1Code.READTRANSACTIONSCREDITS + " or " + OBExternalPermissions1Code
                    .READTRANSACTIONSDEBITS);
            return new PageImpl<>(Collections.emptyList());
        }
    }

    @Override
    public Page<FRTransaction1> byAccountIdInAndBookingDateTimeBetweenWithPermissions(List<String> accountIds,
                                                                                      DateTime fromBookingDateTime, DateTime toBookingDateTime, List<OBExternalPermissions1Code> permissions,
                                                                                      Pageable pageable) {

        OBExternalPermissions1Code permissionLevel = getPermissionLevel(permissions);
        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                    .findByAccountIdInAndBookingDateTimeBetween(accountIds, fromBookingDateTime, toBookingDateTime,
                            pageable), permissionLevel);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdInAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(accountIds,
                                    OBCreditDebitCode.CREDIT, fromBookingDateTime, toBookingDateTime, pageable),
                    permissionLevel);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdInAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(accountIds,
                                    OBCreditDebitCode.DEBIT, fromBookingDateTime, toBookingDateTime, pageable),
                    permissionLevel);
        } else {
            LOGGER.warn("Need at least one of the following permissions: " +
                    OBExternalPermissions1Code.READTRANSACTIONSCREDITS + " or " + OBExternalPermissions1Code
                    .READTRANSACTIONSDEBITS);
            return new PageImpl<>(Collections.emptyList());
        }
    }

    private Page<FRTransaction1> filterTransition(Page<FRTransaction1> transactions, OBExternalPermissions1Code permission) {
        for (FRTransaction1 transaction: transactions) {
            switch (permission) {
                case READTRANSACTIONSBASIC:
                    transaction.getTransaction().setTransactionInformation("");
                    transaction.getTransaction().setBalance(null);
                    transaction.getTransaction().setMerchantDetails(null);
                    break;
            }
        }
        return transactions;
    }

    private OBExternalPermissions1Code getPermissionLevel(List<OBExternalPermissions1Code> permissions) {

        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDETAIL)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSBASIC)) {
            throw new IllegalArgumentException("We can't allow basic and detail at the same time");
        } else if (!permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDETAIL)
                && !permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSBASIC)) {
            throw new IllegalArgumentException("Need at least one permission, either basic or detail");
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDETAIL)) {
            return OBExternalPermissions1Code.READTRANSACTIONSDETAIL;
        } else {
            return OBExternalPermissions1Code.READTRANSACTIONSBASIC;
        }
    }
}
