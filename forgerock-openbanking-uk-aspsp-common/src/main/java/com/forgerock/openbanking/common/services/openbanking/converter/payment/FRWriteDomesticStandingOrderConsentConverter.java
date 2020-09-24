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
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrderConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrderConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrderDataInitiation;
import uk.org.openbanking.datamodel.payment.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataAuthorisationConverter.toFRDataAuthorisation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataSCASupportDataConverter.toFRDataSCASupportData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPermissionConverter.toFRPermission;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRReadRefundAccountConverter.toFRReadRefundAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRSupplementaryDataConverter.toFRSupplementaryData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRSupplementaryDataConverter.toOBSupplementaryData1;

public class FRWriteDomesticStandingOrderConsentConverter {

    // OB to FR
    public static FRWriteDomesticStandingOrderConsent toFRWriteDomesticStandingOrderConsent(OBWriteDomesticStandingOrderConsent1 obWriteDomesticStandingOrderConsent1) {
        return obWriteDomesticStandingOrderConsent1 == null ? null : FRWriteDomesticStandingOrderConsent.builder()
                .data(toFRWriteDomesticStandingOrderConsent(obWriteDomesticStandingOrderConsent1.getData()))
                .risk(toFRRisk(obWriteDomesticStandingOrderConsent1.getRisk()))
                .build();
    }

    public static FRWriteDomesticStandingOrderConsent toFRWriteDomesticStandingOrderConsent(OBWriteDomesticStandingOrderConsent2 obWriteDomesticStandingOrderConsent2) {
        return obWriteDomesticStandingOrderConsent2 == null ? null : FRWriteDomesticStandingOrderConsent.builder()
                .data(toFRWriteDomesticStandingOrderConsent(obWriteDomesticStandingOrderConsent2.getData()))
                .risk(toFRRisk(obWriteDomesticStandingOrderConsent2.getRisk()))
                .build();
    }

    public static FRWriteDomesticStandingOrderConsent toFRWriteDomesticStandingOrderConsent(OBWriteDomesticStandingOrderConsent3 obWriteDomesticStandingOrderConsent3) {
        return obWriteDomesticStandingOrderConsent3 == null ? null : FRWriteDomesticStandingOrderConsent.builder()
                .data(toFRWriteDomesticStandingOrderConsent(obWriteDomesticStandingOrderConsent3.getData()))
                .risk(toFRRisk(obWriteDomesticStandingOrderConsent3.getRisk()))
                .build();
    }

    public static FRWriteDomesticStandingOrderConsent toFRWriteDomesticStandingOrderConsent(OBWriteDomesticStandingOrderConsent4 obWriteDomesticStandingOrderConsent4) {
        return obWriteDomesticStandingOrderConsent4 == null ? null : FRWriteDomesticStandingOrderConsent.builder()
                .data(toFRWriteDomesticStandingOrderConsent(obWriteDomesticStandingOrderConsent4.getData()))
                .risk(toFRRisk(obWriteDomesticStandingOrderConsent4.getRisk()))
                .build();
    }

    public static FRWriteDomesticStandingOrderConsent toFRWriteDomesticStandingOrderConsent(OBWriteDomesticStandingOrderConsent5 obWriteDomesticStandingOrderConsent5) {
        return obWriteDomesticStandingOrderConsent5 == null ? null : FRWriteDomesticStandingOrderConsent.builder()
                .data(toFRWriteDomesticStandingOrderConsent(obWriteDomesticStandingOrderConsent5.getData()))
                .risk(toFRRisk(obWriteDomesticStandingOrderConsent5.getRisk()))
                .build();
    }

    public static FRWriteDomesticStandingOrderConsentData toFRWriteDomesticStandingOrderConsent(OBWriteDataDomesticStandingOrderConsent1 data) {
        return data == null ? null : FRWriteDomesticStandingOrderConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteDomesticStandingOrderDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteDomesticStandingOrderConsentData toFRWriteDomesticStandingOrderConsent(OBWriteDataDomesticStandingOrderConsent2 data) {
        return data == null ? null : FRWriteDomesticStandingOrderConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteDomesticStandingOrderDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteDomesticStandingOrderConsentData toFRWriteDomesticStandingOrderConsent(OBWriteDataDomesticStandingOrderConsent3 data) {
        return data == null ? null : FRWriteDomesticStandingOrderConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteDomesticStandingOrderDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteDomesticStandingOrderConsentData toFRWriteDomesticStandingOrderConsent(OBWriteDomesticStandingOrderConsent4Data data) {
        return data == null ? null : FRWriteDomesticStandingOrderConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteDomesticStandingOrderDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .scASupportData(toFRDataSCASupportData(data.getScASupportData()))
                .build();
    }

    public static FRWriteDomesticStandingOrderConsentData toFRWriteDomesticStandingOrderConsent(OBWriteDomesticStandingOrderConsent5Data data) {
        return data == null ? null : FRWriteDomesticStandingOrderConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .readRefundAccount(toFRReadRefundAccount(data.getReadRefundAccount()))
                .initiation(toFRWriteDomesticStandingOrderDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .scASupportData(toFRDataSCASupportData(data.getScASupportData()))
                .build();
    }

    public static FRWriteDomesticStandingOrderDataInitiation toFRWriteDomesticStandingOrderDataInitiation(OBDomesticStandingOrder1 initiation) {
        return initiation == null ? null : FRWriteDomesticStandingOrderDataInitiation.builder()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toFRAmount(initiation.getFirstPaymentAmount()))
                .recurringPaymentAmount(toFRAmount(initiation.getRecurringPaymentAmount()))
                .finalPaymentAmount(toFRAmount(initiation.getFinalPaymentAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .build();
    }

    public static FRWriteDomesticStandingOrderDataInitiation toFRWriteDomesticStandingOrderDataInitiation(OBDomesticStandingOrder2 initiation) {
        return initiation == null ? null : FRWriteDomesticStandingOrderDataInitiation.builder()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toFRAmount(initiation.getFirstPaymentAmount()))
                .recurringPaymentAmount(toFRAmount(initiation.getRecurringPaymentAmount()))
                .finalPaymentAmount(toFRAmount(initiation.getFinalPaymentAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    public static FRWriteDomesticStandingOrderDataInitiation toFRWriteDomesticStandingOrderDataInitiation(OBDomesticStandingOrder3 initiation) {
        return initiation == null ? null : FRWriteDomesticStandingOrderDataInitiation.builder()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toFRAmount(initiation.getFirstPaymentAmount()))
                .recurringPaymentAmount(toFRAmount(initiation.getRecurringPaymentAmount()))
                .finalPaymentAmount(toFRAmount(initiation.getFinalPaymentAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    public static FRWriteDomesticStandingOrderDataInitiation toFRWriteDomesticStandingOrderDataInitiation(OBWriteDomesticStandingOrder3DataInitiation initiation) {
        return initiation == null ? null : FRWriteDomesticStandingOrderDataInitiation.builder()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toFRAmount(initiation.getFirstPaymentAmount()))
                .recurringPaymentAmount(toFRAmount(initiation.getRecurringPaymentAmount()))
                .finalPaymentAmount(toFRAmount(initiation.getFinalPaymentAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    // FR to OB
    public static OBWriteDomesticStandingOrder3DataInitiation toOBWriteDomesticStandingOrder3DataInitiation(FRWriteDomesticStandingOrderDataInitiation initiation) {
        return initiation == null ? null : new OBWriteDomesticStandingOrder3DataInitiation()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toOBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount(initiation.getFirstPaymentAmount()))
                .recurringPaymentAmount(toOBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount(initiation.getRecurringPaymentAmount()))
                .finalPaymentAmount(toOBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount(initiation.getFinalPaymentAmount()))
                .debtorAccount(toOBWriteDomesticStandingOrder3DataInitiationDebtorAccount(initiation.getDebtorAccount()))
                .creditorAccount(toOBWriteDomesticStandingOrder3DataInitiationCreditorAccount(initiation.getCreditorAccount()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }

    public static OBDomesticStandingOrder1 toOBDomesticStandingOrder1(FRWriteDomesticStandingOrderDataInitiation initiation) {
        return initiation == null ? null : new OBDomesticStandingOrder1()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getFirstPaymentAmount()))
                .recurringPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getRecurringPaymentAmount()))
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getFinalPaymentAmount()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()));
    }

    public static OBDomesticStandingOrder2 toOBDomesticStandingOrder2(FRWriteDomesticStandingOrderDataInitiation initiation) {
        return initiation == null ? null : new OBDomesticStandingOrder2()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getFirstPaymentAmount()))
                .recurringPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getRecurringPaymentAmount()))
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getFinalPaymentAmount()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }

    public static OBDomesticStandingOrder3 toOBDomesticStandingOrder3(FRWriteDomesticStandingOrderDataInitiation initiation) {
        return initiation == null ? null : new OBDomesticStandingOrder3()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toOBDomesticStandingOrder3FirstPaymentAmount(initiation.getFirstPaymentAmount()))
                .recurringPaymentAmount(toOBDomesticStandingOrder3RecurringPaymentAmount(initiation.getRecurringPaymentAmount()))
                .finalPaymentAmount(toOBDomesticStandingOrder3FinalPaymentAmount(initiation.getFinalPaymentAmount()))
                .debtorAccount(toOBCashAccountDebtor4(initiation.getDebtorAccount()))
                .creditorAccount(toOBCashAccountCreditor3(initiation.getCreditorAccount()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }
}
