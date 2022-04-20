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

package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_9.internationalpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalPaymentSubmissionRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAgent;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialCreditor;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.*;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.*;
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

import java.math.BigDecimal;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConsentConverter.toOBWriteInternational3DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConverter.toOBWriteInternational3;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring Integration test for {@link InternationalPaymentsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class InternationalPaymentsApiControllerIT {

    private static final String RS_STORE_URL = "https://rs-store:";
    private static final String CONTEXT_PATH = "/open-banking/v3.1.9/pisp/international-payments/";

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
    public void testGetMissingInternationalPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = saveFRConsent(FRReadRefundAccount.NO);
        FRWriteInternational submissionRequest = JMockData.mock(FRWriteInternational.class);
        FRInternationalPaymentSubmission submission = FRInternationalPaymentSubmission.builder()
                .id(consent.getId())
                .internationalPayment(submissionRequest)
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
    public void testGetInternationalPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = JMockData.mock(FRInternationalConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRInternationalPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<String> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
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
        FRInternationalConsent consent = saveFRConsent(FRReadRefundAccount.NO);
        OBWriteInternational3 submissionRequest = new OBWriteInternational3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteInternational3Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteInternational3DataInitiation(consent.getInitiation()))
                );

        // When
        HttpResponse<OBWriteInternationalResponse5> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalResponse5.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalResponse5 consentResponse = response.getBody();
        FRInternationalPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getInternationalPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consentResponse.getData().getRefund()).isNull();
        assertThat(toOBWriteInternational3(submission.getInternationalPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_9);
    }

    @Test
    public void testCreateInternationalPaymentSubmission_refundNull() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = saveFRConsent(null);
        OBWriteInternational3 submissionRequest = new OBWriteInternational3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteInternational3Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteInternational3DataInitiation(consent.getInitiation()))
                );

        // When
        HttpResponse<OBWriteInternationalResponse5> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalResponse5.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalResponse5 consentResponse = response.getBody();
        FRInternationalPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getInternationalPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consentResponse.getData().getRefund()).isNull();
        assertThat(toOBWriteInternational3(submission.getInternationalPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_9);
    }

    @Test
    public void testCreateInternationalPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = saveFRConsent(FRReadRefundAccount.YES);

        OBWriteInternational3 submissionRequest = new OBWriteInternational3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteInternational3Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteInternational3DataInitiation(consent.getInitiation()))
                );


        // When
        HttpResponse<OBWriteInternationalResponse5> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalResponse5.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalResponse5 rBody = response.getBody();
        FRInternationalPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getInternationalPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(rBody.getData().getConsentId());
        // Refund
        OBWriteInternationalResponse5DataRefund refund = rBody.getData().getRefund();
        assertThat(refund).isNotNull();
        assertThat(refund.getAccount().getIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getIdentification());
        assertThat(refund.getAccount().getName()).isEqualTo(consent.getInitiation().getDebtorAccount().getName());
        assertThat(refund.getAccount().getSchemeName()).isEqualTo(consent.getInitiation().getDebtorAccount().getSchemeName());
        assertThat(refund.getAccount().getSecondaryIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getSecondaryIdentification());

        assertThat(toOBWriteInternational3(submission.getInternationalPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_9);
    }

    @Test
    public void testGetInternationalPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = saveFRConsent(FRReadRefundAccount.NO);
        FRInternationalPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteInternationalResponse5> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalResponse5.class);

        // Then
        OBWriteInternationalResponse5Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteInternational3DataInitiation(submission.getInternationalPayment().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetInternationalPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent consent = saveFRConsent(FRReadRefundAccount.YES);
        FRInternationalPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteInternationalResponse5> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalResponse5.class);

        // Then
        OBWriteInternationalResponse5Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNotNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteInternational3DataInitiation(submission.getInternationalPayment().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    private FRInternationalPaymentSubmission savePaymentSubmission(FRInternationalConsent consent, String xIdempotencyKey) {
        FRWriteInternational submissionRequest = FRWriteInternational.builder()
                .risk(consent.getRisk())
                .data(FRWriteInternationalData.builder()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation())
                        .build())
                .build();
        FRInternationalPaymentSubmission submission = FRInternationalPaymentSubmission.builder()
                .id(consent.getId())
                .internationalPayment(submissionRequest)
                .idempotencyKey(xIdempotencyKey)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRInternationalConsent saveFRConsent(FRReadRefundAccount frReadRefundAccount) {
        FRInternationalConsent consent = FRInternationalConsent.builder()
                .id(IntentType.PAYMENT_INTERNATIONAL_CONSENT.generateIntentId())
                .internationalConsent(
                        FRWriteInternationalConsent.builder()
                                .data(
                                        FRWriteInternationalConsentData.builder()
                                                .readRefundAccount(frReadRefundAccount)
                                                .initiation(FRWriteInternationalDataInitiation.builder().build())
                                                .build()
                                )
                                .risk(
                                        FRPaymentRisk.builder()
                                                .merchantCategoryCode(aValidFRRisk().getMerchantCategoryCode())
                                                .deliveryAddress(aValidFRRisk().getDeliveryAddress())
                                                .build()
                                )
                                .build()
                )
                .build();
        consent.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);
        setupTestOBConsentInitiation(consent.getInitiation());
        consentRepository.save(consent);
        return consent;
    }

    private static void setupTestOBConsentInitiation(FRWriteInternationalDataInitiation initiation) {
        initiation.setInstructionIdentification("ACME412");
        initiation.setEndToEndIdentification("FRESCO.21302.GFX.20");
        initiation.setInstructionPriority(FRInstructionPriority.NORMAL);
        initiation.setCurrencyOfTransfer("GBP");
        initiation.setInstructedAmount(aValidFRAmount());
        initiation.setCreditor(FRFinancialCreditor.builder().build());
        initiation.setDebtorAccount(FRAccountIdentifier.builder()
                .schemeName("UK.OBIE.SortCodeAccountNumber")
                .identification("08080021325600")
                .name("ACME Inc")
                .secondaryIdentification("0001")
                .build()
        );
        initiation.setCreditorAccount(
                FRAccountIdentifier.builder()
                        .schemeName("UK.OBIE.SortCodeAccountNumber")
                        .identification("08080021325600")
                        .name("ACME Inc")
                        .secondaryIdentification("0001")
                        .build()
        );
        initiation.setRemittanceInformation(FRRemittanceInformation.builder()
                .reference("FRESCO-101")
                .unstructured("Internal test code 101010")
                .build()
        );
        initiation.setExchangeRateInformation(FRExchangeRateInformation.builder()
                .exchangeRate(new BigDecimal("2.00"))
                .unitCurrency("GBP")
                .contractIdentification("contract-identification")
                .rateType(FRExchangeRateInformation.FRRateType.ACTUAL)
                .build()
        );
        initiation.setCreditorAgent(FRFinancialAgent.builder()
                .schemeName("UK.OBIE.SortCodeAccountNumber")
                .identification("08080021325603")
                .name("ACME Inc")
                .build()
        );
        initiation.setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
    }
}
