/**
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