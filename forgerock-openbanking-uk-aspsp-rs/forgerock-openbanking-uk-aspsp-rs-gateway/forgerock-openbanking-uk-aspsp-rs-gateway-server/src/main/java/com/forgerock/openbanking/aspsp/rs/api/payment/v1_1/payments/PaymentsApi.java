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
package com.forgerock.openbanking.aspsp.rs.api.payment.v1_1.payments;

import com.forgerock.openbanking.api.annotations.OBGroupName;
import com.forgerock.openbanking.api.annotations.OBReference;
import com.forgerock.openbanking.api.annotations.OpenBankingAPI;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.*;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetupResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;


@Api(tags = {"v1.1-Payments",}, description = "the payments API")
@OpenBankingAPI(
        obVersion = "1.1",
        obGroupName = OBGroupName.PISP,
        obReference = OBReference.SINGLE_PAYMENTS
)
@RequestMapping(value = "/open-banking/v1.1")
public interface PaymentsApi {

    @ApiOperation(
            value = "Create a single immediate payment",
            notes = "Create a single immediate payment",
            response = OBPaymentSetupResponse1.class,
            authorizations = {
                    @Authorization(value = "TPPOAuth2Security", scopes = {
                            @AuthorizationScope(scope = "tpp_client_credential", description = "TPP Client Credential Scope")
                    })
            },
            tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Payment setup resource successfully created",
                    response = OBPaymentSetupResponse1.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 405, message = "Method Not Allowed", response = Void.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = Void.class),
            @ApiResponse(code = 429, message = "Too Many Requests", response = Void.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class)
    })

    @PreAuthorize("hasAuthority('ROLE_PISP')")
    @OpenBankingAPI(
            obReference = OBReference.CREATE_SINGLE_IMMEDIATE_PAYMENT
    )
    @RequestMapping(
            value = "/payments",
            produces = {"application/json; charset=utf-8"},
            consumes = {"application/json; charset=utf-8"},
            method = RequestMethod.POST
    )
    ResponseEntity<OBPaymentSetupResponse1> createSingleImmediatePayment(

            @ApiParam(value = "Every request will be processed only once per x-idempotency-key.  " +
                    "The Idempotency Key will be valid for 24 hours.", required = true)
            @RequestHeader(value = "x-idempotency-key", required = true) String xIdempotencyKey,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload.", required =
                    false)
            @RequestHeader(value = "x-jws-signature", required = false) String xJwsSignature,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are " +
                    "represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Setup a single immediate payment", required = true)
            @Valid
            @RequestBody OBPaymentSetup1 body,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException;


    @ApiOperation(
            value = "Get a single immediate payment",
            notes = "Get a single immediate payment",
            response = OBPaymentSetupResponse1.class,
            authorizations = {
                    @Authorization(value = "PSUOAuth2Security", scopes = {
                            @AuthorizationScope(scope = "payments", description = "Generic payment scope")
                    }),
                    @Authorization(value = "TPPOAuth2Security", scopes = {
                            @AuthorizationScope(scope = "tpp_client_credential",
                                    description = "TPP Client Credential Scope")
                    })
            },
            tags = {}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Payment resource successfully retrieved",
                    response = OBPaymentSetupResponse1.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 405, message = "Method Not Allowed", response = Void.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = Void.class),
            @ApiResponse(code = 429, message = "Too Many Requests", response = Void.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class)})

    @PreAuthorize("hasAuthority('ROLE_PISP')")
    @OpenBankingAPI(
            obReference = OBReference.GET_SINGLE_IMMEDIATE_PAYMENT
    )
    @RequestMapping(
            value = "/payments/{PaymentId}",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET
    )
    ResponseEntity<OBPaymentSetupResponse1> getSingleImmediatePayment(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify " +
                    "the payment setup resource.", required = true)
            @PathVariable("PaymentId") String paymentId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers " +
                    "are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException;

}
