/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.accounts.transactions;

import com.forgerock.openbanking.common.model.openbanking.v3_1.account.FRTransaction4;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;

import java.util.List;

import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.FROM_BOOKING_DATE_TIME;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.TO_BOOKING_DATE_TIME;

public interface FRTransaction4Repository extends MongoRepository<FRTransaction4, String>, FRTransaction4RepositoryCustom {

    Page<FRTransaction4> findByAccountIdAndTransactionCreditDebitIndicator(
            @Param("accountId") String accountId,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            Pageable pageable
    );

    Page<FRTransaction4> findByAccountIdAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(
            @Param("accountId") String accountId,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction4> findByAccountId(
            @Param("accountId") String accountId,
            Pageable pageable
    );

    Page<FRTransaction4> findByAccountIdAndBookingDateTimeBetween(
            @Param("accountId") String accountId,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction4> findByAccountIdAndStatementIdsAndTransactionCreditDebitIndicator(
            @Param("accountId") String accountId,
            @Param("statementId") String statementId,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            Pageable pageable
    );

    Page<FRTransaction4> findByAccountIdAndStatementIdsAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(
            @Param("accountId") String accountId,
            @Param("statementId") String statementId,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction4> findByAccountIdAndStatementIds(
            @Param("accountId") String accountId,
            @Param("statementId") String statementId,
            Pageable pageable
    );

    Page<FRTransaction4> findByAccountIdAndStatementIdsAndBookingDateTimeBetween(
            @Param("accountId") String accountId,
            @Param("statementId") String statementId,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction4> findByAccountIdInAndTransactionCreditDebitIndicator(
            @Param("accountIds") List<String> accountIds,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            Pageable pageable
    );

    Page<FRTransaction4> findByAccountIdInAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(
            @Param("accountIds") List<String> accountIds,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction4> findByAccountIdIn(
            @Param("accountIds") List<String> accountIds,
            Pageable pageable
    );

    Page<FRTransaction4> findByAccountIdInAndBookingDateTimeBetween(
            @Param("accountIds") List<String> accountIds,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Long deleteTransactionByAccountId(@Param("accountId") String accountId);
}

