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

import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPControlParameters;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRVRPInteractionType;
import com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentSupplementaryDataConverter;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPControlParameters;
import uk.org.openbanking.datamodel.vrp.OBVRPInteractionTypes;

import java.util.List;
import java.util.stream.Collectors;

public class FRDomesticVRPControlParametersConverter {

    public static FRDomesticVRPControlParameters toFRDomesticVRPControlParameters(
            OBDomesticVRPControlParameters controlParameters
    ) {
        return controlParameters == null ? null : FRDomesticVRPControlParameters.builder()
                .psuAuthenticationMethods(controlParameters.getPsUAuthenticationMethods())
                .vrpType(controlParameters.getVrPType())
                .validFromDateTime(controlParameters.getValidFromDateTime())
                .validToDateTime(controlParameters.getValidToDateTime())
                .maximumIndividualAmount(FRAmountConverter.toFRAmount(controlParameters.getMaximumIndividualAmount()))
                .periodicLimits(FRPeriodicLimitsConverter.toFRPeriodicLimits(controlParameters.getPeriodicLimits()))
                .supplementaryData(FRPaymentSupplementaryDataConverter.toFRSupplementaryData(controlParameters.getSupplementaryData()))
                .build();
    }

    public static FRDomesticVRPControlParameters toFRDomesticVRPControlParameters(
            uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPControlParameters controlParameters
    ) {
        return controlParameters == null ? null : FRDomesticVRPControlParameters.builder()
                .psuAuthenticationMethods(controlParameters.getPsUAuthenticationMethods())
                .vrpType(controlParameters.getVrPType())
                .validFromDateTime(controlParameters.getValidFromDateTime())
                .validToDateTime(controlParameters.getValidToDateTime())
                .maximumIndividualAmount(FRAmountConverter.toFRAmount(controlParameters.getMaximumIndividualAmount()))
                .periodicLimits(FRPeriodicLimitsConverter.toFRPeriodicLimits(controlParameters.getPeriodicLimits()))
                .supplementaryData(FRPaymentSupplementaryDataConverter.toFRSupplementaryData(controlParameters.getSupplementaryData()))
                .psUInteractionTypes(toFRVRPInteractionTypes(controlParameters.getPsUInteractionTypes()))
                .build();

    }

    public static List<FRVRPInteractionType> toFRVRPInteractionTypes(List<OBVRPInteractionTypes> psUInteractionTypes) {
        return psUInteractionTypes == null ? null : psUInteractionTypes.stream().map(it -> FRVRPInteractionType.fromValue(it.getValue())).collect(Collectors.toList());
    }

    public static List<OBVRPInteractionTypes> toOBVRPInteractionTypes(List<FRVRPInteractionType> interactionTypes) {
        return interactionTypes == null ? null : interactionTypes.stream().map(it -> OBVRPInteractionTypes.fromValue(it.getValue())).collect(Collectors.toList());
    }
}
