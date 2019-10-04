/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking.converter;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBeneficiary1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRBeneficiary2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRBeneficiary3;
import org.springframework.stereotype.Service;

@Service
public class FRBeneficiaryConverter {

    public static FRBeneficiary3 toBeneficiary3(FRBeneficiary2 frBeneficiary2) {
        if (frBeneficiary2==null) return null;
        FRBeneficiary3 frBeneficiary3 =  new FRBeneficiary3();
        frBeneficiary3.setAccountId(frBeneficiary2.getAccountId());
        frBeneficiary3.setBeneficiary(OBBeneficiaryConverter.toOBBeneficiary3(frBeneficiary2.getBeneficiary()));
        frBeneficiary3.setId(frBeneficiary2.getId());
        frBeneficiary3.setCreated(frBeneficiary2.getCreated());
        frBeneficiary3.setUpdated(frBeneficiary2.getUpdated());
        return frBeneficiary3;
    }

    public static FRBeneficiary1 toBeneficiary1(FRBeneficiary3 frBeneficiary3) {
        if (frBeneficiary3==null) return null;
        FRBeneficiary1 frBeneficiary1 =  new FRBeneficiary1();
        frBeneficiary1.setAccountId(frBeneficiary3.getAccountId());
        frBeneficiary1.setBeneficiary(OBBeneficiaryConverter.toOBBeneficiary1(frBeneficiary3.getBeneficiary()));
        frBeneficiary1.setId(frBeneficiary3.getId());
        frBeneficiary1.setCreated(frBeneficiary3.getCreated());
        frBeneficiary1.setUpdated(frBeneficiary3.getUpdated());
        return frBeneficiary1;
    }

}
