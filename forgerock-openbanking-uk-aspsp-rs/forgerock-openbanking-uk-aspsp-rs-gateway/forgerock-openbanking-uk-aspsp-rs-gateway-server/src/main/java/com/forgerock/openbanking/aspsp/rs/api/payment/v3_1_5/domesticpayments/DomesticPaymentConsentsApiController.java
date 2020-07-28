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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_5.domesticpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticConsent5;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.payment.DomesticPaymentService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.payment.OBAuthorisation1;
import uk.org.openbanking.datamodel.payment.OBExternalAuthorisation1Code;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent4Data;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent4DataAuthorisation;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsentResponse5;
import uk.org.openbanking.datamodel.payment.OBWriteFundsConfirmationResponse1;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

import static com.forgerock.openbanking.aspsp.rs.api.payment.ApiVersionMatcher.getOBVersion;
import static uk.org.openbanking.datamodel.service.converter.payment.OBDomesticConverter.toOBDomestic2;

@Controller("DomesticPaymentConsentsApiV3.1.5")
public class DomesticPaymentConsentsApiController implements DomesticPaymentConsentsApi {

    private final RSEndpointWrapperService rsEndpointWrapperService;
    private final RsStoreGateway rsStoreGateway;
    private final DomesticPaymentService paymentsService;
    private final ObjectMapper mapper;

    public DomesticPaymentConsentsApiController(RSEndpointWrapperService rsEndpointWrapperService,
                                                RsStoreGateway rsStoreGateway,
                                                DomesticPaymentService paymentsService,
                                                ObjectMapper objectMapper) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.rsStoreGateway = rsStoreGateway;
        this.paymentsService = paymentsService;
        this.mapper = objectMapper;
    }

    @Override
    public ResponseEntity<OBWriteDomesticConsentResponse5> createDomesticPaymentConsents(
            OBWriteDomesticConsent4 obWriteDomesticConsent4,
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
                .filters(f ->
                        {
                            OBWriteDomesticConsent2 obWriteDomesticConsent2 = toOBWriteDomesticConsent2(obWriteDomesticConsent4);
                            f.verifyIdempotencyKeyLength(xIdempotencyKey);
                            f.verifyJwsDetachedSignature(xJwsSignature, request);
                            f.validateBalanceTransferPayment(obWriteDomesticConsent2);
                            f.validateMoneyTransferPayment(obWriteDomesticConsent2);
                            f.validatePaymPayment(obWriteDomesticConsent2);
                            f.validateRisk(obWriteDomesticConsent4.getRisk());
                        }
                )
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-client-id", tppId);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBWriteDomesticConsentResponse5.class, obWriteDomesticConsent4);
                        }
                );
    }

    private OBWriteDomesticConsent2 toOBWriteDomesticConsent2(OBWriteDomesticConsent4 obWriteDomesticConsent4) {
        return (new OBWriteDomesticConsent2())
                .data(toOBWriteDataDomesticConsent2(obWriteDomesticConsent4.getData()))
                .risk(obWriteDomesticConsent4.getRisk());
    }

    private OBWriteDataDomesticConsent2 toOBWriteDataDomesticConsent2(OBWriteDomesticConsent4Data data) {
        return data == null ? null : (new OBWriteDataDomesticConsent2())
                .initiation(toOBDomestic2(data.getInitiation()))
                .authorisation(toOBAuthorisation1(data.getAuthorisation()));
    }

    private OBAuthorisation1 toOBAuthorisation1(OBWriteDomesticConsent4DataAuthorisation authorisation) {
        return authorisation == null ? null : (new OBAuthorisation1())
                .authorisationType(OBExternalAuthorisation1Code.valueOf(authorisation.getAuthorisationType().name()))
                .completionDateTime(authorisation.getCompletionDateTime());
    }

    @Override
    public ResponseEntity<OBWriteDomesticConsentResponse5> getDomesticPaymentConsentsConsentId(
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

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBWriteDomesticConsentResponse5.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBWriteFundsConfirmationResponse1> getDomesticPaymentConsentsConsentIdFundsConfirmation(
            String consentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        FRDomesticConsent5 payment = paymentsService.getPayment(consentId);

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