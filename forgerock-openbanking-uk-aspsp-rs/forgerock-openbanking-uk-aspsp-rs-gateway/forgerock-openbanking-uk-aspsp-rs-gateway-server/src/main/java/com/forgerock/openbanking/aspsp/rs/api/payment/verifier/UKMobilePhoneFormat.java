/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
