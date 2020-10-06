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
import com.forgerock.openbanking.common.model.openbanking.persistence.account.Account;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.Balance;
import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRTransaction;
import com.forgerock.openbanking.common.services.openbanking.frequency.FrequencyService;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.account.standingorder.StandingOrderService;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode1;
import uk.org.openbanking.datamodel.account.OBEntryStatus1Code;
import uk.org.openbanking.datamodel.account.OBExternalStandingOrderStatus1Code;
import uk.org.openbanking.datamodel.account.OBStandingOrder6;
import uk.org.openbanking.datamodel.account.OBTransaction6;
import uk.org.openbanking.datamodel.account.OBTransactionCashBalance;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.simulator.constants.SimulatorConstants.RUN_SCHEDULED_TASK_PROPERTY;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount9;
import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;

@Slf4j
@Component
@ConditionalOnProperty(name = RUN_SCHEDULED_TASK_PROPERTY, matchIfMissing = true)
public class AcceptDueStandingOrderTask {

    private final static DateTimeFormatter format = DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT);

    private final StandingOrderService standingOrderService;
    private final AccountStoreService accountStoreService;
    private final MoneyService moneyService;
    private final FrequencyService frequencyService;
    private final PaymentNotificationFacade paymentNotificationService;

    public AcceptDueStandingOrderTask(StandingOrderService standingOrderService, AccountStoreService accountStoreService, MoneyService moneyService, FrequencyService frequencyService, PaymentNotificationFacade paymentNotificationService) {
        this.standingOrderService = standingOrderService;
        this.accountStoreService = accountStoreService;
        this.moneyService = moneyService;
        this.frequencyService = frequencyService;
        this.paymentNotificationService = paymentNotificationService;
    }

    @Scheduled(fixedRate = 60 * 1000)
    @SchedulerLock(name = "payDueStandingOrders")
    public void payDueStandingOrders() {
        log.info("Standing order payment task waking up. The time is now {}.", format.print(DateTime.now()));

        final DateTime now = DateTime.now();
        for (FRStandingOrder frStandingOrder : standingOrderService.getActiveStandingOrders()) {
            OBStandingOrder6 obStandingOrder = frStandingOrder.getStandingOrder();

            // Check the OB status code has an ACTIVE status (because standing orders could be imported on /data endpoint with INACTIVE status).
            if (OBExternalStandingOrderStatus1Code.ACTIVE != obStandingOrder.getStandingOrderStatusCode()) {
                log.warn("Standing Order: '{}' has been given an OBExternalStandingOrderStatus1Code of {} and will not be processed.", frStandingOrder, obStandingOrder.getStandingOrderStatusCode());
                continue;
            }

            log.info("Processing standing order {}", obStandingOrder);
            try {
                boolean isFirstPaymentDue = obStandingOrder.getFirstPaymentDateTime().isBefore(now);
                boolean isFinalPaymentDue = obStandingOrder.getFinalPaymentDateTime().isBefore(now);
                boolean isNextRecurringPaymentDue = obStandingOrder.getNextPaymentDateTime().isBefore(now);
                boolean isBeforeFinalPayment = obStandingOrder.getNextPaymentDateTime().isBefore(obStandingOrder.getFinalPaymentDateTime());
                log.debug("Standing order '{}', status: '{}', isFirstPaymentDue {} , isFinalPaymentDue {} , isNextPaymentDue: {}, isBeforeFinalPayment: {}",
                        frStandingOrder.getId(), frStandingOrder.getStatus(), isFirstPaymentDue, isFinalPaymentDue, isNextRecurringPaymentDue, isBeforeFinalPayment);

                if (StandingOrderStatus.ACTIVE == frStandingOrder.getStatus()
                        && isNextRecurringPaymentDue
                        && isBeforeFinalPayment)
                {
                    log.info("Active standing order '{}' has passed recurring payment date but not reached final payment date - make payment and calculate next recurring payment", frStandingOrder.getId());
                    doCreditAndDebitPayment(frStandingOrder, toFRAmount(obStandingOrder.getNextPaymentAmount()));
                    frStandingOrder.getStandingOrder().setNextPaymentDateTime(frequencyService.getNextDateTime(obStandingOrder.getNextPaymentDateTime(), obStandingOrder.getFrequency()));
                }
                else if (StandingOrderStatus.ACTIVE == frStandingOrder.getStatus()
                        && isFinalPaymentDue)
                {
                    log.info("Active standing order '{}' has passed final payment date - make final payment and set to COMPLETE", frStandingOrder.getId());
                    doCreditAndDebitPayment(frStandingOrder, toFRAmount(obStandingOrder.getFinalPaymentAmount()));
                    frStandingOrder.setStatus(StandingOrderStatus.COMPLETED);
                }
                else if (StandingOrderStatus.PENDING == frStandingOrder.getStatus()
                        && isFirstPaymentDue)
                {
                    log.info("Pending standing order '{}' has passed start payment date - make first payment and set to active", frStandingOrder.getId());
                    doCreditAndDebitPayment(frStandingOrder, toFRAmount(obStandingOrder.getFirstPaymentAmount()));
                    frStandingOrder.setStatus(StandingOrderStatus.ACTIVE);
                } else {
                    log.debug("Active standing order '{}' is not due for payment", frStandingOrder.getId());
                }
            } catch (CurrencyConverterException e) {
                log.error("Can't convert amount in the right currency", e);
                log.error("Update payment status to rejected");
                frStandingOrder.setRejectionReason("Can't convert amount in the right currency: "+e.getMessage());
                frStandingOrder.setStatus(StandingOrderStatus.REJECTED);
                log.info("Rejected payment {}", obStandingOrder);
            } catch (Exception e) {
                log.error("Couldn't pay standing order payment.", e);
                log.error("Update payment status to rejected");
                frStandingOrder.setRejectionReason("Failed to execute payment: "+e.getMessage());
                frStandingOrder.setStatus(StandingOrderStatus.REJECTED);
                log.info("Rejected payment {}", obStandingOrder);
            } finally {
                standingOrderService.updateStandingOrder(frStandingOrder);
                paymentNotificationService.paymentStatusChanged(frStandingOrder);
            }
        }
        log.info("All due standing order payments are now completed. The time is now {}.",
                format.print(DateTime.now()));

    }

    private void doCreditAndDebitPayment(FRStandingOrder frStandingOrder, FRAmount amount) throws Exception {
        moveDebitPayment(frStandingOrder, amount);

        String identificationFrom = frStandingOrder.getStandingOrder().getCreditorAccount().getIdentification();
        log.debug("Find if the 'to' account '{}' is own by this ASPSP", identificationFrom);
        Optional<Account> isAccountFromOurs = accountStoreService.findAccountByIdentification(identificationFrom);
        if (isAccountFromOurs.isPresent()) {
            moveCreditPayment(frStandingOrder, identificationFrom, isAccountFromOurs.get(), amount);
        } else {
            log.info("Account '{}' not ours", identificationFrom);
        }
        log.debug("Payment {}", frStandingOrder.getStandingOrder());
    }

    private void moveDebitPayment(FRStandingOrder payment, FRAmount amount) throws CurrencyConverterException {
        Account accountTo = accountStoreService.getAccount(payment.getAccountId());
        log.info("We are going to pay from this account: {}", accountTo);
        moneyService.moveMoney(accountTo, amount, OBCreditDebitCode.DEBIT, payment, this::createTransaction);
    }

    private void moveCreditPayment(FRStandingOrder payment, String identificationFrom, Account accountFrom, FRAmount amount) throws CurrencyConverterException {
        log.info("Account '{}' is ours: {}", identificationFrom, accountFrom);
        log.info("Move the money to this account");
        moneyService.moveMoney(accountFrom, amount, OBCreditDebitCode.CREDIT, payment, this::createTransaction);
    }

    private FRTransaction createTransaction(Account account, FRStandingOrder payment, OBCreditDebitCode creditDebitCode, Balance balance, FRAmount amount) {
        log.debug("Create transaction");
        String transactionId = UUID.randomUUID().toString();
        DateTime bookingDate = new DateTime(payment.getCreated());

        OBTransaction6 obTransaction = new OBTransaction6()
                .transactionId(transactionId)
                .status(OBEntryStatus1Code.BOOKED)
                .valueDateTime(DateTime.now())
                .accountId(account.getId())
                .amount(toOBActiveOrHistoricCurrencyAndAmount9(amount))
                .creditDebitIndicator(creditDebitCode == null ? null : OBCreditDebitCode1.valueOf(creditDebitCode.name()))
                .bookingDateTime(bookingDate)
                .statementReference(new ArrayList<>())
                .balance(new OBTransactionCashBalance()
                        .amount(balance.getCurrencyAndAmount())
                        .creditDebitIndicator(balance.getCreditDebitIndicator())
                        .type(OBBalanceType1Code.INTERIMBOOKED)
                );

        if (payment.getStandingOrder().getReference() != null) {
            obTransaction.transactionReference(payment.getStandingOrder().getReference());
        }

        FRTransaction transaction = FRTransaction.builder()
                .id(transactionId)
                .bookingDateTime(bookingDate)
                .accountId(account.getId())
                .transaction(obTransaction)
                .build();
        log.info("Transaction created {}", transaction);
        return transaction;
    }
}
