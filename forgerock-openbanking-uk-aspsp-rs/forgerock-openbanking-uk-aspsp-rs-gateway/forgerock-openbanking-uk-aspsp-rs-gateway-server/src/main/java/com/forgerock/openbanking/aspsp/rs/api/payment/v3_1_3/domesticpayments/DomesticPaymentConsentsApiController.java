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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_3.domesticpayments;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticConsent2;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.payment.DomesticPaymentService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.payment.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-10-10T14:05:22.993+01:00")

@Controller("DomesticPaymentConsentsApiV3.1.3")
public class DomesticPaymentConsentsApiController implements DomesticPaymentConsentsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomesticPaymentConsentsApiController.class);

    @Autowired
    private RSEndpointWrapperService rsEndpointWrapperService;
    @Autowired
    private RsStoreGateway rsStoreGateway;
    @Autowired
    private DomesticPaymentService paymentsService;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public ResponseEntity<OBWriteDomesticConsentResponse3> createDomesticPaymentConsents(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBWriteDomesticConsent3 obWriteDomesticConsent3Param,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Every request will be processed only once per x-idempotency-key.  The Idempotency Key will be valid for 24 hours. ", required = true)
            @RequestHeader(value = "x-idempotency-key", required = true) String xIdempotencyKey,

            @ApiParam(value = "A detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = true) String xJwsSignature,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-auth-date", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiAuthDate,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.paymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .principal(principal)
                .filters(f ->
                        {
                            OBWriteDomesticConsent2 obWriteDomesticConsent2 = toOBWriteDomesticConsent2(obWriteDomesticConsent3Param);
                            f.verifyIdempotencyKeyLength(xIdempotencyKey);
                            f.verifyJwsDetachedSignature(xJwsSignature, request);
                            f.validateBalanceTransferPayment(obWriteDomesticConsent2);
                            f.validateMoneyTransferPayment(obWriteDomesticConsent2);
                            f.validatePaymPayment(obWriteDomesticConsent2);
                        }
                )
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-client-id", tppId);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBWriteDomesticConsentResponse3.class, obWriteDomesticConsent3Param);
                        }
                );
    }

    @Override
    public ResponseEntity<OBWriteDomesticConsentResponse3> getDomesticPaymentConsentsConsentId(
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

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.paymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .principal(principal)
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-client-id", tppId);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBWriteDomesticConsentResponse2.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBWriteFundsConfirmationResponse1> getDomesticPaymentConsentsConsentIdFundsConfirmation(
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

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        FRDomesticConsent2 payment = paymentsService.getPayment(consentId);

        return rsEndpointWrapperService.paymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .payment(payment)
                .principal(principal)
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

    // TODO #216 - move the following into new converters within the uk-datamodel repo
    private OBWriteDomesticConsent2 toOBWriteDomesticConsent2(OBWriteDomesticConsent3 obWriteDomesticConsent3Param) {
        return (new OBWriteDomesticConsent2())
                .data((new OBWriteDataDomesticConsent2())
                        .authorisation(toOBAuthorisation1(obWriteDomesticConsent3Param.getData().getAuthorisation()))
                        .initiation(toOBDomestic2(obWriteDomesticConsent3Param.getData().getInitiation())))
                .risk(obWriteDomesticConsent3Param.getRisk());
    }

    private OBAuthorisation1 toOBAuthorisation1(OBWriteDomesticConsent3DataAuthorisation authorisation) {
        return (new OBAuthorisation1())
                .authorisationType(toOBExternalAuthorisation1Code(authorisation.getAuthorisationType()))
                .completionDateTime(authorisation.getCompletionDateTime());
    }

    private OBExternalAuthorisation1Code toOBExternalAuthorisation1Code(OBWriteDomesticConsent3DataAuthorisation.AuthorisationTypeEnum authorisationType) {
        return OBExternalAuthorisation1Code.valueOf(authorisationType.getValue());
    }

    private OBDomestic2 toOBDomestic2(OBWriteDomestic2DataInitiation initiation) {
        return new OBDomestic2()
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .creditorPostalAddress(initiation.getCreditorPostalAddress())
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .instructionIdentification(initiation.getInstructionIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()));
    }

    private OBRemittanceInformation1 toOBRemittanceInformation1(OBWriteDomestic2DataInitiationRemittanceInformation remittanceInformation) {
        return (new OBRemittanceInformation1())
                .unstructured(remittanceInformation.getUnstructured())
                .reference(remittanceInformation.getReference());
    }

    private OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBWriteDomestic2DataInitiationInstructedAmount instructedAmount) {
        return (new OBActiveOrHistoricCurrencyAndAmount())
                .currency(instructedAmount.getCurrency())
                .amount(instructedAmount.getAmount());
    }

    private OBCashAccount3 toOBCashAccount3(OBWriteDomestic2DataInitiationDebtorAccount debtorAccount) {
        return (new OBCashAccount3())
                .schemeName(debtorAccount.getSchemeName())
                .identification(debtorAccount.getIdentification())
                .name(debtorAccount.getName())
                .secondaryIdentification(debtorAccount.getSecondaryIdentification());
    }

    private OBCashAccount3 toOBCashAccount3(OBWriteDomestic2DataInitiationCreditorAccount creditorAccount) {
        return (new OBCashAccount3())
                .schemeName(creditorAccount.getSchemeName())
                .identification(creditorAccount.getIdentification())
                .name(creditorAccount.getName())
                .secondaryIdentification(creditorAccount.getSecondaryIdentification());
    }

}