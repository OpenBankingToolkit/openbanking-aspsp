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
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.2.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.api.account.v3_1_5.transactions;

import com.forgerock.openbanking.common.openbanking.OBGroupName;
import com.forgerock.openbanking.common.openbanking.OBReference;
import com.forgerock.openbanking.common.openbanking.OpenBankingAPI;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBReadTransaction6;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.FROM_BOOKING_DATE_TIME;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.TO_BOOKING_DATE_TIME;

@Api(tags = "v3.1.5-Transactions", description = "the transactions API")
@OpenBankingAPI(
        obVersion = "3.1.5",
        obGroupName = OBGroupName.AISP,
        obReference = OBReference.TRANSACTIONS
)
@RequestMapping(value = "/open-banking/v3.1.5/aisp")
public interface TransactionsApi {

    @ApiOperation(value = "Get Transactions", nickname = "getAccountTransactions", notes = "Get transactions related to an account",
            response = OBReadTransaction6.class, authorizations = {
            @Authorization(value = "PSUOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Ability to read Accounts information")
            })
    }, tags = {"v3.1.5-AccountTransactions",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account Transactions successfully retrieved", response = OBReadTransaction6.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error")})

    @PreAuthorize("hasAuthority('ROLE_AISP')")
    @OpenBankingAPI(
            obReference = OBReference.GET_ACCOUNT_TRANSACTIONS
    )
    @RequestMapping(value = "/accounts/{AccountId}/transactions",
            produces = {"application/json; charset=utf-8", "application/jose+jwe"},
            method = RequestMethod.GET)
    ResponseEntity<OBReadTransaction6> getAccountTransactions(
            @ApiParam(value = "AccountId", required = true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") String page,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-auth-date", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiAuthDate,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional - set to 00:00:00 for just Date. The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional - set to 00:00:00 for just Date. The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException;


    @ApiOperation(value = "Get Transactions", nickname = "getAccountStatementTransactions", notes = "Get Statement Transactions related to an account",
            response = OBReadTransaction6.class, authorizations = {
            @Authorization(value = "PSUOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Ability to read Accounts information")
            })
    }, tags = {"v3.1.5-AccountStatementTransactions",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account Statement Transactions successfully retrieved", response = OBReadTransaction6.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error")})

    @PreAuthorize("hasAuthority('ROLE_AISP')")
    @OpenBankingAPI(
            obReference = OBReference.GET_ACCOUNT_STATEMENT_TRANSACTIONS
    )
    @RequestMapping(value = "/accounts/{AccountId}/statements/{StatementId}/transactions",
            produces = {"application/json; charset=utf-8", "application/jose+jwe"},
            method = RequestMethod.GET)
    ResponseEntity<OBReadTransaction6> getAccountStatementTransactions(
            @ApiParam(value = "StatementId", required = true)
            @PathVariable("StatementId") String statementId,

            @ApiParam(value = "AccountId", required = true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") String page,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-auth-date", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiAuthDate,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional - set to 00:00:00 for just Date. The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional - set to 00:00:00 for just Date. The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException;


    @ApiOperation(value = "Get Transactions", nickname = "getTransactions", notes = "Get Transactions", response = OBReadTransaction6.class, authorizations = {
            @Authorization(value = "PSUOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Ability to read Accounts information")
            })
    }, tags = {"v3.1.5-Transactions",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transactions successfully retrieved", response = OBReadTransaction6.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error")})

    @PreAuthorize("hasAuthority('ROLE_AISP')")
    @OpenBankingAPI(
            obReference = OBReference.GET_TRANSACTIONS
    )
    @RequestMapping(value = "/transactions",
            produces = {"application/json; charset=utf-8", "application/jose+jwe"},
            method = RequestMethod.GET)
    ResponseEntity<OBReadTransaction6> getTransactions(
            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") String page,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-auth-date", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiAuthDate,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional - set to 00:00:00 for just Date. The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional - set to 00:00:00 for just Date. The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException;
}
