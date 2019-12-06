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

import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Test;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AccountAccessConsentPermittedPermissionsFilterTest {

    @Test
    public void filter_disallow() {
        // Given
        AccountAccessConsentPermittedPermissionsFilter filter
                = new AccountAccessConsentPermittedPermissionsFilter(Collections.singletonList(OBExternalPermissions1Code.READACCOUNTSDETAIL.toString()));

        // When
        assertThatThrownBy(() ->
            filter.filter(Arrays.asList(OBExternalPermissions1Code.READACCOUNTSBASIC, OBExternalPermissions1Code.READACCOUNTSDETAIL))
        )
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage("One or more permissions requested in the submitted account access consent request are not permitted on this sandbox. The permissions that are not permitted are: [ReadAccountsDetail]");
    }

    @Test
    public void filter_allow() throws Exception {
        // Given
        AccountAccessConsentPermittedPermissionsFilter filter
                = new AccountAccessConsentPermittedPermissionsFilter(Collections.singletonList(OBExternalPermissions1Code.READACCOUNTSDETAIL.toString()));

        // When
        filter.filter(Arrays.asList(OBExternalPermissions1Code.READACCOUNTSBASIC, OBExternalPermissions1Code.READPRODUCTS));

        // Then no exception is thrown
    }

    @Test
    public void filter_empty_allowAll() throws Exception {
        // Given
        AccountAccessConsentPermittedPermissionsFilter filter
                = new AccountAccessConsentPermittedPermissionsFilter(Collections.emptyList());

        // When
        filter.filter(Arrays.asList(OBExternalPermissions1Code.values()));

        // Then no exception is thrown
    }

    @Test
    public void filter_invalid_permission_illegalStateException() {
        // Given
        assertThatThrownBy(() ->
            new AccountAccessConsentPermittedPermissionsFilter(Collections.singletonList("MadeUpPermissionName"))
        ).isInstanceOf(IllegalStateException.class);
    }
}