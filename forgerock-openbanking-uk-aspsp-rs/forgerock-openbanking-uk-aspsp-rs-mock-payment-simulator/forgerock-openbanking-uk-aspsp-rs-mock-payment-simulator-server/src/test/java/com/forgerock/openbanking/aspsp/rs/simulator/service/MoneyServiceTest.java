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
package com.forgerock.openbanking.aspsp.rs.simulator.service;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBalance1;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRPaymentSetup;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.account.FRTransaction6;
import com.forgerock.openbanking.common.services.store.balance.BalanceStoreService;
import com.forgerock.openbanking.common.services.store.transaction.TransactionStoreService;
import com.github.jsonzou.jmockdata.JMockData;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;
import uk.org.openbanking.datamodel.account.OBCashBalance1;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MoneyServiceTest {

    private static final String DEBIT_ACCOUNT = "payer_123";
    private static final String CREDIT_ACCOUNT = "payee_987";

    @Mock
    private BalanceStoreService balanceStoreService;
    @Mock
    private TransactionStoreService transactionStoreService;
    @InjectMocks
    private MoneyService moneyService;

    @Test
    public void shouldDebitAccount() throws CurrencyConverterException {
        // Given
        given(balanceStoreService.getBalance(DEBIT_ACCOUNT, OBBalanceType1Code.INTERIMAVAILABLE))
                .willReturn(Optional.of(defaultBalance(DEBIT_ACCOUNT, "20")));

        // When
        moneyService.moveMoney(defaultAccount(DEBIT_ACCOUNT), defaultAmount(), OBCreditDebitCode.DEBIT,
                new FRPaymentSetup(), mock(CreateTransaction.class));

        // Then
        verify(balanceStoreService).updateBalance(defaultBalance(DEBIT_ACCOUNT, "17.00"));
    }

    @Test
    public void shouldCreditAccount() throws CurrencyConverterException {
        // Given
        given(balanceStoreService.getBalance(CREDIT_ACCOUNT, OBBalanceType1Code.INTERIMAVAILABLE))
                .willReturn(Optional.of(defaultBalance(CREDIT_ACCOUNT, "1")));


        // When
        moneyService.moveMoney(defaultAccount(CREDIT_ACCOUNT), defaultAmount(), OBCreditDebitCode.CREDIT,
                new FRPaymentSetup(), mock(CreateTransaction.class));

        // Then
        verify(balanceStoreService).updateBalance(defaultBalance(CREDIT_ACCOUNT, "4.00"));
    }

    @Test
    public void shouldCreateTransaction() throws CurrencyConverterException {
        // Given
        FRBalance1 balance = defaultBalance(DEBIT_ACCOUNT, "20");
        given(balanceStoreService.getBalance(DEBIT_ACCOUNT, OBBalanceType1Code.INTERIMAVAILABLE))
                .willReturn(Optional.of(balance));
        CreateTransaction<FRPaymentSetup> createTransaction = mock(CreateTransaction.class);
        FRAccount2 account = defaultAccount(DEBIT_ACCOUNT);
        FRPaymentSetup payment = new FRPaymentSetup();
        FRTransaction6 transaction = JMockData.mock(FRTransaction6.class);
        OBActiveOrHistoricCurrencyAndAmount amount = defaultAmount();
        given(createTransaction.createTransaction(account, payment, OBCreditDebitCode.DEBIT, balance, amount)).willReturn(transaction);

        // When
        moneyService.moveMoney(account, amount, OBCreditDebitCode.DEBIT, payment, createTransaction);

        // Then
        verify(transactionStoreService).create(transaction);
    }

    private FRAccount2 defaultAccount(String payAccount) {
        return FRAccount2.builder().id(payAccount).build();
    }

    private OBActiveOrHistoricCurrencyAndAmount defaultAmount() {
        return new OBActiveOrHistoricCurrencyAndAmount()
                .amount("3")
                .currency("GBP");
    }

    private FRBalance1 defaultBalance(String accountId, String amount) {
        return FRBalance1.builder()
                .accountId(accountId)
                .balance(new OBCashBalance1()
                        .creditDebitIndicator(OBCreditDebitCode.DEBIT)
                        .amount(new OBActiveOrHistoricCurrencyAndAmount()
                                .amount(amount)
                                .currency("GBP"))).build();
    }

}