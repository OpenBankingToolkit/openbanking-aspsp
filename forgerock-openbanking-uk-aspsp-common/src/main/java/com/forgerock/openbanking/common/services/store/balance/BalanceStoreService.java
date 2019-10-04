/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.balance;

import com.forgerock.openbanking.common.model.openbanking.forgerock.FRBalance;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;

import java.util.Optional;

public interface BalanceStoreService {

    Optional<FRBalance> getBalance(String accountId, OBBalanceType1Code type);

    void updateBalance(FRBalance balance);
}
