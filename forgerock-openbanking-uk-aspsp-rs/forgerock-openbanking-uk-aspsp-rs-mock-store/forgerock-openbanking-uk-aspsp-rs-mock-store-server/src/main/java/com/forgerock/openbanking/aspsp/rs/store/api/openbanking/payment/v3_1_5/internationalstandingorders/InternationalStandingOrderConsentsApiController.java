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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_5.internationalstandingorders;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalStandingOrderConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalStandingOrderConsent;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsent6;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsentResponse7;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsentResponse7Data;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.forgerock.converter.v3_1_5.ConsentStatusCodeToResponseDataStatusConverter.toOBWriteInternationalStandingOrderConsentResponse7DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialIdentificationConverter.toOBDebtorIdentification1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataAuthorisationConverter.toOBWriteDomesticConsent4DataAuthorisation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPermissionConverter.toOBWriteInternationalStandingOrderConsentResponse7DataPermission;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toFRWriteInternationalStandingOrderConsent;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toOBWriteInternationalStandingOrder4DataInitiation;

@Controller("InternationalStandingOrderConsentsApiV3.1.5")
@Slf4j
public class InternationalStandingOrderConsentsApiController implements InternationalStandingOrderConsentsApi {

    private final InternationalStandingOrderConsentRepository internationalStandingOrderConsentRepository;
    private final TppRepository tppRepository;
    private final ResourceLinkService resourceLinkService;
    private final ConsentMetricService consentMetricService;

    public InternationalStandingOrderConsentsApiController(ConsentMetricService consentMetricService, InternationalStandingOrderConsentRepository internationalStandingOrderConsentRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.internationalStandingOrderConsentRepository = internationalStandingOrderConsentRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    public ResponseEntity<OBWriteInternationalStandingOrderConsentResponse7> createInternationalStandingOrderConsents(
            OBWriteInternationalStandingOrderConsent6 obWriteInternationalStandingOrderConsent6,
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
        log.debug("Received '{}'.", obWriteInternationalStandingOrderConsent6);

        Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        Optional<FRInternationalStandingOrderConsent> consentByIdempotencyKey = internationalStandingOrderConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, obWriteInternationalStandingOrderConsent6, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getInternationalStandingOrderConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent.");

        FRInternationalStandingOrderConsent internationalStandingOrderConsent = FRInternationalStandingOrderConsent.builder()
                .id(IntentType.PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .internationalStandingOrderConsent(toFRWriteInternationalStandingOrderConsent(obWriteInternationalStandingOrderConsent6))
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
        Optional<FRInternationalStandingOrderConsent> isInternationalStandingOrderConsent = internationalStandingOrderConsentRepository.findById(consentId);
        if (!isInternationalStandingOrderConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("International  consent '" + consentId + "' can't be found");
        }
        FRInternationalStandingOrderConsent internationalStandingOrderConsent = isInternationalStandingOrderConsent.get();

        return ResponseEntity.ok(packageResponse(internationalStandingOrderConsent));
    }

    private OBWriteInternationalStandingOrderConsentResponse7 packageResponse(FRInternationalStandingOrderConsent internationalStandingOrderConsent) {
        return new OBWriteInternationalStandingOrderConsentResponse7()
                .data(new OBWriteInternationalStandingOrderConsentResponse7Data()
                        .initiation(toOBWriteInternationalStandingOrder4DataInitiation(internationalStandingOrderConsent.getInitiation()))
                        .status(toOBWriteInternationalStandingOrderConsentResponse7DataStatus(internationalStandingOrderConsent.getStatus()))
                        .creationDateTime(internationalStandingOrderConsent.getCreated())
                        .statusUpdateDateTime(internationalStandingOrderConsent.getStatusUpdate())
                        .consentId(internationalStandingOrderConsent.getId())
                        .permission(toOBWriteInternationalStandingOrderConsentResponse7DataPermission(internationalStandingOrderConsent.getInternationalStandingOrderConsent().getData().getPermission()))
                        .authorisation(toOBWriteDomesticConsent4DataAuthorisation(internationalStandingOrderConsent.getInternationalStandingOrderConsent().getData().getAuthorisation()))
                        .debtor(toOBDebtorIdentification1(internationalStandingOrderConsent.getInitiation().getDebtorAccount()))
                ).risk(toOBRisk1(internationalStandingOrderConsent.getRisk()))
                .links(resourceLinkService.toSelfLink(internationalStandingOrderConsent, discovery -> getVersion(discovery).getGetInternationalStandingOrderConsent()))
                .meta(new Meta());
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_5();
    }

}
