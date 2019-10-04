/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.simulator.scheduler;

import com.forgerock.openbanking.aspsp.rs.simulator.service.MoneyService;
import com.forgerock.openbanking.aspsp.rs.simulator.service.PaymentNotificationFacade;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRScheduledPayment1;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.account.scheduledpayment.ScheduledPaymentService;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.account.OBScheduledPayment1;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AcceptDomesticScheduledPaymentTaskTest {

    private static final String DEBIT_ACCOUNT = "payer_123";
    private static final String CREDIT_ACCOUNT = "payee_987";

    @Mock
    private AccountStoreService account2StoreService;
    @Mock
    private ScheduledPaymentService paymentsService;
    @Mock
    private MoneyService moneyService;
    @Mock
    private PaymentNotificationFacade paymentNotificationService;

    @InjectMocks
    private AcceptDueScheduledPaymentTask acceptDueScheduledPaymentTask;

    @Test
    public void scheduledPaymentDue_shouldDebitAccount() throws CurrencyConverterException {
        // Given
        FRScheduledPayment1 payment = defaultPayment(DateTime.now().minusDays(1), ScheduledPaymentStatus.PENDING);
        given(paymentsService.getPendingAndDueScheduledPayments()).willReturn(Collections.singletonList(payment));
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);

        // When
        acceptDueScheduledPaymentTask.payDueScheduledPayments();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(payment.getScheduledPayment().getInstructedAmount()), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updateSchedulePayment(argThat(p -> p.getStatus().equals(ScheduledPaymentStatus.COMPLETED)));
    }

    @Test
    public void scheduledPaymentDue_shouldCreditAccount() throws CurrencyConverterException {
        // Given
        FRScheduledPayment1 payment = defaultPayment(DateTime.now().minusDays(1), ScheduledPaymentStatus.PENDING);
        given(paymentsService.getPendingAndDueScheduledPayments()).willReturn(Collections.singletonList(payment));
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(defaultAccount(DEBIT_ACCOUNT));

        FRAccount2 account = defaultAccount(CREDIT_ACCOUNT);
        given(account2StoreService.findAccountByIdentification(CREDIT_ACCOUNT))
                .willReturn(Optional.of(account));

        // When
        acceptDueScheduledPaymentTask.payDueScheduledPayments();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(payment.getScheduledPayment().getInstructedAmount()), eq(OBCreditDebitCode.CREDIT), eq(payment), any());
        verify(paymentsService).updateSchedulePayment(argThat(p -> p.getStatus().equals(ScheduledPaymentStatus.COMPLETED)));
    }

    @Test
    public void noDuePaymentsFound() {
        // Given
        given(paymentsService.getPendingAndDueScheduledPayments()).willReturn(Collections.emptyList());

        // When
        acceptDueScheduledPaymentTask.payDueScheduledPayments();

        // Then
        verify(paymentsService, times(0)).updateSchedulePayment(any());
    }

    @Test
    public void shouldRejectPaymentWhenCurrencyConversionException() throws CurrencyConverterException {
        // Given
        FRScheduledPayment1 payment = defaultPayment(DateTime.now().minusDays(1), ScheduledPaymentStatus.PENDING);
        given(paymentsService.getPendingAndDueScheduledPayments()).willReturn(Collections.singletonList(payment));
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(new CurrencyConverterException("Simulated failure")).when(moneyService).moveMoney(any(), any(), any(), any(), any());

        // When
        acceptDueScheduledPaymentTask.payDueScheduledPayments();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(payment.getScheduledPayment().getInstructedAmount()), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updateSchedulePayment(argThat(p -> p.getStatus().equals(ScheduledPaymentStatus.REJECTED)));
        assertThat(payment.getRejectionReason()).isEqualTo("Can't convert amount in the right currency: Simulated failure");
    }

    @Test
    public void shouldRejectPaymentWhenAnyException() throws CurrencyConverterException {
        // Given
        FRScheduledPayment1 payment = defaultPayment(DateTime.now().minusDays(1), ScheduledPaymentStatus.PENDING);
        given(paymentsService.getPendingAndDueScheduledPayments()).willReturn(Collections.singletonList(payment));
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(new RuntimeException("Simulated failure")).when(moneyService).moveMoney(any(), any(), any(), any(), any());

        // When
        acceptDueScheduledPaymentTask.payDueScheduledPayments();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(payment.getScheduledPayment().getInstructedAmount()), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updateSchedulePayment(argThat(p -> p.getStatus().equals(ScheduledPaymentStatus.REJECTED)));
        assertThat(payment.getRejectionReason()).isEqualTo("Failed to execute payment: Simulated failure");
    }

    @Test
    public void scheduledPayment_ignoreIfNotDue() throws CurrencyConverterException {
        // Given
        FRScheduledPayment1 payment = defaultPayment(DateTime.now().plusDays(1), ScheduledPaymentStatus.PENDING);
        given(paymentsService.getPendingAndDueScheduledPayments()).willReturn(Collections.singletonList(payment));

        // When
        acceptDueScheduledPaymentTask.payDueScheduledPayments();

        // Then
        verifyZeroInteractions(moneyService);
        assertThat(payment.getStatus()).isEqualTo(ScheduledPaymentStatus.PENDING);
    }

    private FRAccount2 defaultAccount(String payAccount) {
        return FRAccount2.builder().id(payAccount).build();
    }

    private FRScheduledPayment1 defaultPayment(DateTime scheduledExecution, ScheduledPaymentStatus status) {
        OBScheduledPayment1 scheduledPayment = new OBScheduledPayment1()
                .scheduledPaymentDateTime(scheduledExecution)
                .creditorAccount(new OBCashAccount3().identification(CREDIT_ACCOUNT))
                .instructedAmount(new OBActiveOrHistoricCurrencyAndAmount()
                        .amount("3")
                        .currency("GBP"));
        return FRScheduledPayment1.builder()
                .accountId(DEBIT_ACCOUNT)
                .status(status)
                .id("111")
                .scheduledPayment(scheduledPayment)
                .build();
    }

}