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
package com.forgerock.openbanking.aspsp.rs.api.payment.verifier;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UKMobilePhoneFormatTest {

    @Test
    public void isValid() {
        assertThat(UKMobilePhoneFormat.isValid("notANumber")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid(" ")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid(null)).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("+557123456789")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("1171234567890")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("-4471234567890")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("771234567890")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("77AAAAAAAAAA")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("++44712345678")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("+44712345678+")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("+447123-5689")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("*44712345689")).isFalse();

        // 0
        assertThat(UKMobilePhoneFormat.isValid("07123456789")).isTrue();
        assertThat(UKMobilePhoneFormat.isValid("07123456789  ")).isTrue();
        assertThat(UKMobilePhoneFormat.isValid("0712345678")).isFalse(); // too short
        assertThat(UKMobilePhoneFormat.isValid("071234567890")).isFalse(); // too long

        // +44
        assertThat(UKMobilePhoneFormat.isValid("+447123456789")).isTrue();
        assertThat(UKMobilePhoneFormat.isValid("+44712345678")).isFalse(); // too short
        assertThat(UKMobilePhoneFormat.isValid("+4471234567890")).isFalse(); // too long

        // 0044
        assertThat(UKMobilePhoneFormat.isValid("00447123456789")).isTrue();
        assertThat(UKMobilePhoneFormat.isValid("0044712345678")).isFalse(); // too short
        assertThat(UKMobilePhoneFormat.isValid("004471234567890")).isFalse(); // too long

        // 44
        assertThat(UKMobilePhoneFormat.isValid("447123456789")).isTrue();
        assertThat(UKMobilePhoneFormat.isValid("44712345678")).isFalse(); // too short
        assertThat(UKMobilePhoneFormat.isValid("4471234567890")).isFalse(); // too long

    }
}