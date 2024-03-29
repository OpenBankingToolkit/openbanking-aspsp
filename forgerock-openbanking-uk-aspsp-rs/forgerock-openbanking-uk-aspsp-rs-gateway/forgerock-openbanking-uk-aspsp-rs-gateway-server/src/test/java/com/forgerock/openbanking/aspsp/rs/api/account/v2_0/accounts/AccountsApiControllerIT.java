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
package com.forgerock.openbanking.aspsp.rs.api.account.v2_0.accounts;


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
import uk.org.openbanking.datamodel.account.OBReadAccount2;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode.READACCOUNTSDETAIL;
import static com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode.READBALANCES;
import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsApiControllerIT {

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
    TppStoreService tppStoreService;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper()).verifySsl(false);
        tpp = new Tpp();
        tpp.setClientId(CLIENT_ID);
        tpp.setAuthorisationNumber(AUTHORISATION_NUMBER);
    }

    @Test
    public void getAccountShouldReturnAccountInfo() throws Exception {
        // Given
        String jws = jws("accounts");
        this.mockAuthCollector();
        this.mockTppStoreService();
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

    private void mockAuthCollector(){
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
                        .status(FRExternalRequestStatusCode.AUTHORISED)
                        .build())
                .build());
        given(accountRequestStore.get(any())).willReturn(Optional.of(value));
    }
}

