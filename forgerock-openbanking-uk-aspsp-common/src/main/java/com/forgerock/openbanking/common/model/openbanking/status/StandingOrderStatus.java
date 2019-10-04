/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.status;

/**
 * Internal status of a standing order with regards to payment execution
 */
public enum StandingOrderStatus {
    // Waiting for first payment
    PENDING,
    // Made first payment but not yet made final payment
    ACTIVE,
    // Error in standing order prevented payment
    REJECTED,
    // Made final payment and no longer active
    COMPLETED
}
