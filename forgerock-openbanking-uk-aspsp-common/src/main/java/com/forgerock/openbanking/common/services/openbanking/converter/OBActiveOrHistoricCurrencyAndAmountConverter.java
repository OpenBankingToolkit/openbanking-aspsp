/**
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
package com.forgerock.openbanking.common.services.openbanking.converter;

import uk.org.openbanking.datamodel.payment.*;

public final class OBActiveOrHistoricCurrencyAndAmountConverter {

    public static OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount instructedAmount) {
        return FRModelMapper.map(instructedAmount, OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount instructedAmount) {
        return FRModelMapper.map(instructedAmount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBDomesticStandingOrder3FinalPaymentAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount.class);
    }
    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBDomesticStandingOrder3FirstPaymentAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBDomesticStandingOrder3RecurringPaymentAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBDomesticStandingOrder3FinalPaymentAmount amount) {
        return FRModelMapper.map(amount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBDomesticStandingOrder3FirstPaymentAmount amount) {
        return FRModelMapper.map(amount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBDomesticStandingOrder3RecurringPaymentAmount amount) {
        return FRModelMapper.map(amount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount toPaymentOBActiveOrHistoricCurrencyAndAmount(OBDomestic2InstructedAmount instructedAmount) {
        return FRModelMapper.map(instructedAmount, OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBDomestic2InstructedAmount instructedAmount) {
        return FRModelMapper.map(instructedAmount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount.class);
    }
}
