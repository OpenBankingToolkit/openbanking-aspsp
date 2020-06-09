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

import uk.org.openbanking.datamodel.payment.OBInternational1;
import uk.org.openbanking.datamodel.payment.OBInternational2;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3DataInitiation;

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
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBPriorityCodeConverter.toInstructionPriorityEnum;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBPriorityCodeConverter.toOBPriority2Code;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBRemittanceInformationConverter.toOBRemittanceInformation1;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBRemittanceInformationConverter.toOBWriteDomestic2DataInitiationRemittanceInformation;

public class OBInternationalConverter {

    public static OBInternational1 toOBInternational1(OBWriteInternational3DataInitiation initiation) {
        return initiation == null ? null : (new OBInternational1())
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBPriority2Code(initiation.getInstructionPriority()))
                .purpose(initiation.getPurpose())
                .chargeBearer(initiation.getChargeBearer())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .exchangeRateInformation(toOBExchangeRate1(initiation.getExchangeRateInformation()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditor(toOBPartyIdentification43(initiation.getCreditor()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(initiation.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()));
    }

    public static OBInternational2 toOBInternational2(OBWriteInternational3DataInitiation initiation) {
        return initiation == null ? null : (new OBInternational2())
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructionPriority(toOBPriority2Code(initiation.getInstructionPriority()))
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

    public static OBWriteInternational3DataInitiation toOBWriteInternational3DataInitiation(OBInternational2 obInternational2) {
        return obInternational2 == null ? null : (new OBWriteInternational3DataInitiation())
                .instructionIdentification(obInternational2.getInstructionIdentification())
                .endToEndIdentification(obInternational2.getEndToEndIdentification())
                .localInstrument(obInternational2.getLocalInstrument())
                .instructionPriority(toInstructionPriorityEnum(obInternational2.getInstructionPriority()))
                .purpose(obInternational2.getPurpose())
                .chargeBearer(obInternational2.getChargeBearer())
                .currencyOfTransfer(obInternational2.getCurrencyOfTransfer())
                .instructedAmount(toOBWriteDomestic2DataInitiationInstructedAmount(obInternational2.getInstructedAmount()))
                .exchangeRateInformation(toOBWriteInternational3DataInitiationExchangeRateInformation(obInternational2.getExchangeRateInformation()))
                .debtorAccount(toOBWriteDomestic2DataInitiationDebtorAccount(obInternational2.getDebtorAccount()))
                .creditor(toOBWriteInternational3DataInitiationCreditor(obInternational2.getCreditor()))
                .creditorAgent(toOBWriteInternational3DataInitiationCreditorAgent(obInternational2.getCreditorAgent()))
                .creditorAccount(toOBWriteDomestic2DataInitiationCreditorAccount(obInternational2.getCreditorAccount()))
                .remittanceInformation(toOBWriteDomestic2DataInitiationRemittanceInformation(obInternational2.getRemittanceInformation()))
                .supplementaryData(obInternational2.getSupplementaryData());
    }
}
