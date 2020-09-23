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
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.accounts;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_3.FRAccount4;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.Collection;
import java.util.List;

public interface FRAccount4RepositoryCustom {

    Collection<FRAccount4> byUserIDWithPermissions(
            @Param("userID") String userID,
            @Param("permissions") List<OBExternalPermissions1Code> permissions,
            Pageable pageable);

    FRAccount4 byAccountId(
            @Param("accountId") String accountId,
            @Param("permissions") List<OBExternalPermissions1Code> permissions);

    List<FRAccount4> byAccountIds(
            @Param("accountId") List<String> accountIds,
            @Param("permissions") List<OBExternalPermissions1Code> permissions);

    List<String> getUserIds(DateTime from, DateTime to);

}
