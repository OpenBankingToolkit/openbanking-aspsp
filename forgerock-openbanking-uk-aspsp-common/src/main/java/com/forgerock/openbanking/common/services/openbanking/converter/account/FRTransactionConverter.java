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

import com.forgerock.openbanking.common.model.openbanking.domain.account.FRTransactionData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRTransactionData.FRBankTransactionCodeStructure;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRTransactionData.FRProprietaryBankTransactionCodeStructure;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRTransactionData.FRTransactionCashBalance;
import uk.org.openbanking.datamodel.account.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification3;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification6;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification61;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification62;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountSupplementaryDataConverter.toOBSupplementaryData1;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRCashBalanceConverter.toOBBalanceType1Code;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRCreditDebitIndicatorConverter.toOBCreditDebitCode;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRCreditDebitIndicatorConverter.toOBCreditDebitCode1;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRCreditDebitIndicatorConverter.toOBTransaction5CreditDebitIndicatorEnum;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRCurrencyExchangeConverter.toOBCurrencyExchange5;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount2;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount6;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount60;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount61;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount10;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount9;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialInstrumentConverter.toOBBranchAndFinancialInstitutionIdentification2;

public class FRTransactionConverter {

    // FR to OB
    public static OBTransaction1 toOBTransaction1(FRTransactionData transaction) {
        return transaction == null ? null : new OBTransaction1()
                .accountId(transaction.getAccountId())
                .transactionId(transaction.getTransactionId())
                .transactionReference(transaction.getTransactionReference())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(transaction.getAmount()))
                .creditDebitIndicator(toOBCreditDebitCode(transaction.getCreditDebitIndicator()))
                .status(toOBEntryStatus1Code(transaction.getStatus()))
                .bookingDateTime(transaction.getBookingDateTime())
                .valueDateTime(transaction.getValueDateTime())
                .transactionInformation(transaction.getTransactionInformation())
                .addressLine(transaction.getAddressLine())
                .bankTransactionCode(toOBBankTransactionCodeStructure1(transaction.getBankTransactionCode()))
                .proprietaryBankTransactionCode(toProprietaryBankTransactionCodeStructure1(transaction.getProprietaryBankTransactionCode()))
                .balance(toOBTransactionCashBalance(transaction.getBalance()))
                .merchantDetails(toOBMerchantDetails1(transaction.getMerchantDetails()));
    }

    public static OBTransaction2 toOBTransaction2(FRTransactionData transaction) {
        return transaction == null ? null : new OBTransaction2()
                .accountId(transaction.getAccountId())
                .transactionId(transaction.getTransactionId())
                .transactionReference(transaction.getTransactionReference())
                .statementReference(transaction.getStatementReferences())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(transaction.getAmount()))
                .creditDebitIndicator(toOBCreditDebitCode(transaction.getCreditDebitIndicator()))
                .status(toOBEntryStatus1Code(transaction.getStatus()))
                .bookingDateTime(transaction.getBookingDateTime())
                .valueDateTime(transaction.getValueDateTime())
                .addressLine(transaction.getAddressLine())
                .bankTransactionCode(toOBBankTransactionCodeStructure1(transaction.getBankTransactionCode()))
                .proprietaryBankTransactionCode(toProprietaryBankTransactionCodeStructure1(transaction.getProprietaryBankTransactionCode()))
                .equivalentAmount(null)
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification2(transaction.getCreditorAgent()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification2(transaction.getDebtorAgent()))
                .cardInstrument(toOBTransactionCardInstrument1(transaction.getCardInstrument()))
                .transactionInformation(transaction.getTransactionInformation())
                .balance(toOBTransactionCashBalance(transaction.getBalance()))
                .merchantDetails(toOBMerchantDetails1(transaction.getMerchantDetails()))
                .creditorAccount(toOBCashAccount2(transaction.getCreditorAccount()))
                .debtorAccount(FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification2(transaction.getDebtorAccount()));
    }

    public static OBTransaction3 toOBTransaction3(FRTransactionData transaction) {
        return transaction == null ? null : new OBTransaction3()
                .accountId(transaction.getAccountId())
                .transactionId(transaction.getTransactionId())
                .transactionReference(transaction.getTransactionReference())
                .statementReference(transaction.getStatementReferences())
                .creditDebitIndicator(toOBCreditDebitCode(transaction.getCreditDebitIndicator()))
                .status(toOBEntryStatus1Code(transaction.getStatus()))
                .bookingDateTime(transaction.getBookingDateTime())
                .valueDateTime(transaction.getValueDateTime())
                .addressLine(transaction.getAddressLine())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(transaction.getAmount()))
                .chargeAmount(toOBActiveOrHistoricCurrencyAndAmount(transaction.getChargeAmount()))
                .currencyExchange(toOBCurrencyExchange5(transaction.getCurrencyExchange()))
                .bankTransactionCode(toOBBankTransactionCodeStructure1(transaction.getBankTransactionCode()))
                .proprietaryBankTransactionCode(toOBTransaction3ProprietaryBankTransactionCode(transaction.getProprietaryBankTransactionCode()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(transaction.getCreditorAgent()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification3(transaction.getDebtorAgent()))
                .debtorAccount(toOBCashAccount3(transaction.getDebtorAccount()))
                .cardInstrument(toOBTransactionCardInstrument1(transaction.getCardInstrument()))
                .transactionInformation(transaction.getTransactionInformation())
                .balance(toOBTransactionCashBalance(transaction.getBalance()))
                .merchantDetails(toOBMerchantDetails1(transaction.getMerchantDetails()))
                .creditorAccount(toOBCashAccount3(transaction.getCreditorAccount()));
    }

    public static OBTransaction4 toOBTransaction4(FRTransactionData transaction) {
        return transaction == null ? null : new OBTransaction4()
                .accountId(transaction.getAccountId())
                .transactionId(transaction.getTransactionId())
                .transactionReference(transaction.getTransactionReference())
                .statementReference(transaction.getStatementReferences())
                .creditDebitIndicator(toOBCreditDebitCode(transaction.getCreditDebitIndicator()))
                .status(toOBEntryStatus1Code(transaction.getStatus()))
                .bookingDateTime(transaction.getBookingDateTime())
                .valueDateTime(transaction.getValueDateTime())
                .addressLine(transaction.getAddressLine())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(transaction.getAmount()))
                .chargeAmount(toOBActiveOrHistoricCurrencyAndAmount(transaction.getChargeAmount()))
                .currencyExchange(toOBCurrencyExchange5(transaction.getCurrencyExchange()))
                .bankTransactionCode(toOBBankTransactionCodeStructure1(transaction.getBankTransactionCode()))
                .proprietaryBankTransactionCode(toOBTransaction3ProprietaryBankTransactionCode(transaction.getProprietaryBankTransactionCode()))
                .cardInstrument(toOBTransactionCardInstrument1(transaction.getCardInstrument()))
                .supplementaryData(toOBSupplementaryData1(transaction.getSupplementaryData()))
                .transactionInformation(transaction.getTransactionInformation())
                .balance(toOBTransactionCashBalance(transaction.getBalance()))
                .merchantDetails(toOBMerchantDetails1(transaction.getMerchantDetails()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(transaction.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(transaction.getCreditorAccount()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification3(transaction.getDebtorAgent()))
                .debtorAccount(toOBCashAccount3(transaction.getDebtorAccount()));
    }

    public static OBTransaction5 toOBTransaction5(FRTransactionData transaction) {
        return transaction == null ? null : new OBTransaction5()
                .accountId(transaction.getAccountId())
                .transactionId(transaction.getTransactionId())
                .transactionReference(transaction.getTransactionReference())
                .statementReference(transaction.getStatementReferences())
                .creditDebitIndicator(toOBTransaction5CreditDebitIndicatorEnum(transaction.getCreditDebitIndicator()))
                .status(toOBEntryStatus1Code(transaction.getStatus()))
                .bookingDateTime(transaction.getBookingDateTime())
                .valueDateTime(transaction.getValueDateTime())
                .addressLine(transaction.getAddressLine())
                .amount(toAccountOBActiveOrHistoricCurrencyAndAmount(transaction.getAmount()))
                .chargeAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(transaction.getChargeAmount()))
                .currencyExchange(toOBCurrencyExchange5(transaction.getCurrencyExchange()))
                .bankTransactionCode(toOBBankTransactionCodeStructure1(transaction.getBankTransactionCode()))
                .proprietaryBankTransactionCode(toOBTransaction5ProprietaryBankTransactionCode(transaction.getProprietaryBankTransactionCode()))
                .cardInstrument(toOBTransactionCardInstrument1(transaction.getCardInstrument()))
                .supplementaryData(toOBSupplementaryData1(transaction.getSupplementaryData()))
                .transactionInformation(transaction.getTransactionInformation())
                .balance(toOBTransactionCashBalance(transaction.getBalance()))
                .merchantDetails(toOBMerchantDetails1(transaction.getMerchantDetails()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(transaction.getCreditorAgent()))
                .creditorAccount(toOBCashAccount6(transaction.getCreditorAccount()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification6(transaction.getDebtorAgent()))
                .debtorAccount(toOBCashAccount6(transaction.getDebtorAccount()));
    }

    public static OBTransaction6 toOBTransaction6(FRTransactionData transaction) {
        return transaction == null ? null : new OBTransaction6()
                .accountId(transaction.getAccountId())
                .transactionId(transaction.getTransactionId())
                .transactionReference(transaction.getTransactionReference())
                .statementReference(transaction.getStatementReferences())
                .creditDebitIndicator(toOBCreditDebitCode1(transaction.getCreditDebitIndicator()))
                .status(toOBEntryStatus1Code(transaction.getStatus()))
                .transactionMutability(toOBTransactionMutability1Code(transaction.getTransactionMutability()))
                .bookingDateTime(transaction.getBookingDateTime())
                .valueDateTime(transaction.getValueDateTime())
                .transactionInformation(transaction.getTransactionInformation())
                .addressLine(transaction.getAddressLine())
                .amount(toOBActiveOrHistoricCurrencyAndAmount9(transaction.getAmount()))
                .chargeAmount(toOBActiveOrHistoricCurrencyAndAmount10(transaction.getChargeAmount()))
                .currencyExchange(toOBCurrencyExchange5(transaction.getCurrencyExchange()))
                .bankTransactionCode(toOBBankTransactionCodeStructure1(transaction.getBankTransactionCode()))
                .proprietaryBankTransactionCode(toProprietaryBankTransactionCodeStructure1(transaction.getProprietaryBankTransactionCode()))
                .balance(toOBTransactionCashBalance(transaction.getBalance()))
                .merchantDetails(toOBMerchantDetails1(transaction.getMerchantDetails()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification61(transaction.getCreditorAgent()))
                .creditorAccount(toOBCashAccount60(transaction.getCreditorAccount()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification62(transaction.getDebtorAgent()))
                .debtorAccount(toOBCashAccount61(transaction.getDebtorAccount()))
                .cardInstrument(toOBTransactionCardInstrument1(transaction.getCardInstrument()))
                .supplementaryData(toOBSupplementaryData1(transaction.getSupplementaryData()));
    }

    public static OBEntryStatus1Code toOBEntryStatus1Code(FRTransactionData.FREntryStatus status) {
        return status == null ? null : OBEntryStatus1Code.valueOf(status.name());
    }

    public static OBTransactionMutability1Code toOBTransactionMutability1Code(FRTransactionData.FRTransactionMutability transactionMutability) {
        return transactionMutability == null ? null : OBTransactionMutability1Code.valueOf(transactionMutability.name());
    }


    public static OBBankTransactionCodeStructure1 toOBBankTransactionCodeStructure1(FRBankTransactionCodeStructure transactionCode) {
        return transactionCode == null ? null : new OBBankTransactionCodeStructure1()
                .code(transactionCode.getCode())
                .subCode(transactionCode.getSubCode());
    }

    public static ProprietaryBankTransactionCodeStructure1 toProprietaryBankTransactionCodeStructure1(FRProprietaryBankTransactionCodeStructure proprietaryTransactionCode) {
        return proprietaryTransactionCode == null ? null : new ProprietaryBankTransactionCodeStructure1()
                .code(proprietaryTransactionCode.getCode())
                .issuer(proprietaryTransactionCode.getIssuer());
    }

    public static OBTransaction3ProprietaryBankTransactionCode toOBTransaction3ProprietaryBankTransactionCode(FRProprietaryBankTransactionCodeStructure proprietaryTransactionCode) {
        return proprietaryTransactionCode == null ? null : new OBTransaction3ProprietaryBankTransactionCode()
                .code(proprietaryTransactionCode.getCode())
                .issuer(proprietaryTransactionCode.getIssuer());
    }

    public static OBTransaction5ProprietaryBankTransactionCode toOBTransaction5ProprietaryBankTransactionCode(FRProprietaryBankTransactionCodeStructure proprietaryTransactionCode) {
        return proprietaryTransactionCode == null ? null : new OBTransaction5ProprietaryBankTransactionCode()
                .code(proprietaryTransactionCode.getCode())
                .issuer(proprietaryTransactionCode.getIssuer());
    }

    public static OBTransactionCardInstrument1 toOBTransactionCardInstrument1(FRTransactionData.FRTransactionCardInstrument cardInstrument) {
        return cardInstrument == null ? null : new OBTransactionCardInstrument1()
                .cardSchemeName(toOBExternalCardSchemeType1Code(cardInstrument.getCardSchemeName()))
                .authorisationType(toOBExternalCardAuthorisationType1Code(cardInstrument.getAuthorisationType()))
                .name(cardInstrument.getName())
                .identification(cardInstrument.getIdentification());
    }

    public static OBTransactionCashBalance toOBTransactionCashBalance(FRTransactionCashBalance balance) {
        return balance == null ? null : new OBTransactionCashBalance()
                .amount(toOBActiveOrHistoricCurrencyAndAmount(balance.getAmount()))
                .creditDebitIndicator(toOBCreditDebitCode(balance.getCreditDebitIndicator()))
                .type(toOBBalanceType1Code(balance.getType()));
    }

    public static OBMerchantDetails1 toOBMerchantDetails1(FRTransactionData.FRMerchantDetails merchantDetails) {
        return merchantDetails == null ? null : new OBMerchantDetails1()
                .merchantName(merchantDetails.getMerchantName())
                .merchantCategoryCode(merchantDetails.getMerchantCategoryCode());
    }

    public static OBExternalCardSchemeType1Code toOBExternalCardSchemeType1Code(FRTransactionData.FRCardScheme cardSchemeName) {
        return cardSchemeName == null ? null : OBExternalCardSchemeType1Code.valueOf(cardSchemeName.name());
    }

    public static OBExternalCardAuthorisationType1Code toOBExternalCardAuthorisationType1Code(FRTransactionData.FRCardAuthorisationType authorisationType) {
        return authorisationType == null ? null : OBExternalCardAuthorisationType1Code.valueOf(authorisationType.name());
    }
}
