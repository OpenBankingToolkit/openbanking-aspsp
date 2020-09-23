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
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.payments.InternationalPaymentSubmission4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.InternationalConsent5Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalPaymentSubmission4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRInternationalConsent5;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConsentConverter;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.*;
import uk.org.openbanking.datamodel.service.converter.payment.OBAccountConverter;
import uk.org.openbanking.datamodel.service.converter.payment.OBAmountConverter;
import uk.org.openbanking.datamodel.service.converter.payment.OBExchangeRateConverter;
import uk.org.openbanking.datamodel.service.converter.payment.OBInternationalConverter;
import uk.org.openbanking.datamodel.service.converter.payment.OBRemittanceInformationConverter;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRDataInitiationCreditorAgentTestDataFactory.aValidFRDataInitiationCreditorAgent;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRDataInitiationCreditorTestDataFactory.aValidFRDataInitiationCreditor;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRFRExchangeRateInformationTestDataFactory.aValidFRExchangeRateInformation;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConsentConverter.toOBWriteInternational3DataInitiation;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.openbanking.datamodel.service.converter.payment.OBInternationalIdentifierConverter.toOBBranchAndFinancialInstitutionIdentification3;
import static uk.org.openbanking.datamodel.service.converter.payment.OBInternationalIdentifierConverter.toOBPartyIdentification43;

/**
 * Integration test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.internationalpayments.InternationalPaymentsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InternationalPaymentsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private InternationalConsent5Repository consentRepository;
    @Autowired
    private InternationalPaymentSubmission4Repository submissionRepository;
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
    public void testGetInternationalPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent5 consent = saveConsent();
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        FRInternationalPaymentSubmission4 submission = savePaymentSubmission(consent);

        // When
        HttpResponse<OBWriteInternationalConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBWriteInternationalConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation()).isEqualTo(OBInternationalConverter.toOBInternational2(submission.getInternationalPayment().getData().getInitiation()));
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(consent.getCreated());
        assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualTo(consent.getStatusUpdate());
    }

    @Test
    public void testGetMissingInternationalPaymentSubmissionReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent5 consent = saveConsent();
        OBWriteInternational3 submissionRequest = JMockData.mock(OBWriteInternational3.class);
        FRInternationalPaymentSubmission4 submission = FRInternationalPaymentSubmission4.builder()
                .id(consent.getId())
                .internationalPayment(submissionRequest)
                .build();

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testGetInternationalPaymentSubmissionMissingConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent5 consent = JMockData.mock(FRInternationalConsent5.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        FRInternationalPaymentSubmission4 submission = savePaymentSubmission(consent);

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments/" + submission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateInternationalPaymentSubmission() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent5 consent = saveConsent();
        OBWriteInternational2 submissionRequest = new OBWriteInternational2();
        submissionRequest.setData((new OBWriteDataInternational2())
                .consentId(consent.getId())
                .initiation(FRWriteInternationalConsentConverter.toOBInternational2(consent.getInitiation())));
        submissionRequest.setRisk(toOBRisk1(consent.getRisk()));

        // When
        HttpResponse<OBWriteInternationalResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, "x-idempotency-key")
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(submissionRequest)
                .asObject(OBWriteInternationalResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteInternationalResponse2 consentResponse = response.getBody();
        FRInternationalPaymentSubmission4 submission = submissionRepository.findById(response.getBody().getData().getInternationalPaymentId()).get();
        assertThat(submission.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(toOBWriteInternational2(submission.getInternationalPayment())).isEqualTo(submissionRequest);
        assertThat(submission.getObVersion()).isEqualTo(OBVersion.v3_1);
    }

    @Test
    public void testDuplicatePaymentInitiationShouldReturnForbidden() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent5 consent = saveConsent();
        FRInternationalPaymentSubmission4 submission = savePaymentSubmission(consent);

        OBWriteInternational3 obWriteInternational2 = new OBWriteInternational3();
        obWriteInternational2.risk(toOBRisk1(consent.getRisk()));
        obWriteInternational2.data((new OBWriteInternational3Data())
                .consentId(submission.getId())
                .initiation(toOBWriteInternational3DataInitiation(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments")
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
    public void testMissingConsentOnPaymentInitiationShouldReturnNotFound() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRInternationalConsent5 consent = JMockData.mock(FRInternationalConsent5.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().setInstructedAmount(aValidFRAmount());
        consent.getInitiation().setCreditor(aValidFRDataInitiationCreditor());
        consent.getInitiation().setExchangeRateInformation(aValidFRExchangeRateInformation());
        consent.getInitiation().setCurrencyOfTransfer("USD");
        consent.getInitiation().setCreditorAgent(aValidFRDataInitiationCreditorAgent());
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());

        OBWriteInternational3 submissionRequest = new OBWriteInternational3()
                .risk(toOBRisk1(consent.getRisk()))
                .data(new OBWriteInternational3Data()
                        .consentId(consent.getId())
                        .initiation(toOBWriteInternational3DataInitiation(consent.getInitiation())));

        // When
        HttpResponse<String> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/international-payments")
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

    private FRInternationalPaymentSubmission4 savePaymentSubmission(FRInternationalConsent5 consent) {
        OBWriteInternational3 submissionRequest = JMockData.mock(OBWriteInternational3.class);
        submissionRequest.getData().getInitiation().supplementaryData(new OBSupplementaryData1());
        FRInternationalPaymentSubmission4 submission = FRInternationalPaymentSubmission4.builder()
                .id(consent.getId())
                .internationalPayment(submissionRequest)
                .build();
        submissionRepository.save(submission);
        return submission;
    }

    private FRInternationalConsent5 saveConsent() {
        FRInternationalConsent5 consent = JMockData.mock(FRInternationalConsent5.class);
        consent.setId(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId());
        consent.getInitiation().setInstructedAmount(aValidFRAmount());
        consent.getInitiation().setCreditor(aValidFRDataInitiationCreditor());
        consent.getInitiation().setExchangeRateInformation(aValidFRExchangeRateInformation());
        consent.getInitiation().setCurrencyOfTransfer("USD");
        consent.getInitiation().setCreditorAgent(aValidFRDataInitiationCreditorAgent());
        consent.getInitiation().setSupplementaryData(FRSupplementaryData.builder().data("{}").build());
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());
        consent.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);
        consentRepository.save(consent);
        return consent;
    }

    private OBWriteInternational2 toOBWriteInternational2(OBWriteInternational3 obWriteInternational3) {
        return obWriteInternational3 == null ? null : new OBWriteInternational2()
                .data(toOBWriteDataInternational2(obWriteInternational3.getData()))
                .risk(obWriteInternational3.getRisk());
    }

    private OBWriteDataInternational2 toOBWriteDataInternational2(OBWriteInternational3Data data) {
        return data == null ? null : new OBWriteDataInternational2()
                .consentId(data.getConsentId())
                .initiation(toOBInternational2(data.getInitiation()));
    }

    private OBInternational2 toOBInternational2(OBWriteInternational3DataInitiation initiation) {
        return initiation == null ? null : new OBInternational2()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(initiation.getInstructionPriority() == null ? null : OBPriority2Code.valueOf(initiation.getInstructionPriority().name()))
                .purpose(initiation.getPurpose())
                .chargeBearer(initiation.getChargeBearer())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(OBExchangeRateConverter.toOBExchangeRate1(initiation.getExchangeRateInformation()))
                .debtorAccount(OBAccountConverter.toOBCashAccount3(initiation.getDebtorAccount()))
                .creditor(toOBPartyIdentification43(initiation.getCreditor()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(initiation.getCreditorAgent()))
                .creditorAccount(OBAccountConverter.toOBCashAccount3(initiation.getCreditorAccount()))
                .remittanceInformation(OBRemittanceInformationConverter.toOBRemittanceInformation1(initiation.getRemittanceInformation()))
                .supplementaryData(initiation.getSupplementaryData());
    }

}