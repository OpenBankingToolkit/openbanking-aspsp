/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import org.junit.Test;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadData1;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountAccessConsentBasicAndDetailPermissionsFilterTest {

    private AccountAccessConsentBasicAndDetailPermissionsFilter filter = new AccountAccessConsentBasicAndDetailPermissionsFilter();

    @Test
    public void filterObData() {
        // Given
        OBReadData1 obData = new OBReadData1();
        obData.setPermissions(Arrays.asList(OBExternalPermissions1Code.values()));

        // When
        filter.filter(obData);

        // Then
        assertThat(obData.getPermissions()).containsExactlyInAnyOrder(
                OBExternalPermissions1Code.READACCOUNTSDETAIL,
                OBExternalPermissions1Code.READBALANCES,
                OBExternalPermissions1Code.READBENEFICIARIESDETAIL,
                OBExternalPermissions1Code.READDIRECTDEBITS,
                OBExternalPermissions1Code.READOFFERS,
                OBExternalPermissions1Code.READPAN,
                OBExternalPermissions1Code.READPARTY,
                OBExternalPermissions1Code.READPARTYPSU,
                OBExternalPermissions1Code.READPRODUCTS,
                OBExternalPermissions1Code.READSCHEDULEDPAYMENTSDETAIL,
                OBExternalPermissions1Code.READSTANDINGORDERSDETAIL,
                OBExternalPermissions1Code.READSTATEMENTSDETAIL,
                OBExternalPermissions1Code.READTRANSACTIONSCREDITS,
                OBExternalPermissions1Code.READTRANSACTIONSDEBITS,
                OBExternalPermissions1Code.READTRANSACTIONSDETAIL
        );
    }

    @Test
    public void filterAll() {
        // Given
        final List<OBExternalPermissions1Code> permissions = Arrays.asList(
                OBExternalPermissions1Code.values()
        );

        // When
        final Collection<OBExternalPermissions1Code> filteredPermissions = filter.filter(permissions);

        // Then
        assertThat(filteredPermissions).containsExactlyInAnyOrder(
                OBExternalPermissions1Code.READACCOUNTSDETAIL,
                OBExternalPermissions1Code.READBALANCES,
                OBExternalPermissions1Code.READBENEFICIARIESDETAIL,
                OBExternalPermissions1Code.READDIRECTDEBITS,
                OBExternalPermissions1Code.READOFFERS,
                OBExternalPermissions1Code.READPAN,
                OBExternalPermissions1Code.READPARTY,
                OBExternalPermissions1Code.READPARTYPSU,
                OBExternalPermissions1Code.READPRODUCTS,
                OBExternalPermissions1Code.READSCHEDULEDPAYMENTSDETAIL,
                OBExternalPermissions1Code.READSTANDINGORDERSDETAIL,
                OBExternalPermissions1Code.READSTATEMENTSDETAIL,
                OBExternalPermissions1Code.READTRANSACTIONSCREDITS,
                OBExternalPermissions1Code.READTRANSACTIONSDEBITS,
                OBExternalPermissions1Code.READTRANSACTIONSDETAIL
        );
    }

    @Test
    public void filter_empty() {
        assertThat(filter.filter(Collections.emptyList())).isEmpty();
    }

}