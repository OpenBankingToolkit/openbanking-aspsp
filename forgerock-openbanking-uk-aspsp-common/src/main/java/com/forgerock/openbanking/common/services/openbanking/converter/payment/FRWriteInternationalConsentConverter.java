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

import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalDataInitiation;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.payment.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.toFRAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.toOBWriteDomestic2DataInitiationCreditorAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.toOBWriteDomestic2DataInitiationDebtorAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBWriteDomestic2DataInitiationInstructedAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialIdentificationConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRChargeBearerConverter.toFRChargeBearerType;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRChargeBearerConverter.toOBChargeBearerType1Code;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataAuthorisationConverter.toFRDataAuthorisation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataSCASupportDataConverter.toFRDataSCASupportData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRExchangeRateConverter.toFRExchangeRateInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRExchangeRateConverter.toOBExchangeRate1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRExchangeRateConverter.toOBWriteInternational3DataInitiationExchangeRateInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInstructionPriorityConverter.toFRInstructionPriority;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInstructionPriorityConverter.toOBPriority2Code;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInstructionPriorityConverter.toOBWriteInternational3DataInitiationInstructionPriority;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRReadRefundAccountConverter.toFRReadRefundAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toFRRemittanceInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toOBRemittanceInformation1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toOBWriteDomestic2DataInitiationRemittanceInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRSupplementaryDataConverter.toFRSupplementaryData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRSupplementaryDataConverter.toOBSupplementaryData1;
import static uk.org.openbanking.datamodel.service.converter.payment.CountryCodeHelper.determineCountryCode;

public class FRWriteInternationalConsentConverter {

    // OB to FR
    public static FRWriteInternationalConsent toFRWriteInternationalConsent(OBWriteInternationalConsent1 obWriteInternationalConsent1) {
        return obWriteInternationalConsent1 == null ? null : FRWriteInternationalConsent.builder()
                .data(toFRWriteInternationalConsentData(obWriteInternationalConsent1.getData()))
                .risk(toFRRisk(obWriteInternationalConsent1.getRisk()))
                .build();
    }

    public static FRWriteInternationalConsent toFRWriteInternationalConsent(OBWriteInternationalConsent2 obWriteInternationalConsent2) {
        return obWriteInternationalConsent2 == null ? null : FRWriteInternationalConsent.builder()
                .data(toFRWriteInternationalConsentData(obWriteInternationalConsent2.getData()))
                .risk(toFRRisk(obWriteInternationalConsent2.getRisk()))
                .build();
    }

    public static FRWriteInternationalConsent toFRWriteInternationalConsent(OBWriteInternationalConsent4 obWriteInternationalConsent4) {
        return obWriteInternationalConsent4 == null ? null : FRWriteInternationalConsent.builder()
                .data(toFRWriteInternationalConsentData(obWriteInternationalConsent4.getData()))
                .risk(toFRRisk(obWriteInternationalConsent4.getRisk()))
                .build();
    }

    public static FRWriteInternationalConsent toFRWriteInternationalConsent(OBWriteInternationalConsent5 obWriteInternationalConsent5) {
        return obWriteInternationalConsent5 == null ? null : FRWriteInternationalConsent.builder()
                .data(toFRWriteInternationalConsentData(obWriteInternationalConsent5.getData()))
                .risk(toFRRisk(obWriteInternationalConsent5.getRisk()))
                .build();
    }

    public static FRWriteInternationalConsentData toFRWriteInternationalConsentData(OBWriteDataInternationalConsent1 data) {
        return data == null ? null : FRWriteInternationalConsentData.builder()
                .initiation(toFRWriteInternationalDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteInternationalConsentData toFRWriteInternationalConsentData(OBWriteDataInternationalConsent2 data) {
        return data == null ? null : FRWriteInternationalConsentData.builder()
                .initiation(toFRWriteInternationalDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteInternationalConsentData toFRWriteInternationalConsentData(OBWriteInternationalConsent4Data data) {
        return data == null ? null : FRWriteInternationalConsentData.builder()
                .initiation(toFRWriteInternationalDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .scASupportData(toFRDataSCASupportData(data.getScASupportData()))
                .build();
    }

    public static FRWriteInternationalConsentData toFRWriteInternationalConsentData(OBWriteInternationalConsent5Data data) {
        return data == null ? null : FRWriteInternationalConsentData.builder()
                .readRefundAccount(toFRReadRefundAccount(data.getReadRefundAccount()))
                .initiation(toFRWriteInternationalDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .scASupportData(toFRDataSCASupportData(data.getScASupportData()))
                .build();
    }

    public static FRWriteInternationalDataInitiation toFRWriteInternationalDataInitiation(OBInternational1 initiation) {
        OBCashAccount3 creditorAccount = initiation.getCreditorAccount();
        return initiation == null ? null : FRWriteInternationalDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toFRInstructionPriority(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(toFRChargeBearerType(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(determineCountryCode(creditorAccount.getSchemeName(), creditorAccount.getIdentification())) // default value to prevent validation error
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toFRExchangeRateInformation(initiation.getExchangeRateInformation()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditor(toFRDataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toFRDataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toFRAccount(creditorAccount))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .build();
    }

    public static FRWriteInternationalDataInitiation toFRWriteInternationalDataInitiation(OBInternational2 initiation) {
        OBCashAccount3 creditorAccount = initiation.getCreditorAccount();
        return initiation == null ? null : FRWriteInternationalDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toFRInstructionPriority(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(toFRChargeBearerType(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(determineCountryCode(creditorAccount.getSchemeName(), creditorAccount.getIdentification())) // default value to prevent validation error
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toFRExchangeRateInformation(initiation.getExchangeRateInformation()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditor(toFRDataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toFRDataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toFRAccount(creditorAccount))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    public static FRWriteInternationalDataInitiation toFRWriteInternationalDataInitiation(OBWriteInternational3DataInitiation initiation) {
        return initiation == null ? null : FRWriteInternationalDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toFRInstructionPriority(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .extendedPurpose(initiation.getExtendedPurpose())
                .chargeBearer(toFRChargeBearerType(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(initiation.getDestinationCountryCode())
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toFRExchangeRateInformation(initiation.getExchangeRateInformation()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditor(toFRDataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toFRDataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    // FR to OB
    public static OBWriteInternational3DataInitiation toOBWriteInternational3DataInitiation(FRWriteInternationalDataInitiation initiation) {
        return initiation == null ? null : new OBWriteInternational3DataInitiation()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBWriteInternational3DataInitiationInstructionPriority(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .extendedPurpose(initiation.getExtendedPurpose())
                .chargeBearer(toOBChargeBearerType1Code(initiation.getChargeBearer()))
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

    public static OBInternational1 toOBInternational1(FRWriteInternationalDataInitiation initiation) {
        return initiation == null ? null : new OBInternational1()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBPriority2Code(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(toOBChargeBearerType1Code(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toOBExchangeRate1(initiation.getExchangeRateInformation()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditor(toOBPartyIdentification43(initiation.getCreditor()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(initiation.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()));
    }

    public static OBInternational2 toOBInternational2(FRWriteInternationalDataInitiation initiation) {
        return initiation == null ? null : new OBInternational2()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBPriority2Code(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(toOBChargeBearerType1Code(initiation.getChargeBearer()))
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
