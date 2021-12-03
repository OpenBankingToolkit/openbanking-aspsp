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
package com.forgerock.openbanking.common.services.openbanking.converter.vrp;

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAgent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRPaymentRisk;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRPostalAddress;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRRemittanceInformation;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.*;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.vrp.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentSupplementaryDataConverter.toOBSupplementaryData1;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPControlParametersConverter.toFRDomesticVRPControlParameters;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRWriteDomesticVRPDataInitiationConverter.toFRWriteDomesticVRPDataInitiation;

public class FRDomesticVRPConsentConverter {

    // OB to FR
    public static FRDomesticVRPConsentDetails toFRDomesticVRPConsentDetails(
            OBDomesticVRPConsentRequest obDomesticVRPConsentRequest
    ) {
        return obDomesticVRPConsentRequest == null ? null : FRDomesticVRPConsentDetails.builder()
                .data(toFRDomesticVRPConsentDetailsData(obDomesticVRPConsentRequest.getData()))
                .risk(toFRRisk(obDomesticVRPConsentRequest.getRisk()))
                .build();
    }

    public static FRDomesticVRPConsentDetailsData toFRDomesticVRPConsentDetailsData(
            OBDomesticVRPConsentRequestData data
    ) {
        return data == null ? null : FRDomesticVRPConsentDetailsData.builder()
                .readRefundAccount(FRReadRefundAccount.fromValue(data.getReadRefundAccount().getValue()))
                .initiation(toFRWriteDomesticVRPDataInitiation(data.getInitiation()))
                .controlParameters(toFRDomesticVRPControlParameters(data.getControlParameters()))
                .build();
    }

    // FR to OB request
    public static OBDomesticVRPConsentRequest toOBDomesticVRPConsentRequest(FRDomesticVRPConsentDetails frDomesticVRPConsentDetails) {
        return frDomesticVRPConsentDetails == null ? null : new OBDomesticVRPConsentRequest()
                .data(toOBDomesticVRPConsentRequestData(frDomesticVRPConsentDetails.getData()))
                .risk(toOBRisk1(frDomesticVRPConsentDetails.getRisk()));
    }

    public static OBDomesticVRPConsentRequestData toOBDomesticVRPConsentRequestData(FRDomesticVRPConsentDetailsData data) {
        return data == null ? null : new OBDomesticVRPConsentRequestData()
                .readRefundAccount(
                        OBDomesticVRPConsentRequestData.ReadRefundAccountEnum.fromValue
                                (
                                        data.getReadRefundAccount().getValue()
                                )
                )
                .controlParameters(toOBDomesticVRPControlParameters(data.getControlParameters()))
                .initiation(toOBDomesticVRPInitiation(data.getInitiation()));
    }

    public static OBDomesticVRPInitiation toOBDomesticVRPInitiation(FRWriteDomesticVRPDataInitiation initiation) {
        return initiation == null ? null : new OBDomesticVRPInitiation()
                .creditorAccount(toOBCashAccountCreditor3(initiation.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(initiation.getCreditorAgent()))
                .debtorAccount(toOBCashAccountDebtorWithName(initiation.getDebtorAccount()))
                .remittanceInformation(toOBDomesticVRPInitiationRemittanceInformation(initiation.getRemittanceInformation()));
    }

    public static OBDomesticVRPInitiationRemittanceInformation toOBDomesticVRPInitiationRemittanceInformation(
            FRRemittanceInformation remittanceInformation
    ) {
        return remittanceInformation == null ? null : new OBDomesticVRPInitiationRemittanceInformation()
                .reference(remittanceInformation.getReference())
                .unstructured(remittanceInformation.getUnstructured());
    }

    public static OBCashAccountDebtorWithName toOBCashAccountDebtorWithName(FRAccountIdentifier accountIdentifier) {
        return accountIdentifier == null ? null : new OBCashAccountDebtorWithName()
                .identification(accountIdentifier.getIdentification())
                .name(accountIdentifier.getName())
                .schemeName(accountIdentifier.getSchemeName())
                .secondaryIdentification(accountIdentifier.getSecondaryIdentification());
    }

    public static OBCashAccountCreditor3 toOBCashAccountCreditor3(FRAccountIdentifier accountIdentifier) {
        return accountIdentifier == null ? null : new OBCashAccountCreditor3()
                .identification(accountIdentifier.getIdentification())
                .name(accountIdentifier.getName())
                .schemeName(accountIdentifier.getSchemeName())
                .secondaryIdentification(accountIdentifier.getSecondaryIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(
            FRFinancialAgent agent) {
        return agent == null ? null : new OBBranchAndFinancialInstitutionIdentification6()
                .identification(agent.getIdentification())
                .name(agent.getName())
                .schemeName(agent.getSchemeName())
                .postalAddress(toOBPostalAddress6(agent.getPostalAddress()));
    }

    public static OBPostalAddress6 toOBPostalAddress6(FRPostalAddress address) {
        return address == null ? null : new OBPostalAddress6()
                .addressLine(address.getAddressLine())
                .addressType(OBAddressTypeCode.fromValue(address.getAddressType().getValue()))
                .buildingNumber(address.getBuildingNumber())
                .country(address.getCountry())
                .countrySubDivision(address.getCountrySubDivision())
                .department(address.getDepartment())
                .postCode(address.getPostCode())
                .streetName(address.getStreetName())
                .subDepartment(address.getSubDepartment())
                .townName(address.getTownName());
    }

    public static OBDomesticVRPControlParameters toOBDomesticVRPControlParameters(FRDomesticVRPControlParameters controlParameters) {
        return controlParameters == null ? null : new OBDomesticVRPControlParameters()
                .psUAuthenticationMethods(controlParameters.getPsuAuthenticationMethods())
                .vrPType(controlParameters.getVrpType())
                .validFromDateTime(controlParameters.getValidFromDateTime())
                .validToDateTime(controlParameters.getValidToDateTime())
                .maximumIndividualAmount(toOBActiveOrHistoricCurrencyAndAmount(controlParameters.getMaximumIndividualAmount()))
                .periodicLimits(toListOBDomesticVRPControlParametersPeriodicLimits(controlParameters.getPeriodicLimits()))
                .supplementaryData(toOBSupplementaryData1(controlParameters.getSupplementaryData()));
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(FRAmount amount) {
        return amount == null ? null : new OBActiveOrHistoricCurrencyAndAmount()
                .amount(amount.getAmount())
                .currency(amount.getCurrency());
    }

    public static List<OBDomesticVRPControlParametersPeriodicLimits> toListOBDomesticVRPControlParametersPeriodicLimits(
            List<FRPeriodicLimits> periodicLimits
    ) {
        return periodicLimits == null ? null : periodicLimits.stream().map(
                item -> (new OBDomesticVRPControlParametersPeriodicLimits())
                        .amount(item.getAmount())
                        .currency(item.getCurrency())
                        .periodAlignment(
                                OBDomesticVRPControlParametersPeriodicLimits.PeriodAlignmentEnum.fromValue
                                        (
                                                item.getPeriodAlignment().getValue()
                                        )
                        )
                        .periodType(
                                OBDomesticVRPControlParametersPeriodicLimits.PeriodTypeEnum.fromValue
                                        (
                                                item.getPeriodType().getValue()
                                        )
                        )
        ).collect(Collectors.toList());
    }

    public static OBRisk1 toOBRisk1(FRPaymentRisk frPaymentRisk) {
        return frPaymentRisk == null ? null : new OBRisk1()
                .deliveryAddress(toOBRisk1DeliveryAddress(frPaymentRisk.getDeliveryAddress()))
                .merchantCategoryCode(frPaymentRisk.getMerchantCategoryCode())
                .merchantCustomerIdentification(frPaymentRisk.getMerchantCustomerIdentification())
                .paymentContextCode(toOBExternalPaymentContext1Code(frPaymentRisk.getPaymentContextCode()));
    }

    // FR to OB response
    public static OBDomesticVRPConsentResponse toOBDomesticVRPConsentResponse(FRDomesticVRPConsent consent) {
        return consent == null ? null : new OBDomesticVRPConsentResponse()
                .data(toOBDomesticVRPConsentResponseData(consent))
                .meta(new Meta())
                .risk(toOBRisk1(consent.getRisk()));

    }

    public static OBDomesticVRPConsentResponseData toOBDomesticVRPConsentResponseData(FRDomesticVRPConsent consent) {
        FRDomesticVRPConsentDetails details = consent == null ? null : consent.getVrpDetails();
        FRDomesticVRPConsentDetailsData data = details == null ? null : details.getData();
        return data == null ? null : new OBDomesticVRPConsentResponseData()
                .consentId(consent.getId())
                .creationDateTime(consent.getCreated())
                .readRefundAccount(
                        OBDomesticVRPConsentResponseData.ReadRefundAccountEnum.fromValue
                                (
                                        data.getReadRefundAccount().getValue()
                                )
                )
                .status(
                        OBDomesticVRPConsentResponseData.StatusEnum.fromValue
                                (
                                        consent.getStatus().getValue()
                                )
                )
                .statusUpdateDateTime(consent.getStatusUpdate())
                .controlParameters(toOBDomesticVRPControlParameters(data.getControlParameters()))
                .debtorAccount(toOBCashAccountDebtorWithName(data.getInitiation().getDebtorAccount()))
                .initiation(toOBDomesticVRPInitiation(data.getInitiation()));

    }
}
