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

import com.forgerock.openbanking.common.model.openbanking.domain.account.FRAccountBeneficiary;
import uk.org.openbanking.datamodel.account.OBBeneficiary1;
import uk.org.openbanking.datamodel.account.OBBeneficiary2;
import uk.org.openbanking.datamodel.account.OBBeneficiary3;
import uk.org.openbanking.datamodel.account.OBBeneficiary4;
import uk.org.openbanking.datamodel.account.OBBeneficiary5;
import uk.org.openbanking.datamodel.account.OBBeneficiaryType1Code;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification2;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification3;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification6;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification60;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountSupplementaryDataConverter.toFRSupplementaryData;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountSupplementaryDataConverter.toOBSupplementaryData1;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toFRAccountIdentifier;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount1;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount5;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount50;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRFinancialInstrumentConverter.toFRFinancialAgent;

public class FRAccountBeneficiaryConverter {

    // FR to OB
    public static OBBeneficiary1 toOBBeneficiary1(FRAccountBeneficiary beneficiary) {
        return beneficiary == null ? null : new OBBeneficiary1()
                .accountId(beneficiary.getAccountId())
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .reference(beneficiary.getReference())
                .servicer(toOBBranchAndFinancialInstitutionIdentification2(beneficiary.getCreditorAccount()))
                .creditorAccount(toOBCashAccount1(beneficiary.getCreditorAccount()));
    }

    public static OBBeneficiary2 toOBBeneficiary2(FRAccountBeneficiary beneficiary) {
        return beneficiary == null ? null : new OBBeneficiary2()
                .accountId(beneficiary.getAccountId())
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .reference(beneficiary.getReference())
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(beneficiary.getCreditorAgent()))
                .creditorAccount(toOBCashAccount3(beneficiary.getCreditorAccount()));
    }

    public static OBBeneficiary3 toOBBeneficiary3(FRAccountBeneficiary beneficiary) {
        return beneficiary == null ? null : new OBBeneficiary3()
                .accountId(beneficiary.getAccountId())
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .reference(beneficiary.getReference())
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(beneficiary.getCreditorAgent()))
                .creditorAccount(toOBCashAccount5(beneficiary.getCreditorAccount()));
    }

    public static OBBeneficiary4 toOBBeneficiary4(FRAccountBeneficiary beneficiary) {
        return beneficiary == null ? null : new OBBeneficiary4()
                .accountId(beneficiary.getAccountId())
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .reference(beneficiary.getReference())
                .supplementaryData(toOBSupplementaryData1(beneficiary.getSupplementaryData()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification60(beneficiary.getCreditorAgent()))
                .creditorAccount(toOBCashAccount50(beneficiary.getCreditorAccount()));
    }

    public static OBBeneficiary5 toOBBeneficiary5(FRAccountBeneficiary beneficiary) {
        return beneficiary == null ? null : new OBBeneficiary5()
                .accountId(beneficiary.getAccountId())
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .beneficiaryType(toOBBeneficiaryType1Code(beneficiary.getBeneficiaryType()))
                .reference(beneficiary.getReference())
                .supplementaryData(toOBSupplementaryData1(beneficiary.getSupplementaryData()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification60(beneficiary.getCreditorAgent()))
                .creditorAccount(toOBCashAccount50(beneficiary.getCreditorAccount()));
    }

    public static OBBeneficiaryType1Code toOBBeneficiaryType1Code(FRAccountBeneficiary.FRBeneficiaryType beneficiaryType) {
        return beneficiaryType == null ? null : OBBeneficiaryType1Code.valueOf(beneficiaryType.name());
    }

    // OB to FR
    public static FRAccountBeneficiary toFRAccountBeneficiary(OBBeneficiary3 beneficiary) {
        return beneficiary == null ? null : FRAccountBeneficiary.builder()
                .accountId(beneficiary.getAccountId())
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .reference(beneficiary.getReference())
                .creditorAgent(toFRFinancialAgent(beneficiary.getCreditorAgent()))
                .creditorAccount(toFRAccountIdentifier(beneficiary.getCreditorAccount()))
                .build();
    }

    public static FRAccountBeneficiary toFRAccountBeneficiary(OBBeneficiary5 beneficiary) {
        return beneficiary == null ? null : FRAccountBeneficiary.builder()
                .accountId(beneficiary.getAccountId())
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .beneficiaryType(toFRBeneficiaryType(beneficiary.getBeneficiaryType()))
                .reference(beneficiary.getReference())
                .supplementaryData(toFRSupplementaryData(beneficiary.getSupplementaryData()))
                .creditorAgent(toFRFinancialAgent(beneficiary.getCreditorAgent()))
                .creditorAccount(toFRAccountIdentifier(beneficiary.getCreditorAccount()))
                .build();
    }

    public static FRAccountBeneficiary.FRBeneficiaryType toFRBeneficiaryType(OBBeneficiaryType1Code beneficiaryType) {
        return beneficiaryType == null ? null : FRAccountBeneficiary.FRBeneficiaryType.valueOf(beneficiaryType.name());
    }

}
