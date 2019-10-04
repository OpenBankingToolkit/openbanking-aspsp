/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.simulator.service;

import com.forgerock.openbanking.commons.model.openbanking.forgerock.FRAccount;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.FRBalance;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRTransaction5;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

public interface CreateTransaction<T> {
    FRTransaction5 createTransaction(FRAccount account, T payment, OBCreditDebitCode creditDebitCode, FRBalance balance, OBActiveOrHistoricCurrencyAndAmount amount);
  }