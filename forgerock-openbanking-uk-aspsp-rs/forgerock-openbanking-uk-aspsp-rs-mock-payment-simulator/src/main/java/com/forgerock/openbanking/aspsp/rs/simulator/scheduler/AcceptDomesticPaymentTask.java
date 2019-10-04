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
import com.forgerock.openbanking.commons.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.FRAccount;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.FRBalance;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRDomesticConsent2;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRTransaction5;
import com.forgerock.openbanking.commons.services.openbanking.converter.OBActiveOrHistoricCurrencyAndAmountConverter;
import com.forgerock.openbanking.commons.services.store.account.AccountStoreService;
import com.forgerock.openbanking.commons.services.store.payment.DomesticPaymentService;
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
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.simulator.SimulatorApplication.RUN_SCHEDULED_TASK_PROPERTY;
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
        Collection<FRDomesticConsent2> allPaymentsInProcess = domesticPaymentsService.getAllPaymentsInProcess();
        for (FRDomesticConsent2 payment: allPaymentsInProcess) {
            log.info("Processing payment {}", payment);
            try {
                String identificationTo = moveDebitPayment(payment);
                Optional<FRAccount> isAccountToFromOurs = accountStoreService.findAccountByIdentification(identificationTo);
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

    private String moveDebitPayment(FRDomesticConsent2 payment) throws CurrencyConverterException {
        FRAccount accountFrom = accountStoreService.getAccount(payment.getAccountId());
        log.info("We are going to pay from this account: {}", accountFrom);
        moneyService.moveMoney(accountFrom, payment.getInitiation().getInstructedAmount(),
                OBCreditDebitCode.DEBIT, payment, this::createTransaction);

        String identificationFrom = payment.getInitiation().getCreditorAccount().getIdentification();
        log.debug("Find if the 'to' account '{}' is own by this ASPSP", identificationFrom);
        return identificationFrom;
    }

    private void moveCreditPayment(FRDomesticConsent2 payment, String identificationTo, FRAccount accountFrom) throws CurrencyConverterException {
        log.info("Account '{}' is ours: {}", identificationTo, accountFrom);
        log.info("Move the money to this account");
        moneyService.moveMoney(accountFrom, payment.getInitiation().getInstructedAmount(),
                OBCreditDebitCode.CREDIT, payment, this::createTransaction);
    }

    private FRTransaction5 createTransaction(FRAccount account, FRDomesticConsent2 payment, OBCreditDebitCode creditDebitCode, FRBalance balance, OBActiveOrHistoricCurrencyAndAmount amount) {
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

        if (payment.getInitiation().getRemittanceInformation() != null) {
            obTransaction
                    .transactionReference(payment.getInitiation().getRemittanceInformation().getReference())
                    .transactionInformation(payment.getInitiation().getRemittanceInformation().getUnstructured());
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
