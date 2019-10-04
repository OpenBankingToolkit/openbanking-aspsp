/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.statements;

import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRStatement1;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;


public interface FRStatement1Repository extends MongoRepository<FRStatement1, String>, FRStatement1RepositoryCustom {

    Page<FRStatement1> findByAccountIdAndStartDateTimeBetweenAndEndDateTimeBetween(
            String accountId,
            DateTime fromStartDateTime,
            DateTime toStartDateTime,
            DateTime fromEndDateTime,
            DateTime toEndDateTime,
            Pageable pageable
    );

    Page<FRStatement1> findByAccountId(
            String accountId,
            Pageable pageable
    );

    List<FRStatement1> findByAccountIdAndId(
            String accountId,
            String id
    );

    Page<FRStatement1> findByAccountIdIn(
            List<String> accountIds,
            Pageable pageable
    );

    Long deleteFRStatement1ByAccountId(
            String accountId
    );

    Long countByAccountIdIn(Set<String> accountIds);
}
