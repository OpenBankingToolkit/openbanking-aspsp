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

import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticDataInitiation;
import uk.org.openbanking.datamodel.payment.*;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBWriteDomestic2DataInitiationInstructedAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRPostalAddressConverter.toFRPostalAddress;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRPostalAddressConverter.toOBPostalAddress6;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataAuthorisationConverter.toFRDataAuthorisation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataSCASupportDataConverter.toFRDataSCASupportData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRReadRefundAccountConverter.toFRReadRefundAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toFRRemittanceInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toOBRemittanceInformation1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toOBWriteDomestic2DataInitiationRemittanceInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRSupplementaryDataConverter.toFRSupplementaryData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRSupplementaryDataConverter.toOBSupplementaryData1;

public class FRWriteDomesticConsentConverter {

    // OB to FR
    public static FRWriteDomesticConsent toFRWriteDomesticConsent(OBWriteDomesticConsent1 obWriteDomesticConsent1) {
        return obWriteDomesticConsent1 == null ? null : FRWriteDomesticConsent.builder()
                .data(toFRWriteDomesticConsentData(obWriteDomesticConsent1.getData()))
                .risk(toFRRisk(obWriteDomesticConsent1.getRisk()))
                .build();
    }

    public static FRWriteDomesticConsent toFRWriteDomesticConsent(OBWriteDomesticConsent2 obWriteDomesticConsent2) {
        return obWriteDomesticConsent2 == null ? null : FRWriteDomesticConsent.builder()
                .data(toFRWriteDomesticConsentData(obWriteDomesticConsent2.getData()))
                .risk(toFRRisk(obWriteDomesticConsent2.getRisk()))
                .build();
    }

    public static FRWriteDomesticConsent toFRWriteDomesticConsent(OBWriteDomesticConsent3 obWriteDomesticConsent3) {
        return obWriteDomesticConsent3 == null ? null : FRWriteDomesticConsent.builder()
                .data(toFRWriteDomesticConsentData(obWriteDomesticConsent3.getData()))
                .risk(toFRRisk(obWriteDomesticConsent3.getRisk()))
                .build();
    }

    public static FRWriteDomesticConsent toFRWriteDomesticConsent(OBWriteDomesticConsent4 obWriteDomesticConsent4) {
        return obWriteDomesticConsent4 == null ? null : FRWriteDomesticConsent.builder()
                .data(toFRWriteDomesticConsentData(obWriteDomesticConsent4.getData()))
                .risk(toFRRisk(obWriteDomesticConsent4.getRisk()))
                .build();
    }

    public static FRWriteDomesticConsentData toFRWriteDomesticConsentData(OBWriteDataDomesticConsent1 data) {
        return data == null ? null : FRWriteDomesticConsentData.builder()
                .initiation(toFRWriteDomesticDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteDomesticConsentData toFRWriteDomesticConsentData(OBWriteDataDomesticConsent2 data) {
        return data == null ? null : FRWriteDomesticConsentData.builder()
                .initiation(toFRWriteDomesticDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteDomesticConsentData toFRWriteDomesticConsentData(OBWriteDomesticConsent3Data data) {
        return data == null ? null : FRWriteDomesticConsentData.builder()
                .initiation(toFRWriteDomesticDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .scASupportData(toFRDataSCASupportData(data.getScASupportData()))
                .build();
    }

    public static FRWriteDomesticConsentData toFRWriteDomesticConsentData(OBWriteDomesticConsent4Data data) {
        return data == null ? null : FRWriteDomesticConsentData.builder()
                .readRefundAccount(toFRReadRefundAccount(data.getReadRefundAccount()))
                .initiation(toFRWriteDomesticDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .scASupportData(toFRDataSCASupportData(data.getScASupportData()))
                .build();
    }

    public static FRWriteDomesticDataInitiation toFRWriteDomesticDataInitiation(OBDomestic2 initiation) {
        return initiation == null ? null : FRWriteDomesticDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .creditorPostalAddress(toFRPostalAddress(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    public static FRWriteDomesticDataInitiation toFRWriteDomesticDataInitiation(OBWriteDomestic2DataInitiation initiation) {
        return initiation == null ? null : FRWriteDomesticDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .creditorPostalAddress(toFRPostalAddress(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    public static FRWriteDomesticDataInitiation toFRWriteDomesticDataInitiation(OBDomestic1 initiation) {
        return initiation == null ? null : FRWriteDomesticDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .creditorPostalAddress(toFRPostalAddress(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .build();
    }

    public static FRWriteDomesticConsent toFRWriteDomesticConsent(OBPaymentSetup1 obPaymentSetup1) {
        return obPaymentSetup1 == null ? null : FRWriteDomesticConsent.builder()
                .data(toFRWriteDomesticConsentData(obPaymentSetup1.getData()))
                .risk(toFRRisk(obPaymentSetup1.getRisk()))
                .build();
    }

    public static FRWriteDomesticConsentData toFRWriteDomesticConsentData(OBPaymentDataSetup1 data) {
        return data == null ? null : FRWriteDomesticConsentData.builder()
                .initiation(toFRWriteDomesticDataInitiation(data.getInitiation()))
                .build();
    }

    public static FRWriteDomesticDataInitiation toFRWriteDomesticDataInitiation(OBInitiation1 initiation) {
        return initiation == null ? null : FRWriteDomesticDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(null)
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .creditorPostalAddress(null)
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(null)
                .build();
    }


    // FR to OB
    public static OBWriteDomestic2DataInitiation toOBWriteDomestic2DataInitiation(FRWriteDomesticDataInitiation initiation) {
        return initiation == null ? null : new OBWriteDomestic2DataInitiation()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructedAmount(toOBWriteDomestic2DataInitiationInstructedAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBWriteDomestic2DataInitiationDebtorAccount(initiation.getDebtorAccount()))
                .creditorAccount(toOBWriteDomestic2DataInitiationCreditorAccount(initiation.getCreditorAccount()))
                .creditorPostalAddress(toOBPostalAddress6(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toOBWriteDomestic2DataInitiationRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }

    public static OBInitiation1 toOBInitiation1(FRWriteDomesticDataInitiation initiation) {
        return initiation == null ? null : new OBInitiation1()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                //.debtorAgent(initiation.getDebtorAgent()) // this field isn't available in v3.x, so isn't stored in the repository
                .debtorAccount(toOBCashAccountDebtor1(initiation.getDebtorAccount()))
                //.creditorAgent(initiation.getCreditorAgent()) // this field isn't available in v3.x, so isn't stored in the repository
                .creditorAccount(toOBCashAccountCreditor1(initiation.getCreditorAccount()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()));
    }

    public static OBDomestic1 toOBDomestic1(FRWriteDomesticDataInitiation initiation) {
        return initiation == null ? null : new OBDomestic1()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .creditorPostalAddress(toOBPostalAddress6(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()));
    }

    public static OBDomestic2 toOBDomestic2(FRWriteDomesticDataInitiation initiation) {
        return initiation == null ? null : new OBDomestic2()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .creditorPostalAddress(toOBPostalAddress6(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }
}
