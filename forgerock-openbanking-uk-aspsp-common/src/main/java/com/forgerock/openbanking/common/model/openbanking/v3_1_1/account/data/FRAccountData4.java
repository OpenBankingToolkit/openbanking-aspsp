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
package com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.org.openbanking.datamodel.account.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FRAccountData4 {

    public OBAccount3 account;
    public List<OBCashBalance1> balances = new ArrayList<>();
    public OBReadProduct2DataProduct product;
    public OBParty2 party;
    public List<OBBeneficiary3> beneficiaries = new ArrayList<>();
    public List<OBDirectDebit1> directDebits = new ArrayList<>();
    public List<OBStandingOrder5> standingOrders = new ArrayList<>();
    public List<OBTransaction5> transactions = new ArrayList<>();
    public List<OBStatement1> statements = new ArrayList<>();
    public List<OBScheduledPayment2> scheduledPayments = new ArrayList<>();
    public List<OBOffer1> offers =new ArrayList<>();

    public FRAccountData4 addBalance(OBCashBalance1 balance) {
        balances.add(balance);
        return this;
    }

    public FRAccountData4 addBeneficiary(OBBeneficiary3 beneficiary) {
        beneficiaries.add(beneficiary);
        return this;
    }

    public FRAccountData4 addDirectDebit(OBDirectDebit1 directDebit1) {
        directDebits.add(directDebit1);
        return this;
    }


    public FRAccountData4 addStandingOrder(OBStandingOrder5 standingOrder) {
        standingOrders.add(standingOrder);
        return this;
    }

    public FRAccountData4 addTransaction(OBTransaction5 transaction) {
        transactions.add(transaction);
        return this;
    }

    public FRAccountData4 addStatement(OBStatement1 statement1) {
        statements.add(statement1);
        return this;
    }

    public FRAccountData4 addScheduledPayment(OBScheduledPayment2 scheduledPayment1) {
        scheduledPayments.add(scheduledPayment1);
        return this;
    }

    public FRAccountData4 addOffer(OBOffer1 offer1) {
        offers.add(offer1);
        return this;
    }
}
