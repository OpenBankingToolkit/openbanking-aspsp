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
import com.forgerock.openbanking.common.model.openbanking.domain.account.*;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRBalanceType;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.*;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.data.FRAccountData;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.data.FRUserData;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.google.common.collect.ImmutableList;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.org.openbanking.datamodel.account.OBReadProduct2DataProduct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@NoArgsConstructor
public class DataUpdater {

    private FRAccountRepository accountsRepository;
    private FRBalanceRepository balanceRepository;
    private FRBeneficiaryRepository beneficiaryRepository;
    private FRDirectDebitRepository directDebitRepository;
    private FRProductRepository productRepository;
    private FRStandingOrderRepository standingOrderRepository;
    private FRTransactionRepository transactionRepository;
    private FRStatementRepository statementRepository;
    private FRScheduledPaymentRepository scheduledPaymentRepository;
    private FRPartyRepository partyRepository;
    private FROfferRepository offerRepository;
    private int documentLimit;

    @Autowired
    public DataUpdater(FRAccountRepository accountsRepository, FRBalanceRepository balanceRepository,
                       FRBeneficiaryRepository beneficiaryRepository, FRDirectDebitRepository directDebitRepository,
                       FRProductRepository productRepository, FRStandingOrderRepository standingOrderRepository,
                       FRTransactionRepository transactionRepository, FRStatementRepository statementRepository,
                       FRScheduledPaymentRepository scheduledPaymentRepository, FRPartyRepository partyRepository,
                       FROfferRepository offerRepository, @Value("${rs.data.upload.limit.documents}") Integer documentLimit) {
        this.accountsRepository = accountsRepository;
        this.balanceRepository = balanceRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.directDebitRepository = directDebitRepository;
        this.productRepository = productRepository;
        this.standingOrderRepository = standingOrderRepository;
        this.transactionRepository = transactionRepository;
        this.statementRepository = statementRepository;
        this.scheduledPaymentRepository = scheduledPaymentRepository;
        this.partyRepository = partyRepository;
        this.offerRepository = offerRepository;
        this.documentLimit = documentLimit;
    }

    void updateParty(FRUserData userData) {
        if (userData.getParty() == null) {
            return;
        }
        FRParty party = partyRepository.findByUserId(userData.getUserName());
        if (!party.getId().equals(userData.getParty().getPartyId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    String.format("the party ID '%s' doesn't match '%s' for user '%s'",
                            userData.getParty().getPartyId(),
                            party.getId(),
                            userData.getUserName()));
        }
        if (userData.getParty().getName() != null) {
            party.getParty().setName(userData.getParty().getName());
        }
        if (userData.getParty().getPhone() != null) {
            party.getParty().setPhone(userData.getParty().getPhone());
        }
        if (userData.getParty().getAddresses() != null) {
            party.getParty().setAddresses(userData.getParty().getAddresses());
        }
        if (userData.getParty().getEmailAddress() != null) {
            party.getParty().setEmailAddress(userData.getParty().getEmailAddress());
        }
        if (userData.getParty().getMobile() != null) {
            party.getParty().setMobile(userData.getParty().getMobile());
        }
        if (userData.getParty().getPartyNumber() != null) {
            party.getParty().setPartyNumber(userData.getParty().getPartyNumber());
        }
        if (userData.getParty().getPartyType() != null) {
            party.getParty().setPartyType(userData.getParty().getPartyType());
        }
        partyRepository.save(party);
    }

    void updateAccount(FRAccountData accountDataDiff, FRAccount account, Set<String> accountIds) {
        FRFinancialAccount accountDiff = accountDataDiff.getAccount();
        if (accountDiff.getCurrency() != null) {
            account.getAccount().setCurrency(accountDiff.getCurrency());
        }
        if (accountDiff.getNickname() != null) {
            account.getAccount().setNickname(accountDiff.getNickname());
        }
        if (accountDiff.getAccount() != null) {
            account.getAccount().setAccount(accountDiff.getAccount());
        }
        if (accountDiff.getServicer() != null) {
            account.getAccount().setServicer(accountDiff.getServicer());
        }
        accountsRepository.save(account);
    }

    void updateBalances(FRAccountData accountDataDiff, Set<String> accountIds) {
        //Balance
        List<FRBalance> balancesToSave = new ArrayList<>();
        List<FRBalance> newBalancesToSave = new ArrayList<>();
        Set<FRBalanceType> types = new HashSet<>();
        for (FRCashBalance balanceDiff : accountDataDiff.getBalances()) {
            String accountId = accountDataDiff.getAccount().getAccountId();
            if (balanceDiff.getAccountId() != null && !balanceDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        String.format("The account id '%s' refers in a balance doesn't match the main account id '%s'",
                                balanceDiff.getAccountId(),
                                accountId));
            }
            if (types.contains(balanceDiff.getType())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("You can't add two balances of the same type '%s' for the same account ID '%s'",
                                balanceDiff.getType(),
                                accountId));

            }
            types.add(balanceDiff.getType());
            Optional<FRBalance> isBalance = balanceRepository.findByAccountIdAndBalanceType(
                    accountId,
                    balanceDiff.getType());

            if (isBalance.isPresent()) {
                FRCashBalance balance = isBalance.get().getBalance();

                if (balanceDiff.getAmount() != null) {
                    balance.setAmount(balanceDiff.getAmount());
                }
                if (balanceDiff.getType() != null) {
                    balance.setType(balanceDiff.getType());
                }
                if (balanceDiff.getCreditDebitIndicator() != null) {
                    balance.setCreditDebitIndicator(balanceDiff.getCreditDebitIndicator());
                }
                if (balanceDiff.getCreditLine() != null) {
                    balance.setCreditLine(balanceDiff.getCreditLine());
                }
                if (balanceDiff.getDateTime() != null) {
                    balance.setDateTime(balanceDiff.getDateTime());
                }
                balancesToSave.add(isBalance.get());
            } else {
                FRBalance balance1 = new FRBalance();
                balance1.setBalance(balanceDiff);
                balance1.setAccountId(accountId);
                newBalancesToSave.add(balance1);
            }
        }
        if (balanceRepository.countByAccountIdIn(accountIds) + newBalancesToSave.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add balances as it has exceeded maximum limit of %s", documentLimit));
        }
        List<FRBalance> allBalances = ImmutableList.<FRBalance>builder()
                .addAll(balancesToSave)
                .addAll(newBalancesToSave)
                .build();
        balanceRepository.saveAll(allBalances);
    }

    void updateProducts(FRAccountData accountDataDiff, Set<String> accountIds) {
        //Product
        if (accountDataDiff.getProduct() == null) {
            return;
        }
        Optional<FRProduct> isProduct = productRepository.findById(accountDataDiff.getProduct().getProductId());
        if (isProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    String.format("The product id doesn't exist '%s'",
                            accountDataDiff.getProduct().getProductId()));
        }
        FRProduct product = isProduct.get();

        String accountId = accountDataDiff.getAccount().getAccountId();
        OBReadProduct2DataProduct productDiff = accountDataDiff.getProduct();
        if (productDiff.getAccountId() != null && !productDiff.getAccountId().equals(accountId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    String.format("The account id '%s' refers in product doesn't match the main account id '%s'",
                            productDiff.getAccountId(),
                            accountId));
        }
        if (productDiff.getProductType() != null) {
            product.getProduct().setProductType(productDiff.getProductType());
        }
        if (productDiff.getProductName() != null) {
            product.getProduct().setProductName(productDiff.getProductName());
        }
        productRepository.save(product);
    }

    void updateParty(FRAccountData accountDataDiff, Set<String> accountIds) {
        //Party
        if (accountDataDiff.getParty() == null) {
            return;
        }
        Optional<FRParty> isParty = partyRepository.findById(accountDataDiff.getParty().getPartyId());
        if (isParty.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("The party id '%s' doesn't exist",
                            accountDataDiff.getParty().getPartyId()));
        }
        FRParty party1 = isParty.get();

        FRPartyData partyDiff = accountDataDiff.getParty();

        if (partyDiff.getName() != null) {
            party1.getParty().setName(partyDiff.getName());
        }
        if (partyDiff.getPhone() != null) {
            party1.getParty().setPhone(partyDiff.getPhone());
        }
        if (partyDiff.getAddresses() != null) {
            party1.getParty().setAddresses(partyDiff.getAddresses());
        }
        if (partyDiff.getEmailAddress() != null) {
            party1.getParty().setEmailAddress(partyDiff.getEmailAddress());
        }
        if (partyDiff.getMobile() != null) {
            party1.getParty().setMobile(partyDiff.getMobile());
        }
        if (partyDiff.getPartyNumber() != null) {
            party1.getParty().setPartyNumber(partyDiff.getPartyNumber());
        }
        if (partyDiff.getPartyType() != null) {
            party1.getParty().setPartyType(partyDiff.getPartyType());
        }
        partyRepository.save(party1);
    }

    void updateBeneficiaries(FRAccountData accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Beneficiaries
        List<FRBeneficiary> beneficiariesToSave = new ArrayList<>();
        List<FRBeneficiary> newBeneficiariesToSave = new ArrayList<>();
        for (FRAccountBeneficiary beneficiaryDiff : accountDataDiff.getBeneficiaries()) {

            if (beneficiaryDiff.getAccountId() != null && !beneficiaryDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + beneficiaryDiff.getAccountId() + "' refers in a beneficiary doesn't match the main account id '" + accountId + "'");
            }
            if (beneficiaryDiff.getBeneficiaryId() == null) {
                beneficiaryDiff.setAccountId(accountId);
                beneficiaryDiff.setBeneficiaryId(UUID.randomUUID().toString());
                FRBeneficiary beneficiary = new FRBeneficiary();
                beneficiary.setAccountId(accountId);
                beneficiary.setBeneficiary(beneficiaryDiff);
                beneficiary.setId(beneficiaryDiff.getBeneficiaryId());
                newBeneficiariesToSave.add(beneficiary);
            } else {
                Optional<FRBeneficiary> isBeneficiary = beneficiaryRepository.findById(beneficiaryDiff.getBeneficiaryId());
                if (isBeneficiary.isEmpty() || !isBeneficiary.get().getAccountId().equals(beneficiaryDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The beneficiary id '"
                            + beneficiaryDiff.getBeneficiaryId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                FRAccountBeneficiary beneficiary = isBeneficiary.get().getBeneficiary();
                if (beneficiaryDiff.getReference() != null) {
                    beneficiary.setReference(beneficiaryDiff.getReference());
                }
                if (beneficiaryDiff.getCreditorAccount() != null) {
                    beneficiary.setCreditorAccount(beneficiaryDiff.getCreditorAccount());
                }
                beneficiariesToSave.add(isBeneficiary.get());

            }
        }
        if (beneficiaryRepository.countByAccountIdIn(accountIds) + newBeneficiariesToSave.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add beneficiaries as it has exceeded maximum limit of %s", documentLimit));
        }
        List<FRBeneficiary> allBeneficiaries = ImmutableList.<FRBeneficiary>builder()
                .addAll(beneficiariesToSave)
                .addAll(newBeneficiariesToSave)
                .build();
        beneficiaryRepository.saveAll(allBeneficiaries);
    }

    void updateDirectDebits(FRAccountData accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Direct Debits
        List<FRDirectDebit> directDebitsToSave = new ArrayList<>();
        List<FRDirectDebit> newDirectDebitsToSave = new ArrayList<>();
        for (FRDirectDebitData directDebitDiff : accountDataDiff.getDirectDebits()) {

            if (directDebitDiff.getAccountId() != null && !directDebitDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + directDebitDiff.getAccountId() + "' refers in a direct debit doesn't match the main account id '" + accountId + "'");
            }
            if (directDebitDiff.getDirectDebitId() == null) {
                directDebitDiff.setAccountId(accountId);
                directDebitDiff.setDirectDebitId(UUID.randomUUID().toString());
                FRDirectDebit directDebit = new FRDirectDebit();
                directDebit.setAccountId(accountId);
                directDebit.setDirectDebit(directDebitDiff);
                directDebit.setId(directDebitDiff.getDirectDebitId());
                newDirectDebitsToSave.add(directDebit);
            } else {
                Optional<FRDirectDebit> isDirectDebit = directDebitRepository.findById(directDebitDiff.getDirectDebitId());
                if (isDirectDebit.isEmpty() || !isDirectDebit.get().getAccountId().equals(directDebitDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The direct debit id '"
                            + directDebitDiff.getDirectDebitId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                FRDirectDebitData directDebit = isDirectDebit.get().getDirectDebit();
                if (directDebitDiff.getName() != null) {
                    directDebit.setName(directDebitDiff.getName());
                }
                if (directDebitDiff.getDirectDebitStatusCode() != null) {
                    directDebit.setDirectDebitStatusCode(directDebitDiff.getDirectDebitStatusCode());
                }
                if (directDebitDiff.getMandateIdentification() != null) {
                    directDebit.setMandateIdentification(directDebitDiff.getMandateIdentification());
                }
                if (directDebitDiff.getPreviousPaymentAmount() != null) {
                    directDebit.setPreviousPaymentAmount(directDebitDiff.getPreviousPaymentAmount());
                }
                if (directDebitDiff.getPreviousPaymentDateTime() != null) {
                    directDebit.setPreviousPaymentDateTime(directDebitDiff.getPreviousPaymentDateTime());
                }
                directDebitsToSave.add(isDirectDebit.get());
            }
        }
        if (directDebitRepository.countByAccountIdIn(accountIds) + newDirectDebitsToSave.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add direct debits as it has exceeded maximum limit of %s", documentLimit));
        }
        List<FRDirectDebit> allDirectDebits = ImmutableList.<FRDirectDebit>builder()
                .addAll(directDebitsToSave)
                .addAll(newDirectDebitsToSave)
                .build();
        directDebitRepository.saveAll(allDirectDebits);
    }

    void updateStandingOrders(FRAccountData accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Standing orders
        List<FRStandingOrder> standingOrdersToSave = new ArrayList<>();
        List<FRStandingOrder> newStandingOrdersToSave = new ArrayList<>();
        for (FRStandingOrderData standingOrderDiff : accountDataDiff.getStandingOrders()) {

            if (standingOrderDiff.getAccountId() != null && !standingOrderDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + standingOrderDiff.getAccountId() + "' refers in a standing order doesn't match the main account id '" + accountId + "'");
            }
            if (standingOrderDiff.getStandingOrderId() == null) {
                standingOrderDiff.setAccountId(accountId);
                standingOrderDiff.setStandingOrderId(UUID.randomUUID().toString());
                FRStandingOrder standingOrder = new FRStandingOrder();
                standingOrder.setAccountId(accountId);
                standingOrder.setStandingOrder(standingOrderDiff);
                standingOrder.setId(standingOrderDiff.getStandingOrderId());
                standingOrder.setStatus(StandingOrderStatus.PENDING);
                newStandingOrdersToSave.add(standingOrder);
            } else {
                Optional<FRStandingOrder> isStandingOrder = standingOrderRepository.findById(standingOrderDiff.getStandingOrderId());
                if (isStandingOrder.isEmpty() || !isStandingOrder.get().getAccountId().equals(standingOrderDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The standing order id '"
                            + standingOrderDiff.getStandingOrderId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                FRStandingOrderData standingOrder = isStandingOrder.get().getStandingOrder();
                if (standingOrderDiff.getFrequency() != null) {
                    standingOrder.setFrequency(standingOrderDiff.getFrequency());
                }
                if (standingOrderDiff.getReference() != null) {
                    standingOrder.setReference(standingOrderDiff.getReference());
                }
                if (standingOrderDiff.getFirstPaymentAmount() != null) {
                    standingOrder.setFirstPaymentAmount(standingOrderDiff.getFirstPaymentAmount());
                }
                if (standingOrderDiff.getFirstPaymentDateTime() != null) {
                    standingOrder.setFirstPaymentDateTime(standingOrderDiff.getFirstPaymentDateTime());
                }
                if (standingOrderDiff.getNextPaymentAmount() != null) {
                    standingOrder.setNextPaymentAmount(standingOrderDiff.getNextPaymentAmount());
                }
                if (standingOrderDiff.getNextPaymentDateTime() != null) {
                    standingOrder.setNextPaymentDateTime(standingOrderDiff.getNextPaymentDateTime());
                }
                if (standingOrderDiff.getFinalPaymentAmount() != null) {
                    standingOrder.setFinalPaymentAmount(standingOrderDiff.getFinalPaymentAmount());
                }
                if (standingOrderDiff.getFinalPaymentDateTime() != null) {
                    standingOrder.setFinalPaymentDateTime(standingOrderDiff.getFinalPaymentDateTime());
                }
                if (standingOrderDiff.getCreditorAccount() != null) {
                    standingOrder.setCreditorAccount(standingOrderDiff.getCreditorAccount());
                }
                standingOrdersToSave.add(isStandingOrder.get());
            }
        }
        if (standingOrderRepository.countByAccountIdIn(accountIds) + newStandingOrdersToSave.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add standing orders as it has exceeded maximum limit of %s", documentLimit));
        }
        List<FRStandingOrder> allStandingOrders = ImmutableList.<FRStandingOrder>builder()
                .addAll(standingOrdersToSave)
                .addAll(newStandingOrdersToSave)
                .build();
        standingOrderRepository.saveAll(allStandingOrders);
    }

    void updateTransactions(FRAccountData accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Transactions
        List<FRTransaction> transactionsToSave = new ArrayList<>();
        List<FRTransaction> newTransactionsToSave = new ArrayList<>();
        for (FRTransactionData transactionDiff : accountDataDiff.getTransactions()) {

            if (transactionDiff.getAccountId() != null && !transactionDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + transactionDiff.getAccountId() + "' refers in a transaction doesn't match the main account id '" + accountId + "'");
            }
            if (transactionDiff.getTransactionId() == null) {
                transactionDiff.setAccountId(accountId);
                transactionDiff.setTransactionId(UUID.randomUUID().toString());
                FRTransaction transaction = new FRTransaction();
                transaction.setAccountId(accountId);
                transaction.setBookingDateTime((transactionDiff.getBookingDateTime()));
                transaction.setTransaction(transactionDiff);
                transaction.setId(transactionDiff.getTransactionId());
                newTransactionsToSave.add(transaction);
            } else {
                Optional<FRTransaction> isTransaction = transactionRepository.findById(transactionDiff.getTransactionId());
                if (isTransaction.isEmpty() || !isTransaction.get().getAccountId().equals(transactionDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The transaction id '"
                            + transactionDiff.getTransactionId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                FRTransactionData transaction = isTransaction.get().getTransaction();
                if (transactionDiff.getTransactionReference() != null) {
                    transaction.setTransactionReference(transactionDiff.getTransactionReference());
                }
                if (transactionDiff.getAmount() != null) {
                    transaction.setAmount(transactionDiff.getAmount());
                }
                if (transactionDiff.getCreditDebitIndicator() != null) {
                    transaction.setCreditDebitIndicator(transactionDiff.getCreditDebitIndicator());
                }
                if (transactionDiff.getStatus() != null) {
                    transaction.setStatus(transactionDiff.getStatus());
                }
                if (transactionDiff.getBookingDateTime() != null) {
                    transaction.setBookingDateTime(transactionDiff.getBookingDateTime());
                    isTransaction.get().setBookingDateTime(transactionDiff.getBookingDateTime());
                }
                if (transactionDiff.getValueDateTime() != null) {
                    transaction.setValueDateTime(transactionDiff.getValueDateTime());
                }
                if (transactionDiff.getTransactionInformation() != null) {
                    transaction.setTransactionInformation(transactionDiff.getTransactionInformation());
                }
                if (transactionDiff.getBankTransactionCode() != null) {
                    transaction.setBankTransactionCode(transactionDiff.getBankTransactionCode());
                }
                if (transactionDiff.getProprietaryBankTransactionCode() != null) {
                    transaction.setProprietaryBankTransactionCode(transactionDiff.getProprietaryBankTransactionCode());
                }
                if (transactionDiff.getBalance() != null) {
                    transaction.setBalance(transactionDiff.getBalance());
                }
                transactionsToSave.add(isTransaction.get());
            }
        }
        if (transactionRepository.countByAccountIdIn(accountIds) + newTransactionsToSave.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add transactions as it has exceeded maximum limit of %s", documentLimit));
        }
        List<FRTransaction> allTransactions = ImmutableList.<FRTransaction>builder()
                .addAll(transactionsToSave)
                .addAll(newTransactionsToSave)
                .build();
        transactionRepository.saveAll(allTransactions);
    }

    void updateStatements(FRAccountData accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Statements
        List<FRStatement> statementsToSave = new ArrayList<>();
        List<FRStatement> newStatementsToSave = new ArrayList<>();
        for (FRStatementData statementDiff : accountDataDiff.getStatements()) {

            if (statementDiff.getAccountId() != null && !statementDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + statementDiff.getAccountId() + "' refers in a statement doesn't match the main account id '" + accountId + "'");
            }
            if (statementDiff.getStatementId() == null) {
                statementDiff.setAccountId(accountId);
                statementDiff.setStatementId(UUID.randomUUID().toString());
                FRStatement statement1 = new FRStatement();
                statement1.setAccountId(accountId);
                statement1.setStatement(statementDiff);
                statement1.setId(statementDiff.getStatementId());
                newStatementsToSave.add(statement1);
            } else {
                Optional<FRStatement> isStatement = statementRepository.findById(statementDiff.getStatementId());
                if (isStatement.isEmpty() || !isStatement.get().getAccountId().equals(statementDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The statement id '"
                            + statementDiff.getStatementId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                FRStatementData statement = isStatement.get().getStatement();
                if (statementDiff.getEndDateTime() != null) {
                    statement.setEndDateTime(statementDiff.getEndDateTime());
                    isStatement.get().setEndDateTime(statementDiff.getEndDateTime());
                }
                if (statementDiff.getStartDateTime() != null) {
                    statement.setStartDateTime(statementDiff.getStartDateTime());
                    isStatement.get().setStartDateTime(statementDiff.getStartDateTime());
                }
                if (statementDiff.getType() != null) {
                    statement.setType(statementDiff.getType());
                }
                if (statementDiff.getCreationDateTime() != null) {
                    statement.setCreationDateTime(statementDiff.getCreationDateTime());
                }
                if (statementDiff.getStatementAmount() != null) {
                    statement.setStatementAmount(statementDiff.getStatementAmount());
                }
                if (statementDiff.getStatementBenefit() != null) {
                    statement.setStatementBenefit(statementDiff.getStatementBenefit());
                }
                if (statementDiff.getStatementDateTime() != null) {
                    statement.setStatementDateTime(statementDiff.getStatementDateTime());
                }
                if (statementDiff.getStatementDescription() != null) {
                    statement.setStatementDescription(statementDiff.getStatementDescription());
                }
                if (statementDiff.getStatementFee() != null) {
                    statement.setStatementFee(statementDiff.getStatementFee());
                }
                if (statementDiff.getStatementInterest() != null) {
                    statement.setStatementInterest(statementDiff.getStatementInterest());
                }
                if (statementDiff.getStatementRate() != null) {
                    statement.setStatementRate(statementDiff.getStatementRate());
                }
                if (statementDiff.getStatementReference() != null) {
                    statement.setStatementReference(statementDiff.getStatementReference());
                }
                if (statementDiff.getStatementValue() != null) {
                    statement.setStatementValue(statementDiff.getStatementValue());
                }
                statementsToSave.add(isStatement.get());
            }
        }
        if (statementRepository.countByAccountIdIn(accountIds) + newStatementsToSave.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add statements as it has exceeded maximum limit of %s", documentLimit));
        }
        List<FRStatement> allStatements = ImmutableList.<FRStatement>builder()
                .addAll(statementsToSave)
                .addAll(newStatementsToSave)
                .build();
        statementRepository.saveAll(allStatements);
    }

    void updateScheduledPayments(FRAccountData accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Scheduled Payment
        List<FRScheduledPayment> scheduledPaymentToSave = new ArrayList<>();
        List<FRScheduledPayment> newScheduledPaymentToSave = new ArrayList<>();
        for (FRScheduledPaymentData scheduledPaymentDiff : accountDataDiff.getScheduledPayments()) {

            if (scheduledPaymentDiff.getAccountId() != null && !scheduledPaymentDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + scheduledPaymentDiff.getAccountId() + "' refers in a scheduled payment doesn't match the main account id '" + accountId + "'");
            }
            if (scheduledPaymentDiff.getScheduledPaymentId() == null) {
                scheduledPaymentDiff.setAccountId(accountId);
                scheduledPaymentDiff.setScheduledPaymentId(UUID.randomUUID().toString());
                FRScheduledPayment scheduledPayment1 = new FRScheduledPayment();
                scheduledPayment1.setAccountId(accountId);
                scheduledPayment1.setScheduledPayment(scheduledPaymentDiff);
                scheduledPayment1.setId(scheduledPaymentDiff.getScheduledPaymentId());
                scheduledPayment1.setStatus(ScheduledPaymentStatus.PENDING);
                newScheduledPaymentToSave.add(scheduledPayment1);
            } else {
                Optional<FRScheduledPayment> isScheduledPayment = scheduledPaymentRepository.findById(scheduledPaymentDiff.getScheduledPaymentId());
                if (isScheduledPayment.isEmpty() || !isScheduledPayment.get().getAccountId().equals(scheduledPaymentDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The scheduled payment id '"
                            + scheduledPaymentDiff.getScheduledPaymentId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                FRScheduledPaymentData scheduledPayment = isScheduledPayment.get().getScheduledPayment();
                if (scheduledPaymentDiff.getCreditorAccount() != null) {
                    scheduledPayment.setCreditorAccount(scheduledPaymentDiff.getCreditorAccount());
                }
                if (scheduledPaymentDiff.getInstructedAmount() != null) {
                    scheduledPayment.setInstructedAmount(scheduledPaymentDiff.getInstructedAmount());
                }
                if (scheduledPaymentDiff.getReference() != null) {
                    scheduledPayment.setReference(scheduledPaymentDiff.getReference());
                }
                if (scheduledPaymentDiff.getCreditorAgent() != null) {
                    scheduledPayment.setCreditorAgent(scheduledPaymentDiff.getCreditorAgent());
                }
                if (scheduledPaymentDiff.getScheduledPaymentDateTime() != null) {
                    scheduledPayment.setScheduledPaymentDateTime(scheduledPaymentDiff.getScheduledPaymentDateTime());
                }
                if (scheduledPaymentDiff.getScheduledType() != null) {
                    scheduledPayment.setScheduledType(scheduledPaymentDiff.getScheduledType());
                }
                scheduledPaymentToSave.add(isScheduledPayment.get());
            }
        }
        if (scheduledPaymentRepository.countByAccountIdIn(accountIds) + newScheduledPaymentToSave.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add schedule payments as it has exceeded maximum limit of %s", documentLimit));
        }
        List<FRScheduledPayment> allScheduledPayments = ImmutableList.<FRScheduledPayment>builder()
                .addAll(scheduledPaymentToSave)
                .addAll(newScheduledPaymentToSave)
                .build();
        scheduledPaymentRepository.saveAll(allScheduledPayments);
    }

    void updateOffers(FRAccountData accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Offers
        List<FROffer> offersToSave = new ArrayList<>();
        List<FROffer> newOffersToSave = new ArrayList<>();
        for (FROfferData offersDiff : accountDataDiff.getOffers()) {

            if (offersDiff.getAccountId() != null && !offersDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + offersDiff.getAccountId() + "' refers in a offer doesn't match the main account id '" + accountId + "'");
            }
            if (offersDiff.getOfferId() == null) {
                offersDiff.setAccountId(accountId);
                offersDiff.setOfferId(UUID.randomUUID().toString());
                FROffer offer1 = new FROffer();
                offer1.setAccountId(accountId);
                offer1.setOffer(offersDiff);
                offer1.setId(offersDiff.getOfferId());
                newOffersToSave.add(offer1);
            } else {
                Optional<FROffer> isOffers = offerRepository.findById(offersDiff.getOfferId());
                if (isOffers.isEmpty() || !isOffers.get().getAccountId().equals(offersDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The offer id '"
                            + offersDiff.getOfferId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                FROfferData offer = isOffers.get().getOffer();
                if (offersDiff.getAmount() != null) {
                    offer.setAmount(offersDiff.getAmount());
                }
                if (offersDiff.getDescription() != null) {
                    offer.setDescription(offersDiff.getDescription());
                }
                if (offersDiff.getEndDateTime() != null) {
                    offer.setEndDateTime(offersDiff.getEndDateTime());
                }
                if (offersDiff.getStartDateTime() != null) {
                    offer.setStartDateTime(offersDiff.getStartDateTime());
                }
                if (offersDiff.getURL() != null) {
                    offer.setURL(offersDiff.getURL());
                }
                if (offersDiff.getValue() != null) {
                    offer.setValue(offersDiff.getValue());
                }
                if (offersDiff.getFee() != null) {
                    offer.setFee(offersDiff.getFee());
                }
                if (offersDiff.getOfferType() != null) {
                    offer.setOfferType(offersDiff.getOfferType());
                }
                if (offersDiff.getRate() != null) {
                    offer.setRate(offersDiff.getRate());
                }
                if (offersDiff.getTerm() != null) {
                    offer.setTerm(offersDiff.getTerm());
                }
                offersToSave.add(isOffers.get());
            }
        }
        if (offerRepository.countByAccountIdIn(accountIds) + newOffersToSave.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add offers as it has exceeded maximum limit of %s", documentLimit));
        }
        List<FROffer> allOffers = ImmutableList.<FROffer>builder()
                .addAll(offersToSave)
                .addAll(newOffersToSave)
                .build();
        offerRepository.saveAll(allOffers);
    }

}
