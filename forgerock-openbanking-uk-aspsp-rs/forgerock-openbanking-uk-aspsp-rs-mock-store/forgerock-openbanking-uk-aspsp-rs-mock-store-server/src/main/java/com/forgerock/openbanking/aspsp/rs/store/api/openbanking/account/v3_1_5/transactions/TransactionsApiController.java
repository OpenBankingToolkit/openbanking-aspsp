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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_5.transactions;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.transactions.FRTransactionRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRTransaction;
import com.forgerock.openbanking.common.services.openbanking.converter.account.FRTransactionConverter;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadDataTransaction6;
import uk.org.openbanking.datamodel.account.OBReadTransaction6;
import uk.org.openbanking.datamodel.account.OBTransaction6;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRExternalPermissionsCodeConverter.toFRExternalPermissionsCodeList;

@Controller("TransactionsApiV3.1.5")
@Slf4j
public class TransactionsApiController implements TransactionsApi {

    private final int pageLimitTransactions;

    private final FRTransactionRepository FRTransactionRepository;

    private final AccountDataInternalIdFilter accountDataInternalIdFilter;

    public TransactionsApiController(@Value("${rs.page.default.transaction.size}") int pageLimitTransactions,
                                     FRTransactionRepository FRTransactionRepository,
                                     AccountDataInternalIdFilter accountDataInternalIdFilter) {
        this.pageLimitTransactions = pageLimitTransactions;
        this.FRTransactionRepository = FRTransactionRepository;
        this.accountDataInternalIdFilter = accountDataInternalIdFilter;
    }

    @Override
    public ResponseEntity<OBReadTransaction6> getAccountTransactions(String accountId,
                                                                     int page,
                                                                     String authorization,
                                                                     DateTime xFapiAuthDate,
                                                                     DateTime fromBookingDateTime,
                                                                     DateTime toBookingDateTime,
                                                                     DateTime firstAvailableDate,
                                                                     DateTime lastAvailableDate,
                                                                     String xFapiCustomerIpAddress,
                                                                     String xFapiInteractionId,
                                                                     String xCustomerUserAgent,
                                                                     List<OBExternalPermissions1Code> permissions,
                                                                     String httpUrl) throws OBErrorResponseException {
        log.info("Read transactions for account  {} with minimumPermissions {}", accountId,
                permissions);
        log.debug("transactionStore request transactionFrom {} transactionTo {} ",
                fromBookingDateTime, toBookingDateTime);

        if (toBookingDateTime == null) {
            toBookingDateTime = DateTime.now();
        }
        if (fromBookingDateTime == null) {
            fromBookingDateTime = toBookingDateTime.minusYears(100);
        }

        Page<FRTransaction> response = FRTransactionRepository.byAccountIdAndBookingDateTimeBetweenWithPermissions(accountId,
                fromBookingDateTime, toBookingDateTime, toFRExternalPermissionsCodeList(permissions),
                PageRequest.of(page, pageLimitTransactions, Sort.Direction.ASC, "bookingDateTime"));

        List<OBTransaction6> transactions = response.getContent()
                .stream()
                .map(FRTransaction::getTransaction)
                .map(FRTransactionConverter::toOBTransaction6)
                .map(t -> accountDataInternalIdFilter.apply(t))
                .collect(Collectors.toList());

        //Package the answer
        int totalPages = response.getTotalPages();

        return ResponseEntity.ok(new OBReadTransaction6()
                .data(new OBReadDataTransaction6().transaction(transactions))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages, firstAvailableDate, lastAvailableDate)));
    }

    @Override
    public ResponseEntity<OBReadTransaction6> getAccountStatementTransactions(String statementId,
                                                                              String accountId,
                                                                              int page,
                                                                              String authorization,
                                                                              DateTime xFapiAuthDate,
                                                                              DateTime fromBookingDateTime,
                                                                              DateTime toBookingDateTime,
                                                                              DateTime firstAvailableDate,
                                                                              DateTime lastAvailableDate,
                                                                              String xFapiCustomerIpAddress,
                                                                              String xFapiInteractionId,
                                                                              String xCustomerUserAgent,
                                                                              List<OBExternalPermissions1Code> permissions,
                                                                              String httpUrl) throws OBErrorResponseException {
        log.info("Reading transations from account id {}, statement id {}, fromBookingDate {} toBookingDate {} " +
                        "minimumPermissions {} pageNumber {} ", accountId, statementId,
                fromBookingDateTime, toBookingDateTime, permissions, page);

        if (toBookingDateTime == null) {
            toBookingDateTime = DateTime.now();
        }
        if (fromBookingDateTime == null) {
            fromBookingDateTime = toBookingDateTime.minusYears(100);
        }

        Page<FRTransaction> response = FRTransactionRepository.byAccountIdAndStatementIdAndBookingDateTimeBetweenWithPermissions(accountId, statementId,
                fromBookingDateTime, toBookingDateTime, toFRExternalPermissionsCodeList(permissions),
                PageRequest.of(page, pageLimitTransactions, Sort.Direction.ASC, "bookingDateTime"));

        List<OBTransaction6> transactions = response.getContent()
                .stream()
                .map(FRTransaction::getTransaction)
                .map(FRTransactionConverter::toOBTransaction6)
                .map(t -> accountDataInternalIdFilter.apply(t))
                .collect(Collectors.toList());

        //Package the answer
        int totalPages = response.getTotalPages();

        return ResponseEntity.ok(new OBReadTransaction6().data(new OBReadDataTransaction6().transaction(transactions))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages, firstAvailableDate, lastAvailableDate)));
    }

    @Override
    public ResponseEntity<OBReadTransaction6> getTransactions(int page,
                                                              String authorization,
                                                              DateTime xFapiAuthDate,
                                                              DateTime fromBookingDateTime,
                                                              DateTime toBookingDateTime,
                                                              DateTime firstAvailableDate,
                                                              DateTime lastAvailableDate,
                                                              String xFapiCustomerIpAddress,
                                                              String xFapiInteractionId,
                                                              String xCustomerUserAgent,
                                                              List<String> accountIds,
                                                              List<OBExternalPermissions1Code> permissions,
                                                              String httpUrl) throws OBErrorResponseException {
        log.info("Reading transations from account ids {}, fromBookingDate {} toBookingDate {} minimumPermissions {} pageNumber {} ",
                accountIds, fromBookingDateTime, toBookingDateTime, permissions, page);

        if (toBookingDateTime == null) {
            toBookingDateTime = DateTime.now();
        }
        if (fromBookingDateTime == null) {
            fromBookingDateTime = toBookingDateTime.minusYears(100);
        }

        Page<FRTransaction> body = FRTransactionRepository.byAccountIdInAndBookingDateTimeBetweenWithPermissions(accountIds,
                fromBookingDateTime, toBookingDateTime, toFRExternalPermissionsCodeList(permissions),
                PageRequest.of(page, pageLimitTransactions, Sort.Direction.ASC, "bookingDateTime"));

        List<OBTransaction6> transactions = body.getContent()
                .stream()
                .map(FRTransaction::getTransaction)
                .map(FRTransactionConverter::toOBTransaction6)
                .map(t -> accountDataInternalIdFilter.apply(t))
                .collect(Collectors.toList());

        //Package the answer
        int totalPages = body.getTotalPages();

        return  ResponseEntity.ok(new OBReadTransaction6().data(new OBReadDataTransaction6().transaction(transactions))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages, firstAvailableDate, lastAvailableDate)));
    }
}
