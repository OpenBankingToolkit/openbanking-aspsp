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
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.statements;

import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRStatement1;
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
