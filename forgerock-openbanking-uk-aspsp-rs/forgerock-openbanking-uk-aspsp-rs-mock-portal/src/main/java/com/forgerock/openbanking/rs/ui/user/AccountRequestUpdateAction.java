/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.rs.ui.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum AccountRequestUpdateAction {

    REVOKE("revoke"),
    AUTHORISED("Authorised"),
    SAVE("save");

    private String value;

    AccountRequestUpdateAction(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static AccountRequestUpdateAction fromValue(String text) {
        for (AccountRequestUpdateAction b : AccountRequestUpdateAction.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unknown action '" + text + "'");
    }
}
