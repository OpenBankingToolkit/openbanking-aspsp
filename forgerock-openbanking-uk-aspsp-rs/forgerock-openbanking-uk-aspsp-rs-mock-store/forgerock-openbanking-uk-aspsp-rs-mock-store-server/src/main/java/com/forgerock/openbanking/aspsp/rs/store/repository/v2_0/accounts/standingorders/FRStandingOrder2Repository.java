/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.standingorders;

import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRStandingOrder2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FRStandingOrder2Repository extends MongoRepository<FRStandingOrder2, String>, FRStandingOrder2RepositoryCustom {

    Page<FRStandingOrder2> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    Page<FRStandingOrder2> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteStandingOrderByAccountId(@Param("accountId") String accountId);
}
