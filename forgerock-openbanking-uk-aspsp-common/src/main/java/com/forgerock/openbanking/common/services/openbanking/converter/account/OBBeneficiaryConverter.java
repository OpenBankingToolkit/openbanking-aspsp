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

public final class OBBeneficiaryConverter {

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
            obBeneficiary2.creditorAgent(OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification3(obBeneficiary3.getCreditorAgent()));
        }

        if (obBeneficiary3.getCreditorAccount() != null) {
            obBeneficiary2.creditorAccount(OBCashAccountConverter.toOBCashAccount3(obBeneficiary3.getCreditorAccount()));
        }

        return obBeneficiary2;
    }

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
            obBeneficiary1.servicer(OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification2(obBeneficiary3.getCreditorAgent()));
        }

        if (obBeneficiary3.getCreditorAccount() != null) {
            obBeneficiary1.creditorAccount(OBCashAccountConverter.toOBCashAccount1(obBeneficiary3.getCreditorAccount()));
        }

        return obBeneficiary1;
    }

    public static OBBeneficiary3 toOBBeneficiary3(OBBeneficiary2 obBeneficiary2) {
        if (obBeneficiary2==null) return null;
        OBBeneficiary3 obBeneficiary3 = new OBBeneficiary3();

        obBeneficiary3.setAccountId(obBeneficiary2.getAccountId());
        obBeneficiary3.setBeneficiaryId(obBeneficiary2.getBeneficiaryId());
        obBeneficiary3.reference(obBeneficiary2.getReference());

        obBeneficiary3.creditorAgent(OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification6(obBeneficiary2.getCreditorAgent()));
        obBeneficiary3.creditorAccount(OBCashAccountConverter.toOBCashAccount5(obBeneficiary2.getCreditorAccount()));
        return obBeneficiary3;
    }



}
