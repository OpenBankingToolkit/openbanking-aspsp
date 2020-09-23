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
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.statements;

import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_3.account.FRStatement4;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;


public interface FRStatement4Repository extends MongoRepository<FRStatement4, String>, FRStatement4RepositoryCustom {

    Page<FRStatement4> findByAccountIdAndStartDateTimeBetweenAndEndDateTimeBetween(
            String accountId,
            DateTime fromStartDateTime,
            DateTime toStartDateTime,
            DateTime fromEndDateTime,
            DateTime toEndDateTime,
            Pageable pageable
    );

    Page<FRStatement4> findByAccountId(
            String accountId,
            Pageable pageable
    );

    List<FRStatement4> findByAccountIdAndId(
            String accountId,
            String id
    );

    Page<FRStatement4> findByAccountIdIn(
            List<String> accountIds,
            Pageable pageable
    );

    Long deleteFRStatement4ByAccountId(
            String accountId
    );

    Long countByAccountIdIn(Set<String> accountIds);
}
