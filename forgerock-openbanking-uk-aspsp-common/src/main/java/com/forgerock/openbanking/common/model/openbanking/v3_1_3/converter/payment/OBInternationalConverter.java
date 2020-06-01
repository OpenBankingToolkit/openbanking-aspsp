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
package com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment;

import uk.org.openbanking.datamodel.payment.*;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAccountConverter.*;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAmountConverter.toOBWriteDomestic2DataInitiationInstructedAmount;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBExchangeRateConverter.toOBExchangeRate1;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBExchangeRateConverter.toOBWriteInternational3DataInitiationExchangeRateInformation;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBRemittanceInformationConverter.toOBRemittanceInformation1;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBRemittanceInformationConverter.toOBWriteDomestic2DataInitiationRemittanceInformation;

public class OBInternationalConverter {

    public static OBInternational2 toOBInternational2(OBWriteInternational3DataInitiation initiation) {
        return (new OBInternational2())
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(OBPriority2Code.valueOf(initiation.getInstructionPriority().name()))
                .purpose(initiation.getPurpose())
                .chargeBearer(initiation.getChargeBearer())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toOBExchangeRate1(initiation.getExchangeRateInformation()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditor(toOBPartyIdentification43(initiation.getCreditor()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(initiation.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()))
                .supplementaryData(initiation.getSupplementaryData());
    }

    public static OBWriteInternational3DataInitiation toOBWriteInternational3DataInitiation(OBInternational2 initiation) {
        return (new OBWriteInternational3DataInitiation())
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(OBWriteInternational3DataInitiation.InstructionPriorityEnum.valueOf(initiation.getInstructionPriority().name()))
                .purpose(initiation.getPurpose())
                //.extendedPurpose(initiation.getExtendedPurpose()) // TODO #216 populate the extended purpose
                .chargeBearer(initiation.getChargeBearer())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                //.destinationCountryCode(initiation.getDestinationCountryCode()) // TODO #216 populate the country code
                .instructedAmount(toOBWriteDomestic2DataInitiationInstructedAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toOBWriteInternational3DataInitiationExchangeRateInformation(initiation.getExchangeRateInformation()))
                .debtorAccount(toOBWriteDomestic2DataInitiationDebtorAccount(initiation.getDebtorAccount()))
                .creditor(toOBWriteInternational3DataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toOBWriteInternational3DataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toOBWriteDomestic2DataInitiationCreditorAccount(initiation.getCreditorAccount()))
                .remittanceInformation(toOBWriteDomestic2DataInitiationRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(initiation.getSupplementaryData());
    }

    public static OBWriteInternational2 toOBWriteInternational2(OBWriteInternational3 obWriteInternational3) {
        return (new OBWriteInternational2())
                .data(toOBWriteDataInternational2(obWriteInternational3.getData()))
                .risk(obWriteInternational3.getRisk());
    }

    public static OBWriteDataInternational2 toOBWriteDataInternational2(OBWriteInternational3Data data) {
        return (new OBWriteDataInternational2())
                .consentId(data.getConsentId())
                .initiation(toOBInternational2(data.getInitiation()));
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBWriteInternational3DataInitiationCreditorAgent creditorAgent) {
        return (new OBBranchAndFinancialInstitutionIdentification3())
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(creditorAgent.getPostalAddress());
    }

    public static OBPartyIdentification43 toOBPartyIdentification43(OBWriteInternational3DataInitiationCreditor creditor) {
        return (new OBPartyIdentification43())
                .name(creditor.getName())
                .postalAddress(creditor.getPostalAddress());
    }

    public static OBWriteInternational3DataInitiationCreditorAgent toOBWriteInternational3DataInitiationCreditorAgent(OBBranchAndFinancialInstitutionIdentification3 creditorAgent) {
        return (new OBWriteInternational3DataInitiationCreditorAgent())
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(creditorAgent.getPostalAddress());
    }

    public static OBWriteInternational3DataInitiationCreditor toOBWriteInternational3DataInitiationCreditor(OBPartyIdentification43 creditor) {
        return (new OBWriteInternational3DataInitiationCreditor())
                .name(creditor.getName())
                .postalAddress(creditor.getPostalAddress());
    }

}
