/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.transactions;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRTransaction1;
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

public interface FRTransaction1Repository extends MongoRepository<FRTransaction1, String>, FRTransaction1RepositoryCustom {

    Page<FRTransaction1> findByAccountIdAndTransactionCreditDebitIndicator(
            @Param("accountId") String accountId,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            Pageable pageable
    );

    Page<FRTransaction1> findByAccountIdAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(
            @Param("accountId") String accountId,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction1> findByAccountId(
            @Param("accountId") String accountId,
            Pageable pageable
    );

    Page<FRTransaction1> findByAccountIdAndBookingDateTimeBetween(
            @Param("accountId") String accountId,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction1> findByAccountIdInAndTransactionCreditDebitIndicator(
            @Param("accountIds") List<String> accountIds,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            Pageable pageable
    );

    Page<FRTransaction1> findByAccountIdInAndTransactionCreditDebitIndicatorAndBookingDateTimeBetween(
            @Param("accountIds") List<String> accountIds,
            @Param("creditDebitIndicator") OBCreditDebitCode creditDebitIndicator,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Page<FRTransaction1> findByAccountIdIn(
            @Param("accountIds") List<String> accountIds,
            Pageable pageable
    );

    Page<FRTransaction1> findByAccountIdInAndBookingDateTimeBetween(
            @Param("accountIds") List<String> accountIds,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            Pageable pageable);

    Long deleteTransactionByAccountId(@Param("accountId") String accountId);
}


