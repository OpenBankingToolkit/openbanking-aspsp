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

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.offers.FROffer1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.products.FRProduct2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.party.FRParty2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.standingorders.FRStandingOrder5Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.transactions.FRTransaction5Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accounts.FRAccount4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.beneficiaries.FRBeneficiary4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.directdebits.FRDirectDebit4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.scheduledpayments.FRScheduledPayment4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.statements.FRStatement4Repository;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FROffer;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRProduct;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRParty;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_1.FRStandingOrder5;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_1.FRTransaction5;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRBeneficiary4;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRDirectDebit;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRScheduledPayment;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStatement;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_3.data.FRAccountData4;
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

    private FRAccount4Repository accountsRepository;
    private FRBalance1Repository balanceRepository;
    private FRBeneficiary4Repository beneficiaryRepository;
    private FRDirectDebit4Repository directDebitRepository;
    private FRProduct2Repository productRepository;
    private FRStandingOrder5Repository standingOrderRepository;
    private FRTransaction5Repository transactionRepository;
    private FRStatement4Repository statementRepository;
    private FRScheduledPayment4Repository scheduledPaymentRepository;
    private FRParty2Repository partyRepository;
    private FROffer1Repository offerRepository;
    private int documentLimit;
    private int accountLimit;

    @Autowired
    public DataCreator(FRAccount4Repository accountsRepository, FRBalance1Repository balanceRepository,
                       FRBeneficiary4Repository beneficiaryRepository, FRDirectDebit4Repository directDebitRepository,
                       FRProduct2Repository productRepository, FRStandingOrder5Repository standingOrderRepository,
                       FRTransaction5Repository transactionRepository, FRStatement4Repository statementRepository,
                       FRScheduledPayment4Repository scheduledPaymentRepository, FRParty2Repository partyRepository,
                       FROffer1Repository offerRepository,
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

    List<FROffer> createOffers(FRAccountData4 accountData, Set<String> accountIds) {
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

    List<FRScheduledPayment> createScheduledPayments(FRAccountData4 accountData, Set<String> accountIds) {
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

    List<FRStatement> createStatements(FRAccountData4 accountData, Set<String> accountIds) {
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

    List<FRTransaction5> createTransactions(FRAccountData4 accountData, Set<String> accountIds) {
        List<FRTransaction5> transactions = new ArrayList<>();
        for (OBTransaction5 obTransaction: accountData.getTransactions()) {
            String accountId = obTransaction.getAccountId() != null ? obTransaction.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            obTransaction.setAccountId(accountId);
            obTransaction.setTransactionId(UUID.randomUUID().toString());
            FRTransaction5 transaction = new FRTransaction5();
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

    List<FRStandingOrder5> createStandingOrders(FRAccountData4 accountData, Set<String> accountIds) {
        List<FRStandingOrder5> standingOrders = new ArrayList<>();
        for (OBStandingOrder5 obStandingOrder: accountData.getStandingOrders()) {
            String accountId = obStandingOrder.getAccountId() != null ? obStandingOrder.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            obStandingOrder.setAccountId(accountId);
            obStandingOrder.setStandingOrderId(UUID.randomUUID().toString());
            FRStandingOrder5 standingOrder = new FRStandingOrder5();
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

    List<FRDirectDebit> createDirectDebits(FRAccountData4 accountData, Set<String> accountIds) {
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

    List<FRBeneficiary4> createBeneficiaries(FRAccountData4 accountData, Set<String> accountIds) {
        List<FRBeneficiary4> beneficiaries = new ArrayList<>();
        for (OBBeneficiary4 obBeneficiary: accountData.getBeneficiaries()) {
            String accountId = obBeneficiary.getAccountId() != null ? obBeneficiary.getAccountId() : accountData.getAccount().getAccountId();
            if (!accountIds.contains(accountId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must specify and own the account ID to update");
            }
            obBeneficiary.setAccountId(accountId);
            obBeneficiary.setBeneficiaryId(UUID.randomUUID().toString());
            FRBeneficiary4 beneficiary = new FRBeneficiary4();
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

    Optional<OBParty2> createParty(FRAccountData4 accountData) {
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

    Optional<OBReadProduct2DataProduct> createProducts(FRAccountData4 accountData, Set<String> accountIds) {
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

    List<FRBalance> createBalances(FRAccountData4 accountData, Set<String> accountIds) {
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

    FRAccount createAccount(FRAccountData4 accountData, String username) {
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
