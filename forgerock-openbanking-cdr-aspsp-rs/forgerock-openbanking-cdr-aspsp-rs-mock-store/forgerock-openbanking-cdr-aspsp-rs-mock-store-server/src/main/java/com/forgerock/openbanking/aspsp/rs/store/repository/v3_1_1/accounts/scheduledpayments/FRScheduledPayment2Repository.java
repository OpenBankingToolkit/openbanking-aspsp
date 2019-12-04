/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.scheduledpayments;

import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRScheduledPayment2;
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
