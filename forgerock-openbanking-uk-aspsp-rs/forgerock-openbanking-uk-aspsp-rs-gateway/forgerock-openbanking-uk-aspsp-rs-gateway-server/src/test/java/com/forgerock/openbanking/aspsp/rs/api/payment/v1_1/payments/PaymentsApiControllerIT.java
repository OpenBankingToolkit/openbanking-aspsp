/**
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
package com.forgerock.openbanking.aspsp.rs.api.payment.v1_1.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.core.services.CryptoApiClientImpl;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.oidc.services.UserInfoService;
import com.github.jsonzou.jmockdata.JMockData;
import com.nimbusds.jwt.SignedJWT;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
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
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetupResponse1;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentsApiControllerIT {

    private static final String IDEMPOTENCY_KEY = UUID.randomUUID().toString();

    @LocalServerPort
    private int port;

    @Autowired
    private RSConfiguration rsConfiguration;
    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private UserInfoService userInfoService;


    @MockBean(name="amResourceServerService") // Required to avoid Spring auto-wiring exception
    private AMResourceServerService amResourceServerService;
    @MockBean
    private RsStoreGateway rsStoreGateway;
    @Autowired
    private SpringSecForTest springSecForTest;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void createSinglePaymentShouldReturnCreated() throws Exception {
        // Given
        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBPaymentSetupResponse1 paymentSetupResponse = defaultPaymentResponse();
        OBPaymentSetup1 request = defaultPaymentSetupRequest(paymentSetupResponse);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(paymentSetupResponse));

        // When
        HttpResponse<OBPaymentSetupResponse1> response = Unirest.post("https://rs-api:" + port + "/open-banking/v2.0/payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(OBPaymentSetupResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(paymentSetupResponse);
    }

    @Test
    public void createPaymentShouldBeForbiddenWhenHavingAISPPermission() throws Exception {
        // Given
        String jws = jws("payments");
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        OBPaymentSetupResponse1 paymentSetupResponse = defaultPaymentResponse();
        OBPaymentSetup1 request = defaultPaymentSetupRequest(paymentSetupResponse);

        // When
        HttpResponse<OBPaymentSetupResponse1> response = Unirest.post("https://rs-api:" + port + "/open-banking/v2.0/payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(OBPaymentSetupResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void getPaymentShouldBeForbiddenWhenHavingAISPPermission() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);

        // When
        HttpResponse<JsonNode> response = Unirest.get("https://rs-api:" + port + "/open-banking/v2.0/payments/1")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, IDEMPOTENCY_KEY)
                .asJson();

        // Then
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void createSinglePayment_noAuthHeader_401() throws Exception {
        // Given
        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
        OBPaymentSetupResponse1 paymentSetupResponse = defaultPaymentResponse();
        OBPaymentSetup1 request = defaultPaymentSetupRequest(paymentSetupResponse);
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(paymentSetupResponse));

        // When
        HttpResponse<OBPaymentSetupResponse1> response = Unirest.post("https://rs-api:" + port + "/open-banking/v2.0/payments")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, IDEMPOTENCY_KEY)
                .header(OBHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .body(request)
                .asObject(OBPaymentSetupResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(401);
    }


    private OBPaymentSetup1 defaultPaymentSetupRequest(OBPaymentSetupResponse1 paymentSetupResponse) {
        OBPaymentSetup1 request = JMockData.mock(OBPaymentSetup1.class);
        request.getData().initiation(paymentSetupResponse.getData().getInitiation());
        request.getRisk().deliveryAddress(paymentSetupResponse.getRisk().getDeliveryAddress());
        request.getRisk().merchantCategoryCode(paymentSetupResponse.getRisk().getMerchantCategoryCode());
        return request;
    }

    private OBPaymentSetupResponse1 defaultPaymentResponse() {
        OBPaymentSetupResponse1 paymentSetupResponse = JMockData.mock(OBPaymentSetupResponse1.class);
        paymentSetupResponse.getData().getInitiation().getInstructedAmount().setCurrency("GBP");
        paymentSetupResponse.getData().getInitiation().getInstructedAmount().setAmount("10.00");
        paymentSetupResponse.getRisk().getDeliveryAddress().setCountrySubDivision(Arrays.asList("South Gloucestershire"));
        paymentSetupResponse.getRisk().getDeliveryAddress().setAddressLine(Collections.emptyList());
        paymentSetupResponse.getRisk().getDeliveryAddress().setCountry("GB");
        paymentSetupResponse.getRisk().setMerchantCategoryCode("ABCD");
        paymentSetupResponse.getData().setCreationDateTime(new DateTime().withMillisOfSecond(0));
        paymentSetupResponse.getMeta().setFirstAvailableDateTime(new DateTime().withMillisOfSecond(0));
        paymentSetupResponse.getMeta().setLastAvailableDateTime(new DateTime().withMillisOfSecond(0));
        return paymentSetupResponse;
    }

}