/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.party;

import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRParty1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public class FRParty1RepositoryImpl implements FRParty1RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRParty1RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRParty1Repository party1Repository;

    @Override
    public FRParty1 byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions) {
        return filter(party1Repository.findByAccountId(accountId), permissions);
    }

    @Override
    public FRParty1 byUserIdWithPermissions(String userId, List<OBExternalPermissions1Code> permissions) {
        return filter(party1Repository.findByUserId(userId), permissions);
    }

    @Override
    public Page<FRParty1> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(party1Repository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRParty1> filter(Page<FRParty1> parties, List<OBExternalPermissions1Code> permissions) {
        return parties;
    }

    private FRParty1 filter(FRParty1 party, List<OBExternalPermissions1Code> permissions) {
        return party;
    }
}
