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
package com.forgerock.openbanking.aspsp.rs.api.account.v3_1_3.transactions;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBReadTransaction5;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller("TransactionsApiV3.1.3")
public class TransactionsApiController implements TransactionsApi {

    private final RSEndpointWrapperService rsEndpointWrapperService;

    private final com.forgerock.openbanking.aspsp.rs.api.account.v3_1_2.transactions.TransactionsApiController previousVersionController;

    public TransactionsApiController(RSEndpointWrapperService rsEndpointWrapperService,
                                     @Qualifier("TransactionsApiV3.1.2") com.forgerock.openbanking.aspsp.rs.api.account.v3_1_2.transactions.TransactionsApiController previousVersionController) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.previousVersionController = previousVersionController;
    }


    @Override
    public ResponseEntity<OBReadTransaction5> getAccountTransactions(String accountId,
                                                                     String page,
                                                                     String authorization,
                                                                     DateTime xFapiAuthDate,
                                                                     DateTime fromBookingDateTime,
                                                                     DateTime toBookingDateTime,
                                                                     String xFapiCustomerIpAddress,
                                                                     String xFapiInteractionId,
                                                                     String xCustomerUserAgent,
                                                                     HttpServletRequest request,
                                                                     Principal principal) throws OBErrorResponseException {
        return previousVersionController.getAccountTransactions(
                accountId,
                page,
                rsEndpointWrapperService.rsConfiguration.financialId,
                authorization,
                fromBookingDateTime,
                toBookingDateTime,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                xCustomerUserAgent,
                request,
                principal);
    }

    @Override
    public ResponseEntity<OBReadTransaction5> getAccountStatementTransactions(String statementId,
                                                                              String accountId,
                                                                              String page,
                                                                              String authorization,
                                                                              DateTime xFapiAuthDate,
                                                                              DateTime fromBookingDateTime,
                                                                              DateTime toBookingDateTime,
                                                                              String xFapiCustomerIpAddress,
                                                                              String xFapiInteractionId,
                                                                              String xCustomerUserAgent,
                                                                              HttpServletRequest request,
                                                                              Principal principal) throws OBErrorResponseException {
        return previousVersionController.getAccountStatementTransactions(
                accountId,
                page,
                statementId,
                rsEndpointWrapperService.rsConfiguration.financialId,
                authorization,
                fromBookingDateTime,
                toBookingDateTime,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                xCustomerUserAgent,
                request,
                principal);
    }

    @Override
    public ResponseEntity<OBReadTransaction5> getTransactions(String page,
                                                              String authorization,
                                                              DateTime xFapiAuthDate,
                                                              DateTime fromBookingDateTime,
                                                              DateTime toBookingDateTime,
                                                              String xFapiCustomerIpAddress,
                                                              String xFapiInteractionId,
                                                              String xCustomerUserAgent,
                                                              HttpServletRequest request,
                                                              Principal principal) throws OBErrorResponseException {
        return previousVersionController.getTransactions(
                rsEndpointWrapperService.rsConfiguration.financialId,
                page,
                authorization,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                fromBookingDateTime,
                toBookingDateTime,
                xCustomerUserAgent,
                request,
                principal);
    }
}
