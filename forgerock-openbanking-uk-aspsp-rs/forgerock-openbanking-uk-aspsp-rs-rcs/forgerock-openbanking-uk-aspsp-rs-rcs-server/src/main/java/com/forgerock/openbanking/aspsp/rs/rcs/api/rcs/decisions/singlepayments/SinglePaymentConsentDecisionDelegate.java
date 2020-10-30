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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.singlepayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRPaymentSetup;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.model.rcs.consentdecision.SinglePaymentConsentDecision;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.payment.SinglePaymentService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
class SinglePaymentConsentDecisionDelegate implements ConsentDecisionDelegate {

    private AccountStoreService accountsService;
    private SinglePaymentService paymentsService;
    private ObjectMapper objectMapper;
    private FRPaymentSetup payment;

    SinglePaymentConsentDecisionDelegate(AccountStoreService accountsService, SinglePaymentService paymentsService, ObjectMapper objectMapper, FRPaymentSetup payment) {
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
            List<FRAccount> accounts = accountsService.get(getUserIDBehindConsent());
            Optional<FRAccount> isAny = accounts.stream().filter(account -> account.getId().equals(singlePaymentConsentDecision
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
    public void autoaccept(List<FRAccount> accounts, String username) throws OBErrorException {
        payment.setStatus(ConsentStatusCode.ACCEPTEDCUSTOMERPROFILE);
        payment.setAccountId(accounts.get(0).getId());
        paymentsService.updatePayment(payment);
    }
}
