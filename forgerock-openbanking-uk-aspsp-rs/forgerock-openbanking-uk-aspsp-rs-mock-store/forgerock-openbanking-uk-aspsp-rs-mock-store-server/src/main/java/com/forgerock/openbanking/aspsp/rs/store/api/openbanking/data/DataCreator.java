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
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRAccountBeneficiary;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRCashBalance;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRDirectDebitData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FROfferData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRPartyData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRScheduledPaymentData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStandingOrderData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRTransactionData;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.*;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.data.FRAccountData;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.org.openbanking.datamodel.account.OBReadProduct2DataProduct;

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
        for (FROfferData offerData: accountData.getOffers()) {
            String accountId = offerData.getAccountId() != null ? offerData.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            offerData.setAccountId(accountId);
            offerData.setOfferId(UUID.randomUUID().toString());
            FROffer offer = new FROffer();
            offer.setAccountId(offerData.getAccountId());
            offer.setOffer(offerData);
            offer.setId(offerData.getOfferId());
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
        for (FRScheduledPaymentData scheduledPaymentData: accountData.getScheduledPayments()) {
            String accountId = scheduledPaymentData.getAccountId() != null ? scheduledPaymentData.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            scheduledPaymentData.setAccountId(accountId);
            scheduledPaymentData.setScheduledPaymentId(UUID.randomUUID().toString());
            FRScheduledPayment scheduledPayment = new FRScheduledPayment();
            scheduledPayment.setAccountId(scheduledPaymentData.getAccountId());
            scheduledPayment.setScheduledPayment(scheduledPaymentData);
            scheduledPayment.setId(scheduledPaymentData.getScheduledPaymentId());
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
        for (FRStatementData statementData: accountData.getStatements()) {
            String accountId = statementData.getAccountId() != null ? statementData.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            statementData.setAccountId(accountId);
            statementData.setStatementId(UUID.randomUUID().toString());
            FRStatement statement = new FRStatement();
            statement.setAccountId(statementData.getAccountId());
            statement.setStatement(statementData);
            statement.setEndDateTime(statementData.getEndDateTime());
            statement.setStartDateTime(statementData.getStartDateTime());
            statement.setId(statementData.getStatementId());
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
        for (FRTransactionData transactionData: accountData.getTransactions()) {
            String accountId = transactionData.getAccountId() != null ? transactionData.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            transactionData.setAccountId(accountId);
            transactionData.setTransactionId(UUID.randomUUID().toString());
            FRTransaction transaction = new FRTransaction();
            transaction.setAccountId(transactionData.getAccountId());
            transaction.setBookingDateTime(transactionData.getBookingDateTime());
            transaction.setTransaction(transactionData);
            transaction.setId(transactionData.getTransactionId());
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
        for (FRStandingOrderData standingOrderData: accountData.getStandingOrders()) {
            String accountId = standingOrderData.getAccountId() != null ? standingOrderData.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            standingOrderData.setAccountId(accountId);
            standingOrderData.setStandingOrderId(UUID.randomUUID().toString());
            FRStandingOrder standingOrder = new FRStandingOrder();
            standingOrder.setAccountId(standingOrderData.getAccountId());
            standingOrder.setStandingOrder(standingOrderData);
            standingOrder.setId(standingOrderData.getStandingOrderId());
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
        for (FRDirectDebitData directDebitData: accountData.getDirectDebits()) {
            String accountId = directDebitData.getAccountId() != null ? directDebitData.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            directDebitData.setAccountId(accountId);
            directDebitData.setDirectDebitId(UUID.randomUUID().toString());
            FRDirectDebit directDebit = new FRDirectDebit();
            directDebit.setAccountId(directDebitData.getAccountId());
            directDebit.setDirectDebit(directDebitData);
            directDebit.setId(directDebitData.getDirectDebitId());
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
        for (FRAccountBeneficiary accountBeneficiary: accountData.getBeneficiaries()) {
            String accountId = accountBeneficiary.getAccountId() != null ? accountBeneficiary.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            accountBeneficiary.setAccountId(accountId);
            accountBeneficiary.setBeneficiaryId(UUID.randomUUID().toString());
            FRBeneficiary beneficiary = new FRBeneficiary();
            beneficiary.setAccountId(accountBeneficiary.getAccountId());
            beneficiary.setBeneficiary(accountBeneficiary);
            beneficiary.setId(accountBeneficiary.getBeneficiaryId());
            beneficiaries.add(beneficiary);
        }
        if (beneficiaryRepository.countByAccountIdIn(accountIds) + beneficiaries.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add beneficiaries as it has exceeded maximum limit of %s", documentLimit));
        }
        return beneficiaryRepository.saveAll(beneficiaries);
    }

    Optional<FRPartyData> createParty(FRAccountData accountData) {
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
        for (FRCashBalance frCashBalance: accountData.getBalances()) {
            String accountId = frCashBalance.getAccountId() != null ? frCashBalance.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            frCashBalance.setAccountId(accountId);
            // Check if balance type exists for account already
            Optional<FRBalance> isExists = balanceRepository.findByAccountIdAndBalanceType(frCashBalance.getAccountId(), frCashBalance.getType());
            if (isExists.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("A Balance with this Balance Type '%s' already exists for this Account Id:%s",
                                frCashBalance.getType(), frCashBalance.getAccountId()));
            }
            FRBalance balance = new FRBalance();
            balance.setAccountId(frCashBalance.getAccountId());
            balance.setBalance(frCashBalance);
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
        accountData.getAccount().setAccountId(account.getId());
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
