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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_4.internationalscheduledpayments;

import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.ExchangeRateVerifier;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalScheduledConsent;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.payment.InternationalScheduledPaymentService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.payment.OBWriteFundsConfirmationResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsent5;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsentResponse5;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsentResponse6;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

import static com.forgerock.openbanking.common.utils.ApiVersionUtils.getOBVersion;

@Controller("InternationalScheduledPaymentConsentsApiV3.1.4")
public class InternationalScheduledPaymentConsentsApiController implements InternationalScheduledPaymentConsentsApi {

    private final RSEndpointWrapperService rsEndpointWrapperService;
    private final RsStoreGateway rsStoreGateway;
    private final ExchangeRateVerifier exchangeRateVerifier;
    private final InternationalScheduledPaymentService paymentsService;

    public InternationalScheduledPaymentConsentsApiController(RSEndpointWrapperService rsEndpointWrapperService,
                                                              RsStoreGateway rsStoreGateway,
                                                              ExchangeRateVerifier exchangeRateVerifier,
                                                              InternationalScheduledPaymentService paymentsService) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.rsStoreGateway = rsStoreGateway;
        this.exchangeRateVerifier = exchangeRateVerifier;
        this.paymentsService = paymentsService;
    }

    @Override
    public ResponseEntity<OBWriteInternationalScheduledConsentResponse5> createInternationalScheduledPaymentConsents(
            OBWriteInternationalScheduledConsent5 obWriteInternationalScheduledConsent5,
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
        return rsEndpointWrapperService.paymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .principal(principal)
                .obVersion(getOBVersion(request.getRequestURI()))
                .filters(f -> {
                            f.verifyIdempotencyKeyLength(xIdempotencyKey);
                            f.verifyJwsDetachedSignature(xJwsSignature, request);
                            f.validateRisk(obWriteInternationalScheduledConsent5.getRisk());
                        }

                )
                .execute(
                        (String tppId) -> {
                            exchangeRateVerifier.verify(obWriteInternationalScheduledConsent5.getData().getInitiation().getExchangeRateInformation());

                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-client-id", tppId);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBWriteInternationalScheduledConsentResponse5.class, obWriteInternationalScheduledConsent5);
                        }
                );
    }

    @Override
    public ResponseEntity<OBWriteInternationalScheduledConsentResponse5> getInternationalScheduledPaymentConsentsConsentId(
            String consentId,
            String authorization,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.paymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .principal(principal)
                .obVersion(getOBVersion(request.getRequestURI()))
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-client-id", tppId);
                            //ParameterizedTypeReference<OBWriteInternationalScheduledConsentResponse5> ptr = new ParameterizedTypeReference<OBWriteInternationalScheduledConsentResponse5>() { };
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBWriteInternationalScheduledConsentResponse6.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBWriteFundsConfirmationResponse1> getInternationalScheduledPaymentConsentsConsentIdFundsConfirmation(
            String consentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        FRInternationalScheduledConsent payment = paymentsService.getPayment(consentId);

        return rsEndpointWrapperService.paymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .payment(payment)
                .principal(principal)
                .obVersion(getOBVersion(request.getRequestURI()))
                .filters(f -> {
                    f.verifyConsentStatusForConfirmationOfFund();
                })
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBWriteFundsConfirmationResponse1.class);
                        }
                );
    }
}
