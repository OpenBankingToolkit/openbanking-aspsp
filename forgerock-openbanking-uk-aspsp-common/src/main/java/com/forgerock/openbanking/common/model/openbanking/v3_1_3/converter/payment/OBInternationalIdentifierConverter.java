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
import uk.org.openbanking.datamodel.payment.OBBranchAndFinancialInstitutionIdentification6;
import uk.org.openbanking.datamodel.payment.OBPartyIdentification43;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3DataInitiationCreditor;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3DataInitiationCreditorAgent;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4DataInitiationCreditorAgent;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.ConverterHelper.copyField;

public class OBInternationalIdentifierConverter {

    public static OBWriteInternational3DataInitiationCreditorAgent toOBWriteInternational3DataInitiationCreditorAgent(OBBranchAndFinancialInstitutionIdentification3 creditorAgent) {
        return toAgent(new OBWriteInternational3DataInitiationCreditorAgent(), creditorAgent);
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBWriteInternational3DataInitiationCreditorAgent creditorAgent) {
        return toAgent(new OBBranchAndFinancialInstitutionIdentification3(), creditorAgent);
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBWriteInternationalStandingOrder4DataInitiationCreditorAgent creditorAgent) {
        return toAgent(new OBBranchAndFinancialInstitutionIdentification3(), creditorAgent);
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(OBWriteInternationalStandingOrder4DataInitiationCreditorAgent creditorAgent) {
        return toAgent(new OBBranchAndFinancialInstitutionIdentification6(), creditorAgent);
    }

    public static OBWriteInternationalStandingOrder4DataInitiationCreditorAgent toOBWriteInternationalStandingOrder4DataInitiationCreditorAgent(OBBranchAndFinancialInstitutionIdentification3 creditorAgent) {
        return toAgent(new OBWriteInternationalStandingOrder4DataInitiationCreditorAgent(), creditorAgent);
    }

    public static OBWriteInternationalStandingOrder4DataInitiationCreditorAgent toOBWriteInternationalStandingOrder4DataInitiationCreditorAgent(OBBranchAndFinancialInstitutionIdentification6 creditorAgent) {
        return toAgent(new OBWriteInternationalStandingOrder4DataInitiationCreditorAgent(), creditorAgent);
    }

    public static OBWriteInternational3DataInitiationCreditor toOBWriteInternational3DataInitiationCreditor(OBPartyIdentification43 creditor) {
        return toCreditor(new OBWriteInternational3DataInitiationCreditor(), creditor);
    }

    public static OBPartyIdentification43 toOBPartyIdentification43(OBWriteInternational3DataInitiationCreditor creditor) {
        return toCreditor(new OBPartyIdentification43(), creditor);
    }

    private static <T, U> T toAgent(T newAgent, U originalAgent) {
        if (originalAgent == null) {
            return null;
        }
        copyField(newAgent, originalAgent, "schemeName");
        copyField(newAgent, originalAgent, "identification");
        copyField(newAgent, originalAgent, "name");
        copyField(newAgent, originalAgent, "postalAddress");
        return newAgent;
    }

    private static <T, U> T toCreditor(T newCreditor, U originalCreditor) {
        if (originalCreditor == null) {
            return null;
        }
        copyField(newCreditor, originalCreditor, "name");
        copyField(newCreditor, originalCreditor, "postalAddress");
        return newCreditor;
    }
}
