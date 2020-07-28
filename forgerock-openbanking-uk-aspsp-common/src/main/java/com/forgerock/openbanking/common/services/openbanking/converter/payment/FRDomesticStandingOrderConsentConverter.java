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

import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRDomesticStandingOrderConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticStandingOrderConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRDomesticStandingOrderConsent3;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticStandingOrderConsent5;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.payment.*;
import uk.org.openbanking.datamodel.service.converter.payment.OBAccountConverter;
import uk.org.openbanking.datamodel.service.converter.payment.OBWriteDomesticStandingOrderConsentConverter;

@Service
public class FRDomesticStandingOrderConsentConverter {

    public FRDomesticStandingOrderConsent2 toFRDomesticConsent2(FRDomesticStandingOrderConsent1 frDomesticStandingOrderConsent1) {
        FRDomesticStandingOrderConsent2 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent2();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent1.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent1.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent1.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent1.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent1.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(OBWriteDomesticStandingOrderConsentConverter.toOBWriteDomesticStandingOrderConsent2(frDomesticStandingOrderConsent1.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent1.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent1.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent1.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent1.getUpdated());
        return frDomesticScheduledConsent2;
    }

    public FRDomesticStandingOrderConsent1 toFRDomesticConsent1(FRDomesticStandingOrderConsent2 frDomesticStandingOrderConsent2) {
        FRDomesticStandingOrderConsent1 frDomesticScheduledConsent1 = new FRDomesticStandingOrderConsent1();
        frDomesticScheduledConsent1.setStatus(frDomesticStandingOrderConsent2.getStatus());
        frDomesticScheduledConsent1.setId(frDomesticStandingOrderConsent2.getId());
        frDomesticScheduledConsent1.setUserId(frDomesticStandingOrderConsent2.getUserId());
        frDomesticScheduledConsent1.setAccountId(frDomesticStandingOrderConsent2.getAccountId());
        frDomesticScheduledConsent1.setCreated(frDomesticStandingOrderConsent2.getCreated());
        frDomesticScheduledConsent1.setDomesticStandingOrderConsent(OBWriteDomesticStandingOrderConsentConverter.toOBWriteDomesticStandingOrderConsent1(frDomesticStandingOrderConsent2.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent1.setPispId(frDomesticStandingOrderConsent2.getPispId());
        frDomesticScheduledConsent1.setPispName(frDomesticStandingOrderConsent2.getPispName());
        frDomesticScheduledConsent1.setStatusUpdate(frDomesticStandingOrderConsent2.getStatusUpdate());
        frDomesticScheduledConsent1.setUpdated(frDomesticStandingOrderConsent2.getUpdated());
        return frDomesticScheduledConsent1;
    }

    public FRDomesticStandingOrderConsent2 toFRDomesticConsent2(FRDomesticStandingOrderConsent3 frDomesticStandingOrderConsent3) {
        FRDomesticStandingOrderConsent2 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent2();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent3.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent3.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent3.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent3.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent3.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(OBWriteDomesticStandingOrderConsentConverter.toOBWriteDomesticStandingOrderConsent2(frDomesticStandingOrderConsent3.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent3.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent3.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent3.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent3.getUpdated());
        return frDomesticScheduledConsent2;
    }


    public FRDomesticStandingOrderConsent1 toFRDomesticConsent1(FRDomesticStandingOrderConsent3 frDomesticStandingOrderConsent3) {
        FRDomesticStandingOrderConsent1 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent1();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent3.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent3.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent3.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent3.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent3.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(OBWriteDomesticStandingOrderConsentConverter.toOBWriteDomesticStandingOrderConsent1(frDomesticStandingOrderConsent3.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent3.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent3.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent3.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent3.getUpdated());
        return frDomesticScheduledConsent2;
    }

    public FRDomesticStandingOrderConsent1 toFRDomesticConsent1(FRDomesticStandingOrderConsent5 frDomesticStandingOrderConsent5) {
        FRDomesticStandingOrderConsent1 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent1();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent5.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent5.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent5.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent5.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent5.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(toOBWriteDomesticStandingOrderConsent1(frDomesticStandingOrderConsent5.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent5.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent5.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent5.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent5.getUpdated());
        return frDomesticScheduledConsent2;
    }

    public FRDomesticStandingOrderConsent2 toFRDomesticConsent2(FRDomesticStandingOrderConsent5 frDomesticStandingOrderConsent5) {
        FRDomesticStandingOrderConsent2 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent2();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent5.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent5.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent5.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent5.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent5.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(toOBWriteDomesticStandingOrderConsent2(frDomesticStandingOrderConsent5.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent5.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent5.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent5.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent5.getUpdated());
        return frDomesticScheduledConsent2;
    }

    // TODO #272 move to uk datamodel
    public static OBWriteDomesticStandingOrderConsent1 toOBWriteDomesticStandingOrderConsent1(OBWriteDomesticStandingOrderConsent5 domesticStandingOrderConsent) {
        return (new OBWriteDomesticStandingOrderConsent1())
                .data(toOBWriteDataDomesticStandingOrderConsent1(domesticStandingOrderConsent.getData()))
                .risk(domesticStandingOrderConsent.getRisk());
    }

    public static OBWriteDomesticStandingOrderConsent2 toOBWriteDomesticStandingOrderConsent2(OBWriteDomesticStandingOrderConsent5 domesticStandingOrderConsent) {
        return (new OBWriteDomesticStandingOrderConsent2())
                .data(toOBWriteDataDomesticStandingOrderConsent2(domesticStandingOrderConsent.getData()))
                .risk(domesticStandingOrderConsent.getRisk());
    }

    public static OBWriteDataDomesticStandingOrderConsent1 toOBWriteDataDomesticStandingOrderConsent1(OBWriteDomesticStandingOrderConsent5Data data) {
        return data == null ? null : (new OBWriteDataDomesticStandingOrderConsent1())
                .permission(OBExternalPermissions2Code.valueOf(data.getPermission().name()))
                .initiation(toOBDomesticStandingOrder1(data.getInitiation()))
                .authorisation(toOBAuthorisation1(data.getAuthorisation()));
    }

    public static OBWriteDataDomesticStandingOrderConsent2 toOBWriteDataDomesticStandingOrderConsent2(OBWriteDomesticStandingOrderConsent5Data data) {
        return data == null ? null : (new OBWriteDataDomesticStandingOrderConsent2())
                .permission(OBExternalPermissions2Code.valueOf(data.getPermission().name()))
                .initiation(toOBDomesticStandingOrder2(data.getInitiation()))
                .authorisation(toOBAuthorisation1(data.getAuthorisation()));
    }

    public static OBDomesticStandingOrder1 toOBDomesticStandingOrder1(OBWriteDomesticStandingOrder3DataInitiation initiation) {
        return initiation == null ? null : (new OBDomesticStandingOrder1())
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getFirstPaymentAmount()))
                .recurringPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getRecurringPaymentAmount()))
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getFinalPaymentAmount()))
                .debtorAccount(OBAccountConverter.toOBCashAccount3(initiation.getDebtorAccount()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()));
    }

    public static OBDomesticStandingOrder2 toOBDomesticStandingOrder2(OBWriteDomesticStandingOrder3DataInitiation initiation) {
        return initiation == null ? null : (new OBDomesticStandingOrder2())
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getFirstPaymentAmount()))
                .recurringPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getRecurringPaymentAmount()))
                .finalPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getFinalPaymentAmount()))
                .debtorAccount(OBAccountConverter.toOBCashAccount3(initiation.getDebtorAccount()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .supplementaryData(initiation.getSupplementaryData());
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount amount) {
        return amount == null ? null : (new OBActiveOrHistoricCurrencyAndAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount amount) {
        return amount == null ? null : (new OBActiveOrHistoricCurrencyAndAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount amount) {
        return amount == null ? null : (new OBActiveOrHistoricCurrencyAndAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }

    public static OBCashAccount3 toOBCashAccount3(OBWriteDomesticStandingOrder3DataInitiationCreditorAccount creditorAccount) {
        return creditorAccount == null ? null : (new OBCashAccount3())
                .schemeName(creditorAccount.getSchemeName())
                .identification(creditorAccount.getIdentification())
                .name(creditorAccount.getName())
                .secondaryIdentification(creditorAccount.getSecondaryIdentification());
    }

    public static OBAuthorisation1 toOBAuthorisation1(OBWriteDomesticConsent4DataAuthorisation authorisation) {
        return authorisation == null ? null : (new OBAuthorisation1())
                .authorisationType(OBExternalAuthorisation1Code.valueOf(authorisation.getAuthorisationType().name()))
                .completionDateTime(authorisation.getCompletionDateTime());
    }
}
