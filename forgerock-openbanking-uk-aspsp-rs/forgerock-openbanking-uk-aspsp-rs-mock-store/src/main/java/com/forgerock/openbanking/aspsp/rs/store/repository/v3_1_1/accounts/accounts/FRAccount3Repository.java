/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.accounts;

import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRAccount3;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;


public interface FRAccount3Repository extends MongoRepository<FRAccount3, String>, FRAccount3RepositoryCustom {

    Collection<FRAccount3> findByUserID(@Param("userID") String userID);
    Optional<FRAccount3> findByAccountAccountIdentification(@Param("identification") String identification);
}
