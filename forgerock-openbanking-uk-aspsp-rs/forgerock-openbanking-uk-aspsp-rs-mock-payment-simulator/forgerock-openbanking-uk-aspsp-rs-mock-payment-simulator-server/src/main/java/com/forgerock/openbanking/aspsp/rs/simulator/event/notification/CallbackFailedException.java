/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.simulator.event.notification;

/**
 * Allow clients to handle callback failures in different ways
 */
public class CallbackFailedException extends Exception {
    CallbackFailedException(String s, Exception e) {
        super(s, e);
    }

    CallbackFailedException(String s) {
        super(s);
    }
}
