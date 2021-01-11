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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_4.domesticpayments;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.payment.DomesticPaymentService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.payment.OBWriteDomestic2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticResponse4;
import uk.org.openbanking.datamodel.payment.OBWritePaymentDetailsResponse1;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConsentConverter.toFRWriteDomesticDataInitiation;
import static com.forgerock.openbanking.common.utils.ApiVersionUtils.getOBVersion;

@Controller("DomesticPaymentsApiV3.1.4")
@Slf4j
public class DomesticPaymentsApiController implements DomesticPaymentsApi {

    private final DomesticPaymentService paymentsService;
    private final RSEndpointWrapperService rsEndpointWrapperService;
    private final RsStoreGateway rsStoreGateway;

    public DomesticPaymentsApiController(DomesticPaymentService paymentsService, RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway) {
        this.paymentsService = paymentsService;
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.rsStoreGateway = rsStoreGateway;
    }

    @Override
    public ResponseEntity<OBWriteDomesticResponse4> createDomesticPayments(
            OBWriteDomestic2 obWriteDomestic2,
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
        String consentId = obWriteDomestic2.getData().getConsentId();
        FRDomesticConsent payment = paymentsService.getPayment(consentId);

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
                    f.verifyRiskAndInitiation(toFRWriteDomesticDataInitiation(obWriteDomestic2.getData().getInitiation()), toFRRisk(obWriteDomestic2.getRisk()));
                    f.verifyJwsDetachedSignature(xJwsSignature, request);
                })
                .execute(
                        (String tppId) -> {
                            //Modify the status of the payment
                            log.info("Switch status of payment {} to 'accepted settlement in process'.", consentId);

                            payment.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);
                            log.info("Updating payment");
                            paymentsService.updatePayment(payment);

                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-payment-id", consentId);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBWriteDomesticResponse4.class, obWriteDomestic2);
                        }
                );
    }

    @Override
    public ResponseEntity<OBWriteDomesticResponse4> getDomesticPaymentsDomesticPaymentId(
            String domesticPaymentId,
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
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBWriteDomesticResponse4.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBWritePaymentDetailsResponse1> getDomesticPaymentsDomesticPaymentIdPaymentDetails(
            String domesticPaymentId,
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
