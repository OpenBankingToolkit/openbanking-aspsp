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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_0.domesticpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticPaymentSubmission2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.DomesticConsent5Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticPaymentSubmission2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticConsent5;
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
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteDomestic1;
import uk.org.openbanking.datamodel.payment.OBWriteDomestic2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static uk.org.openbanking.datamodel.service.converter.payment.OBDomesticConverter.toOBDomestic1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticConsentConverter.toOBWriteDomestic2;

@Controller("DomesticPaymentsApiV3.0")
@Slf4j
public class DomesticPaymentsApiController implements DomesticPaymentsApi {

    private DomesticConsent5Repository domesticConsentRepository;
    private DomesticPaymentSubmission2Repository domesticPaymentSubmissionRepository;
    private ResourceLinkService resourceLinkService;

    public DomesticPaymentsApiController(DomesticConsent5Repository domesticConsentRepository, DomesticPaymentSubmission2Repository domesticPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        this.domesticConsentRepository = domesticConsentRepository;
        this.domesticPaymentSubmissionRepository = domesticPaymentSubmissionRepository;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity createDomesticPayments(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBWriteDomestic1 obWriteDomestic1Param,

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
        log.debug("Received payment submission: {}", obWriteDomestic1Param);
        OBWriteDomestic2 payment = toOBWriteDomestic2(obWriteDomestic1Param);
        log.trace("Converted to: {}", payment.getClass());

        String paymentId = payment.getData().getConsentId();
        FRDomesticConsent5 paymentConsent = domesticConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                    // OB specifies a 400 when the id does not match an existing consent
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRDomesticPaymentSubmission2 frPaymentSubmission = FRDomesticPaymentSubmission2.builder()
                .id(payment.getData().getConsentId())
                .domesticPayment(payment)
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(domesticPaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(packagePayment(frPaymentSubmission, paymentConsent));
    }

    @Override
    public ResponseEntity getDomesticPaymentsDomesticPaymentId(
            @ApiParam(value = "DomesticPaymentId", required = true)
            @PathVariable("DomesticPaymentId") String domesticPaymentId,

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
        Optional<FRDomesticPaymentSubmission2> isPaymentSubmission = domesticPaymentSubmissionRepository.findById(domesticPaymentId);
        if (!isPaymentSubmission.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + domesticPaymentId + "' can't be found");
        }
        FRDomesticPaymentSubmission2 frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRDomesticConsent5> isPaymentSetup = domesticConsentRepository.findById(domesticPaymentId);
        if (!isPaymentSetup.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + domesticPaymentId + "' can't be found");
        }
        FRDomesticConsent5 frPaymentSetup = isPaymentSetup.get();
        return ResponseEntity.ok(packagePayment(frPaymentSubmission, frPaymentSetup));
    }

    private OBWriteDomesticResponse1 packagePayment(FRDomesticPaymentSubmission2 frPaymentSubmission, FRPaymentConsent paymentConsent) {
        return new OBWriteDomesticResponse1()
                .data(new OBWriteDataDomesticResponse1()
                .domesticPaymentId(frPaymentSubmission.getId())
                .initiation(toOBDomestic1(frPaymentSubmission.getDomesticPayment().getData().getInitiation()))
                .creationDateTime(paymentConsent.getCreated())
                .statusUpdateDateTime(paymentConsent.getStatusUpdate())
                .status(paymentConsent.getStatus().toOBTransactionIndividualStatus1Code())
                .consentId(paymentConsent.getId()))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> discovery.getV_3_0().getGetDomesticPayment()))
                .meta(new Meta());
    }
}
