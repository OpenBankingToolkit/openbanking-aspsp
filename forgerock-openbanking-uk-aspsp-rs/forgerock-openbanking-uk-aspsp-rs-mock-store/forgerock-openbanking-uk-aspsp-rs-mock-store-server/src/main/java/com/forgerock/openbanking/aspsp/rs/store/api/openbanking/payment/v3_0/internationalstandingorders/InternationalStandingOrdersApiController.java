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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_0.internationalstandingorders;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalStandingOrderConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalStandingOrderPaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalStandingOrderConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalStandingOrderPaymentSubmission;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalStandingOrderResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toOBInternationalStandingOrder1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConverter.toFRWriteInternationalStandingOrder;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("InternationalStandingOrdersApiV3.0")
@Slf4j
public class InternationalStandingOrdersApiController implements InternationalStandingOrdersApi {
    private InternationalStandingOrderConsentRepository internationalStandingOrderConsentRepository;
    private InternationalStandingOrderPaymentSubmissionRepository internationalStandingOrderPaymentSubmissionRepository;
    private ResourceLinkService resourceLinkService;

    public InternationalStandingOrdersApiController(InternationalStandingOrderConsentRepository internationalStandingOrderConsentRepository,
                                                    InternationalStandingOrderPaymentSubmissionRepository internationalStandingOrderPaymentSubmissionRepository,
                                                    ResourceLinkService resourceLinkService) {
        this.internationalStandingOrderConsentRepository = internationalStandingOrderConsentRepository;
        this.internationalStandingOrderPaymentSubmissionRepository = internationalStandingOrderPaymentSubmissionRepository;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity createInternationalStandingOrders(
            @ApiParam(value = "Default", required = true) @Valid @RequestBody OBWriteInternationalStandingOrder1 obWriteInternationalStandingOrder1,
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)

            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)

            @RequestHeader(value = "Authorization", required = true) String authorization,
            @ApiParam(value = "Every request will be processed only once per x-idempotency-key.  The Idempotency Key will be valid for 24 hours.", required = true)

            @RequestHeader(value = "x-idempotency-key", required = true) String xIdempotencyKey,
            @ApiParam(value = "A detached JWS signature of the body of the payload.", required = true)

            @RequestHeader(value = "x-jws-signature", required = true) String xJwsSignature,
            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")

            @RequestHeader(value = "x-fapi-customer-last-logged-time", required = false)
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
        log.debug("Received payment submission: '{}'", obWriteInternationalStandingOrder1);
        FRWriteInternationalStandingOrder frStandingOrder = toFRWriteInternationalStandingOrder(obWriteInternationalStandingOrder1);
        log.trace("Converted to: '{}'", frStandingOrder);

        String paymentId = frStandingOrder.getData().getConsentId();
        FRInternationalStandingOrderConsent paymentConsent = internationalStandingOrderConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRInternationalStandingOrderPaymentSubmission frPaymentSubmission = FRInternationalStandingOrderPaymentSubmission.builder()
                .id(paymentId)
                .internationalStandingOrder(frStandingOrder)
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .version(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(internationalStandingOrderPaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseEntity(frPaymentSubmission, paymentConsent));
    }

    @Override
    public ResponseEntity getInternationalStandingOrdersInternationalStandingOrderPaymentId(
            @ApiParam(value = "InternationalStandingOrderPaymentId", required = true) @PathVariable("InternationalStandingOrderPaymentId") String internationalStandingOrderPaymentId,
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)

            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)

            @RequestHeader(value = "Authorization", required = true) String authorization,
            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")

            @RequestHeader(value = "x-fapi-customer-last-logged-time", required = false)
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
        Optional<FRInternationalStandingOrderPaymentSubmission> isPaymentSubmission = internationalStandingOrderPaymentSubmissionRepository.findById(internationalStandingOrderPaymentId);
        if (!isPaymentSubmission.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + internationalStandingOrderPaymentId + "' can't be found");
        }
        FRInternationalStandingOrderPaymentSubmission frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRInternationalStandingOrderConsent> isPaymentSetup = internationalStandingOrderConsentRepository.findById(internationalStandingOrderPaymentId);
        if (!isPaymentSetup.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + internationalStandingOrderPaymentId + "' can't be found");
        }
        FRInternationalStandingOrderConsent frPaymentSetup = isPaymentSetup.get();
        return ResponseEntity.ok(responseEntity(frPaymentSubmission, frPaymentSetup));
    }

    private OBWriteInternationalStandingOrderResponse1 responseEntity(FRInternationalStandingOrderPaymentSubmission frPaymentSubmission, FRInternationalStandingOrderConsent FRInternationalStandingOrderConsent) {
        return new OBWriteInternationalStandingOrderResponse1()
                .data(new OBWriteDataInternationalStandingOrderResponse1()
                        .internationalStandingOrderId(frPaymentSubmission.getId())
                        .initiation(toOBInternationalStandingOrder1(frPaymentSubmission.getInternationalStandingOrder().getData().getInitiation()))
                        .creationDateTime(FRInternationalStandingOrderConsent.getCreated())
                        .statusUpdateDateTime(FRInternationalStandingOrderConsent.getStatusUpdate())
                        .status(FRInternationalStandingOrderConsent.getStatus().toOBExternalStatusCode1())
                        .consentId(FRInternationalStandingOrderConsent.getId())
                )
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> discovery.getV_3_0().getGetInternationalStandingOrder()))
                .meta(new Meta());
    }
}
