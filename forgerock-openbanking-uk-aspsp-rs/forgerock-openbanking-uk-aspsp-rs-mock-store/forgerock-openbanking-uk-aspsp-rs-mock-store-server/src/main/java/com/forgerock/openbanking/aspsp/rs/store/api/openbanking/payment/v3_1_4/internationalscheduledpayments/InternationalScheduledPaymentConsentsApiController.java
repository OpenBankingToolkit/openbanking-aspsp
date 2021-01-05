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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_4.internationalscheduledpayments;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalScheduledConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalScheduledConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalScheduledConsent;
import com.forgerock.openbanking.common.services.openbanking.FundsAvailabilityService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.repositories.TppRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment4;
import uk.org.openbanking.datamodel.payment.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_4.ResponseReadRefundAccountConverter.toOBWriteInternationalScheduledConsentResponse5DataReadRefundAccount;
import static com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_4.ResponseStatusCodeConverter.toOBWriteInternationalScheduledConsentResponse5DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataAuthorisationConverter.toOBWriteDomesticConsent4DataAuthorisation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRExchangeRateConverter.toOBWriteInternationalConsentResponse5DataExchangeRateInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPermissionConverter.toOBWriteInternationalScheduledConsentResponse5DataPermission;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConsentConverter.toFRWriteInternationalScheduledConsent;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConsentConverter.toOBWriteInternationalScheduled3DataInitiation;

@Controller("InternationalScheduledPaymentConsentsApiV3.1.4")
@Slf4j
public class InternationalScheduledPaymentConsentsApiController implements InternationalScheduledPaymentConsentsApi {

    private final InternationalScheduledConsentRepository internationalScheduledConsentRepository;
    private final TppRepository tppRepository;
    private final FundsAvailabilityService fundsAvailabilityService;
    private final ResourceLinkService resourceLinkService;
    private final ConsentMetricService consentMetricService;

    public InternationalScheduledPaymentConsentsApiController(ConsentMetricService consentMetricService, InternationalScheduledConsentRepository internationalScheduledConsentRepository, TppRepository tppRepository, FundsAvailabilityService fundsAvailabilityService, ResourceLinkService resourceLinkService) {
        this.internationalScheduledConsentRepository = internationalScheduledConsentRepository;
        this.tppRepository = tppRepository;
        this.fundsAvailabilityService = fundsAvailabilityService;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    @Override
    public ResponseEntity<OBWriteInternationalScheduledConsentResponse5> createInternationalScheduledPaymentConsents(
            OBWriteInternationalScheduledConsent5 obWriteInternationalScheduledConsent5,
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
        log.debug("Received: '{}'", obWriteInternationalScheduledConsent5);
        FRWriteInternationalScheduledConsent frScheduledConsent = toFRWriteInternationalScheduledConsent(obWriteInternationalScheduledConsent5);
        log.trace("Converted to: '{}'", frScheduledConsent);

        Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        Optional<FRInternationalScheduledConsent> consentByIdempotencyKey = internationalScheduledConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, frScheduledConsent, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getInternationalScheduledConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(responseEntity(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent.");

        FRInternationalScheduledConsent internationalScheduledConsent = FRInternationalScheduledConsent.builder()
                .id(IntentType.PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .internationalScheduledConsent(frScheduledConsent)
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        log.debug("Saving consent: '{}'", internationalScheduledConsent);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(internationalScheduledConsent.getId(), internationalScheduledConsent.getStatus().name()));
        internationalScheduledConsent = internationalScheduledConsentRepository.save(internationalScheduledConsent);
        log.info("Created consent id: '{}'", internationalScheduledConsent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseEntity(internationalScheduledConsent));
    }

    @Override
    public ResponseEntity getInternationalScheduledPaymentConsentsConsentId(
            String consentId,
            String authorization,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        Optional<FRInternationalScheduledConsent> isScheduledConsent = internationalScheduledConsentRepository.findById(consentId);
        if (!isScheduledConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("International scheduled consent '" + consentId + "' can't be found");
        }
        FRInternationalScheduledConsent internationalScheduledConsent = isScheduledConsent.get();

        return ResponseEntity.ok(responseEntity(internationalScheduledConsent));
    }

    @Override
    public ResponseEntity getInternationalScheduledPaymentConsentsConsentIdFundsConfirmation(
            String consentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            String httpUrl,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        Optional<FRInternationalScheduledConsent> isScheduledConsent = internationalScheduledConsentRepository.findById(consentId);
        if (!isScheduledConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("International scheduled consent '" + consentId + "' can't be found");
        }
        FRInternationalScheduledConsent internationalScheduledConsent = isScheduledConsent.get();


        // Check if funds are available on the account selected in consent
        boolean areFundsAvailable = fundsAvailabilityService.isFundsAvailable(
                internationalScheduledConsent.getAccountId(),
                internationalScheduledConsent.getInitiation().getInstructedAmount().getAmount());

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

    private OBWriteInternationalScheduledConsentResponse5 responseEntity(FRInternationalScheduledConsent internationalScheduledConsent) {
        OBWriteInternationalScheduled3DataInitiation initiation = toOBWriteInternationalScheduled3DataInitiation(internationalScheduledConsent.getInitiation());
        return new OBWriteInternationalScheduledConsentResponse5()
                .data(new OBWriteInternationalScheduledConsentResponse5Data()
                        .readRefundAccount(toOBWriteInternationalScheduledConsentResponse5DataReadRefundAccount(internationalScheduledConsent.getInternationalScheduledConsent().getData().getReadRefundAccount()))
                        .initiation(initiation)
                        .status(toOBWriteInternationalScheduledConsentResponse5DataStatus(internationalScheduledConsent.getStatus()))
                        .creationDateTime(internationalScheduledConsent.getCreated())
                        .statusUpdateDateTime(internationalScheduledConsent.getStatusUpdate())
                        .consentId(internationalScheduledConsent.getId())
                        .exchangeRateInformation(toOBWriteInternationalConsentResponse5DataExchangeRateInformation(internationalScheduledConsent.getCalculatedExchangeRate()))
                        .permission(toOBWriteInternationalScheduledConsentResponse5DataPermission(internationalScheduledConsent.internationalScheduledConsent.getData().getPermission()))
                        .expectedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                        .authorisation(toOBWriteDomesticConsent4DataAuthorisation(internationalScheduledConsent.getInternationalScheduledConsent().getData().getAuthorisation()))
                )
                .risk(toOBRisk1(internationalScheduledConsent.getRisk()))
                .links(resourceLinkService.toSelfLink(internationalScheduledConsent, discovery -> getVersion(discovery).getGetInternationalScheduledPaymentConsent()))
                .meta(new Meta());
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_4();
    }
}
