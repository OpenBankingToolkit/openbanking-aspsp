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
package com.forgerock.openbanking.aspsp.rs.api.account.v3_0.accountaccessconsent;

import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountAccessConsentPermittedPermissionsFilter;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
import com.nimbusds.jwt.SignedJWT;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
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
import uk.org.openbanking.datamodel.account.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountAccessConsentsApiControllerIT {

    @LocalServerPort
    private int port;
    @Autowired
    private RSConfiguration rsConfiguration;
    @MockBean(name = "cryptoApiClient") // Required to avoid Spring auto-wiring exception
    private CryptoApiClient cryptoApiClient;
    @MockBean(name = "amResourceServerService") // Required to avoid Spring auto-wiring exception
    private AMResourceServerService amResourceServerService;
    @MockBean
    private RsStoreGateway rsStoreGateway;
    @MockBean
    private AccountAccessConsentPermittedPermissionsFilter accountAccessConsentPermittedPermissionsFilter;
    @MockBean
    private TppStoreService tppStoreService;
    @Autowired
    private SpringSecForTest springSecForTest;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper()).verifySsl(false);
    }

    @Test
    public void createAccountAccessConsent() throws Exception {
        // Given
        String jws = jws("accounts", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);
        OBReadConsentResponse1 readConsentResponse = new OBReadConsentResponse1()
                .data(new OBReadConsentResponse1Data()
                        .consentId("AISP_3980298093280")
                        .statusUpdateDateTime(DateTime.now())
                        .status(OBExternalRequestStatus1Code.AWAITINGAUTHORISATION)
                        .permissions(Collections.singletonList(OBExternalPermissions1Code.READACCOUNTSBASIC))
                );
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(readConsentResponse));
        Tpp tpp = new Tpp();
        tpp.setAuthorisationNumber("test-tpp");
        given(tppStoreService.findByClientId(any())).willReturn(Optional.of(tpp));
        final OBReadConsent1 obReadConsent = new OBReadConsent1()
                .data(new OBReadData1().permissions(Collections.singletonList(OBExternalPermissions1Code.READACCOUNTSBASIC)))
                .risk(new OBRisk2());

        // When
        HttpResponse<OBReadConsentResponse1> response = Unirest.post("https://rs-api:" + port + "/open-banking/v3.1/aisp/account-access-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, UUID.randomUUID().toString())
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obReadConsent)
                .asObject(OBReadConsentResponse1.class);

        // Then
        verify(accountAccessConsentPermittedPermissionsFilter, times(1)).filter(any());
        assertThat(response.getStatus()).isEqualTo(201);
    }

    @Test
    public void getAccountAccessConsent() throws Exception {

        // Given
        String jws = jws("accounts", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);

        OBReadConsentResponse1 readConsentResponse = new OBReadConsentResponse1();
        given(rsStoreGateway.toRsStore(any(), any(), any())).willReturn(ResponseEntity.ok(readConsentResponse));
        Tpp tpp = new Tpp();
        tpp.setAuthorisationNumber("test-tpp");
        given(tppStoreService.findByClientId(any())).willReturn(Optional.of(tpp));

        // When
        HttpResponse<OBReadConsentResponse1> response = Unirest.get("https://rs-api:" + port + "/open-banking/v3.1/aisp/account-access-consents/100000123")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(OBReadConsentResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);

    }

    private void mockAccessTokenVerification(String jws) throws ParseException, InvalidTokenException, IOException {
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
    }
}
