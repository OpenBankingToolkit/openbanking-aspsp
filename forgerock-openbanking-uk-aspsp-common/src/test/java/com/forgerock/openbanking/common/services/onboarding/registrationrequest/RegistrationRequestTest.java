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
package com.forgerock.openbanking.common.services.onboarding.registrationrequest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.common.services.onboarding.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.common.services.onboarding.TppRegistrationService;
import com.forgerock.openbanking.common.services.onboarding.apiclient.ApiClientIdentity;
import com.forgerock.openbanking.common.services.onboarding.registrationrequest.DirectorySoftwareStatementFactory;
import com.forgerock.openbanking.common.services.onboarding.registrationrequest.RegistrationRequest;
import com.forgerock.openbanking.common.services.onboarding.registrationrequest.RegistrationRequestFactory;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationRequestTest {

    @Mock
    private TppRegistrationService tppRegistrationService;
    private DirectorySoftwareStatementFactory softwareStatementFactory;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private RegistrationRequest registrationRequest;

    @Before
    public void setUp() throws DynamicClientRegistrationException {
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OpenBankingDirectoryConfiguration obDirectoryConfig = new OpenBankingDirectoryConfiguration();
        obDirectoryConfig.issuerId = "OpenBanking Ltd";
        this.softwareStatementFactory = new DirectorySoftwareStatementFactory(obDirectoryConfig);
        RegistrationRequestFactory registrationRequestFactory =
                new RegistrationRequestFactory(this.tppRegistrationService, this.softwareStatementFactory, objectMapper);
        String serialisedRegistrationRequestJWT = getValidRegistrationRequestJWTSerialised();
        this.registrationRequest = registrationRequestFactory.getRegistrationRequestFromJwt(serialisedRegistrationRequestJWT);
    }


    @Test
    public void overwriteRegistrationRequestFieldsFromSSAClaims() throws DynamicClientRegistrationException {
        // Given
        ApiClientIdentity clientIdentity = mock(ApiClientIdentity.class);
        // When
        this.registrationRequest.overwriteRegistrationRequestFieldsFromSSAClaims(clientIdentity);
        // Then
        assertThat(registrationRequest.getJwks_uri()).isEqualTo("https://service.directory.dev-ob.forgerock" +
                ".financial:8074/api/software-statement/60c75ba3c450450011efa679/application/jwk_uri");
    }

    private String getValidRegistrationRequestJWTSerialised(){
        return "eyJraWQiOiI4MDBjODBhNzVjOGEwYWQ0Y2FiNzY0NTJlNGY1ZjlkODE0NDFmZjdjIiwiYWxnIjoiUFMyNTYifQ.eyJ0b2tlbl9lbmRwb2ludF9hdXRoX3NpZ25pbmdfYWxnIjoiUFMyNTYiLCJyZXF1ZXN0X29iamVjdF9lbmNyeXB0aW9uX2FsZyI6IlJTQS1PQUVQLTI1NiIsImdyYW50X3R5cGVzIjpbImF1dGhvcml6YXRpb25fY29kZSIsInJlZnJlc2hfdG9rZW4iLCJjbGllbnRfY3JlZGVudGlhbHMiXSwiaXNzIjoiNjBjNzViYTNjNDUwNDUwMDExZWZhNjc5IiwicmVkaXJlY3RfdXJpcyI6WyJodHRwczpcL1wvd3d3Lmdvb2dsZS5jb20iXSwidG9rZW5fZW5kcG9pbnRfYXV0aF9tZXRob2QiOiJwcml2YXRlX2tleV9qd3QiLCJzb2Z0d2FyZV9zdGF0ZW1lbnQiOiJleUpyYVdRaU9pSmhaR015TmpJMU1HWmtNMlV6TmpJMFlqWTJPR014TkRneE4yWXhaVFE1WTJGbU9ESTVNVGhpSWl3aVlXeG5Jam9pVUZNeU5UWWlmUS5leUp2Y21kZmFuZHJjMTlsYm1Sd2IybHVkQ0k2SWxSUFJFOGlMQ0p6YjJaMGQyRnlaVjl0YjJSbElqb2lWRVZUVkNJc0luTnZablIzWVhKbFgzSmxaR2x5WldOMFgzVnlhWE1pT2xzaWFIUjBjSE02WEM5Y0wyZHZiMmRzWlM1amJ5NTFheUpkTENKdmNtZGZjM1JoZEhWeklqb2lRV04wYVhabElpd2ljMjltZEhkaGNtVmZZMnhwWlc1MFgyNWhiV1VpT2lKS1lXMXBaU2R6SUZOdlpuUjNZWEpsSUVGd2NHeHBZMkYwYVc5dUlpd2ljMjltZEhkaGNtVmZZMnhwWlc1MFgybGtJam9pTmpCak56VmlZVE5qTkRVd05EVXdNREV4WldaaE5qYzVJaXdpYVhOeklqb2lSbTl5WjJWU2IyTnJJaXdpYzI5bWRIZGhjbVZmWTJ4cFpXNTBYMlJsYzJOeWFYQjBhVzl1SWpvaVZHVnpkQ0JoY0hBaUxDSnpiMlowZDJGeVpWOXFkMnR6WDJWdVpIQnZhVzUwSWpvaWFIUjBjSE02WEM5Y0wzTmxjblpwWTJVdVpHbHlaV04wYjNKNUxtUmxkaTF2WWk1bWIzSm5aWEp2WTJzdVptbHVZVzVqYVdGc09qZ3dOelJjTDJGd2FWd3ZjMjltZEhkaGNtVXRjM1JoZEdWdFpXNTBYQzgyTUdNM05XSmhNMk0wTlRBME5UQXdNVEZsWm1FMk56bGNMMkZ3Y0d4cFkyRjBhVzl1WEM5cWQydGZkWEpwSWl3aWMyOW1kSGRoY21WZmFXUWlPaUkyTUdNM05XSmhNMk0wTlRBME5UQXdNVEZsWm1FMk56a2lMQ0p2Y21kZlkyOXVkR0ZqZEhNaU9sdGRMQ0p2WWw5eVpXZHBjM1J5ZVY5MGIzTWlPaUpvZEhSd2N6cGNMMXd2WkdseVpXTjBiM0o1TG1SbGRpMXZZaTVtYjNKblpYSnZZMnN1Wm1sdVlXNWphV0ZzT2pnd056UmNMM1J2YzF3dklpd2liM0puWDJsa0lqb2lOakJqTnpWaU9XTmpORFV3TkRVd01ERXhaV1poTmpjNElpd2ljMjltZEhkaGNtVmZhbmRyYzE5eVpYWnZhMlZrWDJWdVpIQnZhVzUwSWpvaVZFOUVUeUlzSW5OdlpuUjNZWEpsWDNKdmJHVnpJanBiSWtSQlZFRWlMQ0pEUWxCSlNTSXNJbEJKVTFBaUxDSkJTVk5RSWwwc0ltVjRjQ0k2TVRZeU5UYzFOVEU0T1N3aWIzSm5YMjVoYldVaU9pSkJibTl1ZVcxdmRYTWlMQ0p2Y21kZmFuZHJjMTl5WlhadmEyVmtYMlZ1WkhCdmFXNTBJam9pVkU5RVR5SXNJbWxoZENJNk1UWXlOVEUxTURNNE9Td2lhblJwSWpvaU1UQXlZVFF5WmpBdE9EUXdaaTAwWlRRMExXRTJOek10TVRnd05EQTNOV000TVRVekluMC5jWjhhUHpuMVo1MklCeGVnVDFmSmVGMTdTczRKSjVYSkF0MVd6TGF2WDRPSDJBS0l4QUdwMDM4Ni1Pd0twR0ZFQkxrVVB1MFp3VzUtczhNYl9uQjU1OHZDY2NObW14cFV4UExSMl9aZkNzSWlpWVRUNWpYYXh2UnVFa0xJc3Zocy15aVcyc05BRXlRRjJwY010M1NpS01YQTFVeTNIWV9lU0pqaURwQlhBMjZGMlF2b1lJdU1iTzd1WkVEeUp4aTd3RVJjMVFpWlB4UzMydUQ4eHhvd19hbHNEOXJpSVBzNG1HRkg2b1RIekdVSG04RUl3MkRleW9LZmMweFItWVoyUjk5NkxYSUpYMkpmOTJGT0RoSURnOU93eXNMMm1Va2QwbDBULTg4UzJYUmVQWHQ2Z0Z3eXV0bFd2MjkzMTY4R1IteExJeTRBcTIwREJkblBGaUc0eWciLCJzY29wZSI6Im9wZW5pZCBhY2NvdW50cyBwYXltZW50cyBmdW5kc2NvbmZpcm1hdGlvbnMiLCJyZXF1ZXN0X29iamVjdF9zaWduaW5nX2FsZyI6IlBTMjU2IiwiZXhwIjoxNjI1MTUwODcwLCJyZXF1ZXN0X29iamVjdF9lbmNyeXB0aW9uX2VuYyI6IkExMjhDQkMtSFMyNTYiLCJpYXQiOjE2MjUxNTA1NzAsImp0aSI6ImVlNmVjYzhkLTJiNmEtNDJkMS04ZGZlLTBjMmEyNmIyNzU1NiIsInJlc3BvbnNlX3R5cGVzIjpbImNvZGUgaWRfdG9rZW4iXSwiaWRfdG9rZW5fc2lnbmVkX3Jlc3BvbnNlX2FsZyI6IlBTMjU2In0.Ep9b9GXM0wFZtq1HSH4j6LDojAHTgUvxSQIjzxGX9QPklrJoAk_4Zg_Wooy3Jnw4OsoL92pzqoP8CtsQLDVYCvEfGh9TgbS31ItjXjcACBNAx6sWfT0NaE0T1bmjeSppj8pM18qgkNPXRv211AED0QVizE3b07arNjjj2SaVuarWp1AkSEysb4qepejZFazxAzEQuz8s66SxpPKdMfFKcaJUlr2xGbKiHFuAa6f0QrUSIfIUNQf-6DdrFL1w68EoAkkfbagAx5G4S2e_m0SraIbb9aZqm5LMvAVRYsG5tN8yBPfpWchHGI5_uJeFmNtipVfWuu7KuwiGJmGOd3OtiQ";
    }

}