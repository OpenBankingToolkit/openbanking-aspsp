/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.singlepayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.common.model.openbanking.v1_1.payment.FRPaymentSetup1;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.payment.SinglePaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SinglePaymentConsentDecisionFactory {

    private AccountStoreService accountsService;
    private SinglePaymentService paymentsService;
    private ObjectMapper objectMapper;

    public SinglePaymentConsentDecisionFactory(AccountStoreService accountsService, SinglePaymentService paymentsService, ObjectMapper objectMapper) {
        this.accountsService = accountsService;
        this.paymentsService = paymentsService;
        this.objectMapper = objectMapper;
    }

    public ConsentDecisionDelegate create(final String intentId) {
        FRPaymentSetup1 payment = paymentsService.getPayment(intentId);
        return new SinglePaymentConsentDecisionDelegate(accountsService, paymentsService, objectMapper, payment);
    }

}
