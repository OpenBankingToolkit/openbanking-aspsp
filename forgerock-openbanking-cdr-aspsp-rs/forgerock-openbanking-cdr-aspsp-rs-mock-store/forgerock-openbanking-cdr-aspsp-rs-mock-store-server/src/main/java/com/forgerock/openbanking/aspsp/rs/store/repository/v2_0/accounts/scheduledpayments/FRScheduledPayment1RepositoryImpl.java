/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.scheduledpayments;

import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRScheduledPayment1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public class FRScheduledPayment1RepositoryImpl implements FRScheduledPayment1RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRScheduledPayment1RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRScheduledPayment1Repository scheduledPayment1Repository;

    @Override
    public Page<FRScheduledPayment1> byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(scheduledPayment1Repository.findByAccountId(accountId, pageable), permissions);
    }

    @Override
    public Page<FRScheduledPayment1> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(scheduledPayment1Repository.findByAccountIdIn(accountIds, pageable), permissions);
    }


    private Page<FRScheduledPayment1> filter(Page<FRScheduledPayment1> scheduledPayments, List<OBExternalPermissions1Code> permissions) {

        for (FRScheduledPayment1 scheduledPayment: scheduledPayments) {
            for (OBExternalPermissions1Code permission : permissions) {
                switch (permission) {
                    case READSCHEDULEDPAYMENTSBASIC:
                        scheduledPayment.getScheduledPayment().setCreditorAccount(null);
                        scheduledPayment.getScheduledPayment().setCreditorAgent(null);
                        break;
                }
            }
        }
        return scheduledPayments;
    }
}
