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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_1_3;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsent1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsentResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Api(value = "funds-confirmation-consents", description = "the funds-confirmation-consents API")

@RequestMapping(value = "/open-banking/v3.1.3/cbpii")
public interface FundsConfirmationConsentsApi {

    String FUNDS_CONFIRMATION_CONSENTS_PATH = "/funds-confirmation-consents";

    @ApiOperation(value = "Create Funds Confirmation Consent", nickname = "createFundsConfirmationConsents", notes = "", response = OBFundsConfirmationConsentResponse1.class, authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "fundsconfirmations", description = "Funds confirmation entitlement")
            })
    }, tags = {"Funds Confirmations",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Funds Confirmation Consent Created", response = OBFundsConfirmationConsentResponse1.class),
            @ApiResponse(code = 400, message = "Bad request", response = OBErrorResponse1.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden", response = OBErrorResponse1.class),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = OBErrorResponse1.class)})

    @RequestMapping(value = FUNDS_CONFIRMATION_CONSENTS_PATH,
            produces = {"application/json; charset=utf-8"},
            consumes = {"application/json; charset=utf-8"},
            method = RequestMethod.POST)
    ResponseEntity<OBFundsConfirmationConsentResponse1> createFundsConfirmationConsent(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBFundsConfirmationConsent1 obFundsConfirmationConsent,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-auth-date", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiAuthDate,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @ApiParam(value = "The PISP ID")
            @RequestHeader(value = "x-ob-pisp-id", required = false) String pispId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException;


    @ApiOperation(value = "Get Funds Confirmation Consent", nickname = "getFundsConfirmationConsentsConsentId", notes = "", response = OBFundsConfirmationConsentResponse1.class, authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "fundsconfirmations", description = "Funds confirmation entitlement")
            })
    }, tags = {"Funds Confirmations",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Funds Confirmation Consent Read", response = OBFundsConfirmationConsentResponse1.class),
            @ApiResponse(code = 400, message = "Bad request", response = OBErrorResponse1.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden", response = OBErrorResponse1.class),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = OBErrorResponse1.class)})
    @RequestMapping(value = FUNDS_CONFIRMATION_CONSENTS_PATH,
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET)
    ResponseEntity<OBFundsConfirmationConsentResponse1> getFundsConfirmationConsentsConsentId(
            @ApiParam(value = "ConsentId", required = true)
            @PathVariable("ConsentId") String consentId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-auth-date", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiAuthDate,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @ApiParam(value = "The PISP ID" )
            @RequestHeader(value="x-ob-pisp-id", required=false) String pispId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException;


    @ApiOperation(value = "Delete Funds Confirmation Consent", nickname = "deleteFundsConfirmationConsentsConsentId", notes = "", authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "fundsconfirmations", description = "Funds confirmation entitlement")
            })
    }, tags = {"Funds Confirmations",})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Funds Confirmation Consent Deleted"),
            @ApiResponse(code = 400, message = "Bad request", response = OBErrorResponse1.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden", response = OBErrorResponse1.class),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = OBErrorResponse1.class)})
    @RequestMapping(value = FUNDS_CONFIRMATION_CONSENTS_PATH+"/{ConsentId}",
            method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteFundsConfirmationConsentsConsentId(
            @ApiParam(value = "ConsentId", required = true)
            @PathVariable("ConsentId") String consentId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-auth-date", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiAuthDate,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @ApiParam(value = "The PISP ID" )
            @RequestHeader(value="x-ob-pisp-id", required=false) String pispId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException;


}
