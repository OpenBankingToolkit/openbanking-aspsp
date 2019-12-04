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
package com.forgerock.openbanking.aspsp.rs.api.payment.verifier;

import org.springframework.util.StringUtils;

import java.util.Arrays;

public enum UKMobilePhoneFormat {
    PLUS_44("+44", 13),
    ZERO_44("0044", 14),
    ONLY_44("44", 12),
    ONLY_ZERO("0", 11);

    private String countryCode;
    private int length;

    UKMobilePhoneFormat(String countryCode, int length) {
        this.countryCode = countryCode;
        this.length = length;
    }

    private boolean valid(String ukMobileNumber) {
        if (StringUtils.isEmpty(ukMobileNumber)) {
            return false;
        }
        return (!StringUtils.isEmpty(ukMobileNumber)
              && ukMobileNumber.startsWith(countryCode)
              && ukMobileNumber.trim().length() == length)
              && isNumeric(ukMobileNumber.trim())
                ;
     }

     public static boolean isValid(String ukMobilePhoneNumber) {
        return Arrays.stream(UKMobilePhoneFormat.values())
                .anyMatch(v -> v.valid(ukMobilePhoneNumber));
    }

    private static boolean isNumeric(String phoneNum) {
        if (phoneNum.startsWith("+")) {
            phoneNum = phoneNum.substring(1);
        }
        return phoneNum.matches("-?\\d+(\\.\\d+)?");
    }

}
