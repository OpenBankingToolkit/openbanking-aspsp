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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_0.internationalstandingorders;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.InternationalStandingOrderConsent3Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRInternationalStandingOrderConsent3;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRStandingOrderPaymentConverter;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalStandingOrderConsentResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsentResponse1;
import uk.org.openbanking.datamodel.service.converter.payment.OBInternationalStandingOrderConverter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("InternationalStandingOrderConsentsApiV3.0")
@Slf4j
public class InternationalStandingOrderConsentsApiController implements InternationalStandingOrderConsentsApi {
    private final InternationalStandingOrderConsent3Repository internationalStandingOrderConsentRepository;
    private final TppRepository tppRepository;
    private final ResourceLinkService resourceLinkService;
    private ConsentMetricService consentMetricService;

    public InternationalStandingOrderConsentsApiController(ConsentMetricService consentMetricService, InternationalStandingOrderConsent3Repository internationalStandingOrderConsentRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.internationalStandingOrderConsentRepository = internationalStandingOrderConsentRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity<OBWriteInternationalStandingOrderConsentResponse1> createInternationalStandingOrderConsents(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBWriteInternationalStandingOrderConsent1 obWriteInternationalStandingOrderConsent1Param,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Every request will be processed only once per x-idempotency-key.  The Idempotency Key will be valid for 24 hours.", required = true)
            @RequestHeader(value = "x-idempotency-key", required = true) String xIdempotencyKey,

            @ApiParam(value = "A detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = true) String xJwsSignature,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @ApiParam(value = "The PISP ID")
            @RequestHeader(value = "x-ob-client-id", required = false) String clientId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        log.debug("Received '{}'.", obWriteInternationalStandingOrderConsent1Param);
        OBWriteInternationalStandingOrderConsent2 consent2 = OBInternationalStandingOrderConverter.toOBWriteInternationalStandingOrderConsent2(obWriteInternationalStandingOrderConsent1Param);
        log.trace("Converted request body to {}", consent2.getClass());

        final Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        Optional<FRInternationalStandingOrderConsent3> consentByIdempotencyKey = internationalStandingOrderConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, consent2, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getInternationalStandingOrderConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent.");

        FRInternationalStandingOrderConsent3 internationalStandingOrderConsent = FRInternationalStandingOrderConsent3.builder()
                .id(IntentType.PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .internationalStandingOrderConsent(FRStandingOrderPaymentConverter.toOBWriteInternationalStandingOrderConsent3(consent2))
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .created(DateTime.now())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        log.debug("Saving consent: {}", internationalStandingOrderConsent);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(internationalStandingOrderConsent.getId(), internationalStandingOrderConsent.getStatus().name()));
        internationalStandingOrderConsent = internationalStandingOrderConsentRepository.save(internationalStandingOrderConsent);
        log.info("Created consent id: {}", internationalStandingOrderConsent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(internationalStandingOrderConsent));
    }

    @Override
    public ResponseEntity getInternationalStandingOrderConsentsConsentId(
            @ApiParam(value = "ConsentId", required = true)
            @PathVariable("ConsentId") String consentId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        Optional<FRInternationalStandingOrderConsent3> isInternationalStandingOrderConsent = internationalStandingOrderConsentRepository.findById(consentId);
        if (!isInternationalStandingOrderConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("International  consent '" + consentId + "' can't be found");
        }
        FRInternationalStandingOrderConsent3 internationalStandingOrderConsent = isInternationalStandingOrderConsent.get();

        return ResponseEntity.ok(packageResponse(internationalStandingOrderConsent));
    }


    private OBWriteInternationalStandingOrderConsentResponse1 packageResponse(FRInternationalStandingOrderConsent3 internationalStandingOrderConsent) {
        return new OBWriteInternationalStandingOrderConsentResponse1()
                .data(new OBWriteDataInternationalStandingOrderConsentResponse1()
                        .initiation(FRStandingOrderPaymentConverter.toOBInternationalStandingOrder1(internationalStandingOrderConsent.getInitiation()))
                        .status(internationalStandingOrderConsent.getStatus().toOBExternalConsentStatus1Code())
                        .creationDateTime(internationalStandingOrderConsent.getCreated())
                        .statusUpdateDateTime(internationalStandingOrderConsent.getStatusUpdate())
                        .consentId(internationalStandingOrderConsent.getId())
                        .permission(internationalStandingOrderConsent.getInternationalStandingOrderConsent().getData().getPermission())
                        .authorisation(internationalStandingOrderConsent.getInternationalStandingOrderConsent().getData().getAuthorisation())
                )
                .risk(internationalStandingOrderConsent.getRisk())
                .links(resourceLinkService.toSelfLink(internationalStandingOrderConsent, discovery -> discovery.getV_3_0().getGetInternationalStandingOrderConsent()))
                .meta(new Meta());
    }
}
