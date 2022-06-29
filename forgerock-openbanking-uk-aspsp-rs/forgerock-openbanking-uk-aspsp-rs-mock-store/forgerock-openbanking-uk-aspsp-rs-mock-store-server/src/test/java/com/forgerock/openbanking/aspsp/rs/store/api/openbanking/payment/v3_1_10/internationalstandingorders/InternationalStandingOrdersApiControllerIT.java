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

package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_10.internationalstandingorders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_10.internationalstandingorders.InternationalStandingOrdersApiController;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalStandingOrderConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalStandingOrderPaymentSubmissionRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAgent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRPaymentRisk;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalStandingOrderConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalStandingOrderPaymentSubmission;
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
import uk.org.openbanking.datamodel.payment.OBSupplementaryData1;
import uk.org.openbanking.datamodel.payment.OBWriteDomestic2DataInitiationInstructedAmount;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3DataInitiationDebtorAccount;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4Data;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4DataInitiation;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4DataInitiationCreditorAccount;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4DataInitiationCreditorAgent;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderResponse7;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderResponse7Data;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderResponse7DataRefund;

import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toOBWriteInternationalStandingOrder4DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toOBWriteInternationalStandingOrderConsentResponse7DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConverter.toFRWriteInternationalStandingOrder;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring Integration test for {@link InternationalStandingOrdersApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class InternationalStandingOrdersApiControllerIT {

    private static final String RS_STORE_URL = "https://rs-store:";
    private static final String CONTEXT_PATH = "/open-banking/v3.1.10/pisp/international-standing-orders/";

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalStandingOrderConsentRepository consentRepository;
    @Autowired
    private InternationalStandingOrderPaymentSubmissionRepository submissionRepository;
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
    public void testGetMissingInternationalStandingOrderPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent consent = saveFRConsent(FRReadRefundAccount.NO);
        FRWriteInternationalStandingOrder submissionRequest = JMockData.mock(FRWriteInternationalStandingOrder.class);
        FRInternationalStandingOrderPaymentSubmission submission = FRInternationalStandingOrderPaymentSubmission.builder()
                .id(consent.getId())
                .internationalStandingOrder(submissionRequest)
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
    public void testGetInternationalStandingOrderPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent consent = JMockData.mock(FRInternationalStandingOrderConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRInternationalStandingOrderPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<String> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
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
        FRInternationalStandingOrderConsent consent = saveFRConsent(FRReadRefundAccount.NO);
        OBWriteInternationalStandingOrder4 submissionRequest = new OBWriteInternationalStandingOrder4()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteInternationalStandingOrder4Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteInternationalStandingOrder4DataInitiation(consent.getInitiation()))
                );

        // When
        HttpResponse<OBWriteInternationalStandingOrderResponse7> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalStandingOrderResponse7.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalStandingOrderResponse7 consentResponse = response.getBody();
        FRInternationalStandingOrderPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getInternationalStandingOrderId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consentResponse.getData().getRefund()).isNull();
        assertThat(submission.getInternationalStandingOrder()).isEqualTo(toFRWriteInternationalStandingOrder(submissionRequest));
        assertThat(submission.getVersion()).isEqualTo(OBVersion.v3_1_10);
    }

    @Test
    public void testCreateInternationalStandingOrderPaymentSubmission_refundNull() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent consent = saveFRConsent(null);
        OBWriteInternationalStandingOrder4 submissionRequest = new OBWriteInternationalStandingOrder4()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteInternationalStandingOrder4Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteInternationalStandingOrder4DataInitiation(consent.getInitiation()))
                );

        // When
        HttpResponse<OBWriteInternationalStandingOrderResponse7> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalStandingOrderResponse7.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalStandingOrderResponse7 consentResponse = response.getBody();
        FRInternationalStandingOrderPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getInternationalStandingOrderId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consentResponse.getData().getRefund()).isNull();
        assertThat(submission.getInternationalStandingOrder()).isEqualTo(toFRWriteInternationalStandingOrder(submissionRequest));
        assertThat(submission.getVersion()).isEqualTo(OBVersion.v3_1_10);
    }

    @Test
    public void testCreateInternationalStandingOrderPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent consent = saveFRConsent(FRReadRefundAccount.YES);

        OBWriteInternationalStandingOrder4 submissionRequest = new OBWriteInternationalStandingOrder4()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteInternationalStandingOrder4Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteInternationalStandingOrder4DataInitiation(consent.getInitiation()))
                );


        // When
        HttpResponse<OBWriteInternationalStandingOrderResponse7> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalStandingOrderResponse7.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalStandingOrderResponse7 rBody = response.getBody();
        FRInternationalStandingOrderPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getInternationalStandingOrderId()).get();
        assertThat(submission.getId()).isEqualTo(rBody.getData().getConsentId());
        // Refund
        OBWriteInternationalStandingOrderResponse7DataRefund refund = rBody.getData().getRefund();
        assertThat(refund).isNotNull();
        assertThat(refund.getAccount().getIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getIdentification());
        assertThat(refund.getAccount().getName()).isEqualTo(consent.getInitiation().getDebtorAccount().getName());
        assertThat(refund.getAccount().getSchemeName()).isEqualTo(consent.getInitiation().getDebtorAccount().getSchemeName());
        assertThat(refund.getAccount().getSecondaryIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getSecondaryIdentification());
        assertThat(submission.getInternationalStandingOrder()).isEqualTo(toFRWriteInternationalStandingOrder(submissionRequest));
        assertThat(submission.getVersion()).isEqualTo(OBVersion.v3_1_10);
    }

    @Test
    public void testGetInternationalStandingOrderPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent consent = saveFRConsent(FRReadRefundAccount.NO);
        FRInternationalStandingOrderPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteInternationalStandingOrderResponse7> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalStandingOrderResponse7.class);

        // Then
        OBWriteInternationalStandingOrderResponse7Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteInternationalStandingOrderConsentResponse7DataInitiation(submission.getInternationalStandingOrder().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetInternationalStandingOrderPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent consent = saveFRConsent(FRReadRefundAccount.YES);
        FRInternationalStandingOrderPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteInternationalStandingOrderResponse7> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalStandingOrderResponse7.class);

        // Then
        OBWriteInternationalStandingOrderResponse7Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNotNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteInternationalStandingOrderConsentResponse7DataInitiation(submission.getInternationalStandingOrder().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    private FRInternationalStandingOrderPaymentSubmission savePaymentSubmission(FRInternationalStandingOrderConsent consent, String xIdempotencyKey) {
        FRWriteInternationalStandingOrder submissionRequest = FRWriteInternationalStandingOrder.builder()
                .risk(consent.getRisk())
                .data(FRWriteInternationalStandingOrderData.builder()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation())
                        .build())
                .build();
        FRInternationalStandingOrderPaymentSubmission submission = FRInternationalStandingOrderPaymentSubmission.builder()
                .id(consent.getId())
                .internationalStandingOrder(submissionRequest)
                .idempotencyKey(xIdempotencyKey)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRInternationalStandingOrderConsent saveFRConsent(FRReadRefundAccount frReadRefundAccount) {
        FRInternationalStandingOrderConsent consent = FRInternationalStandingOrderConsent.builder()
                .id(IntentType.PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT.generateIntentId())
                .internationalStandingOrderConsent(
                        FRWriteInternationalStandingOrderConsent.builder()
                                .data(
                                        FRWriteInternationalStandingOrderConsentData.builder()
                                                .readRefundAccount(frReadRefundAccount)
                                                .initiation(FRWriteInternationalStandingOrderDataInitiation.builder().build())
                                                .build()
                                )
                                .risk(
                                        FRPaymentRisk.builder()
                                                .merchantCategoryCode(aValidFRRisk().getMerchantCategoryCode())
                                                .deliveryAddress(aValidFRRisk().getDeliveryAddress())
                                                .build()
                                )
                                .build()
                ).build();
        consent.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);
        setupTestFRConsentInitiation(consent.getInitiation());
        consentRepository.save(consent);
        return consent;
    }

    private static void setupTestOBConsentInitiation(OBWriteInternationalStandingOrder4DataInitiation initiation) {
        initiation.setCurrencyOfTransfer("GBP");
        initiation.setPurpose("Test");
        initiation.setFrequency("EvryWorkgDay");
        initiation.setReference("123");
        initiation.setFirstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setInstructedAmount(
                new OBWriteDomestic2DataInitiationInstructedAmount()
                        .currency("GBP").amount("165.88")
        );
        initiation.setDebtorAccount(
                new OBWriteDomesticStandingOrder3DataInitiationDebtorAccount()
                        .schemeName("UK.OBIE.SortCodeAccountNumber")
                        .identification("08080021325600")
                        .name("ACME Inc")
                        .secondaryIdentification("0001")
        );
        initiation.setCreditorAccount(
                new OBWriteInternationalStandingOrder4DataInitiationCreditorAccount()
                        .schemeName("UK.OBIE.SortCodeAccountNumber")
                        .identification("08080021325698")
                        .name("ACME Inc")
                        .secondaryIdentification("0002")
        );
        initiation.setCreditorAgent(
                new OBWriteInternationalStandingOrder4DataInitiationCreditorAgent()
                        .schemeName("UK.OBIE.SortCodeAccountNumber")
                        .identification("08080021325603")
                        .name("ACME Inc")
        );
        initiation.supplementaryData(new OBSupplementaryData1());
    }

    private static void setupTestFRConsentInitiation(FRWriteInternationalStandingOrderDataInitiation initiation) {
        initiation.setCurrencyOfTransfer("GBP");
        initiation.setPurpose("Test");
        initiation.setFrequency("EvryWorkgDay");
        initiation.setReference("123");
        initiation.setFirstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setInstructedAmount(aValidFRAmount());
        initiation.setDebtorAccount(FRAccountIdentifier.builder()
                .schemeName("UK.OBIE.SortCodeAccountNumber")
                .identification("08080021325600")
                .name("ACME Inc")
                .secondaryIdentification("0001")
                .build()
        );
        initiation.setCreditorAccount(FRAccountIdentifier.builder()
                .schemeName("UK.OBIE.SortCodeAccountNumber")
                .identification("08080021325698")
                .name("ACME Inc")
                .secondaryIdentification("0002")
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
