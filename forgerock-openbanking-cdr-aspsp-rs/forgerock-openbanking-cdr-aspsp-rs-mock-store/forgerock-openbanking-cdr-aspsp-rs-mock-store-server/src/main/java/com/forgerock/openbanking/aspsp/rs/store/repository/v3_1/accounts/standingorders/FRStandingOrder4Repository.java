/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.accounts.standingorders;

import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.v3_1.account.FRStandingOrder4;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;


public interface FRStandingOrder4Repository extends MongoRepository<FRStandingOrder4, String>, FRStandingOrder4RepositoryCustom {

    Page<FRStandingOrder4> findByAccountId(@Param("accountId") String accountId, Pageable pageable);

    Page<FRStandingOrder4> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteStandingOrderByAccountId(@Param("accountId") String accountId);

    List<FRStandingOrder4> findByStatusIn(@Param("statuses") Collection<StandingOrderStatus> statuses);
}
