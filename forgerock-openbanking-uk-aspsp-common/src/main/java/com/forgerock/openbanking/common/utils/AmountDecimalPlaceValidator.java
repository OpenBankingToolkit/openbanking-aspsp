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

public class AmountDecimalPlaceValidator {

    /**
     * Validates whether an amount represented as a String contains 0, 1 or 2 decimal places.
     *
     * Assumes that the input has already been validated to be a String amount as defined in the OB spec (OBActiveCurrencyAndAmount_SimpleType)
     *
     * @param amount String representing an amount of money as defined in the OB spec
     * @return true if the amount contains 0,1 or 2 decimal places, otherwise false
     */
    public static boolean validateMax2DecimalPlaces(String amount) {
        if (amount != null && !amount.isBlank()) {
            return getNumberOfDecimalPlaces(amount) <= 2;
        }
        return false;
    }

    private static int getNumberOfDecimalPlaces(String amount) {
        int index = amount.indexOf('.');
        return index < 0 ? 0 : amount.length() - index - 1;
    }
}
