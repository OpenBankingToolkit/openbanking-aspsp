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
package com.forgerock.openbanking.common.services.openbanking.converter.account;

import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_1.account.FRTransaction5;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_5.account.FRTransaction6;
import uk.org.openbanking.datamodel.account.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount10;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount9;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification61;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification62;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.OBCashAccountConverter.toOBCashAccount60;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.OBCashAccountConverter.toOBCashAccount61;

public class FRTransactionConverter {

    public static FRTransaction6 toFRTransaction6(FRTransaction5 frTransaction5) {
        return frTransaction5 == null ? null : FRTransaction6.builder()
                .id(frTransaction5.getId())
                .accountId(frTransaction5.getAccountId())
                .statementIds(frTransaction5.getStatementIds())
                .transaction(toOBTransaction6(frTransaction5.getTransaction()))
                .created(frTransaction5.getCreated())
                .updated(frTransaction5.getUpdated())
                .build();
    }

    public static OBTransaction6 toOBTransaction6(OBTransaction5 obTransaction5) {
        return obTransaction5 == null ? null : (new OBTransaction6())
                .accountId(obTransaction5.getAccountId())
                .transactionId(obTransaction5.getTransactionId())
                .transactionReference(obTransaction5.getTransactionReference())
                .statementReference(obTransaction5.getStatementReference())
                .creditDebitIndicator(OBCreditDebitCode1.valueOf(obTransaction5.getCreditDebitIndicator().name()))
                .status(obTransaction5.getStatus())
                .transactionMutability(OBTransactionMutability1Code.IMMUTABLE) // TODO #279 - is this right?
                .bookingDateTime(obTransaction5.getBookingDateTime())
                .valueDateTime(obTransaction5.getValueDateTime())
                .transactionInformation(obTransaction5.getTransactionInformation())
                .addressLine(obTransaction5.getAddressLine())
                .amount(toOBActiveOrHistoricCurrencyAndAmount9(obTransaction5.getAmount()))
                .chargeAmount(toOBActiveOrHistoricCurrencyAndAmount10(obTransaction5.getChargeAmount()))
                .currencyExchange(obTransaction5.getCurrencyExchange())
                .bankTransactionCode(obTransaction5.getBankTransactionCode())
                .proprietaryBankTransactionCode(toOBTransaction5ProprietaryBankTransactionCode(obTransaction5.getProprietaryBankTransactionCode()))
                .balance(obTransaction5.getBalance())
                .merchantDetails(obTransaction5.getMerchantDetails())
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification61(obTransaction5.getCreditorAgent()))
                .creditorAccount(toOBCashAccount60(obTransaction5.getCreditorAccount()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification62(obTransaction5.getDebtorAgent()))
                .debtorAccount(toOBCashAccount61(obTransaction5.getDebtorAccount()))
                .cardInstrument(obTransaction5.getCardInstrument())
                .supplementaryData(obTransaction5.getSupplementaryData());
    }

    public static ProprietaryBankTransactionCodeStructure1 toOBTransaction5ProprietaryBankTransactionCode(OBTransaction5ProprietaryBankTransactionCode proprietaryBankTransactionCode) {
        return proprietaryBankTransactionCode == null ? null : (new ProprietaryBankTransactionCodeStructure1())
                .code(proprietaryBankTransactionCode.getCode())
                .issuer(proprietaryBankTransactionCode.getIssuer());
    }
}
