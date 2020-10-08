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

import com.forgerock.openbanking.common.model.openbanking.domain.account.FRReadDataResponse;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRReadResponse;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalRequestStatusCode;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;
import uk.org.openbanking.datamodel.account.OBReadDataResponse1;
import uk.org.openbanking.datamodel.account.OBReadResponse1;
import uk.org.openbanking.datamodel.account.OBRisk2;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountRiskConverter.toFRAccountRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountRiskConverter.toOBRisk2;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRLinksConverter.toFRLinks;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRLinksConverter.toLinks;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRMetaConverter.toFRMeta;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRMetaConverter.toMeta;

public class FRReadResponseConverter {

    // OB to FR
    public static FRReadResponse toFRReadResponse(OBReadResponse1 response) {
        return response == null ? null : FRReadResponse.builder()
                .data(toFRReadDataResponse(response.getData()))
                .risk(toFRAccountRisk((OBRisk2)response.getRisk()))
                .links(toFRLinks(response.getLinks()))
                .meta(toFRMeta(response.getMeta()))
                .build();
    }

    public static FRReadDataResponse toFRReadDataResponse(OBReadDataResponse1 data) {
        return data == null ? null : FRReadDataResponse.builder()
                .accountRequestId(data.getAccountRequestId())
                .status(toFRExternalRequestStatusCode(data.getStatus()))
                .creationDateTime(data.getCreationDateTime())
                .permissions(toFRExternalPermissionsCodeList(data.getPermissions()))
                .expirationDateTime(data.getExpirationDateTime())
                .transactionFromDateTime(data.getTransactionFromDateTime())
                .transactionToDateTime(data.getTransactionToDateTime())
                .build();
    }

    // TODO #296 - move to it's own converter
    public static List<FRExternalPermissionsCode> toFRExternalPermissionsCodeList(List<OBExternalPermissions1Code> permissions) {
        return permissions == null ? null : permissions.stream()
                .map(p -> toFRExternalPermissionsCode(p))
                .collect(Collectors.toList());
    }

    // TODO #296 - move to it's own converter
    public static FRExternalPermissionsCode toFRExternalPermissionsCode(OBExternalPermissions1Code permission) {
        return permission == null ? null : FRExternalPermissionsCode.valueOf(permission.name());
    }

    // TODO #296 - move to it's own converter?
    public static FRExternalRequestStatusCode toFRExternalRequestStatusCode(OBExternalRequestStatus1Code status) {
        return status == null ? null : FRExternalRequestStatusCode.valueOf(status.name());
    }

    // FR to OB
    public static OBReadResponse1 toOBReadResponse1(FRReadResponse response) {
        return response == null ? null : new OBReadResponse1()
                .data(toOBReadDataResponse1(response.getData()))
                .risk(toOBRisk2(response.getRisk()))
                .links(toLinks(response.getLinks()))
                .meta(toMeta(response.getMeta()));
    }

    public static OBReadDataResponse1 toOBReadDataResponse1(FRReadDataResponse data) {
        return data == null ? null : new OBReadDataResponse1()
                .accountRequestId(data.getAccountRequestId())
                .status(toOBExternalRequestStatus1Code(data.getStatus()))
                .creationDateTime(data.getCreationDateTime())
                .permissions(toOBExternalPermissions1CodeList(data.getPermissions()))
                .expirationDateTime(data.getExpirationDateTime())
                .transactionFromDateTime(data.getTransactionFromDateTime())
                .transactionToDateTime(data.getTransactionToDateTime());
    }

    // TODO #296 - move to it's own converter
    public static List<OBExternalPermissions1Code> toOBExternalPermissions1CodeList(List<FRExternalPermissionsCode> permissions) {
        return permissions == null ? null : permissions.stream()
                .map(p -> toOBExternalPermissions1Code(p))
                .collect(Collectors.toList());
    }

    // TODO #296 - move to it's own converter
    public static OBExternalPermissions1Code toOBExternalPermissions1Code(FRExternalPermissionsCode permission) {
        return permission == null ? null : OBExternalPermissions1Code.valueOf(permission.name());
    }

    // TODO #296 - move to it's own converter
    public static OBExternalRequestStatus1Code toOBExternalRequestStatus1Code(FRExternalRequestStatusCode status) {
        return status == null ? null : OBExternalRequestStatus1Code.valueOf(status.name());
    }
}
