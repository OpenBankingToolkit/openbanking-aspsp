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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.domesticscheduledpayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.PaymentConsentDecisionUpdater;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticScheduledConsent2;
import com.forgerock.openbanking.common.services.store.payment.DomesticScheduledPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DomesticScheduledPaymentConsentDecisionFactory {

    private PaymentConsentDecisionUpdater paymentConsentDecisionUpdater;
    private DomesticScheduledPaymentService paymentsService;
    private ObjectMapper objectMapper;

    public DomesticScheduledPaymentConsentDecisionFactory(PaymentConsentDecisionUpdater paymentConsentDecisionUpdater, DomesticScheduledPaymentService paymentsService, ObjectMapper objectMapper) {
        this.paymentConsentDecisionUpdater = paymentConsentDecisionUpdater;
        this.paymentsService = paymentsService;
        this.objectMapper = objectMapper;
    }

    public ConsentDecisionDelegate create(final String intentId) {
        FRDomesticScheduledConsent2 consent = paymentsService.getPayment(intentId);
        return new DomesticScheduledPaymentConsentDecisionDelegate(paymentConsentDecisionUpdater, paymentsService, objectMapper, consent);
    }

}
