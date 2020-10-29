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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticConsentRepository;
import com.forgerock.openbanking.repositories.TppRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
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
import lombok.extern.slf4j.Slf4j;
import org.assertj.jodatime.api.Assertions;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.OBSupplementaryData1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsentResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsentResponse2;
import uk.org.openbanking.datamodel.payment.OBWriteFundsConfirmationResponse1;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.MOCK_CLIENT_ID;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.MOCK_PISP_ID;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.MOCK_PISP_NAME;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.setupMockTpp;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConsentConverter.toFRWriteDomesticConsent;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConsentConverter.toOBDomestic2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticConsentConverter.toOBWriteDomesticConsent4;

/**
 * Spring Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticpayments.DomesticPaymentConsentsApiController}.
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticPaymentConsentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticConsentRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;

    @MockBean
    private OpenIdService openIdService;

    @MockBean
    private FundsAvailabilityService fundsAvailabilityService;
    @Autowired
    private SpringSecForTest springSecForTest;

    @MockBean
    private TppRepository tppRepository;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetDomesticPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent consent = JMockData.mock(FRDomesticConsent.class);
        consent.setStatus(ConsentStatusCode.AUTHORISED);
        consent.setIdempotencyKey(UUID.randomUUID().toString());
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        repository.save(consent);

        // When
        HttpResponse<OBWriteDomesticConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payment-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(toOBDomestic2(consent.getInitiation()));
        assertThat(response.getBody().getData().getStatus()).isEqualTo(consent.getStatus().toOBExternalConsentStatus1Code());
        Assertions.assertThat(response.getBody().getData().getCreationDateTime()).isEqualToIgnoringMillis(consent.getCreated());
        Assertions.assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualToIgnoringMillis(consent.getStatusUpdate());
    }

    @Test
    public void testGetDomesticPaymentConsentFunds() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent consent = JMockData.mock(FRDomesticConsent.class);
        consent.setStatus(ConsentStatusCode.AUTHORISED);
        consent.setIdempotencyKey(UUID.randomUUID().toString());
        repository.save(consent);
        given(fundsAvailabilityService.isFundsAvailable(any(), any())).willReturn(true);

        // When
        HttpResponse<OBWriteFundsConfirmationResponse1> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payment-consents/" + consent.getId()+ "/funds-confirmation")
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

    @Test
    public void testGetDomesticPaymentConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent consent = JMockData.mock(FRDomesticConsent.class);
        consent.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payment-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateDomesticPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        setupMockTpp(tppRepository);
        OBWriteDomesticConsent2 consentRequest = JMockData.mock(OBWriteDomesticConsent2.class);
        consentRequest.getData().getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consentRequest.getData().getInitiation().getCreditorPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consentRequest.getData().getInitiation().supplementaryData(new OBSupplementaryData1());
        consentRequest.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");

        // When
        HttpResponse<OBWriteDomesticConsentResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payment-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(consentRequest)
                .asObject(OBWriteDomesticConsentResponse2.class);

        // Then
        log.error("The response: {}", response);
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticConsentResponse2 consentResponse = response.getBody();
        FRDomesticConsent consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getPispName()).isEqualTo(MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(toOBDomestic2(consent.getInitiation())).isEqualTo(consentResponse.getData().getInitiation());
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code()).isEqualTo(consentResponse.getData().getStatus());
        assertThat(consent.getRisk()).isEqualTo(toFRRisk(consentResponse.getRisk()));
        assertThat(consent.getObVersion()).isEqualTo(OBVersion.v3_1);
    }

    @Test
    public void testCreateDomesticPaymentConsent_exists_idempotencyKeyValid_noActionButReturn201() throws UnirestException {
        // Given
        final String idempotencyKey = UUID.randomUUID().toString();
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        setupMockTpp(tppRepository);
        OBWriteDomesticConsent4 consentRequest = JMockData.mock(OBWriteDomesticConsent4.class);
        consentRequest.getData().getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consentRequest.getData().getInitiation().getCreditorPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consentRequest.getData().getInitiation().supplementaryData(null);
        consentRequest.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress().addressLine(Collections.singletonList("3 Queens Square")).country("GP").countrySubDivision(Collections.singletonList("aaa"));
        consentRequest.getData().getAuthorisation().completionDateTime(null);

        FRDomesticConsent existingConsent = JMockData.mock(FRDomesticConsent.class);
        existingConsent.setId(UUID.randomUUID().toString());
        existingConsent.setStatus(ConsentStatusCode.AUTHORISED);
        existingConsent.setDomesticConsent(toFRWriteDomesticConsent(consentRequest));
        existingConsent.setIdempotencyKey(idempotencyKey);
        repository.save(existingConsent);

        // When
        HttpResponse<OBWriteDomesticConsentResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payment-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, idempotencyKey)
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(consentRequest)
                .asObject(OBWriteDomesticConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticConsentResponse2 consentResponse = response.getBody();
        FRDomesticConsent consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(toOBDomestic2(consent.getInitiation())).isEqualTo(consentResponse.getData().getInitiation());
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code()).isEqualTo(consentResponse.getData().getStatus());
    }

    @Test
    @Ignore // This fails intermittently on code fresh - ignoring until more time available to investigate
    public void testCreateDomesticPaymentConsent_exists_idempotencyKeyExpired() throws UnirestException {
        // Given
        final String idempotencyKey = UUID.randomUUID().toString();
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        setupMockTpp(tppRepository);
        OBWriteDomesticConsent1 consentRequest = JMockData.mock(OBWriteDomesticConsent1.class);
        consentRequest.getData().getInitiation().getInstructedAmount().currency("GBP").amount("1.00");
        consentRequest.getData().getInitiation().getCreditorPostalAddress().country("GB").addressLine(Collections.singletonList("3 Queens Square"));
        consentRequest.getRisk().merchantCategoryCode("ABCD").getDeliveryAddress().addressLine(Collections.singletonList("3 Queens Square")).country("GP");
        consentRequest.getData().getAuthorisation().completionDateTime(null);

        OBWriteDomesticConsent4 consent4 = toOBWriteDomesticConsent4(consentRequest);
        FRDomesticConsent existingConsent = JMockData.mock(FRDomesticConsent.class);
        existingConsent.setId(UUID.randomUUID().toString());
        existingConsent.setStatus(ConsentStatusCode.AUTHORISED);
        existingConsent.setDomesticConsent(toFRWriteDomesticConsent(consent4));
        existingConsent.setIdempotencyKey(idempotencyKey);
        existingConsent = repository.save(existingConsent);

        // Created date will be set to now by annotation on initial creation so update it here to 25 hours in past to test idempotency expiry
        existingConsent.setCreated(DateTime.now().minusHours(25));
        repository.save(existingConsent);

        // When
        HttpResponse response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-payment-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, idempotencyKey)
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(consentRequest)
                .asObject(OBWriteDomesticConsentResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }


}