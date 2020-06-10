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

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.ConverterHelper.copyField;

public class OBAmountConverter {

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBWriteDomestic2DataInitiationInstructedAmount amount) {
        return toAmount(new OBActiveOrHistoricCurrencyAndAmount(), amount);
    }

    public static OBWriteDomestic2DataInitiationInstructedAmount toOBWriteDomestic2DataInitiationInstructedAmount(OBActiveOrHistoricCurrencyAndAmount amount) {
        return toAmount(new OBWriteDomestic2DataInitiationInstructedAmount(), amount);
    }

    public static OBWriteDomestic2DataInitiationInstructedAmount toOBWriteDomestic2DataInitiationInstructedAmount(OBDomestic2InstructedAmount amount) {
        return toAmount(new OBWriteDomestic2DataInitiationInstructedAmount(), amount);
    }

    public static OBDomestic2InstructedAmount toOBDomestic2InstructedAmount(OBWriteDomestic2DataInitiationInstructedAmount amount) {
        return toAmount(new OBDomestic2InstructedAmount(), amount);
    }

    public static OBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount toOBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount(OBDomesticStandingOrder3FirstPaymentAmount amount) {
        return toAmount(new OBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount(), amount);
    }

    public static OBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount toOBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount(OBDomesticStandingOrder3RecurringPaymentAmount amount) {
        return toAmount(new OBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount(), amount);
    }

    public static OBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount toOBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount(OBDomesticStandingOrder3FinalPaymentAmount amount) {
        return toAmount(new OBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount(), amount);
    }

    public static OBDomesticStandingOrder3FirstPaymentAmount toOBDomesticStandingOrder3FirstPaymentAmount(OBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount amount) {
        return toAmount(new OBDomesticStandingOrder3FirstPaymentAmount(), amount);
    }

    public static OBDomesticStandingOrder3RecurringPaymentAmount toOBDomesticStandingOrder3RecurringPaymentAmount(OBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount amount) {
        return toAmount(new OBDomesticStandingOrder3RecurringPaymentAmount(), amount);
    }

    public static OBDomesticStandingOrder3FinalPaymentAmount toOBDomesticStandingOrder3FinalPaymentAmount(OBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount amount) {
        return toAmount(new OBDomesticStandingOrder3FinalPaymentAmount(), amount);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBWriteDomestic2DataInitiationInstructedAmount amount) {
        return toAmount(new uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount(), amount);
    }

    private static <T, U> T toAmount(T newAmount, U originalAmount) {
        if (originalAmount == null) {
            return null;
        }
        copyField(newAmount, originalAmount, "currency");
        copyField(newAmount, originalAmount, "amount");
        return newAmount;
    }
}
