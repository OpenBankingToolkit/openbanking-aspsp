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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_5.domesticscheduledpayments;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.DomesticScheduledConsent5Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticScheduledConsent5;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment4;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsentResponse5;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsentResponse5Data;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsentResponse5Data.PermissionEnum;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_5.converter.payment.ConsentStatusCodeToResponseDataStatusConverter.toOBWriteDomesticScheduledConsentResponse5DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;

@Controller("DomesticScheduledPaymentConsentsApiV3.1.5")
@Slf4j
public class DomesticScheduledPaymentConsentsApiController implements DomesticScheduledPaymentConsentsApi {

    private final DomesticScheduledConsent5Repository domesticScheduledConsentRepository;
    private final TppRepository tppRepository;
    private final ResourceLinkService resourceLinkService;
    private final ConsentMetricService consentMetricService;

    @Autowired
    public DomesticScheduledPaymentConsentsApiController(ConsentMetricService consentMetricService, DomesticScheduledConsent5Repository domesticScheduledConsentRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.domesticScheduledConsentRepository = domesticScheduledConsentRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    public ResponseEntity<OBWriteDomesticScheduledConsentResponse5> createDomesticScheduledPaymentConsents(
            OBWriteDomesticScheduledConsent4 obWriteDomesticScheduledConsent4,
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
        log.debug("Received '{}'.", obWriteDomesticScheduledConsent4);

        Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        Optional<FRDomesticScheduledConsent5> consentByIdempotencyKey = domesticScheduledConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, obWriteDomesticScheduledConsent4, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getDomesticScheduledConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent...");

        FRDomesticScheduledConsent5 domesticScheduledConsent = FRDomesticScheduledConsent5.builder()
                .id(IntentType.PAYMENT_DOMESTIC_SCHEDULED_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .domesticScheduledConsent(obWriteDomesticScheduledConsent4)
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        log.debug("Saving consent: {}", domesticScheduledConsent);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(domesticScheduledConsent.getId(), domesticScheduledConsent.getStatus().name()));
        domesticScheduledConsent = domesticScheduledConsentRepository.save(domesticScheduledConsent);
        log.info("Created consent id: {}", domesticScheduledConsent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(domesticScheduledConsent));
    }

    public ResponseEntity getDomesticScheduledPaymentConsentsConsentId(
            String consentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal) throws OBErrorResponseException {

        Optional<FRDomesticScheduledConsent5> isDomesticScheduledConsent = domesticScheduledConsentRepository.findById(consentId);
        if (!isDomesticScheduledConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Domestic scheduled consent '" + consentId + "' can't be found");
        }
        FRDomesticScheduledConsent5 domesticScheduledConsent = isDomesticScheduledConsent.get();

        return ResponseEntity.ok(packageResponse(domesticScheduledConsent));
    }

    private OBWriteDomesticScheduledConsentResponse5 packageResponse(FRDomesticScheduledConsent5 domesticScheduledConsent) {
        return new OBWriteDomesticScheduledConsentResponse5()
                .data(new OBWriteDomesticScheduledConsentResponse5Data()
                        .initiation(domesticScheduledConsent.getInitiation())
                        .status(toOBWriteDomesticScheduledConsentResponse5DataStatus(domesticScheduledConsent.getStatus()))
                        .creationDateTime(domesticScheduledConsent.getCreated())
                        .statusUpdateDateTime(domesticScheduledConsent.getStatusUpdate())
                        .consentId(domesticScheduledConsent.getId())
                        .permission(PermissionEnum.valueOf(domesticScheduledConsent.getDomesticScheduledConsent().getData().getPermission().name()))
                        .authorisation(domesticScheduledConsent.getDomesticScheduledConsent().getData().getAuthorisation())
                        .scASupportData(domesticScheduledConsent.getDomesticScheduledConsent().getData().getScASupportData())
                )
                .links(resourceLinkService.toSelfLink(domesticScheduledConsent, discovery -> getVersion(discovery).getGetDomesticScheduledPaymentConsent()))
                .risk(domesticScheduledConsent.getRisk())
                .meta(new Meta());
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_5();
    }

}