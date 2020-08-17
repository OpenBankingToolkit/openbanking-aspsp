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

import uk.org.openbanking.datamodel.account.OBBeneficiary1;
import uk.org.openbanking.datamodel.account.OBBeneficiary2;
import uk.org.openbanking.datamodel.account.OBBeneficiary3;
import uk.org.openbanking.datamodel.account.OBBeneficiary4;
import uk.org.openbanking.datamodel.account.OBBeneficiary5;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification2;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification3;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification6;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBCashAccountConverter.toOBCashAccount1;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBCashAccountConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBCashAccountConverter.toOBCashAccount5;

public final class OBBeneficiaryConverter {

    public static OBBeneficiary1 toOBBeneficiary1(OBBeneficiary3 obBeneficiary3) {
        if (obBeneficiary3==null) return null;
        OBBeneficiary1 obBeneficiary1 = new OBBeneficiary1();
        if (obBeneficiary3.getAccountId() != null) {
            obBeneficiary1.accountId(obBeneficiary3.getAccountId());
        }

        if (obBeneficiary3.getBeneficiaryId() != null) {
            obBeneficiary1.beneficiaryId(obBeneficiary3.getBeneficiaryId());
        }

        if (obBeneficiary3.getReference() != null) {
            obBeneficiary1.reference(obBeneficiary3.getReference());
        }

        if (obBeneficiary3.getCreditorAgent() != null) {
            obBeneficiary1.servicer(toOBBranchAndFinancialInstitutionIdentification2(obBeneficiary3.getCreditorAgent()));
        }

        if (obBeneficiary3.getCreditorAccount() != null) {
            obBeneficiary1.creditorAccount(toOBCashAccount1(obBeneficiary3.getCreditorAccount()));
        }

        return obBeneficiary1;
    }

    public static OBBeneficiary1 toOBBeneficiary1(OBBeneficiary4 obBeneficiary4) {
        if (obBeneficiary4==null) return null;
        OBBeneficiary1 obBeneficiary1 = new OBBeneficiary1();
        if (obBeneficiary4.getAccountId() != null) {
            obBeneficiary1.accountId(obBeneficiary4.getAccountId());
        }

        if (obBeneficiary4.getBeneficiaryId() != null) {
            obBeneficiary1.beneficiaryId(obBeneficiary4.getBeneficiaryId());
        }

        if (obBeneficiary4.getReference() != null) {
            obBeneficiary1.reference(obBeneficiary4.getReference());
        }

        if (obBeneficiary4.getCreditorAgent() != null) {
            obBeneficiary1.servicer(toOBBranchAndFinancialInstitutionIdentification2(obBeneficiary4.getCreditorAgent()));
        }

        if (obBeneficiary4.getCreditorAccount() != null) {
            obBeneficiary1.creditorAccount(toOBCashAccount1(obBeneficiary4.getCreditorAccount()));
        }

        return obBeneficiary1;
    }

    public static OBBeneficiary2 toOBBeneficiary2(OBBeneficiary3 obBeneficiary3) {
        if (obBeneficiary3==null) return null;
        OBBeneficiary2 obBeneficiary2 = new OBBeneficiary2();
        if (obBeneficiary3.getAccountId() != null) {
            obBeneficiary2.accountId(obBeneficiary3.getAccountId());
        }

        if (obBeneficiary3.getBeneficiaryId() != null) {
            obBeneficiary2.beneficiaryId(obBeneficiary3.getBeneficiaryId());
        }

        if (obBeneficiary3.getReference() != null) {
            obBeneficiary2.reference(obBeneficiary3.getReference());
        }

        if (obBeneficiary3.getCreditorAgent() != null) {
            obBeneficiary2.creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(obBeneficiary3.getCreditorAgent()));
        }

        if (obBeneficiary3.getCreditorAccount() != null) {
            obBeneficiary2.creditorAccount(toOBCashAccount3(obBeneficiary3.getCreditorAccount()));
        }

        return obBeneficiary2;
    }

    public static OBBeneficiary2 toOBBeneficiary2(OBBeneficiary4 obBeneficiary4) {
        if (obBeneficiary4==null) return null;
        OBBeneficiary2 obBeneficiary2 = new OBBeneficiary2();
        if (obBeneficiary4.getAccountId() != null) {
            obBeneficiary2.accountId(obBeneficiary4.getAccountId());
        }

        if (obBeneficiary4.getBeneficiaryId() != null) {
            obBeneficiary2.beneficiaryId(obBeneficiary4.getBeneficiaryId());
        }

        if (obBeneficiary4.getReference() != null) {
            obBeneficiary2.reference(obBeneficiary4.getReference());
        }

        if (obBeneficiary4.getCreditorAgent() != null) {
            obBeneficiary2.creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(obBeneficiary4.getCreditorAgent()));
        }

        if (obBeneficiary4.getCreditorAccount() != null) {
            obBeneficiary2.creditorAccount(toOBCashAccount3(obBeneficiary4.getCreditorAccount()));
        }

        return obBeneficiary2;
    }

    public static OBBeneficiary2 toOBBeneficiary2(OBBeneficiary5 obBeneficiary5) {
        if (obBeneficiary5==null) return null;
        OBBeneficiary2 obBeneficiary2 = new OBBeneficiary2();
        if (obBeneficiary5.getAccountId() != null) {
            obBeneficiary2.accountId(obBeneficiary5.getAccountId());
        }

        if (obBeneficiary5.getBeneficiaryId() != null) {
            obBeneficiary2.beneficiaryId(obBeneficiary5.getBeneficiaryId());
        }

        if (obBeneficiary5.getReference() != null) {
            obBeneficiary2.reference(obBeneficiary5.getReference());
        }

        if (obBeneficiary5.getCreditorAgent() != null) {
            obBeneficiary2.creditorAgent(toOBBranchAndFinancialInstitutionIdentification3(obBeneficiary5.getCreditorAgent()));
        }

        if (obBeneficiary5.getCreditorAccount() != null) {
            obBeneficiary2.creditorAccount(toOBCashAccount3(obBeneficiary5.getCreditorAccount()));
        }

        return obBeneficiary2;
    }

    public static OBBeneficiary3 toOBBeneficiary3(OBBeneficiary2 obBeneficiary2) {
        if (obBeneficiary2==null) return null;
        OBBeneficiary3 obBeneficiary3 = new OBBeneficiary3();

        obBeneficiary3.setAccountId(obBeneficiary2.getAccountId());
        obBeneficiary3.setBeneficiaryId(obBeneficiary2.getBeneficiaryId());
        obBeneficiary3.reference(obBeneficiary2.getReference());

        obBeneficiary3.creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(obBeneficiary2.getCreditorAgent()));
        obBeneficiary3.creditorAccount(toOBCashAccount5(obBeneficiary2.getCreditorAccount()));
        return obBeneficiary3;
    }

    public static OBBeneficiary3 toOBBeneficiary3(OBBeneficiary4 obBeneficiary4) {
        if (obBeneficiary4==null) return null;
        OBBeneficiary3 obBeneficiary3 = new OBBeneficiary3();

        obBeneficiary3.setAccountId(obBeneficiary4.getAccountId());
        obBeneficiary3.setBeneficiaryId(obBeneficiary4.getBeneficiaryId());
        obBeneficiary3.reference(obBeneficiary4.getReference());

        obBeneficiary3.creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(obBeneficiary4.getCreditorAgent()));
        obBeneficiary3.creditorAccount(toOBCashAccount5(obBeneficiary4.getCreditorAccount()));
        return obBeneficiary3;
    }

    public static OBBeneficiary3 toOBBeneficiary3(OBBeneficiary5 obBeneficiary5) {
        if (obBeneficiary5==null) return null;
        OBBeneficiary3 obBeneficiary3 = new OBBeneficiary3();

        obBeneficiary3.setAccountId(obBeneficiary5.getAccountId());
        obBeneficiary3.setBeneficiaryId(obBeneficiary5.getBeneficiaryId());
        obBeneficiary3.reference(obBeneficiary5.getReference());

        obBeneficiary3.creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(obBeneficiary5.getCreditorAgent()));
        obBeneficiary3.creditorAccount(toOBCashAccount5(obBeneficiary5.getCreditorAccount()));
        return obBeneficiary3;
    }

    public static OBBeneficiary4 toOBBeneficiary4(OBBeneficiary5 obBeneficiary5) {
        if (obBeneficiary5==null) return null;
        OBBeneficiary4 obBeneficiary4 = new OBBeneficiary4();

        obBeneficiary4.setAccountId(obBeneficiary5.getAccountId());
        obBeneficiary4.setBeneficiaryId(obBeneficiary5.getBeneficiaryId());
        obBeneficiary4.reference(obBeneficiary5.getReference());

        obBeneficiary4.creditorAgent(obBeneficiary5.getCreditorAgent());
        obBeneficiary4.creditorAccount(obBeneficiary5.getCreditorAccount());
        obBeneficiary4.supplementaryData(obBeneficiary5.getSupplementaryData());
        return obBeneficiary4;
    }
}
