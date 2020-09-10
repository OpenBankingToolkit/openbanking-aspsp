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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticpayments;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.DomesticConsent5Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticConsent5;
import com.forgerock.openbanking.common.services.openbanking.FundsAvailabilityService;
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
import uk.org.openbanking.datamodel.payment.OBFundsAvailableResult1;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticConsentResponse2;
import uk.org.openbanking.datamodel.payment.OBWriteDataFundsConfirmationResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsentResponse2;
import uk.org.openbanking.datamodel.payment.OBWriteFundsConfirmationResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static uk.org.openbanking.datamodel.service.converter.payment.OBConsentAuthorisationConverter.toOBAuthorisation1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBDomesticConverter.toOBDomestic2;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticConsentConverter.toOBWriteDomesticConsent4;

@Controller("DomesticPaymentConsentsApiV3.1")
@Slf4j
public class DomesticPaymentConsentsApiController implements DomesticPaymentConsentsApi {
    private final DomesticConsent5Repository domesticConsentRepository;
    private final TppRepository tppRepository;
    private final FundsAvailabilityService fundsAvailabilityService;
    private final ResourceLinkService resourceLinkService;
    private ConsentMetricService consentMetricService;

    public DomesticPaymentConsentsApiController(DomesticConsent5Repository domesticConsentRepository,
                                                TppRepository tppRepository,
                                                FundsAvailabilityService fundsAvailabilityService,
                                                ResourceLinkService resourceLinkService,
                                                ConsentMetricService consentMetricService) {
        this.domesticConsentRepository = domesticConsentRepository;
        this.tppRepository = tppRepository;
        this.fundsAvailabilityService = fundsAvailabilityService;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity<OBWriteDomesticConsentResponse2> createDomesticPaymentConsents(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBWriteDomesticConsent2 obWriteDomesticConsent2,

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
        log.debug("Received '{}'.", obWriteDomesticConsent2);
        OBWriteDomesticConsent4 consent4 = toOBWriteDomesticConsent4(obWriteDomesticConsent2);
        log.trace("Converted request body to {}", consent4.getClass());

        final Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);

        Optional<FRDomesticConsent5> consentByIdempotencyKey = domesticConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, consent4, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getDomesticConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent.");

        FRDomesticConsent5 domesticConsent = FRDomesticConsent5.builder()
                .id(IntentType.PAYMENT_DOMESTIC_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .domesticConsent(consent4)
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        log.debug("Saving consent: {}", domesticConsent);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(domesticConsent.getId(), domesticConsent.getStatus().name()));
        domesticConsent = domesticConsentRepository.save(domesticConsent);
        log.info("Created consent id: {}", domesticConsent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(domesticConsent));
    }

    @Override
    public ResponseEntity getDomesticPaymentConsentsConsentId(
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
        Optional<FRDomesticConsent5> isDomesticConsent = domesticConsentRepository.findById(consentId);
        if (!isDomesticConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Domestic consent '" + consentId + "' can't be found");
        }
        FRDomesticConsent5 domesticConsent = isDomesticConsent.get();

        return ResponseEntity.ok(packageResponse(domesticConsent));
    }

    @Override
    public ResponseEntity getDomesticPaymentConsentsConsentIdFundsConfirmation(
            @ApiParam(value = "ConsentId", required = true)
            @PathVariable("ConsentId") String consentId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are " +
                    "represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-customer-last-logged-time", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        Optional<FRDomesticConsent5> isDomesticConsent = domesticConsentRepository.findById(consentId);
        if (!isDomesticConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Domestic consent '" + consentId + "' can't be found");
        }
        FRDomesticConsent5 domesticConsent = isDomesticConsent.get();

        // Check if funds are available on the account selected in consent
        boolean areFundsAvailable = fundsAvailabilityService.isFundsAvailable(
                domesticConsent.getAccountId(),
                domesticConsent.getInitiation().getInstructedAmount().getAmount());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new OBWriteFundsConfirmationResponse1()
                        .data(new OBWriteDataFundsConfirmationResponse1()
                                .fundsAvailableResult(new OBFundsAvailableResult1()
                                        .fundsAvailable(areFundsAvailable)
                                        .fundsAvailableDateTime(DateTime.now())
                                ))
                        .links(PaginationUtil.generateLinksOnePager(httpUrl))
                        .meta(new Meta())
                );
    }

    private OBWriteDomesticConsentResponse2 packageResponse(FRDomesticConsent5 domesticConsent) {
        return new OBWriteDomesticConsentResponse2()
                .data(new OBWriteDataDomesticConsentResponse2()
                        .initiation(toOBDomestic2(domesticConsent.getInitiation()))
                        .status(domesticConsent.getStatus().toOBExternalConsentStatus1Code())
                        .creationDateTime(domesticConsent.getCreated())
                        .statusUpdateDateTime(domesticConsent.getStatusUpdate())
                        .consentId(domesticConsent.getId())
                        .authorisation(toOBAuthorisation1(domesticConsent.getDomesticConsent().getData().getAuthorisation()))
                )
                .links(resourceLinkService.toSelfLink(domesticConsent, discovery -> discovery.getV_3_1().getGetDomesticPaymentConsent()))
                .risk(domesticConsent.getRisk())
                .meta(new Meta());
    }

}
