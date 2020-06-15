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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_0.file;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.FileConsent2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.FilePaymentSubmission2Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_1.report.PaymentReportFile2Service;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFileConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFilePaymentSubmission2;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
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
import uk.org.openbanking.datamodel.payment.OBWriteDataFileResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteFile1;
import uk.org.openbanking.datamodel.payment.OBWriteFile2;
import uk.org.openbanking.datamodel.payment.OBWriteFileResponse1;
import uk.org.openbanking.datamodel.service.converter.payment.OBFileConverter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteFileConsentConverter.toOBWriteFile2;

@Controller("FilePaymentsApiV3.0")
@Slf4j
public class FilePaymentsApiController implements FilePaymentsApi {
    private final FileConsent2Repository fileConsentRepository;
    private final FilePaymentSubmission2Repository filePaymentSubmissionRepository;
    private final PaymentReportFile2Service paymentReportFile1Service;
    private final ResourceLinkService resourceLinkService;

    @Autowired
    public FilePaymentsApiController(FileConsent2Repository fileConsentRepository, FilePaymentSubmission2Repository filePaymentSubmissionRepository, PaymentReportFile2Service paymentReportFile1Service, ResourceLinkService resourceLinkService) {
        this.fileConsentRepository = fileConsentRepository;
        this.filePaymentSubmissionRepository = filePaymentSubmissionRepository;
        this.paymentReportFile1Service = paymentReportFile1Service;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity createFilePayments(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBWriteFile1 obWriteFile1Param,

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

            Principal principal) throws OBErrorResponseException {
        log.debug("Received payment submission: {}", obWriteFile1Param);
        OBWriteFile2 payment = toOBWriteFile2(obWriteFile1Param);
        log.trace("Converted to: {}", payment.getClass());

        String paymentId = payment.getData().getConsentId();
        FRFileConsent2 paymentConsent = fileConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRFilePaymentSubmission2 frPaymentSubmission = FRFilePaymentSubmission2.builder()
                .id(paymentId)
                .filePayment(payment)
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(filePaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(packagePayment(frPaymentSubmission, paymentConsent));
    }

    @Override
    public ResponseEntity getFilePaymentsFilePaymentId(
            @ApiParam(value = "FilePaymentId", required = true)
            @PathVariable("FilePaymentId") String filePaymentId,

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
        Optional<FRFilePaymentSubmission2> isPaymentSubmission = filePaymentSubmissionRepository.findById(filePaymentId);
        if (!isPaymentSubmission.isPresent()) {
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.PAYMENT_SUBMISSION_NOT_FOUND
                            .toOBError1(filePaymentId));
        }
        FRFilePaymentSubmission2 frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRFileConsent2> isPaymentSetup = fileConsentRepository.findById(filePaymentId);
        if (!isPaymentSetup.isPresent()) {
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND
                            .toOBError1(filePaymentId));
        }
        FRFileConsent2 frPaymentSetup = isPaymentSetup.get();

        return ResponseEntity.ok(packagePayment(frPaymentSubmission, frPaymentSetup));
    }

    @Override
    public ResponseEntity getFilePaymentsFilePaymentIdReportFile(
            @ApiParam(value = "FilePaymentId", required = true)
            @PathVariable("FilePaymentId") String filePaymentId,

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
        FRFileConsent2 consent = fileConsentRepository.findById(filePaymentId)
                .orElseThrow(() ->
                        new OBErrorResponseException(
                                HttpStatus.BAD_REQUEST,
                                OBRIErrorResponseCategory.REQUEST_INVALID,
                                OBRIErrorType.PAYMENT_ID_NOT_FOUND
                                        .toOBError1(filePaymentId))
                );
        log.debug("Consent '{}' exists with status: {} so generating a report file for type: {}", consent.getId(), consent.getStatus(), consent.getFileType());
        final String reportFile = paymentReportFile1Service.createPaymentReport(consent);
        log.debug("Generated report file for consent: {}", consent.getId());
        return ResponseEntity.ok(reportFile);
    }

    private OBWriteFileResponse1 packagePayment(FRFilePaymentSubmission2 frPaymentSubmission, FRFileConsent2 frFileConsent) {
        return new OBWriteFileResponse1().data(new OBWriteDataFileResponse1()
                .filePaymentId(frPaymentSubmission.getId())
                .initiation(OBFileConverter.toOBFile1(frPaymentSubmission.getFilePayment().getData().getInitiation()))
                .creationDateTime(frFileConsent.getCreated())
                .statusUpdateDateTime(DateTime.now())
                .status(frFileConsent.getStatus().toOBExternalStatusCode1())
                .consentId(frFileConsent.getId()))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> discovery.getV_3_0().getGetFilePayment()))
                .meta(new Meta());
    }

}
