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
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRBalance;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.FRFilePayment;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.account.FRTransaction6;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRFileConsent5;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.payment.FilePaymentService;
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
import uk.org.openbanking.datamodel.account.OBTransaction6;
import uk.org.openbanking.datamodel.account.OBTransactionCashBalance;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.simulator.constants.SimulatorConstants.RUN_SCHEDULED_TASK_PROPERTY;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount9;
import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;

@Slf4j
@Component
@ConditionalOnProperty(name = RUN_SCHEDULED_TASK_PROPERTY, matchIfMissing = true)
public class AcceptFilePaymentTask {

    private final static DateTimeFormatter format = DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT);

    private final FilePaymentService filePaymentService;
    private final AccountStoreService accountStoreService;
    private final MoneyService moneyService;
    private final PaymentNotificationFacade paymentNotificationService;

    public AcceptFilePaymentTask(FilePaymentService filePaymentService, AccountStoreService accountStoreService, MoneyService moneyService, PaymentNotificationFacade paymentNotificationService) {
        this.filePaymentService = filePaymentService;
        this.accountStoreService = accountStoreService;
        this.moneyService = moneyService;
        this.paymentNotificationService = paymentNotificationService;
    }

    @Scheduled(fixedRate = 60 * 1000 * 5)
    @SchedulerLock(name = "filePayment")
    public void autoAcceptPayment() {
        log.info("Auto-accept file payment task waking up. The time is now {}.", format.print(DateTime.now()));
        final Collection<FRFileConsent5> allPaymentsInProcess = filePaymentService.getAllPaymentFilesInProcess();

        for (FRFileConsent5 consent: allPaymentsInProcess) {
            log.info("Processing file consent {}", consent);
            try {
                int paymentNo = 0;
                int success = 0;
                int reject = 0;
                if (consent.getPayments()==null) {
                    consent.setStatus(ConsentStatusCode.REJECTED);
                    continue;
                }

                for (FRFilePayment payment : consent.getPayments()) {
                    paymentNo++;
                    try {
                        if (payment.getStatus()!= FRFilePayment.PaymentStatus.PENDING) {
                            log.debug("Payment '{}' from consent '{}' is not pending", payment, consent.getId());
                            continue;
                        }
                        log.info("Processing pending file payment [{}] : {}", paymentNo, payment);
                        String identificationTo = moveDebitPayment(payment, consent.getAccountId());
                        Optional<FRAccount> isAccountToFromOurs = accountStoreService.findAccountByIdentification(identificationTo);
                        if (isAccountToFromOurs.isPresent()) {
                            moveCreditPayment(payment, identificationTo, isAccountToFromOurs.get());
                        } else {
                            log.info("Account '{}' not ours", identificationTo);
                        }
                        log.debug("File payment [{}] succeeded: {}", paymentNo, payment);
                        success++;
                        payment.setStatus(FRFilePayment.PaymentStatus.COMPLETED);
                    } catch (CurrencyConverterException e) {
                        log.info("Can't convert amount in the right currency for payment within a file. Payment: {}, ConsentId: {}", payment, consent.getId(), e);
                        log.info("Update individual payment status to rejected - other payments in file may still succeed");
                        reject++;
                        payment.setStatus(FRFilePayment.PaymentStatus.REJECTED);
                    } catch (Exception e) {
                        log.warn("An individual payment within a file failed. Payment: {}, ConsentId: {}", payment, consent.getId(), e);
                        log.info("Update individual payment status to rejected - other payments in file may still succeed");
                        reject++;
                        payment.setStatus(FRFilePayment.PaymentStatus.REJECTED);
                    }
                }
                log.info("Finished file payments for consent: '{}'. {} payments succeeded. {} payments were rejected. Update file consent status to completed", consent.getId(), success, reject);
                consent.setStatus(ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);
                log.debug("Consent {}", consent);
            } finally {
                filePaymentService.updatePayment(consent);
                paymentNotificationService.paymentStatusChanged(consent);
            }
        }
        log.info("All file payments in process are now accepted. See you in 5 minutes! The time is now {}.",
                format.print(DateTime.now()));
    }

    private String moveDebitPayment(FRFilePayment payment, String accountId) throws CurrencyConverterException {
        FRAccount accountFrom = accountStoreService.getAccount(accountId);
        log.info("We are going to pay from this account: {}", accountFrom);
        moneyService.moveMoney(accountFrom, payment.getInstructedAmount(),
                OBCreditDebitCode.DEBIT, payment, this::createTransaction);

        String identificationFrom = payment.getCreditorAccountIdentification();
        log.debug("Find if the 'to' account '{}' is own by this ASPSP", identificationFrom);
        return identificationFrom;
    }

    private void moveCreditPayment(FRFilePayment payment, String identificationTo, FRAccount accountFrom) throws CurrencyConverterException {
        log.info("Account '{}' is ours: {}", identificationTo, accountFrom);
        log.info("Move the money to this account");
        moneyService.moveMoney(accountFrom, payment.getInstructedAmount(),
                OBCreditDebitCode.CREDIT, payment, this::createTransaction);
    }

    private FRTransaction6 createTransaction(FRAccount account, FRFilePayment payment, OBCreditDebitCode creditDebitCode, FRBalance balance, OBActiveOrHistoricCurrencyAndAmount amount) {
        log.info("Create transaction");
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
            obTransaction
                    .transactionReference(payment.getRemittanceReference())
                    .transactionInformation(payment.getRemittanceUnstructured());

        FRTransaction6 transaction = FRTransaction6.builder()
                .id(transactionId)
                .bookingDateTime(bookingDate)
                .accountId(account.getId())
                .transaction(obTransaction)
                .build();
        log.info("Transaction created {}", transaction);
        return transaction;
    }
}
