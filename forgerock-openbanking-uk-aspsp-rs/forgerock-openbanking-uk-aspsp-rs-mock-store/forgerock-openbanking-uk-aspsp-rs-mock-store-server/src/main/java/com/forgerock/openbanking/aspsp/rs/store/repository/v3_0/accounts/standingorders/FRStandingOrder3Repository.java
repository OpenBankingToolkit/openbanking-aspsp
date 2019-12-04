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
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.accounts.standingorders;

import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.v3_0.account.FRStandingOrder3;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;


public interface FRStandingOrder3Repository extends MongoRepository<FRStandingOrder3, String>, FRStandingOrder3RepositoryCustom {

    Page<FRStandingOrder3> findByAccountId(@Param("accountId") String accountId, Pageable pageable);

    Page<FRStandingOrder3> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);

    Long deleteStandingOrderByAccountId(@Param("accountId") String accountId);

    List<FRStandingOrder3> findByStatusIn(@Param("statuses") Collection<StandingOrderStatus> statuses);
}
