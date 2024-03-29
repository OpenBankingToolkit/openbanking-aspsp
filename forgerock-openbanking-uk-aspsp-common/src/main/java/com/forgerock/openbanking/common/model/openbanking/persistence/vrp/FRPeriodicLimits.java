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
package com.forgerock.openbanking.common.model.openbanking.persistence.vrp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an equivalent object in the OB data model. It is stored within mongo (instead of the OB object), in order to make it easier to introduce new
 * versions of the Read/Write API.
 *
 * <p>
 * Note that this object is used across multiple versions of the Read/Write API, meaning that some values won't be populated. For this reason it is
 * a mutable {@link lombok.Data} rather than an immutable {@link lombok.Value} one.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRPeriodicLimits {
    private String amount;
    private String currency;
    private FRPeriodicLimits.PeriodTypeEnum periodType;
    private FRPeriodicLimits.PeriodAlignmentEnum periodAlignment;
    private FRSupplementaryData supplementaryData;
    /**
     * ^ Period type for this period limit
     */
    public enum PeriodTypeEnum {
        DAY("Day"),

        WEEK("Week"),

        FORTNIGHT("Fortnight"),

        MONTH("Month"),

        HALF_YEAR("Half-year"),

        YEAR("Year");

        private String value;

        PeriodTypeEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static FRPeriodicLimits.PeriodTypeEnum fromValue(String value) {
            for (FRPeriodicLimits.PeriodTypeEnum b : FRPeriodicLimits.PeriodTypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    /**
     * ^ Specifies whether the period starts on the date of consent creation or lines up with a calendar
     */
    public enum PeriodAlignmentEnum {
        CONSENT("Consent"),

        CALENDAR("Calendar");

        private String value;

        PeriodAlignmentEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static FRPeriodicLimits.PeriodAlignmentEnum fromValue(String value) {
            for (FRPeriodicLimits.PeriodAlignmentEnum b : FRPeriodicLimits.PeriodAlignmentEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }
}
