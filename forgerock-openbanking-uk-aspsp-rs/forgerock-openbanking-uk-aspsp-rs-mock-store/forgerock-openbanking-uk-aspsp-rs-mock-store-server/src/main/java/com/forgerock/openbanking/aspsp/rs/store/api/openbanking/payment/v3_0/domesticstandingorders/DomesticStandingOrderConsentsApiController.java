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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_0.domesticstandingorders;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.DomesticStandingOrderConsent5Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticStandingOrderConsent5;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.payment.OBDomesticStandingOrder1;
import uk.org.openbanking.datamodel.payment.OBExternalPermissions2Code;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticStandingOrderConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticStandingOrderConsentResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3DataInitiation;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsent5;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsent5Data;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsentResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticstandingorders.DomesticStandingOrderConsentsApiController.toOBWriteDomesticStandingOrder3DataInitiationCreditorAccount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticstandingorders.DomesticStandingOrderConsentsApiController.toOBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticstandingorders.DomesticStandingOrderConsentsApiController.toOBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticstandingorders.DomesticStandingOrderConsentsApiController.toOBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticstandingorders.DomesticStandingOrderConsentsApiController.toOBWriteDomesticConsent4DataAuthorisation;
import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDomesticStandingOrderConsentConverter.toOBAuthorisation1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDomesticStandingOrderConsentConverter.toOBDomesticStandingOrder1;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static uk.org.openbanking.datamodel.service.converter.payment.OBAccountConverter.toOBWriteDomesticStandingOrder3DataInitiationDebtorAccount;

@Controller("DomesticStandingOrderConsentsApiV3.0")
@Slf4j
public class DomesticStandingOrderConsentsApiController implements DomesticStandingOrderConsentsApi {

    private final DomesticStandingOrderConsent5Repository domesticStandingOrderConsentRepository;
    private final TppRepository tppRepository;
    private final ResourceLinkService resourceLinkService;
    private ConsentMetricService consentMetricService;

    @Autowired
    public DomesticStandingOrderConsentsApiController(ConsentMetricService consentMetricService, DomesticStandingOrderConsent5Repository domesticStandingOrderConsentRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.domesticStandingOrderConsentRepository = domesticStandingOrderConsentRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity<OBWriteDomesticStandingOrderConsentResponse1> createDomesticStandingOrderConsents(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBWriteDomesticStandingOrderConsent1 obWriteDomesticStandingOrderConsent1,

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
        log.debug("Received '{}'.", obWriteDomesticStandingOrderConsent1);
        OBWriteDomesticStandingOrderConsent5 consent5 = toOBWriteDomesticStandingOrderConsent5(obWriteDomesticStandingOrderConsent1);
        log.trace("Converted request body to {}", consent5.getClass());

        final Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        Optional<FRDomesticStandingOrderConsent5> consentByIdempotencyKey = domesticStandingOrderConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, consent5, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getDomesticStandingOrderConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent...");

        FRDomesticStandingOrderConsent5 domesticStandingOrderConsent = FRDomesticStandingOrderConsent5.builder()
                .id(IntentType.PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .domesticStandingOrderConsent(consent5)
                .statusUpdate(DateTime.now())
                .created(DateTime.now())
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        log.debug("Saving consent: {}", domesticStandingOrderConsent);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(domesticStandingOrderConsent.getId(), domesticStandingOrderConsent.getStatus().name()));
        domesticStandingOrderConsent = domesticStandingOrderConsentRepository.save(domesticStandingOrderConsent);
        log.info("Created consent id: {}", domesticStandingOrderConsent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(domesticStandingOrderConsent));
    }

    @Override
    public ResponseEntity getDomesticStandingOrderConsentsConsentId(
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
        Optional<FRDomesticStandingOrderConsent5> isDomesticStandingOrderConsent = domesticStandingOrderConsentRepository.findById(consentId);
        if (!isDomesticStandingOrderConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Domestic  consent '" + consentId + "' can't be found");
        }
        FRDomesticStandingOrderConsent5 domesticStandingOrderConsent = isDomesticStandingOrderConsent.get();

        return ResponseEntity.ok(packageResponse(domesticStandingOrderConsent));
    }

    private OBWriteDomesticStandingOrderConsentResponse1 packageResponse(FRDomesticStandingOrderConsent5 domesticStandingOrderConsent) {
        return new OBWriteDomesticStandingOrderConsentResponse1()
                .data(new OBWriteDataDomesticStandingOrderConsentResponse1()
                        .initiation(toOBDomesticStandingOrder1(domesticStandingOrderConsent.getInitiation()))
                        .status(domesticStandingOrderConsent.getStatus().toOBExternalConsentStatus1Code())
                        .creationDateTime(domesticStandingOrderConsent.getCreated())
                        .statusUpdateDateTime(domesticStandingOrderConsent.getStatusUpdate())
                        .consentId(domesticStandingOrderConsent.getId())
                        .permission(OBExternalPermissions2Code.valueOf(domesticStandingOrderConsent.getDomesticStandingOrderConsent().getData().getPermission().name()))
                        .authorisation(toOBAuthorisation1(domesticStandingOrderConsent.getDomesticStandingOrderConsent().getData().getAuthorisation()))
                )
                .links(resourceLinkService.toSelfLink(domesticStandingOrderConsent, discovery -> discovery.getV_3_0().getGetDomesticStandingOrderConsent()))
                .risk(domesticStandingOrderConsent.getRisk())
                .meta(new Meta());
    }

    // TODO #272 move to uk-datamodel
    public static OBWriteDomesticStandingOrderConsent5 toOBWriteDomesticStandingOrderConsent5(OBWriteDomesticStandingOrderConsent1 obWriteDomesticStandingOrderConsent1) {
        return (new OBWriteDomesticStandingOrderConsent5())
                .data(toOBWriteDomesticStandingOrderConsent5Data(obWriteDomesticStandingOrderConsent1.getData()))
                .risk(obWriteDomesticStandingOrderConsent1.getRisk());
    }

    public static OBWriteDomesticStandingOrderConsent5Data toOBWriteDomesticStandingOrderConsent5Data(OBWriteDataDomesticStandingOrderConsent1 data) {
        return data == null ? null : (new OBWriteDomesticStandingOrderConsent5Data())
                .permission(OBWriteDomesticStandingOrderConsent5Data.PermissionEnum.valueOf(data.getPermission().name()))
                .readRefundAccount(null)
                .initiation(toOBWriteDomesticStandingOrder3DataInitiation(data.getInitiation()))
                .authorisation(toOBWriteDomesticConsent4DataAuthorisation(data.getAuthorisation()))
                .scASupportData(null);
    }

    public static OBWriteDomesticStandingOrder3DataInitiation toOBWriteDomesticStandingOrder3DataInitiation(OBDomesticStandingOrder1 initiation) {
        return initiation == null ? null : (new OBWriteDomesticStandingOrder3DataInitiation())
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toOBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount(initiation.getFirstPaymentAmount()))
                .recurringPaymentAmount(toOBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount(initiation.getRecurringPaymentAmount()))
                .finalPaymentAmount(toOBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount(initiation.getFinalPaymentAmount()))
                .debtorAccount(toOBWriteDomesticStandingOrder3DataInitiationDebtorAccount(initiation.getDebtorAccount()))
                .creditorAccount(toOBWriteDomesticStandingOrder3DataInitiationCreditorAccount(initiation.getCreditorAccount()))
                .supplementaryData(null);
    }

}
