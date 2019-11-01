/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.party;

import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRParty2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FRParty2Repository extends MongoRepository<FRParty2, String>, FRParty2RepositoryCustom {

    FRParty2 findByAccountId(@Param("accountId") String accountId);
    Page<FRParty2> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteFRParty2ByAccountId(@Param("accountId") String accountId);
    Long deleteFRParty2ByUserId(@Param("userId") String userId);
    FRParty2 findByUserId(@Param("userId") String userId);

}
