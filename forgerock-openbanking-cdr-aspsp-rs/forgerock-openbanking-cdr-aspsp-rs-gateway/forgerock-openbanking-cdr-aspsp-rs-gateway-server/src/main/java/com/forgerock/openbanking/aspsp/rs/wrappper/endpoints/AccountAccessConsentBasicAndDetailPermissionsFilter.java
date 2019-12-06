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
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadData1;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This filter is to detect and remove any 'basic' account consent permissions when the 'detail' permission has also been requested.
 * Requesting both can produce unusual results in the data.
 *
 * e.g. [OBExternalPermissions1Code.READACCOUNTSDETAIL, OBExternalPermissions1Code.READACCOUNTSBASIC]
 * -> [OBExternalPermissions1Code.READACCOUNTSDETAIL]
 */
@Component
@Slf4j
public class AccountAccessConsentBasicAndDetailPermissionsFilter {

    // Where a BASIC and a DETAIL exist for the same type - they should be added here so we can remove basic if detail is already requests.
    // This only applies if DETAIL has all the data fields of BASIC plus some extra fields
    private static final ImmutableMap<OBExternalPermissions1Code, OBExternalPermissions1Code> DETAIL_TO_BASIC_PAIRS
            = ImmutableMap.<OBExternalPermissions1Code, OBExternalPermissions1Code>builder()
            .put(OBExternalPermissions1Code.READACCOUNTSDETAIL, OBExternalPermissions1Code.READACCOUNTSBASIC)
            .put(OBExternalPermissions1Code.READTRANSACTIONSDETAIL, OBExternalPermissions1Code.READTRANSACTIONSBASIC)
            .put(OBExternalPermissions1Code.READBENEFICIARIESDETAIL, OBExternalPermissions1Code.READBENEFICIARIESBASIC)
            .put(OBExternalPermissions1Code.READSTATEMENTSDETAIL, OBExternalPermissions1Code.READSTATEMENTSBASIC)
            .put(OBExternalPermissions1Code.READSTANDINGORDERSDETAIL, OBExternalPermissions1Code.READSTANDINGORDERSBASIC)
            .put(OBExternalPermissions1Code.READSCHEDULEDPAYMENTSDETAIL, OBExternalPermissions1Code.READSCHEDULEDPAYMENTSBASIC)
            .build();

    public final void filter(final OBReadData1 obReadData) {
        obReadData.setPermissions(filter(obReadData.getPermissions()));
    }

    public final List<OBExternalPermissions1Code> filter(final List<OBExternalPermissions1Code> permissions) {
        log.debug("Filtering permissions for duplicate Basic/Detail types {}", permissions);
        if (CollectionUtils.isEmpty(permissions)) {
            return permissions;
        }

        Set<OBExternalPermissions1Code> permissionsToRemove = permissions.stream()
                .filter(permission -> DETAIL_TO_BASIC_PAIRS.keySet().contains(permission))
                .map(DETAIL_TO_BASIC_PAIRS::get)
                .collect(Collectors.toSet());
        log.debug("Removing permissions: {}", permissionsToRemove);

        return permissions.stream()
                .filter(permission -> !permissionsToRemove.contains(permission))
                .collect(Collectors.toList());
    }
}
