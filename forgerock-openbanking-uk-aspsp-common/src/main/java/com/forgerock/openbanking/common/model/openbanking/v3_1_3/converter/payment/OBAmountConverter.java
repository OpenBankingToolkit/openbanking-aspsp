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
package com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment;

import uk.org.openbanking.datamodel.payment.*;

public class OBAmountConverter {

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBWriteDomestic2DataInitiationInstructedAmount amount) {
        return (new OBActiveOrHistoricCurrencyAndAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }

    public static OBWriteDomestic2DataInitiationInstructedAmount toOBWriteDomestic2DataInitiationInstructedAmount(OBActiveOrHistoricCurrencyAndAmount amount) {
        return (new OBWriteDomestic2DataInitiationInstructedAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }

    public static OBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount toOBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount(OBDomesticStandingOrder3FirstPaymentAmount amount) {
        return (new OBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }

    public static OBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount toOBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount(OBDomesticStandingOrder3RecurringPaymentAmount amount) {
        return (new OBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }

    public static OBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount toOBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount(OBDomesticStandingOrder3FinalPaymentAmount amount) {
        return (new OBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }

    public static OBDomesticStandingOrder3FirstPaymentAmount toOBDomesticStandingOrder3FirstPaymentAmount(OBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount amount) {
        return (new OBDomesticStandingOrder3FirstPaymentAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }

    public static OBDomesticStandingOrder3RecurringPaymentAmount toOBDomesticStandingOrder3RecurringPaymentAmount(OBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount amount) {
        return (new OBDomesticStandingOrder3RecurringPaymentAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }

    public static OBDomesticStandingOrder3FinalPaymentAmount toOBDomesticStandingOrder3FinalPaymentAmount(OBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount amount) {
        return (new OBDomesticStandingOrder3FinalPaymentAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }
}
