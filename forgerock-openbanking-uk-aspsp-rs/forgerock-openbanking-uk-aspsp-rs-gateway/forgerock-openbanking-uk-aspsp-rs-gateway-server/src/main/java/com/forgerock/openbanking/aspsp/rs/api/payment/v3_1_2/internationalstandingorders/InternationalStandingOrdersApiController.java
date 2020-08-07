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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_2.internationalstandingorders;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRInternationalStandingOrderConsent5;
import com.forgerock.openbanking.common.services.openbanking.frequency.FrequencyService;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.account.standingorder.StandingOrderService;
import com.forgerock.openbanking.common.services.store.payment.InternationalStandingOrderService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.OBExternalStandingOrderStatus1Code;
import uk.org.openbanking.datamodel.account.OBStandingOrder6;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4DataInitiation;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderResponse3;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount2;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount4;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBCashAccountConverter.toOBCashAccount51;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static uk.org.openbanking.datamodel.service.converter.payment.OBInternationalStandingOrderConverter.toOBWriteInternationalStandingOrder4DataInitiation;

@Controller("InternationalStandingOrdersApiV3.1.2")
public class InternationalStandingOrdersApiController implements InternationalStandingOrdersApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternationalStandingOrdersApiController.class);

    private InternationalStandingOrderService paymentsService;
    private RSEndpointWrapperService rsEndpointWrapperService;
    private RsStoreGateway rsStoreGateway;
    private StandingOrderService standingOrderService;
    private FrequencyService frequencyService;
    private TppStoreService tppStoreService;

    public InternationalStandingOrdersApiController(InternationalStandingOrderService paymentsService, RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway, StandingOrderService standingOrderService, FrequencyService frequencyService, TppStoreService tppStoreService) {
        this.paymentsService = paymentsService;
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.rsStoreGateway = rsStoreGateway;
        this.standingOrderService = standingOrderService;
        this.frequencyService = frequencyService;
        this.tppStoreService = tppStoreService;
    }

    @Override
    public ResponseEntity<OBWriteInternationalStandingOrderResponse3> createInternationalStandingOrders(
            @ApiParam(value = "Default", required = true) @Valid @RequestBody OBWriteInternationalStandingOrder3 OBWriteInternationalStandingOrder3Param,
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)

            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)

            @RequestHeader(value = "Authorization", required = true) String authorization,
            @ApiParam(value = "Every request will be processed only once per x-idempotency-key.  The Idempotency Key will be valid for 24 hours.", required = true)

            @RequestHeader(value = "x-idempotency-key", required = true) String xIdempotencyKey,
            @ApiParam(value = "A detached JWS signature of the body of the payload.", required = true)

            @RequestHeader(value = "x-jws-signature", required = true) String xJwsSignature,
            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")

            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,
            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")

            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,
            @ApiParam(value = "An RFC4122 UID used as a correlation id.")

            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,
            @ApiParam(value = "Indicates the user-agent that the PSU is using.")

            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        String consentId = OBWriteInternationalStandingOrder3Param.getData().getConsentId();
        FRInternationalStandingOrderConsent5 payment = paymentsService.getPayment(consentId);

        return rsEndpointWrapperService.paymentSubmissionEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
                .payment(payment)
                .principal(principal)
                .filters(f -> {
                    f.verifyPaymentIdWithAccessToken();
                    f.verifyIdempotencyKeyLength(xIdempotencyKey);
                    f.verifyPaymentStatus();
                    f.verifyRiskAndInitiation(toOBWriteInternationalStandingOrder4DataInitiation(OBWriteInternationalStandingOrder3Param.getData().getInitiation()), OBWriteInternationalStandingOrder3Param.getRisk());
                    f.verifyJwsDetachedSignature(xJwsSignature, request);

                })
                .execute(
                        (String tppId) -> {
                            //Modify the status of the payment
                            LOGGER.info("Switch status of payment {} to 'accepted settlement in process'.", consentId);

                            payment.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);
                            LOGGER.info("Updating payment");
                            paymentsService.updatePayment(payment);

                            OBWriteInternationalStandingOrder4DataInitiation initiation = payment.getInitiation();
                            OBStandingOrder6 standingOrder = new OBStandingOrder6()
                                    .accountId(payment.getAccountId())
                                    .standingOrderStatusCode(OBExternalStandingOrderStatus1Code.ACTIVE)
                                    .creditorAccount(toOBCashAccount51(initiation.getCreditorAccount()))
                                    .frequency(initiation.getFrequency())
                                    .reference(initiation.getReference())
                                    .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                                    .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount2(initiation.getInstructedAmount()))
                                    .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount3(initiation.getInstructedAmount()))
                                    .nextPaymentDateTime(frequencyService.getNextDateTime(initiation.getFirstPaymentDateTime(), initiation.getFrequency()))
                                    .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                                    .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount4(initiation.getInstructedAmount()))
                                    .standingOrderId(payment.getId());

                            String pispId = tppStoreService.findPispIdByTppId(tppId);
                            standingOrderService.createStandingOrder(standingOrder, pispId);

                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-payment-id", consentId);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBWriteInternationalStandingOrderResponse3.class, OBWriteInternationalStandingOrder3Param);
                        }
                );
    }

    @Override
    public ResponseEntity<OBWriteInternationalStandingOrderResponse3> getInternationalStandingOrdersInternationalStandingOrderPaymentId(
            @ApiParam(value = "InternationalStandingOrderPaymentId", required = true)
            @PathVariable("InternationalStandingOrderPaymentId") String internationalStandingOrderPaymentId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.paymentsRequestPaymentIdEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
                .principal(principal)
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBWriteInternationalStandingOrderResponse3.class);
                        }
                );
    }

}
