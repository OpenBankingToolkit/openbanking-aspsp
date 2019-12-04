/**
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

import com.forgerock.openbanking.common.services.openbanking.converter.FRModelMapper;
import com.forgerock.openbanking.common.services.openbanking.converter.OBActiveOrHistoricCurrencyAndAmountConverter;
import com.forgerock.openbanking.common.services.openbanking.converter.OBCashAccountConverter;
import uk.org.openbanking.datamodel.payment.*;
import uk.org.openbanking.datamodel.service.converter.payment.OBDomesticStandingOrderConverter;
import uk.org.openbanking.datamodel.service.converter.payment.OBInternationalStandingOrderConverter;

public class FRStandingOrderPaymentConverter {
    public static OBWriteInternationalStandingOrderConsent3 toOBWriteInternationalStandingOrderConsent3(OBWriteInternationalStandingOrderConsent2 consent2) {
        return new OBWriteInternationalStandingOrderConsent3()
                .risk(consent2.getRisk())
                .data(toOBWriteDataInternationalStandingOrderConsent3(consent2.getData()));
    }

    private static OBWriteDataInternationalStandingOrderConsent3 toOBWriteDataInternationalStandingOrderConsent3(OBWriteDataInternationalStandingOrderConsent2 data) {
        return new OBWriteDataInternationalStandingOrderConsent3()
                .initiation(toOBInternationalStandingOrder3(data.getInitiation()))
                .permission(data.getPermission())
                .authorisation(data.getAuthorisation())
                ;
    }

    private static OBInternationalStandingOrder3 toOBInternationalStandingOrder3(OBInternationalStandingOrder2 initiation) {
        return new OBInternationalStandingOrder3()
                .creditorAccount(OBCashAccountConverter.toOBCashAccountCreditor3(initiation.getCreditorAccount()))
                .debtorAccount(OBCashAccountConverter.toOBCashAccountDebtor4(initiation.getDebtorAccount()))
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .creditor(initiation.getCreditor())
                .purpose(initiation.getPurpose())
                .chargeBearer(initiation.getChargeBearer())
                .supplementaryData(initiation.getSupplementaryData())
                .frequency(initiation.getFrequency())
                .numberOfPayments(initiation.getNumberOfPayments())
                .reference(initiation.getReference())
                .instructedAmount(toOBDomestic2InstructedAmount(initiation.getInstructedAmount()))
                ;
    }

    private static OBDomestic2InstructedAmount toOBDomestic2InstructedAmount(OBActiveOrHistoricCurrencyAndAmount instructedAmount) {
        return FRModelMapper.map(instructedAmount, OBDomestic2InstructedAmount.class);
    }

    public static OBWriteDomesticStandingOrder3 toOBWriteDomesticStandingOrder3(OBWriteDomesticStandingOrder2 obWriteDomesticStandingOrder2Param) {
        return new OBWriteDomesticStandingOrder3()
                .risk(obWriteDomesticStandingOrder2Param.getRisk())
                .data(toOBWriteDataDomesticStandingOrder3(obWriteDomesticStandingOrder2Param.getData()));
    }

    public static OBWriteInternationalStandingOrder3 toOBInternationalStandingOrder3(OBWriteInternationalStandingOrder2 payment) {
        return new OBWriteInternationalStandingOrder3()
                .risk(payment.getRisk())
                .data(toOBWriteDataInternationalStandingOrder3(payment.getData()));
    }

    private static OBWriteDataInternationalStandingOrder3 toOBWriteDataInternationalStandingOrder3(OBWriteDataInternationalStandingOrder2 data) {
        return new OBWriteDataInternationalStandingOrder3()
                .consentId(data.getConsentId())
                .initiation(toOBInternationalStandingOrder3(data.getInitiation()));
    }

    public static OBWriteDomesticStandingOrderConsent3 toWriteDomesticStandingOrderConsent3Param(OBWriteDomesticStandingOrderConsent2 obWriteDomesticStandingOrderConsent2Param) {
        return new OBWriteDomesticStandingOrderConsent3()
                .risk(obWriteDomesticStandingOrderConsent2Param.getRisk())
                .data(toOBWriteDataDomesticStandingOrderConsent3(obWriteDomesticStandingOrderConsent2Param.getData()));
    }

    private static OBWriteDataDomesticStandingOrder3 toOBWriteDataDomesticStandingOrder3(OBWriteDataDomesticStandingOrder2 data) {
        return new OBWriteDataDomesticStandingOrder3()
                .consentId(data.getConsentId())
                .initiation(toOBDomesticStandingOrder3(data.getInitiation()))
               ;
    }

    public static OBWriteDataDomesticStandingOrderConsent3 toOBWriteDataDomesticStandingOrderConsent3(OBWriteDataDomesticStandingOrderConsent2 obWriteDataDomesticStandingOrderConsent2) {
        return new OBWriteDataDomesticStandingOrderConsent3()
                .authorisation(obWriteDataDomesticStandingOrderConsent2.getAuthorisation())
                .initiation(toOBDomesticStandingOrder3(obWriteDataDomesticStandingOrderConsent2.getInitiation()))
                .permission(obWriteDataDomesticStandingOrderConsent2.getPermission());
    }

    public static OBDomesticStandingOrder3 toOBDomesticStandingOrder3(OBDomesticStandingOrder2 obDomesticStandingOrder2) {
        return new OBDomesticStandingOrder3()
                .creditorAccount(OBCashAccountConverter.toOBCashAccountCreditor3(obDomesticStandingOrder2.getCreditorAccount()))
                .debtorAccount(OBCashAccountConverter.toOBCashAccountDebtor4(obDomesticStandingOrder2.getDebtorAccount()))
                .finalPaymentAmount(toOBDomesticStandingOrder3FinalPaymentAmount(obDomesticStandingOrder2.getFinalPaymentAmount()))
                .finalPaymentDateTime(obDomesticStandingOrder2.getFinalPaymentDateTime())
                .firstPaymentDateTime(obDomesticStandingOrder2.getFirstPaymentDateTime())
                .firstPaymentAmount(toOBDomesticStandingOrder3FirstPaymentAmount(obDomesticStandingOrder2.getFirstPaymentAmount()))
                .recurringPaymentAmount(toOBDomesticStandingOrder3RecurringPaymentAmount(obDomesticStandingOrder2.getRecurringPaymentAmount()))
                .recurringPaymentDateTime(obDomesticStandingOrder2.getRecurringPaymentDateTime())
                .supplementaryData(obDomesticStandingOrder2.getSupplementaryData())
                .frequency(obDomesticStandingOrder2.getFrequency())
                .numberOfPayments(obDomesticStandingOrder2.getNumberOfPayments())
                .reference(obDomesticStandingOrder2.getReference());
    }

    private static OBDomesticStandingOrder3FinalPaymentAmount toOBDomesticStandingOrder3FinalPaymentAmount(OBActiveOrHistoricCurrencyAndAmount amount) {
        return FRModelMapper.map(amount, OBDomesticStandingOrder3FinalPaymentAmount.class);
    }

    private static OBDomesticStandingOrder3FirstPaymentAmount toOBDomesticStandingOrder3FirstPaymentAmount(OBActiveOrHistoricCurrencyAndAmount amount) {
        return FRModelMapper.map(amount, OBDomesticStandingOrder3FirstPaymentAmount.class);
    }

    private static OBDomesticStandingOrder3RecurringPaymentAmount toOBDomesticStandingOrder3RecurringPaymentAmount(OBActiveOrHistoricCurrencyAndAmount amount) {
        return FRModelMapper.map(amount, OBDomesticStandingOrder3RecurringPaymentAmount.class);
    }

    public static OBDomesticStandingOrder1 toOBDomesticStandingOrder1(OBDomesticStandingOrder3 initiation) {
        return OBDomesticStandingOrderConverter.toOBDomesticStandingOrder1(toOBDomesticStandingOrder2(initiation));
    }

    public static OBInternationalStandingOrder1 toOBInternationalStandingOrder1(OBInternationalStandingOrder3 initiation) {
        return OBInternationalStandingOrderConverter.toOBInternationalStandingOrder1(toOBInternationalStandingOrder2(initiation));
    }

    public static OBDomesticStandingOrder2 toOBDomesticStandingOrder2(OBDomesticStandingOrder3 obDomesticStandingOrder3) {
        return new OBDomesticStandingOrder2()
                .creditorAccount(OBCashAccountConverter.toOBCashAccount3(obDomesticStandingOrder3.getCreditorAccount()))
                .debtorAccount(OBCashAccountConverter.toOBCashAccount3(obDomesticStandingOrder3.getDebtorAccount()))
                .finalPaymentAmount(OBActiveOrHistoricCurrencyAndAmountConverter.toOBActiveOrHistoricCurrencyAndAmount(obDomesticStandingOrder3.getFinalPaymentAmount()))
                .finalPaymentDateTime(obDomesticStandingOrder3.getFinalPaymentDateTime())
                .firstPaymentDateTime(obDomesticStandingOrder3.getFirstPaymentDateTime())
                .firstPaymentAmount(OBActiveOrHistoricCurrencyAndAmountConverter.toOBActiveOrHistoricCurrencyAndAmount(obDomesticStandingOrder3.getFirstPaymentAmount()))
                .recurringPaymentAmount(OBActiveOrHistoricCurrencyAndAmountConverter.toOBActiveOrHistoricCurrencyAndAmount(obDomesticStandingOrder3.getRecurringPaymentAmount()))
                .recurringPaymentDateTime(obDomesticStandingOrder3.getRecurringPaymentDateTime())
                .supplementaryData(obDomesticStandingOrder3.getSupplementaryData())
                .frequency(obDomesticStandingOrder3.getFrequency())
                .numberOfPayments(obDomesticStandingOrder3.getNumberOfPayments())
                .reference(obDomesticStandingOrder3.getReference());
    }

    public static OBInternationalStandingOrder2 toOBInternationalStandingOrder2(OBInternationalStandingOrder3 standingOrder3) {
        return new OBInternationalStandingOrder2()
                .creditorAccount(OBCashAccountConverter.toOBCashAccount3(standingOrder3.getCreditorAccount()))
                .debtorAccount(OBCashAccountConverter.toOBCashAccount3(standingOrder3.getDebtorAccount()))
                .finalPaymentDateTime(standingOrder3.getFinalPaymentDateTime())
                .firstPaymentDateTime(standingOrder3.getFirstPaymentDateTime())
                .supplementaryData(standingOrder3.getSupplementaryData())
                .frequency(standingOrder3.getFrequency())
                .numberOfPayments(standingOrder3.getNumberOfPayments())
                .reference(standingOrder3.getReference())
                .currencyOfTransfer(standingOrder3.getCurrencyOfTransfer())
                .creditor(standingOrder3.getCreditor())
                .instructedAmount(OBActiveOrHistoricCurrencyAndAmountConverter.toPaymentOBActiveOrHistoricCurrencyAndAmount(standingOrder3.getInstructedAmount()))
                .purpose(standingOrder3.getPurpose())
                .chargeBearer(standingOrder3.getChargeBearer());
    }

}
