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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_8.vrp;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accounts.FRAccountRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.vrp.DomesticVRPConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetails;
import com.forgerock.openbanking.common.services.openbanking.FundsAvailabilityService;
import com.forgerock.openbanking.common.services.store.account.AccountStoreServiceImpl;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.repositories.TppRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksVrpPayment;
import uk.org.openbanking.datamodel.vrp.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.toFRDomesticVRPConsentDetails;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.toOBDomesticVRPConsentResponse;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-11-17T13:54:56.728Z[Europe/London]")
@Controller("DomesticVrpConsentsApiControllerV3.1.8")
@Slf4j
public class DomesticVrpConsentsApiController implements DomesticVrpConsentsApi {

    private final DomesticVRPConsentRepository domesticVRPConsentRepository;
    private final TppRepository tppRepository;
    private final ResourceLinkService resourceLinkService;
    private final FundsAvailabilityService fundsAvailabilityService;
    private final ConsentMetricService consentMetricService;
    private final FRAccountRepository accountRepository;

    public DomesticVrpConsentsApiController(
            DomesticVRPConsentRepository domesticVRPConsentRepository, TppRepository tppRepository,
            ResourceLinkService resourceLinkService, FundsAvailabilityService fundsAvailabilityService,
            ConsentMetricService consentMetricService,
            FRAccountRepository accountsRepository, FRAccountRepository accountRepository) {
        this.domesticVRPConsentRepository = domesticVRPConsentRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.fundsAvailabilityService = fundsAvailabilityService;
        this.consentMetricService = consentMetricService;
        this.accountRepository = accountRepository;
    }

    @Override
    public ResponseEntity<OBDomesticVRPConsentResponse> domesticVrpConsentsPost(
            String authorization, String xIdempotencyKey, String xJwsSignature,
            OBDomesticVRPConsentRequest obDomesticVRPConsentRequest, String xFapiAuthDate,
            String xFapiCustomerIpAddress, String xFapiInteractionId, String xCustomerUserAgent,
            String clientId, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        log.debug("(store) Received Domestic VRP consent: '{}'", obDomesticVRPConsentRequest);
        log.debug("(store) Request to create a VRP consent received, interactionId '{}'", xFapiInteractionId);
        FRDomesticVRPConsentDetails frDomesticVRPDetails = toFRDomesticVRPConsentDetails(obDomesticVRPConsentRequest);
        log.trace("Converted OB Domestic VRP consent to: '{}'", frDomesticVRPDetails);

        final Tpp tpp = tppRepository.findByClientId(clientId);
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);

        Optional<FRDomesticVRPConsent> vrpConsentByIdempotencyKey = domesticVRPConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());

        if (vrpConsentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, frDomesticVRPDetails, vrpConsentByIdempotencyKey.get(), () -> vrpConsentByIdempotencyKey.get().getVrpDetails());
            log.info("Idempotent request for VRP payment consent is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(vrpConsentByIdempotencyKey.get()));
        }

        log.debug("No Domestic VRP payment consent with matching idempotency key has been found. Creating new consent.");
        FRDomesticVRPConsent domesticVrpConsent = FRDomesticVRPConsent.builder()
                .id(IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .vrpDetails(frDomesticVRPDetails)
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();

        log.debug("Saving Domestic VRP payment consent: '{}'", domesticVrpConsent);
        consentMetricService.sendConsentActivity(
                new ConsentStatusEntry(
                        domesticVrpConsent.getId(),
                        domesticVrpConsent.getStatus().name()
                )
        );
        domesticVRPConsentRepository.save(domesticVrpConsent);
        log.info("Created domestic VRP payment consent id: '{}'", domesticVrpConsent.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(domesticVrpConsent));
    }

    @Override
    public ResponseEntity domesticVrpConsentsGet(
            String consentId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        log.debug("(store) Request to get a VRP consent with consentId '{}'", consentId);
        Optional<FRDomesticVRPConsent> optional = domesticVRPConsentRepository.findById(consentId);
        if (!optional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Domestic VRP payment consent '" + consentId + "' " +
                    "can't be found");
        }
        return ResponseEntity.ok(packageResponse(optional.get()));
    }

    @Override
    public ResponseEntity domesticVrpConsentsDelete(
            String consentId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        log.debug("(store) Request to delete a VRP consent received, consentId '{}'", consentId);
        Optional<FRDomesticVRPConsent> optional = domesticVRPConsentRepository.findById(consentId);
        if (!optional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Domestic VRP payment consent '" + consentId + "' " +
                    "to deleted can't be found");
        }
        domesticVRPConsentRepository.delete(optional.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity domesticVrpConsentsFundsConfirmation(
            String consentId, String authorization, String xJwsSignature,
            OBVRPFundsConfirmationRequest obVRPFundsConfirmationRequest, String xFapiAuthDate,
            String xFapiCustomerIpAddress, String xFapiInteractionId, String xCustomerUserAgent,
            HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        log.debug("(store) Request to get a VRP funds confirmation, consentId '{}'", consentId);
        Optional<FRDomesticVRPConsent> optional = domesticVRPConsentRepository.findById(consentId);
        if (!optional.isPresent()) {
            log.warn("(store) Domestic VRP payment consent '{}' to confirm funds can't be found", consentId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Domestic VRP payment consent '" + consentId +
                    "' to confirm funds can't be found");
        }
        FRDomesticVRPConsent domesticVrpConsent = optional.get();
        if (!domesticVrpConsent.getStatus().equals(ConsentStatusCode.AUTHORISED)) {
            log.error("(store) Funds confirmation for VRP payment consent Id '{}, with status '{}' can't be requested" +
                    " because the consent status hasn't '{}'", consentId, domesticVrpConsent.getStatus().getValue(),
                    ConsentStatusCode.AUTHORISED.getValue());
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.CONSENT_STATUS_NOT_AUTHORISED.toOBError1(consentId)
            );
        }

        // Check if funds are available on the account selected in consent
        String accountIdentification = domesticVrpConsent.getVrpDetails().getData().getInitiation().getDebtorAccount().getIdentification();
        Collection<FRAccount> accountsByUserID = accountRepository.findByUserID(Objects.requireNonNull(domesticVrpConsent.getUserId()));
        Optional<FRAccount> accountOptional = accountsByUserID.stream().filter(
                account -> account.getAccount().getAccounts().stream().filter(
                        a -> a.getIdentification().equals(accountIdentification)
                ).findFirst().isPresent()
        ).findFirst();
        if(!accountOptional.isPresent()){
            log.warn("(store) VRP consent '{}', debtor account with identitication '{}' can't be found", consentId, accountIdentification);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("VRP consent '"+ consentId +"', debtor account with identitication '" + accountIdentification +
                    "' to confirm funds can't be found");
        }
        boolean areFundsAvailable = fundsAvailabilityService.isFundsAvailable(
                accountOptional.get().getId(),
                obVRPFundsConfirmationRequest.getData().getInstructedAmount().getAmount());
        OBVRPFundsConfirmationRequestData data = obVRPFundsConfirmationRequest.getData();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new OBVRPFundsConfirmationResponse()
                        .data(new OBVRPFundsConfirmationResponseData()
                                .fundsConfirmationId(UUID.randomUUID().toString())
                                .consentId(consentId)
                                .creationDateTime(DateTime.now())
                                .reference(data.getReference())
                                .fundsAvailableResult(new OBPAFundsAvailableResult1()
                                        .fundsAvailable(
                                                areFundsAvailable
                                                        ? OBPAFundsAvailableResult1.FundsAvailableEnum.AVAILABLE
                                                        : OBPAFundsAvailableResult1.FundsAvailableEnum.NOTAVAILABLE
                                        )
                                        .fundsAvailableDateTime(DateTime.now())
                                )
                                .instructedAmount(data.getInstructedAmount()))
                );
    }

    private OBDomesticVRPConsentResponse packageResponse(FRDomesticVRPConsent frDomesticVRPConsent) {
        return toOBDomesticVRPConsentResponse(frDomesticVRPConsent)
                .links(
                        resourceLinkService.toSelfLink(
                                frDomesticVRPConsent,
                                discovery -> getVersion(discovery).getCreateDomesticVrpPaymentConsent()
                        )
                );
    }

    protected OBDiscoveryAPILinksVrpPayment getVersion(DiscoveryConfigurationProperties.VrpPaymentApis discovery) {
        return discovery.getV_3_1_8();
    }

}
