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

import uk.org.openbanking.datamodel.account.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBAmountConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification2;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification4;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification5;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification51;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBCashAccountConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBCashAccountConverter.toOBCashAccount5;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBCashAccountConverter.toOBCashAccount51;
import static uk.org.openbanking.datamodel.service.converter.account.OBCashAccountConverter.toOBCashAccount1;

public class OBStandingOrderConverter {

    public static OBStandingOrder1 toOBStandingOrder1(OBStandingOrder6 obStandingOrder) {
        return obStandingOrder == null ? null : (new OBStandingOrder1())
                .accountId(obStandingOrder.getAccountId())
                .standingOrderId(obStandingOrder.getStandingOrderId())
                .frequency(obStandingOrder.getFrequency())
                .reference(obStandingOrder.getReference())
                .firstPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getFirstPaymentAmount()))
                .nextPaymentDateTime(obStandingOrder.getNextPaymentDateTime())
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getNextPaymentAmount()))
                .finalPaymentDateTime(obStandingOrder.getFinalPaymentDateTime())
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getFinalPaymentAmount()))
                .servicer(toOBBranchAndFinancialInstitutionIdentification2(obStandingOrder.getCreditorAgent()))
                .creditorAccount(OBCashAccountConverter.toOBCashAccount1(obStandingOrder.getCreditorAccount()));
    }

    public static OBStandingOrder2 toOBStandingOrder2(OBStandingOrder4 obStandingOrder) {
        return obStandingOrder == null ? null : (new OBStandingOrder2())
                .accountId(obStandingOrder.getAccountId())
                .standingOrderId(obStandingOrder.getStandingOrderId())
                .frequency(obStandingOrder.getFrequency())
                .reference(obStandingOrder.getReference())
                .firstPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .firstPaymentAmount(obStandingOrder.getFirstPaymentAmount())
                .nextPaymentDateTime(obStandingOrder.getNextPaymentDateTime())
                .nextPaymentAmount(obStandingOrder.getNextPaymentAmount())
                .finalPaymentDateTime(obStandingOrder.getFinalPaymentDateTime())
                .finalPaymentAmount(obStandingOrder.getFinalPaymentAmount())
                .standingOrderStatusCode(obStandingOrder.getStandingOrderStatusCode())
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification2(obStandingOrder.getCreditorAgent()))
                .creditorAccount(toOBCashAccount1(obStandingOrder.getCreditorAccount()));
    }

    public static OBStandingOrder3 toOBStandingOrder3(OBStandingOrder6 obStandingOrder) {
        return obStandingOrder == null ? null : (new OBStandingOrder3())
                .accountId(obStandingOrder.getAccountId())
                .frequency(obStandingOrder.getFrequency())
                .nextPaymentDateTime(obStandingOrder.getNextPaymentDateTime())
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getNextPaymentAmount()))
                .standingOrderId(obStandingOrder.getStandingOrderId())
                .standingOrderStatusCode(obStandingOrder.getStandingOrderStatusCode())
                .creditorAccount(toOBCashAccount3(obStandingOrder.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification4(obStandingOrder.getCreditorAgent()))
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getFinalPaymentAmount()))
                .finalPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getFirstPaymentAmount()))
                .firstPaymentDateTime((obStandingOrder.getFirstPaymentDateTime()))
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getNextPaymentAmount()))
                .nextPaymentDateTime((obStandingOrder.getNextPaymentDateTime()))
                .reference(obStandingOrder.getReference());
    }

    public static OBStandingOrder4 toOBStandingOrder4(OBStandingOrder6 obStandingOrder) {
        return obStandingOrder == null ? null : (new OBStandingOrder4())
                .accountId(obStandingOrder.getAccountId())
                .frequency(obStandingOrder.getFrequency())
                .nextPaymentDateTime(obStandingOrder.getNextPaymentDateTime())
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getNextPaymentAmount()))
                .standingOrderId(obStandingOrder.getStandingOrderId())
                .standingOrderStatusCode(obStandingOrder.getStandingOrderStatusCode())
                .creditorAccount(toOBCashAccount3(obStandingOrder.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification4(obStandingOrder.getCreditorAgent()))
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getFinalPaymentAmount()))
                .finalPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getFirstPaymentAmount()))
                .firstPaymentDateTime((obStandingOrder.getFirstPaymentDateTime()))
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getNextPaymentAmount()))
                .nextPaymentDateTime((obStandingOrder.getNextPaymentDateTime()))
                .reference(obStandingOrder.getReference());
    }

    public static OBStandingOrder5 toOBStandingOrder5(OBStandingOrder6 obStandingOrder) {
        return obStandingOrder == null ? null : (new OBStandingOrder5())
                .accountId(obStandingOrder.getAccountId())
                .standingOrderId(obStandingOrder.getStandingOrderId())
                .frequency(obStandingOrder.getFrequency())
                .reference(obStandingOrder.getReference())
                .firstPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .nextPaymentDateTime(obStandingOrder.getNextPaymentDateTime())
                .finalPaymentDateTime(obStandingOrder.getFinalPaymentDateTime())
                .standingOrderStatusCode(obStandingOrder.getStandingOrderStatusCode())
                .firstPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getFirstPaymentAmount()))
                .nextPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount3(obStandingOrder.getNextPaymentAmount()))
                .finalPaymentAmount(toAccountOBActiveOrHistoricCurrencyAndAmount(obStandingOrder.getFinalPaymentAmount()))
                .supplementaryData(obStandingOrder.getSupplementaryData())
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification5(obStandingOrder.getCreditorAgent()))
                .creditorAccount(toOBCashAccount5(obStandingOrder.getCreditorAccount()));
    }

    public static OBStandingOrder6 toOBStandingOrder6(OBStandingOrder4 obStandingOrder) {
        return obStandingOrder == null ? null : (new OBStandingOrder6())
                .accountId(obStandingOrder.getAccountId())
                .frequency(obStandingOrder.getFrequency())
                .nextPaymentDateTime((obStandingOrder.getNextPaymentDateTime()))
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount3(obStandingOrder.getNextPaymentAmount()))
                .standingOrderId(obStandingOrder.getStandingOrderId())
                .standingOrderStatusCode(obStandingOrder.getStandingOrderStatusCode())
                .creditorAccount(toOBCashAccount51(obStandingOrder.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification51(obStandingOrder.getCreditorAgent()))
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount4(obStandingOrder.getFinalPaymentAmount()))
                .finalPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount2(obStandingOrder.getFirstPaymentAmount()))
                .firstPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount3(obStandingOrder.getNextPaymentAmount()))
                .nextPaymentDateTime(obStandingOrder.getNextPaymentDateTime())
                .reference(obStandingOrder.getReference());
    }

    public static OBStandingOrder6 toOBStandingOrder6(OBStandingOrder5 obStandingOrder) {
        return obStandingOrder == null ? null : (new OBStandingOrder6())
                .accountId(obStandingOrder.getAccountId())
                .frequency(obStandingOrder.getFrequency())
                .nextPaymentDateTime((obStandingOrder.getNextPaymentDateTime()))
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount3(obStandingOrder.getNextPaymentAmount()))
                .standingOrderId(obStandingOrder.getStandingOrderId())
                .standingOrderStatusCode(obStandingOrder.getStandingOrderStatusCode())
                .creditorAccount(toOBCashAccount51(obStandingOrder.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification51(obStandingOrder.getCreditorAgent()))
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount4(obStandingOrder.getFinalPaymentAmount()))
                .finalPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount2(obStandingOrder.getFirstPaymentAmount()))
                .firstPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .nextPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount3(obStandingOrder.getNextPaymentAmount()))
                .nextPaymentDateTime(obStandingOrder.getNextPaymentDateTime())
                .reference(obStandingOrder.getReference());
    }

}
