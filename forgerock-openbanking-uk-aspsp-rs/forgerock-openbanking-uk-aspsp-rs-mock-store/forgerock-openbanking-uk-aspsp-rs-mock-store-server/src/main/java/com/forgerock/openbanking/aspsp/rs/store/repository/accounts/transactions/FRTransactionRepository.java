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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;

import java.util.List;
import java.util.Set;

import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.FROM_BOOKING_DATE_TIME;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.TO_BOOKING_DATE_TIME;

public interface FRTransactionRepository extends MongoRepository<FRTransaction, String>, FRTransactionRepositoryCustom {

    Page<FRTransaction> findByAccountIdAndTransactionCreditDebitIndicator(
            @Param("accountId") String accountId,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            Pageable pageable
    );

    Page<FRTransaction> findByAccountIdAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(
            @Param("accountId") String accountId,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction> findByAccountId(
            @Param("accountId") String accountId,
            Pageable pageable
    );

    Page<FRTransaction> findByAccountIdAndBookingDateTimeBetween(
            @Param("accountId") String accountId,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction> findByAccountIdAndStatementIdsAndTransactionCreditDebitIndicator(
            @Param("accountId") String accountId,
            @Param("statementId") String statementId,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            Pageable pageable
    );

    Page<FRTransaction> findByAccountIdAndStatementIdsAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(
            @Param("accountId") String accountId,
            @Param("statementId") String statementId,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction> findByAccountIdAndStatementIds(
            @Param("accountId") String accountId,
            @Param("statementId") String statementId,
            Pageable pageable
    );

    Page<FRTransaction> findByAccountIdAndStatementIdsAndBookingDateTimeBetween(
            @Param("accountId") String accountId,
            @Param("statementId") String statementId,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction> findByAccountIdInAndTransactionCreditDebitIndicator(
            @Param("accountIds") List<String> accountIds,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            Pageable pageable
    );

    Page<FRTransaction> findByAccountIdInAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(
            @Param("accountIds") List<String> accountIds,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction> findByAccountIdIn(
            @Param("accountIds") List<String> accountIds,
            Pageable pageable
    );

    Page<FRTransaction> findByAccountIdInAndBookingDateTimeBetween(
            @Param("accountIds") List<String> accountIds,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Long deleteTransactionByAccountId(@Param("accountId") String accountId);

    Long countByAccountIdIn(Set<String> accountIds);
}


