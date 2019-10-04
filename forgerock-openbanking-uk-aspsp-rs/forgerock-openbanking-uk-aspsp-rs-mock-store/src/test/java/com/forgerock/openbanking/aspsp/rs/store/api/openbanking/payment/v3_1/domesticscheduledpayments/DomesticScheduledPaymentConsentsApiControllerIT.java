/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticscheduledpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticScheduledConsent2Repository;
import com.forgerock.openbanking.commons.auth.Authenticator;
import com.forgerock.openbanking.commons.configuration.applications.RSConfiguration;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRDomesticScheduledConsent2;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.assertj.jodatime.api.Assertions;
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
import uk.org.openbanking.datamodel.payment.OBExternalPermissions2Code;
import uk.org.openbanking.datamodel.payment.OBSupplementaryData1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsentResponse2;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.Authentication.mockAuthentication;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticScheduledPaymentConsentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticScheduledConsent2Repository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;

    @MockBean
    private Authenticator authenticator;

    @MockBean
    private TppRepository tppRepository;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetDomesticScheduledPaymentConsent() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        FRDomesticScheduledConsent2 consent = JMockData.mock(FRDomesticScheduledConsent2.class);
        consent.setStatus(ConsentStatusCode.CONSUMED);
        DateTime requestedExecutionDateTime = DateTime.now().withMillisOfSecond(0);
        consent.getInitiation().requestedExecutionDateTime(DateTime.now().withMillisOfSecond(0));
        consent.getInitiation().supplementaryData(new OBSupplementaryData1());
        repository.save(consent);

        // When
        HttpResponse<OBWriteDomesticScheduledConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-scheduled-payment-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticScheduledConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(consent.getInitiation());
        assertThat(response.getBody().getData().getStatus()).isEqualTo(consent.getStatus().toOBExternalConsentStatus1Code());
        Assertions.assertThat(response.getBody().getData().getCreationDateTime()).isEqualToIgnoringMillis(consent.getCreated());
        Assertions.assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualToIgnoringMillis(consent.getStatusUpdate());
        Assertions.assertThat(response.getBody().getData().getInitiation().getRequestedExecutionDateTime()).isEqualToIgnoringMillis(requestedExecutionDateTime);
    }

    @Test
    public void testGetDomesticScheduledPaymentConsentReturnNotFound() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        FRDomesticScheduledConsent2 consent = JMockData.mock(FRDomesticScheduledConsent2.class);
        consent.setStatus(ConsentStatusCode.CONSUMED);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-scheduled-payment-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateDomesticScheduledPaymentConsent() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        PaymentTestHelper.setupMockTpp(tppRepository);
        OBWriteDomesticScheduledConsent2 consentRequest = JMockData.mock(OBWriteDomesticScheduledConsent2.class);
        consentRequest.getData().getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consentRequest.getData().getInitiation().getCreditorPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consentRequest.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");
        consentRequest.getData().permission(OBExternalPermissions2Code.CREATE);
        consentRequest.getData().getInitiation().supplementaryData(new OBSupplementaryData1());

        // When
        HttpResponse<OBWriteDomesticScheduledConsentResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-scheduled-payment-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .header("x-ob-client-id", PaymentTestHelper.MOCK_CLIENT_ID)
                .body(consentRequest)
                .asObject(OBWriteDomesticScheduledConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticScheduledConsentResponse2 consentResponse = response.getBody();
        FRDomesticScheduledConsent2 consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getPispName()).isEqualTo(PaymentTestHelper.MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(PaymentTestHelper.MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consent.getInitiation()).isEqualTo(consentResponse.getData().getInitiation());
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code()).isEqualTo(consentResponse.getData().getStatus());
        assertThat(consent.getRisk()).isEqualTo(consentResponse.getRisk());
        assertThat(consent.getDomesticScheduledConsent().getData().getPermission()).isEqualTo(consentResponse.getData().getPermission());
    }

}