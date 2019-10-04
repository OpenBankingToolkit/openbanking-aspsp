/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.scheduledpayments;

import com.forgerock.openbanking.commons.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRScheduledPayment2;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


public interface FRScheduledPayment2Repository extends MongoRepository<FRScheduledPayment2, String>, FRScheduledPayment2RepositoryCustom {

    Page<FRScheduledPayment2> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    Page<FRScheduledPayment2> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteFRScheduledPayment1ByAccountId(@Param("accountId") String accountId);

    @Query("{ 'status' : ?0 }, { 'scheduledPayment.ScheduledPaymentDateTime' : { $lt: ?1 } }")
    List<FRScheduledPayment2> findByStatus(@Param("status") ScheduledPaymentStatus status, @Param("scheduledPayment.ScheduledPaymentDateTime") DateTime maxDateTime);

    Long countByAccountIdIn(Set<String> accountIds);

}
