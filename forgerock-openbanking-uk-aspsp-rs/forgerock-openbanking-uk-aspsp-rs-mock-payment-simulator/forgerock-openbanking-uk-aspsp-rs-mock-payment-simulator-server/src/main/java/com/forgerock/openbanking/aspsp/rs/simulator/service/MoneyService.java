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

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.Account;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.Balance;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRTransaction;
import com.forgerock.openbanking.common.services.store.balance.BalanceStoreService;
import com.forgerock.openbanking.common.services.store.transaction.TransactionStoreService;
import com.tunyk.currencyconverter.BankUaCom;
import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverter;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class MoneyService {

    @Autowired
    private BalanceStoreService balanceStoreService;
    @Autowired
    private TransactionStoreService transactionStoreService;

    public <T> void moveMoney(Account account, FRAmount amount, OBCreditDebitCode creditDebitCode, T payment, CreateTransaction<T> createTransaction) throws CurrencyConverterException {
        Optional<Balance> balanceIf = balanceStoreService.getBalance(account.getId(), OBBalanceType1Code.INTERIMAVAILABLE);
        //Verify account for a balance
        if (!balanceIf.isPresent()) {
            throw new IllegalStateException("No balance found of type '"
                    + OBBalanceType1Code.INTERIMAVAILABLE + "' for account id '" + account.getId() + "'");
        }
        Balance balance = balanceIf.get();

        BigDecimal finalAmount = computeAmount(amount, creditDebitCode, balance);

        updateBalance(balance, finalAmount);

        FRTransaction transaction = createTransaction.createTransaction(account, payment, creditDebitCode, balance, amount);

        addTransactionToLatestStatement(account, transaction);

        transaction = transactionStoreService.create(transaction);
        log.info("Transaction created {}", transaction);
        balanceStoreService.updateBalance(balance);
        log.info("Balance updated {}", balance);
    }

    private void addTransactionToLatestStatement(Account account, FRTransaction transaction) {
        if (account.getLatestStatementId() != null) {
            log.info("Add the new transaction id '{}' to the latest statement id '{}'",
                    transaction.getId(), account.getLatestStatementId());
            try {
                transaction.setId(transaction.getId());
                transaction.addStatementId(account.getLatestStatementId());
                log.debug("Added to statement");
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    log.warn("Latest statement '{}' can't be found, skip that part.", account.getLatestStatementId());
                } else {
                    throw e;
                }
            }
        }
    }

    private void updateBalance(Balance balance, BigDecimal finalAmount) {
        log.debug("Update balance amount");
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            balance.setAmount(finalAmount.negate());
            balance.setCreditDebitIndicator(OBCreditDebitCode.CREDIT);
        } else {
            balance.setAmount(finalAmount);
            balance.setCreditDebitIndicator(OBCreditDebitCode.DEBIT);
        }
        log.info("New balance {}", balance);
    }

    private BigDecimal computeAmount(FRAmount amount, OBCreditDebitCode creditDebitCode, Balance balance)
            throws CurrencyConverterException {
        BigDecimal currentAmount = balance.getAmount();
        BigDecimal paymentAmount = new BigDecimal(amount.getAmount());
        log.info("Balance amount {} and payment amount {}", currentAmount, paymentAmount);

        log.debug("Detect if it is an international payment");
        if (!amount.getCurrency()
                .equals(balance.getCurrency())) {
            log.info("International payment: We need to do a conversion of the payment amount {} {} into {}",
                    amount.getAmount(),
                    amount.getCurrency(),
                    balance.getCurrency());
            CurrencyConverter currencyConverter = new BankUaCom(
                    Currency.fromString(amount.getCurrency()),
                    Currency.fromString(balance.getCurrency()));
            float rate = currencyConverter.convertCurrency(1.0f);
            paymentAmount = paymentAmount.multiply(new BigDecimal(Float.toString(rate)));
            log.info("The payment amount converted is {}", paymentAmount);
        }

        BigDecimal finalAmount;
        switch (creditDebitCode) {
            case CREDIT:
                finalAmount = currentAmount.add(paymentAmount);
                break;
            case DEBIT:
            default:
                finalAmount = currentAmount.subtract(paymentAmount);
                break;
        }
        log.info("final amount: {}", finalAmount);
        return finalAmount;
    }
}
