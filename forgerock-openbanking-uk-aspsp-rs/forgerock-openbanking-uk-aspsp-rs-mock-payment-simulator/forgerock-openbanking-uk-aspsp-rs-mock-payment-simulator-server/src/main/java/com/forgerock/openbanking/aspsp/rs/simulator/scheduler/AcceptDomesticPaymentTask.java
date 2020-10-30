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
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.payment.DomesticPaymentService;
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
public class AcceptDomesticPaymentTask {

    private final static DateTimeFormatter format = DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT);

    private final DomesticPaymentService domesticPaymentsService;
    private final AccountStoreService accountStoreService;
    private final MoneyService moneyService;
    private final PaymentNotificationFacade paymentNotificationService;

    public AcceptDomesticPaymentTask(DomesticPaymentService domesticPaymentsService, AccountStoreService accountStoreService, MoneyService moneyService, PaymentNotificationFacade paymentNotificationService) {
        this.domesticPaymentsService = domesticPaymentsService;
        this.accountStoreService = accountStoreService;
        this.moneyService = moneyService;
        this.paymentNotificationService = paymentNotificationService;
    }

    @Scheduled(fixedRate = 60 * 1000)
    @SchedulerLock(name = "domesticPayment")
    public void autoAcceptPayment() {
        log.info("Auto-accept payment task waking up. The time is now {}.", format.print(DateTime.now()));
        Collection<FRDomesticConsent> allPaymentsInProcess = domesticPaymentsService.getAllPaymentsInProcess();
        for (FRDomesticConsent payment : allPaymentsInProcess) {
            log.info("Processing payment {}", payment);
            try {
                String identificationTo = moveDebitPayment(payment);
                Optional<Account> isAccountToFromOurs = accountStoreService.findAccountByIdentification(identificationTo);
                if (isAccountToFromOurs.isPresent()) {
                    moveCreditPayment(payment, identificationTo, isAccountToFromOurs.get());
                } else {
                    log.info("Account '{}' not ours", identificationTo);
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
                domesticPaymentsService.updatePayment(payment);
                paymentNotificationService.paymentStatusChanged(payment);
            }
        }
        log.info("All payments in process are now accepted. See you in 1 minute! The time is now {}.",
                format.print(DateTime.now()));
    }

    private String moveDebitPayment(FRDomesticConsent payment) throws CurrencyConverterException {
        Account accountFrom = accountStoreService.getAccount(payment.getAccountId());
        log.info("We are going to pay from this account: {}", accountFrom);
        moneyService.moveMoney(accountFrom, payment.getInitiation().getInstructedAmount(), FRCreditDebitIndicator.DEBIT, payment, this::createTransaction);

        String identificationFrom = payment.getInitiation().getCreditorAccount().getIdentification();
        log.debug("Find if the 'to' account '{}' is own by this ASPSP", identificationFrom);
        return identificationFrom;
    }

    private void moveCreditPayment(FRDomesticConsent payment, String identificationTo, Account accountFrom) throws CurrencyConverterException {
        log.info("Account '{}' is ours: {}", identificationTo, accountFrom);
        log.info("Move the money to this account");
        moneyService.moveMoney(accountFrom, payment.getInitiation().getInstructedAmount(), FRCreditDebitIndicator.CREDIT, payment, this::createTransaction);
    }

    private FRTransaction createTransaction(Account account, FRDomesticConsent payment, FRCreditDebitIndicator creditDebitIndicator, Balance balance, FRAmount amount) {
        log.info("Create transaction");
        String transactionId = UUID.randomUUID().toString();
        DateTime bookingDate = new DateTime(payment.getCreated());

        FRTransactionData transactionData = FRTransactionData.builder()
                .transactionId(transactionId)
                .status(FRTransactionData.FREntryStatus.BOOKED)
                .valueDateTime(DateTime.now())
                .accountId(account.getId())
                .amount(amount)
                .creditDebitIndicator(creditDebitIndicator)
                .bookingDateTime(bookingDate)
                .statementReferences(new ArrayList<>())
                .balance(FRTransactionData.FRTransactionCashBalance.builder()
                        .amount(balance.getCurrencyAndAmount())
                        .creditDebitIndicator(balance.getCreditDebitIndicator())
                        .type(FRBalanceType.INTERIMBOOKED)
                        .build())
                .build();

        if (payment.getInitiation().getRemittanceInformation() != null) {
            transactionData.setTransactionReference(payment.getInitiation().getRemittanceInformation().getReference());
            transactionData.setTransactionInformation(payment.getInitiation().getRemittanceInformation().getUnstructured());
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
