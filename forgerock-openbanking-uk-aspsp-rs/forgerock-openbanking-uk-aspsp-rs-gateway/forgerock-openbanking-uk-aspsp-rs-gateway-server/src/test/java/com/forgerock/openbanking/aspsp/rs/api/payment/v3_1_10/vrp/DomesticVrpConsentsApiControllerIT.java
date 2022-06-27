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
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
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
import uk.org.openbanking.datamodel.vrp.Links;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentRequest;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentRequestData;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentResponse;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentResponseData;
import uk.org.openbanking.datamodel.vrp.OBPAFundsAvailableResult1;
import uk.org.openbanking.datamodel.vrp.OBVRPFundsConfirmationRequest;
import uk.org.openbanking.datamodel.vrp.OBVRPFundsConfirmationResponse;
import uk.org.openbanking.datamodel.vrp.OBVRPFundsConfirmationResponseData;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static uk.org.openbanking.testsupport.vrp.OBDomesticVRPConsentRequestTestDataFactory.aValidOBDomesticVRPConsentRequest;
import static uk.org.openbanking.testsupport.vrp.OBVRPFundsConfirmationRequestTestDataFactory.aValidOBVRPFundsConfirmationRequest;

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
