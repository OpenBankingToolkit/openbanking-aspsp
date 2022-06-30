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
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.ErrorCode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
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
import uk.org.openbanking.datamodel.payment.OBExternalPaymentContext1Code;
import uk.org.openbanking.datamodel.vrp.Links;
import uk.org.openbanking.datamodel.vrp.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.vrp.OBPAFundsAvailableResult1;
import uk.org.openbanking.datamodel.vrp.OBVRPFundsConfirmationResponse;
import uk.org.openbanking.datamodel.vrp.OBVRPFundsConfirmationResponseData;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentRequestData;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentResponse;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentResponseData;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBVRPFundsConfirmationRequest;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static uk.org.openbanking.testsupport.vrp.OBDomesticVRPConsentRequestTestDataFactory3_1_10.aValidOBDomesticVRPConsentRequest;
import static uk.org.openbanking.testsupport.vrp.OBVRPFundsConfirmationRequestTestDataFactory3_1_10.aValidOBVRPFundsConfirmationRequest;

/**
 * Integration test for {@link DomesticVrpConsentsApiController}
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticVrpConsentsApiControllerIT {

    private static final String HOST = "https://rs-api:";
    private static final String CONTEXT_PATH = "/open-banking/v3.1.10/pisp/domestic-vrp-consents/";
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

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void createVrpPaymentConsent() throws Exception {
        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getData().setReadRefundAccount(OBDomesticVRPConsentRequestData.ReadRefundAccountEnum.NO);
        OBDomesticVRPConsentResponse rsStoreResponse = aValidOBDomesticVRPConsentResponse(request);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(rsStoreResponse));
        Tpp tpp = new Tpp();
        tpp.setAuthorisationNumber("test-tpp");
        given(tppStoreService.findByClientId(any())).willReturn(Optional.of(tpp));

        // When
        HttpResponse<OBDomesticVRPConsentResponse> response = Unirest.post(HOST + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_INTERACTION_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(OBDomesticVRPConsentResponse.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getBody()).isEqualTo(rsStoreResponse);
        assertThat(response.getBody().getRisk()).isEqualTo(request.getRisk());
    }

    @Test
    public void createVrpPaymentConsentWrongGrantType() throws Exception {
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getData().setReadRefundAccount(OBDomesticVRPConsentRequestData.ReadRefundAccountEnum.NO);

        // When
        HttpResponse<ResponseEntity> response = Unirest.post(HOST + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_INTERACTION_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(ResponseEntity.class);
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getParsingError().get().getOriginalBody()).contains("{\"ErrorCode\":\"OBRI.AccessToken.Invalid\"");
    }

    @Test
    public void getVrpFundsConfirmation() throws Exception {
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBVRPFundsConfirmationRequest request = aValidOBVRPFundsConfirmationRequest();
        OBVRPFundsConfirmationResponse rsStoreResponse = aValidOBVRPFundsConfirmationResponse(request);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.OK).body(rsStoreResponse));

        // When
        HttpResponse<OBVRPFundsConfirmationResponse> response = Unirest.post(
                        HOST + port + CONTEXT_PATH + request.getData().getConsentId() + "/funds-confirmation"
                )
                .header(OBHeaders.X_FAPI_INTERACTION_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(OBVRPFundsConfirmationResponse.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody()).isEqualTo(rsStoreResponse);
    }

    @Test
    public void shouldFailToGetFundsConfirmationForAmountWithMoreThan2DP() throws Exception {
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBVRPFundsConfirmationRequest request = aValidOBVRPFundsConfirmationRequest();
        request.getData().setInstructedAmount(new OBActiveOrHistoricCurrencyAndAmount().currency("GBP").amount("123.45678"));

        // When
        HttpResponse<OBErrorResponse1> response = Unirest.post(
                        HOST + port + CONTEXT_PATH + request.getData().getConsentId() + "/funds-confirmation"
                )
                .header(OBHeaders.X_FAPI_INTERACTION_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(OBErrorResponse1.class);

        // Then
        assertThat(response.getStatus()).as("Http Response Code").isEqualTo(HttpStatus.BAD_REQUEST.value());
        final OBErrorResponse1 errorResponse = response.getBody();
        assertThat(errorResponse.getErrors().size()).as("OBErrorResponse1.errors").isEqualTo(1);

        final OBError1 obError1 = errorResponse.getErrors().get(0);
        assertThat(obError1.getErrorCode()).as("ObError1.errorCode").isEqualTo(ErrorCode.OBRI_REQUEST_AMOUNT_MAX_2_DP.getValue());
        assertThat(obError1.getMessage()).as("OError1.message").isEqualTo("Amount represented in field: 'InstructedAmount' can have a maximum of 2 decimal places");
    }

    @Test
    public void getVrpFundsConfirmationConsentIdDontMatch() throws Exception {
        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBVRPFundsConfirmationRequest request = aValidOBVRPFundsConfirmationRequest();
        OBVRPFundsConfirmationResponse rsStoreResponse = aValidOBVRPFundsConfirmationResponse(request);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.OK).body(rsStoreResponse));

        // When
        HttpResponse<ResponseEntity>  response = Unirest.post(
                        HOST + port + CONTEXT_PATH + "DVRP_" + UUID.randomUUID() + "/funds-confirmation"
                )
                .header(OBHeaders.X_FAPI_INTERACTION_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(ResponseEntity.class);
    /*
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The consent ID '" + consentId +
                    "' path parameter does not match with the consent ID '" +
                    obVRPFundsConfirmationRequest.getData().getConsentId() + "' requested to confirm the funds.");
     */
        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getParsingError().get().getOriginalBody()).contains("path parameter does not match with the consent ID");
    }

    @Test
    public void getVrpFundsConfirmationWrongGrantType() throws Exception {
        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBVRPFundsConfirmationRequest request = aValidOBVRPFundsConfirmationRequest();

        // When
        HttpResponse<ResponseEntity> response = Unirest.post(
                        HOST + port + CONTEXT_PATH + request.getData().getConsentId() + "/funds-confirmation"
                )
                .header(OBHeaders.X_FAPI_INTERACTION_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(ResponseEntity.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getParsingError().get().getOriginalBody()).contains("{\"ErrorCode\":\"OBRI.AccessToken.Invalid\"");
    }

    @Test
    public void getVrpPaymentConsent() throws Exception {
        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getData().setReadRefundAccount(OBDomesticVRPConsentRequestData.ReadRefundAccountEnum.NO);
        OBDomesticVRPConsentResponse rsStoreResponse = aValidOBDomesticVRPConsentResponse(request);
        given(rsStoreGateway.toRsStore(any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.OK).body(rsStoreResponse));
//        Tpp tpp = new Tpp();
//        tpp.setAuthorisationNumber("test-tpp");
//        given(tppStoreService.findByClientId(any())).willReturn(Optional.of(tpp));

        // When
        HttpResponse<OBDomesticVRPConsentResponse> response = Unirest.get(HOST + port + CONTEXT_PATH + rsStoreResponse.getData().getConsentId())
                .header(OBHeaders.X_FAPI_INTERACTION_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(OBDomesticVRPConsentResponse.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody()).isEqualTo(rsStoreResponse);
    }

    @Test
    public void deleteVrpPaymentConsent() throws Exception {
        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getData().setReadRefundAccount(OBDomesticVRPConsentRequestData.ReadRefundAccountEnum.NO);
        OBDomesticVRPConsentResponse rsStoreResponse = aValidOBDomesticVRPConsentResponse(request);
        given(rsStoreGateway.toRsStore(any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
//        Tpp tpp = new Tpp();
//        tpp.setAuthorisationNumber("test-tpp");
//        given(tppStoreService.findByClientId(any())).willReturn(Optional.of(tpp));

        // When
        HttpResponse response = Unirest.delete(HOST + port + CONTEXT_PATH + rsStoreResponse.getData().getConsentId())
                .header(OBHeaders.X_FAPI_INTERACTION_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asEmpty();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void shouldRejectConsentForNonSweepingVrpType() throws Exception {
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getData().getControlParameters().setVrPType(List.of("xyz"));
        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        final String expectedErrorCode = ErrorCode.OBRI_REQUEST_VRP_TYPE_MUST_BE_SWEEPING.getValue();
        final String expectedErrorMsg = "'VRPType' field only supports value 'UK.OBIE.VRPType.Sweeping'";

        submitBadVrpConsentAndValidateErrorResponse(request, expectedHttpStatus, expectedErrorCode, expectedErrorMsg);
    }

    @Test
    public void shouldRejectConsentWithPeriodicLimitAmountMoreThan2Dp() throws Exception {
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getData().getControlParameters().getPeriodicLimits().get(0).setAmount("12.222");
        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        final String expectedErrorCode = ErrorCode.OBRI_REQUEST_AMOUNT_MAX_2_DP.getValue();
        final String expectedErrorMsg = "Amount represented in field: 'PeriodicLimits.Amount' can have a maximum of 2 decimal places";

        submitBadVrpConsentAndValidateErrorResponse(request, expectedHttpStatus, expectedErrorCode, expectedErrorMsg);
    }

    @Test
    public void shouldRejectConsentInvalidPSUAuthenticationMethod() throws Exception {
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getData().getControlParameters().setPsUAuthenticationMethods(List.of("sgdfsfgdfsg"));
        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        final String expectedErrorCode = ErrorCode.OBRI_REQUEST_VRP_PSU_AUTHENTICATION_METHODS_INVALID.getValue();
        final String expectedErrorMsg = "'PSUAuthenticationMethods' field only supports value 'UK.OBIE.SCANotRequired'";

        submitBadVrpConsentAndValidateErrorResponse(request, expectedHttpStatus, expectedErrorCode, expectedErrorMsg);
    }

    @Test
    public void shouldRejectConsentInvalidValidToDateTime() throws Exception {
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getData().getControlParameters().setValidToDateTime(request.getData().getControlParameters().getValidFromDateTime().minusMinutes(5));
        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        final String expectedErrorCode = ErrorCode.OBRI_REQUEST_VRP_CONSENT_VALID_TO_DATE_INVALID.getValue();
        final String expectedErrorMsg = "'ValidToDateTime' must be > 'ValidFromDateTime'";

        submitBadVrpConsentAndValidateErrorResponse(request, expectedHttpStatus, expectedErrorCode, expectedErrorMsg);
    }

    @Test
    public void shouldRejectConsentInvalidValidFromDateTime() throws Exception {
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getData().getControlParameters().setValidFromDateTime(DateTime.now().plusDays(120));
        request.getData().getControlParameters().setValidToDateTime(DateTime.now().plusDays(200));
        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        final String expectedErrorCode = ErrorCode.OBRI_REQUEST_VRP_CONSENT_VALID_FROM_DATE_INVALID.getValue();
        final String expectedErrorMsg = "'ValidFromDateTime' cannot be more than 31 days in the future";

        submitBadVrpConsentAndValidateErrorResponse(request, expectedHttpStatus, expectedErrorCode, expectedErrorMsg);
    }

    @Test
    public void shouldRejectConsentInvalidPaymentContextCode() throws Exception {
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getRisk().setPaymentContextCode(OBExternalPaymentContext1Code.ECOMMERCESERVICES);
        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        final String expectedErrorCode = ErrorCode.OBRI_REQUEST_VRP_RISK_PAYMENT_CONTEXT_CODE_INVALID.getValue();
        final String expectedErrorMsg = "'Risk.PaymentContextCode' only supports values: ['PartyToParty', 'TransferToSelf']";

        submitBadVrpConsentAndValidateErrorResponse(request, expectedHttpStatus, expectedErrorCode, expectedErrorMsg);
    }

    @Test
    public void shouldRejectConsentInvalidMaximumIndividualAmountWithMoreThan2DP() throws Exception {
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getData().getControlParameters().setMaximumIndividualAmount(new OBActiveOrHistoricCurrencyAndAmount().currency("GBP").amount("100.32122"));
        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        final String expectedErrorCode = ErrorCode.OBRI_REQUEST_AMOUNT_MAX_2_DP.getValue();
        final String expectedErrorMsg = "Amount represented in field: 'MaximumIndividualAmount' can have a maximum of 2 decimal places";

        submitBadVrpConsentAndValidateErrorResponse(request, expectedHttpStatus, expectedErrorCode, expectedErrorMsg);
    }

    @Test
    public void shouldRejectConsentInvalidMaximumIndividualAmountTooSmall() throws Exception {
        OBDomesticVRPConsentRequest request = aValidOBDomesticVRPConsentRequest();
        request.getData().getControlParameters().setMaximumIndividualAmount(new OBActiveOrHistoricCurrencyAndAmount().currency("GBP").amount("0.22"));
        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        final String expectedErrorCode = ErrorCode.OBRI_REQUEST_VRP_MAX_INDIVIDUAL_AMOUNT_TOO_SMALL.getValue();
        final String expectedErrorMsg = "'MaximumIndividualAmount' must be >= 1.00";

        submitBadVrpConsentAndValidateErrorResponse(request, expectedHttpStatus, expectedErrorCode, expectedErrorMsg);
    }

    private void submitBadVrpConsentAndValidateErrorResponse(OBDomesticVRPConsentRequest request, HttpStatus expectedHttpStatus,
                                                             String expectedErrorCode, String expectedErrorMsg)
            throws JOSEException, ParseException, InvalidTokenException, IOException {

        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));

        HttpResponse<OBErrorResponse1> response = Unirest.post(HOST + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_INTERACTION_ID, rsConfiguration.financialId)
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

    private OBDomesticVRPConsentResponse aValidOBDomesticVRPConsentResponse(OBDomesticVRPConsentRequest request) {
        DateTime now = DateTime.now();
        return new OBDomesticVRPConsentResponse()
                .data(new OBDomesticVRPConsentResponseData()
                        .consentId(UUID.randomUUID().toString())
                        .creationDateTime(now)
                        .readRefundAccount(
                                OBDomesticVRPConsentResponseData.ReadRefundAccountEnum.fromValue(
                                        request.getData().getReadRefundAccount().getValue()
                                )
                        )
                        .status(
                                OBDomesticVRPConsentResponseData.StatusEnum.fromValue(
                                        ConsentStatusCode.AWAITINGAUTHORISATION.getValue()
                                )
                        )
                        .statusUpdateDateTime(now)
                        .controlParameters(request.getData().getControlParameters())
                        .debtorAccount(request.getData().getInitiation().getDebtorAccount())
                        .initiation(request.getData().getInitiation())
                )
                .risk(request.getRisk())
                .links(new Links().self(URI.create(HOST + port + CONTEXT_PATH)));
    }

    private OBVRPFundsConfirmationResponse aValidOBVRPFundsConfirmationResponse(OBVRPFundsConfirmationRequest request) {
        return new OBVRPFundsConfirmationResponse()
                .data(
                        new OBVRPFundsConfirmationResponseData()
                                .consentId(request.getData().getConsentId())
                                .creationDateTime(DateTime.now())
                                .fundsConfirmationId(UUID.randomUUID().toString())
                                .reference(request.getData().getReference())
                                .fundsAvailableResult(
                                        new OBPAFundsAvailableResult1()
                                                .fundsAvailable(OBPAFundsAvailableResult1.FundsAvailableEnum.AVAILABLE)
                                                .fundsAvailableDateTime(DateTime.now()
                                                )
                                )
                                .instructedAmount(request.getData().getInstructedAmount())
                );
    }
}
