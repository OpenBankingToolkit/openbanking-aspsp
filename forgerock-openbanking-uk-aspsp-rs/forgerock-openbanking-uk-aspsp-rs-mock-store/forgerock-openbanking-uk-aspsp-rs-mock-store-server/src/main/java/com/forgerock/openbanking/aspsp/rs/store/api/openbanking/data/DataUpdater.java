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
import com.forgerock.openbanking.aspsp.rs.store.repository.customerinfo.FRCustomerInfoRepository;
import com.forgerock.openbanking.common.model.data.FRAccountData;
import com.forgerock.openbanking.common.model.data.FRCustomerInfo;
import com.forgerock.openbanking.common.model.data.FRUserData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.*;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRBalanceType;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.*;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.google.common.collect.ImmutableList;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.org.openbanking.datamodel.account.*;

import java.util.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountBeneficiaryConverter.toFRAccountBeneficiary;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRCashBalanceConverter.toFRCashBalance;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRDirectDebitConverter.toFRDirectDebitData;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRFinancialAccountConverter.toFRFinancialAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FROfferConverter.toFROfferData;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRPartyConverter.toFRPartyData;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRScheduledPaymentConverter.toFRScheduledPaymentData;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRStandingOrderConverter.toFRStandingOrderData;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRStatementConverter.toFRStatementData;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRTransactionConverter.toFRTransactionData;

@Service
@NoArgsConstructor
@Slf4j
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
    private FRCustomerInfoRepository customerInfoRepository;
    private int documentLimit;

    @Autowired
    public DataUpdater(FRAccountRepository accountsRepository, FRBalanceRepository balanceRepository,
                       FRBeneficiaryRepository beneficiaryRepository, FRDirectDebitRepository directDebitRepository,
                       FRProductRepository productRepository, FRStandingOrderRepository standingOrderRepository,
                       FRTransactionRepository transactionRepository, FRStatementRepository statementRepository,
                       FRScheduledPaymentRepository scheduledPaymentRepository, FRPartyRepository partyRepository,
                       FROfferRepository offerRepository, FRCustomerInfoRepository customerInfoRepository,
                       @Value("${rs.data.upload.limit.documents:5000}") Integer documentLimit) {
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
        this.customerInfoRepository = customerInfoRepository;
    }

    void updateCustomerInfo(FRUserData userData){
        FRCustomerInfo existingCustomerInfo = customerInfoRepository.findByUserID(userData.getUserName());
        if(existingCustomerInfo != null){
            FRCustomerInfo newCustomerInfo = userData.getCustomerInfo();
            if(!newCustomerInfo.getId().equals(existingCustomerInfo.getId())){
                String errorMessage = String.format("The customerInfo ID '%s' in the provided data does not match " +
                                "that in the existing data '%s' for user '%s'", newCustomerInfo.getId(),
                        existingCustomerInfo.getId(), userData.getUserName());
                log.info("updateCustomerInfo() - {}",  errorMessage);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        errorMessage);
            }
            if(newCustomerInfo.getAddress() != null){
                existingCustomerInfo.setAddress(newCustomerInfo.getAddress());
            }
            if(newCustomerInfo.getPartyId() != null){
                existingCustomerInfo.setPartyId(newCustomerInfo.getPartyId());
            }
            if(newCustomerInfo.getBirthdate() != null){
                existingCustomerInfo.setBirthdate(newCustomerInfo.getBirthdate());
            }
            if(newCustomerInfo.getEmail() != null){
                existingCustomerInfo.setEmail(newCustomerInfo.getEmail());
            }
            if(newCustomerInfo.getFamilyName() != null){
                existingCustomerInfo.setFamilyName(newCustomerInfo.getFamilyName());
            }
            if(newCustomerInfo.getGivenName() != null){
                existingCustomerInfo.setGivenName(existingCustomerInfo.getGivenName());
            }
            if(newCustomerInfo.getInitials() != null){
                existingCustomerInfo.setInitials(newCustomerInfo.getInitials());
            }
            if(newCustomerInfo.getPhoneNumber() != null){
                existingCustomerInfo.setPhoneNumber(newCustomerInfo.getPhoneNumber());
            }
            if(newCustomerInfo.getTitle() != null){
                existingCustomerInfo.setTitle(newCustomerInfo.getTitle());
            }
            customerInfoRepository.save(existingCustomerInfo);
        }
    }

    void updateParty(FRUserData userData) {
        if (userData.getParty() == null) {
            return;
        }
        FRPartyData frPartyDiff = toFRPartyData(userData.getParty());
        FRParty frParty = partyRepository.findByUserId(userData.getUserName());
        if (!frParty.getId().equals(frPartyDiff.getPartyId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    String.format("the party ID '%s' doesn't match '%s' for user '%s'",
                            frPartyDiff.getPartyId(),
                            frParty.getId(),
                            userData.getUserName()));
        }
        if (frPartyDiff.getName() != null) {
            frParty.getParty().setName(frPartyDiff.getName());
        }
        if (frPartyDiff.getPhone() != null) {
            frParty.getParty().setPhone(frPartyDiff.getPhone());
        }
        if (frPartyDiff.getAddresses() != null) {
            frParty.getParty().setAddresses(frPartyDiff.getAddresses());
        }
        if (frPartyDiff.getEmailAddress() != null) {
            frParty.getParty().setEmailAddress(frPartyDiff.getEmailAddress());
        }
        if (frPartyDiff.getMobile() != null) {
            frParty.getParty().setMobile(frPartyDiff.getMobile());
        }
        if (frPartyDiff.getPartyNumber() != null) {
            frParty.getParty().setPartyNumber(frPartyDiff.getPartyNumber());
        }
        if (frPartyDiff.getPartyType() != null) {
            frParty.getParty().setPartyType(frPartyDiff.getPartyType());
        }
        partyRepository.save(frParty);
    }

    void updateAccount(FRAccountData accountDataDiff, FRAccount account, Set<String> accountIds) {
        FRFinancialAccount frAccountDiff = toFRFinancialAccount(accountDataDiff.getAccount());
        if (frAccountDiff.getCurrency() != null) {
            account.getAccount().setCurrency(frAccountDiff.getCurrency());
        }
        if (frAccountDiff.getNickname() != null) {
            account.getAccount().setNickname(frAccountDiff.getNickname());
        }
        if (frAccountDiff.getAccounts() != null) {
            account.getAccount().setAccounts(frAccountDiff.getAccounts());
        }
        if (frAccountDiff.getServicer() != null) {
            account.getAccount().setServicer(frAccountDiff.getServicer());
        }
        accountsRepository.save(account);
    }

    void updateBalances(FRAccountData accountDataDiff, Set<String> accountIds) {
        //Balance
        List<FRBalance> balancesToSave = new ArrayList<>();
        List<FRBalance> newBalancesToSave = new ArrayList<>();
        Set<FRBalanceType> balanceTypes = new HashSet<>();
        for (OBCashBalance1 obBalanceDiff : accountDataDiff.getBalances()) {
            String accountId = accountDataDiff.getAccount().getAccountId();
            if (obBalanceDiff.getAccountId() != null && !obBalanceDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        String.format("The account id '%s' refers in a balance doesn't match the main account id '%s'",
                                obBalanceDiff.getAccountId(),
                                accountId));
            }

            FRCashBalance frCashBalanceDiff = toFRCashBalance(obBalanceDiff);
            FRBalanceType balanceType = frCashBalanceDiff.getType();
            if (balanceTypes.contains(balanceType)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("You can't add two balances of the same type '%s' for the same account ID '%s'",
                                balanceType,
                                accountId));

            }
            balanceTypes.add(balanceType);
            Optional<FRBalance> isBalance = balanceRepository.findByAccountIdAndBalanceType(
                    accountId,
                    balanceType);

            if (isBalance.isPresent()) {
                FRCashBalance balance = isBalance.get().getBalance();

                if (frCashBalanceDiff.getAmount() != null) {
                    balance.setAmount(frCashBalanceDiff.getAmount());
                }
                if (balanceType != null) {
                    balance.setType(balanceType);
                }
                if (frCashBalanceDiff.getCreditDebitIndicator() != null) {
                    balance.setCreditDebitIndicator(frCashBalanceDiff.getCreditDebitIndicator());
                }
                if (frCashBalanceDiff.getCreditLines() != null) {
                    balance.setCreditLines(frCashBalanceDiff.getCreditLines());
                }
                if (frCashBalanceDiff.getDateTime() != null) {
                    balance.setDateTime(frCashBalanceDiff.getDateTime());
                }
                balancesToSave.add(isBalance.get());
            } else {
                FRBalance balance1 = new FRBalance();
                balance1.setBalance(frCashBalanceDiff);
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

        FRPartyData frPartyDiff = toFRPartyData(accountDataDiff.getParty());
        FRParty frParty = isParty.get();
        if (frPartyDiff.getName() != null) {
            frParty.getParty().setName(frPartyDiff.getName());
        }
        if (frPartyDiff.getPhone() != null) {
            frParty.getParty().setPhone(frPartyDiff.getPhone());
        }
        if (frPartyDiff.getAddresses() != null) {
            frParty.getParty().setAddresses(frPartyDiff.getAddresses());
        }
        if (frPartyDiff.getEmailAddress() != null) {
            frParty.getParty().setEmailAddress(frPartyDiff.getEmailAddress());
        }
        if (frPartyDiff.getMobile() != null) {
            frParty.getParty().setMobile(frPartyDiff.getMobile());
        }
        if (frPartyDiff.getPartyNumber() != null) {
            frParty.getParty().setPartyNumber(frPartyDiff.getPartyNumber());
        }
        if (frPartyDiff.getPartyType() != null) {
            frParty.getParty().setPartyType(frPartyDiff.getPartyType());
        }
        partyRepository.save(frParty);
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

            FRAccountBeneficiary frBeneficiaryDiff = toFRAccountBeneficiary(obBeneficiaryDiff);
            if (frBeneficiaryDiff.getBeneficiaryId() == null) {
                frBeneficiaryDiff.setAccountId(accountId);
                frBeneficiaryDiff.setBeneficiaryId(UUID.randomUUID().toString());
                FRBeneficiary beneficiary = new FRBeneficiary();
                beneficiary.setAccountId(accountId);
                beneficiary.setBeneficiary(frBeneficiaryDiff);
                beneficiary.setId(frBeneficiaryDiff.getBeneficiaryId());
                newBeneficiariesToSave.add(beneficiary);
            } else {
                Optional<FRBeneficiary> isBeneficiary = beneficiaryRepository.findById(frBeneficiaryDiff.getBeneficiaryId());
                if (isBeneficiary.isEmpty() || !isBeneficiary.get().getAccountId().equals(frBeneficiaryDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The beneficiary id '"
                            + frBeneficiaryDiff.getBeneficiaryId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                FRAccountBeneficiary beneficiary = isBeneficiary.get().getBeneficiary();
                if (frBeneficiaryDiff.getReference() != null) {
                    beneficiary.setReference(frBeneficiaryDiff.getReference());
                }
                if (frBeneficiaryDiff.getCreditorAccount() != null) {
                    beneficiary.setCreditorAccount(frBeneficiaryDiff.getCreditorAccount());
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

            FRDirectDebitData frDirectDebitDiff = toFRDirectDebitData(obDirectDebitDiff);
            if (frDirectDebitDiff.getDirectDebitId() == null) {
                frDirectDebitDiff.setAccountId(accountId);
                frDirectDebitDiff.setDirectDebitId(UUID.randomUUID().toString());
                FRDirectDebit directDebit = new FRDirectDebit();
                directDebit.setAccountId(accountId);
                directDebit.setDirectDebit(frDirectDebitDiff);
                directDebit.setId(frDirectDebitDiff.getDirectDebitId());
                newDirectDebitsToSave.add(directDebit);
            } else {
                Optional<FRDirectDebit> isDirectDebit = directDebitRepository.findById(frDirectDebitDiff.getDirectDebitId());
                if (isDirectDebit.isEmpty() || !isDirectDebit.get().getAccountId().equals(frDirectDebitDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The direct debit id '"
                            + frDirectDebitDiff.getDirectDebitId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                FRDirectDebitData directDebit = isDirectDebit.get().getDirectDebit();
                if (frDirectDebitDiff.getName() != null) {
                    directDebit.setName(frDirectDebitDiff.getName());
                }
                if (frDirectDebitDiff.getDirectDebitStatusCode() != null) {
                    directDebit.setDirectDebitStatusCode(frDirectDebitDiff.getDirectDebitStatusCode());
                }
                if (frDirectDebitDiff.getMandateIdentification() != null) {
                    directDebit.setMandateIdentification(frDirectDebitDiff.getMandateIdentification());
                }
                if (frDirectDebitDiff.getPreviousPaymentAmount() != null) {
                    directDebit.setPreviousPaymentAmount(frDirectDebitDiff.getPreviousPaymentAmount());
                }
                if (frDirectDebitDiff.getPreviousPaymentDateTime() != null) {
                    directDebit.setPreviousPaymentDateTime(frDirectDebitDiff.getPreviousPaymentDateTime());
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

            FRStandingOrderData frStandingOrderDiff = toFRStandingOrderData(obStandingOrderDiff);
            if (frStandingOrderDiff.getStandingOrderId() == null) {
                frStandingOrderDiff.setAccountId(accountId);
                frStandingOrderDiff.setStandingOrderId(UUID.randomUUID().toString());
                FRStandingOrder standingOrder = new FRStandingOrder();
                standingOrder.setAccountId(accountId);
                standingOrder.setStandingOrder(frStandingOrderDiff);
                standingOrder.setId(frStandingOrderDiff.getStandingOrderId());
                standingOrder.setStatus(StandingOrderStatus.PENDING);
                newStandingOrdersToSave.add(standingOrder);
            } else {
                Optional<FRStandingOrder> isStandingOrder = standingOrderRepository.findById(frStandingOrderDiff.getStandingOrderId());
                if (isStandingOrder.isEmpty() || !isStandingOrder.get().getAccountId().equals(frStandingOrderDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The standing order id '"
                            + frStandingOrderDiff.getStandingOrderId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                FRStandingOrderData standingOrder = isStandingOrder.get().getStandingOrder();
                if (frStandingOrderDiff.getFrequency() != null) {
                    standingOrder.setFrequency(frStandingOrderDiff.getFrequency());
                }
                if (frStandingOrderDiff.getReference() != null) {
                    standingOrder.setReference(frStandingOrderDiff.getReference());
                }
                if (frStandingOrderDiff.getFirstPaymentAmount() != null) {
                    standingOrder.setFirstPaymentAmount(frStandingOrderDiff.getFirstPaymentAmount());
                }
                if (frStandingOrderDiff.getFirstPaymentDateTime() != null) {
                    standingOrder.setFirstPaymentDateTime(frStandingOrderDiff.getFirstPaymentDateTime());
                }
                if (frStandingOrderDiff.getNextPaymentAmount() != null) {
                    standingOrder.setNextPaymentAmount(frStandingOrderDiff.getNextPaymentAmount());
                }
                if (frStandingOrderDiff.getNextPaymentDateTime() != null) {
                    standingOrder.setNextPaymentDateTime(frStandingOrderDiff.getNextPaymentDateTime());
                }
                if (frStandingOrderDiff.getFinalPaymentAmount() != null) {
                    standingOrder.setFinalPaymentAmount(frStandingOrderDiff.getFinalPaymentAmount());
                }
                if (frStandingOrderDiff.getFinalPaymentDateTime() != null) {
                    standingOrder.setFinalPaymentDateTime(frStandingOrderDiff.getFinalPaymentDateTime());
                }
                if (frStandingOrderDiff.getCreditorAccount() != null) {
                    standingOrder.setCreditorAccount(frStandingOrderDiff.getCreditorAccount());
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

            FRTransactionData frTransactionDiff = toFRTransactionData(obTransactionDiff);
            if (frTransactionDiff.getTransactionId() == null) {
                frTransactionDiff.setAccountId(accountId);
                frTransactionDiff.setTransactionId(UUID.randomUUID().toString());
                FRTransaction transaction = new FRTransaction();
                transaction.setAccountId(accountId);
                transaction.setBookingDateTime((frTransactionDiff.getBookingDateTime()));
                transaction.setTransaction(frTransactionDiff);
                transaction.setId(frTransactionDiff.getTransactionId());
                newTransactionsToSave.add(transaction);
            } else {
                Optional<FRTransaction> isTransaction = transactionRepository.findById(frTransactionDiff.getTransactionId());
                if (isTransaction.isEmpty() || !isTransaction.get().getAccountId().equals(frTransactionDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The transaction id '"
                            + frTransactionDiff.getTransactionId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                FRTransactionData transaction = isTransaction.get().getTransaction();
                if (frTransactionDiff.getTransactionReference() != null) {
                    transaction.setTransactionReference(frTransactionDiff.getTransactionReference());
                }
                if (frTransactionDiff.getAmount() != null) {
                    transaction.setAmount(frTransactionDiff.getAmount());
                }
                if (frTransactionDiff.getCreditDebitIndicator() != null) {
                    transaction.setCreditDebitIndicator(frTransactionDiff.getCreditDebitIndicator());
                }
                if (frTransactionDiff.getStatus() != null) {
                    transaction.setStatus(frTransactionDiff.getStatus());
                }
                if (frTransactionDiff.getBookingDateTime() != null) {
                    transaction.setBookingDateTime(frTransactionDiff.getBookingDateTime());
                    isTransaction.get().setBookingDateTime(frTransactionDiff.getBookingDateTime());
                }
                if (frTransactionDiff.getValueDateTime() != null) {
                    transaction.setValueDateTime(frTransactionDiff.getValueDateTime());
                }
                if (frTransactionDiff.getTransactionInformation() != null) {
                    transaction.setTransactionInformation(frTransactionDiff.getTransactionInformation());
                }
                if (frTransactionDiff.getBankTransactionCode() != null) {
                    transaction.setBankTransactionCode(frTransactionDiff.getBankTransactionCode());
                }
                if (frTransactionDiff.getProprietaryBankTransactionCode() != null) {
                    transaction.setProprietaryBankTransactionCode(frTransactionDiff.getProprietaryBankTransactionCode());
                }
                if (frTransactionDiff.getBalance() != null) {
                    transaction.setBalance(frTransactionDiff.getBalance());
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
        for (OBStatement2 obStatementDiff : accountDataDiff.getStatements()) {

            if (obStatementDiff.getAccountId() != null && !obStatementDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obStatementDiff.getAccountId() + "' refers in a statement doesn't match the main account id '" + accountId + "'");
            }

            FRStatementData frStatementDiff = toFRStatementData(obStatementDiff);
            if (frStatementDiff.getStatementId() == null) {
                frStatementDiff.setAccountId(accountId);
                frStatementDiff.setStatementId(UUID.randomUUID().toString());
                FRStatement statement1 = new FRStatement();
                statement1.setAccountId(accountId);
                statement1.setStatement(frStatementDiff);
                statement1.setId(frStatementDiff.getStatementId());
                newStatementsToSave.add(statement1);
            } else {
                Optional<FRStatement> isStatement = statementRepository.findById(frStatementDiff.getStatementId());
                if (isStatement.isEmpty() || !isStatement.get().getAccountId().equals(frStatementDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The statement id '"
                            + frStatementDiff.getStatementId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                FRStatementData statement = isStatement.get().getStatement();
                if (frStatementDiff.getEndDateTime() != null) {
                    statement.setEndDateTime(frStatementDiff.getEndDateTime());
                    isStatement.get().setEndDateTime(frStatementDiff.getEndDateTime());
                }
                if (frStatementDiff.getStartDateTime() != null) {
                    statement.setStartDateTime(frStatementDiff.getStartDateTime());
                    isStatement.get().setStartDateTime(frStatementDiff.getStartDateTime());
                }
                if (frStatementDiff.getType() != null) {
                    statement.setType(frStatementDiff.getType());
                }
                if (frStatementDiff.getCreationDateTime() != null) {
                    statement.setCreationDateTime(frStatementDiff.getCreationDateTime());
                }
                if (frStatementDiff.getStatementAmounts() != null) {
                    statement.setStatementAmounts(frStatementDiff.getStatementAmounts());
                }
                if (frStatementDiff.getStatementBenefits() != null) {
                    statement.setStatementBenefits(frStatementDiff.getStatementBenefits());
                }
                if (frStatementDiff.getStatementDateTimes() != null) {
                    statement.setStatementDateTimes(frStatementDiff.getStatementDateTimes());
                }
                if (frStatementDiff.getStatementDescriptions() != null) {
                    statement.setStatementDescriptions(frStatementDiff.getStatementDescriptions());
                }
                if (frStatementDiff.getStatementFees() != null) {
                    statement.setStatementFees(frStatementDiff.getStatementFees());
                }
                if (frStatementDiff.getStatementInterests() != null) {
                    statement.setStatementInterests(frStatementDiff.getStatementInterests());
                }
                if (frStatementDiff.getStatementRates() != null) {
                    statement.setStatementRates(frStatementDiff.getStatementRates());
                }
                if (frStatementDiff.getStatementReference() != null) {
                    statement.setStatementReference(frStatementDiff.getStatementReference());
                }
                if (frStatementDiff.getStatementValues() != null) {
                    statement.setStatementValues(frStatementDiff.getStatementValues());
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
        for (OBScheduledPayment3 obScheduledDiff : accountDataDiff.getScheduledPayments()) {

            if (obScheduledDiff.getAccountId() != null && !obScheduledDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obScheduledDiff.getAccountId() + "' refers in a scheduled payment doesn't match the main account id '" + accountId + "'");
            }

            FRScheduledPaymentData frScheduledDiff = toFRScheduledPaymentData(obScheduledDiff);
            if (frScheduledDiff.getScheduledPaymentId() == null) {
                frScheduledDiff.setAccountId(accountId);
                frScheduledDiff.setScheduledPaymentId(UUID.randomUUID().toString());
                FRScheduledPayment scheduledPayment1 = new FRScheduledPayment();
                scheduledPayment1.setAccountId(accountId);
                scheduledPayment1.setScheduledPayment(frScheduledDiff);
                scheduledPayment1.setId(frScheduledDiff.getScheduledPaymentId());
                scheduledPayment1.setStatus(ScheduledPaymentStatus.PENDING);
                newScheduledPaymentToSave.add(scheduledPayment1);
            } else {
                Optional<FRScheduledPayment> isScheduledPayment = scheduledPaymentRepository.findById(frScheduledDiff.getScheduledPaymentId());
                if (isScheduledPayment.isEmpty() || !isScheduledPayment.get().getAccountId().equals(frScheduledDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The scheduled payment id '"
                            + frScheduledDiff.getScheduledPaymentId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                FRScheduledPaymentData scheduledPayment = isScheduledPayment.get().getScheduledPayment();
                if (frScheduledDiff.getCreditorAccount() != null) {
                    scheduledPayment.setCreditorAccount(frScheduledDiff.getCreditorAccount());
                }
                if (frScheduledDiff.getInstructedAmount() != null) {
                    scheduledPayment.setInstructedAmount(frScheduledDiff.getInstructedAmount());
                }
                if (frScheduledDiff.getReference() != null) {
                    scheduledPayment.setReference(frScheduledDiff.getReference());
                }
                if (frScheduledDiff.getCreditorAgent() != null) {
                    scheduledPayment.setCreditorAgent(frScheduledDiff.getCreditorAgent());
                }
                if (frScheduledDiff.getScheduledPaymentDateTime() != null) {
                    scheduledPayment.setScheduledPaymentDateTime(frScheduledDiff.getScheduledPaymentDateTime());
                }
                if (frScheduledDiff.getScheduledType() != null) {
                    scheduledPayment.setScheduledType(frScheduledDiff.getScheduledType());
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
        for (OBOffer1 obOfferDiff : accountDataDiff.getOffers()) {

            if (obOfferDiff.getAccountId() != null && !obOfferDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obOfferDiff.getAccountId() + "' refers in a offer doesn't match the main account id '" + accountId + "'");
            }

            FROfferData frOfferDiff = toFROfferData(obOfferDiff);
            if (frOfferDiff.getOfferId() == null) {
                frOfferDiff.setAccountId(accountId);
                frOfferDiff.setOfferId(UUID.randomUUID().toString());
                FROffer offer1 = new FROffer();
                offer1.setAccountId(accountId);
                offer1.setOffer(frOfferDiff);
                offer1.setId(frOfferDiff.getOfferId());
                newOffersToSave.add(offer1);
            } else {
                Optional<FROffer> isOffers = offerRepository.findById(frOfferDiff.getOfferId());
                if (isOffers.isEmpty() || !isOffers.get().getAccountId().equals(frOfferDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The offer id '"
                            + frOfferDiff.getOfferId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                FROfferData offer = isOffers.get().getOffer();
                if (frOfferDiff.getAmount() != null) {
                    offer.setAmount(frOfferDiff.getAmount());
                }
                if (frOfferDiff.getDescription() != null) {
                    offer.setDescription(frOfferDiff.getDescription());
                }
                if (frOfferDiff.getEndDateTime() != null) {
                    offer.setEndDateTime(frOfferDiff.getEndDateTime());
                }
                if (frOfferDiff.getStartDateTime() != null) {
                    offer.setStartDateTime(frOfferDiff.getStartDateTime());
                }
                if (frOfferDiff.getURL() != null) {
                    offer.setURL(frOfferDiff.getURL());
                }
                if (frOfferDiff.getValue() != null) {
                    offer.setValue(frOfferDiff.getValue());
                }
                if (frOfferDiff.getFee() != null) {
                    offer.setFee(frOfferDiff.getFee());
                }
                if (frOfferDiff.getOfferType() != null) {
                    offer.setOfferType(frOfferDiff.getOfferType());
                }
                if (frOfferDiff.getRate() != null) {
                    offer.setRate(frOfferDiff.getRate());
                }
                if (frOfferDiff.getTerm() != null) {
                    offer.setTerm(frOfferDiff.getTerm());
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
