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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticscheduledpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticScheduledPaymentSubmission2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.payments.DomesticScheduledConsent4Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticPaymentSubmission2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticScheduledPayment;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRDomesticScheduledConsent4;
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
import uk.org.openbanking.datamodel.payment.*;

import java.util.Arrays;
import java.util.Collections;

import static uk.org.openbanking.datamodel.service.converter.payment.OBDomesticScheduledConverter.toOBDomesticScheduled2;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticscheduledpayments.DomesticScheduledPaymentsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticScheduledPaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticScheduledConsent4Repository consentRepository;
    @Autowired
    private DomesticScheduledPaymentSubmission2Repository submissionRepository;
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
    public void testGetDomesticScheduledPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent4 consent = saveConsent();
        FRDomesticScheduledPayment submission = savePaymentSubmission(consent);

        // When
        HttpResponse<OBWriteDomesticScheduledConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-scheduled-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticScheduledConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(submission.getDomesticScheduledPayment().getData().getInitiation());
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetMissingDomesticScheduledPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent4 consent = saveConsent();
        OBWriteDomestic2 submissionRequest = JMockData.mock(OBWriteDomestic2.class);
        FRDomesticPaymentSubmission2 submission = FRDomesticPaymentSubmission2.builder()
                .id(consent.getId())
                .domesticPayment(submissionRequest)
                .build();

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-scheduled-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testGetDomesticScheduledPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent4 consent = JMockData.mock(FRDomesticScheduledConsent4.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRDomesticScheduledPayment submission = savePaymentSubmission(consent);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-scheduled-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateDomesticScheduledPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent4 consent = saveConsent();
        OBWriteDomesticScheduled2 submissionRequest = new OBWriteDomesticScheduled2()
                .risk(consent.getRisk())
                .data(new OBWriteDataDomesticScheduled2()
                        .consentId(consent.getId())
                        .initiation(toOBDomesticScheduled2(consent.getInitiation())));

        // When
        HttpResponse<OBWriteDomesticScheduledResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-scheduled-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteDomesticScheduledResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticScheduledResponse2 consentResponse = response.getBody();
        FRDomesticScheduledPayment submission = submissionRepository.findById(response.getBody().getData().getDomesticScheduledPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(submission.getDomesticScheduledPayment()).isEqualTo(submissionRequest);
    }

    @Test
    public void testDuplicateScheduledPaymentInitiationShouldReturnForbidden() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent4 consent = saveConsent();
        FRDomesticScheduledPayment submission = savePaymentSubmission(consent);

        OBWriteDomesticScheduled2 obWriteDomestic2 = new OBWriteDomesticScheduled2();
        obWriteDomestic2.risk(consent.getRisk());
        obWriteDomestic2.data(new OBWriteDataDomesticScheduled2()
                .consentId(submission.getId())
                .initiation(toOBDomesticScheduled2(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-scheduled-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obWriteDomestic2)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void testMissingConsentOnScheduledPaymentInitiationShouldReturnNotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticScheduledConsent4 consent = JMockData.mock(FRDomesticScheduledConsent4.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consent.getInitiation().getCreditorPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consent.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");
        consent.getInitiation().supplementaryData(new OBSupplementaryData1());

        OBWriteDomesticScheduled2 submissionRequest = new OBWriteDomesticScheduled2()
                .risk(consent.getRisk())
                .data(new OBWriteDataDomesticScheduled2()
                        .consentId(consent.getId())
                        .initiation(toOBDomesticScheduled2(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-scheduled-payments")
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

    private FRDomesticScheduledPayment savePaymentSubmission(FRDomesticScheduledConsent4 consent) {
        OBWriteDomesticScheduled2 submissionRequest = JMockData.mock(OBWriteDomesticScheduled2.class);
        submissionRequest.getData().initiation(toOBDomesticScheduled2(consent.getInitiation()));
        FRDomesticScheduledPayment submission = FRDomesticScheduledPayment.builder()
                .id(consent.getId())
                .domesticScheduledPayment(submissionRequest)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRDomesticScheduledConsent4 saveConsent() {
        FRDomesticScheduledConsent4 consent = JMockData.mock(FRDomesticScheduledConsent4.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consent.getInitiation().getCreditorPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consent.getInitiation().requestedExecutionDateTime(DateTime.now().withMillisOfSecond(0));
        consent.getInitiation().supplementaryData(new OBSupplementaryData1());
        consent.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");
        consent.setStatus(ConsentStatusCode.CONSUMED);
        consentRepository.save(consent);
        return consent;
    }

}