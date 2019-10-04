/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.domesticstandingorders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.PaymentConsentDecisionUpdater;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRDomesticStandingOrderConsent3;
import com.forgerock.openbanking.common.model.rcs.consentdecision.DomesticStandingOrderConsentDecision;
import com.forgerock.openbanking.common.services.store.payment.DomesticStandingOrderService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
class DomesticStandingOrdersConsentDecisionDelegate implements ConsentDecisionDelegate {

    private PaymentConsentDecisionUpdater paymentConsentDecisionUpdater;
    private DomesticStandingOrderService paymentsService;
    private ObjectMapper objectMapper;
    private FRDomesticStandingOrderConsent3 payment;

    DomesticStandingOrdersConsentDecisionDelegate(PaymentConsentDecisionUpdater paymentConsentDecisionUpdater, DomesticStandingOrderService paymentsService, ObjectMapper objectMapper, FRDomesticStandingOrderConsent3 payment) {
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
        DomesticStandingOrderConsentDecision domesticPaymentConsentDecision = objectMapper.readValue(consentDecisionSerialised, DomesticStandingOrderConsentDecision.class);

        paymentConsentDecisionUpdater.applyUpdate(getUserIDBehindConsent(), domesticPaymentConsentDecision.getAccountId(), decision, p -> paymentsService.updatePayment(p), payment);
    }

    @Override
    public void autoaccept(List<FRAccount2> accounts, String username) throws OBErrorException {
        paymentConsentDecisionUpdater.applyUpdate(username, accounts.get(0).getId(), true, p -> paymentsService.updatePayment(p), payment);
    }
}
