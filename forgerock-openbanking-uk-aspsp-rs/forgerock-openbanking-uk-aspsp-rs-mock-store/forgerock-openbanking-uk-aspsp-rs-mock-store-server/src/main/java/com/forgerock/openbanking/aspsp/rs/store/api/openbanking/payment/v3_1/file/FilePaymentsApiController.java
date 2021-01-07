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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.file;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.FilePaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.FileConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteFile;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.report.PaymentReportFile1Service;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRFilePaymentSubmission;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRFileConsent;
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
import uk.org.openbanking.datamodel.payment.OBWriteDataFileResponse2;
import uk.org.openbanking.datamodel.payment.OBWriteFile2;
import uk.org.openbanking.datamodel.payment.OBWriteFileResponse2;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteFileConsentConverter.toOBFile2;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteFileConverter.toFRWriteFile;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("FilePaymentsApiV3.1")
@Slf4j
public class FilePaymentsApiController implements FilePaymentsApi {
    private final FileConsentRepository fileConsentRepository;
    private final FilePaymentSubmissionRepository filePaymentSubmissionRepository;
    private final PaymentReportFile1Service paymentReportFileService;
    private final ResourceLinkService resourceLinkService;

    public FilePaymentsApiController(FileConsentRepository fileConsentRepository,
                                     FilePaymentSubmissionRepository filePaymentSubmissionRepository,
                                     PaymentReportFile1Service paymentReportFileService,
                                     ResourceLinkService resourceLinkService) {
        this.fileConsentRepository = fileConsentRepository;
        this.filePaymentSubmissionRepository = filePaymentSubmissionRepository;
        this.paymentReportFileService = paymentReportFileService;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity createFilePayments(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBWriteFile2 obWriteFile2,

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
        log.debug("Received payment submission: '{}'", obWriteFile2);
        FRWriteFile frWriteFile = toFRWriteFile(obWriteFile2);
        log.trace("Converted to: '{}'", frWriteFile);

        String paymentId = obWriteFile2.getData().getConsentId();
        FRFileConsent paymentConsent = fileConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRFilePaymentSubmission frPaymentSubmission = FRFilePaymentSubmission.builder()
                .id(paymentId)
                .filePayment(frWriteFile)
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(filePaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseEntity(frPaymentSubmission, paymentConsent));
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
        Optional<FRFilePaymentSubmission> isPaymentSubmission = filePaymentSubmissionRepository.findById(filePaymentId);
        if (!isPaymentSubmission.isPresent()) {
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.PAYMENT_SUBMISSION_NOT_FOUND
                            .toOBError1(filePaymentId));
        }
        FRFilePaymentSubmission frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRFileConsent> isPaymentSetup = fileConsentRepository.findById(filePaymentId);
        if (!isPaymentSetup.isPresent()) {
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND
                            .toOBError1(filePaymentId));
        }
        FRFileConsent frPaymentSetup = isPaymentSetup.get();

        return ResponseEntity.ok(responseEntity(frPaymentSubmission, frPaymentSetup));
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
        FRFileConsent consent = fileConsentRepository.findById(filePaymentId)
                .orElseThrow(() ->
                        new OBErrorResponseException(
                                HttpStatus.BAD_REQUEST,
                                OBRIErrorResponseCategory.REQUEST_INVALID,
                                OBRIErrorType.PAYMENT_ID_NOT_FOUND
                                        .toOBError1(filePaymentId))
                );
        log.debug("Consent '{}' exists so generating a report file for type: '{}'", consent.getId(), consent.getStatus(), consent.getFileType());
        final String reportFile = paymentReportFileService.createPaymentReport(consent);
        log.debug("Generated report file for consent: '{}'", consent.getId());
        return ResponseEntity.ok(reportFile);
    }

    private OBWriteFileResponse2 responseEntity(FRFilePaymentSubmission frPaymentSubmission, FRFileConsent frFileConsent) {
        return new OBWriteFileResponse2().data(new OBWriteDataFileResponse2()
                .filePaymentId(frPaymentSubmission.getId())
                .initiation(toOBFile2(frPaymentSubmission.getFilePayment().getData().getInitiation()))
                .creationDateTime(frFileConsent.getCreated())
                .statusUpdateDateTime(DateTime.now())
                .status(frFileConsent.getStatus().toOBExternalStatusCode1())
                .consentId(frFileConsent.getId()))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> discovery.getV_3_1().getGetFilePayment()))
                .meta(new Meta());
    }

}
