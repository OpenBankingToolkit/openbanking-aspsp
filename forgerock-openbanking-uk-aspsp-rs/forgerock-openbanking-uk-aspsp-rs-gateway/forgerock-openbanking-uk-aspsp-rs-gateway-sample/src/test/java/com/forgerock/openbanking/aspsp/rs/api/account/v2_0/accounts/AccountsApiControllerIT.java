/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.account.v2_0.accounts;


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
import com.nimbusds.jwt.SignedJWT;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
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
import uk.org.openbanking.datamodel.account.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static uk.org.openbanking.datamodel.account.OBExternalPermissions1Code.READACCOUNTSDETAIL;
import static uk.org.openbanking.datamodel.account.OBExternalPermissions1Code.READBALANCES;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsApiControllerIT {

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
    @MockBean
    private RsStoreGateway rsStoreGateway;
    @Autowired
    private SpringSecForTest springSecForTest;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper()).verifySsl(false);
    }

    @Test
    public void getAccountShouldReturnAccountInfo() throws Exception {
        // Given
        String jws = jws("accounts");
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);
        mockAccountPermissions(Collections.singletonList(READACCOUNTSDETAIL));
        OBReadAccount2 obReadAccount2 = new OBReadAccount2();
        given(rsStoreGateway.toRsStore(any(), any(), any())).willReturn(ResponseEntity.ok(obReadAccount2));

        // When
        HttpResponse<OBReadAccount2> response = Unirest.get("https://rs-api:" + port + "/open-banking/v2.0/accounts/100000123")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(OBReadAccount2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void getAccountShouldBeForbiddenWhenNotAISP() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);

        // When
        HttpResponse<JsonNode> response = Unirest.get("https://rs-api:" + port + "/open-banking/v2.0/accounts/100000123")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws("accounts"))
                .asJson();

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void getAccountShouldBeForbiddenWhenNoReadAccountDetailsPermission() throws Exception {
        // Given
        String jws = jws("accounts");
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);
        mockAccountPermissions(Collections.singletonList(READBALANCES));

        // When
        HttpResponse<JsonNode> response = Unirest.get("https://rs-api:" + port + "/open-banking/v2.0/accounts/100000123")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asJson();

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
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
                        .status(OBExternalRequestStatus1Code.AUTHORISED)));
        given(accountRequestStore.get(any())).willReturn(Optional.of(value));
    }
}

