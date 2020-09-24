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
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRPermission;
import uk.org.openbanking.datamodel.payment.*;

public class FRPermissionConverter {

    // OB to FR
    public static FRPermission toFRPermission(OBExternalPermissions2Code permission) {
        return permission == null ? null : FRPermission.valueOf(permission.name());
    }

    public static FRPermission toFRPermission(OBWriteDomesticScheduledConsent4Data.PermissionEnum permission) {
        return permission == null ? null : FRPermission.valueOf(permission.name());
    }

    public static FRPermission toFRPermission(OBWriteDomesticScheduledConsent3Data.PermissionEnum permission) {
        return permission == null ? null : FRPermission.valueOf(permission.name());
    }

    public static FRPermission toFRPermission(OBWriteDomesticStandingOrderConsent4Data.PermissionEnum permission) {
        return permission == null ? null : FRPermission.valueOf(permission.name());
    }

    public static FRPermission toFRPermission(OBWriteDomesticStandingOrderConsent5Data.PermissionEnum permission) {
        return permission == null ? null : FRPermission.valueOf(permission.name());
    }

    public static FRPermission toFRPermission(OBWriteInternationalScheduledConsent4Data.PermissionEnum permission) {
        return permission == null ? null : FRPermission.valueOf(permission.name());
    }

    public static FRPermission toFRPermission(OBWriteInternationalScheduledConsent5Data.PermissionEnum permission) {
        return permission == null ? null : FRPermission.valueOf(permission.name());
    }

    public static FRPermission toFRPermission(OBWriteInternationalStandingOrderConsent5Data.PermissionEnum permission) {
        return permission == null ? null : FRPermission.valueOf(permission.name());
    }

    public static FRPermission toFRPermission(OBWriteInternationalStandingOrderConsent6Data.PermissionEnum permission) {
        return permission == null ? null : FRPermission.valueOf(permission.name());
    }

    // FR to OB
    public static OBWriteDomesticScheduledConsentResponse5Data.PermissionEnum toOBWriteDomesticScheduledConsentResponse5DataPermission(FRPermission permission) {
        return permission == null ? null : OBWriteDomesticScheduledConsentResponse5Data.PermissionEnum.valueOf(permission.name());
    }

    public static OBWriteDomesticStandingOrderConsentResponse6Data.PermissionEnum toOBWriteDomesticStandingOrderConsentResponse6DataPermission(FRPermission permission) {
        return permission == null ? null : OBWriteDomesticStandingOrderConsentResponse6Data.PermissionEnum.valueOf(permission.name());
    }

    public static OBWriteInternationalScheduledConsentResponse6Data.PermissionEnum toOBWriteInternationalScheduledConsentResponse6DataPermission(FRPermission permission) {
        return permission == null ? null : OBWriteInternationalScheduledConsentResponse6Data.PermissionEnum.valueOf(permission.name());
    }

    public static OBWriteInternationalStandingOrderConsentResponse7Data.PermissionEnum toOBWriteInternationalStandingOrderConsentResponse7DataPermission(FRPermission permission) {
        return permission == null ? null : OBWriteInternationalStandingOrderConsentResponse7Data.PermissionEnum.valueOf(permission.name());
    }

    public static OBWriteDomesticScheduledConsentResponse3Data.PermissionEnum toOBWriteDomesticScheduledConsentResponse3DataPermission(FRPermission permission) {
        return permission == null ? null : OBWriteDomesticScheduledConsentResponse3Data.PermissionEnum.valueOf(permission.name());
    }

    public static OBWriteDomesticStandingOrderConsentResponse4Data.PermissionEnum toOBWriteDomesticStandingOrderConsentResponse4DataPermission(FRPermission permission) {
        return permission == null ? null : OBWriteDomesticStandingOrderConsentResponse4Data.PermissionEnum.valueOf(permission.name());
    }

    public static OBWriteInternationalScheduledConsentResponse4Data.PermissionEnum toOBWriteInternationalScheduledConsentResponse4DataPermission(FRPermission permission) {
        return permission == null ? null : OBWriteInternationalScheduledConsentResponse4Data.PermissionEnum.valueOf(permission.name());
    }

    public static OBWriteInternationalStandingOrderConsentResponse5Data.PermissionEnum toOBWriteInternationalStandingOrderConsentResponse5DataPermission(FRPermission permission) {
        return permission == null ? null : OBWriteInternationalStandingOrderConsentResponse5Data.PermissionEnum.valueOf(permission.name());
    }

    public static OBExternalPermissions2Code toOBExternalPermissions2Code(FRPermission permission) {
        return permission == null ? null : OBExternalPermissions2Code.valueOf(permission.name());
    }
}
