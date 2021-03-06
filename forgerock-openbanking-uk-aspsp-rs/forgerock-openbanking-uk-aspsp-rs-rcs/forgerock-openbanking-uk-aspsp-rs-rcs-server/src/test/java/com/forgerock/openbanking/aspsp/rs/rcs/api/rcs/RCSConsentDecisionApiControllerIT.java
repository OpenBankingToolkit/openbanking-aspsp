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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.services.UserProfileService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRPaymentSetup;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
import com.forgerock.openbanking.common.model.rcs.consentdetails.DomesticPaymentConsentDetails;
import com.forgerock.openbanking.common.model.rcs.consentdetails.SinglePaymentConsentDetails;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.payment.DomesticPaymentService;
import com.forgerock.openbanking.common.services.store.payment.SinglePaymentService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.core.services.ApplicationApiClientImpl;
import com.forgerock.openbanking.model.Tpp;
import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.TypeReference;
import com.google.common.collect.ImmutableMap;
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
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;

import java.util.List;
import java.util.Optional;

import static com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.JwtTestHelper.claimsSetToRsa256Jwt;
import static com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.JwtTestHelper.jsonStringToClaimsSet;
import static com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.JwtTestHelper.serializeJwt;
import static com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.JwtTestHelper.signJwt;
import static com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.JwtTestHelper.utf8FileToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RCSConsentDecisionApiControllerIT {
    private static final String CONTENT_TYPE_JWT = "application/jwt; charset=utf-8";
    private static final String MOCK_COOKIE = "iPlanetDirectoryPro=1jd35VBFkd9ZAr51-1ZqqhVLpJk.*AAJTSQACMDEAAlNLABxNNEJBUFVieGZ4NkpuSE9JZUtJelVLS242T2M9AAJTMQAA*";

    private static final String PISP_NAME = "pispTester";
    private static final String PISP_ID = "98311305-1c4f-4ffb-8af4-90c9b220e365";
    private static final String USER_ID = "demo";

    private static final String EXPECTED_DECISION_API_URI = "/api/rcs/consent/decision/";

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    public ApplicationApiClientImpl applicationApiClientImpl;

    @MockBean
    private UserProfileService userProfileService;

    @MockBean
    private SinglePaymentService singlePaymentService;

    @MockBean
    private DomesticPaymentService domesticPaymentService;

    @MockBean
    private AccountStoreService accountsService;
    @MockBean
    private TppStoreService tppStoreService;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
        when(userProfileService.getProfile(any(), anyString(), anyString())).thenReturn(ImmutableMap.of("id", USER_ID));
        when(accountsService.getAccountWithBalances(any())).thenReturn(JMockData.mock(new TypeReference<List<AccountWithBalance>>() {}));
        given(tppStoreService.findById(any())).willReturn(Optional.of(Tpp.builder().clientId(PISP_ID).build()));

    }

    @Test
    public void getConsentDetailsForSinglePayment() throws Exception {
        // Given
        FRWriteDomesticConsentData writeDomesticConsentData = FRWriteDomesticConsentData.builder()
                .initiation(FRWriteDomesticDataInitiation.builder().build())
                .build();
        FRPaymentSetup payment = FRPaymentSetup.builder()
                .pispId(PISP_ID)
                .pispName(PISP_NAME)
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .paymentSetupRequest(FRWriteDomesticConsent.builder().data(writeDomesticConsentData).build())
                .build();
        payment.getInitiation().setInstructedAmount(FRAmount.builder().build());
        when(singlePaymentService.getPayment(any())).thenReturn(payment);
        String signedJwtEncoded = toEncodedSignedTestJwt("jwt/singlePaymentConsentRequestPayload.json");

        // When
        HttpResponse<SinglePaymentConsentDetails> response = Unirest.post("https://rs-rcs:" + port + "/api/rcs/consent/details/")
                .header(OBHeaders.CONTENT_TYPE, CONTENT_TYPE_JWT)
                .header("Cookie", MOCK_COOKIE)
                .body(signedJwtEncoded)
                .asObject(SinglePaymentConsentDetails.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);

        SinglePaymentConsentDetails resp = response.getBody();
        assertThat(resp.getClientId()).isEqualTo(PISP_ID);
        assertThat(resp.getPispName()).isEqualTo(PISP_NAME);
        assertThat(resp.getUsername()).isEqualTo(USER_ID);
        assertThat(resp.getIntentType()).isEqualTo(IntentType.PAYMENT_SINGLE_REQUEST);
        assertThat(resp.getDecisionAPIUri()).isEqualTo(EXPECTED_DECISION_API_URI);
    }

    @Test
    public void getConsentDetailsForDomesticPayment() throws Exception {
        // Given
        FRWriteDomesticConsentData writeDomesticConsentData = FRWriteDomesticConsentData.builder()
                .initiation(FRWriteDomesticDataInitiation.builder().build())
                .build();
        FRDomesticConsent payment = FRDomesticConsent.builder()
                .pispId(PISP_ID)
                .pispName(PISP_NAME)
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .domesticConsent(FRWriteDomesticConsent.builder().data(writeDomesticConsentData).build())
                .build();
        payment.getInitiation().setInstructedAmount(FRAmount.builder().build());
        when(domesticPaymentService.getPayment(any())).thenReturn(payment);
        String signedJwtEncoded = toEncodedSignedTestJwt("jwt/domesticPaymentConsentRequestPayload.json");

        // When
        HttpResponse<DomesticPaymentConsentDetails> response = Unirest.post("https://rs-rcs:" + port + "/api/rcs/consent/details/")
                .header(OBHeaders.CONTENT_TYPE, CONTENT_TYPE_JWT)
                .header("Cookie", MOCK_COOKIE)
                .body(signedJwtEncoded)
                .asObject(DomesticPaymentConsentDetails.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);

        DomesticPaymentConsentDetails resp = response.getBody();
        assertThat(resp.getClientId()).isEqualTo(PISP_ID);
        assertThat(resp.getUsername()).isEqualTo(USER_ID);
        assertThat(resp.getIntentType()).isEqualTo(IntentType.PAYMENT_DOMESTIC_CONSENT);
        assertThat(resp.getDecisionAPIUri()).isEqualTo(EXPECTED_DECISION_API_URI);
    }

    private static String toEncodedSignedTestJwt(final String jwtPayloadFilePath) {
        return utf8FileToString
                .andThen(jsonStringToClaimsSet)
                .andThen(claimsSetToRsa256Jwt)
                .andThen(signJwt)
                .andThen(serializeJwt)
                .apply(jwtPayloadFilePath);
    }

}