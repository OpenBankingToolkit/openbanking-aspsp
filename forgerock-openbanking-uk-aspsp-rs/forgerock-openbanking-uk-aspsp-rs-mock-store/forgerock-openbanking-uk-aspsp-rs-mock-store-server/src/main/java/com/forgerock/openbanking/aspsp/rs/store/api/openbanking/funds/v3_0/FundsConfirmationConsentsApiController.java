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

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.funds.FundsConfirmationConsentRepository;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.repositories.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.persistence.funds.FRFundsConfirmationConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
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
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksFundsConfirmation3;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsent1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsentDataResponse1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsentResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.fund.FRFundsConfirmationConsentConverter.toFRFundsConfirmationConsentData;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("FundsConfirmationConsentsApiV3.0")
@Slf4j
public class FundsConfirmationConsentsApiController implements FundsConfirmationConsentsApi {

    private FundsConfirmationConsentRepository fundsConfirmationConsentRepository;
    private TppRepository tppRepository;
    private ConsentMetricService consentMetricService;
    private ResourceLinkService resourceLinkService;

    public FundsConfirmationConsentsApiController(FundsConfirmationConsentRepository fundsConfirmationConsentRepository, TppRepository tppRepository, ConsentMetricService consentMetricService, ResourceLinkService resourceLinkService) {
        this.fundsConfirmationConsentRepository = fundsConfirmationConsentRepository;
        this.tppRepository = tppRepository;
        this.consentMetricService = consentMetricService;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity<OBFundsConfirmationConsentResponse1> createFundsConfirmationConsent(
            OBFundsConfirmationConsent1 obFundsConfirmationConsent,
            String xFapiFinancialId,
            String authorization,
            DateTime xFapiCustomerLastLoggedTime,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            String clientId,
            HttpServletRequest request,
            Principal principal
    ) {
        log.debug("Received '{}'.", obFundsConfirmationConsent);

        final Tpp tpp = tppRepository.findByClientId(clientId);
        FRFundsConfirmationConsent consent = FRFundsConfirmationConsent.builder()
                .id(IntentType.FUNDS_CONFIRMATION_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .fundsConfirmationConsent(toFRFundsConfirmationConsentData(obFundsConfirmationConsent))
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();

        consentMetricService.sendConsentActivity(new ConsentStatusEntry(consent.getId(), consent.getStatus().name()));

        consent = fundsConfirmationConsentRepository.save(consent);

        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consent,request));
    }

    @Override
    public ResponseEntity getFundsConfirmationConsentsConsentId(
            String consentId,
            String xFapiFinancialId,
            String authorization,
            DateTime xFapiCustomerLastLoggedTime,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) {
        return fundsConfirmationConsentRepository.findById(consentId)
                .<ResponseEntity>map(frFundsConfirmationConsent1 -> ResponseEntity.ok(packageResponse(frFundsConfirmationConsent1, request)))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Funds Confirmation consent '" + consentId + "' can't be found"));
    }

    @Override
    public ResponseEntity deleteFundsConfirmationConsentsConsentId(
            String consentId,
            String xFapiFinancialId,
            String authorization,
            DateTime xFapiCustomerLastLoggedTime,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) {
        Optional<FRFundsConfirmationConsent> existingConsent = fundsConfirmationConsentRepository.findById(consentId);
        if (existingConsent.isPresent()) {
            log.debug("Deleting fund confirmation consent: {}", consentId);
            fundsConfirmationConsentRepository.deleteById(consentId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Funds Confirmation consent '" + consentId + "' can't be found");
        }

    }

    private OBFundsConfirmationConsentResponse1 packageResponse(FRFundsConfirmationConsent consent, HttpServletRequest request) {
        return new OBFundsConfirmationConsentResponse1()
                .data(new OBFundsConfirmationConsentDataResponse1()
                        .debtorAccount(toOBCashAccount3(consent.getFundsConfirmationConsent().getDebtorAccount()))
                        .creationDateTime(consent.getCreated())
                        .status(consent.getStatus().toOBExternalRequestStatus1Code())
                        .statusUpdateDateTime(consent.getStatusUpdate())
                        .expirationDateTime(consent.getFundsConfirmationConsent().getExpirationDateTime())
                        .consentId(consent.getId())
                )
                .meta(new Meta())
                .links(resourceLinkService.toSelfLink(consent, discovery -> discovery.getVersion(VersionPathExtractor.getVersionFromPath(request)).getGetFundsConfirmationConsent()));
    }

}
