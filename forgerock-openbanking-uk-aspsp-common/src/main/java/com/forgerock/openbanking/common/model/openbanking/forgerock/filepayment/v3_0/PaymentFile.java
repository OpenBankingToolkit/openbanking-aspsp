/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0;

import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a parsed valid payment file
 */
public interface PaymentFile {

    /**
     * @return Number of transactions in payyment file
     */
    int getNumberOfTransactions();

    /**
     * @return The control sum (sum of all transaction amounts)
     */
    BigDecimal getControlSum();

    /**
     * @return Media Type of the file contents
     */
    MediaType getContentType();

    /**
     * @return List of each transaction with essential payment details and status
     */
    List<FRFilePayment> getPayments();

}
