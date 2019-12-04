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
package com.forgerock.openbanking.common.services.aspsp;

import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationRequest;
import com.forgerock.openbanking.model.UserContext;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;

@Service
@Slf4j
public class ManualOnboardingService {

    @Autowired
    private RestTemplate restTemplate;

    public OIDCRegistrationResponse registerApplication(UserContext currentUser, String aspspManualOnboardingEndpoint, ManualRegistrationRequest manualRegistrationRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("userId", currentUser.getUsername());

        try {
            String directoryID = currentUser.getSessionClaims().getStringClaim("directoryID");
            httpHeaders.set("directoryID", directoryID);
            if (directoryID == "EIDAS") {
                manualRegistrationRequest.setAppId(currentUser.getSessionClaims().getStringClaim("app_id"));
                manualRegistrationRequest.setOrganisationId(currentUser.getSessionClaims().getStringClaim("org_id"));
                manualRegistrationRequest.setPsd2Roles(currentUser.getSessionClaims().getStringClaim("psd2_roles"));
            }
        } catch (ParseException e) {
            log.error("Couldn't read claims from user context", e);
        }
        HttpEntity<ManualRegistrationRequest> request = new HttpEntity<>(manualRegistrationRequest, httpHeaders);
        return restTemplate.exchange(aspspManualOnboardingEndpoint, HttpMethod.POST,
                request, OIDCRegistrationResponse.class).getBody();
    }

    public Boolean unregisterApplication(String userId, String aspspManualOnboardingEndpoint, String clientId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(aspspManualOnboardingEndpoint + clientId);
        URI uri = builder.build().encode().toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("userId", userId);
        HttpEntity request = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(uri, HttpMethod.DELETE, request, Boolean.class).getBody();
    }
}
