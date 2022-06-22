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

package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_10.internationalscheduledpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_10.internationalscheduledpayments.InternationalScheduledPaymentsApiController;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalScheduledConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalScheduledPaymentSubmissionRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAgent;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialCreditor;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalScheduled;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalScheduledConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalScheduledConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalScheduledData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalScheduledDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRExchangeRateInformation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRInstructionPriority;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRPaymentRisk;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRRemittanceInformation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalScheduledConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalScheduledPaymentSubmission;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalResponse5DataRefund;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled3Data;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledResponse6;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledResponse6Data;

import java.math.BigDecimal;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConsentConverter.toOBWriteInternationalScheduled3DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConverter.toOBWriteInternationalScheduled3;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring Integration test for {@link InternationalScheduledPaymentsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class InternationalScheduledPaymentsApiControllerIT {

    private static final String RS_STORE_URL = "https://rs-store:";
    private static final String CONTEXT_PATH = "/open-banking/v3.1.10/pisp/international-scheduled-payments/";

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalScheduledConsentRepository consentRepository;
    @Autowired
    private InternationalScheduledPaymentSubmissionRepository submissionRepository;
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
    public void testGetMissingInternationalScheduledPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = saveFRConsent(FRReadRefundAccount.NO);
        FRWriteInternationalScheduled submissionRequest = JMockData.mock(FRWriteInternationalScheduled.class);
        FRInternationalScheduledPaymentSubmission submission = FRInternationalScheduledPaymentSubmission.builder()
                .id(consent.getId())
                .internationalScheduledPayment(submissionRequest)
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
    public void testGetInternationalScheduledPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = JMockData.mock(FRInternationalScheduledConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRInternationalScheduledPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<String> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateInternationalScheduledPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = saveFRConsent(FRReadRefundAccount.NO);
        OBWriteInternationalScheduled3 submissionRequest = new OBWriteInternationalScheduled3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteInternationalScheduled3Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteInternationalScheduled3DataInitiation(consent.getInitiation()))
                );

        // When
        HttpResponse<OBWriteInternationalScheduledResponse6> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalScheduledResponse6.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalScheduledResponse6 consentResponse = response.getBody();
        FRInternationalScheduledPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getInternationalScheduledPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consentResponse.getData().getRefund()).isNull();
        assertThat(toOBWriteInternationalScheduled3(submission.getInternationalScheduledPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_10);
    }

    @Test
    public void testCreateInternationalScheduledPaymentSubmission_refundNull() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = saveFRConsent(null);
        OBWriteInternationalScheduled3 submissionRequest = new OBWriteInternationalScheduled3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteInternationalScheduled3Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteInternationalScheduled3DataInitiation(consent.getInitiation()))
                );

        // When
        HttpResponse<OBWriteInternationalScheduledResponse6> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalScheduledResponse6.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalScheduledResponse6 consentResponse = response.getBody();
        FRInternationalScheduledPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getInternationalScheduledPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consentResponse.getData().getRefund()).isNull();
        assertThat(toOBWriteInternationalScheduled3(submission.getInternationalScheduledPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_10);
    }

    @Test
    public void testCreateInternationalScheduledPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = saveFRConsent(FRReadRefundAccount.YES);

        OBWriteInternationalScheduled3 submissionRequest = new OBWriteInternationalScheduled3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteInternationalScheduled3Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteInternationalScheduled3DataInitiation(consent.getInitiation()))
                );


        // When
        HttpResponse<OBWriteInternationalScheduledResponse6> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalScheduledResponse6.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalScheduledResponse6 rBody = response.getBody();
        FRInternationalScheduledPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getInternationalScheduledPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(rBody.getData().getConsentId());
        // Refund
        OBWriteInternationalResponse5DataRefund refund = rBody.getData().getRefund();
        assertThat(refund).isNotNull();
        assertThat(refund.getAccount().getIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getIdentification());
        assertThat(refund.getAccount().getName()).isEqualTo(consent.getInitiation().getDebtorAccount().getName());
        assertThat(refund.getAccount().getSchemeName()).isEqualTo(consent.getInitiation().getDebtorAccount().getSchemeName());
        assertThat(refund.getAccount().getSecondaryIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getSecondaryIdentification());

        assertThat(toOBWriteInternationalScheduled3(submission.getInternationalScheduledPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_10);
    }

    @Test
    public void testGetInternationalScheduledPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = saveFRConsent(FRReadRefundAccount.NO);
        FRInternationalScheduledPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteInternationalScheduledResponse6> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalScheduledResponse6.class);

        // Then
        OBWriteInternationalScheduledResponse6Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteInternationalScheduled3DataInitiation(submission.getInternationalScheduledPayment().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetInternationalScheduledPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = saveFRConsent(FRReadRefundAccount.YES);
        FRInternationalScheduledPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteInternationalScheduledResponse6> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalScheduledResponse6.class);

        // Then
        OBWriteInternationalScheduledResponse6Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNotNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteInternationalScheduled3DataInitiation(submission.getInternationalScheduledPayment().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    private FRInternationalScheduledPaymentSubmission savePaymentSubmission(FRInternationalScheduledConsent consent, String xIdempotencyKey) {
        FRWriteInternationalScheduled submissionRequest = FRWriteInternationalScheduled.builder()
                .risk(consent.getRisk())
                .data(FRWriteInternationalScheduledData.builder()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation())
                        .build())
                .build();
        FRInternationalScheduledPaymentSubmission submission = FRInternationalScheduledPaymentSubmission.builder()
                .id(consent.getId())
                .internationalScheduledPayment(submissionRequest)
                .idempotencyKey(xIdempotencyKey)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRInternationalScheduledConsent saveFRConsent(FRReadRefundAccount frReadRefundAccount) {
        FRInternationalScheduledConsent consent = FRInternationalScheduledConsent.builder()
                .id(IntentType.PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT.generateIntentId())
                .internationalScheduledConsent(
                        FRWriteInternationalScheduledConsent.builder()
                                .data(
                                        FRWriteInternationalScheduledConsentData.builder()
                                                .readRefundAccount(frReadRefundAccount)
                                                .initiation(FRWriteInternationalScheduledDataInitiation.builder().build())
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

    private static void setupTestOBConsentInitiation(FRWriteInternationalScheduledDataInitiation initiation) {
        initiation.setInstructionIdentification("ACME412");
        initiation.setEndToEndIdentification("FRESCO.21302.GFX.20");
        initiation.setInstructionPriority(FRInstructionPriority.NORMAL);
        initiation.setCurrencyOfTransfer("GBP");
        initiation.setInstructedAmount(aValidFRAmount());
        initiation.setRequestedExecutionDateTime(DateTime.now().withMillisOfSecond(0));
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
