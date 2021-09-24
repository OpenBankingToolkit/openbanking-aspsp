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
package com.forgerock.openbanking.aspsp.as.api.registration.manual;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientException;
import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientIdentity;
import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientIdentityFactory;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.RegistrationRequest;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.RegistrationRequestFactory;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationErrorType;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationRequest;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Optional;

@Controller
@Slf4j
public class ManualRegistrationApiController implements ManualRegistrationApi {

    public static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERT = "-----END CERTIFICATE-----";


    private final TppStoreService tppStoreService;

    private final ObjectMapper objectMapper;

    private final TppRegistrationService tppRegistrationService;


    private final ApiClientIdentityFactory identityFactory;

    private final RegistrationRequestFactory registrationRequestFactory;

    private final Resource registrationRequestFile;

    @Autowired
    public ManualRegistrationApiController(TppStoreService tppStoreService, ObjectMapper objectMapper,
                                           TppRegistrationService tppRegistrationService,
                                           ApiClientIdentityFactory identityFactory,
                                           RegistrationRequestFactory registrationRequestFactory,
                                           @Value("${manual-onboarding.registration-request-base}")
                                                       Resource registrationRequestFile) {
        this.tppStoreService = tppStoreService;
        this.objectMapper = objectMapper;
        this.tppRegistrationService = tppRegistrationService;
        this.identityFactory = identityFactory;
        this.registrationRequestFactory = registrationRequestFactory;
        this.registrationRequestFile = registrationRequestFile;
    }



    private String registrationRequestFileContent = null;

    @Override
    public ResponseEntity<OIDCRegistrationResponse> registerApplication(
            @ApiParam(value = "Registration request", required = true)
            @Valid
            @RequestBody ManualRegistrationRequest manualRegistrationRequest,

            Principal principal
    ) throws ApiClientException, DynamicClientRegistrationException, OAuth2InvalidClientException {
        log.debug("registerApplication() Received a manual onboarding registration request {}",
                manualRegistrationRequest);

        ApiClientIdentity apiClientIdentity = identityFactory.getApiClientIdentity(principal);
        log.debug("ApiClientIdentity is '{}'", apiClientIdentity);

        //Prepare the request
        String registrationRequestDefaultJsonClaims = getRegistrationRequestDefaultJsonClaims();
        RegistrationRequest registrationRequest =
                registrationRequestFactory.getRegistrationRequestFromManualRegistrationJson(
                        registrationRequestDefaultJsonClaims, manualRegistrationRequest, objectMapper);

        registrationRequest.overwriteRegistrationRequestFieldsFromSSAClaims(apiClientIdentity);
        log.debug("The OIDC registration request we are going to send to AM {}", registrationRequest);

        //Register the TPP
        String tppIdentifier = registrationRequest.getSoftwareIdFromSSA();
        Tpp tpp = tppRegistrationService.registerTpp(apiClientIdentity, registrationRequest);
        log.debug("Successfully performed manual onboarding! the tpp resulting: {}", tpp);

        return ResponseEntity.status(HttpStatus.CREATED).body(tpp.getRegistrationResponse());
    }


    private JWK parseQwacPem(String qWacPem) throws DynamicClientRegistrationException {
        X509Certificate qSealCertificate = parseCertificate(qWacPem);
        if (qSealCertificate == null) {
            log.debug("registerApplication() Could not parse qWacPem provided; {}", qWacPem);
            throw new DynamicClientRegistrationException("Could not obtain qSeal certificate",
                    DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
        }

        JWK jwk = null;
        try {
            jwk = JWK.parse(qSealCertificate);
        } catch (JOSEException e) {
            String errorMessage = "Failed to parse the transport certificate provided.";
            log.debug("parseQwacPem() {}", errorMessage, e);
            throw new DynamicClientRegistrationException(errorMessage,
                    DynamicClientRegistrationErrorType.INVALID_CLIENT_METADATA);
        }
        return jwk;
    }

    @Override
    public ResponseEntity<Boolean> unregisterApplication(
            @ApiParam(value = "Unregister application", required = true)
            @Valid
            @PathVariable(value = "clientId") String clientId,

            Principal principal
    ) {
        Optional<Tpp> isTpp = tppStoreService.findByClientId(clientId);
        isTpp.ifPresent(tppRegistrationService::deleteOAuth2RegistrationAndTppRecord);
        if (isTpp.isPresent()) {
            Tpp tpp = isTpp.get();
            tppRegistrationService.deleteOAuth2RegistrationAndTppRecord(tpp);
        }
        return ResponseEntity.ok(true);
    }

    public String getRegistrationRequestDefaultJsonClaims() {
        if (registrationRequestFileContent == null) {
            try {
                registrationRequestFileContent = StreamUtils.copyToString(registrationRequestFile.getInputStream(),
                        Charset.defaultCharset());
            } catch (IOException e) {
                log.error("Can't read registration request resource", e);
                throw new RuntimeException(e);
            }
        }
        return registrationRequestFileContent;
    }

    private X509Certificate parseCertificate(String certStr) {
        log.debug("Client certificate as PEM format: \n {}", certStr);
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream inputStream = stripAndDecodePemCert(certStr);
            Certificate certificate = certificateFactory.generateCertificate(inputStream);
            if(certificate instanceof X509Certificate){
                return (X509Certificate) certificate;
            } else {
                log.debug("Provided cert was not an X509Certificate; ", certStr);
            }

        } catch (CertificateException e) {
            log.error("Can't initialise certificate factory", e);
        }
        return null;
    }

    private ByteArrayInputStream stripAndDecodePemCert(String pemFormatCert){
        String strippedCert = stripCertPrefixAndSuffix(pemFormatCert);
        byte[] decoded =  base64Decode(strippedCert);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decoded);
        return inputStream;
    }

    private String stripCertPrefixAndSuffix(String certStr) {
        return certStr.replaceAll("\n", "")
               .replaceAll(BEGIN_CERT, "")
               .replaceAll(END_CERT, "");
    }

    private byte[] base64Decode(String encodedString){
        byte[] decoded = Base64.getDecoder().decode(encodedString);
        return decoded;
    }
}
