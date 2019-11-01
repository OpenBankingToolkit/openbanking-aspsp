/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.accounts.transactions;

import com.forgerock.openbanking.common.model.openbanking.v3_0.account.FRTransaction3;
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

public class FRTransaction3RepositoryImpl implements FRTransaction3RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRTransaction3RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRTransaction3Repository transactionRepository;

    @Override
    public Page<FRTransaction3> byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code>
            permissions, Pageable pageable) {

        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                    .findByAccountId(accountId, pageable), permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndTransactionCreditDebitIndicator(accountId, OBCreditDebitCode.CREDIT, pageable),
                    permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndTransactionCreditDebitIndicator(accountId, OBCreditDebitCode.DEBIT, pageable),
                    permissions);
        } else {
            LOGGER.warn("Need at least one of the following permissions: " +
                    OBExternalPermissions1Code.READTRANSACTIONSCREDITS + " or " + OBExternalPermissions1Code
                    .READTRANSACTIONSDEBITS);
            return new PageImpl<>(Collections.emptyList());
        }
    }

    @Override
    public Page<FRTransaction3> byAccountIdAndBookingDateTimeBetweenWithPermissions(String accountId, DateTime
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
    public Page<FRTransaction3> byAccountIdAndStatementIdWithPermissions(
            String accountId,
            String statementId,
            List<OBExternalPermissions1Code> permissions,
            Pageable pageable) {

        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                    .findByAccountIdAndStatementIds(accountId, statementId, pageable), permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndStatementIdsAndTransactionCreditDebitIndicator(accountId, statementId, OBCreditDebitCode.CREDIT, pageable),
                    permissions);
        } else if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)) {
            return filterTransition(transactionRepository
                            .findByAccountIdAndStatementIdsAndTransactionCreditDebitIndicator(accountId, statementId, OBCreditDebitCode.DEBIT, pageable),
                    permissions);
        } else {
            LOGGER.warn("Need at least one of the following permissions: " +
                    OBExternalPermissions1Code.READTRANSACTIONSCREDITS + " or " + OBExternalPermissions1Code
                    .READTRANSACTIONSDEBITS);
            return new PageImpl<>(Collections.emptyList());
        }
    }

    @Override
    public Page<FRTransaction3> byAccountIdAndStatementIdAndBookingDateTimeBetweenWithPermissions(
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
    public Page<FRTransaction3> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code>
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
    public Page<FRTransaction3> byAccountIdInAndBookingDateTimeBetweenWithPermissions(List<String> accountIds,
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

    private Page<FRTransaction3> filterTransition(Page<FRTransaction3> transactions, List<OBExternalPermissions1Code> permissions) {
        for (FRTransaction3 transaction: transactions) {
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
                        && OBExternalAccountIdentification3Code.PAN.toString().equals(transaction.getTransaction().getDebtorAccount().getSchemeName()))
                {
                    transaction.getTransaction().getCreditorAccount().setIdentification("xxx");
                }
            }
        }
        return transactions;
    }
}
