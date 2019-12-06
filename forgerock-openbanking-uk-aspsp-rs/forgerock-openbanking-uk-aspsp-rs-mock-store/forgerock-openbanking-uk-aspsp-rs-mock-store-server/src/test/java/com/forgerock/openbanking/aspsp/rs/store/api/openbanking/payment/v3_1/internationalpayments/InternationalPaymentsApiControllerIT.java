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
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalConsent2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalPaymentSubmission2Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalPaymentSubmission2;
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
import uk.org.openbanking.datamodel.payment.*;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InternationalPaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalConsent2Repository consentRepository;
    @Autowired
    private InternationalPaymentSubmission2Repository submissionRepository;
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
        FRInternationalConsent2 consent = saveConsent();
        consent.getInitiation().supplementaryData(new OBSupplementaryData1());
        FRInternationalPaymentSubmission2 submission = savePaymentSubmission(consent);

        // When
        HttpResponse<OBWriteInternationalConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(submission.getInternationalPayment().getData().getInitiation());
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetMissingInternationalPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent2 consent = saveConsent();
        OBWriteInternational2 submissionRequest = JMockData.mock(OBWriteInternational2.class);
        FRInternationalPaymentSubmission2 submission = FRInternationalPaymentSubmission2.builder()
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
        FRInternationalConsent2 consent = JMockData.mock(FRInternationalConsent2.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRInternationalPaymentSubmission2 submission = savePaymentSubmission(consent);

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
        FRInternationalConsent2 consent = saveConsent();
        OBWriteInternational2 submissionRequest = new OBWriteInternational2()
                .risk(consent.getRisk())
                .data(new OBWriteDataInternational2()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation()));

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
        FRInternationalPaymentSubmission2 submission = submissionRepository.findById(response.getBody().getData().getInternationalPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(submission.getInternationalPayment()).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1);
    }

    @Test
    public void testDuplicatePaymentInitiationShouldReturnForbidden() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent2 consent = saveConsent();
        FRInternationalPaymentSubmission2 submission = savePaymentSubmission(consent);

        OBWriteInternational2 obWriteInternational2 = new OBWriteInternational2();
        obWriteInternational2.risk(consent.getRisk());
        obWriteInternational2.data(new OBWriteDataInternational2()
                .consentId(submission.getId())
                .initiation(consent.getInitiation()));

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
        FRInternationalConsent2 consent = JMockData.mock(FRInternationalConsent2.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consent.getRisk().merchantCategoryCode("ABCD").getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");
        consent.getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consent.getInitiation().getCreditor().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consent.getInitiation().getExchangeRateInformation().unitCurrency("GBP");
        consent.getInitiation().currencyOfTransfer("USD");
        consent.getInitiation().getCreditorAgent().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consent.getInitiation().supplementaryData(new OBSupplementaryData1());

        OBWriteInternational2 submissionRequest = new OBWriteInternational2()
                .risk(consent.getRisk())
                .data(new OBWriteDataInternational2()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation()));

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

    private FRInternationalPaymentSubmission2 savePaymentSubmission(FRInternationalConsent2 consent) {
        OBWriteInternational2 submissionRequest = JMockData.mock(OBWriteInternational2.class);
        submissionRequest.getData().getInitiation().supplementaryData(new OBSupplementaryData1());
        FRInternationalPaymentSubmission2 submission = FRInternationalPaymentSubmission2.builder()
                .id(consent.getId())
                .internationalPayment(submissionRequest)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRInternationalConsent2 saveConsent() {
        FRInternationalConsent2 consent = JMockData.mock(FRInternationalConsent2.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consent.getInitiation().getCreditor().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consent.getInitiation().getExchangeRateInformation().unitCurrency("GBP");
        consent.getInitiation().currencyOfTransfer("USD");
        consent.getInitiation().getCreditorAgent().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consent.getInitiation().supplementaryData(new OBSupplementaryData1());
        consent.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");
        consent.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);
        consentRepository.save(consent);
        return consent;
    }

}