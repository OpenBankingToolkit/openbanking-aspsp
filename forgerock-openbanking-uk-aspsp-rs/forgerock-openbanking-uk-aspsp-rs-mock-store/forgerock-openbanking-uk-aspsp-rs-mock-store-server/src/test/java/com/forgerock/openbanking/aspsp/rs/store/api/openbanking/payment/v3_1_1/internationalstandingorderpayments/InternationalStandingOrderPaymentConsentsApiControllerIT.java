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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalstandingorderpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalStandingOrderConsentRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalStandingOrderConsent;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.repositories.TppRepository;
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

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRDataInitiationCreditorAgentTestDataFactory.aValidFRDataInitiationCreditorAgent;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRDataInitiationCreditorTestDataFactory.aValidFRDataInitiationCreditor;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toFRWriteInternationalStandingOrderDataInitiation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static uk.org.openbanking.testsupport.payment.OBWriteInternationalStandingOrderConsentTestDataFactory.aValidOBWriteInternationalStandingOrderConsent3;

/**
 * Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalstandingorders.InternationalStandingOrderConsentsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InternationalStandingOrderPaymentConsentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalStandingOrderConsentRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;

    @MockBean
    private TppRepository tppRepository;
    @Autowired
    private SpringSecForTest springSecForTest;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetInternationalStandingOrderPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent consent = JMockData.mock(FRInternationalStandingOrderConsent.class);
        consent.setStatus(ConsentStatusCode.CONSUMED);
        setupTestConsentInitiation(consent.getInitiation());
        repository.save(consent);

        // When
        HttpResponse<OBWriteInternationalStandingOrderConsentResponse3> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/international-standing-order-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalStandingOrderConsentResponse3.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(toFRWriteInternationalStandingOrderDataInitiation(response.getBody().getData().getInitiation())).isEqualTo(consent.getInitiation());
        assertThat(response.getBody().getData().getStatus()).isEqualTo(consent.getStatus().toOBExternalConsentStatus1Code());
    }

    @Test
    public void testGetInternationalStandingOrderPaymentConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalStandingOrderConsent consent = JMockData.mock(FRInternationalStandingOrderConsent.class);
        consent.setStatus(ConsentStatusCode.CONSUMED);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/international-standing-order-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateInternationalStandingOrderPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        PaymentTestHelper.setupMockTpp(tppRepository);
        OBWriteInternationalStandingOrderConsent3 consentRequest = aValidOBWriteInternationalStandingOrderConsent3();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<OBWriteInternationalStandingOrderConsent3>> errors = validator.validate(consentRequest);
        if (!errors.isEmpty()) {
            fail("Invalid request in test: "+errors.toString());
        }

        // When
        HttpResponse<OBWriteInternationalStandingOrderConsentResponse3> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/international-standing-order-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .header("x-ob-client-id", PaymentTestHelper.MOCK_CLIENT_ID)
                .body(consentRequest)
                .asObject(OBWriteInternationalStandingOrderConsentResponse3.class);


        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalStandingOrderConsentResponse3 consentResponse = response.getBody();
        FRInternationalStandingOrderConsent consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consent.getInitiation()).isEqualTo(toFRWriteInternationalStandingOrderDataInitiation(consentResponse.getData().getInitiation()));
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code()).isEqualTo(consentResponse.getData().getStatus());
        assertThat(consent.getPispName()).isEqualTo(PaymentTestHelper.MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(PaymentTestHelper.MOCK_PISP_ID);
    }

    private static void setupTestConsentInitiation(OBWriteInternationalStandingOrder4DataInitiation initiation) {
        initiation.purpose("test");
        initiation.setInstructedAmount(new OBWriteDomestic2DataInitiationInstructedAmount().amount("100.0").currency("GBP"));
        initiation.currencyOfTransfer("GBP");
        initiation.firstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.finalPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.frequency("EvryDay");
        initiation.reference("123");
        initiation.numberOfPayments("12");
        initiation.setCreditor(new OBWriteInternationalScheduledConsentResponse6DataInitiationCreditor().name("user").postalAddress(new OBPostalAddress6().country("GB").addressLine(Collections.singletonList("3 Queens Square"))));
        initiation.setCreditorAgent(new OBWriteInternationalStandingOrder4DataInitiationCreditorAgent().identification("123").name("test").schemeName("UK.OBIE.SortCodeAccountNumber"));
        initiation.setExtendedPurpose(null);
        initiation.setDestinationCountryCode("GB");
        initiation.setSupplementaryData(new OBSupplementaryData1());
    }

    private static void setupTestConsentInitiation(FRWriteInternationalStandingOrderDataInitiation initiation) {
        initiation.setPurpose("test");
        initiation.setInstructedAmount(aValidFRAmount());
        initiation.setCurrencyOfTransfer("GBP");
        initiation.setFirstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFinalPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFrequency("EvryDay");
        initiation.setReference("123");
        initiation.setNumberOfPayments("12");
        initiation.setCreditor(aValidFRDataInitiationCreditor());
        initiation.setCreditorAgent(aValidFRDataInitiationCreditorAgent());
        initiation.setExtendedPurpose(null);
        initiation.setDestinationCountryCode("GB");
        initiation.setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
    }

}