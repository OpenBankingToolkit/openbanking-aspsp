/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.beneficiaries;

import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRBeneficiary2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FRBeneficiary2Repository extends MongoRepository<FRBeneficiary2, String>, FRBeneficiary2RepositoryCustom {

    Page<FRBeneficiary2> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    Page<FRBeneficiary2> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteBeneficiaryByAccountId(@Param("accountId") String accountId);

}
