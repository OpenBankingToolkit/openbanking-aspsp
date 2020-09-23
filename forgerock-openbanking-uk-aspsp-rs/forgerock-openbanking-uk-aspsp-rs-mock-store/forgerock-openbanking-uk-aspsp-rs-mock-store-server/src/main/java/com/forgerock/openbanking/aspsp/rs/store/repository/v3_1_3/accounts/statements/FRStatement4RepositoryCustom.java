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
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public interface FRStatement4RepositoryCustom {

    Page<FRStatement4> byAccountIdWithPermissions(
            String accountId,
            DateTime fromStatementDateTime,
            DateTime toStatementDateTime,
            List<OBExternalPermissions1Code> permissions,
            Pageable pageable);

    List<FRStatement4> byAccountIdAndStatementIdWithPermissions(
            String accountId,
            String statementId,
            List<OBExternalPermissions1Code> permissions
    );

    Page<FRStatement4> byAccountIdInWithPermissions(
            List<String> accountIds,
            List<OBExternalPermissions1Code> permissions,
            Pageable pageable);

}
