/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.file;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.FileConsent2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.FilePaymentSubmission2Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.commons.configuration.discovery.ResourceLinkService;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.filepayment.v3_1.report.PaymentReportFile2Service;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRFileConsent2;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRFilePaymentSubmission2;
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

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("FilePaymentsApiV3.1")
@Slf4j
public class FilePaymentsApiController implements FilePaymentsApi {
    private final FileConsent2Repository fileConsentRepository;
    private final FilePaymentSubmission2Repository filePaymentSubmissionRepository;
    private final PaymentReportFile2Service paymentReportFile1Service;
    private final ResourceLinkService resourceLinkService;

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
            @RequestBody OBWriteFile2 obWriteFile2Param,

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
        log.debug("Received payment submission: {}", obWriteFile2Param);

        String paymentId = obWriteFile2Param.getData().getConsentId();
        FRFileConsent2 paymentConsent = fileConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRFilePaymentSubmission2 frPaymentSubmission = FRFilePaymentSubmission2.builder()
                .id(paymentId)
                .filePayment(obWriteFile2Param)
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
        log.debug("Consent '{}' exists so generating a report file for type: {}", consent.getId(), consent.getStatus(), consent.getFileType());
        final String reportFile = paymentReportFile1Service.createPaymentReport(consent);
        log.debug("Generated report file for consent: {}", consent.getId());
        return ResponseEntity.ok(reportFile);
    }

    private OBWriteFileResponse2 packagePayment(FRFilePaymentSubmission2 frPaymentSubmission, FRFileConsent2 frFileConsent) {
        return new OBWriteFileResponse2().data(new OBWriteDataFileResponse2()
                .filePaymentId(frPaymentSubmission.getId())
                .initiation(frPaymentSubmission.getFilePayment().getData().getInitiation())
                .creationDateTime(frFileConsent.getCreated())
                .statusUpdateDateTime(DateTime.now())
                .status(frFileConsent.getStatus().toOBExternalStatusCode1())
                .consentId(frFileConsent.getId()))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> discovery.getV_3_1().getGetFilePayment()))
                .meta(new Meta());
    }

}
