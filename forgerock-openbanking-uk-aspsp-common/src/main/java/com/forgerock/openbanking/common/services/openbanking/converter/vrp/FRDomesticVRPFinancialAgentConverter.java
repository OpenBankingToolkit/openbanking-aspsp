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

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAgent;
import uk.org.openbanking.datamodel.vrp.OBBranchAndFinancialInstitutionIdentification6;

import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConverters.toFRPostalAddress;

public class FRDomesticVRPFinancialAgentConverter {

    public static FRFinancialAgent toFRFinancialAgent(OBBranchAndFinancialInstitutionIdentification6 agent) {
        return agent == null ? null : FRFinancialAgent.builder()
                .identification(agent.getIdentification())
                .name(agent.getName())
                .schemeName(agent.getSchemeName())
                .postalAddress(toFRPostalAddress(agent.getPostalAddress()))
                .build();
    }
}
