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

package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_4.internationalstandingorders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalStandingOrderConsentRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalStandingOrderConsent;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.repositories.TppRepository;
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
import uk.org.openbanking.datamodel.payment.*;

import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.*;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAccountTestDataFactory.aValidFRAccount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAccountTestDataFactory.aValidFRAccount2;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toFRWriteInternationalStandingOrderDataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toOBWriteInternationalStandingOrder4DataInitiation;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.openbanking.testsupport.payment.OBWriteInternationalStandingOrderConsentTestDataFactory.aValidOBWriteInternationalStandingOrderConsent6;

/**
 * Integration test for {@link InternationalStandingOrderConsentsApiController}.
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InternationalStandingOrderConsentsApiControllerIT {

    private static final String RS_STORE_URL = "https://rs-store:";
    private static final String CONTEXT_PATH = "/open-banking/v3.1.4/pisp/international-standing-order-consents/";

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalStandingOrderConsentRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;
    @Autowired
    private SpringSecForTest springSecForTest;

    @MockBean
    private TppRepository tppRepository;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetInternationalStandingOrderPaymentConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent consent = JMockData.mock(FRInternationalStandingOrderConsent.class);
        consent.setStatus(ConsentStatusCode.CONSUMED);

        // When
        HttpResponse<String> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testGetInternationalStandingOrderPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent consent = saveFRConsent(FRReadRefundAccount.NO, ConsentStatusCode.CONSUMED);

        // When
        HttpResponse<OBWriteInternationalStandingOrderConsentResponse6> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalStandingOrderConsentResponse6.class);

        log.debug("Response {}:{}  {}", response.getStatus(), response.getStatusText(), response.getBody());
        if (response.getParsingError().isPresent()) {
            log.error("Parsing error", response.getParsingError().get());
        }

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(toFRWriteInternationalStandingOrderDataInitiation(response.getBody().getData().getInitiation())).isEqualTo(consent.getInitiation());
        assertThat(response.getBody().getData().getStatus().getValue()).isEqualTo(consent.getStatus().toOBExternalConsentStatus2Code().toString());

    }

    @Test
    public void testGetInternationalStandingOrderPaymentConsent_refundNull() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent consent = saveFRConsent(null, ConsentStatusCode.CONSUMED);

        // When
        HttpResponse<OBWriteInternationalStandingOrderConsentResponse6> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalStandingOrderConsentResponse6.class);

        log.debug("Response {}:{}  {}", response.getStatus(), response.getStatusText(), response.getBody());
        if (response.getParsingError().isPresent()) {
            log.error("Parsing error", response.getParsingError().get());
        }

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getReadRefundAccount()).isNull();
        assertThat(toFRWriteInternationalStandingOrderDataInitiation(response.getBody().getData().getInitiation())).isEqualTo(consent.getInitiation());
        assertThat(response.getBody().getData().getStatus().getValue()).isEqualTo(consent.getStatus().toOBExternalConsentStatus2Code().toString());

    }

    @Test
    public void testCreateInternationalStandingOrderPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        setupMockTpp(tppRepository);

        // When
        HttpResponse<OBWriteInternationalStandingOrderConsentResponse6> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json")
                .header(OBHeaders.ACCEPT, "application/json")
                .header("x-ob-client-id", PaymentTestHelper.MOCK_CLIENT_ID)
                .body(aValidOBWriteInternationalStandingOrderConsent6())
                .asObject(OBWriteInternationalStandingOrderConsentResponse6.class);

        // Then
        log.error("The response: {}", response);
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalStandingOrderConsentResponse6 consentResponse = response.getBody();
        FRInternationalStandingOrderConsent consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getPispName()).isEqualTo(MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(toOBWriteInternationalStandingOrder4DataInitiation(consent.getInitiation())).isEqualTo(consentResponse.getData().getInitiation());
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code().toString()).isEqualTo(consentResponse.getData().getStatus().getValue());
        assertThat(consent.getRisk()).isEqualTo(toFRRisk(consentResponse.getRisk()));
        assertThat(consent.getVersion()).isEqualTo(OBVersion.v3_1_4);
    }

    private FRInternationalStandingOrderConsent saveFRConsent(FRReadRefundAccount frReadRefundAccount, ConsentStatusCode consentStatusCode) {
        FRInternationalStandingOrderConsent consent = JMockData.mock(FRInternationalStandingOrderConsent.class);
        consent.getInternationalStandingOrderConsent().getData().setReadRefundAccount(frReadRefundAccount);
        consent.setId(IntentType.PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT.generateIntentId());
        consent.setIdempotencyKey(UUID.randomUUID().toString());
        consent.setStatus(consentStatusCode);
        setupTestFRConsentInitiation(consent.getInitiation());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());
        consent.setVersion(OBVersion.v3_1_4);
        repository.save(consent);
        return consent;
    }


    private static void setupTestFRConsentInitiation(FRWriteInternationalStandingOrderDataInitiation initiation) {
        initiation.setPurpose("Test");
        initiation.setFrequency("EvryWorkgDay");
        initiation.setReference("123");
        initiation.setFirstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setInstructedAmount(aValidFRAmount());
        initiation.setCurrencyOfTransfer("GBP");
        initiation.setFinalPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setDebtorAccount(aValidFRAccount());
        initiation.setCreditorAccount(aValidFRAccount2());
        initiation.setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
    }
}
