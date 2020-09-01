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

import uk.org.openbanking.datamodel.account.*;

public final class OBBranchAndFinancialInstitutionIdentificationConverter {

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification4 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification2())
                .schemeName(institutionIdentification.getSchemeName() == null ? null : OBExternalFinancialInstitutionIdentification2Code.BICFI)
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification5 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification2())
                .schemeName(institutionIdentification.getSchemeName() == null ? null : OBExternalFinancialInstitutionIdentification2Code.BICFI)
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification6 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification2())
                .schemeName(institutionIdentification.getSchemeName() == null ? null : OBExternalFinancialInstitutionIdentification2Code.BICFI)
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification50 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification2())
                .schemeName(institutionIdentification.getSchemeName() == null ? null : OBExternalFinancialInstitutionIdentification2Code.BICFI)
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification51 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification2())
                .schemeName(institutionIdentification.getSchemeName() == null ? null : OBExternalFinancialInstitutionIdentification2Code.BICFI)
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification60 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification2())
                .schemeName(institutionIdentification.getSchemeName() == null ? null : OBExternalFinancialInstitutionIdentification2Code.BICFI)
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification61 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification2())
                .schemeName(institutionIdentification.getSchemeName() == null ? null : OBExternalFinancialInstitutionIdentification2Code.BICFI)
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification62 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification2())
                .schemeName(institutionIdentification.getSchemeName() == null ? null : OBExternalFinancialInstitutionIdentification2Code.BICFI)
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBCashAccount61 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification2())
                .schemeName(institutionIdentification.getSchemeName() == null ? null : OBExternalFinancialInstitutionIdentification2Code.BICFI)
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBBranchAndFinancialInstitutionIdentification6 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification3())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification())
                .name(institutionIdentification.getName())
                .postalAddress(institutionIdentification.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBBranchAndFinancialInstitutionIdentification60 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification3())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification())
                .name(institutionIdentification.getName())
                .postalAddress(institutionIdentification.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBBranchAndFinancialInstitutionIdentification61 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification3())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification())
                .name(institutionIdentification.getName())
                .postalAddress(institutionIdentification.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBBranchAndFinancialInstitutionIdentification62 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification3())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification())
                .name(institutionIdentification.getName())
                .postalAddress(institutionIdentification.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification4 toOBBranchAndFinancialInstitutionIdentification4(OBBranchAndFinancialInstitutionIdentification51 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification4())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification4 toOBBranchAndFinancialInstitutionIdentification4(OBBranchAndFinancialInstitutionIdentification5 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification4())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification4 toOBBranchAndFinancialInstitutionIdentification4(OBBranchAndFinancialInstitutionIdentification50 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification4())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification5 toOBBranchAndFinancialInstitutionIdentification5(OBBranchAndFinancialInstitutionIdentification50 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification5())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification5 toOBBranchAndFinancialInstitutionIdentification5(OBBranchAndFinancialInstitutionIdentification4 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification5())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification5 toOBBranchAndFinancialInstitutionIdentification5(OBBranchAndFinancialInstitutionIdentification51 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification5())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(OBBranchAndFinancialInstitutionIdentification3 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification6())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification())
                .name(institutionIdentification.getName())
                .postalAddress(institutionIdentification.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(OBBranchAndFinancialInstitutionIdentification60 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification6())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification())
                .name(institutionIdentification.getName())
                .postalAddress(institutionIdentification.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(OBBranchAndFinancialInstitutionIdentification61 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification6())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification())
                .name(institutionIdentification.getName())
                .postalAddress(institutionIdentification.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(OBBranchAndFinancialInstitutionIdentification62 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification6())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification())
                .name(institutionIdentification.getName())
                .postalAddress(institutionIdentification.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification50 toOBBranchAndFinancialInstitutionIdentification50(OBBranchAndFinancialInstitutionIdentification5 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification50())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification51 toOBBranchAndFinancialInstitutionIdentification51(OBBranchAndFinancialInstitutionIdentification4 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification51())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification51 toOBBranchAndFinancialInstitutionIdentification51(OBBranchAndFinancialInstitutionIdentification5 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification51())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification60 toOBBranchAndFinancialInstitutionIdentification60(OBBranchAndFinancialInstitutionIdentification6 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification60())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification())
                .name(institutionIdentification.getName())
                .postalAddress(institutionIdentification.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification61 toOBBranchAndFinancialInstitutionIdentification61(OBBranchAndFinancialInstitutionIdentification6 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification61())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification())
                .name(institutionIdentification.getName())
                .postalAddress(institutionIdentification.getPostalAddress());
    }

    public static OBBranchAndFinancialInstitutionIdentification62 toOBBranchAndFinancialInstitutionIdentification62(OBBranchAndFinancialInstitutionIdentification6 institutionIdentification) {
        return institutionIdentification == null ? null : (new OBBranchAndFinancialInstitutionIdentification62())
                .schemeName(institutionIdentification.getSchemeName())
                .identification(institutionIdentification.getIdentification())
                .name(institutionIdentification.getName())
                .postalAddress(institutionIdentification.getPostalAddress());
    }
}
