/**
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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.internationalscheduledpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalScheduledConsent2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalScheduledPaymentSubmission2Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalPaymentSubmission2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalScheduledConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalScheduledPaymentSubmission2;
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

import static org.assertj.core.api.Assertions.assertThat;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InternationalScheduledPaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalScheduledConsent2Repository consentRepository;
    @Autowired
    private InternationalScheduledPaymentSubmission2Repository submissionRepository;
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
    public void testGetInternationalScheduledPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent2 consent = saveConsent();
        FRInternationalScheduledPaymentSubmission2 submission = savePaymentSubmission(consent);

        // When
        HttpResponse<OBWriteInternationalScheduledConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalScheduledConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(submission.getInternationalScheduledPayment().getData().getInitiation());
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetMissingInternationalScheduledPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent2 consent = saveConsent();
        OBWriteInternational2 submissionRequest = JMockData.mock(OBWriteInternational2.class);
        FRInternationalPaymentSubmission2 submission = FRInternationalPaymentSubmission2.builder()
                .id(consent.getId())
                .internationalPayment(submissionRequest)
                .build();

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments/" + submission.getId())
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
        FRInternationalScheduledConsent2 consent = JMockData.mock(FRInternationalScheduledConsent2.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRInternationalScheduledPaymentSubmission2 submission = savePaymentSubmission(consent);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments/" + submission.getId())
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
        FRInternationalScheduledConsent2 consent = saveConsent();
        OBWriteInternationalScheduled2 submissionRequest = new OBWriteInternationalScheduled2()
                .risk(consent.getRisk())
                .data(new OBWriteDataInternationalScheduled2()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation()));

        // When
        HttpResponse<OBWriteInternationalScheduledResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalScheduledResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalScheduledResponse2 consentResponse = response.getBody();
        FRInternationalScheduledPaymentSubmission2 submission = submissionRepository.findById(response.getBody().getData().getInternationalScheduledPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(submission.getInternationalScheduledPayment()).isEqualTo(submissionRequest);
    }

    @Test
    public void testDuplicateScheduledPaymentInitiationShouldReturnForbidden() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent2 consent = saveConsent();
        FRInternationalScheduledPaymentSubmission2 submission = savePaymentSubmission(consent);

        OBWriteInternationalScheduled2 obWriteInternational2 = new OBWriteInternationalScheduled2();
        obWriteInternational2.risk(consent.getRisk());
        obWriteInternational2.data(new OBWriteDataInternationalScheduled2()
                .consentId(submission.getId())
                .initiation(consent.getInitiation()));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments")
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
    public void testMissingConsentOnScheduledPaymentInitiationShouldReturnNotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent2 consent = JMockData.mock(FRInternationalScheduledConsent2.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consent.getInitiation().getCreditorAgent().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consent.getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consent.getInitiation().getCreditor().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consent.getInitiation().getExchangeRateInformation().unitCurrency("GBP");
        consent.getInitiation().currencyOfTransfer("USD");
        consent.getInitiation().purpose("to");
        consent.getInitiation().supplementaryData(new OBSupplementaryData1());
        consent.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");

        OBWriteInternationalScheduled2 submissionRequest = new OBWriteInternationalScheduled2()
                .risk(consent.getRisk())
                .data(new OBWriteDataInternationalScheduled2()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation()));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments")
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

    private FRInternationalScheduledPaymentSubmission2 savePaymentSubmission(FRInternationalScheduledConsent2 consent) {
        OBWriteInternationalScheduled2 submissionRequest = JMockData.mock(OBWriteInternationalScheduled2.class);
        submissionRequest.getData().initiation(consent.getInitiation());
        FRInternationalScheduledPaymentSubmission2 submission = FRInternationalScheduledPaymentSubmission2.builder()
                .id(consent.getId())
                .internationalScheduledPayment(submissionRequest)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRInternationalScheduledConsent2 saveConsent() {
        FRInternationalScheduledConsent2 consent = JMockData.mock(FRInternationalScheduledConsent2.class);
        consent.setId(IntentType.PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT.generateIntentId());
        consent.getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consent.getInitiation().getCreditor().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consent.getInitiation().getExchangeRateInformation().unitCurrency("GBP");
        consent.getInitiation().currencyOfTransfer("USD");
        consent.getInitiation().purpose("to");
        consent.getInitiation().getCreditorAgent().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
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