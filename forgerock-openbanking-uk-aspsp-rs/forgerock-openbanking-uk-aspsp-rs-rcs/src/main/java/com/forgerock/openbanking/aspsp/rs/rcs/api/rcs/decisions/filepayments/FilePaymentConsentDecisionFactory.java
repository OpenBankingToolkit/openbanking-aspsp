/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.filepayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.PaymentConsentDecisionUpdater;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFileConsent2;
import com.forgerock.openbanking.common.services.store.payment.FilePaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FilePaymentConsentDecisionFactory {

    private PaymentConsentDecisionUpdater paymentConsentDecisionUpdater;
    private FilePaymentService paymentsService;
    private ObjectMapper objectMapper;

    public FilePaymentConsentDecisionFactory(PaymentConsentDecisionUpdater paymentConsentDecisionUpdater, FilePaymentService paymentsService, ObjectMapper objectMapper) {
        this.paymentConsentDecisionUpdater = paymentConsentDecisionUpdater;
        this.paymentsService = paymentsService;
        this.objectMapper = objectMapper;
    }

    public ConsentDecisionDelegate create(final String intentId) {
        FRFileConsent2 consent = paymentsService.getPayment(intentId);
        return new FilePaymentConsentDecisionDelegate(paymentConsentDecisionUpdater, paymentsService, objectMapper, consent);
    }

}
