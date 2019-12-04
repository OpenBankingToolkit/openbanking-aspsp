/**
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
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.DomesticStandingOrderConsent3Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticStandingOrderConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRDomesticStandingOrderConsent3;
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

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;




@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticStandingOrderPaymentConsentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticStandingOrderConsent3Repository repository;
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
        FRDomesticStandingOrderConsent3 consent =  JMockData.mock(FRDomesticStandingOrderConsent3.class);
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
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(consent.getInitiation());
        assertThat(response.getBody().getData().getStatus()).isEqualTo(consent.getStatus().toOBExternalConsentStatus1Code());
    }

    @Test
    public void testGetDomesticStandingOrderPaymentConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticStandingOrderConsent2 consent = JMockData.mock(FRDomesticStandingOrderConsent2.class);
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
        OBWriteDomesticStandingOrderConsent3 consentRequest = JMockData.mock(OBWriteDomesticStandingOrderConsent3.class);
        setupTestConsentInitiation(consentRequest.getData().getInitiation());
        consentRequest.getData().authorisation(new OBAuthorisation1().authorisationType(OBExternalAuthorisation1Code.ANY).completionDateTime(DateTime.now()));
        consentRequest.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");
        consentRequest.getData().permission(OBExternalPermissions2Code.CREATE);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<OBWriteDomesticStandingOrderConsent3>> errors = validator.validate(consentRequest);
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
        FRDomesticStandingOrderConsent3 consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getPispName()).isEqualTo(PaymentTestHelper.MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(PaymentTestHelper.MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consent.getInitiation()).isEqualTo(consentResponse.getData().getInitiation());
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code()).isEqualTo(consentResponse.getData().getStatus());
        assertThat(consent.getRisk()).isEqualTo(consentResponse.getRisk());
        assertThat(consent.getDomesticStandingOrderConsent().getData().getPermission()).isEqualTo(consentResponse.getData().getPermission());
        assertThat(consent.getObVersion()).isEqualTo(OBVersion.v3_1_1);
    }

    @Test
    public void testCreateDomesticStandingOrderPaymentConsent_noOptionalFields() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        PaymentTestHelper.setupMockTpp(tppRepository);
        OBWriteDomesticStandingOrderConsent3 consentRequest = JMockData.mock(OBWriteDomesticStandingOrderConsent3.class);
        setupTestConsentInitiation(consentRequest.getData().getInitiation());
        consentRequest.getData().authorisation(new OBAuthorisation1().authorisationType(OBExternalAuthorisation1Code.ANY).completionDateTime(DateTime.now()));
        consentRequest.getRisk().merchantCategoryCode("ABCD")
                .getDeliveryAddress()
                .countrySubDivision(Arrays.asList("Wessex"))
                .addressLine(Collections.singletonList("3 Queens Square"))
                .country("GP");
        consentRequest.getData().permission(OBExternalPermissions2Code.CREATE);

        // Ensure optional fields are null
        consentRequest.getData().getInitiation().setDebtorAccount(null);
        consentRequest.getData().getInitiation().setFinalPaymentAmount(null);
        consentRequest.getData().getInitiation().setRecurringPaymentAmount(null);
        consentRequest.getData().getInitiation().setFinalPaymentDateTime(null);
        consentRequest.getData().getInitiation().setRecurringPaymentDateTime(null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<OBWriteDomesticStandingOrderConsent3>> errors = validator.validate(consentRequest);
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

    private static void setupTestConsentInitiation(OBDomesticStandingOrder3 initiation ) {
        initiation.setRecurringPaymentAmount(new OBDomesticStandingOrder3RecurringPaymentAmount().amount("100.0").currency("GBP"));
        initiation.recurringPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFirstPaymentAmount(new OBDomesticStandingOrder3FirstPaymentAmount().amount("120.0").currency("GBP"));
        initiation.firstPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.setFinalPaymentAmount(new OBDomesticStandingOrder3FinalPaymentAmount().amount("90.0").currency("GBP"));
        initiation.finalPaymentDateTime(DateTime.now().withMillisOfSecond(0));
        initiation.frequency("EvryDay");
        initiation.reference("123");
        initiation.numberOfPayments("12");
        initiation.supplementaryData(new OBSupplementaryData1());
        initiation.setDebtorAccount(new OBCashAccountDebtor4().identification("123").name("test").schemeName("UK.OBIE.SortCodeAccountNumber"));
        initiation.setCreditorAccount(new OBCashAccountCreditor3().identification("321").name("test2").schemeName("UK.OBIE.SortCodeAccountNumber"));
    }


}