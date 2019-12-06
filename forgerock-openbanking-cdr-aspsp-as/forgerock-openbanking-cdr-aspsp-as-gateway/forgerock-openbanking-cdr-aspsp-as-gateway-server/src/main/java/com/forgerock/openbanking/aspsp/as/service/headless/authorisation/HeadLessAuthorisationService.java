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
package com.forgerock.openbanking.aspsp.as.service.headless.authorisation;

import com.forgerock.openbanking.am.gateway.AMGateway;
import com.forgerock.openbanking.am.services.AMAuthentication;
import com.forgerock.openbanking.aspsp.as.service.headless.ParseUriUtils;
import com.forgerock.openbanking.common.conf.headless.HeadLessAuthProperties;
import com.forgerock.openbanking.common.model.rcs.RedirectionAction;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


@Service
@Slf4j
public class HeadLessAuthorisationService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AMAuthentication amAuthentication;

    @Value("${dns.hosts.root}")
    private String dnsHostRoot;
    @Value("${scgw.port}")
    private String scgwPort;
    @Value("${rcs.internal-port}")
    private String rcsPort;
    @Value("${am.cookie.name}")
    private String cookieName;
    @Value("${am.matls-hostname}")
    private String amMatlsHostname;
    @Value("${am.internal.root}")
    private String amRoot;

    @Autowired
    private HeadLessAuthProperties headLessAuthProperties;

    public ResponseEntity getAuthorisation(
            AMGateway amGateway,
            String responseType, String clientId, String state, String nonce, String scopes, String redirectUri,
            String requestParameters, String username, String password
    ) throws OBErrorResponseException {

        log.debug("Start the headless authorisation flow.");
        log.debug("The X_HEADLESS_AUTH_* received are: username='{}' and password='{}'", username, password);

        if (username == null || username.equals("")) {
            username = headLessAuthProperties.getDefaultPsu().getUsername();
        }
        if (password == null || password.equals("")) {
            password = headLessAuthProperties.getDefaultPsu().getPassword();
        }
        try {
            log.debug("Login to AM using the user username='{}' and password='{}'", username, password);
            AMAuthentication.TokenResponse authenticate = amAuthentication.authenticate( username, password, "simple%20login");
            log.debug("Successfully authenticated. The token id : '{}'", authenticate.getTokenId());

            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(amRoot)
                    .path("/oauth2/authorize")
                    .queryParam("response_type", responseType)
                    .queryParam("client_id", clientId)
                    .queryParam("state", state)
                    .queryParam("nonce", nonce)
                    .queryParam("scope", scopes)
                    .queryParam("redirect_uri", redirectUri)
                    .queryParam("request", requestParameters);

            log.debug("Call the AM authorisation endpoint to get the consent request JWT");
            String consentRequest = getConsentRequest(amGateway, uriComponentsBuilder.toUriString(), authenticate);

            log.debug("Call the RCS with the consent request '{}'", consentRequest);
            RedirectionAction redirectionAction = getRedirectionActionFromRCS(authenticate, consentRequest);
            log.debug("The redirection action received from the RCS: '{}'", redirectionAction);

            return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectionAction.getRedirectUri()).build();
        } catch (WrongResponseEntity e) {
            return e.responseEntity;
        }
    }

    private String getConsentRequest(AMGateway amGateway, String amAuthorizationEndpoint, AMAuthentication.TokenResponse authenticate) throws OBErrorResponseException, WrongResponseEntity {
        //Send the request to AM which will ask us to redirect to the RCS
        HttpHeaders amHeader = new HttpHeaders();
        amHeader.add("Cookie", cookieName + "=" + authenticate.getTokenId());

        ResponseEntity<String> responseEntity = amGateway.toAM(amAuthorizationEndpoint, HttpMethod.GET, amHeader,
                new ParameterizedTypeReference<String>() {}, null);
        log.debug("Response received by AM: {}", responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.FOUND) {
            throw new WrongResponseEntity(responseEntity);
        }

        //Extract the RCS consent
        String location = responseEntity.getHeaders().getFirst(HttpHeaders.LOCATION);
        log.debug("The redirection to the consent page should be in the location '{}'", location);

        try {
            URL url = new URL(location);
            Map<String, String> queryParameters = ParseUriUtils.parseQueryOrFragment(url.getQuery());
            if (!queryParameters.containsKey("consent_request")) {
                throw new WrongResponseEntity(responseEntity);
            }
            log.debug("The consent_request '{}'", queryParameters.get("consent_request"));
            return queryParameters.get("consent_request");
        } catch (MalformedURLException e) {
            log.error("The location '{}' to the RCS, returned by AM, is not an URL", location, e);
            throw new OBErrorResponseException(
                    OBRIErrorType.HEAD_LESS_AUTH_RCS_URI_INCORRECT.getHttpStatus(),
                    OBRIErrorResponseCategory.HEADLESS_AUTH,
                    OBRIErrorType.HEAD_LESS_AUTH_RCS_URI_INCORRECT.toOBError1(location));
        }
    }

    private RedirectionAction getRedirectionActionFromRCS(AMAuthentication.TokenResponse authenticate, String consentRequest) {
        //Auto-approve consent via the RCS
        ParameterizedTypeReference<RedirectionAction> rcsPtr = new ParameterizedTypeReference<RedirectionAction>() {
        };
        HttpHeaders rcsHeader = new HttpHeaders();
        rcsHeader.add("Content-Type", "application/jwt");
        rcsHeader.add("Cookie", cookieName + "=" + authenticate.getTokenId());
        HttpEntity rcsHttpEntity = new HttpEntity(consentRequest, rcsHeader);

        return restTemplate.exchange("https://rs-rcs:" + rcsPort + "/api/rcs/consent/auto-accept/",
                HttpMethod.POST, rcsHttpEntity, rcsPtr).getBody();
    }

    public static class WrongResponseEntity extends Exception {

        public ResponseEntity responseEntity;

        public WrongResponseEntity(ResponseEntity responseEntity) {
            this.responseEntity = responseEntity;
        }
    }
}
