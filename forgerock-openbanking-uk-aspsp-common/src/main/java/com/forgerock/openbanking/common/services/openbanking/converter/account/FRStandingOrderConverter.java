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

import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStandingOrderData;
import uk.org.openbanking.datamodel.account.OBExternalStandingOrderStatus1Code;
import uk.org.openbanking.datamodel.account.OBStandingOrder1;
import uk.org.openbanking.datamodel.account.OBStandingOrder2;
import uk.org.openbanking.datamodel.account.OBStandingOrder3;
import uk.org.openbanking.datamodel.account.OBStandingOrder4;
import uk.org.openbanking.datamodel.account.OBStandingOrder5;
import uk.org.openbanking.datamodel.account.OBStandingOrder6;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountSupplementaryDataConverter.toOBSupplementaryData1;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount1;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount5;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount51;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialInstrumentConverter.toOBBranchAndFinancialInstitutionIdentification2;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialInstrumentConverter.toOBBranchAndFinancialInstitutionIdentification4;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialInstrumentConverter.toOBBranchAndFinancialInstitutionIdentification5;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialInstrumentConverter.toOBBranchAndFinancialInstitutionIdentification51;

public class FRStandingOrderConverter {

    public static OBStandingOrder1 toOBStandingOrder1(FRStandingOrderData standingOrder) {
        return standingOrder == null ? null : new OBStandingOrder1()
                .accountId(standingOrder.getAccountId())
                .standingOrderId(standingOrder.getStandingOrderId())
                .frequency(standingOrder.getFrequency())
                .reference(standingOrder.getReference())
                .firstPaymentDateTime(standingOrder.getFirstPaymentDateTime())
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getFirstPaymentAmount()))
                .nextPaymentDateTime(standingOrder.getNextPaymentDateTime())
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getNextPaymentAmount()))
                .finalPaymentDateTime(standingOrder.getFinalPaymentDateTime())
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getFinalPaymentAmount()))
                .servicer(toOBBranchAndFinancialInstitutionIdentification2(standingOrder.getCreditorAgent()))
                .creditorAccount(toOBCashAccount1(standingOrder.getCreditorAccount()));
    }

    public static OBStandingOrder2 toOBStandingOrder2(FRStandingOrderData standingOrder) {
        return standingOrder == null ? null : new OBStandingOrder2()
                .accountId(standingOrder.getAccountId())
                .standingOrderId(standingOrder.getStandingOrderId())
                .frequency(standingOrder.getFrequency())
                .reference(standingOrder.getReference())
                .firstPaymentDateTime(standingOrder.getFirstPaymentDateTime())
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getFirstPaymentAmount()))
                .nextPaymentDateTime(standingOrder.getNextPaymentDateTime())
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getNextPaymentAmount()))
                .finalPaymentDateTime(standingOrder.getFinalPaymentDateTime())
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getFinalPaymentAmount()))
                .standingOrderStatusCode(toOBExternalStandingOrderStatus1Code(standingOrder.getStandingOrderStatusCode()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification2(standingOrder.getCreditorAgent()))
                .creditorAccount(toOBCashAccount1(standingOrder.getCreditorAccount()));
    }

    public static OBStandingOrder3 toOBStandingOrder3(FRStandingOrderData standingOrder) {
        return standingOrder == null ? null : new OBStandingOrder3()
                .accountId(standingOrder.getAccountId())
                .standingOrderId(standingOrder.getStandingOrderId())
                .frequency(standingOrder.getFrequency())
                .reference(standingOrder.getReference())
                .firstPaymentDateTime(standingOrder.getFirstPaymentDateTime())
                .nextPaymentDateTime(standingOrder.getNextPaymentDateTime())
                .finalPaymentDateTime(standingOrder.getFinalPaymentDateTime())
                .standingOrderStatusCode(toOBExternalStandingOrderStatus1Code(standingOrder.getStandingOrderStatusCode()))
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getFirstPaymentAmount()))
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getNextPaymentAmount()))
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getFinalPaymentAmount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification4(standingOrder.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(standingOrder.getCreditorAccount()));
    }

    public static OBStandingOrder4 toOBStandingOrder4(FRStandingOrderData standingOrder) {
        return standingOrder == null ? null : new OBStandingOrder4()
                .accountId(standingOrder.getAccountId())
                .standingOrderId(standingOrder.getStandingOrderId())
                .frequency(standingOrder.getFrequency())
                .reference(standingOrder.getReference())
                .firstPaymentDateTime(standingOrder.getFirstPaymentDateTime())
                .nextPaymentDateTime(standingOrder.getNextPaymentDateTime())
                .finalPaymentDateTime(standingOrder.getFinalPaymentDateTime())
                .standingOrderStatusCode(toOBExternalStandingOrderStatus1Code(standingOrder.getStandingOrderStatusCode()))
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getFirstPaymentAmount()))
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getNextPaymentAmount()))
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(standingOrder.getFinalPaymentAmount()))
                .supplementaryData(toOBSupplementaryData1(standingOrder.getSupplementaryData()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification4(standingOrder.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(standingOrder.getCreditorAccount()));
    }

    public static OBStandingOrder5 toOBStandingOrder5(FRStandingOrderData standingOrder) {
        return standingOrder == null ? null : new OBStandingOrder5()
                .accountId(standingOrder.getAccountId())
                .standingOrderId(standingOrder.getStandingOrderId())
                .frequency(standingOrder.getFrequency())
                .reference(standingOrder.getReference())
                .firstPaymentDateTime(standingOrder.getFirstPaymentDateTime())
                .nextPaymentDateTime(standingOrder.getNextPaymentDateTime())
                .finalPaymentDateTime(standingOrder.getFinalPaymentDateTime())
                .standingOrderStatusCode(toOBExternalStandingOrderStatus1Code(standingOrder.getStandingOrderStatusCode()))
                .firstPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(standingOrder.getFirstPaymentAmount()))
                .nextPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(standingOrder.getNextPaymentAmount()))
                .finalPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(standingOrder.getFinalPaymentAmount()))
                .supplementaryData(toOBSupplementaryData1(standingOrder.getSupplementaryData()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification5(standingOrder.getCreditorAgent()))
                .creditorAccount(toOBCashAccount5(standingOrder.getCreditorAccount()));
    }

    public static OBStandingOrder6 toOBStandingOrder6(FRStandingOrderData standingOrder) {
        return standingOrder == null ? null : new OBStandingOrder6()
                .accountId(standingOrder.getAccountId())
                .standingOrderId(standingOrder.getStandingOrderId())
                .frequency(standingOrder.getFrequency())
                .reference(standingOrder.getReference())
                .firstPaymentDateTime(standingOrder.getFirstPaymentDateTime())
                .nextPaymentDateTime(standingOrder.getNextPaymentDateTime())
                .lastPaymentDateTime(standingOrder.getLastPaymentDateTime())
                .finalPaymentDateTime(standingOrder.getFinalPaymentDateTime())
                .numberOfPayments(standingOrder.getNumberOfPayments())
                .standingOrderStatusCode(toOBExternalStandingOrderStatus1Code(standingOrder.getStandingOrderStatusCode()))
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount2(standingOrder.getFirstPaymentAmount()))
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount3(standingOrder.getNextPaymentAmount()))
                .lastPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount11(standingOrder.getLastPaymentAmount()))
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount4(standingOrder.getFinalPaymentAmount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification51(standingOrder.getCreditorAgent()))
                .creditorAccount(toOBCashAccount51(standingOrder.getCreditorAccount()))
                .supplementaryData(toOBSupplementaryData1(standingOrder.getSupplementaryData()));
    }

    public static OBExternalStandingOrderStatus1Code toOBExternalStandingOrderStatus1Code(FRStandingOrderData.FRStandingOrderStatus standingOrderStatusCode) {
        return standingOrderStatusCode == null ? null : OBExternalStandingOrderStatus1Code.valueOf(standingOrderStatusCode.name());
    }
}
