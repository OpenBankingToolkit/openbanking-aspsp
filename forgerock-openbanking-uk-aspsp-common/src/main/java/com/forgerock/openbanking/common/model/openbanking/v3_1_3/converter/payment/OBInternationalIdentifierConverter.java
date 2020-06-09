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

import uk.org.openbanking.datamodel.payment.OBBranchAndFinancialInstitutionIdentification3;
import uk.org.openbanking.datamodel.payment.OBPartyIdentification43;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3DataInitiationCreditor;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3DataInitiationCreditorAgent;

public class OBInternationalIdentifierConverter {

    public static OBWriteInternational3DataInitiationCreditorAgent toOBWriteInternational3DataInitiationCreditorAgent(OBBranchAndFinancialInstitutionIdentification3 creditorAgent) {
        return creditorAgent == null ? null : (new OBWriteInternational3DataInitiationCreditorAgent())
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(creditorAgent.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBWriteInternational3DataInitiationCreditorAgent creditorAgent) {
        return creditorAgent == null ? null : (new OBBranchAndFinancialInstitutionIdentification3())
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(creditorAgent.getPostalAddress());
    }

    public static OBWriteInternational3DataInitiationCreditor toOBWriteInternational3DataInitiationCreditor(OBPartyIdentification43 creditor) {
        return creditor == null ? null : (new OBWriteInternational3DataInitiationCreditor())
                .name(creditor.getName())
                .postalAddress(creditor.getPostalAddress());
    }

    public static OBPartyIdentification43 toOBPartyIdentification43(OBWriteInternational3DataInitiationCreditor creditor) {
        return creditor == null ? null : (new OBPartyIdentification43()
                .name(creditor.getName())
                .postalAddress(creditor.getPostalAddress()));
    }
}
