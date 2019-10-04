/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.accounts;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRAccount1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;


public interface FRAccount1Repository extends MongoRepository<FRAccount1, String>, FRAccount1RepositoryCustom {

    Page<FRAccount1> findByUserID(@Param("userID") String userID, Pageable pageable);
}
