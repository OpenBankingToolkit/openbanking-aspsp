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
package com.forgerock.openbanking.common.services.frequency;

import com.forgerock.openbanking.common.model.openbanking.frequency.FrequencyType;
import com.forgerock.openbanking.common.services.openbanking.frequency.FrequencyService;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

/**
 * Unit test for {@link com.forgerock.openbanking.common.services.openbanking.frequency.FrequencyService}.
 */
public class FrequencyServiceTest {
    // (Saturday 6th Feb 2021)
    private static final DateTime PREVIOUS_DATE_TIME = new DateTime(2021, 2, 6, 11, 0);

    private final FrequencyService frequencyService = new FrequencyService();

    // EVERYDAY("EvryDay")
    @Test
    public void shouldMatchPattern_everyDay() {
        // Given
        String frequency = FrequencyType.EVERYDAY.getFrequencyStr();

        // When
        DateTime dateTime = frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency);

        // Then
        assertThat(dateTime).isNotNull();
        assertThat(dateTime.getYear()).isEqualTo(PREVIOUS_DATE_TIME.getYear());
        assertThat(dateTime.getMonthOfYear()).isEqualTo(PREVIOUS_DATE_TIME.getMonthOfYear());
        assertThat(dateTime.getDayOfMonth()).isEqualTo(PREVIOUS_DATE_TIME.getDayOfMonth() + 1);
    }

    @Test
    public void shouldRaiseException_everyDay() {
        // Given
        String frequency = FrequencyType.EVERYDAY.getFrequencyStr() + "x";

        // When
        IllegalArgumentException e = catchThrowableOfType(() ->
                frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency), IllegalArgumentException.class);

        // Then
        assertThat(e.getMessage()).isEqualTo("Frequency type value not found: EvryDayx");
    }

    // EVERYWORKINGDAY("EvryWorkgDay"),
    @Test
    public void shouldMatchPattern_everyWorkingDay() {
        // Given
        String frequency = FrequencyType.EVERYWORKINGDAY.getFrequencyStr();

        // When
        DateTime dateTime = frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency);

        // Then
        assertThat(dateTime).isNotNull();
        assertThat(dateTime.getYear()).isEqualTo(PREVIOUS_DATE_TIME.getYear());
        assertThat(dateTime.getMonthOfYear()).isEqualTo(PREVIOUS_DATE_TIME.getMonthOfYear());
        assertThat(dateTime.getDayOfMonth()).isEqualTo(PREVIOUS_DATE_TIME.getDayOfMonth() + 2);
    }

    @Test
    public void shouldRaiseException_everyWorkingDay() {
        // Given
        String frequency = FrequencyType.EVERYWORKINGDAY.getFrequencyStr() + "x";

        // When
        IllegalArgumentException e = catchThrowableOfType(() ->
                frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency), IllegalArgumentException.class);

        // Then
        assertThat(e.getMessage()).isEqualTo("Frequency type value not found: EvryWorkgDayx");
    }

    // INTERVALWEEKDAY("IntrvlWkDay", "0?([1-9]):0?([1-7])$")
    @Test
    public void shouldMatchPattern_weekday() {
        // Given
        String frequency = FrequencyType.INTERVALWEEKDAY.getFrequencyStr() + ":01:07";

        // When
        DateTime dateTime = frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency);

        // Then
        assertThat(dateTime).isNotNull();
        assertThat(dateTime.getYear()).isEqualTo(PREVIOUS_DATE_TIME.getYear());
        assertThat(dateTime.getMonthOfYear()).isEqualTo(PREVIOUS_DATE_TIME.getMonthOfYear());
        assertThat(dateTime.getDayOfMonth()).isEqualTo(PREVIOUS_DATE_TIME.getDayOfMonth() + 8);
    }

    @Test
    public void shouldRaiseException_weekday() {
        // Given
        String frequency = FrequencyType.INTERVALWEEKDAY.getFrequencyStr() + ":00:07";

        // When
        IllegalArgumentException e = catchThrowableOfType(() ->
                frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency), IllegalArgumentException.class);

        // Then
        assertThat(e.getMessage()).isEqualTo("Frequency 'IntrvlWkDay:00:07' doesn't match regex 'IntrvlWkDay:0?([1-9]):0?([1-7])$'");
    }

    // WEEKINMONTHDAY("WkInMnthDay", "0?([1-5]):0?([1-7])$")
    @Test
    public void shouldMatchPattern_weekInMonthDay() {
        // Given
        String frequency = FrequencyType.WEEKINMONTHDAY.getFrequencyStr() + ":02:03";

        // When
        DateTime dateTime = frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency);

        // Then
        assertThat(dateTime).isNotNull();
        assertThat(dateTime.getYear()).isEqualTo(PREVIOUS_DATE_TIME.getYear());
        assertThat(dateTime.getMonthOfYear()).isEqualTo(PREVIOUS_DATE_TIME.getMonthOfYear() + 1);
        assertThat(dateTime.getDayOfMonth()).isEqualTo(2 * 7 + 3);
    }

    @Test
    public void shouldRaiseException_weekInMonthDay() {
        // Given
        String frequency = FrequencyType.WEEKINMONTHDAY.getFrequencyStr() + ":00:03";

        // When
        IllegalArgumentException e = catchThrowableOfType(() ->
                frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency), IllegalArgumentException.class);

        // Then
        assertThat(e.getMessage()).isEqualTo("Frequency 'WkInMnthDay:00:03' doesn't match regex 'WkInMnthDay:0?([1-5]):0?([1-7])$'");
    }

    // INTERVALMONTHDAY("IntrvlMnthDay", "(0?[1-6]|12|24):(-0?[1-5]|0?[1-9]|[12][0-9]|3[01])$")
    @Test
    public void shouldMatchPattern_intervalMonthDay() {
        // Given
        String frequency = FrequencyType.INTERVALMONTHDAY.getFrequencyStr() + ":01:30";

        // When
        DateTime dateTime = frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency);

        // Then
        assertThat(dateTime).isNotNull();
        assertThat(dateTime.getYear()).isEqualTo(PREVIOUS_DATE_TIME.getYear());
        assertThat(dateTime.getMonthOfYear()).isEqualTo(PREVIOUS_DATE_TIME.getMonthOfYear() + 1);
        assertThat(dateTime.getDayOfMonth()).isEqualTo(30);
    }

    @Test
    public void shouldRaiseException_intervalMonthDay() {
        // Given
        String frequency = FrequencyType.INTERVALMONTHDAY.getFrequencyStr() + ":01:-8";

        // When
        IllegalArgumentException e = catchThrowableOfType(() ->
                frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency), IllegalArgumentException.class);

        // Then
        assertThat(e.getMessage()).isEqualTo("Frequency 'IntrvlMnthDay:01:-8' doesn't match regex 'IntrvlMnthDay:(0?[1-6]|12|24):(-0?[1-5]|0?[1-9]|[12][0-9]|3[01])$'");
    }

    // QUARTERDAY("QtrDay", "(ENGLISH|SCOTTISH|RECEIVED)$")
    @Test
    public void shouldMatchPattern_quarterDay() {
        // Given
        String frequency = FrequencyType.QUARTERDAY.getFrequencyStr() + ":ENGLISH";

        // When
        DateTime dateTime = frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency);

        // Then
        assertThat(dateTime).isNotNull();
        assertThat(dateTime.getYear()).isEqualTo(PREVIOUS_DATE_TIME.getYear());
        assertThat(dateTime.getMonthOfYear()).isEqualTo(PREVIOUS_DATE_TIME.getMonthOfYear() + 1);
        assertThat(dateTime.getDayOfMonth()).isEqualTo(25);
    }

    @Test
    public void shouldRaiseException_quarterDay() {
        // Given
        String frequency = FrequencyType.QUARTERDAY.getFrequencyStr() + ":SENT";

        // When
        IllegalArgumentException e = catchThrowableOfType(() ->
                frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency), IllegalArgumentException.class);

        // Then
        assertThat(e.getMessage()).isEqualTo("Frequency 'QtrDay:SENT' doesn't match regex 'QtrDay:(ENGLISH|SCOTTISH|RECEIVED)$'");
    }

    // INTERVALDAY("IntrvlDay", "(0?[2-9]|[1-2][0-9]|3[0-1])$");
    @Test
    public void shouldMatchPattern_intervalDay() {
        // Given
        String frequency = FrequencyType.INTERVALDAY.getFrequencyStr() + ":02";

        // When
        DateTime dateTime = frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency);

        // Then
        assertThat(dateTime).isNotNull();
        assertThat(dateTime.getYear()).isEqualTo(PREVIOUS_DATE_TIME.getYear());
        assertThat(dateTime.getMonthOfYear()).isEqualTo(PREVIOUS_DATE_TIME.getMonthOfYear());
        assertThat(dateTime.getDayOfMonth()).isEqualTo(PREVIOUS_DATE_TIME.getDayOfMonth() + 2);
    }

    @Test
    public void shouldRaiseException_intervalDay() {
        // Given
        String frequency = FrequencyType.INTERVALDAY.getFrequencyStr() + ":1";

        // When
        IllegalArgumentException e = catchThrowableOfType(() ->
                frequencyService.getNextDateTime(PREVIOUS_DATE_TIME, frequency), IllegalArgumentException.class);

        // Then
        assertThat(e.getMessage()).isEqualTo("Frequency 'IntrvlDay:1' doesn't match regex 'IntrvlDay:(0?[2-9]|[1-2][0-9]|3[0-1])$'");
    }

}
