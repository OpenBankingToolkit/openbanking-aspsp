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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_0.domesticscheduledpayments;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.DomesticScheduledConsent5Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticScheduledConsent5;
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
import uk.org.openbanking.datamodel.payment.OBExternalPermissions2Code;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticScheduledConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticScheduledConsentResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent4Data;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsentResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticstandingorders.DomesticStandingOrderConsentsApiController.toOBWriteDomesticConsent4DataAuthorisation;
import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDomesticStandingOrderConsentConverter.toOBAuthorisation1;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static uk.org.openbanking.datamodel.service.converter.payment.OBDomesticScheduledConverter.toOBDomesticScheduled1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBDomesticScheduledConverter.toOBWriteDomesticScheduled2DataInitiation;

@Controller("DomesticScheduledPaymentConsentsApiV3.0")
@Slf4j
public class DomesticScheduledPaymentConsentsApiController implements DomesticScheduledPaymentConsentsApi {
    private final DomesticScheduledConsent5Repository domesticScheduledConsentRepository;
    private TppRepository tppRepository;
    private ResourceLinkService resourceLinkService;
    private ConsentMetricService consentMetricService;

    public DomesticScheduledPaymentConsentsApiController(ConsentMetricService consentMetricService, DomesticScheduledConsent5Repository domesticScheduledConsentRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.domesticScheduledConsentRepository = domesticScheduledConsentRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity<OBWriteDomesticScheduledConsentResponse1> createDomesticScheduledPaymentConsents(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBWriteDomesticScheduledConsent1 obWriteDomesticScheduledConsent1,

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
        log.debug("Received '{}'.", obWriteDomesticScheduledConsent1);
        OBWriteDomesticScheduledConsent4 consent4 = toOBWriteDomesticScheduledConsent4(obWriteDomesticScheduledConsent1);
        log.trace("Converted request body to {}", consent4.getClass());

        final Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        Optional<FRDomesticScheduledConsent5> consentByIdempotencyKey = domesticScheduledConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, consent4, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getDomesticScheduledConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent...");

        FRDomesticScheduledConsent5 domesticScheduledConsent = FRDomesticScheduledConsent5.builder()
                .id(IntentType.PAYMENT_DOMESTIC_SCHEDULED_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .domesticScheduledConsent(consent4)
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .created(DateTime.now())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();

        consentMetricService.sendConsentActivity(new ConsentStatusEntry(domesticScheduledConsent.getId(), domesticScheduledConsent.getStatus().name()));
        log.debug("Saving consent: {}", domesticScheduledConsent);
        domesticScheduledConsent = domesticScheduledConsentRepository.save(domesticScheduledConsent);
        log.info("Created consent id: {}", domesticScheduledConsent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(domesticScheduledConsent));
    }

    @Override
    public ResponseEntity getDomesticScheduledPaymentConsentsConsentId(
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
        Optional<FRDomesticScheduledConsent5> isDomesticScheduledConsent = domesticScheduledConsentRepository.findById(consentId);
        if (!isDomesticScheduledConsent.isPresent()) {
            // OB specifies a 400 when the id does not match an existing consent
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Domestic scheduled consent '" + consentId + "' can't be found");
        }
        FRDomesticScheduledConsent5 domesticScheduledConsent = isDomesticScheduledConsent.get();

        return ResponseEntity.ok(packageResponse(domesticScheduledConsent));
    }

    private OBWriteDomesticScheduledConsentResponse1 packageResponse(FRDomesticScheduledConsent5 domesticScheduledConsent) {
        return new OBWriteDomesticScheduledConsentResponse1()
                .data(new OBWriteDataDomesticScheduledConsentResponse1()
                        .initiation(toOBDomesticScheduled1(domesticScheduledConsent.getInitiation()))
                        .status(domesticScheduledConsent.getStatus().toOBExternalConsentStatus1Code())
                        .creationDateTime(domesticScheduledConsent.getCreated())
                        .statusUpdateDateTime(domesticScheduledConsent.getStatusUpdate())
                        .consentId(domesticScheduledConsent.getId())
                        .permission(OBExternalPermissions2Code.valueOf(domesticScheduledConsent.getDomesticScheduledConsent().getData().getPermission().name()))
                        .authorisation(toOBAuthorisation1(domesticScheduledConsent.getDomesticScheduledConsent().getData().getAuthorisation()))

                )
                .links(resourceLinkService.toSelfLink(domesticScheduledConsent, discovery -> discovery.getV_3_0().getGetDomesticScheduledPaymentConsent()))
                .risk(domesticScheduledConsent.getRisk())
                .meta(new Meta());
    }

    // TODO #272 - move to uk-datamodel
    public static OBWriteDomesticScheduledConsent4 toOBWriteDomesticScheduledConsent4(OBWriteDomesticScheduledConsent1 obWriteDomesticScheduledConsent1) {
        return (new OBWriteDomesticScheduledConsent4())
                .data(toOBWriteDomesticScheduledConsent4Data(obWriteDomesticScheduledConsent1.getData()))
                .risk(obWriteDomesticScheduledConsent1.getRisk());
    }

    public static OBWriteDomesticScheduledConsent4Data toOBWriteDomesticScheduledConsent4Data(OBWriteDataDomesticScheduledConsent1 data) {
        return data == null ? null : (new OBWriteDomesticScheduledConsent4Data())
                .permission(OBWriteDomesticScheduledConsent4Data.PermissionEnum.valueOf(data.getPermission().name()))
                .readRefundAccount(null)
                .initiation(toOBWriteDomesticScheduled2DataInitiation(data.getInitiation()))
                .authorisation(toOBWriteDomesticConsent4DataAuthorisation(data.getAuthorisation()))
                .scASupportData(null);
    }

}
