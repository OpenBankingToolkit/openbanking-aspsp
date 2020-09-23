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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_3.domesticstandingorders;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrderDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticStandingOrderConsent;
import com.forgerock.openbanking.common.services.openbanking.frequency.FrequencyService;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.account.standingorder.StandingOrderService;
import com.forgerock.openbanking.common.services.store.payment.DomesticStandingOrderService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBExternalStandingOrderStatus1Code;
import uk.org.openbanking.datamodel.account.OBStandingOrder6;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderResponse4;
import uk.org.openbanking.datamodel.payment.OBWritePaymentDetailsResponse1;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

import static com.forgerock.openbanking.aspsp.rs.api.payment.ApiVersionMatcher.getOBVersion;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.toOBCashAccount51;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount2;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount4;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticStandingOrderConsentConverter.toFRWriteDomesticStandingOrderDataInitiation;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-22T14:20:48.770Z")

@Controller("DomesticStandingOrdersApiV3.1.3")
@Slf4j
public class DomesticStandingOrdersApiController implements DomesticStandingOrdersApi {

    private final DomesticStandingOrderService paymentsService;
    private final RSEndpointWrapperService rsEndpointWrapperService;
    private final RsStoreGateway rsStoreGateway;
    private final StandingOrderService standingOrderService;
    private final FrequencyService frequencyService;
    private final TppStoreService tppStoreService;

    public DomesticStandingOrdersApiController(DomesticStandingOrderService paymentsService,
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

    public ResponseEntity<OBWriteDomesticStandingOrderResponse4> createDomesticStandingOrders(
            OBWriteDomesticStandingOrder3 obWriteDomesticStandingOrder3,
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
        String consentId = obWriteDomesticStandingOrder3.getData().getConsentId();
        FRDomesticStandingOrderConsent payment = paymentsService.getPayment(consentId);

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
                    f.verifyRiskAndInitiation(
                            toFRWriteDomesticStandingOrderDataInitiation(obWriteDomesticStandingOrder3.getData().getInitiation()),
                            toFRRisk(obWriteDomesticStandingOrder3.getRisk()));
                    f.verifyJwsDetachedSignature(xJwsSignature, request);
                })
                .execute(
                        (String tppId) -> {
                            //Modify the status of the payment
                            log.info("Switch status of payment {} to 'accepted settlement in process'.", consentId);

                            FRWriteDomesticStandingOrderDataInitiation paymentInitiation = payment.getDomesticStandingOrderConsent().getData().getInitiation();
                            DateTime firstPaymentDateTime = paymentInitiation.getFirstPaymentDateTime();

                            OBStandingOrder6 standingOrder = new OBStandingOrder6()
                                    .accountId(payment.getAccountId())
                                    .standingOrderStatusCode(OBExternalStandingOrderStatus1Code.ACTIVE)
                                    .creditorAccount(toOBCashAccount51(paymentInitiation.getCreditorAccount()))
                                    .frequency(paymentInitiation.getFrequency())
                                    .reference(paymentInitiation.getReference())
                                    .firstPaymentDateTime(firstPaymentDateTime)
                                    .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount2(paymentInitiation.getFirstPaymentAmount()))
                                    .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount3(paymentInitiation.getRecurringPaymentAmount()))
                                    .nextPaymentDateTime(frequencyService.getNextDateTime(firstPaymentDateTime, paymentInitiation.getFrequency()))
                                    .finalPaymentDateTime(paymentInitiation.getFinalPaymentDateTime())
                                    .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount4(paymentInitiation.getFinalPaymentAmount()))
                                    .standingOrderId(payment.getId());

                            String pispId = tppStoreService.findByClientId(tppId)
                                    .map(tpp -> tpp.getId())
                                    .orElse(null);
                            standingOrderService.createStandingOrder(standingOrder, pispId);

                            log.info("Updating standing order '{}'", payment.getId());
                            payment.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);
                            paymentsService.updatePayment(payment);

                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-payment-id", consentId);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBWriteDomesticStandingOrderResponse4.class, obWriteDomesticStandingOrder3);
                        }
                );
    }

    public ResponseEntity<OBWriteDomesticStandingOrderResponse4> getDomesticStandingOrdersDomesticStandingOrderId(
            String domesticStandingOrderId,
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
                            return rsStoreGateway.toRsStore(request, new HttpHeaders(), OBWriteDomesticStandingOrderResponse4.class);
                        }
                );
    }

    public ResponseEntity<OBWritePaymentDetailsResponse1> getDomesticStandingOrdersDomesticStandingOrderIdPaymentDetails(
            String domesticStandingOrderId,
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
