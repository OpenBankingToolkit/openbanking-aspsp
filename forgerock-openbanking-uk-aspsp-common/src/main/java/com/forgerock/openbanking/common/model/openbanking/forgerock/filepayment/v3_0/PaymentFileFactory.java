/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0;

import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_1.OBIEPaymentInitiationFile31;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.google.common.base.Preconditions;

public class PaymentFileFactory {

    /**
     * Get a payment file for the supplied type and content
     * @param paymentFileType File type
     * @param fileContent File content as a string
     * @return Payment file
     * @throws OBErrorException File contents not valid or did not match type
     */
    public static PaymentFile createPaymentFile(PaymentFileType paymentFileType, String fileContent) throws OBErrorException {
        Preconditions.checkNotNull(paymentFileType, "Cannot have a null file type");
        switch (paymentFileType) {
            case UK_OBIE_PAIN_001:
                return new OBIEPain001File(fileContent);
            case UK_OBIE_PAYMENT_INITIATION_V3_0:
                return new OBIEPaymentInitiationFile3(fileContent);
            case UK_OBIE_PAYMENT_INITIATION_V3_1:
                return new OBIEPaymentInitiationFile31(fileContent);
            default:
                throw new IllegalArgumentException("Unsupported payment file type:"+paymentFileType);
        }
    }

}
