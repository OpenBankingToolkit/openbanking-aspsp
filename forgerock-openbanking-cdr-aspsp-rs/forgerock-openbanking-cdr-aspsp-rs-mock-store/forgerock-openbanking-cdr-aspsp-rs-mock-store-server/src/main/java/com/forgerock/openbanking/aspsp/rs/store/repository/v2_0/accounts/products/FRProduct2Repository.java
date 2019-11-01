/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.products;

import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRProduct2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FRProduct2Repository extends MongoRepository<FRProduct2, String>, FRProduct2RepositoryCustom {

    Page<FRProduct2> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    Page<FRProduct2> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteProductByAccountId(@Param("accountId") String accountId);

}
