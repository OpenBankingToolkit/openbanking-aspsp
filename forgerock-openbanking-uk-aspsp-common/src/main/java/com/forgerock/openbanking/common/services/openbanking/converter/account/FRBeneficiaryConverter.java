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

import com.forgerock.openbanking.common.model.openbanking.persistence.account.v1_1.FRBeneficiary1;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_1.FRBeneficiary3;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_3.FRBeneficiary4;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_5.FRBeneficiary5;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.OBBeneficiary1;
import uk.org.openbanking.datamodel.account.OBBeneficiary3;
import uk.org.openbanking.datamodel.account.OBBeneficiary5;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification2;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification60;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.OBCashAccountConverter.toOBCashAccount1;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.OBCashAccountConverter.toOBCashAccount50;

@Service
public class FRBeneficiaryConverter {


    public static FRBeneficiary1 toBeneficiary1(FRBeneficiary4 frBeneficiary4) {
        if (frBeneficiary4 == null) return null;
        FRBeneficiary1 frBeneficiary1 = new FRBeneficiary1();
        frBeneficiary1.setAccountId(frBeneficiary4.getAccountId());
        frBeneficiary1.setBeneficiary(OBBeneficiaryConverter.toOBBeneficiary1(frBeneficiary4.getBeneficiary()));
        frBeneficiary1.setId(frBeneficiary4.getId());
        frBeneficiary1.setCreated(frBeneficiary4.getCreated());
        frBeneficiary1.setUpdated(frBeneficiary4.getUpdated());
        return frBeneficiary1;
    }

    public static FRBeneficiary1 toBeneficiary1(FRBeneficiary5 frBeneficiary5) {
        if (frBeneficiary5 == null) return null;
        FRBeneficiary1 frBeneficiary1 = new FRBeneficiary1();
        frBeneficiary1.setAccountId(frBeneficiary5.getAccountId());
        frBeneficiary1.setBeneficiary(toOBBeneficiary1(frBeneficiary5.getBeneficiary()));
        frBeneficiary1.setId(frBeneficiary5.getId());
        frBeneficiary1.setCreated(frBeneficiary5.getCreated());
        frBeneficiary1.setUpdated(frBeneficiary5.getUpdated());
        return frBeneficiary1;
    }

    public static FRBeneficiary5 toFRBeneficiary5(FRBeneficiary3 frBeneficiary3) {
        if (frBeneficiary3 == null) return null;
        FRBeneficiary5 frBeneficiary5 = new FRBeneficiary5();
        frBeneficiary5.setAccountId(frBeneficiary3.getAccountId());
        frBeneficiary5.setBeneficiary(toOBBeneficiary5(frBeneficiary3.getBeneficiary()));
        frBeneficiary5.setId(frBeneficiary3.getId());
        frBeneficiary5.setCreated(frBeneficiary3.getCreated());
        frBeneficiary5.setUpdated(frBeneficiary3.getUpdated());
        return frBeneficiary5;
    }

    public static OBBeneficiary1 toOBBeneficiary1(OBBeneficiary5 obBeneficiary5) {
        return obBeneficiary5 == null ? null : (new OBBeneficiary1())
                .accountId(obBeneficiary5.getAccountId())
                .beneficiaryId(obBeneficiary5.getBeneficiaryId())
                .reference(obBeneficiary5.getReference())
                .servicer(toOBBranchAndFinancialInstitutionIdentification2(obBeneficiary5.getCreditorAgent()))
                .creditorAccount(toOBCashAccount1(obBeneficiary5.getCreditorAccount()));
    }

    public static OBBeneficiary5 toOBBeneficiary5(OBBeneficiary3 obBeneficiary3) {
        return obBeneficiary3 == null ? null : (new OBBeneficiary5())
                .accountId(obBeneficiary3.getAccountId())
                .beneficiaryId(obBeneficiary3.getBeneficiaryId())
                .beneficiaryType(null) // TODO #279 - should this be defaulted?
                .reference(obBeneficiary3.getReference())
                .supplementaryData(null)
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification60(obBeneficiary3.getCreditorAgent()))
                .creditorAccount(toOBCashAccount50(obBeneficiary3.getCreditorAccount()));
    }

}
