/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.verifier;


import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.payment.OBExchangeRate1;

@Component
@Slf4j
public class ExchangeRateVerifier {

    public void verify(OBExchangeRate1 exchangeRate) throws OBErrorException {
        if (exchangeRate==null) {
            log.debug("No exchange rate provided to verify");
            return;
        }
        switch (exchangeRate.getRateType()) {
            case ACTUAL:
            case INDICATIVE:
                if (exchangeRate.getExchangeRate()!=null) {
                    log.debug("ACTUAL/INDICATIVE cannot have an exchange rate specified in request");
                    throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_EXCHANGE_RATE);
                }
                if (exchangeRate.getContractIdentification()!=null) {
                    log.debug("ACTUAL/INDICATIVE cannot have a contract identification specified in request");
                    throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_EXCHANGE_RATE);
                }
                break;
            case AGREED:
                if (exchangeRate.getExchangeRate()==null) {
                    log.debug("AGREED must have an exchange rate specified in request");
                    throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_EXCHANGE_RATE);
                }
                if (StringUtils.isEmpty(exchangeRate.getContractIdentification())) {
                    log.debug("AGREED must have a contract identification specified in request");
                    throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_EXCHANGE_RATE);
                }
                break;
            default:
                throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_EXCHANGE_RATE_TYPE, exchangeRate.getRateType());
        }
    }
}
