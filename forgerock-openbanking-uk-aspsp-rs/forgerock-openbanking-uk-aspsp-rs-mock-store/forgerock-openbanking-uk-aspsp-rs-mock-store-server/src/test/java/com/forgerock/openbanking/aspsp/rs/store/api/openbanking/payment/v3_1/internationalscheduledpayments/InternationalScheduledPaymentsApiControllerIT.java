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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.internationalscheduledpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalScheduledPaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalScheduledConsentRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalPaymentSubmission;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalScheduledPaymentSubmission;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalScheduledConsent;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConsentConverter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.*;
import uk.org.openbanking.datamodel.service.converter.payment.OBInternationalScheduledConverter;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRDataInitiationCreditorAgentTestDataFactory.aValidFRDataInitiationCreditorAgent;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRDataInitiationCreditorTestDataFactory.aValidFRDataInitiationCreditor;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRFRExchangeRateInformationTestDataFactory.aValidFRExchangeRateInformation;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConsentConverter.toOBWriteInternationalScheduled3DataInitiation;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.openbanking.datamodel.service.converter.payment.OBAccountConverter.toOBCashAccount3;
import static uk.org.openbanking.datamodel.service.converter.payment.OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static uk.org.openbanking.datamodel.service.converter.payment.OBExchangeRateConverter.toOBExchangeRate1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBInternationalIdentifierConverter.toOBBranchAndFinancialInstitutionIdentification3;
import static uk.org.openbanking.datamodel.service.converter.payment.OBInternationalIdentifierConverter.toOBPartyIdentification43;
import static uk.org.openbanking.datamodel.service.converter.payment.OBRemittanceInformationConverter.toOBRemittanceInformation1;

/**
 * Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.internationalscheduledpayments.InternationalScheduledPaymentsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InternationalScheduledPaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalScheduledConsentRepository consentRepository;
    @Autowired
    private InternationalScheduledPaymentSubmissionRepository submissionRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;
    @Autowired
    private SpringSecForTest springSecForTest;


    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetInternationalScheduledPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = saveConsent();
        FRInternationalScheduledPaymentSubmission submission = savePaymentSubmission(consent);

        // When
        HttpResponse<OBWriteInternationalScheduledConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalScheduledConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        OBInternationalScheduled2 initiation = OBInternationalScheduledConverter.toOBInternationalScheduled2(submission.getInternationalScheduledPayment().getData().getInitiation());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(initiation);
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetMissingInternationalScheduledPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = saveConsent();
        OBWriteInternational3 submissionRequest = JMockData.mock(OBWriteInternational3.class);
        FRInternationalPaymentSubmission submission = FRInternationalPaymentSubmission.builder()
                .id(consent.getId())
                .internationalPayment(submissionRequest)
                .build();

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testGetInternationalScheduledPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = JMockData.mock(FRInternationalScheduledConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRInternationalScheduledPaymentSubmission submission = savePaymentSubmission(consent);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateInternationalScheduledPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = saveConsent();
        OBWriteDataInternationalScheduled2 data = (new OBWriteDataInternationalScheduled2())
                .consentId(consent.getId())
                .initiation(FRWriteInternationalScheduledConsentConverter.toOBInternationalScheduled2(consent.getInitiation()));
        OBWriteInternationalScheduled2 submissionRequest = new OBWriteInternationalScheduled2()
                .risk(toOBRisk1(consent.getRisk()))
                .data(data);

        // When
        HttpResponse<OBWriteInternationalScheduledResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalScheduledResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalScheduledResponse2 consentResponse = response.getBody();
        FRInternationalScheduledPaymentSubmission submission = submissionRepository.findById(response.getBody().getData().getInternationalScheduledPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(toOBWriteInternationalScheduled2(submission.getInternationalScheduledPayment())).isEqualTo(submissionRequest);
    }

    @Test
    public void testDuplicateScheduledPaymentInitiationShouldReturnForbidden() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = saveConsent();
        FRInternationalScheduledPaymentSubmission submission = savePaymentSubmission(consent);

        OBWriteInternationalScheduled2 obWriteInternational2 = new OBWriteInternationalScheduled2();
        obWriteInternational2.risk(toOBRisk1(consent.getRisk()));
        obWriteInternational2.data(new OBWriteDataInternationalScheduled2()
                .consentId(submission.getId())
                .initiation(FRWriteInternationalScheduledConsentConverter.toOBInternationalScheduled2(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obWriteInternational2)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void testMissingConsentOnScheduledPaymentInitiationShouldReturnNotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalScheduledConsent consent = JMockData.mock(FRInternationalScheduledConsent.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().setInstructedAmount(aValidFRAmount());
        consent.getInitiation().setCreditorAgent(aValidFRDataInitiationCreditorAgent());
        consent.getInitiation().setCreditor(aValidFRDataInitiationCreditor());
        consent.getInitiation().setExchangeRateInformation(aValidFRExchangeRateInformation());
        consent.getInitiation().setCurrencyOfTransfer("USD");
        consent.getInitiation().setPurpose("to");
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());

        OBWriteInternationalScheduled2 submissionRequest = (new OBWriteInternationalScheduled2())
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteDataInternationalScheduled2()
                        .consentId(consent.getId())
                        .initiation(FRWriteInternationalScheduledConsentConverter.toOBInternationalScheduled2(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-scheduled-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(String.class);


        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    private FRInternationalScheduledPaymentSubmission savePaymentSubmission(FRInternationalScheduledConsent consent) {
        OBWriteInternationalScheduled3 submissionRequest = JMockData.mock(OBWriteInternationalScheduled3.class);
        submissionRequest.getData().initiation(toOBWriteInternationalScheduled3DataInitiation(consent.getInitiation()));
        FRInternationalScheduledPaymentSubmission submission = FRInternationalScheduledPaymentSubmission.builder()
                .id(consent.getId())
                .internationalScheduledPayment(submissionRequest)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRInternationalScheduledConsent saveConsent() {
        FRInternationalScheduledConsent consent = JMockData.mock(FRInternationalScheduledConsent.class);
        consent.setId(IntentType.PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT.generateIntentId());
        consent.getInitiation().setInstructedAmount(aValidFRAmount());
        consent.getInitiation().setCreditor(aValidFRDataInitiationCreditor());
        consent.getInitiation().setExchangeRateInformation(aValidFRExchangeRateInformation());
        consent.getInitiation().setCurrencyOfTransfer("USD");
        consent.getInitiation().setPurpose("to");
        consent.getInitiation().setCreditorAgent(aValidFRDataInitiationCreditorAgent());
        consent.getInitiation().setRequestedExecutionDateTime(DateTime.now().withMillisOfSecond(0));
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());
        consent.setStatus(ConsentStatusCode.CONSUMED);
        consentRepository.save(consent);
        return consent;
    }

    private OBWriteInternationalScheduled2 toOBWriteInternationalScheduled2(OBWriteInternationalScheduled3 obWriteInternationalScheduled3) {
        return obWriteInternationalScheduled3 == null ? null : new OBWriteInternationalScheduled2()
                .data(toOBWriteDataInternationalScheduled2(obWriteInternationalScheduled3.getData()))
                .risk(obWriteInternationalScheduled3.getRisk());
    }

    private OBWriteDataInternationalScheduled2 toOBWriteDataInternationalScheduled2(OBWriteInternationalScheduled3Data data) {
        return data == null ? null : new OBWriteDataInternationalScheduled2()
                .consentId(data.getConsentId())
                .initiation(toOBInternationalScheduled2(data.getInitiation()));
    }

    private OBInternationalScheduled2 toOBInternationalScheduled2(OBWriteInternationalScheduled3DataInitiation initiation) {
        return initiation == null ? null : new OBInternationalScheduled2()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(initiation.getInstructionPriority() == null ? null : OBPriority2Code.valueOf(initiation.getInstructionPriority().name()))
                .purpose(initiation.getPurpose())
                .chargeBearer(initiation.getChargeBearer())
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toOBExchangeRate1(initiation.getExchangeRateInformation()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditor(toOBPartyIdentification43(initiation.getCreditor()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(initiation.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()))
                .supplementaryData(initiation.getSupplementaryData());
    }

}