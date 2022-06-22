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

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRExternalExtendedAccountType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRExternalPaymentContextCode;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRPaymentRisk;
import uk.org.openbanking.datamodel.payment.OBExternalExtendedAccountType1Code;
import uk.org.openbanking.datamodel.payment.OBExternalPaymentContext1Code;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBRisk1DeliveryAddress;

public class FRPaymentRiskConverter {

    public static FRPaymentRisk toFRRisk(OBRisk1 obRisk1) {
        return obRisk1 == null ? null : FRPaymentRisk.builder()
                .deliveryAddress(toFRRiskDeliveryAddress(obRisk1.getDeliveryAddress()))
                .merchantCategoryCode(obRisk1.getMerchantCategoryCode())
                .merchantCustomerIdentification(obRisk1.getMerchantCustomerIdentification())
                .paymentContextCode(toFRExternalPaymentContextCode(obRisk1.getPaymentContextCode()))
                .beneficiaryAccountType(toFRExternalExtendedAccountType(obRisk1.getBeneficiaryAccountType()))
                .paymentPurposeCode(obRisk1.getPaymentPurposeCode())
                .contractPresentIndicator(obRisk1.getContractPresentInidicator())
                .beneficiaryPrepopulatedIndicator(obRisk1.getBeneficiaryPrepopulatedIndicator())
                .build();
    }

    public static FRExternalExtendedAccountType toFRExternalExtendedAccountType(OBExternalExtendedAccountType1Code obExternalExtendedAccountType1Code) {
        return obExternalExtendedAccountType1Code == null ? null : FRExternalExtendedAccountType.fromValue(obExternalExtendedAccountType1Code.getValue());
    }

    public static FRPaymentRisk.FRRiskDeliveryAddress toFRRiskDeliveryAddress(OBRisk1DeliveryAddress obRisk1DeliveryAddress) {
        return obRisk1DeliveryAddress == null ? null : FRPaymentRisk.FRRiskDeliveryAddress.builder()
                .addressLine(obRisk1DeliveryAddress.getAddressLine())
                .streetName(obRisk1DeliveryAddress.getStreetName())
                .buildingNumber(obRisk1DeliveryAddress.getBuildingNumber())
                .postCode(obRisk1DeliveryAddress.getPostCode())
                .townName(obRisk1DeliveryAddress.getTownName())
                .countrySubDivision(obRisk1DeliveryAddress.getCountrySubDivision())
                .country(obRisk1DeliveryAddress.getCountry())
                .build();
    }

    public static FRExternalPaymentContextCode toFRExternalPaymentContextCode(OBExternalPaymentContext1Code obExternalPaymentContext1Code) {
        return obExternalPaymentContext1Code == null ? null : FRExternalPaymentContextCode.valueOf(obExternalPaymentContext1Code.name());
    }

    public static OBRisk1 toOBRisk1(FRPaymentRisk frPaymentRisk) {
        return frPaymentRisk == null ? null : new OBRisk1()
                .deliveryAddress(toOBRisk1DeliveryAddress(frPaymentRisk.getDeliveryAddress()))
                .merchantCategoryCode(frPaymentRisk.getMerchantCategoryCode())
                .merchantCustomerIdentification(frPaymentRisk.getMerchantCustomerIdentification())
                .paymentContextCode(toOBExternalPaymentContext1Code(frPaymentRisk.getPaymentContextCode()))
                .paymentPurposeCode(frPaymentRisk.getPaymentPurposeCode())
                .beneficiaryPrepopulatedIndicator(frPaymentRisk.getBeneficiaryPrepopulatedIndicator())
                .contractPresentInidicator(frPaymentRisk.getContractPresentIndicator())
                .beneficiaryAccountType(toOBExternalExtendedAccountType1Code(frPaymentRisk.getBeneficiaryAccountType()));
    }

    public static OBExternalExtendedAccountType1Code toOBExternalExtendedAccountType1Code(FRExternalExtendedAccountType frExternalExtendedAccountType) {
        return frExternalExtendedAccountType == null ? null : OBExternalExtendedAccountType1Code.fromValue(frExternalExtendedAccountType.getValue());
    }

    public static OBRisk1DeliveryAddress toOBRisk1DeliveryAddress(FRPaymentRisk.FRRiskDeliveryAddress frDeliveryAddress) {
        return frDeliveryAddress == null ? null : new OBRisk1DeliveryAddress()
                .addressLine(frDeliveryAddress.getAddressLine())
                .streetName(frDeliveryAddress.getStreetName())
                .buildingNumber(frDeliveryAddress.getBuildingNumber())
                .postCode(frDeliveryAddress.getPostCode())
                .townName(frDeliveryAddress.getTownName())
                .countrySubDivision(frDeliveryAddress.getCountrySubDivision())
                .country(frDeliveryAddress.getCountry());
    }

    public static OBExternalPaymentContext1Code toOBExternalPaymentContext1Code(FRExternalPaymentContextCode frExternalPaymentContextCode) {
        return frExternalPaymentContextCode == null ? null : OBExternalPaymentContext1Code.valueOf(frExternalPaymentContextCode.name());
    }
}
