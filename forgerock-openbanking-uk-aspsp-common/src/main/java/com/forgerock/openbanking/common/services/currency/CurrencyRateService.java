/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.currency;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import uk.org.openbanking.datamodel.payment.OBExchangeRate1;
import uk.org.openbanking.datamodel.payment.OBExchangeRate2;
import uk.org.openbanking.datamodel.payment.OBExchangeRateType2Code;

import java.math.BigDecimal;

@Slf4j
public final class CurrencyRateService {

    // Currency rate is hardcoded for sandbox for simplicity - if required in future we can implement different exchange rates for currencies / datetimes
    private static final String FIXED_RATE = "1.5";

    // Actual Currency rate expiration time - hardcoded period should be good enough for sandbox testing.
    private static final int ACTUAL_RATE_EXPIRATION_MINUTES = 60;

    /**
     * This method gets the exchange rate requested in the initiation with the correct calculated fields.
     * <ul>
     * <li/>For Actual Rate: the rate value and expiration date are populated with test values.
     * <li/>For Indicative Rate: the rate value only is populated with test value.
     * <li/>For Agreed Rate: the exchange rate is returned with no modification
     * </ul>
     *
     * Provided OBExchangeRate1 should already be validated using com.forgerock.openbanking.aspsp.rs.api.payment.verifier.ExchangeRateVerifier
     *
     * @param submittedExchangeRate Request exchange rate submitted by the user
     * @param createdDateTime Date request was submitted - used to calculate how long an agreed rate will be valid for.
     * @return Exchange rate with populated fields
     */
    public static OBExchangeRate2 getCalculatedExchangeRate(final OBExchangeRate1 submittedExchangeRate, DateTime createdDateTime) {
        if (submittedExchangeRate==null) {
            return null; // Exchange rate is optional in international payment requests
        }
        if (submittedExchangeRate.getRateType()==null) {
            throw new IllegalArgumentException("Missing the exchange rate type");
        }
        final OBExchangeRate2 calculatedExchangeRate = new OBExchangeRate2()
                .expirationDateTime(getExpirationDate(submittedExchangeRate, createdDateTime))
                .rateType(submittedExchangeRate.getRateType())
                .contractIdentification(submittedExchangeRate.getContractIdentification())
                .exchangeRate(getExchangeRate(submittedExchangeRate))
                .unitCurrency(submittedExchangeRate.getUnitCurrency());
        log.debug("Calculated exchange rate: {} from requested rate: {} for creation date: {}", calculatedExchangeRate, submittedExchangeRate, createdDateTime);
        return calculatedExchangeRate;
    }

    private static DateTime getExpirationDate(OBExchangeRate1 submittedRate, DateTime createdDateTime) {
        if (submittedRate==null) {
            return null;
        }
        switch (submittedRate.getRateType()) {
            case ACTUAL:
                return ((createdDateTime==null) ? DateTime.now() : createdDateTime).plusMinutes(ACTUAL_RATE_EXPIRATION_MINUTES);
            case INDICATIVE:
            case AGREED:
                return null;
            default:
                throw new UnsupportedOperationException("Unsupported currency rate type: "+submittedRate.getRateType());
        }
    }

    private static BigDecimal getExchangeRate(OBExchangeRate1 submittedRate) {
        switch (submittedRate.getRateType()) {
            case INDICATIVE:
            case ACTUAL:
                return new BigDecimal(FIXED_RATE);
            case AGREED:
                if (submittedRate.getExchangeRate()==null) {
                    throw new IllegalArgumentException("Missing the exchange rate value which is mandatory for exchange rate type: '"+ OBExchangeRateType2Code.AGREED+"'");
                }
                return submittedRate.getExchangeRate();
            default:
                throw new UnsupportedOperationException("Unsupported currency rate type: "+submittedRate.getRateType());
        }
    }
}
