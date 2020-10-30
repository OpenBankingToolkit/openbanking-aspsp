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
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalScheduledConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalScheduledConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalScheduledDataInitiation;
import com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.payment.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toFRAccountIdentifier;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBWriteDomestic2DataInitiationCreditorAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBWriteDomestic2DataInitiationDebtorAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBWriteDomestic2DataInitiationInstructedAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialInstrumentConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRChargeBearerConverter.toFRChargeBearerType;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRChargeBearerConverter.toOBChargeBearerType1Code;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataAuthorisationConverter.toFRDataAuthorisation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataSCASupportDataConverter.toFRDataSCASupportData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRExchangeRateConverter.toFRExchangeRateInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRExchangeRateConverter.toOBExchangeRate1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRExchangeRateConverter.toOBWriteInternational3DataInitiationExchangeRateInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInstructionPriorityConverter.toFRInstructionPriority;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInstructionPriorityConverter.toOBPriority2Code;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInstructionPriorityConverter.toOBWriteInternationalScheduled3DataInitiationInstructionPriority;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPermissionConverter.toFRPermission;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRReadRefundAccountConverter.toFRReadRefundAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toFRRemittanceInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toOBRemittanceInformation1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toOBWriteDomestic2DataInitiationRemittanceInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentSupplementaryDataConverter.toFRSupplementaryData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentSupplementaryDataConverter.toOBSupplementaryData1;
import static uk.org.openbanking.datamodel.service.converter.payment.CountryCodeHelper.determineCountryCode;

public class FRWriteInternationalScheduledConsentConverter {

    // OB to FR
    public static FRWriteInternationalScheduledConsent toFRWriteInternationalScheduledConsent(OBWriteInternationalScheduledConsent1 obWriteInternationalScheduledConsent1) {
        return obWriteInternationalScheduledConsent1 == null ? null : FRWriteInternationalScheduledConsent.builder()
                .data(toFRWriteInternationalScheduledConsentData(obWriteInternationalScheduledConsent1.getData()))
                .risk(toFRRisk(obWriteInternationalScheduledConsent1.getRisk()))
                .build();
    }

    public static FRWriteInternationalScheduledConsent toFRWriteInternationalScheduledConsent(OBWriteInternationalScheduledConsent2 obWriteInternationalScheduledConsent2) {
        return obWriteInternationalScheduledConsent2 == null ? null : FRWriteInternationalScheduledConsent.builder()
                .data(toFRWriteInternationalScheduledConsentData(obWriteInternationalScheduledConsent2.getData()))
                .risk(toFRRisk(obWriteInternationalScheduledConsent2.getRisk()))
                .build();
    }

    public static FRWriteInternationalScheduledConsent toFRWriteInternationalScheduledConsent(OBWriteInternationalScheduledConsent4 obWriteInternationalScheduledConsent4) {
        return obWriteInternationalScheduledConsent4 == null ? null : FRWriteInternationalScheduledConsent.builder()
                .data(toFRWriteInternationalScheduledConsentData(obWriteInternationalScheduledConsent4.getData()))
                .risk(toFRRisk(obWriteInternationalScheduledConsent4.getRisk()))
                .build();
    }

    public static FRWriteInternationalScheduledConsent toFRWriteInternationalScheduledConsent(OBWriteInternationalScheduledConsent5 obWriteInternationalScheduledConsent5) {
        return obWriteInternationalScheduledConsent5 == null ? null : FRWriteInternationalScheduledConsent.builder()
                .data(toFRWriteInternationalScheduledConsentData(obWriteInternationalScheduledConsent5.getData()))
                .risk(toFRRisk(obWriteInternationalScheduledConsent5.getRisk()))
                .build();
    }

    public static FRWriteInternationalScheduledConsentData toFRWriteInternationalScheduledConsentData(OBWriteDataInternationalScheduledConsent1 data) {
        return data == null ? null : FRWriteInternationalScheduledConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteInternationalScheduledDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteInternationalScheduledConsentData toFRWriteInternationalScheduledConsentData(OBWriteDataInternationalScheduledConsent2 data) {
        return data == null ? null : FRWriteInternationalScheduledConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteInternationalScheduledDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteInternationalScheduledConsentData toFRWriteInternationalScheduledConsentData(OBWriteInternationalScheduledConsent4Data data) {
        return data == null ? null : FRWriteInternationalScheduledConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteInternationalScheduledDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .scASupportData(toFRDataSCASupportData(data.getScASupportData()))
                .build();
    }

    public static FRWriteInternationalScheduledConsentData toFRWriteInternationalScheduledConsentData(OBWriteInternationalScheduledConsent5Data data) {
        return data == null ? null : FRWriteInternationalScheduledConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .readRefundAccount(toFRReadRefundAccount(data.getReadRefundAccount()))
                .initiation(toFRWriteInternationalScheduledDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .scASupportData(toFRDataSCASupportData(data.getScASupportData()))
                .build();
    }

    public static FRWriteInternationalScheduledDataInitiation toFRWriteInternationalScheduledDataInitiation(OBInternationalScheduled1 initiation) {
        OBCashAccount3 creditorAccount = initiation.getCreditorAccount();
        return initiation == null ? null : FRWriteInternationalScheduledDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toFRInstructionPriority(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(toFRChargeBearerType(initiation.getChargeBearer()))
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(determineCountryCode(creditorAccount.getSchemeName(), creditorAccount.getIdentification())) // default value to prevent validation error
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toFRExchangeRateInformation(initiation.getExchangeRateInformation()))
                .debtorAccount(FRAccountIdentifierConverter.toFRAccountIdentifier(initiation.getDebtorAccount()))
                .creditor(toFRFinancialCreditor(initiation.getCreditor()))
                .creditorAgent(toFRFinancialAgent(initiation.getCreditorAgent()))
                .creditorAccount(FRAccountIdentifierConverter.toFRAccountIdentifier(creditorAccount))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .build();
    }

    public static FRWriteInternationalScheduledDataInitiation toFRWriteInternationalScheduledDataInitiation(OBInternationalScheduled2 initiation) {
        OBCashAccount3 creditorAccount = initiation.getCreditorAccount();
        return initiation == null ? null : FRWriteInternationalScheduledDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toFRInstructionPriority(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(toFRChargeBearerType(initiation.getChargeBearer()))
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(determineCountryCode(creditorAccount.getSchemeName(), creditorAccount.getIdentification())) // default value to prevent validation error
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toFRExchangeRateInformation(initiation.getExchangeRateInformation()))
                .debtorAccount(FRAccountIdentifierConverter.toFRAccountIdentifier(initiation.getDebtorAccount()))
                .creditor(toFRFinancialCreditor(initiation.getCreditor()))
                .creditorAgent(toFRFinancialAgent(initiation.getCreditorAgent()))
                .creditorAccount(FRAccountIdentifierConverter.toFRAccountIdentifier(creditorAccount))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    public static FRWriteInternationalScheduledDataInitiation toFRWriteInternationalScheduledDataInitiation(OBWriteInternationalScheduled3DataInitiation initiation) {
        return initiation == null ? null : FRWriteInternationalScheduledDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toFRInstructionPriority(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .extendedPurpose(initiation.getExtendedPurpose())
                .chargeBearer(toFRChargeBearerType(initiation.getChargeBearer()))
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(initiation.getDestinationCountryCode())
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toFRExchangeRateInformation(initiation.getExchangeRateInformation()))
                .debtorAccount(FRAccountIdentifierConverter.toFRAccountIdentifier(initiation.getDebtorAccount()))
                .creditor(toFRFinancialCreditor(initiation.getCreditor()))
                .creditorAgent(toFRFinancialAgent(initiation.getCreditorAgent()))
                .creditorAccount(FRAccountIdentifierConverter.toFRAccountIdentifier(initiation.getCreditorAccount()))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }


    // FR to OB
    public static OBWriteInternationalScheduled3DataInitiation toOBWriteInternationalScheduled3DataInitiation(FRWriteInternationalScheduledDataInitiation initiation) {
        return initiation == null ? null : new OBWriteInternationalScheduled3DataInitiation()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBWriteInternationalScheduled3DataInitiationInstructionPriority(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .extendedPurpose(initiation.getExtendedPurpose())
                .chargeBearer(toOBChargeBearerType1Code(initiation.getChargeBearer()))
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(initiation.getDestinationCountryCode())
                .instructedAmount(toOBWriteDomestic2DataInitiationInstructedAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toOBWriteInternational3DataInitiationExchangeRateInformation(initiation.getExchangeRateInformation()))
                .debtorAccount(toOBWriteDomestic2DataInitiationDebtorAccount(initiation.getDebtorAccount()))
                .creditor(toOBWriteInternational3DataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toOBWriteInternational3DataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toOBWriteDomestic2DataInitiationCreditorAccount(initiation.getCreditorAccount()))
                .remittanceInformation(toOBWriteDomestic2DataInitiationRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }

    public static OBInternationalScheduled1 toOBInternationalScheduled1(FRWriteInternationalScheduledDataInitiation initiation) {
        return initiation == null ? null : new OBInternationalScheduled1()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBPriority2Code(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(toOBChargeBearerType1Code(initiation.getChargeBearer()))
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toOBExchangeRate1(initiation.getExchangeRateInformation()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditor(toOBPartyIdentification43(initiation.getCreditor()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(initiation.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()));
    }

    public static OBInternationalScheduled2 toOBInternationalScheduled2(FRWriteInternationalScheduledDataInitiation initiation) {
        return initiation == null ? null : new OBInternationalScheduled2()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBPriority2Code(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(toOBChargeBearerType1Code(initiation.getChargeBearer()))
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toOBExchangeRate1(initiation.getExchangeRateInformation()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditor(toOBPartyIdentification43(initiation.getCreditor()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(initiation.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }
}
