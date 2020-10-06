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

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.balances.FRBalanceRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.offers.FROfferRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.products.FRProductRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.party.FRPartyRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accounts.FRAccountRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.directdebits.FRDirectDebitRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.scheduledpayments.FRScheduledPaymentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.statements.FRStatementRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.beneficiaries.FRBeneficiaryRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.standingorders.FRStandingOrderRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.transactions.FRTransactionRepository;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FROffer;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRProduct;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRParty;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRDirectDebit;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRScheduledPayment;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStatement;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRBeneficiary;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRTransaction;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.data.FRAccountData;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.data.FRUserData;
import com.google.common.collect.ImmutableList;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.org.openbanking.datamodel.account.*;

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
        FRParty party1 = partyRepository.findByUserId(userData.getUserName());
        if (!party1.getId().equals(userData.getParty().getPartyId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    String.format("the party ID '%s' doesn't match '%s' for user '%s'",
                            userData.getParty().getPartyId(),
                            party1.getId(),
                            userData.getUserName()));
        }
        if (userData.getParty().getName() != null) {
            party1.getParty().setName(userData.getParty().getName());
        }
        if (userData.getParty().getPhone() != null) {
            party1.getParty().setPhone(userData.getParty().getPhone());
        }
        if (userData.getParty().getAddress() != null) {
            party1.getParty().setAddress(userData.getParty().getAddress());
        }
        if (userData.getParty().getEmailAddress() != null) {
            party1.getParty().setEmailAddress(userData.getParty().getEmailAddress());
        }
        if (userData.getParty().getMobile() != null) {
            party1.getParty().setMobile(userData.getParty().getMobile());
        }
        if (userData.getParty().getPartyNumber() != null) {
            party1.getParty().setPartyNumber(userData.getParty().getPartyNumber());
        }
        if (userData.getParty().getPartyType() != null) {
            party1.getParty().setPartyType(userData.getParty().getPartyType());
        }
        partyRepository.save(party1);
    }

    void updateAccount(FRAccountData accountDataDiff, FRAccount account, Set<String> accountIds) {
        OBAccount6 accountDiff = accountDataDiff.getAccount();
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
        Set<OBBalanceType1Code> types = new HashSet<>();
        for (OBCashBalance1 obBalanceDiff : accountDataDiff.getBalances()) {
            String accountId = accountDataDiff.getAccount().getAccountId();
            if (obBalanceDiff.getAccountId() != null && !obBalanceDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        String.format("The account id '%s' refers in a balance doesn't match the main account id '%s'",
                                obBalanceDiff.getAccountId(),
                                accountId));
            }
            if (types.contains(obBalanceDiff.getType())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("You can't add two balances of the same type '%s' for the same account ID '%s'",
                                obBalanceDiff.getType(),
                                accountId));

            }
            types.add(obBalanceDiff.getType());
            Optional<FRBalance> isBalance = balanceRepository.findByAccountIdAndBalanceType(
                    accountId,
                    obBalanceDiff.getType());

            if (isBalance.isPresent()) {
                OBCashBalance1 balance = isBalance.get().getBalance();

                if (obBalanceDiff.getAmount() != null) {
                    balance.setAmount(obBalanceDiff.getAmount());
                }
                if (obBalanceDiff.getType() != null) {
                    balance.setType(obBalanceDiff.getType());
                }
                if (obBalanceDiff.getCreditDebitIndicator() != null) {
                    balance.setCreditDebitIndicator(obBalanceDiff.getCreditDebitIndicator());
                }
                if (obBalanceDiff.getCreditLine() != null) {
                    balance.setCreditLine(obBalanceDiff.getCreditLine());
                }
                if (obBalanceDiff.getDateTime() != null) {
                    balance.setDateTime(obBalanceDiff.getDateTime());
                }
                balancesToSave.add(isBalance.get());
            } else {
                FRBalance balance1 = new FRBalance();
                balance1.setBalance(obBalanceDiff);
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

        OBParty2 partyDiff = accountDataDiff.getParty();

        if (partyDiff.getName() != null) {
            party1.getParty().setName(partyDiff.getName());
        }
        if (partyDiff.getPhone() != null) {
            party1.getParty().setPhone(partyDiff.getPhone());
        }
        if (partyDiff.getAddress() != null) {
            party1.getParty().setAddress(partyDiff.getAddress());
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
        for (OBBeneficiary5 obBeneficiaryDiff : accountDataDiff.getBeneficiaries()) {

            if (obBeneficiaryDiff.getAccountId() != null && !obBeneficiaryDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obBeneficiaryDiff.getAccountId() + "' refers in a beneficiary doesn't match the main account id '" + accountId + "'");
            }
            if (obBeneficiaryDiff.getBeneficiaryId() == null) {
                obBeneficiaryDiff.setAccountId(accountId);
                obBeneficiaryDiff.setBeneficiaryId(UUID.randomUUID().toString());
                FRBeneficiary beneficiary = new FRBeneficiary();
                beneficiary.setAccountId(accountId);
                beneficiary.setBeneficiary(obBeneficiaryDiff);
                beneficiary.setId(obBeneficiaryDiff.getBeneficiaryId());
                newBeneficiariesToSave.add(beneficiary);
            } else {
                Optional<FRBeneficiary> isBeneficiary = beneficiaryRepository.findById(obBeneficiaryDiff.getBeneficiaryId());
                if (isBeneficiary.isEmpty() || !isBeneficiary.get().getAccountId().equals(obBeneficiaryDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The beneficiary id '"
                            + obBeneficiaryDiff.getBeneficiaryId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                OBBeneficiary5 beneficiary = isBeneficiary.get().getBeneficiary();
                if (obBeneficiaryDiff.getReference() != null) {
                    beneficiary.setReference(obBeneficiaryDiff.getReference());
                }
                if (obBeneficiaryDiff.getCreditorAccount() != null) {
                    beneficiary.setCreditorAccount(obBeneficiaryDiff.getCreditorAccount());
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
        for (OBReadDirectDebit2DataDirectDebit obDirectDebitDiff : accountDataDiff.getDirectDebits()) {

            if (obDirectDebitDiff.getAccountId() != null && !obDirectDebitDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obDirectDebitDiff.getAccountId() + "' refers in a direct debit doesn't match the main account id '" + accountId + "'");
            }
            if (obDirectDebitDiff.getDirectDebitId() == null) {
                obDirectDebitDiff.setAccountId(accountId);
                obDirectDebitDiff.setDirectDebitId(UUID.randomUUID().toString());
                FRDirectDebit directDebit = new FRDirectDebit();
                directDebit.setAccountId(accountId);
                directDebit.setDirectDebit(obDirectDebitDiff);
                directDebit.setId(obDirectDebitDiff.getDirectDebitId());
                newDirectDebitsToSave.add(directDebit);
            } else {
                Optional<FRDirectDebit> isDirectDebit = directDebitRepository.findById(obDirectDebitDiff.getDirectDebitId());
                if (isDirectDebit.isEmpty() || !isDirectDebit.get().getAccountId().equals(obDirectDebitDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The direct debit id '"
                            + obDirectDebitDiff.getDirectDebitId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                OBReadDirectDebit2DataDirectDebit directDebit = isDirectDebit.get().getDirectDebit();
                if (obDirectDebitDiff.getName() != null) {
                    directDebit.setName(obDirectDebitDiff.getName());
                }
                if (obDirectDebitDiff.getDirectDebitStatusCode() != null) {
                    directDebit.setDirectDebitStatusCode(obDirectDebitDiff.getDirectDebitStatusCode());
                }
                if (obDirectDebitDiff.getMandateIdentification() != null) {
                    directDebit.setMandateIdentification(obDirectDebitDiff.getMandateIdentification());
                }
                if (obDirectDebitDiff.getPreviousPaymentAmount() != null) {
                    directDebit.setPreviousPaymentAmount(obDirectDebitDiff.getPreviousPaymentAmount());
                }
                if (obDirectDebitDiff.getPreviousPaymentDateTime() != null) {
                    directDebit.setPreviousPaymentDateTime(obDirectDebitDiff.getPreviousPaymentDateTime());
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
        for (OBStandingOrder6 obStandingOrderDiff : accountDataDiff.getStandingOrders()) {

            if (obStandingOrderDiff.getAccountId() != null && !obStandingOrderDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obStandingOrderDiff.getAccountId() + "' refers in a standing order doesn't match the main account id '" + accountId + "'");
            }
            if (obStandingOrderDiff.getStandingOrderId() == null) {
                obStandingOrderDiff.setAccountId(accountId);
                obStandingOrderDiff.setStandingOrderId(UUID.randomUUID().toString());
                FRStandingOrder standingOrder = new FRStandingOrder();
                standingOrder.setAccountId(accountId);
                standingOrder.setStandingOrder(obStandingOrderDiff);
                standingOrder.setId(obStandingOrderDiff.getStandingOrderId());
                standingOrder.setStatus(StandingOrderStatus.PENDING);
                newStandingOrdersToSave.add(standingOrder);
            } else {
                Optional<FRStandingOrder> isStandingOrder = standingOrderRepository.findById(obStandingOrderDiff.getStandingOrderId());
                if (isStandingOrder.isEmpty() || !isStandingOrder.get().getAccountId().equals(obStandingOrderDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The standing order id '"
                            + obStandingOrderDiff.getStandingOrderId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                OBStandingOrder6 standingOrder = isStandingOrder.get().getStandingOrder();
                if (obStandingOrderDiff.getFrequency() != null) {
                    standingOrder.setFrequency(obStandingOrderDiff.getFrequency());
                }
                if (obStandingOrderDiff.getReference() != null) {
                    standingOrder.setReference(obStandingOrderDiff.getReference());
                }
                if (obStandingOrderDiff.getFirstPaymentAmount() != null) {
                    standingOrder.setFirstPaymentAmount(obStandingOrderDiff.getFirstPaymentAmount());
                }
                if (obStandingOrderDiff.getFirstPaymentDateTime() != null) {
                    standingOrder.setFirstPaymentDateTime(obStandingOrderDiff.getFirstPaymentDateTime());
                }
                if (obStandingOrderDiff.getNextPaymentAmount() != null) {
                    standingOrder.setNextPaymentAmount(obStandingOrderDiff.getNextPaymentAmount());
                }
                if (obStandingOrderDiff.getNextPaymentDateTime() != null) {
                    standingOrder.setNextPaymentDateTime(obStandingOrderDiff.getNextPaymentDateTime());
                }
                if (obStandingOrderDiff.getFinalPaymentAmount() != null) {
                    standingOrder.setFinalPaymentAmount(obStandingOrderDiff.getFinalPaymentAmount());
                }
                if (obStandingOrderDiff.getFinalPaymentDateTime() != null) {
                    standingOrder.setFinalPaymentDateTime(obStandingOrderDiff.getFinalPaymentDateTime());
                }
                if (obStandingOrderDiff.getCreditorAccount() != null) {
                    standingOrder.setCreditorAccount(obStandingOrderDiff.getCreditorAccount());
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
        for (OBTransaction6 obTransactionDiff : accountDataDiff.getTransactions()) {

            if (obTransactionDiff.getAccountId() != null && !obTransactionDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obTransactionDiff.getAccountId() + "' refers in a transaction doesn't match the main account id '" + accountId + "'");
            }
            if (obTransactionDiff.getTransactionId() == null) {
                obTransactionDiff.setAccountId(accountId);
                obTransactionDiff.setTransactionId(UUID.randomUUID().toString());
                FRTransaction transaction = new FRTransaction();
                transaction.setAccountId(accountId);
                transaction.setBookingDateTime((obTransactionDiff.getBookingDateTime()));
                transaction.setTransaction(obTransactionDiff);
                transaction.setId(obTransactionDiff.getTransactionId());
                newTransactionsToSave.add(transaction);
            } else {
                Optional<FRTransaction> isTransaction = transactionRepository.findById(obTransactionDiff.getTransactionId());
                if (isTransaction.isEmpty() || !isTransaction.get().getAccountId().equals(obTransactionDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The transaction id '"
                            + obTransactionDiff.getTransactionId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                OBTransaction6 transaction = isTransaction.get().getTransaction();
                if (obTransactionDiff.getTransactionReference() != null) {
                    transaction.setTransactionReference(obTransactionDiff.getTransactionReference());
                }
                if (obTransactionDiff.getAmount() != null) {
                    transaction.setAmount(obTransactionDiff.getAmount());
                }
                if (obTransactionDiff.getCreditDebitIndicator() != null) {
                    transaction.setCreditDebitIndicator(obTransactionDiff.getCreditDebitIndicator());
                }
                if (obTransactionDiff.getStatus() != null) {
                    transaction.setStatus(obTransactionDiff.getStatus());
                }
                if (obTransactionDiff.getBookingDateTime() != null) {
                    transaction.setBookingDateTime(obTransactionDiff.getBookingDateTime());
                    isTransaction.get().setBookingDateTime(obTransactionDiff.getBookingDateTime());
                }
                if (obTransactionDiff.getValueDateTime() != null) {
                    transaction.setValueDateTime(obTransactionDiff.getValueDateTime());
                }
                if (obTransactionDiff.getTransactionInformation() != null) {
                    transaction.setTransactionInformation(obTransactionDiff.getTransactionInformation());
                }
                if (obTransactionDiff.getBankTransactionCode() != null) {
                    transaction.setBankTransactionCode(obTransactionDiff.getBankTransactionCode());
                }
                if (obTransactionDiff.getProprietaryBankTransactionCode() != null) {
                    transaction.setProprietaryBankTransactionCode(obTransactionDiff.getProprietaryBankTransactionCode());
                }
                if (obTransactionDiff.getBalance() != null) {
                    transaction.setBalance(obTransactionDiff.getBalance());
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
        for (OBStatement2 obStatement1Diff : accountDataDiff.getStatements()) {

            if (obStatement1Diff.getAccountId() != null && !obStatement1Diff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obStatement1Diff.getAccountId() + "' refers in a statement doesn't match the main account id '" + accountId + "'");
            }
            if (obStatement1Diff.getStatementId() == null) {
                obStatement1Diff.setAccountId(accountId);
                obStatement1Diff.setStatementId(UUID.randomUUID().toString());
                FRStatement statement1 = new FRStatement();
                statement1.setAccountId(accountId);
                statement1.setStatement(obStatement1Diff);
                statement1.setId(obStatement1Diff.getStatementId());
                newStatementsToSave.add(statement1);
            } else {
                Optional<FRStatement> isStatement = statementRepository.findById(obStatement1Diff.getStatementId());
                if (isStatement.isEmpty() || !isStatement.get().getAccountId().equals(obStatement1Diff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The statement id '"
                            + obStatement1Diff.getStatementId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                OBStatement2 statement = isStatement.get().getStatement();
                if (obStatement1Diff.getEndDateTime() != null) {
                    statement.setEndDateTime(obStatement1Diff.getEndDateTime());
                    isStatement.get().setEndDateTime(obStatement1Diff.getEndDateTime());
                }
                if (obStatement1Diff.getStartDateTime() != null) {
                    statement.setStartDateTime(obStatement1Diff.getStartDateTime());
                    isStatement.get().setStartDateTime(obStatement1Diff.getStartDateTime());
                }
                if (obStatement1Diff.getType() != null) {
                    statement.setType(obStatement1Diff.getType());
                }
                if (obStatement1Diff.getCreationDateTime() != null) {
                    statement.setCreationDateTime(obStatement1Diff.getCreationDateTime());
                }
                if (obStatement1Diff.getStatementAmount() != null) {
                    statement.setStatementAmount(obStatement1Diff.getStatementAmount());
                }
                if (obStatement1Diff.getStatementBenefit() != null) {
                    statement.setStatementBenefit(obStatement1Diff.getStatementBenefit());
                }
                if (obStatement1Diff.getStatementDateTime() != null) {
                    statement.setStatementDateTime(obStatement1Diff.getStatementDateTime());
                }
                if (obStatement1Diff.getStatementDescription() != null) {
                    statement.setStatementDescription(obStatement1Diff.getStatementDescription());
                }
                if (obStatement1Diff.getStatementFee() != null) {
                    statement.setStatementFee(obStatement1Diff.getStatementFee());
                }
                if (obStatement1Diff.getStatementInterest() != null) {
                    statement.setStatementInterest(obStatement1Diff.getStatementInterest());
                }
                if (obStatement1Diff.getStatementRate() != null) {
                    statement.setStatementRate(obStatement1Diff.getStatementRate());
                }
                if (obStatement1Diff.getStatementReference() != null) {
                    statement.setStatementReference(obStatement1Diff.getStatementReference());
                }
                if (obStatement1Diff.getStatementValue() != null) {
                    statement.setStatementValue(obStatement1Diff.getStatementValue());
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
        for (OBScheduledPayment3 OBScheduledPayment3Diff : accountDataDiff.getScheduledPayments()) {

            if (OBScheduledPayment3Diff.getAccountId() != null && !OBScheduledPayment3Diff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + OBScheduledPayment3Diff.getAccountId() + "' refers in a scheduled payment doesn't match the main account id '" + accountId + "'");
            }
            if (OBScheduledPayment3Diff.getScheduledPaymentId() == null) {
                OBScheduledPayment3Diff.setAccountId(accountId);
                OBScheduledPayment3Diff.setScheduledPaymentId(UUID.randomUUID().toString());
                FRScheduledPayment scheduledPayment1 = new FRScheduledPayment();
                scheduledPayment1.setAccountId(accountId);
                scheduledPayment1.setScheduledPayment(OBScheduledPayment3Diff);
                scheduledPayment1.setId(OBScheduledPayment3Diff.getScheduledPaymentId());
                scheduledPayment1.setStatus(ScheduledPaymentStatus.PENDING);
                newScheduledPaymentToSave.add(scheduledPayment1);
            } else {
                Optional<FRScheduledPayment> isScheduledPayment = scheduledPaymentRepository.findById(OBScheduledPayment3Diff.getScheduledPaymentId());
                if (isScheduledPayment.isEmpty() || !isScheduledPayment.get().getAccountId().equals(OBScheduledPayment3Diff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The scheduled payment id '"
                            + OBScheduledPayment3Diff.getScheduledPaymentId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                OBScheduledPayment3 scheduledPayment = isScheduledPayment.get().getScheduledPayment();
                if (OBScheduledPayment3Diff.getCreditorAccount() != null) {
                    scheduledPayment.setCreditorAccount(OBScheduledPayment3Diff.getCreditorAccount());
                }
                if (OBScheduledPayment3Diff.getInstructedAmount() != null) {
                    scheduledPayment.setInstructedAmount(OBScheduledPayment3Diff.getInstructedAmount());
                }
                if (OBScheduledPayment3Diff.getReference() != null) {
                    scheduledPayment.setReference(OBScheduledPayment3Diff.getReference());
                }
                if (OBScheduledPayment3Diff.getCreditorAgent() != null) {
                    scheduledPayment.setCreditorAgent(OBScheduledPayment3Diff.getCreditorAgent());
                }
                if (OBScheduledPayment3Diff.getScheduledPaymentDateTime() != null) {
                    scheduledPayment.setScheduledPaymentDateTime(OBScheduledPayment3Diff.getScheduledPaymentDateTime());
                }
                if (OBScheduledPayment3Diff.getScheduledType() != null) {
                    scheduledPayment.setScheduledType(OBScheduledPayment3Diff.getScheduledType());
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
        for (OBOffer1 offersDiff : accountDataDiff.getOffers()) {

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

                OBOffer1 offer = isOffers.get().getOffer();
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
