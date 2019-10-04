/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.account.v1_1.transactions;


import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRAccountRequest1;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.accountrequest.AccountRequestStoreService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.account.*;

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

    @LocalServerPort
    private int port;

    @Autowired
    private RSConfiguration rsConfiguration;
    @MockBean
    public AccountRequestStoreService accountRequestStore;
    @Autowired
    private SpringSecForTest springSecForTest;

    @MockBean
    private UserInfoService userInfoService;


    @MockBean(name = "cryptoApiClient") // Required to avoid Spring auto-wiring exception
    private CryptoApiClient cryptoApiClient;
    @MockBean
    private RsStoreGateway rsStoreGateway;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper()).verifySsl(false);
    }

    @Test
    public void getAccountTransactionShouldBeOk() throws Exception {
        // Given
        String jws = jws("accounts");
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);
        mockAccountPermissions(Arrays.asList(
                OBExternalPermissions1Code.READTRANSACTIONSDETAIL,
                OBExternalPermissions1Code.READTRANSACTIONSCREDITS,
                OBExternalPermissions1Code.READTRANSACTIONSDEBITS));
        OBReadTransaction1 transaction = new OBReadTransaction1();
        given(rsStoreGateway.toRsStore(any(), any(), any(), any())).willReturn(ResponseEntity.ok(transaction));

        // When
        HttpResponse<OBReadTransaction1> response = Unirest.get("https://rs-api:" + port + "/open-banking/v1.1/accounts/100000123/transactions")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(OBReadTransaction1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void getAccountTransactionShouldNotGetMoreTransactionThatConsentAllows() throws Exception {
        // Given
        String jws = jws("accounts");
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);
        mockAccountPermissions(Arrays.asList(
                OBExternalPermissions1Code.READTRANSACTIONSDETAIL,
                OBExternalPermissions1Code.READTRANSACTIONSCREDITS,
                OBExternalPermissions1Code.READTRANSACTIONSDEBITS));
        OBReadTransaction1 transaction = new OBReadTransaction1();
        given(rsStoreGateway.toRsStore(any(), any(), any(), any())).willReturn(ResponseEntity.ok(transaction));
        DateTime older = DateTime.now().minusDays(5);
        DateTime newer = DateTime.now().plusDays(5);

        // When
        HttpResponse<OBReadTransaction1> response = Unirest.get("https://rs-api:" + port + "/open-banking/v1.1/accounts/100000123/transactions")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .queryString(TO_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(newer))
                .queryString(FROM_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(older))
                .asObject(OBReadTransaction1.class);

        assertThat(response.getStatus()).isEqualTo(200);
        ImmutableMap<String, String> params = ImmutableMap.<String, String>builder()
                .put(TO_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(CONSENT_TO))
                .put(FROM_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(CONSENT_FROM)).build();
        verify(rsStoreGateway).toRsStore(any(), any(), eq(params), any());
    }

    @Test
    @WithMockUser(roles = "AISP")
    public void getTransactionShouldBeOk() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);

        String jws = jws("accounts");

        mockAccessTokenVerification(jws);
        mockAccountPermissions(Arrays.asList(
                OBExternalPermissions1Code.READTRANSACTIONSDETAIL,
                OBExternalPermissions1Code.READTRANSACTIONSCREDITS,
                OBExternalPermissions1Code.READTRANSACTIONSDEBITS));
        OBReadTransaction1 transaction = new OBReadTransaction1();
        given(rsStoreGateway.toRsStore(any(), any(), any(), any())).willReturn(ResponseEntity.ok(transaction));

        // When
        HttpResponse<OBReadTransaction1> response = Unirest.get("https://rs-api:" + port + "/open-banking/v1.1/transactions")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(OBReadTransaction1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void getTransactionShouldNotGetMoreTransactionThatConsentAllows() throws Exception {
        // Given
        String jws = jws("accounts");
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);
        mockAccountPermissions(Arrays.asList(
                OBExternalPermissions1Code.READTRANSACTIONSDETAIL,
                OBExternalPermissions1Code.READTRANSACTIONSCREDITS,
                OBExternalPermissions1Code.READTRANSACTIONSDEBITS));
        OBReadTransaction1 transaction = new OBReadTransaction1();
        given(rsStoreGateway.toRsStore(any(), any(), any(), any())).willReturn(ResponseEntity.ok(transaction));
        DateTime older = DateTime.now().minusDays(5);
        DateTime newer = DateTime.now().plusDays(5);

        // When
        HttpResponse<OBReadTransaction1> response = Unirest.get("https://rs-api:" + port + "/open-banking/v1.1/transactions")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .queryString(TO_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(newer))
                .queryString(FROM_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(older))
                .asObject(OBReadTransaction1.class);

        assertThat(response.getStatus()).isEqualTo(200);
        ImmutableMap<String, String> params = ImmutableMap.<String, String>builder()
                .put(TO_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(CONSENT_TO))
                .put(FROM_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(CONSENT_FROM)).build();
        verify(rsStoreGateway).toRsStore(any(), any(), eq(params), any());
    }

    private void mockAccessTokenVerification(String jws) throws ParseException, InvalidTokenException, IOException {
        given(cryptoApiClient.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
    }

    private void mockAccountPermissions(List<OBExternalPermissions1Code> permissions) {
        FRAccountRequest1 value = new FRAccountRequest1();
        Tpp tpp = new Tpp();
        tpp.setClientId("test-tpp");
        value.setAisp(tpp);
        value.setAccountIds(Collections.singletonList("100000123"));
        value.setAccountRequest(new OBReadResponse1()
                .data(new OBReadDataResponse1()
                        .permissions(permissions)
                        .transactionFromDateTime(CONSENT_FROM)
                        .transactionToDateTime(CONSENT_TO)
                        .status(OBExternalRequestStatus1Code.AUTHORISED)));
        given(accountRequestStore.get(any())).willReturn(Optional.of(value));
    }

}