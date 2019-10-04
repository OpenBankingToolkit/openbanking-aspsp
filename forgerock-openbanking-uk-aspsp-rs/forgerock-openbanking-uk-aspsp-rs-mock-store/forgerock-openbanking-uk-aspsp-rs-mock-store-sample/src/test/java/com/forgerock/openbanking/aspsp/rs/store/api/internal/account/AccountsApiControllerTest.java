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

@RunWith(MockitoJUnitRunner.class)
public class AccountsApiControllerTest {
    @Mock
    private FRAccount3Repository accountsRepository;
    @Mock
    private FRBalance1Repository balanceRepository;
    @InjectMocks
    AccountsApiController accountsApiController;

    @Test
    public void getAccounts_noBalances() {
        // Given
        given(accountsRepository.findByUserID(any())).willReturn(Collections.singletonList(
                FRAccount3.builder().id("111").build()
        ));
        given(balanceRepository.findByAccountIdIn(any())).willReturn(Collections.emptyList());

        // When
        ResponseEntity<List<FRAccountWithBalance>> result = accountsApiController.getAccounts("user1", true);

        // Then
        List<FRAccountWithBalance> results = Objects.requireNonNull(result.getBody());
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getBalances()).isEmpty();
    }

    @Test
    public void getAccounts_noAccount() {
        // Given
        given(accountsRepository.findByUserID(any())).willReturn(Collections.emptyList());

        // When
        ResponseEntity<List<FRAccountWithBalance>> result = accountsApiController.getAccounts("user1", true);

        // Then
        assertThat(Objects.requireNonNull(result.getBody())).isEmpty();
    }

    @Test
    public void getAccounts_balanceNotRequired() {
        // Given
        given(accountsRepository.findByUserID(any())).willReturn(Collections.singletonList(
                FRAccount3.builder().id("111").build()
        ));

        // When
        ResponseEntity<List<FRAccountWithBalance>> result = accountsApiController.getAccounts("user1", false);

        // Then
        List<FRAccountWithBalance> results = Objects.requireNonNull(result.getBody());
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getBalances().size()).isEqualTo(0);
        verifyZeroInteractions(balanceRepository);
    }

    @Test
    public void getAccounts_withBalance() {
        // Given
        given(accountsRepository.findByUserID(any())).willReturn(Collections.singletonList(
                FRAccount3.builder().id("111").build()
        ));
        given(balanceRepository.findByAccountIdIn(any())).willReturn(Collections.singletonList(
                FRBalance1.builder().accountId("111").balance(new OBCashBalance1().accountId("111")).build()
        ));

        // When
        ResponseEntity<List<FRAccountWithBalance>> result = accountsApiController.getAccounts("user1", true);

        // Then
        List<FRAccountWithBalance> results = Objects.requireNonNull(result.getBody());
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getBalances().size()).isEqualTo(1);
    }
}