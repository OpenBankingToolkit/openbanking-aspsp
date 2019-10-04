/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.internationalstandingorders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.PaymentConsentDecisionUpdater;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRInternationalStandingOrderConsent3;
import com.forgerock.openbanking.common.services.store.payment.InternationalStandingOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class InternationalStandingOrderConsentDecisionFactory {

    private PaymentConsentDecisionUpdater paymentConsentDecisionUpdater;
    private InternationalStandingOrderService paymentsService;
    private ObjectMapper objectMapper;

    public InternationalStandingOrderConsentDecisionFactory(PaymentConsentDecisionUpdater paymentConsentDecisionUpdater, InternationalStandingOrderService paymentsService, ObjectMapper objectMapper) {
        this.paymentConsentDecisionUpdater = paymentConsentDecisionUpdater;
        this.paymentsService = paymentsService;
        this.objectMapper = objectMapper;
    }

    public ConsentDecisionDelegate create(final String intentId) {
        FRInternationalStandingOrderConsent3 consent = paymentsService.getPayment(intentId);
        return new InternationalStandingOrderConsentDecisionDelegate(paymentConsentDecisionUpdater, paymentsService, objectMapper, consent);
    }

}
