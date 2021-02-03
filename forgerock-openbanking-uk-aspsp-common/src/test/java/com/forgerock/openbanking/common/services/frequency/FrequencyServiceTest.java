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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

/**
 * Unit test for {@link FrequencyService}.
 */
@RunWith(Parameterized.class)
public class FrequencyServiceTest {

    private final String frequencyInterval;
    private final String wrongFrequencyInterval;
    private final DateTime firstDateTime;
    private final FrequencyService frequencyService;
    private static final String WRONG = "WRONG";

    public FrequencyServiceTest(String frequencyInterval, String wrongFrequencyInterval, DateTime firstDateTime) {
        this.frequencyInterval = frequencyInterval;
        this.wrongFrequencyInterval = wrongFrequencyInterval;
        this.firstDateTime = firstDateTime;
        this.frequencyService = new FrequencyService();
    }

    @Parameterized.Parameters(
            name = "{index}: frequencyInterval ({0}), firstDateTime ({2}), wrongFrequencyInterval ({1})"
    )
    public static Collection<Object[]> frequencies() {

        List<Object[]> args = new ArrayList<>();
        args.add(new Object[]{FrequencyType.EVERYDAY.getFrequencyStr(),
                FrequencyType.EVERYDAY.getFrequencyStr() + WRONG
                ,DateTime.now()});

        args.add(new Object[]{FrequencyType.EVERYWORKINGDAY.getFrequencyStr(),
                FrequencyType.EVERYWORKINGDAY.getFrequencyStr() + WRONG
                ,DateTime.now()});

        args.add(new Object[]{FrequencyType.INTERVALWEEKDAY.getFrequencyStr() + ":01:07",
                FrequencyType.INTERVALWEEKDAY.getFrequencyStr() + ":01:08",
                DateTime.now()});

        args.add(new Object[]{FrequencyType.WEEKINMONTHDAY.getFrequencyStr() + ":02:03",
                FrequencyType.WEEKINMONTHDAY.getFrequencyStr() + ":00:03",
                DateTime.now()});

        args.add(new Object[]{FrequencyType.INTERVALMONTHDAY.getFrequencyStr() + ":01:30",
                FrequencyType.INTERVALMONTHDAY.getFrequencyStr() + ":1:-8",
                DateTime.now()});

        args.add(new Object[]{FrequencyType.QUARTERDAY.getFrequencyStr() + ":ENGLISH",
                FrequencyType.QUARTERDAY.getFrequencyStr() + ":SENT",
                DateTime.now()});

        args.add(new Object[]{FrequencyType.INTERVALDAY.getFrequencyStr() + ":02",
                FrequencyType.INTERVALDAY.getFrequencyStr() + ":01",
                DateTime.now()});

        return args;
    }

    @Test
    public void shouldMatchPattern_frequencyInterval() {
        // Given
        // this.firsDateTime
        // this.frequencyInterval

        // When
        DateTime dateTime = frequencyService.getNextDateTime(firstDateTime, frequencyInterval);

        // Then
        assertThat(dateTime).isNotNull();
    }

    @Test
    public void wrongFrequencyInterval() {
        // Given
        // this.firsDateTime
        // this.wrongFrequencyInterval

        // When
        IllegalArgumentException e = catchThrowableOfType(() ->
                frequencyService.getNextDateTime(firstDateTime, wrongFrequencyInterval), IllegalArgumentException.class);

        // Then
        String[] parts = wrongFrequencyInterval.split(":", 2);
        if (wrongFrequencyInterval.endsWith(WRONG)) {
            assertThat(e.getMessage()).isEqualTo("Frequency type value not found: " + wrongFrequencyInterval);
        } else {
            assertThat(e.getMessage()).isEqualTo("Frequency '" + wrongFrequencyInterval + "' doesn't match regex '" +
                    FrequencyType.fromFrequencyString(parts[0]).getPattern().pattern() + "'");
        }
    }
}
