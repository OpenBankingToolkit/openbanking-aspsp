/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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