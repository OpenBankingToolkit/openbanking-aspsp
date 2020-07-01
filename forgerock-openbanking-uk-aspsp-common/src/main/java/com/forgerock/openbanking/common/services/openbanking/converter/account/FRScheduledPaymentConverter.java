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

import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRScheduledPayment1;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRScheduledPayment2;
import org.joda.time.DateTime;
import uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount1;
import uk.org.openbanking.datamodel.account.OBScheduledPayment1;
import uk.org.openbanking.datamodel.account.OBScheduledPayment2;
import uk.org.openbanking.datamodel.account.OBScheduledPayment3;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification4;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification5;

public class FRScheduledPaymentConverter {

    public static FRScheduledPayment2 toScheduledPayment2(FRScheduledPayment1 frScheduledPayment1) {
        FRScheduledPayment2 frScheduledPayment2 = FRScheduledPayment2.builder()
                .accountId(frScheduledPayment1.getAccountId())
                .created(frScheduledPayment1.getCreated())
                .updated(frScheduledPayment1.getUpdated())
                .accountId(frScheduledPayment1.getAccountId())
                .pispId(frScheduledPayment1.getPispId())
                .rejectionReason(frScheduledPayment1.getRejectionReason())
                .status(frScheduledPayment1.getStatus())
                .id(frScheduledPayment1.getId())
                .build();

        frScheduledPayment2.setScheduledPayment(toOBScheduledPayment2(frScheduledPayment1.getScheduledPayment()));
        return frScheduledPayment2;
    }

    public static OBScheduledPayment1 toOBScheduledPayment1(OBScheduledPayment2 obScheduledPayment2) {
        OBScheduledPayment1 obScheduledPayment1 = new OBScheduledPayment1()
                .scheduledPaymentId(obScheduledPayment2.getScheduledPaymentId())
                .reference(obScheduledPayment2.getReference())
                .accountId(obScheduledPayment2.getAccountId())
                .scheduledType(obScheduledPayment2.getScheduledType());

        if (obScheduledPayment2.getCreditorAccount() != null) {
            obScheduledPayment1.setCreditorAccount(FRAccountConverter.toOBCashAccount3(obScheduledPayment2.getCreditorAccount()));
        }
        if (obScheduledPayment2.getInstructedAmount() != null) {
            obScheduledPayment1.setInstructedAmount(toAccountPayment(obScheduledPayment2.getInstructedAmount()));
        }
        if (obScheduledPayment2.getScheduledPaymentDateTime() != null) {
            obScheduledPayment1.setScheduledPaymentDateTime(DateTime.parse(obScheduledPayment2.getScheduledPaymentDateTime().toString()));
        }
        if (obScheduledPayment2.getCreditorAgent() != null) {
            obScheduledPayment1.setCreditorAgent(toOBBranchAndFinancialInstitutionIdentification4(obScheduledPayment2.getCreditorAgent()));
        }
        return obScheduledPayment1;
    }

    public static OBScheduledPayment1 toOBScheduledPayment1(OBScheduledPayment3 obScheduledPayment3) {
        OBScheduledPayment1 obScheduledPayment1 = new OBScheduledPayment1()
                .scheduledPaymentId(obScheduledPayment3.getScheduledPaymentId())
                .reference(obScheduledPayment3.getReference())
                .accountId(obScheduledPayment3.getAccountId())
                .scheduledType(obScheduledPayment3.getScheduledType());

        if (obScheduledPayment3.getCreditorAccount() != null) {
            obScheduledPayment1.setCreditorAccount(FRAccountConverter.toOBCashAccount3(obScheduledPayment3.getCreditorAccount()));
        }
        if (obScheduledPayment3.getInstructedAmount() != null) {
            obScheduledPayment1.setInstructedAmount(toAccountPayment(obScheduledPayment3.getInstructedAmount()));
        }
        if (obScheduledPayment3.getScheduledPaymentDateTime() != null) {
            obScheduledPayment1.setScheduledPaymentDateTime(DateTime.parse(obScheduledPayment3.getScheduledPaymentDateTime().toString()));
        }
        if (obScheduledPayment3.getCreditorAgent() != null) {
            obScheduledPayment1.setCreditorAgent(toOBBranchAndFinancialInstitutionIdentification4(obScheduledPayment3.getCreditorAgent()));
        }
        return obScheduledPayment1;
    }

    public static OBScheduledPayment2 toOBScheduledPayment2(OBScheduledPayment1 scheduledPayment) {
        OBScheduledPayment2 obScheduledPayment2 = new OBScheduledPayment2()
                .scheduledPaymentId(scheduledPayment.getScheduledPaymentId())
                .reference(scheduledPayment.getReference())
                .accountId(scheduledPayment.getAccountId())
                .scheduledType(scheduledPayment.getScheduledType());

        if (scheduledPayment.getCreditorAccount() != null) {
            obScheduledPayment2.setCreditorAccount(FRAccountConverter.toOBCashAccount5(scheduledPayment.getCreditorAccount()));
        }
        if (scheduledPayment.getInstructedAmount() != null) {
            obScheduledPayment2.setInstructedAmount(toAccountPayment(scheduledPayment.getInstructedAmount()));
        }
        if (scheduledPayment.getScheduledPaymentDateTime() != null) {
            obScheduledPayment2.setScheduledPaymentDateTime(scheduledPayment.getScheduledPaymentDateTime());
        }
        if (scheduledPayment.getCreditorAgent() != null) {
            obScheduledPayment2.setCreditorAgent(toOBBranchAndFinancialInstitutionIdentification5(scheduledPayment.getCreditorAgent()));
        }
        return obScheduledPayment2;
    }

    public static OBScheduledPayment2 toOBScheduledPayment2(OBScheduledPayment3 scheduledPayment) {
        OBScheduledPayment2 obScheduledPayment2 = new OBScheduledPayment2()
                .scheduledPaymentId(scheduledPayment.getScheduledPaymentId())
                .reference(scheduledPayment.getReference())
                .accountId(scheduledPayment.getAccountId())
                .scheduledType(scheduledPayment.getScheduledType());

        if (scheduledPayment.getCreditorAccount() != null) {
            obScheduledPayment2.setCreditorAccount(FRAccountConverter.toOBCashAccount5(scheduledPayment.getCreditorAccount()));
        }
        if (scheduledPayment.getInstructedAmount() != null) {
            obScheduledPayment2.setInstructedAmount(toOBActiveOrHistoricCurrencyAndAmount(scheduledPayment.getInstructedAmount()));
        }
        if (scheduledPayment.getScheduledPaymentDateTime() != null) {
            obScheduledPayment2.setScheduledPaymentDateTime(scheduledPayment.getScheduledPaymentDateTime());
        }
        if (scheduledPayment.getCreditorAgent() != null) {
            obScheduledPayment2.setCreditorAgent(toOBBranchAndFinancialInstitutionIdentification5(scheduledPayment.getCreditorAgent()));
        }
        return obScheduledPayment2;
    }


    public static OBActiveOrHistoricCurrencyAndAmount toAccountPayment(uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount instructedAmount) {
        if (instructedAmount == null) return null;
        return new OBActiveOrHistoricCurrencyAndAmount().currency(instructedAmount.getCurrency()).amount(instructedAmount.getAmount());
    }

    public static OBActiveOrHistoricCurrencyAndAmount toAccountPayment(OBActiveOrHistoricCurrencyAndAmount1 instructedAmount) {
        if (instructedAmount == null) return null;
        return new OBActiveOrHistoricCurrencyAndAmount().currency(instructedAmount.getCurrency()).amount(instructedAmount.getAmount());
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountPayment(OBActiveOrHistoricCurrencyAndAmount instructedAmount) {
        if (instructedAmount == null) return null;
        return new uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount().currency(instructedAmount.getCurrency()).amount(instructedAmount.getAmount());
    }

    public static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount1 instructedAmount) {
        if (instructedAmount == null) return null;
        return new uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount()
                .currency(instructedAmount.getCurrency())
                .amount(instructedAmount.getAmount());
    }

}
