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
package com.forgerock.openbanking.common.services.openbanking.converter.account;

import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRAccountServicer;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAgent;
import uk.org.openbanking.datamodel.account.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountPostalAddressConverter.toOBPostalAddress6;

public class FRAccountServicerConverter {

    // FR to OB
    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(FRAccountServicer creditorAgent) {
        return creditorAgent == null ? null : new OBBranchAndFinancialInstitutionIdentification2()
                .schemeName(toOBExternalFinancialInstitutionIdentification2Code(creditorAgent.getSchemeName()))
                .identification(creditorAgent.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(FRAccountIdentifier creditorAccount) {
        return creditorAccount == null ? null : new OBBranchAndFinancialInstitutionIdentification2()
                .schemeName(toOBExternalFinancialInstitutionIdentification2Code(creditorAccount.getSchemeName()))
                .identification(creditorAccount.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(FRFinancialAgent creditorAgent) {
        return creditorAgent == null ? null : new OBBranchAndFinancialInstitutionIdentification3()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toOBPostalAddress6(creditorAgent.getPostalAddress()));
    }

    public static OBBranchAndFinancialInstitutionIdentification4 toOBBranchAndFinancialInstitutionIdentification4(FRAccountServicer servicer) {
        return servicer == null ? null : new OBBranchAndFinancialInstitutionIdentification4()
                .schemeName(servicer.getSchemeName())
                .identification(servicer.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification5 toOBBranchAndFinancialInstitutionIdentification5(FRAccountServicer servicer) {
        return servicer == null ? null : new OBBranchAndFinancialInstitutionIdentification5()
                .schemeName(servicer.getSchemeName())
                .identification(servicer.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification50 toOBBranchAndFinancialInstitutionIdentification50(FRAccountServicer servicer) {
        return servicer == null ? null : new OBBranchAndFinancialInstitutionIdentification50()
                .schemeName(servicer.getSchemeName())
                .identification(servicer.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(FRFinancialAgent creditorAgent) {
        return creditorAgent == null ? null : new OBBranchAndFinancialInstitutionIdentification6()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toOBPostalAddress6(creditorAgent.getPostalAddress()));
    }

    public static OBBranchAndFinancialInstitutionIdentification60 toOBBranchAndFinancialInstitutionIdentification60(FRFinancialAgent creditorAgent) {
        return creditorAgent == null ? null : new OBBranchAndFinancialInstitutionIdentification60()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toOBPostalAddress6(creditorAgent.getPostalAddress()));
    }

    public static OBBranchAndFinancialInstitutionIdentification61 toOBBranchAndFinancialInstitutionIdentification61(FRFinancialAgent creditorAgent) {
        return creditorAgent == null ? null : new OBBranchAndFinancialInstitutionIdentification61()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toOBPostalAddress6(creditorAgent.getPostalAddress()));
    }

    public static OBBranchAndFinancialInstitutionIdentification62 toOBBranchAndFinancialInstitutionIdentification62(FRFinancialAgent creditorAgent) {
        return creditorAgent == null ? null : new OBBranchAndFinancialInstitutionIdentification62()
                .schemeName(creditorAgent.getSchemeName())
                .identification(creditorAgent.getIdentification())
                .name(creditorAgent.getName())
                .postalAddress(toOBPostalAddress6(creditorAgent.getPostalAddress()));
    }

    public static OBExternalFinancialInstitutionIdentification2Code toOBExternalFinancialInstitutionIdentification2Code(String schemeName) {
        return schemeName == null ? null : OBExternalFinancialInstitutionIdentification2Code.valueOf(schemeName);
    }

    // OB to FR
    public static FRAccountServicer toFRAccountServicer(OBBranchAndFinancialInstitutionIdentification5 servicer) {
        return servicer == null ? null : FRAccountServicer.builder()
                .schemeName(servicer.getSchemeName())
                .identification(servicer.getIdentification())
                .build();
    }

    public static FRAccountServicer toFRAccountServicer(OBBranchAndFinancialInstitutionIdentification50 servicer) {
        return servicer == null ? null : FRAccountServicer.builder()
                .schemeName(servicer.getSchemeName())
                .identification(servicer.getIdentification())
                .build();
    }
}
