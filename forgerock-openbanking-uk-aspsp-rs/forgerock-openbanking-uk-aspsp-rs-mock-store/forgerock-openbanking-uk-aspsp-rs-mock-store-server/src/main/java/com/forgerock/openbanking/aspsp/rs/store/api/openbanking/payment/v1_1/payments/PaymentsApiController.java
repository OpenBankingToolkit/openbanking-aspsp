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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v1_1.payments;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.FRPaymentSetupRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRPaymentSetup;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.payment.OBPaymentDataSetupResponse1;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetupResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConsentConverter.toFRWriteDomesticConsent;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConsentConverter.toOBInitiation1;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("PaymentsApiV1.1")
@Slf4j
public class PaymentsApiController implements PaymentsApi {

    @Autowired
    private FRPaymentSetupRepository frPaymentSetupRepository;
    @Autowired
    private TppRepository tppRepository;
    @Autowired
    private ResourceLinkService resourceLinkService;
    @Autowired
    private ConsentMetricService consentMetricService;

    @Override
    public ResponseEntity createSingleImmediatePayment(
            @ApiParam(value = "Every request will be processed only once per x-idempotency-key.  " +
                    "The Idempotency Key will be valid for 24 hours.", required = true)
            @RequestHeader(value = "x-idempotency-key", required = true) String xIdempotencyKey,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = false) String xJwsSignature,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP ID")
            @RequestHeader(value = "x-ob-client-id", required = false) String clientId,

            @ApiParam(value = "Setup a single immediate payment", required = true)
            @Valid
            @RequestBody OBPaymentSetup1 paymentSetupPOSTRequest,

            HttpServletRequest httpServletRequest
    ) throws OBErrorResponseException {
        log.debug("Received a POST payment setup request '{}'.", paymentSetupPOSTRequest);

        final Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);

        Optional<FRPaymentSetup> consentByIdempotencyKey = frPaymentSetupRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, paymentSetupPOSTRequest, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getPaymentSetupRequest());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageIntoPaymentSetupResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent.");

        FRPaymentSetup frPaymentSetup = new FRPaymentSetup();
        frPaymentSetup.setId(IntentType.PAYMENT_SINGLE_REQUEST.generateIntentId());

        frPaymentSetup.setStatus(ConsentStatusCode.ACCEPTEDTECHNICALVALIDATION);
        frPaymentSetup.setPaymentSetupRequest(toFRWriteDomesticConsent(paymentSetupPOSTRequest));
        frPaymentSetup.setPisp(tpp);
        frPaymentSetup.setIdempotencyKey(xIdempotencyKey);
        frPaymentSetup.setObVersion(VersionPathExtractor.getVersionFromPath(httpServletRequest));

        consentMetricService.sendConsentActivity(new ConsentStatusEntry(frPaymentSetup.getId(), frPaymentSetup.getStatus().name()));
        frPaymentSetup = frPaymentSetupRepository.save(frPaymentSetup);

        return ResponseEntity.status(HttpStatus.CREATED).body(packageIntoPaymentSetupResponse(frPaymentSetup));
    }

    @Override
    public ResponseEntity getSingleImmediatePayment(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify " +
                    "the payment setup resource.", required = true)
            @PathVariable("PaymentId") String paymentId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are " +
                    "represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId
    ) throws OBErrorResponseException {

        Optional<FRPaymentSetup> isPaymentSetup = frPaymentSetupRepository.findById(paymentId);
        if (isPaymentSetup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup '" + paymentId + "' can't be found");
        }
        FRPaymentSetup frPaymentSetup = isPaymentSetup.get();

        return ResponseEntity.ok(packageIntoPaymentSetupResponse(frPaymentSetup));
    }

    private OBPaymentSetupResponse1 packageIntoPaymentSetupResponse(FRPaymentSetup frPaymentSetup) {
        OBPaymentDataSetupResponse1 data = new OBPaymentDataSetupResponse1()
                .paymentId(frPaymentSetup.getId())
                .status(frPaymentSetup.getStatus().toOBTransactionIndividualStatus1Code())
                .creationDateTime(frPaymentSetup.getCreated())
                .initiation(toOBInitiation1(frPaymentSetup.getPaymentSetupRequest().getData().getInitiation()));
        return new OBPaymentSetupResponse1()
                .data(data)
                .risk(toOBRisk1(frPaymentSetup.getPaymentSetupRequest().getRisk()))
                .links(resourceLinkService.toSelfLink(frPaymentSetup, discovery -> discovery.getV_1_1().getGetSingleImmediatePayment()))
                .meta(new Meta());
    }





}
