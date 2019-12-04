/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.event.v3_0;


import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.OBRIRole;
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
import uk.org.openbanking.datamodel.event.OBCallbackUrlResponseData1;
import uk.org.openbanking.datamodel.event.OBCallbackUrlsResponse1;
import uk.org.openbanking.datamodel.event.OBCallbackUrlsResponseData1;

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

    @MockBean(name="amResourceServerService") // Required to avoid Spring auto-wiring exception
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
        String jws = jws("accounts", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        mockAccessTokenVerification(jws);
        OBCallbackUrlsResponse1 obCallbackUrlsResponse = new OBCallbackUrlsResponse1();
        obCallbackUrlsResponse.data(new OBCallbackUrlsResponseData1().callbackUrl(Collections.singletonList(new OBCallbackUrlResponseData1().callbackUrlId("123").url("https://localhost").version("v3.0"))));
        given(rsStoreGateway.toRsStore(any(), any(), any())).willReturn(ResponseEntity.ok(obCallbackUrlsResponse));

        // When
        HttpResponse<OBCallbackUrlsResponse1> response = Unirest.get("https://rs-api:" + port + "/open-banking/v3.0/callback-urls")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "Bearer " + jws)
                .asObject(OBCallbackUrlsResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
    }

    private void mockAccessTokenVerification(String jws) throws ParseException, InvalidTokenException, IOException {
        given(amResourceServerService.verifyAccessToken("Bearer " + jws)).willReturn(SignedJWT.parse(jws));
    }
}
