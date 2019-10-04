/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.party;

import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRParty2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public class FRParty2RepositoryImpl implements FRParty2RepositoryCustom {
    @Autowired
    @Lazy
    private FRParty2Repository party1Repository;

    @Override
    public FRParty2 byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions) {
        return filter(party1Repository.findByAccountId(accountId), permissions);
    }

    @Override
    public FRParty2 byUserIdWithPermissions(String userId, List<OBExternalPermissions1Code> permissions) {
        return filter(party1Repository.findByUserId(userId), permissions);
    }

    @Override
    public Page<FRParty2> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(party1Repository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRParty2> filter(Page<FRParty2> parties, List<OBExternalPermissions1Code> permissions) {
        return parties;
    }

    private FRParty2 filter(FRParty2 party, List<OBExternalPermissions1Code> permissions) {
        return party;
    }
}
