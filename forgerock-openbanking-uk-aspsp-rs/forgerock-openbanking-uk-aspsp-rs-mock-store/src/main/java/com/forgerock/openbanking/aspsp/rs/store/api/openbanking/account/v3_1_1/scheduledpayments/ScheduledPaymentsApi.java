/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_1.scheduledpayments;

import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.*;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadScheduledPayment2;

import java.util.List;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Api(tags = "v3.1.1-Scheduled-Payments", description = "the scheduled-payments API")
@RequestMapping(value = "/open-banking/v3.1.1/aisp")
public interface ScheduledPaymentsApi {
    
    @ApiOperation(value = "Get Account Scheduled Payments", nickname = "getAccountScheduledPayments",
            notes = "Get Scheduled Payments related to an account", response = OBReadScheduledPayment2.class, authorizations = {
            @Authorization(value = "PSUOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Ability to get Accounts information")
            })
    }, tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account Scheduled Payment successfully retrieved", response = OBReadScheduledPayment2.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    @RequestMapping(value = "/accounts/{AccountId}/scheduled-payments",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity<OBReadScheduledPayment2> getAccountScheduledPayments(
            @ApiParam(value = "A unique identifier used to identify the account resource.", required = true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

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

            @ApiParam(value = "The OB permissions")
            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @ApiParam(value = "The origin http url")
            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException;

    @ApiOperation(value = "Get Scheduled Payments", nickname = "getScheduledPayments", notes = "Get Scheduled Payments", response = OBReadScheduledPayment2.class, authorizations = {
            @Authorization(value = "PSUOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Ability to get Accounts information")
            })
    }, tags={ })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account Scheduled Payment successfully retrieved", response = OBReadScheduledPayment2.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    @RequestMapping(value = "/scheduled-payments",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity<OBReadScheduledPayment2> getScheduledPayments(
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

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

            @ApiParam(value = "The OB account IDs")
            @RequestHeader(value = "x-ob-account-ids", required = true) List<String> accountIds,

            @ApiParam(value = "The OB permissions")
            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @ApiParam(value = "The origin http url")
            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException;
}
