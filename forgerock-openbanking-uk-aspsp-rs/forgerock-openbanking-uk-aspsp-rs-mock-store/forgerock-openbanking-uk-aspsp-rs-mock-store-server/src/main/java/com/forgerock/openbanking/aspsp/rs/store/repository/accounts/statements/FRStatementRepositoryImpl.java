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
package com.forgerock.openbanking.aspsp.rs.store.repository.accounts.statements;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStatement;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public class FRStatementRepositoryImpl implements FRStatementRepositoryCustom {

    @Autowired
    @Lazy
    private FRStatementRepository statement1Repository;

    @Override
    public Page<FRStatement> byAccountIdWithPermissions(
            String accountId,
            DateTime fromStatementDateTime,
            DateTime toStatementDateTime,
            List<OBExternalPermissions1Code> permissions, Pageable pageable) {

        if (fromStatementDateTime == null && toStatementDateTime == null) {
            return filter(statement1Repository.findByAccountId(accountId, pageable), permissions);
        } else {
            return filter(statement1Repository.findByAccountIdAndStartDateTimeBetweenAndEndDateTimeBetween(accountId, fromStatementDateTime, toStatementDateTime, fromStatementDateTime, toStatementDateTime, pageable), permissions);
        }
    }

    @Override
    public List<FRStatement> byAccountIdAndStatementIdWithPermissions(
            String accountId,
            String statementId,
            List<OBExternalPermissions1Code> permissions) {

        return filter(statement1Repository.findByAccountIdAndId(accountId, statementId), permissions);
    }

    @Override
    public Page<FRStatement> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(statement1Repository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRStatement> filter(Page<FRStatement> statements, List<OBExternalPermissions1Code> permissions) {
        for (OBExternalPermissions1Code permission : permissions) {
            for (FRStatement statement : statements) {
                switch (permission) {
                    case READSTATEMENTSBASIC:
                        statement.getStatement().setStatementAmount(null);
                        break;
                }
            }
        }
        return statements;
    }

    private List<FRStatement> filter(List<FRStatement> statements, List<OBExternalPermissions1Code> permissions) {
        for (OBExternalPermissions1Code permission : permissions) {
            for (FRStatement statement : statements) {
                switch (permission) {
                    case READSTATEMENTSBASIC:
                        statement.getStatement().setStatementAmount(null);
                        break;
                }
            }
        }
        return statements;
    }
}
