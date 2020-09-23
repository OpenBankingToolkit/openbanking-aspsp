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
package com.forgerock.openbanking.common.services.openbanking.converter.transaction;

import com.forgerock.openbanking.common.services.openbanking.converter.common.OBCashAccountConverter;
import com.forgerock.openbanking.common.services.openbanking.converter.FRModelMapper;
import com.forgerock.openbanking.common.services.openbanking.converter.OBActiveOrHistoricCurrencyAndAmountConverter;
import uk.org.openbanking.datamodel.account.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.OBAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification2;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification3;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification6;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.OBCashAccountConverter.toOBCashAccount2;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.OBCashAccountConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.OBCashAccountConverter.toOBCashAccount6;

public final class OBTransactionConverter {

    public static OBTransaction1 toTransaction1(OBTransaction2 obTransaction2) {
        OBTransaction1 transaction1 = (new OBTransaction1()).accountId(obTransaction2.getAccountId()).amount(obTransaction2.getAmount()).creditDebitIndicator(obTransaction2.getCreditDebitIndicator()).status(obTransaction2.getStatus()).bookingDateTime(obTransaction2.getBookingDateTime());
        if (obTransaction2.getTransactionId() != null) {
            transaction1.transactionId(obTransaction2.getTransactionId());
        }

        if (obTransaction2.getTransactionReference() != null) {
            transaction1.transactionReference(obTransaction2.getTransactionReference());
        }

        if (obTransaction2.getValueDateTime() != null) {
            transaction1.valueDateTime(obTransaction2.getValueDateTime());
        }

        if (obTransaction2.getTransactionInformation() != null) {
            transaction1.transactionInformation(obTransaction2.getTransactionInformation());
        }

        if (obTransaction2.getAddressLine() != null) {
            transaction1.addressLine(obTransaction2.getAddressLine());
        }

        if (obTransaction2.getBankTransactionCode() != null) {
            transaction1.bankTransactionCode(obTransaction2.getBankTransactionCode());
        }

        if (obTransaction2.getProprietaryBankTransactionCode() != null) {
            transaction1.proprietaryBankTransactionCode(obTransaction2.getProprietaryBankTransactionCode());
        }

        if (obTransaction2.getBalance() != null) {
            transaction1.balance(obTransaction2.getBalance());
        }

        if (obTransaction2.getMerchantDetails() != null) {
            transaction1.merchantDetails(obTransaction2.getMerchantDetails());
        }

        return transaction1;
    }

    public static OBTransaction1 toTransaction1(OBTransaction3 obTransaction3) {
        OBTransaction1 transaction1 = (new OBTransaction1()).accountId(obTransaction3.getAccountId()).amount(obTransaction3.getAmount()).creditDebitIndicator(obTransaction3.getCreditDebitIndicator()).status(obTransaction3.getStatus()).bookingDateTime(obTransaction3.getBookingDateTime());
        if (obTransaction3.getTransactionId() != null) {
            transaction1.transactionId(obTransaction3.getTransactionId());
        }

        if (obTransaction3.getTransactionReference() != null) {
            transaction1.transactionReference(obTransaction3.getTransactionReference());
        }

        if (obTransaction3.getValueDateTime() != null) {
            transaction1.valueDateTime(obTransaction3.getValueDateTime());
        }

        if (obTransaction3.getTransactionInformation() != null) {
            transaction1.transactionInformation(obTransaction3.getTransactionInformation());
        }

        if (obTransaction3.getAddressLine() != null) {
            transaction1.addressLine(obTransaction3.getAddressLine());
        }

        if (obTransaction3.getBankTransactionCode() != null) {
            transaction1.bankTransactionCode(obTransaction3.getBankTransactionCode());
        }

        if (obTransaction3.getProprietaryBankTransactionCode() != null) {
            transaction1.proprietaryBankTransactionCode((new ProprietaryBankTransactionCodeStructure1()).code(obTransaction3.getProprietaryBankTransactionCode().getCode()).issuer(obTransaction3.getProprietaryBankTransactionCode().getIssuer()));
        }

        if (obTransaction3.getBalance() != null) {
            transaction1.balance(obTransaction3.getBalance());
        }

        if (obTransaction3.getMerchantDetails() != null) {
            transaction1.merchantDetails(obTransaction3.getMerchantDetails());
        }

        return transaction1;
    }

    public static OBTransaction2 toTransaction2(OBTransaction3 obTransaction3) {
        OBTransaction2 transaction2 = (new OBTransaction2()).accountId(obTransaction3.getAccountId()).amount(obTransaction3.getAmount()).creditDebitIndicator(obTransaction3.getCreditDebitIndicator()).status(obTransaction3.getStatus()).bookingDateTime(obTransaction3.getBookingDateTime());
        if (obTransaction3.getTransactionId() != null) {
            transaction2.transactionId(obTransaction3.getTransactionId());
        }

        if (obTransaction3.getTransactionReference() != null) {
            transaction2.transactionReference(obTransaction3.getTransactionReference());
        }

        if (obTransaction3.getValueDateTime() != null) {
            transaction2.valueDateTime(obTransaction3.getValueDateTime());
        }

        if (obTransaction3.getTransactionInformation() != null) {
            transaction2.transactionInformation(obTransaction3.getTransactionInformation());
        }

        if (obTransaction3.getAddressLine() != null) {
            transaction2.addressLine(obTransaction3.getAddressLine());
        }

        if (obTransaction3.getBankTransactionCode() != null) {
            transaction2.bankTransactionCode(obTransaction3.getBankTransactionCode());
        }

        if (obTransaction3.getProprietaryBankTransactionCode() != null) {
            transaction2.proprietaryBankTransactionCode((new ProprietaryBankTransactionCodeStructure1()).code(obTransaction3.getProprietaryBankTransactionCode().getCode()).issuer(obTransaction3.getProprietaryBankTransactionCode().getIssuer()));
        }

        if (obTransaction3.getBalance() != null) {
            transaction2.balance(obTransaction3.getBalance());
        }

        if (obTransaction3.getMerchantDetails() != null) {
            transaction2.merchantDetails(obTransaction3.getMerchantDetails());
        }

        return transaction2;
    }

    public static OBTransaction2 toTransaction2(OBTransaction1 obTransaction1) {
        OBTransaction2 OBTransaction2 = (new OBTransaction2()).accountId(obTransaction1.getAccountId()).amount(obTransaction1.getAmount()).creditDebitIndicator(obTransaction1.getCreditDebitIndicator()).status(obTransaction1.getStatus()).bookingDateTime(obTransaction1.getBookingDateTime());
        if (obTransaction1.getTransactionId() != null) {
            OBTransaction2.transactionId(obTransaction1.getTransactionId());
        }

        if (obTransaction1.getTransactionReference() != null) {
            OBTransaction2.transactionReference(obTransaction1.getTransactionReference());
        }

        if (obTransaction1.getValueDateTime() != null) {
            OBTransaction2.valueDateTime(obTransaction1.getValueDateTime());
        }

        if (obTransaction1.getTransactionInformation() != null) {
            OBTransaction2.transactionInformation(obTransaction1.getTransactionInformation());
        }

        if (obTransaction1.getAddressLine() != null) {
            OBTransaction2.addressLine(obTransaction1.getAddressLine());
        }

        if (obTransaction1.getBankTransactionCode() != null) {
            OBTransaction2.bankTransactionCode(obTransaction1.getBankTransactionCode());
        }

        if (obTransaction1.getProprietaryBankTransactionCode() != null) {
            OBTransaction2.proprietaryBankTransactionCode(obTransaction1.getProprietaryBankTransactionCode());
        }

        if (obTransaction1.getBalance() != null) {
            OBTransaction2.balance(obTransaction1.getBalance());
        }

        if (obTransaction1.getMerchantDetails() != null) {
            OBTransaction2.merchantDetails(obTransaction1.getMerchantDetails());
        }

        return OBTransaction2;
    }

    public static OBTransaction3 toTransaction3(OBTransaction2 obTransaction2) {
        OBTransaction3 OBTransaction3 = (new OBTransaction3()).accountId(obTransaction2.getAccountId()).amount(obTransaction2.getAmount()).creditDebitIndicator(obTransaction2.getCreditDebitIndicator()).status(obTransaction2.getStatus()).bookingDateTime(obTransaction2.getBookingDateTime());
        if (obTransaction2.getTransactionId() != null) {
            OBTransaction3.transactionId(obTransaction2.getTransactionId());
        }

        if (obTransaction2.getTransactionReference() != null) {
            OBTransaction3.transactionReference(obTransaction2.getTransactionReference());
        }

        if (obTransaction2.getValueDateTime() != null) {
            OBTransaction3.valueDateTime(obTransaction2.getValueDateTime());
        }

        if (obTransaction2.getTransactionInformation() != null) {
            OBTransaction3.transactionInformation(obTransaction2.getTransactionInformation());
        }

        if (obTransaction2.getAddressLine() != null) {
            OBTransaction3.addressLine(obTransaction2.getAddressLine());
        }

        if (obTransaction2.getBankTransactionCode() != null) {
            OBTransaction3.bankTransactionCode(obTransaction2.getBankTransactionCode());
        }

        if (obTransaction2.getProprietaryBankTransactionCode() != null) {
            OBTransaction3.proprietaryBankTransactionCode((new OBTransaction3ProprietaryBankTransactionCode()).code(obTransaction2.getProprietaryBankTransactionCode().getCode()).issuer(obTransaction2.getProprietaryBankTransactionCode().getIssuer()));
        }

        if (obTransaction2.getBalance() != null) {
            OBTransaction3.balance(obTransaction2.getBalance());
        }

        if (obTransaction2.getMerchantDetails() != null) {
            OBTransaction3.merchantDetails(obTransaction2.getMerchantDetails());
        }

        return OBTransaction3;
    }

    public static OBTransaction4 toTransaction4(OBTransaction3 obTransaction3) {
        return (new OBTransaction4()).accountId(obTransaction3.getAccountId()).amount(obTransaction3.getAmount()).creditDebitIndicator(obTransaction3.getCreditDebitIndicator()).status(obTransaction3.getStatus()).bookingDateTime(obTransaction3.getBookingDateTime()).bankTransactionCode(obTransaction3.getBankTransactionCode()).proprietaryBankTransactionCode(obTransaction3.getProprietaryBankTransactionCode()).transactionId(obTransaction3.getTransactionId()).transactionInformation(obTransaction3.getTransactionInformation()).transactionReference(obTransaction3.getTransactionReference()).addressLine(obTransaction3.getAddressLine()).amount(obTransaction3.getAmount()).balance(obTransaction3.getBalance()).bookingDateTime(obTransaction3.getBookingDateTime()).cardInstrument(obTransaction3.getCardInstrument()).chargeAmount(obTransaction3.getChargeAmount()).creditDebitIndicator(obTransaction3.getCreditDebitIndicator()).creditorAccount(obTransaction3.getCreditorAccount()).creditorAgent(obTransaction3.getCreditorAgent()).currencyExchange(obTransaction3.getCurrencyExchange()).debtorAccount(obTransaction3.getDebtorAccount()).debtorAgent(obTransaction3.getDebtorAgent()).merchantDetails(obTransaction3.getMerchantDetails()).statementReference(obTransaction3.getStatementReference()).status(obTransaction3.getStatus()).valueDateTime(obTransaction3.getValueDateTime());
    }

    public static OBTransaction3 toOBTransaction3(OBTransaction5 obTransaction5) {
        OBTransaction4 obTransaction4 = OBTransactionConverter.toOBTransaction4(obTransaction5);
        return (new OBTransaction3()).accountId(obTransaction4.getAccountId()).amount(obTransaction4.getAmount()).creditDebitIndicator(obTransaction4.getCreditDebitIndicator()).status(obTransaction4.getStatus()).bookingDateTime(obTransaction4.getBookingDateTime()).bankTransactionCode(obTransaction4.getBankTransactionCode()).proprietaryBankTransactionCode(obTransaction4.getProprietaryBankTransactionCode()).transactionId(obTransaction4.getTransactionId()).transactionInformation(obTransaction4.getTransactionInformation()).transactionReference(obTransaction4.getTransactionReference()).addressLine(obTransaction4.getAddressLine()).amount(obTransaction4.getAmount()).balance(obTransaction4.getBalance()).bookingDateTime(obTransaction4.getBookingDateTime()).cardInstrument(obTransaction4.getCardInstrument()).chargeAmount(obTransaction4.getChargeAmount()).creditDebitIndicator(obTransaction4.getCreditDebitIndicator()).creditorAccount(obTransaction4.getCreditorAccount()).creditorAgent(obTransaction4.getCreditorAgent()).currencyExchange(obTransaction4.getCurrencyExchange()).debtorAccount(obTransaction4.getDebtorAccount()).debtorAgent(obTransaction4.getDebtorAgent()).merchantDetails(obTransaction4.getMerchantDetails()).statementReference(obTransaction4.getStatementReference()).status(obTransaction4.getStatus()).valueDateTime(obTransaction4.getValueDateTime());
    }

    public static OBTransaction2 toOBTransaction2(OBTransaction5 obTransaction5) {
        OBTransaction4 obTransaction4 = OBTransactionConverter.toOBTransaction4(obTransaction5);
        OBTransaction2 transaction2 = (new OBTransaction2()).accountId(obTransaction4.getAccountId()).amount(obTransaction4.getAmount()).creditDebitIndicator(obTransaction4.getCreditDebitIndicator()).status(obTransaction4.getStatus()).bookingDateTime(obTransaction4.getBookingDateTime());
        if (obTransaction4.getTransactionId() != null) {
            transaction2.transactionId(obTransaction4.getTransactionId());
        }

        if (obTransaction4.getTransactionReference() != null) {
            transaction2.transactionReference(obTransaction4.getTransactionReference());
        }

        if (obTransaction4.getValueDateTime() != null) {
            transaction2.valueDateTime(obTransaction4.getValueDateTime());
        }

        if (obTransaction4.getTransactionInformation() != null) {
            transaction2.transactionInformation(obTransaction4.getTransactionInformation());
        }

        if (obTransaction4.getAddressLine() != null) {
            transaction2.addressLine(obTransaction4.getAddressLine());
        }

        if (obTransaction4.getBankTransactionCode() != null) {
            transaction2.bankTransactionCode(obTransaction4.getBankTransactionCode());
        }

        if (obTransaction4.getProprietaryBankTransactionCode() != null) {
            transaction2.proprietaryBankTransactionCode((new ProprietaryBankTransactionCodeStructure1()).code(obTransaction4.getProprietaryBankTransactionCode().getCode()).issuer(obTransaction4.getProprietaryBankTransactionCode().getIssuer()));
        }

        if (obTransaction4.getBalance() != null) {
            transaction2.balance(obTransaction4.getBalance());
        }

        if (obTransaction4.getMerchantDetails() != null) {
            transaction2.merchantDetails(obTransaction4.getMerchantDetails());
        }

        return transaction2;
    }

    public static OBTransaction1 toOBTransaction1(OBTransaction5 obTransaction5) {
        OBTransaction4 obTransaction4 = toOBTransaction4(obTransaction5);
        OBTransaction1 transaction1 = (new OBTransaction1()).accountId(obTransaction4.getAccountId()).amount(obTransaction4.getAmount()).creditDebitIndicator(obTransaction4.getCreditDebitIndicator()).status(obTransaction4.getStatus()).bookingDateTime(obTransaction4.getBookingDateTime());
        if (obTransaction4.getTransactionId() != null) {
            transaction1.transactionId(obTransaction4.getTransactionId());
        }

        if (obTransaction4.getTransactionReference() != null) {
            transaction1.transactionReference(obTransaction4.getTransactionReference());
        }

        if (obTransaction4.getValueDateTime() != null) {
            transaction1.valueDateTime(obTransaction4.getValueDateTime());
        }

        if (obTransaction4.getTransactionInformation() != null) {
            transaction1.transactionInformation(obTransaction4.getTransactionInformation());
        }

        if (obTransaction4.getAddressLine() != null) {
            transaction1.addressLine(obTransaction4.getAddressLine());
        }

        if (obTransaction4.getBankTransactionCode() != null) {
            transaction1.bankTransactionCode(obTransaction4.getBankTransactionCode());
        }

        if (obTransaction4.getProprietaryBankTransactionCode() != null) {
            transaction1.proprietaryBankTransactionCode((new ProprietaryBankTransactionCodeStructure1()).code(obTransaction4.getProprietaryBankTransactionCode().getCode()).issuer(obTransaction4.getProprietaryBankTransactionCode().getIssuer()));
        }

        if (obTransaction4.getBalance() != null) {
            transaction1.balance(obTransaction4.getBalance());
        }

        if (obTransaction4.getMerchantDetails() != null) {
            transaction1.merchantDetails(obTransaction4.getMerchantDetails());
        }

        return transaction1;
    }

    public static OBTransaction1 toOBTransaction1(OBTransaction6 obTransaction6) {
        return obTransaction6 == null ? null : (new OBTransaction1())
                .accountId(obTransaction6.getAccountId())
                .transactionId(obTransaction6.getTransactionId())
                .transactionReference(obTransaction6.getTransactionReference())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(obTransaction6.getAmount()))
                .creditDebitIndicator(toOBCreditDebitCode(obTransaction6.getCreditDebitIndicator()))
                .status(obTransaction6.getStatus())
                .bookingDateTime(obTransaction6.getBookingDateTime())
                .valueDateTime(obTransaction6.getValueDateTime())
                .transactionInformation(obTransaction6.getTransactionInformation())
                .addressLine(obTransaction6.getAddressLine())
                .bankTransactionCode(obTransaction6.getBankTransactionCode())
                .proprietaryBankTransactionCode(obTransaction6.getProprietaryBankTransactionCode())
                .balance(obTransaction6.getBalance())
                .merchantDetails(obTransaction6.getMerchantDetails());
    }

    public static OBTransaction2 toOBTransaction2(OBTransaction6 obTransaction6) {
        return obTransaction6 == null ? null : (new OBTransaction2())
                .accountId(obTransaction6.getAccountId())
                .transactionId(obTransaction6.getTransactionId())
                .transactionReference(obTransaction6.getTransactionReference())
                .statementReference(obTransaction6.getStatementReference())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(obTransaction6.getAmount()))
                .creditDebitIndicator(toOBCreditDebitCode(obTransaction6.getCreditDebitIndicator()))
                .status(obTransaction6.getStatus())
                .bookingDateTime(obTransaction6.getBookingDateTime())
                .valueDateTime(obTransaction6.getValueDateTime())
                .addressLine(obTransaction6.getAddressLine())
                .bankTransactionCode(obTransaction6.getBankTransactionCode())
                .proprietaryBankTransactionCode(obTransaction6.getProprietaryBankTransactionCode())
                .equivalentAmount(null)
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification2(obTransaction6.getCreditorAgent()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification2(obTransaction6.getDebtorAgent()))
                .cardInstrument(obTransaction6.getCardInstrument())
                .transactionInformation(obTransaction6.getTransactionInformation())
                .balance(obTransaction6.getBalance())
                .merchantDetails(obTransaction6.getMerchantDetails())
                .creditorAccount(toOBCashAccount2(obTransaction6.getCreditorAccount()))
                .debtorAccount(toOBBranchAndFinancialInstitutionIdentification2(obTransaction6.getDebtorAccount()));
    }

    public static OBTransaction3 toOBTransaction3(OBTransaction6 obTransaction6) {
        return obTransaction6 == null ? null : (new OBTransaction3())
                .accountId(obTransaction6.getAccountId())
                .transactionId(obTransaction6.getTransactionId())
                .transactionReference(obTransaction6.getTransactionReference())
                .statementReference(obTransaction6.getStatementReference())
                .creditDebitIndicator(toOBCreditDebitCode(obTransaction6.getCreditDebitIndicator()))
                .status(obTransaction6.getStatus())
                .bookingDateTime(obTransaction6.getBookingDateTime())
                .valueDateTime(obTransaction6.getValueDateTime())
                .addressLine(obTransaction6.getAddressLine())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(obTransaction6.getAmount()))
                .chargeAmount(toOBActiveOrHistoricCurrencyAndAmount(obTransaction6.getChargeAmount()))
                .currencyExchange(obTransaction6.getCurrencyExchange())
                .bankTransactionCode(obTransaction6.getBankTransactionCode())
                .proprietaryBankTransactionCode(toOBTransaction3ProprietaryBankTransactionCode(obTransaction6.getProprietaryBankTransactionCode()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(obTransaction6.getCreditorAgent()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification3(obTransaction6.getDebtorAgent()))
                .debtorAccount(toOBCashAccount3(obTransaction6.getDebtorAccount()))
                .cardInstrument(obTransaction6.getCardInstrument())
                .transactionInformation(obTransaction6.getTransactionInformation())
                .balance(obTransaction6.getBalance())
                .merchantDetails(obTransaction6.getMerchantDetails())
                .creditorAccount(toOBCashAccount3(obTransaction6.getCreditorAccount()));
    }

    public static OBTransaction4 toOBTransaction4(OBTransaction6 obTransaction6) {
        return obTransaction6 == null ? null : (new OBTransaction4())
                .accountId(obTransaction6.getAccountId())
                .transactionId(obTransaction6.getTransactionId())
                .transactionReference(obTransaction6.getTransactionReference())
                .statementReference(obTransaction6.getStatementReference())
                .creditDebitIndicator(toOBCreditDebitCode(obTransaction6.getCreditDebitIndicator()))
                .status(obTransaction6.getStatus())
                .bookingDateTime(obTransaction6.getBookingDateTime())
                .valueDateTime(obTransaction6.getValueDateTime())
                .addressLine(obTransaction6.getAddressLine())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(obTransaction6.getAmount()))
                .chargeAmount(toOBActiveOrHistoricCurrencyAndAmount(obTransaction6.getChargeAmount()))
                .currencyExchange(obTransaction6.getCurrencyExchange())
                .bankTransactionCode(obTransaction6.getBankTransactionCode())
                .proprietaryBankTransactionCode(toOBTransaction3ProprietaryBankTransactionCode(obTransaction6.getProprietaryBankTransactionCode()))
                .cardInstrument(obTransaction6.getCardInstrument())
                .supplementaryData(obTransaction6.getSupplementaryData())
                .transactionInformation(obTransaction6.getTransactionInformation())
                .balance(obTransaction6.getBalance())
                .merchantDetails(obTransaction6.getMerchantDetails())
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(obTransaction6.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(obTransaction6.getCreditorAccount()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification3(obTransaction6.getDebtorAgent()))
                .debtorAccount(toOBCashAccount3(obTransaction6.getDebtorAccount()));
    }

    public static OBTransaction4 toOBTransaction4(OBTransaction5 transaction) {
        return new OBTransaction4()
                .accountId(transaction.getAccountId())
                .amount(OBActiveOrHistoricCurrencyAndAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount(transaction.getAmount()))
                .addressLine(transaction.getAddressLine())
                .balance(transaction.getBalance()).bookingDateTime(transaction.getBookingDateTime())
                .bookingDateTime(transaction.getBookingDateTime())
                .bankTransactionCode(transaction.getBankTransactionCode())
                .cardInstrument(transaction.getCardInstrument())
                .chargeAmount(OBActiveOrHistoricCurrencyAndAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount(transaction.getChargeAmount()))
                .creditDebitIndicator(OBCreditDebitCode.valueOf(transaction.getCreditDebitIndicator().name()))
                .creditorAccount(OBCashAccountConverter.toOBCashAccount3(transaction.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(transaction.getCreditorAgent()))
                .currencyExchange(transaction.getCurrencyExchange())
                .debtorAccount(OBCashAccountConverter.toOBCashAccount3(transaction.getDebtorAccount()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification3(transaction.getDebtorAgent()))
                .merchantDetails(transaction.getMerchantDetails())
                .proprietaryBankTransactionCode(toOBTransaction3ProprietaryBankTransactionCode(transaction.getProprietaryBankTransactionCode()))
                .statementReference(transaction.getStatementReference())
                .status(transaction.getStatus())
                .transactionId(transaction.getTransactionId())
                .transactionInformation(transaction.getTransactionInformation())
                .transactionReference(transaction.getTransactionReference())
                .valueDateTime(transaction.getValueDateTime())
                .supplementaryData(transaction.getSupplementaryData());
    }

    public static OBTransaction5 toOBTransaction5(OBTransaction4 transaction) {
        return new OBTransaction5()
                .accountId(transaction.getAccountId())
                .amount(OBActiveOrHistoricCurrencyAndAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount(transaction.getAmount()))
                .addressLine(transaction.getAddressLine())
                .balance(transaction.getBalance()).bookingDateTime(transaction.getBookingDateTime())
                .bookingDateTime(transaction.getBookingDateTime())
                .bankTransactionCode(transaction.getBankTransactionCode())
                .cardInstrument(transaction.getCardInstrument())
                .chargeAmount(OBActiveOrHistoricCurrencyAndAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount(transaction.getChargeAmount()))
                .creditDebitIndicator(OBTransaction5.CreditDebitIndicatorEnum.valueOf(transaction.getCreditDebitIndicator().name()))
                .creditorAccount(toOBCashAccount6(transaction.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(transaction.getCreditorAgent()))
                .currencyExchange(transaction.getCurrencyExchange())
                .debtorAccount(toOBCashAccount6(transaction.getDebtorAccount()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification6(transaction.getDebtorAgent()))
                .merchantDetails(transaction.getMerchantDetails())
                .proprietaryBankTransactionCode(toOBTransaction5ProprietaryBankTransactionCode(transaction.getProprietaryBankTransactionCode()))
                .statementReference(transaction.getStatementReference())
                .status(transaction.getStatus())
                .transactionId(transaction.getTransactionId())
                .transactionInformation(transaction.getTransactionInformation())
                .transactionReference(transaction.getTransactionReference())
                .valueDateTime(transaction.getValueDateTime())
                .supplementaryData(transaction.getSupplementaryData());
    }

    public static OBTransaction5 toOBTransaction5(OBTransaction6 transaction) {
        return new OBTransaction5()
                .accountId(transaction.getAccountId())
                .amount(toAccountOBActiveOrHistoricCurrencyAndAmount(transaction.getAmount()))
                .addressLine(transaction.getAddressLine())
                .balance(transaction.getBalance()).bookingDateTime(transaction.getBookingDateTime())
                .bookingDateTime(transaction.getBookingDateTime())
                .bankTransactionCode(transaction.getBankTransactionCode())
                .cardInstrument(transaction.getCardInstrument())
                .chargeAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(transaction.getChargeAmount()))
                .creditDebitIndicator(OBTransaction5.CreditDebitIndicatorEnum.valueOf(transaction.getCreditDebitIndicator().name()))
                .creditorAccount(toOBCashAccount6(transaction.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(transaction.getCreditorAgent()))
                .currencyExchange(transaction.getCurrencyExchange())
                .debtorAccount(toOBCashAccount6(transaction.getDebtorAccount()))
                .debtorAgent(toOBBranchAndFinancialInstitutionIdentification6(transaction.getDebtorAgent()))
                .merchantDetails(transaction.getMerchantDetails())
                .proprietaryBankTransactionCode(toOBTransaction5ProprietaryBankTransactionCode(transaction.getProprietaryBankTransactionCode()))
                .statementReference(transaction.getStatementReference())
                .status(transaction.getStatus())
                .transactionId(transaction.getTransactionId())
                .transactionInformation(transaction.getTransactionInformation())
                .transactionReference(transaction.getTransactionReference())
                .valueDateTime(transaction.getValueDateTime())
                .supplementaryData(transaction.getSupplementaryData());
    }

    public static OBCreditDebitCode toOBCreditDebitCode(OBCreditDebitCode1 creditDebitIndicator) {
        return FRModelMapper.map(creditDebitIndicator, OBCreditDebitCode.class);
    }

    public static OBTransaction3ProprietaryBankTransactionCode toOBTransaction3ProprietaryBankTransactionCode(ProprietaryBankTransactionCodeStructure1 proprietaryBankTransactionCode) {
        return FRModelMapper.map(proprietaryBankTransactionCode, OBTransaction3ProprietaryBankTransactionCode.class);
    }

    private static OBTransaction5ProprietaryBankTransactionCode toOBTransaction5ProprietaryBankTransactionCode(ProprietaryBankTransactionCodeStructure1 proprietaryBankTransactionCode) {
        return FRModelMapper.map(proprietaryBankTransactionCode, OBTransaction5ProprietaryBankTransactionCode.class);
    }

    private static OBTransaction3ProprietaryBankTransactionCode toOBTransaction3ProprietaryBankTransactionCode(OBTransaction5ProprietaryBankTransactionCode obTransaction5ProprietaryBankTransactionCode) {
        return FRModelMapper.map(obTransaction5ProprietaryBankTransactionCode, OBTransaction3ProprietaryBankTransactionCode.class);
    }

    private static OBTransaction5ProprietaryBankTransactionCode toOBTransaction5ProprietaryBankTransactionCode(OBTransaction3ProprietaryBankTransactionCode obTransaction3ProprietaryBankTransactionCode) {
        return FRModelMapper.map(obTransaction3ProprietaryBankTransactionCode, OBTransaction5ProprietaryBankTransactionCode.class);
    }
}
