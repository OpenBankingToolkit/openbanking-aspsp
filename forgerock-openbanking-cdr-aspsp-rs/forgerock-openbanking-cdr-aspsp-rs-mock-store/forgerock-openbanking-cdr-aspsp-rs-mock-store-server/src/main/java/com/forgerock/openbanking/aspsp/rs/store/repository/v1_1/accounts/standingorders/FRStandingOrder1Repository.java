/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.standingorders;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRStandingOrder1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FRStandingOrder1Repository extends MongoRepository<FRStandingOrder1, String>, FRStandingOrder1RepositoryCustom {

    Page<FRStandingOrder1> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    Page<FRStandingOrder1> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteStandingOrderByAccountId(@Param("accountId") String accountId);
}
