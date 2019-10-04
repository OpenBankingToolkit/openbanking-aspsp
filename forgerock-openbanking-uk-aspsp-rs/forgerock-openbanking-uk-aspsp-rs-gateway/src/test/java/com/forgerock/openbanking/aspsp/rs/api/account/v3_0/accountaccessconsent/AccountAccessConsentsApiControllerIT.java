/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.account.v3_0.accountaccessconsent;

import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountAccessConsentPermittedPermissionsFilter;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.account.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountAccessConsentsApiControllerIT {

    @LocalServerPort
    private int port;
    @Autowired
    private RSConfiguration rsConfiguration;

    @MockBean(name = "cryptoApiClient") // Required to avoid Spring auto-wiring exception
    private CryptoApiClient cryptoApiClient;
    @MockBean
    private RsStoreGateway rsStoreGateway;
    @MockBean
    private AccountAccessConsentPermittedPermissionsFilter accountAccessConsentPermittedPermissionsFilter;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper()).verifySsl(false);
    }

    @Test
    public void createAccountAccessConsent() throws Exception {
        // Given
        String jws = jws("accounts", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        //mockAuthentication(authenticator, "ROLE_AISP");
        mockAccessTokenVerification(jws);
        OBReadConsentResponse1 readConsentResponse = new OBReadConsentResponse1()
                .data(new OBReadConsentResponse1Data()
                .consentId("AISP_3980298093280")
                .status(OBExternalRequestStatus1Code.AWAITINGAUTHORISATION)
                .permissions(Collections.singletonList(OBExternalPermissions1Code.READACCOUNTSBASIC))
                );
        given(rsStoreGateway.toRsStore(any(), any(), any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.CREATED).body(readConsentResponse));
        final OBReadConsent1 obReadConsent = new OBReadConsent1()
                .data(new OBReadData1().permissions(Collections.singletonList(OBExternalPermissions1Code.READACCOUNTSBASIC)))
                .risk(new OBRisk2());

        // When
        HttpResponse<OBReadConsentResponse1> response = Unirest.post("https://rs-api:" + port + "/open-banking/v3.1/aisp/account-access-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, UUID.randomUUID().toString())
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obReadConsent)
                .asObject(OBReadConsentResponse1.class);

        // Then
        verify(accountAccessConsentPermittedPermissionsFilter, times(1)).filter(any());
        assertThat(response.getStatus()).isEqualTo(201);
    }

    @Test
    public void getAccountAccessConsent() throws Exception {

        // Given
        String jws = jws("accounts", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        //mockAuthentication(authenticator, "ROLE_AISP");
        mockAccessTokenVerification(jws);

        OBReadConsentResponse1 readConsentResponse = new OBReadConsentResponse1();
        given(rsStoreGateway.toRsStore(any(), any(), any())).willReturn(ResponseEntity.ok(readConsentResponse));

        // When
        HttpResponse<OBReadConsentResponse1> response = Unirest.get("https://rs-api:" + port + "/open-banking/v3.1/aisp/account-access-consents/100000123")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(OBReadConsentResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);

    }

    private void mockAccessTokenVerification(String jws) throws ParseException, InvalidTokenException, IOException {
        given(cryptoApiClient.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
    }
}