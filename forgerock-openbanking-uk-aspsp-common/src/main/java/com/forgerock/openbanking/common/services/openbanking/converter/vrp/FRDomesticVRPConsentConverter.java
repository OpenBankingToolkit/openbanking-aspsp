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

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetails;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetailsData;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentRequest;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentRequestData;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
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


}
