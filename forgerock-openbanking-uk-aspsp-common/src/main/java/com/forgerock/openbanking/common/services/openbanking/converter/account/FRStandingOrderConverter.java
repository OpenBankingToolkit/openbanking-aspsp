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

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRStandingOrder1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRStandingOrder2;
import com.forgerock.openbanking.common.model.openbanking.v3_0.account.FRStandingOrder3;
import com.forgerock.openbanking.common.model.openbanking.v3_1.account.FRStandingOrder4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRStandingOrder5;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.account.FRStandingOrder6;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBStandingOrderConverter.toOBStandingOrder1;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBStandingOrderConverter.toOBStandingOrder2;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBStandingOrderConverter.toOBStandingOrder3;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBStandingOrderConverter.toOBStandingOrder4;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBStandingOrderConverter.toOBStandingOrder6;

public class FRStandingOrderConverter {

    public static FRStandingOrder1 toStandingOrder1(FRStandingOrder6 frStandingOrder6) {
        FRStandingOrder1 standingOrder1 = new FRStandingOrder1();
        standingOrder1.setId(frStandingOrder6.getId());
        standingOrder1.setAccountId(frStandingOrder6.getAccountId());
        standingOrder1.setStandingOrder(toOBStandingOrder1(frStandingOrder6.getStandingOrder()));
        standingOrder1.setCreated(frStandingOrder6.getCreated());
        standingOrder1.setUpdated(frStandingOrder6.getUpdated());
        return standingOrder1;
    }

    public static FRStandingOrder2 toStandingOrder2(FRStandingOrder4 standingOrder4) {
        FRStandingOrder2 standingOrder2 = new FRStandingOrder2();
        standingOrder2.setAccountId(standingOrder4.getAccountId());
        standingOrder2.setId(standingOrder4.getId());
        standingOrder2.setCreated(standingOrder4.getCreated());
        standingOrder2.setUpdated(standingOrder4.getUpdated());
        standingOrder2.setStandingOrder(toOBStandingOrder2(standingOrder4.getStandingOrder()));
        return standingOrder2;
    }

    public static FRStandingOrder2 toStandingOrder2(FRStandingOrder6 standingOrder6) {
        return toStandingOrder2(toStandingOrder4(standingOrder6));
    }

    public static FRStandingOrder3 toStandingOrder3(FRStandingOrder6 standingOrder6) {
        FRStandingOrder3 standingOrder3 = new FRStandingOrder3();
        standingOrder3.setAccountId(standingOrder6.getAccountId());
        standingOrder3.setId(standingOrder6.getId());
        standingOrder3.setCreated(standingOrder6.getCreated());
        standingOrder3.setUpdated(standingOrder6.getUpdated());
        standingOrder3.setStandingOrder(toOBStandingOrder3(standingOrder6.getStandingOrder()));
        return standingOrder3;
    }

    public static FRStandingOrder4 toStandingOrder4(FRStandingOrder6 standingOrder6) {
        FRStandingOrder4 standingOrder4 = new FRStandingOrder4();
        standingOrder4.setAccountId(standingOrder6.getAccountId());
        standingOrder4.setId(standingOrder6.getId());
        standingOrder4.setCreated(standingOrder6.getCreated());
        standingOrder4.setUpdated(standingOrder6.getUpdated());
        standingOrder4.setStandingOrder(toOBStandingOrder4(standingOrder6.getStandingOrder()));
        return standingOrder4;
    }

    public static FRStandingOrder6 toStandingOrder6(FRStandingOrder4 frStandingOrder4) {
        FRStandingOrder6 standingOrder6 = new FRStandingOrder6();
        standingOrder6.setAccountId(frStandingOrder4.getAccountId());
        standingOrder6.setId(frStandingOrder4.getId());
        standingOrder6.setCreated(frStandingOrder4.getCreated());
        standingOrder6.setUpdated(frStandingOrder4.getUpdated());
        standingOrder6.setStandingOrder(toOBStandingOrder6(frStandingOrder4.getStandingOrder()));
        return standingOrder6;
    }

    public static FRStandingOrder6 toFRStandingOrder6(FRStandingOrder5 frStandingOrder5) {
        FRStandingOrder6 standingOrder6 = new FRStandingOrder6();
        standingOrder6.setAccountId(frStandingOrder5.getAccountId());
        standingOrder6.setId(frStandingOrder5.getId());
        standingOrder6.setCreated(frStandingOrder5.getCreated());
        standingOrder6.setUpdated(frStandingOrder5.getUpdated());
        standingOrder6.setStandingOrder(toOBStandingOrder6(frStandingOrder5.getStandingOrder()));
        return standingOrder6;
    }
}
