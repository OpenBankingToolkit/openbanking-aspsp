/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.directdebits;

import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.standingorders.FRStandingOrder1RepositoryImpl;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRDirectDebit1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public class FRDirectDebit1RepositoryImpl implements FRDirectDebit1RepositoryCustom {

    private static final Logger LOGGER = LoggerFactory.getLogger(FRStandingOrder1RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRDirectDebit1Repository directDebitRepository;

    @Override
    public Page<FRDirectDebit1> byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions,
                                                           Pageable pageable) {
        return filter(directDebitRepository.findByAccountId(accountId, pageable), permissions);
    }

    @Override
    public Page<FRDirectDebit1> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(directDebitRepository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRDirectDebit1> filter(Page<FRDirectDebit1> directDebits, List<OBExternalPermissions1Code> permissions) {
        return directDebits;
    }
}
