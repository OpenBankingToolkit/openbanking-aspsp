/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.singlepayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecision;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.commons.model.openbanking.v1_1.payment.FRPaymentSetup1;
import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.commons.model.rcs.consentdecision.SinglePaymentConsentDecision;
import com.forgerock.openbanking.commons.services.store.account.AccountStoreService;
import com.forgerock.openbanking.commons.services.store.payment.SinglePaymentService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
class SinglePaymentConsentDecisionDelegate implements ConsentDecision {

    private AccountStoreService accountsService;
    private SinglePaymentService paymentsService;
    private ObjectMapper objectMapper;
    private FRPaymentSetup1 payment;

    SinglePaymentConsentDecisionDelegate(AccountStoreService accountsService, SinglePaymentService paymentsService, ObjectMapper objectMapper, FRPaymentSetup1 payment) {
        this.accountsService = accountsService;
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
        SinglePaymentConsentDecision singlePaymentConsentDecision = objectMapper.readValue(consentDecisionSerialised, SinglePaymentConsentDecision.class);

        if (decision) {
            List<FRAccount2> accounts = accountsService.get(getUserIDBehindConsent());
            Optional<FRAccount2> isAny = accounts.stream().filter(account -> account.getId().equals(singlePaymentConsentDecision
                    .getAccountId())).findAny();
            if (!isAny.isPresent()) {
                log.error("The account selected {} is not own by this user {}. List accounts {}", singlePaymentConsentDecision
                        .getAccountId(), getUserIDBehindConsent(), accounts);
                throw new OBErrorException(OBRIErrorType.RCS_CONSENT_DECISION_INVALID_ACCOUNT,  getUserIDBehindConsent(), singlePaymentConsentDecision
                        .getAccountId(), accounts);
            }

            payment.setStatus(ConsentStatusCode.ACCEPTEDCUSTOMERPROFILE);
            payment.setAccountId(singlePaymentConsentDecision.getAccountId());
            paymentsService.updatePayment(payment);
        } else {
            log.debug("The current payment '{}' has been deny", payment.getId());
            payment.setStatus(ConsentStatusCode.REJECTED);
            paymentsService.updatePayment(payment);
        }
    }

    @Override
    public void autoaccept(List<FRAccount2> accounts, String username) throws OBErrorException {
        payment.setStatus(ConsentStatusCode.ACCEPTEDCUSTOMERPROFILE);
        payment.setAccountId(accounts.get(0).getId());
        paymentsService.updatePayment(payment);
    }
}
