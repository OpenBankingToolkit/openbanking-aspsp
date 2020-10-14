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
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.accountrequests;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRAccountRequest1;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FRAccountRequest1Repository extends MongoRepository<FRAccountRequest1, String> {

    Optional<FRAccountRequest1> findByAccountRequestId(@Param("accountRequestId") String accountRequestId);
    Collection<FRAccountRequest1> findByUserId(@Param("userId") String userId);
    Collection<FRAccountRequest1> findByUserIdAndClientId(@Param("userId") String userId, @Param("clientId") String clientId);
    Collection<FRAccountRequest1> findByUserIdAndAccountRequestDataStatusIn(@Param("userId") String userId, @Param("statuses") List<OBExternalRequestStatus1Code> status);
}