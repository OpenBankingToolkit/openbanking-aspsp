/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.services;

import com.forgerock.openbanking.am.gateway.AMASPSPGateway;
import com.forgerock.openbanking.commons.model.rcs.RedirectionAction;
import com.forgerock.openbanking.config.ApplicationConfiguration;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
 * RCS flows contains the functions needed by RCS to communicate with the AS.
 */
@Service
@Slf4j
public class RcsService {
    @Autowired
    private CryptoApiClient cryptoApiClient;
    @Value("${rcs.issuer-id}")
    public String issuerId;
    @Value("${am.cookie.name}")
    private String cookieName;
    @Autowired
    private AMASPSPGateway amGateway;

    /**
     * Generate a new RCS authentication JWT.
     *
     * @return a JWT that can be used to authenticate RCS to the AS.
     * @throws JOSEException
     */
    public String generateRCSConsentResponse(ApplicationConfiguration rcsConfiguration, ApplicationConfiguration
            amConfiguration, String csrf, boolean decision, List<String> scopes, String clientId) throws JOSEException {
        JWTClaimsSet.Builder requestParameterClaims;
        requestParameterClaims = new JWTClaimsSet.Builder();

        requestParameterClaims.issuer(rcsConfiguration.getIssuerID());
        requestParameterClaims.audience(amConfiguration.getIssuerID());
        requestParameterClaims.expirationTime(new Date(new Date().getTime() + Duration.ofMinutes(5).toMillis()));
        requestParameterClaims.claim("decision", decision);
        requestParameterClaims.claim("csrf", csrf);
        requestParameterClaims.claim("scopes", scopes);
        requestParameterClaims.claim("clientId", clientId);

        return cryptoApiClient.signClaims(issuerId, requestParameterClaims.build(), false);
    }

    public ResponseEntity sendRCSResponseToAM(String ssoToken, RedirectionAction redirectionAction) {
        //Return to  AM
        HttpHeaders amHeaderRcsResponse = new HttpHeaders();
        amHeaderRcsResponse.add("Cookie", cookieName + "=" + ssoToken);
        amHeaderRcsResponse.add("Content-Type", "application/x-www-form-urlencoded");

        log.debug("Consent response {} to {} to uri '{}'", redirectionAction.getConsentJwt(), redirectionAction.getRequestMethod(), redirectionAction.getRedirectUri());
        String body = "consent_response=" + redirectionAction.getConsentJwt();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(redirectionAction.getRedirectUri());
        return amGateway.toAM(builder.build(true).toUri().toString(), HttpMethod.POST, amHeaderRcsResponse, new ParameterizedTypeReference<String>() {}, body);
    }
}
