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
package com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment;

import uk.org.openbanking.datamodel.payment.*;

public class OBExchangeRateConverter {

    public static OBExchangeRate1 toOBExchangeRate1(OBWriteInternational3DataInitiationExchangeRateInformation exchangeRateInformation) {
        return (new OBExchangeRate1())
                .unitCurrency(exchangeRateInformation.getUnitCurrency())
                .exchangeRate(exchangeRateInformation.getExchangeRate())
                .rateType(OBExchangeRateType2Code.fromValue(exchangeRateInformation.getRateType().getValue()))
                .contractIdentification(exchangeRateInformation.getContractIdentification());
    }

    public static OBWriteInternationalConsentResponse4DataExchangeRateInformation toOBWriteInternationalConsentResponse4DataExchangeRateInformation(OBExchangeRate2 calculatedExchangeRate) {
        return (new OBWriteInternationalConsentResponse4DataExchangeRateInformation())
                .unitCurrency(calculatedExchangeRate.getUnitCurrency())
                .exchangeRate(calculatedExchangeRate.getExchangeRate())
                .rateType(OBWriteInternationalConsentResponse4DataExchangeRateInformation.RateTypeEnum.valueOf(calculatedExchangeRate.getRateType().name()))
                .contractIdentification(calculatedExchangeRate.getContractIdentification())
                .expirationDateTime(calculatedExchangeRate.getExpirationDateTime());
    }

    public static OBWriteInternational3DataInitiationExchangeRateInformation toOBWriteInternational3DataInitiationExchangeRateInformation(OBExchangeRate1 exchangeRateInformation) {
        return (new OBWriteInternational3DataInitiationExchangeRateInformation())
                .unitCurrency(exchangeRateInformation.getUnitCurrency())
                .exchangeRate(exchangeRateInformation.getExchangeRate())
                .rateType(OBWriteInternational3DataInitiationExchangeRateInformation.RateTypeEnum.valueOf(exchangeRateInformation.getRateType().name()))
                .contractIdentification(exchangeRateInformation.getContractIdentification());
    }

}
