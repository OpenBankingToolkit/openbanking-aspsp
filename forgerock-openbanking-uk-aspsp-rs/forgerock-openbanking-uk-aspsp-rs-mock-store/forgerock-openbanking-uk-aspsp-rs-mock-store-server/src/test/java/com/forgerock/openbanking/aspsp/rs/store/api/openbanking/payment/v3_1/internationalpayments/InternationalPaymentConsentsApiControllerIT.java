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
import com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.InternationalConsent5Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRInternationalConsent5;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.openbanking.FundsAvailabilityService;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
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

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConsentConverter.toOBInternational2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.internationalpayments.InternationalPaymentConsentsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InternationalPaymentConsentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalConsent5Repository repository;
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
    @Autowired
    private SpringSecForTest springSecForTest;


    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetInternationalPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent5 consent = JMockData.mock(FRInternationalConsent5.class);
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
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
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(toOBInternational2(consent.getInitiation()));
        assertThat(response.getBody().getData().getStatus()).isEqualTo(consent.getStatus().toOBExternalConsentStatus1Code());
        Assertions.assertThat(response.getBody().getData().getCreationDateTime()).isEqualToIgnoringMillis(consent.getCreated());
        Assertions.assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualToIgnoringMillis(consent.getStatusUpdate());
    }

    @Test
    public void testGetInternationalPaymentConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent5 consent = JMockData.mock(FRInternationalConsent5.class);
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
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
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
        FRInternationalConsent5 consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getPispName()).isEqualTo(PaymentTestHelper.MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(PaymentTestHelper.MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(toOBInternational2(consent.getInitiation())).isEqualTo(consentResponse.getData().getInitiation());
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code()).isEqualTo(consentResponse.getData().getStatus());
        assertThat(consent.getObVersion()).isEqualTo(OBVersion.v3_1);
    }

    @Test
    public void testCreateInternationalPaymentConsent_noExchangeRate() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
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
        FRInternationalConsent5 consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getInitiation().getExchangeRateInformation()).isNull();
    }

    @Test
    public void testGetDomesticPaymentConsentFunds() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent5 consent = JMockData.mock(FRInternationalConsent5.class);
        consent.setStatus(ConsentStatusCode.AUTHORISED);
        consent.setIdempotencyKey(UUID.randomUUID().toString());
        repository.save(consent);
        given(fundsAvailabilityService.isFundsAvailable(any(), any())).willReturn(true);

        // When
        HttpResponse<OBWriteFundsConfirmationResponse1> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payment-consents/" + consent.getId() + "/funds-confirmation")
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