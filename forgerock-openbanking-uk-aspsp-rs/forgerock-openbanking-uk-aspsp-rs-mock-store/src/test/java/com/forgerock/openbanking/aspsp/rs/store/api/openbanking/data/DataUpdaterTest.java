/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.data;

import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.directdebits.FRDirectDebit1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.offers.FROffer1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.products.FRProduct2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.statements.FRStatement1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.accounts.FRAccount3Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.beneficiaries.FRBeneficiary3Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.party.FRParty2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.scheduledpayments.FRScheduledPayment2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.standingorders.FRStandingOrder5Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.transactions.FRTransaction5Repository;
import com.forgerock.openbanking.commons.model.openbanking.v1_1.account.FRBalance1;
import com.forgerock.openbanking.commons.model.openbanking.v1_1.account.FRDirectDebit1;
import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FROffer1;
import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRStatement1;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRBeneficiary3;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRScheduledPayment2;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRStandingOrder5;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRTransaction5;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.data.FRAccountData4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Iterables;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uk.org.openbanking.datamodel.account.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DataUpdaterTest {

    private DataUpdater dataUpdater;
    @Mock
    private FRAccount3Repository accountsRepository;
    @Mock
    private FRBalance1Repository balanceRepository;
    @Mock
    private FRBeneficiary3Repository beneficiaryRepository;
    @Mock
    private FRDirectDebit1Repository directDebitRepository;
    @Mock
    private FRProduct2Repository productRepository;
    @Mock
    private FRStandingOrder5Repository standingOrderRepository;
    @Mock
    private FRTransaction5Repository transactionRepository;
    @Mock
    private FRStatement1Repository statementRepository;
    @Mock
    private FRScheduledPayment2Repository scheduledPaymentRepository;
    @Mock
    private FRParty2Repository partyRepository;
    @Mock
    private FROffer1Repository offerRepository;

    @Before
    public void setUp() {
        dataUpdater = new DataUpdater(accountsRepository, balanceRepository, beneficiaryRepository,
                directDebitRepository, productRepository, standingOrderRepository, transactionRepository,
                statementRepository, scheduledPaymentRepository, partyRepository, offerRepository, 1000);
    }

    @Test
    public void updateBalancesShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData4 accountData = new FRAccountData4().addBalance(new OBCashBalance1().accountId(accountId).type(OBBalanceType1Code.INTERIMAVAILABLE));
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(balanceRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataUpdater.updateBalances(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void updateBeneficiariesShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData4 accountData = new FRAccountData4().addBeneficiary(new OBBeneficiary3().accountId(accountId));
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(beneficiaryRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataUpdater.updateBeneficiaries(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void updateDirectDebitsShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData4 accountData = new FRAccountData4().addDirectDebit(new OBDirectDebit1().accountId(accountId));
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(directDebitRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataUpdater.updateDirectDebits(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void updateStandingOrdersShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData4 accountData = new FRAccountData4().addStandingOrder(new OBStandingOrder5().accountId(accountId));
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(standingOrderRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataUpdater.updateStandingOrders(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void updateTransactionsShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData4 accountData = new FRAccountData4().addTransaction(new OBTransaction5().accountId(accountId));
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(transactionRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataUpdater.updateTransactions(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void updateStatementsShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData4 accountData = new FRAccountData4().addStatement(new OBStatement1().accountId(accountId));
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(statementRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataUpdater.updateStatements(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void updateScheduledPaymentsShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData4 accountData = new FRAccountData4().addScheduledPayment(new OBScheduledPayment2().accountId(accountId));
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(scheduledPaymentRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataUpdater.updateScheduledPayments(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void updateOffersShouldThrowExceptionForExceedingLimit() {
        // Given
        String accountId = "1";
        FRAccountData4 accountData = new FRAccountData4().addOffer(new OBOffer1().accountId(accountId));
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(offerRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);

        assertThatThrownBy(
                // When
                () -> dataUpdater.updateOffers(accountData, Collections.singleton("1"))
        )
                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @Test
    public void updateBalancesShouldAllowUpdatesWhenOnLimit() {
        // Given
        String accountId = "1";
        OBCashBalance1 cashBalance = new OBCashBalance1().accountId(accountId).type(OBBalanceType1Code.INTERIMAVAILABLE);
        FRAccountData4 accountData = new FRAccountData4().addBalance(cashBalance);
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(balanceRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);
        FRBalance1 existingBalance = FRBalance1.builder().balance(cashBalance).build();
        given(balanceRepository.findByAccountIdAndBalanceType(accountId, OBBalanceType1Code.INTERIMAVAILABLE)).willReturn(Optional.of(existingBalance));

        // When
        dataUpdater.updateBalances(accountData, Collections.singleton("1"));

        // Then
        verify(balanceRepository).saveAll(Collections.singletonList(existingBalance));
    }

    @Test
    public void updateBeneficiariesShouldAllowUpdatesWhenOnLimit() {
        // Given
        String accountId = "1";
        OBBeneficiary3 beneficiary = new OBBeneficiary3().beneficiaryId("2").accountId(accountId);
        FRAccountData4 accountData = new FRAccountData4().addBeneficiary(beneficiary);
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(beneficiaryRepository.countByAccountIdIn(Collections.singleton("1"))).willReturn(1000L);
        FRBeneficiary3 existingBeneficiary = FRBeneficiary3.builder().beneficiary(beneficiary).accountId(accountId).build();
        given(beneficiaryRepository.findById(beneficiary.getBeneficiaryId())).willReturn(Optional.of(existingBeneficiary));

        // When
        dataUpdater.updateBeneficiaries(accountData, Collections.singleton("1"));

        // Then
        verify(beneficiaryRepository).saveAll(Collections.singletonList(existingBeneficiary));
    }

    @Test
    public void updateDirectDebitsShouldAllowUpdatesWhenOnLimit() {
        // Given
        String accountId = "1";
        OBDirectDebit1 directDebit = new OBDirectDebit1().accountId(accountId).directDebitId("2");
        FRAccountData4 accountData = new FRAccountData4().addDirectDebit(directDebit);
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(directDebitRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);
        FRDirectDebit1 existingDirectDebit = FRDirectDebit1.builder().directDebit(directDebit).accountId(accountId).build();
        given(directDebitRepository.findById(directDebit.getDirectDebitId())).willReturn(Optional.of(existingDirectDebit));

        // When
        dataUpdater.updateDirectDebits(accountData, Collections.singleton("1"));

        // Then
        verify(directDebitRepository).saveAll(Collections.singletonList(existingDirectDebit));
    }

    @Test
    public void updateStandingOrdersShouldAllowUpdatesWhenOnLimit() {
        // Given
        String accountId = "1";
        OBStandingOrder5 standingOrder = new OBStandingOrder5().accountId(accountId).standingOrderId("2");
        FRAccountData4 accountData = new FRAccountData4().addStandingOrder(standingOrder);
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(standingOrderRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);
        FRStandingOrder5 existingStandingOrder = FRStandingOrder5.builder().standingOrder(standingOrder).accountId(accountId).build();
        given(standingOrderRepository.findById(standingOrder.getStandingOrderId())).willReturn(Optional.of(existingStandingOrder));

        // When
        dataUpdater.updateStandingOrders(accountData, Collections.singleton("1"));

        // Then
        verify(standingOrderRepository).saveAll(Collections.singletonList(existingStandingOrder));
    }

    @Test
    public void updateTransactionsShouldAllowUpdatesWhenOnLimit() {
        // Given
        String accountId = "1";
        OBTransaction5 transaction = new OBTransaction5().transactionId("2").accountId(accountId);
        FRAccountData4 accountData = new FRAccountData4().addTransaction(transaction);
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(transactionRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);
        FRTransaction5 existingTransaction = FRTransaction5.builder().transaction(transaction).accountId(accountId).build();
        given(transactionRepository.findById(transaction.getTransactionId())).willReturn(Optional.of(existingTransaction));

        // When
        dataUpdater.updateTransactions(accountData, Collections.singleton("1"));

        // Then
        verify(transactionRepository).saveAll(Collections.singletonList(existingTransaction));
    }

    @Test
    public void updateStatementsShouldAllowUpdatesWhenOnLimit() {
        // Given
        String accountId = "1";
        OBStatement1 statement = new OBStatement1().accountId(accountId).statementId("2");
        FRAccountData4 accountData = new FRAccountData4().addStatement(statement);
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(statementRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);
        FRStatement1 existingStatement = FRStatement1.builder().statement(statement).accountId(accountId).build();
        given(statementRepository.findById(statement.getStatementId())).willReturn(Optional.of(existingStatement));

        // When
        dataUpdater.updateStatements(accountData, Collections.singleton("1"));

        // Then
        verify(statementRepository).saveAll(Collections.singletonList(existingStatement));
    }

    @Test
    public void updateScheduledPaymentsShouldAllowUpdatesWhenOnLimit() {
        // Given
        String accountId = "1";
        OBScheduledPayment2 scheduledPayment = new OBScheduledPayment2().accountId(accountId).scheduledPaymentId("2");
        FRAccountData4 accountData = new FRAccountData4().addScheduledPayment(scheduledPayment);
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(scheduledPaymentRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);
        FRScheduledPayment2 existingScheduledPayment = FRScheduledPayment2.builder().scheduledPayment(scheduledPayment).accountId(accountId).build();
        given(scheduledPaymentRepository.findById(scheduledPayment.getScheduledPaymentId())).willReturn(Optional.of(existingScheduledPayment));

        // When
        dataUpdater.updateScheduledPayments(accountData, Collections.singleton("1"));

        // Then
        verify(scheduledPaymentRepository).saveAll(Collections.singletonList(existingScheduledPayment));
    }

    @Test
    public void updateOffersShouldAllowUpdatesWhenOnLimit() {
        // Given
        String accountId = "1";
        OBOffer1 offer = new OBOffer1().accountId(accountId).offerId("2");
        FRAccountData4 accountData = new FRAccountData4().addOffer(offer);
        accountData.setAccount(new OBAccount3().accountId(accountId));
        given(offerRepository.countByAccountIdIn(Collections.singleton(accountId))).willReturn(1000L);
        FROffer1 existingOffer = FROffer1.builder().offer(offer).accountId(accountId).build();
        given(offerRepository.findById(offer.getOfferId())).willReturn(Optional.of(existingOffer));

        // When
        dataUpdater.updateOffers(accountData, Collections.singleton("1"));

        // Then
        verify(offerRepository).saveAll(Collections.singletonList(existingOffer));
    }

    @Test
    public void updateBalance_noExistingBalances_acceptAndCreate() {
        // Given
        given(balanceRepository.findByAccountIdAndBalanceType(any(), any())).willReturn(Optional.empty());
        OBCashBalance1 interimAvailBalance = new OBCashBalance1()
                .accountId("1")
                .type(OBBalanceType1Code.INTERIMAVAILABLE);

        // When
        dataUpdater.updateBalances(accountDataWithBalance(interimAvailBalance), Collections.emptySet());

        // Then
        verify(balanceRepository).saveAll(argThat((b) -> Iterables.firstOf(b).getAccountId().equals("1")));
    }

    @Test
    public void updateBalance_balanceOfSameType_reject() {
        // Given
        OBCashBalance1 interimAvailBalance = new OBCashBalance1()
                .accountId("1")
                .type(OBBalanceType1Code.INTERIMAVAILABLE);
        FRBalance1 frBalance = FRBalance1.builder()
                .balance(interimAvailBalance)
                .accountId(interimAvailBalance.getAccountId())
                .build();
        given(balanceRepository.findByAccountIdAndBalanceType(any(), any())).willReturn(Optional.of(frBalance));
        FRAccountData4 accountDataDiff = accountDataWithBalance(interimAvailBalance);
        accountDataDiff.setBalances(Arrays.asList(interimAvailBalance, interimAvailBalance));

        // When
        assertThatThrownBy(() -> {
            dataUpdater.updateBalances(accountDataDiff, Collections.emptySet());
        })

                // Then
                .satisfies(t -> assertThat(((ResponseStatusException)t).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void updateBalance_existingBalanceOfDiffType_acceptAndCreate() {
        // Given
        given(balanceRepository.findByAccountIdAndBalanceType(eq("1"), eq(OBBalanceType1Code.OPENINGBOOKED))).willReturn(Optional.empty());
        OBCashBalance1 openingBookedBalance = new OBCashBalance1()
                .accountId("1")
                .type(OBBalanceType1Code.OPENINGBOOKED);
        // When
        dataUpdater.updateBalances(accountDataWithBalance(openingBookedBalance), Collections.emptySet());

        // Then
        verify(balanceRepository).saveAll(argThat((b) -> Iterables.firstOf(b).getAccountId().equals("1")));
    }

    private FRAccountData4 accountDataWithBalance(OBCashBalance1 balance) {
        FRAccountData4 accountData = new FRAccountData4();
        accountData.setAccount(new OBAccount3().accountId(balance.getAccountId()));
        accountData.setBalances(Collections.singletonList(balance));
        return accountData;
    }
}