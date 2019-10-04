/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as.service.headless.accesstoken;

import com.forgerock.openbanking.am.gateway.AMGateway;
import com.forgerock.openbanking.aspsp.as.api.accesstoken.AccessTokenApiController;
import com.forgerock.openbanking.aspsp.as.api.authorisation.redirect.AuthorisationApi;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

@Service
@Slf4j
public class HeadLessAccessTokenService {

    @Autowired
    private AuthorisationApi authorisationApi;

    public ResponseEntity getAccessToken(AMGateway amGateway,  AccessTokenApiController.PairClientIDAuthMethod clientIDAuthMethod,
                                         MultiValueMap paramMap, HttpServletRequest request
    ) throws OBErrorResponseException, OBErrorException {
        ResponseEntity authorisation = authorisationApi.getAuthorisation(
                (String) paramMap.getFirst("response_type"),
                (String) paramMap.getFirst("client_id"),
                (String) paramMap.getFirst("state"),
                (String) paramMap.getFirst("nonce"),
                (String) paramMap.getFirst("scope"),
                (String) paramMap.getFirst("redirect_uri"),
                (String) paramMap.getFirst("request"),
                true,
                (String) paramMap.getFirst("username"),
                (String) paramMap.getFirst("password"),
                "",
                null,
                request
        );

        String location = authorisation.getHeaders().getFirst(org.apache.http.HttpHeaders.LOCATION);
        String code = getCode(location);

        return exchangeCode(amGateway, clientIDAuthMethod, paramMap, request, code);
    }

    private String getCode(String location) throws OBErrorResponseException {
        try {
            URL url = new URL(location);
            if (url.getRef() == null) {
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
            if (!fragment.containsKey("code")) {
                log.error("The location '{}' doesn't have an code", location);
                throw new OBErrorResponseException(
                        OBRIErrorType.HEAD_LESS_AUTH_TPP_URI_NO_CODE.getHttpStatus(),
                        OBRIErrorResponseCategory.HEADLESS_AUTH,
                        OBRIErrorType.HEAD_LESS_AUTH_TPP_URI_NO_CODE.toOBError1(location));
            }
            return fragment.get("code");
        } catch (MalformedURLException e) {
            log.error("The location '{}' to the TPP, returned by AM, is not an URL", location, e);
            throw new OBErrorResponseException(
                    OBRIErrorType.HEAD_LESS_AUTH_TPP_URI_INCORRECT.getHttpStatus(),
                    OBRIErrorResponseCategory.HEADLESS_AUTH,
                    OBRIErrorType.HEAD_LESS_AUTH_TPP_URI_INCORRECT.toOBError1(location));
        }
    }

    private ResponseEntity exchangeCode(AMGateway amGateway, AccessTokenApiController.PairClientIDAuthMethod tokenEndpointAuthMethods, MultiValueMap paramMap, HttpServletRequest request, String code) throws OBErrorResponseException {
        StringBuilder body = new StringBuilder();
        body.append("grant_type=authorization_code");

        Map<String, String> parameters = paramMap.toSingleValueMap();
        switch (tokenEndpointAuthMethods.authMethod) {
            case CLIENT_SECRET_BASIC:
                //Would be in the request header already
                break;
            case TLS_CLIENT_AUTH:
                body.append("&client_id=" + encode(parameters.get("client_id")));
                break;
            case CLIENT_SECRET_POST:
                body.append("&client_id=" + encode(parameters.get("client_id")));
                body.append("&client_secret=" + encode(parameters.get("client_secret")));
                break;
            case CLIENT_SECRET_JWT:
                throw new RuntimeException("Not Implemented");
            case PRIVATE_KEY_JWT:
                body.append("&client_assertion_type=" + encode(parameters.get("client_assertion_type")));
                body.append("&client_assertion=" + encode(parameters.get("client_assertion")));
                break;
        }

        body.append("&redirect_uri=" + encode(parameters.get("redirect_uri")));
        body.append("&code=" + encode(code));

        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        ResponseEntity responseFromAM = amGateway.toAM(request, httpHeaders,
                new ParameterizedTypeReference<AccessTokenResponse>() {}, body.toString());
        return ResponseEntity.status(responseFromAM.getStatusCode()).body(responseFromAM.getBody());
    }

    private String encode(String value) throws OBErrorResponseException {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Couldn't encode the body parameter '{}'", value, e);
            throw new OBErrorResponseException(
                    OBRIErrorType.HEAD_LESS_AUTH_EXCHANGE_CODE_BODY_ERROR.getHttpStatus(),
                    OBRIErrorResponseCategory.HEADLESS_AUTH,
                    OBRIErrorType.HEAD_LESS_AUTH_EXCHANGE_CODE_BODY_ERROR.toOBError1(value));
        }
    }
}
