/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v1_1.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.payments.paymentsetup.FRPaymentSetup1Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v1_1.payment.FRPaymentSetup1;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetupResponse1;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.jodatime.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;
    @Autowired
    private FRPaymentSetup1Repository repository;


    @MockBean
    private TppRepository tppRepository;


    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void shouldCreateSinglePaymentConsent() throws UnirestException {
        // Given
        //mockAuthentication(authenticator, "ROLE_PISP");
        setupMockTpp(tppRepository);
        OBPaymentSetup1 consentRequest = JMockData.mock(OBPaymentSetup1.class);
        consentRequest.getData().getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consentRequest.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");

        DateTime beforeCreation = new DateTime();
        // When
        HttpResponse<OBPaymentSetupResponse1> response = Unirest.post("https://rs-store:" + port + "/open-banking/v1.1/payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(consentRequest)
                .asObject(OBPaymentSetupResponse1.class);

        // Then
        log.error("The response: {}", response);
        assertThat(response.getStatus()).isEqualTo(201);
        OBPaymentSetupResponse1 consentResponse = response.getBody();
        FRPaymentSetup1 consent = repository.findById(consentResponse.getData().getPaymentId()).get();
        assertThat(consent.getPispName()).isEqualTo(MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getPaymentId());
        assertThat(consent.getInitiation()).isEqualTo(consentResponse.getData().getInitiation());
        assertThat(consent.getStatus().toOBTransactionIndividualStatus1Code()).isEqualTo(consentResponse.getData().getStatus());

        // Is between start of test and now
        assertThat(consent.getCreated()).isAfter(beforeCreation);
        assertThat(consent.getCreated()).isBefore(new DateTime());
    }

    @Test
    public void shouldGetSinglePaymentConsent() throws UnirestException {
        // Given
        //mockAuthentication(authenticator, "ROLE_PISP");
        FRPaymentSetup1 consent = JMockData.mock(FRPaymentSetup1.class);
        consent.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);
        repository.save(consent);


        // When
        HttpResponse<OBPaymentSetupResponse1> response = Unirest.get("https://rs-store:" + port + "/open-banking/v1.1/payments/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .asObject(OBPaymentSetupResponse1.class);

        // Then
        log.error("The response: {}", response);
        assertThat(response.getStatus()).isEqualTo(200);
        OBPaymentSetupResponse1 consentResponse = response.getBody();
        assertThat(consentResponse.getData().getPaymentId()).isEqualTo(consent.getId());
        assertThat(consentResponse.getData().getInitiation()).isEqualTo(consent.getInitiation());
        assertThat(consentResponse.getData().getStatus()).isEqualTo(consent.getStatus().toOBTransactionIndividualStatus1Code());
        assertThat(consentResponse.getData().getCreationDateTime()).isEqualToIgnoringMillis(consent.getCreated());
    }

    @Test
    public void getSinglePaymentConsent_wrongConsentId_badRequest() throws UnirestException {
        // Given
        //mockAuthentication(authenticator, "ROLE_PISP");

        // When
        HttpResponse<OBPaymentSetupResponse1> response = Unirest.get("https://rs-store:" + port + "/open-banking/v1.1/payments/wrongId")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .asObject(OBPaymentSetupResponse1.class);

        // Then
        log.error("The response: {}", response);
        assertThat(response.getStatus()).isEqualTo(400);
    }
}