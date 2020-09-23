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
import com.forgerock.openbanking.common.model.openbanking.persistence.v1_1.account.FRBalance1;
import com.forgerock.openbanking.common.model.openbanking.persistence.v2_0.account.FROffer1;
import com.forgerock.openbanking.common.model.openbanking.persistence.v2_0.account.FRProduct2;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_1.account.FRParty2;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_1.account.FRStandingOrder5;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_1.account.FRTransaction5;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_3.account.FRAccount4;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_3.account.FRBeneficiary4;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_3.account.FRDirectDebit4;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_3.account.FRScheduledPayment4;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_3.account.FRStatement4;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_3.account.data.FRAccountData4;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_3.account.data.FRUserData4;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller("DataApi")
@Slf4j
public class DataApiController implements DataApi {

    private final FRAccount4Repository accountsRepository;
    private final FRBalance1Repository balanceRepository;
    private final FRBeneficiary4Repository beneficiaryRepository;
    private final FRDirectDebit4Repository directDebitRepository;
    private final FRProduct2Repository productRepository;
    private final FRStandingOrder5Repository standingOrderRepository;
    private final FRTransaction5Repository transactionRepository;
    private final FRStatement4Repository statementRepository;
    private final FRScheduledPayment4Repository scheduledPayment1Repository;
    private final FRParty2Repository partyRepository;
    private final FROffer1Repository offerRepository;
    private final DataUpdater dataUpdater;
    private final DataCreator dataCreator;

    public DataApiController(FRDirectDebit4Repository directDebitRepository, FRAccount4Repository accountsRepository,
                             FRBalance1Repository balanceRepository, FRBeneficiary4Repository beneficiaryRepository,
                             FRProduct2Repository productRepository, FRStandingOrder5Repository standingOrderRepository,
                             FRTransaction5Repository transactionRepository, FRStatement4Repository statementRepository,
                             DataCreator dataCreator, FRScheduledPayment4Repository scheduledPayment1Repository,
                             FRParty2Repository partyRepository, DataUpdater dataUpdater, FROffer1Repository offerRepository) {
        this.directDebitRepository = directDebitRepository;
        this.accountsRepository = accountsRepository;
        this.balanceRepository = balanceRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.productRepository = productRepository;
        this.standingOrderRepository = standingOrderRepository;
        this.transactionRepository = transactionRepository;
        this.statementRepository = statementRepository;
        this.dataCreator = dataCreator;
        this.scheduledPayment1Repository = scheduledPayment1Repository;
        this.partyRepository = partyRepository;
        this.dataUpdater = dataUpdater;
        this.offerRepository = offerRepository;
    }

    @Override
    public ResponseEntity<Page<FRAccountData4>> exportAccountData(
            @PageableDefault Pageable pageable
    ) {
        List<FRAccountData4> accountDatas = new ArrayList<>();
        Page<FRAccount4> page = accountsRepository.findAll(pageable);
        // process last page
        for (FRAccount4 account : page.getContent()) {
            accountDatas.add(getAccount(account));
        }
        return ResponseEntity.ok(new PageImpl<>(accountDatas, page.getPageable(), page.getTotalElements()));
    }

    @Override
    public ResponseEntity<Boolean> hasData(
            @RequestParam("userId") String userId
    ) {
        return ResponseEntity.ok(accountsRepository.findByUserID(userId).size() > 0);
    }

    @Override
    public ResponseEntity<FRUserData4> exportUserData(
            @RequestParam("userId") String userId
    ) {
        FRUserData4 userData = new FRUserData4(userId);
        for (FRAccount4 account : accountsRepository.findByUserID(userId)) {
            userData.addAccountData(getAccount(account));
        }

        FRParty2 byUserId = partyRepository.findByUserId(userId);
        if (byUserId != null) {
            userData.setParty(byUserId.getParty());
        }
        return ResponseEntity.ok(userData);
    }

    @Override
    public ResponseEntity updateUserData(
            @RequestBody FRUserData4 userData
    ) {

        dataUpdater.updateParty(userData);

        Set<String> accountIds = accountsRepository.findByUserID(userData.getUserName())
                .stream()
                .map(FRAccount4::getId)
                .collect(Collectors.toSet());
        for (FRAccountData4 accountDataDiff : userData.getAccountDatas()) {

            String accountId = accountDataDiff.getAccount().getAccountId();
            //Account
            Optional<FRAccount4> isAccount = accountsRepository.findById(accountId);
            if (isAccount.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account ID '" + accountId + "' doesn't exist");
            }
            FRAccount4 account = isAccount.get();
            if (!account.getUserID().equals(userData.getUserName())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account ID '"
                        + accountDataDiff.getAccount().getAccountId() + "' is not owned by user '" + userData.getUserName() + "'");

            }

            dataUpdater.updateAccount(accountDataDiff, account, accountIds);
            dataUpdater.updateBalances(accountDataDiff, accountIds);
            dataUpdater.updateProducts(accountDataDiff, accountIds);
            dataUpdater.updateParty(accountDataDiff, accountIds);
            dataUpdater.updateBeneficiaries(accountDataDiff, accountIds);
            dataUpdater.updateDirectDebits(accountDataDiff, accountIds);
            dataUpdater.updateStandingOrders(accountDataDiff, accountIds);
            dataUpdater.updateTransactions(accountDataDiff, accountIds);
            dataUpdater.updateStatements(accountDataDiff, accountIds);
            dataUpdater.updateScheduledPayments(accountDataDiff, accountIds);
            dataUpdater.updateOffers(accountDataDiff, accountIds);
        }
        return exportUserData(userData.getUserName());
    }

    @Override
    public ResponseEntity importUserData(
            @RequestBody FRUserData4 userData
    ) {
        FRUserData4 userDataResponse = new FRUserData4(userData.getUserName());
        if (userData.getParty() != null) {
            FRParty2 party1 = partyRepository.findByUserId(userData.getUserName());

            //Party
            if (party1 != null) {
                userData.getParty().setPartyId(party1.getId());
            }

            FRParty2 party = new FRParty2();
            party.setUserId(userData.getUserName());
            party.setParty(userData.getParty());
            party.setId(userData.getParty().getPartyId());
            userDataResponse.setParty(partyRepository.save(party).getParty());

        }

        Set<String> existingAccountIds = accountsRepository.findByUserID(userData.getUserName())
                .stream()
                .map(FRAccount4::getId)
                .collect(Collectors.toSet());

        for (FRAccountData4 accountData : userData.getAccountDatas()) {
            FRAccountData4 accountDataResponse = new FRAccountData4();

            //Account
            if (accountData.getAccount() != null) {
                accountDataResponse.setAccount(dataCreator.createAccount(accountData, userData.getUserName()).getAccount());
                existingAccountIds.add(accountDataResponse.getAccount().getAccountId());
            }
            //Product
            dataCreator.createProducts(accountData, existingAccountIds).ifPresent(accountDataResponse::setProduct);
            //Party
            dataCreator.createParty(accountData).ifPresent(accountDataResponse::setParty);
            //Balance
            dataCreator.createBalances(accountData, existingAccountIds).forEach(b -> accountDataResponse.addBalance(b.getBalance()));
            //Beneficiaries
            dataCreator.createBeneficiaries(accountData, existingAccountIds).forEach(b -> accountDataResponse.addBeneficiary(b.getBeneficiary()));
            //Direct debits
            dataCreator.createDirectDebits(accountData, existingAccountIds).forEach(d -> accountDataResponse.addDirectDebit(d.getDirectDebit()));
            //Standing orders
            dataCreator.createStandingOrders(accountData, existingAccountIds).forEach(d -> accountDataResponse.addStandingOrder(d.getStandingOrder()));
            //Transactions
            dataCreator.createTransactions(accountData, existingAccountIds).forEach(d -> accountDataResponse.addTransaction(d.getTransaction()));
            //Statements
            dataCreator.createStatements(accountData, existingAccountIds).forEach(d -> accountDataResponse.addStatement(d.getStatement()));
            //Scheduled payments
            dataCreator.createScheduledPayments(accountData, existingAccountIds).forEach(d -> accountDataResponse.addScheduledPayment(d.getScheduledPayment()));
            //offers
            dataCreator.createOffers(accountData, existingAccountIds).forEach(d -> accountDataResponse.addOffer(d.getOffer()));

            userDataResponse.addAccountData(accountDataResponse);
        }
        return ResponseEntity.ok(userDataResponse);
    }

    @Override
    public ResponseEntity<Boolean> deleteUserData(
            @RequestParam("userId") String userId
    ) {
        Collection<FRAccount4> accounts = accountsRepository.findByUserID(userId);
        for (FRAccount4 account : accounts) {
            deleteAccount(account, userId);
        }
        return ResponseEntity.ok(accounts.size() > 0);
    }

    private void deleteAccount(FRAccount4 account, String userId) {
        accountsRepository.deleteById(account.getId());
        balanceRepository.deleteBalanceByAccountId(account.getId());
        productRepository.deleteProductByAccountId(account.getId());
        beneficiaryRepository.deleteBeneficiaryByAccountId(account.getId());
        directDebitRepository.deleteDirectDebitByAccountId(account.getId());
        standingOrderRepository.deleteStandingOrderByAccountId(account.getId());
        transactionRepository.deleteTransactionByAccountId(account.getId());
        statementRepository.deleteFRStatement4ByAccountId(account.getId());
        scheduledPayment1Repository.deleteFRScheduledPayment1ByAccountId(account.getId());
        partyRepository.deleteFRParty2ByAccountId(account.getId());
        offerRepository.deleteFROffer1ByAccountId(account.getId());
        partyRepository.deleteFRParty2ByAccountId(userId);

    }

    private FRAccountData4 getAccount(FRAccount4 account) {
        FRAccountData4 accountData = new FRAccountData4();
        accountData.setAccount(account.getAccount());

        Page<FRProduct2> products = productRepository.findByAccountId(account.getId(), (PageRequest.of(0, 1)));
        if (!products.getContent().isEmpty()) {
            accountData.setProduct(products.getContent().get(0).getProduct());
        }
        {
            final int pageLimit = 300;
            int pageNumber = 0;
            Page<FRBalance1> page = balanceRepository.findByAccountId(account.getId(), PageRequest.of(pageNumber, pageLimit));
            while (page.hasNext()) {
                for (FRBalance1 balance : page.getContent()) {
                    accountData.addBalance(balance.getBalance());
                }
                page = balanceRepository.findAll(PageRequest.of(pageNumber++, pageLimit));
            }
            // process last page
            for (FRBalance1 balance : page.getContent()) {
                accountData.addBalance(balance.getBalance());
            }
        }
        {
            final int pageLimit = 300;
            int pageNumber = 0;
            Page<FRBeneficiary4> page = beneficiaryRepository.findByAccountId(account.getId(), PageRequest.of(pageNumber, pageLimit));
            while (page.hasNext()) {
                for (FRBeneficiary4 beneficiary : page.getContent()) {
                    accountData.addBeneficiary(beneficiary.getBeneficiary());
                }
                page = beneficiaryRepository.findAll(PageRequest.of(pageNumber++, pageLimit));
            }
            // process last page
            for (FRBeneficiary4 beneficiary : page.getContent()) {
                accountData.addBeneficiary(beneficiary.getBeneficiary());
            }
        }
        {
            final int pageLimit = 300;
            int pageNumber = 0;
            Page<FRDirectDebit4> page = directDebitRepository.findByAccountId(account.getId(), PageRequest.of(pageNumber, pageLimit));
            while (page.hasNext()) {
                for (FRDirectDebit4 directDebit : page.getContent()) {
                    accountData.addDirectDebit(directDebit.getDirectDebit());
                }
                page = directDebitRepository.findAll(PageRequest.of(pageNumber++, pageLimit));
            }
            // process last page
            for (FRDirectDebit4 directDebit : page.getContent()) {
                accountData.addDirectDebit(directDebit.getDirectDebit());
            }
        }
        {
            final int pageLimit = 300;
            int pageNumber = 0;
            Page<FRStandingOrder5> page = standingOrderRepository.findByAccountId(account.getId(), PageRequest.of(pageNumber, pageLimit));
            while (page.hasNext()) {
                for (FRStandingOrder5 standingOrder : page.getContent()) {
                    accountData.addStandingOrder(standingOrder.getStandingOrder());
                }
                page = standingOrderRepository.findAll(PageRequest.of(pageNumber++, pageLimit));
            }
            // process last page
            for (FRStandingOrder5 standingOrder : page.getContent()) {
                accountData.addStandingOrder(standingOrder.getStandingOrder());
            }
        }
        {
            final int pageLimit = 300;
            int pageNumber = 0;
            Page<FRTransaction5> page = transactionRepository.findByAccountId(account.getId(), PageRequest.of(pageNumber, pageLimit));
            while (page.hasNext()) {
                for (FRTransaction5 transaction : page.getContent()) {
                    accountData.addTransaction(transaction.getTransaction());
                }
                page = transactionRepository.findAll(PageRequest.of(pageNumber++, pageLimit));
            }
            // process last page
            for (FRTransaction5 transaction : page.getContent()) {
                accountData.addTransaction(transaction.getTransaction());
            }
        }
        {
            final int pageLimit = 300;
            int pageNumber = 0;
            Page<FRStatement4> page = statementRepository.findByAccountId(account.getId(), PageRequest.of(pageNumber, pageLimit));
            while (page.hasNext()) {
                for (FRStatement4 statement1 : page.getContent()) {
                    accountData.addStatement(statement1.getStatement());
                }
                page = statementRepository.findAll(PageRequest.of(pageNumber++, pageLimit));
            }
            // process last page
            for (FRStatement4 statement1 : page.getContent()) {
                accountData.addStatement(statement1.getStatement());
            }
        }
        {
            final int pageLimit = 300;
            int pageNumber = 0;
            Page<FRScheduledPayment4> page = scheduledPayment1Repository.findByAccountId(account.getId(), PageRequest.of(pageNumber, pageLimit));
            while (page.hasNext()) {
                for (FRScheduledPayment4 scheduledPayment1 : page.getContent()) {
                    accountData.addScheduledPayment(scheduledPayment1.getScheduledPayment());
                }
                page = scheduledPayment1Repository.findAll(PageRequest.of(pageNumber++, pageLimit));
            }
            // process last page
            for (FRScheduledPayment4 scheduledPayment1 : page.getContent()) {
                accountData.addScheduledPayment(scheduledPayment1.getScheduledPayment());
            }
        }
        {
            FRParty2 party = partyRepository.findByAccountId(account.getId());
            if (party != null) {
                accountData.setParty(party.getParty());
            }
        }
        {
            final int pageLimit = 300;
            int pageNumber = 0;
            Page<FROffer1> page = offerRepository.findByAccountId(account.getId(), PageRequest.of(pageNumber, pageLimit));
            while (page.hasNext()) {
                for (FROffer1 offer1 : page.getContent()) {
                    accountData.addOffer(offer1.getOffer());
                }
                page = offerRepository.findAll(PageRequest.of(pageNumber++, pageLimit));
            }
            // process last page
            for (FROffer1 offer1 : page.getContent()) {
                accountData.addOffer(offer1.getOffer());
            }
        }
        return accountData;
    }
}
