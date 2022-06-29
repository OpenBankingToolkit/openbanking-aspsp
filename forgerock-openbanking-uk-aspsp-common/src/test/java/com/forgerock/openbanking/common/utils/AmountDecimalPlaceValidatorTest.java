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
package com.forgerock.openbanking.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class AmountDecimalPlaceValidatorTest {

    @Test
    public void test2DecimalPlaceStrings() {
        final String[] validValues = {
                "0",
                "1",
                String.valueOf(Integer.MAX_VALUE),
                "-1",
                String.valueOf(Integer.MIN_VALUE),
                "0.1",
                "1.0",
                "12.3",
                "100.7",
                "1.19",
                "22.22",
                "1000000.01"
        };
        for (String value : validValues) {
            assertTrue("value= " + value, AmountDecimalPlaceValidator.validateMax2DecimalPlaces(value));
        }

        final String[] invalidValues = {
                null,
                "",
                "0.001",
                "-0.111",
                "-72834.98735",
                "12.333",
                "99.999999",
                "123456.7890121212112121212"
        };
        for (String value : invalidValues) {
            assertFalse("value= " + value, AmountDecimalPlaceValidator.validateMax2DecimalPlaces(value));
        }
    }
}
