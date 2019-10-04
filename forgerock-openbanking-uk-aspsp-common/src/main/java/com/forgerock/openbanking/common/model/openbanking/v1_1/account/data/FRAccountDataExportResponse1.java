/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v1_1.account.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.*;
import uk.org.openbanking.datamodel.account.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FRAccountDataExportResponse1 {

    public FRAccount1 account;
    public FRBalance1 balance;
    public FRProduct1 product;
    public Map<String, FRBeneficiary1> beneficiaries = new HashMap<>();
    public Map<String, FRDirectDebit1> directDebits = new HashMap<>();
    public Map<String, FRStandingOrder1> standingOrders = new HashMap<>();
    public Map<String, FRTransaction1> transactions = new HashMap<>();

    public OBAccount1 getAccount() {
        return account.getAccount();
    }

    public void setAccount(FRAccount1 account) {
        this.account = account;
    }

    public OBCashBalance1 getBalance() {
        return balance.getBalance();
    }

    public void setBalance(FRBalance1 balance) {
        this.balance = balance;
    }

    public OBProduct1 getProduct() {
        return product.getProduct();
    }

    public void setProduct(FRProduct1 product) {
        this.product = product;
    }

    @JsonIgnore
    public Map<String, FRBeneficiary1> getBeneficiariesById() {
        return beneficiaries;
    }

    public Collection<OBBeneficiary1> getBeneficiaries() {
        return beneficiaries.values().stream().map(b -> b.getBeneficiary()).collect(Collectors.toList());
    }

    public void setBeneficiaries(Map<String, FRBeneficiary1> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    @JsonIgnore
    public Map<String, FRDirectDebit1> getDirectDebitsById() {
        return directDebits;
    }

    public Collection<OBDirectDebit1> getDirectDebits() {
        return directDebits.values().stream().map(d -> d.getDirectDebit()).collect(Collectors.toList());
    }

    public void setDirectDebits(Map<String, FRDirectDebit1> directDebits) {
        this.directDebits = directDebits;
    }

    @JsonIgnore
    public Map<String, FRStandingOrder1> getStandingOrdersById() {
        return standingOrders;
    }

    public Collection<OBStandingOrder1> getStandingOrders() {
        return standingOrders.values().stream().map(s -> s.getStandingOrder()).collect(Collectors.toList());
    }

    public void setStandingOrders(Map<String, FRStandingOrder1> standingOrders) {
        this.standingOrders = standingOrders;
    }

    @JsonIgnore
    public Map<String, FRTransaction1> getTransactionsById() {
        return transactions;
    }

    public Collection<OBTransaction1> getTransactions() {
        return transactions.values().stream().map(t -> t.getTransaction()).collect(Collectors.toList());
    }
    public void setTransactions(Map<String, FRTransaction1> transactions) {
        this.transactions = transactions;
    }

    public void addBeneficiary(FRBeneficiary1 beneficiary) {
        beneficiaries.put(beneficiary.getId(), beneficiary);
    }

    public void addDirectDebit(FRDirectDebit1 directDebit) {
        directDebits.put(directDebit.getId(), directDebit);
    }

    public void addStandingOrder(FRStandingOrder1 standingOrder) {
        standingOrders.put(standingOrder.getId(), standingOrder);
    }

    public void addTransaction(FRTransaction1 transaction) {
        transactions.put(transaction.getId(), transaction);
    }
}
