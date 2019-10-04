/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_1.payment;

import java.util.Date;

public interface FRPaymentSubmission {
    String getId();
    Date getCreated();
    default String getIdempotencyKey() {
        return null; // Not used on V1.1, V2.0
    }
}
