/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.statements;

import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRStatement1;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public class FRStatement1RepositoryImpl implements FRStatement1RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRStatement1RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRStatement1Repository statement1Repository;

    @Override
    public Page<FRStatement1> byAccountIdWithPermissions(
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
    public List<FRStatement1> byAccountIdAndStatementIdWithPermissions(
            String accountId,
            String statementId,
            List<OBExternalPermissions1Code> permissions) {

        return filter(statement1Repository.findByAccountIdAndId(accountId, statementId), permissions);
    }

    @Override
    public Page<FRStatement1> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(statement1Repository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRStatement1> filter(Page<FRStatement1> statements, List<OBExternalPermissions1Code> permissions) {
        for (OBExternalPermissions1Code permission : permissions) {
            for (FRStatement1 statement : statements) {
                switch (permission) {
                    case READSTATEMENTSBASIC:
                        statement.getStatement().setStatementAmount(null);
                        break;
                }
            }
        }
        return statements;
    }

    private List<FRStatement1> filter(List<FRStatement1> statements, List<OBExternalPermissions1Code> permissions) {
        for (OBExternalPermissions1Code permission : permissions) {
            for (FRStatement1 statement : statements) {
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
