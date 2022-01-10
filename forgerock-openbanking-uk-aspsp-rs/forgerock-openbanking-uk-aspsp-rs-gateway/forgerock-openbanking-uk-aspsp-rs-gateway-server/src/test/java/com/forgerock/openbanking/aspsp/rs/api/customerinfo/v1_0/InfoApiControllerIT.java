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
package com.forgerock.openbanking.aspsp.rs.api.customerinfo.v1_0;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
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
import kong.unirest.Unirest;
import kong.unirest.UnirestParsingException;
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
import uk.org.openbanking.datamodel.customerinfo.ReadCustomerInfo;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InfoApiControllerIT {

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
    @MockBean
    private TppStoreService tppStoreService;
    @Autowired
    private SpringSecForTest springSecForTest;


    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        JacksonObjectMapper jacksonObjectMapper = new JacksonObjectMapper(objectMapper);
        Unirest.config().setObjectMapper(jacksonObjectMapper).verifySsl(false);
        tpp = new Tpp();
        tpp.setClientId(CLIENT_ID);
        tpp.setAuthorisationNumber(AUTHORISATION_NUMBER);
    }

    @Test
    public void getAccountCustomerInfoShouldBeOk() throws Exception {
        // Given
        String jws = jws("accounts");
        this.mockAuthCollector();
        this.mockTppStoreService();
        mockAccessTokenVerification(jws);
        mockAccountPermissions(Arrays.asList(FRExternalPermissionsCode.READCUSTOMERINFOCONSENT));

        ReadCustomerInfo readCustomerInfo = InfoApiTestHelper.getValidReadCustomerInfo();
        ResponseEntity responseEntity = ResponseEntity.ok(readCustomerInfo);
        given(rsStoreGateway.toRsStore(any(), any(), any())).willReturn(responseEntity);

        // When
        HttpResponse<ReadCustomerInfo>   response = Unirest.get("https://rs-api:" + port + "/customer-info/v1.0/info")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(ReadCustomerInfo.class)
                .ifFailure(failingResponse -> {
                    UnirestParsingException ex = failingResponse.getParsingError().get();
                    if(ex != null) {
                        log.debug("Unirest parsing error '{}'", ex.getCause());
                        assertThat(ex).isNull();
                    }
                });

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody()).isEqualTo(readCustomerInfo);
    }

    @Test
    public void getAccountCustomerInfoShouldFailWhenCustomerIsUnder18() throws Exception {
        // Given
        String jws = jws("accounts");
        this.mockAuthCollector();
        this.mockTppStoreService();
        mockAccessTokenVerification(jws);
        mockAccountPermissions(Arrays.asList(FRExternalPermissionsCode.READCUSTOMERINFOCONSENT));

        ReadCustomerInfo readCustomerInfo = InfoApiTestHelper.getReadCustomerInfo_Under18();
        given(rsStoreGateway.toRsStore(any(), any(), any())).willReturn(ResponseEntity.ok(readCustomerInfo));

        // When
        HttpResponse<ReadCustomerInfo>   response = Unirest.get("https://rs-api:" + port + "/customer-info/v1.0/info")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(ReadCustomerInfo.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
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
        Tpp tpp = new Tpp();
        tpp.setClientId("test-tpp");
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
