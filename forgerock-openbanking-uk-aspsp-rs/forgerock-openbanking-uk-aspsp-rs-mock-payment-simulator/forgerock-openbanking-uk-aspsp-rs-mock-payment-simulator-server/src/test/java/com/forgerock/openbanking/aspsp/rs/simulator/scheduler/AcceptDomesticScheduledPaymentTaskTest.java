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
package com.forgerock.openbanking.aspsp.rs.simulator.scheduler;

import com.forgerock.openbanking.aspsp.rs.simulator.service.MoneyService;
import com.forgerock.openbanking.aspsp.rs.simulator.service.PaymentNotificationFacade;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRScheduledPayment;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.account.scheduledpayment.ScheduledPaymentService;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount1;
import uk.org.openbanking.datamodel.account.OBCashAccount51;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.account.OBScheduledPayment3;

import java.util.Collections;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

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
        FRScheduledPayment payment = defaultPayment(DateTime.now().minusDays(1), ScheduledPaymentStatus.PENDING);
        given(paymentsService.getPendingAndDueScheduledPayments()).willReturn(Collections.singletonList(payment));
        FRAccount account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);

        // When
        acceptDueScheduledPaymentTask.payDueScheduledPayments();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(toFRAmount(payment.getScheduledPayment().getInstructedAmount())), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updateSchedulePayment(argThat(p -> p.getStatus().equals(ScheduledPaymentStatus.COMPLETED)));
    }

    @Test
    public void scheduledPaymentDue_shouldCreditAccount() throws CurrencyConverterException {
        // Given
        FRScheduledPayment payment = defaultPayment(DateTime.now().minusDays(1), ScheduledPaymentStatus.PENDING);
        given(paymentsService.getPendingAndDueScheduledPayments()).willReturn(Collections.singletonList(payment));
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(defaultAccount(DEBIT_ACCOUNT));

        FRAccount account = defaultAccount(CREDIT_ACCOUNT);
        given(account2StoreService.findAccountByIdentification(CREDIT_ACCOUNT)).willReturn(Optional.of(account));

        // When
        acceptDueScheduledPaymentTask.payDueScheduledPayments();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(toFRAmount(payment.getScheduledPayment().getInstructedAmount())), eq(OBCreditDebitCode.CREDIT), eq(payment), any());
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
        FRScheduledPayment payment = defaultPayment(DateTime.now().minusDays(1), ScheduledPaymentStatus.PENDING);
        given(paymentsService.getPendingAndDueScheduledPayments()).willReturn(Collections.singletonList(payment));
        FRAccount account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(new CurrencyConverterException("Simulated failure")).when(moneyService).moveMoney(any(), any(), any(), any(), any());

        // When
        acceptDueScheduledPaymentTask.payDueScheduledPayments();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(toFRAmount(payment.getScheduledPayment().getInstructedAmount())), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updateSchedulePayment(argThat(p -> p.getStatus().equals(ScheduledPaymentStatus.REJECTED)));
        assertThat(payment.getRejectionReason()).isEqualTo("Can't convert amount in the right currency: Simulated failure");
    }

    @Test
    public void shouldRejectPaymentWhenAnyException() throws CurrencyConverterException {
        // Given
        FRScheduledPayment payment = defaultPayment(DateTime.now().minusDays(1), ScheduledPaymentStatus.PENDING);
        given(paymentsService.getPendingAndDueScheduledPayments()).willReturn(Collections.singletonList(payment));
        FRAccount account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(new RuntimeException("Simulated failure")).when(moneyService).moveMoney(any(), any(), any(), any(), any());

        // When
        acceptDueScheduledPaymentTask.payDueScheduledPayments();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(toFRAmount(payment.getScheduledPayment().getInstructedAmount())), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updateSchedulePayment(argThat(p -> p.getStatus().equals(ScheduledPaymentStatus.REJECTED)));
        assertThat(payment.getRejectionReason()).isEqualTo("Failed to execute payment: Simulated failure");
    }

    @Test
    public void scheduledPayment_ignoreIfNotDue() throws CurrencyConverterException {
        // Given
        FRScheduledPayment payment = defaultPayment(DateTime.now().plusDays(1), ScheduledPaymentStatus.PENDING);
        given(paymentsService.getPendingAndDueScheduledPayments()).willReturn(Collections.singletonList(payment));

        // When
        acceptDueScheduledPaymentTask.payDueScheduledPayments();

        // Then
        verifyZeroInteractions(moneyService);
        assertThat(payment.getStatus()).isEqualTo(ScheduledPaymentStatus.PENDING);
    }

    private FRAccount defaultAccount(String payAccount) {
        return FRAccount.builder().id(payAccount).build();
    }

    private FRScheduledPayment defaultPayment(DateTime scheduledExecution, ScheduledPaymentStatus status) {
        OBScheduledPayment3 scheduledPayment = new OBScheduledPayment3()
                .scheduledPaymentDateTime(scheduledExecution)
                .creditorAccount(new OBCashAccount51().identification(CREDIT_ACCOUNT))
                .instructedAmount(new OBActiveOrHistoricCurrencyAndAmount1()
                        .amount("3")
                        .currency("GBP"));
        return FRScheduledPayment.builder()
                .accountId(DEBIT_ACCOUNT)
                .status(status)
                .id("111")
                .scheduledPayment(scheduledPayment)
                .build();
    }
}