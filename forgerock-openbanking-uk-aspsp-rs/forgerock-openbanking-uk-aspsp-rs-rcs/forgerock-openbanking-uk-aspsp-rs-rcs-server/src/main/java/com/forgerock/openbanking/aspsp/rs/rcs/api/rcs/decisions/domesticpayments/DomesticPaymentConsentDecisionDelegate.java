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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.domesticpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.PaymentConsentDecisionUpdater;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
import com.forgerock.openbanking.common.model.rcs.consentdecision.DomesticPaymentConsentDecision;
import com.forgerock.openbanking.common.services.store.payment.DomesticPaymentService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
class DomesticPaymentConsentDecisionDelegate implements ConsentDecisionDelegate {

    private PaymentConsentDecisionUpdater paymentConsentDecisionUpdater;
    private DomesticPaymentService paymentsService;
    private ObjectMapper objectMapper;
    private FRDomesticConsent payment;

    DomesticPaymentConsentDecisionDelegate(PaymentConsentDecisionUpdater paymentConsentDecisionUpdater, DomesticPaymentService paymentsService, ObjectMapper objectMapper, FRDomesticConsent payment) {
        this.paymentConsentDecisionUpdater = paymentConsentDecisionUpdater;
        this.paymentsService = paymentsService;
        this.objectMapper = objectMapper;
        this.payment = payment;
    }

    @Override
    public String getTppIdBehindConsent() {
        return payment.getPispId();
    }

    @Override
    public String getUserIDBehindConsent() {
        return payment.getUserId();
    }

    @Override
    public void consentDecision(String consentDecisionSerialised, boolean decision) throws IOException, OBErrorException {
        DomesticPaymentConsentDecision domesticPaymentConsentDecision = objectMapper.readValue(consentDecisionSerialised, DomesticPaymentConsentDecision.class);

        paymentConsentDecisionUpdater.applyUpdate(getUserIDBehindConsent(), domesticPaymentConsentDecision.getAccountId(), decision, p -> paymentsService.updatePayment(p), payment);
    }

    @Override
    public void autoaccept(List<FRAccount> accounts, String username) throws OBErrorException {
        paymentConsentDecisionUpdater.applyUpdate(username, accounts.get(0).getId(), true, p -> paymentsService.updatePayment(p), payment);
    }
}
