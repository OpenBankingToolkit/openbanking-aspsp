/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBalance1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;


public interface FRBalance1RepositoryCustom {

    Page<FRBalance1> byAccountIdWithPermissions(
            @Param("accountId") String accountId,
            @Param("permissions") List<OBExternalPermissions1Code> permissions,
            Pageable pageable);

    Page<FRBalance1> byAccountIdInWithPermissions(
            @Param("accountIds") List<String> accountIds,
            @Param("permissions") List<OBExternalPermissions1Code> permissions,
            Pageable pageable);
}
