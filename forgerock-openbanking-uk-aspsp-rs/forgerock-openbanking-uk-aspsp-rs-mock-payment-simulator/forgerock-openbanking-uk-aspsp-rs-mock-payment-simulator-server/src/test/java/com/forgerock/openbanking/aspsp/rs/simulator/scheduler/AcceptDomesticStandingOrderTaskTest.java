/**
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
import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRStandingOrder5;
import com.forgerock.openbanking.common.services.openbanking.frequency.FrequencyService;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.account.standingorder.StandingOrderService;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.account.*;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AcceptDomesticStandingOrderTaskTest {

    private static final String DEBIT_ACCOUNT = "payer_123";
    private static final String CREDIT_ACCOUNT = "payee_987";

    @Mock
    private AccountStoreService account2StoreService;
    @Mock
    private StandingOrderService paymentsService;
    @Mock
    private MoneyService moneyService;
    @Mock
    private PaymentNotificationFacade paymentNotificationService;
    @Mock
    private FrequencyService frequencyService;

    @InjectMocks
    private AcceptDueStandingOrderTask acceptDueStandingOrderTask;

    @Test
    public void pendingStandingOrder_firstPaymentDue_shouldDebitAccount() throws CurrencyConverterException {
        // Given
        FRStandingOrder5 payment = defaultPayment(StandingOrderStatus.PENDING);
        given(paymentsService.getActiveStandingOrders()).willReturn(Collections.singletonList(payment));
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);

        // When
        acceptDueStandingOrderTask.payDueStandingOrders();

        // Then
        verify(moneyService).moveMoney(eq(account), any(), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updateStandingOrder(argThat(p -> p.getStatus().equals(StandingOrderStatus.ACTIVE)));
    }

    @Test
    public void pendingStandingOrder_firstPaymentDue_shouldCreditAccount() throws CurrencyConverterException {
        // Given
        FRStandingOrder5 payment = defaultPayment(StandingOrderStatus.PENDING);
        given(paymentsService.getActiveStandingOrders()).willReturn(Collections.singletonList(payment));
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(defaultAccount(DEBIT_ACCOUNT));

        FRAccount2 account = defaultAccount(CREDIT_ACCOUNT);
        given(account2StoreService.findAccountByIdentification(CREDIT_ACCOUNT))
                .willReturn(Optional.of(account));

        // When
        acceptDueStandingOrderTask.payDueStandingOrders();

        // Then
        verify(moneyService).moveMoney(eq(account), any(), eq(OBCreditDebitCode.CREDIT), eq(payment), any());
        verify(paymentsService).updateStandingOrder(argThat(p -> p.getStatus().equals(StandingOrderStatus.ACTIVE)));
    }

    @Test
    public void pendingStandingOrder_recurringPaymentDue_shouldDebitAccount() throws CurrencyConverterException {
        // Given
        FRStandingOrder5 payment = defaultPayment(StandingOrderStatus.ACTIVE);
        given(paymentsService.getActiveStandingOrders()).willReturn(Collections.singletonList(payment));
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        given(frequencyService.getNextDateTime(any(), any())).willReturn(DateTime.now().minusDays(1));

        // When
        acceptDueStandingOrderTask.payDueStandingOrders();

        // Then
        verify(moneyService).moveMoney(eq(account), any(), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updateStandingOrder(argThat(p -> p.getStatus().equals(StandingOrderStatus.ACTIVE)));
    }

    @Test
    public void pendingStandingOrder_finalPaymentDue_shouldDebitAccount() throws CurrencyConverterException {
        // Given
        FRStandingOrder5 payment = defaultPayment(StandingOrderStatus.ACTIVE);
        payment.getStandingOrder().setNextPaymentDateTime(payment.getStandingOrder().getFinalPaymentDateTime());
        given(paymentsService.getActiveStandingOrders()).willReturn(Collections.singletonList(payment));
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);

        // When
        acceptDueStandingOrderTask.payDueStandingOrders();

        // Then
        verify(moneyService).moveMoney(eq(account), any(), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updateStandingOrder(argThat(p -> p.getStatus().equals(StandingOrderStatus.COMPLETED)));
    }

    @Test
    public void noDuePaymentsFound() {
        // Given
        given(paymentsService.getActiveStandingOrders()).willReturn(Collections.emptyList());

        // When
        acceptDueStandingOrderTask.payDueStandingOrders();

        // Then
        verify(paymentsService, times(0)).updateStandingOrder(any());
    }

    @Test
    public void shouldRejectPaymentWhenCurrencyConversionException() throws CurrencyConverterException {
        // Given
        FRStandingOrder5 payment = defaultPayment(StandingOrderStatus.PENDING);
        given(paymentsService.getActiveStandingOrders()).willReturn(Collections.singletonList(payment));
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(new CurrencyConverterException("Simulated failure")).when(moneyService).moveMoney(any(), any(), any(), any(), any());

        // When
        acceptDueStandingOrderTask.payDueStandingOrders();

        // Then
        verify(moneyService).moveMoney(eq(account), any(), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updateStandingOrder(argThat(p -> p.getStatus().equals(StandingOrderStatus.REJECTED)));
        assertThat(payment.getRejectionReason()).isEqualTo("Can't convert amount in the right currency: Simulated failure");
    }

    @Test
    public void shouldRejectPaymentWhenAnyException() throws CurrencyConverterException {
        // Given
        FRStandingOrder5 payment = defaultPayment(StandingOrderStatus.PENDING);
        given(paymentsService.getActiveStandingOrders()).willReturn(Collections.singletonList(payment));
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(new RuntimeException("Simulated failure")).when(moneyService).moveMoney(any(), any(), any(), any(), any());

        // When
        acceptDueStandingOrderTask.payDueStandingOrders();

        // Then
        verify(moneyService).moveMoney(eq(account), any(), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updateStandingOrder(argThat(p -> p.getStatus().equals(StandingOrderStatus.REJECTED)));
        assertThat(payment.getRejectionReason()).isEqualTo("Failed to execute payment: Simulated failure");
    }

    @Test
    public void scheduledPayment_ignoreIfInactive() {
        // Given
        FRStandingOrder5 payment = defaultPayment(StandingOrderStatus.PENDING);
        payment.getStandingOrder().setStandingOrderStatusCode(OBExternalStandingOrderStatus1Code.INACTIVE);
        given(paymentsService.getActiveStandingOrders()).willReturn(Collections.singletonList(payment));

        // When
        acceptDueStandingOrderTask.payDueStandingOrders();

        // Then
        verifyZeroInteractions(moneyService);
    }

    private FRAccount2 defaultAccount(String payAccount) {
        return FRAccount2.builder().id(payAccount).build();
    }

    private FRStandingOrder5 defaultPayment(StandingOrderStatus status) {

        OBStandingOrder5 standingOrder = new OBStandingOrder5()
                .creditorAccount(new OBCashAccount5().identification(CREDIT_ACCOUNT))
                .firstPaymentDateTime(DateTime.now().minusDays(3))
                .nextPaymentDateTime(DateTime.now().minusDays(2))
                .finalPaymentDateTime(DateTime.now().minusDays(1))
                .firstPaymentAmount(new OBActiveOrHistoricCurrencyAndAmount() .amount("1").currency("GBP"))
                .nextPaymentAmount(new OBActiveOrHistoricCurrencyAndAmount() .amount("2").currency("GBP"))
                .finalPaymentAmount(new OBActiveOrHistoricCurrencyAndAmount() .amount("3").currency("GBP"))
                .frequency("EvryDay")
                .standingOrderStatusCode(OBExternalStandingOrderStatus1Code.ACTIVE)
                .reference("test");
        return FRStandingOrder5.builder()
                .accountId(DEBIT_ACCOUNT)
                .status(status)
                .id("111")
                .standingOrder(standingOrder)
                .build();
    }

}