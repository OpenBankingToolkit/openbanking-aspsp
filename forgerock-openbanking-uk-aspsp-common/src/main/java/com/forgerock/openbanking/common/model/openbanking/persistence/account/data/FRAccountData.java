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
package com.forgerock.openbanking.common.model.openbanking.persistence.account.data;

import com.forgerock.openbanking.common.model.openbanking.domain.account.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.org.openbanking.datamodel.account.OBReadProduct2DataProduct;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FRAccountData {

    private FRFinancialAccount account;
    private List<FRCashBalance> balances = new ArrayList<>();
    private OBReadProduct2DataProduct product;
    private FRPartyData party;
    private List<FRAccountBeneficiary> beneficiaries = new ArrayList<>();
    private List<FRDirectDebitData> directDebits = new ArrayList<>();
    private List<FRStandingOrderData> standingOrders = new ArrayList<>();
    private List<FRTransactionData> transactions = new ArrayList<>();
    private List<FRStatementData> statements = new ArrayList<>();
    private List<FRScheduledPaymentData> scheduledPayments = new ArrayList<>();
    private List<FROfferData> offers = new ArrayList<>();

    public FRAccountData addBalance(FRCashBalance balance) {
        balances.add(balance);
        return this;
    }

    public FRAccountData addBeneficiary(FRAccountBeneficiary beneficiary) {
        beneficiaries.add(beneficiary);
        return this;
    }

    public FRAccountData addDirectDebit(FRDirectDebitData directDebit) {
        directDebits.add(directDebit);
        return this;
    }

    public FRAccountData addStandingOrder(FRStandingOrderData standingOrder) {
        standingOrders.add(standingOrder);
        return this;
    }

    public FRAccountData addTransaction(FRTransactionData transaction) {
        transactions.add(transaction);
        return this;
    }

    public FRAccountData addStatement(FRStatementData statement) {
        statements.add(statement);
        return this;
    }

    public FRAccountData addScheduledPayment(FRScheduledPaymentData scheduledPayment) {
        scheduledPayments.add(scheduledPayment);
        return this;
    }

    public FRAccountData addOffer(FROfferData offer) {
        offers.add(offer);
        return this;
    }
}
