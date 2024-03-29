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
package com.forgerock.openbanking.aspsp.rs.api.account.v2_0.transactions;


import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRReadDataResponse;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRReadResponse;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalRequestStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccountRequest;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.accountrequest.AccountRequestStoreService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.oidc.services.UserInfoService;
import com.google.common.collect.ImmutableMap;
import com.nimbusds.jwt.SignedJWT;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.account.OBReadTransaction2;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.FROM_BOOKING_DATE_TIME;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.TO_BOOKING_DATE_TIME;
import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsApiControllerIT {

    private static final DateTime CONSENT_FROM = DateTime.now().minusDays(3);
    private static final DateTime CONSENT_TO = DateTime.now().plusDays(3);
    private static final String CLIENT_ID = "test-tpp";
    private static final String AUTHORISATION_NUMBER = "PDSGB-OB-324354";

    private Tpp tpp;

    @LocalServerPort
    private int port;

    @Autowired
    private RSConfiguration rsConfiguration;
    @MockBean
    public AccountRequestStoreService accountRequestStore;

    @MockBean
    private UserInfoService userInfoService;

    @MockBean(name="cryptoApiClient") // Required to avoid Spring auto-wiring exception
    private CryptoApiClient cryptoApiClient;
    @MockBean(name="amResourceServerService") // Required to avoid Spring auto-wiring exception
    private AMResourceServerService amResourceServerService;
    @MockBean
    private RsStoreGateway rsStoreGateway;
    @Autowired
    private SpringSecForTest springSecForTest;
    @MockBean
    private TppStoreService tppStoreService;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper()).verifySsl(false);
        this.tpp = new Tpp();
        tpp.setClientId(CLIENT_ID);
        tpp.setAuthorisationNumber(AUTHORISATION_NUMBER);
    }

    @Test
    public void getAccountTransactionShouldBeOk() throws Exception {
        // Given
        String jws = jws("accounts");
        this.mockAuthenticationCollector();
        this.mockTppStoreService();
        mockAccessTokenVerification(jws);
        mockAccountPermissions(Arrays.asList(
                FRExternalPermissionsCode.READTRANSACTIONSDETAIL,
                FRExternalPermissionsCode.READTRANSACTIONSCREDITS,
                FRExternalPermissionsCode.READTRANSACTIONSDEBITS));
        OBReadTransaction2 transaction = new OBReadTransaction2();
        given(rsStoreGateway.toRsStore(any(), any(), any(), any())).willReturn(ResponseEntity.ok(transaction));



        // When
        HttpResponse<OBReadTransaction2> response = Unirest.get("https://rs-api:" + port + "/open-banking/v2.0/accounts/100000123/transactions")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(OBReadTransaction2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void getAccountTransactionShouldNotGetMoreTransactionThatConsentAllows() throws Exception {
        // Given
        String jws = jws("accounts");
        this.mockAuthenticationCollector();
        this.mockTppStoreService();
        this.mockAccessTokenVerification(jws);
        mockAccountPermissions(Arrays.asList(
                FRExternalPermissionsCode.READTRANSACTIONSDETAIL,
                FRExternalPermissionsCode.READTRANSACTIONSCREDITS,
                FRExternalPermissionsCode.READTRANSACTIONSDEBITS));
        OBReadTransaction2 transaction = new OBReadTransaction2();
        given(rsStoreGateway.toRsStore(any(), any(), any(), any())).willReturn(ResponseEntity.ok(transaction));
        DateTime older = DateTime.now().minusDays(5);
        DateTime newer = DateTime.now().plusDays(5);

        // When
        HttpResponse<OBReadTransaction2> response = Unirest.get("https://rs-api:" + port + "/open-banking/v2.0/accounts/100000123/transactions")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .queryString(TO_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(newer))
                .queryString(FROM_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(older))
                .asObject(OBReadTransaction2.class);

        assertThat(response.getStatus()).isEqualTo(200);
        ImmutableMap<String, String> params = ImmutableMap.<String, String>builder()
                .put(TO_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(CONSENT_TO))
                .put(FROM_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(CONSENT_FROM)).build();
        verify(rsStoreGateway).toRsStore(any(), any(), eq(params), any());
    }

    @Test
    public void getAccountStatementTransactionShouldBeOk() throws Exception {
        // Given
        String jws = jws("accounts");
        this.mockAuthenticationCollector();
        this.mockTppStoreService();
        mockAccessTokenVerification(jws);
        mockAuthenticationCollector();
        mockAccountPermissions(Arrays.asList(
                FRExternalPermissionsCode.READSTATEMENTSDETAIL,
                FRExternalPermissionsCode.READTRANSACTIONSDETAIL,
                FRExternalPermissionsCode.READTRANSACTIONSCREDITS,
                FRExternalPermissionsCode.READTRANSACTIONSDEBITS));
        OBReadTransaction2 transaction = new OBReadTransaction2();
        given(rsStoreGateway.toRsStore(any(), any(), any(), any())).willReturn(ResponseEntity.ok(transaction));

        // When
        HttpResponse<OBReadTransaction2> response = Unirest.get("https://rs-api:" + port + "/open-banking/v2.0/accounts/100000123/statements/1000001234/transactions")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(OBReadTransaction2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }



    @Test
    public void getAccountStatementTransactionShouldNotGetMoreTransactionThatConsentAllows() throws Exception {
        // Given
        String jws = jws("accounts");
        this.mockAuthenticationCollector();
        this.mockTppStoreService();
        mockAccessTokenVerification(jws);
        mockAccountPermissions(Arrays.asList(
                FRExternalPermissionsCode.READSTATEMENTSDETAIL,
                FRExternalPermissionsCode.READTRANSACTIONSDETAIL,
                FRExternalPermissionsCode.READTRANSACTIONSCREDITS,
                FRExternalPermissionsCode.READTRANSACTIONSDEBITS));
        OBReadTransaction2 transaction = new OBReadTransaction2();
        given(rsStoreGateway.toRsStore(any(), any(), any(), any())).willReturn(ResponseEntity.ok(transaction));
        DateTime older = DateTime.now().minusDays(5);
        DateTime newer = DateTime.now().plusDays(5);

        // When
        HttpResponse<OBReadTransaction2> response = Unirest.get("https://rs-api:" + port + "/open-banking/v2.0/accounts/100000123/statements/1000001234/transactions")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .queryString(TO_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(newer))
                .queryString(FROM_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(older))
                .asObject(OBReadTransaction2.class);

        assertThat(response.getStatus()).isEqualTo(200);
        ImmutableMap<String, String> params = ImmutableMap.<String, String>builder()
                .put(TO_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(CONSENT_TO))
                .put(FROM_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(CONSENT_FROM)).build();
        verify(rsStoreGateway).toRsStore(any(), any(), eq(params), any());
    }

    @Test
    public void getTransactionShouldBeOk() throws Exception {
        // Given
        String jws = jws("accounts");
        this.mockAuthenticationCollector();
        this.mockTppStoreService();
        mockAccessTokenVerification(jws);
        mockAccountPermissions(Arrays.asList(
                FRExternalPermissionsCode.READTRANSACTIONSDETAIL,
                FRExternalPermissionsCode.READTRANSACTIONSCREDITS,
                FRExternalPermissionsCode.READTRANSACTIONSDEBITS));
        OBReadTransaction2 transaction = new OBReadTransaction2();
        given(rsStoreGateway.toRsStore(any(), any(), any(), any())).willReturn(ResponseEntity.ok(transaction));

        // When
        HttpResponse<OBReadTransaction2> response = Unirest.get("https://rs-api:" + port + "/open-banking/v2.0/transactions")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(OBReadTransaction2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void getTransactionShouldNotGetMoreTransactionThatConsentAllows() throws Exception {
        // Given
        String jws = jws("accounts");
        this.mockAuthenticationCollector();
        this.mockTppStoreService();
        mockAccessTokenVerification(jws);
        mockAccountPermissions(Arrays.asList(
                FRExternalPermissionsCode.READTRANSACTIONSDETAIL,
                FRExternalPermissionsCode.READTRANSACTIONSCREDITS,
                FRExternalPermissionsCode.READTRANSACTIONSDEBITS));
        OBReadTransaction2 transaction = new OBReadTransaction2();
        given(rsStoreGateway.toRsStore(any(), any(), any(), any())).willReturn(ResponseEntity.ok(transaction));
        DateTime older = DateTime.now().minusDays(5);
        DateTime newer = DateTime.now().plusDays(5);

        // When
        HttpResponse<OBReadTransaction2> response = Unirest.get("https://rs-api:" + port + "/open-banking/v2.0/transactions")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .queryString(TO_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(newer))
                .queryString(FROM_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(older))
                .asObject(OBReadTransaction2.class);

        assertThat(response.getStatus()).isEqualTo(200);
        ImmutableMap<String, String> params = ImmutableMap.<String, String>builder()
                .put(TO_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(CONSENT_TO))
                .put(FROM_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(CONSENT_FROM)).build();
        verify(rsStoreGateway).toRsStore(any(), any(), eq(params), any());
    }

    private void mockAuthenticationCollector() {
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        springSecForTest.mockAuthCollector.mockUser(AUTHORISATION_NUMBER, OBRIRole.ROLE_AISP);
    }

    private void mockTppStoreService(){
        given(tppStoreService.findByClientId(CLIENT_ID)).willReturn(Optional.of(tpp));
    }

    private void mockAccessTokenVerification(String jws) throws ParseException, InvalidTokenException, IOException {
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
    }

    private void mockAccountPermissions(List<FRExternalPermissionsCode> permissions) {
        FRAccountRequest value = new FRAccountRequest();
        value.setAisp(tpp);
        value.setAccountIds(Collections.singletonList("100000123"));
        value.setAccountRequest(FRReadResponse.builder()
                .data(FRReadDataResponse.builder()
                        .permissions(permissions)
                        .transactionFromDateTime(CONSENT_FROM)
                        .transactionToDateTime(CONSENT_TO)
                        .status(FRExternalRequestStatusCode.AUTHORISED)
                        .build())
                .build());
        given(accountRequestStore.get(any())).willReturn(Optional.of(value));
    }

}