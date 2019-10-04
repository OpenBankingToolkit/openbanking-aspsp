/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.constants;

/**
 * All the constants used by the Remote Consent Service
 */
public class RCSConstants {
    public static class Claims {
        public static final String PAYMENT_ID = "payment_id";
        public static final String CSRF = "csrf";
        public static final String SCOPES = "scopes";
        public static final String CLIENT_ID = "clientId";
    }

    public static class Decision {
        public static final String ALLOW = "allow";
        public static final String DENY = "deny";
    }

    public static class Parameters {
        public static final String CONSENT_RESPONSE = "consent_response";
    }
}
