/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.internationalstandingorders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecision;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.PaymentConsentDecisionUpdater;
import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.payment.FRInternationalStandingOrderConsent3;
import com.forgerock.openbanking.commons.model.rcs.consentdecision.InternationalStandingOrderConsentDecision;
import com.forgerock.openbanking.commons.services.store.payment.InternationalStandingOrderService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
class InternationalStandingOrderConsentDecisionDelegate implements ConsentDecision {

    private PaymentConsentDecisionUpdater paymentConsentDecisionUpdater;
    private InternationalStandingOrderService paymentsService;
    private ObjectMapper objectMapper;
    private FRInternationalStandingOrderConsent3 payment;

    InternationalStandingOrderConsentDecisionDelegate(PaymentConsentDecisionUpdater paymentConsentDecisionUpdater, InternationalStandingOrderService paymentsService, ObjectMapper objectMapper, FRInternationalStandingOrderConsent3 consent) {
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
        InternationalStandingOrderConsentDecision paymentConsentDecision = objectMapper.readValue(consentDecisionSerialised, InternationalStandingOrderConsentDecision.class);
        paymentConsentDecisionUpdater.applyUpdate(getUserIDBehindConsent(), paymentConsentDecision.getAccountId(), decision, p -> paymentsService.updatePayment(p), payment);
    }

    @Override
    public void autoaccept(List<FRAccount2> accounts, String username) throws OBErrorException {
        paymentConsentDecisionUpdater.applyUpdate(username, accounts.get(0).getId(), true, p -> paymentsService.updatePayment(p), payment);
    }
}
