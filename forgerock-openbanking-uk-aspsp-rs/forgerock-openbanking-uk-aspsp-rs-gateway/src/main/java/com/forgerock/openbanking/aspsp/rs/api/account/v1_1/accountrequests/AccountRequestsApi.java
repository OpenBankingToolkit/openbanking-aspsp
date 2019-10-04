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
package com.forgerock.openbanking.aspsp.rs.api.account.v1_1.accountrequests;

import com.forgerock.openbanking.common.openbanking.OBGroupName;
import com.forgerock.openbanking.common.openbanking.OBReference;
import com.forgerock.openbanking.common.openbanking.OpenBankingAPI;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.*;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uk.org.openbanking.datamodel.account.OBReadRequest1;
import uk.org.openbanking.datamodel.account.OBReadResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-16T08:37:28.078Z")

@Api(tags = "v1.1-AccountRequests", description = "the account-requests API")
@OpenBankingAPI(
        obVersion = "1.1",
        obGroupName = OBGroupName.AISP,
        obReference = OBReference.ACCOUNT_REQUEST
)
@RequestMapping(
        value = "/open-banking/v1.1"
)
public interface AccountRequestsApi {

    @ApiOperation(value = "Create an account request", notes = "Create an account request",
            response = OBReadResponse1.class, authorizations = {
        @Authorization(value = "TPPOAuth2Security", scopes = {
            @AuthorizationScope(scope = "tpp_client_credential", description = "TPP Client Credential Scope")
            })
    }, tags={ })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "FRAccount1 Request resource successfully created",
                response = OBReadResponse1.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 405, message = "Method Not Allowed", response = Void.class),
        @ApiResponse(code = 406, message = "Not Acceptable", response = Void.class),
        @ApiResponse(code = 429, message = "Too Many Requests", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class) })

    @PreAuthorize("hasAuthority('ROLE_AISP')")
    @OpenBankingAPI(
            obReference = OBReference.CREATE_ACCOUNT_REQUEST
    )
    @RequestMapping(value = "/account-requests",
            produces = { "application/json; charset=utf-8" },
            consumes = { "application/json; charset=utf-8" },
            method = RequestMethod.POST
    )
    ResponseEntity<OBReadResponse1> createAccountRequest(
            @ApiParam(value = "Create an Account Request", required = true)
            @Valid
            @RequestBody OBReadRequest1 body,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = false) String xJwsSignature,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are " +
                    "represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException;


    @ApiOperation(value = "Delete an account request", notes = "Delete an account request", response = Void.class,
            authorizations = {
        @Authorization(value = "TPPOAuth2Security", scopes = {
            @AuthorizationScope(scope = "tpp_client_credential", description = "TPP Client Credential Scope")
            })
    }, tags={  })
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Account Request resource successfully deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 405, message = "Method Not Allowed", response = Void.class),
        @ApiResponse(code = 406, message = "Not Acceptable", response = Void.class),
        @ApiResponse(code = 429, message = "Too Many Requests", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class) })

    @PreAuthorize("hasAuthority('ROLE_AISP')")
    @OpenBankingAPI(
            obReference = OBReference.DELETE_ACCOUNT_REQUEST
    )
    @RequestMapping(value = "/account-requests/{AccountRequestId}",
        produces = { "application/json; charset=utf-8" },
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteAccountRequest(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify the account " +
                    "request resource.", required = true)
            @PathVariable("AccountRequestId") String accountRequestId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException;


    @ApiOperation(value = "Get an account request", notes = "Get an account request", response = OBReadResponse1.class,
            authorizations = {
        @Authorization(value = "TPPOAuth2Security", scopes = {
            @AuthorizationScope(scope = "tpp_client_credential", description = "TPP Client Credential Scope")
            })
    }, tags={ })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Account Request resource successfully retrieved",
                response = OBReadResponse1.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 405, message = "Method Not Allowed", response = Void.class),
        @ApiResponse(code = 406, message = "Not Acceptable", response = Void.class),
        @ApiResponse(code = 429, message = "Too Many Requests", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class) })

    @PreAuthorize("hasAuthority('ROLE_AISP')")
    @OpenBankingAPI(
            obReference = OBReference.GET_ACCOUNT_REQUEST
    )
    @RequestMapping(value = "/account-requests/{AccountRequestId}", produces = { "application/json; charset=utf-8" },
        method = RequestMethod.GET)
    ResponseEntity<OBReadResponse1> getAccountRequest(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify " +
                    "the account request resource.", required = true)
            @PathVariable("AccountRequestId") String accountRequestId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

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

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException;

}
