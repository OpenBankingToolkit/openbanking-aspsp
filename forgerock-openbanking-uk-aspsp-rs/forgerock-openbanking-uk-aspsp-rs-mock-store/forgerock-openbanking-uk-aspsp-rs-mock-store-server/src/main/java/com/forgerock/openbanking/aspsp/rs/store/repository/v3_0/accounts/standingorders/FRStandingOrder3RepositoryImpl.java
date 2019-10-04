/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.accounts.standingorders;

import com.forgerock.openbanking.common.model.openbanking.v3_0.account.FRStandingOrder3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public class FRStandingOrder3RepositoryImpl implements FRStandingOrder3RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRStandingOrder3RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRStandingOrder3Repository standingOrderRepository;

    @Override
    public Page<FRStandingOrder3> byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(standingOrderRepository.findByAccountId(accountId, pageable), permissions);
    }

    @Override
    public Page<FRStandingOrder3> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(standingOrderRepository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRStandingOrder3> filter(Page<FRStandingOrder3> standingOrders, List<OBExternalPermissions1Code> permissions) {
        for (OBExternalPermissions1Code permission : permissions) {
            switch (permission) {

                case READSTANDINGORDERSBASIC:
                    for (FRStandingOrder3 standingOrder: standingOrders) {
                        standingOrder.getStandingOrder().creditorAccount(null);
                    }
                    break;
            }
        }
        return standingOrders;
    }
}
