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
package com.forgerock.openbanking.common.services.openbanking;


import com.forgerock.openbanking.common.model.openbanking.persistence.account.Balance;
import com.forgerock.openbanking.common.services.store.balance.BalanceStoreService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class FundsAvailabilityService {

    private final BalanceStoreService balanceStoreService;

    public FundsAvailabilityService(BalanceStoreService balanceStoreService) {
        this.balanceStoreService = balanceStoreService;
    }

    public boolean isFundsAvailable(String accountId, String amount) {
        Preconditions.checkArgument(!StringUtils.isEmpty(accountId), "Account Id cannot be empty");
        Preconditions.checkArgument(!StringUtils.isEmpty(amount), "Amount cannot be empty");

        Optional<Balance> balanceIf = balanceStoreService.getBalance(accountId, OBBalanceType1Code.INTERIMAVAILABLE);

        //Verify account for a balance
        Balance balance = balanceIf
                .orElseThrow(() -> new IllegalStateException("No balance found of type '"+OBBalanceType1Code.INTERIMAVAILABLE + "' for account id '" + accountId + "'"));
        BigDecimal currentBalance = balance.getAmount();
        BigDecimal requestAmount = new BigDecimal(amount);

        log.debug("Check if balance: '{}' from accountId: '{}' is sufficient to cover the amount: '{}'", currentBalance.toPlainString(), accountId, amount);
        return (currentBalance.compareTo(requestAmount) >= 0);
    }
}
