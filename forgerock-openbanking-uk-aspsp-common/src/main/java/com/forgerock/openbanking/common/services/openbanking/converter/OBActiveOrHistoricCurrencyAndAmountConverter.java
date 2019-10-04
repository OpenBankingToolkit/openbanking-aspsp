/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
