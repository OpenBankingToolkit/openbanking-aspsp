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
package com.forgerock.openbanking.common.services;

import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.common.error.exception.AccessTokenReWriteException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.oidc.AccessTokenResponse;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;




@RunWith(MockitoJUnitRunner.class)
public class JwtOverridingServiceTest {

    @Mock
    JwtOverridingService jwtOverridingService;
    @Mock
    CryptoApiClient cryptoApiClient;
    AMOpenBankingConfiguration amOpenBankingConfiguration;

    @Before
    public void setUp() throws Exception {
        this.amOpenBankingConfiguration = new AMOpenBankingConfiguration();
        this.jwtOverridingService = new JwtOverridingService(cryptoApiClient, amOpenBankingConfiguration);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldRewriteAccessTokenResponseIdToken() throws AccessTokenReWriteException {
        // Given
        this.amOpenBankingConfiguration.issuerId = "acme bank Ltd";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "https://location");
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setId_token("eyJraWQiOiIzYmEwYjRjMGRjOGNiOTg3YTE5ZjNiNDgwZDFiODlmMWNiODA4MDI5IiwiYWxnIjoi" +
                "UFMyNTYifQ.eyJzdWIiOiJmMjU5MDlmZC01MzEwLTRiNjgtYTI5Yy0zM2QyYjE1OWMzNzUiLCJhdWQiOiJodHRwczpcL1wvYXMuY" +
                "XNwc3AuZGV2LW9iLmZvcmdlcm9jay5maW5hbmNpYWw6ODA3NFwvb2F1dGgyIiwiaXNzIjoiZjI1OTA5ZmQtNTMxMC00YjY4LWEyO" +
                "WMtMzNkMmIxNTljMzc1IiwiZXhwIjoxNjIxNDEwODU1LCJpYXQiOjE2MjE0MTA1NTUsImp0aSI6IjgzMDEwYzU4LTczNDYtNGJjM" +
                "S05OTVhLTg4NDI4NTRhMGEzOSJ9.PTnkBmKeWhT1kLlOkVJGrBKJHBksY16ynzP1KvHzwcYFMm6ixNd4iDc2aosSI7vH9ufamnR0" +
                "O9UoZzi3uWflWq9B2ah8m8rNNWYr-Y3B4Ev_nrKDDgozWZ_u0PY5Tzau0TVcVYyRkNTIXoVk-hYVLjuxZiMPQm7Ceid-KemK6Y04" +
                "UAiwGZou4KIKWXjGySOBlJEZO42LTZk0UKAC8AeXlQCo_QSzOchrD8wmJOeCH59VooZku5eubviKZ1UKo0hkDxLg13IjER-dansJ" +
                "tHqCcDCVV9n04Rvvs88yvvCWTcRsdEpUUg-e-sGVUR2ER9UyQMyY012_fA-R7W8H4jv_iQ");
        ResponseEntity responseEntity = new ResponseEntity(accessTokenResponse, httpHeaders, HttpStatus.OK);
        when(this.cryptoApiClient.signClaims(anyString(), any(JWTClaimsSet.class), anyBoolean())).thenReturn(
                "RewrittenIdToken");

        // Then
        ResponseEntity<AccessTokenResponse> rewrittenResponseEntity =
                this.jwtOverridingService.rewriteAccessTokenResponseIdToken(responseEntity);

        // When
        assertThat(rewrittenResponseEntity.getBody().getId_token()).isEqualTo("RewrittenIdToken");
    }

    @Test
    public void shouldFailWhenStatusNotOK_rewriteAccessTokenResponseIdToken() {
        // Given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "https://location");
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.FOUND);


        // Then
        AccessTokenReWriteException accessTokenReWriteException = catchThrowableOfType(
                ()->this.jwtOverridingService.rewriteAccessTokenResponseIdToken(responseEntity),
                AccessTokenReWriteException.class);

        // When
        assertThat(accessTokenReWriteException.getMessage()).contains("Expected 200 (OK)");
    }

    @Test
    public void shouldFailWhenNoBody_rewriteAccessTokenResponseIdToken() {
        // Given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "https://location");
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);


        // Then
        AccessTokenReWriteException accessTokenReWriteException = catchThrowableOfType(
                ()->this.jwtOverridingService.rewriteAccessTokenResponseIdToken(responseEntity),
                AccessTokenReWriteException.class);

        // When
        assertThat(accessTokenReWriteException.getMessage()).contains("responseEntity has no body");
    }

    @Test
    public void shouldReturnOrignialWhenBodyHasNoIdToken_rewriteAccessTokenResponseIdToken() throws
            AccessTokenReWriteException {
        // Given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "https://location");
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        ResponseEntity responseEntity = new ResponseEntity(accessTokenResponse, httpHeaders, HttpStatus.OK);

        // Then
        ResponseEntity result  = jwtOverridingService.rewriteAccessTokenResponseIdToken(responseEntity);

        // When
        assertThat(result).isSameAs(responseEntity);
    }

    @Test
    public void shouldFailWhenBodyHasInvalidIdToken_rewriteAccessTokenResponseIdToken() {
        // Given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "https://location");
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setId_token("InvalidToken");
        ResponseEntity responseEntity = new ResponseEntity(accessTokenResponse, httpHeaders, HttpStatus.OK);


        // Then
        AccessTokenReWriteException accessTokenReWriteException = catchThrowableOfType(
                ()->this.jwtOverridingService.rewriteAccessTokenResponseIdToken(responseEntity),
                AccessTokenReWriteException.class);

        // When
        assertThat(accessTokenReWriteException.getMessage()).contains("Could not parse id_token");
    }

    @Test
    public void rewriteIdTokenFragmentInLocationHeader() throws AccessTokenReWriteException,
            URISyntaxException {
        // Given
        amOpenBankingConfiguration.issuerId = "acme bank Ltd";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI("https://location#id_token=eyJ0eXAiOiJKV1QiLCJraWQiOiJzUUhYOHlnT3JGcHBsZ09ZZ" +
                "kxpQUNTNzJOMG89IiwiYWxnIjoiUFMyNTYifQ.eyJzdWIiOiJBQUNfZGZkMzI4NGEtMTkxYy00ODA0LTlmZmItNTFlNGRjZTljN" +
                "jkwIiwiYXVkaXRUcmFja2luZ0lkIjoiZmRhYmM2MWEtYmZjOS00OWNlLThmZTUtODczOGQzMGExMmY5LTkyMzE5NyIsImlzcyI6" +
                "Imh0dHBzOi8vYXMuYXNwc3Auc2FuZGJveC5sbG95ZHNiYW5raW5nLmNvbS9vYXV0aDIiLCJ0b2tlbk5hbWUiOiJpZF90b2tlbiI" +
                "sIm5vbmNlIjoiYjc3Y2ZjYzItYmI4Yi00Zjc2LTlhOGYtNGM1N2M5ZjJmNjhiIiwiYWNyIjoidXJuOm9wZW5iYW5raW5nOnBzZD" +
                "I6c2NhIiwiYXVkIjoiNjUwOTE2OTYtOWRmYi00NDIxLWI1MTYtZGUwYzk3ZWI5ODc0IiwiY19oYXNoIjoiczVNUXBjWVZaNmY0c" +
                "WE5QmxsdzZaQSIsIm9wZW5iYW5raW5nX2ludGVudF9pZCI6IkFBQ19kZmQzMjg0YS0xOTFjLTQ4MDQtOWZmYi01MWU0ZGNlOWM2" +
                "OTAiLCJzX2hhc2giOiJ2NWgwTUFUTS13NEVGWXRCcEVtemR3IiwiYXpwIjoiNjUwOTE2OTYtOWRmYi00NDIxLWI1MTYtZGUwYzk" +
                "3ZWI5ODc0IiwiYXV0aF90aW1lIjoxNjE4NTcwODM2LCJyZWFsbSI6Ii9vcGVuYmFua2luZyIsImV4cCI6MTYxODU3ODQ1OCwidG" +
                "9rZW5UeXBlIjoiSldUVG9rZW4iLCJpYXQiOjE2MTg1NzQ4NTh9.VnxIks-Vm56NjhNrxGxlyDczazJj1c5R8n4YVKjsmq10WT0m" +
                "C8nnUuMsuVhqn2k6J2KUqTgvN_JGaxTsAfDVtyIbpieRiuJoNFF6xBTCPArp4V5qm4rff6imKUYTBFj0zzbnacMjA048LkbLWEl" +
                "JhXNbiwUQJ-PbMzkCLRAbH2p5LYJTZUwDKsupD-uLxbkiwCGXy4Oa-yCwEh_nMwQoA1XUdBfhFMf0i4kjg5z6W01x8P4CC04iVb" +
                "gKeqBrwREfknw6aZ2zoGaUYtyaPVhmkrnR3cDGFCKPc5De0TUdI3iSRUIDYOtCKs1M-ZAshdBc0B6A2Vz2aJEOXgo16_yREA"));
        ResponseEntity responseEntity = new ResponseEntity(httpHeaders, HttpStatus.FOUND);

        when(this.cryptoApiClient.signClaims(anyString(), any(JWTClaimsSet.class), anyBoolean())).thenReturn(
                "RewrittenIdToken");


        // When
        ResponseEntity newResponseEntity =
                this.jwtOverridingService.rewriteIdTokenFragmentInLocationHeader(responseEntity);

        // Then
        assertThat(newResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(newResponseEntity.getHeaders().getLocation()).hasFragment("id_token=RewrittenIdToken");
    }

    @Test
    public void shouldFailWhenStatusNotFound_rewriteIdTokenFragmentInLocationHeader() {
        // Given
        amOpenBankingConfiguration.issuerId = "acme bank Ltd";
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

        // When
        AccessTokenReWriteException accessTokenReWriteException = catchThrowableOfType(
                ()->this.jwtOverridingService.rewriteIdTokenFragmentInLocationHeader(responseEntity),
                AccessTokenReWriteException.class );

        // Then
        assertThat(accessTokenReWriteException.getMessage()).contains("does not have a FOUND http status");
    }
}