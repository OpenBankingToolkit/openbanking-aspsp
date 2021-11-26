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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_8.vrp;

<<<<<<< HEAD
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.vrp.DomesticVRPConsentRepository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetails;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.repositories.TppRepository;
import lombok.extern.slf4j.Slf4j;
=======
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
>>>>>>> 45ea6103 (Move Matts controllers to the payments/v3_1_8/vrp package)
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentRequest;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentResponse;
import uk.org.openbanking.datamodel.vrp.OBVRPFundsConfirmationRequest;
<<<<<<< HEAD
import uk.org.openbanking.datamodel.vrp.OBVRPFundsConfirmationResponse;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.toFRDomesticVRPConsentDetails;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-11-17T13:54:56.728Z[Europe/London]")
@Controller("DomesticVrpConsentsApiControllerV3.1.8")
@Slf4j
public class DomesticVrpConsentsApiController implements DomesticVrpConsentsApi {

    private DomesticVRPConsentRepository domesticVRPConsentRepository;
    private TppRepository tppRepository;
    private ResourceLinkService resourceLinkService;
    private ConsentMetricService consentMetricService;

    public DomesticVrpConsentsApiController(
            DomesticVRPConsentRepository domesticVRPConsentRepository, TppRepository tppRepository,
            ResourceLinkService resourceLinkService, ConsentMetricService consentMetricService
    ) {
        this.domesticVRPConsentRepository = domesticVRPConsentRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity<OBDomesticVRPConsentResponse> domesticVrpConsentsPost(
            String authorization, String xIdempotencyKey, String xJwsSignature,
            OBDomesticVRPConsentRequest obDomesticVRPConsentRequest, String xFapiAuthDate,
            String xFapiCustomerIpAddress, String xFapiInteractionId, String xCustomerUserAgent,
            String clientId, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        log.debug("Received VRP consent: '{}'", obDomesticVRPConsentRequest);
        FRDomesticVRPConsentDetails frDomesticVRPDetails = toFRDomesticVRPConsentDetails(obDomesticVRPConsentRequest);
        log.trace("Converted OB VRP consent to: '{}'", frDomesticVRPDetails);
        final Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        return new ResponseEntity<OBDomesticVRPConsentResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<OBDomesticVRPConsentResponse> domesticVrpConsentsGet(
            String consentId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        return new ResponseEntity<OBDomesticVRPConsentResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Void> domesticVrpConsentsDelete(
            String consentId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<OBVRPFundsConfirmationResponse> domesticVrpConsentsFundsConfirmation(
            String consentId, String authorization, String xJwsSignature,
            OBVRPFundsConfirmationRequest obVRPFundsConfirmationRequest, String xFapiAuthDate,
            String xFapiCustomerIpAddress, String xFapiInteractionId, String xCustomerUserAgent,
            HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        return new ResponseEntity<OBVRPFundsConfirmationResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

=======

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-05-27T13:44:23.551801+01:00[Europe/London]")

@Controller("DomesticVrpConsentsApiControllerV3.1.8")
public class DomesticVrpConsentsApiController implements DomesticVrpConsentsApi {

    @Override
    public ResponseEntity<OBDomesticVRPConsentResponse> createDomesticVrpConsent(
            OBDomesticVRPConsentRequest domesticVRPConsentRequest
    ) throws OBErrorResponseException {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<OBDomesticVRPConsentResponse> getDomesticVrpConsent() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Void> deleteDomesticVrpConsent() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Override
    public ResponseEntity<Object> createDomesticVrpConsentsConsentFundsConfirmation(
            OBVRPFundsConfirmationRequest vrpFundsConfirmationRequest
    ) throws OBErrorResponseException {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
>>>>>>> 45ea6103 (Move Matts controllers to the payments/v3_1_8/vrp package)
}
