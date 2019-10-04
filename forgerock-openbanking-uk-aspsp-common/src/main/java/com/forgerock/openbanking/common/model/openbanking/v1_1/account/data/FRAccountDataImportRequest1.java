/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v1_1.account.data;

import uk.org.openbanking.datamodel.account.*;

import java.util.ArrayList;
import java.util.List;

public class FRAccountDataImportRequest1 {

    public OBAccount1 account;
    public OBCashBalance1 balance;
    public OBProduct1 product;
    public List<OBBeneficiary1> beneficiaries = new ArrayList<>();
    public List<OBDirectDebit1> directDebits = new ArrayList<>();
    public List<OBStandingOrder1> standingOrders = new ArrayList<>();
    public List<OBTransaction1> transactions = new ArrayList<>();

    public OBAccount1 getAccount() {
        return account;
    }

    public void setAccount(OBAccount1 account) {
        this.account = account;
    }

    public OBCashBalance1 getBalance() {
        return balance;
    }

    public void setBalance(OBCashBalance1 balance) {
        this.balance = balance;
    }

    public OBProduct1 getProduct() {
        return product;
    }

    public void setProduct(OBProduct1 product) {
        this.product = product;
    }

    public List<OBBeneficiary1> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<OBBeneficiary1> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public List<OBDirectDebit1> getDirectDebits() {
        return directDebits;
    }

    public void setDirectDebits(List<OBDirectDebit1> directDebits) {
        this.directDebits = directDebits;
    }

    public List<OBStandingOrder1> getStandingOrders() {
        return standingOrders;
    }

    public void setStandingOrders(List<OBStandingOrder1> standingOrders) {
        this.standingOrders = standingOrders;
    }

    public List<OBTransaction1> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<OBTransaction1> transactions) {
        this.transactions = transactions;
    }

    public void addBeneficiary(OBBeneficiary1 beneficiary) {
        beneficiaries.add(beneficiary);
    }

    public void addDirectDebit(OBDirectDebit1 directDebit) {
        directDebits.add(directDebit);
    }

    public void addStandingOrder(OBStandingOrder1 standingOrder) {
        standingOrders.add(standingOrder);
    }

    public void addTransaction(OBTransaction1 transaction) {
        transactions.add(transaction);
    }
}
