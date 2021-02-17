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
package com.forgerock.openbanking.aspsp.rs.api.event.aggregatedpolling;

import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.common.conf.RSConfiguration;
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
import org.apache.http.entity.ContentType;
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
import uk.org.openbanking.datamodel.event.OBEventPolling1;
import uk.org.openbanking.datamodel.event.OBEventPollingResponse1;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AggregatedPollingApiControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private RSConfiguration rsConfiguration;

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
    public void pollEvents_v3_1_2() throws Exception {
        // Given
        String jws = jws(OpenBankingConstants.Scope.EVENT_POLLING, OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);

        OBEventPollingResponse1 obEventPollingResponse = new OBEventPollingResponse1()
                .sets(Map.of("asdfasdfas","eyJhbG....asefasefa","asdfasdfas2","eyJhbG2....asefasefa"))
                .moreAvailable(false);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.ok(obEventPollingResponse));
        OBEventPolling1 obEventPolling = new OBEventPolling1().returnImmediately(true);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post("https://rs-api:" + port + "/open-banking/v3.1.2/events")
                .body(obEventPolling)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .asObject(OBEventPollingResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void pollEvents_v3_1_2_wrong_scope() throws Exception {
        // Given
        String jws = jws(OpenBankingConstants.Scope.ACCOUNTS, OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);

        OBEventPollingResponse1 obEventPollingResponse = new OBEventPollingResponse1()
                .sets(Map.of("asdfasdfas","eyJhbG....asefasefa","asdfasdfas2","eyJhbG2....asefasefa"))
                .moreAvailable(false);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.ok(obEventPollingResponse));
        OBEventPolling1 obEventPolling = new OBEventPolling1().returnImmediately(true);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post("https://rs-api:" + port + "/open-banking/v3.1.2/events")
                .body(obEventPolling)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .asObject(OBEventPollingResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getParsingError().get().getOriginalBody()).contains("Invalid access token. Missing scopes: [eventpolling]");
        assertThat(response.getParsingError().get().getOriginalBody()).contains(OBRIErrorType.ACCESS_TOKEN_INVALID_SCOPE.getCode().getValue());
    }

    @Test
    public void pollEvents_v3_1_3() throws Exception {
        // Given
        String jws = jws(OpenBankingConstants.Scope.EVENT_POLLING, OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);

        OBEventPollingResponse1 obEventPollingResponse = new OBEventPollingResponse1()
                .sets(Map.of("asdfasdfas","eyJhbG....asefasefa","asdfasdfas2","eyJhbG2....asefasefa"))
                .moreAvailable(false);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.ok(obEventPollingResponse));
        OBEventPolling1 obEventPolling = new OBEventPolling1().returnImmediately(true);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post("https://rs-api:" + port + "/open-banking/v3.1.3/events")
                .body(obEventPolling)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .asObject(OBEventPollingResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void pollEvents_v3_1_4_wrong_scope() throws Exception {
        // Given
        String jws = jws(OpenBankingConstants.Scope.EVENT_POLLING, OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);

        OBEventPollingResponse1 obEventPollingResponse = new OBEventPollingResponse1()
                .sets(Map.of("asdfasdfas","eyJhbG....asefasefa","asdfasdfas2","eyJhbG2....asefasefa"))
                .moreAvailable(false);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.ok(obEventPollingResponse));
        OBEventPolling1 obEventPolling = new OBEventPolling1().returnImmediately(true);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post("https://rs-api:" + port + "/open-banking/v3.1.4/events")
                .body(obEventPolling)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .asObject(OBEventPollingResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getParsingError().get().getOriginalBody()).contains("Invalid access token. Missing scopes: [accounts, fundsconfirmations]");
        assertThat(response.getParsingError().get().getOriginalBody()).contains(OBRIErrorType.ACCESS_TOKEN_INVALID_SCOPE.getCode().getValue());
    }

    @Test
    public void pollEvents_v3_1_4() throws Exception {
        // Given
        String jws = jws(OpenBankingConstants.Scope.ACCOUNTS, OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);

        OBEventPollingResponse1 obEventPollingResponse = new OBEventPollingResponse1()
                .sets(Map.of("asdfasdfas","eyJhbG....asefasefa","asdfasdfas2","eyJhbG2....asefasefa"))
                .moreAvailable(false);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.ok(obEventPollingResponse));
        OBEventPolling1 obEventPolling = new OBEventPolling1().returnImmediately(true);

        // When
        HttpResponse<OBEventPollingResponse1> response = Unirest.post("https://rs-api:" + port + "/open-banking/v3.1.4/events")
                .body(obEventPolling)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .asObject(OBEventPollingResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    private void mockAccessTokenVerification(String jws) throws ParseException, InvalidTokenException, IOException {
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
    }
}
