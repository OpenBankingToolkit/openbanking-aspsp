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

import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderDataInitiation;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.payment.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBDomestic2InstructedAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBWriteDomestic2DataInitiationInstructedAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialIdentificationConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRChargeBearerConverter.toFRChargeBearerType;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRChargeBearerConverter.toOBChargeBearerType1Code;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataAuthorisationConverter.toFRDataAuthorisation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataSCASupportDataConverter.toFRDataSCASupportData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPermissionConverter.toFRPermission;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRReadRefundAccountConverter.toFRReadRefundAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRSupplementaryDataConverter.toFRSupplementaryData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRSupplementaryDataConverter.toOBSupplementaryData1;
import static uk.org.openbanking.datamodel.service.converter.payment.CountryCodeHelper.determineCountryCode;

public class FRWriteInternationalStandingOrderConsentConverter {

    // OB to FR
    public static FRWriteInternationalStandingOrderConsent toFRWriteInternationalStandingOrderConsent(OBWriteInternationalStandingOrderConsent1 obWriteInternationalStandingOrderConsent1) {
        return obWriteInternationalStandingOrderConsent1 == null ? null : FRWriteInternationalStandingOrderConsent.builder()
                .data(toFRWriteInternationalStandingOrderConsentData(obWriteInternationalStandingOrderConsent1.getData()))
                .risk(toFRRisk(obWriteInternationalStandingOrderConsent1.getRisk()))
                .build();
    }

    public static FRWriteInternationalStandingOrderConsent toFRWriteInternationalStandingOrderConsent(OBWriteInternationalStandingOrderConsent2 obWriteInternationalStandingOrderConsent2) {
        return obWriteInternationalStandingOrderConsent2 == null ? null : FRWriteInternationalStandingOrderConsent.builder()
                .data(toFRWriteInternationalStandingOrderConsentData(obWriteInternationalStandingOrderConsent2.getData()))
                .risk(toFRRisk(obWriteInternationalStandingOrderConsent2.getRisk()))
                .build();
    }

    public static FRWriteInternationalStandingOrderConsent toFRWriteInternationalStandingOrderConsent(OBWriteInternationalStandingOrderConsent3 obWriteInternationalStandingOrderConsent3) {
        return obWriteInternationalStandingOrderConsent3 == null ? null : FRWriteInternationalStandingOrderConsent.builder()
                .data(toFRWriteInternationalStandingOrderConsentData(obWriteInternationalStandingOrderConsent3.getData()))
                .risk(toFRRisk(obWriteInternationalStandingOrderConsent3.getRisk()))
                .build();
    }

    public static FRWriteInternationalStandingOrderConsent toFRWriteInternationalStandingOrderConsent(OBWriteInternationalStandingOrderConsent5 obWriteInternationalStandingOrderConsent5) {
        return obWriteInternationalStandingOrderConsent5 == null ? null : FRWriteInternationalStandingOrderConsent.builder()
                .data(toFRWriteInternationalStandingOrderConsentData(obWriteInternationalStandingOrderConsent5.getData()))
                .risk(toFRRisk(obWriteInternationalStandingOrderConsent5.getRisk()))
                .build();
    }

    public static FRWriteInternationalStandingOrderConsent toFRWriteInternationalStandingOrderConsent(OBWriteInternationalStandingOrderConsent6 obWriteInternationalStandingOrderConsent6) {
        return obWriteInternationalStandingOrderConsent6 == null ? null : FRWriteInternationalStandingOrderConsent.builder()
                .data(toFRWriteInternationalStandingOrderConsentData(obWriteInternationalStandingOrderConsent6.getData()))
                .risk(toFRRisk(obWriteInternationalStandingOrderConsent6.getRisk()))
                .build();
    }

    public static FRWriteInternationalStandingOrderConsentData toFRWriteInternationalStandingOrderConsentData(OBWriteDataInternationalStandingOrderConsent1 data) {
        return data == null ? null : FRWriteInternationalStandingOrderConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteInternationalStandingOrderDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteInternationalStandingOrderConsentData toFRWriteInternationalStandingOrderConsentData(OBWriteDataInternationalStandingOrderConsent2 data) {
        return data == null ? null : FRWriteInternationalStandingOrderConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteInternationalStandingOrderDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteInternationalStandingOrderConsentData toFRWriteInternationalStandingOrderConsentData(OBWriteDataInternationalStandingOrderConsent3 data) {
        return data == null ? null : FRWriteInternationalStandingOrderConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteInternationalStandingOrderDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteInternationalStandingOrderConsentData toFRWriteInternationalStandingOrderConsentData(OBWriteInternationalStandingOrderConsent5Data data) {
        return data == null ? null : FRWriteInternationalStandingOrderConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteInternationalStandingOrderDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .scASupportData(toFRDataSCASupportData(data.getScASupportData()))
                .build();
    }

    public static FRWriteInternationalStandingOrderConsentData toFRWriteInternationalStandingOrderConsentData(OBWriteInternationalStandingOrderConsent6Data data) {
        return data == null ? null : FRWriteInternationalStandingOrderConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .readRefundAccount(toFRReadRefundAccount(data.getReadRefundAccount()))
                .initiation(toFRWriteInternationalStandingOrderDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .scASupportData(toFRDataSCASupportData(data.getScASupportData()))
                .build();
    }

    public static FRWriteInternationalStandingOrderDataInitiation toFRWriteInternationalStandingOrderDataInitiation(OBInternationalStandingOrder1 initiation) {
        OBCashAccount3 creditorAccount = initiation.getCreditorAccount();
        return initiation == null ? null : FRWriteInternationalStandingOrderDataInitiation.builder()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .purpose(initiation.getPurpose())
                .chargeBearer(toFRChargeBearerType(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(determineCountryCode(creditorAccount.getSchemeName(), creditorAccount.getIdentification())) // default value to prevent validation error
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditor(toFRDataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toFRDataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toFRAccount(creditorAccount))
                .build();
    }

    public static FRWriteInternationalStandingOrderDataInitiation toFRWriteInternationalStandingOrderDataInitiation(OBInternationalStandingOrder2 initiation) {
        OBCashAccount3 creditorAccount = initiation.getCreditorAccount();
        return initiation == null ? null : FRWriteInternationalStandingOrderDataInitiation.builder()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .purpose(initiation.getPurpose())
                .chargeBearer(toFRChargeBearerType(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(determineCountryCode(creditorAccount.getSchemeName(), creditorAccount.getIdentification())) // default value to prevent validation error
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditor(toFRDataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toFRDataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toFRAccount(creditorAccount))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    public static FRWriteInternationalStandingOrderDataInitiation toFRWriteInternationalStandingOrderDataInitiation(OBInternationalStandingOrder3 initiation) {
        OBCashAccountCreditor3 creditorAccount = initiation.getCreditorAccount();
        return initiation == null ? null : FRWriteInternationalStandingOrderDataInitiation.builder()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .purpose(initiation.getPurpose())
                .chargeBearer(toFRChargeBearerType(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(determineCountryCode(creditorAccount.getSchemeName(), creditorAccount.getIdentification())) // default value to prevent validation error
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditor(toFRDataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toFRDataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toFRAccount(creditorAccount))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    public static FRWriteInternationalStandingOrderDataInitiation toFRWriteInternationalStandingOrderDataInitiation(OBWriteInternationalStandingOrder4DataInitiation initiation) {
        return initiation == null ? null : FRWriteInternationalStandingOrderDataInitiation.builder()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .purpose(initiation.getPurpose())
                .extendedPurpose(initiation.getExtendedPurpose())
                .chargeBearer(toFRChargeBearerType(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(initiation.getDestinationCountryCode())
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditor(toFRDataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toFRDataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    // FR to OB
    public static OBWriteInternationalStandingOrder4DataInitiation toOBWriteInternationalStandingOrder4DataInitiation(FRWriteInternationalStandingOrderDataInitiation initiation) {
        return initiation == null ? null : new OBWriteInternationalStandingOrder4DataInitiation()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .purpose(initiation.getPurpose())
                .extendedPurpose(initiation.getExtendedPurpose())
                .chargeBearer(toOBChargeBearerType1Code(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .destinationCountryCode(initiation.getDestinationCountryCode())
                .instructedAmount(toOBWriteDomestic2DataInitiationInstructedAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBWriteDomesticStandingOrder3DataInitiationDebtorAccount(initiation.getDebtorAccount()))
                .creditor(toOBWriteInternational3DataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toOBWriteInternationalStandingOrder4DataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toOBWriteInternationalStandingOrder4DataInitiationCreditorAccount(initiation.getCreditorAccount()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }

    public static OBInternationalStandingOrder1 toOBInternationalStandingOrder1(FRWriteInternationalStandingOrderDataInitiation initiation) {
        return initiation == null ? null : new OBInternationalStandingOrder1()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .purpose(initiation.getPurpose())
                .chargeBearer(toOBChargeBearerType1Code(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditor(toOBPartyIdentification43(initiation.getCreditor()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(initiation.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()));
    }

    public static OBInternationalStandingOrder2 toOBInternationalStandingOrder2(FRWriteInternationalStandingOrderDataInitiation initiation) {
        return initiation == null ? null : new OBInternationalStandingOrder2()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .purpose(initiation.getPurpose())
                .chargeBearer(toOBChargeBearerType1Code(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditor(toOBPartyIdentification43(initiation.getCreditor()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(initiation.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }

    public static OBInternationalStandingOrder3 toOBInternationalStandingOrder3(FRWriteInternationalStandingOrderDataInitiation initiation) {
        return initiation == null ? null : new OBInternationalStandingOrder3()
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .purpose(initiation.getPurpose())
                .chargeBearer(toOBChargeBearerType1Code(initiation.getChargeBearer()))
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBDomestic2InstructedAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBCashAccountDebtor4(initiation.getDebtorAccount()))
                .creditor(toOBPartyIdentification43(initiation.getCreditor()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(initiation.getCreditorAgent()))
                .creditorAccount(toOBCashAccountCreditor3(initiation.getCreditorAccount()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }
}
