/**
 * Copyright 2019 ForgeRock AS.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
