/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.domesticpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecision;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.PaymentConsentDecisionUpdater;
import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRDomesticConsent2;
import com.forgerock.openbanking.commons.model.rcs.consentdecision.DomesticPaymentConsentDecision;
import com.forgerock.openbanking.commons.services.store.payment.DomesticPaymentService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
class DomesticPaymentConsentDecisionDelegate implements ConsentDecision {

    private PaymentConsentDecisionUpdater paymentConsentDecisionUpdater;
    private DomesticPaymentService paymentsService;
    private ObjectMapper objectMapper;
    private FRDomesticConsent2 payment;

    DomesticPaymentConsentDecisionDelegate(PaymentConsentDecisionUpdater paymentConsentDecisionUpdater, DomesticPaymentService paymentsService, ObjectMapper objectMapper, FRDomesticConsent2 payment) {
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
    public void autoaccept(List<FRAccount2> accounts, String username) throws OBErrorException {
        paymentConsentDecisionUpdater.applyUpdate(username, accounts.get(0).getId(), true, p -> paymentsService.updatePayment(p), payment);
    }
}
