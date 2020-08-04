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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticPaymentSubmission2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.DomesticConsent5Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticPaymentSubmission2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticConsent5;
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
import uk.org.openbanking.datamodel.payment.OBSupplementaryData1;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomestic2;
import uk.org.openbanking.datamodel.payment.OBWriteDomestic2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsentResponse2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticResponse2;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.openbanking.datamodel.service.converter.payment.OBDomesticConverter.toOBDomestic2;

/**
 * Spring Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticpayments.DomesticPaymentsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DomesticPaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticConsent5Repository consentRepository;
    @Autowired
    private DomesticPaymentSubmission2Repository submissionRepository;
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
    public void testGetDomesticPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent5 consent = saveConsent();
        FRDomesticPaymentSubmission2 submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<OBWriteDomesticConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(submission.getDomesticPayment().getData().getInitiation());
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetMissingDomesticPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent5 consent = saveConsent();
        OBWriteDomestic2 submissionRequest = JMockData.mock(OBWriteDomestic2.class);
        FRDomesticPaymentSubmission2 submission = FRDomesticPaymentSubmission2.builder()
                .id(consent.getId())
                .domesticPayment(submissionRequest)
                .build();

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payments/" + submission.getId())
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
        FRDomesticConsent5 consent = JMockData.mock(FRDomesticConsent5.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRDomesticPaymentSubmission2 submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payments/" + submission.getId())
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
        FRDomesticConsent5 consent = saveConsent();
        OBWriteDomestic2 submissionRequest = new OBWriteDomestic2()
                .risk(consent.getRisk())
                .data(new OBWriteDataDomestic2()
                        .consentId(consent.getId())
                        .initiation(toOBDomestic2(consent.getInitiation())));

        // When
        HttpResponse<OBWriteDomesticResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteDomesticResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticResponse2 consentResponse = response.getBody();
        FRDomesticPaymentSubmission2 submission = submissionRepository.findById(response.getBody().getData().getDomesticPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(submission.getDomesticPayment()).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1);
    }

    @Test
    public void testDuplicatePaymentInitiationShouldReturnForbidden() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent5 consent = saveConsent();
        FRDomesticPaymentSubmission2 submission = savePaymentSubmission(consent, UUID.randomUUID().toString());

        OBWriteDomestic2 obWriteDomestic2 = new OBWriteDomestic2();
        obWriteDomestic2.risk(consent.getRisk());
        obWriteDomestic2.data(new OBWriteDataDomestic2()
                .consentId(submission.getId())
                .initiation(toOBDomestic2(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "differentKey")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obWriteDomestic2)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void testDuplicatePaymentInitiation_validIdempotency_ShouldReturnCreated() throws Exception {
        // Given
        final String idempotencyKey = UUID.randomUUID().toString();
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent5 consent = saveConsent();
        FRDomesticPaymentSubmission2 submission = savePaymentSubmission(consent, idempotencyKey);

        OBWriteDomestic2 obWriteDomestic1 = submission.getDomesticPayment();

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.0/pisp/domestic-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, idempotencyKey)
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obWriteDomestic1)
                .asObject(String.class);
        log.debug("Response: {} , {}", response.getStatus(), response.getBody());
        // Then
        assertThat(response.getStatus()).isEqualTo(201);
    }


    @Test
    public void testMissingConsentOnPaymentInitiationShouldReturnNotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent5 consent = JMockData.mock(FRDomesticConsent5.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consent.getInitiation().getCreditorPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consent.getInitiation().supplementaryData(new OBSupplementaryData1());
        consent.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");

        OBWriteDomestic2 submissionRequest = new OBWriteDomestic2()
                .risk(consent.getRisk())
                .data(new OBWriteDataDomestic2()
                        .consentId(consent.getId())
                        .initiation(toOBDomestic2(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payments")
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

    private FRDomesticPaymentSubmission2 savePaymentSubmission(FRDomesticConsent5 consent, String xIdempotencyKey) {
        OBWriteDomestic2 submissionRequest = JMockData.mock(OBWriteDomestic2.class);
        submissionRequest = new OBWriteDomestic2()
                .risk(consent.getRisk())
                .data(new OBWriteDataDomestic2()
                        .consentId(consent.getId())
                        .initiation(toOBDomestic2(consent.getInitiation())));
        FRDomesticPaymentSubmission2 submission = FRDomesticPaymentSubmission2.builder()
                .id(consent.getId())
                .domesticPayment(submissionRequest)
                .idempotencyKey(xIdempotencyKey)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRDomesticConsent5 saveConsent() {
        FRDomesticConsent5 consent = JMockData.mock(FRDomesticConsent5.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consent.getInitiation().getCreditorPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consent.getInitiation().supplementaryData(new OBSupplementaryData1());
        consent.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");
        consent.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);
        consentRepository.save(consent);
        return consent;
    }

}