/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.offers;

import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FROffer1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public class FROffer1RepositoryImpl implements FROffer1RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FROffer1RepositoryImpl.class);

    @Autowired
    @Lazy
    private FROffer1Repository offer1Repository;

    @Override
    public Page<FROffer1> byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(offer1Repository.findByAccountId(accountId, pageable), permissions);
    }

    @Override
    public Page<FROffer1> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(offer1Repository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FROffer1> filter(Page<FROffer1> offers, List<OBExternalPermissions1Code> permissions) {
        return offers;
    }
}