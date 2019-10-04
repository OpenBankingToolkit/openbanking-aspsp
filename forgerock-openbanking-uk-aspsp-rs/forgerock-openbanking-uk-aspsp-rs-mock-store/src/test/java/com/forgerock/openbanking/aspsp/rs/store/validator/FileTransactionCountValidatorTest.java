/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.validator;

import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.PaymentFile;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFileConsent2;
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Test;
import uk.org.openbanking.datamodel.payment.OBFile2;
import uk.org.openbanking.datamodel.payment.OBWriteDataFileConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteFileConsent2;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileTransactionCountValidatorTest {

    @Test
    public void validate_countMatches() throws Exception {
        FileTransactionCountValidator.validate(getConsent(10), getPaymentFile(10));
    }

    @Test
    public void validate_mismatch_throwException() throws Exception {
        assertThatThrownBy(() ->
                FileTransactionCountValidator.validate(getConsent(11), getPaymentFile(12))
        ).isInstanceOf(OBErrorException.class)
                .hasMessage("The file received contains 12 transactions but the file consent metadata indicated that we are expecting a file with 11 transactions'");
    }

    private static FRFileConsent2 getConsent(int noOfTransactions) {
        return FRFileConsent2.builder()
                .writeFileConsent(new OBWriteFileConsent2().data(new OBWriteDataFileConsent2().initiation(new OBFile2()
                        .numberOfTransactions(String.valueOf(noOfTransactions))
                ))).build();
    }

    private static PaymentFile getPaymentFile(int noOfTransactions) {
        PaymentFile paymentFile = mock(PaymentFile.class);
        when(paymentFile.getNumberOfTransactions()).thenReturn(noOfTransactions);
        return paymentFile;
    }
}