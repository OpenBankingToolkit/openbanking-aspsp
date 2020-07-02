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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_3.internationalstandingorders;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.payments.InternationalStandingOrderConsent4Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalStandingOrderConsent4;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsent5;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsentResponse5;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsentResponse5Data;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsentResponse5Data.PermissionEnum;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.ConsentStatusCodeToResponseDataStatusConverter.toOBWriteInternationalStandingOrderConsentResponse5DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-22T14:20:48.770Z")

@Controller("InternationalStandingOrderConsentsApiV3.1.3")
@Slf4j
public class InternationalStandingOrderConsentsApiController implements InternationalStandingOrderConsentsApi {

    private final InternationalStandingOrderConsent4Repository internationalStandingOrderConsentRepository;
    private final TppRepository tppRepository;
    private final ResourceLinkService resourceLinkService;
    private final ConsentMetricService consentMetricService;

    public InternationalStandingOrderConsentsApiController(ConsentMetricService consentMetricService, InternationalStandingOrderConsent4Repository internationalStandingOrderConsentRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.internationalStandingOrderConsentRepository = internationalStandingOrderConsentRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    public ResponseEntity<OBWriteInternationalStandingOrderConsentResponse5> createInternationalStandingOrderConsents(
            OBWriteInternationalStandingOrderConsent5 obWriteInternationalStandingOrderConsent5,
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
        log.debug("Received '{}'.", obWriteInternationalStandingOrderConsent5);

        Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        Optional<FRInternationalStandingOrderConsent4> consentByIdempotencyKey = internationalStandingOrderConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, obWriteInternationalStandingOrderConsent5, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getInternationalStandingOrderConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent.");

        FRInternationalStandingOrderConsent4 internationalStandingOrderConsent = FRInternationalStandingOrderConsent4.builder()
                .id(IntentType.PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .internationalStandingOrderConsent(obWriteInternationalStandingOrderConsent5)
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .idempotencyKey(xIdempotencyKey)
                .version(VersionPathExtractor.getVersionFromPath(request))
                .build();
        log.debug("Saving consent: {}", internationalStandingOrderConsent);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(internationalStandingOrderConsent.getId(), internationalStandingOrderConsent.getStatus().name()));
        internationalStandingOrderConsent = internationalStandingOrderConsentRepository.save(internationalStandingOrderConsent);
        log.info("Created consent id: {}", internationalStandingOrderConsent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(internationalStandingOrderConsent));
    }

    public ResponseEntity getInternationalStandingOrderConsentsConsentId(
            String consentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        Optional<FRInternationalStandingOrderConsent4> isInternationalStandingOrderConsent = internationalStandingOrderConsentRepository.findById(consentId);
        if (!isInternationalStandingOrderConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("International  consent '" + consentId + "' can't be found");
        }
        FRInternationalStandingOrderConsent4 internationalStandingOrderConsent = isInternationalStandingOrderConsent.get();

        return ResponseEntity.ok(packageResponse(internationalStandingOrderConsent));
    }

    private OBWriteInternationalStandingOrderConsentResponse5 packageResponse(FRInternationalStandingOrderConsent4 internationalStandingOrderConsent) {
        return new OBWriteInternationalStandingOrderConsentResponse5()
                .data(new OBWriteInternationalStandingOrderConsentResponse5Data()
                        .initiation(internationalStandingOrderConsent.getInitiation())
                        .status(toOBWriteInternationalStandingOrderConsentResponse5DataStatus(internationalStandingOrderConsent.getStatus()))
                        .creationDateTime(internationalStandingOrderConsent.getCreated())
                        .statusUpdateDateTime(internationalStandingOrderConsent.getStatusUpdate())
                        .consentId(internationalStandingOrderConsent.getId())
                        .permission(PermissionEnum.valueOf(internationalStandingOrderConsent.getInternationalStandingOrderConsent().getData().getPermission().name()))
                        .authorisation(internationalStandingOrderConsent.getInternationalStandingOrderConsent().getData().getAuthorisation())
                ).risk(internationalStandingOrderConsent.getRisk())
                .links(resourceLinkService.toSelfLink(internationalStandingOrderConsent, discovery -> getVersion(discovery).getGetInternationalStandingOrderConsent()))
                .meta(new Meta());
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_3();
    }

}
