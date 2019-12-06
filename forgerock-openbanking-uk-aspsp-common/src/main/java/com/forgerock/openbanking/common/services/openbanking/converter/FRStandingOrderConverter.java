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
package com.forgerock.openbanking.common.services.openbanking.converter;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRStandingOrder1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRStandingOrder2;
import com.forgerock.openbanking.common.model.openbanking.v3_0.account.FRStandingOrder3;
import com.forgerock.openbanking.common.model.openbanking.v3_1.account.FRStandingOrder4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRStandingOrder5;
import uk.org.openbanking.datamodel.account.*;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.service.converter.OBBranchAndFinancialInstitutionIdentificationConverter;
import uk.org.openbanking.datamodel.service.converter.OBCashAccountConverter;
import uk.org.openbanking.datamodel.service.converter.OBStandingOrderConverter;

public class FRStandingOrderConverter {

    public static FRStandingOrder1 toStandingOrder1(FRStandingOrder2 standingOrder2) {
        FRStandingOrder1 standingOrder1 =  new FRStandingOrder1();
        standingOrder1.setAccountId(standingOrder2.getAccountId());
        standingOrder1.setId(standingOrder2.getId());
        standingOrder1.setCreated(standingOrder2.getCreated());
        standingOrder1.setUpdated(standingOrder2.getUpdated());
        standingOrder1.setStandingOrder(OBStandingOrderConverter.toStandingOrder1(standingOrder2.getStandingOrder()));
        return standingOrder1;
    }

    public static FRStandingOrder1 toStandingOrder1(FRStandingOrder3 standingOrder3) {
        FRStandingOrder1 standingOrder1 =  new FRStandingOrder1();
        standingOrder1.setAccountId(standingOrder3.getAccountId());
        standingOrder1.setId(standingOrder3.getId());
        standingOrder1.setCreated(standingOrder3.getCreated());
        standingOrder1.setUpdated(standingOrder3.getUpdated());
        standingOrder1.setStandingOrder(OBStandingOrderConverter.toStandingOrder1(standingOrder3.getStandingOrder()));
        return standingOrder1;
    }

    public static FRStandingOrder2 toStandingOrder2(FRStandingOrder1 standingOrder1) {
        FRStandingOrder2 standingOrder2 =  new FRStandingOrder2();
        standingOrder2.setAccountId(standingOrder1.getAccountId());
        standingOrder2.setId(standingOrder1.getId());
        standingOrder2.setCreated(standingOrder1.getCreated());
        standingOrder2.setUpdated(standingOrder1.getUpdated());
        standingOrder2.setStandingOrder(OBStandingOrderConverter.toStandingOrder2(standingOrder1.getStandingOrder()));
        return standingOrder2;
    }

    public static FRStandingOrder2 toStandingOrder2(FRStandingOrder3 standingOrder3) {
        FRStandingOrder2 standingOrder2 =  new FRStandingOrder2();
        standingOrder2.setAccountId(standingOrder3.getAccountId());
        standingOrder2.setId(standingOrder3.getId());
        standingOrder2.setCreated(standingOrder3.getCreated());
        standingOrder2.setUpdated(standingOrder3.getUpdated());
        standingOrder2.setStandingOrder(OBStandingOrderConverter.toStandingOrder2(standingOrder3.getStandingOrder()));
        return standingOrder2;
    }


    public static FRStandingOrder3 toStandingOrder3(FRStandingOrder2 frStandingOrder2) {
        FRStandingOrder3 standingOrder3 = new FRStandingOrder3();
        standingOrder3.setId(frStandingOrder2.getId());
        standingOrder3.setAccountId(frStandingOrder2.getAccountId());
        standingOrder3.setCreated(frStandingOrder2.getCreated());
        standingOrder3.setUpdated(frStandingOrder2.getUpdated());

        standingOrder3.setStandingOrder(OBStandingOrderConverter.toStandingOrder3(frStandingOrder2.getStandingOrder()));
        return standingOrder3;
    }

    public static FRStandingOrder4 toStandingOrder4(FRStandingOrder3 frStandingOrder3) {
        FRStandingOrder4 standingOrder4 = new FRStandingOrder4();
        standingOrder4.setId(frStandingOrder3.getId());
        standingOrder4.setAccountId(frStandingOrder3.getAccountId());
        standingOrder4.setCreated(frStandingOrder3.getCreated());
        standingOrder4.setUpdated(frStandingOrder3.getUpdated());

        standingOrder4.setStandingOrder(OBStandingOrderConverter.toStandingOrder4(frStandingOrder3.getStandingOrder()));
        return standingOrder4;
    }

    public static FRStandingOrder3 toStandingOrder3(FRStandingOrder4 standingOrder4) {
        FRStandingOrder3 standingOrder3 =  new FRStandingOrder3();
        standingOrder3.setAccountId(standingOrder4.getAccountId());
        standingOrder3.setId(standingOrder4.getId());
        standingOrder3.setCreated(standingOrder4.getCreated());
        standingOrder3.setUpdated(standingOrder4.getUpdated());
        standingOrder3.setStandingOrder(OBStandingOrderConverter.toStandingOrder3(standingOrder4.getStandingOrder()));
        return standingOrder3;
    }

    public static FRStandingOrder2 toStandingOrder2(FRStandingOrder4 standingOrder4) {
        FRStandingOrder2 standingOrder2 =  new FRStandingOrder2();
        standingOrder2.setAccountId(standingOrder4.getAccountId());
        standingOrder2.setId(standingOrder4.getId());
        standingOrder2.setCreated(standingOrder4.getCreated());
        standingOrder2.setUpdated(standingOrder4.getUpdated());
        standingOrder2.setStandingOrder(toStandingOrder2(standingOrder4.getStandingOrder()));
        return standingOrder2;
    }

    public static FRStandingOrder1 toStandingOrder1(FRStandingOrder5 standingOrder5) {
        return toStandingOrder1(toStandingOrder4(standingOrder5));
    }

    public static FRStandingOrder2 toStandingOrder2(FRStandingOrder5 standingOrder5) {
        return toStandingOrder2(toStandingOrder4(standingOrder5));
    }

    public static FRStandingOrder3 toStandingOrder3(FRStandingOrder5 standingOrder5) {
        return toStandingOrder3(toStandingOrder4(standingOrder5));
    }

    public static FRStandingOrder1 toStandingOrder1(FRStandingOrder4 standingOrder4) {
        FRStandingOrder1 standingOrder1 =  new FRStandingOrder1();
        standingOrder1.setAccountId(standingOrder4.getAccountId());
        standingOrder1.setId(standingOrder4.getId());
        standingOrder1.setCreated(standingOrder4.getCreated());
        standingOrder1.setUpdated(standingOrder4.getUpdated());
        standingOrder1.setStandingOrder(OBStandingOrderConverter.toStandingOrder1(standingOrder4.getStandingOrder()));
        return standingOrder1;
    }

    private static OBStandingOrder2 toStandingOrder2(OBStandingOrder4 obStandingOrder4) {
        if (obStandingOrder4==null) {
            return null;
        }
        OBStandingOrder2 standingOrder2 = (new OBStandingOrder2()).accountId(obStandingOrder4.getAccountId()).frequency(obStandingOrder4.getFrequency()).nextPaymentDateTime(obStandingOrder4.getNextPaymentDateTime()).nextPaymentAmount(obStandingOrder4.getNextPaymentAmount());
        if (obStandingOrder4.getStandingOrderId() != null) {
            standingOrder2.standingOrderId(obStandingOrder4.getStandingOrderId());
        }

        if (obStandingOrder4.getReference() != null) {
            standingOrder2.reference(obStandingOrder4.getReference());
        }

        if (obStandingOrder4.getFirstPaymentDateTime() != null) {
            standingOrder2.firstPaymentDateTime(obStandingOrder4.getFirstPaymentDateTime());
        }

        if (obStandingOrder4.getFirstPaymentAmount() != null) {
            standingOrder2.firstPaymentAmount(obStandingOrder4.getFirstPaymentAmount());
        }

        if (obStandingOrder4.getFinalPaymentDateTime() != null) {
            standingOrder2.finalPaymentDateTime(obStandingOrder4.getFinalPaymentDateTime());
        }

        if (obStandingOrder4.getFinalPaymentAmount() != null) {
            standingOrder2.finalPaymentAmount(obStandingOrder4.getFinalPaymentAmount());
        }

        if (obStandingOrder4.getCreditorAgent() != null) {
            standingOrder2.setCreditorAgent(OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification2(obStandingOrder4.getCreditorAgent()));
        }

        if (obStandingOrder4.getCreditorAccount() != null) {
            standingOrder2.creditorAccount(OBCashAccountConverter.toOBCashAccount1(obStandingOrder4.getCreditorAccount()));
        }
        if (obStandingOrder4.getStandingOrderStatusCode() != null) {
            standingOrder2.standingOrderStatusCode(obStandingOrder4.getStandingOrderStatusCode());
        }

        return standingOrder2;
    }


    public static FRStandingOrder4 toStandingOrder4(FRStandingOrder5 standingOrder5) {
        FRStandingOrder4 standingOrder4 =  new FRStandingOrder4();
        standingOrder4.setAccountId(standingOrder5.getAccountId());
        standingOrder4.setId(standingOrder5.getId());
        standingOrder4.setCreated(standingOrder5.getCreated());
        standingOrder4.setUpdated(standingOrder5.getUpdated());
        standingOrder4.setStandingOrder(toOBStandingOrder4(standingOrder5.getStandingOrder()));
        return standingOrder4;
    }

    public static OBStandingOrder4 toOBStandingOrder4(OBStandingOrder5 obStandingOrder) {
        return (new OBStandingOrder4())
                .accountId(obStandingOrder.getAccountId())
                .frequency(obStandingOrder.getFrequency())
                .nextPaymentDateTime(obStandingOrder.getNextPaymentDateTime())
                .nextPaymentAmount(toAccountPayment(obStandingOrder.getNextPaymentAmount()))
                .standingOrderId(obStandingOrder.getStandingOrderId())
                .standingOrderStatusCode(obStandingOrder.getStandingOrderStatusCode())
                .creditorAccount(FRAccountConverter.toOBCashAccount3(obStandingOrder.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification4(obStandingOrder.getCreditorAgent()))
                .finalPaymentAmount(toAccountPayment(obStandingOrder.getFinalPaymentAmount()))
                .finalPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .firstPaymentAmount(toAccountPayment(obStandingOrder.getFirstPaymentAmount()))
                .firstPaymentDateTime((obStandingOrder.getFirstPaymentDateTime()))
                .nextPaymentAmount(toAccountPayment(obStandingOrder.getNextPaymentAmount()))
                .nextPaymentDateTime((obStandingOrder.getNextPaymentDateTime()))
                .reference(obStandingOrder.getReference());
    }

    private static OBActiveOrHistoricCurrencyAndAmount toAccountPayment(uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount instructedAmount) {
        if (instructedAmount==null) return null;
        return new OBActiveOrHistoricCurrencyAndAmount().currency(instructedAmount.getCurrency()).amount(instructedAmount.getAmount());
    }

    private static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountPayment(OBActiveOrHistoricCurrencyAndAmount instructedAmount) {
        if (instructedAmount==null) return null;
        return new uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount().currency(instructedAmount.getCurrency()).amount(instructedAmount.getAmount());
    }

    private static OBBranchAndFinancialInstitutionIdentification4 toOBBranchAndFinancialInstitutionIdentification4(OBBranchAndFinancialInstitutionIdentification5 creditorAgent) {
        if (creditorAgent==null) return null;
        return new OBBranchAndFinancialInstitutionIdentification4().identification(creditorAgent.getIdentification()).schemeName(creditorAgent.getIdentification());
    }

    private static OBBranchAndFinancialInstitutionIdentification5 toOBBranchAndFinancialInstitutionIdentification5(OBBranchAndFinancialInstitutionIdentification4 creditorAgent) {
        if (creditorAgent==null) return null;
        return new OBBranchAndFinancialInstitutionIdentification5().identification(creditorAgent.getIdentification()).schemeName(creditorAgent.getIdentification());
    }

    public static FRStandingOrder5 toStandingOrder5(FRStandingOrder4 frStandingOrder4) {
        FRStandingOrder5 standingOrder5 =  new FRStandingOrder5();
        standingOrder5.setAccountId(frStandingOrder4.getAccountId());
        standingOrder5.setId(frStandingOrder4.getId());
        standingOrder5.setCreated(frStandingOrder4.getCreated());
        standingOrder5.setUpdated(frStandingOrder4.getUpdated());
        standingOrder5.setStandingOrder(toOBStandingOrder5(frStandingOrder4.getStandingOrder()));
        return standingOrder5;
    }

    private static OBStandingOrder5 toOBStandingOrder5(OBStandingOrder4 obStandingOrder) {
        return (new OBStandingOrder5())
                .accountId(obStandingOrder.getAccountId())
                .frequency(obStandingOrder.getFrequency())
                .nextPaymentDateTime((obStandingOrder.getNextPaymentDateTime()))
                .nextPaymentAmount(toAccountPayment(obStandingOrder.getNextPaymentAmount()))
                .standingOrderId(obStandingOrder.getStandingOrderId())
                .standingOrderStatusCode(obStandingOrder.getStandingOrderStatusCode())
                .creditorAccount(FRAccountConverter.toOBCashAccount5(obStandingOrder.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification5(obStandingOrder.getCreditorAgent()))
                .finalPaymentAmount(toAccountPayment(obStandingOrder.getFinalPaymentAmount()))
                .finalPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .firstPaymentAmount(toAccountPayment(obStandingOrder.getFirstPaymentAmount()))
                .firstPaymentDateTime(obStandingOrder.getFirstPaymentDateTime())
                .nextPaymentAmount(toAccountPayment(obStandingOrder.getNextPaymentAmount()))
                .nextPaymentDateTime(obStandingOrder.getNextPaymentDateTime())
                .reference(obStandingOrder.getReference());
    }
}
