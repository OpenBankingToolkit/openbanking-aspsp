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
package com.forgerock.openbanking.aspsp.rs.api.payment.v1_1.payments;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetupResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("PaymentsApiV1.1")
public class PaymentsApiController implements PaymentsApi {
    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentsApi.class);

    @Autowired
    private RSEndpointWrapperService rsEndpointWrapperService;
    @Autowired
    private RsStoreGateway rsStoreGateway;

    public ResponseEntity createSingleImmediatePayment(
            @ApiParam(value = "Every request will be processed only once per x-idempotency-key.  " +
                    "The Idempotency Key will be valid for 24 hours." ,required=true)
            @RequestHeader(value="x-idempotency-key", required=true) String xIdempotencyKey,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload." ,required=true)
            @RequestHeader(value="x-jws-signature", required=false) String xJwsSignature,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @ApiParam(value = "Setup a single immediate payment" ,required=true )
            @Valid
            @RequestBody OBPaymentSetup1 paymentSetupPOSTRequest,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        LOGGER.debug("Received a POST payment setup request '{}'.", paymentSetupPOSTRequest);

        return rsEndpointWrapperService.paymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
                .principal(principal)
                .filters(f ->
                        {
                            f.verifyIdempotencyKeyLength(xIdempotencyKey);
                            f.verifyJwsDetachedSignature(xJwsSignature, request);
                        }
                )
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-client-id", tppId);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBPaymentSetupResponse1.class, paymentSetupPOSTRequest);
                        }
                );
    }

    public ResponseEntity getSingleImmediatePayment(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify " +
                    "the payment setup resource.",required=true )
            @PathVariable("PaymentId") String paymentId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

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

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {

        return rsEndpointWrapperService.paymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
                .principal(principal)
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-client-id", tppId);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBPaymentSetupResponse1.class);
                        }
                );
    }
}
