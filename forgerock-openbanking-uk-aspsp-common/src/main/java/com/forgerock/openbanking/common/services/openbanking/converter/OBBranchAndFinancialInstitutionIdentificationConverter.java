/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking.converter;

import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification2;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification3;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification6;
import uk.org.openbanking.datamodel.account.OBExternalFinancialInstitutionIdentification2Code;

public final class OBBranchAndFinancialInstitutionIdentificationConverter {

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(OBBranchAndFinancialInstitutionIdentification3 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification6.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification6 institutionIdentification) {
        if (institutionIdentification==null) return null;
        return new OBBranchAndFinancialInstitutionIdentification2()
                .identification(institutionIdentification.getIdentification())
                .schemeName(OBExternalFinancialInstitutionIdentification2Code.fromValue(institutionIdentification.getSchemeName()));
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBBranchAndFinancialInstitutionIdentification6 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification3.class);
    }
}
