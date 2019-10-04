/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.internationalpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalConsent2Repository;
import com.forgerock.openbanking.commons.auth.Authenticator;
import com.forgerock.openbanking.commons.configuration.applications.RSConfiguration;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRInternationalConsent2;
import com.forgerock.openbanking.commons.model.version.OBVersion;
import com.forgerock.openbanking.commons.services.openbanking.FundsAvailabilityService;
import com.forgerock.openbanking.oidc.services.OpenIdService;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.assertj.jodatime.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.OBSupplementaryData1;
import uk.org.openbanking.datamodel.payment.OBWriteFundsConfirmationResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsentResponse2;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.Authentication.mockAuthentication;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InternationalPaymentConsentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalConsent2Repository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;

    @MockBean
    private OpenIdService openIdService;

    @MockBean
    private FundsAvailabilityService fundsAvailabilityService;

    @MockBean
    private TppRepository tppRepository;

    @MockBean
    private Authenticator authenticator;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetInternationalPaymentConsent() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        FRInternationalConsent2 consent = JMockData.mock(FRInternationalConsent2.class);
        consent.getInitiation().supplementaryData(new OBSupplementaryData1());
        consent.setStatus(ConsentStatusCode.CONSUMED);
        repository.save(consent);

        // When
        HttpResponse<OBWriteInternationalConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payment-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(consent.getInitiation());
        assertThat(response.getBody().getData().getStatus()).isEqualTo(consent.getStatus().toOBExternalConsentStatus1Code());
        Assertions.assertThat(response.getBody().getData().getCreationDateTime()).isEqualToIgnoringMillis(consent.getCreated());
        Assertions.assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualToIgnoringMillis(consent.getStatusUpdate());
    }

    @Test
    public void testGetInternationalPaymentConsentReturnNotFound() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        FRInternationalConsent2 consent = JMockData.mock(FRInternationalConsent2.class);
        consent.setStatus(ConsentStatusCode.CONSUMED);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payment-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateInternationalPaymentConsent() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        PaymentTestHelper.setupMockTpp(tppRepository);
        OBWriteInternationalConsent2 consentRequest = JMockData.mock(OBWriteInternationalConsent2.class);
        consentRequest.getData().getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consentRequest.getData().getInitiation().getCreditor().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consentRequest.getData().getInitiation().getExchangeRateInformation().unitCurrency("GBP");
        consentRequest.getData().getInitiation().currencyOfTransfer("USD");
        consentRequest.getData().getInitiation().getCreditorAgent().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consentRequest.getData().getInitiation().supplementaryData(new OBSupplementaryData1());
        consentRequest.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");

        // When
        HttpResponse<OBWriteInternationalConsentResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payment-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .header("x-ob-client-id", PaymentTestHelper.MOCK_CLIENT_ID)
                .body(consentRequest)
                .asObject(OBWriteInternationalConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalConsentResponse2 consentResponse = response.getBody();
        FRInternationalConsent2 consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getPispName()).isEqualTo(PaymentTestHelper.MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(PaymentTestHelper.MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consent.getInitiation()).isEqualTo(consentResponse.getData().getInitiation());
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code()).isEqualTo(consentResponse.getData().getStatus());
        assertThat(consent.getObVersion()).isEqualTo(OBVersion.v3_1);
    }

    @Test
    public void testCreateInternationalPaymentConsent_noExchangeRate() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        PaymentTestHelper.setupMockTpp(tppRepository);
        OBWriteInternationalConsent2 consentRequest = JMockData.mock(OBWriteInternationalConsent2.class);
        consentRequest.getData().getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consentRequest.getData().getInitiation().getCreditor().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consentRequest.getData().getInitiation().currencyOfTransfer("USD");
        consentRequest.getData().getInitiation().setExchangeRateInformation(null);
        consentRequest.getData().getInitiation().getCreditorAgent().getPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consentRequest.getData().getInitiation().supplementaryData(new OBSupplementaryData1());
        consentRequest.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");

        // When
        HttpResponse<OBWriteInternationalConsentResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payment-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .header("x-ob-client-id", PaymentTestHelper.MOCK_CLIENT_ID)
                .body(consentRequest)
                .asObject(OBWriteInternationalConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalConsentResponse2 consentResponse = response.getBody();
        FRInternationalConsent2 consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getInitiation().getExchangeRateInformation()).isNull();
    }

    @Test
    public void testGetDomesticPaymentConsentFunds() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_PISP");
        FRInternationalConsent2 consent = JMockData.mock(FRInternationalConsent2.class);
        consent.setStatus(ConsentStatusCode.AUTHORISED);
        consent.setIdempotencyKey(UUID.randomUUID().toString());
        repository.save(consent);
        given(fundsAvailabilityService.isFundsAvailable(any(), any())).willReturn(true);

        // When
        HttpResponse<OBWriteFundsConfirmationResponse1> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payment-consents/" + consent.getId()+ "/funds-confirmation")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-url", "https://it-test")
                .asObject(OBWriteFundsConfirmationResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getFundsAvailableResult().isFundsAvailable()).isEqualTo(true);
        assertThat(response.getBody().getData().getFundsAvailableResult().getFundsAvailableDateTime()).isNotNull();
        assertThat(response.getBody().getMeta()).isNotNull();
    }


}