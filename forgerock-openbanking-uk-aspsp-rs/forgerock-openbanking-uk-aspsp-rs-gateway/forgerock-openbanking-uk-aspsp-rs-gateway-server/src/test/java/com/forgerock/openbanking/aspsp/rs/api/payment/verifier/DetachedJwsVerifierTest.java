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
package com.forgerock.openbanking.aspsp.rs.api.payment.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.DirectorySoftwareStatement;
import com.forgerock.openbanking.model.DirectorySoftwareStatementOpenBanking;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * A unit test for {@link DetachedJwsVerifier} which does not require a SpringBoot context to fire up.
 */
@RunWith(MockitoJUnitRunner.class)
public class DetachedJwsVerifierTest {

    @Mock
    private TppStoreService tppStoreService;

    @Mock
    private CryptoApiClient cryptoApiClient;

    @InjectMocks
    private DetachedJwsVerifier detachedJwsVerifier;

    @Test
    public void shouldVerifyDetachedJwsGivenVersionBefore3_1_4AndB64HeaderIsFalse() throws Exception {
        // Given
        String detachedJws = "eyJiNjQiOmZhbHNlLCJodHRwOi8vb3BlbmJhbmtpbmcub3JnLnVrL2lhdCI6MTU5MjE2NDIzMSwiaHR0cDovL29wZW5iYW5raW5nLm9yZy51ay90YW4iOiJvcGVuYmFua2luZy5vcmcudWsiLCJodHRwOi8vb3BlbmJhbmtpbmcub3JnLnVrL2lzcyI6IjViMWQ5NzFmLTJmYWEtNDk3ZC1hZDFjLTg3ZjIyY2E4ZjA5MyIsImFsZyI6IlBTMjU2Iiwia2lkIjoiU3dkSGFPOGxZSElzRmx1TjNtZDA2WUk1b1ljIiwiY3JpdCI6WyJiNjQiLCJodHRwOi8vb3BlbmJhbmtpbmcub3JnLnVrL2lhdCIsImh0dHA6Ly9vcGVuYmFua2luZy5vcmcudWsvdGFuIiwiaHR0cDovL29wZW5iYW5raW5nLm9yZy51ay9pc3MiXX0..DQY57jqzjckHtOhw7r2qJI3V_gB4B7g53iUxz_TFnA8LE-jJf3ABe97Tx8aeJZt2hs3N3x5JYL2Dja_WghbV4vGVyxQzNcmIq9WcAdYHRbFZko_9yFYbiJ3MopB9mKtKeZ1cXMVwfH8MhVlb23IhL0e0UStXjmi54aTziFxvEutBcmxaU8K7cIR8AB4RK5q4eqXpvFmftoKtqr_naNPTLgtaq9LwUd8-ptvdm3rfls58JODaruuKUxFk1YX5tdjtCpMaDwt1VYIjVZvlS7IqVa2jGm-0REvDZK1z0HzyrUxdP2FUQldCPRP3wohOZ7ZyW7Dj3yxoo5ijJPIqx3yNRw";
        HttpServletRequest request = setupHttpServletRequestMock();
        Authentication principal = setupUserPrincipalMock();
        setupMocksForValidJws();

        // When
        detachedJwsVerifier.verifyDetachedJws(detachedJws, OBVersion.v3_1_3, request, principal);

        // Then No Exception is thrown
    }

    @Test
    public void shouldVerifyDetachedJwsGivenVersion3_1_4AndB64HeaderIsMissing() throws Exception {
        // Given
        String detachedJws = "eyJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL2lhdCI6MTU5ODQzNjcwOCwiaHR0cDpcL1wvb3BlbmJhbmtpbmcub3JnLnVrXC90YW4iOiJvcGVuYmFua2luZy5vcmcudWsiLCJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL2lzcyI6Imh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvaWF0IiwiY3JpdCI6WyJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL2lhdCIsImh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvdGFuIiwiaHR0cDpcL1wvb3BlbmJhbmtpbmcub3JnLnVrXC9pc3MiXSwiYWxnIjoiUFMyNTYiLCJraWQiOiJ0X0l1NnhYWUV0Mnhod01Bc19ybGFjR3hrRVkifQ..cxZkqGmnxApJcU8oKgNP3PVXhAVtO37ULnCIaNo6ayZrbaQp_6u4Ap4mTXOCvPtl6AfE_SF89xcAqipJV6l_hsOL4UKrmmcT5TNgXnHTOGFwx1lCrdFl0dZWXYvyT4WUctc3laLLmrQjyAfZqsmScT9b63ewx6R6aJ6qwe171OOFVSSFbpKVLXkPevcIltdmpX9rn_m_6nVYRrzRG4eeaTYAmd-nTZxHiJ0FWJ348G4y8E9WW7so4fwooYfoAjWq716ZTwNC7iEsJuGO6X8JpQYn66bYlYSeiyV-q41V5cu9R_QmAwKs2leYsB34YiHp6VteilcmyF9H19zRTkyTeg";
        HttpServletRequest request = setupHttpServletRequestMock();
        Authentication principal = setupUserPrincipalMock();
        setupMocksForValidJws();

        // When
        detachedJwsVerifier.verifyDetachedJws(detachedJws, OBVersion.v3_1_4, request, principal);

        // Then No Exception is thrown
    }

    @Test
    public void shouldFailToVerifyDetachedJwsGivenVerificationEnabledAndJwsIsNull() throws IOException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        Authentication principal = mock(Authentication.class);

        // When
        OBErrorException exception = catchThrowableOfType(() -> detachedJwsVerifier.verifyDetachedJws(null, OBVersion.v3_1_3, request, principal), OBErrorException.class);

        // Then
        assertThat(exception).hasMessage("Invalid detached signature null. Reason: Not provided");
    }

    @Test
    public void shouldFailToVerifyB64HeaderGivenVersionBefore3_1_4AndB64HeaderIsTrue() throws Exception {
        // Given
        String detachedJws = "eyJiNjQiOnRydWUsImh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvaWF0IjoxNTk4NDMwMDA4LCJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL3RhbiI6Im9wZW5iYW5raW5nLm9yZy51ayIsImNyaXQiOlsiYjY0IiwiaHR0cDpcL1wvb3BlbmJhbmtpbmcub3JnLnVrXC9pYXQiLCJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL3RhbiIsImh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvaXNzIl0sImtpZCI6InRfSXU2eFhZRXQyeGh3TUFzX3JsYWNHeGtFWSIsImh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvaXNzIjoiaHR0cDpcL1wvb3BlbmJhbmtpbmcub3JnLnVrXC9pYXQiLCJhbGciOiJQUzI1NiJ9..yfhofJGNJfseVOEhCKanjxHlxlnMCdBKOy9HQFvMf7ZmEpkp2DiKKHJeK1LzDHYOo6WtkImIWwuTS3VvzBPrn7-z83CqM-BHZRzI-_E2I7EzaOzr8We4PtBVDk4wgwSxZW5Q0MPM-WcKgAMPskqrCVXMHLce2AcVsK6bivpi8mdSlA0rVj5FXhXw75-_fuWz8-2GY4xNF0h5YH7Tk4qpQsdpFhgpiagT29ujDcX46g5rF9mA8hUWqtJQE5yoF64S_lBUf4c_R1K1NyG5IwT-GbIoECF-epK5ybNLuD_ZfTfLcWVLI8rav0dRpiI0rdg5-upuB94H-npx1k1KsRXqSA";
        HttpServletRequest request = setupHttpServletRequestMock();
        Authentication principal = mock(Authentication.class);

        // When
        OBErrorException exception = catchThrowableOfType(() -> detachedJwsVerifier.verifyDetachedJws(detachedJws, OBVersion.v3_1_3, request, principal), OBErrorException.class);

        // Then
        assertThat(exception).hasMessage("Invalid detached signature " + detachedJws + ". Reason: b64 claim header not set to false");
    }

    @Test
    public void shouldFailToVerifyB64HeaderGivenVersionBefore3_1_4AndB64HeaderIsNonBoolean() throws ParseException, IOException, OBErrorException {
        // Given
        String detachedJws = "eyJiNjQiOiJub3RfZmFsc2UiLCJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL2lhdCI6MTU5ODQzNjgxMiwiaHR0cDpcL1wvb3BlbmJhbmtpbmcub3JnLnVrXC90YW4iOiJvcGVuYmFua2luZy5vcmcudWsiLCJjcml0IjpbImI2NCIsImh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvaWF0IiwiaHR0cDpcL1wvb3BlbmJhbmtpbmcub3JnLnVrXC90YW4iLCJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL2lzcyJdLCJraWQiOiJ0X0l1NnhYWUV0Mnhod01Bc19ybGFjR3hrRVkiLCJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL2lzcyI6Imh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvaWF0IiwiYWxnIjoiUFMyNTYifQ..G3SB5PVYpdeh9G_ihr-WKVb0JZPERG6AkvgmprD7NfrXnyiOYNowJzAPyIB4AqEZepzAxUsynL5yYBkCKT411YUjj7BcWnwDVeUeohoBxGIx3dM15Jz4KTaVS6qepNFfigwuovhO9avg498xKwOeLUULrRPJ9Er2Sy5h52UUV2mdJe_7xxzC1scET49hYqwdwrEaseN0HoCUno6-93rx7SSa6Btcz-bnTu6erLB1PUsTHB9pRzauxpf6AZ2YwC9a8lu4z0sz1hb6Y5RqUgToXTTj-MMl8win65WNV3puMmhuPIQI4Ij6iYwiC32qRyipfaqspfpp7s9kq_EMw6-Wrw";
        HttpServletRequest request = setupHttpServletRequestMock();
        Authentication principal = mock(Authentication.class);

        //When
        OBErrorException exception = catchThrowableOfType(() -> detachedJwsVerifier.verifyDetachedJws(detachedJws, OBVersion.v3_1_3, request, principal), OBErrorException.class);

        // Then
        assertThat(exception).hasMessage("Invalid detached signature " + detachedJws + ". Reason: Invalid JWS header: Unexpected type of JSON object member with key \"b64\"");
    }

    @Test
    public void shouldFailToVerifyB64HeaderGivenVersionBefore3_1_4AndB64HeaderIsMissing() throws Exception {
        // Given
        String detachedJws = "eyJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL2lhdCI6MTU5ODQzNjcwOCwiaHR0cDpcL1wvb3BlbmJhbmtpbmcub3JnLnVrXC90YW4iOiJvcGVuYmFua2luZy5vcmcudWsiLCJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL2lzcyI6Imh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvaWF0IiwiY3JpdCI6WyJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL2lhdCIsImh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvdGFuIiwiaHR0cDpcL1wvb3BlbmJhbmtpbmcub3JnLnVrXC9pc3MiXSwiYWxnIjoiUFMyNTYiLCJraWQiOiJ0X0l1NnhYWUV0Mnhod01Bc19ybGFjR3hrRVkifQ..cxZkqGmnxApJcU8oKgNP3PVXhAVtO37ULnCIaNo6ayZrbaQp_6u4Ap4mTXOCvPtl6AfE_SF89xcAqipJV6l_hsOL4UKrmmcT5TNgXnHTOGFwx1lCrdFl0dZWXYvyT4WUctc3laLLmrQjyAfZqsmScT9b63ewx6R6aJ6qwe171OOFVSSFbpKVLXkPevcIltdmpX9rn_m_6nVYRrzRG4eeaTYAmd-nTZxHiJ0FWJ348G4y8E9WW7so4fwooYfoAjWq716ZTwNC7iEsJuGO6X8JpQYn66bYlYSeiyV-q41V5cu9R_QmAwKs2leYsB34YiHp6VteilcmyF9H19zRTkyTeg";
        HttpServletRequest request = setupHttpServletRequestMock();
        Authentication principal = mock(Authentication.class);

        // When
        OBErrorException exception = catchThrowableOfType(() -> detachedJwsVerifier.verifyDetachedJws(detachedJws, OBVersion.v3_1_3, request, principal), OBErrorException.class);

        // Then
        assertThat(exception).hasMessage("Invalid detached signature " + detachedJws + ". Reason: b64 claim header not set to false");
    }

    @Test
    public void shouldFailToVerifyB64HeaderGivenVersion3_1_4AndB64HeaderIsPresent() throws Exception {
        // Given
        String detachedJws = "eyJiNjQiOnRydWUsImh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvaWF0IjoxNTk4NDMwMDA4LCJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL3RhbiI6Im9wZW5iYW5raW5nLm9yZy51ayIsImNyaXQiOlsiYjY0IiwiaHR0cDpcL1wvb3BlbmJhbmtpbmcub3JnLnVrXC9pYXQiLCJodHRwOlwvXC9vcGVuYmFua2luZy5vcmcudWtcL3RhbiIsImh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvaXNzIl0sImtpZCI6InRfSXU2eFhZRXQyeGh3TUFzX3JsYWNHeGtFWSIsImh0dHA6XC9cL29wZW5iYW5raW5nLm9yZy51a1wvaXNzIjoiaHR0cDpcL1wvb3BlbmJhbmtpbmcub3JnLnVrXC9pYXQiLCJhbGciOiJQUzI1NiJ9..yfhofJGNJfseVOEhCKanjxHlxlnMCdBKOy9HQFvMf7ZmEpkp2DiKKHJeK1LzDHYOo6WtkImIWwuTS3VvzBPrn7-z83CqM-BHZRzI-_E2I7EzaOzr8We4PtBVDk4wgwSxZW5Q0MPM-WcKgAMPskqrCVXMHLce2AcVsK6bivpi8mdSlA0rVj5FXhXw75-_fuWz8-2GY4xNF0h5YH7Tk4qpQsdpFhgpiagT29ujDcX46g5rF9mA8hUWqtJQE5yoF64S_lBUf4c_R1K1NyG5IwT-GbIoECF-epK5ybNLuD_ZfTfLcWVLI8rav0dRpiI0rdg5-upuB94H-npx1k1KsRXqSA";
        HttpServletRequest request = setupHttpServletRequestMock();
        Authentication principal = mock(Authentication.class);

        // When
        OBErrorException exception = catchThrowableOfType(() -> detachedJwsVerifier.verifyDetachedJws(detachedJws, OBVersion.v3_1_4, request, principal), OBErrorException.class);

        // Then
        assertThat(exception).hasMessage("Invalid detached signature " + detachedJws + ". Reason: b64 claim header must not be present");
    }



    private HttpServletRequest setupHttpServletRequestMock() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletInputStream servletInputStream = new DelegatingServletInputStream(new ByteArrayInputStream("{}".getBytes()));
        given(request.getInputStream()).willReturn(servletInputStream);
        return request;
    }

    private Authentication setupUserPrincipalMock() {
        Authentication principal = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getUsername()).willReturn("username");
        given(principal.getPrincipal()).willReturn(userDetails);
        return principal;
    }

    private void setupMocksForValidJws() throws ParseException, InvalidTokenException, IOException {
        // String ssa = "{\"org_jwks_endpoint\":\"TODO\",\"software_mode\":\"TEST\",
        // \"software_redirect_uris\":[],\"org_status\":\"Active\",\"software_client_id\":\"5f98223fc10e5100103e2c5a\",
        // \"iss\":\"ForgeRock\",
        // \"software_jwks_endpoint\":\"https:\\/\\/service.directory.dev-ob.forgerock.financial:8074\\/api\\/software-statement\\/5f98223fc10e5100103e2c5a\\/application\\/jwk_uri\",
        // \"software_id\":\"5f98223fc10e5100103e2c5a\",\"org_contacts\":[],\"ob_registry_tos\":\"https:\\/\\/directory.dev-ob.forgerock.financial:8074\\/tos\\/\",
        // \"org_id\":\"5f980f21c10e5100103e2c52\",\"software_jwks_revoked_endpoint\":\"TODO\",\"software_roles\":[\"PISP\",\"CBPII\",\"DATA\",\"AISP\"],\"exp\":1604410559,\"org_name\":\"Anonymous\",\"org_jwks_revoked_endpoint\":\"TODO\",\"iat\":1603805759,\"jti\":\"b4934f04-321d-4d85-9cea-ea6015ae4372\"}";
        DirectorySoftwareStatement ssa = DirectorySoftwareStatementOpenBanking.builder()
            .org_jwks_endpoint("TODO")
            .software_mode("TEST")
            .software_redirect_uris(List.of())
            .org_status("Active")
            .software_client_id("5f98223fc10e5100103e2c5a")
            .iss("ForgeRock")
            .software_jwks_endpoint("https://service.directory.dev-ob.forgerock.financial:8074/api/software-statement/5f98223fc10e5100103e2c5a/application/jwk_uri")
            .software_id("5f98223fc10e5100103e2c5a")
            .org_contacts(List.of())
            .build();


        Tpp tpp = mock(Tpp.class);
        given(tppStoreService.findByClientId(anyString())).willReturn(Optional.of(tpp));
        OIDCRegistrationResponse oidcRegistrationResponse = mock(OIDCRegistrationResponse.class);
        given(tpp.getRegistrationResponse()).willReturn(oidcRegistrationResponse);
        given(tpp.getSsa()).willReturn(ssa);
        given(oidcRegistrationResponse.getJwks()).willReturn(null);
        given(oidcRegistrationResponse.getJwks_uri()).willReturn(null);
        given(cryptoApiClient.validateDetachedJWS(any(), any(), any(), any(), any())).willReturn(null);
    }

}
