/**
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