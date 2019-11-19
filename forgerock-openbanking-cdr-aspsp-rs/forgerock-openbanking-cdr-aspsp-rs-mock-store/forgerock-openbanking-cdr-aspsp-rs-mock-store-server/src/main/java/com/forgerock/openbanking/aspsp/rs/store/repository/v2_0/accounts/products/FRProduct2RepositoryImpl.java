/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.products;

import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRProduct2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public class FRProduct2RepositoryImpl implements FRProduct2RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRProduct2RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRProduct2Repository productRepository;

    @Override
    public Page<FRProduct2> byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(productRepository.findByAccountId(accountId, pageable), permissions);
    }

    @Override
    public Page<FRProduct2> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(productRepository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRProduct2> filter(Page<FRProduct2> products, List<OBExternalPermissions1Code> permissions) {
        return products;
    }
}