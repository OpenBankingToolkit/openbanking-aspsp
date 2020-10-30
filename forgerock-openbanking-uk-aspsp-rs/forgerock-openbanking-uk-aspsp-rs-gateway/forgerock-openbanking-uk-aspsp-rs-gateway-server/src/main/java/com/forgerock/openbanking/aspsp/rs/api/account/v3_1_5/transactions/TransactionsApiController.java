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
package com.forgerock.openbanking.aspsp.rs.api.account.v3_1_5.transactions;

import com.forgerock.openbanking.aspsp.rs.api.account.Transactions;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBReadTransaction5;
import uk.org.openbanking.datamodel.account.OBReadTransaction6;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Controller("TransactionsApiV3.1.5")
@Slf4j
public class TransactionsApiController implements TransactionsApi {

    private final RSEndpointWrapperService rsEndpointWrapperService;

    private final RsStoreGateway rsStoreGateway;

    public TransactionsApiController(RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.rsStoreGateway = rsStoreGateway;
    }


    @Override
    public ResponseEntity<OBReadTransaction6> getAccountTransactions(String accountId,
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
        return rsEndpointWrapperService.<OBReadTransaction6>transctionsEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .accountId(accountId)
                .fromBookingDateTime(fromBookingDateTime)
                .toBookingDateTime(toBookingDateTime)
                .principal(principal)
                .page(page)
                .minimumPermissions(
                        FRExternalPermissionsCode.READTRANSACTIONSBASIC,
                        FRExternalPermissionsCode.READTRANSACTIONSDETAIL,
                        FRExternalPermissionsCode.READTRANSACTIONSCREDITS,
                        FRExternalPermissionsCode.READTRANSACTIONSDEBITS
                )
                .execute(
                        (accountRequest, permissions, transactionFrom, transactionTo, fromBookingDateTimeUPD, toBookingDateTimeUPD, pageNumber) -> {
                            log.info("Read transactions for account  {} with minimumPermissions {}", accountId, permissions);
                            log.debug("transactionStore request transactionFrom {} transactionTo {} fromBookingDateTimeUPD {} toBookingDateTimeUPD {} ",
                                    transactionFrom, transactionTo, fromBookingDateTimeUPD, toBookingDateTimeUPD);
                            HttpHeaders additionalHttpHeaders = Transactions.defaultTransactionHttpHeaders(request, permissions, transactionFrom, transactionTo);
                            Map<String, String> additionalParams = Transactions.dateTransactionParameters(fromBookingDateTimeUPD, toBookingDateTimeUPD);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, additionalParams, OBReadTransaction6.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBReadTransaction6> getAccountStatementTransactions(String statementId,
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
        return rsEndpointWrapperService.<OBReadTransaction5>transctionsEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .fromBookingDateTime(fromBookingDateTime)
                .toBookingDateTime(toBookingDateTime)
                .accountId(accountId)
                .principal(principal)
                .page(page)
                .minimumPermissions(
                        FRExternalPermissionsCode.READSTATEMENTSBASIC,
                        FRExternalPermissionsCode.READSTATEMENTSDETAIL
                )
                .execute(
                        (accountRequest, permissions, transactionFrom, transactionTo, fromBookingDateTimeUPD, toBookingDateTimeUPD, pageNumber) -> {
                            log.debug("transactionStore request transactionFrom {} transactionTo {} fromBookingDateTimeUPD {} toBookingDateTimeUPD {} ",
                                    transactionFrom, transactionTo, fromBookingDateTimeUPD, toBookingDateTimeUPD);

                            HttpHeaders additionalHttpHeaders = Transactions.defaultTransactionHttpHeaders(request, permissions, transactionFrom, transactionTo);
                            Map<String, String> additionalParams = Transactions.dateTransactionParameters(fromBookingDateTimeUPD, toBookingDateTimeUPD);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, additionalParams, OBReadTransaction5.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBReadTransaction6> getTransactions(String page,
                                                              String authorization,
                                                              DateTime xFapiAuthDate,
                                                              DateTime fromBookingDateTime,
                                                              DateTime toBookingDateTime,
                                                              String xFapiCustomerIpAddress,
                                                              String xFapiInteractionId,
                                                              String xCustomerUserAgent,
                                                              HttpServletRequest request,
                                                              Principal principal) throws OBErrorResponseException {
        return rsEndpointWrapperService.<OBReadTransaction5>transctionsEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .principal(principal)
                .fromBookingDateTime(fromBookingDateTime)
                .toBookingDateTime(toBookingDateTime)
                .page(page)
                .minimumPermissions(
                        FRExternalPermissionsCode.READTRANSACTIONSDETAIL,
                        FRExternalPermissionsCode.READTRANSACTIONSBASIC,
                        FRExternalPermissionsCode.READTRANSACTIONSCREDITS,
                        FRExternalPermissionsCode.READTRANSACTIONSDEBITS)
                .execute(
                        (accountRequest, permissions, transactionFrom, transactionTo, fromBookingDateTimeUPD, toBookingDateTimeUPD, pageNumber) -> {
                            log.info("Read transactions for accounts {} with minimumPermissions {}", accountRequest.getAccountIds(), permissions);
                            HttpHeaders additionalHttpHeaders = Transactions.defaultTransactionHttpHeaders(request, permissions, transactionFrom, transactionTo);
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());

                            Map<String, String> additionalParams = Transactions.dateTransactionParameters(fromBookingDateTimeUPD, toBookingDateTimeUPD);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, additionalParams, OBReadTransaction5.class);
                        }
                );
    }
}
