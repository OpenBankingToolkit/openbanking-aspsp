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
package com.forgerock.openbanking.common.services.openbanking.converter.account;

import com.forgerock.openbanking.common.model.openbanking.domain.account.FRReadConsentResponse;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRReadConsentResponseData;
import uk.org.openbanking.datamodel.account.OBReadConsentResponse1;
import uk.org.openbanking.datamodel.account.OBReadConsentResponse1Data;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountRiskConverter.toFRAccountRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountRiskConverter.toOBRisk2;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRLinksConverter.toFRLinks;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRLinksConverter.toLinks;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRMetaConverter.toFRMeta;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRMetaConverter.toMeta;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRReadResponseConverter.toFRExternalPermissionsCodeList;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRReadResponseConverter.toFRExternalRequestStatusCode;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRReadResponseConverter.toOBExternalPermissions1CodeList;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRReadResponseConverter.toOBExternalRequestStatus1Code;

public class FRReadConsentResponseConverter {

    public static FRReadConsentResponse toFRReadConsentResponse(OBReadConsentResponse1 response) {
        return response == null ? null : FRReadConsentResponse.builder()
                .data(toFRReadConsentResponseData(response.getData()))
                .risk(toFRAccountRisk(response.getRisk()))
                .links(toFRLinks(response.getLinks()))
                .meta(toFRMeta(response.getMeta()))
                .build();
    }

    public static FRReadConsentResponseData toFRReadConsentResponseData(OBReadConsentResponse1Data data) {
        return data == null ? null : FRReadConsentResponseData.builder()
                .consentId(data.getConsentId())
                .creationDateTime(data.getCreationDateTime())
                .status(toFRExternalRequestStatusCode(data.getStatus()))
                .statusUpdateDateTime(data.getStatusUpdateDateTime())
                .permissions(toFRExternalPermissionsCodeList(data.getPermissions()))
                .expirationDateTime(data.getExpirationDateTime())
                .transactionFromDateTime(data.getTransactionFromDateTime())
                .transactionToDateTime(data.getTransactionToDateTime())
                .build();
    }

    // FR to OB
    public static OBReadConsentResponse1 toOBReadConsentResponse1(FRReadConsentResponse accountAccessConsent) {
        return accountAccessConsent == null ? null : new OBReadConsentResponse1()
                .data(toOBReadConsentResponse1Data(accountAccessConsent.getData()))
                .risk(toOBRisk2(accountAccessConsent.getRisk()))
                .links(toLinks(accountAccessConsent.getLinks()))
                .meta(toMeta(accountAccessConsent.getMeta()));
    }

    public static OBReadConsentResponse1Data toOBReadConsentResponse1Data(FRReadConsentResponseData data) {
        return data == null ? null : new OBReadConsentResponse1Data()
        .consentId(data.getConsentId())
        .creationDateTime(data.getCreationDateTime())
        .status(toOBExternalRequestStatus1Code(data.getStatus()))
        .statusUpdateDateTime(data.getStatusUpdateDateTime())
        .permissions(toOBExternalPermissions1CodeList(data.getPermissions()))
        .expirationDateTime(data.getExpirationDateTime())
        .transactionFromDateTime(data.getTransactionFromDateTime())
        .transactionToDateTime(data.getTransactionToDateTime());
    }
}
