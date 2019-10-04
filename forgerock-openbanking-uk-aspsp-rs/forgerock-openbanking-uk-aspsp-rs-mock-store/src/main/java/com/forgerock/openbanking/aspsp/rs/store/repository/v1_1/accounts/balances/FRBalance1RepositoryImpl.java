/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances;

import com.forgerock.openbanking.commons.model.openbanking.v1_1.account.FRBalance1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

@Repository
public class FRBalance1RepositoryImpl implements FRBalance1RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRBalance1RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRBalance1Repository balanceRepository;

    @Override
    public Page<FRBalance1> byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions,
                                                       Pageable pageable) {
        return filter(balanceRepository.findByAccountId(accountId, pageable), permissions);
    }

    @Override
    public Page<FRBalance1> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions,
                                                         Pageable pageable) {
        return filter(balanceRepository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRBalance1> filter(Page<FRBalance1> balances, List<OBExternalPermissions1Code> permissions) {
        return balances;
    }
}
