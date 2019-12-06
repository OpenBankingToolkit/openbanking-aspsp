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
