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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.am.services.UserProfileService;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.singlepayments.SinglePaymentConsentDecisionDelegate;
import com.forgerock.openbanking.aspsp.rs.rcs.services.IntentTypeService;
import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.aspsp.rs.rcs.services.RcsService;
import com.forgerock.openbanking.common.conf.RcsConfiguration;
import com.forgerock.openbanking.common.constants.RCSConstants;
import com.forgerock.openbanking.common.model.rcs.RedirectionAction;
import com.forgerock.openbanking.common.model.rcs.consentdecision.ConsentDecision;
import com.forgerock.openbanking.common.services.JwtOverridingService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.Tpp;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.JwtTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RCSConsentDecisionApiControllerTest {

    @Mock
    private CryptoApiClient cryptoApiClient;
    @Mock
    private RcsService rcsService;
    @Mock
    private RcsConfiguration rcsConfiguration;
    @Mock
    private RCSErrorService rcsErrorService;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private IntentTypeService intentTypeService;
    @Mock
    private TppStoreService tppStoreService;
    @Mock
    private JwtOverridingService jwtOverridingService;

    private final AMOpenBankingConfiguration amOpenBankingConfiguration = new AMOpenBankingConfiguration();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Class under test
    private RCSConsentDecisionApiController consentDecisionApiController;


    @Before
    public void setUp() throws Exception {
        this.consentDecisionApiController = new RCSConsentDecisionApiController(cryptoApiClient,rcsService,
                rcsConfiguration,amOpenBankingConfiguration, rcsErrorService, objectMapper, userProfileService,
                intentTypeService, tppStoreService, jwtOverridingService);

        SinglePaymentConsentDecisionDelegate singlePaymentConsentDecisionDelegate =
                mock(SinglePaymentConsentDecisionDelegate.class);
        when(intentTypeService.getConsentDecision(anyString())).thenReturn(singlePaymentConsentDecisionDelegate);
        when(singlePaymentConsentDecisionDelegate.getTppIdBehindConsent()).thenReturn("TppId");
        when(singlePaymentConsentDecisionDelegate.getUserIDBehindConsent()).thenReturn("username");

        Tpp tpp = mock(Tpp.class);
        Optional<Tpp> isTpp = Optional.of(tpp);
        when(this.tppStoreService.findById(anyString())).thenReturn(isTpp);
        when(tpp.getClientId()).thenReturn("98311305-1c4f-4ffb-8af4-90c9b220e365");

        amOpenBankingConfiguration.userProfileId = "username";
        amOpenBankingConfiguration.endpointUserProfile = "endpointUserProfile";
        amOpenBankingConfiguration.cookieName = "cookieName";
        Map<String, String> profile = new HashMap<String, String>();
        profile.put(amOpenBankingConfiguration.userProfileId, "username");
        when(this.userProfileService.getProfile(anyString(), anyString(), anyString())).thenReturn(profile);

        ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(this.rcsService.sendRCSResponseToAM(anyString(), any(RedirectionAction.class))).thenReturn(responseEntity);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.FOUND);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "https://www.google.com#code=oq62Wr-V0E7cnuIl5rDSwscCyVo&id_token" +
                "=eyJ0eXAiOiJKV1QiLCJraWQiOiJzUUhYOHlnT3JGcHBsZ09ZZkxpQUNTNzJOMG89IiwiYWxnIjoiUFMyNTYifQ.eyJzdWIiOiJ" +
                "BQUNfM2ZkODYwYjktOGZlYS00NWMwLThiNTItNDJkNjM1YWRjYzRjIiwiYXVkaXRUcmFja2luZ0lkIjoiZmRhYmM2MWEtYmZjOS" +
                "00OWNlLThmZTUtODczOGQzMGExMmY5LTIyNTczNDgiLCJpc3MiOiJodHRwczovL2FzLmFzcHNwLnNhbmRib3gubGxveWRzYmFua" +
                "2luZy5jb20vb2F1dGgyIiwidG9rZW5OYW1lIjoiaWRfdG9rZW4iLCJub25jZSI6IjEwZDI2MGJmLWE3ZDktNDQ0YS05MmQ5LTdi" +
                "N2E1ZjA4ODIwOCIsImF1ZCI6IjE3ZTI5NmYyLTNkMDctNDMxMS05ZDAwLTk4ZDY3YjUyNTk4MCIsImNfaGFzaCI6IjB4UDJseGh" +
                "qek1GOTlmakhmel92dWciLCJhY3IiOiJ1cm46b3BlbmJhbmtpbmc6cHNkMjpzY2EiLCJvcGVuYmFua2luZ19pbnRlbnRfaWQiOi" +
                "JBQUNfM2ZkODYwYjktOGZlYS00NWMwLThiNTItNDJkNjM1YWRjYzRjIiwic19oYXNoIjoiWE5TdVJac0pVb1pjdjdZOTBoNFpfU" +
                "SIsImF6cCI6IjE3ZTI5NmYyLTNkMDctNDMxMS05ZDAwLTk4ZDY3YjUyNTk4MCIsImF1dGhfdGltZSI6MTYyMDkxOTc2MiwicmVh" +
                "bG0iOiIvb3BlbmJhbmtpbmciLCJleHAiOjE2MjA5MjM0MDIsInRva2VuVHlwZSI6IkpXVFRva2VuIiwiaWF0IjoxNjIwOTE5ODA" +
                "yfQ.U7zDwtrnE2ocbpcbhOc3f-j6ve0EKoh6S9n8qVH69-zezg0Qcro3PTd59gEA6L0kz5ror6epScSaWy-6rKClCr8sFqA_Fx9" +
                "W5qe5K-WUFTWMtfX2ncIwf-RJzxazzfxkpIrKfVhi6R96qpi7RYSZ3GWUfxBm2za24ZvyUNVR5c9ptlyke5h4Wq1QZkaiHv02VT" +
                "EO2GbumtWIWYfpl84FepZvDm6E6O7K0KCTs8G--jrCnZljwL-qSgWEUkIDja4eaz4KYdZ7-U-CUVzdoMaxhZY5CbM09PRRPsiuy" +
                "vsZ6FVGx6YGHUN-BDIFssy6hpD53Dp2HGbCxZ0unBU50Q9N3w&state=10d260bf-a7d9-444a-92d9-7b7a5f088208");
        URI rewrittenUri = new URI("https://www.google.com#code=oq62Wr-V0E7cnuIl5rDSwscCyVo&id_token" +
                "=re-writtenIdToken");

        HttpHeaders rewrittenHeaders = new HttpHeaders();
        rewrittenHeaders.add("Location", "https://www.google.com#code=oq62Wr-V0E7cnuIl5rDSwscCyVo&id_token=re" +
                        "-writtenIdToken");
        ResponseEntity rewrittenResponseEntity = new ResponseEntity(null, rewrittenHeaders, HttpStatus.FOUND);
        when(jwtOverridingService.rewriteIdTokenFragmentInLocationHeader(any(ResponseEntity.class)))
                .thenReturn(rewrittenResponseEntity);


    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void decisionAccountSharing() throws OBErrorException, JsonProcessingException {
        // Given
        String signedJwtEncoded = toEncodedSignedTestJwt("jwt/singlePaymentConsentRequestPayload.json");
        String ssoToken = "dlkjdsflkjdlsfhlkfdk";
        ConsentDecision consentDecision = new ConsentDecision();
        consentDecision.setDecision(RCSConstants.Decision.ALLOW);
        consentDecision.setConsentJwt(signedJwtEncoded);
        String consentDecisionSerialized = objectMapper.writeValueAsString(consentDecision);

        // When
        ResponseEntity responseEntity = consentDecisionApiController.decisionAccountSharing(consentDecisionSerialized,
                ssoToken);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();

        ObjectMapper objectMapper = new ObjectMapper();
        RedirectionAction redirectAction = (RedirectionAction) responseEntity.getBody();
        assertThat(redirectAction).isNotNull();
        assertThat(redirectAction.getRedirectUri()).contains("re-writtenIdToken");
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