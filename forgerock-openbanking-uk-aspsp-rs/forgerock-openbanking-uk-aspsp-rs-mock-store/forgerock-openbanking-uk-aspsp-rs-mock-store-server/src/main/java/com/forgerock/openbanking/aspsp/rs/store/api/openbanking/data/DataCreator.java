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
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.org.openbanking.datamodel.account.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@NoArgsConstructor
public class DataCreator {

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
    private int accountLimit;

    @Autowired
    public DataCreator(FRAccountRepository accountsRepository, FRBalanceRepository balanceRepository,
                       FRBeneficiaryRepository beneficiaryRepository, FRDirectDebitRepository directDebitRepository,
                       FRProductRepository productRepository, FRStandingOrderRepository standingOrderRepository,
                       FRTransactionRepository transactionRepository, FRStatementRepository statementRepository,
                       FRScheduledPaymentRepository scheduledPaymentRepository, FRPartyRepository partyRepository,
                       FROfferRepository offerRepository,
                       @Value("${rs.data.upload.limit.documents}") Integer documentLimit,
                       @Value("${rs.data.upload.limit.accounts}") Integer accountLimit) {
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
        this.accountLimit = accountLimit;
    }

    List<FROffer> createOffers(FRAccountData accountData, Set<String> accountIds) {
        List<FROffer> offers = new ArrayList<>();
        for (OBOffer1 obOffer: accountData.getOffers()) {
            String accountId = obOffer.getAccountId() != null ? obOffer.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            obOffer.setAccountId(accountId);
            obOffer.setOfferId(UUID.randomUUID().toString());
            FROffer offer = new FROffer();
            offer.setAccountId(obOffer.getAccountId());
            offer.setOffer(obOffer);
            offer.setId(obOffer.getOfferId());
            offers.add(offer);
        }
        if (offerRepository.countByAccountIdIn(accountIds) + offers.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add offers as it has exceeded maximum limit of %s", documentLimit));
        }
        return offerRepository.saveAll(offers);
    }

    List<FRScheduledPayment> createScheduledPayments(FRAccountData accountData, Set<String> accountIds) {
        List<FRScheduledPayment> scheduledPayments = new ArrayList<>();
        for (OBScheduledPayment3 obScheduledPayment: accountData.getScheduledPayments()) {
            String accountId = obScheduledPayment.getAccountId() != null ? obScheduledPayment.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            obScheduledPayment.setAccountId(accountId);
            obScheduledPayment.setScheduledPaymentId(UUID.randomUUID().toString());
            FRScheduledPayment scheduledPayment = new FRScheduledPayment();
            scheduledPayment.setAccountId(obScheduledPayment.getAccountId());
            scheduledPayment.setScheduledPayment(obScheduledPayment);
            scheduledPayment.setId(obScheduledPayment.getScheduledPaymentId());
            scheduledPayment.setStatus(ScheduledPaymentStatus.PENDING);
            scheduledPayments.add(scheduledPayment);
        }
        if (scheduledPaymentRepository.countByAccountIdIn(accountIds) + scheduledPayments.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add scheduled payments as it has exceeded maximum limit of %s", documentLimit));
        }
        return scheduledPaymentRepository.saveAll(scheduledPayments);
    }

    List<FRStatement> createStatements(FRAccountData accountData, Set<String> accountIds) {
        List<FRStatement> statements = new ArrayList<>();
        for (OBStatement2 obStatement: accountData.getStatements()) {
            String accountId = obStatement.getAccountId() != null ? obStatement.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            obStatement.setAccountId(accountId);
            obStatement.setStatementId(UUID.randomUUID().toString());
            FRStatement statement = new FRStatement();
            statement.setAccountId(obStatement.getAccountId());
            statement.setStatement(obStatement);
            statement.setEndDateTime(obStatement.getEndDateTime());
            statement.setStartDateTime(obStatement.getStartDateTime());
            statement.setId(obStatement.getStatementId());
            statements.add(statement);
        }
        if (statementRepository.countByAccountIdIn(accountIds) + statements.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add statements as it has exceeded maximum limit of %s", documentLimit));
        }
        return statementRepository.saveAll(statements);
    }

    List<FRTransaction> createTransactions(FRAccountData accountData, Set<String> accountIds) {
        List<FRTransaction> transactions = new ArrayList<>();
        for (OBTransaction6 obTransaction: accountData.getTransactions()) {
            String accountId = obTransaction.getAccountId() != null ? obTransaction.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            obTransaction.setAccountId(accountId);
            obTransaction.setTransactionId(UUID.randomUUID().toString());
            FRTransaction transaction = new FRTransaction();
            transaction.setAccountId(obTransaction.getAccountId());
            transaction.setBookingDateTime(obTransaction.getBookingDateTime());
            transaction.setTransaction(obTransaction);
            transaction.setId(obTransaction.getTransactionId());
            transactions.add(transaction);
        }
        if (transactionRepository.countByAccountIdIn(accountIds) + transactions.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add transactions as it has exceeded maximum limit of %s", documentLimit));
        }
        return transactionRepository.saveAll(transactions);
    }

    List<FRStandingOrder> createStandingOrders(FRAccountData accountData, Set<String> accountIds) {
        List<FRStandingOrder> standingOrders = new ArrayList<>();
        for (OBStandingOrder6 obStandingOrder: accountData.getStandingOrders()) {
            String accountId = obStandingOrder.getAccountId() != null ? obStandingOrder.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            obStandingOrder.setAccountId(accountId);
            obStandingOrder.setStandingOrderId(UUID.randomUUID().toString());
            FRStandingOrder standingOrder = new FRStandingOrder();
            standingOrder.setAccountId(obStandingOrder.getAccountId());
            standingOrder.setStandingOrder(obStandingOrder);
            standingOrder.setId(obStandingOrder.getStandingOrderId());
            standingOrder.setStatus(StandingOrderStatus.PENDING);
            standingOrders.add(standingOrder);
        }
        if (standingOrderRepository.countByAccountIdIn(accountIds) + standingOrders.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add standing orders as it has exceeded maximum limit of %s", documentLimit));
        }
        return standingOrderRepository.saveAll(standingOrders);
    }

    List<FRDirectDebit> createDirectDebits(FRAccountData accountData, Set<String> accountIds) {
        List<FRDirectDebit> directDebits = new ArrayList<>();
        for (OBReadDirectDebit2DataDirectDebit obDirectDebit: accountData.getDirectDebits()) {
            String accountId = obDirectDebit.getAccountId() != null ? obDirectDebit.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            obDirectDebit.setAccountId(accountId);
            obDirectDebit.setDirectDebitId(UUID.randomUUID().toString());
            FRDirectDebit directDebit = new FRDirectDebit();
            directDebit.setAccountId(obDirectDebit.getAccountId());
            directDebit.setDirectDebit(obDirectDebit);
            directDebit.setId(obDirectDebit.getDirectDebitId());
            directDebits.add(directDebit);
        }
        if (directDebitRepository.countByAccountIdIn(accountIds) + directDebits.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add direct debits as it has exceeded maximum limit of %s", documentLimit));
        }
        return directDebitRepository.saveAll(directDebits);
    }

    List<FRBeneficiary> createBeneficiaries(FRAccountData accountData, Set<String> accountIds) {
        List<FRBeneficiary> beneficiaries = new ArrayList<>();
        for (OBBeneficiary5 obBeneficiary: accountData.getBeneficiaries()) {
            String accountId = obBeneficiary.getAccountId() != null ? obBeneficiary.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            obBeneficiary.setAccountId(accountId);
            obBeneficiary.setBeneficiaryId(UUID.randomUUID().toString());
            FRBeneficiary beneficiary = new FRBeneficiary();
            beneficiary.setAccountId(obBeneficiary.getAccountId());
            beneficiary.setBeneficiary(obBeneficiary);
            beneficiary.setId(obBeneficiary.getBeneficiaryId());
            beneficiaries.add(beneficiary);
        }
        if (beneficiaryRepository.countByAccountIdIn(accountIds) + beneficiaries.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add beneficiaries as it has exceeded maximum limit of %s", documentLimit));
        }
        return beneficiaryRepository.saveAll(beneficiaries);
    }

    Optional<OBParty2> createParty(FRAccountData accountData) {
        if (accountData.getParty() == null) {
            return Optional.empty();
        }
        accountData.getParty().setPartyId(UUID.randomUUID().toString());
        FRParty party = new FRParty();
        party.setAccountId(accountData.getAccount().getAccountId());
        party.setId(accountData.getParty().getPartyId());
        party.setParty(accountData.getParty());
        return Optional.of(partyRepository.save(party).getParty());
    }

    Optional<OBReadProduct2DataProduct> createProducts(FRAccountData accountData, Set<String> accountIds) {
        OBReadProduct2DataProduct obProduct = accountData.getProduct();
        if (obProduct == null) {
            return Optional.empty();
        }
        String accountId = obProduct.getAccountId() != null ? obProduct.getAccountId() : accountData.getAccount().getAccountId();
        if (!accountIds.contains(accountId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
        }
        obProduct.setProductId(UUID.randomUUID().toString());
        obProduct.setAccountId(accountId);
        FRProduct product = new FRProduct();
        product.setAccountId(accountId);
        product.setId(obProduct.getProductId());
        product.setProduct(obProduct);
        return Optional.of(productRepository.save(product).getProduct());
    }

    List<FRBalance> createBalances(FRAccountData accountData, Set<String> accountIds) {
        List<FRBalance> balances = new ArrayList<>();
        for (OBCashBalance1 obBalance: accountData.getBalances()) {
            String accountId = obBalance.getAccountId() != null ? obBalance.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            obBalance.setAccountId(accountId);
            // Check if balance type exists for account already
            Optional<FRBalance> isExists = balanceRepository.findByAccountIdAndBalanceType(obBalance.getAccountId(), obBalance.getType());
            if (isExists.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("A Balance with this Balance Type '%s' already exists for this Account Id:%s",
                                obBalance.getType(), obBalance.getAccountId()));
            }
            FRBalance balance = new FRBalance();
            balance.setAccountId(obBalance.getAccountId());
            balance.setBalance(obBalance);
            balances.add(balance);
        }
        if (balanceRepository.countByAccountIdIn(accountIds) + balances.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add balance as it has exceeded maximum limit of %s", documentLimit));
        }
        return balanceRepository.saveAll(balances);
    }

    FRAccount createAccount(FRAccountData accountData, String username) {
        FRAccount account = new FRAccount();
        account.setCreated(new Date());
        account.setId(UUID.randomUUID().toString());
        account.setUserID(username);
        accountData.getAccount().accountId(account.getId());
        account.setAccount(accountData.getAccount());
        account = accountsRepository.save(account);
        Example<FRAccount> example = Example.of(FRAccount.builder().userID(username).build());
        if (accountsRepository.count(example) > accountLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add account as it has exceeded maximum limit of %s", documentLimit));
        }
        return account;
    }
    
}
