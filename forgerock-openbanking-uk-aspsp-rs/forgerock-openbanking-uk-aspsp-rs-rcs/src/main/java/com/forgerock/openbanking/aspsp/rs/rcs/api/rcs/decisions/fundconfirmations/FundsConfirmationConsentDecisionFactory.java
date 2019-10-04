/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.fundconfirmations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecision;
import com.forgerock.openbanking.commons.model.openbanking.v3_0.funds.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.commons.services.store.account.AccountStoreService;
import com.forgerock.openbanking.commons.services.store.funds.FundsConfirmationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class FundsConfirmationConsentDecisionFactory {

    private FundsConfirmationService fundsConfirmationService;
    private ObjectMapper objectMapper;
    private AccountStoreService accountStoreService;

    public FundsConfirmationConsentDecisionFactory(FundsConfirmationService fundsConfirmationService, ObjectMapper objectMapper, AccountStoreService accountStoreService) {
        this.fundsConfirmationService = fundsConfirmationService;
        this.objectMapper = objectMapper;
        this.accountStoreService = accountStoreService;
    }

    public ConsentDecision create(final String intentId) {
        FRFundsConfirmationConsent1 consent = fundsConfirmationService.getConsent(intentId);
        return new FundsConfirmationConsentDecisionDelegate(fundsConfirmationService, accountStoreService, objectMapper, consent);
    }

}
