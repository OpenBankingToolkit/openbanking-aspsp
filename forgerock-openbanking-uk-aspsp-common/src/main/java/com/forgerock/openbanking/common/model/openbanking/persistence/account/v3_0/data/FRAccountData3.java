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
package com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_0.data;

import uk.org.openbanking.datamodel.account.*;

import java.util.ArrayList;
import java.util.List;

public class FRAccountData3 {

    public OBAccount2 account;
    public List<OBCashBalance1> balances = new ArrayList<>();
    public OBReadProduct2DataProduct product;
    public OBParty1 party;
    public List<OBBeneficiary2> beneficiaries = new ArrayList<>();
    public List<OBDirectDebit1> directDebits = new ArrayList<>();
    public List<OBStandingOrder4> standingOrders = new ArrayList<>();
    public List<OBTransaction4> transactions = new ArrayList<>();
    public List<OBStatement1> statements = new ArrayList<>();
    public List<OBScheduledPayment1> scheduledPayments = new ArrayList<>();
    public List<OBOffer1> offers =new ArrayList<>();

    public OBAccount2 getAccount() {
        return account;
    }

    public void setAccount(OBAccount2 account) {
        this.account = account;
    }

    public List<OBCashBalance1> getBalances() {
        return balances;
    }

    public void setBalances(List<OBCashBalance1> balances) {
        this.balances = balances;
    }

    public void addBalance(OBCashBalance1 balance) {
        balances.add(balance);
    }

    public OBReadProduct2DataProduct getProduct() {
        return product;
    }

    public void setProduct(OBReadProduct2DataProduct product) {
        this.product = product;
    }

    public OBParty1 getParty() {
        return party;
    }

    public void setParty(OBParty1 party) {
        this.party = party;
    }

    public List<OBBeneficiary2> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<OBBeneficiary2> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public void addBeneficiary(OBBeneficiary2 beneficiary2) {
        beneficiaries.add(beneficiary2);
    }

    public List<OBDirectDebit1> getDirectDebits() {
        return directDebits;
    }

    public void setDirectDebits(List<OBDirectDebit1> directDebits) {
        this.directDebits = directDebits;
    }

    public void addDirectDebit(OBDirectDebit1 directDebit1) {
        directDebits.add(directDebit1);
    }

    public List<OBStandingOrder4> getStandingOrders() {
        return standingOrders;
    }

    public void setStandingOrders(List<OBStandingOrder4> standingOrders) {
        this.standingOrders = standingOrders;
    }


    public void addStandingOrder(OBStandingOrder4 standingOrder) {
        standingOrders.add(standingOrder);
    }

    public List<OBTransaction4> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<OBTransaction4> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(OBTransaction4 transaction) {
        transactions.add(transaction);
    }


    public List<OBStatement1> getStatements() {
        return statements;
    }

    public void setStatements(List<OBStatement1> statements) {
        this.statements = statements;
    }

    public void addStatement(OBStatement1 statement1) {
        statements.add(statement1);
    }

    public List<OBScheduledPayment1> getScheduledPayments() {
        return scheduledPayments;
    }

    public void setScheduledPayments(List<OBScheduledPayment1> scheduledPayments) {
        this.scheduledPayments = scheduledPayments;
    }

    public void addScheduledPayment(OBScheduledPayment1 scheduledPayment1) {
        scheduledPayments.add(scheduledPayment1);
    }


    public List<OBOffer1> getOffers() {
        return offers;
    }

    public void setOffers(List<OBOffer1> offers) {
        this.offers = offers;
    }

    public void addOffer(OBOffer1 offer1) {
        offers.add(offer1);
    }
}
