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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;

import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.common.conf.PlatformConfiguration;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRRemittanceInformation;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetailsData;
import com.forgerock.openbanking.common.model.rcs.consentdetails.DomesticVrpPaymentConsentDetails;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.services.store.vrp.DomesticVrpPaymentConsentService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RCSVrpPaymentDetailsApi implements RCSDetailsApi {

    private final RCSErrorService rcsErrorService;
    private final DomesticVrpPaymentConsentService consentService;
    private final TppStoreService tppStoreService;
    private final PlatformConfiguration platformConfiguration;

    public RCSVrpPaymentDetailsApi(RCSErrorService rcsErrorService, DomesticVrpPaymentConsentService consentService, TppStoreService tppStoreService, PlatformConfiguration platformConfiguration) {
        this.rcsErrorService = rcsErrorService;
        this.consentService = consentService;
        this.tppStoreService = tppStoreService;
        this.platformConfiguration = platformConfiguration;
    }

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<AccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a VRP consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The VRP payment consent id '{}'", consentId);

        log.debug("Populate the model with the VRP payment and consent data");
        FRDomesticVRPConsent vrpConsent = consentService.getVrpPayment(consentId);
        if (vrpConsent == null) {
            log.error("VRP Consent ID '{}' not found", consentId);
            return rcsErrorService.invalidConsentError(
                    remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_INVALID,
                    String.format("Consent ID '%s' not found.", consentId));
        }
        Optional<Tpp> isTpp = tppStoreService.findById(vrpConsent.getPispId());
        if (isTpp.isEmpty()) {
            log.error("The TPP '{}' (Client ID {}) that created this vrp consent id '{}' doesn't exist anymore.", vrpConsent.getPispId(), clientId, consentId);
            return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, consentId);
        }
        Tpp tpp = isTpp.get();

        //Verify the pisp is the same than the one that created this payment ^
        verifyTppCreatedPayment(clientId, tpp.getClientId(), consentId);
        //Associate the vrp payment to this user
        vrpConsent.setUserId(username);
        consentService.updateVrpPayment(vrpConsent);
        FRDomesticVRPConsentDetailsData data = vrpConsent.getVrpDetails().getData();
        return ResponseEntity.ok(
                DomesticVrpPaymentConsentDetails.builder()
                        .username(username)
                        .pispName(tpp.getOfficialName())
                        .aspspName(platformConfiguration.getAspspName())
                        .merchantName(vrpConsent.getPispName())
                        .logo(tpp.getLogo())
                        .clientId(clientId)
                        .creditorAccount(
                                Optional.ofNullable(data.getInitiation().getCreditorAccount())
                                        .orElse(null)
                        )
                        .debtorAccount(
                                Optional.ofNullable(data.getInitiation().getDebtorAccount())
                                        .orElse(null)
                        )
                        .controlParameters(
                                Optional.ofNullable(data.getControlParameters())
                                        .orElse(null)
                        )
                        .paymentReference(
                                Optional.ofNullable(data.getInitiation().getRemittanceInformation())
                                        .map(FRRemittanceInformation::getReference)
                                        .orElse(null))
                        .debtorReference(
                                Optional.ofNullable(data.getInitiation().getRemittanceInformation())
                                        .map(FRRemittanceInformation::getUnstructured)
                                        .orElse(null))
                        .build());
    }

}
