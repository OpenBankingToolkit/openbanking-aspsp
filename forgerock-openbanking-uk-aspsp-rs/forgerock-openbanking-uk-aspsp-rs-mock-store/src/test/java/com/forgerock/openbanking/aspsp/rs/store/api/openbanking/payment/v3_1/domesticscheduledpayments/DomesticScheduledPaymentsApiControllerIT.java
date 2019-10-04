/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticscheduledpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticScheduledConsent2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticScheduledPaymentSubmission2Repository;
import com.forgerock.openbanking.commons.auth.Authenticator;
import com.forgerock.openbanking.commons.configuration.applications.RSConfiguration;
import com.forgerock.openbanking.commons.model.openbanking.IntentType;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRDomesticPaymentSubmission2;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRDomesticScheduledConsent2;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRDomesticScheduledPayment;
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
public class DomesticScheduledPaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticScheduledConsent2Repository consentRepository;
    @Autowired
    private DomesticScheduledPaymentSubmission2Repository submissionRepository;
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
    public void testGetDomesticScheduledPaymentSubmission() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        FRDomesticScheduledConsent2 consent = saveConsent();
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
        mockAuthentication(authenticator, "ROLE_PISP");
        FRDomesticScheduledConsent2 consent = saveConsent();
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
        mockAuthentication(authenticator, "ROLE_PISP");
        FRDomesticScheduledConsent2 consent = JMockData.mock(FRDomesticScheduledConsent2.class);
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
        mockAuthentication(authenticator, "ROLE_PISP");
        FRDomesticScheduledConsent2 consent = saveConsent();
        OBWriteDomesticScheduled2 submissionRequest = new OBWriteDomesticScheduled2()
                .risk(consent.getRisk())
                .data(new OBWriteDataDomesticScheduled2()
                        .consentId(consent.getId())
                        .initiation(consent.getInitiation()));

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
        mockAuthentication(authenticator, "ROLE_PISP");
        FRDomesticScheduledConsent2 consent = saveConsent();
        FRDomesticScheduledPayment submission = savePaymentSubmission(consent);

        OBWriteDomesticScheduled2 obWriteDomestic2 = new OBWriteDomesticScheduled2();
        obWriteDomestic2.risk(consent.getRisk());
        obWriteDomestic2.data(new OBWriteDataDomesticScheduled2()
                .consentId(submission.getId())
                .initiation(consent.getInitiation()));

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
        mockAuthentication(authenticator, "ROLE_PISP");
        FRDomesticScheduledConsent2 consent = JMockData.mock(FRDomesticScheduledConsent2.class);
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
                        .initiation(consent.getInitiation()));

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

    private FRDomesticScheduledPayment savePaymentSubmission(FRDomesticScheduledConsent2 consent) {
        OBWriteDomesticScheduled2 submissionRequest = JMockData.mock(OBWriteDomesticScheduled2.class);
        submissionRequest.getData().initiation(consent.getInitiation());
        FRDomesticScheduledPayment submission = FRDomesticScheduledPayment.builder()
                .id(consent.getId())
                .domesticScheduledPayment(submissionRequest)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRDomesticScheduledConsent2 saveConsent() {
        FRDomesticScheduledConsent2 consent = JMockData.mock(FRDomesticScheduledConsent2.class);
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