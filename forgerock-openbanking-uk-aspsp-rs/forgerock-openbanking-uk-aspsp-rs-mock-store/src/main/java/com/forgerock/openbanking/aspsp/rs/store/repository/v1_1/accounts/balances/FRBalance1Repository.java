/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances;

import com.forgerock.openbanking.commons.model.openbanking.v1_1.account.FRBalance1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface FRBalance1Repository extends MongoRepository<FRBalance1, String>, FRBalance1RepositoryCustom {

    Page<FRBalance1> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    Page<FRBalance1> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);
    Collection<FRBalance1> findByAccountIdIn(@Param("accountIds") List<String> accountIds);
    Optional<FRBalance1> findByAccountIdAndBalanceType(@Param("accountId") String accountId, @Param("type") OBBalanceType1Code type);

    Long deleteBalanceByAccountId(@Param("accountId") String accountId);

    Long countByAccountIdIn(Set<String> accountIds);
}
