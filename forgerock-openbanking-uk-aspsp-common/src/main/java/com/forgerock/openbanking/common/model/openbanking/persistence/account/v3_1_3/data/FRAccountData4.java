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
package com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_3.data;

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

    private OBAccount6 account;
    private List<OBCashBalance1> balances = new ArrayList<>();
    private OBReadProduct2DataProduct product;
    private OBParty2 party;
    private List<OBBeneficiary4> beneficiaries = new ArrayList<>();
    private List<OBReadDirectDebit2DataDirectDebit> directDebits = new ArrayList<>();
    private List<OBStandingOrder5> standingOrders = new ArrayList<>();
    private List<OBTransaction5> transactions = new ArrayList<>();
    private List<OBStatement2> statements = new ArrayList<>();
    private List<OBScheduledPayment3> scheduledPayments = new ArrayList<>();
    private List<OBOffer1> offers = new ArrayList<>();

    public FRAccountData4 addBalance(OBCashBalance1 balance) {
        balances.add(balance);
        return this;
    }

    public FRAccountData4 addBeneficiary(OBBeneficiary4 beneficiary) {
        beneficiaries.add(beneficiary);
        return this;
    }

    public FRAccountData4 addDirectDebit(OBReadDirectDebit2DataDirectDebit directDebit1) {
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

    public FRAccountData4 addStatement(OBStatement2 statement1) {
        statements.add(statement1);
        return this;
    }

    public FRAccountData4 addScheduledPayment(OBScheduledPayment3 scheduledPayment1) {
        scheduledPayments.add(scheduledPayment1);
        return this;
    }

    public FRAccountData4 addOffer(OBOffer1 offer1) {
        offers.add(offer1);
        return this;
    }
}
