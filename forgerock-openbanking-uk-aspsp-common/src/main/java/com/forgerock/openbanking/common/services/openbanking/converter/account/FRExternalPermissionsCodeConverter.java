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

import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;
import java.util.stream.Collectors;

public class FRExternalPermissionsCodeConverter {

    // OB to FR
    public static List<FRExternalPermissionsCode> toFRExternalPermissionsCodeList(List<OBExternalPermissions1Code> permissions) {
        return permissions == null ? null : permissions.stream()
                .map(p -> toFRExternalPermissionsCode(p))
                .collect(Collectors.toList());
    }

    public static FRExternalPermissionsCode toFRExternalPermissionsCode(OBExternalPermissions1Code permission) {
        return permission == null ? null : FRExternalPermissionsCode.valueOf(permission.name());
    }

    // FR to OB
    public static List<OBExternalPermissions1Code> toOBExternalPermissions1CodeList(List<FRExternalPermissionsCode> permissions) {
        return permissions == null ? null : permissions.stream()
                .map(p -> toOBExternalPermissions1Code(p))
                .collect(Collectors.toList());
    }

    public static OBExternalPermissions1Code toOBExternalPermissions1Code(FRExternalPermissionsCode permission) {
        return permission == null ? null : OBExternalPermissions1Code.valueOf(permission.name());
    }
}
