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
package com.forgerock.openbanking.aspsp.rs.store.validator;


import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.PaymentFile;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRFileConsent5;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class ControlSumValidator {

    /**
     * Check that the provided payment file and payment file consent metadata have the same control sum (defined as sum of all transaction amounts in file).
     * This is an extra validation step to ensure correct and valid file has been uploaded for the consent
     * @param paymentFile Payment file body
     * @param consent Payment file consent
     * @throws OBErrorException Validation failed
     */
    public static void validate(FRFileConsent5 consent, PaymentFile paymentFile) throws OBErrorException {
        validate(paymentFile, consent.getInitiation().getControlSum(), consent.getId());
    }

    private static void validate(PaymentFile paymentFile, BigDecimal consentControlSum, String consentId) throws OBErrorException {
        BigDecimal fileControlSum = paymentFile.getControlSum();
        log.debug("Metadata indicates expected control sum of '{}'. File contains actual control sum of '{}'", consentControlSum, fileControlSum);
        if (fileControlSum.compareTo(consentControlSum)!=0) {
            log.warn("File consent metadata indicated control sum of '{}' but found a control sum of '{}' in uploaded file", consentControlSum, fileControlSum);
            throw new OBErrorException(OBRIErrorType.REQUEST_FILE_INCORRECT_CONTROL_SUM, fileControlSum.toPlainString(), consentControlSum.toPlainString());
        }
        log.debug("File control sum count is correct for consent id: {}", consentId);
    }
}
