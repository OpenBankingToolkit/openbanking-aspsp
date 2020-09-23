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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalstandingorderpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.payments.InternationalStandingOrderPaymentSubmission4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.InternationalStandingOrderConsent5Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalPaymentSubmission4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalStandingOrderPaymentSubmission4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRInternationalStandingOrderConsent5;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4DataInitiation;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsentResponse3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderResponse3;
import uk.org.openbanking.datamodel.service.converter.payment.OBInternationalStandingOrderConverter;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRDataInitiationCreditorAgentTestDataFactory.aValidFRDataInitiationCreditorAgent;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRDataInitiationCreditorTestDataFactory.aValidFRDataInitiationCreditor;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toOBInternationalStandingOrder3;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toOBWriteInternationalStandingOrder4DataInitiation;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalstandingorders.InternationalStandingOrdersApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InternationalStandingOrderPaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalStandingOrderConsent5Repository consentRepository;
    @Autowired
    private InternationalStandingOrderPaymentSubmission4Repository submissionRepository;
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
    public void testGetInternationalStandingOrderPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent5 consent = saveConsent();
        FRInternationalStandingOrderPaymentSubmission4 submission = savePaymentSubmission(consent);

        // When
        HttpResponse<OBWriteInternationalStandingOrderConsentResponse3> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/international-standing-orders/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalStandingOrderConsentResponse3.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        OBWriteInternationalStandingOrder4DataInitiation initiation = submission.getInternationalStandingOrder().getData().getInitiation();
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(OBInternationalStandingOrderConverter.toOBInternationalStandingOrder3(initiation));
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetMissingInternationalStandingOrderPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent5 consent = saveConsent();
        OBWriteInternational3 submissionRequest = JMockData.mock(OBWriteInternational3.class);
        FRInternationalPaymentSubmission4 submission = FRInternationalPaymentSubmission4.builder()
                .id(consent.getId())
                .internationalPayment(submissionRequest)
                .build();

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/international-standing-orders/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testGetInternationalStandingOrderPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent5 consent = JMockData.mock(FRInternationalStandingOrderConsent5.class);
        consent.setId(IntentType.PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT.generateIntentId());
        FRInternationalStandingOrderPaymentSubmission4 submission = savePaymentSubmission(consent);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/international-standing-orders/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateInternationalStandingOrderPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent5 consent = saveConsent();
        OBWriteInternationalStandingOrder3 submissionRequest = new OBWriteInternationalStandingOrder3()
                .risk(toOBRisk1(consent.getRisk()))
                .data((new OBWriteDataInternationalStandingOrder3())
                        .consentId(consent.getId())
                        .initiation(toOBInternationalStandingOrder3(consent.getInitiation())));

        // When
        HttpResponse<OBWriteInternationalStandingOrderResponse3> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/international-standing-orders")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalStandingOrderResponse3.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalStandingOrderResponse3 consentResponse = response.getBody();
        FRInternationalStandingOrderPaymentSubmission4 submission = submissionRepository.findById(response.getBody().getData().getInternationalStandingOrderId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
    }

    @Test
    public void testDuplicateStandingOrderPaymentInitiationShouldReturnForbidden() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent5 consent = saveConsent();
        FRInternationalStandingOrderPaymentSubmission4 submission = savePaymentSubmission(consent);

        OBWriteInternationalStandingOrder3 obWriteInternational3 = new OBWriteInternationalStandingOrder3();
        obWriteInternational3.risk(toOBRisk1(consent.getRisk()));
        obWriteInternational3.data((new OBWriteDataInternationalStandingOrder3())
                .consentId(submission.getId())
                .initiation(toOBInternationalStandingOrder3(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/international-standing-orders")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obWriteInternational3)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void testMissingConsentOnStandingOrderPaymentInitiationShouldReturnNotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent5 consent = JMockData.mock(FRInternationalStandingOrderConsent5.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        setupTestConsentInitiation(consent.getInitiation());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());

        OBWriteInternationalStandingOrder3 submissionRequest = new OBWriteInternationalStandingOrder3()
                .risk(toOBRisk1(consent.getRisk()))
                .data((new OBWriteDataInternationalStandingOrder3())
                        .consentId(consent.getId())
                        .initiation(toOBInternationalStandingOrder3(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/international-standing-orders")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(String.class);


        // Then
        System.err.println("Response:" + response.getBody());
        assertThat(response.getStatus()).isEqualTo(400);
    }

    private FRInternationalStandingOrderPaymentSubmission4 savePaymentSubmission(FRInternationalStandingOrderConsent5 consent) {
        OBWriteInternationalStandingOrder4 submissionRequest = JMockData.mock(OBWriteInternationalStandingOrder4.class);
        setupTestConsentInitiation(consent.getInitiation());
        submissionRequest.getData().initiation(toOBWriteInternationalStandingOrder4DataInitiation(consent.getInitiation()));
        FRInternationalStandingOrderPaymentSubmission4 submission = FRInternationalStandingOrderPaymentSubmission4.builder()
                .id(consent.getId())
                .internationalStandingOrder(submissionRequest)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRInternationalStandingOrderConsent5 saveConsent() {
        FRInternationalStandingOrderConsent5 consent = JMockData.mock(FRInternationalStandingOrderConsent5.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT.generateIntentId());
        setupTestConsentInitiation(consent.getInitiation());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());
        consent.setStatus(ConsentStatusCode.CONSUMED);
        consentRepository.save(consent);
        return consent;
    }

    private static void setupTestConsentInitiation(FRWriteInternationalStandingOrderDataInitiation initiation) {
        initiation.setPurpose("t1");
        initiation.setInstructedAmount(FRAmount.builder().currency("GBP").amount("100.00").build());
        initiation.setCurrencyOfTransfer("GBP");
        initiation.setFirstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFinalPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFrequency("EvryDay");
        initiation.setReference("123");
        initiation.setNumberOfPayments("12");
        initiation.setCreditor(aValidFRDataInitiationCreditor());
        initiation.setCreditorAgent(aValidFRDataInitiationCreditorAgent());
        initiation.setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
    }

}