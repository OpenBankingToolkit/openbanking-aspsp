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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_3.internationalscheduledpayments;

import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.ExchangeRateVerifier;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalScheduledConsent4;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.payment.InternationalScheduledPaymentService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import org.joda.time.DateTime;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.payment.OBWriteFundsConfirmationResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsentResponse2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsentResponse4;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

import static com.forgerock.openbanking.aspsp.rs.api.payment.ApiVersionMatcher.getOBVersion;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-22T14:20:48.770Z")

@Controller("InternationalScheduledPaymentConsentsApiV3.1.3")
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

    public ResponseEntity<OBWriteInternationalScheduledConsentResponse4> createInternationalScheduledPaymentConsents(
            OBWriteInternationalScheduledConsent4 obWriteInternationalScheduledConsent4,
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
                            f.validateRisk(obWriteInternationalScheduledConsent4.getRisk());
                        }

                )
                .execute(
                        (String tppId) -> {
                            exchangeRateVerifier.verify(obWriteInternationalScheduledConsent4.getData().getInitiation().getExchangeRateInformation());

                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-client-id", tppId);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBWriteInternationalScheduledConsentResponse4.class, obWriteInternationalScheduledConsent4);
                        }
                );
    }

    public ResponseEntity<OBWriteInternationalScheduledConsentResponse4> getInternationalScheduledPaymentConsentsConsentId(
            String consentId,
            String authorization,
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
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-client-id", tppId);
                            ParameterizedTypeReference<OBWriteInternationalScheduledConsentResponse2> ptr = new ParameterizedTypeReference<OBWriteInternationalScheduledConsentResponse2>() {
                            };

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBWriteInternationalScheduledConsentResponse2.class);
                        }
                );
    }

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
        FRInternationalScheduledConsent4 payment = paymentsService.getPayment(consentId);

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
