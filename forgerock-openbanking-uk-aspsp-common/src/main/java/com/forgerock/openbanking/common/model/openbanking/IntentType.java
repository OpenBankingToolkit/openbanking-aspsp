/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking;


import com.forgerock.openbanking.common.openbanking.OBGroupName;

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
