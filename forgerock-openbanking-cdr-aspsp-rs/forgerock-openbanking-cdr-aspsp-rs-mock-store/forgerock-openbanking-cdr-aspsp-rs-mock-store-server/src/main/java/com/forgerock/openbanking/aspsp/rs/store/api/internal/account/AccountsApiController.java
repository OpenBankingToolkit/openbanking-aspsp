/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.account;


import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.accounts.FRAccount3Repository;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBalance1;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRAccount3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBCashBalance1;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class AccountsApiController implements AccountsApi {
    private final FRAccount3Repository accountsRepository;
    private final FRBalance1Repository balanceRepository;

    @Autowired
    public AccountsApiController(FRAccount3Repository accountsRepository, FRBalance1Repository balanceRepository) {
        this.accountsRepository = accountsRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public ResponseEntity<List<FRAccountWithBalance>> getAccounts(
            @RequestParam("userId") String userId,
            @RequestParam(value = "withBalance", required = false, defaultValue = "false") Boolean withBalance
    ) {
        log.info("Read all accounts for user ID '{}', with Balances: {}", userId, withBalance);
        Collection<FRAccount3> accountsByUserID = accountsRepository.findByUserID(Objects.requireNonNull(userId));
        if (!withBalance || accountsByUserID.isEmpty()) {
            log.debug("No balances required so returning {} accounts for userId: {}", accountsByUserID.size(), userId);
            return ResponseEntity.ok(
                    accountsByUserID.stream()
                            .map(a -> toFRAccountWithBalance(a, Collections.emptyMap()))
                            .collect(Collectors.toList()));
        }

        final Map<String, List<FRBalance1>> balancesByAccountId  =
                balanceRepository.findByAccountIdIn(accountsByUserID.stream()
                        .map(FRAccount3::getId)
                        .collect(Collectors.toList())
                ).stream().collect(
                        Collectors.groupingBy(
                                FRBalance1::getAccountId,
                                HashMap::new,
                                Collectors.toCollection(ArrayList::new))
                        );
        log.debug("Got balances by accountId: {}", balancesByAccountId);

        return ResponseEntity.ok(
                accountsByUserID.stream()
                        .map(a -> toFRAccountWithBalance(a, balancesByAccountId))
                        .collect(Collectors.toList())
        );

    }

    @Override
    public ResponseEntity<FRAccount3> findByAccountId(
            @PathVariable("accountId") String accountId,
            @RequestParam("permissions") List<String> permissions
    ) {
        log.debug("Read account {} with permissions: {}", accountId, permissions);
        return new ResponseEntity<>(accountsRepository.byAccountId(accountId,
                permissions.stream()
                        .map(OBExternalPermissions1Code::fromValue)
                        .collect(Collectors.toList())),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Optional<FRAccount3>> findByIdentification(
            @RequestParam("identification") String identification
    ) {
        log.debug("Find accounts by identification {}", identification);
        return new ResponseEntity<>(accountsRepository.findByAccountAccountIdentification(identification), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FRAccount3> getAccount(
            @PathVariable("accountId") String accountId
    ) {
        log.debug("Read accounts with id {}", accountId);
        return new ResponseEntity(accountsRepository.findById(accountId), HttpStatus.OK);
    }

    private FRAccountWithBalance toFRAccountWithBalance(FRAccount3 account, Map<String, List<FRBalance1>> balanceMap) {
        final List<OBCashBalance1> balances = Optional.ofNullable(balanceMap.get(account.getId()))
                .orElse(Collections.emptyList())
                .stream()
                .map(FRBalance1::getBalance)
                .collect(Collectors.toList());

        return new FRAccountWithBalance(account, balances);
    }
}