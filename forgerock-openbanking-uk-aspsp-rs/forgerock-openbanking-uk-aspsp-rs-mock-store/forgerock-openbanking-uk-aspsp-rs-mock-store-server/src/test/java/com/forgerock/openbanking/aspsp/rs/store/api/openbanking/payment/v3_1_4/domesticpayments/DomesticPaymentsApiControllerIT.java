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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_4.domesticpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticPaymentSubmissionRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDataDomestic;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomestic;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticPaymentSubmission;
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
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConsentConverter.toOBDomestic2;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConsentConverter.toOBWriteDomestic2DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConverter.toOBWriteDomestic2;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_4.domesticpayments.DomesticPaymentsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DomesticPaymentsApiControllerIT {

    private static final String RS_STORE_URL = "https://rs-store:";
    private static final String CONTEXT_PATH = "/open-banking/v3.1.4/pisp/domestic-payments/";

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticConsentRepository consentRepository;
    @Autowired
    private DomesticPaymentSubmissionRepository submissionRepository;
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
        FRDomesticConsent consent = saveConsent(FRReadRefundAccount.NO);
        FRWriteDomestic submissionRequest = JMockData.mock(FRWriteDomestic.class);
        FRDomesticPaymentSubmission submission = FRDomesticPaymentSubmission.builder()
                .id(consent.getId())
                .domesticPayment(submissionRequest)
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
        FRDomesticConsent consent = JMockData.mock(FRDomesticConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRDomesticPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

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
        FRDomesticConsent consent = saveConsent(FRReadRefundAccount.NO);
        OBWriteDomestic2 submissionRequest = new OBWriteDomestic2()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteDataDomestic2()
                        .consentId(consent.getId())
                        .initiation(toOBDomestic2(consent.getInitiation())));

        // When
        HttpResponse<OBWriteDomesticResponse4> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteDomesticResponse4.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticResponse4 consentResponse = response.getBody();
        FRDomesticPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getDomesticPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consentResponse.getData().getRefund()).isNull();
        assertThat(toOBWriteDomestic2(submission.getDomesticPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_4);
    }

    @Test
    public void testCreateDomesticPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent consent = saveConsent(FRReadRefundAccount.YES);
        OBWriteDomestic2 submissionRequest = new OBWriteDomestic2()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteDataDomestic2()
                        .consentId(consent.getId())
                        .initiation(toOBDomestic2(consent.getInitiation())));

        // When
        HttpResponse<OBWriteDomesticResponse4> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteDomesticResponse4.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticResponse4 rBody = response.getBody();
        FRDomesticPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getDomesticPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(rBody.getData().getConsentId());
        // Refund
        OBWriteDomesticResponse4DataRefund refund = rBody.getData().getRefund();
        assertThat(refund).isNotNull();
        assertThat(refund.getAccount().getIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getIdentification());
        assertThat(refund.getAccount().getName()).isEqualTo(consent.getInitiation().getDebtorAccount().getName());
        assertThat(refund.getAccount().getSchemeName()).isEqualTo(consent.getInitiation().getDebtorAccount().getSchemeName());
        assertThat(refund.getAccount().getSecondaryIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getSecondaryIdentification());

        assertThat(toOBWriteDomestic2(submission.getDomesticPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_4);
    }

    @Test
    public void testGetDomesticPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent consent = saveConsent(FRReadRefundAccount.NO);
        FRDomesticPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteDomesticResponse4> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticResponse4.class);

        // Then
        OBWriteDomesticResponse4Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteDomestic2DataInitiation(submission.getDomesticPayment().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetDomesticPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent consent = saveConsent(FRReadRefundAccount.YES);
        FRDomesticPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteDomesticResponse4> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticResponse4.class);

        // Then
        OBWriteDomesticResponse4Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNotNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteDomestic2DataInitiation(submission.getDomesticPayment().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    private FRDomesticPaymentSubmission savePaymentSubmission(FRDomesticConsent consent, String xIdempotencyKey) {
        FRWriteDomestic submissionRequest = FRWriteDomestic.builder()
                .risk(consent.getRisk())
                .data(FRWriteDataDomestic.builder()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation())
                        .build())
                .build();
        FRDomesticPaymentSubmission submission = FRDomesticPaymentSubmission.builder()
                .id(consent.getId())
                .domesticPayment(submissionRequest)
                .idempotencyKey(xIdempotencyKey)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRDomesticConsent saveConsent(FRReadRefundAccount frReadRefundAccount) {
        FRDomesticConsent consent = JMockData.mock(FRDomesticConsent.class);
        consent.getDomesticConsent().getData().setReadRefundAccount(frReadRefundAccount);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
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
