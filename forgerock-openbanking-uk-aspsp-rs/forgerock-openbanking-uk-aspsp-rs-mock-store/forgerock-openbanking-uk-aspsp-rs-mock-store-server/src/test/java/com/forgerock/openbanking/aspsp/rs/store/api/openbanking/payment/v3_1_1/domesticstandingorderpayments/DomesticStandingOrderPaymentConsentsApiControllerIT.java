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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticstandingorderpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticStandingOrderConsentRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrderDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRPermission;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticStandingOrderConsent;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
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
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent4DataAuthorisation.AuthorisationTypeEnum;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsent5Data.PermissionEnum;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAccountTestDataFactory.aValidFRAccount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAccountTestDataFactory.aValidFRAccount2;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticStandingOrderConsentConverter.toFRWriteDomesticStandingOrderDataInitiation;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticstandingorders.DomesticStandingOrderConsentsApiController}.
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticStandingOrderPaymentConsentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticStandingOrderConsentRepository repository;
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
    public void testGetDomesticStandingOrderPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = JMockData.mock(FRDomesticStandingOrderConsent.class);
        consent.setStatus(ConsentStatusCode.CONSUMED);
        setupTestConsentInitiation(consent.getInitiation());
        repository.save(consent);

        // When
        HttpResponse<OBWriteDomesticStandingOrderConsentResponse3> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/domestic-standing-order-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteDomesticStandingOrderConsentResponse3.class);
        log.debug("Response {}:{}  {}", response.getStatus(), response.getStatusText(), response.getBody());
        if (response.getParsingError().isPresent()) {
            log.error("Parsing error", response.getParsingError().get());
        }

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(toFRWriteDomesticStandingOrderDataInitiation(response.getBody().getData().getInitiation())).isEqualTo(consent.getInitiation());
        assertThat(response.getBody().getData().getStatus()).isEqualTo(consent.getStatus().toOBExternalConsentStatus1Code());
    }

    @Test
    public void testGetDomesticStandingOrderPaymentConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent consent = JMockData.mock(FRDomesticStandingOrderConsent.class);
        consent.setStatus(ConsentStatusCode.CONSUMED);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/domestic-standing-order-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateDomesticStandingOrderPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        PaymentTestHelper.setupMockTpp(tppRepository);
        OBWriteDomesticStandingOrderConsent5 consentRequest = JMockData.mock(OBWriteDomesticStandingOrderConsent5.class);
        setupTestConsentInitiation(consentRequest.getData().getInitiation());
        consentRequest.getData().authorisation(new OBWriteDomesticConsent4DataAuthorisation().authorisationType(AuthorisationTypeEnum.ANY).completionDateTime(DateTime.now()));
        consentRequest.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");
        consentRequest.getData().permission(PermissionEnum.CREATE);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<OBWriteDomesticStandingOrderConsent5>> errors = validator.validate(consentRequest);
        assertThat(errors.isEmpty()).isTrue();

        // When
        log.debug("Request {}", consentRequest);
        HttpResponse<OBWriteDomesticStandingOrderConsentResponse3> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1.1/pisp/domestic-standing-order-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "675765765675765hfytrfy5r")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .header("x-ob-client-id", PaymentTestHelper.MOCK_CLIENT_ID)
                .body(consentRequest)
                .asObject(OBWriteDomesticStandingOrderConsentResponse3.class);
        log.debug("Response ({}:{}), {}", response.getStatus(), response.getStatusText(), response.getBody());
        if (response.getParsingError().isPresent()) {
            log.error("Parsing error", response.getParsingError().get());
        }

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteDomesticStandingOrderConsentResponse3 consentResponse = response.getBody();
        FRDomesticStandingOrderConsent consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getPispName()).isEqualTo(PaymentTestHelper.MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(PaymentTestHelper.MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consent.getInitiation()).isEqualTo(toFRWriteDomesticStandingOrderDataInitiation(consentResponse.getData().getInitiation()));
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code()).isEqualTo(consentResponse.getData().getStatus());
        assertThat(consent.getRisk()).isEqualTo(toFRRisk(consentResponse.getRisk()));
        assertThat(consent.getDomesticStandingOrderConsent().getData().getPermission()).isEqualTo(FRPermission.valueOf(consentResponse.getData().getPermission().name()));
        assertThat(consent.getObVersion()).isEqualTo(OBVersion.v3_1_1);
    }

    @Test
    public void testCreateDomesticStandingOrderPaymentConsent_noOptionalFields() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        PaymentTestHelper.setupMockTpp(tppRepository);
        OBWriteDomesticStandingOrderConsent5 consentRequest = JMockData.mock(OBWriteDomesticStandingOrderConsent5.class);
        setupTestConsentInitiation(consentRequest.getData().getInitiation());
        consentRequest.getData().authorisation(new OBWriteDomesticConsent4DataAuthorisation().authorisationType(AuthorisationTypeEnum.ANY).completionDateTime(DateTime.now()));
        consentRequest.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");
        consentRequest.getData().permission(PermissionEnum.CREATE);

        // Ensure optional fields are null
        consentRequest.getData().getInitiation().setDebtorAccount(null);
        consentRequest.getData().getInitiation().setFinalPaymentAmount(null);
        consentRequest.getData().getInitiation().setRecurringPaymentAmount(null);
        consentRequest.getData().getInitiation().setFinalPaymentDateTime(null);
        consentRequest.getData().getInitiation().setRecurringPaymentDateTime(null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<OBWriteDomesticStandingOrderConsent5>> errors = validator.validate(consentRequest);
        assertThat(errors.isEmpty()).isTrue();

        // When
        log.debug("Request {}", consentRequest);
        HttpResponse<OBWriteDomesticStandingOrderConsentResponse3> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/domestic-standing-order-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "675765765675765hfytrfy5r")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .header("x-ob-client-id", PaymentTestHelper.MOCK_CLIENT_ID)
                .body(consentRequest)
                .asObject(OBWriteDomesticStandingOrderConsentResponse3.class);
        log.debug("Response ({}:{}), {}", response.getStatus(), response.getStatusText(), response.getBody());
        if (response.getParsingError().isPresent()) {
            log.error("Parsing error", response.getParsingError().get());
        }

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
    }

    private static void setupTestConsentInitiation(OBWriteDomesticStandingOrder3DataInitiation initiation) {
        initiation.setRecurringPaymentAmount(new OBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount().amount("100.0").currency("GBP"));
        initiation.recurringPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFirstPaymentAmount(new OBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount().amount("120.0").currency("GBP"));
        initiation.firstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFinalPaymentAmount(new OBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount().amount("90.0").currency("GBP"));
        initiation.finalPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.frequency("EvryDay");
        initiation.reference("123");
        initiation.numberOfPayments("12");
        initiation.supplementaryData(new OBSupplementaryData1());
        initiation.setDebtorAccount(new OBWriteDomesticStandingOrder3DataInitiationDebtorAccount().identification("123").name("test").schemeName("UK.OBIE.SortCodeAccountNumber"));
        initiation.setCreditorAccount(new OBWriteDomesticStandingOrder3DataInitiationCreditorAccount().identification("321").name("test2").schemeName("UK.OBIE.SortCodeAccountNumber"));
    }

    private static void setupTestConsentInitiation(FRWriteDomesticStandingOrderDataInitiation initiation) {
        initiation.setRecurringPaymentAmount(aValidFRAmount());
        initiation.setRecurringPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFirstPaymentAmount(aValidFRAmount());
        initiation.setFirstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFinalPaymentAmount(aValidFRAmount());
        initiation.setFinalPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFrequency("EvryDay");
        initiation.setReference("123");
        initiation.setNumberOfPayments("12");
        initiation.setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        initiation.setDebtorAccount(aValidFRAccount());
        initiation.setCreditorAccount(aValidFRAccount2());
    }

}