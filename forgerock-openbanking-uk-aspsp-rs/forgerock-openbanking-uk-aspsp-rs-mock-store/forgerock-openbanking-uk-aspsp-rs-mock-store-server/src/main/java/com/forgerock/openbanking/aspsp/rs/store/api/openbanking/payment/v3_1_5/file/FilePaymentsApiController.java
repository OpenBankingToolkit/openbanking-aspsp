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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_5.file;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.FileConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.FilePaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteFile;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.UnsupportedFileTypeException;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.report.PaymentReportFile1Service;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRFileConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRFilePaymentSubmission;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment4;
import uk.org.openbanking.datamodel.payment.OBWriteFile2;
import uk.org.openbanking.datamodel.payment.OBWriteFileResponse3;
import uk.org.openbanking.datamodel.payment.OBWriteFileResponse3Data;
import uk.org.openbanking.datamodel.payment.OBWritePaymentDetailsResponse1;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_5.ResponseStatusCodeConverter.toOBWriteFileResponse3DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBDebtorIdentification1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteFileConsentConverter.toOBWriteFile2DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteFileConverter.toFRWriteFile;

@Controller("FilePaymentsApiV3.1.5")
@Slf4j
public class FilePaymentsApiController implements FilePaymentsApi {

    private final FileConsentRepository fileConsentRepository;
    private final FilePaymentSubmissionRepository filePaymentSubmissionRepository;
    private final PaymentReportFile1Service paymentReportFileService;
    private final ResourceLinkService resourceLinkService;

    public FilePaymentsApiController(FileConsentRepository fileConsentRepository, FilePaymentSubmissionRepository filePaymentSubmissionRepository, PaymentReportFile1Service paymentReportFileService, ResourceLinkService resourceLinkService) {
        this.fileConsentRepository = fileConsentRepository;
        this.filePaymentSubmissionRepository = filePaymentSubmissionRepository;
        this.paymentReportFileService = paymentReportFileService;
        this.resourceLinkService = resourceLinkService;
    }

    public ResponseEntity<OBWriteFileResponse3> createFilePayments(
            OBWriteFile2 obWriteFile2,
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

    public ResponseEntity<OBWriteFileResponse3> getFilePaymentsFilePaymentId(
            String filePaymentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
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

    public ResponseEntity<OBWritePaymentDetailsResponse1> getFilePaymentsFilePaymentIdPaymentDetails(
            String filePaymentId,
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

    public ResponseEntity getFilePaymentsFilePaymentIdReportFile(
            String filePaymentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
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
        try {
            final String reportFile = paymentReportFileService.createPaymentReport(consent);
            log.debug("Generated report file for consent: '{}'", consent.getId());
            return ResponseEntity.ok(reportFile);
        } catch (UnsupportedFileTypeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("{ \"Description\" : \"Report for file type " + consent.getFileType().getFileType() + " not supported\" }");
        }
    }

    private OBWriteFileResponse3 responseEntity(FRFilePaymentSubmission frPaymentSubmission, FRFileConsent frFileConsent) {
        return new OBWriteFileResponse3()
                .data(new OBWriteFileResponse3Data()
                        .filePaymentId(frPaymentSubmission.getId())
                        .initiation(toOBWriteFile2DataInitiation(frPaymentSubmission.getFilePayment().getData().getInitiation()))
                        .creationDateTime(frFileConsent.getCreated())
                        .statusUpdateDateTime(DateTime.now())
                        .status(toOBWriteFileResponse3DataStatus(frFileConsent.getStatus()))
                        .debtor(toOBDebtorIdentification1(frFileConsent.getInitiation().getDebtorAccount()))
                        .consentId(frFileConsent.getId()))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> getVersion(discovery).getGetFilePayment()))
                .meta(new Meta());
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_5();
    }

}
