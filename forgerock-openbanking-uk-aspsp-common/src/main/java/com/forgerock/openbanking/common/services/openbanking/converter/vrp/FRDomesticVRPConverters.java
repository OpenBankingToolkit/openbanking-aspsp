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

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRPostalAddress;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRRemittanceInformation;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.*;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentSupplementaryDataConverter;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter;
import lombok.extern.slf4j.Slf4j;
import uk.org.openbanking.datamodel.vrp.*;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toFRAccountIdentifier;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentSupplementaryDataConverter.toOBSupplementaryData1;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPFinancialAgentConverter.toFRFinancialAgent;

@Slf4j
public class FRDomesticVRPConverters {

    // OB to FR
    public static FRDomesticVRPRequest toFRDomesticVRPRequest(
            OBDomesticVRPRequest obDomesticVRPRequest
    ) {
        FRDomesticVRPRequest frDomesticVRPRequest = obDomesticVRPRequest == null ? null : FRDomesticVRPRequest.builder()
                .data(toFRDomesticVRPRequestData(obDomesticVRPRequest.getData()))
                .risk(toFRRisk(obDomesticVRPRequest.getRisk()))
                .build();
        log.trace("toFRDomesticVRPRequest() converted OBDomesticVRPRequest to FRDomesticVrpRequest; '{}'",
                frDomesticVRPRequest);
        return frDomesticVRPRequest;
    }

    public static FRDomesticVRPRequest toFRDomesticVRPRequest(
            uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest obDomesticVRPRequest
    ) {
        FRDomesticVRPRequest frDomesticVRPRequest = obDomesticVRPRequest == null ? null : FRDomesticVRPRequest.builder()
                .data(toFRDomesticVRPRequestData(obDomesticVRPRequest.getData()))
                .risk(toFRRisk(obDomesticVRPRequest.getRisk()))
                .build();
        log.trace("toFRDomesticVRPRequest() converted OBDomesticVRPRequest to FRDomesticVrpRequest; '{}'",
                frDomesticVRPRequest);
        return frDomesticVRPRequest;
    }

    public static FRDomesticVRPRequestData toFRDomesticVRPRequestData(OBDomesticVRPRequestData obDomesticVRPRequestData){
        return obDomesticVRPRequestData == null ? null : FRDomesticVRPRequestData.builder()
                .consentId(obDomesticVRPRequestData.getConsentId())
                .initiation(FRWriteDomesticVRPDataInitiationConverter.toFRWriteDomesticVRPDataInitiation(obDomesticVRPRequestData.getInitiation()))
                .psuAuthenticationMethod(obDomesticVRPRequestData.getPsUAuthenticationMethod())
                .instruction(toFRDomesticVRPInstruction(obDomesticVRPRequestData.getInstruction()))
                .build();
    }

    public static FRDomesticVRPRequestData toFRDomesticVRPRequestData(uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequestData obDomesticVRPRequestData){
        return obDomesticVRPRequestData == null ? null : FRDomesticVRPRequestData.builder()
                .consentId(obDomesticVRPRequestData.getConsentId())
                .initiation(FRWriteDomesticVRPDataInitiationConverter.toFRWriteDomesticVRPDataInitiation(obDomesticVRPRequestData.getInitiation()))
                .psuAuthenticationMethod(obDomesticVRPRequestData.getPsUAuthenticationMethod())
                .instruction(toFRDomesticVRPInstruction(obDomesticVRPRequestData.getInstruction()))
                .psuInteractionType(toFRVRPInteractionType(obDomesticVRPRequestData.getPsUInteractionType()))
                .build();
    }

    public static FRVRPInteractionType toFRVRPInteractionType(OBVRPInteractionTypes interactionTypes) {
        return interactionTypes == null ? null : FRVRPInteractionType.fromValue(interactionTypes.getValue());
    }

    public static FRDomesticVRPInstruction toFRDomesticVRPInstruction(OBDomesticVRPInstruction instruction) {
        FRDomesticVRPInstruction frInstruction = FRDomesticVRPInstruction.builder()
                .creditorAccount(toFRAccountIdentifier(instruction.getCreditorAccount()))
                .instructionIdentification(instruction.getInstructionIdentification())
                .endToEndIdentification(instruction.getEndToEndIdentification())
                .creditorAgent(toFRFinancialAgent(instruction.getCreditorAgent()))
                .instructedAmount(toFRAmount(instruction.getInstructedAmount()))
                .localInstrument(instruction.getLocalInstrument())
                .remittanceInformation(FRRemittanceInformationConverter.toFRRemittanceInformation(instruction.getRemittanceInformation()))
                .supplementaryData(FRPaymentSupplementaryDataConverter.toFRSupplementaryData(instruction.getSupplementaryData()))
                .build();
        return frInstruction;
    }

    public static FRDomesticVRPInstruction toFRDomesticVRPInstruction(uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPInstruction instruction) {
        FRDomesticVRPInstruction frInstruction = FRDomesticVRPInstruction.builder()
                .creditorAccount(toFRAccountIdentifier(instruction.getCreditorAccount()))
                .instructionIdentification(instruction.getInstructionIdentification())
                .endToEndIdentification(instruction.getEndToEndIdentification())
                .creditorPostalAddress(toFRPostalAddress(instruction.getCreditorPostalAddress()))
                .instructedAmount(toFRAmount(instruction.getInstructedAmount()))
                .localInstrument(instruction.getLocalInstrument())
                .remittanceInformation(FRRemittanceInformationConverter.toFRRemittanceInformation(instruction.getRemittanceInformation()))
                .supplementaryData(FRPaymentSupplementaryDataConverter.toFRSupplementaryData(instruction.getSupplementaryData()))
                .build();
        return frInstruction;
    }

    public static FRPostalAddress toFRPostalAddress(uk.org.openbanking.datamodel.vrp.OBPostalAddress6 obPostalAddress6) {
        return obPostalAddress6 == null ? null : FRPostalAddress.builder()
                .addressType(toFRAddressTypeCode(obPostalAddress6.getAddressType()))
                .department(obPostalAddress6.getDepartment())
                .subDepartment(obPostalAddress6.getSubDepartment())
                .streetName(obPostalAddress6.getStreetName())
                .buildingNumber(obPostalAddress6.getBuildingNumber())
                .postCode(obPostalAddress6.getPostCode())
                .townName(obPostalAddress6.getTownName())
                .countrySubDivision(obPostalAddress6.getCountrySubDivision())
                .country(obPostalAddress6.getCountry())
                .addressLine(obPostalAddress6.getAddressLine())
                .build();
    }

    public static FRPostalAddress.AddressTypeCode toFRAddressTypeCode(OBAddressTypeCode obAddressTypeCode) {
        return obAddressTypeCode == null ? null : FRPostalAddress.AddressTypeCode.valueOf(obAddressTypeCode.name());
    }

    public static OBDomesticVRPRequest toOBDomesticVRPRequest(FRDomesticVRPRequest frDomesticVRPRequest){
        return frDomesticVRPRequest == null ? null : new OBDomesticVRPRequest()
                .data(toOBDomesticVRPRequestData(frDomesticVRPRequest.getData()))
                .risk(toOBRisk1(frDomesticVRPRequest.getRisk()));
    }

    public static uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest toOBDomesticVRPRequestv3_1_10(FRDomesticVRPRequest frDomesticVRPRequest){
        return frDomesticVRPRequest == null ? null : new uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest()
                .data(toOBDomesticVRPRequestDatav3_1_10(frDomesticVRPRequest.getData()))
                .risk(toOBRisk1(frDomesticVRPRequest.getRisk()));
    }

    public static OBDomesticVRPRequestData toOBDomesticVRPRequestData(FRDomesticVRPRequestData data){
        return data == null ? null : new OBDomesticVRPRequestData()
                .consentId(data.getConsentId())
                .initiation(toOBDomesticVRPInitiation(data.getInitiation()))
                .instruction(toOBDomesticVRPInstruction(data.getInstruction()));
    }

    public static uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequestData toOBDomesticVRPRequestDatav3_1_10(FRDomesticVRPRequestData data){
        return data == null ? null : new uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequestData()
                .consentId(data.getConsentId())
                .initiation(toOBDomesticVRPInitiationv3_1_10(data.getInitiation()))
                .instruction(toOBDomesticVRPInstructionv3_1_10(data.getInstruction()))
                .psUInteractionType(toOBVRPInteractionTypes(data.psuInteractionType))
                .psUAuthenticationMethod(data.psuAuthenticationMethod);
    }

    public static OBVRPInteractionTypes toOBVRPInteractionTypes(FRVRPInteractionType interactionType) {
        return interactionType == null ? null : OBVRPInteractionTypes.fromValue(interactionType.getValue());
    }

    public static OBDomesticVRPInitiation toOBDomesticVRPInitiation(FRWriteDomesticVRPDataInitiation initiation){
        return initiation == null ? null : new OBDomesticVRPInitiation()
                .creditorAccount(toOBCashAccountCreditor3(initiation.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(initiation.getCreditorAgent()))
                .debtorAccount(toOBCashAccountDebtorWithName(initiation.getDebtorAccount()))
                .remittanceInformation(
                        toOBDomesticVRPInitiationRemittanceInformation(initiation.getRemittanceInformation())
                );
    }

    public static OBDomesticVRPInstruction toOBDomesticVRPInstruction(FRDomesticVRPInstruction instruction){
        return instruction == null ? null : new OBDomesticVRPInstruction()
                .endToEndIdentification(instruction.getEndToEndIdentification())
                .instructionIdentification(instruction.getInstructionIdentification())
                .localInstrument(instruction.getLocalInstrument())
                .creditorAccount(toOBCashAccountCreditor3(instruction.getCreditorAccount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(instruction.getCreditorAgent()))
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(instruction.getInstructedAmount()))
                .remittanceInformation(toOBVRPRemittanceInformation(instruction.getRemittanceInformation()))
                .supplementaryData(toOBSupplementaryData1(instruction.getSupplementaryData()));
    }

    public static uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPInstruction toOBDomesticVRPInstructionv3_1_10(FRDomesticVRPInstruction instruction){
        return instruction == null ? null : new uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPInstruction()
                .endToEndIdentification(instruction.getEndToEndIdentification())
                .instructionIdentification(instruction.getInstructionIdentification())
                .localInstrument(instruction.getLocalInstrument())
                .creditorAccount(toOBCashAccountCreditor3(instruction.getCreditorAccount()))
                // TODO support mapping old data
                .creditorPostalAddress(toOBPostalAddress6(instruction.getCreditorPostalAddress()))
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount(instruction.getInstructedAmount()))
                .remittanceInformation(toOBVRPRemittanceInformation(instruction.getRemittanceInformation()))
                .supplementaryData(toOBSupplementaryData1(instruction.getSupplementaryData()));
    }

    public static OBVRPRemittanceInformation toOBVRPRemittanceInformation(FRRemittanceInformation remittanceInformation){
        return remittanceInformation == null ? null : new OBVRPRemittanceInformation()
                .reference(remittanceInformation.getReference())
                .unstructured(remittanceInformation.getUnstructured());
    }
}
