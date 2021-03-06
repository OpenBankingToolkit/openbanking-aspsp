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
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRScheduledPaymentData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRTransactionData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRBalanceType;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRCreditDebitIndicator;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.Account;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.Balance;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRScheduledPayment;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRTransaction;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.account.scheduledpayment.ScheduledPaymentService;
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
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.simulator.constants.SimulatorConstants.RUN_SCHEDULED_TASK_PROPERTY;
import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;

@Slf4j
@Component
@ConditionalOnProperty(name = RUN_SCHEDULED_TASK_PROPERTY, matchIfMissing = true)
public class AcceptDueScheduledPaymentTask {
    private final static DateTimeFormatter format = DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT);

    private final ScheduledPaymentService scheduledPaymentService;
    private final AccountStoreService accountStoreService;
    private final MoneyService moneyService;
    private final PaymentNotificationFacade paymentNotificationService;

    public AcceptDueScheduledPaymentTask(ScheduledPaymentService scheduledPaymentService, AccountStoreService accountStoreService, MoneyService moneyService, PaymentNotificationFacade paymentNotificationService) {
        this.scheduledPaymentService = scheduledPaymentService;
        this.accountStoreService = accountStoreService;
        this.moneyService = moneyService;
        this.paymentNotificationService = paymentNotificationService;
    }

    @Scheduled(fixedRate = 60 * 1000)
    @SchedulerLock(name = "payDueScheduledPayments")
    public void payDueScheduledPayments() {
        log.info("Scheduled payments payment task waking up. The time is now {}.", format.print(DateTime.now()));

        for (FRScheduledPayment scheduledPayment : scheduledPaymentService.getPendingAndDueScheduledPayments()) {
            FRScheduledPaymentData payment = scheduledPayment.getScheduledPayment();

            if (!payment.getScheduledPaymentDateTime().isBefore(DateTime.now())) {
                log.warn("Scheduled payment: {} with scheduledPaymentDateTime: {} is not due yet and should not have been loaded", scheduledPayment.getId(), payment.getScheduledPaymentDateTime());
                continue;
            }

            log.info("Processing scheduled payment {}", payment);
            try {
                String identificationFrom = moveDebitPayment(scheduledPayment);
                Optional<Account> isAccountFromOurs = accountStoreService.findAccountByIdentification(identificationFrom);
                if (isAccountFromOurs.isPresent()) {
                    moveCreditPayment(scheduledPayment, identificationFrom, isAccountFromOurs.get());
                } else {
                    log.info("Account '{}' not ours", identificationFrom);
                }
                log.info("Update payment status to completed");
                scheduledPayment.setStatus(ScheduledPaymentStatus.COMPLETED);
                log.debug("Payment {}", payment);
            } catch (CurrencyConverterException e) {
                log.info("Can't convert amount in the right currency", e);
                log.info("Update payment status to rejected");
                scheduledPayment.setRejectionReason("Can't convert amount in the right currency: " + e.getMessage());
                scheduledPayment.setStatus(ScheduledPaymentStatus.REJECTED);
                log.info("Payment {}", payment);
            } catch (Exception e) {
                log.error("Couldn't pay scheduled payment.", e);
                log.info("Update payment status to rejected");
                scheduledPayment.setRejectionReason("Failed to execute payment: " + e.getMessage());
                scheduledPayment.setStatus(ScheduledPaymentStatus.REJECTED);
                log.info("Payment {}", payment);
            } finally {
                scheduledPaymentService.updateSchedulePayment(scheduledPayment);
                paymentNotificationService.paymentStatusChanged(scheduledPayment);
            }
        }
        log.info("All due scheduled payments are now completed. The time is now {}.",
                format.print(DateTime.now()));
    }

    private String moveDebitPayment(FRScheduledPayment payment) throws CurrencyConverterException {
        Account accountFrom = accountStoreService.getAccount(payment.getAccountId());
        log.info("We are going to pay from this account: {}", accountFrom);
        moneyService.moveMoney(accountFrom, payment.getScheduledPayment().getInstructedAmount(), FRCreditDebitIndicator.DEBIT, payment, this::createTransaction);

        String identificationFrom = payment.getScheduledPayment().getCreditorAccount().getIdentification();
        log.debug("Find if the 'to' account '{}' is own by this ASPSP", identificationFrom);
        return identificationFrom;
    }

    private void moveCreditPayment(FRScheduledPayment payment, String identificationTo, Account accountFrom) throws CurrencyConverterException {
        log.info("Account '{}' is ours: {}", identificationTo, accountFrom);
        log.info("Move the money to this account");
        moneyService.moveMoney(accountFrom, payment.getScheduledPayment().getInstructedAmount(), FRCreditDebitIndicator.CREDIT, payment, this::createTransaction);
    }

    private FRTransaction createTransaction(Account account, FRScheduledPayment payment, FRCreditDebitIndicator creditDebitCode, Balance balance, FRAmount amount) {
        log.info("Create transaction");
        String transactionId = UUID.randomUUID().toString();
        DateTime bookingDate = new DateTime(payment.getCreated());

        FRTransactionData transactionData = FRTransactionData.builder()
                .transactionId(transactionId)
                .status(FRTransactionData.FREntryStatus.BOOKED)
                .valueDateTime(DateTime.now())
                .accountId(account.getId())
                .amount(amount)
                .creditDebitIndicator(creditDebitCode)
                .bookingDateTime(bookingDate)
                .statementReferences(new ArrayList<>())
                .balance(FRTransactionData.FRTransactionCashBalance.builder()
                        .amount(balance.getCurrencyAndAmount())
                        .creditDebitIndicator(balance.getCreditDebitIndicator())
                        .type(FRBalanceType.INTERIMBOOKED)
                        .build())
                .build();

        if (payment.getScheduledPayment().getReference() != null) {
            transactionData.setTransactionReference(payment.getScheduledPayment().getReference());
        }

        FRTransaction transaction = FRTransaction.builder()
                .id(transactionId)
                .bookingDateTime(bookingDate)
                .accountId(account.getId())
                .transaction(transactionData)
                .build();
        log.info("Transaction created {}", transaction);
        return transaction;
    }
}
