/**
 * Copyright 2019 ForgeRock AS.
 *
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
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.payment.OBExchangeRate1;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3DataInitiationExchangeRateInformation;

@Component
@Slf4j
public class ExchangeRateVerifier {

    public void verify(OBExchangeRate1 exchangeRate) throws OBErrorException {
        if (exchangeRate == null) {
            log.debug("No exchange rate provided to verify");
            return;
        }
        switch (exchangeRate.getRateType()) {
            case ACTUAL:
            case INDICATIVE:
                if (exchangeRate.getExchangeRate() != null) {
                    log.debug("ACTUAL/INDICATIVE cannot have an exchange rate specified in request");
                    throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_EXCHANGE_RATE);
                }
                if (exchangeRate.getContractIdentification() != null) {
                    log.debug("ACTUAL/INDICATIVE cannot have a contract identification specified in request");
                    throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_EXCHANGE_RATE);
                }
                break;
            case AGREED:
                if (exchangeRate.getExchangeRate() == null) {
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

    public void verify(OBWriteInternational3DataInitiationExchangeRateInformation exchangeRate) throws OBErrorException {
        if (exchangeRate == null) {
            log.debug("No exchange rate provided to verify");
            return;
        }
        switch (exchangeRate.getRateType()) {
            case ACTUAL:
            case INDICATIVE:
                if (exchangeRate.getExchangeRate() != null) {
                    log.debug("ACTUAL/INDICATIVE cannot have an exchange rate specified in request");
                    throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_EXCHANGE_RATE);
                }
                if (exchangeRate.getContractIdentification() != null) {
                    log.debug("ACTUAL/INDICATIVE cannot have a contract identification specified in request");
                    throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_EXCHANGE_RATE);
                }
                break;
            case AGREED:
                if (exchangeRate.getExchangeRate() == null) {
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
