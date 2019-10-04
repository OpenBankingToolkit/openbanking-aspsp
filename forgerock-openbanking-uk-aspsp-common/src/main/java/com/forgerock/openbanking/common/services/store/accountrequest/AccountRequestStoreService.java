/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.accountrequest;


import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountRequest;

import java.util.Optional;

public interface AccountRequestStoreService {

    Optional<FRAccountRequest> get(String accountRequestId);

    void save(FRAccountRequest accountRequest);
}
