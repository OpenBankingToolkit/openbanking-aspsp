/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.products;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRProduct1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FRProduct1Repository extends MongoRepository<FRProduct1, String>, FRProduct1RepositoryCustom {

    Page<FRProduct1> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    Page<FRProduct1> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteProductByAccountId(@Param("accountId") String accountId);

}
