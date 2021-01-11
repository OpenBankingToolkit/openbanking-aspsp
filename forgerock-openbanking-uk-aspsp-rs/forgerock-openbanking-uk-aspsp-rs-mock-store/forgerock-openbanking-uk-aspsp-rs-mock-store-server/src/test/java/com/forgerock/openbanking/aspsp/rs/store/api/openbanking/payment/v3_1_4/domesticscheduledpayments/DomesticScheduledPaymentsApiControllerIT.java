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

package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_4.domesticscheduledpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticScheduledConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticScheduledPaymentSubmissionRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDataDomesticScheduled;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticScheduled;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticScheduledConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticScheduledPaymentSubmission;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
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
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.*;

import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRPostalAddressTestDataFactory.aValidFRPostalAddress;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticScheduledConsentConverter.toOBDomesticScheduled2;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticScheduledConsentConverter.toOBWriteDomesticScheduled2DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticScheduledConverter.toOBWriteDomesticScheduled2;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring Integration test for {@link DomesticScheduledPaymentsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DomesticScheduledPaymentsApiControllerIT {

    private static final String RS_STORE_URL = "https://rs-store:";
    private static final String CONTEXT_PATH = "/open-banking/v3.1.4/pisp/domestic-scheduled-payments/";

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticScheduledConsentRepository consentRepository;
    @Autowired
    private DomesticScheduledPaymentSubmissionRepository submissionRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;
    @Autowired
    private SpringSecForTest springSecForTest;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetMissingDomesticPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent consent = saveConsent(FRReadRefundAccount.NO);
        FRWriteDomesticScheduled submissionRequest = JMockData.mock(FRWriteDomesticScheduled.class);
        FRDomesticScheduledPaymentSubmission submission = FRDomesticScheduledPaymentSubmission.builder()
                .id(consent.getId())
                .domesticScheduledPayment(submissionRequest)
                .build();

        // When
        HttpResponse<String> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testGetDomesticPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent consent = JMockData.mock(FRDomesticScheduledConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRDomesticScheduledPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<String> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateDomesticPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent consent = saveConsent(FRReadRefundAccount.NO);
        OBWriteDomesticScheduled2 submissionRequest = new OBWriteDomesticScheduled2()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteDataDomesticScheduled2()
                        .consentId(consent.getId())
                        .initiation(toOBDomesticScheduled2(consent.getInitiation())));

        // When
        HttpResponse<OBWriteDomesticScheduledResponse4> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteDomesticScheduledResponse4.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticScheduledResponse4 consentResponse = response.getBody();
        FRDomesticScheduledPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getDomesticScheduledPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consentResponse.getData().getRefund()).isNull();
        assertThat(toOBWriteDomesticScheduled2(submission.getDomesticScheduledPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_4);
    }

    @Test
    public void testCreateDomesticPaymentSubmission_refundNull() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent consent = saveConsent(null);
        OBWriteDomesticScheduled2 submissionRequest = new OBWriteDomesticScheduled2()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteDataDomesticScheduled2()
                        .consentId(consent.getId())
                        .initiation(toOBDomesticScheduled2(consent.getInitiation())));

        // When
        HttpResponse<OBWriteDomesticScheduledResponse4> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteDomesticScheduledResponse4.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticScheduledResponse4 consentResponse = response.getBody();
        FRDomesticScheduledPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getDomesticScheduledPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consentResponse.getData().getRefund()).isNull();
        assertThat(toOBWriteDomesticScheduled2(submission.getDomesticScheduledPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_4);
    }

    @Test
    public void testCreateDomesticPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent consent = saveConsent(FRReadRefundAccount.YES);
        OBWriteDomesticScheduled2 submissionRequest = new OBWriteDomesticScheduled2()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteDataDomesticScheduled2()
                        .consentId(consent.getId())
                        .initiation(toOBDomesticScheduled2(consent.getInitiation())));

        // When
        HttpResponse<OBWriteDomesticScheduledResponse4> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteDomesticScheduledResponse4.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticScheduledResponse4 rBody = response.getBody();
        FRDomesticScheduledPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getDomesticScheduledPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(rBody.getData().getConsentId());
        // Refund
        OBWriteDomesticResponse4DataRefund refund = rBody.getData().getRefund();
        assertThat(refund).isNotNull();
        assertThat(refund.getAccount().getIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getIdentification());
        assertThat(refund.getAccount().getName()).isEqualTo(consent.getInitiation().getDebtorAccount().getName());
        assertThat(refund.getAccount().getSchemeName()).isEqualTo(consent.getInitiation().getDebtorAccount().getSchemeName());
        assertThat(refund.getAccount().getSecondaryIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getSecondaryIdentification());

        assertThat(toOBWriteDomesticScheduled2(submission.getDomesticScheduledPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_4);
    }

    @Test
    public void testGetDomesticPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent consent = saveConsent(FRReadRefundAccount.NO);
        FRDomesticScheduledPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteDomesticScheduledResponse4> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticScheduledResponse4.class);

        // Then
        OBWriteDomesticScheduledResponse4Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteDomesticScheduled2DataInitiation(submission.getDomesticScheduledPayment().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetDomesticPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent consent = saveConsent(FRReadRefundAccount.YES);
        FRDomesticScheduledPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteDomesticScheduledResponse4> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticScheduledResponse4.class);

        // Then
        OBWriteDomesticScheduledResponse4Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNotNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteDomesticScheduled2DataInitiation(submission.getDomesticScheduledPayment().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    private FRDomesticScheduledPaymentSubmission savePaymentSubmission(FRDomesticScheduledConsent consent, String xIdempotencyKey) {
        FRWriteDomesticScheduled submissionRequest = FRWriteDomesticScheduled.builder()
                .risk(consent.getRisk())
                .data(FRWriteDataDomesticScheduled.builder()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation())
                        .build())
                .build();
        FRDomesticScheduledPaymentSubmission submission = FRDomesticScheduledPaymentSubmission.builder()
                .id(consent.getId())
                .domesticScheduledPayment(submissionRequest)
                .idempotencyKey(xIdempotencyKey)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRDomesticScheduledConsent saveConsent(FRReadRefundAccount frReadRefundAccount) {
        FRDomesticScheduledConsent consent = JMockData.mock(FRDomesticScheduledConsent.class);
        consent.getDomesticScheduledConsent().getData().setReadRefundAccount(frReadRefundAccount);
        consent.setId(IntentType.PAYMENT_DOMESTIC_SCHEDULED_CONSENT.generateIntentId());
        consent.getInitiation().setInstructedAmount(aValidFRAmount());
        consent.getInitiation().setCreditorPostalAddress(aValidFRPostalAddress());
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());
        consent.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);
        consentRepository.save(consent);
        return consent;
    }
}
