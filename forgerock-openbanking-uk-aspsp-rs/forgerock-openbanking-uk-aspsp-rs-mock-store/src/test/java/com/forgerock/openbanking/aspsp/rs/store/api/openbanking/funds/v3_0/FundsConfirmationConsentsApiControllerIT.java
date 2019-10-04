/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_0;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.funds.FundsConfirmationConsentRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_0.funds.FRFundsConfirmationConsent1;
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
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsent1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsentResponse1;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FundsConfirmationConsentsApiControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private FundsConfirmationConsentRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;

    @MockBean
    private TppRepository tppRepository;



    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetConsent() throws UnirestException {
        // Given
        //mockAuthentication(authenticator, "ROLE_PISP");
        FRFundsConfirmationConsent1 consent = JMockData.mock(FRFundsConfirmationConsent1.class);
        consent.setStatus(ConsentStatusCode.AUTHORISED);
        repository.save(consent);

        // When
        HttpResponse<OBFundsConfirmationConsentResponse1> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.0/cbpii/funds-confirmation-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBFundsConfirmationConsentResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getStatus().name()).isEqualTo(consent.getStatus().toOBExternalConsentStatus1Code().name());
        Assertions.assertThat(response.getBody().getData().getCreationDateTime()).isEqualToIgnoringMillis(consent.getCreated());
        Assertions.assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualToIgnoringMillis(consent.getStatusUpdate());
    }

    @Test
    public void testGetConsentReturnNotFound() throws UnirestException {
        // Given
        //mockAuthentication(authenticator, "ROLE_PISP");

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.0/pisp/international-scheduled-payment-consents/12345")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateConsent() throws UnirestException {
        // Given
        //mockAuthentication(authenticator, "ROLE_PISP");
        setupMockTpp(tppRepository);
        OBFundsConfirmationConsent1 consentRequest = JMockData.mock(OBFundsConfirmationConsent1.class);
        consentRequest.getData().getDebtorAccount().identification("123").name("tester");
        consentRequest.getData().expirationDateTime(DateTime.parse("2018-11-01"));

        // When
        HttpResponse<OBFundsConfirmationConsentResponse1> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.0/cbpii/funds-confirmation-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .body(consentRequest)
                .asObject(OBFundsConfirmationConsentResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBFundsConfirmationConsentResponse1 consentResponse = response.getBody();
        FRFundsConfirmationConsent1 consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getPispName()).isEqualTo(MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code().name()).isEqualTo(consentResponse.getData().getStatus().name());
    }
}