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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_9.domesticpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_7.domesticpayments.DomesticPaymentConsentsApiController;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.oidc.services.UserInfoService;
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
import uk.org.openbanking.datamodel.account.Links;
import uk.org.openbanking.datamodel.payment.*;

import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static uk.org.openbanking.testsupport.payment.OBWriteDomesticConsentTestDataFactory.aValidOBWriteDomesticConsent4;

/**
 * Integration test for {@link DomesticPaymentConsentsApiController}.
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticPaymentConsentsApiControllerIT {

    private static final String CONTEXT_PATH = "/open-banking/v3.1.9/pisp/domestic-payment-consents/";

    private static final String IDEMPOTENCY_KEY = UUID.randomUUID().toString();

    @LocalServerPort
    private int port;

    @Autowired
    private RSConfiguration rsConfiguration;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserInfoService userInfoService;

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
    public void testCreateDomesticPaymentConsent() throws Exception {
        // Given
        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBWriteDomesticConsent4 request = aValidOBWriteDomesticConsent4();
        OBWriteDomesticConsentResponse5 rsStoreResponse = aValidOBWriteDomesticConsentResponse5(request);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(rsStoreResponse));
        Tpp tpp = new Tpp();
        tpp.setAuthorisationNumber("test-tpp");
        given(tppStoreService.findByClientId(any())).willReturn(Optional.of(tpp));

        // When
        HttpResponse<OBWriteDomesticConsentResponse5> response = Unirest.post("https://rs-api:" + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(OBWriteDomesticConsentResponse5.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(rsStoreResponse);
    }

    private OBWriteDomesticConsentResponse5 aValidOBWriteDomesticConsentResponse5(OBWriteDomesticConsent4 request) {
        DateTime now = DateTime.now();
        return new OBWriteDomesticConsentResponse5()
                .data(new OBWriteDomesticConsentResponse5Data()
                        .consentId(UUID.randomUUID().toString())
                        .creationDateTime(now)
                        .status(OBWriteDomesticConsentResponse5Data.StatusEnum.AWAITINGAUTHORISATION)
                        .statusUpdateDateTime(now)
                        .readRefundAccount(request.getData().getReadRefundAccount())
                        .cutOffDateTime(now.plusDays(1))
                        .expectedExecutionDateTime(now)
                        .expectedSettlementDateTime(now.plusDays(1))
                        .initiation(request.getData().getInitiation())
                        .authorisation(request.getData().getAuthorisation())
                        .scASupportData(request.getData().getScASupportData())
                        .debtor(toOBCashAccountDebtor4(request.getData().getInitiation().getDebtorAccount()))
                )
                .risk(request.getRisk())
                .links(new Links().self("https://rs-api:" + port + CONTEXT_PATH));
    }

    private OBCashAccountDebtor4 toOBCashAccountDebtor4(OBWriteDomestic2DataInitiationDebtorAccount debtorAccount) {
        return debtorAccount == null ? null : new OBCashAccountDebtor4()
                .schemeName(debtorAccount.getSchemeName())
                .identification(debtorAccount.getIdentification())
                .name(debtorAccount.getName())
                .secondaryIdentification(debtorAccount.getSecondaryIdentification());
    }
}
