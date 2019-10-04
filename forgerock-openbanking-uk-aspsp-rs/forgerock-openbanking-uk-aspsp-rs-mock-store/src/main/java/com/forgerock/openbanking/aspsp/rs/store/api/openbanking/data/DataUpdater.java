/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.data;

import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.directdebits.FRDirectDebit1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.offers.FROffer1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.products.FRProduct2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.statements.FRStatement1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.accounts.FRAccount3Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.beneficiaries.FRBeneficiary3Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.party.FRParty2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.scheduledpayments.FRScheduledPayment2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.standingorders.FRStandingOrder5Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.transactions.FRTransaction5Repository;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBalance1;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRDirectDebit1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FROffer1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRProduct2;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRStatement1;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.*;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.data.FRAccountData4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.data.FRUserData4;
import com.google.common.collect.ImmutableList;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.org.openbanking.datamodel.account.*;

import java.util.*;

@Service
@NoArgsConstructor
public class DataUpdater {

    private FRAccount3Repository accountsRepository;
    private FRBalance1Repository balanceRepository;
    private FRBeneficiary3Repository beneficiaryRepository;
    private FRDirectDebit1Repository directDebitRepository;
    private FRProduct2Repository productRepository;
    private FRStandingOrder5Repository standingOrderRepository;
    private FRTransaction5Repository transactionRepository;
    private FRStatement1Repository statementRepository;
    private FRScheduledPayment2Repository scheduledPaymentRepository;
    private FRParty2Repository partyRepository;
    private FROffer1Repository offerRepository;
    private int documentLimit;

    @Autowired
    public DataUpdater(FRAccount3Repository accountsRepository, FRBalance1Repository balanceRepository,
                       FRBeneficiary3Repository beneficiaryRepository, FRDirectDebit1Repository directDebitRepository,
                       FRProduct2Repository productRepository, FRStandingOrder5Repository standingOrderRepository,
                       FRTransaction5Repository transactionRepository, FRStatement1Repository statementRepository,
                       FRScheduledPayment2Repository scheduledPaymentRepository, FRParty2Repository partyRepository,
                       FROffer1Repository offerRepository, @Value("${rs.data.upload.limit.documents}") Integer documentLimit) {
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

    void updateParty(FRUserData4 userData) {
        if (userData.getParty() == null) {
            return;
        }
        FRParty2 party1 = partyRepository.findByUserId(userData.getUserName());
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

    void updateAccount(FRAccountData4 accountDataDiff, FRAccount3 account, Set<String> accountIds) {
        OBAccount3 accountDiff = accountDataDiff.getAccount();
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

    void updateBalances(FRAccountData4 accountDataDiff, Set<String> accountIds) {
        //Balance
        List<FRBalance1> balancesToSave = new ArrayList<>();
        List<FRBalance1> newBalancesToSave = new ArrayList<>();
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
            Optional<FRBalance1> isBalance = balanceRepository.findByAccountIdAndBalanceType(
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
                FRBalance1 balance1 = new FRBalance1();
                balance1.setBalance(obBalanceDiff);
                balance1.setAccountId(accountId);
                newBalancesToSave.add(balance1);
            }
        }
        if (balanceRepository.countByAccountIdIn(accountIds) + newBalancesToSave.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add balances as it has exceeded maximum limit of %s", documentLimit));
        }
        List<FRBalance1> allBalances = ImmutableList.<FRBalance1>builder()
                .addAll(balancesToSave)
                .addAll(newBalancesToSave)
                .build();
        balanceRepository.saveAll(allBalances);
    }

    void updateProducts(FRAccountData4 accountDataDiff, Set<String> accountIds) {
        //Product
        if (accountDataDiff.getProduct() == null) {
            return;
        }
        Optional<FRProduct2> isProduct = productRepository.findById(accountDataDiff.getProduct().getProductId());
        if (isProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    String.format("The product id doesn't exist '%s'",
                            accountDataDiff.getProduct().getProductId()));
        }
        FRProduct2 product = isProduct.get();

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

    void updateParty(FRAccountData4 accountDataDiff, Set<String> accountIds) {
        //Party
        if (accountDataDiff.getParty() == null) {
            return;
        }
        Optional<FRParty2> isParty = partyRepository.findById(accountDataDiff.getParty().getPartyId());
        if (isParty.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("The party id '%s' doesn't exist",
                            accountDataDiff.getParty().getPartyId()));
        }
        FRParty2 party1 = isParty.get();

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

    void updateBeneficiaries(FRAccountData4 accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Beneficiaries
        List<FRBeneficiary3> beneficiariesToSave = new ArrayList<>();
        List<FRBeneficiary3> newBeneficiariesToSave = new ArrayList<>();
        for (OBBeneficiary3 obBeneficiaryDiff : accountDataDiff.getBeneficiaries()) {

            if (obBeneficiaryDiff.getAccountId() != null && !obBeneficiaryDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obBeneficiaryDiff.getAccountId() + "' refers in a beneficiary doesn't match the main account id '" + accountId + "'");
            }
            if (obBeneficiaryDiff.getBeneficiaryId() == null) {
                obBeneficiaryDiff.setAccountId(accountId);
                obBeneficiaryDiff.setBeneficiaryId(UUID.randomUUID().toString());
                FRBeneficiary3 beneficiary = new FRBeneficiary3();
                beneficiary.setAccountId(accountId);
                beneficiary.setBeneficiary(obBeneficiaryDiff);
                beneficiary.setId(obBeneficiaryDiff.getBeneficiaryId());
                newBeneficiariesToSave.add(beneficiary);
            } else {
                Optional<FRBeneficiary3> isBeneficiary = beneficiaryRepository.findById(obBeneficiaryDiff.getBeneficiaryId());
                if (isBeneficiary.isEmpty() || !isBeneficiary.get().getAccountId().equals(obBeneficiaryDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The beneficiary id '"
                            + obBeneficiaryDiff.getBeneficiaryId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                OBBeneficiary3 beneficiary = isBeneficiary.get().getBeneficiary();
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
        List<FRBeneficiary3> allBeneficiaries = ImmutableList.<FRBeneficiary3>builder()
                .addAll(beneficiariesToSave)
                .addAll(newBeneficiariesToSave)
                .build();
        beneficiaryRepository.saveAll(allBeneficiaries);
    }

    void updateDirectDebits(FRAccountData4 accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Direct Debits
        List<FRDirectDebit1> directDebitsToSave = new ArrayList<>();
        List<FRDirectDebit1> newDirectDebitsToSave = new ArrayList<>();
        for (OBDirectDebit1 obDirectDebitDiff : accountDataDiff.getDirectDebits()) {

            if (obDirectDebitDiff.getAccountId() != null && !obDirectDebitDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obDirectDebitDiff.getAccountId() + "' refers in a direct debit doesn't match the main account id '" + accountId + "'");
            }
            if (obDirectDebitDiff.getDirectDebitId() == null) {
                obDirectDebitDiff.setAccountId(accountId);
                obDirectDebitDiff.setDirectDebitId(UUID.randomUUID().toString());
                FRDirectDebit1 directDebit = new FRDirectDebit1();
                directDebit.setAccountId(accountId);
                directDebit.setDirectDebit(obDirectDebitDiff);
                directDebit.setId(obDirectDebitDiff.getDirectDebitId());
                newDirectDebitsToSave.add(directDebit);
            } else {
                Optional<FRDirectDebit1> isDirectDebit = directDebitRepository.findById(obDirectDebitDiff.getDirectDebitId());
                if (isDirectDebit.isEmpty() || !isDirectDebit.get().getAccountId().equals(obDirectDebitDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The direct debit id '"
                            + obDirectDebitDiff.getDirectDebitId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                OBDirectDebit1 directDebit = isDirectDebit.get().getDirectDebit();
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
        List<FRDirectDebit1> allDirectDebits = ImmutableList.<FRDirectDebit1>builder()
                .addAll(directDebitsToSave)
                .addAll(newDirectDebitsToSave)
                .build();
        directDebitRepository.saveAll(allDirectDebits);
    }

    void updateStandingOrders(FRAccountData4 accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Standing orders
        List<FRStandingOrder5> standingOrdersToSave = new ArrayList<>();
        List<FRStandingOrder5> newStandingOrdersToSave = new ArrayList<>();
        for (OBStandingOrder5 obStandingOrderDiff : accountDataDiff.getStandingOrders()) {

            if (obStandingOrderDiff.getAccountId() != null && !obStandingOrderDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obStandingOrderDiff.getAccountId() + "' refers in a standing order doesn't match the main account id '" + accountId + "'");
            }
            if (obStandingOrderDiff.getStandingOrderId() == null) {
                obStandingOrderDiff.setAccountId(accountId);
                obStandingOrderDiff.setStandingOrderId(UUID.randomUUID().toString());
                FRStandingOrder5 standingOrder = new FRStandingOrder5();
                standingOrder.setAccountId(accountId);
                standingOrder.setStandingOrder(obStandingOrderDiff);
                standingOrder.setId(obStandingOrderDiff.getStandingOrderId());
                standingOrder.setStatus(StandingOrderStatus.PENDING);
                newStandingOrdersToSave.add(standingOrder);
            } else {
                Optional<FRStandingOrder5> isStandingOrder = standingOrderRepository.findById(obStandingOrderDiff.getStandingOrderId());
                if (isStandingOrder.isEmpty() || !isStandingOrder.get().getAccountId().equals(obStandingOrderDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The standing order id '"
                            + obStandingOrderDiff.getStandingOrderId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                OBStandingOrder5 standingOrder = isStandingOrder.get().getStandingOrder();
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
        List<FRStandingOrder5> allStandingOrders = ImmutableList.<FRStandingOrder5>builder()
                .addAll(standingOrdersToSave)
                .addAll(newStandingOrdersToSave)
                .build();
        standingOrderRepository.saveAll(allStandingOrders);
    }

    void updateTransactions(FRAccountData4 accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Transactions
        List<FRTransaction5> transactionsToSave = new ArrayList<>();
        List<FRTransaction5> newTransactionsToSave = new ArrayList<>();
        for (OBTransaction5 obTransactionDiff : accountDataDiff.getTransactions()) {

            if (obTransactionDiff.getAccountId() != null && !obTransactionDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obTransactionDiff.getAccountId() + "' refers in a transaction doesn't match the main account id '" + accountId + "'");
            }
            if (obTransactionDiff.getTransactionId() == null) {
                obTransactionDiff.setAccountId(accountId);
                obTransactionDiff.setTransactionId(UUID.randomUUID().toString());
                FRTransaction5 transaction = new FRTransaction5();
                transaction.setAccountId(accountId);
                transaction.setBookingDateTime((obTransactionDiff.getBookingDateTime()));
                transaction.setTransaction(obTransactionDiff);
                transaction.setId(obTransactionDiff.getTransactionId());
                newTransactionsToSave.add(transaction);
            } else {
                Optional<FRTransaction5> isTransaction = transactionRepository.findById(obTransactionDiff.getTransactionId());
                if (isTransaction.isEmpty() || !isTransaction.get().getAccountId().equals(obTransactionDiff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The transaction id '"
                            + obTransactionDiff.getTransactionId() + "' doesn't exist or doesn't belongs to this account ID.");
                }
                OBTransaction5 transaction = isTransaction.get().getTransaction();
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
        List<FRTransaction5> allTransactions = ImmutableList.<FRTransaction5>builder()
                .addAll(transactionsToSave)
                .addAll(newTransactionsToSave)
                .build();
        transactionRepository.saveAll(allTransactions);
    }

    void updateStatements(FRAccountData4 accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Statements
        List<FRStatement1> statementsToSave = new ArrayList<>();
        List<FRStatement1> newStatementsToSave = new ArrayList<>();
        for (OBStatement1 obStatement1Diff : accountDataDiff.getStatements()) {

            if (obStatement1Diff.getAccountId() != null && !obStatement1Diff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + obStatement1Diff.getAccountId() + "' refers in a statement doesn't match the main account id '" + accountId + "'");
            }
            if (obStatement1Diff.getStatementId() == null) {
                obStatement1Diff.setAccountId(accountId);
                obStatement1Diff.setStatementId(UUID.randomUUID().toString());
                FRStatement1 statement1 = new FRStatement1();
                statement1.setAccountId(accountId);
                statement1.setStatement(obStatement1Diff);
                statement1.setId(obStatement1Diff.getStatementId());
                newStatementsToSave.add(statement1);
            } else {
                Optional<FRStatement1> isStatement = statementRepository.findById(obStatement1Diff.getStatementId());
                if (isStatement.isEmpty() || !isStatement.get().getAccountId().equals(obStatement1Diff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The statement id '"
                            + obStatement1Diff.getStatementId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                OBStatement1 statement = isStatement.get().getStatement();
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
        List<FRStatement1> allStatements = ImmutableList.<FRStatement1>builder()
                .addAll(statementsToSave)
                .addAll(newStatementsToSave)
                .build();
        statementRepository.saveAll(allStatements);
    }

    void updateScheduledPayments(FRAccountData4 accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Scheduled Payment
        List<FRScheduledPayment2> scheduledPaymentToSave = new ArrayList<>();
        List<FRScheduledPayment2> newScheduledPaymentToSave = new ArrayList<>();
        for (OBScheduledPayment2 OBScheduledPayment2Diff : accountDataDiff.getScheduledPayments()) {

            if (OBScheduledPayment2Diff.getAccountId() != null && !OBScheduledPayment2Diff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + OBScheduledPayment2Diff.getAccountId() + "' refers in a scheduled payment doesn't match the main account id '" + accountId + "'");
            }
            if (OBScheduledPayment2Diff.getScheduledPaymentId() == null) {
                OBScheduledPayment2Diff.setAccountId(accountId);
                OBScheduledPayment2Diff.setScheduledPaymentId(UUID.randomUUID().toString());
                FRScheduledPayment2 scheduledPayment1 = new FRScheduledPayment2();
                scheduledPayment1.setAccountId(accountId);
                scheduledPayment1.setScheduledPayment(OBScheduledPayment2Diff);
                scheduledPayment1.setId(OBScheduledPayment2Diff.getScheduledPaymentId());
                scheduledPayment1.setStatus(ScheduledPaymentStatus.PENDING);
                newScheduledPaymentToSave.add(scheduledPayment1);
            } else {
                Optional<FRScheduledPayment2> isScheduledPayment = scheduledPaymentRepository.findById(OBScheduledPayment2Diff.getScheduledPaymentId());
                if (isScheduledPayment.isEmpty() || !isScheduledPayment.get().getAccountId().equals(OBScheduledPayment2Diff.getAccountId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The scheduled payment id '"
                            + OBScheduledPayment2Diff.getScheduledPaymentId() + "' doesn't exist or doesn't belongs to this account ID.");
                }

                OBScheduledPayment2 scheduledPayment = isScheduledPayment.get().getScheduledPayment();
                if (OBScheduledPayment2Diff.getCreditorAccount() != null) {
                    scheduledPayment.setCreditorAccount(OBScheduledPayment2Diff.getCreditorAccount());
                }
                if (OBScheduledPayment2Diff.getInstructedAmount() != null) {
                    scheduledPayment.setInstructedAmount(OBScheduledPayment2Diff.getInstructedAmount());
                }
                if (OBScheduledPayment2Diff.getReference() != null) {
                    scheduledPayment.setReference(OBScheduledPayment2Diff.getReference());
                }
                if (OBScheduledPayment2Diff.getCreditorAgent() != null) {
                    scheduledPayment.setCreditorAgent(OBScheduledPayment2Diff.getCreditorAgent());
                }
                if (OBScheduledPayment2Diff.getScheduledPaymentDateTime() != null) {
                    scheduledPayment.setScheduledPaymentDateTime(OBScheduledPayment2Diff.getScheduledPaymentDateTime());
                }
                if (OBScheduledPayment2Diff.getScheduledType() != null) {
                    scheduledPayment.setScheduledType(OBScheduledPayment2Diff.getScheduledType());
                }
                scheduledPaymentToSave.add(isScheduledPayment.get());
            }
        }
        if (scheduledPaymentRepository.countByAccountIdIn(accountIds) + newScheduledPaymentToSave.size() > documentLimit) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                    String.format("Cannot add schedule payments as it has exceeded maximum limit of %s", documentLimit));
        }
        List<FRScheduledPayment2> allScheduledPayments = ImmutableList.<FRScheduledPayment2>builder()
                .addAll(scheduledPaymentToSave)
                .addAll(newScheduledPaymentToSave)
                .build();
        scheduledPaymentRepository.saveAll(allScheduledPayments);
    }

    void updateOffers(FRAccountData4 accountDataDiff, Set<String> accountIds) {
        String accountId = accountDataDiff.getAccount().getAccountId();
        //Offers
        List<FROffer1> offersToSave = new ArrayList<>();
        List<FROffer1> newOffersToSave = new ArrayList<>();
        for (OBOffer1 offersDiff : accountDataDiff.getOffers()) {

            if (offersDiff.getAccountId() != null && !offersDiff.getAccountId().equals(accountId)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The account id '"
                        + offersDiff.getAccountId() + "' refers in a offer doesn't match the main account id '" + accountId + "'");
            }
            if (offersDiff.getOfferId() == null) {
                offersDiff.setAccountId(accountId);
                offersDiff.setOfferId(UUID.randomUUID().toString());
                FROffer1 offer1 = new FROffer1();
                offer1.setAccountId(accountId);
                offer1.setOffer(offersDiff);
                offer1.setId(offersDiff.getOfferId());
                newOffersToSave.add(offer1);
            } else {
                Optional<FROffer1> isOffers = offerRepository.findById(offersDiff.getOfferId());
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
        List<FROffer1> allOffers = ImmutableList.<FROffer1>builder()
                .addAll(offersToSave)
                .addAll(newOffersToSave)
                .build();
        offerRepository.saveAll(allOffers);
    }

}
