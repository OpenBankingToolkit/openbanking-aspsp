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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.account.OBCashBalance1;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class AccountsApiControllerTest {
    @Mock
    private FRAccount4Repository accountsRepository;
    @Mock
    private FRBalance1Repository balanceRepository;
    @InjectMocks
    AccountsApiController accountsApiController;

    @Test
    public void getAccounts_noBalances() {
        // Given
        given(accountsRepository.findByUserID(any())).willReturn(Collections.singletonList(
                FRAccount4.builder().id("111").build()
        ));
        given(balanceRepository.findByAccountIdIn(any())).willReturn(Collections.emptyList());

        // When
        ResponseEntity<List<AccountWithBalance>> result = accountsApiController.getAccounts("user1", true);

        // Then
        List<AccountWithBalance> results = Objects.requireNonNull(result.getBody());
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getBalances()).isEmpty();
    }

    @Test
    public void getAccounts_noAccount() {
        // Given
        given(accountsRepository.findByUserID(any())).willReturn(Collections.emptyList());

        // When
        ResponseEntity<List<AccountWithBalance>> result = accountsApiController.getAccounts("user1", true);

        // Then
        assertThat(Objects.requireNonNull(result.getBody())).isEmpty();
    }

    @Test
    public void getAccounts_balanceNotRequired() {
        // Given
        given(accountsRepository.findByUserID(any())).willReturn(Collections.singletonList(
                FRAccount4.builder().id("111").build()
        ));

        // When
        ResponseEntity<List<AccountWithBalance>> result = accountsApiController.getAccounts("user1", false);

        // Then
        List<AccountWithBalance> results = Objects.requireNonNull(result.getBody());
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getBalances().size()).isEqualTo(0);
        verifyZeroInteractions(balanceRepository);
    }

    @Test
    public void getAccounts_withBalance() {
        // Given
        given(accountsRepository.findByUserID(any())).willReturn(Collections.singletonList(
                FRAccount4.builder().id("111").build()
        ));
        given(balanceRepository.findByAccountIdIn(any())).willReturn(Collections.singletonList(
                FRBalance1.builder().accountId("111").balance(new OBCashBalance1().accountId("111")).build()
        ));

        // When
        ResponseEntity<List<AccountWithBalance>> result = accountsApiController.getAccounts("user1", true);

        // Then
        List<AccountWithBalance> results = Objects.requireNonNull(result.getBody());
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getBalances().size()).isEqualTo(1);
    }
}