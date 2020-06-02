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
package com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment;

import uk.org.openbanking.datamodel.payment.*;

public class OBInternationalIdentifierConverter {

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBWriteInternational3DataInitiationCreditorAgent creditorAgent) {
        return (new OBBranchAndFinancialInstitutionIdentification3())
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(creditorAgent.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(OBWriteInternationalStandingOrder4DataInitiationCreditorAgent creditorAgent) {
        return (new OBBranchAndFinancialInstitutionIdentification6())
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(creditorAgent.getPostalAddress());
    }

    public static OBPartyIdentification43 toOBPartyIdentification43(OBWriteInternational3DataInitiationCreditor creditor) {
        return (new OBPartyIdentification43())
                .name(creditor.getName())
                .postalAddress(creditor.getPostalAddress());
    }

    public static OBWriteInternational3DataInitiationCreditorAgent toOBWriteInternational3DataInitiationCreditorAgent(OBBranchAndFinancialInstitutionIdentification3 creditorAgent) {
        return (new OBWriteInternational3DataInitiationCreditorAgent())
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(creditorAgent.getPostalAddress());
    }

    public static OBWriteInternationalStandingOrder4DataInitiationCreditorAgent toOBWriteInternationalStandingOrder4DataInitiationCreditorAgent(OBBranchAndFinancialInstitutionIdentification6 creditorAgent) {
        return (new OBWriteInternationalStandingOrder4DataInitiationCreditorAgent())
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(creditorAgent.getPostalAddress());
    }

    public static OBWriteInternational3DataInitiationCreditor toOBWriteInternational3DataInitiationCreditor(OBPartyIdentification43 creditor) {
        return (new OBWriteInternational3DataInitiationCreditor())
                .name(creditor.getName())
                .postalAddress(creditor.getPostalAddress());
    }

}
