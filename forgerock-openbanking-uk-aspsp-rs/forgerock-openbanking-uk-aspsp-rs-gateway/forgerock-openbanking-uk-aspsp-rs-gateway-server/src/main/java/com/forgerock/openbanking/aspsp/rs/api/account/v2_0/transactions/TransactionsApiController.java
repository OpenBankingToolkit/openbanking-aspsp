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
package com.forgerock.openbanking.aspsp.rs.api.account.v2_0.transactions;

import com.forgerock.openbanking.aspsp.rs.api.account.Transactions;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBReadTransaction1;
import uk.org.openbanking.datamodel.account.OBReadTransaction2;
import uk.org.openbanking.datamodel.account.OBReadTransaction3;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.FROM_BOOKING_DATE_TIME;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.TO_BOOKING_DATE_TIME;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-16T08:37:28.078Z")

@Controller("TransactionsApiV2.0")
public class TransactionsApiController implements TransactionsApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsApiController.class);

    @Autowired
    private com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService RSEndpointWrapperService;
    @Autowired
    private RsStoreGateway rsStoreGateway;

    @Override
    public ResponseEntity<OBReadTransaction2> getAccountTransactions(
            @ApiParam(value = "A unique identifier used to identify the account resource.",required=true )
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") String page,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional - set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional - set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using." )
            @RequestHeader(value="x-customer-user-agent", required=false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return RSEndpointWrapperService.<OBReadTransaction2>transctionsEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
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
                            LOGGER.info("Read transactions for account  {} with minimumPermissions {}", accountId,
                                    permissions);
                            LOGGER.debug("transactionStore request transactionFrom {} transactionTo {} " +
                                            "fromBookingDateTimeUPD {} toBookingDateTimeUPD {} ",
                                    transactionFrom, transactionTo, fromBookingDateTimeUPD, toBookingDateTimeUPD);
                            HttpHeaders additionalHttpHeaders = Transactions.defaultTransactionHttpHeaders(request, permissions, transactionFrom, transactionTo);
                            Map<String, String> additionalParams = Transactions.dateTransactionParameters(fromBookingDateTimeUPD, toBookingDateTimeUPD);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, additionalParams, OBReadTransaction2.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBReadTransaction2> getAccountStatementTransactions(
            @ApiParam(value = "A unique identifier used to identify the account resource.",required=true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") String page,

            @ApiParam(value = "A unique identifier used to identify the statement resource.",required=true)
            @PathVariable("StatementId") String statementId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using." )
            @RequestHeader(value="x-customer-user-agent", required=false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return RSEndpointWrapperService.<OBReadTransaction2>transctionsEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
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
                            LOGGER.debug("transactionStore request transactionFrom {} transactionTo {} " +
                                            "fromBookingDateTimeUPD {} toBookingDateTimeUPD {} ",
                                    transactionFrom, transactionTo, fromBookingDateTimeUPD, toBookingDateTimeUPD);
                            HttpHeaders additionalHttpHeaders = Transactions.defaultTransactionHttpHeaders(request, permissions, transactionFrom, transactionTo);
                            Map<String, String> additionalParams = Transactions.dateTransactionParameters(fromBookingDateTimeUPD, toBookingDateTimeUPD);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, additionalParams, OBReadTransaction2.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBReadTransaction2> getTransactions(
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") String page,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are " +
                    "represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,

            @ApiParam(value = "Indicates the user-agent that the PSU is using." )
            @RequestHeader(value="x-customer-user-agent", required=false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return RSEndpointWrapperService.<OBReadTransaction1>transctionsEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
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
                            LOGGER.info("Read transactions for accounts {} with minimumPermissions {}",
                                    accountRequest.getAccountIds(), permissions);
                            HttpHeaders additionalHttpHeaders = Transactions.defaultTransactionHttpHeaders(request, permissions, transactionFrom, transactionTo);
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            Map<String, String> additionalParams = Transactions.dateTransactionParameters(fromBookingDateTimeUPD, toBookingDateTimeUPD);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, additionalParams, OBReadTransaction3.class);
                        }
                );
    }
}
