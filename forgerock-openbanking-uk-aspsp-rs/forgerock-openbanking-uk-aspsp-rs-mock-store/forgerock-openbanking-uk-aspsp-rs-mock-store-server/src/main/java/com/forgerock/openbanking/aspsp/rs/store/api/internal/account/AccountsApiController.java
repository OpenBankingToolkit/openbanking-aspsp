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
package com.forgerock.openbanking.aspsp.rs.store.api.internal.account;

import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.accounts.FRAccount4Repository;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v1_1.FRBalance1;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_3.FRAccount4;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBCashBalance1;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class AccountsApiController implements AccountsApi {
    private final FRAccount4Repository accountsRepository;
    private final FRBalance1Repository balanceRepository;

    @Autowired
    public AccountsApiController(FRAccount4Repository accountsRepository, FRBalance1Repository balanceRepository) {
        this.accountsRepository = accountsRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public ResponseEntity<List<AccountWithBalance>> getAccounts(
            @RequestParam("userId") String userId,
            @RequestParam(value = "withBalance", required = false, defaultValue = "false") Boolean withBalance
    ) {
        log.info("Read all accounts for user ID '{}', with Balances: {}", userId, withBalance);
        Collection<FRAccount4> accountsByUserID = accountsRepository.findByUserID(Objects.requireNonNull(userId));
        if (!withBalance || accountsByUserID.isEmpty()) {
            log.debug("No balances required so returning {} accounts for userId: {}", accountsByUserID.size(), userId);
            return ResponseEntity.ok(
                    accountsByUserID.stream()
                            .map(a -> toFRAccountWithBalance(a, Collections.emptyMap()))
                            .collect(Collectors.toList()));
        }

        final Map<String, List<FRBalance1>> balancesByAccountId  =
                balanceRepository.findByAccountIdIn(accountsByUserID.stream()
                        .map(FRAccount4::getId)
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
    public ResponseEntity<FRAccount4> findByAccountId(
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
    public ResponseEntity<Optional<FRAccount4>> findByIdentification(
            @RequestParam("identification") String identification
    ) {
        log.debug("Find accounts by identification {}", identification);
        return new ResponseEntity<>(accountsRepository.findByAccountAccountIdentification(identification), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FRAccount4> getAccount(
            @PathVariable("accountId") String accountId
    ) {
        log.debug("Read accounts with id {}", accountId);
        return new ResponseEntity(accountsRepository.findById(accountId), HttpStatus.OK);
    }

    private AccountWithBalance toFRAccountWithBalance(FRAccount4 account, Map<String, List<FRBalance1>> balanceMap) {
        final List<OBCashBalance1> balances = Optional.ofNullable(balanceMap.get(account.getId()))
                .orElse(Collections.emptyList())
                .stream()
                .map(FRBalance1::getBalance)
                .collect(Collectors.toList());

        return new AccountWithBalance(account, balances);
    }
}
