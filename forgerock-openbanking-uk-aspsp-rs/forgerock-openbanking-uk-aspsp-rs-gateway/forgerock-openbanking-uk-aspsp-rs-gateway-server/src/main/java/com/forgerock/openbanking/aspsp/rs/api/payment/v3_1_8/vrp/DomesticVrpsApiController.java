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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_8.vrp;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.DomesticVrpPaymentsEndpointWrapper;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.vrp.DomesticVrpPaymentConsentService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.vrp.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Collections;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-11-17T13:54:56.728Z[Europe/London]")
@Controller("DomesticVrpsApiV3.1.8")
@Slf4j
public class DomesticVrpsApiController implements DomesticVrpsApi{


    private final DomesticVrpPaymentConsentService vrpPaymentConsentService;
    private final RSEndpointWrapperService rsEndpointWrapperService;
    private final RsStoreGateway rsStoreGateway;

    @Autowired
    public DomesticVrpsApiController(RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway
            , DomesticVrpPaymentConsentService vrpPaymentConsentService) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.rsStoreGateway = rsStoreGateway;
        this.vrpPaymentConsentService = vrpPaymentConsentService;
    }


    @Override
    public ResponseEntity<OBDomesticVRPResponse> domesticVrpGet(
            String domesticVRPId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        return new ResponseEntity<OBDomesticVRPResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<OBDomesticVRPDetails> domesticVrpPaymentDetailsGet(
            String domesticVRPId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        return new ResponseEntity<OBDomesticVRPDetails>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    /**
     *         @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
     *             @RequestHeader(value = "Authorization", required = true) String authorization,
     *
     *             @ApiParam(value = "A detached JWS signature of the body of the payload.", required = true)
     *             @RequestHeader(value = "x-jws-signature", required = true) String xJwsSignature,
     *
     *             @ApiParam(value = "Default", required = true)
     *             @Valid
     *             @RequestBody OBDomesticVRPRequest obDomesticVRPRequest,
     *
     *             @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
     *             @RequestHeader(value = "x-fapi-auth-date", required = false) String xFapiAuthDate,
     *
     *             @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
     *             @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,
     *
     *             @ApiParam(value = "An RFC4122 UID used as a correlation id.")
     *             @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,
     *
     *             @ApiParam(value = "Indicates the user-agent that the PSU is using.")
     *             @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,
     */
    public ResponseEntity<OBDomesticVRPResponse> domesticVrpPost(
            String authorization, String xJwsSignature, OBDomesticVRPRequest obDomesticVRPRequest, String xFapiAuthDate,
            String xFapiCustomerIpAddress, String xFapiInteractionId, String xCustomerUserAgent,
            HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        log.debug("domesticVrpPost() Recieved OBDomesticVrpRequest {}", obDomesticVRPRequest);
        @NotNull @Valid OBDomesticVRPInitiation initiation = obDomesticVRPRequest.getData().getInitiation();
        String consentId = obDomesticVRPRequest.getData().getConsentId();
        log.debug("domesticVrpPost() consentId is {}", consentId);
        // Need a consent service that gets 'payments' from the rs-store. Payments are actually consents poorly named
        // :-( -> technical debt
        // TODO Change payments services to consent services?
        FRDomesticVRPConsent consent = vrpPaymentConsentService.getVrpPayment(consentId);

        DomesticVrpPaymentsEndpointWrapper vrpPaymentsEndpointWrapper = rsEndpointWrapperService.vrpPaymentEndpoint();
        vrpPaymentsEndpointWrapper.authorization(authorization);
        vrpPaymentsEndpointWrapper.xFapiFinancialId(rsEndpointWrapperService.getRsConfiguration().financialId);
        vrpPaymentsEndpointWrapper.principal(principal);
        vrpPaymentsEndpointWrapper.payment(consent);
        vrpPaymentsEndpointWrapper.filters(f -> {
            f.verifyJwsDetachedSignature(xJwsSignature, request);
            f.validateRisk(obDomesticVRPRequest.getRisk());
            f.checkRequestAndConsentInitiationMatch(initiation, consent);
            f.checkRequestAndConsentRiskMatch(obDomesticVRPRequest, consent);
        });
        ResponseEntity responseEntity =  vrpPaymentsEndpointWrapper.execute((String tppId) -> {
            HttpHeaders additionalHeaders = new HttpHeaders();
            additionalHeaders.add("x-ob-client-id", tppId);
            return rsStoreGateway.toRsStore(
                    request, additionalHeaders, Collections.emptyMap(),
                    OBDomesticVRPConsentResponse.class, obDomesticVRPRequest
            );
        });

        return responseEntity;
    }
}
