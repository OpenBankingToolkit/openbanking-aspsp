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
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.payment.DomesticPaymentService;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AcceptDomesticPaymentTaskTest {

    private static final String DEBIT_ACCOUNT = "payer_123";
    private static final String CREDIT_ACCOUNT = "payee_987";

    @Mock
    private AccountStoreService account2StoreService;
    @Mock
    private DomesticPaymentService paymentsService;
    @Mock
    private MoneyService moneyService;
    @Mock
    PaymentNotificationFacade paymentNotificationService;
    @InjectMocks
    private AcceptDomesticPaymentTask acceptDomesticPaymentTask;

    @Test
    public void shouldDebitAccount() throws CurrencyConverterException {
        // Given
        FRDomesticConsent payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        FRAccount account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        FRAmount instructedAmount = payment.getInitiation().getInstructedAmount();

        // When
        acceptDomesticPaymentTask.autoAcceptPayment();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(instructedAmount), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED)));
    }

    @Test
    public void shouldCreditAccount() throws CurrencyConverterException {
        // Given
        FRDomesticConsent payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(defaultAccount(DEBIT_ACCOUNT));

        FRAccount account = defaultAccount(CREDIT_ACCOUNT);
        given(account2StoreService.findAccountByIdentification(CREDIT_ACCOUNT)).willReturn(Optional.of(account));
        FRAmount instructedAmount = payment.getInitiation().getInstructedAmount();

        // When
        acceptDomesticPaymentTask.autoAcceptPayment();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(instructedAmount), eq(OBCreditDebitCode.CREDIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED)));
    }

    @Test
    public void shouldRejectPaymentWhenCurrencyConversionException() throws CurrencyConverterException {
        // Given
        FRDomesticConsent payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        FRAccount account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(CurrencyConverterException.class).when(moneyService).moveMoney(any(), any(), any(), any(), any());
        FRAmount instructedAmount = payment.getInitiation().getInstructedAmount();

        // When
        acceptDomesticPaymentTask.autoAcceptPayment();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(instructedAmount), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.REJECTED)));
    }

    @Test
    public void shouldRejectPaymentWhenAnyException() throws CurrencyConverterException {
        // Given
        FRDomesticConsent payment = defaultPayment();
        given(paymentsService.getAllPaymentsInProcess()).willReturn(Collections.singleton(payment));
        FRAccount account = defaultAccount(DEBIT_ACCOUNT);
        given(account2StoreService.getAccount(DEBIT_ACCOUNT)).willReturn(account);
        doThrow(new RuntimeException()).when(moneyService).moveMoney(any(), any(), any(), any(), any());
        FRAmount instructedAmount = payment.getInitiation().getInstructedAmount();

        // When
        acceptDomesticPaymentTask.autoAcceptPayment();

        // Then
        verify(moneyService).moveMoney(eq(account), eq(instructedAmount), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.REJECTED)));
    }

    private FRAccount defaultAccount(String payAccount) {
        return FRAccount.builder().id(payAccount).build();
    }

    private FRDomesticConsent defaultPayment() {
        FRWriteDomesticDataInitiation initiation = FRWriteDomesticDataInitiation.builder()
                .creditorAccount(FRFinancialAccount.builder().identification(CREDIT_ACCOUNT).build())
                .instructedAmount(FRAmount.builder().currency("GBP").amount("3").build())
                .build();
        return FRDomesticConsent.builder()
                .accountId(DEBIT_ACCOUNT)
                .domesticConsent(FRWriteDomesticConsent.builder()
                        .data(FRWriteDomesticConsentData.builder()
                                .initiation(initiation)
                                .build())
                        .build())
                .build();
    }

}