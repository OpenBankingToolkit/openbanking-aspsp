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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_10.vrp;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.constants.OpenBankingHttpHeaders;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentResponse;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBVRPFundsConfirmationRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBVRPFundsConfirmationResponse;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-11-17T13:54:56.728Z[Europe/London]")
@Controller("DomesticVrpConsentsApiV3.1.10")
@Slf4j
public class DomesticVrpConsentsApiController implements DomesticVrpConsentsApi {

    @Autowired
    private RSEndpointWrapperService rsEndpointWrapperService;
    @Autowired
    private RsStoreGateway rsStoreGateway;

    @Override
    public ResponseEntity domesticVrpConsentsPost(
            String authorization, String xIdempotencyKey, String xJwsSignature,
            OBDomesticVRPConsentRequest obDomesticVRPConsentRequest, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        log.debug("Request to create a VRP consent received, interactionId '{}'", xFapiInteractionId);
        return rsEndpointWrapperService.vrpPaymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiInteractionId)
                .principal(principal)
                .obVersion(OBVersion.v3_1_10)
                .filters(f -> {
                    f.verifyIdempotencyKeyLength(xIdempotencyKey);
                    f.verifyJwsDetachedSignature(xJwsSignature, request);
                })
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHeaders = new HttpHeaders();
                            additionalHeaders.add("x-ob-client-id", tppId);
                            return rsStoreGateway.toRsStore(
                                    request, additionalHeaders, Collections.emptyMap(),
                                    OBDomesticVRPConsentResponse.class, obDomesticVRPConsentRequest
                            );
                        }
                );
    }

    @Override
    public ResponseEntity domesticVrpConsentsGet(
            String consentId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        log.debug("Request to get a VRP consent received, consentId '{}'", consentId);
        return rsEndpointWrapperService.vrpPaymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiInteractionId)
                .principal(principal)
                .obVersion(OBVersion.v3_1_10)
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBDomesticVRPConsentResponse.class);
                        }
                );
    }

    @Override
    public ResponseEntity domesticVrpConsentsDelete(
            String consentId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.vrpPaymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiInteractionId)
                .principal(principal)
                .obVersion(OBVersion.v3_1_10)
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Void.class);
                        }
                );
    }

    @Override
    public ResponseEntity domesticVrpConsentsFundsConfirmation(
            String consentId, String authorization, String xJwsSignature,
            OBVRPFundsConfirmationRequest obVRPFundsConfirmationRequest, String xFapiAuthDate,
            String xFapiCustomerIpAddress, String xFapiInteractionId, String xCustomerUserAgent,
            HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        log.debug("Request to get a VRP funds confirmation, consentId '{}'", consentId);
        if (!consentId.equals(obVRPFundsConfirmationRequest.getData().getConsentId())) {
            log.error(
                    "The consent ID '{}' path parameter does not match with the consent ID '{}' requested to confirm the funds.",
                    consentId, obVRPFundsConfirmationRequest.getData().getConsentId()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The consent ID '" + consentId +
                    "' path parameter does not match with the consent ID '" +
                    obVRPFundsConfirmationRequest.getData().getConsentId() + "' requested to confirm the funds.");
        }
        log.debug("(domesticVrpConsentsFundsConfirmation) Request mode test: '{}'", StringUtils.hasLength(request.getHeader(OpenBankingHttpHeaders.X_OB_MODE_TEST)));
        return rsEndpointWrapperService.vrpPaymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiInteractionId)
                .principal(principal)
                .isAuthorizationCodeGrantType(true)
                .obVersion(OBVersion.v3_1_10)
                .filters(f -> {
                    f.verifyJwsDetachedSignature(xJwsSignature, request);
                })
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHeaders = new HttpHeaders();
                            return rsStoreGateway.toRsStore(
                                    request, additionalHeaders, Collections.emptyMap(),
                                    OBVRPFundsConfirmationResponse.class, obVRPFundsConfirmationRequest
                            );
                        }
                );
    }
}
