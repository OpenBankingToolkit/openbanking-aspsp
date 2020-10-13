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
package com.forgerock.openbanking.aspsp.rs.store.repository.accounts.directdebits;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRDirectDebit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;

import java.util.List;

public interface FRDirectDebitRepositoryCustom {

    Page<FRDirectDebit> byAccountIdWithPermissions(
            @Param("accountId") String accountId,
            @Param("permissions") List<FRExternalPermissionsCode> permissions,
            Pageable pageable);

    Page<FRDirectDebit> byAccountIdInWithPermissions(
            @Param("accountIds") List<String> accountIds,
            @Param("permissions") List<FRExternalPermissionsCode> permissions,
            Pageable pageable);

}
