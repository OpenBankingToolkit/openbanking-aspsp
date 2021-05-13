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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_6.domesticstandingorders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_5.domesticstandingorders.DomesticStandingOrdersApiController;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticStandingOrderConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticStandingOrderPaymentSubmissionRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDataDomesticStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrderDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
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
import uk.org.openbanking.datamodel.payment.*;

import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAccountIdentifierTestDataFactory.aValidFRAccountIdentifier;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAccountIdentifierTestDataFactory.aValidFRAccountIdentifier2;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticStandingOrderConsentConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticStandingOrderConverter.toOBWriteDomesticDomesticStandingOrder3;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring Integration test for {@link DomesticStandingOrdersApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DomesticStandingOrdersApiControllerIT {

    private static final String RS_STORE_URL = "https://rs-store:";
    private static final String CONTEXT_PATH = "/open-banking/v3.1.6/pisp/domestic-standing-orders/";

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
    public void testGetMissingDomesticStandingOrderPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = saveConsent(FRReadRefundAccount.NO);
        FRWriteDomesticStandingOrder submissionRequest = JMockData.mock(FRWriteDomesticStandingOrder.class);
        FRDomesticStandingOrderPaymentSubmission submission = FRDomesticStandingOrderPaymentSubmission.builder()
                .id(consent.getId())
                .domesticStandingOrder(submissionRequest)
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
    public void testGetDomesticStandingOrderPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = JMockData.mock(FRDomesticStandingOrderConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRDomesticStandingOrderPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<String> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
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
        FRDomesticStandingOrderConsent consent = saveConsent(FRReadRefundAccount.NO);
        OBWriteDomesticStandingOrder3 submissionRequest = new OBWriteDomesticStandingOrder3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteDomesticStandingOrder3Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteDomesticStandingOrder3DataInitiation(consent.getInitiation())));

        // When
        HttpResponse<OBWriteDomesticStandingOrderResponse6> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteDomesticStandingOrderResponse6.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticStandingOrderResponse6 consentResponse = response.getBody();
        FRDomesticStandingOrderPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getDomesticStandingOrderId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consentResponse.getData().getRefund()).isNull();
        assertThat(toOBWriteDomesticDomesticStandingOrder3(submission.getDomesticStandingOrder())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_6);
    }

    @Test
    public void testCreateDomesticStandingOrderPaymentSubmission_refundNull() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = saveConsent(null);
        OBWriteDomesticStandingOrder3 submissionRequest = new OBWriteDomesticStandingOrder3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteDomesticStandingOrder3Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteDomesticStandingOrder3DataInitiation(consent.getInitiation())));

        // When
        HttpResponse<OBWriteDomesticStandingOrderResponse6> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteDomesticStandingOrderResponse6.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticStandingOrderResponse6 consentResponse = response.getBody();
        FRDomesticStandingOrderPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getDomesticStandingOrderId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consentResponse.getData().getRefund()).isNull();
        assertThat(toOBWriteDomesticDomesticStandingOrder3(submission.getDomesticStandingOrder())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_6);
    }

    @Test
    public void testCreateDomesticStandingOrderPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = saveConsent(FRReadRefundAccount.YES);
        OBWriteDomesticStandingOrder3 submissionRequest = new OBWriteDomesticStandingOrder3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteDomesticStandingOrder3Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteDomesticStandingOrder3DataInitiation(consent.getInitiation())));

        // When
        HttpResponse<OBWriteDomesticStandingOrderResponse6> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteDomesticStandingOrderResponse6.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticStandingOrderResponse6 rBody = response.getBody();
        FRDomesticStandingOrderPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getDomesticStandingOrderId()).get();
        assertThat(submission.getId()).isEqualTo(rBody.getData().getConsentId());
        // Refund
        OBWriteDomesticResponse5DataRefund refund = rBody.getData().getRefund();
        assertThat(refund).isNotNull();
        assertThat(refund.getAccount().getIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getIdentification());
        assertThat(refund.getAccount().getName()).isEqualTo(consent.getInitiation().getDebtorAccount().getName());
        assertThat(refund.getAccount().getSchemeName()).isEqualTo(consent.getInitiation().getDebtorAccount().getSchemeName());
        assertThat(refund.getAccount().getSecondaryIdentification()).isEqualTo(consent.getInitiation().getDebtorAccount().getSecondaryIdentification());

        assertThat(toOBWriteDomesticDomesticStandingOrder3(submission.getDomesticStandingOrder())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1_6);
    }

    @Test
    public void testGetDomesticStandingOrderPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = saveConsent(FRReadRefundAccount.NO);
        FRDomesticStandingOrderPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteDomesticStandingOrderResponse6> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticStandingOrderResponse6.class);

        // Then
        OBWriteDomesticStandingOrderResponse6Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteDomesticStandingOrderConsentResponse6DataInitiation(submission.getDomesticStandingOrder().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetDomesticStandingOrderPaymentSubmission_expectRefundAccount() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = saveConsent(FRReadRefundAccount.YES);
        FRDomesticStandingOrderPaymentSubmission submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteDomesticStandingOrderResponse6> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticStandingOrderResponse6.class);

        // Then
        OBWriteDomesticStandingOrderResponse6Data bodyData = response.getBody().getData();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(bodyData.getRefund()).isNotNull();
        assertThat(bodyData.getConsentId()).isEqualTo(consent.getId());
        assertThat(bodyData.getInitiation()).isEqualTo(toOBWriteDomesticStandingOrderConsentResponse6DataInitiation(submission.getDomesticStandingOrder().getData().getInitiation()));
        assertThat(bodyData.getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(bodyData.getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    private FRDomesticStandingOrderPaymentSubmission savePaymentSubmission(FRDomesticStandingOrderConsent consent, String xIdempotencyKey) {
        FRWriteDomesticStandingOrder submissionRequest = FRWriteDomesticStandingOrder.builder()
                .risk(consent.getRisk())
                .data(FRWriteDataDomesticStandingOrder.builder()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation())
                        .build())
                .build();
        FRDomesticStandingOrderPaymentSubmission submission = FRDomesticStandingOrderPaymentSubmission.builder()
                .id(consent.getId())
                .domesticStandingOrder(submissionRequest)
                .idempotencyKey(xIdempotencyKey)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRDomesticStandingOrderConsent saveConsent(FRReadRefundAccount frReadRefundAccount) {
        FRDomesticStandingOrderConsent consent = JMockData.mock(FRDomesticStandingOrderConsent.class);
        setupTestFRConsentInitiation(consent.getInitiation());
        consent.getDomesticStandingOrderConsent().getData().setReadRefundAccount(frReadRefundAccount);
        consent.setId(IntentType.PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT.generateIntentId());
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());
        consent.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);
        consentRepository.save(consent);
        return consent;
    }

    private static void setupTestFRConsentInitiation(FRWriteDomesticStandingOrderDataInitiation initiation) {
        initiation.setFirstPaymentAmount(aValidFRAmount());
        initiation.setFirstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFrequency("EvryDay");
        initiation.setReference("123");
        initiation.setNumberOfPayments("12");
        initiation.setRecurringPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setRecurringPaymentAmount(aValidFRAmount());
        initiation.setFinalPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFinalPaymentAmount(aValidFRAmount());
        initiation.setDebtorAccount(aValidFRAccountIdentifier());
        initiation.setCreditorAccount(aValidFRAccountIdentifier2());
        initiation.setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
    }
}
