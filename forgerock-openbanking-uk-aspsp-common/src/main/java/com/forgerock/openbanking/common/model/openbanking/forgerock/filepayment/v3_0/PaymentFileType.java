/**
 * Copyright 2019 ForgeRock AS.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0;

import org.springframework.http.MediaType;

import java.util.Arrays;

public enum PaymentFileType {
    UK_OBIE_PAYMENT_INITIATION_V3_0("UK.OBIE.PaymentInitiation.3.0", MediaType.APPLICATION_JSON),

    // Note: This enum is also used in v3.1.1 onwards in the OB spec
    UK_OBIE_PAYMENT_INITIATION_V3_1("UK.OBIE.PaymentInitiation.3.1", MediaType.APPLICATION_JSON),
    UK_OBIE_PAIN_001("UK.OBIE.pain.001.001.08", MediaType.TEXT_XML),
    // Note: specific types for csv files
    UK_LBG_FPS_BATCH_V10("UK.LBG.O4B.BATCH.FPS", MediaType.TEXT_PLAIN),
    UK_LBG_BACS_BULK_V10("UK.LBG.O4B.BULK.BACS", MediaType.TEXT_PLAIN);

    private final String fileType;
    private final MediaType contentType;

    PaymentFileType(String fileType, MediaType contentType) {
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
