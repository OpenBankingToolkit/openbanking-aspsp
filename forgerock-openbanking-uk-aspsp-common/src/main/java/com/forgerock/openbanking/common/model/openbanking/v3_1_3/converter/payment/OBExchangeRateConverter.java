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


import uk.org.openbanking.datamodel.payment.OBExchangeRate1;
import uk.org.openbanking.datamodel.payment.OBExchangeRate2;
import uk.org.openbanking.datamodel.payment.OBExchangeRateType2Code;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3DataInitiationExchangeRateInformation;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsentResponse4DataExchangeRateInformation;

public class OBExchangeRateConverter {

    public static OBExchangeRate1 toOBExchangeRate1(OBWriteInternational3DataInitiationExchangeRateInformation exchangeRateInformation) {
        return exchangeRateInformation == null ? null : (new OBExchangeRate1())
                .unitCurrency(exchangeRateInformation.getUnitCurrency())
                .exchangeRate(exchangeRateInformation.getExchangeRate())
                .rateType(toOBExchangeRateType2Code(exchangeRateInformation.getRateType()))
                .contractIdentification(exchangeRateInformation.getContractIdentification());
    }

    public static OBExchangeRate2 toOBExchangeRate2(OBWriteInternationalConsentResponse4DataExchangeRateInformation calculatedExchangeRate) {
        return calculatedExchangeRate == null ? null : (new OBExchangeRate2())
                .unitCurrency(calculatedExchangeRate.getUnitCurrency())
                .exchangeRate(calculatedExchangeRate.getExchangeRate())
                .rateType(toOBExchangeRateType2Code(calculatedExchangeRate.getRateType()))
                .contractIdentification(calculatedExchangeRate.getContractIdentification())
                .expirationDateTime(calculatedExchangeRate.getExpirationDateTime());
    }

    public static OBWriteInternational3DataInitiationExchangeRateInformation toOBWriteInternational3DataInitiationExchangeRateInformation(OBExchangeRate1 exchangeRateInformation) {
        return exchangeRateInformation == null ? null : (new OBWriteInternational3DataInitiationExchangeRateInformation())
                .unitCurrency(exchangeRateInformation.getUnitCurrency())
                .exchangeRate(exchangeRateInformation.getExchangeRate())
                .rateType(toRateTypeEnum(exchangeRateInformation.getRateType()))
                .contractIdentification(exchangeRateInformation.getContractIdentification());
    }

    public static OBWriteInternational3DataInitiationExchangeRateInformation.RateTypeEnum toRateTypeEnum(OBExchangeRateType2Code rateType) {
        return rateType == null ? null : OBWriteInternational3DataInitiationExchangeRateInformation.RateTypeEnum.valueOf(rateType.name());
    }

    public static OBExchangeRateType2Code toOBExchangeRateType2Code(OBWriteInternational3DataInitiationExchangeRateInformation.RateTypeEnum rateType) {
        return rateType == null ? null : OBExchangeRateType2Code.valueOf(rateType.name());
    }

    public static OBExchangeRateType2Code toOBExchangeRateType2Code(OBWriteInternationalConsentResponse4DataExchangeRateInformation.RateTypeEnum rateType) {
        return rateType == null ? null : OBExchangeRateType2Code.valueOf(rateType.name());
    }
}
