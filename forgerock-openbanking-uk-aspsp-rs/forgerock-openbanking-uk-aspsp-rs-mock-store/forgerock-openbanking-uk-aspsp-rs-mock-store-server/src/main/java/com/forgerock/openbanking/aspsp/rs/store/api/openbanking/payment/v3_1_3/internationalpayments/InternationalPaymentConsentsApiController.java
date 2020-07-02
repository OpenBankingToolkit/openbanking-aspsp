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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_3.internationalpayments;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.payments.InternationalConsent4Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalConsent4;
import com.forgerock.openbanking.common.services.openbanking.FundsAvailabilityService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment4;
import uk.org.openbanking.datamodel.payment.OBFundsAvailableResult1;
import uk.org.openbanking.datamodel.payment.OBWriteDataFundsConfirmationResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteFundsConfirmationResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsentResponse4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsentResponse4Data;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.ConsentStatusCodeToResponseDataStatusConverter.toOBWriteInternationalConsentResponse4DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-22T14:20:48.770Z")

@Controller("InternationalPaymentConsentsApiV3.1.3")
@Slf4j
public class InternationalPaymentConsentsApiController implements InternationalPaymentConsentsApi {

    private final InternationalConsent4Repository internationalConsentRepository;
    private final TppRepository tppRepository;
    private final FundsAvailabilityService fundsAvailabilityService;
    private final ResourceLinkService resourceLinkService;
    private final ConsentMetricService consentMetricService;

    public InternationalPaymentConsentsApiController(ConsentMetricService consentMetricService, InternationalConsent4Repository internationalConsentRepository, TppRepository tppRepository, FundsAvailabilityService fundsAvailabilityService, ResourceLinkService resourceLinkService) {
        this.internationalConsentRepository = internationalConsentRepository;
        this.tppRepository = tppRepository;
        this.fundsAvailabilityService = fundsAvailabilityService;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    public ResponseEntity<OBWriteInternationalConsentResponse4> createInternationalPaymentConsents(
            OBWriteInternationalConsent4 obWriteInternationalConsent4,
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
        log.debug("Received '{}'.", obWriteInternationalConsent4);

        final Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        Optional<FRInternationalConsent4> consentByIdempotencyKey = internationalConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, obWriteInternationalConsent4, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getInternationalConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent.");

        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        FRInternationalConsent4 internationalConsent = FRInternationalConsent4.builder()
                .id(IntentType.PAYMENT_INTERNATIONAL_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .internationalConsent(obWriteInternationalConsent4)
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        log.debug("Saving consent: {}", internationalConsent);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(internationalConsent.getId(), internationalConsent.getStatus().name()));
        internationalConsent = internationalConsentRepository.save(internationalConsent);
        log.info("Created consent id: {}", internationalConsent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(internationalConsent));
    }

    public ResponseEntity getInternationalPaymentConsentsConsentId(
            String consentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        Optional<FRInternationalConsent4> isInternationalConsent = internationalConsentRepository.findById(consentId);
        if (!isInternationalConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("International consent '" + consentId + "' can't be found");
        }
        FRInternationalConsent4 internationalConsent = isInternationalConsent.get();

        return ResponseEntity.ok(packageResponse(internationalConsent));
    }

    public ResponseEntity getInternationalPaymentConsentsConsentIdFundsConfirmation(
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
        Optional<FRInternationalConsent4> isInternationalConsent = internationalConsentRepository.findById(consentId);
        if (!isInternationalConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("International consent '" + consentId + "' can't be found");
        }
        FRInternationalConsent4 internationalConsent = isInternationalConsent.get();

        // Check if funds are available on the account selected in consent
        boolean areFundsAvailable = fundsAvailabilityService.isFundsAvailable(
                internationalConsent.getAccountId(),
                internationalConsent.getInitiation().getInstructedAmount().getAmount());

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

    private OBWriteInternationalConsentResponse4 packageResponse(FRInternationalConsent4 internationalConsent) {
        return new OBWriteInternationalConsentResponse4()
                .data(new OBWriteInternationalConsentResponse4Data()
                        .initiation(internationalConsent.getInitiation())
                        .status(toOBWriteInternationalConsentResponse4DataStatus(internationalConsent.getStatus()))
                        .creationDateTime(internationalConsent.getCreated())
                        .statusUpdateDateTime(internationalConsent.getStatusUpdate())
                        .exchangeRateInformation(internationalConsent.getCalculatedExchangeRate())
                        .consentId(internationalConsent.getId())
                        .authorisation(internationalConsent.getInternationalConsent().getData().getAuthorisation())
                )
                .risk(internationalConsent.getRisk())
                .links(resourceLinkService.toSelfLink(internationalConsent, discovery -> getVersion(discovery).getGetInternationalPaymentConsent()))
                .meta(new Meta());
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_3();
    }

}
