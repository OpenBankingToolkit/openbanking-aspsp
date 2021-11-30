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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_8.vrp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.services.store.vrp.DomesticVrpPaymentConsentService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
import com.github.jsonzou.jmockdata.JMockData;
import com.nimbusds.jwt.SignedJWT;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Ignore;
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
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPRequest;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPResponse;
import uk.org.openbanking.testsupport.vrp.OBDomesticVRPResponseTestDataFactory;

import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static uk.org.openbanking.testsupport.payment.OBRisk1TestDataFactory.aValidOBRisk1;

/**
 * Integration test for {@link DomesticVrpConsentsApiController}
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class DomesticVrpApiControllerIT {

    private static final String HOST = "https://rs-api:";
    private static final String VRP_CONTXT_PATH = "/open-banking/v3.1.8/pisp/domestic-vrps";
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
    @MockBean
    private DomesticVrpPaymentConsentService vrpConsentService;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void createVrpPaymentConsent() throws Exception {
        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBDomesticVRPRequest request = new OBDomesticVRPRequest();
        OBDomesticVRPResponse rsStoreResponse = aValidOBDomesticVRPResponse(request);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(rsStoreResponse));
        Tpp tpp = new Tpp();
        tpp.setAuthorisationNumber("test-tpp");
        given(tppStoreService.findByClientId(any())).willReturn(Optional.of(tpp));
        given(vrpConsentService.getVrpPayment("DVRP_12345")).willReturn(aValidFRDomesticVRPConsent());

        // When
        HttpResponse<OBDomesticVRPResponse> response = Unirest.post(HOST + port + VRP_CONTXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.X_JWS_SIGNATURE, jws)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(OBDomesticVRPResponse.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(rsStoreResponse);
    }

    private FRDomesticVRPConsent aValidFRDomesticVRPConsent(){
        return JMockData.mock(FRDomesticVRPConsent.class);
    }

    private OBDomesticVRPResponse aValidOBDomesticVRPResponse(OBDomesticVRPRequest request) {
        return (new OBDomesticVRPResponse())
                .data(OBDomesticVRPResponseTestDataFactory.aValidOBDomesticVRPResponseData(request))
                .risk(aValidOBRisk1())
                .links(new Links().self(HOST + port + VRP_CONTXT_PATH));
    }
}
