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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_10.vrp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetails;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.services.store.vrp.DomesticVrpPaymentConsentService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.ErrorCode;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.error.VRPErrorControlParametersFields;
import com.github.jsonzou.jmockdata.JMockData;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
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
import uk.org.openbanking.datamodel.account.Links;
import uk.org.openbanking.datamodel.error.OBError1;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.vrp.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentResponse;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentResponseData;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPInitiation;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPResponse;
import uk.org.openbanking.testsupport.vrp.OBDomesticVRPRequestTestDataFactory3_1_10;
import uk.org.openbanking.testsupport.vrp.OBDomesticVRPResponseTestDataFactory3_1_10;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.toFRDomesticVRPConsentDetails;
import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static uk.org.openbanking.testsupport.payment.OBRisk1TestDataFactory.aValidOBRisk1;
import static uk.org.openbanking.testsupport.vrp.OBDomesticVRPConsentRequestTestDataFactory3_1_10.aValidOBDomesticVRPConsentRequest;
import static uk.org.openbanking.testsupport.vrp.OBDomesticVRPRequestTestDataFactory3_1_10.aValidOBDomesticVRPRequest;


/**
 * Integration test for {@link DomesticVrpsApiController}
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticVrpsApiControllerIT {

    private static final String HOST = "https://rs-api:";
    private static final String VRP_CONTXT_PATH = "/open-banking/v3.1.10/pisp/domestic-vrps/";
    private static final String IDEMPOTENCY_KEY = UUID.randomUUID().toString();

    @LocalServerPort
    private int port;

    @Autowired
    private RSConfiguration rsConfiguration;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean(name = "cryptoApiClient") // Required to avoid Spring auto-wiring exception
    private CryptoApiClient cryptoApiClient;
    @MockBean(name = "amResourceServerService") // Required to avoid Spring auto-wiring exception
    private AMResourceServerService amResourceServerService;
    @MockBean
    private RsStoreGateway rsStoreGateway;
    @MockBean
    private TppStoreService tppStoreService;
    @Autowired
    private SpringSecForTest springSecForTest;
    @MockBean
    private DomesticVrpPaymentConsentService vrpPaymentConsentService;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void createVrpPayment() throws Exception {
        // Given
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        FRDomesticVRPConsent frDomesticVRPConsent = aValidFRDomesticVRPConsent(
                IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId(),
                ConsentStatusCode.AUTHORISED
        );

        OBDomesticVRPConsentResponse consentResponse = FRDomesticVRPConsentConverter.toOBDomesticVRPConsentResponsev3_1_10(frDomesticVRPConsent);

        OBDomesticVRPRequest request = buildAValidOBDomesticVRPRequest(consentResponse);

        OBDomesticVRPResponse rsStoreResponse = aValidOBDomesticVRPResponse(request);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(rsStoreResponse));
        Tpp tpp = new Tpp();
        tpp.setAuthorisationNumber("test-tpp");
        given(tppStoreService.findByClientId(any())).willReturn(Optional.of(tpp));
        given(vrpPaymentConsentService.getVrpPaymentConsent(request.getData().getConsentId())).willReturn(frDomesticVRPConsent);

        // When
        HttpResponse<OBDomesticVRPResponse> response = Unirest.post(HOST + port + VRP_CONTXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(OBDomesticVRPResponse.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getBody()).isEqualTo(rsStoreResponse);
    }

    @Test
    public void createVrpPaymentInitiationNotMatch() throws Exception {
        // Given
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        FRDomesticVRPConsent frDomesticVRPConsent = aValidFRDomesticVRPConsent(
                IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId(),
                ConsentStatusCode.AUTHORISED
        );

        OBDomesticVRPConsentResponse consentResponse = FRDomesticVRPConsentConverter.toOBDomesticVRPConsentResponsev3_1_10(frDomesticVRPConsent);

        OBDomesticVRPRequest request = buildAValidOBDomesticVRPRequest(consentResponse);
        request.getData().setInitiation(new OBDomesticVRPInitiation());

        given(vrpPaymentConsentService.getVrpPaymentConsent(request.getData().getConsentId())).willReturn(frDomesticVRPConsent);

        // When
        HttpResponse<ResponseEntity> response = Unirest.post(HOST + port + VRP_CONTXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(ResponseEntity.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getParsingError().get().getOriginalBody()).contains(
                "{\"ErrorCode\":\"UK.OBIE.Resource.ConsentMismatch\""
        );
        assertThat(response.getParsingError().get().getOriginalBody()).contains(
                "The provided initiation data differs from that in the matching consent"
        );
    }

    @Test
    public void createVrpPaymentRiskNotMatch() throws Exception {
        // Given
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        FRDomesticVRPConsent frDomesticVRPConsent = aValidFRDomesticVRPConsent(
                IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId(),
                ConsentStatusCode.AUTHORISED
        );

        OBDomesticVRPConsentResponse consentResponse = FRDomesticVRPConsentConverter.toOBDomesticVRPConsentResponsev3_1_10(frDomesticVRPConsent);

        OBDomesticVRPRequest request = buildAValidOBDomesticVRPRequest(consentResponse);
        request.setRisk(new OBRisk1());

        given(vrpPaymentConsentService.getVrpPaymentConsent(request.getData().getConsentId())).willReturn(frDomesticVRPConsent);

        // When
        HttpResponse<ResponseEntity> response = Unirest.post(HOST + port + VRP_CONTXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(ResponseEntity.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getParsingError().get().getOriginalBody()).contains(
                "{\"ErrorCode\":\"UK.OBIE.Resource.ConsentMismatch\""
        );
        assertThat(response.getParsingError().get().getOriginalBody()).contains(
                "The provided risk data differs from that in the matching consent"
        );
    }

    @Test
    public void createVrpPaymentInstructedCreditorAccountNotProvided() throws Exception {
        // Given
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        FRDomesticVRPConsent frDomesticVRPConsent = aValidFRDomesticVRPConsent(
                IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId(),
                ConsentStatusCode.AUTHORISED
        );

        OBDomesticVRPConsentResponse consentResponse = FRDomesticVRPConsentConverter.toOBDomesticVRPConsentResponsev3_1_10(frDomesticVRPConsent);

        OBDomesticVRPRequest request = buildAValidOBDomesticVRPRequest(consentResponse);
        frDomesticVRPConsent.getVrpDetails().getData().getInitiation().setCreditorAccount(null);
        consentResponse.getData().getInitiation().setCreditorAccount(null);
        request.getData().getInstruction().setCreditorAccount(null);

        given(vrpPaymentConsentService.getVrpPaymentConsent(request.getData().getConsentId())).willReturn(frDomesticVRPConsent);

        // When
        HttpResponse<ResponseEntity> response = Unirest.post(HOST + port + VRP_CONTXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(ResponseEntity.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getParsingError().get().getOriginalBody()).contains(
                "{\"ErrorCode\":\"UK.OBIE.Field.Invalid\""
        );
        assertThat(response.getParsingError().get().getOriginalBody()).contains(
                "data.instruction.creditorAccount"
        );
    }

    @Test
    public void createVrpPaymentBreachLimitationMaxAmount() throws Exception {
        // Given
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        FRDomesticVRPConsent frDomesticVRPConsent = aValidFRDomesticVRPConsent(
                IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId(),
                ConsentStatusCode.AUTHORISED
        );

        OBDomesticVRPConsentResponse consentResponse = FRDomesticVRPConsentConverter.toOBDomesticVRPConsentResponsev3_1_10(frDomesticVRPConsent);
        Double maxAmount = Double.valueOf(consentResponse.getData().getControlParameters().getMaximumIndividualAmount().getAmount());
        OBDomesticVRPRequest request = buildAValidOBDomesticVRPRequest(consentResponse);
        request.getData().getInstruction().getInstructedAmount().setAmount(
                Double.toString(Double.sum(maxAmount.doubleValue(), Double.parseDouble("1000.00")))
        );

        given(vrpPaymentConsentService.getVrpPaymentConsent(request.getData().getConsentId())).willReturn(frDomesticVRPConsent);

        // When
        HttpResponse<ResponseEntity> response = Unirest.post(HOST + port + VRP_CONTXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(ResponseEntity.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getParsingError().get().getOriginalBody()).contains(
                "{\"ErrorCode\":\"UK.OBIE.Rules.FailsControlParameters\""
        );
        String expectedMessage = String.format(
                "The field '%s' breach a limitation set by '%s'",
                VRPErrorControlParametersFields.RequestControlFields.MAX_INDIVIDUAL_AMOUNT,
                VRPErrorControlParametersFields.ConsentControlFields.MAX_INDIVIDUAL_AMOUNT
        );
        assertThat(response.getParsingError().get().getOriginalBody()).contains(expectedMessage);
    }

    @Test
    public void simulateVrpLimitBreachResponse() throws Exception {
        // Given
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        FRDomesticVRPConsent frDomesticVRPConsent = aValidFRDomesticVRPConsent(
                IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId(),
                ConsentStatusCode.AUTHORISED
        );

        OBDomesticVRPConsentResponse consentResponse = FRDomesticVRPConsentConverter.toOBDomesticVRPConsentResponsev3_1_10(frDomesticVRPConsent);

        OBDomesticVRPRequest request = buildAValidOBDomesticVRPRequest(consentResponse);

        OBDomesticVRPResponse rsStoreResponse = aValidOBDomesticVRPResponse(request);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(rsStoreResponse));
        Tpp tpp = new Tpp();
        tpp.setAuthorisationNumber("test-tpp");
        given(tppStoreService.findByClientId(any())).willReturn(Optional.of(tpp));
        given(vrpPaymentConsentService.getVrpPaymentConsent(request.getData().getConsentId())).willReturn(frDomesticVRPConsent);

        // When
        HttpResponse<OBErrorResponse1> response = Unirest.post(HOST + port + VRP_CONTXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .header("x-vrp-limit-breach-response-simulation", "Month-Calendar")
                .body(request)
                .asObject(OBErrorResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        List<OBError1> errors = response.getBody().getErrors();
        Assert.assertEquals("OBError1 objects in response", 1, errors.size());
        OBError1 error = errors.get(0);
        Assert.assertEquals(OBRIErrorType.REQUEST_VRP_CONTROL_PARAMETERS_PAYMENT_PERIODIC_LIMIT_BREACH.getCode().getValue(),
                            error.getErrorCode());
        Assert.assertEquals("Unable to complete payment due to payment limit breach, periodic limit of '10.01' 'GBP' for period 'Month' 'Calendar' has been breached",
                            error.getMessage());
    }

    @Test
    public void simulateVrpLimitBreachResponseInvalidHeader() throws Exception {
        // Given
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        FRDomesticVRPConsent frDomesticVRPConsent = aValidFRDomesticVRPConsent(
                IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId(),
                ConsentStatusCode.AUTHORISED
        );

        OBDomesticVRPConsentResponse consentResponse = FRDomesticVRPConsentConverter.toOBDomesticVRPConsentResponsev3_1_10(frDomesticVRPConsent);

        OBDomesticVRPRequest request = buildAValidOBDomesticVRPRequest(consentResponse);

        OBDomesticVRPResponse rsStoreResponse = aValidOBDomesticVRPResponse(request);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(rsStoreResponse));
        Tpp tpp = new Tpp();
        tpp.setAuthorisationNumber("test-tpp");
        given(tppStoreService.findByClientId(any())).willReturn(Optional.of(tpp));
        given(vrpPaymentConsentService.getVrpPaymentConsent(request.getData().getConsentId())).willReturn(frDomesticVRPConsent);

        // When
        HttpResponse<OBErrorResponse1> response = Unirest.post(HOST + port + VRP_CONTXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .header("x-vrp-limit-breach-response-simulation", "badLimitBreachValue")
                .body(request)
                .asObject(OBErrorResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        List<OBError1> errors = response.getBody().getErrors();
        Assert.assertEquals("OBError1 objects in response", 1, errors.size());
        OBError1 error = errors.get(0);
        Assert.assertEquals(ErrorCode.OBRI_REQUEST_VRP_LIMIT_BREACH_SIMULATION_INVALID_HEADER_VALUE.getValue(), error.getErrorCode());
        Assert.assertEquals("Invalid Header value 'badLimitBreachValue', unable to simulate the payment limitation breach",
                error.getMessage());
    }

    @Test
    public void shouldRejectedInstructedAmountsWithMoreThan2DecimalPlaces() throws Exception {
        OBDomesticVRPRequest request = OBDomesticVRPRequestTestDataFactory3_1_10.aValidOBDomesticVRPRequest();
        request.getData().getInstruction().setInstructedAmount(new OBActiveOrHistoricCurrencyAndAmount().amount("0.00001").currency("GBP"));
        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        final String expectedErrorCode = ErrorCode.OBRI_REQUEST_AMOUNT_MAX_2_DP.getValue();
        final String expectedErrorMsg = "Amount represented in field: 'InstructedAmount' can have a maximum of 2 decimal places";

        submitBadVrpRequestAndValidateErrorResponse(request, expectedHttpStatus, expectedErrorCode, expectedErrorMsg);
    }

    private void submitBadVrpRequestAndValidateErrorResponse(OBDomesticVRPRequest request, HttpStatus expectedHttpStatus,
                                                             String expectedErrorCode, String expectedErrorMsg)
            throws JOSEException, ParseException, InvalidTokenException, IOException {
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        FRDomesticVRPConsent frDomesticVRPConsent = aValidFRDomesticVRPConsent(
                IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId(),
                ConsentStatusCode.AUTHORISED
        );

        Tpp tpp = new Tpp();
        tpp.setAuthorisationNumber("test-tpp");
        given(tppStoreService.findByClientId(any())).willReturn(Optional.of(tpp));
        given(vrpPaymentConsentService.getVrpPaymentConsent(request.getData().getConsentId())).willReturn(frDomesticVRPConsent);

        HttpResponse<OBErrorResponse1> response = Unirest.post(HOST + port + VRP_CONTXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(OBErrorResponse1.class);

        assertThat(response.getStatus()).as("Http Response Code").isEqualTo(expectedHttpStatus.value());
        final OBErrorResponse1 errorResponse = response.getBody();
        assertThat(errorResponse.getErrors().size()).as("OBErrorResponse1.errors").isEqualTo(1);

        final OBError1 obError1 = errorResponse.getErrors().get(0);
        assertThat(obError1.getErrorCode()).as("ObError1.errorCode").isEqualTo(expectedErrorCode);
        assertThat(obError1.getMessage()).as("OError1.message").isEqualTo(expectedErrorMsg);
    }

    private FRDomesticVRPConsent aValidFRDomesticVRPConsent(String consentId, ConsentStatusCode consentStatusCode) {
        FRDomesticVRPConsentDetails details = toFRDomesticVRPConsentDetails(aValidOBDomesticVRPConsentRequest());
        FRDomesticVRPConsent consent = JMockData.mock(FRDomesticVRPConsent.class);
        consent.setVrpDetails(details);
        consent.setId(consentId);
        consent.setIdempotencyKey(UUID.randomUUID().toString());
        consent.setStatus(consentStatusCode);
        consent.setObVersion(OBVersion.v3_1_10);
        return consent;
    }

    private OBDomesticVRPRequest buildAValidOBDomesticVRPRequest(OBDomesticVRPConsentResponse consentResponse) {
        OBDomesticVRPConsentResponseData consentResponseData = consentResponse.getData();
        OBDomesticVRPInitiation consentInitiation = consentResponseData.getInitiation();
        OBDomesticVRPRequest request = aValidOBDomesticVRPRequest(consentResponseData.getConsentId());
        request.setRisk(consentResponse.getRisk());
        request.getData().setInitiation(consentInitiation);
        request.getData().getInstruction().setCreditorAccount(consentInitiation.getCreditorAccount());
        request.getData().getInstruction().setCreditorPostalAddress(consentInitiation.getCreditorPostalAddress());
        request.getData().getInstruction().getRemittanceInformation().setReference(
                consentInitiation.getRemittanceInformation().getReference()
        );
        request.getData().getInstruction().getRemittanceInformation().setUnstructured(
                consentInitiation.getRemittanceInformation().getUnstructured()
        );
        request.getData().getInstruction().setSupplementaryData(
                consentResponseData.getControlParameters().getSupplementaryData()
        );
        return request;
    }

    private OBDomesticVRPResponse aValidOBDomesticVRPResponse(OBDomesticVRPRequest request) {
        return (new OBDomesticVRPResponse())
                .data(OBDomesticVRPResponseTestDataFactory3_1_10.aValidOBDomesticVRPResponseData(request))
                .risk(aValidOBRisk1())
                .links(new Links().self(HOST + port + VRP_CONTXT_PATH));
    }
}
