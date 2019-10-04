/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.openbanking;

public enum OBGroupName {

    AISP("aisp"),
    PISP("pisp"),
    CBPII("cbpii"),
    EVENT(""),
    NONE("");

    private String reference;

    OBGroupName(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }
}
