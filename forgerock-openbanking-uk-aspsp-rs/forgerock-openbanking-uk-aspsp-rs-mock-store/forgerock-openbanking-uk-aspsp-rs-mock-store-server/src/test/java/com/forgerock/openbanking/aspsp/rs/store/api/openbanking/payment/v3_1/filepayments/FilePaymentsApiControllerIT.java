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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.filepayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.FileConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.FilePaymentSubmissionRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteFile;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.report.PaymentReportFile1Service;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRFileConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRFilePaymentSubmission;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.*;

import java.math.BigDecimal;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteFileConsentConverter.toOBFile2;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteFileConsentConverter.toOBWriteFile2DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteFileConverter.toOBWriteFile2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class FilePaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private FileConsentRepository consentRepository;
    @Autowired
    private FilePaymentSubmissionRepository submissionRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;
    @Autowired
    private SpringSecForTest springSecForTest;

    @MockBean
    private PaymentReportFile1Service paymentReportFileService;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetInternationalPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFileConsent consent = saveConsent();
        FRFilePaymentSubmission submission = savePaymentSubmission(consent);

        // When
        HttpResponse<OBWriteFileConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(OBWriteFileConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(toOBFile2(submission.getFilePayment().getData().getInitiation()));
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(consent.getCreated());
    }

    @Test
    public void testGetMissingInternationalPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFileConsent consent = saveConsent();
        FRWriteFile submissionRequest = JMockData.mock(FRWriteFile.class);
        FRFilePaymentSubmission submission = FRFilePaymentSubmission.builder()
                .id(consent.getId())
                .filePayment(submissionRequest)
                .build();

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testGetInternationalPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFileConsent consent = JMockData.mock(FRFileConsent.class);
        consent.setId(IntentType.PAYMENT_FILE_CONSENT.generateIntentId());
        FRFilePaymentSubmission submission = savePaymentSubmission(consent);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateInternationalPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFileConsent consent = saveConsent();
        OBWriteFile2 submissionRequest = new OBWriteFile2()
                .data(new OBWriteFile2Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteFile2DataInitiation(consent.getInitiation())));

        // When
        HttpResponse<OBWriteFileResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteFileResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteFileResponse2 consentResponse = response.getBody();
        FRFilePaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getFilePaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(toOBWriteFile2(submission.getFilePayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1);
    }

    @Test
    public void testDuplicatePaymentInitiationShouldReturnForbidden() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFileConsent consent = saveConsent();
        FRFilePaymentSubmission submission = savePaymentSubmission(consent);

        OBWriteFile2 obWriteFile = new OBWriteFile2();
        obWriteFile.data(new OBWriteFile2Data()
                .consentId(submission.getId())
                .initiation(toOBWriteFile2DataInitiation(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obWriteFile)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void testMissingConsentOnPaymentInitiationShouldReturnNotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFileConsent consent = JMockData.mock(FRFileConsent.class);
        consent.setId(IntentType.PAYMENT_FILE_CONSENT.generateIntentId());
        consent.getInitiation().setControlSum(new BigDecimal("1001.1"));
        consent.getInitiation().setFileType("UK.OBIE.PaymentInitiation.3.0");
        consent.getInitiation().setFileReference("Test");
        consent.getInitiation().setNumberOfTransactions("100");
        consent.getInitiation().setFileHash("sdjhgfksfkshfjksh");
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());

        OBWriteFile2 submissionRequest = new OBWriteFile2()
                .data(new OBWriteFile2Data()
                .consentId(consent.getId())
                .initiation(toOBWriteFile2DataInitiation(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testMissingConsentOnGetFileReportShouldReturnNotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);

        // When
        HttpResponse response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payments/123/report-file")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .header("Accept","application/json; charset=utf-8" )
                .asString();
        log.debug("Response: {}", response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testReportNotReady_NotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFileConsent consent = saveConsent(ConsentStatusCode.PENDING);
        when(paymentReportFileService.createPaymentReport(eq(consent))).thenThrow(
             new OBErrorResponseException(HttpStatus.NOT_FOUND,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.FILE_PAYMENT_REPORT_NOT_READY.toOBError1(consent.getId(), consent.getStatus().toString(), ""))

        );

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payments/"+consent.getId()+"/report-file")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .header("Accept","application/json; charset=utf-8" )
                .asString();
        log.debug("Response: {}", response.getBody());
        // Then
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void testGetReportFileSuccess() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFileConsent consent = saveConsent();
        when(paymentReportFileService.createPaymentReport(eq(consent))).thenReturn("{\"Data\": {\"DomesticPayments\": []} }");

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payments/"+consent.getId()+"/report-file")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .header("Accept","application/json; charset=utf-8" )
                .asString();
        log.debug("Response: {}", response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("{\"Data\": {\"DomesticPayments\": []} }");
    }

    private FRFilePaymentSubmission savePaymentSubmission(FRFileConsent consent) {
        FRWriteFile submissionRequest = JMockData.mock(FRWriteFile.class);
        submissionRequest.getData().getInitiation().setRequestedExecutionDateTime(null);
        submissionRequest.getData().getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        FRFilePaymentSubmission submission = FRFilePaymentSubmission.builder()
                .id(consent.getId())
                .filePayment(submissionRequest)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRFileConsent saveConsent() {
        return saveConsent(ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);
    }

    private FRFileConsent saveConsent(ConsentStatusCode statusCode) {
        FRFileConsent consent = JMockData.mock(FRFileConsent.class);
        consent.setId(IntentType.PAYMENT_FILE_CONSENT.generateIntentId());
        consent.getInitiation().setControlSum(new BigDecimal("1001.1"));
        consent.getInitiation().setFileType("UK.OBIE.PaymentInitiation.3.0");
        consent.getInitiation().setFileReference("Test");
        consent.getInitiation().setNumberOfTransactions("100");
        consent.getInitiation().setFileHash("sdjhgfksfkshfjksh");
        consent.getInitiation().setRequestedExecutionDateTime(null);
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        consent.setStatus(statusCode);
        consentRepository.save(consent);
        return consent;
    }

}