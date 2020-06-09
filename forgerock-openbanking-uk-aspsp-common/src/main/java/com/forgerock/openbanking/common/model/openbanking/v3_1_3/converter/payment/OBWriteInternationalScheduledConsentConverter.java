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

import uk.org.openbanking.datamodel.payment.OBExternalPermissions2Code;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalScheduledConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalScheduledConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsent4Data;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBConsentAuthorisationConverter.toOBAuthorisation1;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBConsentAuthorisationConverter.toOBWriteDomesticConsent3DataAuthorisation;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalScheduledConverter.toOBInternationalScheduled1;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalScheduledConverter.toOBWriteInternationalScheduled3DataInitiation;

public class OBWriteInternationalScheduledConsentConverter {

    public static OBWriteInternationalScheduledConsent1 toOBWriteInternationalScheduledConsent1(OBWriteInternationalScheduledConsent4 obWriteInternationalScheduledConsent4) {
        return (new OBWriteInternationalScheduledConsent1()
                .data(toOBWriteDataInternationalScheduledConsent1(obWriteInternationalScheduledConsent4.getData()))
                .risk(obWriteInternationalScheduledConsent4.getRisk())
        );
    }

    public static OBWriteInternationalScheduledConsent4 toOBWriteInternationalScheduledConsent4(OBWriteInternationalScheduledConsent1 obWriteInternationalScheduledConsent1) {
        return (new OBWriteInternationalScheduledConsent4())
                .data(toOBWriteInternationalScheduledConsent4Data(obWriteInternationalScheduledConsent1.getData()))
                .risk(obWriteInternationalScheduledConsent1.getRisk());
    }

    public static OBWriteInternationalScheduledConsent4 toOBWriteInternationalScheduledConsent4(OBWriteInternationalScheduledConsent2 obWriteInternationalScheduledConsent2) {
        return (new OBWriteInternationalScheduledConsent4())
                .data(toOBWriteInternationalScheduledConsent4Data(obWriteInternationalScheduledConsent2.getData()))
                .risk(obWriteInternationalScheduledConsent2.getRisk());
    }

    private static OBWriteDataInternationalScheduledConsent1 toOBWriteDataInternationalScheduledConsent1(OBWriteInternationalScheduledConsent4Data data) {
        return data == null ? null : (new OBWriteDataInternationalScheduledConsent1())
                .permission(toOBExternalPermissions2Code(data.getPermission()))
                .initiation(toOBInternationalScheduled1(data.getInitiation()))
                .authorisation(toOBAuthorisation1(data.getAuthorisation()));
    }

    public static OBWriteInternationalScheduledConsent4Data toOBWriteInternationalScheduledConsent4Data(OBWriteDataInternationalScheduledConsent1 data) {
        return data == null ? null : (new OBWriteInternationalScheduledConsent4Data())
                .permission(toPermissionEnum(data.getPermission()))
                .initiation(toOBWriteInternationalScheduled3DataInitiation(data.getInitiation()))
                .authorisation(toOBWriteDomesticConsent3DataAuthorisation(data.getAuthorisation()));
    }

    public static OBWriteInternationalScheduledConsent4Data toOBWriteInternationalScheduledConsent4Data(OBWriteDataInternationalScheduledConsent2 data) {
        return data == null ? null : (new OBWriteInternationalScheduledConsent4Data())
                .permission(toPermissionEnum(data.getPermission()))
                .initiation(toOBWriteInternationalScheduled3DataInitiation(data.getInitiation()))
                .authorisation(toOBWriteDomesticConsent3DataAuthorisation(data.getAuthorisation()));
    }

    public static OBWriteInternationalScheduledConsent4Data.PermissionEnum toPermissionEnum(OBExternalPermissions2Code permission) {
        return permission == null ? null : OBWriteInternationalScheduledConsent4Data.PermissionEnum.valueOf(permission.name());
    }

    public static OBExternalPermissions2Code toOBExternalPermissions2Code(OBWriteInternationalScheduledConsent4Data.PermissionEnum permission) {
        return permission == null ? null : OBExternalPermissions2Code.valueOf(permission.name());
    }
}
