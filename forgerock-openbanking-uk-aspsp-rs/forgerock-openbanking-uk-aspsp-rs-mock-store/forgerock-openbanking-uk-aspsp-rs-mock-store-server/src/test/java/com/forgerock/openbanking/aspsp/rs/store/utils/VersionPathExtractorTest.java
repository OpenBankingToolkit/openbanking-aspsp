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
package com.forgerock.openbanking.aspsp.rs.store.utils;

import com.forgerock.openbanking.common.model.version.OBVersion;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionPathExtractorTest {

    @Test
    public void getVersionFromPath() {
        assertThat(VersionPathExtractor.getVersionFromPath("/open-banking/v1.1/aisp/account-consent/1")).isEqualTo(OBVersion.v1_1);
        assertThat(VersionPathExtractor.getVersionFromPath("/open-banking/v3.1/aisp/account-consent/1")).isEqualTo(OBVersion.v3_1);
        assertThat(VersionPathExtractor.getVersionFromPath("/open-banking/v3.1.2/pisp/domestic-payment-consent/1")).isEqualTo(OBVersion.v3_1_2);

        assertThat(VersionPathExtractor.getVersionFromPath("/open-banking/discovery")).isNull();
    }
}