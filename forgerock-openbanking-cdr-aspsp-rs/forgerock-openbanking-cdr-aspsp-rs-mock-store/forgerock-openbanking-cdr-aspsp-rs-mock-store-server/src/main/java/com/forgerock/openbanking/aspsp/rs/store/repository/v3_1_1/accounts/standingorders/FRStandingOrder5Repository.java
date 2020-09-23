/**
 * Copyright 2019 ForgeRock AS.
 *
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
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.standingorders;

import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_1.FRStandingOrder5;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface FRStandingOrder5Repository extends MongoRepository<FRStandingOrder5, String>, FRStandingOrder5RepositoryCustom {

    Page<FRStandingOrder5> findByAccountId(@Param("accountId") String accountId, Pageable pageable);

    Page<FRStandingOrder5> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteStandingOrderByAccountId(@Param("accountId") String accountId);

    List<FRStandingOrder5> findByStatusIn(@Param("statuses") Collection<StandingOrderStatus> statuses);

    Long countByAccountIdIn(Set<String> accountIds);
}
