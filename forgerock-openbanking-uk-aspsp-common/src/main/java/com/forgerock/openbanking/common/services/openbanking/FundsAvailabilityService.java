/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking;


import com.forgerock.openbanking.common.model.openbanking.forgerock.FRBalance;
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

        Optional<FRBalance> balanceIf = balanceStoreService.getBalance(accountId, OBBalanceType1Code.INTERIMAVAILABLE);

        //Verify account for a balance
        FRBalance balance = balanceIf
                .orElseThrow(() -> new IllegalStateException("No balance found of type '"+OBBalanceType1Code.INTERIMAVAILABLE + "' for account id '" + accountId + "'"));
        BigDecimal currentBalance = balance.getAmount();
        BigDecimal requestAmount = new BigDecimal(amount);

        log.debug("Check if balance: '{}' from accountId: '{}' is sufficient to cover the amount: '{}'", currentBalance.toPlainString(), accountId, amount);
        return (currentBalance.compareTo(requestAmount) >= 0);
    }
}
