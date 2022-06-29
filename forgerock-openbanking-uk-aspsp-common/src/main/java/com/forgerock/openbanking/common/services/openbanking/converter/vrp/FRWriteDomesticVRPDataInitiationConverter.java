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
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRWriteDomesticVRPDataInitiation;
import com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentPostalAddressConverter;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRRemittanceInformationConverter;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPInitiation;

public class FRWriteDomesticVRPDataInitiationConverter {

    public static FRWriteDomesticVRPDataInitiation toFRWriteDomesticVRPDataInitiation(
            OBDomesticVRPInitiation initiation
    ) {
        return initiation == null ? null : FRWriteDomesticVRPDataInitiation.builder()
                .creditorAccount(FRAccountIdentifierConverter.toFRAccountIdentifier(initiation.getCreditorAccount()))
                .debtorAccount(FRAccountIdentifierConverter.toFRAccountIdentifier(initiation.getDebtorAccount()))
                .creditorAgent(FRDomesticVRPFinancialAgentConverter.toFRFinancialAgent(initiation.getCreditorAgent()))
                .remittanceInformation(FRRemittanceInformationConverter.toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .build();
    }

    public static FRWriteDomesticVRPDataInitiation toFRWriteDomesticVRPDataInitiation(
            uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPInitiation initiation
    ) {
        return initiation == null ? null : FRWriteDomesticVRPDataInitiation.builder()
                .creditorAccount(FRAccountIdentifierConverter.toFRAccountIdentifier(initiation.getCreditorAccount()))
                .debtorAccount(FRAccountIdentifierConverter.toFRAccountIdentifier(initiation.getDebtorAccount()))
                .creditorPostalAddress(FRPaymentPostalAddressConverter.toFRPostalAddress(initiation.getCreditorPostalAddress()))
                .remittanceInformation(FRRemittanceInformationConverter.toFRRemittanceInformation(initiation.getRemittanceInformation()))
                .build();
    }
}
