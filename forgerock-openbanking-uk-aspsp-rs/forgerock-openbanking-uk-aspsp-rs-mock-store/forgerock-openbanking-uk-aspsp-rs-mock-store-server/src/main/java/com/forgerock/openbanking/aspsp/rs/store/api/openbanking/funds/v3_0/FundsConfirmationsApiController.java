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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_0;

import com.forgerock.openbanking.aspsp.rs.store.repository.funds.FundsConfirmationConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.funds.FundsConfirmationRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.funds.FRFundsConfirmationData;
import com.forgerock.openbanking.common.model.openbanking.persistence.funds.FRFundsConfirmation;
import com.forgerock.openbanking.common.model.openbanking.persistence.funds.FRFundsConfirmationConsent;
import com.forgerock.openbanking.common.services.openbanking.FundsAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmation1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationResponse1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationResponse1Data;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBFundsConfirmation1DataInstructedAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.fund.FRFundsConfirmationConverter.toFRFundsConfirmationData;


@Controller("FundsConfirmationsApiV3.0")
@Slf4j
public class FundsConfirmationsApiController implements FundsConfirmationsApi {

    private FundsConfirmationRepository fundsConfirmationRepository;
    private FundsConfirmationConsentRepository fundsConfirmationConsentRepository;
    private FundsAvailabilityService fundsAvailabilityService;
    private ResourceLinkService resourceLinkService;

    public FundsConfirmationsApiController(FundsConfirmationRepository fundsConfirmationRepository, FundsConfirmationConsentRepository fundsConfirmationConsentRepository, FundsAvailabilityService fundsAvailabilityService, ResourceLinkService resourceLinkService) {
        this.fundsConfirmationRepository = fundsConfirmationRepository;
        this.fundsConfirmationConsentRepository = fundsConfirmationConsentRepository;
        this.fundsAvailabilityService = fundsAvailabilityService;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity createFundsConfirmation(
            @Valid OBFundsConfirmation1 obFundsConfirmation1,
            String xFapiFinancialId, String authorization,
            DateTime xFapiCustomerLastLoggedTime,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) {
        log.debug("Create funds confirmation: {}", obFundsConfirmation1);

        String consentId = obFundsConfirmation1.getData().getConsentId();
        Optional<FRFundsConfirmation> isSubmission = fundsConfirmationRepository.findById(consentId);

        Optional<FRFundsConfirmationConsent> isConsent = fundsConfirmationConsentRepository.findById(consentId);
        if (!isConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consent behind funds confirmation submission '" + consentId + "' can't be found");
        }

        // Check if funds are available on the account selected in consent
        boolean areFundsAvailable = fundsAvailabilityService.isFundsAvailable(
                isConsent.get().getAccountId(),
                obFundsConfirmation1.getData().getInstructedAmount().getAmount());


        FRFundsConfirmation frFundsConfirmation = isSubmission
                .orElseGet(() ->
                        FRFundsConfirmation.builder()
                                .id(consentId)
                                .created(DateTime.now())
                                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                                .build()
        );
        frFundsConfirmation.setFundsAvailable(areFundsAvailable);
        frFundsConfirmation.setFundsConfirmation(toFRFundsConfirmationData(obFundsConfirmation1));
        frFundsConfirmation = fundsConfirmationRepository.save(frFundsConfirmation);

        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(frFundsConfirmation, isConsent.get(), request));
    }

    @Override
    public ResponseEntity getFundsConfirmationId(
            String fundsConfirmationId,
            String xFapiFinancialId,
            String authorization,
            DateTime xFapiCustomerLastLoggedTime,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) {
        Optional<FRFundsConfirmation> isFundsConfirmation = fundsConfirmationRepository.findById(fundsConfirmationId);
        if (!isFundsConfirmation.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + fundsConfirmationId + "' can't be found");
        }
        FRFundsConfirmation frPaymentSubmission = isFundsConfirmation.get();

        Optional<FRFundsConfirmationConsent> isSetup = fundsConfirmationConsentRepository.findById(frPaymentSubmission.getFundsConfirmation().getConsentId());
        return isSetup
                .<ResponseEntity>map(frFundsConfirmationConsent1 -> ResponseEntity.ok(packageResponse(frPaymentSubmission, frFundsConfirmationConsent1, request)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + fundsConfirmationId + "' can't be found"));

    }

    private OBFundsConfirmationResponse1 packageResponse(FRFundsConfirmation fundsConfirmation, FRFundsConfirmationConsent consent, HttpServletRequest request) {
        final FRFundsConfirmationData obFundsConfirmationData = fundsConfirmation.getFundsConfirmation();
        return new OBFundsConfirmationResponse1()
                .data(new OBFundsConfirmationResponse1Data()
                        .instructedAmount(toOBFundsConfirmation1DataInstructedAmount(obFundsConfirmationData.getInstructedAmount()))
                        .creationDateTime(fundsConfirmation.getCreated())
                        .fundsConfirmationId(fundsConfirmation.getId())
                        .fundsAvailable(fundsConfirmation.isFundsAvailable())
                        .reference(obFundsConfirmationData.getReference())
                        .consentId(consent.getId()))
                .meta(new Meta())
                .links(resourceLinkService.toSelfLink(fundsConfirmation, discovery -> discovery.getVersion(VersionPathExtractor.getVersionFromPath(request)).getCreateFundsConfirmation()));
    }

}
