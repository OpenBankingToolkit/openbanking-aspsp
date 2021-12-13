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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.vrp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.services.store.vrp.DomesticVrpPaymentConsentService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class DomesticVRPConsentDecisionDelegate implements ConsentDecisionDelegate {
    private DomesticVrpPaymentConsentService consentService;
    private ObjectMapper objectMapper;
    private FRDomesticVRPConsent consent;

    public DomesticVRPConsentDecisionDelegate(
            FRDomesticVRPConsent consent,
            DomesticVrpPaymentConsentService consentService,
            ObjectMapper objectMapper
    ) {
        this.consentService = consentService;
        this.objectMapper = objectMapper;
        this.consent = consent;
    }

    @Override
    public String getTppIdBehindConsent() {
        return consent.getPispId();
    }

    @Override
    public String getUserIDBehindConsent() {
        return consent.getUserId();
    }

    @Override
    public void consentDecision(String consentDecisionSerialised, boolean decision) throws IOException, OBErrorException {
        if (decision) {
            log.debug("The current VRP payment consent: '{}' has been accepted by the PSU: {}", consent.getId(), consent.getUserId());
            consent.setStatus(ConsentStatusCode.AUTHORISED);
        } else {
            log.debug("The current VRP payment consent: '{}' has been rejected by the PSU: {}", consent.getId(), consent.getUserId());
            consent.setStatus(ConsentStatusCode.REJECTED);
        }
        consentService.updateVrpPaymentConsent(consent);
    }

    @Override
    public void autoaccept(List<FRAccount> accounts, String username) throws OBErrorException {
        log.debug("The current VRP payment consent: '{}' has been accepted automatically for the user: {}", consent.getId(), consent.getUserId());
        consent.setStatus(ConsentStatusCode.AUTHORISED);
        consentService.updateVrpPaymentConsent(consent);
    }
}
