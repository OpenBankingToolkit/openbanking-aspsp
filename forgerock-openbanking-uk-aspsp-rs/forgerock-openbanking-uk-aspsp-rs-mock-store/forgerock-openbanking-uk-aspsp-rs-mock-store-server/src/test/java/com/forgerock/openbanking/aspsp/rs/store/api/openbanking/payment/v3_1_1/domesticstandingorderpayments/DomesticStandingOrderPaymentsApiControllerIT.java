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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticstandingorderpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticStandingOrderConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticStandingOrderPaymentSubmissionRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomestic;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrderConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrderDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticPaymentSubmission;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticStandingOrderConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticStandingOrderPaymentSubmission;
import com.forgerock.openbanking.common.model.version.OBVersion;
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
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsentResponse3;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderResponse2;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticStandingOrderConsentConverter.toOBDomesticStandingOrder3;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticstandingorders.DomesticStandingOrdersApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticStandingOrderPaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticStandingOrderConsentRepository consentRepository;
    @Autowired
    private DomesticStandingOrderPaymentSubmissionRepository submissionRepository;
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
    public void testGetDomesticStandingOrderPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = saveConsent();
        FRDomesticStandingOrderPaymentSubmission submission = savePaymentSubmission(consent);

        // When
        HttpResponse<OBWriteDomesticStandingOrderConsentResponse3> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/domestic-standing-orders/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticStandingOrderConsentResponse3.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(toOBDomesticStandingOrder3(submission.getDomesticStandingOrder().getData().getInitiation()));
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetMissingDomesticStandingOrderPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = saveConsent();
        FRWriteDomestic submissionRequest = JMockData.mock(FRWriteDomestic.class);
        FRDomesticPaymentSubmission submission = FRDomesticPaymentSubmission.builder()
                .id(consent.getId())
                .domesticPayment(submissionRequest)
                .build();

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/domestic-standing-orders/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testGetDomesticStandingOrderPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = JMockData.mock(FRDomesticStandingOrderConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRDomesticStandingOrderPaymentSubmission submission = savePaymentSubmission(consent);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/domestic-standing-orders/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateDomesticStandingOrderPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = saveConsent();
        OBWriteDomesticStandingOrder3 submissionRequest = JMockData.mock(OBWriteDomesticStandingOrder3.class)
                .risk(toOBRisk1(consent.getRisk()));
        submissionRequest.getData()
                .consentId(consent.getId())
                .initiation(toOBDomesticStandingOrder3(consent.getInitiation()));

        // When
        HttpResponse<OBWriteDomesticStandingOrderResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/domestic-standing-orders")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteDomesticStandingOrderResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticStandingOrderResponse2 consentResponse = response.getBody();
        FRDomesticStandingOrderPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getDomesticStandingOrderId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_1);
    }

    @Test
    public void testDuplicateStandingOrderPaymentInitiationShouldReturnForbidden() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = saveConsent();
        FRDomesticStandingOrderPaymentSubmission submission = savePaymentSubmission(consent);

        OBWriteDomesticStandingOrder3 obWriteDomestic = JMockData.mock(OBWriteDomesticStandingOrder3.class);
        obWriteDomestic.risk(toOBRisk1(consent.getRisk()));
        obWriteDomestic.data(new OBWriteDataDomesticStandingOrder3()
                .consentId(submission.getId())
                .initiation(toOBDomesticStandingOrder3(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-standing-orders")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obWriteDomestic)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void testMissingConsentOnStandingOrderPaymentInitiationShouldReturnNotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = JMockData.mock(FRDomesticStandingOrderConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        setupTestConsentInitiation(consent.getInitiation());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());

        OBWriteDomesticStandingOrder3 submissionRequest = new OBWriteDomesticStandingOrder3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteDataDomesticStandingOrder3()
                        .consentId(consent.getId())
                        .initiation(toOBDomesticStandingOrder3(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-standing-orders")
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

    private FRDomesticStandingOrderPaymentSubmission savePaymentSubmission(FRDomesticStandingOrderConsent consent) {
        FRWriteDomesticStandingOrder submissionRequest = JMockData.mock(FRWriteDomesticStandingOrder.class);
        setupTestConsentInitiation(consent.getInitiation());
        submissionRequest.getData().setInitiation(consent.getInitiation());
        FRDomesticStandingOrderPaymentSubmission submission = FRDomesticStandingOrderPaymentSubmission.builder()
                .id(consent.getId())
                .domesticStandingOrder(submissionRequest)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private OBWriteDomesticStandingOrder3 getOBWriteDomesticStandingOrder3() {
        return new OBWriteDomesticStandingOrder3()
                .data(new OBWriteDataDomesticStandingOrder3())
                .risk(new OBRisk1());
    }

    private FRDomesticStandingOrderConsent saveConsent() {
        FRDomesticStandingOrderConsent consent = JMockData.mock(FRDomesticStandingOrderConsent.class);
        consent.setDomesticStandingOrderConsent(JMockData.mock(FRWriteDomesticStandingOrderConsent.class));
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        setupTestConsentInitiation(consent.getInitiation());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());
        consent.setStatus(ConsentStatusCode.CONSUMED);
        consentRepository.save(consent);
        return consent;
    }

    private static void setupTestConsentInitiation(FRWriteDomesticStandingOrderDataInitiation initiation) {
        initiation.setRecurringPaymentAmount(FRAmount.builder().currency("GBP").amount("100.0").build());
        initiation.setRecurringPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFirstPaymentAmount(FRAmount.builder().currency("GBP").amount("120.0").build());
        initiation.setFirstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFinalPaymentAmount(FRAmount.builder().currency("GBP").amount("120.0").build());
        initiation.setFinalPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFrequency("EvryDay");
        initiation.setReference("123");
        initiation.setNumberOfPayments("12");
        initiation.setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        initiation.setDebtorAccount(FRAccountIdentifier.builder().identification("123").name("test").schemeName("UK.OBIE.SortCodeAccountNumber").build());
        initiation.setCreditorAccount(FRAccountIdentifier.builder().identification("321").name("test2").schemeName("UK.OBIE.SortCodeAccountNumber").build());
    }

}