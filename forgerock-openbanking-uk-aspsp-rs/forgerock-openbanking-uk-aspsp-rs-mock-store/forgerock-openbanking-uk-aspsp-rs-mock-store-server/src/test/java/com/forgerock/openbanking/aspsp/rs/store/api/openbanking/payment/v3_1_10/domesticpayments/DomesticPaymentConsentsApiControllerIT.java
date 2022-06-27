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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_10.domesticpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticConsentRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRRemittanceInformation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.openbanking.FundsAvailabilityService;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.repositories.TppRepository;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsentResponse5;
import uk.org.openbanking.datamodel.payment.OBWriteFundsConfirmationResponse1;

import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.MOCK_CLIENT_ID;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.MOCK_PISP_ID;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.MOCK_PISP_NAME;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.setupMockTpp;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAccountIdentifierTestDataFactory.aValidFRAccountIdentifier2;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConsentConverter.toFRWriteDomesticDataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticConsentConverter.toOBWriteDomestic2DataInitiation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static uk.org.openbanking.testsupport.payment.OBWriteDomesticConsentTestDataFactory.aValidOBWriteDomesticConsent4;

/**
 * Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_10.domesticpayments.DomesticPaymentConsentsApiController}.
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticPaymentConsentsApiControllerIT {

    private static final String RS_STORE_URL = "https://rs-store:";
    private static final String CONTEXT_PATH = "/open-banking/v3.1.10/pisp/domestic-payment-consents/";

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticConsentRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;
    @Autowired
    private SpringSecForTest springSecForTest;

    @MockBean
    private FundsAvailabilityService fundsAvailabilityService;

    @MockBean
    private TppRepository tppRepository;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testCreateDomesticPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        setupMockTpp(tppRepository);

        // When
        HttpResponse<OBWriteDomesticConsentResponse5> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(aValidOBWriteDomesticConsent4())
                .asObject(OBWriteDomesticConsentResponse5.class);

        // Then
        log.error("The response: {}", response);
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticConsentResponse5 consentResponse = response.getBody();
        FRDomesticConsent consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getPispName()).isEqualTo(MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(toOBWriteDomestic2DataInitiation(consent.getInitiation())).isEqualTo(consentResponse.getData().getInitiation());
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code().toString()).isEqualTo(consentResponse.getData().getStatus().getValue());
        assertThat(consent.getRisk()).isEqualTo(toFRRisk(consentResponse.getRisk()));
        assertThat(consent.getObVersion()).isEqualTo(OBVersion.v3_1_10);
    }

    @Test
    public void testGetDomesticPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent consent = saveFRConsent(FRReadRefundAccount.NO, ConsentStatusCode.CONSUMED);

        // When
        HttpResponse<OBWriteDomesticConsentResponse5> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticConsentResponse5.class);

        log.debug("Response {}:{}  {}", response.getStatus(), response.getStatusText(), response.getBody());
        if (response.getParsingError().isPresent()) {
            log.error("Parsing error", response.getParsingError().get());
        }

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(toFRWriteDomesticDataInitiation(response.getBody().getData().getInitiation())).isEqualTo(consent.getInitiation());
        assertThat(response.getBody().getData().getStatus().getValue()).isEqualTo(consent.getStatus().toOBExternalConsentStatus2Code().toString());
        assertThat(response.getBody().getRisk()).isEqualTo(toOBRisk1(consent.getRisk()));

    }

    @Test
    public void testGetDomesticPaymentConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent consent = JMockData.mock(FRDomesticConsent.class);
        consent.setStatus(ConsentStatusCode.CONSUMED);

        // When
        HttpResponse<String> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400); // seems odd this isn't a 404
    }

    @Test
    public void testGetDomesticPaymentConsent_RefundNull() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent consent = saveFRConsent(null, ConsentStatusCode.CONSUMED);

        // When
        HttpResponse<OBWriteDomesticConsentResponse5> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticConsentResponse5.class);

        log.debug("Response {}:{}  {}", response.getStatus(), response.getStatusText(), response.getBody());
        if (response.getParsingError().isPresent()) {
            log.error("Parsing error", response.getParsingError().get());
        }

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(toFRWriteDomesticDataInitiation(response.getBody().getData().getInitiation())).isEqualTo(consent.getInitiation());
        assertThat(response.getBody().getData().getStatus().getValue()).isEqualTo(consent.getStatus().toOBExternalConsentStatus2Code().toString());

    }

    @Test
    public void testGetDomesticPaymentConsentFunds() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticConsent consent = saveFRConsent(FRReadRefundAccount.NO, ConsentStatusCode.AUTHORISED);

        given(fundsAvailabilityService.isFundsAvailable(any(), any())).willReturn(true);

        // When
        HttpResponse<OBWriteFundsConfirmationResponse1> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + consent.getId() + "/funds-confirmation")
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

    private FRDomesticConsent saveFRConsent(FRReadRefundAccount frReadRefundAccount, ConsentStatusCode consentStatusCode) {
        FRDomesticConsent consent = JMockData.mock(FRDomesticConsent.class);
        consent.getDomesticConsent().getData().setReadRefundAccount(frReadRefundAccount);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.setIdempotencyKey(UUID.randomUUID().toString());
        consent.setStatus(consentStatusCode);
        setupTestFRConsentInitiation(consent.getInitiation());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());
        consent.setObVersion(OBVersion.v3_1_10);
        repository.save(consent);
        return consent;
    }

    private static void setupTestFRConsentInitiation(FRWriteDomesticDataInitiation initiation) {
        initiation.setInstructionIdentification("ACME412");
        initiation.setEndToEndIdentification("FRESCO.21302.GFX.20");
        initiation.setInstructedAmount(aValidFRAmount());
        initiation.setCreditorAccount(aValidFRAccountIdentifier2());
        initiation.setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        initiation.setRemittanceInformation(FRRemittanceInformation.builder().build());
    }
}
