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

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBeneficiary1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRBeneficiary2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRBeneficiary3;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRBeneficiary4;
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

    public static FRBeneficiary1 toBeneficiary1(FRBeneficiary4 frBeneficiary4) {
        if (frBeneficiary4==null) return null;
        FRBeneficiary1 frBeneficiary1 =  new FRBeneficiary1();
        frBeneficiary1.setAccountId(frBeneficiary4.getAccountId());
        frBeneficiary1.setBeneficiary(OBBeneficiaryConverter.toOBBeneficiary1(frBeneficiary4.getBeneficiary()));
        frBeneficiary1.setId(frBeneficiary4.getId());
        frBeneficiary1.setCreated(frBeneficiary4.getCreated());
        frBeneficiary1.setUpdated(frBeneficiary4.getUpdated());
        return frBeneficiary1;
    }
}
