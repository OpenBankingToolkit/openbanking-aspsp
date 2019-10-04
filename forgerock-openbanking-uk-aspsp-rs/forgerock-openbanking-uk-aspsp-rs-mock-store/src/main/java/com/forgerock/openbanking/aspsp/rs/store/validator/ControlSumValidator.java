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
    public static void validate(FRFileConsent2 consent, PaymentFile paymentFile) throws OBErrorException {
        BigDecimal fileControlSum = paymentFile.getControlSum();
        log.debug("Metadata indicates expected control sum of '{}'. File contains actual control sum of '{}'", consent.getInitiation().getControlSum(), fileControlSum);
        if (fileControlSum.compareTo(consent.getInitiation().getControlSum())!=0) {
            log.warn("File consent metadata indicated control sum of '{}' but found a control sum of '{}' in uploaded file", consent.getInitiation().getControlSum(), fileControlSum);
            throw new OBErrorException(OBRIErrorType.REQUEST_FILE_INCORRECT_CONTROL_SUM, fileControlSum.toPlainString(), consent.getInitiation().getControlSum().toPlainString());
        }
        log.debug("File control sum count is correct for consent id: {}", consent.getId());
    }
}
