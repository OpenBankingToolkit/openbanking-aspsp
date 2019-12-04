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
package com.forgerock.openbanking.aspsp.rs.api.payment.verifier;

import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Test;
import uk.org.openbanking.datamodel.payment.OBExchangeRate1;
import uk.org.openbanking.datamodel.payment.OBExchangeRateType2Code;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExchangeRateVerifierTest {

    private ExchangeRateVerifier exchangeRateVerifier = new ExchangeRateVerifier();

    @Test
    public void verifyExchangeRate_actualRate_noRateOrContractSpecified() throws Exception {
        // Given
        final OBExchangeRate1 exchangeRate = new OBExchangeRate1()
                .rateType(OBExchangeRateType2Code.ACTUAL)
                .unitCurrency("GBP");

        // When
        exchangeRateVerifier.verify(exchangeRate);

        // Then : No Exception thrown
    }

    @Test
    public void verifyExchangeRate_actualRate_RateSpecified_Fail() throws Exception {
        // Given
        final OBExchangeRate1 exchangeRate = new OBExchangeRate1()
                .rateType(OBExchangeRateType2Code.ACTUAL)
                .exchangeRate(new BigDecimal("1.5"))
                .unitCurrency("GBP");

        // When
        assertThatThrownBy(() ->
                exchangeRateVerifier.verify(exchangeRate))

                // Then
                .isExactlyInstanceOf(OBErrorException.class)
                .hasMessage("Payment invalid. Invalid exchange rate information provided");

    }

    @Test
    public void verifyExchangeRate_actualRate_ContractSpecified_Fail() throws Exception {
        // Given
        final OBExchangeRate1 exchangeRate = new OBExchangeRate1()
                .rateType(OBExchangeRateType2Code.ACTUAL)
                .contractIdentification("abc")
                .unitCurrency("GBP");

        // When
        assertThatThrownBy(() ->
                exchangeRateVerifier.verify(exchangeRate))

                // Then
                .isExactlyInstanceOf(OBErrorException.class)
                .hasMessage("Payment invalid. Invalid exchange rate information provided");

    }

    @Test
    public void verifyExchangeRate_indicativeRate_noRateOrContractSpecified() throws Exception {
        // Given
        final OBExchangeRate1 exchangeRate = new OBExchangeRate1()
                .rateType(OBExchangeRateType2Code.INDICATIVE)
                .unitCurrency("GBP");

        // When
        exchangeRateVerifier.verify(exchangeRate);

        // Then : No Exception thrown
    }

    @Test
    public void verifyExchangeRate_indicativeRate_RateSpecified_Fail() throws Exception {
        // Given
        final OBExchangeRate1 exchangeRate = new OBExchangeRate1()
                .rateType(OBExchangeRateType2Code.INDICATIVE)
                .exchangeRate(new BigDecimal("1.5"))
                .unitCurrency("GBP");

        // When
        assertThatThrownBy(() ->
                exchangeRateVerifier.verify(exchangeRate))

                // Then
                .isExactlyInstanceOf(OBErrorException.class)
                .hasMessage("Payment invalid. Invalid exchange rate information provided");

    }

    @Test
    public void verifyExchangeRate_indicativeRate_ContractSpecified_Fail() throws Exception {
        // Given
        final OBExchangeRate1 exchangeRate = new OBExchangeRate1()
                .rateType(OBExchangeRateType2Code.INDICATIVE)
                .contractIdentification("abc")
                .unitCurrency("GBP");

        // When
        assertThatThrownBy(() ->
                exchangeRateVerifier.verify(exchangeRate))

                // Then
                .isExactlyInstanceOf(OBErrorException.class)
                .hasMessage("Payment invalid. Invalid exchange rate information provided");

    }

    @Test
    public void verifyExchangeRate_agreedRate_rateAndContractSpecified() throws Exception {
        // Given
        final OBExchangeRate1 exchangeRate = new OBExchangeRate1()
                .rateType(OBExchangeRateType2Code.AGREED)
                .exchangeRate(new BigDecimal("1.5"))
                .contractIdentification("abc")
                .unitCurrency("GBP");

        // When
        exchangeRateVerifier.verify(exchangeRate);

        // Then : No Exception thrown
    }

    @Test
    public void verifyExchangeRate_agreedRate_ContractMissing_Fail() throws Exception {
        // Given
        final OBExchangeRate1 exchangeRate = new OBExchangeRate1()
                .rateType(OBExchangeRateType2Code.AGREED)
                .exchangeRate(new BigDecimal("1.5"))
                .unitCurrency("GBP");

        // When
        assertThatThrownBy(() ->
                exchangeRateVerifier.verify(exchangeRate))

                // Then
                .isExactlyInstanceOf(OBErrorException.class)
                .hasMessage("Payment invalid. Invalid exchange rate information provided");

    }

    @Test
    public void verifyExchangeRate_agreedRate_RateMissing_Fail() throws Exception {
        // Given
        final OBExchangeRate1 exchangeRate = new OBExchangeRate1()
                .rateType(OBExchangeRateType2Code.AGREED)
                .contractIdentification("abc")
                .unitCurrency("GBP");

        // When
        assertThatThrownBy(() ->
                exchangeRateVerifier.verify(exchangeRate))

                // Then
                .isExactlyInstanceOf(OBErrorException.class)
                .hasMessage("Payment invalid. Invalid exchange rate information provided");

    }
}