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
package com.forgerock.openbanking.aspsp.rs.api.payment;

import com.forgerock.openbanking.common.model.version.OBVersion;
import org.junit.Test;

import static com.forgerock.openbanking.aspsp.rs.api.payment.ApiVersionMatcher.getOBVersion;
import static com.forgerock.openbanking.common.model.version.OBVersion.v3_0;
import static com.forgerock.openbanking.common.model.version.OBVersion.v3_1_3;
import static com.forgerock.openbanking.common.model.version.OBVersion.v3_1_4;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

/**
 * Unit test for {@link ApiVersionMatcher}.
 */
public class ApiVersionMatcherTest {

    @Test
    public void shouldGetVersionGiven3_0() {
        // Given
        String requestUri = "/open-banking/v3.0/pisp";

        // When
        OBVersion version = getOBVersion(requestUri);

        // Then
        assertThat(version).isEqualTo(v3_0);
    }

    @Test
    public void shouldGetVersionGiven3_1_3() {
        // Given
        String requestUri = "/open-banking/v3.1.3/pisp";

        // When
        OBVersion version = getOBVersion(requestUri);

        // Then
        assertThat(version).isEqualTo(v3_1_3);
    }

    @Test
    public void shouldGetVersionGiven3_1_4() {
        // Given
        String requestUri = "/open-banking/v3.1.4/pisp";

        // When
        OBVersion version = getOBVersion(requestUri);

        // Then
        assertThat(version).isEqualTo(v3_1_4);
    }

    @Test
    public void shouldGetVersionGivenUnknownVersion() {
        // Given
        String requestUri = "/open-banking/v10.10.10/pisp";

        // When
        IllegalArgumentException exception = catchThrowableOfType(() -> getOBVersion(requestUri), IllegalArgumentException.class);

        // Then
        assertThat(exception.getMessage()).isEqualTo("Unknown version in request URI: " + requestUri);
    }

    @Test
    public void shouldFailToGetVersionGivenInvalidVersion() {
        // Given
        String requestUri = "/open-banking/123/pisp";

        // When
        IllegalArgumentException exception = catchThrowableOfType(() -> getOBVersion(requestUri), IllegalArgumentException.class);

        // Then
        assertThat(exception.getMessage()).isEqualTo("Unable to determine version from request URI: " + requestUri);
    }

}
