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
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRCreditDebitIndicator;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalConsent;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.payment.InternationalPaymentService;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AcceptInternationalPaymentTaskTest {


    private static final String DEBIT_ACCOUNT = "payer_123";
    private static final String CREDIT_ACCOUNT = "payee_987";

    @Mock
    private AccountStoreService account2StoreService;
    @Mock
    private InternationalPaymentService paymentsService;
    @Mock
    private MoneyService moneyService;
    @Mock
    private PaymentNotificationFacade paymentNotificationService;
    @InjectMocks
    private AcceptInternationalPaymentTask acceptDomesticPaymentTask;

    @Test
    public void shouldDebitAccount() throws CurrencyConverterException {
        // Given
        FRInternationalConsent payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        FRAccount account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);

        // When
        acceptDomesticPaymentTask.autoAcceptPayment();

        // Then
        FRAmount amount = payment.getInitiation().getInstructedAmount();
        verify(moneyService).moveMoney(eq(account), eq(amount), eq(FRCreditDebitIndicator.DEBIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED)));
    }

    @Test
    public void shouldCreditAccount() throws CurrencyConverterException {
        // Given
        FRInternationalConsent payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(defaultAccount(DEBIT_ACCOUNT));

        FRAccount account = defaultAccount(CREDIT_ACCOUNT);
        given(account2StoreService.findAccountByIdentification(CREDIT_ACCOUNT))
                .willReturn(Optional.of(account));

        // When
        acceptDomesticPaymentTask.autoAcceptPayment();

        // Then
        FRAmount amount = payment.getInitiation().getInstructedAmount();
        verify(moneyService).moveMoney(eq(account), eq(amount), eq(FRCreditDebitIndicator.CREDIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED)));
    }

    @Test
    public void shouldRejectPaymentWhenCurrencyConversionException() throws CurrencyConverterException {
        // Given
        FRInternationalConsent payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        FRAccount account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(CurrencyConverterException.class).when(moneyService).moveMoney(any(), any(), any(), any(), any());

        // When
        acceptDomesticPaymentTask.autoAcceptPayment();

        // Then
        FRAmount amount = payment.getInitiation().getInstructedAmount();
        verify(moneyService).moveMoney(eq(account), eq(amount), eq(FRCreditDebitIndicator.DEBIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.REJECTED)));
    }

    @Test
    public void shouldRejectPaymentWhenAnyException() throws CurrencyConverterException {
        // Given
        FRInternationalConsent payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        FRAccount account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(new RuntimeException()).when(moneyService).moveMoney(any(), any(), any(), any(), any());

        // When
        acceptDomesticPaymentTask.autoAcceptPayment();

        // Then
        FRAmount amount = payment.getInitiation().getInstructedAmount();
        verify(moneyService).moveMoney(eq(account), eq(amount), eq(FRCreditDebitIndicator.DEBIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.REJECTED)));
    }

    private FRAccount defaultAccount(String payAccount) {
        return FRAccount.builder().id(payAccount).build();
    }

    private FRInternationalConsent defaultPayment() {
        FRWriteInternationalDataInitiation initiation = FRWriteInternationalDataInitiation.builder()
                .creditorAccount(FRAccountIdentifier.builder().identification(CREDIT_ACCOUNT).build())
                .instructedAmount(FRAmount.builder().currency("GBP").amount("3").build())
                .build();
        return FRInternationalConsent.builder()
                .accountId(DEBIT_ACCOUNT)
                .internationalConsent(FRWriteInternationalConsent.builder()
                        .data(FRWriteInternationalConsentData.builder()
                                .initiation(initiation)
                                .build())
                        .build())
                .build();
    }

}