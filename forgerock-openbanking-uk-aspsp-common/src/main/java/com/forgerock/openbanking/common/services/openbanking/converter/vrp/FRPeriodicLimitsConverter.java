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
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.forgerock.openbanking.common.services.openbanking.converter.vrp;

import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRPeriodicLimits;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPControlParametersPeriodicLimits;

import java.util.List;
import java.util.stream.Collectors;

public class FRPeriodicLimitsConverter {

    public static List<FRPeriodicLimits> toFRPeriodicLimits(List<OBDomesticVRPControlParametersPeriodicLimits> periodicLimits){
        return periodicLimits == null ? null : periodicLimits.stream().map(
                item -> FRPeriodicLimits.builder()
                        .amount(item.getAmount())
                        .currency(item.getCurrency())
                        .periodAlignment(FRPeriodicLimits.PeriodAlignmentEnum.fromValue(item.getPeriodAlignment().getValue()))
                        .periodType(FRPeriodicLimits.PeriodTypeEnum.fromValue(item.getPeriodType().getValue()))
                        .build()
        ).collect(Collectors.toList());
    }
}
