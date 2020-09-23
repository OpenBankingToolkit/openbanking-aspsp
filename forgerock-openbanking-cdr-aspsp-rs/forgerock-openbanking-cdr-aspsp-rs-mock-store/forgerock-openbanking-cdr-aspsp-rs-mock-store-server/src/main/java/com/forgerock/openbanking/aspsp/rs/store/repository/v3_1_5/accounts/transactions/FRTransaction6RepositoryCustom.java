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
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.accounts.transactions;

import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_5.account.FRTransaction6;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.FROM_BOOKING_DATE_TIME;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.TO_BOOKING_DATE_TIME;

public interface FRTransaction6RepositoryCustom {

    Page<FRTransaction6> byAccountIdAndBookingDateTimeBetweenWithPermissions(
            @Param("accountId") String accountId,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            @Param("permissions") List<OBExternalPermissions1Code> permissions,
            Pageable pageable);

    Page<FRTransaction6> byAccountIdAndStatementIdAndBookingDateTimeBetweenWithPermissions(
            @Param("accountId") String accountId,
            @Param("statementId") String statementId,
            @Param(FROM_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,
            @Param(TO_BOOKING_DATE_TIME) @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime
                    toBookingDateTime,
            @Param("permissions") List<OBExternalPermissions1Code> permissions,
            Pageable pageable);


    Page<FRTransaction6> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code>
            permissions, Pageable pageable);

    Page<FRTransaction6> byAccountIdInAndBookingDateTimeBetweenWithPermissions(List<String> accountIds,
                                                                               DateTime fromBookingDateTime, DateTime toBookingDateTime, List<OBExternalPermissions1Code> permissions,
                                                                               Pageable pageable);
}
