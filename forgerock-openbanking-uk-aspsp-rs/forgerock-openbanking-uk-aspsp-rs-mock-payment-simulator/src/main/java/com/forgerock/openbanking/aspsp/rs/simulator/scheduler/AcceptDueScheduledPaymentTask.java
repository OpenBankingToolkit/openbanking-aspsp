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
import com.forgerock.openbanking.commons.model.openbanking.forgerock.FRAccount;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.FRBalance;
import com.forgerock.openbanking.commons.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRScheduledPayment1;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRTransaction5;
import com.forgerock.openbanking.commons.services.openbanking.converter.OBActiveOrHistoricCurrencyAndAmountConverter;
import com.forgerock.openbanking.commons.services.store.account.AccountStoreService;
import com.forgerock.openbanking.commons.services.store.account.scheduledpayment.ScheduledPaymentService;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.org.openbanking.datamodel.account.*;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.simulator.SimulatorApplication.RUN_SCHEDULED_TASK_PROPERTY;
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

        for (FRScheduledPayment1 scheduledPayment : scheduledPaymentService.getPendingAndDueScheduledPayments()) {
            OBScheduledPayment1 payment = scheduledPayment.getScheduledPayment();

            if (!payment.getScheduledPaymentDateTime().isBefore(DateTime.now())) {
                log.warn("Scheduled payment: {} with scheduledPaymentDateTime: {} is not due yet and should not have been loaded", scheduledPayment.getId(), payment.getScheduledPaymentDateTime());
                continue;
            }

            log.info("Processing scheduled payment {}", payment);
            try {
                String identificationFrom = moveDebitPayment(scheduledPayment);
                Optional<FRAccount> isAccountFromOurs = accountStoreService.findAccountByIdentification(identificationFrom);
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
                scheduledPayment.setRejectionReason("Can't convert amount in the right currency: "+e.getMessage());
                scheduledPayment.setStatus(ScheduledPaymentStatus.REJECTED);
                log.info("Payment {}", payment);
            } catch (Exception e) {
                log.error("Couldn't pay scheduled payment.", e);
                log.info("Update payment status to rejected");
                scheduledPayment.setRejectionReason("Failed to execute payment: "+e.getMessage());
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

    private String moveDebitPayment(FRScheduledPayment1 payment) throws CurrencyConverterException {
        FRAccount accountFrom = accountStoreService.getAccount(payment.getAccountId());
        log.info("We are going to pay from this account: {}", accountFrom);
        moneyService.moveMoney(accountFrom, payment.getScheduledPayment().getInstructedAmount(),
                OBCreditDebitCode.DEBIT, payment, this::createTransaction);

        String identificationFrom = payment.getScheduledPayment().getCreditorAccount().getIdentification();
        log.debug("Find if the 'to' account '{}' is own by this ASPSP", identificationFrom);
        return identificationFrom;
    }

    private void moveCreditPayment(FRScheduledPayment1 payment, String identificationTo, FRAccount accountFrom) throws CurrencyConverterException {
        log.info("Account '{}' is ours: {}", identificationTo, accountFrom);
        log.info("Move the money to this account");
        moneyService.moveMoney(accountFrom, payment.getScheduledPayment().getInstructedAmount(),
                OBCreditDebitCode.CREDIT, payment, this::createTransaction);
    }

    private FRTransaction5 createTransaction(FRAccount account, FRScheduledPayment1 payment, OBCreditDebitCode creditDebitCode, FRBalance balance, OBActiveOrHistoricCurrencyAndAmount amount) {
        log.info("Create transaction");
        String transactionId = UUID.randomUUID().toString();
        DateTime bookingDate = new DateTime(payment.getCreated());

        OBTransaction5 obTransaction = new OBTransaction5()
                .transactionId(transactionId)
                .status(OBEntryStatus1Code.BOOKED)
                .valueDateTime(DateTime.now())
                .accountId(account.getId())
                .amount(OBActiveOrHistoricCurrencyAndAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount(amount))
                .creditDebitIndicator(OBTransaction5.CreditDebitIndicatorEnum.fromValue(creditDebitCode.toString()))
                .bookingDateTime(bookingDate)
                .statementReference(new ArrayList<>())
                .balance(new OBTransactionCashBalance()
                        .amount(balance.getCurrencyAndAmount())
                        .creditDebitIndicator(balance.getCreditDebitIndicator())
                        .type(OBBalanceType1Code.INTERIMBOOKED)
                );

        if (payment.getScheduledPayment().getReference() != null) {
            obTransaction.transactionReference(payment.getScheduledPayment().getReference());
        }

        FRTransaction5 transaction = FRTransaction5.builder()
                .id(transactionId)
                .bookingDateTime(bookingDate)
                .accountId(account.getId())
                .transaction(obTransaction)
                .build();
        log.info("Transaction created {}", transaction);
        return transaction;
    }
}
