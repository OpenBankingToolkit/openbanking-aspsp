/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.account;

import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;

import java.util.List;
import java.util.Optional;

public interface AccountStoreService {

    FRAccount getAccount(String accountId);

    Optional<FRAccount> findAccountByIdentification(String identification);

    List<FRAccount2> get(String userID);

    List<FRAccountWithBalance> getAccountWithBalances(String userID);

}
