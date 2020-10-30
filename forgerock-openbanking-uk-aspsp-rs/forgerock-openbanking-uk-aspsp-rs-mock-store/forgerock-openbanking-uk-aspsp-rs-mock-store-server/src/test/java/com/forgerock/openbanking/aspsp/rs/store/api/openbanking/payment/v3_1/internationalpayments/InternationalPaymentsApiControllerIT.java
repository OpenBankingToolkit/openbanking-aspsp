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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.internationalpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalPaymentSubmissionRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternational;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalPaymentSubmission;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternational2;
import uk.org.openbanking.datamodel.payment.OBWriteInternational2;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3Data;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsentResponse2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalResponse2;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRDataInitiationCreditorAgentTestDataFactory.aValidFRDataInitiationCreditorAgent;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRDataInitiationCreditorTestDataFactory.aValidFRDataInitiationCreditor;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRFRExchangeRateInformationTestDataFactory.aValidFRExchangeRateInformation;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConsentConverter.toOBInternational2;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConsentConverter.toOBWriteInternational3DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConverter.toOBWriteInternational2;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.internationalpayments.InternationalPaymentsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InternationalPaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalConsentRepository consentRepository;
    @Autowired
    private InternationalPaymentSubmissionRepository submissionRepository;
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
    public void testGetInternationalPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = saveConsent();
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        FRInternationalPaymentSubmission submission = savePaymentSubmission(consent);

        // When
        HttpResponse<OBWriteInternationalConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(toOBInternational2(submission.getInternationalPayment().getData().getInitiation()));
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetMissingInternationalPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = saveConsent();
        FRWriteInternational submissionRequest = JMockData.mock(FRWriteInternational.class);
        FRInternationalPaymentSubmission submission = FRInternationalPaymentSubmission.builder()
                .id(consent.getId())
                .internationalPayment(submissionRequest)
                .build();

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testGetInternationalPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = JMockData.mock(FRInternationalConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRInternationalPaymentSubmission submission = savePaymentSubmission(consent);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateInternationalPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = saveConsent();
        OBWriteInternational2 submissionRequest = new OBWriteInternational2();
        submissionRequest.setData((new OBWriteDataInternational2())
                .consentId(consent.getId())
                .initiation(toOBInternational2(consent.getInitiation())));
        submissionRequest.setRisk(toOBRisk1(consent.getRisk()));

        // When
        HttpResponse<OBWriteInternationalResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalResponse2 consentResponse = response.getBody();
        FRInternationalPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getInternationalPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(toOBWriteInternational2(submission.getInternationalPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1);
    }

    @Test
    public void testDuplicatePaymentInitiationShouldReturnForbidden() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = saveConsent();
        FRInternationalPaymentSubmission submission = savePaymentSubmission(consent);

        OBWriteInternational3 obWriteInternational2 = new OBWriteInternational3();
        obWriteInternational2.risk(toOBRisk1(consent.getRisk()));
        obWriteInternational2.data((new OBWriteInternational3Data())
                .consentId(submission.getId())
                .initiation(toOBWriteInternational3DataInitiation(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obWriteInternational2)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void testMissingConsentOnPaymentInitiationShouldReturnNotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = JMockData.mock(FRInternationalConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().setInstructedAmount(aValidFRAmount());
        consent.getInitiation().setCreditor(aValidFRDataInitiationCreditor());
        consent.getInitiation().setExchangeRateInformation(aValidFRExchangeRateInformation());
        consent.getInitiation().setCurrencyOfTransfer("USD");
        consent.getInitiation().setCreditorAgent(aValidFRDataInitiationCreditorAgent());
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());

        OBWriteInternational3 submissionRequest = new OBWriteInternational3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteInternational3Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteInternational3DataInitiation(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments")
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

    private FRInternationalPaymentSubmission savePaymentSubmission(FRInternationalConsent consent) {
        FRWriteInternational submissionRequest = JMockData.mock(FRWriteInternational.class);
        submissionRequest.getData().getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        FRInternationalPaymentSubmission submission = FRInternationalPaymentSubmission.builder()
                .id(consent.getId())
                .internationalPayment(submissionRequest)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRInternationalConsent saveConsent() {
        FRInternationalConsent consent = JMockData.mock(FRInternationalConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().setInstructedAmount(aValidFRAmount());
        consent.getInitiation().setCreditor(aValidFRDataInitiationCreditor());
        consent.getInitiation().setExchangeRateInformation(aValidFRExchangeRateInformation());
        consent.getInitiation().setCurrencyOfTransfer("USD");
        consent.getInitiation().setCreditorAgent(aValidFRDataInitiationCreditorAgent());
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());
        consent.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);
        consentRepository.save(consent);
        return consent;
    }

}