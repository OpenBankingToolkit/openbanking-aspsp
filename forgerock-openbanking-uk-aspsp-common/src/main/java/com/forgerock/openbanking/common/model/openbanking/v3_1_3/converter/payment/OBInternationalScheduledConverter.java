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

import uk.org.openbanking.datamodel.payment.OBInternationalScheduled1;
import uk.org.openbanking.datamodel.payment.OBInternationalScheduled2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled3DataInitiation;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAccountConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAccountConverter.toOBWriteDomestic2DataInitiationCreditorAccount;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAccountConverter.toOBWriteDomestic2DataInitiationDebtorAccount;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAmountConverter.toOBWriteDomestic2DataInitiationInstructedAmount;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBExchangeRateConverter.toOBExchangeRate1;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBExchangeRateConverter.toOBWriteInternational3DataInitiationExchangeRateInformation;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalIdentifierConverter.toOBBranchAndFinancialInstitutionIdentification3;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalIdentifierConverter.toOBPartyIdentification43;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalIdentifierConverter.toOBWriteInternational3DataInitiationCreditor;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalIdentifierConverter.toOBWriteInternational3DataInitiationCreditorAgent;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBPriorityCodeConverter.toOBPriority2Code;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBPriorityCodeConverter.toOBWriteInternationalScheduled3DataInitiationInstructionPriorityEnum;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBRemittanceInformationConverter.toOBRemittanceInformation1;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBRemittanceInformationConverter.toOBWriteDomestic2DataInitiationRemittanceInformation;

public class OBInternationalScheduledConverter {


    public static OBInternationalScheduled1 toOBInternationalScheduled1(OBWriteInternationalScheduled3DataInitiation initiation) {
        return initiation == null ? null : (new OBInternationalScheduled1())
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBPriority2Code(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(initiation.getChargeBearer())
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

    public static OBInternationalScheduled2 toOBInternationalScheduled2(OBWriteInternationalScheduled3DataInitiation initiation) {
        return initiation == null ? null : (new OBInternationalScheduled2())
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBPriority2Code(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(initiation.getChargeBearer())
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
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

    public static OBWriteInternationalScheduled3DataInitiation toOBWriteInternationalScheduled3DataInitiation(OBInternationalScheduled1 initiation) {
        return initiation == null ? null : (new OBWriteInternationalScheduled3DataInitiation())
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBWriteInternationalScheduled3DataInitiationInstructionPriorityEnum(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(initiation.getChargeBearer())
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBWriteDomestic2DataInitiationInstructedAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toOBWriteInternational3DataInitiationExchangeRateInformation(initiation.getExchangeRateInformation()))
                .debtorAccount(toOBWriteDomestic2DataInitiationDebtorAccount(initiation.getDebtorAccount()))
                .creditor(toOBWriteInternational3DataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toOBWriteInternational3DataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toOBWriteDomestic2DataInitiationCreditorAccount(initiation.getCreditorAccount()))
                .remittanceInformation(toOBWriteDomestic2DataInitiationRemittanceInformation(initiation.getRemittanceInformation()));
    }

    public static OBWriteInternationalScheduled3DataInitiation toOBWriteInternationalScheduled3DataInitiation(OBInternationalScheduled2 initiation) {
        return initiation == null ? null : (new OBWriteInternationalScheduled3DataInitiation())
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBWriteInternationalScheduled3DataInitiationInstructionPriorityEnum(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(initiation.getChargeBearer())
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBWriteDomestic2DataInitiationInstructedAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toOBWriteInternational3DataInitiationExchangeRateInformation(initiation.getExchangeRateInformation()))
                .debtorAccount(toOBWriteDomestic2DataInitiationDebtorAccount(initiation.getDebtorAccount()))
                .creditor(toOBWriteInternational3DataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toOBWriteInternational3DataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toOBWriteDomestic2DataInitiationCreditorAccount(initiation.getCreditorAccount()))
                .remittanceInformation(toOBWriteDomestic2DataInitiationRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(initiation.getSupplementaryData());
    }

}
