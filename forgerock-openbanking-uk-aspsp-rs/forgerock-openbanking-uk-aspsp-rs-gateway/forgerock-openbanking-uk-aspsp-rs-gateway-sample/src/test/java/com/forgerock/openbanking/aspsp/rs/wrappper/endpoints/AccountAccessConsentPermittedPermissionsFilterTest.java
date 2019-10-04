/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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