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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_9.vrp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.vrp.DomesticVRPConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.vrp.FRDomesticVrpPaymentSubmissionRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetails;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVrpPaymentSubmission;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.error.OBError1;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;
import uk.org.openbanking.datamodel.vrp.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.MOCK_CLIENT_ID;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.setupMockTpp;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAccountIdentifierTestDataFactory.aValidFRAccountIdentifier;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.toFRDomesticVRPConsentDetails;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConverters.toFRDomesticVRPRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.org.openbanking.testsupport.vrp.OBDomesticVRPConsentRequestTestDataFactory.aValidOBDomesticVRPConsentRequest;
import static uk.org.openbanking.testsupport.vrp.OBDomesticVRPRequestTestDataFactory.aValidOBDomesticVRPRequest;

/**
 * Integration test for {@link DomesticVrpsApiController}
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticVrpsApiControllerIT {
    private static final String RS_STORE_URL = "https://rs-store:";
    private static final String CONTEXT_PATH = "/open-banking/v3.1.9/pisp/domestic-vrps/";
    private static final String DETAILS_CONTEXT_PATH = "/payment-details";

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticVRPConsentRepository consentRepository;
    @Autowired
    private FRDomesticVrpPaymentSubmissionRepository paymentSubmissionRepository;
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
    public void createVrpPaymentSubmission() throws UnirestException {
        // Given
        setupMockTpp(tppRepository);

        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        OBDomesticVRPRequest request = aValidOBDomesticVRPRequest();
        FRDomesticVRPConsent consent = saveFRConsent(
                request.getData().getConsentId(),
                FRReadRefundAccount.YES, ConsentStatusCode.AUTHORISED
        );
        // When
        HttpResponse<OBDomesticVRPResponse> response = postVrpRequest(request, UUID.randomUUID().toString());

        // Then
        log.debug("Response {}:{}  {}", response.getStatus(), response.getStatusText(), response.getBody());
        validateSuccessVRPPaymentResponse(request, response);
    }

    @Test
    public void createMultiplePaymentsForConsent() {
        // Given
        setupMockTpp(tppRepository);

        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        OBDomesticVRPRequest request = aValidOBDomesticVRPRequest();
        saveFRConsent(request.getData().getConsentId(), FRReadRefundAccount.YES, ConsentStatusCode.AUTHORISED);

        // When
        int numPayments = 10;
        final List<OBDomesticVRPResponse> vrpResponses = new ArrayList<>(numPayments);
        for (int i = 0 ; i < numPayments; i++) {
            HttpResponse<OBDomesticVRPResponse> response = postVrpRequest(request, UUID.randomUUID().toString());
            validateSuccessVRPPaymentResponse(request, response);
            vrpResponses.add(response.getBody());
        }

        // Then
        final Set<String> domesticVrpIds = vrpResponses.stream().map(response -> response.getData().getDomesticVRPId()).collect(Collectors.toSet());
        assertThat(domesticVrpIds).size().isEqualTo(numPayments);
    }

    @Test
    public void testSubmittingSameVrpPaymentTwiceIsIdempotent() {
        setupMockTpp(tppRepository);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        OBDomesticVRPRequest request = aValidOBDomesticVRPRequest();
        FRDomesticVRPConsent consent = saveFRConsent(
                request.getData().getConsentId(),
                FRReadRefundAccount.YES, ConsentStatusCode.AUTHORISED
        );

        final String idempotencyKey = UUID.randomUUID().toString();
        final HttpResponse<OBDomesticVRPResponse> initialResponse = postVrpRequest(request, idempotencyKey);
        final HttpResponse<OBDomesticVRPResponse> secondResponse  = postVrpRequest(request, idempotencyKey);

        // Verify both requests yield the same response
        assertThat(secondResponse.getBody()).isEqualTo(initialResponse.getBody());
        assertThat(secondResponse.getStatus()).isEqualTo(initialResponse.getStatus());

        validateSuccessVRPPaymentResponse(request, initialResponse);

        verify(tppRepository, times(2)).findByClientId(eq(MOCK_CLIENT_ID));
    }

    @Test
    public void testSubmittingDifferentPaymentsWithSameIdempotencyKeyFails() {
        setupMockTpp(tppRepository);

        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        OBDomesticVRPRequest request = aValidOBDomesticVRPRequest();
        final String consentId = request.getData().getConsentId();
        FRDomesticVRPConsent consent = saveFRConsent(
                consentId,
                FRReadRefundAccount.YES, ConsentStatusCode.AUTHORISED
        );
        // When
        final String idempotencyKey = UUID.randomUUID().toString();
        HttpResponse<OBDomesticVRPResponse> response = postVrpRequest(request, idempotencyKey);
        validateSuccessVRPPaymentResponse(request, response);

        // Create a new request, using the same consentId
        OBDomesticVRPRequest differentRequest = aValidOBDomesticVRPRequest();
        differentRequest.getData().setConsentId(consentId);
        differentRequest.getData().getInstruction().setInstructedAmount(new OBActiveOrHistoricCurrencyAndAmount().amount("12.34").currency("GBP"));

        // Post with same idempotencyKey
        final HttpResponse<OBErrorResponse1> response2 = postVrpRequest(differentRequest, idempotencyKey, OBErrorResponse1.class);

        assertThat(response2.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response2.getBody().getCode()).isEqualTo("OBRI.Request.Invalid");
        final List<OBError1> errors = response2.getBody().getErrors();
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0).getErrorCode()).isEqualTo("FR.OBRI.idempotency.key.RequestBodyChanged");
    }

    @Test
    public void createVrpPaymentSubmissionConsentNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        OBDomesticVRPRequest request = aValidOBDomesticVRPRequest();

        // When
        HttpResponse<ResponseEntity> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(request)
                .asObject(ResponseEntity.class);

        // Then
        if (response.getParsingError().isPresent()) {
            log.error("The response: {}", response);
            log.error("Parsing error", response.getParsingError().get());
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getParsingError().get().getOriginalBody()).contains("Payment consent behind payment " +
                "submission '" + request.getData().getConsentId() + "' can't be found");
    }

    @Test
    public void getDomesticVrpPayment() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);

        FRDomesticVRPConsent consent = saveFRConsent(
                IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId(),
                FRReadRefundAccount.YES, ConsentStatusCode.AUTHORISED
        );

        FRDomesticVrpPaymentSubmission paymentSubmission = savePaymentSubmission(
                consent.getId()
        );

        // When
        HttpResponse<OBDomesticVRPResponse> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + paymentSubmission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBDomesticVRPResponse.class);

        log.debug("Response {}:{}  {}", response.getStatus(), response.getStatusText(), response.getBody());
        if (response.getParsingError().isPresent()) {
            log.error("Parsing error", response.getParsingError().get());
        }

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(paymentSubmission.getDomesticVrpPayment().getData().getConsentId());
        assertThat(response.getBody().getData().getCreationDateTime()).isEqualTo(new DateTime(paymentSubmission.getCreated()));
    }

    @Test
    public void getDomesticVrpPaymentNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        String domesticVRPId = "DVRP_submission_not_found";

        // When
        HttpResponse<ResponseEntity> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + domesticVRPId)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(ResponseEntity.class);

        // Then
        if (response.getParsingError().isPresent()) {
            log.error("The response: {}", response);
            log.error("Parsing error", response.getParsingError().get());
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getParsingError().get().getOriginalBody()).contains("Domestic VRP payment '" +
                domesticVRPId + "' can't be found");
    }

    @Test
    public void getDomesticVrpPaymentNotFoundRelatedConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        OBDomesticVRPRequest request = aValidOBDomesticVRPRequest();
        FRDomesticVrpPaymentSubmission paymentSubmission = savePaymentSubmission(
                request.getData().getConsentId()
        );

        // When
        HttpResponse<ResponseEntity> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + paymentSubmission.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(ResponseEntity.class);

        // Then
        if (response.getParsingError().isPresent()) {
            log.error("The response: {}", response);
            log.error("Parsing error", response.getParsingError().get());
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getParsingError().get().getOriginalBody()).contains("VRP Payment consent behind payment " +
                "submission '" + paymentSubmission.getId() + "' can't be found");
    }

    @Test
    public void getDomesticVrpPaymentDetails() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);

        FRDomesticVrpPaymentSubmission paymentSubmission = savePaymentSubmission(
                IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId()
        );

        // When
        HttpResponse<OBDomesticVRPDetails> response = Unirest.get(
                        RS_STORE_URL + port + CONTEXT_PATH + paymentSubmission.getId() + DETAILS_CONTEXT_PATH
                )
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBDomesticVRPDetails.class);

        log.debug("Response {}:{}  {}", response.getStatus(), response.getStatusText(), response.getBody());
        if (response.getParsingError().isPresent()) {
            log.error("Parsing error", response.getParsingError().get());
        }

        // Then
        String localInstrument = paymentSubmission.getDomesticVrpPayment().getData().getInstruction().getLocalInstrument();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        OBDomesticVRPDetailsDataPaymentStatus status = response.getBody().getData().getPaymentStatus().get(0);
        assertThat(status).isNotNull();
        assertThat(status.getStatus()).isIn(OBDomesticVRPDetailsDataPaymentStatus.StatusEnum.values());
        assertThat(status.getStatusDetail().getLocalInstrument()).isEqualTo(localInstrument);
    }

    @Test
    public void getDomesticVrpPaymentDetailsNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        String domesticVRPId = "DVRP_submission_not_found";
        // When
        HttpResponse<ResponseEntity> response = Unirest.get(
                        RS_STORE_URL + port + CONTEXT_PATH + domesticVRPId + DETAILS_CONTEXT_PATH
                )
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(ResponseEntity.class);

        // Then
        if (response.getParsingError().isPresent()) {
            log.error("The response: {}", response);
            log.error("Parsing error", response.getParsingError().get());
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getParsingError().get().getOriginalBody()).contains("Domestic VRP payment '" +
                domesticVRPId + "' can't be found to retrieve the details");
    }

    private HttpResponse<OBDomesticVRPResponse> postVrpRequest(OBDomesticVRPRequest request, String idempotencyKey) {
        return postVrpRequest(request, idempotencyKey, OBDomesticVRPResponse.class);
    }

    private <T> HttpResponse<T> postVrpRequest(OBDomesticVRPRequest request, String idempotencyKey, Class<T> responseClass) {
        HttpResponse<T> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, idempotencyKey)
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(request)
                .asObject(responseClass);
        return response;
    }

    private void validateSuccessVRPPaymentResponse(OBDomesticVRPRequest request, HttpResponse<OBDomesticVRPResponse> response) {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        if (response.getParsingError().isPresent()) {
            fail("Parsing error", response.getParsingError().get());
        }
        OBDomesticVRPResponse vrpResponse = response.getBody();
        FRDomesticVrpPaymentSubmission paymentSubmission = paymentSubmissionRepository.findById(vrpResponse.getData().getDomesticVRPId()).get();
        assertThat(paymentSubmission.id).isEqualTo(vrpResponse.getData().getDomesticVRPId());
        assertThat(paymentSubmission.domesticVrpPayment.data.consentId).isEqualTo(request.getData().getConsentId());
        assertThat(vrpResponse.getData().getStatus()).isEqualTo(OBDomesticVRPResponseData.StatusEnum.PENDING);
        assertThat(paymentSubmission.getStatus()).isEqualTo(vrpResponse.getData().getStatus());
    }

    private FRDomesticVRPConsent saveFRConsent(String consentId, FRReadRefundAccount frReadRefundAccount, ConsentStatusCode consentStatusCode) {
        FRDomesticVRPConsentDetails details = toFRDomesticVRPConsentDetails(aValidOBDomesticVRPConsentRequest());
        FRDomesticVRPConsent consent = JMockData.mock(FRDomesticVRPConsent.class);
        consent.setVrpDetails(details);
        consent.getVrpDetails().getData().setReadRefundAccount(frReadRefundAccount);
        consent.getVrpDetails().getData().getInitiation().setDebtorAccount(aValidFRAccountIdentifier());
        consent.setId(consentId);
        consent.setIdempotencyKey(UUID.randomUUID().toString());
        consent.setStatus(consentStatusCode);
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());
        consent.setObVersion(OBVersion.v3_1_9);
        consentRepository.save(consent);
        return consent;
    }

    private FRDomesticVrpPaymentSubmission savePaymentSubmission(String consentId) {
        OBDomesticVRPRequest request = aValidOBDomesticVRPRequest();
        request.getData().setConsentId(consentId);
        FRDomesticVrpPaymentSubmission paymentSubmission = FRDomesticVrpPaymentSubmission.builder()
                .id(consentId)
                .domesticVrpPayment(toFRDomesticVRPRequest(request))
                .status(OBDomesticVRPResponseData.StatusEnum.PENDING)
                .created(new Date())
                .updated(new Date())
                .obVersion(OBVersion.v3_1_9)
                .build();
        paymentSubmissionRepository.save(paymentSubmission);
        return paymentSubmission;
    }
}
