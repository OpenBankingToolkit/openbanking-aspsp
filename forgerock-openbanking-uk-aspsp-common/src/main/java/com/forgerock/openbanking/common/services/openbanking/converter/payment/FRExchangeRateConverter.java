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
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRExchangeRateInformation;
import uk.org.openbanking.datamodel.payment.*;

public class FRExchangeRateConverter {

    // OB to FR
    public static FRExchangeRateInformation toFRExchangeRateInformation(OBExchangeRate1 obExchangeRate1) {
        return obExchangeRate1 == null ? null : FRExchangeRateInformation.builder()
                .unitCurrency(obExchangeRate1.getUnitCurrency())
                .exchangeRate(obExchangeRate1.getExchangeRate())
                .rateType(toFRRateType(obExchangeRate1.getRateType()))
                .contractIdentification(obExchangeRate1.getContractIdentification())
                .build();
    }

    public static FRExchangeRateInformation toFRExchangeRateInformation(OBWriteInternational3DataInitiationExchangeRateInformation exchangeRateInformation) {
        return exchangeRateInformation == null ? null : FRExchangeRateInformation.builder()
                .unitCurrency(exchangeRateInformation.getUnitCurrency())
                .exchangeRate(exchangeRateInformation.getExchangeRate())
                .rateType(toFRRateType(exchangeRateInformation.getRateType()))
                .contractIdentification(exchangeRateInformation.getContractIdentification())
                .build();
    }

    public static FRExchangeRateInformation.FRRateType toFRRateType(OBExchangeRateType2Code obExchangeRateType2Code) {
        return obExchangeRateType2Code == null ? null : FRExchangeRateInformation.FRRateType.valueOf(obExchangeRateType2Code.name());
    }

    public static FRExchangeRateInformation.FRRateType toFRRateType(OBWriteInternational3DataInitiationExchangeRateInformation.RateTypeEnum rateType) {
        return rateType == null ? null : FRExchangeRateInformation.FRRateType.valueOf(rateType.name());
    }

    // FR to OB
    public static OBWriteInternational3DataInitiationExchangeRateInformation toOBWriteInternational3DataInitiationExchangeRateInformation(FRExchangeRateInformation frExchangeRateInformation) {
        return frExchangeRateInformation == null ? null : new OBWriteInternational3DataInitiationExchangeRateInformation()
                .unitCurrency(frExchangeRateInformation.getUnitCurrency())
                .exchangeRate(frExchangeRateInformation.getExchangeRate())
                .rateType(toOBWriteInternational3DataInitiationExchangeRateInformationRateType(frExchangeRateInformation.getRateType()))
                .contractIdentification(frExchangeRateInformation.getContractIdentification());
    }

    public static OBWriteInternationalConsentResponse4DataExchangeRateInformation toOBWriteInternationalConsentResponse4DataExchangeRateInformation(FRExchangeRateInformation exchangeRateInformation) {
        return exchangeRateInformation == null ? null : new OBWriteInternationalConsentResponse4DataExchangeRateInformation()
                .unitCurrency(exchangeRateInformation.getUnitCurrency())
                .exchangeRate(exchangeRateInformation.getExchangeRate())
                .rateType(toOBWriteInternationalConsentResponse4DataExchangeRateInformationRateType(exchangeRateInformation.getRateType()))
                .contractIdentification(exchangeRateInformation.getContractIdentification())
                .expirationDateTime(exchangeRateInformation.getExpirationDateTime());
    }

    public static OBWriteInternationalConsentResponse5DataExchangeRateInformation toOBWriteInternationalConsentResponse5DataExchangeRateInformation(FRExchangeRateInformation exchangeRateInformation) {
        return exchangeRateInformation == null ? null : new OBWriteInternationalConsentResponse5DataExchangeRateInformation()
                .unitCurrency(exchangeRateInformation.getUnitCurrency())
                .exchangeRate(exchangeRateInformation.getExchangeRate())
                .rateType(toOBWriteInternationalConsentResponse5DataExchangeRateInformationRateType(exchangeRateInformation.getRateType()))
                .contractIdentification(exchangeRateInformation.getContractIdentification())
                .expirationDateTime(exchangeRateInformation.getExpirationDateTime());
    }

    public static OBWriteInternationalConsentResponse6DataExchangeRateInformation toOBWriteInternationalConsentResponse6DataExchangeRateInformation(FRExchangeRateInformation exchangeRateInformation) {
        return exchangeRateInformation == null ? null : new OBWriteInternationalConsentResponse6DataExchangeRateInformation()
                .unitCurrency(exchangeRateInformation.getUnitCurrency())
                .exchangeRate(exchangeRateInformation.getExchangeRate())
                .rateType(toOBWriteInternationalConsentResponse6DataExchangeRateInformationRateType(exchangeRateInformation.getRateType()))
                .contractIdentification(exchangeRateInformation.getContractIdentification())
                .expirationDateTime(exchangeRateInformation.getExpirationDateTime());
    }

    public static OBExchangeRate1 toOBExchangeRate1(FRExchangeRateInformation frExchangeRateInformation) {
        return frExchangeRateInformation == null ? null : new OBExchangeRate1()
                .unitCurrency(frExchangeRateInformation.getUnitCurrency())
                .exchangeRate(frExchangeRateInformation.getExchangeRate())
                .rateType(toOBExchangeRateType2Code(frExchangeRateInformation.getRateType()))
                .contractIdentification(frExchangeRateInformation.getContractIdentification());
    }

    public static OBExchangeRate2 toOBExchangeRate2(FRExchangeRateInformation exchangeRateInformation) {
        return exchangeRateInformation == null ? null : new OBExchangeRate2()
                .unitCurrency(exchangeRateInformation.getUnitCurrency())
                .exchangeRate(exchangeRateInformation.getExchangeRate())
                .rateType(toOBExchangeRateType2Code(exchangeRateInformation.getRateType()))
                .contractIdentification(exchangeRateInformation.getContractIdentification())
                .expirationDateTime(exchangeRateInformation.getExpirationDateTime());
    }

    public static OBWriteInternational3DataInitiationExchangeRateInformation.RateTypeEnum toOBWriteInternational3DataInitiationExchangeRateInformationRateType(FRExchangeRateInformation.FRRateType rateType) {
        return rateType == null ? null : OBWriteInternational3DataInitiationExchangeRateInformation.RateTypeEnum.valueOf(rateType.name());
    }

    public static OBWriteInternationalConsentResponse4DataExchangeRateInformation.RateTypeEnum toOBWriteInternationalConsentResponse4DataExchangeRateInformationRateType(FRExchangeRateInformation.FRRateType rateType) {
        return rateType == null ? null : OBWriteInternationalConsentResponse4DataExchangeRateInformation.RateTypeEnum.valueOf(rateType.name());
    }

    public static OBWriteInternationalConsentResponse5DataExchangeRateInformation.RateTypeEnum toOBWriteInternationalConsentResponse5DataExchangeRateInformationRateType(FRExchangeRateInformation.FRRateType rateType) {
        return rateType == null ? null : OBWriteInternationalConsentResponse5DataExchangeRateInformation.RateTypeEnum.valueOf(rateType.name());
    }

    public static OBWriteInternationalConsentResponse6DataExchangeRateInformation.RateTypeEnum toOBWriteInternationalConsentResponse6DataExchangeRateInformationRateType(FRExchangeRateInformation.FRRateType rateType) {
        return rateType == null ? null : OBWriteInternationalConsentResponse6DataExchangeRateInformation.RateTypeEnum.valueOf(rateType.name());
    }

    public static OBExchangeRateType2Code toOBExchangeRateType2Code(FRExchangeRateInformation.FRRateType rateType) {
        return rateType == null ? null : OBExchangeRateType2Code.valueOf(rateType.name());
    }

}
