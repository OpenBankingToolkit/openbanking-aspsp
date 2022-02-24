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
package com.forgerock.openbanking.register.api.manual;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2BearerTokenUsageInvalidTokenException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2BearerTokenUsageMissingAuthInfoException;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationApplication;
import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationRequest;
import com.forgerock.openbanking.common.services.aspsp.ManualOnboardingService;
import com.forgerock.openbanking.common.services.onboarding.TppRegistrationService;
import com.forgerock.openbanking.common.services.onboarding.apiclient.ApiClientException;
import com.forgerock.openbanking.common.services.onboarding.apiclient.ApiClientIdentity;
import com.forgerock.openbanking.common.services.onboarding.apiclient.ApiClientIdentityFactory;
import com.forgerock.openbanking.common.services.onboarding.registrationrequest.RegistrationRequest;
import com.forgerock.openbanking.common.services.onboarding.registrationrequest.RegistrationRequestFactory;
import com.forgerock.openbanking.common.services.security.Psd2WithSessionApiHelperService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.forgerock.openbanking.register.service.ManualRegistrationApplicationService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class ManualRegistrationApiController implements ManualRegistrationApi {

    @Autowired
    public ManualRegistrationApiController(ManualOnboardingService manualOnboardingService,
                                           @Value("${manual-onboarding.aspspManualOnboardingEndpoint}")
                                           String aspspManualOnboardingEndpoint,
                                           ManualRegistrationApplicationService manualRegistrationApplicationService,
                                           Psd2WithSessionApiHelperService psd2WithSessionApiHelperService,
                                           ApiClientIdentityFactory identityFactory,
                                           TppRegistrationService tppRegistrationService,
                                           RegistrationRequestFactory registrationRequestFactory,
                                           ObjectMapper objectMapper,
                                           @Value("${manual-onboarding.registration-request-base}")
                                                   Resource registrationRequestFile) {
        this.manualOnboardingService = manualOnboardingService;
        this.aspspManualOnboardingEndpoint = aspspManualOnboardingEndpoint;
        this.manualRegistrationApplicationService = manualRegistrationApplicationService;
        this.psd2WithSessionApiHelperService = psd2WithSessionApiHelperService;
        this.identityFactory = identityFactory;
        this.tppRegistrationService = tppRegistrationService;
        this.registrationRequestFactory = registrationRequestFactory;
        this.objectMapper = objectMapper;
        this.registrationRequestFile = registrationRequestFile;
    }


    private final ManualOnboardingService manualOnboardingService;
    private final String aspspManualOnboardingEndpoint;
    private final ManualRegistrationApplicationService manualRegistrationApplicationService;
    private final Psd2WithSessionApiHelperService psd2WithSessionApiHelperService;
    private final ApiClientIdentityFactory identityFactory;
    private final TppRegistrationService tppRegistrationService;
    private final RegistrationRequestFactory registrationRequestFactory;
    ObjectMapper objectMapper;
    Resource registrationRequestFile;

    private String registrationRequestFileContent = null;

    @Override
    public ResponseEntity<String> getOrganizationIdentifier(
            Principal principal
    ) throws OAuth2InvalidClientException {
        try{
            ApiClientIdentity apiClientIdentity = identityFactory.getApiClientIdentity(principal);
            String organizationIdentifier =
                    apiClientIdentity.getAuthorisationNumber().orElseThrow(
                            () -> new OAuth2InvalidClientException("Could not get OrganizationIdentifier from  " +
                                    "certificate"));
            return ResponseEntity.status(HttpStatus.OK).body(organizationIdentifier);
        } catch (ApiClientException e) {
            log.info("getOrganizationIdentifier() caught ApiClientException; ", e);
            throw new OAuth2InvalidClientException("Failed to obtain OrganizationIdentifier from certificate");
        }
    }

    @Override
    public ResponseEntity<ManualRegistrationApplication> registerApplication(
            @ApiParam(value = "Registration request", required = true)
            @Valid
            @RequestBody ManualRegistrationRequest manualRegistrationRequest,

            @CookieValue(value = "obri-session", required = true) String obriSession,

            Principal principal
    ) throws OAuth2InvalidClientException {
        log.debug("registerApplication called. manualRegistrationRequest is '{}'", manualRegistrationRequest);

        ApiClientIdentity apiClientIdentity = null;
        try {
            String userNameOfSessionHolder = this.getUserNameFromSession(obriSession);
            apiClientIdentity = identityFactory.getApiClientIdentity(principal);
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


            ManualRegistrationApplication manualRegistrationApplication = ManualRegistrationApplication.builder()
                    .userId(userNameOfSessionHolder)
                    .manualRegistrationRequest(manualRegistrationRequest)
                    .description(manualRegistrationRequest.getApplicationDescription())
                    .softwareClientId(tpp.getClientId())
                    .oidcRegistrationResponse(tpp.getRegistrationResponse())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(manualRegistrationApplicationService.createApplication(manualRegistrationApplication));

        } catch (ApiClientException e) {
            log.info("registerApplication() caught ApiClientException; ", e);
            throw new OAuth2InvalidClientException(e.getMessage());
        } catch (DynamicClientRegistrationException e) {
            log.info("registerApplication() caught DynamicClientRegistrationException; ", e);
            throw new OAuth2InvalidClientException(e.getMessage());
        }
    }



    @Override
    public ResponseEntity<ManualRegistrationApplication> unregisterApplication(
            @ApiParam(value = "Unregister application", required = true)
            @Valid
            @PathVariable(value = "applicationId") String applicationId,

            @CookieValue(value = "obri-session", required = true) String obriSession,

            Principal principal
    ) throws OBErrorResponseException, OAuth2InvalidClientException, OAuth2BearerTokenUsageMissingAuthInfoException,
            OAuth2BearerTokenUsageInvalidTokenException {
        String methodName = "unregisterApplication()";
        log.info("{} called for ClientId '{}', tpp is '{}'", methodName, applicationId, principal.getName());

        String userNameOfSessionHolder = this.getUserNameFromSession(obriSession);
        ManualRegistrationApplication manualRegistrationApplication =
                getManualApplicationIfOwnedBySessionOwner(applicationId, userNameOfSessionHolder);


        String oauth2ClientId = manualRegistrationApplication.getOidcRegistrationResponse().getClientId();
        Tpp tpp = tppRegistrationService.getTpp(oauth2ClientId);
        tppRegistrationService.ensureTppOwnsOidcRegistration(tpp, principal.getName());


        if(!sessionHolderOwnsManualRegistration(userNameOfSessionHolder, manualRegistrationApplication)){
            log.info("unregisterApplication() logged in user does not own this manual registration application");
            throw new OAuth2InvalidClientException("Logged in user does not own this manual registration application");
        }


        tppRegistrationService.deleteOAuth2RegistrationAndTppRecord(tpp);
        log.info("{} Unregistered ClientId '{}'", methodName, applicationId);

        manualRegistrationApplicationService.deleteApplication(manualRegistrationApplication);

        return ResponseEntity.ok(manualRegistrationApplication);
    }

    @Override
    public ResponseEntity<ManualRegistrationApplication> getApplication(
            @ApiParam(value = "Unregister application", required = true)
            @Valid
            @PathVariable(value = "applicationId") String applicationId,

            @CookieValue(value = "obri-session", required = true) String obriSession,

            Principal principal
    ) throws OBErrorResponseException, OAuth2InvalidClientException {
        log.info("getApplication() called for applicationId '{}' by tpp '{}'", applicationId, principal.getName());
        String userNameOfSessionHolder = this.getUserNameFromSession(obriSession);
        log.debug("getApplication() username of session owner is '{}'", userNameOfSessionHolder);
        ManualRegistrationApplication application = getManualApplicationIfOwnedBySessionOwner(
                applicationId, userNameOfSessionHolder);
        ensurePrincipalOwnsTppRegistrations(List.of(application), principal);

        log.debug("getApplication() returning application id '{}'", application.getId());
        return ResponseEntity.ok(application);
    }


    @Override
    public ResponseEntity<Collection<ManualRegistrationApplication>> getApplications(
            @CookieValue(value = "obri-session", required = true) String obriSession,

            Principal principal
    ) throws OAuth2InvalidClientException {
        log.info("getApplications() called by tpp '{}'", principal.getName());
        String userNameOfSessionHolder = this.getUserNameFromSession(obriSession);
        log.debug("getApplications() username of session owner is '{}'", userNameOfSessionHolder);
        Collection<ManualRegistrationApplication> applications =
                manualRegistrationApplicationService.getAllApplications(userNameOfSessionHolder);
        ensurePrincipalOwnsTppRegistrations(applications, principal);

        return ResponseEntity.ok(applications);
    }

    private void ensurePrincipalOwnsTppRegistrations(Collection<ManualRegistrationApplication> applications,
                                                    Principal principal) throws OAuth2InvalidClientException {
        log.debug("ensurePrincipalOwnsTppRegistrations() checking that '{}' applications are owned by '{}'",
                applications.size(), principal.getName());
        for(ManualRegistrationApplication application : applications) {
            OIDCRegistrationResponse regResponse = application.getOidcRegistrationResponse();
            if (regResponse == null) {
                String errorString = "Failed to determine if MATLS client cert belongs to the TPP that owns the " +
                        "application with id ";
                log.info("principalOwnsTppRegistration() {}'{}'", errorString, application.getId());
                throw new OAuth2InvalidClientException(errorString + application.getId() + "'");
            }
            String oauth2ClientId = regResponse.getClientId();
            Tpp tpp = tppRegistrationService.getTpp(oauth2ClientId);
            tppRegistrationService.ensureTppOwnsOidcRegistration(tpp, principal.getName());
        }
        log.debug("ensurePrincipalOwnsTppRegistrations() all application's OAuth2 clients owned by '{}'",
                principal.getName());
    }


    private boolean sessionHolderOwnsManualRegistration(
            String userNameOfSessionHolder, ManualRegistrationApplication manualRegistrationApplication) {
        String usernameOfManualRegistrationOwner = manualRegistrationApplication.getUserId();
        return StringUtils.isNotBlank(userNameOfSessionHolder) &&
                userNameOfSessionHolder.equals(usernameOfManualRegistrationOwner);
    }

    private ManualRegistrationApplication getManualApplicationIfOwnedBySessionOwner(
            String applicationId, String usernameOfSessionHolder) throws OBErrorResponseException {
        Optional<ManualRegistrationApplication> isApplication =
                manualRegistrationApplicationService.findById(applicationId);
        if (isApplication.isEmpty()) {
            throw new OBErrorResponseException(
                    OBRIErrorType.MANUAL_ONBOARDING_APPLICATION_NOT_FOUND.getHttpStatus(),
                    OBRIErrorResponseCategory.MANUAL_ONBOARDING,
                    OBRIErrorType.MANUAL_ONBOARDING_APPLICATION_NOT_FOUND.toOBError1(applicationId));
        }

        ManualRegistrationApplication application = isApplication.get();
        if (!sessionHolderOwnsManualRegistration(usernameOfSessionHolder, application)) {
            throw new OBErrorResponseException(
                    OBRIErrorType.MANUAL_ONBOARDING_WRONG_USER.getHttpStatus(),
                    OBRIErrorResponseCategory.MANUAL_ONBOARDING,
                    OBRIErrorType.MANUAL_ONBOARDING_WRONG_USER.toOBError1(applicationId));
        }
        return application;
    }


    private String getRegistrationRequestDefaultJsonClaims() {
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

    private String getUserNameFromSession(String session) throws OAuth2InvalidClientException{
        try {
            return this.psd2WithSessionApiHelperService.getPsuNameFromSession(session);
        } catch (OBErrorException e) {
            log.info("getUserNameFromSession() failed getting username from session. caught OBErrorException; ", e);
            throw new OAuth2InvalidClientException("Failed to get username from session");
        }
    }
}
