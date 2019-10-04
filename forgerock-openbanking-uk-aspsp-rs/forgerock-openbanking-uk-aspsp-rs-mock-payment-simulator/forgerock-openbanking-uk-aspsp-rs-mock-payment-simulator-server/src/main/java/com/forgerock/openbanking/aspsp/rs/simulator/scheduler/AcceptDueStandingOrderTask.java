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
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRBalance;
import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRStandingOrder5;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRTransaction5;
import com.forgerock.openbanking.common.services.openbanking.converter.OBActiveOrHistoricCurrencyAndAmountConverter;
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
        for (FRStandingOrder5 frStandingOrder : standingOrderService.getActiveStandingOrders()) {
            OBStandingOrder5 obStandingOrder = frStandingOrder.getStandingOrder();

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
                    doCreditAndDebitPayment(frStandingOrder, (OBActiveOrHistoricCurrencyAndAmountConverter.toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getNextPaymentAmount())));
                    frStandingOrder.getStandingOrder().setNextPaymentDateTime(frequencyService.getNextDateTime(obStandingOrder.getNextPaymentDateTime(), obStandingOrder.getFrequency()));
                }
                else if (StandingOrderStatus.ACTIVE == frStandingOrder.getStatus()
                        && isFinalPaymentDue)
                {
                    log.info("Active standing order '{}' has passed final payment date - make final payment and set to COMPLETE", frStandingOrder.getId());
                    doCreditAndDebitPayment(frStandingOrder, OBActiveOrHistoricCurrencyAndAmountConverter.toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getFinalPaymentAmount()));
                    frStandingOrder.setStatus(StandingOrderStatus.COMPLETED);
                }
                else if (StandingOrderStatus.PENDING == frStandingOrder.getStatus()
                        && isFirstPaymentDue)
                {
                    log.info("Pending standing order '{}' has passed start payment date - make first payment and set to active", frStandingOrder.getId());
                    doCreditAndDebitPayment(frStandingOrder, OBActiveOrHistoricCurrencyAndAmountConverter.toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getFirstPaymentAmount()));
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

    private void doCreditAndDebitPayment(FRStandingOrder5 frStandingOrder, OBActiveOrHistoricCurrencyAndAmount amount) throws Exception {
        moveDebitPayment(frStandingOrder, amount);

        String identificationFrom = frStandingOrder.getStandingOrder().getCreditorAccount().getIdentification();
        log.debug("Find if the 'to' account '{}' is own by this ASPSP", identificationFrom);
        Optional<FRAccount> isAccountFromOurs = accountStoreService.findAccountByIdentification(identificationFrom);
        if (isAccountFromOurs.isPresent()) {
            moveCreditPayment(frStandingOrder, identificationFrom, isAccountFromOurs.get(), amount);
        } else {
            log.info("Account '{}' not ours", identificationFrom);
        }
        log.debug("Payment {}", frStandingOrder.getStandingOrder());
    }

    private void moveDebitPayment(FRStandingOrder5 payment, OBActiveOrHistoricCurrencyAndAmount amount) throws CurrencyConverterException {
        FRAccount accountTo = accountStoreService.getAccount(payment.getAccountId());
        log.info("We are going to pay from this account: {}", accountTo);
        moneyService.moveMoney(accountTo, amount,
                OBCreditDebitCode.DEBIT, payment, this::createTransaction);
    }

    private void moveCreditPayment(FRStandingOrder5 payment, String identificationFrom, FRAccount accountFrom, OBActiveOrHistoricCurrencyAndAmount amount) throws CurrencyConverterException {
        log.info("Account '{}' is ours: {}", identificationFrom, accountFrom);
        log.info("Move the money to this account");
        moneyService.moveMoney(accountFrom, amount,
                OBCreditDebitCode.CREDIT, payment, this::createTransaction);
    }

    private FRTransaction5 createTransaction(FRAccount account, FRStandingOrder5 payment, OBCreditDebitCode creditDebitCode, FRBalance balance, OBActiveOrHistoricCurrencyAndAmount amount) {
        log.debug("Create transaction");
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

        if (payment.getStandingOrder().getReference() != null) {
            obTransaction.transactionReference(payment.getStandingOrder().getReference());
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
