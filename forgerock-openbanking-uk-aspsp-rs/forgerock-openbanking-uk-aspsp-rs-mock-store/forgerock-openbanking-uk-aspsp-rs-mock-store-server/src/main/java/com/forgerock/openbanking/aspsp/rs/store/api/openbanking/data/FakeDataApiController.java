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
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accounts.FRAccountRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.balances.FRBalanceRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.beneficiaries.FRBeneficiaryRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.directdebits.FRDirectDebitRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.offers.FROfferRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.party.FRPartyRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.products.FRProductRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.scheduledpayments.FRScheduledPaymentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.standingorders.FRStandingOrderRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.statements.FRStatementRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.transactions.FRTransactionRepository;
import com.forgerock.openbanking.common.conf.data.DataConfigurationProperties;
import com.forgerock.openbanking.common.model.data.FRUserData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.*;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRDirectDebitData.FRDirectDebitStatus;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRFinancialAccount.FRAccountStatusCode;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRFinancialAccount.FRAccountSubTypeCode;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRFinancialAccount.FRAccountTypeCode;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStandingOrderData.FRStandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData.FRStatementType;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRBalanceType;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRCreditDebitIndicator;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.*;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
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
import uk.org.openbanking.datamodel.account.OBExternalAccountIdentification4Code;
import uk.org.openbanking.datamodel.account.OBExternalStatementAmountType1Code;
import uk.org.openbanking.datamodel.account.OBReadProduct2DataProduct;
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
    private FRAccountRepository accountsRepository;
    @Autowired
    private FRBalanceRepository balanceRepository;
    @Autowired
    private FRBeneficiaryRepository beneficiaryRepository;
    @Autowired
    private FRDirectDebitRepository directDebitRepository;
    @Autowired
    private FRProductRepository productRepository;
    @Autowired
    private FRStandingOrderRepository standingOrderRepository;
    @Autowired
    private FRTransactionRepository transactionRepository;
    @Autowired
    private FRStatementRepository statementRepository;
    @Autowired
    private FRScheduledPaymentRepository scheduledPaymentRepository;
    @Autowired
    private FRPartyRepository partyRepository;
    @Autowired
    private FROfferRepository offerRepository;
    @Autowired
    private DataApiController dataController;
    @Autowired
    private ObjectMapper mapper;
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
            FRUserData template = getTemplate(dataTemplateProfile.getTemplate(), username);
            template.setUserName(username);
            return dataController.importUserData(template);
        }
    }

    private FRUserData getTemplate(Resource template, String username) {
        try {
            String content = StreamUtils.copyToString(template.getInputStream(), Charset.defaultCharset());
            content = content.replaceAll("$username", username);
            return mapper.readValue(content, FRUserData.class);
        } catch (IOException e) {
            LOGGER.error("Can't read registration request resource", e);
            throw new RuntimeException(e);
        }
    }

    private FRUserData generateRandomData(String userId, String username)
     {
        LOGGER.debug("Generate data for user '{}'", userId);

        if (accountsRepository.findByUserID(userId).size() > 0 ) {
            LOGGER.debug("User {} already have some data", userId);
        }
        {
            Integer sortCode = ThreadLocalRandom.current().nextInt(0, 999999);
            Integer accountNumber = ThreadLocalRandom.current().nextInt(0, 99999999);

            String accountId = UUID.randomUUID().toString();
            FRAccount accountPremierBank = new FRAccount();
            accountPremierBank.setCreated(new Date());
            accountPremierBank.setId(accountId);
            accountPremierBank.setUserID(userId);
            accountPremierBank.setAccount(FRFinancialAccount.builder()
                    .accountId(accountId)
                    .accountType(FRAccountTypeCode.PERSONAL)
                    .accountSubType(FRAccountSubTypeCode.CURRENTACCOUNT)
                    .currency(GBP)
                    .nickname("UK Bills")
                    .status(FRAccountStatusCode.ENABLED)
                    .statusUpdateDateTime(DateTime.now())
                    .openingDate(DateTime.now().minusDays(1))
                    .maturityDate(DateTime.now().plusDays(1))
                    .accounts(Collections.singletonList(FRAccountIdentifier.builder()
                            .schemeName(OBExternalAccountIdentification4Code.SORTCODEACCOUNTNUMBER.toString())
                            .identification(sortCode.toString() + accountNumber.toString())
                            .name(username)
                            .secondaryIdentification(ThreadLocalRandom.current().nextInt(0, 99999999) + "")
                            .build()))
                    .build()
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
             FRAccount accountPremierBank = new FRAccount();
             accountPremierBank.setId(accountId);
             accountPremierBank.setCreated(new Date());
             accountPremierBank.setUserID(userId);
             accountPremierBank.setAccount(FRFinancialAccount.builder()
                     .accountId(accountId)
                     .accountType(FRAccountTypeCode.PERSONAL)
                     .accountSubType(FRAccountSubTypeCode.CURRENTACCOUNT)
                     .currency(EUR)
                     .nickname("FR Bills")
                     .status(FRAccountStatusCode.ENABLED)
                     .statusUpdateDateTime(DateTime.now())
                     .openingDate(DateTime.now().minusDays(1))
                     .maturityDate(DateTime.now().plusDays(1))
                     .accounts(Collections.singletonList(FRAccountIdentifier.builder()
                             .schemeName(OBExternalAccountIdentification4Code.SORTCODEACCOUNTNUMBER.toString())
                             .identification(sortCode.toString() + accountNumber.toString())
                             .name(username)
                             .secondaryIdentification(ThreadLocalRandom.current().nextInt(0, 99999999) + "")
                             .build()))
                     .build()
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
            FRAccount accountPremierCard = new FRAccount();
            accountPremierCard.setCreated(new Date());
            accountPremierCard.setId(accountId);
            accountPremierCard.setUserID(userId);
            accountPremierCard.setAccount(FRFinancialAccount.builder()
                    .accountId(accountId)
                    .accountType(FRAccountTypeCode.PERSONAL)
                    .accountSubType(FRAccountSubTypeCode.CURRENTACCOUNT)
                    .currency(GBP)
                    .nickname("Household")
                    .status(FRAccountStatusCode.ENABLED)
                    .statusUpdateDateTime(DateTime.now())
                    .openingDate(DateTime.now().minusDays(1))
                    .maturityDate(DateTime.now().plusDays(1))
                    .accounts(Collections.singletonList(FRAccountIdentifier.builder()
                            .schemeName(OBExternalAccountIdentification4Code.SORTCODEACCOUNTNUMBER.toString())
                            .identification(sortCode.toString() + accountNumber.toString())
                            .name(username)
                            .build()))
                    .build()
            );
            LOGGER.debug("Account '{}' generated for user '{}'", accountPremierCard, userId);
            accountsRepository.save(accountPremierCard);
            generateAccountData(accountPremierCard);
            generateParty(accountPremierCard, username);
            generateOfferLimitIncrease(accountPremierCard);
        }

        generateGlobalParty(userId, username);

        return dataController.exportUserData(userId).getBody();
     }

    private void generateAccountData(FRAccount account) {
        FRBalance balance = generateBalance(account, FRCreditDebitIndicator.DEBIT, null);
        int nbBeneficiaries = ThreadLocalRandom.current().nextInt(2,8);
        int nbDirectDebits = ThreadLocalRandom.current().nextInt(2,8);
        int nbStandingOrders = ThreadLocalRandom.current().nextInt(2,8);
        int nbScheduledPayment = ThreadLocalRandom.current().nextInt(2,8);

        LOGGER.debug("Generate {} beneficiaries", nbBeneficiaries);
        List<FRBeneficiary> beneficiarys = new ArrayList<>();
        for (int i = 0; i < nbBeneficiaries; i++) {
            beneficiarys.add(generateBeneficiary(account));
        }

        LOGGER.debug("Generate {} direct debits", nbDirectDebits);
        List<FRDirectDebit> directDebit1s = new ArrayList<>();
        for (int i = 0; i < nbDirectDebits; i++) {
            directDebit1s.add(generateDirectDebit(account));
        }
        FRProduct product2 = generateProduct(account);

        LOGGER.debug("Generate {} standing orders", nbStandingOrders);
        List<FRStandingOrder> standingOrder3s = new ArrayList<>();
        for (int i = 0; i < nbStandingOrders; i++) {
            standingOrder3s.add(generateStandingOrder(account));
        }

        LOGGER.debug("Generate statements");
        List<FRStatement> statements = new ArrayList<>();
        List<FRTransaction> transactions = new ArrayList<>();
        DateTime currentMonth = DateTime.now().dayOfMonth().withMinimumValue().minusMonths(12);
        for (int i = 12; i > 0; i-- ) {
            LOGGER.debug("Month: {}", FORMATTER.print(currentMonth));
            FRStatement statement1 = generateStatements(account, balance, currentMonth);
            statements.add(statement1);
            currentMonth = currentMonth.plusMonths(1);
            transactions.addAll(generateTransactions(account, statement1, balance));
            updateStatement(statement1, balance);
        }
        LOGGER.debug("Month: {}", FORMATTER.print(currentMonth));
        FRStatement statement1 = generateStatements(account, balance, currentMonth);
        account.setLatestStatementId(statement1.getId());

        LOGGER.debug("Generate {} standing orders", nbScheduledPayment);
        List<FRScheduledPayment> scheduledPayments = new ArrayList<>();
        for (int i = 0; i < nbScheduledPayment; i++) {
            scheduledPayments.add(generateScheduledPayment(account));
        }

        beneficiaryRepository.saveAll(beneficiarys);
        directDebitRepository.saveAll(directDebit1s);
        scheduledPaymentRepository.saveAll(scheduledPayments);
        standingOrderRepository.saveAll(standingOrder3s);
        statementRepository.saveAll(statements);
        transactionRepository.saveAll(transactions);
        productRepository.save(product2);
        balanceRepository.save(balance);

        accountsRepository.save(account);
    }

    private FRBalance generateBalance(FRAccount account, FRCreditDebitIndicator creditDebitCode, List<FRCreditLine> creditLine) {
        Double amount = generateAmount(1000.0d, 10000.0d);
        FRBalance balance = new FRBalance();
        balance.setAccountId(account.getId());
        balance.setBalance(FRCashBalance.builder()
                .accountId(account.getId())
                .amount(FRAmount.builder().amount(FORMAT_AMOUNT.format(amount)).currency(account.getAccount().getCurrency()).build())
                .creditDebitIndicator(creditDebitCode)
                .type(FRBalanceType.INTERIMAVAILABLE)
                .dateTime(DateTime.now())
                .creditLines(creditLine)
                .build()
        );
        LOGGER.debug("FRBalance1 '{}' generated", balance);
        return balance;
    }

    private FRBeneficiary generateBeneficiary(FRAccount account) {
        FRBeneficiary beneficiary = new FRBeneficiary();
        beneficiary.setAccountId(account.getId());
        Integer sortCode = ThreadLocalRandom.current().nextInt(0, 999999);
        Integer accountNumber = ThreadLocalRandom.current().nextInt(0, 99999999);
        String company = companies.get(ThreadLocalRandom.current().nextInt(companies.size()));
        String name = names.get(ThreadLocalRandom.current().nextInt(names.size()));

        beneficiary.setBeneficiary(FRAccountBeneficiary.builder()
                .accountId(account.getId())
                .beneficiaryId(UUID.randomUUID().toString())
                .reference(company)
                .creditorAccount(FRAccountIdentifier.builder()
                        .schemeName(OBExternalAccountIdentification2Code.SortCodeAccountNumber.getReference())
                        .identification(sortCode.toString() + accountNumber.toString())
                        .name(name)
                        .build())
                .build()
        );
        beneficiary.setId(beneficiary.getBeneficiary().getBeneficiaryId());
        LOGGER.debug("FRBeneficiary1 '{}' generated", beneficiary);
        return beneficiary;
    }

    private FRDirectDebit generateDirectDebit(FRAccount account) {
        String company = companies.get(ThreadLocalRandom.current().nextInt(companies.size()));

        Double amount = generateAmount(10.0d, 500.0d);
        FRDirectDebit directDebit = new FRDirectDebit();
        directDebit.setAccountId(account.getId());
        directDebit.setDirectDebit(FRDirectDebitData.builder()
                .accountId(account.getId())
                .directDebitId(UUID.randomUUID().toString())
                .mandateIdentification(company.trim())
                .directDebitStatusCode(FRDirectDebitStatus.ACTIVE)
                .name(company)
                .previousPaymentDateTime(DateTime.now().minusMonths(1))
                .previousPaymentAmount(FRAmount.builder()
                        .amount(FORMAT_AMOUNT.format(amount))
                        .currency(account.getAccount().getCurrency())
                        .build())
                .build()
        );
        directDebit.setId(directDebit.getDirectDebit().getDirectDebitId());

        LOGGER.debug("Direct debit '{}' generated", directDebit);
        return directDebit;
    }

    private FRProduct generateProduct(FRAccount account) {
        FRProduct product = new FRProduct();
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

    private FRStandingOrder generateStandingOrder(FRAccount account) {
        Double amount = generateAmount(10.0d, 500.0d);

        Integer sortCode = ThreadLocalRandom.current().nextInt(0, 999999);
        Integer accountNumber = ThreadLocalRandom.current().nextInt(0, 99999999);

        String company = companies.get(ThreadLocalRandom.current().nextInt(companies.size()));
        String name = names.get(ThreadLocalRandom.current().nextInt(names.size()));

        FRStandingOrder standingOrder = new FRStandingOrder();
        standingOrder.setAccountId(account.getId());
        standingOrder.setStandingOrder(FRStandingOrderData.builder()
                .accountId(account.getId())
                .standingOrderId(UUID.randomUUID().toString())
                .standingOrderStatusCode(FRStandingOrderStatus.ACTIVE)
                .frequency("EvryWorkgDay")
                .reference(company)
                .firstPaymentDateTime(DateTime.now().minusYears(1))
                .firstPaymentAmount(FRAmount.builder()
                        .amount(FORMAT_AMOUNT.format(amount))
                        .currency(account.getAccount().getCurrency()).build())
                .nextPaymentDateTime(DateTime.now().plusMonths(2))
                .nextPaymentAmount(FRAmount.builder().amount(FORMAT_AMOUNT.format(amount))
                        .currency(account.getAccount().getCurrency()).build())
                .finalPaymentDateTime(DateTime.now().plusYears(10))
                .firstPaymentAmount(FRAmount.builder().amount(FORMAT_AMOUNT.format(amount))
                        .currency(account.getAccount().getCurrency()).build())
                .standingOrderStatusCode(FRStandingOrderStatus.ACTIVE)
                .creditorAccount(FRAccountIdentifier.builder()
                        .schemeName(OBExternalAccountIdentification2Code.SortCodeAccountNumber.getReference())
                        .identification(sortCode.toString() + accountNumber.toString())
                        .name(name)
                        .build())
                .build()
        );
        standingOrder.setId(standingOrder.getStandingOrder().getStandingOrderId());

        LOGGER.debug("Standing order '{}' generated", standingOrder);
        return standingOrder;
    }

    private FRStatement generateStatements(FRAccount account, FRBalance balance, DateTime startDate) {
        String statementId = UUID.randomUUID().toString();
        FRStatement statement = new FRStatement();
        statement.setAccountId(account.getId());
        statement.setId(statementId);
        statement.setStatement(FRStatementData.builder()
                .accountId(account.getId())
                .statementId(statementId)
                .statementReference(FORMATTER.print(startDate))
                .type(FRStatementType.REGULARPERIODIC)
                .startDateTime(startDate)
                .endDateTime(startDate.plusMonths(1).minusDays(1))
                .statementDescriptions(Arrays.asList(FORMATTER_HUMAN.print(startDate)))
                .statementAmounts(Collections.singletonList(FRStatementData.FRStatementAmount.builder()
                        .amount(FRAmount.builder()
                                .amount(balance.getBalance().getAmount().getAmount())
                                .currency(balance.getBalance().getAmount().getCurrency())
                                .build())
                        .creditDebitIndicator(balance.getBalance().getCreditDebitIndicator())
                        .type(OBExternalStatementAmountType1Code.PREVIOUSCLOSINGBALANCE.toString())
                        .build()))
                .build()
        );

        return statement;
    }

    private void updateStatement(FRStatement statement, FRBalance balance) {
        statement.getStatement().addStatementAmount(FRStatementData.FRStatementAmount.builder()
                .amount(FRAmount.builder()
                        .amount(balance.getBalance().getAmount().getAmount())
                        .currency(balance.getBalance().getAmount().getCurrency())
                        .build()
                )
                .creditDebitIndicator(balance.getBalance().getCreditDebitIndicator())
                .type(OBExternalStatementAmountType1Code.CLOSINGBALANCE.toString())
                .build()
        );
    }

    private List<FRTransaction> generateTransactions(FRAccount account, FRStatement statement, FRBalance balance) {
        int nbTransactions = ThreadLocalRandom.current().nextInt(7,30);
        List<FRTransaction> transactions = new ArrayList<>();
        LOGGER.debug("Generate {} transactions", nbTransactions);
        for (int i = 0; i < nbTransactions; i++) {
            transactions.add(generateTransaction(account, statement, balance));
        }
        return transactions;
    }

    private FRTransaction generateTransaction(FRAccount account, FRStatement statement, FRBalance balance) {
        String name = names.get(ThreadLocalRandom.current().nextInt(names.size()));

        long deltaTime = (statement.getStatement().getEndDateTime().getMillis() - statement.getStatement().getStartDateTime().getMillis()) / 1000;
        DateTime bookingDate = new DateTime(statement.getStatement().getStartDateTime()).plusSeconds(ThreadLocalRandom.current().nextInt(0, Math.toIntExact(deltaTime)));
        DateTime valueDate =  new DateTime(bookingDate).plusSeconds(ThreadLocalRandom.current().nextInt(60, 5*60));

        FRCreditDebitIndicator creditDebitIndicator = FRCreditDebitIndicator.values() [
                ThreadLocalRandom.current().nextInt(0, FRCreditDebitIndicator.values().length)];

        Double transactionAmount = generateAmount(10.0d, 500.0d);
        Double balanceAmount = Double.valueOf(balance.getBalance().getAmount().getAmount());
        Double finalAmount;
        String transactionInformation;
        switch (creditDebitIndicator) {
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
            balance.getBalance().setCreditDebitIndicator(FRCreditDebitIndicator.CREDIT);
        } else {
            balance.getBalance().getAmount().setAmount(FORMAT_AMOUNT.format(finalAmount));
            balance.getBalance().setCreditDebitIndicator(FRCreditDebitIndicator.DEBIT);
        }

        FRTransaction transaction = new FRTransaction();
        transaction.addStatementId(statement.getId());
        transaction.setAccountId(account.getId());
        transaction.setBookingDateTime(bookingDate);
        transaction.setTransaction(FRTransactionData.builder()
                .accountId(account.getId())
                .transactionId(UUID.randomUUID().toString())
                .transactionReference("Ref " + ThreadLocalRandom.current().nextInt(10000))
                .amount(FRAmount.builder()
                        .amount(FORMAT_AMOUNT.format(transactionAmount))
                        .currency(account.getAccount().getCurrency())
                        .build())
                .creditDebitIndicator(creditDebitIndicator)
                .status(FRTransactionData.FREntryStatus.BOOKED)
                .bookingDateTime(bookingDate)
                .valueDateTime(valueDate)
                .transactionInformation(transactionInformation)
                .bankTransactionCode(FRTransactionData.FRBankTransactionCodeStructure.builder()
                        .code("ReceivedCreditTransfer")
                        .subCode("DomesticCreditTransfer")
                        .build()
                )
                .proprietaryBankTransactionCode(FRTransactionData.FRProprietaryBankTransactionCodeStructure.builder()
                        .code("Transfer")
                        .issuer("AlphaBank")
                        .build()
                )
                .balance(FRTransactionData.FRTransactionCashBalance.builder()
                        .amount(balance.getBalance().getAmount())
                        .creditDebitIndicator(balance.getBalance().getCreditDebitIndicator())
                        .type(FRBalanceType.INTERIMBOOKED)
                        .build()
                )
                .build()
        );
        transaction.setId(transaction.getTransaction().getTransactionId());

        LOGGER.debug("FRTransaction1 '{}' generated", transaction);
        return transaction;
    }

    private FRScheduledPayment generateScheduledPayment(FRAccount account) {
        String scheduledPaymentId = UUID.randomUUID().toString();

        Double amount = generateAmount(10.0d, 500.0d);
        Integer accountNumber = ThreadLocalRandom.current().nextInt(0, 99999999);
        String company = companies.get(ThreadLocalRandom.current().nextInt(companies.size()));
        String name = names.get(ThreadLocalRandom.current().nextInt(names.size()));
        Integer sortCode = ThreadLocalRandom.current().nextInt(0, 999999);

        FRScheduledPayment scheduledPayment = new FRScheduledPayment();
        scheduledPayment.setId(scheduledPaymentId);
        scheduledPayment.setAccountId(account.getId());
        scheduledPayment.setStatus(ScheduledPaymentStatus.PENDING);
        scheduledPayment.setScheduledPayment(FRScheduledPaymentData.builder()
                .scheduledPaymentId(scheduledPaymentId)
                .scheduledPaymentDateTime(DateTime.now().plusDays(ThreadLocalRandom.current().nextInt(15, 200)))
                .scheduledType(FRScheduledPaymentData.FRScheduleType.EXECUTION)
                .reference(company)
                .instructedAmount(FRAmount.builder()
                        .amount(FORMAT_AMOUNT.format(amount))
                        .currency(account.getAccount().getCurrency())
                        .build())
                .creditorAccount(FRAccountIdentifier.builder()
                        .schemeName(OBExternalAccountIdentification2Code.SortCodeAccountNumber.getReference())
                        .identification(sortCode.toString() + accountNumber.toString())
                        .name(name)
                        .build()
                )
                .build()
        );
        return scheduledPayment;
    }

    private FRParty generateParty(FRAccount account2, String username) {
        FRParty party = partyRepository.findByAccountId(account2.getId());
        String partyId = (party == null) ? UUID.randomUUID().toString() : party.getId();
        party = new FRParty();
        party.setAccountId(account2.getId());
        party.setId(partyId);
        party.setParty(FRPartyData.builder()
                .partyId(partyId)
                .name(username)
                .build()
        );
        partyRepository.save(party);
        return party;
    }

    private FRParty generateGlobalParty(String userId, String username) {
        FRParty existing = partyRepository.findByUserId(username);

        String partyId = (existing ==null) ? UUID.randomUUID().toString() : existing.getId();
        FRParty party = new FRParty();
        party.setId(partyId);
        party.setUserId(userId);
        party.setParty(FRPartyData.builder()
                .partyId(partyId)
                .name(username)
                .build()
        );
        partyRepository.save(party);
        return party;
    }

    private FROffer generateOfferLimitIncrease(FRAccount account2) {

        Double amount = generateAmount(1000.0d, 15000.0d);
        amount = amount - (amount % 100);

        String offerId = UUID.randomUUID().toString();
        FROffer offer1 = new FROffer();
        offer1.setAccountId(account2.getId());
        offer1.setId(offerId);
        offer1.setOffer(FROfferData.builder()
                .offerId(offerId)
                .offerType(FROfferData.FROfferType.LIMITINCREASE)
                .description("Credit limit increase for the account up to £" + FORMAT_AMOUNT.format(amount))
                .amount(FRAmount.builder()
                        .amount(FORMAT_AMOUNT.format(amount))
                        .currency(account2.getAccount().getCurrency())
                        .build())
                .build()
        );

        offerRepository.save(offer1);
        return offer1;
    }

    private FROffer generateOfferBalanceTransfer(FRAccount account2) {

        Double amount = generateAmount(1000.0d, 5000.0d);
        amount = round(amount - (amount % 100), 2);

        String offerId = UUID.randomUUID().toString();
        FROffer offer1 = new FROffer();
        offer1.setAccountId(account2.getId());
        offer1.setId(offerId);
        offer1.setOffer(FROfferData.builder()
                .offerId(offerId)
                .offerType(FROfferData.FROfferType.BALANCETRANSFER)
                .description("Balance transfer offer up to £" + FORMAT_AMOUNT.format(amount))
                .amount(FRAmount.builder()
                        .amount(FORMAT_AMOUNT.format(amount))
                        .currency(account2.getAccount().getCurrency())
                        .build())
                .build()
        );

        offerRepository.save(offer1);
        return offer1;
    }

    private Double generateAmount(Double min, Double max) {
        Double amount = ThreadLocalRandom.current().nextDouble(min, max);
        return round(amount, 2);
    }

    private static double round(double value, int places) {
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
