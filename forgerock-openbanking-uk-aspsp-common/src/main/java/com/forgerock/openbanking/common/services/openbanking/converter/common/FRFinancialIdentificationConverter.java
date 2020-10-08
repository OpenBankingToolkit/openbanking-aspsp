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
package com.forgerock.openbanking.common.services.openbanking.converter.common;

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialCreditor;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAgent;
import uk.org.openbanking.datamodel.payment.OBBranchAndFinancialInstitutionIdentification3;
import uk.org.openbanking.datamodel.payment.OBBranchAndFinancialInstitutionIdentification6;
import uk.org.openbanking.datamodel.payment.OBDebtorIdentification1;
import uk.org.openbanking.datamodel.payment.OBPartyIdentification43;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3DataInitiationCreditor;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3DataInitiationCreditorAgent;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4DataInitiationCreditorAgent;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRPostalAddressConverter.toFRPostalAddress;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRPostalAddressConverter.toOBPostalAddress6;

public class FRFinancialIdentificationConverter {

    // OB to FR
    public static FRFinancialCreditor toFRDataInitiationCreditor(OBPartyIdentification43 creditor) {
        return creditor == null ? null : FRFinancialCreditor.builder()
                .name(creditor.getName())
                .postalAddress(toFRPostalAddress(creditor.getPostalAddress()))
                .build();
    }

    public static FRFinancialCreditor toFRDataInitiationCreditor(OBWriteInternational3DataInitiationCreditor creditor) {
        return creditor == null ? null : FRFinancialCreditor.builder()
                .name(creditor.getName())
                .postalAddress(toFRPostalAddress(creditor.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRDataInitiationCreditorAgent(OBBranchAndFinancialInstitutionIdentification3 creditorAgent) {
        return creditorAgent == null ? null : FRFinancialAgent.builder()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toFRPostalAddress(creditorAgent.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRDataInitiationCreditorAgent(OBBranchAndFinancialInstitutionIdentification6 creditorAgent) {
        return creditorAgent == null ? null : FRFinancialAgent.builder()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toFRPostalAddress(creditorAgent.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRDataInitiationCreditorAgent(OBWriteInternational3DataInitiationCreditorAgent creditorAgent) {
        return creditorAgent == null ? null : FRFinancialAgent.builder()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toFRPostalAddress(creditorAgent.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRDataInitiationCreditorAgent(OBWriteInternationalStandingOrder4DataInitiationCreditorAgent creditorAgent) {
        return creditorAgent == null ? null : FRFinancialAgent.builder()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toFRPostalAddress(creditorAgent.getPostalAddress()))
                .build();
    }

    // FR to OB
    public static OBWriteInternational3DataInitiationCreditor toOBWriteInternational3DataInitiationCreditor(FRFinancialCreditor creditor) {
        return creditor == null ? null : new OBWriteInternational3DataInitiationCreditor()
                .name(creditor.getName())
                .postalAddress(toOBPostalAddress6(creditor.getPostalAddress()));
    }

    public static OBWriteInternational3DataInitiationCreditorAgent toOBWriteInternational3DataInitiationCreditorAgent(FRFinancialAgent creditorAgent) {
        return creditorAgent == null ? null : new OBWriteInternational3DataInitiationCreditorAgent()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toOBPostalAddress6(creditorAgent.getPostalAddress()));
    }

    public static OBWriteInternationalStandingOrder4DataInitiationCreditorAgent toOBWriteInternationalStandingOrder4DataInitiationCreditorAgent(FRFinancialAgent creditorAgent) {
        return creditorAgent == null ? null : new OBWriteInternationalStandingOrder4DataInitiationCreditorAgent()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toOBPostalAddress6(creditorAgent.getPostalAddress()));
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(FRFinancialAgent creditorAgent) {
        return creditorAgent == null ? null : new OBBranchAndFinancialInstitutionIdentification3()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toOBPostalAddress6(creditorAgent.getPostalAddress()));
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(FRFinancialAgent creditorAgent) {
        return creditorAgent == null ? null : new OBBranchAndFinancialInstitutionIdentification6()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toOBPostalAddress6(creditorAgent.getPostalAddress()));
    }

    public static OBPartyIdentification43 toOBPartyIdentification43(FRFinancialCreditor creditor) {
        return creditor == null ? null : new OBPartyIdentification43()
                .name(creditor.getName())
                .postalAddress(toOBPostalAddress6(creditor.getPostalAddress()));
    }

    // TODO #296 - check FRAccount is the correct object here
    public static OBDebtorIdentification1 toOBDebtorIdentification1(FRAccountIdentifier frAccountIdentifier) {
        return frAccountIdentifier == null ? null : new OBDebtorIdentification1()
                .name(frAccountIdentifier.getName());
    }
}
