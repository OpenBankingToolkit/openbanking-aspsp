/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock;

import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

import java.math.BigDecimal;

public interface FRBalance {

    OBActiveOrHistoricCurrencyAndAmount getCurrencyAndAmount();

    BigDecimal getAmount();

    String getCurrency();

    OBCreditDebitCode getCreditDebitIndicator();

    void setAmount(BigDecimal amount);

    void setCreditDebitIndicator(OBCreditDebitCode code);

}
