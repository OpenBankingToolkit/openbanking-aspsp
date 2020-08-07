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
package com.forgerock.openbanking.common.services.openbanking.converter.account;

import com.forgerock.openbanking.common.services.openbanking.converter.FRModelMapper;
import uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount1;
import uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount10;
import uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount2;
import uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount3;
import uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount4;
import uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount9;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.payment.OBWriteDomestic2DataInitiationInstructedAmount;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount;

public class OBAmountConverter {

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount1 amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount9 amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount10 amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount2 amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount3 amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount4 amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount2 toOBActiveOrHistoricCurrencyAndAmount2(OBActiveOrHistoricCurrencyAndAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount2.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount3 toOBActiveOrHistoricCurrencyAndAmount3(OBActiveOrHistoricCurrencyAndAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount3.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount4 toOBActiveOrHistoricCurrencyAndAmount4(OBActiveOrHistoricCurrencyAndAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount4.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount2 toOBActiveOrHistoricCurrencyAndAmount2(OBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount2.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount3 toOBActiveOrHistoricCurrencyAndAmount3(OBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount3.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount4 toOBActiveOrHistoricCurrencyAndAmount4(OBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount4.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount9 toOBActiveOrHistoricCurrencyAndAmount9(OBActiveOrHistoricCurrencyAndAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount9.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount2 toOBActiveOrHistoricCurrencyAndAmount2(OBWriteDomestic2DataInitiationInstructedAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount2.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount3 toOBActiveOrHistoricCurrencyAndAmount3(OBWriteDomestic2DataInitiationInstructedAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount3.class);
    }

    public static OBActiveOrHistoricCurrencyAndAmount4 toOBActiveOrHistoricCurrencyAndAmount4(OBWriteDomestic2DataInitiationInstructedAmount amount) {
        return FRModelMapper.map(amount, OBActiveOrHistoricCurrencyAndAmount4.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount9 amount) {
        return FRModelMapper.map(amount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount10 amount) {
        return FRModelMapper.map(amount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount4 amount) {
        return FRModelMapper.map(amount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount3(OBActiveOrHistoricCurrencyAndAmount3 amount) {
        return FRModelMapper.map(amount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount1 amount) {
        return FRModelMapper.map(amount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount amount) {
        return FRModelMapper.map(amount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount2 amount) {
        return FRModelMapper.map(amount, uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount.class);
    }
}
