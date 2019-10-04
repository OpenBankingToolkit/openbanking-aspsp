/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.v1_1.old;

import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.forgerock.openbanking.common.constants.RCSConstants.Claims.PAYMENT_ID;


@Service
public class PaymentId {

    public String create() {
        return PAYMENT_ID + UUID.randomUUID().toString();
    }

}
