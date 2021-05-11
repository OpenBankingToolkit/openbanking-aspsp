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

import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationApplication;
import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationRequest;
import com.forgerock.openbanking.common.services.aspsp.ManualOnboardingService;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.forgerock.openbanking.register.model.ManualRegUser;
import com.forgerock.openbanking.register.service.ManualRegistrationApplicationService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.forgerock.spring.security.multiauth.model.authentication.JwtAuthentication;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.security.Principal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class ManualRegistrationApiController implements ManualRegistrationApi {

    @Autowired
    private ManualOnboardingService manualOnboardingService;
    @Value("${manual-onboarding.aspspManualOnboardingEndpoint}")
    private String aspspManualOnboardingEndpoint;
    @Autowired
    private ManualRegistrationApplicationService manualRegistrationApplicationService;

    @Override
    public ResponseEntity<ManualRegistrationApplication> registerApplication(
            @ApiParam(value = "Registration request", required = true)
            @Valid
            @RequestBody ManualRegistrationRequest manualRegistrationRequest,

            Principal principal
    ) throws OBErrorResponseException, OBErrorException {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) principal;
        ManualRegUser manualRegUser = fromAuthentication(jwtAuthentication);
        try {
            String softwareClientId;
            if (!"EIDAS".equals(manualRegUser.getDirectoryID())) {
                SignedJWT ssaJws = SignedJWT.parse(manualRegistrationRequest.getSoftwareStatementAssertion());
                //Convert in json for convenience
                JWTClaimsSet ssaClaims = ssaJws.getJWTClaimsSet();
                //Verify the SSA wasn't already used
                softwareClientId = ssaClaims.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_CLIENT_ID);
            } else {
                softwareClientId = manualRegUser.getAppId();
                manualRegistrationRequest.setAppId(manualRegUser.getAppId());
                manualRegistrationRequest.setOrganisationId(manualRegUser.getOrganisationId());
                manualRegistrationRequest.setPsd2Roles(manualRegUser.getPsd2Roles());
            }

            Optional<ManualRegistrationApplication> bySoftwareClientId = manualRegistrationApplicationService.findBySoftwareClientId(softwareClientId);
            if (bySoftwareClientId.isPresent()) {
                ManualRegistrationApplication manualRegistrationApplication = bySoftwareClientId.get();
                if (!"EIDAS".equals(manualRegUser.getDirectoryID())) {

                    log.debug("The SSA was already used for application {}", manualRegistrationApplication);
                    throw new OBErrorResponseException(
                            OBRIErrorType.MANUAL_ONBOARDING_SOFTWARE_STATEMENT_ALREADY_ONBOARD.getHttpStatus(),
                            OBRIErrorResponseCategory.MANUAL_ONBOARDING,
                            OBRIErrorType.MANUAL_ONBOARDING_SOFTWARE_STATEMENT_ALREADY_ONBOARD.toOBError1());
                } else {
                    log.debug("The EIDAS was already used for application {}", manualRegistrationApplication);
                    throw new OBErrorResponseException(
                            OBRIErrorType.MANUAL_ONBOARDING_EIDAS_ALREADY_ONBOARD.getHttpStatus(),
                            OBRIErrorResponseCategory.MANUAL_ONBOARDING,
                            OBRIErrorType.MANUAL_ONBOARDING_EIDAS_ALREADY_ONBOARD.toOBError1());
                }
            }
            OIDCRegistrationResponse oidcRegistrationResponse = manualOnboardingService.registerApplication(
                    jwtAuthentication,
                    aspspManualOnboardingEndpoint,
                    manualRegistrationRequest);

            //Register the manual on-boarding application wrapper around it
            ManualRegistrationApplication application = ManualRegistrationApplication.builder()
                    .userId(((UserDetails)jwtAuthentication.getPrincipal()).getUsername())
                    .manualRegistrationRequest(manualRegistrationRequest)
                    .description(manualRegistrationRequest.getApplicationDescription())
                    .softwareClientId(softwareClientId)
                    .oidcRegistrationResponse(oidcRegistrationResponse)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(manualRegistrationApplicationService.createApplication(application));
        } catch (ParseException e) {
            log.error("Couldn't parse registration request", e);
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_REQUEST_INVALID_FORMAT);
        }
    }

    @Override
    public ResponseEntity<ManualRegistrationApplication> unregisterApplication(
            @ApiParam(value = "Unregister application", required = true)
            @Valid
            @PathVariable(value = "applicationId") String applicationId,

            Principal principal
    ) throws OBErrorResponseException {
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        ManualRegistrationApplication application = verifyApplicationOwner(applicationId, currentUser);
        manualOnboardingService.unregisterApplication(currentUser.getUsername(), aspspManualOnboardingEndpoint, application.getOidcRegistrationResponse().getClientId());
        manualRegistrationApplicationService.deleteApplication(application);
        return ResponseEntity.ok(application);
    }

    @Override
    public ResponseEntity<ManualRegistrationApplication> getApplication(
            @ApiParam(value = "Unregister application", required = true)
            @Valid
            @PathVariable(value = "applicationId") String applicationId,

            Principal principal
    ) throws OBErrorResponseException {
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        ManualRegistrationApplication application = verifyApplicationOwner(applicationId, currentUser);
        return ResponseEntity.ok(application);
    }

    @Override
    public ResponseEntity<Collection<ManualRegistrationApplication>> getApplications(
            Principal principal
    ) {
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        return ResponseEntity.ok(manualRegistrationApplicationService.getAllApplications(currentUser.getUsername()));
    }

    private ManualRegistrationApplication verifyApplicationOwner(String applicationId, UserDetails currentUser) throws OBErrorResponseException {
        Optional<ManualRegistrationApplication> isApplication = manualRegistrationApplicationService.findById(applicationId);
        if (!isApplication.isPresent()) {
            throw new OBErrorResponseException(
                    OBRIErrorType.MANUAL_ONBOARDING_APPLICATION_NOT_FOUND.getHttpStatus(),
                    OBRIErrorResponseCategory.MANUAL_ONBOARDING,
                    OBRIErrorType.MANUAL_ONBOARDING_APPLICATION_NOT_FOUND.toOBError1(applicationId));
        }
        ManualRegistrationApplication application = isApplication.get();
        if (!application.getUserId().equals(currentUser.getUsername())) {
            throw new OBErrorResponseException(
                    OBRIErrorType.MANUAL_ONBOARDING_WRONG_USER.getHttpStatus(),
                    OBRIErrorResponseCategory.MANUAL_ONBOARDING,
                    OBRIErrorType.MANUAL_ONBOARDING_WRONG_USER.toOBError1(applicationId));
        }
        return application;
    }

    public ManualRegUser fromAuthentication(JwtAuthentication jwtAuthentication) {
        ManualRegUser user = new ManualRegUser();
        user.setId(((UserDetails)jwtAuthentication.getPrincipal()).getUsername().toLowerCase());
        user.setAuthorities(jwtAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        try {
            String directoryID = jwtAuthentication.getJwtClaimsSet().getStringClaim("directoryID");
            user.setDirectoryID(directoryID);
            if ("EIDAS".equals(directoryID)) {
                user.setAppId(jwtAuthentication.getJwtClaimsSet().getStringClaim("app_id"));
                user.setOrganisationId(jwtAuthentication.getJwtClaimsSet().getStringClaim("org_id"));
                user.setPsd2Roles(jwtAuthentication.getJwtClaimsSet().getStringClaim("psd2_roles"));
            }
        } catch (ParseException e) {
            log.error("Couldn't read claims from user context", e);
        }
        return user;
    }
}
