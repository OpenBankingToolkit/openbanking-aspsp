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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_3.file;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.FilePaymentSubmission2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.FileConsent5Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_1.report.PaymentReportFile2Service;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFilePaymentSubmission2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRFileConsent5;
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
import uk.org.openbanking.datamodel.payment.OBWriteDataFileResponse2;
import uk.org.openbanking.datamodel.payment.OBWriteFile2;
import uk.org.openbanking.datamodel.payment.OBWriteFileResponse2;
import uk.org.openbanking.datamodel.payment.OBWritePaymentDetailsResponse1;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRFileConsentConverter.toFRFileConsent2;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-22T14:20:48.770Z")

@Controller("FilePaymentsApiV3.1.3")
@Slf4j
public class FilePaymentsApiController implements FilePaymentsApi {

    private final FileConsent5Repository fileConsentRepository;
    private final FilePaymentSubmission2Repository filePaymentSubmissionRepository;
    private final PaymentReportFile2Service paymentReportFile1Service;
    private final ResourceLinkService resourceLinkService;

    public FilePaymentsApiController(FileConsent5Repository fileConsentRepository, FilePaymentSubmission2Repository filePaymentSubmissionRepository, PaymentReportFile2Service paymentReportFile1Service, ResourceLinkService resourceLinkService) {
        this.fileConsentRepository = fileConsentRepository;
        this.filePaymentSubmissionRepository = filePaymentSubmissionRepository;
        this.paymentReportFile1Service = paymentReportFile1Service;
        this.resourceLinkService = resourceLinkService;
    }

    public ResponseEntity<OBWriteFileResponse2> createFilePayments(
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
        log.debug("Received payment submission: {}", obWriteFile2);

        String paymentId = obWriteFile2.getData().getConsentId();
        FRFileConsent5 paymentConsent = fileConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRFilePaymentSubmission2 frPaymentSubmission = FRFilePaymentSubmission2.builder()
                .id(paymentId)
                .filePayment(obWriteFile2)
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(filePaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(packagePayment(frPaymentSubmission, paymentConsent));
    }

    public ResponseEntity<OBWriteFileResponse2> getFilePaymentsFilePaymentId(
            String filePaymentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
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

        Optional<FRFileConsent5> isPaymentSetup = fileConsentRepository.findById(filePaymentId);
        if (!isPaymentSetup.isPresent()) {
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND
                            .toOBError1(filePaymentId));
        }
        FRFileConsent5 frPaymentSetup = isPaymentSetup.get();

        return ResponseEntity.ok(packagePayment(frPaymentSubmission, frPaymentSetup));
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
        FRFileConsent5 consent = fileConsentRepository.findById(filePaymentId)
                .orElseThrow(() ->
                        new OBErrorResponseException(
                                HttpStatus.BAD_REQUEST,
                                OBRIErrorResponseCategory.REQUEST_INVALID,
                                OBRIErrorType.PAYMENT_ID_NOT_FOUND
                                        .toOBError1(filePaymentId))
                );
        log.debug("Consent '{}' exists so generating a report file for type: {}", consent.getId(), consent.getStatus(), consent.getFileType());
        final String reportFile = paymentReportFile1Service.createPaymentReport(toFRFileConsent2(consent));
        log.debug("Generated report file for consent: {}", consent.getId());
        return ResponseEntity.ok(reportFile);
    }

    private OBWriteFileResponse2 packagePayment(FRFilePaymentSubmission2 frPaymentSubmission, FRFileConsent5 frFileConsent) {
        return new OBWriteFileResponse2().data(new OBWriteDataFileResponse2()
                .filePaymentId(frPaymentSubmission.getId())
                .initiation(frPaymentSubmission.getFilePayment().getData().getInitiation())
                .creationDateTime(frFileConsent.getCreated())
                .statusUpdateDateTime(DateTime.now())
                .status(frFileConsent.getStatus().toOBExternalStatusCode1())
                .consentId(frFileConsent.getId()))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> getVersion(discovery).getGetFilePayment()))
                .meta(new Meta());
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_3();
    }

}
