/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.validator;


import com.forgerock.openbanking.commons.model.openbanking.forgerock.filepayment.v3_0.PaymentFile;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRFileConsent2;
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
        log.debug("Metadata indicates expected transaction count of '{}'. File contains '{}' transactions", consent.getInitiation().getNumberOfTransactions(), paymentFile.getNumberOfTransactions());
        if (paymentFile.getNumberOfTransactions() != Integer.valueOf(consent.getInitiation().getNumberOfTransactions())) {
            log.warn("File consent metadata indicated {} transactions would be present but found {} in uploaded file", consent.getInitiation().getNumberOfTransactions(), paymentFile.getNumberOfTransactions());
            throw new OBErrorException(OBRIErrorType.REQUEST_FILE_WRONG_NUMBER_OF_TRANSACTIONS, String.valueOf(paymentFile.getNumberOfTransactions()),  consent.getInitiation().getNumberOfTransactions());
        }
        log.debug("File transaction count is correct for consent id: {}", consent.getId());
    }
}
