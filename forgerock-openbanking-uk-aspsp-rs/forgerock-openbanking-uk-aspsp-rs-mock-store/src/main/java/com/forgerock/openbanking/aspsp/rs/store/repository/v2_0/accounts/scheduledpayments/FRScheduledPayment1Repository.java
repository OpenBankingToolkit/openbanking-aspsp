/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.scheduledpayments;

import com.forgerock.openbanking.commons.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRScheduledPayment1;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FRScheduledPayment1Repository extends MongoRepository<FRScheduledPayment1, String>, FRScheduledPayment1RepositoryCustom {

    Page<FRScheduledPayment1> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    Page<FRScheduledPayment1> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteFRScheduledPayment1ByAccountId(@Param("accountId") String accountId);

    @Query("{ 'status' : ?0 }, { 'scheduledPayment.ScheduledPaymentDateTime' : { $lt: ?1 } }")
    List<FRScheduledPayment1> findByStatus(@Param("status") ScheduledPaymentStatus status, @Param("scheduledPayment.ScheduledPaymentDateTime") DateTime maxDateTime);
}
