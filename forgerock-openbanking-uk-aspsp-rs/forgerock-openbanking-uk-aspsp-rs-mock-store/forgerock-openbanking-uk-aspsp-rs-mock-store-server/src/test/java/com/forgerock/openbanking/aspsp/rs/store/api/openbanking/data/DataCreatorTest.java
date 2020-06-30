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

import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.offers.FROffer1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.products.FRProduct2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.party.FRParty2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.standingorders.FRStandingOrder5Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.transactions.FRTransaction5Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.accounts.FRAccount4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.beneficiaries.FRBeneficiary4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.directdebits.FRDirectDebit4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.scheduledpayments.FRScheduledPayment4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.statements.FRStatement4Repository;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBalance1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FROffer1;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRStandingOrder5;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRTransaction5;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRBeneficiary4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRDirectDebit4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRScheduledPayment4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRStatement4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.data.FRAccountData4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Iterables;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uk.org.openbanking.datamodel.account.*;

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
    private FRAccount4Repository accountsRepository;
    @Mock
    private FRBalance1Repository balanceRepository;
    @Mock
    private FRBeneficiary4Repository beneficiaryRepository;
    @Mock
    private FRDirectDebit4Repository directDebitRepository;
    @Mock
    private FRProduct2Repository productRepository;
    @Mock
    private FRStandingOrder5Repository standingOrderRepository;
    @Mock
    private FRTransaction5Repository transactionRepository;
    @Mock
    private FRStatement4Repository statementRepository;
    @Mock
    private FRScheduledPayment4Repository scheduledPaymentRepository;
    @Mock
    private FRParty2Repository partyRepository;
    @Mock
    private FROffer1Repository offerRepository;
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
        FRAccountData4 accountData = new FRAccountData4().addBalance(new OBCashBalance1().accountId(accountId).type(OBBalanceType1Code.INTERIMAVAILABLE));
        accountData.setAccount(new OBAccount6().accountId(accountId));
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
        FRAccountData4 accountData = new FRAccountData4().addBeneficiary(new OBBeneficiary4().accountId(accountId));
        accountData.setAccount(new OBAccount6().accountId(accountId));
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
        FRAccountData4 accountData = new FRAccountData4().addDirectDebit(new OBReadDirectDebit2DataDirectDebit().accountId(accountId));
        accountData.setAccount(new OBAccount6().accountId(accountId));
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
        FRAccountData4 accountData = new FRAccountData4().addStandingOrder(new OBStandingOrder5().accountId(accountId));
        accountData.setAccount(new OBAccount6().accountId(accountId));
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
        FRAccountData4 accountData = new FRAccountData4().addTransaction(new OBTransaction5().accountId(accountId));
        accountData.setAccount(new OBAccount6().accountId(accountId));
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
        FRAccountData4 accountData = new FRAccountData4().addStatement(new OBStatement2().accountId(accountId));
        accountData.setAccount(new OBAccount6().accountId(accountId));
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
        FRAccountData4 accountData = new FRAccountData4().addScheduledPayment(new OBScheduledPayment3().accountId(accountId));
        accountData.setAccount(new OBAccount6().accountId(accountId));
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
        FRAccountData4 accountData = new FRAccountData4().addOffer(new OBOffer1().accountId(accountId));
        accountData.setAccount(new OBAccount6().accountId(accountId));
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
        OBCashBalance1 cashBalance = new OBCashBalance1().accountId(accountId).type(OBBalanceType1Code.INTERIMAVAILABLE);
        FRAccountData4 accountData = new FRAccountData4().addBalance(cashBalance);
        accountData.setAccount(new OBAccount6().accountId(accountId));
        given(balanceRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createBalances(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRBalance1>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(balanceRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createBeneficiariesShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        OBBeneficiary4 beneficiary = new OBBeneficiary4().beneficiaryId("2").accountId(accountId);
        FRAccountData4 accountData = new FRAccountData4().addBeneficiary(beneficiary);
        accountData.setAccount(new OBAccount6().accountId(accountId));
        given(beneficiaryRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createBeneficiaries(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRBeneficiary4>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(beneficiaryRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createDirectDebitsShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        OBReadDirectDebit2DataDirectDebit directDebit = new OBReadDirectDebit2DataDirectDebit().accountId(accountId).directDebitId("2");
        FRAccountData4 accountData = new FRAccountData4().addDirectDebit(directDebit);
        accountData.setAccount(new OBAccount6().accountId(accountId));
        given(directDebitRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createDirectDebits(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRDirectDebit4>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(directDebitRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createStandingOrdersShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        OBStandingOrder5 standingOrder = new OBStandingOrder5().accountId(accountId).standingOrderId("2");
        FRAccountData4 accountData = new FRAccountData4().addStandingOrder(standingOrder);
        accountData.setAccount(new OBAccount6().accountId(accountId));
        given(standingOrderRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createStandingOrders(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRStandingOrder5>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(standingOrderRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createTransactionsShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        OBTransaction5 transaction = new OBTransaction5().transactionId("2").accountId(accountId);
        FRAccountData4 accountData = new FRAccountData4().addTransaction(transaction);
        accountData.setAccount(new OBAccount6().accountId(accountId));
        given(transactionRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createTransactions(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRTransaction5>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(transactionRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createStatementsShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        OBStatement2 statement = new OBStatement2().accountId(accountId).statementId("2");
        FRAccountData4 accountData = new FRAccountData4().addStatement(statement);
        accountData.setAccount(new OBAccount6().accountId(accountId));
        given(statementRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);
        FRStatement4 existingStatement = FRStatement4.builder().statement(statement).accountId(accountId).build();

        // When
        dataCreator.createStatements(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRStatement4>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(statementRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createScheduledPaymentsShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        OBScheduledPayment3 scheduledPayment = new OBScheduledPayment3().accountId(accountId).scheduledPaymentId("2");
        FRAccountData4 accountData = new FRAccountData4().addScheduledPayment(scheduledPayment);
        accountData.setAccount(new OBAccount6().accountId(accountId));
        given(scheduledPaymentRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createScheduledPayments(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FRScheduledPayment4>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(scheduledPaymentRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createOffersShouldAllowCreateWhenOnLimit() {
        // Given
        String accountId = "1";
        OBOffer1 offer = new OBOffer1().accountId(accountId).offerId("2");
        FRAccountData4 accountData = new FRAccountData4().addOffer(offer);
        accountData.setAccount(new OBAccount6().accountId(accountId));
        given(offerRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(999L);

        // When
        dataCreator.createOffers(accountData, Collections.singleton("1"));

        // Then
        ArgumentCaptor<List<FROffer1>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(offerRepository).saveAll(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().get(0).getAccountId()).isEqualTo(accountId);
    }

    @Test
    public void createBalance_noExistingBalances_acceptAndCreate() {
        // Given
        given(balanceRepository.findByAccountIdAndBalanceType(any(), any())).willReturn(Optional.empty());
        OBCashBalance1 interimAvailBalance = new OBCashBalance1()
                .accountId("1")
                .type(OBBalanceType1Code.INTERIMAVAILABLE);

        // When
        dataCreator.createBalances(accountDataWithBalance(interimAvailBalance), Collections.singleton(interimAvailBalance.getAccountId()));

        // Then
        verify(balanceRepository).saveAll(argThat((b) -> Iterables.firstOf(b).getAccountId().equals("1")));
    }

    @Test
    public void createBalance_existingBalanceOfSameType_reject() {
        // Given
        OBCashBalance1 interimAvailBalance = new OBCashBalance1()
                .accountId("1")
                .type(OBBalanceType1Code.INTERIMAVAILABLE);
        FRBalance1 frBalance = FRBalance1.builder()
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
        given(balanceRepository.findByAccountIdAndBalanceType(eq("1"), eq(OBBalanceType1Code.OPENINGBOOKED))).willReturn(Optional.empty());
        OBCashBalance1 openingBookedBalance = new OBCashBalance1()
                .accountId("1")
                .type(OBBalanceType1Code.OPENINGBOOKED);
        // When
        dataCreator.createBalances(accountDataWithBalance(openingBookedBalance), Collections.singleton(openingBookedBalance.getAccountId()));

        // Then
        verify(balanceRepository).saveAll(argThat((b) -> Iterables.firstOf(b).getAccountId().equals("1")));
    }

    private FRAccountData4 accountDataWithBalance(OBCashBalance1 balance) {
        FRAccountData4 accountData = new FRAccountData4();
        accountData.setAccount(new OBAccount6().accountId(balance.getAccountId()));
        accountData.setBalances(Collections.singletonList(balance));
        return accountData;
    }

}