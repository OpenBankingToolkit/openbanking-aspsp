/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalstandingorderpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.InternationalStandingOrderConsent3Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.InternationalStandingOrderPaymentSubmission3Repository;
import com.forgerock.openbanking.commons.auth.Authenticator;
import com.forgerock.openbanking.commons.configuration.applications.RSConfiguration;
import com.forgerock.openbanking.commons.model.openbanking.IntentType;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRInternationalPaymentSubmission2;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.payment.FRInternationalStandingOrderConsent3;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.payment.FRInternationalStandingOrderPaymentSubmission3;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.*;

import java.util.Arrays;
import java.util.Collections;

import static com.forgerock.openbanking.integration.test.support.Authentication.mockAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InternationalStandingOrderPaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalStandingOrderConsent3Repository consentRepository;
    @Autowired
    private InternationalStandingOrderPaymentSubmission3Repository submissionRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;

    @MockBean
    private Authenticator authenticator;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetInternationalStandingOrderPaymentSubmission() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        FRInternationalStandingOrderConsent3 consent = saveConsent();
        FRInternationalStandingOrderPaymentSubmission3 submission = savePaymentSubmission(consent);

        // When
        HttpResponse<OBWriteInternationalStandingOrderConsentResponse3> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/international-standing-orders/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalStandingOrderConsentResponse3.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(submission.getInternationalStandingOrder().getData().getInitiation());
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetMissingInternationalStandingOrderPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        FRInternationalStandingOrderConsent3 consent = saveConsent();
        OBWriteInternational2 submissionRequest = JMockData.mock(OBWriteInternational2.class);
        FRInternationalPaymentSubmission2 submission = FRInternationalPaymentSubmission2.builder()
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
        mockAuthentication(authenticator, "ROLE_PISP");
        FRInternationalStandingOrderConsent3 consent = JMockData.mock(FRInternationalStandingOrderConsent3.class);
        consent.setId(IntentType.PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT.generateIntentId());
        FRInternationalStandingOrderPaymentSubmission3 submission = savePaymentSubmission(consent);

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
        mockAuthentication(authenticator, "ROLE_PISP");
        FRInternationalStandingOrderConsent3 consent = saveConsent();
        OBWriteInternationalStandingOrder3 submissionRequest = new OBWriteInternationalStandingOrder3()
                .risk(consent.getRisk())
                .data(new OBWriteDataInternationalStandingOrder3()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation()));

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
        FRInternationalStandingOrderPaymentSubmission3 submission = submissionRepository.findById(response.getBody().getData().getInternationalStandingOrderId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
    }

    @Test
    public void testDuplicateStandingOrderPaymentInitiationShouldReturnForbidden() throws Exception {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        FRInternationalStandingOrderConsent3 consent = saveConsent();
        FRInternationalStandingOrderPaymentSubmission3 submission = savePaymentSubmission(consent);

        OBWriteInternationalStandingOrder3 obWriteInternational3 = new OBWriteInternationalStandingOrder3();
        obWriteInternational3.risk(consent.getRisk());
        obWriteInternational3.data(new OBWriteDataInternationalStandingOrder3()
                .consentId(submission.getId())
                .initiation(consent.getInitiation()));

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
        mockAuthentication(authenticator, "ROLE_PISP");
        FRInternationalStandingOrderConsent3 consent = JMockData.mock(FRInternationalStandingOrderConsent3.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        setupTestConsentInitiation(consent.getInitiation());
        consent.getRisk().merchantCategoryCode("ABCD").getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");

        OBWriteInternationalStandingOrder3 submissionRequest = new OBWriteInternationalStandingOrder3()
                .risk(consent.getRisk())
                .data(new OBWriteDataInternationalStandingOrder3()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation()));

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

    private FRInternationalStandingOrderPaymentSubmission3 savePaymentSubmission(FRInternationalStandingOrderConsent3 consent) {
        OBWriteInternationalStandingOrder3 submissionRequest = JMockData.mock(OBWriteInternationalStandingOrder3.class);
        setupTestConsentInitiation(consent.getInitiation());
        submissionRequest.getData().initiation(consent.getInitiation());
        FRInternationalStandingOrderPaymentSubmission3 submission = FRInternationalStandingOrderPaymentSubmission3.builder()
                .id(consent.getId())
                .internationalStandingOrder(submissionRequest)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRInternationalStandingOrderConsent3 saveConsent() {
        FRInternationalStandingOrderConsent3 consent = JMockData.mock(FRInternationalStandingOrderConsent3.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT.generateIntentId());
        setupTestConsentInitiation(consent.getInitiation());
        consent.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");
        consent.setStatus(ConsentStatusCode.CONSUMED);
        consentRepository.save(consent);
        return consent;
    }

    private static void setupTestConsentInitiation(OBInternationalStandingOrder3 initiation) {
        initiation.purpose("t1");
        initiation.getInstructedAmount().amount("100.00");
        initiation.getInstructedAmount().currency("GBP");
        initiation.currencyOfTransfer("GBP");
        initiation.firstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.finalPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.frequency("EvryDay");
        initiation.reference("123");
        initiation.numberOfPayments("12");
        initiation.getCreditor().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        initiation.getCreditorAgent().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        initiation.supplementaryData(new OBSupplementaryData1());
    }

}