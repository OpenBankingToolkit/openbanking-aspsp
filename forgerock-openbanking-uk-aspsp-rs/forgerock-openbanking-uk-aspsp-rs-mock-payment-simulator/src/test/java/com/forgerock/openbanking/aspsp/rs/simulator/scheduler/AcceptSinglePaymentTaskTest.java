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
import com.forgerock.openbanking.commons.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.commons.model.openbanking.v1_1.payment.FRPaymentSetup1;
import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.commons.services.store.account.AccountStoreService;
import com.forgerock.openbanking.commons.services.store.payment.SinglePaymentService;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.payment.OBCashAccountCreditor1;
import uk.org.openbanking.datamodel.payment.OBInitiation1;
import uk.org.openbanking.datamodel.payment.OBPaymentDataSetup1;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AcceptSinglePaymentTaskTest {

    private static final String DEBIT_ACCOUNT = "payer_123";
    private static final String CREDIT_ACCOUNT = "payee_987";

    @Mock
    private AccountStoreService account2StoreService;
    @Mock
    private SinglePaymentService paymentsService;
    @Mock
    private MoneyService moneyService;
    @Mock
    private PaymentNotificationFacade paymentNotificationService;
    @InjectMocks
    private AcceptSinglePaymentTask autoAcceptPaymentTask;

    @Test
    public void shouldDebitAccount() throws CurrencyConverterException {
        // Given
        FRPaymentSetup1 payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);

        // When
        autoAcceptPaymentTask.autoAcceptPayment();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(payment.getInitiation().getInstructedAmount()), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED)));
    }

    @Test
    public void shouldCreditAccount() throws CurrencyConverterException {
        // Given
        FRPaymentSetup1 payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(defaultAccount(DEBIT_ACCOUNT));

        FRAccount2 account = defaultAccount(CREDIT_ACCOUNT);
        given(account2StoreService.findAccountByIdentification(CREDIT_ACCOUNT))
                .willReturn(Optional.of(account));

        // When
        autoAcceptPaymentTask.autoAcceptPayment();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(payment.getInitiation().getInstructedAmount()), eq(OBCreditDebitCode.CREDIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED)));
    }

    @Test
    public void shouldRejectPaymentWhenCurrencyConversionException() throws CurrencyConverterException {
        // Given
        FRPaymentSetup1 payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(CurrencyConverterException.class).when(moneyService).moveMoney(any(), any(), any(), any(), any());

        // When
        autoAcceptPaymentTask.autoAcceptPayment();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(payment.getInitiation().getInstructedAmount()), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.REJECTED)));
    }

    @Test
    public void shouldRejectPaymentWhenAnyException() throws CurrencyConverterException {
        // Given
        FRPaymentSetup1 payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(new RuntimeException()).when(moneyService).moveMoney(any(), any(), any(), any(), any());

        // When
        autoAcceptPaymentTask.autoAcceptPayment();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(payment.getInitiation().getInstructedAmount()), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.REJECTED)));
    }

    private FRAccount2 defaultAccount(String payAccount) {
        return FRAccount2.builder().id(payAccount).build();
    }

    private FRPaymentSetup1 defaultPayment() {
        OBInitiation1 initiation = new OBInitiation1()
                .creditorAccount(new OBCashAccountCreditor1().identification(CREDIT_ACCOUNT))
                .instructedAmount(new OBActiveOrHistoricCurrencyAndAmount()
                        .amount("3")
                        .currency("GBP"));
        return FRPaymentSetup1.builder()
                .accountId(DEBIT_ACCOUNT)
                .paymentSetupRequest(new OBPaymentSetup1()
                        .data(new OBPaymentDataSetup1()
                                .initiation(initiation)))
                .build();
    }

}