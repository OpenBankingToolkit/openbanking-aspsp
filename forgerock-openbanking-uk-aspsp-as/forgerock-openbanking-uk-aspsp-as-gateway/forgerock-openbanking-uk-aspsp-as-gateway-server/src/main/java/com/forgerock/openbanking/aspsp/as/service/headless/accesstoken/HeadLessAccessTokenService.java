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
package com.forgerock.openbanking.aspsp.as.service.headless.accesstoken;

import com.forgerock.openbanking.am.gateway.AMGateway;
import com.forgerock.openbanking.aspsp.as.api.authorisation.redirect.AuthorisationApi;
import com.forgerock.openbanking.aspsp.as.service.PairClientIDAuthMethod;
import com.forgerock.openbanking.aspsp.as.service.headless.ParseUriUtils;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.AccessTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.forgerock.openbanking.constants.OIDCConstants.OIDCClaim;

@Service
@Slf4j
public class HeadLessAccessTokenService {

    private final AuthorisationApi authorisationApi;

    @Autowired
    public HeadLessAccessTokenService(AuthorisationApi authorisationApi) {
        this.authorisationApi = authorisationApi;
    }


    public ResponseEntity<AccessTokenResponse> getAccessToken(AMGateway amGateway,  PairClientIDAuthMethod clientIDAuthMethod,
                                         MultiValueMap<String, String> paramMap, HttpServletRequest request)
            throws OBErrorResponseException, OBErrorException {
        ResponseEntity authorisation = authorisationApi.getAuthorisation(
                paramMap.getFirst(OIDCClaim.RESPONSE_TYPE),
                paramMap.getFirst(OIDCClaim.CLIENT_ID),
                paramMap.getFirst(OIDCClaim.STATE),
                paramMap.getFirst(OIDCClaim.NONCE),
                paramMap.getFirst(OIDCClaim.SCOPE),
                paramMap.getFirst(OIDCClaim.REDIRECT_URI),
                paramMap.getFirst(OIDCClaim.REQUEST),
                true,
                paramMap.getFirst(OIDCClaim.USERNAME),
                paramMap.getFirst(OIDCClaim.PASSWORD),
                "",
                null,
                request
        );

        HttpStatus statusCode = authorisation.getStatusCode();
        if(!statusCode.is3xxRedirection() && !statusCode.is2xxSuccessful()){
            log.warn("getAccessToken() authorisation failed. Response was '{}' ", authorisation);
            throw new OBErrorResponseException(
                    OBRIErrorType.HEAD_LESS_AUTH_AS_ERROR_RECEIVED.getHttpStatus(),
                    OBRIErrorResponseCategory.HEADLESS_AUTH,
                    OBRIErrorType.HEAD_LESS_AUTH_AS_ERROR_RECEIVED.toOBError1(
                            authorisation.getStatusCode(), authorisation.getBody(), authorisation.getHeaders()));
        }

        String location = authorisation.getHeaders().getFirst(org.apache.http.HttpHeaders.LOCATION);
        if(location == null || location.isBlank()){
            throw new OBErrorResponseException(
                    OBRIErrorType.HEAD_LESS_AUTH_TPP_URI_INCORRECT.getHttpStatus(),
                    OBRIErrorResponseCategory.HEADLESS_AUTH,
                    OBRIErrorType.HEAD_LESS_AUTH_TPP_URI_INCORRECT.toOBError1(location));
        }
        String code = getAuthorizationCode(location);

        return exchangeAuthorizationCodeForAccessToken(amGateway, clientIDAuthMethod, paramMap, request, code);
    }

    /**
     * Get the authorizationCode from the location header. This method expects to find the code in the URL fragment
     * of the location header.
     * @param location the location parameter of the OAuth2 redirect from the auth server to the client
     * @return the Authorization Code or null if this can't be obtained from the location string passed in
     * @throws OBErrorResponseException If the location doesn't contain a valid code fragment
     */
    private String getAuthorizationCode(String location) throws OBErrorResponseException {
        try {
            URL url = new URL(location);
            if (url.getRef() == null) {
                log.error("getAuthorizationCode() The location '{}' doesn't have an anchor fragment", location);
                //We received an error.
                // Ex: https://www.google.com?error_description=JWT%20invalid.%20Expiration%20time%20incorrect.&state=10d260bf-a7d9-444a-92d9-7b7a5f088208&error=invalid_request_object
                Map<String, String> query = ParseUriUtils.parseQueryOrFragment(url.getQuery());
                throw new OBErrorResponseException(
                        OBRIErrorType.HEAD_LESS_AUTH_AS_ERROR_RECEIVED.getHttpStatus(),
                        OBRIErrorResponseCategory.HEADLESS_AUTH,
                        OBRIErrorType.HEAD_LESS_AUTH_AS_ERROR_RECEIVED.toOBError1(
                                query.get("error"), query.get("error_description"), query.get("state")));
            }
            Map<String, String> fragment = ParseUriUtils.parseQueryOrFragment(url.getRef());
            String authCode = fragment.get("code");
            if ( authCode == null || authCode.isBlank()) {
                log.error("getAuthorizationCode() The location '{}' doesn't have a code value in the fragment",
                        location);
                throw new OBErrorResponseException(
                        OBRIErrorType.HEAD_LESS_AUTH_TPP_URI_NO_CODE.getHttpStatus(),
                        OBRIErrorResponseCategory.HEADLESS_AUTH,
                        OBRIErrorType.HEAD_LESS_AUTH_TPP_URI_NO_CODE.toOBError1(location));
            }
            return authCode;
        } catch (MalformedURLException e) {
            log.error("The location '{}' to the TPP, returned by AM, is not an URL", location, e);
            throw new OBErrorResponseException(
                    OBRIErrorType.HEAD_LESS_AUTH_TPP_URI_INCORRECT.getHttpStatus(),
                    OBRIErrorResponseCategory.HEADLESS_AUTH,
                    OBRIErrorType.HEAD_LESS_AUTH_TPP_URI_INCORRECT.toOBError1(location));
        }
    }

    private ResponseEntity<AccessTokenResponse> exchangeAuthorizationCodeForAccessToken(AMGateway amGateway,
                                                                   PairClientIDAuthMethod tokenEndpointAuthMethods,
                                                                   MultiValueMap paramMap, HttpServletRequest request,
                                                                   String code) throws OBErrorResponseException {
        StringBuilder requestBody = new StringBuilder();
        requestBody.append("grant_type=authorization_code");

        Map<String, String> parameters = paramMap.toSingleValueMap();
        switch (tokenEndpointAuthMethods.authMethod) {
            case CLIENT_SECRET_BASIC:
                //Would be in the request header already
                break;
            case TLS_CLIENT_AUTH:
                requestBody.append("&client_id=").append(encode(parameters.get("client_id")));
                break;
            case CLIENT_SECRET_POST:
                requestBody.append("&client_id=").append(encode(parameters.get("client_id")));
                requestBody.append("&client_secret=").append(encode(parameters.get("client_secret")));
                break;
            case CLIENT_SECRET_JWT:
                throw new RuntimeException("Not Implemented");
            case PRIVATE_KEY_JWT:
                requestBody.append("&client_assertion_type=").append(encode(parameters.get("client_assertion_type")));
                requestBody.append("&client_assertion=").append(encode(parameters.get("client_assertion")));
                break;
        }

        requestBody.append("&redirect_uri=").append(encode(parameters.get("redirect_uri")));
        requestBody.append("&code=").append(encode(code));

        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        ResponseEntity responseFromAM = amGateway.toAM(request, httpHeaders,
                new ParameterizedTypeReference<AccessTokenResponse>() {}, requestBody.toString());
        HttpStatus statusCode = responseFromAM.getStatusCode();

        if (!statusCode.is2xxSuccessful() && !statusCode.is3xxRedirection()) {
            log.warn("getAccessToken() unsuccessful call to headlessAuthTokenService. StatusCode: {}, body: {}",
                    statusCode, responseFromAM.getBody());
            throw new OBErrorResponseException(
                    OBRIErrorType.ACCESS_TOKEN_INVALID.getHttpStatus(),
                    OBRIErrorResponseCategory.ACCESS_TOKEN,
                    OBRIErrorType.ACCESS_TOKEN_INVALID.toOBError1(responseFromAM.getBody())
            );
        }

        Object responseBody = responseFromAM.getBody();
        if( responseBody instanceof AccessTokenResponse){
            ResponseEntity<AccessTokenResponse> responseEntity =
                    new ResponseEntity<>((AccessTokenResponse)responseFromAM.getBody(),
                            responseFromAM.getStatusCode());
            return responseEntity;
        } else {
            throw new OBErrorResponseException(
                    OBRIErrorType.ACCESS_TOKEN_INVALID.getHttpStatus(),
                    OBRIErrorResponseCategory.ACCESS_TOKEN,
                    OBRIErrorType.ACCESS_TOKEN_INVALID.toOBError1(responseFromAM.getBody())
            );
        }
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
