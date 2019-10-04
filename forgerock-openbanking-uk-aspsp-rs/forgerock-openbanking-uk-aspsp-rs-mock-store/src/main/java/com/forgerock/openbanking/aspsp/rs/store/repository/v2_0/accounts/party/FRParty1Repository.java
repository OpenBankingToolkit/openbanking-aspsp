/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.party;

import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRParty1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FRParty1Repository extends MongoRepository<FRParty1, String>, FRParty1RepositoryCustom {

    FRParty1 findByAccountId(@Param("accountId") String accountId);
    Page<FRParty1> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteFRParty1ByAccountId(@Param("accountId") String accountId);
    Long deleteFRParty1ByUserId(@Param("userId") String userId);
    FRParty1 findByUserId(@Param("userId") String userId);

}
