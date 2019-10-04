/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.transaction;

import com.forgerock.openbanking.common.model.openbanking.v3_1.account.FRTransaction4;

public interface Transaction4StoreService {

    FRTransaction4 create(FRTransaction4 transaction);
}