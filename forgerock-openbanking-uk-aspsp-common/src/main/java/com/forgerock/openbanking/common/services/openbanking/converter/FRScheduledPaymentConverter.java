/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking.converter;

import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRScheduledPayment1;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRScheduledPayment2;
import org.joda.time.DateTime;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification4;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification5;
import uk.org.openbanking.datamodel.account.OBScheduledPayment1;
import uk.org.openbanking.datamodel.account.OBScheduledPayment2;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

public class FRScheduledPaymentConverter {

    public static OBScheduledPayment1 toOBScheduledPayment1(OBScheduledPayment2 obScheduledPayment2) {
        OBScheduledPayment1 obScheduledPayment1 = new OBScheduledPayment1()
                .scheduledPaymentId(obScheduledPayment2.getScheduledPaymentId())
                .reference(obScheduledPayment2.getReference())
                .accountId(obScheduledPayment2.getAccountId())
                .scheduledType(obScheduledPayment2.getScheduledType());

        if (obScheduledPayment2.getCreditorAccount()!=null) {
            obScheduledPayment1.setCreditorAccount(FRAccountConverter.toOBCashAccount3(obScheduledPayment2.getCreditorAccount()));
        }
        if (obScheduledPayment2.getInstructedAmount()!=null) {
            obScheduledPayment1.setInstructedAmount(toAccountPayment(obScheduledPayment2.getInstructedAmount()));
        }
        if (obScheduledPayment2.getScheduledPaymentDateTime()!=null) {
            obScheduledPayment1.setScheduledPaymentDateTime(DateTime.parse(obScheduledPayment2.getScheduledPaymentDateTime().toString()));
        }
        if (obScheduledPayment2.getScheduledPaymentDateTime()!=null) {
            obScheduledPayment1.setCreditorAgent(toOBBranchAndFinancialInstitutionIdentification4(obScheduledPayment2.getCreditorAgent()));
        }
        return obScheduledPayment1;
    }

    private static OBBranchAndFinancialInstitutionIdentification4 toOBBranchAndFinancialInstitutionIdentification4(OBBranchAndFinancialInstitutionIdentification5 creditorAgent) {
        if (creditorAgent==null) return null;
        return new OBBranchAndFinancialInstitutionIdentification4().identification(creditorAgent.getIdentification()).schemeName(creditorAgent.getIdentification());
    }

    private static OBActiveOrHistoricCurrencyAndAmount toAccountPayment(uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount instructedAmount) {
        if (instructedAmount==null) return null;
        return new OBActiveOrHistoricCurrencyAndAmount().currency(instructedAmount.getCurrency()).amount(instructedAmount.getAmount());
    }

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

    private static OBScheduledPayment2 toOBScheduledPayment2(OBScheduledPayment1 scheduledPayment) {
        OBScheduledPayment2 obScheduledPayment2 = new OBScheduledPayment2()
                .scheduledPaymentId(scheduledPayment.getScheduledPaymentId())
                .reference(scheduledPayment.getReference())
                .accountId(scheduledPayment.getAccountId())
                .scheduledType(scheduledPayment.getScheduledType());

        if (scheduledPayment.getCreditorAccount()!=null) {
            obScheduledPayment2.setCreditorAccount(FRAccountConverter.toOBCashAccount5(scheduledPayment.getCreditorAccount()));
        }
        if (scheduledPayment.getInstructedAmount()!=null) {
            obScheduledPayment2.setInstructedAmount(toAccountPayment(scheduledPayment.getInstructedAmount()));
        }
        if (scheduledPayment.getScheduledPaymentDateTime()!=null) {
            obScheduledPayment2.setScheduledPaymentDateTime(scheduledPayment.getScheduledPaymentDateTime());
        }
        if (scheduledPayment.getScheduledPaymentDateTime()!=null) {
            obScheduledPayment2.setCreditorAgent(toOBBranchAndFinancialInstitutionIdentification5(scheduledPayment.getCreditorAgent()));
        }
        return obScheduledPayment2;
    }

    private static OBBranchAndFinancialInstitutionIdentification5 toOBBranchAndFinancialInstitutionIdentification5(OBBranchAndFinancialInstitutionIdentification4 creditorAgent) {
        if (creditorAgent==null) return new OBBranchAndFinancialInstitutionIdentification5();
        return new OBBranchAndFinancialInstitutionIdentification5()
                .identification(creditorAgent.getIdentification())
                .schemeName(creditorAgent.getIdentification());
    }

    private static uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount toAccountPayment(OBActiveOrHistoricCurrencyAndAmount instructedAmount) {
        if (instructedAmount==null) return null;
        return new uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount().currency(instructedAmount.getCurrency()).amount(instructedAmount.getAmount());
    }
}
