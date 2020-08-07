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
package com.forgerock.openbanking.common.model.openbanking.v3_1_5.account.data;

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
public class FRAccountData5 {

    private OBAccount6 account;
    private List<OBCashBalance1> balances = new ArrayList<>();
    private OBReadProduct2DataProduct product;
    private OBParty2 party;
    private List<OBBeneficiary5> beneficiaries = new ArrayList<>();
    private List<OBReadDirectDebit2DataDirectDebit> directDebits = new ArrayList<>();
    private List<OBStandingOrder6> standingOrders = new ArrayList<>();
    private List<OBTransaction6> transactions = new ArrayList<>();
    private List<OBStatement2> statements = new ArrayList<>();
    private List<OBScheduledPayment3> scheduledPayments = new ArrayList<>();
    private List<OBOffer1> offers = new ArrayList<>();

    public FRAccountData5 addBalance(OBCashBalance1 balance) {
        balances.add(balance);
        return this;
    }

    public FRAccountData5 addBeneficiary(OBBeneficiary5 beneficiary) {
        beneficiaries.add(beneficiary);
        return this;
    }

    public FRAccountData5 addDirectDebit(OBReadDirectDebit2DataDirectDebit directDebit1) {
        directDebits.add(directDebit1);
        return this;
    }

    public FRAccountData5 addStandingOrder(OBStandingOrder6 standingOrder) {
        standingOrders.add(standingOrder);
        return this;
    }

    public FRAccountData5 addTransaction(OBTransaction6 transaction) {
        transactions.add(transaction);
        return this;
    }

    public FRAccountData5 addStatement(OBStatement2 statement1) {
        statements.add(statement1);
        return this;
    }

    public FRAccountData5 addScheduledPayment(OBScheduledPayment3 scheduledPayment1) {
        scheduledPayments.add(scheduledPayment1);
        return this;
    }

    public FRAccountData5 addOffer(OBOffer1 offer1) {
        offers.add(offer1);
        return this;
    }
}
