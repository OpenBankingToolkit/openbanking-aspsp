/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.balance;

import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBalance1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;

import java.util.Optional;

@Controller
public class BalanceApiController implements BalanceApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceApiController.class);

    @Autowired
    private FRBalance1Repository balanceRepository;

    @Override
    public ResponseEntity<Optional<FRBalance1>> findByAcountId(
            @RequestParam("accountId") String accountId,
            @RequestParam("type") OBBalanceType1Code type
    ) {
        LOGGER.debug("Read balances for account {} with page {}", accountId);
        return ResponseEntity.ok(balanceRepository.findByAccountIdAndBalanceType(accountId, type));
    }

    @Override
    public ResponseEntity save(
            @RequestBody FRBalance1 balance1
    ) {
        LOGGER.debug("Save balance1 {}", balance1);
        return ResponseEntity.ok(balanceRepository.save(balance1));
    }
}
