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
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link com.forgerock.openbanking.common.services.openbanking.frequency.FrequencyService}.
 */
public class FrequencyServiceTest {

    FrequencyService frequencyService;
    @Before
    public void setUp() throws Exception {
        frequencyService = new FrequencyService();
    }

    // EVERYDAY("EvryDay")
    @Test
    public void shouldMatchPattern_everyDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.EVERYDAY.getFrequencyStr();

        // When
        DateTime dateTime = frequencyService.getNextDateTime(firstDateTime, frequency);

        // Then
        assertThat(dateTime).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseException_everyDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.EVERYDAY.getFrequencyStr() + "x";

        // When
        frequencyService.getNextDateTime(firstDateTime, frequency);
    }

    // EVERYWORKINGDAY("EvryWorkgDay"),
    @Test
    public void shouldMatchPattern_everyWorkingDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.EVERYWORKINGDAY.getFrequencyStr();

        // When
        DateTime dateTime = frequencyService.getNextDateTime(firstDateTime, frequency);

        // Then
        assertThat(dateTime).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseException_everyWorkingDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.EVERYWORKINGDAY.getFrequencyStr() + "x";

        // When
        frequencyService.getNextDateTime(firstDateTime, frequency);
    }

    // INTERVALWEEKDAY("IntrvlWkDay", "0?([1-9]):0?([1-7])$")
    @Test
    public void shouldMatchPattern_weekday() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.INTERVALWEEKDAY.getFrequencyStr() + ":01:07";

        // When
        DateTime dateTime = frequencyService.getNextDateTime(firstDateTime, frequency);

        // Then
        assertThat(dateTime).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseException_weekday() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.INTERVALWEEKDAY.getFrequencyStr() + ":00:07";

        // When
        frequencyService.getNextDateTime(firstDateTime, frequency);
    }

    // WEEKINMONTHDAY("WkInMnthDay", "0?([1-5]):0?([1-7])$")
    @Test
    public void shouldMatchPattern_weekInMonthDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.WEEKINMONTHDAY.getFrequencyStr() + ":02:03";

        // When
        DateTime dateTime = frequencyService.getNextDateTime(firstDateTime, frequency);

        // Then
        assertThat(dateTime).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseException_weekInMonthDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.WEEKINMONTHDAY.getFrequencyStr() + ":00:03";

        // When
        frequencyService.getNextDateTime(firstDateTime, frequency);
    }
    // INTERVALMONTHDAY("IntrvlMnthDay", "(0?[1-6]|12|24):(-0?[1-5]|0?[1-9]|[12][0-9]|3[01])$")
    @Test
    public void shouldMatchPattern_intervalMonthDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.INTERVALMONTHDAY.getFrequencyStr() + ":01:30";

        // When
        DateTime dateTime = frequencyService.getNextDateTime(firstDateTime, frequency);

        // Then
        assertThat(dateTime).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseException_intervalMonthDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.INTERVALMONTHDAY.getFrequencyStr() + ":01:-8";

        // When
        frequencyService.getNextDateTime(firstDateTime, frequency);
    }

    // QUARTERDAY("QtrDay", "(ENGLISH|SCOTTISH|RECEIVED)$")
    @Test
    public void shouldMatchPattern_quarterDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.QUARTERDAY.getFrequencyStr() + ":ENGLISH";

        // When
        DateTime dateTime = frequencyService.getNextDateTime(firstDateTime, frequency);

        // Then
        assertThat(dateTime).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseException_quarterDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.QUARTERDAY.getFrequencyStr() + ":SENT";

        // When
        frequencyService.getNextDateTime(firstDateTime, frequency);
    }

    // INTERVALDAY("IntrvlDay", "(0?[2-9]|[1-2][0-9]|3[0-1])$");
    @Test
    public void shouldMatchPattern_intervaldDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.INTERVALDAY.getFrequencyStr() + ":02";

        // When
        DateTime dateTime = frequencyService.getNextDateTime(firstDateTime, frequency);

        // Then
        assertThat(dateTime).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRaiseException_intervalDay() {
        // Given
        DateTime firstDateTime = DateTime.now();
        String frequency = FrequencyType.INTERVALDAY.getFrequencyStr() + ":1";

        // When
        frequencyService.getNextDateTime(firstDateTime, frequency);
    }

}
