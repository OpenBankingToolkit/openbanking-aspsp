/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.standingorders;

import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRStandingOrder2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public interface FRStandingOrder2RepositoryCustom {

    Page<FRStandingOrder2> byAccountIdWithPermissions(
            @Param("accountId") String accountId,
            @Param("permission") List<OBExternalPermissions1Code> permissions,
            Pageable pageable);

    Page<FRStandingOrder2> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable);
}
