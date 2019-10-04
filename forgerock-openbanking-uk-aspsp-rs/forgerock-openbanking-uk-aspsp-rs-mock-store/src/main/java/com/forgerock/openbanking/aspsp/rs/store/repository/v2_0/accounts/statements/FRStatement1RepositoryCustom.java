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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public interface FRStatement1RepositoryCustom {

    Page<FRStatement1> byAccountIdWithPermissions(
            String accountId,
            DateTime fromStatementDateTime,
            DateTime toStatementDateTime,
            List<OBExternalPermissions1Code> permissions,
            Pageable pageable);

    List<FRStatement1> byAccountIdAndStatementIdWithPermissions(
            String accountId,
            String statementId,
            List<OBExternalPermissions1Code> permissions
    );

    Page<FRStatement1> byAccountIdInWithPermissions(
            List<String> accountIds,
            List<OBExternalPermissions1Code> permissions,
            Pageable pageable);

}
