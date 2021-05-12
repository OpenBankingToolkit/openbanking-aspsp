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

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAgent;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialCreditor;
import uk.org.openbanking.datamodel.account.*;
import uk.org.openbanking.datamodel.payment.OBBranchAndFinancialInstitutionIdentification3;
import uk.org.openbanking.datamodel.payment.OBBranchAndFinancialInstitutionIdentification6;
import uk.org.openbanking.datamodel.payment.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBExternalFinancialInstitutionIdentification2Code;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentPostalAddressConverter.toFRPostalAddress;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentPostalAddressConverter.toOBPostalAddress6;

public class FRFinancialInstrumentConverter {

    // OB to FR
    public static FRFinancialCreditor toFRFinancialCreditor(OBPartyIdentification43 creditor) {
        return creditor == null ? null : FRFinancialCreditor.builder()
                .name(creditor.getName())
                .postalAddress(toFRPostalAddress(creditor.getPostalAddress()))
                .build();
    }

    public static FRFinancialCreditor toFRFinancialCreditor(OBWriteInternational3DataInitiationCreditor creditor) {
        return creditor == null ? null : FRFinancialCreditor.builder()
                .name(creditor.getName())
                .postalAddress(toFRPostalAddress(creditor.getPostalAddress()))
                .build();
    }

    public static FRFinancialCreditor toFRFinancialCreditor(OBWriteInternationalScheduledConsentResponse6DataInitiationCreditor creditor) {
        return creditor == null ? null : FRFinancialCreditor.builder()
                .name(creditor.getName())
                .postalAddress(toFRPostalAddress(creditor.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRFinancialAgent(OBBranchAndFinancialInstitutionIdentification3 agent) {
        return agent == null ? null : FRFinancialAgent.builder()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toFRPostalAddress(agent.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRFinancialAgent(OBBranchAndFinancialInstitutionIdentification5 agent) {
        return agent == null ? null : FRFinancialAgent.builder()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .build();
    }

    public static FRFinancialAgent toFRFinancialAgent(OBBranchAndFinancialInstitutionIdentification6 agent) {
        return agent == null ? null : FRFinancialAgent.builder()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toFRPostalAddress(agent.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRFinancialAgent(uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification6 agent) {
        return agent == null ? null : FRFinancialAgent.builder()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toFRPostalAddress(agent.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRFinancialAgent(OBBranchAndFinancialInstitutionIdentification51 agent) {
        return agent == null ? null : FRFinancialAgent.builder()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .build();
    }

    public static FRFinancialAgent toFRFinancialAgent(OBBranchAndFinancialInstitutionIdentification60 agent) {
        return agent == null ? null : FRFinancialAgent.builder()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toFRPostalAddress(agent.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRFinancialAgent(OBBranchAndFinancialInstitutionIdentification61 agent) {
        return agent == null ? null : FRFinancialAgent.builder()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toFRPostalAddress(agent.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRFinancialAgent(OBBranchAndFinancialInstitutionIdentification62 agent) {
        return agent == null ? null : FRFinancialAgent.builder()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toFRPostalAddress(agent.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRFinancialAgent(OBWriteInternational3DataInitiationCreditorAgent agent) {
        return agent == null ? null : FRFinancialAgent.builder()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toFRPostalAddress(agent.getPostalAddress()))
                .build();
    }

    public static FRFinancialAgent toFRFinancialAgent(OBWriteInternationalStandingOrder4DataInitiationCreditorAgent agent) {
        return agent == null ? null : FRFinancialAgent.builder()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toFRPostalAddress(agent.getPostalAddress()))
                .build();
    }

    // FR to OB
    public static OBWriteInternational3DataInitiationCreditor toOBWriteInternational3DataInitiationCreditor(FRFinancialCreditor creditor) {
        return creditor == null ? null : new OBWriteInternational3DataInitiationCreditor()
                .name(creditor.getName())
                .postalAddress(toOBPostalAddress6(creditor.getPostalAddress()));
    }

    public static OBWriteInternationalScheduledConsentResponse6DataInitiationCreditor toOBWriteInternationalScheduledConsentResponse6DataInitiationCreditor(FRFinancialCreditor creditor) {
        return creditor == null ? null : new OBWriteInternationalScheduledConsentResponse6DataInitiationCreditor()
                .name(creditor.getName())
                .postalAddress(toOBPostalAddress6(creditor.getPostalAddress()));
    }

    public static OBWriteInternational3DataInitiationCreditorAgent toOBWriteInternational3DataInitiationCreditorAgent(FRFinancialAgent agent) {
        return agent == null ? null : new OBWriteInternational3DataInitiationCreditorAgent()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toOBPostalAddress6(agent.getPostalAddress()));
    }

    public static OBWriteInternationalStandingOrder4DataInitiationCreditorAgent toOBWriteInternationalStandingOrder4DataInitiationCreditorAgent(FRFinancialAgent agent) {
        return agent == null ? null : new OBWriteInternationalStandingOrder4DataInitiationCreditorAgent()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toOBPostalAddress6(agent.getPostalAddress()));
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(FRFinancialAgent agent) {
        return agent == null ? null : new OBBranchAndFinancialInstitutionIdentification2()
                .schemeName(toOBExternalFinancialInstitutionIdentification2Code(agent.getSchemeName()))
                .identification(agent.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(FRFinancialAgent agent) {
        return agent == null ? null : new OBBranchAndFinancialInstitutionIdentification3()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toOBPostalAddress6(agent.getPostalAddress()));
    }

    public static OBBranchAndFinancialInstitutionIdentification4 toOBBranchAndFinancialInstitutionIdentification4(FRFinancialAgent agent) {
        return agent == null ? null : new OBBranchAndFinancialInstitutionIdentification4()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification5 toOBBranchAndFinancialInstitutionIdentification5(FRFinancialAgent agent) {
        return agent == null ? null : new OBBranchAndFinancialInstitutionIdentification5()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification51 toOBBranchAndFinancialInstitutionIdentification51(FRFinancialAgent agent) {
        return agent == null ? null : new OBBranchAndFinancialInstitutionIdentification51()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(FRFinancialAgent agent) {
        return agent == null ? null : new OBBranchAndFinancialInstitutionIdentification6()
                .schemeName(agent.getSchemeName())
                .identification(agent.getIdentification())
                .name(agent.getName())
                .postalAddress(toOBPostalAddress6(agent.getPostalAddress()));
    }

    public static OBPartyIdentification43 toOBPartyIdentification43(FRFinancialCreditor creditor) {
        return creditor == null ? null : new OBPartyIdentification43()
                .name(creditor.getName())
                .postalAddress(toOBPostalAddress6(creditor.getPostalAddress()));
    }

}
