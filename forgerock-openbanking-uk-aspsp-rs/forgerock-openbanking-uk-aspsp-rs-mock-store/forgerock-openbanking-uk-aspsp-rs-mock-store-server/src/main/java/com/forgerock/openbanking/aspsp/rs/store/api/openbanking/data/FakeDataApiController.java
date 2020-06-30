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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.offers.FROffer1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.products.FRProduct2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.party.FRParty2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.standingorders.FRStandingOrder5Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.transactions.FRTransaction5Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.accounts.FRAccount4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.beneficiaries.FRBeneficiary4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.directdebits.FRDirectDebit4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.scheduledpayments.FRScheduledPayment4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.statements.FRStatement4Repository;
import com.forgerock.openbanking.common.conf.data.DataConfigurationProperties;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBalance1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FROffer1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRProduct2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRParty2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRStandingOrder5;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRTransaction5;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRAccount4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRBeneficiary4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRDirectDebit4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRScheduledPayment4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRStatement4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.data.FRUserData4;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.*;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.payment.OBExternalAccountIdentification2Code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Controller("FakeDataApi")
public class FakeDataApiController implements FakeDataApi {
    private final static Logger LOGGER = LoggerFactory.getLogger(FakeDataApiController.class);
    private static final String RANDOM_PROFILE_ID = "random";

    private static final String GBP = "GBP";
    private static final String EUR = "EUR";
    private static final String COMPANIES_CSV = "companies.csv";
    private static final String NAMES_CSV = "names.csv";

    public static final String STATEMENT_DATE_FORMAT = "yyyy-MM";
    public static final String STATEMENT_HUMAN_DATE_FORMAT = "MMM yyyy";
    private final static DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(STATEMENT_DATE_FORMAT);
    private final static DateTimeFormatter FORMATTER_HUMAN = DateTimeFormat.forPattern(STATEMENT_HUMAN_DATE_FORMAT);

    private final static NumberFormat FORMAT_AMOUNT = new DecimalFormat("#0.00");

    @Autowired
    private FRAccount4Repository accountsRepository;
    @Autowired
    private FRBalance1Repository balanceRepository;
    @Autowired
    private FRBeneficiary4Repository beneficiaryRepository;
    @Autowired
    private FRDirectDebit4Repository directDebitRepository;
    @Autowired
    private FRProduct2Repository productRepository;
    @Autowired
    private FRStandingOrder5Repository standingOrderRepository;
    @Autowired
    private FRTransaction5Repository transactionRepository;
    @Autowired
    private FRStatement4Repository statement1Repository;
    @Autowired
    private FRScheduledPayment4Repository scheduledPaymentRepository;
    @Autowired
    private FRParty2Repository partyRepository;
    @Autowired
    private FROffer1Repository offer1Repository;
    @Autowired
    private DataApiController data2Controller;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private DataApiController dataApiController;
    @Autowired
    private DataConfigurationProperties dataConfig;


    private List<String> companies;
    private List<String> names;

    public FakeDataApiController() throws IOException {
        companies = loadCSV(new ClassPathResource(COMPANIES_CSV));
        names = loadCSV(new ClassPathResource(NAMES_CSV));
    }

    @Override
    public ResponseEntity generateFakeData(
            @RequestParam("userId") String userId,
            @RequestParam("username") String username,
            @RequestParam(name = "profile", required = false) String profile
    ) throws OBErrorException {
        if (RANDOM_PROFILE_ID.equals(profile)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(generateRandomData(userId, username));
        } else {
            Optional<DataConfigurationProperties.DataTemplateProfile> any =
                    dataConfig.getProfiles().stream().filter(t -> t.getId().equals(profile)).findAny();
            if (!any.isPresent()) {
                throw new OBErrorException(OBRIErrorType.DATA_INVALID_REQUEST,
                        "Profile '" + profile + "' doesn't exist."
                );
            }
            DataConfigurationProperties.DataTemplateProfile dataTemplateProfile = any.get();
            FRUserData4 template = getTemplate(dataTemplateProfile.getTemplate(), username);
            template.setUserName(username);
            return dataApiController.importUserData(template);
        }
    }

    public FRUserData4 getTemplate(Resource template, String username) {
        try {
            String content = StreamUtils.copyToString(template.getInputStream(), Charset.defaultCharset());
            content = content.replaceAll("$username", username);
            return mapper.readValue(content, FRUserData4.class);
        } catch (IOException e) {
            LOGGER.error("Can't read registration request resource", e);
            throw new RuntimeException(e);
        }
    }

    public FRUserData4 generateRandomData(String userId, String username)
     {
        LOGGER.debug("Generate data for user '{}'", userId);

        if (accountsRepository.findByUserID(userId).size() > 0 ) {
            LOGGER.debug("User {} already have some data", userId);
        }
        {
            Integer sortCode = ThreadLocalRandom.current().nextInt(0, 999999);
            Integer accountNumber = ThreadLocalRandom.current().nextInt(0, 99999999);

            String accountId = UUID.randomUUID().toString();
            FRAccount4 accountPremierBank = new FRAccount4();
            accountPremierBank.setCreated(new Date());
            accountPremierBank.setId(accountId);
            accountPremierBank.setUserID(userId);
            accountPremierBank.setAccount(new OBAccount6()
                    .accountId(accountId)
                    .accountType(OBExternalAccountType1Code.PERSONAL)
                    .accountSubType(OBExternalAccountSubType1Code.CURRENTACCOUNT)
                    .currency(GBP)
                    .nickname("UK Bills")
                    .status(OBAccountStatus1Code.ENABLED)
                    .statusUpdateDateTime(DateTime.now())
                    .openingDate(DateTime.now().minusDays(1))
                    .maturityDate(DateTime.now().plusDays(1))
                    .account(Collections.singletonList(new OBAccount3Account()
                            .schemeName(OBExternalAccountIdentification4Code.SORTCODEACCOUNTNUMBER.toString())
                            .identification(sortCode.toString() + accountNumber.toString())
                            .name(username)
                            .secondaryIdentification(ThreadLocalRandom.current().nextInt(0, 99999999) + "")))
            );

            LOGGER.debug("Account '{}' generated for user '{}'", accountPremierBank, userId);
            accountsRepository.save(accountPremierBank);
            generateAccountData(accountPremierBank);
            generateParty(accountPremierBank, username);
            generateOfferLimitIncrease(accountPremierBank);
            generateOfferBalanceTransfer(accountPremierBank);
        }
         {
             Integer sortCode = ThreadLocalRandom.current().nextInt(0, 999999);
             Integer accountNumber = ThreadLocalRandom.current().nextInt(0, 99999999);

             String accountId = UUID.randomUUID().toString();
             FRAccount4 accountPremierBank = new FRAccount4();
             accountPremierBank.setId(accountId);
             accountPremierBank.setCreated(new Date());
             accountPremierBank.setUserID(userId);
             accountPremierBank.setAccount(new OBAccount6()
                     .accountId(accountId)
                     .accountType(OBExternalAccountType1Code.PERSONAL)
                     .accountSubType(OBExternalAccountSubType1Code.CURRENTACCOUNT)
                     .currency(EUR)
                     .nickname("FR Bills")
                     .status(OBAccountStatus1Code.ENABLED)
                     .statusUpdateDateTime(DateTime.now())
                     .openingDate(DateTime.now().minusDays(1))
                     .maturityDate(DateTime.now().plusDays(1))
                     .account(Collections.singletonList(new OBAccount3Account()
                             .schemeName(OBExternalAccountIdentification4Code.SORTCODEACCOUNTNUMBER.toString())
                             .identification(sortCode.toString() + accountNumber.toString())
                             .name(username)
                             .secondaryIdentification(ThreadLocalRandom.current().nextInt(0, 99999999) + "")))
             );

             LOGGER.debug("Account '{}' generated for user '{}'", accountPremierBank, userId);
             accountsRepository.save(accountPremierBank);
             generateAccountData(accountPremierBank);
             generateParty(accountPremierBank, username);
             generateOfferLimitIncrease(accountPremierBank);
             generateOfferBalanceTransfer(accountPremierBank);
         }
        {
            Integer sortCode = ThreadLocalRandom.current().nextInt(0, 999999);
            Integer accountNumber = ThreadLocalRandom.current().nextInt(0, 99999999);

            String accountId = UUID.randomUUID().toString();
            FRAccount4 accountPremierCard = new FRAccount4();
            accountPremierCard.setCreated(new Date());
            accountPremierCard.setId(accountId);
            accountPremierCard.setUserID(userId);
            accountPremierCard.setAccount(new OBAccount6()
                    .accountId(accountId)
                    .accountType(OBExternalAccountType1Code.PERSONAL)
                    .accountSubType(OBExternalAccountSubType1Code.CURRENTACCOUNT)
                    .currency(GBP)
                    .nickname("Household")
                    .status(OBAccountStatus1Code.ENABLED)
                    .statusUpdateDateTime(DateTime.now())
                    .openingDate(DateTime.now().minusDays(1))
                    .maturityDate(DateTime.now().plusDays(1))
                    .account(Collections.singletonList(new OBAccount3Account()
                            .schemeName(OBExternalAccountIdentification4Code.SORTCODEACCOUNTNUMBER.toString())
                            .identification(sortCode.toString() + accountNumber.toString())
                            .name(username))
                    )
            );
            LOGGER.debug("Account '{}' generated for user '{}'", accountPremierCard, userId);
            accountsRepository.save(accountPremierCard);
            generateAccountData(accountPremierCard);
            generateParty(accountPremierCard, username);
            generateOfferLimitIncrease(accountPremierCard);
        }

        generateGlobalParty(userId, username);

        return data2Controller.exportUserData(userId).getBody();
     }

    private void generateAccountData(FRAccount4 account) {
        FRBalance1 balance = generateBalance(account, OBCreditDebitCode.DEBIT, null);
        int nbBeneficiaries = ThreadLocalRandom.current().nextInt(2,8);
        int nbDirectDebits = ThreadLocalRandom.current().nextInt(2,8);
        int nbStandingOrders = ThreadLocalRandom.current().nextInt(2,8);
        int nbScheduledPayment = ThreadLocalRandom.current().nextInt(2,8);

        LOGGER.debug("Generate {} beneficiaries", nbBeneficiaries);
        List<FRBeneficiary4> beneficiarys = new ArrayList<>();
        for (int i = 0; i < nbBeneficiaries; i++) {
            beneficiarys.add(generateBeneficiary(account));
        }

        LOGGER.debug("Generate {} direct debits", nbDirectDebits);
        List<FRDirectDebit4> directDebit1s = new ArrayList<>();
        for (int i = 0; i < nbDirectDebits; i++) {
            directDebit1s.add(generateDirectDebit(account));
        }
        FRProduct2 product2 = generateProduct(account);

        LOGGER.debug("Generate {} standing orders", nbStandingOrders);
        List<FRStandingOrder5> standingOrder3s = new ArrayList<>();
        for (int i = 0; i < nbStandingOrders; i++) {
            standingOrder3s.add(generateStandingOrder(account));
        }

        LOGGER.debug("Generate statements");
        List<FRStatement4> statements = new ArrayList<>();
        List<FRTransaction5> transactions = new ArrayList<>();
        DateTime currentMonth = DateTime.now().dayOfMonth().withMinimumValue().minusMonths(12);
        for (int i = 12; i > 0; i-- ) {
            LOGGER.debug("Month: {}", FORMATTER.print(currentMonth));
            FRStatement4 statement1 = generateStatements(account, balance, currentMonth);
            statements.add(statement1);
            currentMonth = currentMonth.plusMonths(1);
            transactions.addAll(generateTransactions(account, statement1, balance));
            updateStatement(statement1, balance);
        }
        LOGGER.debug("Month: {}", FORMATTER.print(currentMonth));
        FRStatement4 statement1 = generateStatements(account, balance, currentMonth);
        account.setLatestStatementId(statement1.getId());

        LOGGER.debug("Generate {} standing orders", nbScheduledPayment);
        List<FRScheduledPayment4> scheduledPayments = new ArrayList<>();
        for (int i = 0; i < nbScheduledPayment; i++) {
            scheduledPayments.add(generateScheduledPayment(account));
        }

        beneficiaryRepository.saveAll(beneficiarys);
        directDebitRepository.saveAll(directDebit1s);
        scheduledPaymentRepository.saveAll(scheduledPayments);
        standingOrderRepository.saveAll(standingOrder3s);
        statement1Repository.saveAll(statements);
        transactionRepository.saveAll(transactions);
        productRepository.save(product2);
        balanceRepository.save(balance);

        accountsRepository.save(account);
    }

    private FRBalance1 generateBalance(FRAccount4 account, OBCreditDebitCode obCreditDebitCode, List<OBCreditLine1> creditLine) {
        Double amount = generateAmount(1000.0d, 10000.0d);
        FRBalance1 balance = new FRBalance1();
        balance.setAccountId(account.getId());
        balance.setBalance(new OBCashBalance1()
                .accountId(account.getId())
                .amount(new OBActiveOrHistoricCurrencyAndAmount().amount(FORMAT_AMOUNT.format(amount)).currency(account.getAccount().getCurrency()))
                .creditDebitIndicator(obCreditDebitCode)
                .type(OBBalanceType1Code.INTERIMAVAILABLE)
                .dateTime(DateTime.now())
                .creditLine(creditLine)
        );
        LOGGER.debug("FRBalance1 '{}' generated", balance);
        return balance;
    }

    private FRBeneficiary4 generateBeneficiary(FRAccount4 account) {
        FRBeneficiary4 beneficiary = new FRBeneficiary4();
        beneficiary.setAccountId(account.getId());
        Integer sortCode = ThreadLocalRandom.current().nextInt(0, 999999);
        Integer accountNumber = ThreadLocalRandom.current().nextInt(0, 99999999);
        String company = companies.get(ThreadLocalRandom.current().nextInt(companies.size()));
        String name = names.get(ThreadLocalRandom.current().nextInt(names.size()));

        beneficiary.setBeneficiary(new OBBeneficiary4()
                .accountId(account.getId())
                .beneficiaryId(UUID.randomUUID().toString())
                .reference(company)
                .creditorAccount(new OBCashAccount50()
                        .schemeName(OBExternalAccountIdentification2Code.SortCodeAccountNumber.getReference())
                        .identification(sortCode.toString() + accountNumber.toString())
                        .name(name)
                )
        );
        beneficiary.setId(beneficiary.getBeneficiary().getBeneficiaryId());
        LOGGER.debug("FRBeneficiary1 '{}' generated", beneficiary);
        return beneficiary;
    }

    private FRDirectDebit4 generateDirectDebit(FRAccount4 account) {
        String company = companies.get(ThreadLocalRandom.current().nextInt(companies.size()));

        Double amount = generateAmount(10.0d, 500.0d);
        FRDirectDebit4 directDebit = new FRDirectDebit4();
        directDebit.setAccountId(account.getId());
        directDebit.setDirectDebit(new OBReadDirectDebit2DataDirectDebit()
                .accountId(account.getId())
                .directDebitId(UUID.randomUUID().toString())
                .mandateIdentification(company.trim())
                .directDebitStatusCode(OBExternalDirectDebitStatus1Code.ACTIVE)
                .name(company)
                .previousPaymentDateTime(DateTime.now().minusMonths(1))
                .previousPaymentAmount(new OBActiveOrHistoricCurrencyAndAmount0().amount(FORMAT_AMOUNT.format(amount))
                        .currency(account.getAccount().getCurrency()))
        );
        directDebit.setId(directDebit.getDirectDebit().getDirectDebitId());

        LOGGER.debug("Direct debit '{}' generated", directDebit);
        return directDebit;
    }

    private FRProduct2 generateProduct(FRAccount4 account) {
        FRProduct2 product = new FRProduct2();
        product.setAccountId(account.getId());
        product.setProduct(new OBReadProduct2DataProduct()
                .accountId(account.getId())
                .productId(UUID.randomUUID().toString())
                .productType(OBReadProduct2DataProduct.ProductTypeEnum.PERSONALCURRENTACCOUNT)
                .productName("321 Product")
        );
        product.setId(product.getProduct().getProductId());
        LOGGER.debug("FRProduct1 '{}' generated", product);
        productRepository.save(product);
        return product;
    }

    private FRStandingOrder5 generateStandingOrder(FRAccount4 account) {
        Double amount = generateAmount(10.0d, 500.0d);

        Integer sortCode = ThreadLocalRandom.current().nextInt(0, 999999);
        Integer accountNumber = ThreadLocalRandom.current().nextInt(0, 99999999);

        String company = companies.get(ThreadLocalRandom.current().nextInt(companies.size()));
        String name = names.get(ThreadLocalRandom.current().nextInt(names.size()));

        FRStandingOrder5 standingOrder = new FRStandingOrder5();
        standingOrder.setAccountId(account.getId());
        standingOrder.setStandingOrder(new OBStandingOrder5()
                .accountId(account.getId())
                .standingOrderId(UUID.randomUUID().toString())
                .standingOrderStatusCode(OBExternalStandingOrderStatus1Code.ACTIVE)
                .frequency("EvryWorkgDay")
                .reference(company)
                .firstPaymentDateTime(DateTime.now().minusYears(1))
                .firstPaymentAmount(new uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount()
                        .amount(FORMAT_AMOUNT.format(amount))
                        .currency(account.getAccount().getCurrency()))
                .nextPaymentDateTime(DateTime.now().plusMonths(2))
                .nextPaymentAmount(new uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount().amount(FORMAT_AMOUNT.format(amount))
                        .currency(account.getAccount().getCurrency()))
                .finalPaymentDateTime(DateTime.now().plusYears(10))
                .firstPaymentAmount(new uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount().amount(FORMAT_AMOUNT.format(amount))
                        .currency(account.getAccount().getCurrency()))
                .standingOrderStatusCode(OBExternalStandingOrderStatus1Code.ACTIVE)
                .creditorAccount(new OBCashAccount5()
                        .schemeName(OBExternalAccountIdentification2Code.SortCodeAccountNumber.getReference())
                        .identification(sortCode.toString() + accountNumber.toString())
                        .name(name)
                )
        );
        standingOrder.setId(standingOrder.getStandingOrder().getStandingOrderId());

        LOGGER.debug("Standing order '{}' generated", standingOrder);
        return standingOrder;
    }

    public FRStatement4 generateStatements(FRAccount4 account, FRBalance1 balance, DateTime startDate) {
        String statementId = UUID.randomUUID().toString();
        FRStatement4 statement = new FRStatement4();
        statement.setAccountId(account.getId());
        statement.setId(statementId);
        statement.setStatement(new OBStatement2()
                .accountId(account.getId())
                .statementId(statementId)
                .statementReference(FORMATTER.print(startDate))
                .type(OBExternalStatementType1Code.REGULARPERIODIC)
                .startDateTime(startDate)
                .endDateTime(startDate.plusMonths(1).minusDays(1))
                .statementDescription(Arrays.asList(FORMATTER_HUMAN.print(startDate)))
                .addStatementAmountItem(new OBStatementAmount1()
                        .amount(new OBActiveOrHistoricCurrencyAndAmount()
                                        .amount(balance.getBalance().getAmount().getAmount())
                                        .currency(balance.getBalance().getAmount().getCurrency())
                                )
                        .creditDebitIndicator(balance.getBalance().getCreditDebitIndicator())
                        .type(OBExternalStatementAmountType1Code.PREVIOUSCLOSINGBALANCE.toString())
                )
        );

        return statement;
    }

    private void updateStatement(FRStatement4 statement, FRBalance1 balance) {
        statement.getStatement().addStatementAmountItem(new OBStatementAmount1()
                .amount(new OBActiveOrHistoricCurrencyAndAmount()
                        .amount(balance.getBalance().getAmount().getAmount())
                        .currency(balance.getBalance().getAmount().getCurrency())
                )
                .creditDebitIndicator(balance.getBalance().getCreditDebitIndicator())
                .type(OBExternalStatementAmountType1Code.CLOSINGBALANCE.toString())
        );
    }

    private List<FRTransaction5> generateTransactions(FRAccount4 account, FRStatement4 statement, FRBalance1 balance) {
        int nbTransactions = ThreadLocalRandom.current().nextInt(7,30);
        List<FRTransaction5> transactions = new ArrayList<>();
        LOGGER.debug("Generate {} transactions", nbTransactions);
        for (int i = 0; i < nbTransactions; i++) {
            transactions.add(generateTransaction(account, statement, balance));
        }
        return transactions;
    }

    private FRTransaction5 generateTransaction(FRAccount4 account, FRStatement4 statement, FRBalance1 balance) {
        String name = names.get(ThreadLocalRandom.current().nextInt(names.size()));

        long deltaTime = (statement.getStatement().getEndDateTime().getMillis() - statement.getStatement().getStartDateTime().getMillis()) / 1000;
        DateTime bookingDate = new DateTime(statement.getStatement().getStartDateTime()).plusSeconds(ThreadLocalRandom.current().nextInt(0, Math.toIntExact(deltaTime)));
        DateTime valueDate =  new DateTime(bookingDate).plusSeconds(ThreadLocalRandom.current().nextInt(60, 5*60));

        OBTransaction5.CreditDebitIndicatorEnum obCreditDebitCode = OBTransaction5.CreditDebitIndicatorEnum.values() [
                ThreadLocalRandom.current().nextInt(0, OBCreditDebitCode.values().length)];

        Double transactionAmount = generateAmount(10.0d, 500.0d);
        Double balanceAmount = Double.valueOf(balance.getBalance().getAmount().getAmount());
        Double finalAmount;
        String transactionInformation;
        switch (obCreditDebitCode) {
        case DEBIT:
            finalAmount = balanceAmount - transactionAmount;
            transactionInformation = "Cash to " + name;
            break;
        case CREDIT:
            default:
            finalAmount = balanceAmount + transactionAmount;
            transactionInformation = "Cash from " + name;
        }
        finalAmount = round(finalAmount, 2);
        if (finalAmount <= 0) {
            balance.getBalance().getAmount().setAmount(FORMAT_AMOUNT.format(-1 * finalAmount));
            balance.getBalance().setCreditDebitIndicator(OBCreditDebitCode.CREDIT);
        } else {
            balance.getBalance().getAmount().setAmount(FORMAT_AMOUNT.format(finalAmount));
            balance.getBalance().setCreditDebitIndicator(OBCreditDebitCode.DEBIT);
        }

        FRTransaction5 transaction = new FRTransaction5();
        transaction.addStatementId(statement.getId());
        transaction.setAccountId(account.getId());
        transaction.setBookingDateTime(bookingDate);
        transaction.setTransaction(new OBTransaction5()
                .accountId(account.getId())
                .transactionId(UUID.randomUUID().toString())
                .transactionReference("Ref " + ThreadLocalRandom.current().nextInt(10000))
                .amount(new uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount().amount(FORMAT_AMOUNT.format(transactionAmount))
                        .currency(account.getAccount().getCurrency()))
                .creditDebitIndicator(obCreditDebitCode)
                .status(OBEntryStatus1Code.BOOKED)
                .bookingDateTime(bookingDate)
                .valueDateTime(valueDate)
                .transactionInformation(transactionInformation)
                .bankTransactionCode(new OBBankTransactionCodeStructure1()
                        .code("ReceivedCreditTransfer")
                        .subCode("DomesticCreditTransfer")
                )
                .proprietaryBankTransactionCode(
                        new OBTransaction5ProprietaryBankTransactionCode()
                        .code("Transfer")
                        .issuer("AlphaBank")
                )
                .balance(new OBTransactionCashBalance()
                        .amount(balance.getBalance().getAmount())
                        .creditDebitIndicator(balance.getBalance().getCreditDebitIndicator())
                        .type(OBBalanceType1Code.INTERIMBOOKED)
                )
        );
        transaction.setId(transaction.getTransaction().getTransactionId());

        LOGGER.debug("FRTransaction1 '{}' generated", transaction);
        return transaction;
    }

    public FRScheduledPayment4 generateScheduledPayment(FRAccount4 account) {
        String scheduledPaymentId = UUID.randomUUID().toString();

        Double amount = generateAmount(10.0d, 500.0d);
        Integer accountNumber = ThreadLocalRandom.current().nextInt(0, 99999999);
        String company = companies.get(ThreadLocalRandom.current().nextInt(companies.size()));
        String name = names.get(ThreadLocalRandom.current().nextInt(names.size()));
        Integer sortCode = ThreadLocalRandom.current().nextInt(0, 999999);

        FRScheduledPayment4 scheduledPayment = new FRScheduledPayment4();
        scheduledPayment.setId(scheduledPaymentId);
        scheduledPayment.setAccountId(account.getId());
        scheduledPayment.setStatus(ScheduledPaymentStatus.PENDING);
        scheduledPayment.setScheduledPayment(new OBScheduledPayment3()
                .scheduledPaymentId(scheduledPaymentId)
                .scheduledPaymentDateTime(DateTime.now().plusDays(ThreadLocalRandom.current().nextInt(15, 200)))
                .scheduledType(OBExternalScheduleType1Code.EXECUTION)
                .reference(company)
                .instructedAmount(new OBActiveOrHistoricCurrencyAndAmount1()
                        .amount(FORMAT_AMOUNT.format(amount)).currency(account.getAccount().getCurrency()))
                .creditorAccount(new OBCashAccount51()
                        .schemeName(OBExternalAccountIdentification2Code.SortCodeAccountNumber.getReference())
                        .identification(sortCode.toString() + accountNumber.toString())
                        .name(name)
                )
        );
        return scheduledPayment;
    }

    public FRParty2 generateParty(FRAccount4 account2, String username) {
        FRParty2 party = partyRepository.findByAccountId(account2.getId());
        String partyId = (party == null) ? UUID.randomUUID().toString() : party.getId();
        party = new FRParty2();
        party.setAccountId(account2.getId());
        party.setId(partyId);
        party.setParty(new OBParty2()
                .partyId(partyId)
                .name(username)
        );
        partyRepository.save(party);
        return party;
    }

    public FRParty2 generateGlobalParty(String userId, String username) {
        FRParty2 existing = partyRepository.findByUserId(username);

        String partyId = (existing ==null) ? UUID.randomUUID().toString() : existing.getId();
        FRParty2 party = new FRParty2();
        party.setId(partyId);
        party.setUserId(userId);
        party.setParty(new OBParty2()
                .partyId(partyId)
                .name(username)
        );
        partyRepository.save(party);
        return party;
    }

    public FROffer1 generateOfferLimitIncrease(FRAccount4 account2) {

        Double amount = generateAmount(1000.0d, 15000.0d);
        amount = amount - (amount % 100);

        String offerId = UUID.randomUUID().toString();
        FROffer1 offer1 = new FROffer1();
        offer1.setAccountId(account2.getId());
        offer1.setId(offerId);
        offer1.setOffer(new OBOffer1()
                .offerId(offerId)
                .offerType(OBExternalOfferType1Code.LIMITINCREASE)
                .description("Credit limit increase for the account up to £" + FORMAT_AMOUNT.format(amount))
                .amount(new OBActiveOrHistoricCurrencyAndAmount().amount(FORMAT_AMOUNT.format(amount))
                        .currency(account2.getAccount().getCurrency()))
        );

        offer1Repository.save(offer1);
        return offer1;
    }

    public FROffer1 generateOfferBalanceTransfer(FRAccount4 account2) {

        Double amount = generateAmount(1000.0d, 5000.0d);
        amount = round(amount - (amount % 100), 2);

        String offerId = UUID.randomUUID().toString();
        FROffer1 offer1 = new FROffer1();
        offer1.setAccountId(account2.getId());
        offer1.setId(offerId);
        offer1.setOffer(new OBOffer1()
                .offerId(offerId)
                .offerType(OBExternalOfferType1Code.BALANCETRANSFER)
                .description("Balance transfer offer up to £" + FORMAT_AMOUNT.format(amount))
                .amount(new OBActiveOrHistoricCurrencyAndAmount().amount(FORMAT_AMOUNT.format(amount))
                        .currency(account2.getAccount().getCurrency()))
        );

        offer1Repository.save(offer1);
        return offer1;
    }

    private Double generateAmount(Double min, Double max) {
        Double amount = ThreadLocalRandom.current().nextDouble(min, max);
        return round(amount, 2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private List<String> loadCSV(Resource resource) throws IOException {

        LOGGER.debug("Load resource {}", resource);
        List<String> content = new ArrayList<>();

        String line;
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                while ((line = br.readLine()) != null) {
                    content.add(line);
                }
            } catch (IOException e) {
                LOGGER.error("Can't load resource '{}'", resource, e);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return content;
    }
}
