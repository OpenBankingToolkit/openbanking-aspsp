/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.status;

/** Records if this scheduled payment has been processed or not. Not all scheduled payments have an associated payment consent (e.g. created on /data API). */
public enum ScheduledPaymentStatus {
        PENDING, COMPLETED, REJECTED
}
