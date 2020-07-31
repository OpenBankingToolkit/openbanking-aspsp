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
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_5.internationalstandingorders;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalStandingOrderConsent4;
import com.forgerock.openbanking.common.services.openbanking.frequency.FrequencyService;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.account.standingorder.StandingOrderService;
import com.forgerock.openbanking.common.services.store.payment.InternationalStandingOrderService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBExternalStandingOrderStatus1Code;
import uk.org.openbanking.datamodel.account.OBStandingOrder5;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4DataInitiation;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderResponse7;
import uk.org.openbanking.datamodel.payment.OBWritePaymentDetailsResponse1;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

import static com.forgerock.openbanking.aspsp.rs.api.payment.ApiVersionMatcher.getOBVersion;
import static uk.org.openbanking.datamodel.service.converter.payment.OBAccountConverter.toOBCashAccount5;
import static uk.org.openbanking.datamodel.service.converter.payment.OBAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount;

@Controller("InternationalStandingOrdersApiV3.1.5")
@Slf4j
public class InternationalStandingOrdersApiController implements InternationalStandingOrdersApi {

    private final InternationalStandingOrderService paymentsService;
    private final RSEndpointWrapperService rsEndpointWrapperService;
    private final RsStoreGateway rsStoreGateway;
    private final StandingOrderService standingOrderService;
    private final FrequencyService frequencyService;
    private final TppStoreService tppStoreService;

    public InternationalStandingOrdersApiController(InternationalStandingOrderService paymentsService,
                                                    RSEndpointWrapperService rsEndpointWrapperService,
                                                    RsStoreGateway rsStoreGateway,
                                                    StandingOrderService standingOrderService,
                                                    FrequencyService frequencyService,
                                                    TppStoreService tppStoreService) {
        this.paymentsService = paymentsService;
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.rsStoreGateway = rsStoreGateway;
        this.standingOrderService = standingOrderService;
        this.frequencyService = frequencyService;
        this.tppStoreService = tppStoreService;
    }

    public ResponseEntity<OBWriteInternationalStandingOrderResponse7> createInternationalStandingOrders(
            OBWriteInternationalStandingOrder4 obWriteInternationalStandingOrder4,
            String authorization,
            String xIdempotencyKey,
            String xJwsSignature,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        String consentId = obWriteInternationalStandingOrder4.getData().getConsentId();
        FRInternationalStandingOrderConsent4 payment = paymentsService.getPayment(consentId);

        return rsEndpointWrapperService.paymentSubmissionEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .payment(payment)
                .principal(principal)
                .obVersion(getOBVersion(request.getRequestURI()))
                .filters(f -> {
                    f.verifyPaymentIdWithAccessToken();
                    f.verifyIdempotencyKeyLength(xIdempotencyKey);
                    f.verifyPaymentStatus();
                    f.verifyRiskAndInitiation(obWriteInternationalStandingOrder4.getData().getInitiation(), obWriteInternationalStandingOrder4.getRisk());
                    f.verifyJwsDetachedSignature(xJwsSignature, request);

                })
                .execute(
                        (String tppId) -> {
                            //Modify the status of the payment
                            log.info("Switch status of payment {} to 'accepted settlement in process'.", consentId);

                            payment.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);
                            log.info("Updating payment");
                            paymentsService.updatePayment(payment);

                            OBWriteInternationalStandingOrder4DataInitiation initiation = payment.getInitiation();
                            OBStandingOrder5 standingOrder = new OBStandingOrder5()
                                    .accountId(payment.getAccountId())
                                    .standingOrderStatusCode(OBExternalStandingOrderStatus1Code.ACTIVE)
                                    .creditorAccount(toOBCashAccount5(initiation.getCreditorAccount()))
                                    .frequency(initiation.getFrequency())
                                    .reference(initiation.getReference())
                                    .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                                    .firstPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                                    .nextPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                                    .nextPaymentDateTime(frequencyService.getNextDateTime(initiation.getFirstPaymentDateTime(), initiation.getFrequency()))
                                    .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                                    .finalPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                                    .standingOrderId(payment.getId());

                            String pispId = tppStoreService.findPispIdByTppId(tppId);
                            standingOrderService.createStandingOrder(standingOrder, pispId);

                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-payment-id", consentId);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBWriteInternationalStandingOrderResponse7.class, obWriteInternationalStandingOrder4);
                        }
                );
    }

    public ResponseEntity<OBWriteInternationalStandingOrderResponse7> getInternationalStandingOrdersInternationalStandingOrderPaymentId(
            String internationalStandingOrderPaymentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.paymentsRequestPaymentIdEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .principal(principal)
                .obVersion(getOBVersion(request.getRequestURI()))
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBWriteInternationalStandingOrderResponse7.class);
                        }
                );
    }

    public ResponseEntity<OBWritePaymentDetailsResponse1> getInternationalStandingOrdersInternationalStandingOrderPaymentIdPaymentDetails(
            String internationalStandingOrderPaymentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        // Optional endpoint - not implemented
        return new ResponseEntity<OBWritePaymentDetailsResponse1>(HttpStatus.NOT_IMPLEMENTED);
    }

}
