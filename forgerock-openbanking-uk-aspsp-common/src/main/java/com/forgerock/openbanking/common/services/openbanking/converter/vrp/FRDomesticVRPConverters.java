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
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.*;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentSupplementaryDataConverter;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter;
import lombok.extern.slf4j.Slf4j;
import uk.org.openbanking.datamodel.vrp.OBAddressTypeCode;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPInitiation;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPInstruction;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPRequest;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPRequestData;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toFRAccountIdentifier;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
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

    public static FRDomesticVRPRequestData toFRDomesticVRPRequestData(OBDomesticVRPRequestData obDomesticVRPRequestData){
        return obDomesticVRPRequestData == null ? null : FRDomesticVRPRequestData.builder()
                .consentId(obDomesticVRPRequestData.getConsentId())
                .initiation(toFRDomesticVRPInitiation(obDomesticVRPRequestData.getInitiation()))
                .psuAuthenticationMethod(obDomesticVRPRequestData.getPsUAuthenticationMethod())
                .instruction(toOBDomesticVRPRequestDataInstruction(obDomesticVRPRequestData.getInstruction()))
                .build();
    }

    public static FRDomesticVRPInstruction toOBDomesticVRPRequestDataInstruction(OBDomesticVRPInstruction instruction) {
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



    public static FRDomesticVRPDataInitiation toFRDomesticVRPInitiation(OBDomesticVRPInitiation initiation) {
        FRDomesticVRPDataInitiation frInitiation = FRWriteDomesticVRPDataInitiation.builder()
                .creditorAccount(toFRAccountIdentifier(initiation.getCreditorAccount()))
                .debtorAccount(toFRAccountIdentifier(initiation.getDebtorAccount()))
                .creditorAgent(toFRFinancialAgent(initiation.getCreditorAgent()))
                .remittanceInformation(FRRemittanceInformationConverter.toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .build();
        return frInitiation;
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
}
