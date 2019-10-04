/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0;

import org.springframework.http.MediaType;

import java.util.Arrays;

public enum PaymentFileType {
    UK_OBIE_PAYMENT_INITIATION_V3_0("UK.OBIE.PaymentInitiation.3.0", MediaType.APPLICATION_JSON),

    // Note: This enum is also used for 3.1.1 and 3.1.2 in OB spec
    UK_OBIE_PAYMENT_INITIATION_V3_1("UK.OBIE.PaymentInitiation.3.1", MediaType.APPLICATION_JSON),
    UK_OBIE_PAIN_001("UK.OBIE.pain.001.001.08", MediaType.TEXT_XML);

    private final String fileType;
    private final MediaType contentType;

    private PaymentFileType(String fileType, MediaType contentType) {
        this.fileType = fileType;
        this.contentType = contentType;
    }

    public String getFileType() {
        return fileType;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public boolean isSupported(String fileType) {
        return Arrays.stream(PaymentFileType.values())
                .anyMatch(e -> e.fileType.equals(fileType));
    }

    public static PaymentFileType fromFileType(String value) {
        for(PaymentFileType paymentFileType: PaymentFileType.values()) {
            if (paymentFileType.fileType.equals(value)) {
                return paymentFileType;
            }
        }
        throw new UnsupportedOperationException("Unsupported payment file type: '" + value + "'");
    }
}
