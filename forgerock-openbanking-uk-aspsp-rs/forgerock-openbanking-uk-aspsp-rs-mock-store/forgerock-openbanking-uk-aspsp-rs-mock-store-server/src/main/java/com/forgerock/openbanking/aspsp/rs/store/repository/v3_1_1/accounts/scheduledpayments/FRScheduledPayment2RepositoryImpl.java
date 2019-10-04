/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.scheduledpayments;

import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRScheduledPayment2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

public class FRScheduledPayment2RepositoryImpl implements FRScheduledPayment2RepositoryCustom {

    @Autowired
    @Lazy
    private FRScheduledPayment2Repository scheduledPayment1Repository;

    @Override
    public Page<FRScheduledPayment2> byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(scheduledPayment1Repository.findByAccountId(accountId, pageable), permissions);
    }

    @Override
    public Page<FRScheduledPayment2> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions, Pageable pageable) {
        return filter(scheduledPayment1Repository.findByAccountIdIn(accountIds, pageable), permissions);
    }


    private Page<FRScheduledPayment2> filter(Page<FRScheduledPayment2> scheduledPayments, List<OBExternalPermissions1Code> permissions) {

        for (FRScheduledPayment2 scheduledPayment: scheduledPayments) {
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
