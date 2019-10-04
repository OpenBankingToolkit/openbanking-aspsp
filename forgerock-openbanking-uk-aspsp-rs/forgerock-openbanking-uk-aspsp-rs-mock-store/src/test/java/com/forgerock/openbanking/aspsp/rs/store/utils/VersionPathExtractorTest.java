/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.utils;

import com.forgerock.openbanking.commons.model.version.OBVersion;
import org.junit.Test;

public class VersionPathExtractorTest {

    @Test
    public void getVersionFromPath() {
        assertThat(VersionPathExtractor.getVersionFromPath("/open-banking/v1.1/aisp/account-consent/1")).isEqualTo(OBVersion.v1_1);
        assertThat(VersionPathExtractor.getVersionFromPath("/open-banking/v3.1/aisp/account-consent/1")).isEqualTo(OBVersion.v3_1);
        assertThat(VersionPathExtractor.getVersionFromPath("/open-banking/v3.1.2/pisp/domestic-payment-consent/1")).isEqualTo(OBVersion.v3_1_2);

        assertThat(VersionPathExtractor.getVersionFromPath("/open-banking/discovery")).isNull();
    }
}