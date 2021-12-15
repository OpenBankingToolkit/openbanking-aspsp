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
package com.forgerock.openbanking.common.model.openbanking;


import com.forgerock.openbanking.api.annotations.OBGroupName;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public enum IntentType {

    ACCOUNT_REQUEST("AR_", OBGroupName.AISP),
    PAYMENT_SINGLE_REQUEST("PR_", OBGroupName.PISP),

    ACCOUNT_ACCESS_CONSENT("AAC_", OBGroupName.AISP),

    PAYMENT_DOMESTIC_CONSENT("PDC_", OBGroupName.PISP),
    PAYMENT_DOMESTIC_SCHEDULED_CONSENT("PDSC_", OBGroupName.PISP),
    PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT("PDSOC_", OBGroupName.PISP),
    PAYMENT_INTERNATIONAL_CONSENT("PIC_", OBGroupName.PISP),
    PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT("PISC_", OBGroupName.PISP),
    PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT("PISOC_", OBGroupName.PISP),
    PAYMENT_FILE_CONSENT("PFC_", OBGroupName.PISP),
    FUNDS_CONFIRMATION_CONSENT("FCC_", OBGroupName.CBPII),
    DOMESTIC_VRP_PAYMENT_CONSENT("DVRP_", OBGroupName.PISP),
    CUSTOMER_INFO_CONSENT("CIC", OBGroupName.INFO),

    @Deprecated
    PAYMENT_REQUEST_LEGACY("P", OBGroupName.PISP),
    @Deprecated
    ACCOUNT_REQUEST_LEGACY("A", OBGroupName.AISP);

    private String intentIdPrefix;
    private OBGroupName obGroupName;

    IntentType(String intentIdPrefix, OBGroupName obGroupName) {
        this.intentIdPrefix = intentIdPrefix;
        this.obGroupName = obGroupName;
    }

    public static IntentType identify(String intentId) {
        for(IntentType type : IntentType.values()) {
            if (intentId.startsWith(type.intentIdPrefix)) {
                //calling getType to handle legacy types
                return type.getType();
            }
        }
        return null;
    }

    public OBGroupName getObGroupName() {
        return obGroupName;
    }

    public String generateIntentId() {
        String intentId = intentIdPrefix + UUID.randomUUID().toString();
        return intentId.substring(0, Math.min(intentId.length(), 40));
    }

    private IntentType getType() {
        //For legacy reason, we convert the old account and payment request into the new model
        switch (this) {
            case PAYMENT_REQUEST_LEGACY:
                return PAYMENT_SINGLE_REQUEST;
            case ACCOUNT_REQUEST_LEGACY:
                return ACCOUNT_REQUEST;
            default:
                return this;
        }
    }

    public static List<IntentType> byOBGroupeName(OBGroupName obGroupName) {
        return Arrays.asList(IntentType.values()).stream().filter(i -> i.obGroupName == obGroupName).collect(Collectors.toList());
    }
}
