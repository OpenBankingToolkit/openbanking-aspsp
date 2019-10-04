/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.frequency;

import java.util.regex.Pattern;

public enum FrequencyType {
    EVERYDAY("EvryDay"),
    EVERYWORKINGDAY("EvryWorkgDay"),
    INTERVALWEEKDAY("IntrvlWkDay", "0?([1-9]):0?([1-7])$"),
    WEEKINMONTHDAY("WkInMnthDay", "0?([1-5]):0?([1-7])$"),
    INTERVALMONTHDAY("IntrvlMnthDay", "(0?[1-6]|12|24):(-0?[1-5]|[1][0-9]|2[0-8]|0?[1-9])$"),
    QUARTERDAY("QtrDay", "(ENGLISH|SCOTTISH|RECEIVED)$"),
    INTERVALDAY("IntrvlDay", "(0?[2-9]|[1-2][0-9]|3[0-1])$");

    private String frequencyStr;
    private Pattern pattern;

    FrequencyType(String frequencyStr) {
        this.frequencyStr = frequencyStr;
        this.pattern = Pattern.compile(frequencyStr);
    }

    FrequencyType(String frequencyStr, String pattern) {
        this.frequencyStr = frequencyStr;
        this.pattern = Pattern.compile(frequencyStr + ":" + pattern);
    }

    public String getFrequencyStr() {
        return frequencyStr;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public static FrequencyType fromFrequencyString(String freqStr) {
        for (FrequencyType frequencyType : FrequencyType.values()) {
            if (frequencyType.getFrequencyStr().equals(freqStr)) {
                return frequencyType;
            }
        }
        throw new IllegalArgumentException("Frequency type value not found: " + freqStr);
    }
}