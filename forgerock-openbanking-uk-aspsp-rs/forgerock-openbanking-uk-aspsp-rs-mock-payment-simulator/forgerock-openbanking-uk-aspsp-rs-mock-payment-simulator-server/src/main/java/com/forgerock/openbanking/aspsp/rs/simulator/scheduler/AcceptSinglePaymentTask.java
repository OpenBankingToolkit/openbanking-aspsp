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
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRTransactionData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRBalanceType;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRCreditDebitIndicator;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.Account;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.Balance;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRTransaction;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRPaymentSetup;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.payment.SinglePaymentService;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.simulator.constants.SimulatorConstants.RUN_SCHEDULED_TASK_PROPERTY;
import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;

@Slf4j
@Component
@ConditionalOnProperty(name = RUN_SCHEDULED_TASK_PROPERTY, matchIfMissing = true)
public class AcceptSinglePaymentTask {

    private final static DateTimeFormatter format = DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT);

    private SinglePaymentService paymentsService;
    private AccountStoreService accountStoreService;
    private MoneyService moneyService;
    private PaymentNotificationFacade paymentNotificationService;

    public AcceptSinglePaymentTask(SinglePaymentService paymentsService, AccountStoreService accountStoreService, MoneyService moneyService, PaymentNotificationFacade paymentNotificationService) {
        this.paymentsService = paymentsService;
        this.accountStoreService = accountStoreService;
        this.moneyService = moneyService;
        this.paymentNotificationService = paymentNotificationService;
    }

    @Scheduled(fixedRate = 60 * 1000)
    @SchedulerLock(name = "singlePayment")
    public void autoAcceptPayment() {
        log.info("Auto-accept payment task waking up. The time is now {}.", format.print(DateTime.now()));
        Collection<FRPaymentSetup> allPaymentsInProcess = paymentsService.getAllPaymentsInProcess();
        for (FRPaymentSetup payment: allPaymentsInProcess) {
            log.info("Processing payment {}", payment);
            try {
                Account accountTo = accountStoreService.getAccount(payment.getAccountId());
                String identificationFrom = moveDebitPayment(payment, accountTo);
                Optional<Account> isAccountFromOurs = accountStoreService.findAccountByIdentification(identificationFrom);
                if (isAccountFromOurs.isPresent()) {
                    moveCreditPayment(payment, identificationFrom, isAccountFromOurs.get());
                } else {
                    log.info("Account '{}' not ours", identificationFrom);
                }
                log.info("Update payment status to completed");
                payment.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);
                log.info("Payment {}", payment);
            } catch (CurrencyConverterException e) {
                log.info("Can't convert amount in the right currency", e);
                log.info("Update payment status to rejected");
                payment.setStatus(ConsentStatusCode.REJECTED);
                log.info("Payment {}", payment);
            } catch (Exception e) {
                log.error("Couldn't auto-pay payment.", e);
                log.info("Update payment status to rejected");
                payment.setStatus(ConsentStatusCode.REJECTED);
                log.info("Payment {}", payment);
            } finally {
                paymentsService.updatePayment(payment);
                paymentNotificationService.paymentStatusChanged(payment);
            }
        }
        log.info("All payments in process are now accepted. See you in a minutes! The time is now {}.",
                format.print(DateTime.now()));
    }

    private void moveCreditPayment(FRPaymentSetup payment, String identificationFrom, Account accountFrom) throws CurrencyConverterException {
        log.info("Account '{}' is ours: {}", identificationFrom, accountFrom);
        log.info("Move the money to this account");
        moneyService.moveMoney(accountFrom, payment.getInitiation().getInstructedAmount(), FRCreditDebitIndicator.CREDIT, payment, this::createTransaction);
    }

    private String moveDebitPayment(FRPaymentSetup payment, Account accountTo) throws CurrencyConverterException {
        log.info("We are going to pay from this account: {}", accountTo);
        moneyService.moveMoney(accountTo, payment.getInitiation().getInstructedAmount(), FRCreditDebitIndicator.DEBIT, payment,  this::createTransaction);

        String identificationFrom = payment.getInitiation().getCreditorAccount().getIdentification();
        log.info("Find if the 'to' account '{}' is own by this ASPSP", identificationFrom);
        return identificationFrom;
    }

    private FRTransaction createTransaction(Account account, FRPaymentSetup paymentSetup, FRCreditDebitIndicator creditDebitCode, Balance balance, FRAmount amount) {
        log.info("Create transaction");
        String transactionId = UUID.randomUUID().toString();

        DateTime bookingDate = new DateTime(paymentSetup.getCreated());
        FRTransactionData transactionData = FRTransactionData.builder()
                .accountId(account.getId())
                .transactionId(transactionId)
                .amount(amount)
                .creditDebitIndicator(creditDebitCode)
                .status(FRTransactionData.FREntryStatus.BOOKED)
                .bookingDateTime(bookingDate)
                .valueDateTime(DateTime.now())
                .balance(FRTransactionData.FRTransactionCashBalance.builder()
                        .amount(balance.getCurrencyAndAmount())
                        .creditDebitIndicator(balance.getCreditDebitIndicator())
                        .type(FRBalanceType.INTERIMBOOKED)
                        .build())
                .build();

        if (paymentSetup.getInitiation().getRemittanceInformation() != null) {
            transactionData.setTransactionReference(paymentSetup.getInitiation().getRemittanceInformation().getReference());
            transactionData.setTransactionInformation(paymentSetup.getInitiation().getRemittanceInformation().getUnstructured());
        }

        FRTransaction transaction = FRTransaction.builder()
                .id(transactionId)
                .bookingDateTime(bookingDate)
                .accountId(account.getId())
                .transaction(transactionData)
                .statementIds(new ArrayList<>()) // Need a mutable list to be set here so don't use Lombok.singular to init list
                .build();
        log.info("Transaction created {}", transaction);
        return transaction;
    }
}
