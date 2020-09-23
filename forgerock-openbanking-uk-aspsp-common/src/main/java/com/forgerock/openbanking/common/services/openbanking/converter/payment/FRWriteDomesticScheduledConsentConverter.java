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

import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticScheduledConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticScheduledConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticScheduledDataInitiation;
import uk.org.openbanking.datamodel.payment.OBDomesticScheduled1;
import uk.org.openbanking.datamodel.payment.OBDomesticScheduled2;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticScheduledConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduled2DataInitiation;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent4Data;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.toFRAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.toOBWriteDomestic2DataInitiationCreditorAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountConverter.toOBWriteDomestic2DataInitiationDebtorAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBWriteDomestic2DataInitiationInstructedAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRPostalAddressConverter.toFRPostalAddress;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRPostalAddressConverter.toOBPostalAddress6;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataAuthorisationConverter.toFRDataAuthorisation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataSCASupportDataConverter.toFRDataSCASupportData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPermissionConverter.toFRPermission;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRReadRefundAccountConverter.toFRReadRefundAccount;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toFRRemittanceInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toOBRemittanceInformation1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter.toOBWriteDomestic2DataInitiationRemittanceInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRSupplementaryDataConverter.toFRSupplementaryData;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRSupplementaryDataConverter.toOBSupplementaryData1;

public class FRWriteDomesticScheduledConsentConverter {

    // OB to FR
    public static FRWriteDomesticScheduledConsent toFRWriteDomesticScheduledConsent(OBWriteDomesticScheduledConsent2 obWriteDomesticScheduledConsent2) {
        return obWriteDomesticScheduledConsent2 == null ? null : FRWriteDomesticScheduledConsent.builder()
                .data(toFRWriteDomesticScheduledConsentData(obWriteDomesticScheduledConsent2.getData()))
                .risk(toFRRisk(obWriteDomesticScheduledConsent2.getRisk()))
                .build();
    }

    public static FRWriteDomesticScheduledConsent toFRWriteDomesticScheduledConsent(OBWriteDomesticScheduledConsent4 obWriteDomesticScheduledConsent4) {
        return obWriteDomesticScheduledConsent4 == null ? null : FRWriteDomesticScheduledConsent.builder()
                .data(toFRWriteDomesticScheduledConsentData(obWriteDomesticScheduledConsent4.getData()))
                .risk(toFRRisk(obWriteDomesticScheduledConsent4.getRisk()))
                .build();
    }

    public static FRWriteDomesticScheduledConsentData toFRWriteDomesticScheduledConsentData(OBWriteDataDomesticScheduledConsent2 data) {
        return data == null ? null : FRWriteDomesticScheduledConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .initiation(toFRWriteDomesticScheduledDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .build();
    }

    public static FRWriteDomesticScheduledConsentData toFRWriteDomesticScheduledConsentData(OBWriteDomesticScheduledConsent4Data data) {
        return data == null ? null : FRWriteDomesticScheduledConsentData.builder()
                .permission(toFRPermission(data.getPermission()))
                .readRefundAccount(toFRReadRefundAccount(data.getReadRefundAccount()))
                .initiation(toFRWriteDomesticScheduledDataInitiation(data.getInitiation()))
                .authorisation(toFRDataAuthorisation(data.getAuthorisation()))
                .scASupportData(toFRDataSCASupportData(data.getScASupportData()))
                .build();
    }

    public static FRWriteDomesticScheduledDataInitiation toFRWriteDomesticScheduledDataInitiation(OBDomesticScheduled1 initiation) {
        return initiation == null ? null : FRWriteDomesticScheduledDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .creditorPostalAddress(toFRPostalAddress(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .build();
    }

    public static FRWriteDomesticScheduledDataInitiation toFRWriteDomesticScheduledDataInitiation(OBDomesticScheduled2 initiation) {
        return initiation == null ? null : FRWriteDomesticScheduledDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .creditorPostalAddress(toFRPostalAddress(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    public static FRWriteDomesticScheduledDataInitiation toFRWriteDomesticScheduledDataInitiation(OBWriteDomesticScheduled2DataInitiation initiation) {
        return initiation == null ? null : FRWriteDomesticScheduledDataInitiation.builder()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .instructedAmount(toFRAmount(initiation.getInstructedAmount()))
                .debtorAccount(toFRAccount(initiation.getDebtorAccount()))
                .creditorAccount(toFRAccount(initiation.getCreditorAccount()))
                .creditorPostalAddress(toFRPostalAddress(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(toFRSupplementaryData(initiation.getSupplementaryData()))
                .build();
    }

    // FR to OB
    public static OBWriteDomesticScheduled2DataInitiation toOBWriteDomesticScheduled2DataInitiation(FRWriteDomesticScheduledDataInitiation initiation) {
        return initiation == null ? null : new OBWriteDomesticScheduled2DataInitiation()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .instructedAmount(toOBWriteDomestic2DataInitiationInstructedAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBWriteDomestic2DataInitiationDebtorAccount(initiation.getDebtorAccount()))
                .creditorAccount(toOBWriteDomestic2DataInitiationCreditorAccount(initiation.getCreditorAccount()))
                .creditorPostalAddress(toOBPostalAddress6(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toOBWriteDomestic2DataInitiationRemittanceInformation(initiation.getRemittanceInformation()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }

    public static OBDomesticScheduled1 toOBDomesticScheduled1(FRWriteDomesticScheduledDataInitiation initiation) {
        return initiation == null ? null : new OBDomesticScheduled1()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .creditorPostalAddress(toOBPostalAddress6(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()));
    }

    public static OBDomesticScheduled2 toOBDomesticScheduled2(FRWriteDomesticScheduledDataInitiation initiation) {
        return initiation == null ? null : new OBDomesticScheduled2()
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .creditorAccount(toOBCashAccount3(initiation.getCreditorAccount()))
                .creditorPostalAddress(toOBPostalAddress6(initiation.getCreditorPostalAddress()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()))
                .supplementaryData(toOBSupplementaryData1(initiation.getSupplementaryData()));
    }
}
