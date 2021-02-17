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
package com.forgerock.openbanking.aspsp.rs.api.event.v3_0;

import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.oidc.services.UserInfoService;
import com.nimbusds.jwt.SignedJWT;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
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
import uk.org.openbanking.datamodel.event.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CallbackUrlApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RSConfiguration rsConfiguration;

    @MockBean
    private UserInfoService userInfoService;
    @Autowired
    private SpringSecForTest springSecForTest;
    @MockBean(name = "cryptoApiClient") // Required to avoid Spring auto-wiring exception
    private CryptoApiClient cryptoApiClient;
    @MockBean(name = "amResourceServerService") // Required to avoid Spring auto-wiring exception
    private AMResourceServerService amResourceServerService;
    @MockBean
    private RsStoreGateway rsStoreGateway;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper()).verifySsl(false);
    }

    @Test
    public void getCallbackUrls() throws Exception {
        // Given
        String jws = jws(OpenBankingConstants.Scope.ACCOUNTS, OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);
        OBCallbackUrlsResponse1 obCallbackUrlsResponse = new OBCallbackUrlsResponse1();
        obCallbackUrlsResponse.data(new OBCallbackUrlsResponseData1()
                .callbackUrl(Collections.singletonList(new OBCallbackUrlResponseData1().callbackUrlId("123").url("https://localhost").version(OBVersion.v3_0.getCanonicalVersion()))));
        given(rsStoreGateway.toRsStore(any(), any(), any())).willReturn(ResponseEntity.ok(obCallbackUrlsResponse));

        // When
        HttpResponse<OBCallbackUrlsResponse1> response = Unirest.get("https://rs-api:" + port + "/open-banking/v3.0/callback-urls")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(OBCallbackUrlsResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void createCallbackUrls_badRequest_InvalidObject() throws Exception {
        // Given
        String jws = jws(OpenBankingConstants.Scope.ACCOUNTS, OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);
        OBCallbackUrl1 obCallbackUrl1 = new OBCallbackUrl1().data(
                new OBCallbackUrlData1()
                        .url("https://tpp.domain/v3.1/event-notifications")
                        .version(OBVersion.v3_0.getCanonicalVersion())
        );

        HttpResponse<OBCallbackUrlResponse1> response = Unirest.post("https://rs-api:" + port + "/open-banking/"+OBVersion.v3_0.getCanonicalName()+"/callback-urls")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obCallbackUrl1)
                .asObject(OBCallbackUrlResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getParsingError().get().getOriginalBody()).contains("Version on the callback url field https://tpp.domain/v3.1/event-notifications doesn't match with the version value field 3.0");
        assertThat(response.getParsingError().get().getOriginalBody()).contains(OBRIErrorType.REQUEST_OBJECT_INVALID.getCode().getValue());
    }

    private void mockAccessTokenVerification(String jws) throws ParseException, InvalidTokenException, IOException {
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
    }
}
