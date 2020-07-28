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
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFileConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRFileConsent5;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileTransactionCountValidator {

    /**
     * Check that the number of transaction contained in a payment file is equal to the stated number of transaction is the file payment consent metadata.
     * @param consent File payment consent
     * @param paymentFile Payment file body
     * @throws OBErrorException Validation failed
     */
    public static void validate(FRFileConsent2 consent, PaymentFile paymentFile) throws OBErrorException {
        validate(paymentFile.getNumberOfTransactions(), Integer.valueOf(consent.getInitiation().getNumberOfTransactions()), consent.getId());
    }

    /**
     * Check that the number of transaction contained in a payment file is equal to the stated number of transaction is the file payment consent metadata.
     * @param consent File payment consent
     * @param paymentFile Payment file body
     * @throws OBErrorException Validation failed
     */
    public static void validate(FRFileConsent5 consent, PaymentFile paymentFile) throws OBErrorException {
        validate(paymentFile.getNumberOfTransactions(), Integer.valueOf(consent.getInitiation().getNumberOfTransactions()), consent.getId());
    }

    private static void validate(int paymentFileNumberOfTransactions, int consentNumberOfTransactions, String consentId) throws OBErrorException {
        log.debug("Metadata indicates expected transaction count of '{}'. File contains '{}' transactions", consentNumberOfTransactions, paymentFileNumberOfTransactions);
        if (paymentFileNumberOfTransactions != consentNumberOfTransactions) {
            log.warn("File consent metadata indicated {} transactions would be present but found {} in uploaded file", consentNumberOfTransactions, paymentFileNumberOfTransactions);
            throw new OBErrorException(OBRIErrorType.REQUEST_FILE_WRONG_NUMBER_OF_TRANSACTIONS, paymentFileNumberOfTransactions,  consentNumberOfTransactions);
        }
        log.debug("File transaction count is correct for consent id: {}", consentId);
    }
}
