/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.payment;


import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;

/**
 * An internal Payment service
 * @param <T> A type of payment consent
 */
public interface PaymentService<T extends FRPaymentConsent> {

    /**
     * Get payment consent by id
     * @param paymentId Payment id
     * @return Payment consent
     */
    T getPayment(String paymentId);

    /**
     * Update payment consent
     * @param payment Payment consent
     */
    void updatePayment(T payment);

}
