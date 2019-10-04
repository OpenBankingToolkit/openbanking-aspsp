/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.services.UserProfileService;
import com.forgerock.openbanking.commons.auth.Authenticator;
import com.forgerock.openbanking.commons.model.openbanking.IntentType;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.commons.model.openbanking.v1_1.payment.FRPaymentSetup1;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRDomesticConsent2;
import com.forgerock.openbanking.commons.model.rcs.consentdetails.DomesticPaymentConsentDetails;
import com.forgerock.openbanking.commons.model.rcs.consentdetails.SinglePaymentConsentDetails;
import com.forgerock.openbanking.commons.services.store.payment.DomesticPaymentService;
import com.forgerock.openbanking.commons.services.store.payment.SinglePaymentService;
import com.forgerock.openbanking.core.services.ApplicationApiClientImpl;
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
import uk.org.openbanking.datamodel.payment.*;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;

import static com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.JwtTestHelper.*;
import static com.forgerock.openbanking.integration.test.support.Authentication.mockAuthentication;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private Authenticator authenticator;

    @MockBean
    public ApplicationApiClientImpl applicationApiClientImpl;

    @MockBean
    private UserProfileService userProfileService;

    @MockBean
    private SinglePaymentService singlePaymentService;

    @MockBean
    private DomesticPaymentService domesticPaymentService;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
        mockAuthentication(authenticator, "PISP");
        when(userProfileService.getProfile(any(), anyString(), anyString())).thenReturn(ImmutableMap.of("id", USER_ID));
    }

    @Test
    public void getConsentDetailsForSinglePayment() throws Exception {
        // Given
        FRPaymentSetup1 payment = FRPaymentSetup1.builder()
                .pispId(PISP_ID)
                .pispName(PISP_NAME)
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .paymentSetupRequest(new OBPaymentSetup1().data(new OBPaymentDataSetup1().initiation(new OBInitiation1())))
                .build();
        payment.getInitiation().setInstructedAmount(new OBActiveOrHistoricCurrencyAndAmount());
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
        FRDomesticConsent2 payment = FRDomesticConsent2.builder()
                .pispId(PISP_ID)
                .pispName(PISP_NAME)
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .domesticConsent(new OBWriteDomesticConsent2().data(new OBWriteDataDomesticConsent2().initiation(new OBDomestic2())))
                .build();
        payment.getInitiation().setInstructedAmount(new OBActiveOrHistoricCurrencyAndAmount());
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
        assertThat(resp.getPispName()).isEqualTo(PISP_NAME);
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