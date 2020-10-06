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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.internationalscheduledpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.PaymentConsentDecisionUpdater;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalScheduledConsent;
import com.forgerock.openbanking.common.model.rcs.consentdecision.InternationalScheduledPaymentConsentDecision;
import com.forgerock.openbanking.common.services.store.payment.InternationalScheduledPaymentService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
class InternationalScheduledPaymentConsentDecisionDelegate implements ConsentDecisionDelegate {

    private PaymentConsentDecisionUpdater paymentConsentDecisionUpdater;
    private InternationalScheduledPaymentService paymentsService;
    private ObjectMapper objectMapper;
    private FRInternationalScheduledConsent payment;

    InternationalScheduledPaymentConsentDecisionDelegate(PaymentConsentDecisionUpdater paymentConsentDecisionUpdater, InternationalScheduledPaymentService paymentsService, ObjectMapper objectMapper, FRInternationalScheduledConsent consent) {
        this.paymentConsentDecisionUpdater = paymentConsentDecisionUpdater;
        this.paymentsService = paymentsService;
        this.objectMapper = objectMapper;
        this.payment = consent;
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
        InternationalScheduledPaymentConsentDecision paymentConsentDecision = objectMapper.readValue(consentDecisionSerialised, InternationalScheduledPaymentConsentDecision.class);
        paymentConsentDecisionUpdater.applyUpdate(getUserIDBehindConsent(), paymentConsentDecision.getAccountId(), decision, p -> paymentsService.updatePayment(p), payment);
    }

    @Override
    public void autoaccept(List<FRAccount> accounts, String username) throws OBErrorException {
        paymentConsentDecisionUpdater.applyUpdate(username, accounts.get(0).getId(), true, p -> paymentsService.updatePayment(p), payment);
    }
}
