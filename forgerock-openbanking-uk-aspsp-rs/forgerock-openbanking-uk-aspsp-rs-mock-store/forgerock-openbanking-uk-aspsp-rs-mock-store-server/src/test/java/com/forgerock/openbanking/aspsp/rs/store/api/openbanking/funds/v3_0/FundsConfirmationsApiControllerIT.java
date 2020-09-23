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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_0;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.funds.FundsConfirmationConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.funds.FundsConfirmationRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.funds.v3_0.FRFundsConfirmation1;
import com.forgerock.openbanking.common.model.openbanking.persistence.funds.v3_0.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.common.services.openbanking.FundsAvailabilityService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmation1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationData1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationResponse1;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FundsConfirmationsApiControllerIT {
    private static final Logger log = LoggerFactory.getLogger(FundsConfirmationsApiControllerIT.class);

    @LocalServerPort
    private int port;

    @Autowired
    private FundsConfirmationConsentRepository fundsConfirmationConsentRepository;
    @Autowired
    private FundsConfirmationRepository fundsConfirmationRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;

    @MockBean
    private FundsAvailabilityService fundsAvailabilityService;
    @Autowired
    private SpringSecForTest springSecForTest;


    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetFundsConfirmation() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFundsConfirmationConsent1 consent = saveConsent();
        FRFundsConfirmation1 fundsConfirmation = saveFundsConfirmation(consent);

        // When
        HttpResponse<OBFundsConfirmationResponse1> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.0/cbpii/funds-confirmations/" + fundsConfirmation.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBFundsConfirmationResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getFundsAvailable()).isTrue();
        assertThat(response.getBody().getData().getCreationDateTime()).isNotNull();
        assertThat(response.getBody().getData().getInstructedAmount().getAmount()).isEqualTo("100.0");
        assertThat(response.getBody().getData().getReference()).isEqualTo("test1");
    }

    @Test
    public void testGetMissing_ReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.0/cbpii/funds-confirmations/12345")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testGetFundsConfirmationWithMissingConsent_ReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFundsConfirmationConsent1 consent = JMockData.mock(FRFundsConfirmationConsent1.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        // Consent not saved in repo
        FRFundsConfirmation1 submission = saveFundsConfirmation(consent);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.0/cbpii/funds-confirmations/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateFundsConfirmation() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFundsConfirmationConsent1 consent = saveConsent();
        OBFundsConfirmation1 request = new OBFundsConfirmation1()
                .data(new OBFundsConfirmationData1()
                        .consentId(consent.getId())
                        .instructedAmount(new OBActiveOrHistoricCurrencyAndAmount().amount("100,0").currency("GBP"))
                        .reference("test1")
                );
        given(fundsAvailabilityService.isFundsAvailable(any(), any())).willReturn(true);

        // When
        HttpResponse<OBFundsConfirmationResponse1> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.0/cbpii/funds-confirmations")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(request)
                .asObject(OBFundsConfirmationResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBFundsConfirmationResponse1 responseBody = response.getBody();
        FRFundsConfirmation1 submission = fundsConfirmationRepository.findById(response.getBody().getData().getFundsConfirmationId()).get();
        assertThat(submission.getId()).isEqualTo(responseBody.getData().getFundsConfirmationId());
        assertThat(submission.getFundsConfirmation().getData().getInstructedAmount()).isEqualTo(request.getData().getInstructedAmount());
        assertThat(submission.getFundsConfirmation().getData().getReference()).isEqualTo(request.getData().getReference());
        assertThat(submission.isFundsAvailable()).isEqualTo(true);
    }

    @Test
    public void testDuplicateCreation_UpdateAndReturn() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRFundsConfirmationConsent1 consent = saveConsent();
        FRFundsConfirmation1 frFundsConfirmation = saveFundsConfirmation(consent);

        OBFundsConfirmation1 request = new OBFundsConfirmation1()
                .data(new OBFundsConfirmationData1()
                        .consentId(consent.getId())
                        .instructedAmount(new OBActiveOrHistoricCurrencyAndAmount().amount("100000.0").currency("GBP"))
                        .reference("test1")
                );
        given(fundsAvailabilityService.isFundsAvailable(any(), any())).willReturn(false);

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.0/cbpii/funds-confirmations")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(request)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
    }

    @Test
    public void testMissingConsentOnPaymentInitiationShouldReturnNotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        OBFundsConfirmation1 request = new OBFundsConfirmation1()
                .data(new OBFundsConfirmationData1()
                        .consentId(IntentType.FUNDS_CONFIRMATION_CONSENT.generateIntentId())
                        .instructedAmount(new OBActiveOrHistoricCurrencyAndAmount().amount("100,0").currency("GBP"))
                        .reference("test1")
                );

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.0/cbpii/funds-confirmations")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(request)
                .asObject(String.class);
        log.debug("Response: {} : {} [{}]",response.getStatus(), response.getStatusText(), response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(404);
    }

    private FRFundsConfirmation1 saveFundsConfirmation(FRFundsConfirmationConsent1 consent) {
        OBFundsConfirmation1 fundsConfirmation = JMockData.mock(OBFundsConfirmation1.class);
        fundsConfirmation.getData()
                .instructedAmount(new OBActiveOrHistoricCurrencyAndAmount().amount("100.0"))
                .consentId(consent.getId())
                .reference("test1");
        FRFundsConfirmation1 frFundsConfirmation = FRFundsConfirmation1.builder()
                .id(consent.getId())
                .fundsConfirmation(fundsConfirmation)
                .created(DateTime.now())
                .build();
        frFundsConfirmation.setFundsAvailable(true);
        fundsConfirmationRepository.save(frFundsConfirmation);
        return frFundsConfirmation;
    }

    private FRFundsConfirmationConsent1 saveConsent() {
        FRFundsConfirmationConsent1 consent = JMockData.mock(FRFundsConfirmationConsent1.class);
        consent.setId(IntentType.FUNDS_CONFIRMATION_CONSENT.generateIntentId());
        consent.setStatusUpdate(DateTime.now());
        consent.setStatus(ConsentStatusCode.AUTHORISED);
        consent.getFundsConfirmationConsent().getData().expirationDateTime(DateTime.parse("2018-11-21T10:43:12.000Z"));
        fundsConfirmationConsentRepository.save(consent);
        return consent;
    }

}