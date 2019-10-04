/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.2.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.api.account.v3_0.transactions;

import com.forgerock.openbanking.commons.rest.annotations.openbanking.OBGroupName;
import com.forgerock.openbanking.commons.rest.annotations.openbanking.OBReference;
import com.forgerock.openbanking.commons.rest.annotations.openbanking.OpenBankingAPI;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.*;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uk.org.openbanking.datamodel.account.OBReadTransaction3;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.FROM_BOOKING_DATE_TIME;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.TO_BOOKING_DATE_TIME;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-16T08:37:28.078Z")

@Api(tags = "v3.0-Transactions", description = "the transactions API")
@OpenBankingAPI(
        obVersion = "3.0",
        obGroupName = OBGroupName.AISP,
        obReference = OBReference.TRANSACTIONS
)
@RequestMapping(value = "/open-banking/v3.0/aisp")
public interface TransactionsApi {

    @ApiOperation(value = "Get Account Transactions", notes = "Get transactions related to an account",
            response = OBReadTransaction3.class, authorizations = {
            @Authorization(value = "PSUOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Ability to get Accounts information")
            })
    }, tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account Transactions successfully retrieved", response = OBReadTransaction3.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 405, message = "Method Not Allowed", response = Void.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = Void.class),
            @ApiResponse(code = 429, message = "Too Many Requests", response = Void.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class) })

    @PreAuthorize("hasAuthority('ROLE_AISP')")
    @OpenBankingAPI(
            obReference = OBReference.GET_ACCOUNT_TRANSACTIONS
    )
    @RequestMapping(value = "/accounts/{AccountId}/transactions",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity<OBReadTransaction3> getAccountTransactions(
            @ApiParam(value = "A unique identifier used to identify the account resource.", required = true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") String page,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false) DateTime toBookingDateTime,

            @ApiParam(value = "The time when the PSU last logged in with the TPP. " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException;

    @ApiOperation(value = "Get Statement Transactions", nickname = "getAccountStatementTransactions",
            notes = "Get Statement Transactions related to an account", response = OBReadTransaction3.class, authorizations = {
            @Authorization(value = "PSUOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Ability to get Accounts information")
            })
    }, tags={ "v2.0-Transactions" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account Transactions successfully retrieved", response = OBReadTransaction3.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    @OpenBankingAPI(
            obReference = OBReference.GET_ACCOUNT_STATEMENT_TRANSACTIONS
    )
    @RequestMapping(value = "/accounts/{AccountId}/statements/{StatementId}/transactions",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity<OBReadTransaction3> getAccountStatementTransactions(
            @ApiParam(value = "A unique identifier used to identify the account resource.", required = true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") String page,

            @ApiParam(value = "A unique identifier used to identify the statement resource.", required = true)
            @PathVariable("StatementId") String statementId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false) DateTime toBookingDateTime,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException;

    @ApiOperation(value = "Get Transactions", notes = "Get Transactions", response = OBReadTransaction3.class, authorizations = {
        @Authorization(value = "PSUOAuth2Security", scopes = {
            @AuthorizationScope(scope = "accounts", description = "Ability to get Accounts information")
            })
    }, tags={  })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Transactions successfully retrieved", response = OBReadTransaction3.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 405, message = "Method Not Allowed", response = Void.class),
        @ApiResponse(code = 406, message = "Not Acceptable", response = Void.class),
        @ApiResponse(code = 429, message = "Too Many Requests", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class) })

    @PreAuthorize("hasAuthority('ROLE_AISP')")
    @OpenBankingAPI(
            obReference = OBReference.GET_TRANSACTIONS
    )
    @RequestMapping(value = "/transactions",
        produces = { "application/json; charset=utf-8" },
        method = RequestMethod.GET)
    ResponseEntity<OBReadTransaction3> getTransactions(
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") String page,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are " +
                    "represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional - " +
                    "set to 00:00:00 for just Date.  The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false) DateTime toBookingDateTime,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException;
}
