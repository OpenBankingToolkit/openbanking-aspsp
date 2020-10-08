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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.data;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accounts.FRAccountRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.balances.FRBalanceRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.beneficiaries.FRBeneficiaryRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.directdebits.FRDirectDebitRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.offers.FROfferRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.party.FRPartyRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.products.FRProductRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.scheduledpayments.FRScheduledPaymentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.standingorders.FRStandingOrderRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.statements.FRStatementRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.transactions.FRTransactionRepository;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRAccountBeneficiary;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRCashBalance;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRDirectDebitData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRFinancialAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FROfferData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRScheduledPaymentData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStandingOrderData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRTransactionData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRBalanceType;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRBeneficiary;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRDirectDebit;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FROffer;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRScheduledPayment;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStatement;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRTransaction;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.data.FRAccountData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Iterables;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DataCreatorTest {

    @Mock
    private FRAccountRepository accountsRepository;
    @Mock
    private FRBalanceRepository balanceRepository;
    @Mock
    private FRBeneficiaryRepository beneficiaryRepository;
    @Mock
    private FRDirectDebitRepository directDebitRepository;
    @Mock
    private FRProductRepository productRepository;
    @Mock
    private FRStandingOrderRepository standingOrderRepository;
    @Mock
    private FRTransactionRepository transactionRepository;
    @Mock
    private FRStatementRepository statementRepository;
    @Mock
    private FRScheduledPaymentRepository scheduledPaymentRepository;
    @Mock
    private FRPartyRepository partyRepository;
    @Mock
    private FROfferRepository offerRepository;
    private DataCreator dataCreator;

    @Before
    public void setUp() {
        dataCreator = new DataCreator(accountsRepository, balanceRepository, beneficiaryRepository,
                directDebitRepository, productRepository, standingOrderRepository, transactionRepository,
                statementRepository, scheduledPaymentRepository, partyRepository, offerRepository, 1000,
                1000);
    }
    @Test
    public void createBalancesShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData accountData = new FRAccountData().addBalance(FRCashBalance.builder().accountId(accountId).type(FRBalanceType.INTERIMAVAILABLE).build());
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(balanceRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataCreator.createBalances(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void createBeneficiariesShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData accountData = new FRAccountData().addBeneficiary(FRAccountBeneficiary.builder().accountId(accountId).build());
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(beneficiaryRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataCreator.createBeneficiaries(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void createDirectDebitsShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData accountData = new FRAccountData().addDirectDebit(FRDirectDebitData.builder().accountId(accountId).build());
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(directDebitRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataCreator.createDirectDebits(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void createStandingOrdersShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData accountData = new FRAccountData().addStandingOrder(FRStandingOrderData.builder().accountId(accountId).build());
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(standingOrderRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataCreator.createStandingOrders(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void createTransactionsShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData accountData = new FRAccountData().addTransaction(FRTransactionData.builder().accountId(accountId).build());
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(transactionRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataCreator.createTransactions(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void createStatementsShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData accountData = new FRAccountData().addStatement(FRStatementData.builder().accountId(accountId).build());
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(statementRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataCreator.createStatements(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void createScheduledPaymentsShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData accountData = new FRAccountData().addScheduledPayment(FRScheduledPaymentData.builder().accountId(accountId).build());
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(scheduledPaymentRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataCreator.createScheduledPayments(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void createOffersShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData accountData = new FRAccountData().addOffer(FROfferData.builder().accountId(accountId).build());
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(offerRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataCreator.createOffers(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void createBalancesShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        FRCashBalance cashBalance = FRCashBalance.builder().accountId(accountId).type(FRBalanceType.INTERIMAVAILABLE).build();
        FRAccountData accountData = new FRAccountData().addBalance(cashBalance);
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(balanceRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createBalances(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRBalance>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(balanceRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createBeneficiariesShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        FRAccountBeneficiary beneficiary = FRAccountBeneficiary.builder().beneficiaryId("2").accountId(accountId).build();
        FRAccountData accountData = new FRAccountData().addBeneficiary(beneficiary);
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(beneficiaryRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createBeneficiaries(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRBeneficiary>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(beneficiaryRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createDirectDebitsShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        FRDirectDebitData directDebit = FRDirectDebitData.builder().accountId(accountId).directDebitId("2").build();
        FRAccountData accountData = new FRAccountData().addDirectDebit(directDebit);
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(directDebitRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createDirectDebits(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRDirectDebit>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(directDebitRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createStandingOrdersShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        FRStandingOrderData standingOrder = FRStandingOrderData.builder().accountId(accountId).standingOrderId("2").build();
        FRAccountData accountData = new FRAccountData().addStandingOrder(standingOrder);
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(standingOrderRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createStandingOrders(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRStandingOrder>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(standingOrderRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createTransactionsShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        FRTransactionData transaction = FRTransactionData.builder().transactionId("2").accountId(accountId).build();
        FRAccountData accountData = new FRAccountData().addTransaction(transaction);
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(transactionRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createTransactions(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRTransaction>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(transactionRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createStatementsShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        FRStatementData statement = FRStatementData.builder().accountId(accountId).statementId("2").build();
        FRAccountData accountData = new FRAccountData().addStatement(statement);
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(statementRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);
        FRStatement existingStatement = FRStatement.builder().statement(statement).accountId(accountId).build();

        // When
        dataCreator.createStatements(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRStatement>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(statementRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createScheduledPaymentsShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        FRScheduledPaymentData scheduledPayment = FRScheduledPaymentData.builder().accountId(accountId).scheduledPaymentId("2").build();
        FRAccountData accountData = new FRAccountData().addScheduledPayment(scheduledPayment);
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(scheduledPaymentRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createScheduledPayments(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRScheduledPayment>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(scheduledPaymentRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createOffersShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        FROfferData offer = FROfferData.builder().accountId(accountId).offerId("2").build();
        FRAccountData accountData = new FRAccountData().addOffer(offer);
        accountData.setAccount(FRFinancialAccount.builder().accountId(accountId).build());
        given(offerRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createOffers(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FROffer>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(offerRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createBalance_noExistingBalances_acceptAndCreate() {
        // Given
        given(balanceRepository.findByAccountIdAndBalanceType(any(), any())).willReturn(Optional.empty());
        FRCashBalance interimAvailBalance = FRCashBalance.builder()
                .accountId("1")
                .type(FRBalanceType.INTERIMAVAILABLE).build();

        // When
        dataCreator.createBalances(accountDataWithBalance(interimAvailBalance), Collections.singleton(interimAvailBalance.getAccountId()));

        // Then
        verify(balanceRepository).saveAll(argThat((b) -> Iterables.firstOf(b).getAccountId().equals("1")));
    }

    @Test
    public void createBalance_existingBalanceOfSameType_reject() {
        // Given
        FRCashBalance interimAvailBalance = FRCashBalance.builder()
                .accountId("1")
                .type(FRBalanceType.INTERIMAVAILABLE).build();
        FRBalance frBalance = FRBalance.builder()
                .balance(interimAvailBalance)
                .accountId(interimAvailBalance.getAccountId())
                .build();
        given(balanceRepository.findByAccountIdAndBalanceType(any(), any())).willReturn(Optional.of(frBalance));

        // When
        assertThatThrownBy(() -> dataCreator.createBalances(accountDataWithBalance(interimAvailBalance), Collections.singleton(interimAvailBalance.getAccountId())))

                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void createBalance_existingBalanceOfDiffType_acceptAndCreate() {
        // Given
        given(balanceRepository.findByAccountIdAndBalanceType(eq("1"), eq(FRBalanceType.OPENINGBOOKED))).willReturn(Optional.empty());
        FRCashBalance openingBookedBalance = FRCashBalance.builder()
                .accountId("1")
                .type(FRBalanceType.OPENINGBOOKED).build();
        // When
        dataCreator.createBalances(accountDataWithBalance(openingBookedBalance), Collections.singleton(openingBookedBalance.getAccountId()));

        // Then
        verify(balanceRepository).saveAll(argThat((b) -> Iterables.firstOf(b).getAccountId().equals("1")));
    }

    private FRAccountData accountDataWithBalance(FRCashBalance balance) {
        FRAccountData accountData = new FRAccountData();
        accountData.setAccount(FRFinancialAccount.builder().accountId(balance.getAccountId()).build());
        accountData.setBalances(Collections.singletonList(balance));
        return accountData;
    }

}