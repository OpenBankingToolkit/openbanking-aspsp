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

import com.forgerock.openbanking.common.model.openbanking.domain.account.FRScheduledPaymentData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRScheduledPaymentData.FRScheduleType;
import uk.org.openbanking.datamodel.account.OBExternalScheduleType1Code;
import uk.org.openbanking.datamodel.account.OBScheduledPayment1;
import uk.org.openbanking.datamodel.account.OBScheduledPayment2;
import uk.org.openbanking.datamodel.account.OBScheduledPayment3;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toFRAccountIdentifier;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount5;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount51;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount1;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialInstrumentConverter.toFRFinancialAgent;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialInstrumentConverter.toOBBranchAndFinancialInstitutionIdentification4;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialInstrumentConverter.toOBBranchAndFinancialInstitutionIdentification5;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialInstrumentConverter.toOBBranchAndFinancialInstitutionIdentification51;

public class FRScheduledPaymentConverter {

    // OB to FR
    public static FRScheduledPaymentData toFRScheduledPaymentData(OBScheduledPayment3 scheduledPayment) {
        return scheduledPayment == null ? null : FRScheduledPaymentData.builder()
                .accountId(scheduledPayment.getAccountId())
                .scheduledPaymentId(scheduledPayment.getScheduledPaymentId())
                .scheduledPaymentDateTime(scheduledPayment.getScheduledPaymentDateTime())
                .scheduledType(toFRScheduleType(scheduledPayment.getScheduledType()))
                .reference(scheduledPayment.getReference())
                .debtorReference(scheduledPayment.getDebtorReference())
                .instructedAmount(toFRAmount(scheduledPayment.getInstructedAmount()))
                .creditorAgent(toFRFinancialAgent(scheduledPayment.getCreditorAgent()))
                .creditorAccount(toFRAccountIdentifier(scheduledPayment.getCreditorAccount()))
                .build();
    }

    public static FRScheduleType toFRScheduleType(OBExternalScheduleType1Code scheduledType) {
        return scheduledType == null ? null : FRScheduleType.valueOf(scheduledType.name());
    }

    // FR to OB
    public static OBScheduledPayment1 toOBScheduledPayment1(FRScheduledPaymentData scheduledPaymentData) {
        return scheduledPaymentData == null ? null : new OBScheduledPayment1()
                .accountId(scheduledPaymentData.getAccountId())
                .scheduledPaymentId(scheduledPaymentData.getScheduledPaymentId())
                .scheduledPaymentDateTime(scheduledPaymentData.getScheduledPaymentDateTime())
                .scheduledType(toOBExternalScheduleType1Code(scheduledPaymentData.getScheduledType()))
                .reference(scheduledPaymentData.getReference())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(scheduledPaymentData.getInstructedAmount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification4(scheduledPaymentData.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(scheduledPaymentData.getCreditorAccount()));
    }

    public static OBScheduledPayment2 toOBScheduledPayment2(FRScheduledPaymentData scheduledPaymentData) {
        return scheduledPaymentData == null ? null : new OBScheduledPayment2()
                .accountId(scheduledPaymentData.getAccountId())
                .scheduledPaymentId(scheduledPaymentData.getScheduledPaymentId())
                .scheduledPaymentDateTime(scheduledPaymentData.getScheduledPaymentDateTime())
                .scheduledType(toOBExternalScheduleType1Code(scheduledPaymentData.getScheduledType()))
                .reference(scheduledPaymentData.getReference())
                .instructedAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(scheduledPaymentData.getInstructedAmount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification5(scheduledPaymentData.getCreditorAgent()))
                .creditorAccount(toOBCashAccount5(scheduledPaymentData.getCreditorAccount()));
    }

    public static OBScheduledPayment3 toOBScheduledPayment3(FRScheduledPaymentData scheduledPaymentData) {
        return scheduledPaymentData == null ? null : new OBScheduledPayment3()
                .accountId(scheduledPaymentData.getAccountId())
                .scheduledPaymentId(scheduledPaymentData.getScheduledPaymentId())
                .scheduledPaymentDateTime(scheduledPaymentData.getScheduledPaymentDateTime())
                .scheduledType(toOBExternalScheduleType1Code(scheduledPaymentData.getScheduledType()))
                .reference(scheduledPaymentData.getReference())
                .debtorReference(scheduledPaymentData.getDebtorReference())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount1(scheduledPaymentData.getInstructedAmount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification51(scheduledPaymentData.getCreditorAgent()))
                .creditorAccount(toOBCashAccount51(scheduledPaymentData.getCreditorAccount()));
    }

    public static OBExternalScheduleType1Code toOBExternalScheduleType1Code(FRScheduleType scheduledType) {
        return scheduledType == null ? null : OBExternalScheduleType1Code.valueOf(scheduledType.name());
    }


}
