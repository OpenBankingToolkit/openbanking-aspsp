/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.beneficiaries;

import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRBeneficiary3;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


public interface FRBeneficiary3Repository extends MongoRepository<FRBeneficiary3, String>, FRBeneficiary3RepositoryCustom {

    Page<FRBeneficiary3> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    Page<FRBeneficiary3> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteBeneficiaryByAccountId(@Param("accountId") String accountId);

    Long countByAccountIdIn(Set<String> accountIds);
}
