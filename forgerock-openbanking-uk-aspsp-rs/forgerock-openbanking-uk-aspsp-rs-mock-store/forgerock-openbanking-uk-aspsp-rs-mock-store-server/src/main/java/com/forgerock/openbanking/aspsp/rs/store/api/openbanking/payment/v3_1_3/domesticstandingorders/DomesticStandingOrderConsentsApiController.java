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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_3.domesticstandingorders;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.DomesticStandingOrderConsent3Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRDomesticStandingOrderConsent3;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsentResponse4;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsentResponse4Data;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsentResponse4Data.PermissionEnum;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.ConsentStatusCodeToResponseDataStatusConverter.toOBWriteDomesticStandingOrderConsentResponse4DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;
import static uk.org.openbanking.datamodel.service.converter.payment.OBConsentAuthorisationConverter.toOBWriteDomesticConsent3DataAuthorisation;
import static uk.org.openbanking.datamodel.service.converter.payment.OBDomesticStandingOrderConverter.toOBWriteDomesticStandingOrder3DataInitiation;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticStandingOrderConsentConverter.toOBWriteDomesticStandingOrderConsent3;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-22T14:20:48.770Z")

@Controller("DomesticStandingOrderConsentsApiV3.1.3")
@Slf4j
public class DomesticStandingOrderConsentsApiController implements DomesticStandingOrderConsentsApi {

    private final DomesticStandingOrderConsent3Repository domesticStandingOrderConsentRepository;
    private final TppRepository tppRepository;
    private final ResourceLinkService resourceLinkService;
    private final ConsentMetricService consentMetricService;

    @Autowired
    public DomesticStandingOrderConsentsApiController(ConsentMetricService consentMetricService, DomesticStandingOrderConsent3Repository domesticStandingOrderConsentRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.domesticStandingOrderConsentRepository = domesticStandingOrderConsentRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    public ResponseEntity<OBWriteDomesticStandingOrderConsentResponse4> createDomesticStandingOrderConsents(
            OBWriteDomesticStandingOrderConsent4 obWriteDomesticStandingOrderConsent4,
            String authorization,
            String xIdempotencyKey,
            String xJwsSignature,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            String clientId,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        log.debug("Received '{}'.", obWriteDomesticStandingOrderConsent4);

        Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        Optional<FRDomesticStandingOrderConsent3> consentByIdempotencyKey = domesticStandingOrderConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, obWriteDomesticStandingOrderConsent4, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getDomesticStandingOrderConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent...");

        FRDomesticStandingOrderConsent3 domesticStandingOrderConsent = FRDomesticStandingOrderConsent3.builder()
                .id(IntentType.PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .domesticStandingOrderConsent(toOBWriteDomesticStandingOrderConsent3(obWriteDomesticStandingOrderConsent4))
                .statusUpdate(DateTime.now())
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

    public ResponseEntity getDomesticStandingOrderConsentsConsentId(
            String consentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        Optional<FRDomesticStandingOrderConsent3> isDomesticStandingOrderConsent = domesticStandingOrderConsentRepository.findById(consentId);
        if (!isDomesticStandingOrderConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Domestic  consent '" + consentId + "' can't be found");
        }
        FRDomesticStandingOrderConsent3 domesticStandingOrderConsent = isDomesticStandingOrderConsent.get();

        return ResponseEntity.ok(packageResponse(domesticStandingOrderConsent));
    }

    private OBWriteDomesticStandingOrderConsentResponse4 packageResponse(FRDomesticStandingOrderConsent3 domesticStandingOrderConsent) {
        return new OBWriteDomesticStandingOrderConsentResponse4()
                .data(new OBWriteDomesticStandingOrderConsentResponse4Data()
                        .initiation(toOBWriteDomesticStandingOrder3DataInitiation(domesticStandingOrderConsent.getInitiation()))
                        .status(toOBWriteDomesticStandingOrderConsentResponse4DataStatus(domesticStandingOrderConsent.getStatus()))
                        .creationDateTime(domesticStandingOrderConsent.getCreated())
                        .statusUpdateDateTime(domesticStandingOrderConsent.getStatusUpdate())
                        .consentId(domesticStandingOrderConsent.getId())
                        .permission(PermissionEnum.valueOf(domesticStandingOrderConsent.getDomesticStandingOrderConsent().getData().getPermission().name()))
                        .authorisation(toOBWriteDomesticConsent3DataAuthorisation(domesticStandingOrderConsent.getDomesticStandingOrderConsent().getData().getAuthorisation()))
                )
                .links(resourceLinkService.toSelfLink(domesticStandingOrderConsent, discovery -> discovery.getV_3_1_3().getGetDomesticStandingOrderConsent()))
                .risk(domesticStandingOrderConsent.getRisk())
                .meta(new Meta());
    }

}
