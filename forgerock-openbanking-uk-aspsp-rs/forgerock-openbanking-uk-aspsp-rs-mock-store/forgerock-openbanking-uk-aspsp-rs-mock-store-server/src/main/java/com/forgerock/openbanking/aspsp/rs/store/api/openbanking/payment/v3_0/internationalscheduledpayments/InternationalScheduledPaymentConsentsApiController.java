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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_0.internationalscheduledpayments;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalScheduledConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalScheduledConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalScheduledDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalScheduledConsent;
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
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalScheduledConsentResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsentResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataAuthorisationConverter.toOBAuthorisation1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPermissionConverter.toOBExternalPermissions2Code;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConsentConverter.toFRWriteInternationalScheduledConsent;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConsentConverter.toOBInternationalScheduled1;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static uk.org.openbanking.datamodel.service.converter.payment.OBExchangeRateConverter.toOBExchangeRate2;

@Controller("InternationalScheduledPaymentConsentsApiV3.0")
@Slf4j
public class InternationalScheduledPaymentConsentsApiController implements InternationalScheduledPaymentConsentsApi {
    private final InternationalScheduledConsentRepository internationalScheduledConsentRepository;
    private final TppRepository tppRepository;
    private final ResourceLinkService resourceLinkService;
    private ConsentMetricService consentMetricService;

    public InternationalScheduledPaymentConsentsApiController(ConsentMetricService consentMetricService, InternationalScheduledConsentRepository internationalScheduledConsentRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.internationalScheduledConsentRepository = internationalScheduledConsentRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity<OBWriteInternationalScheduledConsentResponse1> createInternationalScheduledPaymentConsents(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBWriteInternationalScheduledConsent1 obWriteInternationalScheduledConsent1,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Every request will be processed only once per x-idempotency-key.  The Idempotency Key will be valid for 24 hours.", required = true)
            @RequestHeader(value = "x-idempotency-key", required = true) String xIdempotencyKey,

            @ApiParam(value = "A detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = true) String xJwsSignature,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-customer-last-logged-time", required = false)
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
        log.debug("Received: '{}'", obWriteInternationalScheduledConsent1);
        FRWriteInternationalScheduledConsent frWriteInternationalScheduledConsent = toFRWriteInternationalScheduledConsent(obWriteInternationalScheduledConsent1);
        log.trace("Converted to: '{}'", frWriteInternationalScheduledConsent);

        final Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        Optional<FRInternationalScheduledConsent> consentByIdempotencyKey = internationalScheduledConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, frWriteInternationalScheduledConsent, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getInternationalScheduledConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent.");

        FRInternationalScheduledConsent internationalScheduledConsent = FRInternationalScheduledConsent.builder()
                .id(IntentType.PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .internationalScheduledConsent(frWriteInternationalScheduledConsent)
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .created(DateTime.now())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        log.debug("Saving consent: '{}'", internationalScheduledConsent);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(internationalScheduledConsent.getId(), internationalScheduledConsent.getStatus().name()));
        internationalScheduledConsent = internationalScheduledConsentRepository.save(internationalScheduledConsent);
        log.info("Created consent id: '{}'", internationalScheduledConsent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(internationalScheduledConsent));
    }

    @Override
    public ResponseEntity getInternationalScheduledPaymentConsentsConsentId(
            @ApiParam(value = "ConsentId", required = true)
            @PathVariable("ConsentId") String consentId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-customer-last-logged-time", required = false)
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
        Optional<FRInternationalScheduledConsent> isScheduledConsent = internationalScheduledConsentRepository.findById(consentId);
        if (!isScheduledConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("International scheduled consent '" + consentId + "' can't be found");
        }
        FRInternationalScheduledConsent internationalScheduledConsent = isScheduledConsent.get();

        return ResponseEntity.ok(packageResponse(internationalScheduledConsent));
    }

    private OBWriteInternationalScheduledConsentResponse1 packageResponse(FRInternationalScheduledConsent internationalScheduledConsent) {
        FRWriteInternationalScheduledDataInitiation initiation = internationalScheduledConsent.getInitiation();
        return new OBWriteInternationalScheduledConsentResponse1()
                .data(new OBWriteDataInternationalScheduledConsentResponse1()
                        .initiation(toOBInternationalScheduled1(initiation))
                        .status(internationalScheduledConsent.getStatus().toOBExternalConsentStatus1Code())
                        .creationDateTime(internationalScheduledConsent.getCreated())
                        .statusUpdateDateTime(internationalScheduledConsent.getStatusUpdate())
                        .consentId(internationalScheduledConsent.getId())
                        .exchangeRateInformation(toOBExchangeRate2(internationalScheduledConsent.getCalculatedExchangeRate()))
                        .permission(toOBExternalPermissions2Code(internationalScheduledConsent.internationalScheduledConsent.getData().getPermission()))
                        .expectedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                        .authorisation(toOBAuthorisation1(internationalScheduledConsent.getInternationalScheduledConsent().getData().getAuthorisation()))
                )
                .risk(toOBRisk1(internationalScheduledConsent.getRisk()))
                .links(resourceLinkService.toSelfLink(internationalScheduledConsent, discovery -> discovery.getV_3_0().getGetInternationalScheduledPaymentConsent()))
                .meta(new Meta());
    }
}
