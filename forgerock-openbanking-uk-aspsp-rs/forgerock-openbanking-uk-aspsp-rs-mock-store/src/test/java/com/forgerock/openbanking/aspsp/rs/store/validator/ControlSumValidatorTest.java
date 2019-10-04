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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControlSumValidatorTest {
    @Test
    public void validateMatching_noException() throws Exception {
        ControlSumValidator.validate(getConsent("100.00"), getPaymentFile("100.00"));
    }

    @Test
    public void validateMatching_ignoreZeroDecimalPlaces_noException() throws Exception {
        ControlSumValidator.validate(getConsent("100"), getPaymentFile("100.00"));
    }

    @Test
    public void validateMatching_noDecimalPlaces_noException() throws Exception {
        ControlSumValidator.validate(getConsent("100"), getPaymentFile("100"));
    }

    @Test
    public void validate_mismatch_throwException() throws Exception {
        assertThatThrownBy(() ->
                ControlSumValidator.validate(getConsent("100.01"), getPaymentFile("100.009"))
        ).isInstanceOf(OBErrorException.class)
        .hasMessage("The file received contains total transaction value of: 100.009 but the file consent metadata indicated a control sum value of 100.01'");
    }

    private static FRFileConsent2 getConsent(String controlSum) {
        return FRFileConsent2.builder()
                .writeFileConsent(new OBWriteFileConsent2().data(new OBWriteDataFileConsent2().initiation(new OBFile2()
                        .controlSum(new BigDecimal(controlSum))
                ))).build();
    }

    private static PaymentFile getPaymentFile(String controlSum) {
        PaymentFile paymentFile = mock(PaymentFile.class);
        when(paymentFile.getControlSum()).thenReturn(new BigDecimal(controlSum));
        return paymentFile;
    }
}