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
package com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment;

import uk.org.openbanking.datamodel.payment.*;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsent5Data.PermissionEnum;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBConsentAuthorisationConverter.toOBWriteDomesticConsent3DataAuthorisation;

public class OBWriteInternationalStandingOrderConsentConverter {

    public static OBWriteInternationalStandingOrderConsent5 toOBWriteInternationalStandingOrderConsent5(OBWriteInternationalStandingOrderConsent2 consent2) {
        return (new OBWriteInternationalStandingOrderConsent5())
                .data(toOBWriteInternationalStandingOrderConsent5Data(consent2.getData()))
                .risk(consent2.getRisk());
    }

    public static OBWriteInternationalStandingOrderConsent5 toOBWriteInternationalStandingOrderConsent5(OBWriteInternationalStandingOrderConsent3 consent3) {
        return (new OBWriteInternationalStandingOrderConsent5())
                .data(toOBWriteInternationalStandingOrderConsent5Data(consent3.getData()))
                .risk(consent3.getRisk());
    }

    public static OBWriteInternationalStandingOrderConsent5Data toOBWriteInternationalStandingOrderConsent5Data(OBWriteDataInternationalStandingOrderConsent2 data) {
        return data == null ? null : (new OBWriteInternationalStandingOrderConsent5Data())
                .permission(toPermissionEnum(data.getPermission()))
                .initiation(OBInternationalStandingOrderConverter.toOBWriteInternationalStandingOrder4DataInitiation(data.getInitiation()))
                .authorisation(toOBWriteDomesticConsent3DataAuthorisation(data.getAuthorisation()));
    }

    public static OBWriteInternationalStandingOrderConsent5Data toOBWriteInternationalStandingOrderConsent5Data(OBWriteDataInternationalStandingOrderConsent3 data) {
        return data == null ? null : (new OBWriteInternationalStandingOrderConsent5Data())
                .permission(toPermissionEnum(data.getPermission()))
                .initiation(OBInternationalStandingOrderConverter.toOBWriteInternationalStandingOrder4DataInitiation(data.getInitiation()))
                .authorisation(toOBWriteDomesticConsent3DataAuthorisation(data.getAuthorisation()));
    }

    public static PermissionEnum toPermissionEnum(OBExternalPermissions2Code permission) {
        return permission == null ? null : PermissionEnum.valueOf(permission.name());
    }


    public static OBExternalPermissions2Code toOBExternalPermissions2Code(PermissionEnum permission) {
        return permission == null ? null : OBExternalPermissions2Code.valueOf(permission.name());
    }
}
