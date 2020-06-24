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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_3.transactions;

import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadTransaction5;

import java.util.List;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.AccountsUtil.DUMMY_FINANCIAL_ID;

@Controller("TransactionsApiV3.1.3")
public class TransactionsApiController implements TransactionsApi {

    private final com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_2.transactions.TransactionsApiController previousVersionController;

    public TransactionsApiController(
            @Qualifier("TransactionsApiV3.1.2") com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_2.transactions.TransactionsApiController previousVersionController
    ) {
        this.previousVersionController = previousVersionController;
    }

    @Override
    public ResponseEntity<OBReadTransaction5> getAccountTransactions(String accountId,
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
        return previousVersionController.getAccountTransactions(
                accountId,
                page,
                DUMMY_FINANCIAL_ID,
                authorization,
                fromBookingDateTime,
                toBookingDateTime,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                xCustomerUserAgent,
                firstAvailableDate,
                lastAvailableDate,
                permissions,
                httpUrl);
    }

    @Override
    public ResponseEntity<OBReadTransaction5> getAccountStatementTransactions(String statementId,
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
        return previousVersionController.getAccountStatementTransactions(
                accountId,
                page,
                statementId,
                DUMMY_FINANCIAL_ID,
                authorization,
                fromBookingDateTime,
                toBookingDateTime,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                xCustomerUserAgent,
                firstAvailableDate,
                lastAvailableDate,
                permissions,
                httpUrl);
    }

    @Override
    public ResponseEntity<OBReadTransaction5> getTransactions(int page,
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
        return previousVersionController.getTransactions(
                DUMMY_FINANCIAL_ID,
                page,
                authorization,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                fromBookingDateTime,
                toBookingDateTime,
                xCustomerUserAgent,
                firstAvailableDate,
                lastAvailableDate,
                accountIds,
                permissions,
                httpUrl);
    }
}
