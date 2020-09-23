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
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v1_1.payment.FRPaymentSetup1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.payment.SinglePaymentService;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

import java.util.Collections;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
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
        OBActiveOrHistoricCurrencyAndAmount instructedAmount = toOBActiveOrHistoricCurrencyAndAmount(payment.getInitiation().getInstructedAmount());
        verify(moneyService).moveMoney(eq(account), eq(instructedAmount), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
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
        OBActiveOrHistoricCurrencyAndAmount instructedAmount = toOBActiveOrHistoricCurrencyAndAmount(payment.getInitiation().getInstructedAmount());
        verify(moneyService).moveMoney(eq(account), eq(instructedAmount), eq(OBCreditDebitCode.CREDIT), eq(payment), any());
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
        OBActiveOrHistoricCurrencyAndAmount instructedAmount = toOBActiveOrHistoricCurrencyAndAmount(payment.getInitiation().getInstructedAmount());
        verify(moneyService).moveMoney(eq(account), eq(instructedAmount), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
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
        OBActiveOrHistoricCurrencyAndAmount instructedAmount = toOBActiveOrHistoricCurrencyAndAmount(payment.getInitiation().getInstructedAmount());
        verify(moneyService).moveMoney(eq(account), eq(instructedAmount), eq(OBCreditDebitCode.DEBIT), eq(payment), any());
        verify(paymentsService).updatePayment(argThat(p -> p.getStatus().equals(ConsentStatusCode.REJECTED)));
    }

    private FRAccount2 defaultAccount(String payAccount) {
        return FRAccount2.builder().id(payAccount).build();
    }

    private FRPaymentSetup1 defaultPayment() {
        FRWriteDomesticDataInitiation initiation = FRWriteDomesticDataInitiation.builder()
                .creditorAccount(FRAccount.builder().identification(CREDIT_ACCOUNT).build())
                .instructedAmount(FRAmount.builder().currency("GBP").amount("3").build())
                .build();
        return FRPaymentSetup1.builder()
                .accountId(DEBIT_ACCOUNT)
                .paymentSetupRequest(FRWriteDomesticConsent.builder()
                        .data(FRWriteDomesticConsentData.builder().initiation(initiation).build())
                        .build())
                .build();
    }

}