/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

/**
 * Represents an individual single payment in a payment file.
 * This could be a domestic or international payment of single, scheduled or standing order type so will abstract the values required for payment processing so that
 * they can be processed in a generic way
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FRFilePayment {
    private String instructionIdentification;
    private String endToEndIdentification;
    private PaymentStatus status;

    private DateTime created;

    private String remittanceReference;

    private String remittanceUnstructured;

    private OBActiveOrHistoricCurrencyAndAmount instructedAmount;

    private String creditorAccountIdentification;

    /**
     * Status of an individual entry in a file payment
     */
    public enum PaymentStatus {
        PENDING, COMPLETED, REJECTED;
    }

}
