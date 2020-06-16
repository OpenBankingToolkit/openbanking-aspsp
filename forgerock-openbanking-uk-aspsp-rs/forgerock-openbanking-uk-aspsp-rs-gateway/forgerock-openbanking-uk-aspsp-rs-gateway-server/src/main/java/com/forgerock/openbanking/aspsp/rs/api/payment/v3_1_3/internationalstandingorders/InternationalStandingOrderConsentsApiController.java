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
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_3.internationalstandingorders;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsent5;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsentResponse5;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-22T14:20:48.770Z")

@Controller("InternationalStandingOrderConsentsApiV3.1.3")
public class InternationalStandingOrderConsentsApiController implements InternationalStandingOrderConsentsApi {

    private final RSEndpointWrapperService rsEndpointWrapperService;
    private final RsStoreGateway rsStoreGateway;

    public InternationalStandingOrderConsentsApiController(RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.rsStoreGateway = rsStoreGateway;
    }

    public ResponseEntity<OBWriteInternationalStandingOrderConsentResponse5> createInternationalStandingOrderConsents(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBWriteInternationalStandingOrderConsent5 obWriteInternationalStandingOrderConsent5Param,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Every request will be processed only once per x-idempotency-key.  The Idempotency Key will be valid for 24 hours. ", required = true)
            @RequestHeader(value = "x-idempotency-key", required = true) String xIdempotencyKey,

            @ApiParam(value = "A detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = true) String xJwsSignature,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-auth-date", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiAuthDate,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.paymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .principal(principal)
                .filters(f -> {
                            f.verifyIdempotencyKeyLength(xIdempotencyKey);
                            f.verifyJwsDetachedSignature(xJwsSignature, request);
                        }
                )
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-client-id", tppId);

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBWriteInternationalStandingOrderConsentResponse5.class, obWriteInternationalStandingOrderConsent5Param);
                        }
                );
    }

    public ResponseEntity<OBWriteInternationalStandingOrderConsentResponse5> getInternationalStandingOrderConsentsConsentId(

            @ApiParam(value = "ConsentId", required = true)
            @PathVariable("ConsentId") String consentId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-auth-date", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiAuthDate,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.paymentEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .principal(principal)
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-client-id", tppId);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBWriteInternationalStandingOrderConsentResponse5.class);
                        }
                );
    }

}